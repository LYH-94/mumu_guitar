package bo.impl;

import bo.OrderService;
import bo.exception.OrderServiceImplException;
import controllers.exception.ProductControllerImplException;
import dao.impl.OrderDAOImpl;
import pojo.Order;
import util.ConnUtils;

import java.util.List;

public class OrderServiceImpl implements OrderService {

    private OrderDAOImpl orderDAO = null;

    @Override
    public List<Order> getAllOrder() throws OrderServiceImplException {
        try {
            return orderDAO.getAllOrder(ConnUtils.getConn(), Order.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("OrderServiceImpl 的 getAllOrder() 有問題。");
        }
    }
}
