package controllers;

import pojo.User;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public interface UserController {
    public String checkIdentity(HttpServletRequest req);

    public String logInPage();

    public String logIn(String account, String password, String verification, HttpServletRequest req);

    public String logOut(HttpServletRequest req);

    public String registerPage();

    public String register(HttpServletRequest req, String account, String password, String username, String gender, LocalDate birthday, String phone, String email, String verification);

    public String member_personalInformationPage();

    public String updatePersonalInfo(HttpServletRequest req, String account, String password, String username, String gender, LocalDate birthday, String phone, String email);

    public User getUserById(int userId);

    public String getAllMemberList(HttpServletRequest req);

    public String switchStatus(HttpServletRequest req, Integer userId, String status);
}