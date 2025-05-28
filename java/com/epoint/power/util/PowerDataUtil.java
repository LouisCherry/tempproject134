package com.epoint.power.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.XML;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class PowerDataUtil {

    /**
     * 处理电力入参，转化对应xml入参为json
     *
     * @param params 入参
     * @return xml转化的json
     */
    public static JSONObject dealPowerRequestParam(String params) {
        JSONObject jsonObject = JSONObject.parseObject(params);
        String xmlContent = jsonObject.getJSONObject("data").getString("param");
        String xml2JSON = XML.toJSONObject(xmlContent).toString();
        return JSONObject.parseObject(xml2JSON);
    }

    public static String getXmlContent(String params) {
        JSONObject jsonObject = JSONObject.parseObject(params);
        return jsonObject.getJSONObject("data").getString("param");
    }


    /**
     * 构建三方接口请求信息Xml
     *
     * @param from    源单位id
     * @param batchId 批次号
     * @param dataId  统一查询号
     * @param data    基本信息数据
     * @param files   文件
     * @return xml
     */
    public static String buildXmlStr(String from, String batchId, String dataId, JSONObject data, JSONArray files, String typeId) {
        Document document = DocumentHelper.createDocument();
        Element item = document.addElement("item");
        document.setRootElement(item);

        item.addElement("FROM").setText(from);
        item.addElement("TO").setText("005117558");
        item.addElement("CC");
        item.addElement("BCC");
        item.addElement("PARAMS");
        item.addElement("TYPEID").setText(typeId);
        Element batchIdNode = item.addElement("BATCHID");
        batchIdNode.setText(batchId);
        // 数据信息
        Element subItemNode = item.addElement("DATALIST").addElement("item");
        item.addElement("PARAMS");

        subItemNode.addElement("DATAID").setText(dataId);
        subItemNode.addElement("RECVER").setText("0");
        // 字段信息节点
        Element textNode = subItemNode.addElement("TEXT");
        // 附件信息节点
        Element fileNode = subItemNode.addElement("STREAMINGDATAS");
        subItemNode.addElement("PARAMS");
        Element dataItemNode = textNode.addElement("item");
        data.forEach((k, v) -> dataItemNode.addElement(k).setText(v == null ? "" : v.toString()));
        // 添加材料清单节点
        Element blclqd = dataItemNode.addElement("BQCLQD");
        blclqd.addAttribute("isList", "1");

        Element sqclqd = dataItemNode.addElement("SQCLQD");
        sqclqd.addAttribute("isList", "1");


        // 文件
        if (CollectionUtils.isNotEmpty(files)) {
            files.forEach(file -> {
                JSONObject fileObj = (JSONObject) file;
                Element fileItemNode = fileNode.addElement("item");
                fileObj.forEach((k, v) -> fileItemNode.addElement(k).setText(v.toString()));
            });
        }
        return item.asXML();
    }

    public static String inputStream2Base64(InputStream is) {
        byte[] data = null;
        try {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = is.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            data = swapStream.toByteArray();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * base64 转 input流
     *
     * @param base64string base64字符串
     * @return 输入流
     */
    public static InputStream base2InputStream(String base64string) {
        ByteArrayInputStream stream = null;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] bytes1 = decoder.decodeBuffer(base64string);
            stream = new ByteArrayInputStream(bytes1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return stream;
    }
}
