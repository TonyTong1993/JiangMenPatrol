package com.ecity.cswatersupply.utils;

import java.math.BigDecimal;

public class DecimaUtils {
    /**
     * 默认保留两位小数
     * @param num
     * @return
     */
    public static double DoubleFormat2(double num){
        BigDecimal bd = new BigDecimal(num);
        bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }
    /**
     * 
     * @param num 需要处理的数字
     * @param numa 保留几位小数
     * @return
     */
    public static double DoubleFormat2(double num,int numa){
        BigDecimal bd = new BigDecimal(num);
        bd = bd.setScale(numa,BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }
}
