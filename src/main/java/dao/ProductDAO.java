package dao;

import pojo.Product;

import java.sql.Connection;
import java.util.List;

public interface ProductDAO {
    //獲取所有商品數據。
    public List<Product> getAllProduct(Connection conn, Class<Product> clazz);
}
