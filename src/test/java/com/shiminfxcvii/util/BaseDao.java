package com.shiminfxcvii.util;

import com.shiminfxcvii.entity.SearchRecord;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@SpringBootTest
public class BaseDao {

    @Resource
    private DataSource dataSource;
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Test
    public void test() throws SQLException {
        boolean result = DataSourceUtils.getConnection(dataSource).getMetaData().getTables(null, "employee_management", "persistent_logins", new String[]{"TABLE"}).next();
        System.out.println(result + "666666666666666666666");
    }

    @Test
    public void test1() {
        List<SearchRecord> query = jdbcTemplate.query("SELECT * FROM employee_management.search_record", new BeanPropertyRowMapper<>(SearchRecord.class));
        System.out.println(query.size());
        for (SearchRecord searchRecord : query) {
            System.out.println(searchRecord);
        }
    }

    @Test
    public void test3() throws Exception {
        // 1 提供Properties实例对象
        Properties pros = new Properties();
        FileInputStream fs = new FileInputStream("src/main/resources/application.yml");
        pros.load(fs);

        // 2 读取配置信息
        String url = pros.getProperty("spring.datasource.url");
        String user = pros.getProperty("spring.datasource.user");
        String password = pros.getProperty("spring.datasource.password");
        String driverClass = pros.getProperty("spring.datasource.driver-class-name");

        // 4 加载驱动
        Class<Driver> clazz = (Class<Driver>) Class.forName(driverClass);
        Driver driver = clazz.newInstance();

        // 5 注册驱动
        DriverManager.registerDriver(driver);

        // X 第4、第5 合成一步即可
        // Class.forName(driverClass);

        /*
			第一、
				加载驱动的目的就是注册驱动，那么我们就可以利用类的加载机制
                使得Driver类加载到内存中，只获得类的字节码文件即可
                而在Driver类加载之后，会自动调用Driver中声明的静态代码块，完成自动注册驱动
                也就是说同时实现了加载驱动、注册驱动两步
                故：第4、第5 这两步可以用 `Class.forName(driverClass)` 代替


			第二、
				第4、第5 这两步都省略，也同样可以完成自动注册驱动
				因为从MySQL5之后，可以利用驱动文件完成自动注册驱动
				这里说的驱动文件是仅针对MySQL的，如果更换数据库，可能就没有驱动文件来自动注册驱动
				故为了代码的健壮性，应写上 `Class.forName(driverClass)`

            ，仅针对MySQL数据库


        */

        // 6 获取对象
        Connection conn = DriverManager.getConnection(url, user, password);

        // 7 使用对象
        System.out.println(conn);
    }

/*
1. 配置文件：jdbc.properties
    user=root
    password=abc123
    url=jdbc:mysql://localhost:3306/test
    driverClass=com.mysql.jdbc.Driver

2. 使用配置文件的好处：
	-- 实现了代码和数据的分离，如果需要修改配置信息，直接在配置文件中修改，不需要深入代码
	-- 修改配置信息时省去重新编译的过程
*/

}