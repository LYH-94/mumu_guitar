package dao.impl;

import dao.BaseDAO;
import dao.FavoriteDAO;
import dao.exception.FavoriteDAOImplException;
import pojo.Favorite;
import pojo.Product;

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

    @Override
    public List<Product> getFilteredFavoriteProduct(Connection conn, Class<Product> clazz, String classification, int lowest_price, int highest_price, byte inventory, String searchProduct, byte sortBy, int pageNumber, int userId) throws FavoriteDAOImplException {
        try {
            String sql;
            int offset = (pageNumber - 1) * 6; // 計算分頁偏移量。

            if (inventory == 2) { // inventory == 2 庫存-所有商品
                if (sortBy == 1) { // 降冪排序 sortBy == 1
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price between ? AND ? AND inventory >= 0 AND name LIKE ? AND id IN(SELECT product FROM t_favorite  WHERE owner = ?) ORDER BY price DESC LIMIT 6 OFFSET ?";
                } else { // 升冪排序 sortBy == 2
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price between ? AND ? AND inventory >= 0 AND name LIKE ? AND id IN(SELECT product FROM t_favorite  WHERE owner = ?) ORDER BY price ASC LIMIT 6 OFFSET ?";
                }
            } else if (inventory == 1) { // inventory == 1 庫存-有現貨
                if (sortBy == 1) { // 降冪排序 sortBy == 1
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory > 0 AND name LIKE ? AND id IN(SELECT product FROM t_favorite  WHERE owner = ?) ORDER BY price DESC LIMIT 6 OFFSET ?";
                } else { // 升冪排序 sortBy == 2
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory > 0 AND name LIKE ? AND id IN(SELECT product FROM t_favorite  WHERE owner = ?) ORDER BY price ASC LIMIT 6 OFFSET ?";
                }
            } else { // inventory == 0 庫存-需調貨
                if (sortBy == 1) { // 降冪排序 sortBy == 1
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory = 0 AND name LIKE ? AND id IN(SELECT product FROM t_favorite  WHERE owner = ?) ORDER BY price DESC LIMIT 6 OFFSET ?";
                } else { // 升冪排序 sortBy == 2
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory = 0 AND name LIKE ? AND id IN(SELECT product FROM t_favorite  WHERE owner = ?) ORDER BY price ASC LIMIT 6 OFFSET ?";
                }
            }

            // 在 SQL 語句中，通配符不能直接給 "" 這樣的空字串，因此處理成中模糊匹配的寫法，"%%" 效果可視為與指定字段中的數據皆匹配。
            searchProduct = "%" + searchProduct + "%";
            classification = "%" + classification + "%";
            return super.getForList(conn, clazz, sql, classification, lowest_price, highest_price, searchProduct, userId, offset);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FavoriteDAOImplException("FavoriteDAOImpl 的 getFilteredFavoriteProduct() 有問題。");
        }
    }

    @Override
    public int getFilterFavoriteProductCount(Connection conn, String classification, int lowest_price, int highest_price, byte inventory, String searchProduct, int userId) throws FavoriteDAOImplException {
        try {
            String sql;

            if (inventory == 2) { // inventory == 2 庫存-所有商品
                sql = "SELECT COUNT(id) FROM t_product WHERE type LIKE ? AND price between ? AND ? AND inventory >= 0 AND name LIKE ? AND id IN(SELECT product FROM t_favorite  WHERE owner = ?)";
            } else if (inventory == 1) { // inventory == 1 庫存-有現貨
                sql = "SELECT COUNT(id) FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory > 0 AND name LIKE ? AND id IN(SELECT product FROM t_favorite  WHERE owner = ?)";
            } else { // inventory == 0 庫存-需調貨
                sql = "SELECT COUNT(id) FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory = 0 AND name LIKE ? AND id IN(SELECT product FROM t_favorite  WHERE owner = ?)";
            }

            // 在 SQL 語句中，通配符不能直接給 "" 這樣的空字串，因此處理成模糊匹配的寫法，"%%" 效果可視為與指定字段中的數據皆匹配。
            searchProduct = "%" + searchProduct + "%";
            classification = "%" + classification + "%";
            return super.getCount(conn, sql, classification, lowest_price, highest_price, searchProduct, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FavoriteDAOImplException("FavoriteDAOImpl 的 getFilterFavoriteProductCount() 有問題。");
        }
    }
}
