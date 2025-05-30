package com.epoint.wechat.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONObject;
import com.epoint.core.utils.string.StringUtil;

/**
 *  社保接口调用使用
 *  [移植智能化一体机设备的代码]
 * @作者 shibin
 * @version [版本号, 2019年1月3日]
 */
public class xmlUtil
{
    //private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    // 截取字符串
    public static String subString(String str, String strStart, String strEnd) {
        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);

        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strStart + ", 无法截取目标字符串";
        }
        if (strEndIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strEnd + ", 无法截取目标字符串";
        }
        /* 开始截取 */
        String result = str.substring(strStartIndex, strEndIndex).substring(strStart.length());
        return result;
    }

    // Doma4j解析xml字符串
    @SuppressWarnings("unchecked")
    public static String parsexml(String xmlString) {
        JSONObject rootJson = new JSONObject();
        try {
            Document document = DocumentHelper.parseText(xmlString);
            Element root = document.getRootElement();
            List<Element> data = root.element("d").elements("r");
            if (StringUtil.isNotBlank(data)) {
                List<JSONObject> datalist = new ArrayList<JSONObject>();
                for (int i = 0; i < data.size(); i++) {
                    List<Attribute> attr = data.get(i).attributes();
                    if (StringUtil.isNotBlank(attr)) {
                        JSONObject item = new JSONObject();
                        for (int j = 0; j < attr.size(); j++) {
                            item.put(attr.get(j).getName(), attr.get(j).getValue());
                        }
                        datalist.add(item);
                    }
                    else {
                        rootJson.put("info", "");
                    }
                }
                rootJson.put("info", datalist);
            }
            else {
                rootJson.put("info", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return rootJson.toJSONString();
    }

}
