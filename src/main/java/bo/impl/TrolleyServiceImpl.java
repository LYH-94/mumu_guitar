package bo.impl;

import bo.TrolleyService;
import bo.exception.TrolleyServiceImplException;
import dao.impl.TrolleyDAOImpl;
import pojo.Trolley;
import util.ConnUtils;

import java.util.List;

public class TrolleyServiceImpl implements TrolleyService {

    TrolleyDAOImpl trolleyDAO = null;

    /**
     * 獲取指定會員的 Trolley 列表。注意，Trolley 中只紀錄產品的 ID 而已。
     *
     * @param userId 會員 id。
     * @return
     * @throws TrolleyServiceImplException
     */
    @Override
    public List<Trolley> getTrolleyByUserId(int userId) throws TrolleyServiceImplException {
        try {
            return trolleyDAO.getTrolleyByUserId(ConnUtils.getConn(), Trolley.class, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrolleyServiceImplException("TrolleyServiceImpl 的 getTrolleyByUserId() 有問題。");
        }
    }

    /**
     * 添加商品到指定會員的購物車中。
     *
     * @param productId 商品 id。
     * @param userId    會員 id。
     * @return
     * @throws TrolleyServiceImplException
     */
    @Override
    public boolean addTrolley(Integer productId, Integer userId) throws TrolleyServiceImplException {
        try {
            // 先判斷該用戶的購物車中是否已經有相同的商品。
            // 若不重複則返回為 true，重複則返回 false。
            boolean b = trolleyDAO.checkTrolley(ConnUtils.getConn(), Trolley.class, productId, userId);

            if (b) {
                // 若重複則進行刪除操作。
                trolleyDAO.deleteTrolley(ConnUtils.getConn(), productId, userId);
                return b;
            } else {
                // 不重複則進行添加操作。
                return trolleyDAO.addTrolley(ConnUtils.getConn(), productId, userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrolleyServiceImplException("TrolleyServiceImpl 的 addTrolley() 有問題。");
        }
    }

    /**
     * 檢查指定會員的購物車中是否已存在指定商品。
     *
     * @param productId 商品 id。
     * @param userId    會員 id。
     * @return
     * @throws TrolleyServiceImplException
     */
    @Override
    public boolean checkTrolley(Integer productId, Integer userId) throws TrolleyServiceImplException {
        try {
            return trolleyDAO.checkTrolley(ConnUtils.getConn(), Trolley.class, productId, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrolleyServiceImplException("TrolleyServiceImpl 的 checkTrolley() 有問題。");
        }
    }

    /**
     * 將購物車中指定商品的數量加一。
     *
     * @param productId       商品 id。
     * @param currentQuantity 該商品的當前數量。
     * @throws TrolleyServiceImplException
     */
    @Override
    public void plusQuantity(Integer productId, Integer userId, Integer currentQuantity) throws TrolleyServiceImplException {
        try {
            boolean b = trolleyDAO.plusQuantity(ConnUtils.getConn(), productId, userId, currentQuantity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrolleyServiceImplException("TrolleyServiceImpl 的 plusQuantity() 有問題。");
        }
    }

    /**
     * 將購物車中指定商品的數量減一。
     *
     * @param productId       商品 id。
     * @param currentQuantity 該商品的當前數量。
     * @throws TrolleyServiceImplException
     */
    @Override
    public void reduceQuantity(Integer productId, Integer userId, Integer currentQuantity) throws TrolleyServiceImplException {
        try {
            trolleyDAO.reduceQuantity(ConnUtils.getConn(), productId, userId, currentQuantity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrolleyServiceImplException("TrolleyServiceImpl 的 reduceQuantity() 有問題。");
        }
    }

    /**
     * 刪除購物車中的指定商品。
     *
     * @param productId 商品 id。
     * @param userId    會員 id。
     * @throws TrolleyServiceImplException
     */
    @Override
    public void deleteTrolleyProduct(Integer productId, Integer userId) throws TrolleyServiceImplException {
        try {
            trolleyDAO.deleteTrolley(ConnUtils.getConn(), productId, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrolleyServiceImplException("TrolleyServiceImpl 的 deleteTrolleyProduct() 有問題。");
        }
    }

    /**
     * 清空購物車中的商品。
     *
     * @param userId 會員 id。
     * @throws TrolleyServiceImplException
     */
    @Override
    public void clearTrolley(Integer userId) throws TrolleyServiceImplException {
        try {
            trolleyDAO.clearTrolley(ConnUtils.getConn(), userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrolleyServiceImplException("TrolleyServiceImpl 的 clearTrolley() 有問題。");
        }
    }
}