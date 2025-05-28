package com.epoint.jnznsb.shebao.selfservicemachine;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.XML;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.log.LogUtil;

@RestController
@RequestMapping(value = "/yibao")
public class YiBaoRestController
{
    transient Logger log = LogUtil.getLog(SheBaoRestController.class);
    public static final String licenseKey = "3f8a85b7-551d-4922-9482-eaf7120f0885";

    /**
     * 
     * 1 医疗缴费历史查询
     */
    @RequestMapping(value = "/yiliaojiaofeihistory", method = RequestMethod.POST)
    public String YiLiaoJiaoFeiHistory(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String sfzhm = obj.getString("sfzhm");

            String qsny = obj.getString("qsny");
            String zzny = obj.getString("zzny");
            String nypdbz = obj.getString("nypdbz");

            DwPspProxy.getInstance().setDwPspServerUrl("http://10.79.64.16:8080/dwpspserver");

            String serviceName = "HcService_3778";
            String operationName = "getPsnMediClctHis";
            String xmlPara = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>" + "<s sfzhm=\"" + sfzhm + "\"/><s nypdbz=\""
                    + nypdbz + "\"/><s qsny=\"" + qsny + "\"/><s zzny=\"" + zzny + "\"/><s nypdbz=\"0\"/></p>";

            String xmlresult = DwPspProxy.getInstance().invokeService(licenseKey, serviceName, operationName, xmlPara);

            JSONObject dataJson = new JSONObject();
            org.json.JSONObject xmlJSONObj = XML.toJSONObject(xmlresult);

            log.info("============医疗缴费历史查询==========" + xmlJSONObj);

            org.json.JSONObject p = xmlJSONObj.getJSONObject("p");

            JSONArray s = p.getJSONArray("s");

            //先判断当前接口是否有问题
            int errflagindex = getErrFlagIndex(s, "errflag");

            int errtextindex = getErrFlagIndex(s, "errtext");

            if (Integer.parseInt(s.getJSONObject(errflagindex).get("errflag").toString()) == 0) {
                //无问题
                dataJson.put("errflag", 0);
                org.json.JSONObject d = p.getJSONObject("d");
                JSONArray r = new JSONArray();

                try {
                    r = d.getJSONArray("r");
                }
                catch (Exception a) {

                }

                JSONObject dataJsondata = new JSONObject();
                List<JSONObject> list = new ArrayList<JSONObject>();
                for (int i = 0; i < r.length(); i++) {
                    dataJsondata = new JSONObject();

                    dataJsondata.put("xzbz", r.getJSONObject(i).get("xzbz"));
                    dataJsondata.put("jflslbmc", r.getJSONObject(i).get("jflslbmc"));
                    dataJsondata.put("dwmc", r.getJSONObject(i).get("dwmc"));
                    dataJsondata.put("jbrq", r.getJSONObject(i).get("jbrq"));

                    dataJsondata.put("jflslb", r.getJSONObject(i).get("jflslb"));
                    dataJsondata.put("jfrq", r.getJSONObject(i).get("jfrq"));
                    dataJsondata.put("grjfjs", r.getJSONObject(i).get("grjfjs"));
                    dataJsondata.put("dwjfe", r.getJSONObject(i).get("dwjfe"));

                    dataJsondata.put("dwbh", r.getJSONObject(i).get("dwbh"));
                    dataJsondata.put("jfny", r.getJSONObject(i).get("jfny"));
                    //dataJsondata.put("fsyymc", r.getJSONObject(i).get("fsyymc"));
                    dataJsondata.put("xzbzmc", r.getJSONObject(i).get("xzbzmc"));

                    dataJsondata.put("dwjfjs", r.getJSONObject(i).get("dwjfjs"));
                    dataJsondata.put("fsrq", r.getJSONObject(i).get("fsrq"));
                    dataJsondata.put("fsyy", r.getJSONObject(i).get("fsyy"));
                    dataJsondata.put("grjfe", r.getJSONObject(i).get("grjfe"));
                    list.add(dataJsondata);
                }
                dataJson.put("_lesb__text", list);
            }
            else {
                dataJson.put("errflag", 1);
                dataJson.put("_lesb__text", s.getJSONObject(errtextindex).get("errtext"));
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 2 职工医疗账户查询
     */
    @RequestMapping(value = "/zhigongyiliaoaccount", method = RequestMethod.POST)
    public String ZhiGongYiLiaoAccount(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String sfzhm = obj.getString("sfzhm");

            String qsrq = obj.getString("qsrq");
            String zzrq = obj.getString("zzrq");

            DwPspProxy.getInstance().setDwPspServerUrl("http://10.79.64.16:8080/dwpspserver");

            String serviceName = "SiServiceMedi";
            String operationName = "getCardTransItem";
            String xmlPara = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>" + "<s sfzhm=\"" + sfzhm + "\"/><s qsrq=\""
                    + qsrq + "\"/><s zzrq=\"" + zzrq + "\"/></p>";

            String xmlresult = DwPspProxy.getInstance().invokeService(licenseKey, serviceName, operationName, xmlPara);

            JSONObject dataJson = new JSONObject();
            org.json.JSONObject xmlJSONObj = XML.toJSONObject(xmlresult);

            log.info("============职工医疗账户查询==========" + xmlJSONObj);
            org.json.JSONObject p = xmlJSONObj.getJSONObject("p");
            //system.out.println(p);
            JSONArray s = p.getJSONArray("s");

            //先判断当前接口是否有问题
            int errflagindex = getErrFlagIndex(s, "errflag");

            int errtextindex = getErrFlagIndex(s, "errtext");

            if (Integer.parseInt(s.getJSONObject(errflagindex).get("errflag").toString()) == 0) {
                //无问题
                dataJson.put("errflag", 0);
                org.json.JSONObject d = p.getJSONObject("d");

                dataJson.put("_lesb__text", d.get("k"));
            }
            else {
                dataJson.put("errflag", 1);
                dataJson.put("_lesb__text", s.getJSONObject(errtextindex).get("errtext"));
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    //根据属性名称找Index
    int getErrFlagIndex(JSONArray data, String key) {

        for (int i = 0; i < data.length(); i++) {

            if (data.getJSONObject(i).keys().next().equals(key)) {
                return i;
            }
        }
        return 0;
    }

}
