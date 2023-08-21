package bo;

import pojo.Product;
import pojo.ProductAddedFavoAndTrolInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProductService {
    public List<Product> getAllProduct();

    public List<Product> getProductByType(String type);

    public Product getProductById(int id);

    public List<ProductAddedFavoAndTrolInfo> addFavoAndTrollInfo(List<Product> productList, HttpServletRequest req);
}
