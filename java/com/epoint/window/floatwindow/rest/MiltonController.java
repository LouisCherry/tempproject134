package com.epoint.window.floatwindow.rest;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.window.floatwindow.api.IFloatWindowService;
import com.epoint.window.floatwindow.api.entity.FloatWindow;

/**
 * [一句话功能简述]
 * 
 * @作者
 * @version
 */
@RestController
@RequestMapping("/floatwindow")
public class MiltonController
{

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IFloatWindowService service;

    @Autowired
    private IAttachService iAttachService;

    /**
     * 部门或窗口信息获取接口
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/getWindowList", method = RequestMethod.POST)
    public String getOuOrWindowList(@RequestBody String params) {
        log.info("----------开始调用getWindowList接口--------");
        try {
            JSONObject json = JSON.parseObject(params);
            // 身份验证
            String token = json.getString("token");
            if (!ZwdtConstant.SysValidateData.equals(token)) {
                log.info("----------调用getWindowList接口结束--------");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
            JSONObject obj = (JSONObject) json.get("params");
            String areacode = obj.getString("areacode");

            String type = obj.getString("type");

            JSONObject jsondata = new JSONObject();
            if (StringUtil.isNotBlank(type) && StringUtil.isNotBlank(areacode)) {
                List<Object> sqllist = new ArrayList<Object>();
                String sql = " ";
                if (StringUtil.isNotBlank(areacode)) {
                    sql += "and site = '" + areacode + "' ";
                }
                if (StringUtil.isNotBlank(type)) {
                    sql += "and type = '" + type + "' ";
                }
                sql += "and start = '1' ";
                sql += " order by sort ";

                List<FloatWindow> list = service.findList(ListGenerator.generateSql("float_window", sql, null, null),
                        sqllist.toArray());
                int count = service.countFloatWindow(ListGenerator.generateSql("float_window", sql), sqllist.toArray());
                jsondata.put("taotal", count);
                JSONArray jsonArray = new JSONArray();
                for (FloatWindow floatWindow : list) {
                    JSONObject jsonstr = new JSONObject();
                    jsonstr.put("name", floatWindow.getName());
                    jsonstr.put("url", floatWindow.getUrl());
                    String attachguid = "";
                    if (StringUtil.isNotBlank(floatWindow.getImgclientguid())) {
                        List<FrameAttachInfo> askattachList = iAttachService
                                .getAttachInfoListByGuid(floatWindow.getImgclientguid());
                        if (askattachList != null && !askattachList.isEmpty()) {
                            attachguid = askattachList.get(0).getAttachGuid();
                        }
                    }
                    jsonstr.put("imgattachguid", attachguid);

                    jsonstr.put("appmark", floatWindow.getAppmark());
                    jsonstr.put("sort", floatWindow.getSort());
                    jsonArray.add(jsonstr);
                }
                jsondata.put("list", jsonArray);

            }
            return JsonUtils.zwdtRestReturn("1", "获取成功", jsondata);
        }
        catch (Exception e) {
            log.error("异常信息:", e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

}
