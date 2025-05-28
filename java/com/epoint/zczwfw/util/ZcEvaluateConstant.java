package com.epoint.zczwfw.util;

/**
 * 
 * 邹城短信回访评价常量类
 * 
 * @author yrchan
 * @version 2022年4月12日
 */
public class ZcEvaluateConstant
{

    /**
     * 
     * 导入处理进度
     * 
     * @author yrchan
     * @version 2022年4月12日
     */
    public interface impProcessing
    {
        /**
         * 代码项名称
         */
        String NAME = "导入处理进度";

        /**
         * 待处理
         */
        int DCL = 1;

        /**
         * 处理中
         */
        int DCZ = 2;

        /**
         * 处理完成
         */
        int CLWC = 3;

    }

}
