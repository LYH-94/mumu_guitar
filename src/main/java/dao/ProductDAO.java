package dao;

import pojo.OrderProduct;
import pojo.Product;

import java.sql.Connection;
import java.util.List;

public interface ProductDAO {
    public List<Product> getAllProduct(Connection conn, Class<Product> clazz);

    public List<Product> getProductByType(Connection conn, Class<Product> clazz, String type);

    public List<OrderProduct> getProductByOrderId(Connection conn, Class<OrderProduct> clazz, int orderId);

    public Product getProductById(Connection conn, Class<Product> clazz, int id);

    public List<Product> getFilteredProduct(Connection conn, Class<Product> clazz, String classification, int lowest_price, int highest_price, byte inventory, String searchProduct, byte sortBy, int pageNumber);

    public List<Product> getFilteredAllProduct(Connection conn, Class<Product> clazz, String searchProduct, int pageNumber);

    public int getFilterProductCount(Connection conn, String classification, int lowest_price, int highest_price, byte inventory, String searchProduct);

    public int getFilterAllProductCount(Connection conn, String searchProductNumber);

    public void editProductById(Connection conn, Integer id, String number, String photo, String name, String Introduce, String brand, String model, String type, Integer inventory, Integer sales, Integer price);

    public void stopSaleProductById(Connection conn, Integer id, String status);

    public void addProduct(Connection conn, String number, String photo, String name, String introduce, String brand, String model, String type, Integer price, Integer inventory, Integer sales);
}