package com.epoint.auditselfservice.auditselfservicerest.ShebaoLs.rest;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.znsb.jnzwfw.selfservicemachine.DwPspProxy;

@RestController
@RequestMapping({"/shebaols" })
public class ShebaoLsRestController
{
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @RequestMapping(value = {"/setcardls" }, method = {RequestMethod.POST })
    public String setcardls(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String sfzhm = obj.getString("sfzhm");
            DwPspProxy.getInstance().setDwPspServerUrl("http://10.156.80.1:8080/dwpspserver");
            String licenseKey = "3f8a85b7-551d-4922-9482-eaf7120f0885";
            String serviceName = "ScmsCardService";
            String operationName = "scCardLsgs";
            String xmlPara = "<?xml version=\"1.0\" encoding=\"GBK\"?><p><s sfzhm=\"" + sfzhm
                    + "\"/><s xtlb=\"11\"/><s jbr=\"政务平台\"/></p>";
            String xmlresult = DwPspProxy.getInstance().invokeService(licenseKey, serviceName, operationName, xmlPara);
            // String xmlresult = "<p><s errflag=\"1\"/><s
            // errtext=\"身份证号码为320882199703311619的人没有可以临时挂失挂失的社保卡\"/><s
            // _lesb__errcode_=\"0\"/></p>";
            log.info("=======setcardls**xmlresult【" + xmlresult + "】=======");
            return JsonUtils.zwdtRestReturn("1", "调用成功", parsexml(xmlresult));
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e, "");
        }
    }

    public static String parsexml(String xmlString) {
        JSONObject rootJson = new JSONObject();
        try {
            Document document = DocumentHelper.parseText(xmlString);
            List<Attribute> sAttrList = null;
            Element root = document.getRootElement();
            List<Element> sElementList = root.elements("s");
            if (!sElementList.isEmpty()) {
                for (Element sElement : sElementList) {
                    sAttrList = sElement.attributes();
                    for (Attribute attribute : sAttrList) {
                        rootJson.put(attribute.getName(), attribute.getValue());
                    }
                }
            }
            List<Element> rElementsList = null;
            List<Attribute> rAttrList = null;
            JSONObject item = null;
            List<JSONObject> datalist = new ArrayList<JSONObject>();
            Element dElement = root.element("d");
            if (dElement != null) {
                rElementsList = dElement.elements("r");
            }
            if (rElementsList != null && !rElementsList.isEmpty()) {
                for (Element rElement : rElementsList) {
                    rAttrList = rElement.attributes();
                    if (!rAttrList.isEmpty()) {
                        item = new JSONObject();
                        for (Attribute rAttr : rAttrList) {
                            item.put(rAttr.getName(), rAttr.getValue());
                        }
                        datalist.add(item);
                    }
                }
            }
            rootJson.put("info", datalist);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return rootJson.toJSONString();
    }
}
