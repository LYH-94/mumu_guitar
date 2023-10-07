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

    /**
     * 獲取所有會員的訂單。
     *
     * @param req
     * @throws OrderServiceImplException
     */
    @Override
    public void getAllOrderList(HttpServletRequest req) throws OrderServiceImplException {
        try {
            HttpSession session = req.getSession();

            // 1.處理搜尋。
            String searchOrder = "";
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

            // 2.獲取使用者選擇的頁碼。
            int orderPageNumber;
            if (StringUtils.isEmpty(req.getParameter("orderPageNumber"))) {
                // 預設為第一頁。
                orderPageNumber = 1;
            } else {
                orderPageNumber = Integer.parseInt(req.getParameter("orderPageNumber"));
            }
            session.setAttribute("session_orderPageNumber", Integer.toString(orderPageNumber));

            // 調用 DAO。
            List<Order> allOrderList = orderDAO.getAllOrderList(ConnUtils.getConn(), Order.class, searchOrder, orderPageNumber);

            // 獲取各訂單的所屬會員並設置給 allOrderList 中的訂單。
            for (int i = 0; i < allOrderList.size(); i++) {
                allOrderList.get(i).setOwner(userController.getUserById(allOrderList.get(i).getOwner().getId()));
            }

            //  將獲取到的訂單儲存在 session 中。
            session.setAttribute("allOrderList", allOrderList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderServiceImplException("OrderServiceImpl 的 getAllOrderList() 有問題。");
        }
    }

    /**
     * 獲取特定會員所需的訂單。
     *
     * @param req
     * @throws OrderServiceImplException
     */
    @Override
    public void getUserOrderList(HttpServletRequest req) throws OrderServiceImplException {
        try {
            HttpSession session = req.getSession();

            // 1.處理搜尋。
            String searchOrder = "";
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

            // 2.獲取使用者選擇的頁碼。
            int orderPageNumber;
            if (StringUtils.isEmpty(req.getParameter("orderPageNumber"))) {
                // 預設為第一頁。
                orderPageNumber = 1;
            } else {
                orderPageNumber = Integer.parseInt(req.getParameter("orderPageNumber"));
            }
            session.setAttribute("session_orderPageNumber", Integer.toString(orderPageNumber));

            // 3.獲取特定會員所需的訂單。
            int userId = ((User) session.getAttribute("user")).getId();
            List<Order> userOrderList = orderDAO.getUserOrderList(ConnUtils.getConn(), Order.class, userId, searchOrder, orderPageNumber);

            //  將獲取到的訂單儲存在 session 中。
            session.setAttribute("userOrderList", userOrderList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderServiceImplException("OrderServiceImpl 的 getUserOrderList() 有問題。");
        }
    }

    /**
     * 獲取所有會員的訂單總數。
     *
     * @param req
     * @throws OrderServiceImplException
     */
    @Override
    public void getAllOrderCount(HttpServletRequest req) throws OrderServiceImplException {
        try {
            HttpSession session = req.getSession();

            // 1.處理搜尋。
            String searchOrder = "";
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

            // 2.獲取所有會員的訂單數量，用於計算訂單總頁數。
            int orderPages;
            int allOrderCount = orderDAO.getAllOrderCount(ConnUtils.getConn(), searchOrder);

            if (allOrderCount == 0) { // 10 筆訂單為一頁。
                orderPages = 1;
            } else if (allOrderCount % 10 != 0) {
                orderPages = (allOrderCount / 10) + 1;
            } else {
                orderPages = allOrderCount / 10;
            }

            req.getSession().setAttribute("allOrderCount", allOrderCount);
            req.getSession().setAttribute("orderPages", orderPages);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderServiceImplException("OrderServiceImpl 的 getAllOrderCount() 有問題。");
        }
    }

    /**
     * 獲取特定會員所需的訂單總數。
     *
     * @param req
     * @throws OrderServiceImplException
     */
    @Override
    public void getUserOrderCount(HttpServletRequest req) throws OrderServiceImplException {
        try {
            HttpSession session = req.getSession();

            // 1.處理搜尋。
            String searchOrder = "";
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

            // 2.獲取特定會員所需的訂單數量，用於計算訂單總頁數。
            int orderPages;
            int userId = ((User) session.getAttribute("user")).getId();
            int userOrderCount = orderDAO.getUserOrderCount(ConnUtils.getConn(), userId, searchOrder);

            // 每一頁顯示 10 筆訂單。
            if (userOrderCount == 0) {
                orderPages = 1;
            } else if (userOrderCount % 10 != 0) {
                orderPages = (userOrderCount / 10) + 1;
            } else {
                orderPages = userOrderCount / 10;
            }

            req.getSession().setAttribute("userOrderCount", userOrderCount);
            req.getSession().setAttribute("orderPages", orderPages);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderServiceImplException("OrderServiceImpl 的 getUserOrderCount() 有問題。");
        }
    }

    /**
     * 透過訂單編號獲取該訂單的詳細資訊。
     *
     * @param req
     * @param number
     * @throws OrderServiceImplException
     */
    @Override
    public void getOrderDetailByNumber(HttpServletRequest req, String number) throws OrderServiceImplException {
        try {
            // 透過訂單編號獲取訂單。
            Order order = orderDAO.getOrderByNumber(ConnUtils.getConn(), Order.class, number);

            // 獲取該訂單的 owner，並賦予給 order。
            User user = userController.getUserById(order.getOwner().getId());
            order.setOwner(user);

            // 透過訂單id獲取該訂單中的所有商品，並透過商品 id ( OrderProduct中的product.id ) 獲取商品。
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

    /**
     * 新增訂單至數據庫中。
     *
     * @param req
     * @param purchaser 購買人姓名。
     * @param phone     聯絡電話。
     * @param address   配送地址。
     * @throws OrderServiceImplException
     */
    @Override
    public void addOrder(HttpServletRequest req, String purchaser, String phone, String address) throws OrderServiceImplException {
        try {
            HttpSession session = req.getSession();

            // 設置訂單需要的資料。(1.訂單編號 2.下單日期 3.訂單總額 4.發貨狀態 5.所屬用戶 6.購買人 7.連絡電話 8.付款方式(固定的不用設置) 9.配送地址)
            // 1.下單日期-獲取當前時間。
            Date date = new Date();

            // 2.訂單編號-獲取將當前時間轉成一整個字串並加上四位隨機數。
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
            String number = sdf.format(date);

            // 生成四位隨機數。
            Random random = new Random();

            int min = 1000; // 最小值 1000 (包括 1000)。
            int max = 9999; // 最大值 9999（包括 9999）。
            String randomFourNumber = String.valueOf(random.nextInt(max - min + 1) + min);

            // 字串拼接
            number = number + randomFourNumber;

            // 3.訂單總額
            TrolleyClass trolleyClass = (TrolleyClass) session.getAttribute("trolleyClass");
            Integer totalAmount = trolleyClass.getTotalAmount();

            // 4.發貨狀態 ( 預設為 0 )

            // 5.所屬用戶
            User user = (User) session.getAttribute("user");
            Integer owner = user.getId();

            // 6.購買人姓名 - purchaser
            // 7.連絡電話 - phone

            // 8.付款方式(固定的不用設置)
            // 9.配送地址 - address

            // 調用 DAO，於 t_order 數據表中新增訂單。
            orderDAO.addOrder(ConnUtils.getConn(), number, date, totalAmount, owner, purchaser, phone, address);

            // 於 t_orderProduct 數據表中新增該訂單的商品內容。
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

    /**
     * 切換訂單的狀態。
     *
     * @param status 訂單當前狀態。
     * @param number 訂單編號。
     * @throws OrderServiceImplException
     */
    @Override
    public void switchStatus(Integer status, String number) throws OrderServiceImplException {
        try {
            // 切換狀態。
            if (status == 0) {
                status = 1;
            } else if (status == 1) {
                status = 2;
            }

            // 調用 DAO。
            orderDAO.switchStatus(ConnUtils.getConn(), status, number);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderServiceImplException("OrderServiceImpl 的 switchStatus() 有問題。");
        }
    }
}