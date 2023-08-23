package controllers.impl;

import bo.impl.ProductServiceImpl;
import controllers.ProductController;
import controllers.exception.ProductControllerImplException;
import pojo.Product;
import pojo.ProductAddedFavoAndTrolInfo;
import util.StringUtils;
import util.compareUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProductControllerImpl implements ProductController {

    private ProductServiceImpl productService = null;

    @Override
    public List<ProductAddedFavoAndTrolInfo> getProduct(HttpServletRequest req) throws ProductControllerImplException {
        try {
            // 判斷是否有選擇商品的分類。
            String classification = req.getParameter("classification");
            HttpSession session = req.getSession();
            String session_classification = (String) session.getAttribute("session_classification");
            // 先以 req 請求為主進行來判斷，若為空才根據 session 來判斷。
            if (StringUtils.isEmpty(classification)) {
                if (StringUtils.isEmpty(session_classification)) {
                    classification = "所有商品";
                } else {
                    classification = session_classification;
                }
            }

            // 在 session 中以數字的方式儲存 classification，用以渲染模板時，判斷當前用戶選擇的商品類型。
            // 所有商品：
            // 木吉他：1000
            // 電吉他：2000
            // ...
            List<Product> productList = null;
            if ("所有商品".equals(classification)) { // 預設進入商品頁面時，是顯示所有商品。
                session.setAttribute("classification", 0);
                session.setAttribute("session_classification", "所有商品");
                productList = getAllProduct();
            } else if ("木吉他".equals(classification)) {
                session.setAttribute("classification", 1000);
                session.setAttribute("session_classification", "木吉他");
                productList = getProductByType(classification);
            } else if ("電吉他".equals(classification)) {
                session.setAttribute("classification", 2000);
                session.setAttribute("session_classification", "電吉他");
                productList = getProductByType(classification);
            }

            // 獲取篩選條件並篩選商品。
            // 獲取篩選表單中的數據。
            int lowest_price = 0;
            int highest_price = 999999;
            byte inventory = 2;
            String searchProduct = "";

            String session_lowest_price = (String) session.getAttribute("session_lowest_price");
            if (StringUtils.isEmpty(req.getParameter("lowest_price"))) {
                if (StringUtils.isEmpty(session_lowest_price)) {
                    lowest_price = 0;
                } else {
                    lowest_price = Integer.parseInt(session_lowest_price);
                }
            } else if (Integer.parseInt(req.getParameter("lowest_price")) < 0) {
                lowest_price = 0;
            } else if (Integer.parseInt(req.getParameter("lowest_price")) > 999999) {
                lowest_price = 999999;
            } else {
                lowest_price = Integer.parseInt(req.getParameter("lowest_price"));
            }
            session.setAttribute("session_lowest_price", Integer.toString(lowest_price));

            String session_highest_price = (String) session.getAttribute("session_highest_price");
            if (StringUtils.isEmpty(req.getParameter("highest_price"))) {
                if (StringUtils.isEmpty(session_highest_price)) {
                    highest_price = 999999;
                } else {
                    highest_price = Integer.parseInt(session_highest_price);
                }
            } else if (Integer.parseInt(req.getParameter("highest_price")) < 0 || Integer.parseInt(req.getParameter("highest_price")) > 999999) {
                highest_price = 999999;
            } else {
                highest_price = Integer.parseInt(req.getParameter("highest_price"));
            }
            session.setAttribute("session_highest_price", Integer.toString(highest_price));

            String session_inventory = (String) session.getAttribute("session_inventory");
            if (StringUtils.isEmpty(req.getParameter("inventory"))) {
                if (StringUtils.isEmpty(session_inventory)) {
                    inventory = 2;
                } else {
                    inventory = Byte.parseByte(session_inventory);
                }
            } else {
                inventory = Byte.parseByte(req.getParameter("inventory"));
            }
            session.setAttribute("session_inventory", Byte.toString(inventory));

            String session_searchProduct = (String) session.getAttribute("session_searchProduct");
            if (StringUtils.isEmpty(req.getParameter("searchProduct"))) {
                if (StringUtils.isEmpty(session_searchProduct)) {
                    searchProduct = "";
                } else {
                    searchProduct = session_searchProduct;
                }
            } else if ("reset".equals(req.getParameter("searchProduct"))) {
                searchProduct = "";
            } else {
                searchProduct = req.getParameter("searchProduct");
            }
            session.setAttribute("session_searchProduct", searchProduct);

            // 開始篩選商品。
            for (int i = 0; i < productList.size(); ++i) {
                // 1.篩選價格。
                if (productList.get(i).getPrice() >= lowest_price && productList.get(i).getPrice() <= highest_price) {

                    // 2.篩選庫存。
                    if (inventory == 1) { // 1=有現貨、0=需調貨、2=所有商品
                        if (productList.get(i).getInventory() > 0) {
                            // 3.篩選商品名稱。
                            boolean match = true;
                            for (int j = 0; j < searchProduct.length(); ++j) {
                                if (productList.get(i).getName().indexOf(searchProduct.charAt(j)) != -1) {
                                    match = false;
                                }
                            }
                            if (match == true && StringUtils.isNotEmpty(searchProduct)) {
                                productList.remove(i);
                                --i;
                            }
                        } else {
                            productList.remove(i);
                            --i;
                        }
                    } else if (inventory == 0) {
                        if (productList.get(i).getInventory() <= 0) {
                            // 3.篩選商品名稱。
                            boolean match = true;
                            for (int j = 0; j < searchProduct.length(); ++j) {
                                if (productList.get(i).getName().indexOf(searchProduct.charAt(j)) != -1) {
                                    match = false;
                                }
                            }
                            if (match == true && StringUtils.isNotEmpty(searchProduct)) {
                                productList.remove(i);
                                --i;
                            }
                        } else {
                            productList.remove(i);
                            --i;
                        }
                    } else {
                        // 3.篩選商品名稱。
                        boolean match = true;
                        for (int j = 0; j < searchProduct.length(); ++j) {
                            if (productList.get(i).getName().indexOf(searchProduct.charAt(j)) != -1) {
                                match = false;
                            }
                        }
                        if (match == true && StringUtils.isNotEmpty(searchProduct)) {
                            productList.remove(i);
                            --i;
                        }
                    }
                } else {
                    productList.remove(i);
                    --i;
                }
            }

            // 獲取選擇的排序方式並排序商品。
            // 首先獲取用戶選擇的排序方式。1-售價(H-L)、2-售價(L-H)。
            byte sortBy;
            String session_sortBy = (String) session.getAttribute("session_sortBy");
            if (StringUtils.isEmpty(req.getParameter("sortBy"))) { // 預設為 1。
                if (StringUtils.isEmpty(session_sortBy)) {
                    sortBy = 1;
                } else {
                    sortBy = Byte.parseByte(session_sortBy);
                }
            } else {
                sortBy = Byte.parseByte(req.getParameter("sortBy"));
            }
            session.setAttribute("session_sortBy", Byte.toString(sortBy));

            // 進行排序。
            compareUtils compareUtils = new compareUtils();
            if (sortBy == 1) {
                Collections.sort(productList, compareUtils);
                Collections.reverse(productList);
            } else if (sortBy == 2) {
                Collections.sort(productList, compareUtils);
            }

            // 至此 productList 是根據需求獲取的商品數據 (選擇的商品類型或篩選過後的商品等等)，但還不包含是否有添加到用戶的追蹤清單或購物車的訊息，
            // 因此還需要將商品與追蹤、購物車訊息重新包裝成 ProductAddedFavoAndTrolInfo 物件，方便後續在前端時的顯示判斷。
            // 調用函式 productService.addFavoAndTrollInfo 包裝 ProductAddedFavoAndTrolInfo 物件。
            List<ProductAddedFavoAndTrolInfo> productAddedFavoAndTrolInfoList = productService.addFavoAndTrollInfo(productList, req);

            //回傳。
            return productAddedFavoAndTrolInfoList;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("ProductControllerImpl 的 getProduct() 有問題。");
        }
    }

    @Override
    public List<Product> getAllProduct() {
        try {
            return productService.getAllProduct();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("ProductControllerImpl 的 getAllProduct() 有問題。");
        }
    }

    @Override
    public List<Product> getProductByType(String type) throws ProductControllerImplException {
        try {
            return productService.getProductByType(type);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("ProductControllerImpl 的 getProductByType() 有問題。");
        }
    }

    @Override
    public Product getProductById(int id) throws ProductControllerImplException {
        try {
            return productService.getProductById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("ProductControllerImpl 的 getProductById() 有問題。");
        }
    }

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

            //處理回傳。
            return hotProductAddedFavoAndTrolInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("ProductControllerImpl 的 getHotProduct() 有問題。");
        }
    }

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
}
