package controllers;

import javax.servlet.http.HttpServletRequest;

public interface OrderController {
    public String getAllOrderList(HttpServletRequest req);

    public String getUserOrderList(HttpServletRequest req);

    public String getOrderDetailByNumber(HttpServletRequest req, String number);

    public String addOrder(HttpServletRequest req, String purchaser, String phone, String address);

    public String switchStatus(HttpServletRequest req, Integer status, String number);
}