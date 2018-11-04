package com.imooc.utils;

import com.imooc.enums.CodeEnum;

/**
 * @author Kent
 * @date 2017-11-06.
 */
public class EnumUtil {
    public static <T extends CodeEnum> T getByCode(Integer code,Class<T> enumClass) {
        for (T each : enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }
}
