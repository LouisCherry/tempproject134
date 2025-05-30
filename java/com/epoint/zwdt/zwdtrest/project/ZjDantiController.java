package com.epoint.zwdt.zwdtrest.project;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditsp.dantiinfo.api.IDantiInfoService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.zwdt.zwdtrest.project.api.IJnDantiinfoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.lang.invoke.MethodHandles;
import java.util.UUID;

@RestController
@RequestMapping("/zjdantirest")
public class ZjDantiController {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 附件API
     */
    @Autowired
    private IAttachService iAttachService;

    @Autowired
    private IJnDantiinfoService jnDantiinfoService;

    @Autowired
    private IDantiInfoService dantiInfoService;

    /**
     * 获取单体信息
     *
     * @return
     * @params params
     */
    @RequestMapping(value = "/getDantiinfo", method = RequestMethod.POST)
    public String getDantiinfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getDantiinfo接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String dtbm = obj.getString("dtbm");
            JSONObject dataJson = new JSONObject();
            if (StringUtil.isBlank(dtbm)) {
                return JsonUtils.zwdtRestReturn("0", "请传入单体编码", "");
            }
            DantiInfo dantiInfo = jnDantiinfoService.getDantiInfoByBm(dtbm);
            if (dantiInfo != null) {
                dataJson.put("dtmc", dantiInfo.getDantiname());
                dataJson.put("dtbm", dantiInfo.getStr("dtbm"));
            }
            log.info("=======结束调用getDantiinfo接口=======");
            return JsonUtils.zwdtRestReturn("1", " 单体查询成功！", dataJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "查询失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取落图附件
     *
     * @param params
     */

    @RequestMapping(value = "/saveDantiFile", method = RequestMethod.POST)
    public String getOuInfo(@RequestBody String params) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String dtbm = obj.getString("dtbm");
            String url = obj.getString("url");
            if (StringUtil.isBlank(dtbm)) {
                return JsonUtils.zwdtRestReturn("0", "请传入单体编码", "");
            }
            if (StringUtil.isBlank(url)) {
                return JsonUtils.zwdtRestReturn("0", "请传入url参数", "");
            }
            DantiInfo dantiInfo = jnDantiinfoService.getDantiInfoByBm(dtbm);
            String fjid = UUID.randomUUID().toString();
            if (dantiInfo != null) {
                dantiInfo.set("fjid", fjid);
                dantiInfo.set("zjurl", url);
                dantiInfoService.update(dantiInfo);
                return JsonUtils.zwdtRestReturn("1", "插入附件成功！", "");
            } else {
                return JsonUtils.zwdtRestReturn("0", "查询单体失败！", "");
            }
        } catch (Exception e) {
            log.info("【调用saveDantiFile异常】" + params);
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "插入附件失败！", e.getMessage());
        }
    }

}
