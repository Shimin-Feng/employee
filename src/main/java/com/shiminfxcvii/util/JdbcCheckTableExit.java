package com.shiminfxcvii.util;

import java.sql.*;

/**
 * TODO: Intellij IDEA 已经提示过可以不使用下面的方式连接 MySQL，那么如何简化此类的代码？
 * JdbcTemplate template = new JdbcTemplate(dataSource);
 * template.query("SHOW TABLES FROM employee_management LIKE 'persistent_logins';", new BeanPropertyRowMapper<>());
 */
public class JdbcCheckTableExit {

    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:3306/employee_management";
    private static final String user = "root";
    private static final String password = "MySQL1672943850";

    /**
     * 判断表 "persistent_logins" 是否存在于数据库
     *
     * @return true 存在，false 不存在
     * @method isTableExist
     * @author shiminfxcvii
     * @created 2022/5/1 15:22
     * @see java.lang.Class
     * @see java.sql.DriverManager
     * @see java.sql.Connection
     * @see java.sql.DatabaseMetaData
     * @see java.sql.ResultSet
     */
    public static boolean isTableExist(String schemaPattern, String tableNamePattern) {
        var isExist = false;
        Connection c = null;
        DatabaseMetaData d;
        ResultSet r;
        try {
            Class.forName(driver);
            c = DriverManager.getConnection(url, user, password);
            c.setAutoCommit(false);
            d = c.getMetaData();
            r = d.getTables(null, schemaPattern, tableNamePattern, new String[]{"TABLE"});
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