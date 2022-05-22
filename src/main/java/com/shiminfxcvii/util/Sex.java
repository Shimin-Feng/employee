package com.shiminfxcvii.util;

import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * @author shiminfxcvii
 * @version 1.0
 * @description 常量化性别
 * @class Sex
 * @created 2022/5/13 17:38 周五
 */
public enum Sex {

    Female("0", "女"), Male("1", "男");

    private static final Sex[] VALUES;

    static {
        VALUES = values();
    }

    private final String number;
    private final String gender;

    Sex(String number, String gender) {
        this.number = number;
        this.gender = gender;
    }

    @Nullable
    public static Sex resolveByNumber(String number) {
        // Use cached VALUES instead of values() to prevent array allocation.
        for (Sex sex : VALUES) {
            if (Objects.equals(sex.getNumber(), number)) {
                return sex;
            }
        }
        return null;
    }

    @Nullable
    public static Sex resolveByGender(String gender) {
        // Use cached VALUES instead of values() to prevent array allocation.
        for (Sex sex : VALUES) {
            if (Objects.equals(sex.getGender(), gender)) {
                return sex;
            }
        }
        return null;
    }

    public String getNumber() {
        return this.number;
    }

    public String getGender() {
        return this.gender;
    }
}
