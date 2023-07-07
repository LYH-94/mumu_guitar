package controllers;

import pojo.Favorite;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface FavoriteController {
    public String getFavoriteByUserId(HttpServletRequest req);

    public String add_delete_Favorite(Integer productId, HttpServletRequest req, HttpServletResponse resp);
}
