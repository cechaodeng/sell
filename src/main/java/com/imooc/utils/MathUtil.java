package com.imooc.utils;

import java.util.DoubleSummaryStatistics;

/**
 * @author Kent
 * @date 2017-11-05.
 */
public class MathUtil {
    /**
     * 精度
     */
    private static final Double MONEY_RANGE = 0.01;

    /**
     * 比较两个数值是否相等,如果两数的差值小于精度,则表示两数相等
     * 相等返回true,不相等返回false
     * @param d1
     * @param d2
     * @return
     */
    public static Boolean equals(Double d1,Double d2) {
        Double result = Math.abs(d1 - d2);
        if (result < MONEY_RANGE) {
            return true;
        } else {
            return false;
        }

    }
}
