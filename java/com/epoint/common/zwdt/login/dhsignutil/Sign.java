package com.epoint.common.zwdt.login.dhsignutil;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;


/**
 * 
 * @Description:获取验签工具类
 * @author yuqing
 * @date 2019年12月19日下午2:56:32
 *
 */
public class Sign {

    public static void main(String[] args) {
        // 设置参数
        Map<String, String> map = new HashMap<>(16);

        // 应用唯一标识（JMAS平台获取）
        map.put("app_id", "appId");

        // 接口唯一标识（JMAS平台获取）
        map.put("interface_id", "interfaceId");

        // 版本号(格式:x.x, 例如:1.0)
        map.put("version", "1.0");

        // 三方接口的参数，json格式：{"键":"值", "键":"值"}
        Map<String, String> paramMap = new HashMap<>(16);
        paramMap.put("name", "hanweb");
        String bizContent = JSON.toJSONString(paramMap);
        map.put("biz_content", bizContent);

        // 编码格式, 默认UTF-8
        String charset = "UTF-8";
        map.put("charset", charset);

        // 时间戳，毫秒为单位
        map.put("timestamp", System.currentTimeMillis() + "");
        System.out.println(System.currentTimeMillis() + "");

        // 来源 0：PC；1：APP；2：支付宝；3：微信
        map.put("origin", "0");

        // 应用私钥（在平台应用详情处复制）
        String privateKey =
            "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAL1xPmciI6W2grfir5lWykikigo0Tw+PdbwIcPyluxfg9EcCeii3XzUhCpbEtSp7Jy6hqwTPge8bywSg5BAGvON02uBY5DN6y2Pqfu4dsIMCGwPHI2FQlu/88EAJBv6esYFFIsdr45uBG9Wya/je+vpMJ3E4A9tgP76v6/1bK2zLAgMBAAECgYAu5Px8jS/j0oUTPfMX8ysJxmlBU2eKw4lybWiCsgfZRl9RwKQ6tgHEZhR38+Ogy3GMkoqCG1fft7KOx8EM0o4pFvhpHohDXDmEKHYDJvRkQUoGurmN6Qdatt03MT4EKMBI087O/CMplc9lmPPJNbQqyWZ884LSpSH6He75yc1nUQJBAN56EBwJEBLE5d4ZnNbfAdlmftTqobqT2ZlmQz5/ucGMOCW1WcoSFeVlYRogiIZE6srIOEDxbpRIm8IXhClk+cMCQQDZ/OSEGNMD1vTvkBp1C56D1KGnb3q6dbfIrK/bKEREk7SVY6HRUGnuMgXQNpM2dmVjJhfXwIouD2sDhq6dC4hZAkAR4Fj/B7Nk7rFRwVka4txxLY0vapMIPO0VKGRq1zSD2tKAJSxL0lw0DIta4BZYQ51iIehzP3MVMyhg1ibmdJilAkBI+EC+f1jTpjcjokdY+uS7GIhsdgLNO+6jfDr5z8badd8zSsC2QobTN2d+xWYOCx/xSqUckdUvifW2nnUBGQUxAkB/x4A4JUBQCzVVcDVqsqgDIrsWqvHJRQOCUDA1rYvKPVy0lx2LTkhOL4/WfEr2nPOz7Yh+WgDlOZSq9Eanxv/N";

        String sign = createsign(map, privateKey, charset);
        System.out.println(sign);
    }

    public static String createsign(Map<String, String> map, String privateKey, String charset) {
        // 获取sign
        String sign = SignatureUtil.rsaSign(map, privateKey, charset, "RSA");
        // 返回sign
        return sign;
    }
}
