package dao;

import pojo.OrderProduct;
import pojo.Product;

import java.sql.Connection;
import java.util.List;

public interface ProductDAO {
    // 獲取所有商品數據。
    public List<Product> getAllProduct(Connection conn, Class<Product> clazz);

    // 通過 Type 獲取商品。
    public List<Product> getProductByType(Connection conn, Class<Product> clazz, String type);

    public List<OrderProduct> getProductByOrderId(Connection conn, Class<OrderProduct> clazz, int orderId);

    // 通過 Id 獲取商品。
    public Product getProductById(Connection conn, Class<Product> clazz, int id);

    // 獲取過濾後的商品。
    public List<Product> getFilteredProduct(Connection conn, Class<Product> clazz, String classification, int lowest_price, int highest_price, byte inventory, String searchProduct, byte sortBy, int pageNumber);

    public List<Product> getFilteredAllProduct(Connection conn, Class<Product> clazz, String searchProduct, int pageNumber);

    // 獲取過濾後的商品數量。
    public int getFilterProductCount(Connection conn, String classification, int lowest_price, int highest_price, byte inventory, String searchProduct);

    public int getFilterAllProductCount(Connection conn, String searchProductNumber);

    // 修改商品。
    public void editProductById(Connection conn, Integer id, String number, String photo, String name, String Introduce, String brand, String model, String type, Integer inventory, Integer sales, Integer price);

    // 刪除商品。
    public void stopSaleProductById(Connection conn, Integer id, String status);

    // 新增商品。
    public void addProduct(Connection conn, String number, String photo, String name, String introduce, String brand, String model, String type, Integer price, Integer inventory, Integer sales);
}
