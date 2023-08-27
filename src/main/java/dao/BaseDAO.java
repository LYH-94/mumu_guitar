package dao;

import dao.exception.BaseDAOException;
import util.DataTransformationUtils;
import util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

//通用的 DAO 父類。
public abstract class BaseDAO {
    //查詢操作：查詢特殊值(如聚合函數等)的通用方法 - 考慮事務。(不在方法內獲取或關閉數據庫連接，改由呼叫方法時才傳入連接。)
    public <E> E getValue(Connection conn, String sql, Object ...args) throws BaseDAOException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            ps = conn.prepareStatement(sql);
            for(int i = 0; i < args.length; i++){
                ps.setObject(i + 1,args[i]);
            }

            rs = ps.executeQuery();
            if(rs.next()){
                return (E) rs.getObject(1);
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new BaseDAOException("BaseDAO 的 getValue() 有問題。");
        } finally {
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }

    //查詢操作：查詢單表或不同數據表的通用寫法 Version 2.0 - 考慮事務。(不在方法內獲取或關閉數據庫連接，改由呼叫方法時才傳入連接。)
    public <T> T getInstance(Connection conn, Class<T> clazz, String sql, Object ...args) throws BaseDAOException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            //1.預編譯 SQL 語句，返回 PreparedStatement 物件。
            ps = conn.prepareStatement(sql);

            //2.填充佔位符。
            for(int i = 0; i < args.length; i++){
                ps.setObject(i + 1, args[i]);
            }

            //3.執行操作並返回結果集。
            rs = ps.executeQuery();

            //4.取得結果集的元數據和元數據的字段數。
            ResultSetMetaData rsmd = rs.getMetaData();
            //通過 ResultSetMetaData 取得結果集中的字段數。
            int columnCount = rsmd.getColumnCount();

            //5.處裡結果集。
            //結果集的 next()：判斷結果集的下一條是否有數據，如果有數據則返回true，並指針下移，如果返回false，指針不會下移。
            //若不只一條紀錄，則改用 while()。
            if (rs.next()) {
                T t = clazz.newInstance();

                for (int i = 0; i < columnCount; i++) {
                    //獲取字段的值。
                    Object columnValues = rs.getObject(i + 1);

                    //因 MySQL 8 的問題，獲取 Datatime 類型的字段時，會被轉化成 LocalDateTime 類型 (正常應該是對應到 Timestamp 類型)，它無法賦予給 POJO 中 Date 類型的屬性，
                    //所以需要在屬性賦值錢進行檢查並轉換，將 LocalDateTime  類型手動轉換成 Date 類型。
                    if ("java.time.LocalDateTime".equals(columnValues.getClass().getName())) {
                        columnValues = DataTransformationUtils.LocalDateTimeToTimestamp(columnValues);
                    }

                    //獲取字段的字段名。
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //在 POJO 中存在某些屬性，是根據實體來自定義的類型，但從數據表中獲取的字段為了減少占用的空間都 Integer 類型來替代，
                    //因此要再賦予值之前封裝成對應類型的物件。
                    if ("owner".equals(columnLabel)) {
                        columnValues = DataTransformationUtils.IntegerToUser(columnValues);
                    } else if ("belongOrder".equals(columnLabel)) {
                        columnValues = DataTransformationUtils.IntegerToOrder(columnValues);
                    } else if ("product".equals(columnLabel)) {
                        columnValues = DataTransformationUtils.IntegerToProduct(columnValues);
                    }

                    //通過反射機制：給 cust 物件當前指定的 columnName 屬性，賦值為 columnValues。
                    Field field = clazz.getDeclaredField(columnLabel);

                    //可能會是私有的屬性，因此打開訪問權限。
                    field.setAccessible(true);
                    field.set(t, columnValues);
                }
                return t;
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new BaseDAOException("BaseDAO 的 getInstance() 有問題。");
        }  finally {
            //6.關閉資源
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }

    //查詢操作：當返回的結果集是多條紀錄時 Version 2.0 - 考慮事務。(不在方法內獲取或關閉數據庫連接，改由呼叫方法時才傳入連接。)
    public <T> List<T> getForList(Connection conn, Class<T> clazz, String sql, Object ...args) throws BaseDAOException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            //1.預編譯 SQL 語句，返回 PreparedStatement 物件。
            ps = conn.prepareStatement(sql);

