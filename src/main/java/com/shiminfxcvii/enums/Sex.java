package com.shiminfxcvii.enums;

import com.shiminfxcvii.util.Constants;
import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * 设置统一获取性别的方式
 *
 * @author ShiminFXCVII
 * @since 2022/5/13 17:38 周五
 */
public enum Sex implements Constants {

    /**
     * 女
     */
    Female("女"),
    /**
     * 男
     */
    Male("男"),
    /**
     * 未知
     */
    Unknown("未知");

    private static final Sex[] VALUES = values();
    private final String gender;

    Sex(String gender) {
        this.gender = gender;
    }

    /**
     * 通过性别对应的数字获取性别
     *
     * @param ordinal 枚举常量序数
     * @return {@link Sex}
     * @author ShiminFXCVII
     * @since 6/2/2022 1:48 PM
     */
    public static String getGenderByOrdinal(@Nullable Integer ordinal) {
        // Use cached VALUES instead of values() to prevent array allocation.
        for (Sex sex : VALUES)
            if (Objects.equals(sex.ordinal(), ordinal))
                return sex.getGender();

        return Unknown.getGender();
    }

    /**
     * 通过数字对应的性别获取枚举常量序数
     *
     * @param gender 枚举常量序数对应的性别
     * @return {@link Sex}
     * @author ShiminFXCVII
     * @since 6/2/2022 1:48 PM
     */
    public static Integer getOrdinalByGender(@Nullable String gender) {
        // Use cached VALUES instead of values() to prevent array allocation.
        for (Sex sex : VALUES)
            if (Objects.equals(sex.getGender(), gender))
                return sex.ordinal();

        return Unknown.ordinal();
    }

    public String getGender() {
        return this.gender;
    }

}