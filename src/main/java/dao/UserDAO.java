package dao;

import pojo.User;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public interface UserDAO {
    public User loginVerification(Connection conn, Class<User> clazz, String account, String password);

    public User getUserById(Connection conn, Class<User> clazz, int id);

    public boolean register(Connection conn, String account, String password, String username, String gender, LocalDate birthday, String phone, String email);

    public boolean updatePersonalInfo(Connection conn, String account, String password, String username, String gender, LocalDate birthday, String phone, String email);

    public boolean checkForDuplicateUsers(Connection conn, Class<User> clazz, String account);

    public List<User> getAllMemberList(Connection conn, Class<User> clazz, String searchMember, int memberPageNumber);

    public int getAllMemberCount(Connection conn, String searchMember);

    public void switchStatus(Connection conn, int userId, String status);
}