package dao.impl;

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
            throw new OrderDAOImplException("OrderDAOImpl 的 getAllOrder() 有問題。");
        }
    }

    @Override
    public List<Order> getUserOrderList(Connection conn, Class<Order> clazz, int userId, String searchOrder) throws OrderDAOImplException {
        try {
            String sql = "SELECT * FROM t_order WHERE owner = ? AND number LIKE ?";

            // 在 SQL 語句中，通配符不能直接給 "" 這樣的空字串，因此處理成模糊匹配的寫法，"%%" 效果可視為與指定字段中的數據皆匹配。
            searchOrder = "%" + searchOrder + "%";
            return super.getForList(conn, clazz, sql, userId, searchOrder);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderDAOImplException("OrderDAOImpl 的 getUserOrderList() 有問題。");
        }
    }

    @Override
    public int getUserOrderCount(Connection conn, int userId, String searchOrder) throws OrderDAOImplException {
        try {
            String sql = "SELECT COUNT(id) FROM t_order WHERE owner = ? AND number LIKE ?";

            // 在 SQL 語句中，通配符不能直接給 "" 這樣的空字串，因此處理成模糊匹配的寫法，"%%" 效果可視為與指定字段中的數據皆匹配。
            searchOrder = "%" + searchOrder + "%";
            return super.getCount(conn, sql, userId, searchOrder);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderDAOImplException("OrderDAOImpl 的 getUserOrderCount() 有問題。");
        }
    }

    @Override
    public Order getOrderByNumber(Connection conn, Class<Order> clazz, String number) throws OrderDAOImplException {
        try {
            String sql = "SELECT * FROM t_order WHERE number = ?";

            return super.getInstance(conn, clazz, sql, number);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderDAOImplException("OrderDAOImpl 的 getOrderByNumber() 有問題。");
        }
    }

    @Override
    public List<OrderProduct> getAllOrderProduct(Connection conn, Class<OrderProduct> clazz, int belongOrder) throws OrderDAOImplException {
        try {
            String sql = "SELECT * FROM t_orderProduct WHERE belongOrder = ?";
            return super.getForList(conn, clazz, sql, belongOrder);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderDAOImplException("OrderDAOImpl 的 getAllOrderProduct() 有問題。");
        }
    }
}
