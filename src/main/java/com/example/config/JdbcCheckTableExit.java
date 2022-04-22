package com.example.config;

import java.sql.*;

/**
 * TODO: Intellij IDEA 已经提示过可以不使用下面的方式连接 MySQL，那么如何简化此类的代码？
 * JdbcTemplate template = new JdbcTemplate(dataSource);
 * template.query("SHOW TABLES FROM test_database LIKE 'persistent_logins';", new BeanPropertyRowMapper<>());
 */
public class JdbcCheckTableExit {

    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:3306/test_database";
    private static final String user = "root";
    private static final String password = "MySQL1672943850";

    public static boolean isExist() {
        var isExist = false;
        Connection c = null;
        DatabaseMetaData d;
        ResultSet r;
        try {
            Class.forName(driver);
            c = DriverManager.getConnection(url, user, password);
            c.setAutoCommit(false);
            d = c.getMetaData();
            r = d.getTables("test_database", null, "persistent_logins",
                    new String[]{"TABLE"});
            isExist = r.next();
            r.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != c) {
                    c.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isExist;
    }
}