package controllers.impl;

import bo.impl.OrderServiceImpl;
import controllers.OrderController;
import controllers.exception.OrderControllerImplException;

import javax.servlet.http.HttpServletRequest;

public class OrderControllerImpl implements OrderController {

    private OrderServiceImpl orderService = null;
    private TrolleyControllerImpl trolleyController = null;

    /**
     * 獲取所有會員的訂單。
     *
     * @param req
     * @return
     * @throws OrderControllerImplException
     */
    @Override
    public String getAllOrderList(HttpServletRequest req) throws OrderControllerImplException {
        try {
            // 獲取所有用戶的訂單。
            orderService.getAllOrderList(req);

            // 獲取所有用戶的訂單總數。
            orderService.getAllOrderCount(req);

            return "manager_order";
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderControllerImplException("OrderControllerImpl 的 getAllOrderList() 有問題。");
        }
    }

    /**
     * 獲取指定會員的所有訂單。
     *
     * @param req
     * @return
     * @throws OrderControllerImplException
     */
    @Override
    public String getUserOrderList(HttpServletRequest req) throws OrderControllerImplException {
        try {
            // 獲取特定用戶所需的訂單。
            orderService.getUserOrderList(req);

            // 獲取特定用戶所需的訂單數量。
            orderService.getUserOrderCount(req);

            return "member_myOrderList";
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderControllerImplException("OrderControllerImpl 的 getUserOrderList() 有問題。");
        }
    }

    /**
     * 透過訂單編號獲取訂單詳情。
     *
     * @param req
     * @param number 訂單編號。
     * @return
     * @throws OrderControllerImplException
     */
    @Override
    public String getOrderDetailByNumber(HttpServletRequest req, String number) throws OrderControllerImplException {
        try {
            // 透過訂單編號獲取該訂單的詳細資訊。
            orderService.getOrderDetailByNumber(req, number);

            return "orderDetail";
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderControllerImplException("OrderControllerImpl 的 getOrderDetailByNumber() 有問題。");
        }
    }

    /**
     * 新增訂單。
     *
     * @param req
     * @param purchaser 購買人姓名。
     * @param phone     聯絡電話
     * @param address   配送地址。
     * @return
     * @throws OrderControllerImplException
     */
    @Override
    public String addOrder(HttpServletRequest req, String purchaser, String phone, String address) throws OrderControllerImplException {
        try {
            // 新增訂單到數據庫中。
            orderService.addOrder(req, purchaser, phone, address);

            // 清空購物車。
            trolleyController.clearTrolley(req);

            return getUserOrderList(req);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderControllerImplException("OrderControllerImpl 的 addOrder() 有問題。");
        }
    }

    /**
     * 切換訂單狀態。
     *
     * @param req
     * @param status 當前訂單狀態。
     * @param number 訂單編號。
     * @return
     * @throws OrderControllerImplException
     */
    @Override
    public String switchStatus(HttpServletRequest req, Integer status, String number) throws OrderControllerImplException {
        try {
            orderService.switchStatus(status, number);

            return getAllOrderList(req);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderControllerImplException("OrderControllerImpl 的 switchStatus() 有問題。");
        }
    }
}