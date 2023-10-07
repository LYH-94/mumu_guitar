package controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TrolleyController {
    public String getTrolleyByUserId(HttpServletRequest req);

    public boolean checkTrolley(Integer productId, Integer userId);

    public String add_delete_Trolley(Integer productId, HttpServletRequest req, HttpServletResponse resp);

    public String plusQuantity(HttpServletRequest req, Integer productId, Integer currentQuantity);

    public String reduceQuantity(HttpServletRequest req, Integer productId, Integer currentQuantity);

    public String deleteTrolleyProduct(HttpServletRequest req, Integer productId);

    public String clearTrolley(HttpServletRequest req);

    public String checkout(HttpServletRequest req);
}