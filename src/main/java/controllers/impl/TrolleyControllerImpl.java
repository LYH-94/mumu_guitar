package controllers.impl;

import bo.impl.TrolleyServiceImpl;
import controllers.TrolleyController;
import controllers.exception.TrolleyControllerImplException;
import pojo.Product;
import pojo.Trolley;
import pojo.TrolleyClass;
import pojo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class TrolleyControllerImpl implements TrolleyController {

    private TrolleyServiceImpl trolleyService = null;

    private ProductControllerImpl productController = null;

    private TrolleyClass trolleyClass = null;

    @Override
    public String getTrolleyByUserId(HttpServletRequest req) throws TrolleyControllerImplException {
        try {

            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");

            if (user != null && "general".equals(user.getIdentity())) { // 判斷是否已經登入且是一般用戶。
                int userId = user.getId();

                // 獲取該用戶的 trolley。
                List<Trolley> trolleyList = trolleyService.getTrolleyByUserId(userId);

                // 獲取每個 trolley 中的 product 並賦予給 trolley。
                Integer totalQuantity = 0;
                Integer totalAmount = 0;
                trolleyClass.getProduct().clear();
                trolleyClass.getQuantity().clear();
                trolleyClass.getSubTotal().clear();
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
}
