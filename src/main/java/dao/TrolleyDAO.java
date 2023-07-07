package dao;

import pojo.Favorite;
import pojo.Trolley;

import java.sql.Connection;
import java.util.List;

public interface TrolleyDAO {
    public List<Trolley> getTrolleyByUserId(Connection conn, Class<Trolley> clazz, int userId);

    // 獲取指定用戶的指定商品數據。
    public boolean checkForDuplicateUsers(Connection conn, Class<Trolley> clazz, int productId, int userId);

    public boolean addTrolley(Connection conn, Integer productId, Integer userId);

    public boolean deleteTrolley(Connection conn, Integer productId, Integer userId);
}
