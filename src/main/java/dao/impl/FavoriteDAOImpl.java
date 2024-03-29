package dao.impl;

import dao.BaseDAO;
import dao.FavoriteDAO;
import dao.exception.FavoriteDAOImplException;
import pojo.Favorite;
import pojo.Product;

import java.sql.Connection;
import java.util.List;

public class FavoriteDAOImpl extends BaseDAO implements FavoriteDAO {
    /**
     * 檢查商品是否已經存在該會員的追蹤列表中。
     *
     * @param conn      JDBC 物件。
     * @param clazz     Class 類。
     * @param productId 商品 id。
     * @param userId    使用者 id。
     * @return
     * @throws FavoriteDAOImplException
     */
    @Override
    public boolean checkForDuplicateUsers(Connection conn, Class<Favorite> clazz, int productId, int userId) throws FavoriteDAOImplException {
        try {
            String sql = "SELECT * FROM t_favorite WHERE product = ? AND owner = ?";
            Favorite favorite = super.getInstance(conn, clazz, sql, productId, userId);

            // 若有回傳 favorite 物件的話，表示該會員的追蹤列表中已經存在該商品。
            // 若不重複則返回 true，重複則返回 false。
            return (favorite == null ? true : false);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FavoriteDAOImplException("FavoriteDAOImpl 的 getFavoriteByUserIdAndProductId() 有問題。");
        }
    }

    /**
     * 檢查指定用戶是否將該商品添加到追蹤清單。
     *
     * @param conn      JDBC 物件。
     * @param clazz     Class 類。
     * @param productId 商品 id。
     * @param userId    使用者 id。
     * @return
     * @throws FavoriteDAOImplException
     */
    @Override
    public boolean checkFavorite(Connection conn, Class<Favorite> clazz, Integer productId, Integer userId) throws FavoriteDAOImplException {
        try {
            String sql = "SELECT * FROM t_favorite WHERE product = ? AND owner = ?";
            Favorite favorite = super.getInstance(conn, clazz, sql, productId, userId);

            // 若 !=null 表示該用戶有追蹤該商品。
            return (favorite != null ? true : false);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FavoriteDAOImplException("FavoriteDAOImpl 的 checkFavorite() 有問題。");
        }
    }

    /**
     * 將指定商品添加到指定會員的追蹤列表中。
     *
     * @param conn      JDBC 物件。
     * @param productId 商品 id。
     * @param userId    使用者 id。
     * @return
     * @throws FavoriteDAOImplException
     */
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

    /**
     * 刪除指定會員的追蹤列表中的指定商品。
     *
     * @param conn      JDBC 物件。
     * @param productId 商品 id。
     * @param userId    使用者 id。
     * @return
     * @throws FavoriteDAOImplException
     */
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

