package com.epoint.lsyc.common.util;

import java.io.File;

public class LsZwfwBjsbConstant {
    public static final String CONSTANT_STR_3 = "3";
    public static final String CONSTANT_STR_4 = "4";
    public static final String CONSTANT_STR_5 = "5";
    public static final String CONSTANT_STR_6 = "6";
    public static final String CONSTANT_STR_7 = "7";
    public static final String CONSTANT_STR_EIGHT = "8";
    public static final String CONSTANT_STR_NINE = "9";
    public static final String CONSTANT_STR_10 = "10";
    public static final String CONSTANT_STR_11 = "11";
    public static final String CONSTANT_STR_12 = "12";
    public static final String CONSTANT_STR_20 = "20";
    public static final String CONSTANT_STR_35 = "35";
    public static final String CONSTANT_STR_40 = "40";
    public static final String CONSTANT_STR_NULL = "";
    public static final String CONSTANT_STR_NBSP = " ";
    public static final String CONSTANT_STR_01 = "01";
    public static final String CONSTANT_STR_00 = "00";
    public static final String CONSTANT_STR_02 = "02";
    public static final String CONSTANT_STR_03 = "03";
    public static final String CONSTANT_STR_04 = "04";
    public static final String CONSTANT_STR_05 = "05";
    public static final String CONSTANT_STR_06 = "06";
    public static final String CONSTANT_STR_07 = "07";
    public static final String CONSTANT_STR_99 = "99";
    public static final String CONSTANT_STR_200 = "200";
    public static final String CONSTANT_STR_1753 = "1753";
    public static final String CONSTANT_STR_GZR = "工作日";
    public static final String CONSTANT_STR_ZW = "暂无";
    public static final String CONSTANT_STR_INSERT = "insert";
    public static final String CONSTANT_STR_UPDATE = "update";
    public static final String CONSTANT_STR_DELETE = "delete";
    public static final String CONSTANT_STR_I = "I";
    public static final Integer CONSTANT_INT_2 = 2;
    public static final Integer CONSTANT_INT_4 = 4;
    public static final String PROJPWD = "000000";
    public static final String DATESTR = "yyyy-MM-dd";
    public static final String DATETIMESTR_24H = "yyyy-MM-dd HH:mm:ss";
    public static final String TIMESTR_24H = "HH:mm:ss";
    public static final String ONEDAY_LASTTIME = "23:59:59";
    public static final String FILEDOWN_URL = "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=";
    public static final String TAKETYPE_DZ ="附件上传";
    public static final String TAKETYPE_ZHIZ ="纸质收取";
    public static final String SYSTEMCODE = "33110189002";
    public static final String UNDEFINED = "undefined";
    public static final String OU_OR_USER_INVOLVED_SYSTEM_YC = "1";
    public static final String OU_OR_USER_INVOLVED_SYSTEM_SP = "2";
    public static final String OU_OR_USER_INVOLVED_SYSTEM_YCANDSP = "3";
    public static void fileDelete(String filePath) {
        File file = new File(filePath);
        if (file != null) {
            // 判断是否是目录
            if (file.isDirectory()) {
                // 遍历子目录进行删除
                for (File subFile : file.listFiles()) {
                    fileDelete(subFile.getPath());
                }
                file.delete();
            } else {
                // 不是目录，删除文件
                file.delete();
            }
        }
    }
}
