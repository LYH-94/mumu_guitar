package controllers.impl;

import bo.impl.FavoriteServiceImpl;
import bo.impl.ProductServiceImpl;
import controllers.FavoriteController;
import controllers.exception.FavoriteControllerImplException;
import pojo.Product;
import pojo.ProductAddedFavoAndTrolInfo;
import pojo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.List;

public class FavoriteControllerImpl implements FavoriteController {

    private FavoriteServiceImpl favoriteService = null;
    private ProductServiceImpl productService = null;

    @Override
    public String getFavoriteByUserId(HttpServletRequest req) throws FavoriteControllerImplException {
        try {

            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");

            if (user != null && "general".equals(user.getIdentity())) { // 若非 null 表示已經登入。
                // 獲取該用戶追蹤的商品。
                int userId = user.getId();
                List<Product> favoriteProductList = favoriteService.getFilteredFavoriteProductByUserId(req, userId);

                // 調用函式 productService.addFavoAndTrollInfo 包裝 ProductAddedFavoAndTrolInfo 物件。
                List<ProductAddedFavoAndTrolInfo> favoriteProductAddedFavoAndTrolInfoList = productService.addFavoAndTrollInfo(favoriteProductList, req);

                // 獲取獲取到的商品數量，用於計算頁數與實現分頁功能。
                int pages;
                int favoriteProductCount = favoriteService.getFilteredFavoriteProductCountByUserId(req, userId);
                if (favoriteProductCount == 0) {
                    pages = 1;
                } else if (favoriteProductCount % 6 != 0) {
                    pages = (favoriteProductCount / 6) + 1;
                } else {
                    pages = favoriteProductCount / 6;
                }
                session.setAttribute("favoriteProductCount", favoriteProductCount); // 獲取的追蹤商品總數。
                session.setAttribute("pages", pages); // 根據追蹤商品總數計算的總頁數。

                session.setAttribute("favoriteProductList", favoriteProductAddedFavoAndTrolInfoList);

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
