package dao;

import pojo.Order;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderDAO {
    public List<Order> getAllOrderList(Connection conn, Class<Order> clazz, String searchOrder, int orderPageNumber);

    public List<Order> getUserOrderList(Connection conn, Class<Order> clazz, int userId, String searchOrder, int orderPageNumber);

    public int getAllOrderCount(Connection conn, String searchOrder);

    public int getUserOrderCount(Connection conn, int userId, String searchOrder);

    public Order getOrderByNumber(Connection conn, Class<Order> clazz, String number);

    public void addOrder(Connection conn, String number, LocalDateTime date, Integer totalAmount, Integer owner, String purchaser, String phone, String address);

    public void addOrderProduct(Connection conn, Integer product, Integer quantity, Integer belongOrder);

    public void switchStatus(Connection conn, Integer status, String number);
}