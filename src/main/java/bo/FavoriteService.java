package bo;

import pojo.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface FavoriteService {
    public List<Product> getFilteredFavoriteProductByUserId(HttpServletRequest req, int userId);

    public int getFilteredFavoriteProductCountByUserId(HttpServletRequest req, int userId);

    public boolean addFavorite(Integer productId, Integer userId);

    public boolean checkFavorite(Integer productId, Integer userId);
}