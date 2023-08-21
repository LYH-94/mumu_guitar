package controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TrolleyController {
    public String getTrolleyByUserId(HttpServletRequest req);
    public boolean checkTrolley(Integer productId, Integer userId);

    public String add_delete_Trolley(Integer productId, HttpServletRequest req, HttpServletResponse resp);
}
