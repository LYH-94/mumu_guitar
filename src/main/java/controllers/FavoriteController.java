package controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface FavoriteController {
    public String getFavoriteByUserId(HttpServletRequest req);

    public boolean checkFavorite(Integer productId, Integer userId);

    public String add_delete_Favorite(Integer productId, HttpServletRequest req, HttpServletResponse resp);
}