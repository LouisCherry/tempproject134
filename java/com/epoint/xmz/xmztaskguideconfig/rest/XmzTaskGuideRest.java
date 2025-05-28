package com.epoint.xmz.xmztaskguideconfig.rest;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.OfficeWebUrlEncryptUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.xmz.xmztaskguideconfig.api.IXmzTaskguideConfigService;
import com.epoint.xmz.xmztaskguideconfig.api.entity.XmzTaskguideConfig;

@RestController
@RequestMapping("/xmztaskguide")
public class XmzTaskGuideRest
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IXmzTaskguideConfigService iXmzTaskguideConfigService;
    @Autowired
    private IAttachService iAttachService;

    @RequestMapping(value = "/getTaskList", method = RequestMethod.POST)
    public String getTaskList(@RequestBody String params) {
        try {
            log.info("=======开始调用getTaskList接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            // 2.校验token
            String checkToken = JsonUtils.checkUserAuth(jsonObject.getString("token"));
            if (StringUtil.isNotBlank(checkToken)) {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", checkToken);
            }
            JSONObject json = jsonObject.getJSONObject("params");
            String pagenum = json.getString("pagenum");
            String pagesize = json.getString("pagesize");
            String areacode = json.getString("areacode");
            int pageNum = 0;
            int pageSize = 10;
            if (StringUtil.isNotBlank(pagenum) && StringUtil.isNotBlank(pagesize) && StringUtil.isNotBlank(areacode)) {
                pageNum = Integer.valueOf(pagenum);
                pageSize = Integer.valueOf(pagesize);
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "传入参数为空！", params);
            }
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("areacode", areacode);
            List<XmzTaskguideConfig> taskGuideList = iXmzTaskguideConfigService.selectTaskGuidefigList(sql.getMap(),
                    pageNum, pageSize);
            JSONObject returnObject = new JSONObject();
            List<Record> list = new ArrayList<>();
            if (taskGuideList != null && !taskGuideList.isEmpty()) {
                for (XmzTaskguideConfig xmzTaskguideConfig : taskGuideList) {
                    Record record = new Record();
                    record.put("taskname", xmzTaskguideConfig.getTaskname());
                    record.put("taskid", xmzTaskguideConfig.getTaskid());
                    record.put("rowguid", xmzTaskguideConfig.getRowguid());
                    list.add(record);
                }
            }
            returnObject.put("list", list);
            returnObject.put("total", list.size());
            return JsonUtils.zwdtRestReturn("1", "获取成功！", returnObject.toJSONString());
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskList接口参数：params【" + params + "】=======");
            log.info("=======getTaskList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取失败！", e.getMessage());
        }
    }

    @RequestMapping(value = "/getTaskguide", method = RequestMethod.POST)
    public String getTaskguide(@RequestBody String params) {
        try {
            log.info("=======开始调用getTaskguide接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            // 2.校验token
            String checkToken = JsonUtils.checkUserAuth(jsonObject.getString("token"));
            if (StringUtil.isNotBlank(checkToken)) {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", checkToken);
            }
            JSONObject json = jsonObject.getJSONObject("params");
            String taskguid = json.getString("taskguid");
            XmzTaskguideConfig xmzTaskguideConfig = iXmzTaskguideConfigService.find(taskguid);
            JSONObject returnObject = new JSONObject();
            if (xmzTaskguideConfig != null) {
                FrameAttachInfo frameAttachInfo = iAttachService
                        .getAttachInfoListByGuid(xmzTaskguideConfig.getGuidecliengguid()).get(0);

                // 附件guid
                returnObject.put("attachguid", frameAttachInfo.getAttachGuid());
                // 下载地址
                returnObject.put("downloadurl",
                        "http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                + frameAttachInfo.getAttachGuid());
                // 预览地址
                String preSys = "http://59.206.96.143:25990/jnzwfw/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                        + frameAttachInfo.getAttachGuid();
                String officeweb365url = ConfigUtil.getConfigValue("officeweb365url");
                String preViewUrl = officeweb365url
                        + OfficeWebUrlEncryptUtil.getEncryptUrl(preSys, frameAttachInfo.getContentType());
                returnObject.put("preViewUrl", preViewUrl);
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "获取办事指南失败！", "");
            }
            return JsonUtils.zwdtRestReturn("1", "获取成功！", returnObject);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskguide接口参数：params【" + params + "】=======");
            log.info("=======getTaskguide异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取失败！", e.getMessage());
        }
    }
}
