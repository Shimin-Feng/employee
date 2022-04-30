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
 * @description: 合并两个 Lists
 */
public class CustomMethods {
    public static <T> List<T> mergeLists(@NotNull List<T> list1, List<T> list2) {
        if (list1.size() > 0 && list2.size() > 0) {
            List<T> list3 = new ArrayList<>(list1);
            List<T> list4 = new ArrayList<>();
            for (T t1 : list2) {
                int i = 0;
                for (T t2 : list3) {
                    if (t1.equals(t2)) {
                        i = 1;
                        break;
                    }
                }
                if (0 == i) {
                    list4.add(t1);
                }
            }
            list3.addAll(list4);
            return list3;
        } else if (list1.size() > 0) {
            return list1;
        } else {
            return list2;
        }
    }

    public static <T> @NotNull List<T> getTenSearchRecords(@NotNull List<T> recordNames) {
        List<T> newRecordNames = new ArrayList<>();
        if (recordNames.size() > 0) {
            int i = 0;
            for (T t : recordNames) {
                if (i < 10) {
                    newRecordNames.add(t);
                    i++;
                } else {
                    break;
                }
            }
        }
        return newRecordNames;
    }

    public static boolean isSame(@NotNull Employee o, @NotNull Employee t) {
        return Objects.equals(o.getEmployeeName(), t.getEmployeeName())
                && Objects.equals(o.getEmployeeIdCard(), t.getEmployeeIdCard())
                && Objects.equals(o.getEmployeeAddress(), t.getEmployeeAddress())
                && Objects.equals(o.getEmployeePhoneNumber(), t.getEmployeePhoneNumber());
    }
}