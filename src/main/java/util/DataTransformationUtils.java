package util;

import util.exception.DataTransformationUtilsException;

import java.lang.reflect.Constructor;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DataTransformationUtils {
    // 將 LocalDateTime 類型轉換成 Timestamp類型。
    public static Timestamp LocalDateTimeToTimestamp(Object columnValues) {
        if ("java.time.LocalDateTime".equals(columnValues.getClass().getName())) {
            return Timestamp.valueOf((LocalDateTime) columnValues);
        } else {
            System.out.println("LocalDateTimeToTimestamp()：要轉換的類型不是 LocalDateTime。");
        }
        return null;
    }

    // 封裝成 User 類型的物件。
    // 注意，pojo 類中要有帶參建構子 (id)。
    public static Object IntegerToUser(Object columnValues) throws DataTransformationUtilsException {
        try {
            Class typeClassName = Class.forName("pojo.User");

            Constructor constructor = typeClassName.getDeclaredConstructor(Integer.class);
            return constructor.newInstance(columnValues);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataTransformationUtilsException("DataTransformationUtils 的 IntegerToUser() 方法有問題。");
        }
    }

    // 封裝成 Order 類型的物件。
    // 注意，pojo 類中要有帶參建構子 (id)。
    public static Object IntegerToOrder(Object columnValues) throws DataTransformationUtilsException {
        try {
            Class typeClassName = Class.forName("pojo.Order");

            Constructor constructor = typeClassName.getDeclaredConstructor(Integer.class);
            return constructor.newInstance(columnValues);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataTransformationUtilsException("DataTransformationUtils 的 IntegerToOrder() 方法有問題。");
        }
    }

    // 封裝成 Product 類型的物件。
    // 注意，pojo 類中要有帶參建構子 (id)。
    public static Object IntegerToProduct(Object columnValues) throws DataTransformationUtilsException {
        try {
            Class typeClassName = Class.forName("pojo.Product");

            Constructor constructor = typeClassName.getDeclaredConstructor(Integer.class);
            return constructor.newInstance(columnValues);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataTransformationUtilsException("DataTransformationUtils 的 IntegerToProduct() 方法有問題。");
        }
    }
}
