package com.epoint.auditproject.guiji.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.ECKeyUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;


public class SM2Util {


    /**
     * sm2 加密
     * @param publicKey 提供的秘钥
     * @param data      需要加密的数据
     * @return 加密后字符串
     */
    public static String encryptData(String publicKey,String data) {
        SM2 sm2 = SmUtil.sm2(null, ECKeyUtil.toSm2PublicParams(publicKey));
        String encryptBcd = sm2.encryptBcd(data, KeyType.PublicKey);
        // 这里的处理前端也可以处理，这个就看怎么约定了，其实都无伤大雅
        if (StrUtil.isNotBlank(encryptBcd)) {
            // 生成的加密密文会带04，因为前端sm-crypto默认的是1-C1C3C2模式，这里需去除04才能正常解密
            if (encryptBcd.startsWith("04")) {
                encryptBcd = encryptBcd.substring(2);
            }
            // 前端解密时只能解纯小写形式的16进制数据，这里需要将所有大写字母转化为小写
            encryptBcd = encryptBcd.toLowerCase();
        }
        return encryptBcd;
    }

    public static void main(String[] args) {
        System.out.println(encryptData("046289ee9301c092405d0d6f46bed92372fdd6016a52c1a5b5c21a9cbff6ee54dbce63663522f30b243b9d9a38264d890f0cf2e277c7d7c9d2a2d38ee4095f70fb", "{\"datasourcessysname\":\"济宁市政务服务平台\",\"regioncode\":\"370800000000\",\"submit\":\"2\",\"systemlevel\":\"3\",\"isagency\":\"0\",\"taskname\":\"大件运输“一件事”\",\"deptcode\":\"11370800MB28559184\",\"gotourl\":\"http://jizwfw.sd.gov.cn/jnzwdt\",\"contactname\":\"杨士超\",\"applicantmobile\":\"15063176937\",\"isdraft\":\"0\",\"appmark\":\"JNSZWFWW\",\"applicantpagecode\":\"370827199511062018\",\"regionname\":\"济宁市\",\"applicanttype\":\"2\",\"taskcode\":\"37TCP0003A24J0800\",\"applytime\":\"2025-01-24 10:37:38\",\"deptname\":\"济宁市行政审批服务局\",\"applicant\":\"杨士超\",\"projectno\":\"37TCP0003A24J080025000002\",\"datasources\":\"1\",\"taskversion\":\"01\",\"businessmodel\":\"1\",\"catalogcode\":\"37TCP0003\",\"applicantpagetype\":\"111\"}"));
    }
}

