package bo;

import javax.servlet.http.HttpServletRequest;

public interface OrderService {
    // 獲取所有用戶的訂單。
    public void getAllOrderList(HttpServletRequest req);

    // 獲取特定用戶所需的訂單。
    public void getUserOrderList(HttpServletRequest req);

    // 獲取所有用戶的訂單總數。
    public void getAllOrderCount(HttpServletRequest req);

    // 獲取特定用戶所需的訂單總數。
    public void getUserOrderCount(HttpServletRequest req);

    // 透過訂單編號獲取該訂單的詳細資訊。
    public void getOrderDetailByNumber(HttpServletRequest req, String number);

    // 新增訂單至數據庫中。
    public void addOrder(HttpServletRequest req, String purchaser, String phone, String address);

    // 切換訂單的狀態。
    public void switchStatus(Integer status, String number);
}
