package com.epoint.znsb.jnzwfw.selfservicemachine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;

public class JnSelfservicemachineSignUtil
{

    //1对所有请求的参数进行排序
    //排序方法。参数source ：所有请求的参数存放的map
    protected static Map<String, Object> sortMapByKey(Map<String, Object> source) {
        if (source == null || source.isEmpty()) {
            return new HashMap<String, Object>();
        }
        Map<String, Object> dest = new TreeMap<String, Object>(new Comparator<String>()
        {
            @Override
            public int compare(String key1, String key2) {
                return key1.compareTo(key2);
            }
        });
        dest.putAll(source);
        return dest;
    }

    //2对排序后的参数进行拼接，生成字符串后再md5加密,加密后的字符串即签名字符串sign
    public static String getSign(Map<String, Object> values, String key) {
        Map<String, Object> sorts = sortMapByKey(values);// 排序 Map
        StringBuilder content = new StringBuilder();
        for (String k : sorts.keySet()) {
            Object o = sorts.get(k);
            if (o != null) {
                Object os = o;
                if (os.getClass().isArray()) {
                    int length = Array.getLength(os);
                    List<Object> rl = new ArrayList();
                    for (int i = 0; i < length; i++) {
                        rl.add(Array.get(os, i));
                    }
                    if (rl != null && !"sign".equals(k)) {
                        content.append(k).append("=").append(rl).append("&");
                    }
                }
                else {
                    String value = os.toString();
                    if (value != null && !"".equals(value) && !"sign".equals(k)) {
                        content.append(k).append("=").append(value).append("&");
                    }
                }

            }
        }
        // 最后拼接平台提供的密钥
        content.append("key=").append(key);
        return DigestUtils.md5Hex(content.toString()).toUpperCase();
    }

}
