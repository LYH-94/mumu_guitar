package bo;

import pojo.Favorite;
import pojo.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface FavoriteService {
    // 獲取指定用戶追蹤的商品。
    public List<Product> getFilteredFavoriteProductByUserId(HttpServletRequest req, int userId);

    public int getFilteredFavoriteProductCountByUserId(HttpServletRequest req, int userId);

    // 添加商品到指定用戶的商品追蹤表中。
    public boolean addFavorite(Integer productId, Integer userId);

    public boolean checkFavorite(Integer productId, Integer userId);
}
