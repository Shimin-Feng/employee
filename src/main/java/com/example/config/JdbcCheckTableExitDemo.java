package com.example.config;

import java.sql.*;

/**
 * 如何简化下面的代码？
 */
public class JdbcCheckTableExitDemo {

    private static final String url = "jdbc:mysql://localhost:3306/test_database";
    private static final String user = "root";
    private static final String password = "MySQL1672943850";
    private static final String driver = "com.mysql.cj.jdbc.Driver";

    public boolean isExist() {
        boolean isExist = false;
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