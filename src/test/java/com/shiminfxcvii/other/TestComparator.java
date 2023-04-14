package com.shiminfxcvii.other;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Represents an operation that accepts a single input argument and returns no
 * result. Unlike most other functional interfaces, {@code Consumer} is expected
 * to operate via side-effects.
 * <p>
 * This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #equals(Object)}.
 *
 * @param <T> the type of the input to the operation
 * @since 1.8
 */
@FunctionalInterface
public interface TestComparator<T> {
    int compare(T o1, T o2);

    boolean equals(Object obj);
}

class lambda_Comparator {
    //下面给出 lambda 以及实际替代的内部类写法
    private static Comparator<String> newComparator() {
        return (a, b) -> b.length() - a.length();
    }

    private static Comparator<String> newComparator1() {
        return new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return b.length() - a.length();
            }
        };
    }

    public static void main(String[] args) {
        String[] array = {"abc", "ab", "abcd"};
        System.out.println(Arrays.toString(array));
        Arrays.sort(array, newComparator()); // 方式一
        Arrays.sort(array, (a, b) -> b.length() - a.length());//更简单的方式
        System.out.println(Arrays.toString(array));
    }
}