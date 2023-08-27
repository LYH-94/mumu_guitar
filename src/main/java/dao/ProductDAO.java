package dao;

import pojo.Product;
import pojo.User;
import util.ConnUtils;

import java.sql.Connection;
import java.util.List;

public interface ProductDAO {
    // 獲取所有商品數據。
    public List<Product> getAllProduct(Connection conn, Class<Product> clazz);

    // 通過 Type 獲取商品。
    public List<Product> getProductByType(Connection conn, Class<Product> clazz, String type);

    // 通過 Id 獲取商品。
    public Product getProductById(Connection conn, Class<Product> clazz, int id);

    // 獲取過濾後的商品。
    public List<Product> getFilteredProduct(Connection conn, Class<Product> clazz, String classification, int lowest_price, int highest_price, byte inventory, String searchProduct, byte sortBy, int pageNumber);

    // 獲取過濾後的商品數量。
    public int getFilterProductCount(Connection conn, String classification, int lowest_price, int highest_price, byte inventory, String searchProduct);

}
