package controllers.impl;

import bo.impl.ProductServiceImpl;
import controllers.ProductController;
import controllers.exception.ProductControllerImplException;
import pojo.OrderProduct;
import pojo.Product;
import pojo.ProductAddedFavoAndTrolInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProductControllerImpl implements ProductController {

    private ProductServiceImpl productService = null;

    /**
     * 獲取用戶所需的商品列表。(for 用戶)
     *
     * @param req
     * @return
     * @throws ProductControllerImplException
     */
    @Override
    public List<ProductAddedFavoAndTrolInfo> getProduct(HttpServletRequest req) throws ProductControllerImplException {

        try {
            // 獲取需要的商品。
            List<Product> productList = productService.getFilteredProduct(req);

            // getFilteredProduct() 獲取到的 productList 是根據需求獲取的商品數據 (選擇的商品類型或篩選過後的商品等等)，但還不包含是否有添加到用戶的追蹤清單或購物車的訊息，
            // 因此還需要將商品與追蹤、購物車訊息重新包裝成 ProductAddedFavoAndTrolInfo 物件，方便後續在前端時的顯示判斷。
            // 調用函式 productService.addFavoAndTrollInfo 包裝 ProductAddedFavoAndTrolInfo 物件。
            List<ProductAddedFavoAndTrolInfo> productAddedFavoAndTrolInfoList = productService.addFavoAndTrollInfo(productList, req);

            // 獲取獲取到的商品數量，用於計算頁數與實現分頁功能。
            int pages;
            int productCount = productService.getFilterProductCount(req);
            if (productCount == 0) {
                pages = 1;
            } else if (productCount % 6 != 0) {
                pages = (productCount / 6) + 1;
            } else {
                pages = productCount / 6;
            }
            req.getSession().setAttribute("productCount", productCount); // 獲取的商品總數。
            req.getSession().setAttribute("pages", pages); // 根據商品總數計算的總頁數。

            return productAddedFavoAndTrolInfoList;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("ProductControllerImpl 的 getProduct() 有問題。");
        }
    }

    /**
     * 獲取所有商品數據。(for 管理員)
     *
     * @param req
     * @return
     * @throws ProductControllerImplException
     */
    @Override
    public String getAllProductList(HttpServletRequest req) throws ProductControllerImplException {
        try {
            // 獲取需要的商品。
            productService.getFilteredAllProduct(req);

            // 獲取獲取到的商品數量，用於計算頁數與實現分頁功能。
            productService.getFilterAllProductCount(req);

            return "manager_product";
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("ProductControllerImpl 的 getAllProductList() 有問題。");
        }
    }

    /**
     * 通過 Id 獲取商品。
     *
     * @param id
     * @return
     * @throws ProductControllerImplException
     */
    @Override
    public Product getProductById(int id) throws ProductControllerImplException {
        try {
            return productService.getProductById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("ProductControllerImpl 的 getProductById() 有問題。");
        }
    }

    /**
     * 通過訂單 id 獲取商品。
     *
     * @param orderId
     * @return
     * @throws ProductControllerImplException
     */
    @Override
    public List<OrderProduct> getProductByOrderId(int orderId) throws ProductControllerImplException {
        try {
            return productService.getProductByOrderId(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("ProductControllerImpl 的 getProductByOrderNumber() 有問題。");
        }
    }

    /**
     * 獲取前三的熱門商品。
     *
     * @param req
     * @return
     * @throws ProductControllerImplException
     */
    @Override
    public List<ProductAddedFavoAndTrolInfo> getHotProduct(HttpServletRequest req) throws ProductControllerImplException {
        try {
            List<Product> allProduct = productService.getAllProduct();

            // 對 allProduct 進行排序，根據 sales 屬性進行比較。
            allProduct.sort(Comparator.comparingInt(Product::getSales).reversed());

            // 取前三個商品做為目前的熱門商品。
            List<Product> hotProductList = allProduct.stream().limit(3).collect(Collectors.toList());

            // 調用函式 productService.addFavoAndTrollInfo 包裝 ProductAddedFavoAndTrolInfo 物件。
            List<ProductAddedFavoAndTrolInfo> hotProductAddedFavoAndTrolInfoList = productService.addFavoAndTrollInfo(hotProductList, req);

            return hotProductAddedFavoAndTrolInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("ProductControllerImpl 的 getHotProduct() 有問題。");
        }
    }

    /**
     * 獲取商品的詳細訊息。
     *
     * @param id
     * @param req
     * @return
     * @throws ProductControllerImplException
     */
    @Override
    public String productDescription(Integer id, HttpServletRequest req) throws ProductControllerImplException {
        try {
            req.getSession().setAttribute("productDesc", getProductById(id));
            return "productDescription";
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("ProductControllerImpl 的 productDescription() 有問題。");
        }
    }

    /**
     * 修改商品資料。
     *
     * @param req
     * @param id        要修改的商品 id。
     * @param number    商品編號。
     * @param photo     商品照片。
     * @param name      商品名稱。
     * @param Introduce 商品介紹。
     * @param brand     商品品牌。
     * @param model     商品型號。
     * @param type      商品類型。
     * @param inventory 商品庫存。
     * @param sales     商品銷量。
     * @param price     商品價格。
     * @return
     * @throws ProductControllerImplException
     */
    @Override
    public String editProductById(HttpServletRequest req, Integer id, String number, String photo, String name, String Introduce, String brand, String model, String type, Integer inventory, Integer sales, Integer price) throws ProductControllerImplException {
        try {
            productService.editProductById(id, number, photo, name, Introduce, brand, model, type, inventory, sales, price);
            return getAllProductList(req);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("ProductControllerImpl 的 editProductById() 有問題。");
        }
    }

    /**
     * 切換商品販售狀態。(正常/停售)
     *
     * @param req
     * @param id     商品 id。
     * @param status 商品狀態。
     * @return
     * @throws ProductControllerImplException
     */
    @Override
    public String stopSaleProductById(HttpServletRequest req, Integer id, String status) throws ProductControllerImplException {
        try {
            productService.stopSaleProductById(id, status);
            return getAllProductList(req);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("ProductControllerImpl 的 editProductById() 有問題。");
        }
    }

    /**
     * 新增商品。
     *
     * @param req
     * @return
     * @throws ProductControllerImplException
     */
    @Override
    public String addProduct(HttpServletRequest req) throws ProductControllerImplException {
        try {
            productService.addProduct(req);
            return getAllProductList(req);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("ProductControllerImpl 的 addProduct() 有問題。");
        }
    }
}