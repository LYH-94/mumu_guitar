package dao.impl;

import dao.BaseDAO;
import dao.OrderDAO;
import dao.exception.OrderDAOImplException;
import pojo.Order;
import pojo.OrderProduct;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

public class OrderDAOImpl extends BaseDAO implements OrderDAO {
    @Override
    public List<Order> getAllOrderList(Connection conn, Class<Order> clazz, String searchOrder, int orderPageNumber) throws OrderDAOImplException {
        try {
            int offset = (orderPageNumber - 1) * 10; // 計算分頁偏移量。
            String sql = "SELECT * FROM t_order WHERE number LIKE ? ORDER BY status ASC, date DESC LIMIT 10 OFFSET ?";

            // 在 SQL 語句中，通配符不能直接給 "" 這樣的空字串，因此處理成模糊匹配的寫法，"%%" 效果可視為與指定字段中的數據皆匹配。
            searchOrder = "%" + searchOrder + "%";
            return super.getForList(conn, clazz, sql, searchOrder, offset);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderDAOImplException("OrderDAOImpl 的 getAllOrderList() 有問題。");
        }
    }

    @Override
    public List<Order> getUserOrderList(Connection conn, Class<Order> clazz, int userId, String searchOrder, int orderPageNumber) throws OrderDAOImplException {
        try {
            int offset = (orderPageNumber - 1) * 10; // 計算分頁偏移量。
            String sql = "SELECT * FROM t_order WHERE owner = ? AND number LIKE ? ORDER BY status ASC, date DESC LIMIT 10 OFFSET ?";

            // 在 SQL 語句中，通配符不能直接給 "" 這樣的空字串，因此處理成模糊匹配的寫法，"%%" 效果可視為與指定字段中的數據皆匹配。
            searchOrder = "%" + searchOrder + "%";
            return super.getForList(conn, clazz, sql, userId, searchOrder, offset);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderDAOImplException("OrderDAOImpl 的 getUserOrderList() 有問題。");
        }
    }

    @Override
    public int getAllOrderCount(Connection conn, String searchOrder) throws OrderDAOImplException {
        try {
            String sql = "SELECT COUNT(id) FROM t_order WHERE number LIKE ?";

            // 在 SQL 語句中，通配符不能直接給 "" 這樣的空字串，因此處理成模糊匹配的寫法，"%%" 效果可視為與指定字段中的數據皆匹配。
            searchOrder = "%" + searchOrder + "%";
            return super.getCount(conn, sql, searchOrder);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderDAOImplException("OrderDAOImpl 的 getAllOrderCount() 有問題。");
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

    @Override
    public void addOrder(Connection conn, String number, Date date, Integer totalAmount, Integer owner, String purchaser, String phone, String address) throws OrderDAOImplException {
        try {
            String sql = "INSERT INTO t_order(number,date,totalAmount,owner,purchaser,phone,address) VALUES(?,?,?,?,?,?,?)";

            super.update(conn, sql, number, date, totalAmount, owner, purchaser, phone, address);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderDAOImplException("OrderDAOImpl 的 addOrder() 有問題。");
        }
    }

    @Override
    public void addOrderProduct(Connection conn, Integer product, Integer quantity, Integer belongOrder) throws OrderDAOImplException {
        try {
            String sql = "INSERT INTO t_orderProduct(product,quantity,belongOrder) VALUES(?,?,?)";

            super.update(conn, sql, product, quantity, belongOrder);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderDAOImplException("OrderDAOImpl 的 addOrderProduct() 有問題。");
        }
    }

    @Override
    public void switchStatus(Connection conn, Integer status, String number) throws OrderDAOImplException {
        try {
            String sql = "UPDATE t_order SET status = ? WHERE number = ?";

            super.update(conn, sql, status, number);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderDAOImplException("OrderDAOImpl 的 switchStatus() 有問題。");
        }
    }
}
