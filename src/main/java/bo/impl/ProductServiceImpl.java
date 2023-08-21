package bo.impl;

import bo.ProductService;
import bo.exception.ProductServiceImplException;
import controllers.exception.ProductControllerImplException;
import controllers.impl.FavoriteControllerImpl;
import controllers.impl.TrolleyControllerImpl;
import dao.impl.ProductDAOImpl;
import pojo.*;
import util.ConnUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class ProductServiceImpl implements ProductService {

    private FavoriteControllerImpl favoriteController = null;
    private TrolleyControllerImpl trolleyController = null;
    private ProductDAOImpl productDAO = null;

    @Override
    public List<Product> getAllProduct() throws ProductServiceImplException {
        try {
            return productDAO.getAllProduct(ConnUtils.getConn(), Product.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("ProductServiceImpl 的 getAllProduct() 有問題。");
        }
    }

    @Override
    public List<Product> getProductByType(String type) throws ProductServiceImplException {
        try {
            return productDAO.getProductByType(ConnUtils.getConn(), Product.class, type);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("ProductServiceImpl 的 getProductByType() 有問題。");
        }
    }

    @Override
    public Product getProductById(int id) throws ProductServiceImplException {
        try {
            return productDAO.getProductById(ConnUtils.getConn(), Product.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("ProductServiceImpl 的 getProductById() 有問題。");
        }
    }

    @Override
    public List<ProductAddedFavoAndTrolInfo> addFavoAndTrollInfo(List<Product> productList, HttpServletRequest req) throws ProductServiceImplException {
        try {
            // 輸入參數 productList 是根據需求獲取的商品數據 (所以商品或特定類型等等)，但還不包含是否有添加到用戶的追蹤清單或購物車，
            // 因此在該函數中將商品與追蹤與購物車訊息重新包裝成 ProductAddedFavoAndTrolInfo 物件，方便後續在前端時的顯示判斷。

            // 1.創建 List<ProductAddedFavoAndTrolInfo> 用於儲存包裝後的物件。
            List<ProductAddedFavoAndTrolInfo> productAddedFavoAndTrolInfoList = new ArrayList<>();

            // 2.判斷是否為訪客、會員或管理員。
            HttpSession session = req.getSession();

            User user = (User) session.getAttribute("user");
            if (user == null) { // 沒有 user 表示是訪客。
                for (int i = 0; i < productList.size(); i++) {
                    ProductAddedFavoAndTrolInfo productAddedFavoAndTrolInfo = new ProductAddedFavoAndTrolInfo();
                    productAddedFavoAndTrolInfo.setProduct(productList.get(i));
                    productAddedFavoAndTrolInfo.setExist_favorite(false);
                    productAddedFavoAndTrolInfo.setExist_trolley(false);

                    productAddedFavoAndTrolInfoList.add(productAddedFavoAndTrolInfo);
                }

                return productAddedFavoAndTrolInfoList;
            } else {
                if ("general".equals(user.getIdentity())) {
                    for (int i = 0; i < productList.size(); i++) {
                        ProductAddedFavoAndTrolInfo productAddedFavoAndTrolInfo = new ProductAddedFavoAndTrolInfo();
                        productAddedFavoAndTrolInfo.setProduct(productList.get(i));

                        // 查詢數據庫中，該用戶是否有追蹤該商品。
                        boolean checkFavorite = favoriteController.checkFavorite(productList.get(i).getId(), user.getId());
                        productAddedFavoAndTrolInfo.setExist_favorite(checkFavorite);

                        // 查詢數據庫中，該用戶是否有將該商品添加到購物車。
                        boolean checkTrolley = trolleyController.checkTrolley(productList.get(i).getId(), user.getId());
                        productAddedFavoAndTrolInfo.setExist_trolley(checkTrolley);

                        productAddedFavoAndTrolInfoList.add(productAddedFavoAndTrolInfo);
                    }

                    return productAddedFavoAndTrolInfoList;
                } else if ("manager".equals(user.getIdentity())) {
                    for (int i = 0; i < productList.size(); i++) {
                        ProductAddedFavoAndTrolInfo productAddedFavoAndTrolInfo = new ProductAddedFavoAndTrolInfo();
                        productAddedFavoAndTrolInfo.setProduct(productList.get(i));
                        productAddedFavoAndTrolInfo.setExist_favorite(false);
                        productAddedFavoAndTrolInfo.setExist_trolley(false);

                        productAddedFavoAndTrolInfoList.add(productAddedFavoAndTrolInfo);
                    }

                    return productAddedFavoAndTrolInfoList;
                } else { //user 不等於 null 也不符合任何用戶或管理員，所以也是訪客。
                    for (int i = 0; i < productList.size(); i++) {
                        ProductAddedFavoAndTrolInfo productAddedFavoAndTrolInfo = new ProductAddedFavoAndTrolInfo();
                        productAddedFavoAndTrolInfo.setProduct(productList.get(i));
                        productAddedFavoAndTrolInfo.setExist_favorite(false);
                        productAddedFavoAndTrolInfo.setExist_trolley(false);

                        productAddedFavoAndTrolInfoList.add(productAddedFavoAndTrolInfo);
                    }

                    return productAddedFavoAndTrolInfoList;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("ProductServiceImpl 的 addFavoAndTrollInfo() 有問題。");
        }
    }
}
