package servlets;

import ioc.BeanFactory;
import servlets.exception.DispatcherServletException;
import util.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// 使用通配符 * 來配置請求，例如：
// *.do：表示攔截所有以 .do 結尾的請求。
@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet {

    private BeanFactory beanFactory = null;

    public DispatcherServlet() {
    }

    public void init() throws DispatcherServletException {
        try {
            super.init();
            ServletContext servletContext = getServletContext();
            Object beanFactoryObj = servletContext.getAttribute("beanFactory");

            if (beanFactoryObj != null) {
                beanFactory = (BeanFactory) beanFactoryObj;
            } else {
                throw new DispatcherServletException("DispatcherServlet 的 beanFactory 為 null。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DispatcherServletException("DispatcherServlet 的 init() 有問題。");
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws DispatcherServletException {

        // 將經過 *.do 攔截下來的請求，與要調用的控制器類進行映射，步驟如下：
        // 假設 URL 是 http://localhost:8080/FruitInventorySystem_exercise01_opt02/fruit.do
        // 第 1 步：可以使用 req.getServletPath() 方法來獲取請求字串。※要注意，是在 HttpServletRequest 類型下才有該方法。
        //         如果接收到的請求為 fruit.do，那獲取到的會是 /fruit.do 格式的字串。
        // 第 2 步：透過字串擷取來去除 / 和 .do 的部分。/fruit.do -> fruit
        // 第 3 步：由於初始化時已經解析了配置文件 (applicationContext.xml)，
        //         因此可以利用 fruit 字串透過 branFactory.getBean() 方法來獲取相應的控制器 bean 物件。fruit -> FruitController

        // 第 1 步
        String servletPath = req.getServletPath();

        // 第 2 步
        servletPath = servletPath.substring(1); //從第 1 位開始擷取 (開頭是 0)，因此 / 被去除了。
        int lastDotDoIndex = servletPath.lastIndexOf(".do"); // 找出 .do 的索引位置。
        servletPath = servletPath.substring(0, lastDotDoIndex); //將 .do 去除掉。就會剩下 fruit 字串。

        // 第 3 步
        Object controllerBeanObj = beanFactory.getBean(servletPath);

        // 接著就可以根據不同的操作來調用不同的方法。
        String operate = req.getParameter("operate");

        if (StringUtils.isEmpty(operate)) {
            operate = "checkIdentity";
        }

        // 獲取當前類中所有的方法。
        Method[] methods = controllerBeanObj.getClass().getDeclaredMethods();

        // 用 for 循環找出與 operate 對應的方法。
        for (Method m : methods) {

            // 獲取方法名稱並與 operate 進行比對。
            if (operate.equals(m.getName())) {

                // 找到對應的方法後，獲取所需參數並調用該方法。
                try {
                    // 1.統一獲取請求參數。
                    // 由於用反射技術時，直接獲取參數的話，參數名稱會被自動更名為 arg0,arg1,arg2,....
                    // 但這樣的名稱無法使用 req.getParameter() 方法來獲取對應的參數值，
                    // 因此需要獲取的是這些被自動更名的參數的實際名稱。該需求於 JDK8 的新特性解決了。
                    // 設置 File -> Settings -> Build, Execution, Deployment -> Java Compiler
                    // -> 於 Additional command line parameters: 下加指令 -> "-parameters" (不含雙引號) 即可。
                    // 可以使編譯器編譯時，將參數名稱一併帶入 class 文件中。
                    // 優點是可以找到參數的實際名稱，缺點是 class 文件體積會比較大。
                    // ※注意，更改後，所有 class 文件需要重新編譯。
                    Parameter[] parameters = m.getParameters();

                    // 設置 parameterValues 來裝載參數的值。
                    // ※這裡不考慮複選框的情況。
                    Object[] parameterValues = new Object[parameters.length];
                    for (int i = 0; i < parameters.length; i++) {
                        Parameter parameter = parameters[i];
                        String parameterName = parameter.getName();

                        // 如果參數名是 req,resp,session，那就不是通過請求中獲取參數的方式了。
                        if ("req".equals(parameterName)) {
                            parameterValues[i] = req;
                        } else if ("resp".equals(parameterName)) { // 本案例沒有傳 resp，但還是把它寫完整。
                            parameterValues[i] = resp;
                        } else if ("session".equals(parameterName)) { // 本案例沒有傳 session，但還是把它寫完整。
                            parameterValues[i] = req.getSession();
                        } else {
                            // 從請求中獲取參數值。
                            String parameterValue = req.getParameter(parameterName);

                            // 由於參數所需要的類型不同，而 req.getParameter() 方法獲取到的參數都是 String，
                            // 因此需要判斷類型並進行類型轉換。
                            String typeName = parameter.getType().getName();

                            Object parameterObj = parameterValue;
                            if (parameterObj != null) {
                                if ("java.lang.Integer".equals(typeName)) { // 本案例考慮整數、字串與Date類型。
                                    if (StringUtils.isEmpty(parameterValue)) {
                                        parameterValue = "0";
                                    }
                                    parameterObj = Integer.parseInt(parameterValue);
                                } else if ("java.time.LocalDate".equals(typeName)) {
                                    //System.out.println("123123");
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                    parameterObj = LocalDate.parse(parameterValue, formatter);
                                }
                            }

                            parameterValues[i] = parameterObj;
                        }
                    }

                    // 2.調用控制器 bean 物件中的方法，並接收回傳值。
                    m.setAccessible(true); // 將該成員函數的訪問權限設置為強制訪問。
                    Object methodReturnObj = m.invoke(controllerBeanObj, parameterValues);
                    String methodReturnStr = null;
                    if (methodReturnObj != null) {
                        methodReturnStr = (String) methodReturnObj;
                    }

                    // 3.統一執行視圖處理。接收到回傳值後，進行視圖處理。
                    if (methodReturnStr.startsWith("redirect:")) { // 如果是以 redirect: 開頭的則為 true。
                        // 進行字串擷取。留下 redirect: 後面的字串。
                        String redirectStr = methodReturnStr.substring("redirect:".length());

                        // 進行資源的客戶端重定向。
                        resp.sendRedirect(redirectStr);
                    } else if (methodReturnStr != "error") {
                        super.processTemplate(methodReturnStr, req, resp);
                    } else {
                        System.out.println("視圖處理錯誤! 接收到的值為：" + methodReturnStr);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new DispatcherServletException("DispatcherServlet 的 service() 有問題。");
                }
            }
        }
    }
}
