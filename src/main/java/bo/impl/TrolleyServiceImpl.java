package bo.impl;

import bo.TrolleyService;
import bo.exception.FavoriteServiceImplException;
import bo.exception.TrolleyServiceImplException;
import dao.TrolleyDAO;
import dao.impl.TrolleyDAOImpl;
import pojo.Favorite;
import pojo.Trolley;
import util.ConnUtils;

import java.util.List;

public class TrolleyServiceImpl implements TrolleyService {

    TrolleyDAOImpl trolleyDAO = null;

    @Override
    public List<Trolley> getTrolleyByUserId(int userId) throws TrolleyServiceImplException {
        try {
            return trolleyDAO.getTrolleyByUserId(ConnUtils.getConn(), Trolley.class, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrolleyServiceImplException("TrolleyServiceImpl 的 getTrolleyByUserId() 有問題。");
        }
    }

    @Override
    public boolean addTrolley(Integer productId, Integer userId) throws TrolleyServiceImplException {
        try {
            // 先判斷該用戶的購物車中是否已經有相同的商品。
            boolean b = trolleyDAO.checkForDuplicateUsers(ConnUtils.getConn(), Trolley.class, productId, userId);
            if (b) { // 若為不重複為 true，重複則為 false。*/
                // 不重複則可以進行添加操作。
                return trolleyDAO.addTrolley(ConnUtils.getConn(), productId, userId);
            } else {
                // 若重複則進行刪除操作。
                boolean b1 = trolleyDAO.deleteTrolley(ConnUtils.getConn(), productId, userId);
                return b;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrolleyServiceImplException("TrolleyServiceImpl 的 addTrolley() 有問題。");
        }
    }
}
