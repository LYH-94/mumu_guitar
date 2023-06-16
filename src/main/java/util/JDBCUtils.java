package util;

import util.exception.JDBCUtilsException;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class JDBCUtils {

    //獲取數據庫的連接。
    public static Connection getConnection() throws JDBCUtilsException {
        FileInputStream fis = null;
        Connection conn = null;

        try {
            //1.讀取配置文件中的基本訊息。
            fis = new FileInputStream("C:\\Users\\LIN-YAOHUA\\Desktop\\GitHub_local\\mumu_guitar\\src\\main\\java\\jdbc.properties");

            Properties pros = new Properties();
            pros.load(fis);

            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            String url = pros.getProperty("url");
            String driverClass = pros.getProperty("driverClass");

            //2.加載驅動。
            Class.forName(driverClass);

            //3.獲取連接。
            conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            throw new JDBCUtilsException("JDBCUtils 的 getConnection() 有問題。");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //關閉 Connection 和 Statement 的操作。
    //注意，要導入的包要選擇通用的 Driver 介面，不要選特定廠商的數據庫驅動。
    public static void closeResource(Connection conn, Statement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //關閉 Connection、Statement 和 ResultSet 的操作。
    //注意，要導入的包要選擇通用的 Driver 介面，不要選特定廠商的數據庫驅動。
    public static void closeResource(Connection conn, Statement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

