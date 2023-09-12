package bo.impl;

import bo.UserService;
import bo.exception.UserServiceImplException;
import controllers.exception.UserControllerImplException;
import dao.impl.UserDAOImpl;
import pojo.User;
import util.ConnUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.time.LocalDate;

public class UserServiceImpl implements UserService {

    private UserDAOImpl userDAO = null;

    @Override
    public User loginVerification(String account, String password) throws UserServiceImplException {
        try {
            return userDAO.loginVerification(ConnUtils.getConn(), User.class, account, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserControllerImplException("UserServiceImpl 的 loginVerification() 有問題。");
        }
    }

    // 銷毀當前 session。
    @Override
    public void invalidateSession(HttpServletRequest req) throws UserServiceImplException {
        try {
            req.getSession().invalidate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserControllerImplException("UserServiceImpl 的 invalidateSession() 有問題。");
        }
    }

    @Override
    public User getUserById(int id) throws UserServiceImplException {
        try {
            return userDAO.getUserById(ConnUtils.getConn(), User.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserControllerImplException("UserServiceImpl 的 getUserById() 有問題。");
        }
    }

    @Override
    public boolean register(String account, String password, String username, String gender, LocalDate birthday, String phone, String email) throws UserServiceImplException {
        try {
            // 先檢查 User 數據表中是否已經存在相同的 account。若為不重複為 true，重複則為 false。
            boolean b = userDAO.checkForDuplicateUsers(ConnUtils.getConn(), User.class, account);

            if (b) {
                // 新增會員資料
                return userDAO.register(ConnUtils.getConn(), account, password, username, gender, birthday, phone, email);
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new UserServiceImplException("UserServiceImplException 的 register() 有問題。");
        }
    }

    @Override
    public boolean updatePersonalInfo(String account, String password, String username, String gender, LocalDate birthday, String phone, String email) throws UserServiceImplException {
        try {
            return userDAO.updatePersonalInfo(ConnUtils.getConn(), account, password, username, gender, birthday, phone, email);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserServiceImplException("UserServiceImplException 的 updatPersonalInfo() 有問題。");
        }
    }

}
