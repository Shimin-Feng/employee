package com.test;

import com.example.entity.User;
import org.junit.platform.commons.util.StringUtils;

import java.lang.reflect.Field;

/**
 * @Name TestEntity
 * @Author $himin F
 * @Date 2022/5/1 4:33 周日
 * @Version 1.0
 * @description:
 */
public class TestEntity {

    public static void main(String[] args) {
        User user1 = new User();
        user1.setUserId("");
        user1.setUsername("aaa");
        user1.setAuthorities("ADMIN");
        user1.setPassword("123456");
        User user2 = new User();

        boolean u1Flag = checkObjAllFieldsIsNull(user1);
        boolean u2Flag = checkObjAllFieldsIsNull(user2);

        System.out.println("user1 是否为空：" + u1Flag);
        System.out.println("user2 是否为空：" + u2Flag);

    }

    /**
     * 判断对象中属性值是否全为空
     *
     * @param object
     * @return
     */
    public static boolean checkObjAllFieldsIsNull(Object object) {
        if (null == object) {
            return true;
        }

        try {
            for (Field f : object.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.get(object) != null && StringUtils.isNotBlank(f.get(object).toString())) {
                    System.out.println(f.getName() + ": " + f.get(object));
                    return false;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

}