package com.xunce.common.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DruidUtils {

    private static DataSource source;
    static{
        try {
            Properties pros = new Properties();
            InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            pros.load(inputStream);
            source = DruidDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过连接池获取数据库连接，用完记得close连接，不close不会将conn放回连接池
     * @return
     */
    public static Connection getConnection() {
        try {
            return source.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
