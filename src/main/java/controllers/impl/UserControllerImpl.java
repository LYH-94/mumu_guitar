package controllers.impl;

import bo.impl.UserServiceImpl;
import controllers.UserController;
import controllers.exception.UserControllerImplException;
import pojo.Order;
import pojo.Product;
import pojo.User;
import util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

public class UserControllerImpl implements UserController {

    private UserServiceImpl userService = null;
    private ProductControllerImpl productController = null;
    private OrderControllerImpl orderController = null;

    @Override
    public String checkIdentity(HttpServletRequest req) throws UserControllerImplException {
        try {
            HttpSession session = req.getSession();

            User user = (User) session.getAttribute("user");
            session.setAttribute("verification", "123"); // 只用來判斷登入時帳號密碼是否錯誤，錯誤時為 "fail"。
            req.getSession().setAttribute("duplicateUsers", "false"); // 只用來判斷註冊時帳號是已存在，已存在時為 "true"。

            // 判斷是否有選擇商品的分類。
            String classification = req.getParameter("classification");
            if (StringUtils.isEmpty(classification)) classification = "all";

            if (user == null) { // 沒有 user 表示是訪客。
                // 由於是訪客，所以直接調用 ProductController 將產品數據直接渲染到 index.html 上。
                List<Product> productList = null;
                if ("all".equals(classification)) { // 預設進入商品頁面時，是顯示所有商品。
                    productList = productController.getAllProduct();
                } else if ("木吉他".equals(classification)) {
                    productList = productController.getProductByType(classification);
                } else if ("電吉他".equals(classification)) {
                    productList = productController.getProductByType(classification);
                }


                // 獲取銷量前三名的熱門商品。
                List<Product> hotProductList = productController.getHotProduct();

                // 將所有商品和熱門商品的數據存入 session 中，且創建一個 User 物件。
                session.setAttribute("productList", productList);
                session.setAttribute("hotProductList", hotProductList);
                session.setAttribute("user", new User());

                return "index";
            } else {
                if ("general".equals(user.getIdentity())) {
                    // 一般用戶與訪客差不多，只是在 HTML 頁面上多顯示歡迎訊息。
                    // 調用 ProductController 將產品數據直接渲染到 index.html 上。
                    List<Product> productList = null;
                    if ("all".equals(classification)) { // 預設進入商品頁面時，是顯示所有商品。
                        productList = productController.getAllProduct();
                    } else if ("木吉他".equals(classification)) {
                        productList = productController.getProductByType(classification);
                    } else if ("電吉他".equals(classification)) {
                        productList = productController.getProductByType(classification);
                    }

                    // 獲取銷量前三名的熱門商品。
                    List<Product> hotProductList = productController.getHotProduct();

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
                } else {
                    // 由於是訪客，所以直接調用 ProductController 將產品數據直接渲染到 index.html 上。
                    List<Product> productList = null;
                    if ("all".equals(classification)) { // 預設進入商品頁面時，是顯示所有商品。
                        productList = productController.getAllProduct();
                    } else if ("木吉他".equals(classification)) {
                        productList = productController.getProductByType(classification);
                    } else if ("電吉他".equals(classification)) {
                        productList = productController.getProductByType(classification);
                    }

                    // 獲取銷量前三名的熱門商品。
                    List<Product> hotProductList = productController.getHotProduct();

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
            if (verification.equals((String) req.getSession().getAttribute("KAPTCHA_SESSION_KEY"))) {
                // 驗證用戶帳號與密碼。
                User user = userService.loginVerification(account, password);

                // 用 user 判斷是否登入成功。若成功則將user加入session並調用checkIdentity()，若失敗則返回logIn.html並顯示帳號密碼錯誤訊息。
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
    public String registerPage(HttpServletRequest req) {
        return "register";
    }

    @Override
    public String register(HttpServletRequest req, String account, String password, String username, String gender, LocalDate birthday, String phone, String email, String verification) throws UserControllerImplException {
        try {
            if (verification.equals((String) req.getSession().getAttribute("KAPTCHA_SESSION_KEY"))) {
                boolean b = userService.register(account, password, username, gender, birthday, phone, email);

                // 用 b 判斷是否註冊成功。若成功則調用將user加入session並調用logIn()，若失敗則調用registerPage()並於register.html顯示"帳號已存在，請更換。"訊息。
                // 若註冊成功為 true，不成功則為 false。
                if (b) {
                    return logIn(account, password, (String) req.getSession().getAttribute("kaptcha_session_key"), req);
                } else {
                    req.getSession().setAttribute("duplicateUsers", "true");
                    return registerPage(req);
                }
            } else {
                req.getSession().setAttribute("duplicateUsers", "true");
                return registerPage(req);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserControllerImplException("UserControllerImpl 的 register() 有問題。");
        }
    }
}
