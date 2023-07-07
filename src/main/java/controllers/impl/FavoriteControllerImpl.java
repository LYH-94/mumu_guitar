package controllers.impl;

import bo.impl.FavoriteServiceImpl;
import controllers.FavoriteController;
import controllers.exception.FavoriteControllerImplException;
import pojo.Favorite;
import pojo.Product;
import pojo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.List;

public class FavoriteControllerImpl implements FavoriteController {

    private FavoriteServiceImpl favoriteService = null;

    private ProductControllerImpl productController = null;

    @Override
    public String getFavoriteByUserId(HttpServletRequest req) throws FavoriteControllerImplException {
        try {

            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");

            if (user != null && "general".equals(user.getIdentity())) { // 若非 null 表示已經登入。
                int userId = user.getId();

                // 獲取該用戶的 favorite。
                List<Favorite> favoriteList = favoriteService.getFavoriteByUserId(userId);

                // 獲取每個 favorite 中的 product 並賦予給 favorite。
                for (int i = 0; i < favoriteList.size(); i++) {
                    Product product = productController.getProductById(favoriteList.get(i).getProduct().getId());
                    favoriteList.get(i).setProduct(product);
                }

                session.setAttribute("favoriteList", favoriteList);

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
