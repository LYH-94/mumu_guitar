package dao.impl;

import dao.BaseDAO;
import dao.UserDAO;
import dao.exception.UserDAOImplException;
import pojo.User;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class UserDAOImpl extends BaseDAO implements UserDAO {
    /**
     * 登入驗證。
     *
     * @param conn     JDBC 物件。
     * @param clazz    Class 類。
     * @param account  帳號。
     * @param password 密碼。
     * @return 返回該會員的 user 物件。
     * @throws UserDAOImplException
     */
    @Override
    public User loginVerification(Connection conn, Class<User> clazz, String account, String password) throws UserDAOImplException {
        try {
            String sql = "SELECT * FROM t_user WHERE account COLLATE utf8mb4_bin = ? AND password COLLATE utf8mb4_bin = ?";
            return super.getInstance(conn, clazz, sql, account, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserDAOImplException("UserDAOImpl 的 loginVerification() 有問題。");
        }
    }

    /**
     * 透過 id 獲取 user。
     *
     * @param conn  JDBC 物件。
     * @param clazz Class 類。
     * @param id    使用者 id。
     * @return 返回指定 id 的 User 物件。
     * @throws UserDAOImplException
     */
    @Override
    public User getUserById(Connection conn, Class<User> clazz, int id) throws UserDAOImplException {
        try {
            String sql = "SELECT * FROM t_user WHERE id = ?";
            return super.getInstance(conn, clazz, sql, id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserDAOImplException("UserDAOImpl 的 getUserById() 有問題。");
        }
    }

    /**
     * 註冊會員。
     *
     * @param conn     JDBC 物件。
     * @param account  帳號。
     * @param password 密碼。
     * @param username 使用者名稱。
     * @param gender   性別。
     * @param birthday 生日。
     * @param phone    聯絡電話。
     * @param email    電子信箱。
     * @return
     * @throws UserDAOImplException
     */
    @Override
    public boolean register(Connection conn, String account, String password, String username, String gender, LocalDate birthday, String phone, String email) throws UserDAOImplException {
        try {
            String sql = "INSERT INTO t_user(account,password,username,gender,birthday,phone,email) VALUES(?,?,?,?,?,?,?)";
            return super.update(conn, sql, account, password, username, gender, birthday, phone, email);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserDAOImplException("UserDAOImpl 的 register() 有問題。");
        }
    }

    /**
     * 用於更新會員資料。
     *
     * @param conn     JDBC 物件。
     * @param account  帳號。
     * @param password 密碼。
     * @param username 使用者名稱。
     * @param gender   性別。
     * @param birthday 生日。
     * @param phone    聯絡電話。
     * @param email    電子信箱。
     * @return
     * @throws UserDAOImplException
     */
    @Override
    public boolean updatePersonalInfo(Connection conn, String account, String password, String username, String gender, LocalDate birthday, String phone, String email) throws UserDAOImplException {
        try {
            String sql = "UPDATE t_user SET password = ?, username = ?, gender = ?, birthday = ?, phone = ?, email = ? WHERE account = ?";
            return super.update(conn, sql, password, username, gender, birthday, phone, email, account);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserDAOImplException("UserDAOImpl 的 updatPersonalInfo() 有問題。");
        }
    }

    /**
     * 檢查是否有重複的會員。
     *
     * @param conn    JDBC 物件。
     * @param clazz   Class 類。
     * @param account 帳號。
     * @return
     * @throws UserDAOImplException
     */
    @Override
    public boolean checkForDuplicateUsers(Connection conn, Class<User> clazz, String account) throws UserDAOImplException {
        try {
            String sql = "SELECT * FROM t_user WHERE account = ?";
            User user = super.getInstance(conn, clazz, sql, account);

            // 若不重複為 true，重複則為 false。
            return (user == null ? true : false);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserDAOImplException("UserDAOImpl 的 checkForDuplicateUsers() 有問題。");
        }
    }

    /**
     * 獲取所有會員資料。
     *
     * @param conn             JDBC 物件。
     * @param clazz            Class 類。
     * @param searchMember     搜尋的會員。
     * @param memberPageNumber 點選的頁數。
     * @return 返回符合條件的 User 物件。
     * @throws UserDAOImplException
     */
    @Override
    public List<User> getAllMemberList(Connection conn, Class<User> clazz, String searchMember, int memberPageNumber) throws UserDAOImplException {
        try {
            // 計算分頁偏移量。
            int offset = (memberPageNumber - 1) * 10;

            // 在 SQL 語句中，通配符不能直接給 "" 這樣的空字串，因此處理成模糊匹配的寫法，
            // "%%" 效果可視為與指定字段中的數據皆匹配。
            searchMember = "%" + searchMember + "%";

            String sql = "SELECT * FROM t_user WHERE account LIKE ? ORDER BY account ASC LIMIT 10 OFFSET ?";
            return super.getForList(conn, clazz, sql, searchMember, offset);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserDAOImplException("UserDAOImpl 的 getAllMemberList() 有問題。");
        }
    }

    /**
     * 獲取所有會員資料的數量。
     *
     * @param conn         JDBC 物件。
     * @param searchMember 搜尋的會員。
     * @return 返回會員資料的數量。
     * @throws UserDAOImplException
     */
    @Override
    public int getAllMemberCount(Connection conn, String searchMember) throws UserDAOImplException {
        try {
            // 在 SQL 語句中，通配符不能直接給 "" 這樣的空字串，因此處理成模糊匹配的寫法，"%%" 效果可視為與指定字段中的數據皆匹配。
            searchMember = "%" + searchMember + "%";

            String sql = "SELECT COUNT(id) FROM t_user WHERE account LIKE ?";
            return super.getCount(conn, sql, searchMember);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserDAOImplException("UserDAOImpl 的 getAllMemberCount() 有問題。");
        }
    }

    /**
     * 切換會員的狀態。(正常/停權)
     *
     * @param conn   JDBC 物件。
     * @param userId 使用者 id。
     * @param status 會員當前狀態。
     * @throws UserDAOImplException
     */
    @Override
    public void switchStatus(Connection conn, int userId, String status) throws UserDAOImplException {
        try {
            String sql = "UPDATE t_user SET status = ? WHERE id = ?";

            super.update(conn, sql, status, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserDAOImplException("UserDAOImpl 的 switchStatus() 有問題。");
        }
    }
}