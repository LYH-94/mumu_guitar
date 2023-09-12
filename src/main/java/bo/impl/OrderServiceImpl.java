package bo.impl;

import bo.OrderService;
import bo.exception.OrderServiceImplException;
import controllers.impl.ProductControllerImpl;
import controllers.impl.UserControllerImpl;
import dao.impl.OrderDAOImpl;
import pojo.Order;
import pojo.OrderProduct;
import pojo.Product;
import pojo.User;
import util.ConnUtils;
import util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    private OrderDAOImpl orderDAO = null;
    private UserControllerImpl userController = null;
    private ProductControllerImpl productController = null;

    @Override
    public List<Order> getAllOrder() throws OrderServiceImplException {
        try {
            return orderDAO.getAllOrder(ConnUtils.getConn(), Order.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderServiceImplException("OrderServiceImpl 的 getAllOrder() 有問題。");
        }
    }

    @Override
    public void getUserOrderList(HttpServletRequest req) throws OrderServiceImplException {
        try {
            // 處理搜尋。
            String searchOrder = "";
            HttpSession session = req.getSession();
            String session_searchOrder = (String) session.getAttribute("session_searchOrder");
            if (StringUtils.isEmpty(req.getParameter("searchOrder"))) {
                if (StringUtils.isEmpty(session_searchOrder)) {
                    searchOrder = "";
                } else {
                    searchOrder = session_searchOrder;
                }
            } else if ("reset".equals(req.getParameter("searchOrder"))) {
                searchOrder = "";
            } else {
                searchOrder = req.getParameter("searchOrder");
            }
            session.setAttribute("session_searchOrder", searchOrder);

            // 獲取特定用戶所需的訂單。
            int userId = ((User) session.getAttribute("user")).getId();
            List<Order> userOrderList = orderDAO.getUserOrderList(ConnUtils.getConn(), Order.class, userId, searchOrder);

            //  將獲取到的訂單儲存在session中。
            session.setAttribute("userOrderList", userOrderList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderServiceImplException("OrderServiceImpl 的 getUserOrderList() 有問題。");
        }
    }

    @Override
    public void getUserOrderCount(HttpServletRequest req) throws OrderServiceImplException {
        try {
            // 處理搜尋。
            String searchOrder = "";
            HttpSession session = req.getSession();
            String session_searchOrder = (String) session.getAttribute("session_searchOrder");
            if (StringUtils.isEmpty(req.getParameter("searchOrder"))) {
                if (StringUtils.isEmpty(session_searchOrder)) {
                    searchOrder = "";
                } else {
                    searchOrder = session_searchOrder;
                }
            } else if ("reset".equals(req.getParameter("searchOrder"))) {
                searchOrder = "";
            } else {
                searchOrder = req.getParameter("searchOrder");
            }
            session.setAttribute("session_searchOrder", searchOrder);

            // 獲取特定用戶所需的訂單數量。
            int userId = ((User) session.getAttribute("user")).getId();
            int userOrderCount = orderDAO.getUserOrderCount(ConnUtils.getConn(), userId, searchOrder);

            // 處理分頁。

            req.getSession().setAttribute("userOrderCount", userOrderCount);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderServiceImplException("OrderServiceImpl 的 getUserOrderCount() 有問題。");
        }
    }

    @Override
    public void getOrderDetailByNumber(HttpServletRequest req, String number) throws OrderServiceImplException {
        try {
            // 透過訂單編號獲取訂單。
            Order order = orderDAO.getOrderByNumber(ConnUtils.getConn(), Order.class, number);

            // 獲取該訂單的 owner，並賦予給 order。
            User user = userController.getUserById(order.getOwner().getId());
            order.setOwner(user);

            // 透過訂單id獲取該訂單中的所有商品，並透過商品id(OrderProduct中的product.id)獲取商品。
            List<OrderProduct> productList = productController.getProductByOrderId(order.getId());
            for (int i = 0; i < productList.size(); ++i) {
                Product product = productController.getProductById(productList.get(i).getProduct().getId());
                productList.get(i).setProduct(product);

                // 計算小計。
                Integer subTotal = product.getPrice() * productList.get(i).getQuantity();
                productList.get(i).setSubTotal(subTotal);
            }
            order.setOrderProductList(productList);

            HttpSession session = req.getSession();
            session.setAttribute("order", order);

        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderServiceImplException("OrderServiceImpl 的 getOrderDetailByNumber() 有問題。");
        }
    }
}
