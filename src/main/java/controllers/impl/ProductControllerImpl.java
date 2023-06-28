package controllers.impl;

import bo.impl.ProductServiceImpl;
import controllers.ProductController;
import controllers.exception.ProductControllerImplException;
import pojo.Product;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProductControllerImpl implements ProductController {

    private ProductServiceImpl productService = null;

    @Override
    public List<Product> getAllProduct() throws ProductControllerImplException {
        try {
            return productService.getAllProduct();
        }catch (Exception e){
            e.printStackTrace();
            throw new ProductControllerImplException("ProductControllerImpl 的 getAllProduct() 有問題。");
        }
    }

    @Override
    public List<Product> getHotProduct() throws ProductControllerImplException {
        try {
            List<Product> allProduct = productService.getAllProduct();

            // 對 allProduct 進行排序，根據 sales 屬性進行比較。
            allProduct.sort(Comparator.comparingInt(Product::getSales).reversed());

            // 回傳前三個商品做為目前的熱門商品。
            return allProduct.stream().limit(3).collect(Collectors.toList());
        }catch (Exception e){
            e.printStackTrace();
            throw new ProductControllerImplException("ProductControllerImpl 的 getHotProduct() 有問題。");
        }
    }

    @Override
    public Product getProductById(int id) throws ProductControllerImplException {
        try {
            return productService.getProductById(id);
        }catch (Exception e){
            e.printStackTrace();
            throw new ProductControllerImplException("ProductControllerImpl 的 getProductById() 有問題。");
        }
    }
}
