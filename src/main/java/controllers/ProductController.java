package controllers;

import pojo.Product;

import java.util.List;

public interface ProductController {
    //獲取所有商品數據。
    public List<Product> getAllProduct();
    public List<Product> getHotProduct();
}
