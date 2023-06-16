package util;

import util.exception.connUtilException;

import java.sql.Connection;

public class ConnUtils {
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    //創建 conn。
    public static Connection createConn() throws connUtilException {
        try {
            Connection conn = JDBCUtils.getConnection();
            threadLocal.set(conn);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            throw new connUtilException("connUtil 的 create() 有問題。");
        }
    }

    //獲取當前 conn。
    public static Connection getConn() throws connUtilException {
        try {
            return threadLocal.get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new connUtilException("connUtil 的 getConn() 有問題。");
        }
    }

    //關閉 conn。
    public static void closeConn(Connection conn) throws connUtilException {
        try {
            conn.close();
            threadLocal.set(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new connUtilException("connUtil 的 closeConn() 有問題。");
        }
    }
}
