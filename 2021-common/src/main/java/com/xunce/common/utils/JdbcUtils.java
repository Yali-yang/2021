package com.xunce.common.utils;


import com.xunce.common.entity.User;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcUtils {

    public static void main(String[] args) {
//        commonQueryForSingleTable();
        List<User> users = commonQuery(User.class, "SELECT BCODE as bccode,ENTRYTIME as entrytime FROM fc_bondip where BONDIPID in(83774, 176935)");
        System.out.println(users);
    }

    /**
     * 通过表的查询，重点是反射
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public static <T> List<T> commonQuery(Class<T> clazz, String sql, Object... args){
        List<T> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            connection = JdbcUtils.getConnection();
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            while(rs.next()){
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
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JdbcUtils.closeResource(connection, ps, rs);
        }
        return list;
    }

    /**
     * 单个表的通用查询
     */
    public static void commonQueryForSingleTable() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            String sql = "SELECT BCODE as bccode,ENTRYTIME as entrytime FROM fc_bondip where BONDIPID in(?, ?)";
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, 83774);
            ps.setInt(2, 176935);

            resultSet = ps.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            List<User> list = new ArrayList<>();
            while (resultSet.next()) {
                User user = new User();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    Object columnValue = resultSet.getObject(i + 1);
                    // 获取的是列的别名
                    String columnName = metaData.getColumnLabel(i + 1);

                    // 重点在这里，通过反射，给类赋值
                    Field field = User.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(user, columnValue);
                }
                list.add(user);
            }
            System.out.println(list);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResource(conn, ps, resultSet);
        }
    }

    public static Connection getConnection() throws Exception {
        String driverClass = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://192.168.0.88:53306/xc_ods?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useOldAliasMetadataBehavior=true";
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
