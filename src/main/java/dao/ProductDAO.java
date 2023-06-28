package dao;

import pojo.Product;
import pojo.User;
import util.ConnUtils;

import java.sql.Connection;
import java.util.List;

public interface ProductDAO {
    // 獲取所有商品數據。
    public List<Product> getAllProduct(Connection conn, Class<Product> clazz);

    // 通過 Id 獲取商品。
    public Product getProductById(Connection conn, Class<Product> clazz, int id);
}
