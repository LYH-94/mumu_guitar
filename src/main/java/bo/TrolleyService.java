package bo;

import pojo.Trolley;

import java.util.List;

public interface TrolleyService {
    public List<Trolley> getTrolleyByUserId(int userId);

    public boolean addTrolley(Integer productId, Integer userId);

    public boolean checkTrolley(Integer productId, Integer userId);

    public void plusQuantity(Integer productId, Integer userId, Integer currentQuantity);

    public void reduceQuantity(Integer productId, Integer userId, Integer currentQuantity);

    public void deleteTrolleyProduct(Integer productId, Integer userId);

    public void clearTrolley(Integer userId);
}