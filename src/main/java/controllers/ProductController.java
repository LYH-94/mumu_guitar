package controllers;

import pojo.OrderProduct;
import pojo.Product;
import pojo.ProductAddedFavoAndTrolInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProductController {
    public List<ProductAddedFavoAndTrolInfo> getProduct(HttpServletRequest req);

    public String getAllProductList(HttpServletRequest req);

    public Product getProductById(int id);

    public List<OrderProduct> getProductByOrderId(int orderId);

    public List<ProductAddedFavoAndTrolInfo> getHotProduct(HttpServletRequest req);

    public String productDescription(Integer id, HttpServletRequest req);

    public String editProductById(HttpServletRequest req, Integer id, String number, String photo, String name, String Introduce, String brand, String model, String type, Integer inventory, Integer sales, Integer price);

    public String stopSaleProductById(HttpServletRequest req, Integer id, String status);

    public String addProduct(HttpServletRequest req);
}