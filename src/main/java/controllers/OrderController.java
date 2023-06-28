package controllers;

import pojo.Order;

import java.util.List;

public interface OrderController {
    // 獲取所有用戶的訂單。
    public List<Order> getAllOrder();
}
