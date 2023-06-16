package listeners;

import ioc.BeanFactory;
import ioc.impl.ClassPathXmlApplicationContext;
import listeners.exception.ContextLoaderListenerException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

//監聽 Web 程序啟動，於啟動時去創建 IOC 容器，然後保存到 application 保存作用域中。
@WebListener
public class ContextLoaderListener implements ServletContextListener {

    private BeanFactory beanFactory = null;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) throws ContextLoaderListenerException {
        // 1.獲取 ServletContext 物件。
        ServletContext application = servletContextEvent.getServletContext();

        // 2.獲取 ServletContext 的初始化參數。
        String path = application.getInitParameter("contextConfigLocation");

        // 3.創建 IOC 容器。
        beanFactory = new ClassPathXmlApplicationContext(path);

        if (beanFactory != null) {
            // 4.將 IOC 容器存入 application 保存作用域中。
            application.setAttribute("beanFactory", beanFactory);
        } else {
            throw new ContextLoaderListenerException("ContextLoaderListener 的 beanFactory 為 null。");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
