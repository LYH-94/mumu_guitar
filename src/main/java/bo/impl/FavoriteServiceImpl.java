package bo.impl;

import bo.FavoriteService;
import bo.exception.FavoriteServiceImplException;
import controllers.exception.UserControllerImplException;
import dao.impl.FavoriteDAOImpl;
import pojo.Favorite;
import pojo.User;
import util.ConnUtils;

import javax.servlet.http.HttpSession;
import java.util.List;

public class FavoriteServiceImpl implements FavoriteService {

    FavoriteDAOImpl favoriteDAO = null;

    @Override
    public List<Favorite> getFavoriteByUserId(int userId) throws FavoriteServiceImplException {
        try {
            return favoriteDAO.getFavoriteByUserId(ConnUtils.getConn(), Favorite.class, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserControllerImplException("FavoriteServiceImpl 的 getFavoriteByUserId() 有問題。");
        }
    }
}
