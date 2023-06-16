package ioc.impl;

import ioc.BeanFactory;
import ioc.exception.ClassPathXmlApplicationContextException;
import util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ClassPathXmlApplicationContext implements BeanFactory {

    // 設置 beanMap，是用於儲存所有 bean 物件的容器。
    private Map<String, Object> beanMap = new HashMap<>();


    public ClassPathXmlApplicationContext() {
        this("applicationContext.xml"); //配置文件的預設路徑。
    }

    public ClassPathXmlApplicationContext(String path) throws ClassPathXmlApplicationContextException {
        if (StringUtils.isEmpty(path)) {
            throw new ClassPathXmlApplicationContextException("ClassPathXmlApplicationContext 的 IOC 容器的配置文件沒有指定。");
        }

        try {
            // 由於需要將請求映射到對應的 bean 物件，所以先在建構函數中解析配置文件 (applicationContext.xml)。
            // 加載配置文件。
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);

            // 1.創建 DocumentBuilderFactory 物件。
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

            // 2.創建 DocumentBuilder 物件。
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            // 3.創建 Document 物件。※注意，import org.w3c.dom.Document;
            Document document = documentBuilder.parse(inputStream);

            // 有了 document 之後，就可以利用它來獲取配置文件中的標籤。如同 JS 的 ( Document Object Model; DOM ) 技術。
            // 4.獲取配置文件中所有的 bean 元素節點。
            NodeList beanNodeList = document.getElementsByTagName("bean");

            for (int i = 0; i < beanNodeList.getLength(); i++) {
                // 獲取當前節點。
                Node beanNode = beanNodeList.item(i);

                // 判斷當前節點是否為元素節點。
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {

                    // 若為元素節點，則強轉為 Element 類型。有比較好用的方法，如 getAttribute()。
                    Element beanElement = (Element) beanNode;

                    // 獲取元素節點中的參數值。返回的是字串類型。
                    String beanId = beanElement.getAttribute("id");
                    String beanClassName = beanElement.getAttribute("class");

                    // 由於要映射的是該類的實體物件，而不是類別名稱，因此需利用反射來獲取該類的實體物件。
                    Object beanObj = Class.forName(beanClassName).newInstance();

                    // 最後將 beanId 和 beanObj 存入 beanMap 容器中。
                    beanMap.put(beanId, beanObj);

                    // 注意，到目前為止還只是將所有需要的 bean 物件存入 beanMap 中，雖然可以透過 beanId 從 beanMap 中獲取對應的 bean 物件，
                    // 但 bean 物件與 bean 物件之間的依賴關係還沒有設置。需要組裝 bean 物件之間的依賴關係。
                }
            }

            // 5.組裝 bean 物件之間的依賴關係。
            for (int i = 0; i < beanNodeList.getLength(); i++) {
                // 獲取當前節點。
                Node beanNode = beanNodeList.item(i);

                // 判斷當前節點是否為元素節點。
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    // 若為元素節點，則強轉為 Element 類型。有比較好用的方法，如 getAttribute()。
                    Element beanElement = (Element) beanNode;

                    // 獲取當前元素節點中的 Id。返回的是字串類型。下面用於獲取當前 bean 物件時需要。
                    String beanId = beanElement.getAttribute("id");

                    // 獲取當前節點的所有子節點。
                    NodeList beanChildNodeList = beanElement.getChildNodes();

                    for (int j = 0; j < beanChildNodeList.getLength(); j++) {
                        // 獲取當前子節點。
                        Node beanChildNode = beanChildNodeList.item(j);

                        // 判斷當前子節點是否為元素節點且該節點的名稱為 property。
                        if (beanChildNode.getNodeType() == Node.ELEMENT_NODE && "property".equals(beanChildNode.getNodeName())) {
                            // 若為元素節點，則強轉為 Element 類型。有比較好用的方法，如 getAttribute()。
                            Element propertyElement = (Element) beanChildNode;

                            // 獲取子元素節點中的參數值。返回的是字串類型。
                            String propertyName = propertyElement.getAttribute("name");
                            String propertyRef = propertyElement.getAttribute("ref");

                            // 接下來要開始組裝 bean 物件之間的依賴關係。
                            // 1.透過 beanMap 找到 propertyRef 對應的 bean 物件。
                            Object refObj = beanMap.get(propertyRef);

                            // 2.將 refObj 設置給當前 bean 物件中與 property 屬性值同名的成員變數。※這就稱為「依賴注入」。
                            //   2.1.首先獲取當前 bean 物件。
                            Object beanObj = beanMap.get(beanId);

                            //   2.2.利用反射來獲取當前 bean 物件的類別。
                            Class beanClazz = beanObj.getClass();

                            //   2.3.獲取該類別中與 propertyName 屬性值同名的成員變數。
                            Field propertyField = beanClazz.getDeclaredField(propertyName);

                            //   2.4.將該成員變數的訪問權限設置為強制訪問。
                            propertyField.setAccessible(true);

                            //   2.5.將 refObj 設置給當前 bean 物件中的 propertyField 成員變數。
                            propertyField.set(beanObj, refObj);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClassPathXmlApplicationContextException("ClassPathXmlApplicationContext 的建構函式有問題。");
        }
    }

    @Override
    public Object getBean(String id) {
        return beanMap.get(id);
    }
}
