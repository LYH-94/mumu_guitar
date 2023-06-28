package controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

public interface UserController {

    // 檢查用戶身分。
    public String checkIdentity(HttpServletRequest req);

    // 讓 Thymeleaf 渲染 logIn.html 頁面用。
    public String logInPage();

    // 登入會員。
    public String logIn(String account, String password, String verification, HttpServletRequest req);

    // 登出會員。
    public String logOut(HttpServletRequest req);

    // 讓 Thymeleaf 渲染 register.html 頁面用。
    public String registerPage(HttpServletRequest req);

    // 註冊會員。
    public String register(HttpServletRequest req, String account, String password, String username, String gender, LocalDate birthday, String phone, String email, String verification);
}
