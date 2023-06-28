package dao.impl;

import controllers.exception.UserControllerImplException;
import dao.BaseDAO;
import dao.FavoriteDAO;
import dao.exception.FavoriteDAOImplException;
import pojo.Favorite;
import util.ConnUtils;

import java.sql.Connection;
import java.util.List;

public class FavoriteDAOImpl extends BaseDAO implements FavoriteDAO {
    @Override
    public List<Favorite> getFavoriteByUserId(Connection conn, Class<Favorite> clazz, int userId) throws FavoriteDAOImplException {
        try {
            String sql = "SELECT * FROM t_favorite WHERE owner = ?";
            return super.getForList(ConnUtils.getConn(), clazz, sql, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserControllerImplException("FavoriteDAOImpl 的 getFavoriteByUserId() 有問題。");
        }
    }
}
