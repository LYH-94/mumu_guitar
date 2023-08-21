package controllers.impl;

import bo.impl.FavoriteServiceImpl;
import controllers.FavoriteController;
import controllers.exception.FavoriteControllerImplException;
import pojo.Favorite;
import pojo.Product;
import pojo.ProductAddedFavoAndTrolInfo;
import pojo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FavoriteControllerImpl implements FavoriteController {

    private FavoriteServiceImpl favoriteService = null;
    private ProductControllerImpl productController = null;
    private TrolleyControllerImpl trolleyController = null;

    @Override
    public String getFavoriteByUserId(HttpServletRequest req) throws FavoriteControllerImplException {
        try {

            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");

            if (user != null && "general".equals(user.getIdentity())) { // 若非 null 表示已經登入。
                // 1.創建 List<ProductAddedFavoAndTrolInfo> 用於儲存包裝後的物件。
                List<ProductAddedFavoAndTrolInfo> productAddedFavoAndTrolInfoList = new ArrayList<>();

                int userId = user.getId();

                // 獲取該用戶的 favorite 與對應的商品物件。
                List<Favorite> favoriteList = favoriteService.getFavoriteByUserId(userId);

                for (int i = 0; i < favoriteList.size(); i++) {
                    Product product = productController.getProductById(favoriteList.get(i).getProduct().getId());
                    favoriteList.get(i).setProduct(product);
                }

                //包裝商品訊息。
                for (int i = 0; i < favoriteList.size(); i++) {
                    ProductAddedFavoAndTrolInfo productAddedFavoAndTrolInfo = new ProductAddedFavoAndTrolInfo();
                    productAddedFavoAndTrolInfo.setProduct(favoriteList.get(i).getProduct());

                    // 因商品本就是透過用戶的追蹤清單獲取的，所以直接設置為 true。
                    productAddedFavoAndTrolInfo.setExist_favorite(true);

                    // 查詢數據庫中，該用戶是否有將該商品添加到購物車。
                    boolean checkTrolley = trolleyController.checkTrolley(favoriteList.get(i).getProduct().getId(), userId);
                    productAddedFavoAndTrolInfo.setExist_trolley(checkTrolley);

                    productAddedFavoAndTrolInfoList.add(productAddedFavoAndTrolInfo);
                }

                session.setAttribute("productList", productAddedFavoAndTrolInfoList);

                return "favorite";

            } else {
                return "logIn";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FavoriteControllerImplException("FavoriteControllerImpl 的 getFavoriteByUserId() 有問題。");
        }
    }

    @Override
    public boolean checkFavorite(Integer productId, Integer userId) throws FavoriteControllerImplException {
        try {
            return favoriteService.checkFavorite(productId, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FavoriteControllerImplException("FavoriteControllerImpl 的 checkFavorite() 有問題。");
        }
    }

    @Override
    public String add_delete_Favorite(Integer productId, HttpServletRequest req, HttpServletResponse resp) throws FavoriteControllerImplException {
        try {
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");

            if (user != null && "general".equals(user.getIdentity())) { // 若非 null 表示已經登入。
                boolean b = favoriteService.addFavorite(productId, user.getId());

                PrintWriter out = resp.getWriter();
                if (b == true) {
                    out.print("add_success");
                } else {
                    out.print("delete_success");
                }
                out.flush();
                out.close();
            } else {
                PrintWriter out = resp.getWriter();
                out.print("logIn"); // 尚未登入，請用戶進行登入操作。
                out.flush();
                out.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new FavoriteControllerImplException("FavoriteControllerImpl 的 addFavorite() 有問題。");
        }
        return "axios";
    }
}
