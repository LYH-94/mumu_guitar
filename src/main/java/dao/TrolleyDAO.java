package dao;

import pojo.Trolley;

import java.sql.Connection;
import java.util.List;

public interface TrolleyDAO {
    public List<Trolley> getTrolleyByUserId(Connection conn, Class<Trolley> clazz, int userId);

    public boolean checkTrolley(Connection conn, Class<Trolley> clazz, Integer productId, Integer userId);

    public boolean addTrolley(Connection conn, Integer productId, Integer userId);

    public boolean deleteTrolley(Connection conn, Integer productId, Integer userId);

    public boolean clearTrolley(Connection conn, Integer userId);

    public boolean plusQuantity(Connection conn, Integer productId, Integer userId, Integer currentQuantity);

    public boolean reduceQuantity(Connection conn, Integer productId, Integer userId, Integer currentQuantity);
}