package dao;

import pojo.Favorite;
import pojo.Product;

import java.sql.Connection;
import java.util.List;

public interface FavoriteDAO {
    public List<Favorite> getFavoriteByUserId(Connection conn, Class<Favorite> clazz, int userId);

    // 獲取指定用戶的指定商品數據。
    public boolean checkForDuplicateUsers(Connection conn, Class<Favorite> clazz, int productId, int userId);

    // 檢查指定用戶是否將該商品添加到追蹤清單。
    public boolean checkFavorite(Connection conn, Class<Favorite> clazz, Integer productId, Integer userId);

    public boolean addFavorite(Connection conn, Integer productId, Integer userId);

    public boolean deleteFavorite(Connection conn, Integer productId, Integer userId);

    // 獲取過濾後的商品。
    public List<Product> getFilteredFavoriteProduct(Connection conn, Class<Product> clazz, String classification, int lowest_price, int highest_price, byte inventory, String searchProduct, byte sortBy, int pageNumber, int userId);

    public int getFilterFavoriteProductCount(Connection conn, String classification, int lowest_price, int highest_price, byte inventory, String searchProduct, int userId);
}
