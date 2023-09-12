package controllers.impl;

import bo.impl.UserServiceImpl;
import controllers.UserController;
import controllers.exception.UserControllerImplException;
import pojo.Order;
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

    @Override
    public String checkIdentity(HttpServletRequest req) throws UserControllerImplException {
        try {
            HttpSession session = req.getSession();

            User user = (User) session.getAttribute("user");
            session.setAttribute("verification", "ok"); // 只用來判斷登入時帳號密碼是否錯誤，錯誤時為 "fail"。
            session.setAttribute("duplicateUsers", "false"); // 只用來判斷註冊時帳號是否已存在，已存在時為 "true"。

            if (user == null) { // 沒有 user 表示是訪客。
                // 由於是訪客，所以直接調用 ProductController 將產品數據直接渲染到 index.html 上。
                List<ProductAddedFavoAndTrolInfo> productList = productController.getProduct(req);

                // 獲取銷量前三名的熱門商品。
                List<ProductAddedFavoAndTrolInfo> hotProductList = productController.getHotProduct(req);

                // 將所有商品和熱門商品的數據存入 session 中，且創建一個 User 物件。
                session.setAttribute("productList", productList);
                session.setAttribute("hotProductList", hotProductList);
                session.setAttribute("user", new User());

                return "index";
            } else {
                if ("general".equals(user.getIdentity())) {
                    // 一般用戶與訪客差不多，只是在 HTML 頁面上多顯示歡迎訊息。
                    // 調用 ProductController 將產品數據直接渲染到 index.html 上。
                    List<ProductAddedFavoAndTrolInfo> productList = productController.getProduct(req);

                    // 獲取銷量前三名的熱門商品。
                    List<ProductAddedFavoAndTrolInfo> hotProductList = productController.getHotProduct(req);

                    // 將所有商品和熱門商品的數據存入 session 中。
                    session.setAttribute("productList", productList);
                    session.setAttribute("hotProductList", hotProductList);

                    return "index";
                } else if ("manager".equals(user.getIdentity())) {
                    // 獲取所有用戶的訂單。
                    List<Order> ordertList = orderController.getAllOrder();

                    // 獲取各訂單所屬用戶並賦給 ordertList。
                    for (int i = 0; i < ordertList.size(); i++) {
                        ordertList.get(i).setOwner(userService.getUserById(ordertList.get(i).getOwner().getId()));
                    }

                    // 將所有用戶訂單存入 session 中。
                    session.setAttribute("ordertList", ordertList);

                    return "manager_order";
                } else { // user 不等於 null 也不符合任何用戶或管理員，所以也是訪客。
                    // 由於是訪客，所以直接調用 ProductController 將產品數據直接渲染到 index.html 上。
                    List<ProductAddedFavoAndTrolInfo> productList = productController.getProduct(req);

                    // 獲取銷量前三名的熱門商品。
                    List<ProductAddedFavoAndTrolInfo> hotProductList = productController.getHotProduct(req);

                    // 將所有商品和熱門商品的數據存入 session 中。
                    session.setAttribute("productList", productList);
                    session.setAttribute("hotProductList", hotProductList);

                    return "index";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserControllerImplException("UserControllerImpl 的 checkIdentity() 有問題。");
        }
    }

    @Override
    public String logInPage() {
        return "logIn";
    }

    @Override
    public String logIn(String account, String password, String verification, HttpServletRequest req) throws UserControllerImplException {
        try {
            if (verification.equals(req.getSession().getAttribute("KAPTCHA_SESSION_KEY"))) {
                // 驗證用戶帳號與密碼。
                User user = userService.loginVerification(account, password);

                // 用 user 判斷是否登入成功。若成功則將 user 加入 session 並調用 checkIdentity()，若失敗則返回 logIn.html 並顯示帳號密碼錯誤訊息。
                if (user != null) {
                    req.getSession().setAttribute("user", user);

                    // 調用 checkIdentity()。
                    return checkIdentity(req);
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

    @Override
    public String logOut(HttpServletRequest req) throws UserControllerImplException {
        try {
            // 銷毀當前 session。
            userService.invalidateSession(req);

            // 調用 checkIdentity()。
            return checkIdentity(req);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserControllerImplException("UserControllerImpl 的 logOut() 有問題。");
        }
    }

    @Override
    public String registerPage() {
        return "register";
    }

    @Override
    public String register(HttpServletRequest req, String account, String password, String username, String gender, LocalDate birthday, String phone, String email, String verification) throws UserControllerImplException {
        try {
            if (verification.equals(req.getSession().getAttribute("KAPTCHA_SESSION_KEY"))) {
                boolean b = userService.register(account, password, username, gender, birthday, phone, email);

                // 用 b 判斷是否註冊成功。若成功則調用將 user 加入 session 並調用 logIn()，若失敗則調用 registerPage() 並於 register.html 顯示"帳號已存在，請更換。"訊息。
                // 若註冊成功為 true，不成功則為 false。
                if (b) {
                    return logIn(account, password, (String) req.getSession().getAttribute("kaptcha_session_key"), req);
                } else {
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

    @Override
    public String member_personalInformationPage() {
        return "member_personalInformation";
    }

    @Override
    public String updatePersonalInfo(HttpServletRequest req, String account, String password, String username, String gender, LocalDate birthday, String phone, String email) throws UserControllerImplException {
        try {
            boolean b = userService.updatePersonalInfo(account, password, username, gender, birthday, phone, email);

            if (b) { // 更新成功。
                // 更新session中的user資料。
                User user = userService.loginVerification(account, password);

                req.getSession().setAttribute("user", user);
                req.getSession().setAttribute("updateResult", "ok");
                return "member_personalInformation";
            } else { // 無任何資料被更動或更新失敗。
                req.getSession().setAttribute("updateResult", "fail");
                return "member_personalInformation";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserControllerImplException("UserControllerImpl 的 updatPersonalInfo() 有問題。");
        }
    }

    @Override
    public User getUserById(int userId) throws UserControllerImplException {
        try {
            return userService.getUserById(userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserControllerImplException("UserControllerImpl 的 getUserById() 有問題。");
        }
    }

}
