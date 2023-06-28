package controllers;

import javax.servlet.http.HttpServletRequest;

public interface FavoriteController {
    public String getFavoriteByUserId(HttpServletRequest req);
}
