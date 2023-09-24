package controllers;

import javax.servlet.http.HttpServletRequest;

public interface OrderController {
    // 獲取所有用戶的訂單。
    public String getAllOrderList(HttpServletRequest req);

    // 獲取特定用戶的所有訂單。
    public String getUserOrderList(HttpServletRequest req);

    // 透過訂單編號獲取訂單詳情。
    public String getOrderDetailByNumber(HttpServletRequest req, String number);

    // 新增訂單。
    public String addOrder(HttpServletRequest req, String purchaser, String phone, String address);

    // 切換訂單狀態。
    public String switchStatus(HttpServletRequest req, Integer status, String number);
}
