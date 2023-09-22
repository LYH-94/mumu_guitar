package bo.impl;

import bo.OrderService;
import bo.exception.OrderServiceImplException;
import controllers.impl.ProductControllerImpl;
import controllers.impl.UserControllerImpl;
import dao.impl.OrderDAOImpl;
import pojo.*;
import util.ConnUtils;
import util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

            // 獲取用戶選擇的頁碼。
            int orderPageNumber; // 預設為第一頁。
            if (StringUtils.isEmpty(req.getParameter("orderPageNumber"))) { // 預設為 1。
                orderPageNumber = 1;
            } else {
                orderPageNumber = Integer.parseInt(req.getParameter("orderPageNumber"));
            }
            session.setAttribute("session_orderPageNumber", Integer.toString(orderPageNumber));

            // 獲取特定用戶所需的訂單。
            int userId = ((User) session.getAttribute("user")).getId();
            List<Order> userOrderList = orderDAO.getUserOrderList(ConnUtils.getConn(), Order.class, userId, searchOrder, orderPageNumber);

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

            // 獲取特定用戶所需的訂單數量，用於計算訂單總頁數。
            int orderPages;
            int userId = ((User) session.getAttribute("user")).getId();
            int userOrderCount = orderDAO.getUserOrderCount(ConnUtils.getConn(), userId, searchOrder);

            if (userOrderCount == 0) { // 10 筆訂單為一頁。
                orderPages = 1;
            } else if (userOrderCount % 10 != 0) {
                orderPages = (userOrderCount / 10) + 1;
            } else {
                orderPages = userOrderCount / 10;
            }

            req.getSession().setAttribute("userOrderCount", userOrderCount); // 將訂單總數儲存於 session。
            req.getSession().setAttribute("orderPages", orderPages); // 根據訂單總數計算的總頁數，儲存於 session。

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

    @Override
    public void addOrder(HttpServletRequest req, String purchaser, String phone, String address) throws OrderServiceImplException {
        try {
            HttpSession session = req.getSession();

            // 設置訂單需要的資料。(1.訂單編號 2.下單日期 3.訂單總額 4.發貨狀態 5.所屬用戶 6.購買人 7.連絡電話 8.付款方式(固定的不用設置) 9.配送地址)
            // 1. 下單日期-獲取當前時間。
            Date date = new Date();

            // 2. 訂單編號-獲取將當前時間轉成一整個字串並加上四位隨機數。
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
            String number = sdf.format(date);

            // 生成四位隨機數。
            Random random = new Random();

            int min = 1000; // 最小值 1000 (包括 1000)。
            int max = 9999; // 最大值 9999（包括 9999）
            String randomFourNumber = String.valueOf(random.nextInt(max - min + 1) + min);

            // 字串拼接
            number = number + randomFourNumber;

            // 3. 訂單總額
            TrolleyClass trolleyClass = (TrolleyClass) session.getAttribute("trolleyClass");
            Integer totalAmount = trolleyClass.getTotalAmount();

            // 4.發貨狀態(預設為 0)

            // 5. 所屬用戶
            User user = (User) session.getAttribute("user");
            Integer owner = user.getId();

            // 6.購買人姓名 - purchaser
            // 7.連絡電話 - phone

            // 8.付款方式(固定的不用設置)
            // 9.配送地址 - address

            // 於t_order數據表中新增訂單。
            orderDAO.addOrder(ConnUtils.getConn(), number, date, totalAmount, owner, purchaser, phone, address);

            // 於t_orderProduct數據表中新增該訂單的商品內容。
            // 獲取訂單 id。
            Order order = orderDAO.getOrderByNumber(ConnUtils.getConn(), Order.class, number);
            Integer belongOrder = order.getId();

            for (int i = 0; i < trolleyClass.getProduct().size(); ++i) {
                orderDAO.addOrderProduct(ConnUtils.getConn(), trolleyClass.getProduct().get(i).getId(), trolleyClass.getQuantity().get(i), belongOrder);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderServiceImplException("OrderServiceImpl 的 addOrder() 有問題。");
        }
    }
}