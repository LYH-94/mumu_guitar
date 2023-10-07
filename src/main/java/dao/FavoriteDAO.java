package dao;

import pojo.Favorite;
import pojo.Product;

import java.sql.Connection;
import java.util.List;

public interface FavoriteDAO {
    public boolean checkForDuplicateUsers(Connection conn, Class<Favorite> clazz, int productId, int userId);

    public boolean checkFavorite(Connection conn, Class<Favorite> clazz, Integer productId, Integer userId);

    public boolean addFavorite(Connection conn, Integer productId, Integer userId);

    public boolean deleteFavorite(Connection conn, Integer productId, Integer userId);

    public List<Product> getFilteredFavoriteProduct(Connection conn, Class<Product> clazz, String classification, int lowest_price, int highest_price, byte inventory, String searchProduct, byte sortBy, int pageNumber, int userId);

    public int getFilterFavoriteProductCount(Connection conn, String classification, int lowest_price, int highest_price, byte inventory, String searchProduct, int userId);
}