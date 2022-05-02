package com.shiminfxcvii.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shiminfxcvii
 * @version 1.0
 * @class CustomMethods
 * @created 2022/4/25 23:10 周一
 * @description 自定义方法类
 */
public class CustomMethods {

    /**
     * 合并两个 List
     *
     * @param <E> {@code List} 的元素类型
     * @param o1  List<E>
     * @param o2  List<E>
     * @return 返回一个新 List，如果其中一个为空则返回另外一个
     * @method mergeTwoLists
     * @author shiminfxcvii
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
     * @param <E> {@code List} 的元素类型
     * @param o   List<E>
     * @return 返回一个新 List
     * @method getListTopTenData
     * @author shiminfxcvii
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
}