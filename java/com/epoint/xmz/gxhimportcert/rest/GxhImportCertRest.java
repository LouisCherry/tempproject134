package com.epoint.xmz.gxhimportcert.rest;

import java.lang.invoke.MethodHandles;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.asutils.MobileAESEncDec;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmz.gxhimportcert.api.IGxhImportCertService;
import com.epoint.xmz.gxhimportcert.api.entity.GxhImportCert;

@RestController
@RequestMapping("/gxhimportcertsms")
public class GxhImportCertRest
{
    @Autowired
    private IGxhImportCertService service;

    private static final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 短信状态报告
     * 
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/pushStatus", method = RequestMethod.POST)
    public String pushStatus(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用pushStatus接口=======" + new Date());
            JSONObject resultJSON = new JSONObject();
            // 1、入参转化为JSON对象
            JSONObject paramsJson = JSONObject.parseObject(params);
            log.info("=======调用pushStatus接口入参：=======" + paramsJson.toJSONString());

            return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "成功", resultJSON.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("=======pushStatus接口参数：params【" + params + "】=======");
            log.error("=======pushStatus异常信息：" + e.getMessage() + "=======", e);
            return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取上行短信数据
     * 
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/pushSms", method = RequestMethod.POST)
    public String pushSms(@RequestBody String params) {
        try {
            log.info("=======开始调用pushSms接口=======" + new Date());
            JSONObject resultJSON = new JSONObject();
            SqlConditionUtil sql = new SqlConditionUtil();
            // 1、入参转化为JSON对象
            JSONObject paramsJson = JSONObject.parseObject(params);
            if (paramsJson != null) {
                String mobile = paramsJson.getString("mobile");
                String smsContent = paramsJson.getString("smsContent");
//                String sendTime=paramsJson.getString("sendTime");
//                String addSerial=paramsJson.getString("addSerial");
                if (StringUtil.isNotBlank(mobile)) {
                    sql.eq("mobile", mobile);
                    sql.eq("is_zb", "1");
                    sql.eq("smstatus", "1");
                    GxhImportCert cert = service.getCertByCondition(sql.getMap());
                    if (cert != null) {
                        if ("1".equals(smsContent)) {
                            cert.setSmstatus("2");
                        }
                        else {
                            cert.setSmstatus("3");
                        }
                        service.update(cert);
                    }

                }
            }

            return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "成功", resultJSON.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("=======pushSms接口参数：params【" + params + "】=======");
            log.error("=======pushSms异常信息：" + e.getMessage() + "=======", e);
            return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "失败：" + e.getMessage(), "");
        }
    }

}
