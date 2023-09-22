package bo;

import pojo.Order;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrderService {
    // 獲取所有用戶的訂單。
    public List<Order> getAllOrder();

    // 獲取特定用戶所需的訂單。
    public void getUserOrderList(HttpServletRequest req);

    // 獲取特定用戶所需的訂單總數。
    public void getUserOrderCount(HttpServletRequest req);

    // 透過訂單編號獲取該訂單的詳細資訊。
    public void getOrderDetailByNumber(HttpServletRequest req, String number);

    // 新增訂單至數據庫中。
    public void addOrder(HttpServletRequest req, String purchaser, String phone, String address);
}