            //2.填充佔位符。
            for(int i = 0; i < args.length; i++){
                ps.setObject(i + 1, args[i]);
            }

            //3.執行操作並返回結果集。
            rs = ps.executeQuery();

            //4.取得結果集的元數據和元數據的字段數。
            ResultSetMetaData rsmd = rs.getMetaData();
            //通過 ResultSetMetaData 取得結果集中的字段數。
            int columnCount = rsmd.getColumnCount();

            //5.創建一個用於儲存結果集的集合。
            ArrayList<T> list = new ArrayList<T>();

            //6.處裡結果集。
            //結果集的 next()：判斷結果集的下一條是否有數據，如果有數據則返回true，並指針下移，如果返回false，指針不會下移。
            //因不只一條紀錄，則用 while()。
            while (rs.next()) {
                T t = clazz.newInstance();

                for (int i = 0; i < columnCount; i++) {
                    //獲取每個字段的值。
                    Object columnValues = rs.getObject(i + 1);

                    //因 MySQL 8 的問題，獲取 Datatime 類型的字段時，會被轉化成 LocalDateTime 類型 (正常應該是對應到 Timestamp 類型)，它無法賦予給 POJO 中 Date 類型的屬性，
                    //所以需要在屬性賦值錢進行檢查並轉換，將 LocalDateTime  類型手動轉換成 Date 類型。
                    if ("java.time.LocalDateTime".equals(columnValues.getClass().getName())) {
                        columnValues = DataTransformationUtils.LocalDateTimeToTimestamp(columnValues);
                    }

                    //獲取每個字段的字段名。
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //在 POJO 中存在某些屬性，是根據實體來自定義的類型，但從數據表中獲取的字段為了減少占用的空間都 Integer 類型來替代，
                    //因此要再賦予值之前封裝成對應類型的物件。
                    if ("owner".equals(columnLabel)) {
                        columnValues = DataTransformationUtils.IntegerToUser(columnValues);
                    } else if ("belongOrder".equals(columnLabel)) {
                        columnValues = DataTransformationUtils.IntegerToOrder(columnValues);
                    } else if ("product".equals(columnLabel)) {
                        columnValues = DataTransformationUtils.IntegerToProduct(columnValues);
                    }

                    //通過反射機制：給 cust 物件當前指定的 columnName 屬性，賦值為 columnValues。
                    Field field = clazz.getDeclaredField(columnLabel);
                    //可能會是私有的屬性，因此打開訪問權限。
                    field.setAccessible(true);
                    field.set(t, columnValues);
                }

                //將每個物件(紀錄)添加到 list 中。
                list.add(t);
            }
            return list;
        } catch (Exception e){
            e.printStackTrace();
            throw new BaseDAOException("BaseDAO 的 getForList() 有問題。");
        }  finally {
            //7.關閉資源
            JDBCUtils.closeResource(null, ps, rs);
        }
    }

    //通用的增刪改操作 Version 2.0 - 考慮事務。(不在方法內獲取或關閉數據庫連接，改由呼叫方法時才傳入連接。)
    public boolean update(Connection conn, String sql, Object ...args) throws BaseDAOException { //運用可變形參的特性。
        PreparedStatement ps = null;

        try{
            //1.預編譯 SQL 語句，返回 PreparedStatement 物件。
            ps = conn.prepareStatement(sql);

            //2.填充佔位符。
            //SQL 中佔位符的個數與可變形參的長度相同。
            for(int i = 0; i < args.length; i++){
                ps.setObject(i+1,args[i]); //小心參數聲明錯誤。(索引從1開始、陣列從0開始)
            }

            //3.執行操作。
            //ps.execute();
            return (ps.executeUpdate() > 0 ? true : false);
        } catch (Exception e){
            e.printStackTrace();
            throw new BaseDAOException("BaseDAO 的 update() 有問題。");
        }finally {
            //4.關閉資源。
            //注意，不要關掉數據庫連接。
            JDBCUtils.closeResource(null, ps);
        }
    }

    // 查詢紀錄條數。
    public int getCount(Connection conn, String sql, Object... args) throws BaseDAOException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseDAOException("BaseDAO 的 getCount() 有問題。");
        } finally {
            JDBCUtils.closeResource(null, ps, rs);
        }
        return 0;
    }
}