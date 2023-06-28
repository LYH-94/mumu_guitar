package dao;

import pojo.Trolley;

import java.sql.Connection;
import java.util.List;

public interface TrolleyDAO {
    public List<Trolley> getTrolleyByUserId(Connection conn, Class<Trolley> clazz, int userId);
}
