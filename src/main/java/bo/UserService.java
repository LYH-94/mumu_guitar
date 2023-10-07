package bo;

import pojo.User;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public interface UserService {
    public User loginVerification(String account, String password);

    public void invalidateSession(HttpServletRequest req);

    public User getUserById(int id);

    public boolean register(String account, String password, String username, String gender, LocalDate birthday, String phone, String email);

    public boolean updatePersonalInfo(String account, String password, String username, String gender, LocalDate birthday, String phone, String email);

    public void getAllMemberList(HttpServletRequest req);

    public void getAllMemberCount(HttpServletRequest req);

    public void switchStatus(HttpServletRequest req, int userId, String status);
}