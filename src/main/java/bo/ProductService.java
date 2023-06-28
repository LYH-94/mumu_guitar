package bo;

import pojo.Product;

import java.util.List;

public interface ProductService {
    public List<Product> getAllProduct();

    public Product getProductById(int id);
}
