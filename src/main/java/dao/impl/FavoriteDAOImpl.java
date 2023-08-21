package dao.impl;

import dao.BaseDAO;
import dao.FavoriteDAO;
import dao.exception.FavoriteDAOImplException;
import pojo.Favorite;

import java.sql.Connection;
import java.util.List;

public class FavoriteDAOImpl extends BaseDAO implements FavoriteDAO {
    @Override
    public List<Favorite> getFavoriteByUserId(Connection conn, Class<Favorite> clazz, int userId) throws FavoriteDAOImplException {
        try {
            String sql = "SELECT * FROM t_favorite WHERE owner = ?";
            return super.getForList(conn, clazz, sql, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FavoriteDAOImplException("FavoriteDAOImpl 的 getFavoriteByUserId() 有問題。");
        }
    }

    @Override
    public boolean checkForDuplicateUsers(Connection conn, Class<Favorite> clazz, int productId, int userId) throws FavoriteDAOImplException {
        try {
            String sql = "SELECT * FROM t_favorite WHERE product = ? AND owner = ?";
            Favorite favorite = super.getInstance(conn, clazz, sql, productId, userId);

            return (favorite == null ? true : false); // 若為不重複為 true，重複則為 false。
        } catch (Exception e) {
            e.printStackTrace();
            throw new FavoriteDAOImplException("FavoriteDAOImpl 的 getFavoriteByUserIdAndProductId() 有問題。");
        }
    }

    @Override
    public boolean checkFavorite(Connection conn, Class<Favorite> clazz, Integer productId, Integer userId) throws FavoriteDAOImplException {
        try {
            String sql = "SELECT * FROM t_favorite WHERE product = ? AND owner = ?";

            Favorite favorite = super.getInstance(conn, clazz, sql, productId, userId);

            return (favorite != null ? true : false); // 若 !=null 表示該用戶有追蹤該商品。
        } catch (Exception e) {
            e.printStackTrace();
            throw new FavoriteDAOImplException("FavoriteDAOImpl 的 checkFavorite() 有問題。");
        }
    }

    @Override
    public boolean addFavorite(Connection conn, Integer productId, Integer userId) throws FavoriteDAOImplException {
        try {
            String sql = "INSERT INTO t_favorite(product,owner) VALUES(?,?)";
            return super.update(conn, sql, productId, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FavoriteDAOImplException("FavoriteDAOImpl 的 addFavorite() 有問題。");
        }
    }

    @Override
    public boolean deleteFavorite(Connection conn, Integer productId, Integer userId) throws FavoriteDAOImplException {
        try {
            String sql = "DELETE FROM t_favorite WHERE product = ? AND owner =?";
            return super.update(conn, sql, productId, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FavoriteDAOImplException("FavoriteDAOImpl 的 deleteFavorite() 有問題。");
        }
    }
}
