package controllers;

import pojo.Product;

import java.util.List;

public interface ProductController {
    // 獲取所有商品數據。
    public List<Product> getAllProduct();

    // 獲取前三的熱門商品。
    public List<Product> getHotProduct();

    // 通過 Type 獲取商品。
    public List<Product> getProductByType(String type);

    // 通過 Id 獲取商品。
    public Product getProductById(int id);
}
