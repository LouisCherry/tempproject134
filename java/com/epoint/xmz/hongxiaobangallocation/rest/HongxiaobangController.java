package com.epoint.xmz.hongxiaobangallocation.rest;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.xmz.hongxiaobangallocation.api.IHongxiaobangAllocationService;
import com.epoint.xmz.hongxiaobangallocation.api.entity.HongxiaobangAllocation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("hongxiaobangrest")
public class HongxiaobangController {

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IHongxiaobangAllocationService service;

    @Autowired
    private IAttachService iAttachService;


    /**
     * 红小帮信息展示
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/getinfo", method = RequestMethod.POST)
    public String getinfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getinfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                JSONObject dataJson = new JSONObject();
                List<JSONObject> attachlist = new ArrayList<>();
                HongxiaobangAllocation hongxiaobangAllocation = service.getAllocation();
                if (hongxiaobangAllocation != null) {
                    String cliengguid = hongxiaobangAllocation.getPictureguid();
                    List<FrameAttachInfo> list = iAttachService.getAttachInfoListByGuid(cliengguid);
                    if (!list.isEmpty()) {
                        for (FrameAttachInfo attachInfo : list) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("imgUrl", WebUtil.getRequestCompleteUrl(request)
                                    + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                    + attachInfo.getAttachGuid());
                            attachlist.add(jsonObject);
                        }
                        dataJson.put("teamImgList", attachlist);
                    }
                    dataJson.put("teamInfo", hongxiaobangAllocation.getContent());
                }
                log.info("=======结束调用getinfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "配置查询成功！", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getinfo接口参数：params【" + params + "】=======");
            log.info("=======getinfo接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "配置查询异常：" + e.getMessage(), "");
        }
    }

}
