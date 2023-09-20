package controllers.impl;

import bo.impl.TrolleyServiceImpl;
import controllers.TrolleyController;
import controllers.exception.TrolleyControllerImplException;
import pojo.Product;
import pojo.Trolley;
import pojo.TrolleyClass;
import pojo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.List;

public class TrolleyControllerImpl implements TrolleyController {

    private TrolleyServiceImpl trolleyService = null;

    private ProductControllerImpl productController = null;

    @Override
    public String getTrolleyByUserId(HttpServletRequest req) throws TrolleyControllerImplException {
        try {

            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");
            TrolleyClass trolleyClass = new TrolleyClass();

            if (user != null && "general".equals(user.getIdentity())) { // 判斷是否已經登入且是一般用戶。
                int userId = user.getId();

                // 獲取該用戶的 trolley。
                List<Trolley> trolleyList = trolleyService.getTrolleyByUserId(userId);

                // 獲取每個 trolley 中的 product 並賦予給 trolley。
                Integer totalQuantity = 0;
                Integer totalAmount = 0;
                for (int i = 0; i < trolleyList.size(); i++) {
                    Product product = productController.getProductById(trolleyList.get(i).getProduct().getId());
                    Integer quantity = trolleyList.get(i).getQuantity();
                    Integer subTotal = quantity * product.getPrice();

                    trolleyClass.getProduct().add(product); // 商品
                    trolleyClass.getQuantity().add(quantity); // 商品數量
                    trolleyClass.getSubTotal().add(subTotal); // 小計

                    totalQuantity = totalQuantity + quantity;
                    totalAmount = totalAmount + subTotal;
                }

                trolleyClass.setTotalQuantity(totalQuantity); //總商品數量
                trolleyClass.setTotalAmount(totalAmount); //總金額
                trolleyClass.setOwner(user);

                session.setAttribute("trolleyClass", trolleyClass);

                return "trolley";
            } else {
                return "logIn";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrolleyControllerImplException("TrolleyControllerImpl 的 getFavoriteByUserId() 有問題。");
        }
    }

    @Override
    public boolean checkTrolley(Integer productId, Integer userId) throws TrolleyControllerImplException {
        try {
            return trolleyService.checkTrolley(productId, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrolleyControllerImplException("TrolleyControllerImpl 的 checkTrolley() 有問題。");
        }
    }

    @Override
    public String add_delete_Trolley(Integer productId, HttpServletRequest req, HttpServletResponse resp) throws TrolleyControllerImplException {
        try {
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");

            if (user != null && "general".equals(user.getIdentity())) { // 若非 null 表示已經登入。
                boolean b = trolleyService.addTrolley(productId, user.getId());

                PrintWriter out = resp.getWriter();
                if (b == true) {
                    out.print("add_success");
                } else {
                    out.print("delete_success");
                }
                out.flush();
                out.close();
            } else {
                PrintWriter out = resp.getWriter();
                out.print("logIn"); // 尚未登入，請用戶進行登入操作。
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrolleyControllerImplException("TrolleyControllerImpl 的 add_delete_Trolley() 有問題。");
        }
        return "axios";
    }

    @Override
    public String plusQuantity(HttpServletRequest req, Integer productId, Integer currentQuantity) throws TrolleyControllerImplException {
        try {
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");

            trolleyService.plusQuantity(productId, user.getId(), currentQuantity);

            return getTrolleyByUserId(req);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrolleyControllerImplException("TrolleyControllerImpl 的 plusQuantity() 有問題。");
        }
    }

    @Override
    public String reduceQuantity(HttpServletRequest req, Integer productId, Integer currentQuantity) throws TrolleyControllerImplException {
        try {
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");

            trolleyService.reduceQuantity(productId, user.getId(), currentQuantity);

            return getTrolleyByUserId(req);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrolleyControllerImplException("TrolleyControllerImpl 的 reduceQuantity() 有問題。");
        }
    }

    @Override
    public String deleteTrolleyProduct(HttpServletRequest req, Integer productId) throws TrolleyControllerImplException {
        try {
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");

            trolleyService.deleteTrolleyProduct(productId, user.getId());

            return getTrolleyByUserId(req);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrolleyControllerImplException("TrolleyControllerImpl 的 deleteTrolleyProduct() 有問題。");
        }
    }

    @Override
    public String clearTrolley(HttpServletRequest req) throws TrolleyControllerImplException {
        try {
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");

            trolleyService.clearTrolley(user.getId());

            return getTrolleyByUserId(req);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrolleyControllerImplException("TrolleyControllerImpl 的 clearTrolley() 有問題。");
        }
    }

    @Override
    public String checkout(HttpServletRequest req) {
        return "checkout";
    }
}
