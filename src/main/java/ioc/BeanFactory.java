package ioc;

public interface BeanFactory {
    // 透過 id 來獲取某一個 Bean 物件。
    Object getBean(String id);
}
