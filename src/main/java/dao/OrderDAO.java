package dao;

import pojo.Order;
import pojo.OrderProduct;
import pojo.Product;

import java.sql.Connection;
import java.util.List;

public interface OrderDAO {
    // 獲取所有的用戶訂單。
    public List<Order> getAllOrder(Connection conn, Class<Order> clazz);

    // 獲取特定用戶所需的訂單。
    public List<Order> getUserOrderList(Connection conn, Class<Order> clazz, int userId, String searchOrder, int orderPageNumber);

    // 獲取特定用戶所需的訂單數量。
    public int getUserOrderCount(Connection conn, int userId, String searchOrder);

    // 透過訂單編號獲取訂單。
    public Order getOrderByNumber(Connection conn, Class<Order> clazz, String number);

    // 獲取特定用戶訂單中的所有商品。
    public List<OrderProduct> getAllOrderProduct(Connection conn, Class<OrderProduct> clazz, int belongOrder);
}
