package bo;

import pojo.OrderProduct;
import pojo.Product;
import pojo.ProductAddedFavoAndTrolInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProductService {
    public List<Product> getAllProduct();

    public List<Product> getProductByType(String type);

    public List<OrderProduct> getProductByOrderId(int orderId);

    public Product getProductById(int id);

    public List<Product> getFilteredProduct(HttpServletRequest req);

    public int getFilterProductCount(HttpServletRequest req);

    public List<ProductAddedFavoAndTrolInfo> addFavoAndTrollInfo(List<Product> productList, HttpServletRequest req);
}
