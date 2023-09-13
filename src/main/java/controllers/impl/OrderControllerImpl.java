package controllers.impl;

import bo.impl.OrderServiceImpl;
import controllers.OrderController;
import controllers.exception.OrderControllerImplException;
import pojo.Order;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class OrderControllerImpl implements OrderController {

    private OrderServiceImpl orderService = null;

    @Override
    public List<Order> getAllOrder() throws OrderControllerImplException {
        try {
            return orderService.getAllOrder();
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderControllerImplException("OrderControllerImpl 的 getAllOrder() 有問題。");
        }
    }

    @Override
    public String getUserOrderList(HttpServletRequest req) throws OrderControllerImplException {
        try {
            // 獲取特定用戶所需的訂單。
            orderService.getUserOrderList(req);

            // 獲取特定用戶所需的訂單數量。
            orderService.getUserOrderCount(req);
            return "member_myOrderList";
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderControllerImplException("OrderControllerImpl 的 getUserOrderList() 有問題。");
        }
    }

    @Override
    public String getOrderDetailByNumber(HttpServletRequest req, String number) throws OrderControllerImplException {
        try {
            // 透過訂單編號獲取該訂單的詳細資訊。
            orderService.getOrderDetailByNumber(req, number);

            return "orderDetail";
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderControllerImplException("OrderControllerImpl 的 getOrderDetailByNumber() 有問題。");
        }
    }
}
