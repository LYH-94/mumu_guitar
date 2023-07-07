package dao;

import pojo.Favorite;

import java.sql.Connection;
import java.util.List;

public interface FavoriteDAO {
    public List<Favorite> getFavoriteByUserId(Connection conn, Class<Favorite> clazz, int userId);

    // 獲取指定用戶的指定商品數據。
    public boolean checkForDuplicateUsers(Connection conn, Class<Favorite> clazz, int productId, int userId);

    public boolean addFavorite(Connection conn, Integer productId, Integer userId);

    public boolean deleteFavorite(Connection conn, Integer productId, Integer userId);
}
