package bo;

import pojo.Favorite;
import pojo.Trolley;

import java.util.List;

public interface TrolleyService {
    // 獲取指定用戶的Trolley列表。注意，Trolley中只紀錄產品的ID而已。
    public List<Trolley> getTrolleyByUserId(int userId);

    // 添加商品到指定用戶的購物車中。
    public boolean addTrolley(Integer productId, Integer userId);

    public boolean checkTrolley(Integer productId, Integer userId);
}
