package bo;

import pojo.Favorite;

import java.util.List;

public interface FavoriteService {
    // 獲取指定用戶的Favorite列表。注意，Favorite中只紀錄產品的ID而已。
    public List<Favorite> getFavoriteByUserId(int userId);
}
