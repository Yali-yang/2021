package com.xunce.common.utils;


import com.xunce.common.entity.User;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcUtils {

    public static void main(String[] args) throws Exception {
        Connection connection = getConnection();
        List<User> users = commonQuery(connection, User.class, "SELECT BCODE as bccode,ENTRYTIME as entrytime FROM fc_bondip where BONDIPID in(83774, 176935)");
        connection.close();
        System.out.println(users);
    }

    /**
     * * 批量插入的方式三：100万条数据，6秒左右
     * * 1.addBatch()、executeBatch()、clearBatch()
     * * 2.mysql服务器默认是关闭批处理的，我们需要通过一个参数，让mysql开启批处理的支持。
     * * 		 ?rewriteBatchedStatements=true 写在配置文件的url后面
     * * 3.使用更新的mysql 驱动：mysql-connector-java-5.1.37-bin.jar
     * * 4.设置不允许自动提交数据
     */
    public static void batchInsert() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {

            long start = System.currentTimeMillis();

            conn = JdbcUtils.getConnection();

            //设置不允许自动提交数据
            conn.setAutoCommit(false);

            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <= 1000000; i++) {
                ps.setObject(1, "name_" + i);

                //1."攒"sql
                ps.addBatch();

                if (i % 500 == 0) {
                    //2.执行batch
                    ps.executeBatch();

                    //3.清空batch
                    ps.clearBatch();
                }

            }

            //提交数据
            conn.commit();

            long end = System.currentTimeMillis();

            System.out.println("花费的时间为：" + (end - start));//20000:83065 -- 565
        } catch (Exception e) {                                //1000000:16086 -- 5114
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResource(conn, ps, null);
        }
    }

    /**
     * 1.通过表的查询，重点是反射
     * 2.connection从外层传入，可以通知事务
     * 通过connection可以控制在当前连接下，设置事务，设置隔离级别（connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);）
     *
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public static <T> List<T> commonQuery(Connection connection, Class<T> clazz, String sql, Object... args) {
        List<T> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            while (rs.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    Object columnValue = rs.getObject(i + 1);

                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResource(connection, ps, rs);
        }
        return list;
    }


    public static Connection getConnection() throws Exception {
        String driverClass = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://192.168.0.88:53306/xc_ods";
        String user = "root";
        String password = "mysql.admin.pass";

        Class.forName(driverClass);
        Connection conn = DriverManager.getConnection(url, user, password);

        return conn;
    }

    public static void closeResource(Connection conn, Statement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
