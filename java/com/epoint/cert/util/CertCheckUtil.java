package com.epoint.cert.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import cn.hutool.core.util.StrUtil;

public class CertCheckUtil {
    private static final String rootCode = "1.2.156.3005.2"; //根代码;
    private static final String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String versionNumber = "001";
	
    public static String getCheckCode(String reckoningStr) {
        Integer X; //校验位
        int M = 36;
        int P = 0; //递归结算结果
        // 1.将待计算校验位的字符串进行递归计算
        for (int i = 0; i < reckoningStr.length(); i++) {
            String strVal = String.valueOf(reckoningStr.charAt(i));
            int ai = 0;
            if (strVal.matches("[A-Z]+")) {
                ai = str.indexOf(strVal) + 1;
            } else {
                ai = Integer.valueOf(strVal);
            }

            int R1, R2, R3;
            if (i == 0) {
                R1 = M + ai;
            } else {
                R1 = P + ai;
            }
            if (ai > 10 && R1 > (M + 1)) {
                R1 = R1 % (M + 1);
            } else if (ai < 10 && R1 > M) {
                R1 = R1 % M;
            }

            R2 = R1 % M == 0 ? 36 : R1 % M;
            R3 = R2 * 2;
            P = R3 % (M + 1);
        }
//        //system.out.println("递归结算结果:P={}"+P);

        //2.计算校验位X，应满足满足公式 (P + X - 1) % M =0
        int N = 0;
        X = M * (N + 1) - P + 1;
        while (X < 1) {
            X = M * (N++ + 1) - P + 1;
            if (X >= 1 && X <= M) {
                break;
            }
        }
//        //system.out.println("最终校验位：X={}"+X);
        return StrUtil.toString(str.charAt(X - 1));
    }
}
