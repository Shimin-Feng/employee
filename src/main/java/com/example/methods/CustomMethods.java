package com.example.methods;

import com.example.entity.Employee;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Name CustomMethods
 * @Author $himin F
 * @Date 2022/4/25 23:10 周一
 * @Version 1.0
 * @description: 自定义方法类
 */
public class CustomMethods {

    /**
     * 合并两个 List
     *
     * @param o1 List<E>
     * @param o2 List<E>
     * @return 返回一个新 List，如果其中一个为空则返回另外一个
     * @method mergeTwoLists
     * @author $himin F
     * @created 2022/5/1 2:24
     * @see java.util.List
     */
    public static <E> List<E> mergeTwoLists(@NotNull List<E> o1, List<E> o2) {
        if (o1.size() > 0 && o2.size() > 0) {
            List<E> n1 = new ArrayList<>(o1);
            List<E> n2 = new ArrayList<>();
            for (E e1 : o2) {
                int i = 0;
                for (E e2 : n1) {
                    if (e1.equals(e2)) {
                        i = 1;
                        break;
                    }
                }
                if (0 == i) {
                    n2.add(e1);
                }
            }
            n1.addAll(n2);
            return n1;
        } else if (o1.size() > 0) {
            return o1;
        } else {
            return o2;
        }
    }

    /**
     * 取 List 数组前十条数据
     *
     * @param o List<E>
     * @return 返回一个新 List
     * @method getListTopTenData
     * @author $himin F
     * @created 2022/5/1 2:27
     * @see java.util.List
     */
    public static <E> @NotNull List<E> getListTopTenData(@NotNull List<E> o) {
        List<E> n = new ArrayList<>();
        if (o.size() > 0) {
            int i = 0;
            for (E t : o) {
                if (i < 10) {
                    n.add(t);
                    i++;
                } else {
                    break;
                }
            }
        }
        return n;
    }

    /**
     * 比较两个对象的属性值是否相同
     *
     * @param o Employee
     * @param t Employee
     * @return true 如果所有属性值都相同
     * @method equals
     * @author $himin F
     * @created 2022/5/1 2:33
     * @see com.example.entity.Employee
     */
    public static boolean equals(@NotNull Employee o, @NotNull Employee t) {
        return Objects.equals(o.getEmployeeName(), t.getEmployeeName())
                && Objects.equals(o.getEmployeeIdCard(), t.getEmployeeIdCard())
                && Objects.equals(o.getEmployeeAddress(), t.getEmployeeAddress())
                && Objects.equals(o.getEmployeePhoneNumber(), t.getEmployeePhoneNumber());
    }
}