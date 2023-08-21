package dao.impl;

import dao.BaseDAO;
import dao.TrolleyDAO;
import dao.exception.TrolleyDAOImplException;
import pojo.Trolley;
import util.ConnUtils;

import java.sql.Connection;
import java.util.List;

public class TrolleyDAOImpl extends BaseDAO implements TrolleyDAO {

    @Override
    public boolean checkForDuplicateUsers(Connection conn, Class<Trolley> clazz, int productId, int userId) throws TrolleyDAOImplException {
        try {
            String sql = "SELECT * FROM t_trolley WHERE product = ? AND owner = ?";
            Trolley trolley = super.getInstance(conn, clazz, sql, productId, userId);

            return (trolley == null ? true : false); // 若為不重複為 true，重複則為 false。
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrolleyDAOImplException("TrolleyDAOImpl 的 checkForDuplicateUsers() 有問題。");
        }
    }

    @Override
    public boolean checkTrolley(Connection conn, Class<Trolley> clazz, Integer productId, Integer userId) throws TrolleyDAOImplException {
        try {
            String sql = "SELECT * FROM t_trolley WHERE product = ? AND owner = ?";
            Trolley trolley = super.getInstance(conn, clazz, sql, productId, userId);

            return (trolley != null ? true : false); // 若 !=null 表示該用戶有添加該商品到購物車中。
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrolleyDAOImplException("TrolleyDAOImpl 的 checkTrolley() 有問題。");
        }
    }

    @Override
    public List<Trolley> getTrolleyByUserId(Connection conn, Class<Trolley> clazz, int userId) throws TrolleyDAOImplException {
        try {
            String sql = "SELECT * FROM t_trolley WHERE owner = ?";
            return super.getForList(ConnUtils.getConn(), clazz, sql, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrolleyDAOImplException("TrolleyDAOImpl 的 getFavoriteByUserId() 有問題。");
        }
    }

    @Override
    public boolean addTrolley(Connection conn, Integer productId, Integer userId) throws TrolleyDAOImplException {
        try {
            String sql = "INSERT INTO t_trolley(product,quantity,owner) VALUES(?,1,?)";
            return super.update(conn, sql, productId, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrolleyDAOImplException("TrolleyDAOImpl 的 addTrolley() 有問題。");
        }
    }

    @Override
    public boolean deleteTrolley(Connection conn, Integer productId, Integer userId) throws TrolleyDAOImplException {
        try {
            String sql = "DELETE FROM t_trolley WHERE product = ? AND owner =?";
            return super.update(conn, sql, productId, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrolleyDAOImplException("TrolleyDAOImpl 的 deleteTrolley() 有問題。");
        }
    }

}
