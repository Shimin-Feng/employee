package com.test;

import com.example.entity.Employee;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

/**
 * @Name TestObject
 * @Author $himin F
 * @Date 2022/5/1 4:15 ÷‹»’
 * @Version 1.0
 * @description:
 */
public class TestObject {

    public static boolean hasContent(Object object) {
        return hasContentWithoutKeys(object, Collections.singletonList(""));
    }

    public static boolean hasContentWithoutKeys(Object object, List<String> keys) {
        final Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                if (!field.isSynthetic()) {
                    final boolean accessible = field.isAccessible();
                    if (null != field.get(object) && !keys.contains(field.getName())) {
                        return true;
                    }
                    field.setAccessible(accessible);
                }
            }
        } catch (Exception e) {
            //nothing to do
        }
        return false;
    }

    @Test
    public void test() {

        Employee employee = new Employee();
        employee.setEmployeeName("Jobs");
        boolean b = hasContent(employee);
        System.out.println(b);
    }

}