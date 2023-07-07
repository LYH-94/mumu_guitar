package bo;

import pojo.Favorite;

import java.util.List;

public interface FavoriteService {
    // 獲取指定用戶的Favorite列表。注意，Favorite中只紀錄產品的ID而已。
    public List<Favorite> getFavoriteByUserId(int userId);

    // 添加商品到指定用戶的商品追蹤表中。
    public boolean addFavorite(Integer productId, Integer userId);
}