    /**
     * 獲取指定會員的追蹤列表中，過濾後的商品。
     *
     * @param conn           JDBC 物件。
     * @param clazz          Class 類。
     * @param classification 商品分類。
     * @param lowest_price   最低價格。
     * @param highest_price  最高價格。
     * @param inventory      庫存狀態。
     * @param searchProduct  搜尋商品。
     * @param sortBy         排序方式。
     * @param pageNumber     選擇的頁數。
     * @param userId         使用者 id。
     * @return
     * @throws FavoriteDAOImplException
     */
    @Override
    public List<Product> getFilteredFavoriteProduct(Connection conn, Class<Product> clazz, String classification, int lowest_price, int highest_price, byte inventory, String searchProduct, byte sortBy, int pageNumber, int userId) throws FavoriteDAOImplException {
        try {
            // 計算分頁偏移量。
            int offset = (pageNumber - 1) * 6;

            // 在 SQL 語句中，通配符不能直接給 "" 這樣的空字串，因此處理成中模糊匹配的寫法，"%%" 效果可視為與指定字段中的數據皆匹配。
            searchProduct = "%" + searchProduct + "%";
            classification = "%" + classification + "%";

            String sql;
            // inventory == 2 庫存-所有商品
            // inventory == 1 庫存-有現貨
            // inventory == 0 庫存-需調貨
            if (inventory == 2) {
                // 降冪排序 sortBy == 1
                // 升冪排序 sortBy == 2
                if (sortBy == 1) {
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price between ? AND ? AND inventory >= 0 AND name LIKE ? AND id IN(SELECT product FROM t_favorite  WHERE owner = ?) AND status = '正常' ORDER BY price DESC LIMIT 6 OFFSET ?";
                } else {
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price between ? AND ? AND inventory >= 0 AND name LIKE ? AND id IN(SELECT product FROM t_favorite  WHERE owner = ?) AND status = '正常' ORDER BY price ASC LIMIT 6 OFFSET ?";
                }
            } else if (inventory == 1) {
                if (sortBy == 1) {
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory > 0 AND name LIKE ? AND id IN(SELECT product FROM t_favorite  WHERE owner = ?) AND status = '正常' ORDER BY price DESC LIMIT 6 OFFSET ?";
                } else {
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory > 0 AND name LIKE ? AND id IN(SELECT product FROM t_favorite  WHERE owner = ?) AND status = '正常' ORDER BY price ASC LIMIT 6 OFFSET ?";
                }
            } else {
                if (sortBy == 1) {
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory = 0 AND name LIKE ? AND id IN(SELECT product FROM t_favorite  WHERE owner = ?) AND status = '正常' ORDER BY price DESC LIMIT 6 OFFSET ?";
                } else {
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory = 0 AND name LIKE ? AND id IN(SELECT product FROM t_favorite  WHERE owner = ?) AND status = '正常' ORDER BY price ASC LIMIT 6 OFFSET ?";
                }
            }

            return super.getForList(conn, clazz, sql, classification, lowest_price, highest_price, searchProduct, userId, offset);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FavoriteDAOImplException("FavoriteDAOImpl 的 getFilteredFavoriteProduct() 有問題。");
        }
    }

    /**
     * 獲取指定會員的追蹤列表中，過濾後的商品數量。
     *
     * @param conn           JDBC 物件。
     * @param classification 商品分類。
     * @param lowest_price   最低價格。
     * @param highest_price  最高價格。
     * @param inventory      庫存狀態。
     * @param searchProduct  搜尋商品。
     * @param userId         使用者 id。
     * @return
     * @throws FavoriteDAOImplException
     */
    @Override
    public int getFilterFavoriteProductCount(Connection conn, String classification, int lowest_price, int highest_price, byte inventory, String searchProduct, int userId) throws FavoriteDAOImplException {
        try {
            // 在 SQL 語句中，通配符不能直接給 "" 這樣的空字串，因此處理成模糊匹配的寫法，"%%" 效果可視為與指定字段中的數據皆匹配。
            searchProduct = "%" + searchProduct + "%";
            classification = "%" + classification + "%";

            String sql;
            // inventory == 2 庫存-所有商品
            // inventory == 1 庫存-有現貨
            // inventory == 0 庫存-需調貨
            if (inventory == 2) {
                sql = "SELECT COUNT(id) FROM t_product WHERE type LIKE ? AND price between ? AND ? AND inventory >= 0 AND name LIKE ? AND id IN(SELECT product FROM t_favorite  WHERE owner = ?) AND status = '正常'";
            } else if (inventory == 1) {
                sql = "SELECT COUNT(id) FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory > 0 AND name LIKE ? AND id IN(SELECT product FROM t_favorite  WHERE owner = ?) AND status = '正常'";
            } else {
                sql = "SELECT COUNT(id) FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory = 0 AND name LIKE ? AND id IN(SELECT product FROM t_favorite  WHERE owner = ?) AND status = '正常'";
            }

            return super.getCount(conn, sql, classification, lowest_price, highest_price, searchProduct, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FavoriteDAOImplException("FavoriteDAOImpl 的 getFilterFavoriteProductCount() 有問題。");
        }
    }
}