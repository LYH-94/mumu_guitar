package controllers.impl;

import bo.impl.FavoriteServiceImpl;
import controllers.FavoriteController;
import controllers.exception.FavoriteControllerImplException;
import controllers.exception.UserControllerImplException;
import pojo.Favorite;
import pojo.Product;
import pojo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class FavoriteControllerImpl implements FavoriteController {

    private FavoriteServiceImpl favoriteService = null;

    private ProductControllerImpl productController = null;

    @Override
    public String getFavoriteByUserId(HttpServletRequest req) throws FavoriteControllerImplException {
        try {

            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");

            if (user != null && "general".equals(user.getIdentity())) { // 或非 null 表示
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
            throw new UserControllerImplException("FavoriteControllerImpl 的 getFavoriteByUserId() 有問題。");
        }
    }
}
