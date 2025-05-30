package com.epoint.zwdt.zwdtrest.areacode;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.string.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.lang.invoke.MethodHandles;
import java.net.URLEncoder;

/**
 * 用户信息设置相关接口
 *
 * @version [F9.3, 2018年1月16日]
 * @作者 WST
 */
@RestController
@RequestMapping("/jnzwdtareacode")
public class JnzwdtareacodeController {

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * 代码项API
     */
    @Autowired
    private IAuditOrgaArea IauditOrgaAreService;

    /**
     * 获取个人信息的接口
     *
     * @param params 接口入参
     * @return
     */
    @RequestMapping(value = "/getwzdsnumbyareacode", method = RequestMethod.POST)
    public String getWzdsnumbyareacode(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getAccountInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String aracode = obj.getString("areacode");
                AuditOrgaArea area = IauditOrgaAreService.getAreaByAreacode(aracode).getResult();
                if (StringUtil.isNotBlank(area)) {
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("wzdsnum", area.get("wzds_num"));
                    return JsonUtils.zwdtRestReturn("1", "获取网站大师编码成功", dataJson.toString());
                } else {
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("wzdsnum", "370800");
                    return JsonUtils.zwdtRestReturn("1", "获取网站大师编码成功", dataJson.toString());
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "网站大师栏目异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取公告接口
     *
     * @param params 接口入参
     * @return
     */
    @RequestMapping(value = "/webBuilderInfoList", method = RequestMethod.POST)
    public String webBuilderInfoList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用webBuilderInfoList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String url = "http://112.6.110.176:28080/epoint-web-wzds/rest/webBuilderWebServiceForMicroPortalImpl/getInfoList";
                url += "?params=" + URLEncoder.encode(obj.toJSONString());
                String result = HttpUtil.doPostJson(url, null, null);
                return JsonUtils.zwdtRestReturn("1", "获取公告列表成功！", result);
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "网站大师栏目异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取公告详情接口
     *
     * @param params 接口入参
     * @return
     */
    @RequestMapping(value = "/webBuilderInfoDetail", method = RequestMethod.POST)
    public String webBuilderInfoDetail(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用webBuilderInfoDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String url = "http://112.6.110.176:28080/epoint-web-wzds/rest/webBuilderWebServiceForMicroPortalImpl/getInfoDetail";
                url += "?params=" + URLEncoder.encode(obj.toJSONString());
                String result = HttpUtil.doPostJson(url, null, null);
                return JsonUtils.zwdtRestReturn("1", "获取公告详情成功！", result);
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取网站大师详情异常：" + e.getMessage(), "");
        }
    }

}
