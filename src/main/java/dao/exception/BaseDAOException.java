package dao.exception;

public class BaseDAOException extends RuntimeException {
    public BaseDAOException(String msg) {
        super(msg);
    }
}
