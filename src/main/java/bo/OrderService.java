package bo;

import javax.servlet.http.HttpServletRequest;

public interface OrderService {
    public void getAllOrderList(HttpServletRequest req);

    public void getUserOrderList(HttpServletRequest req);

    public void getAllOrderCount(HttpServletRequest req);

    public void getUserOrderCount(HttpServletRequest req);

    public void getOrderDetailByNumber(HttpServletRequest req, String number);

    public void addOrder(HttpServletRequest req, String purchaser, String phone, String address);

    public void switchStatus(Integer status, String number);
}