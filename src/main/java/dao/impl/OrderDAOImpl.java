package dao.impl;

import controllers.exception.ProductControllerImplException;
import dao.BaseDAO;
import dao.OrderDAO;
import dao.exception.OrderDAOImplException;
import pojo.Order;
import pojo.OrderProduct;

import java.sql.Connection;
import java.util.List;

public class OrderDAOImpl extends BaseDAO implements OrderDAO {
    @Override
    public List<Order> getAllOrder(Connection conn, Class<Order> clazz) throws OrderDAOImplException {
        try {
            String sql = "SELECT * FROM t_order";
            return super.getForList(conn, clazz, sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("OrderDAOImpl 的 getAllOrder() 有問題。");
        }
    }

    @Override
    public List<OrderProduct> getAllOrderProduct(Connection conn, Class<OrderProduct> clazz, int belongOrder) throws OrderDAOImplException {
        try {
            String sql = "SELECT * FROM t_orderProduct WHERE belongOrder = ?";
            return super.getForList(conn, clazz, sql, belongOrder);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("OrderDAOImpl 的 getAllOrderProduct() 有問題。");
        }
    }
}
