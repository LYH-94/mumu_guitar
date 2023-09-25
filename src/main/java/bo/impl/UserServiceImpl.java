package bo.impl;

import bo.UserService;
import bo.exception.UserServiceImplException;
import controllers.exception.UserControllerImplException;
import dao.impl.UserDAOImpl;
import pojo.User;
import util.ConnUtils;
import util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

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

    @Override
    public void getAllMemberList(HttpServletRequest req) throws UserServiceImplException {
        try {
            // 處理搜尋。
            String searchMember = "";
            HttpSession session = req.getSession();
            String session_searchMember = (String) session.getAttribute("session_searchMember");
            if (StringUtils.isEmpty(req.getParameter("searchMember"))) {
                if (StringUtils.isEmpty(session_searchMember)) {
                    searchMember = "";
                } else {
                    searchMember = session_searchMember;
                }
            } else if ("reset".equals(req.getParameter("searchMember"))) {
                searchMember = "";
            } else {
                searchMember = req.getParameter("searchMember");
            }
            session.setAttribute("session_searchMember", searchMember);

            // 獲取用戶選擇的頁碼。
            int memberPageNumber; // 預設為第一頁。
            if (StringUtils.isEmpty(req.getParameter("memberPageNumber"))) { // 預設為 1。
                memberPageNumber = 1;
            } else {
                memberPageNumber = Integer.parseInt(req.getParameter("memberPageNumber"));
            }
            session.setAttribute("session_memberPageNumber", Integer.toString(memberPageNumber));

            List<User> allMemberList = userDAO.getAllMemberList(ConnUtils.getConn(), User.class, searchMember, memberPageNumber);

            session.setAttribute("allMemberList", allMemberList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserServiceImplException("UserServiceImplException 的 getAllMemberList() 有問題。");
        }
    }

    @Override
    public void getAllMemberCount(HttpServletRequest req) throws UserServiceImplException {
        try {
            // 處理搜尋。
            String searchMember = "";
            HttpSession session = req.getSession();
            String session_searchMember = (String) session.getAttribute("session_searchMember");
            if (StringUtils.isEmpty(req.getParameter("searchMember"))) {
                if (StringUtils.isEmpty(session_searchMember)) {
                    searchMember = "";
                } else {
                    searchMember = session_searchMember;
                }
            } else if ("reset".equals(req.getParameter("searchMember"))) {
                searchMember = "";
            } else {
                searchMember = req.getParameter("searchMember");
            }
            session.setAttribute("session_searchMember", searchMember);

            // 獲取所有用戶的訂單數量，用於計算訂單總頁數。
            int memberPages;
            int allMemberCount = userDAO.getAllMemberCount(ConnUtils.getConn(), searchMember);

            if (allMemberCount == 0) { // 10 筆訂單為一頁。
                memberPages = 1;
            } else if (allMemberCount % 10 != 0) {
                memberPages = (allMemberCount / 10) + 1;
            } else {
                memberPages = allMemberCount / 10;
            }

            req.getSession().setAttribute("allMemberCount", allMemberCount); // 將訂單總數儲存於 session。
            req.getSession().setAttribute("memberPages", memberPages); // 根據訂單總數計算的總頁數，儲存於 session。

        } catch (Exception e) {
            e.printStackTrace();
            throw new UserServiceImplException("UserServiceImplException 的 getAllMemberCount() 有問題。");
        }
    }

    @Override
    public void switchStatus(HttpServletRequest req, int userId, String status) throws UserServiceImplException {
        try {
            if ("正常".equals(status)) {
                status = "停權";
            } else if ("停權".equals(status)) {
                status = "正常";
            }

            userDAO.switchStatus(ConnUtils.getConn(), userId, status);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserServiceImplException("UserServiceImplException 的 switchStatus() 有問題。");
        }
    }

}
