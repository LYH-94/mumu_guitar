package dao.impl;

import dao.BaseDAO;
import dao.ProductDAO;
import dao.exception.ProductDAOImplException;
import pojo.OrderProduct;
import pojo.Product;

import java.sql.Connection;
import java.util.List;

public class ProductDAOImpl extends BaseDAO implements ProductDAO {
    /**
     * 獲取所有商品數據。
     *
     * @param conn  JDBC 物件。
     * @param clazz Class 類。
     * @return
     * @throws ProductDAOImplException
     */
    @Override
    public List<Product> getAllProduct(Connection conn, Class<Product> clazz) throws ProductDAOImplException {
        try {
            String sql = "SELECT * FROM t_product WHERE status = '正常'";
            return super.getForList(conn, clazz, sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductDAOImplException("ProductDAOImpl 的 getAllProduct() 有問題。");
        }
    }

    /**
     * 通過 Type 獲取商品。
     *
     * @param conn
     * @param clazz
     * @param type  商品類型。
     * @return
     * @throws ProductDAOImplException
     */
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

    /**
     * 透過訂單 id 獲取商品。
     *
     * @param conn
     * @param clazz
     * @param orderId 訂單 id。
     * @return
     * @throws ProductDAOImplException
     */
    @Override
    public List<OrderProduct> getProductByOrderId(Connection conn, Class<OrderProduct> clazz, int orderId) throws ProductDAOImplException {
        try {
            String sql = "SELECT * FROM t_orderproduct WHERE belongOrder = ?";
            return super.getForList(conn, clazz, sql, orderId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductDAOImplException("ProductDAOImpl 的 getProductByOrderNumber() 有問題。");
        }
    }

    /**
     * 通過 Id 獲取商品。
     *
     * @param conn
     * @param clazz
     * @param id    商品 id。
     * @return
     * @throws ProductDAOImplException
     */
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

    /**
     * 獲取過濾後的商品。
     *
     * @param conn
     * @param clazz
     * @param classification 商品分類。
     * @param lowest_price   最低價格。
     * @param highest_price  最高價格。
     * @param inventory      庫存狀態。
     * @param searchProduct  搜尋商品。
     * @param sortBy         排序方式。
     * @param pageNumber     使用者選擇的頁數。
     * @return
     * @throws ProductDAOImplException
     */
    @Override
    public List<Product> getFilteredProduct(Connection conn, Class<Product> clazz, String classification, int lowest_price, int highest_price, byte inventory, String searchProduct, byte sortBy, int pageNumber) throws ProductDAOImplException {
        try {
            // 計算分頁偏移量。
            int offset = (pageNumber - 1) * 6;

            // 在 SQL 語句中，通配符不能直接給 "" 這樣的空字串，
            // 因此處理成中模糊匹配的寫法，"%%" 效果可視為與指定字段中的數據皆匹配。
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
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price between ? AND ? AND inventory >= 0 AND name LIKE ? AND status = '正常' ORDER BY price DESC LIMIT 6 OFFSET ?";
                } else {
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price between ? AND ? AND inventory >= 0 AND name LIKE ? AND status = '正常' ORDER BY price ASC LIMIT 6 OFFSET ?";
                }
            } else if (inventory == 1) {
                if (sortBy == 1) {
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory > 0 AND name LIKE ? AND status = '正常' ORDER BY price DESC LIMIT 6 OFFSET ?";
                } else {
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory > 0 AND name LIKE ? AND status = '正常' ORDER BY price ASC LIMIT 6 OFFSET ?";
                }
            } else {
                if (sortBy == 1) {
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory = 0 AND name LIKE ? AND status = '正常' ORDER BY price DESC LIMIT 6 OFFSET ?";
                } else {
                    sql = "SELECT * FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory = 0 AND name LIKE ? AND status = '正常' ORDER BY price ASC LIMIT 6 OFFSET ?";
                }
            }

            return super.getForList(conn, clazz, sql, classification, lowest_price, highest_price, searchProduct, offset);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductDAOImplException("ProductDAOImpl 的 getFilteredProduct() 有問題。");
        }
    }

    /**
     * 獲取過濾後的所有商品。
     *
     * @param conn
     * @param clazz
     * @param searchProductNumber 搜尋商品編號。
     * @param pageNumber          使用者選擇的頁數。
     * @return
     * @throws ProductDAOImplException
     */
    @Override
    public List<Product> getFilteredAllProduct(Connection conn, Class<Product> clazz, String searchProductNumber, int pageNumber) throws ProductDAOImplException {
        try {
            // 計算分頁偏移量。
            int offset = (pageNumber - 1) * 10;

            // 在 SQL 語句中，通配符不能直接給 "" 這樣的空字串，
            // 因此處理成中模糊匹配的寫法，"%%" 效果可視為與指定字段中的數據皆匹配。
            searchProductNumber = "%" + searchProductNumber + "%";

            String sql = "SELECT * FROM t_product WHERE number LIKE ? ORDER BY number ASC LIMIT 10 OFFSET ?";
            return super.getForList(conn, clazz, sql, searchProductNumber, offset);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductDAOImplException("ProductDAOImpl 的 getFilteredAllProduct() 有問題。");
        }
    }

