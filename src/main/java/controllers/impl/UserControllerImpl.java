package controllers.impl;

import bo.impl.UserServiceImpl;
import controllers.UserController;
import controllers.exception.UserControllerImplException;
import pojo.ProductAddedFavoAndTrolInfo;
import pojo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

public class UserControllerImpl implements UserController {

    private ProductControllerImpl productController = null;
    private OrderControllerImpl orderController = null;
    private UserServiceImpl userService = null;

    /**
     * 檢查訪問該網站者的身分。
     *
     * @param req Http 請求。
     * @return 返回要經過 tyhmeleaf 渲染的 Html 頁面。
     */
    @Override
    public String checkIdentity(HttpServletRequest req) throws UserControllerImplException {
        try {
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");

            // 用於渲染 Html 頁面時，判斷登入時帳號密碼是否錯誤，錯誤時為 "fail"。
            session.setAttribute("verification", "ok");

            // 用於渲染 Html 頁面時，判斷註冊時帳號是否已經存在，已存在時為 "true"。
            session.setAttribute("duplicateUsers", "false");

            // user == null 表示是訪客。
            if (user == null) {
                // 由於是訪客，所以直接調用 ProductController 將產品資訊渲染到 index.html 頁面上。
                List<ProductAddedFavoAndTrolInfo> productList = productController.getProduct(req);

                // 獲取銷量前三名的熱門商品。
                List<ProductAddedFavoAndTrolInfo> hotProductList = productController.getHotProduct(req);

                // 將所有商品和熱門商品的資訊存入 session 中，且創建一個 User 物件用於渲染 index 頁面時使用。
                session.setAttribute("productList", productList);
                session.setAttribute("hotProductList", hotProductList);
                session.setAttribute("user", new User());

                // 渲染 index 頁面。
                return "index";
            } else {
                // 若為 general 則表示一般會員。
                // 若為 manager 表示管理員。
                // 皆不是則表示為訪客。
                if ("general".equals(user.getIdentity())) {
                    // 一般會員與訪客差不多，只是在 HTML 頁面上多顯示歡迎訊息。
                    // 調用 ProductController 將產品資訊渲染到 index.html 頁面上。
                    List<ProductAddedFavoAndTrolInfo> productList = productController.getProduct(req);

                    // 獲取銷量前三名的熱門商品。
                    List<ProductAddedFavoAndTrolInfo> hotProductList = productController.getHotProduct(req);

                    // 將所有商品和熱門商品的資料存入 session 中。
                    session.setAttribute("productList", productList);
                    session.setAttribute("hotProductList", hotProductList);

                    // 渲染 index 頁面。
                    return "index";
                } else if ("manager".equals(user.getIdentity())) {
                    // 獲取所有用戶的訂單，並渲染 manager_order 頁面。
                    return orderController.getAllOrderList(req);
                } else {
                    // 由於是訪客，所以直接調用 ProductController 將產品資訊渲染到 index.html 頁面上。
                    List<ProductAddedFavoAndTrolInfo> productList = productController.getProduct(req);

                    // 獲取銷量前三名的熱門商品。
                    List<ProductAddedFavoAndTrolInfo> hotProductList = productController.getHotProduct(req);

                    // 將所有商品和熱門商品的數據存入 session 中。
                    session.setAttribute("productList", productList);
                    session.setAttribute("hotProductList", hotProductList);

                    // 渲染 index 頁面。
                    return "index";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserControllerImplException("UserControllerImpl 的 checkIdentity() 有問題。");
        }
    }

    /**
     * 讓 Thymeleaf 渲染 logIn.html 頁面用。
     */
    @Override
    public String logInPage() {
        return "logIn";
    }

    /**
     * 登入驗證。
     *
     * @param account      帳號。
     * @param password     密碼。
     * @param verification 驗證碼。
     * @param req          Http 請求。
     * @return 返回要渲染的頁面。
     * @throws UserControllerImplException
     */
    @Override
    public String logIn(String account, String password, String verification, HttpServletRequest req) throws UserControllerImplException {
        try {
            // 比對驗證碼。
            if (verification.equals(req.getSession().getAttribute("KAPTCHA_SESSION_KEY"))) {
                // 驗證會員帳號與密碼。
                User user = userService.loginVerification(account, password);

                // 若數據庫中有該會員的帳號資料，則會返回該會員的 user 物件。
                // 用 user 判斷是否登入成功。若成功則將 user 覆蓋掉 session 中的 "user"，並調用 checkIdentity()，
                // 若失敗則返回 logIn.html 並顯示帳號密碼錯誤訊息。
                if (user != null && "正常".equals(user.getStatus())) {
                    req.getSession().setAttribute("user", user);

                    // 調用 checkIdentity()。
                    return checkIdentity(req);
                } else if (user != null && "停權".equals(user.getStatus())) {
                    req.getSession().setAttribute("verification", "停權");
                    return logInPage();
                } else {
                    req.getSession().setAttribute("verification", "fail");
                    return logInPage();
                }
            } else {
                req.getSession().setAttribute("verification", "fail");
                return logInPage();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new UserControllerImplException("UserControllerImpl 的 logIn() 有問題。");
        }
    }

    /**
     * 用於登出會員。
     *
     * @param req Http 請求。
     * @return 返回要渲染的頁面。
     * @throws UserControllerImplException
     */
    @Override
    public String logOut(HttpServletRequest req) throws UserControllerImplException {
        try {
            // 直接銷毀當前 session。
            userService.invalidateSession(req);

            // 調用 checkIdentity()。
            return checkIdentity(req);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserControllerImplException("UserControllerImpl 的 logOut() 有問題。");
        }
    }

    /**
     * 讓 Thymeleaf 渲染 register.html 頁面用。
     */
    @Override
    public String registerPage() {
        return "register";
    }

    /**
     * 用於註冊會員。
     *
     * @param req          Http 請求。
     * @param account      帳號。
     * @param password     密碼。
     * @param username     使用者名稱。
     * @param gender       性別。
     * @param birthday     生日。
     * @param phone        聯絡電話。
     * @param email        電子信箱。
     * @param verification 驗證碼。
     * @return 返回要渲染的頁面。
     * @throws UserControllerImplException
     */
    @Override
    public String register(HttpServletRequest req, String account, String password, String username, String gender, LocalDate birthday, String phone, String email, String verification) throws UserControllerImplException {
        try {
            // 比對驗證碼。
            if (verification.equals(req.getSession().getAttribute("KAPTCHA_SESSION_KEY"))) {
                boolean b = userService.register(account, password, username, gender, birthday, phone, email);

                // 用 b 判斷是否註冊成功。若註冊成功為 true，不成功則為 false。
                // 若成功則調用 logIn()，若失敗則調用 registerPage() 並於 register.html 顯示"帳號已存在，請更換。"訊息。
                if (b) {
                    return logIn(account, password, (String) req.getSession().getAttribute("KAPTCHA_SESSION_KEY"), req);
                } else {
                    // 用於渲染 Html 頁面時使用。
                    req.getSession().setAttribute("duplicateUsers", "true");
                    return registerPage();
                }
            } else {
                req.getSession().setAttribute("duplicateUsers", "true");
                return registerPage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserControllerImplException("UserControllerImpl 的 register() 有問題。");
        }
    }

    /**
     * 讓 Thymeleaf 渲染 member_personalInformation.html 頁面用。
     */
    @Override
    public String member_personalInformationPage() {
        return "member_personalInformation";
    }

    /**
     * 用於更新會員資料。
     *
     * @param req      Http 請求。
     * @param account  帳號。
     * @param password 密碼。
     * @param username 使用者名稱。
     * @param gender   性別。
     * @param birthday 生日。
     * @param phone    聯絡電話。
     * @param email    電子信箱。
     * @return 返回要渲染的頁面。
     * @throws UserControllerImplException
     */
    @Override
    public String updatePersonalInfo(HttpServletRequest req, String account, String password, String username, String gender, LocalDate birthday, String phone, String email) throws UserControllerImplException {
        try {
            boolean b = userService.updatePersonalInfo(account, password, username, gender, birthday, phone, email);

            // 若更新成功 b 為 true，無任何資料被更動或更新失敗則為 false。
            if (b) {
                // 更新 session 中的 user 資料。
                User user = userService.loginVerification(account, password);
                req.getSession().setAttribute("user", user);

                // 用於渲染 Html 頁面時使用。
                req.getSession().setAttribute("updateResult", "ok");
                return "member_personalInformation";
            } else {
                req.getSession().setAttribute("updateResult", "fail");
                return "member_personalInformation";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserControllerImplException("UserControllerImpl 的 updatPersonalInfo() 有問題。");
        }
    }

    /**
     * 透過 id 獲取 user 物件。
     *
     * @param userId 使用者 id。
     * @return 返回指定 id 的 User 物件。
     * @throws UserControllerImplException
     */
    @Override
    public User getUserById(int userId) throws UserControllerImplException {
        try {
            return userService.getUserById(userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserControllerImplException("UserControllerImpl 的 getUserById() 有問題。");
        }
    }

    /**
     * 獲取所有會員資料。
     *
     * @param req Http 請求。
     * @return 讓 Thymeleaf 渲染 manager_member.html 頁面。
     * @throws UserControllerImplException
     */
    @Override
    public String getAllMemberList(HttpServletRequest req) throws UserControllerImplException {
        try {
            // 獲取所有用戶的資訊。
            userService.getAllMemberList(req);

            // 獲取所有用戶的數量。
            userService.getAllMemberCount(req);

            return "manager_member";
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserControllerImplException("UserControllerImpl 的 getAllMemberList() 有問題。");
        }
    }

    /**
     * 切換會員的狀態。(正常/停權)
     *
     * @param req    Http 請求。
     * @param userId 使用者 id。
     * @param status 會員當前狀態。
     * @return 重新獲取所有會員資料再讓 Thymeleaf 渲染 manager_member.html 頁面。
     * @throws UserControllerImplException
     */
    @Override
    public String switchStatus(HttpServletRequest req, Integer userId, String status) throws UserControllerImplException {
        try {
            userService.switchStatus(req, userId, status);
            return getAllMemberList(req);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserControllerImplException("UserControllerImpl 的 switchStatus() 有問題。");
        }
    }
}