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

    /**
     * 透過會員 id 獲取該會員的購物車商品。
     *
     * @param req
     * @return
     * @throws TrolleyControllerImplException
     */
    @Override
    public String getTrolleyByUserId(HttpServletRequest req) throws TrolleyControllerImplException {
        try {
            TrolleyClass trolleyClass = new TrolleyClass();
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");

            // 判斷是否已經登入且是一般用戶。
            if (user != null && "general".equals(user.getIdentity())) {
                int userId = user.getId();

                // 獲取該用戶的 trolley。
                List<Trolley> trolleyList = trolleyService.getTrolleyByUserId(userId);

                // 獲取每個 trolley 中的 product 並賦予給 trolley。
                Integer totalQuantity = 0;
                Integer totalAmount = 0;
                for (int i = 0; i < trolleyList.size(); i++) {
                    Product product = productController.getProductById(trolleyList.get(i).getProduct().getId());
                    if (product != null) {
                        Integer quantity = trolleyList.get(i).getQuantity();
                        Integer subTotal = quantity * product.getPrice();

                        // 添加商品、商品數量、小計
                        trolleyClass.getProduct().add(product);
                        trolleyClass.getQuantity().add(quantity);
                        trolleyClass.getSubTotal().add(subTotal);

                        totalQuantity = totalQuantity + quantity;
                        totalAmount = totalAmount + subTotal;
                    }
                }

                // 設置商品總數量、總金額。
                trolleyClass.setTotalQuantity(totalQuantity);
                trolleyClass.setTotalAmount(totalAmount);
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

    /**
     * 檢查指定會員的購物車中是否已存在指定商品。
     *
     * @param productId 商品 id。
     * @param userId    會員 id。
     * @return
     * @throws TrolleyControllerImplException
     */
    @Override
    public boolean checkTrolley(Integer productId, Integer userId) throws TrolleyControllerImplException {
        try {
            return trolleyService.checkTrolley(productId, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrolleyControllerImplException("TrolleyControllerImpl 的 checkTrolley() 有問題。");
        }
    }

    /**
     * 新增或刪除購物車中的商品。
     *
     * @param productId 商品 id。
     * @param req
     * @param resp
     * @return
     * @throws TrolleyControllerImplException
     */
    @Override
    public String add_delete_Trolley(Integer productId, HttpServletRequest req, HttpServletResponse resp) throws TrolleyControllerImplException {
        try {
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");

            // 若非 null 表示已經登入。
            if (user != null && "general".equals(user.getIdentity())) {
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

    /**
     * 將購物車中指定商品的數量加一。
     *
     * @param req
     * @param productId       商品 id。
     * @param currentQuantity 該商品的當前數量。
     * @return
     * @throws TrolleyControllerImplException
     */
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

    /**
     * 將購物車中指定商品的數量減一。
     *
     * @param req
     * @param productId       商品 id。
     * @param currentQuantity 該商品的當前數量。
     * @return
     * @throws TrolleyControllerImplException
     */
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

    /**
     * 刪除購物車中的指定商品。
     *
     * @param req
     * @param productId 商品 id。
     * @return
     * @throws TrolleyControllerImplException
     */
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

    /**
     * 清空購物車中的商品。
     *
     * @param req
     * @return
     * @throws TrolleyControllerImplException
     */
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

    /**
     * 結帳按鈕被點擊時，跳轉至結帳頁。
     *
     * @param req
     * @return
     */
    @Override
    public String checkout(HttpServletRequest req) {
        return "checkout";
    }
}