package dao;

import pojo.Favorite;
import pojo.Trolley;

import java.sql.Connection;
import java.util.List;

public interface TrolleyDAO {
    public List<Trolley> getTrolleyByUserId(Connection conn, Class<Trolley> clazz, int userId);

    // 獲取指定用戶的指定商品數據。
    public boolean checkForDuplicateUsers(Connection conn, Class<Trolley> clazz, int productId, int userId);

    // 檢查指定用戶是否將該商品添加到購物車。
    public boolean checkTrolley(Connection conn, Class<Trolley> clazz, Integer productId, Integer userId);

    public boolean addTrolley(Connection conn, Integer productId, Integer userId);

    public boolean deleteTrolley(Connection conn, Integer productId, Integer userId);
    public boolean clearTrolley(Connection conn, Integer userId);

    public boolean plusQuantity(Connection conn, Integer productId, Integer userId, Integer currentQuantity);
    public boolean reduceQuantity(Connection conn, Integer productId, Integer userId, Integer currentQuantity);
}
