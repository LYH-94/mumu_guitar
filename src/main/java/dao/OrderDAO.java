package dao;

import pojo.Order;
import pojo.OrderProduct;
import pojo.Product;

import java.sql.Connection;
import java.util.List;

public interface OrderDAO {
    // 獲取所有的用戶訂單。
    public List<Order> getAllOrder(Connection conn, Class<Order> clazz);

    // 獲取特定用戶訂單中的所有商品。
    public List<OrderProduct> getAllOrderProduct(Connection conn, Class<OrderProduct> clazz, int belongOrder);
}
