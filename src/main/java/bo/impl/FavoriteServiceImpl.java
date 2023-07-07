package bo.impl;

import bo.FavoriteService;
import bo.exception.FavoriteServiceImplException;
import dao.impl.FavoriteDAOImpl;
import pojo.Favorite;
import util.ConnUtils;

import java.util.List;

public class FavoriteServiceImpl implements FavoriteService {

    FavoriteDAOImpl favoriteDAO = null;

    @Override
    public List<Favorite> getFavoriteByUserId(int userId) throws FavoriteServiceImplException {
        try {
            return favoriteDAO.getFavoriteByUserId(ConnUtils.getConn(), Favorite.class, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FavoriteServiceImplException("FavoriteServiceImpl 的 getFavoriteByUserId() 有問題。");
        }
    }

    @Override
    public boolean addFavorite(Integer productId, Integer userId) throws FavoriteServiceImplException {
        try {
            // 先判斷該用戶是否已經有追蹤同樣的商品。
            boolean b = favoriteDAO.checkForDuplicateUsers(ConnUtils.getConn(), Favorite.class, productId, userId);
            if (b) { // 若為不重複為 true，重複則為 false。*/
                // 不重複則可以進行添加操作。
                return favoriteDAO.addFavorite(ConnUtils.getConn(), productId, userId);
            } else {
                // 若重複則進行刪除操作。
                boolean b1 = favoriteDAO.deleteFavorite(ConnUtils.getConn(), productId, userId);
                return b;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FavoriteServiceImplException("FavoriteServiceImpl 的 addFavorite() 有問題。");
        }
    }
}
