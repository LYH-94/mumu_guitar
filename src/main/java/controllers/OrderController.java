package controllers;

import pojo.Order;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrderController {
    // 獲取所有用戶的訂單。
    public List<Order> getAllOrder();

    // 獲取特定用戶的所有訂單。
    public String getUserOrderList(HttpServletRequest req);

    // 透過訂單編號獲取訂單詳情。
    public String getOrderDetailByNumber(HttpServletRequest req, String number);

    // 新增訂單。
    public String addOrder(HttpServletRequest req, String purchaser, String phone, String address);
}
