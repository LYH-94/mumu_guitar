package util;

import util.exception.TransationManagerException;

import java.sql.Connection;

public class TransationManager {

    //開啟事務。
    public static void beginTrans() throws TransationManagerException {
        Connection conn = ConnUtils.getConn();

        try {
            if (conn != null) {
                conn.setAutoCommit(false);
            } else {
                conn = ConnUtils.createConn();
                conn.setAutoCommit(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TransationManagerException("TransationManager 的 beginTrans() 有問題。");
        }
    }

    //提交事務。
    public static void commit() throws TransationManagerException {
        Connection conn = ConnUtils.getConn();

        try {
            if (conn != null) {
                conn.commit();
            } else {
                System.out.println("沒有創建 conn，無法進行提交。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TransationManagerException("TransationManager 的 commit() 有問題。");
        }
    }

    //回滾事務。
    public static void rollback() throws TransationManagerException {
        Connection conn = ConnUtils.getConn();

        try {
            if (conn != null) {
                conn.rollback();
            } else {
                System.out.println("沒有創建 conn，無法進行回滾。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TransationManagerException("TransationManager 的 rollback() 有問題。");
        }
    }

    //關閉事務。
    public static void close() throws TransationManagerException {
        Connection conn = ConnUtils.getConn();

        try {
            if (conn != null) {
                ConnUtils.closeConn(conn);
            } else {
                System.out.println("沒有創建 conn，無法進行關閉。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TransationManagerException("TransationManager 的 close() 有問題。");
        }
    }
}
