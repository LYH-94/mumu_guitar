package servlets;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JavaxServletWebApplication;
import servlets.exception.ViewBaseServletException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewBaseServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private JavaxServletWebApplication application;

    @Override
    public void init() throws ViewBaseServletException {
        try {
            //1. 獲取 ServletContext 物件。
            ServletContext servletContext = this.getServletContext();

            //2. 創建一個 JavaxServletWebApplication 物件 (它是 IWebApplication 的子類)。
            this.application = JavaxServletWebApplication.buildApplication(servletContext);

            //3. 創建 Thymeleaf 解析器物件 (該類的建構函數接收 IWebApplication 類型的物件)。
            final WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(this.application);

            //4. 給解析器物鍵設置參數
            //  4.1 HTML 是默認模式，明確設置是為了程式碼更容易理解。
            templateResolver.setTemplateMode(TemplateMode.HTML);

            //  4.2 設置前綴
            //  從 web.xml 中獲取名稱為 view-prefix 的值。
            String viewPrefix = servletContext.getInitParameter("view-prefix");

            //  設置前綴。
            templateResolver.setPrefix(viewPrefix);

            //  4.3 設置後綴
            //  從 web.xml 中獲取名稱為 view-suffix 的值。
            String viewSuffix = servletContext.getInitParameter("view-suffix");

            //  設置前綴。
            templateResolver.setSuffix(viewSuffix);

            //  4.4 設置緩存過期時間 (毫秒)
            templateResolver.setCacheTTLMs(60000L);

            //  4.5 設置是否緩存
            templateResolver.setCacheable(true);

            //  4.6 設置服務器端編碼方式
            templateResolver.setCharacterEncoding("utf-8");

            //5. 創建模板引擎物件
            templateEngine = new TemplateEngine();

            //6. 給模板引擎物件設置模板解析器
            templateEngine.setTemplateResolver(templateResolver);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ViewBaseServletException("ViewBaseServlet 的 init() 有問題。");
        }
    }

    protected void processTemplate(String templateName, HttpServletRequest req, HttpServletResponse resp) throws ViewBaseServletException {
        try {
            //1. 創建 IWebExchange 類型的物件
            final IWebExchange webExchange = this.application.buildExchange(req, resp);

            //2. 設置響應体內容類型和字符集
            resp.setContentType("text/html;charset=UTF-8");

            //3. 創建 WebContext 物件
            WebContext webContext = new WebContext(webExchange, webExchange.getLocale());

            //4. 處理模板數據
            templateEngine.process(templateName, webContext, resp.getWriter());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ViewBaseServletException("ViewBaseServlet 的 processTemplate() 有問題。");
        }
    }
}