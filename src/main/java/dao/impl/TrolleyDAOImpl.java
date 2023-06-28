package dao.impl;

import controllers.exception.UserControllerImplException;
import dao.BaseDAO;
import dao.TrolleyDAO;
import dao.exception.TrolleyDAOImplException;
import pojo.Trolley;
import util.ConnUtils;

import java.sql.Connection;
import java.util.List;

public class TrolleyDAOImpl extends BaseDAO implements TrolleyDAO {
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
}