    /**
     * 獲取過濾後的商品數量。
     *
     * @param conn
     * @param classification 商品分類。
     * @param lowest_price   最低價格。
     * @param highest_price  最高價格。
     * @param inventory      商品庫存。
     * @param searchProduct  搜尋商品。
     * @return
     * @throws ProductDAOImplException
     */
    @Override
    public int getFilterProductCount(Connection conn, String classification, int lowest_price, int highest_price, byte inventory, String searchProduct) throws ProductDAOImplException {
        try {
            // 在 SQL 語句中，通配符不能直接給 "" 這樣的空字串，
            // 因此處理成模糊匹配的寫法，"%%" 效果可視為與指定字段中的數據皆匹配。
            searchProduct = "%" + searchProduct + "%";
            classification = "%" + classification + "%";

            String sql;
            // inventory == 2 庫存-所有商品
            // inventory == 1 庫存-有現貨
            // inventory == 0 庫存-需調貨
            if (inventory == 2) {
                sql = "SELECT COUNT(id) FROM t_product WHERE type LIKE ? AND price between ? AND ? AND inventory >= 0 AND name LIKE ? AND status = '正常'";
            } else if (inventory == 1) {
                sql = "SELECT COUNT(id) FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory > 0 AND name LIKE ? AND status = '正常'";
            } else {
                sql = "SELECT COUNT(id) FROM t_product WHERE type LIKE ? AND price BETWEEN ? AND ? AND inventory = 0 AND name LIKE ? AND status = '正常'";
            }

            return super.getCount(conn, sql, classification, lowest_price, highest_price, searchProduct);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductDAOImplException("ProductDAOImpl 的 getFilterProductCount() 有問題。");
        }
    }

    /**
     * 獲取過濾後的所有商品數量。
     *
     * @param conn
     * @param searchProductNumber 搜尋商品編號。
     * @return
     * @throws ProductDAOImplException
     */
    @Override
    public int getFilterAllProductCount(Connection conn, String searchProductNumber) throws ProductDAOImplException {
        try {
            // 在 SQL 語句中，通配符不能直接給 "" 這樣的空字串，
            // 因此處理成模糊匹配的寫法，"%%" 效果可視為與指定字段中的數據皆匹配。
            searchProductNumber = "%" + searchProductNumber + "%";

            String sql = "SELECT COUNT(id) FROM t_product WHERE number LIKE ?";

            return super.getCount(conn, sql, searchProductNumber);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductDAOImplException("ProductDAOImpl 的 getFilterAllProductCount() 有問題。");
        }
    }

    /**
     * 修改商品。
     *
     * @param conn
     * @param id        商品 id。
     * @param number    商品編號。
     * @param photo     商品照片。
     * @param name      商品名稱。
     * @param Introduce 商品介紹。
     * @param brand     商品品牌。
     * @param model     商品型號。
     * @param type      商品類型。
     * @param inventory 商品庫存。
     * @param sales     商品銷量。
     * @param price     商品價格。
     * @throws ProductDAOImplException
     */
    @Override
    public void editProductById(Connection conn, Integer id, String number, String photo, String name, String Introduce, String brand, String model, String type, Integer inventory, Integer sales, Integer price) throws ProductDAOImplException {
        try {
            String sql = "UPDATE t_product SET number = ?, photo = ?, name = ?, Introduce = ?, brand = ?, model = ?, type = ?, inventory = ?, sales = ?, price = ? WHERE id = ?";
            super.update(conn, sql, number, photo, name, Introduce, brand, model, type, inventory, sales, price, id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductDAOImplException("ProductDAOImpl 的 editProductById() 有問題。");
        }
    }

    /**
     * 停售指定 id 商品。
     *
     * @param conn
     * @param id
     * @param status
     * @throws ProductDAOImplException
     */
    @Override
    public void stopSaleProductById(Connection conn, Integer id, String status) throws ProductDAOImplException {
        try {
            String sql = "UPDATE t_product SET status = ? WHERE id = ?";
            super.update(conn, sql, status, id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductDAOImplException("ProductDAOImpl 的 editProductById() 有問題。");
        }
    }

    /**
     * 新增商品。
     *
     * @param conn
     * @param number    商品編號。
     * @param photo     商品照片。
     * @param name      商品名稱。
     * @param introduce 商品介紹。
     * @param brand     商品品牌。
     * @param model     商品型號。
     * @param type      商品類型。
     * @param price     商品價格。
     * @param inventory 商品庫存。
     * @param sales     商品銷量。
     * @throws ProductDAOImplException
     */
    @Override
    public void addProduct(Connection conn, String number, String photo, String name, String introduce, String brand, String model, String type, Integer price, Integer inventory, Integer sales) throws ProductDAOImplException {
        try {
            String sql = "INSERT INTO t_product(number,photo,name,introduce,brand,model,type,price,inventory,sales) VALUES(?,?,?,?,?,?,?,?,?,?)";
            super.update(conn, sql, number, photo, name, introduce, brand, model, type, price, inventory, sales);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductDAOImplException("ProductDAOImpl 的 addProduct() 有問題。");
        }
    }
}