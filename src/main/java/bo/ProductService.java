package bo;

import pojo.OrderProduct;
import pojo.Product;
import pojo.ProductAddedFavoAndTrolInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProductService {
    public List<Product> getAllProduct();

    public List<OrderProduct> getProductByOrderId(int orderId);

    // 有用。
    public Product getProductById(int id);

    public List<Product> getFilteredProduct(HttpServletRequest req);

    public void getFilteredAllProduct(HttpServletRequest req);

    public int getFilterProductCount(HttpServletRequest req);

    public void getFilterAllProductCount(HttpServletRequest req);

    public List<ProductAddedFavoAndTrolInfo> addFavoAndTrollInfo(List<Product> productList, HttpServletRequest req);

    public void editProductById(Integer id, String number, String photo, String name, String introduce, String brand, String model, String type, Integer inventory, Integer sales, Integer price);

    public void stopSaleProductById(Integer id, String status);

    public void addProduct(HttpServletRequest req);
}