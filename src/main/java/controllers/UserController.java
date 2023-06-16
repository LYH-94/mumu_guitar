package controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserController {

    //檢查用戶身分。
    public String checkIdentity(HttpServletRequest req, HttpServletResponse resp);
}
