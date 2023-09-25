package controllers;

import pojo.User;

import javax.servlet.http.HttpServletRequest;
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
    public String registerPage();

    // 註冊會員。
    public String register(HttpServletRequest req, String account, String password, String username, String gender, LocalDate birthday, String phone, String email, String verification);

    // 用於跳轉用戶資料頁面-已經獲取 User 數據，因此不需要經過 Thymeleaf 渲染，所以直接跳轉至用戶資料頁面 member_personalInformation.html。
    public String member_personalInformationPage();

    // 更新用戶資料。
    public String updatePersonalInfo(HttpServletRequest req, String account, String password, String username, String gender, LocalDate birthday, String phone, String email);

    // 透過id獲取user。
    public User getUserById(int userId);

    // 獲取所有用戶資料。
    public String getAllMemberList(HttpServletRequest req);

    // 切換用戶狀態。
    public String switchStatus(HttpServletRequest req, Integer userId, String status);

}
