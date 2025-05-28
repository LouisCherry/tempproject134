package com.epoint.increase.office365.rest;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.authentication.UserSession;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

public class FsEformUtil
{
    /**
     * 
     * [获取当前表单业务数据]
     * 
     * @param rowguid
     * @param formid
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static String getPageData(String rowguid, String formid, String epointsformurl) {

        Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
        log.info("----------------开始获取当前表单业务数据--------------");
        String returnData = "";
        try {
            if (StringUtil.isBlank(epointsformurl)) {
                log.info("----------------新版电子表单地址没有配置，请前往系统参数epointsformurl中配置-------------");
                return "";
            }
            // 获取电子表单数据
            epointsformurl += "/rest/sform/getPageData";
            JSONObject json = new JSONObject();
            json.put("tableName", "");
            json.put("rowGuid", rowguid);
            json.put("formId", formid);
            Map<String, String> params = new HashMap<String, String>();
            params.put("params", json.toJSONString());
            returnData = HttpUtil.doHttp(epointsformurl, null, params, "post", HttpUtil.RTN_TYPE_STRING);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        log.info("----------------结束获取当前表单业务数据--------------");
        return returnData;
    }
    /**
     * 
     * [新增修改表单业务数据]
     * 
     * @param rowguid
     * @param formid
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static String savePageData(String rowguid, String formid, String epointsformurl,JSONObject paramsInside) {
        Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
        log.info("----------------新增修改当前表单业务数据--------------");
        String returnData = "";
        try {
            if (StringUtil.isBlank(epointsformurl)) {
                log.info("----------------新版电子表单地址没有配置，请前往系统参数epointsformurl中配置-------------");
                return "";
            }
            String tempReturnData=getEpointSformInfo(formid, epointsformurl);
            JSONObject tempOb=JSONObject.parseObject(tempReturnData);
            String sqltablename=tempOb.getJSONObject("custom").getJSONObject("formData").getJSONObject("formInfo").getString("sqltablename");
            // 获取电子表单数据
            epointsformurl += "/rest/sform/savePageData";
            paramsInside.put("tableName", sqltablename);
            paramsInside.put("rowGuid", rowguid);
            paramsInside.put("formId", formid);
            String identityNum=null;
            UserSession userSession=UserSession.getInstance();
            if(userSession!=null) {
                identityNum=userSession.getDisplayName();
            }
            paramsInside.put("identityNum", identityNum);
            JSONObject params = new JSONObject();
            params.put("params", paramsInside);
            returnData = HttpUtil.doHttp(epointsformurl, null, params, "post", HttpUtil.RTN_TYPE_STRING);
        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        log.info("----------------新增修改当前表单业务数据--------------");
        return returnData;
    }
    
    
    /**
     * 
     * [获取表单基本信息]
     * 
     * @param formid
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static String getEpointSformInfo(String formid, String epointsformurl) {
        Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
        log.info("----------------获取表单基本信息--------------");
        String returnData = "";
        try {
            if (StringUtil.isBlank(epointsformurl)) {
                log.info("----------------新版电子表单地址没有配置，请前往系统参数epointsformurl中配置-------------");
                return "";
            }
            // 获取电子表单数据
            epointsformurl += "/rest/sform/getEpointSformInfo";
            //paramsInside.put("tableName", "");
            JSONObject paramsInside = new JSONObject();
            paramsInside.put("formId", formid);
            JSONObject params = new JSONObject();
            params.put("params", paramsInside);
            returnData = HttpUtil.doHttp(epointsformurl, null, params, "post", HttpUtil.RTN_TYPE_STRING);
            
        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        log.info("----------------获取表单基本信息--------------");
        return returnData;
    }
}
