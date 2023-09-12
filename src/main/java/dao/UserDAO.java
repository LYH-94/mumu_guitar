package dao;

import pojo.User;

import java.sql.Connection;
import java.time.LocalDate;

public interface UserDAO {
    // 登入驗證。
    public User loginVerification(Connection conn, Class<User> clazz, String account, String password);

    // 通過 id 獲取 User 物件。
    public User getUserById(Connection conn, Class<User> clazz, int id);

    // 註冊會員。
    public boolean register(Connection conn, String account, String password, String username, String gender, LocalDate birthday, String phone, String email);

    // 更新用戶資料。
    public boolean updatePersonalInfo(Connection conn, String account, String password, String username, String gender, LocalDate birthday, String phone, String email);

    // 檢查重複的用戶。
    public boolean checkForDuplicateUsers(Connection conn, Class<User> clazz, String account);
}
