package controllers.impl;

import bo.impl.OrderServiceImpl;
import controllers.OrderController;
import controllers.exception.OrderControllerImplException;
import controllers.exception.UserControllerImplException;
import pojo.Order;

import java.util.List;

public class OrderControllerImpl implements OrderController {

    private OrderServiceImpl orderService = null;

    @Override
    public List<Order> getAllOrder() throws OrderControllerImplException {
        try {
            return orderService.getAllOrder();
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserControllerImplException("OrderControllerImpl 的 getAllOrder() 有問題。");
        }
    }
}
