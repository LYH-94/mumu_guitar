package controllers;

import pojo.OrderProduct;
import pojo.Product;
import pojo.ProductAddedFavoAndTrolInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProductController {
    // 獲取用戶所需的商品列表。(for 用戶)
    public List<ProductAddedFavoAndTrolInfo> getProduct(HttpServletRequest req);

    // 獲取所有商品數據。(for 管理員)
    public String getAllProductList(HttpServletRequest req);

    // 通過 Type 獲取商品。
    public List<Product> getProductByType(String type);

    // 通過 Id 獲取商品。
    public Product getProductById(int id);

    // 通過訂單id獲取商品。
    public List<OrderProduct> getProductByOrderId(int orderId);

    // 獲取前三的熱門商品。
    public List<ProductAddedFavoAndTrolInfo> getHotProduct(HttpServletRequest req);

    // 獲取商品的詳細訊息。
    public String productDescription(Integer id, HttpServletRequest req);

    // 修改商品。
    public String editProductById(HttpServletRequest req, Integer id, String number, String photo, String name, String Introduce, String brand, String model, String type, Integer inventory, Integer sales, Integer price);

    // 刪除商品。
    public String stopSaleProductById(HttpServletRequest req, Integer id, String status);

    // 新增商品。
    public String addProduct(HttpServletRequest req);
}
