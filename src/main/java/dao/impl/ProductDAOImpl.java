package dao.impl;

import dao.BaseDAO;
import dao.ProductDAO;
import dao.exception.ProductDAOImplException;
import pojo.Product;

import java.sql.Connection;
import java.util.List;

public class ProductDAOImpl extends BaseDAO implements ProductDAO {
    @Override
    public List<Product> getAllProduct(Connection conn, Class<Product> clazz) throws ProductDAOImplException {
        try {
            String sql = "SELECT * FROM t_product";
            return super.getForList(conn, clazz, sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductDAOImplException("ProductDAOImpl 的 getAllProduct() 有問題。");
        }
    }

    @Override
    public List<Product> getProductByType(Connection conn, Class<Product> clazz, String type) throws ProductDAOImplException {
        try {
            String sql = "SELECT * FROM t_product WHERE type = ?";
            return super.getForList(conn, clazz, sql, type);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductDAOImplException("ProductDAOImpl 的 getProductByType() 有問題。");
        }
    }

    @Override
    public Product getProductById(Connection conn, Class<Product> clazz, int id) throws ProductDAOImplException {
        try {
            String sql = "SELECT * FROM t_product WHERE id = ?";
            return super.getInstance(conn, clazz, sql, id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductDAOImplException("ProductDAOImpl 的 getProductById() 有問題。");
        }
    }

    @Override
    public List<Product> getFilteredProduct(Connection conn, Class<Product> clazz, String classification, int lowest_price, int highest_price, byte inventory, String searchProduct, byte sortBy, int pageNumber) throws ProductDAOImplException {
        try {
            String sql;
            int offset = (pageNumber - 1) * 6; // 計算分頁偏移量。

            if (inventory == 2) { // inventory == 2 庫存-所有商品
                if (sortBy == 1) { // 降冪排序 sortBy == 1
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price between ? AND ? AND inventory >= 0 AND name LIKE ? ORDER BY price DESC LIMIT 6 OFFSET ?";
                } else { // 升冪排序 sortBy == 2
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price between ? AND ? AND inventory >= 0 AND name LIKE ? ORDER BY price ASC LIMIT 6 OFFSET ?";
                }
            } else if (inventory == 1) { // inventory == 1 庫存-有現貨
                if (sortBy == 1) { // 降冪排序 sortBy == 1
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory > 0 AND name LIKE ? ORDER BY price DESC LIMIT 6 OFFSET ?";
                } else { // 升冪排序 sortBy == 2
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory > 0 AND name LIKE ? ORDER BY price ASC LIMIT 6 OFFSET ?";
                }
            } else { // inventory == 0 庫存-需調貨
                if (sortBy == 1) { // 降冪排序 sortBy == 1
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory = 0 AND name LIKE ? ORDER BY price DESC LIMIT 6 OFFSET ?";
                } else { // 升冪排序 sortBy == 2
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory = 0 AND name LIKE ? ORDER BY price ASC LIMIT 6 OFFSET ?";
                }
            }

            // 在 SQL 語句中，通配符不能直接給 "" 這樣的空字串，因此處理成中模糊匹配的寫法，"%%" 效果可視為與指定字段中的數據皆匹配。
            searchProduct = "%" + searchProduct + "%";
            classification = "%" + classification + "%";
            return super.getForList(conn, clazz, sql, classification, lowest_price, highest_price, searchProduct, offset);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductDAOImplException("ProductDAOImpl 的 getFilteredProduct() 有問題。");
        }
    }

    @Override
    public int getFilterProductCount(Connection conn, String classification, int lowest_price, int highest_price, byte inventory, String searchProduct) throws ProductDAOImplException {
        try {
            String sql;

            if (inventory == 2) { // inventory == 2 庫存-所有商品
                sql = "SELECT COUNT(id) FROM t_product WHERE type LIKE ? AND price between ? AND ? AND inventory >= 0 AND name LIKE ?";
            } else if (inventory == 1) { // inventory == 1 庫存-有現貨
                sql = "SELECT COUNT(id) FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory > 0 AND name LIKE ?";
            } else { // inventory == 0 庫存-需調貨
                sql = "SELECT COUNT(id) FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory = 0 AND name LIKE ?";
            }

            // 在 SQL 語句中，通配符不能直接給 "" 這樣的空字串，因此處理成模糊匹配的寫法，"%%" 效果可視為與指定字段中的數據皆匹配。
            searchProduct = "%" + searchProduct + "%";
            classification = "%" + classification + "%";
            return super.getCount(conn, sql, classification, lowest_price, highest_price, searchProduct);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductDAOImplException("ProductDAOImpl 的 getFilterProductCount() 有問題。");
        }
    }
}