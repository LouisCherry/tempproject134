package com.epoint.auditproject.guiji.utils;

import cn.hutool.crypto.symmetric.SM4;

import java.nio.charset.StandardCharsets;

public class SM4Util {
    public static String encrypt(String keyStr, String data) {
        SM4 sm4 = new SM4(keyStr.getBytes(StandardCharsets.UTF_8));
        return sm4.encryptHex(data, StandardCharsets.UTF_8);
    }

    public static String decrypt(String keyStr, String data) {
        SM4 sm4 = new SM4(keyStr.getBytes(StandardCharsets.UTF_8));
        return sm4.decryptStr(data, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        System.out.println(decrypt("6nvjansgxsvvku7w", "8f383bad1ad82c3a419a02836f466433644d0e6b05f25d170bca4f048ba9cd25"));
    }
}
