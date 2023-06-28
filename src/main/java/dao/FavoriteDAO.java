package dao;

import pojo.Favorite;
import util.ConnUtils;

import java.sql.Connection;
import java.util.List;

public interface FavoriteDAO {
    public List<Favorite> getFavoriteByUserId(Connection conn, Class<Favorite> clazz, int userId);
}
