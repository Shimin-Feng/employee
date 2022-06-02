package com.shiminfxcvii.util;

import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * 设置统一获取性别的方式
 *
 * @author shiminfxcvii
 * @since 2022/5/13 17:38 周五
 */
public enum Sex implements Constants {
    Female("0", "女"),
    Male("1", "男");

    private static final Sex[] VALUES = values();
    private final String number;
    private final String gender;

    Sex(String number, String gender) {
        this.number = number;
        this.gender = gender;
    }

    /**
     * 通过性别对应的数字获取 Sex
     *
     * @param number 性别对应的数字
     * @return Sex / null
     * @method resolveByNumber
     * @author shiminfxcvii
     * @since 6/2/2022 1:48 PM
     */
    @Nullable
    public static Sex resolveByNumber(String number) {
        // Use cached VALUES instead of values() to prevent array allocation.
        for (Sex sex : VALUES)
            if (Objects.equals(sex.getNumber(), number))
                return sex;

        return null;
    }

    /**
     * 通过数字对应的性别获取 Sex
     *
     * @param gender 数字对应的性别
     * @return Sex / null
     * @method resolveByGender
     * @author shiminfxcvii
     * @since 6/2/2022 1:48 PM
     */
    @Nullable
    public static Sex resolveByGender(String gender) {
        // Use cached VALUES instead of values() to prevent array allocation.
        for (Sex sex : VALUES)
            if (Objects.equals(sex.getGender(), gender))
                return sex;

        return null;
    }

    public String getNumber() {
        return this.number;
    }

    public String getGender() {
        return this.gender;
    }
}
