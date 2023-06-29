package bo;

import pojo.Product;

import java.util.List;

public interface ProductService {
    public List<Product> getAllProduct();

    public List<Product> getProductByType(String type);

    public Product getProductById(int id);
}
