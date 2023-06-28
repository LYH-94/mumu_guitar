package bo;

import pojo.Favorite;
import pojo.Trolley;

import java.util.List;

public interface TrolleyService {
    // 獲取指定用戶的Trolley列表。注意，Trolley中只紀錄產品的ID而已。
    public List<Trolley> getTrolleyByUserId(int userId);
}
