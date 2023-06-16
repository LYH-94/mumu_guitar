package controllers.impl;

import controllers.UserController;
import controllers.exception.UserControllerImplException;
import pojo.Product;
import util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

public class UserControllerImpl implements UserController {

    //private UserServiceImpl userService = null;
    private ProductControllerImpl productController = null;

    @Override
    public String checkIdentity(HttpServletRequest req, HttpServletResponse resp) throws UserControllerImplException {
        try{
            HttpSession session = req.getSession();

            String account = (String) session.getAttribute("account");
            String pwd = (String) session.getAttribute("pwd");

            if (StringUtils.isEmpty(account) || StringUtils.isEmpty(pwd)) { //沒有 account、pwd 表示是訪客。
                System.out.println("session = null");
                //由於是訪客，所以直接 ProductController 將所有產品數據 ( 預設進入商品頁面時，是顯示所有商品。 ) 直接渲染到 index.html 上。
                List<Product> productList = productController.getAllProduct();

                //獲取銷量前三名的熱門商品。
                List<Product> hotProductList = productController.getHotProduct();

                //將所有商品和熱門商品的數據存入 session 中。
                session.setAttribute("productList", productList);
                session.setAttribute("hotProductList", hotProductList);

                return "index";
            } else {
            /*
            String account = (String) session.getAttribute("account");
            String pwd = (String) session.getAttribute("pwd");
            User user = userService.getCurrentUser(account, pwd);
            if (user.getIdentity().equals("general")) {
                System.out.println("session = general");
                //ProductController => index.html
            } else if (user.getIdentity().equals("manager")) {
                System.out.println("session = manager");
                //OrderController => manager_order.html
            }
            */
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new UserControllerImplException("UserControllerImpl 的 checkIdentity() 有問題。");
        }
        return "123test";
    }
}
