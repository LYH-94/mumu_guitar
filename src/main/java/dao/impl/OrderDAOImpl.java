package dao.impl;

import dao.BaseDAO;
import dao.OrderDAO;
import dao.exception.OrderDAOImplException;
import pojo.Order;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDAOImpl extends BaseDAO implements OrderDAO {
    /**
     * 獲取所有會員的訂單。
     *
     * @param conn            JDBC 物件。
     * @param clazz           Class 類。
     * @param searchOrder     搜尋商品。
     * @param orderPageNumber 使用者選擇的頁數。
     * @return
     * @throws OrderDAOImplException
     */
    @Override
    public List<Order> getAllOrderList(Connection conn, Class<Order> clazz, String searchOrder, int orderPageNumber) throws OrderDAOImplException {
        try {
            // 計算分頁偏移量。
            int offset = (orderPageNumber - 1) * 10;

            // 在 SQL 語句中，通配符不能直接給 "" 這樣的空字串，
            // 因此處理成模糊匹配的寫法，"%%" 效果可視為與指定字段中的數據皆匹配。
            searchOrder = "%" + searchOrder + "%";

            String sql = "SELECT * FROM t_order WHERE number LIKE ? ORDER BY status ASC, date DESC LIMIT 10 OFFSET ?";

            return super.getForList(conn, clazz, sql, searchOrder, offset);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderDAOImplException("OrderDAOImpl 的 getAllOrderList() 有問題。");
        }
    }

    /**
     * 獲取指定會員所需的訂單。
     *
     * @param conn            JDBC 物件。
     * @param clazz           Class 類。
     * @param userId          使用者 id。
     * @param searchOrder     搜尋訂單。
     * @param orderPageNumber 使用者選擇的頁數。
     * @return
     * @throws OrderDAOImplException
     */
    @Override
    public List<Order> getUserOrderList(Connection conn, Class<Order> clazz, int userId, String searchOrder, int orderPageNumber) throws OrderDAOImplException {
        try {
            // 計算分頁偏移量。
            int offset = (orderPageNumber - 1) * 10;

            // 在 SQL 語句中，通配符不能直接給 "" 這樣的空字串，
            // 因此處理成模糊匹配的寫法，"%%" 效果可視為與指定字段中的數據皆匹配。
            searchOrder = "%" + searchOrder + "%";

            String sql = "SELECT * FROM t_order WHERE owner = ? AND number LIKE ? ORDER BY status ASC, date DESC LIMIT 10 OFFSET ?";

            return super.getForList(conn, clazz, sql, userId, searchOrder, offset);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderDAOImplException("OrderDAOImpl 的 getUserOrderList() 有問題。");
        }
    }

    /**
     * 獲取所有會員的訂單總數。
     *
     * @param conn        JDBC 物件。
     * @param searchOrder 搜尋訂單。
     * @return
     * @throws OrderDAOImplException
     */
    @Override
    public int getAllOrderCount(Connection conn, String searchOrder) throws OrderDAOImplException {
        try {
            // 在 SQL 語句中，通配符不能直接給 "" 這樣的空字串，
            // 因此處理成模糊匹配的寫法，"%%" 效果可視為與指定字段中的數據皆匹配。
            searchOrder = "%" + searchOrder + "%";

            String sql = "SELECT COUNT(id) FROM t_order WHERE number LIKE ?";

            return super.getCount(conn, sql, searchOrder);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderDAOImplException("OrderDAOImpl 的 getAllOrderCount() 有問題。");
        }
    }

    /**
     * 獲取特定會員所需的訂單數量。
     *
     * @param conn        JDBC 物件。
     * @param userId      使用者 id。
     * @param searchOrder 搜尋訂單。
     * @return
     * @throws OrderDAOImplException
     */
    @Override
    public int getUserOrderCount(Connection conn, int userId, String searchOrder) throws OrderDAOImplException {
        try {
            // 在 SQL 語句中，通配符不能直接給 "" 這樣的空字串，
            // 因此處理成模糊匹配的寫法，"%%" 效果可視為與指定字段中的數據皆匹配。
            searchOrder = "%" + searchOrder + "%";

            String sql = "SELECT COUNT(id) FROM t_order WHERE owner = ? AND number LIKE ?";

            return super.getCount(conn, sql, userId, searchOrder);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderDAOImplException("OrderDAOImpl 的 getUserOrderCount() 有問題。");
        }
    }

    /**
     * 過訂單編號獲取訂單。
     *
     * @param conn   JDBC 物件。
     * @param clazz  Class 類。
     * @param number 訂單編號。
     * @return
     * @throws OrderDAOImplException
     */
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

    /**
     * 新增訂單。
     *
     * @param conn        JDBC 物件。
     * @param number      訂單編號。
     * @param date        下單日期。
     * @param totalAmount 訂單總金額。
     * @param owner       所屬會員。
     * @param purchaser   購買人姓名。
     * @param phone       聯絡電話。
     * @param address     配送地址。
     * @throws OrderDAOImplException
     */
    @Override
    public void addOrder(Connection conn, String number, LocalDateTime date, Integer totalAmount, Integer owner, String purchaser, String phone, String address) throws OrderDAOImplException {
        try {
            String sql = "INSERT INTO t_order(number,date,totalAmount,owner,purchaser,phone,address) VALUES(?,?,?,?,?,?,?)";
            super.update(conn, sql, number, date, totalAmount, owner, purchaser, phone, address);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderDAOImplException("OrderDAOImpl 的 addOrder() 有問題。");
        }
    }

    /**
     * 新增訂單中的商品。
     *
     * @param conn        JDBC 物件。
     * @param product     要新增的商品。
     * @param quantity    要新增的商品數量。
     * @param belongOrder 所屬訂單。
     * @throws OrderDAOImplException
     */
    @Override
    public void addOrderProduct(Connection conn, Integer product, Integer quantity, Integer belongOrder) throws OrderDAOImplException {
        try {
            String sql = "INSERT INTO t_orderproduct(product,quantity,belongOrder) VALUES(?,?,?)";
            super.update(conn, sql, product, quantity, belongOrder);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderDAOImplException("OrderDAOImpl 的 addOrderProduct() 有問題。");
        }
    }

    /**
     * 切換訂單的狀態。
     *
     * @param conn   JDBC 物件。
     * @param status 新的訂單狀態。
     * @param number 訂單編號。
     * @throws OrderDAOImplException
     */
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