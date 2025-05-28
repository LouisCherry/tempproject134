package com.epoint.yczwfw.auditqueue.windowled.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.gxqzwfw.auditqueue.windowled.domain.WindowLed;
import com.epoint.yczwfw.auditqueue.windowled.inter.IWindowLed;

@RestController
@RequestMapping("/windowled")
public class WindowLEDRestController
{
    @Autowired
    private IWindowLed windowledservice;
 
    
    @RequestMapping(value = "/queuemsg", method = RequestMethod.POST)
    public String queuemsg(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String windownostr = obj.getString("queueneme");
            JSONObject dataJson =new JSONObject();
            List<WindowLed> windowsinfolist = new ArrayList<WindowLed>();

            if (StringUtil.isNotBlank(windownostr)) {
                windowsinfolist=windowledservice.findlist(windownostr);
                for(WindowLed WINDOW :windowsinfolist){
                   String sql = "select windowname from audit_orga_window where WINDOWNO='"+WINDOW.getWindowno()+"' and  CenterGuid='d26c7632-c6a5-4cfb-8d9f-c7e9befca400' ";
                   AuditOrgaWindow widown = CommonDao.getInstance().find(sql, AuditOrgaWindow.class);
                   if(widown!=null){
                       WINDOW.put("windowname", widown.getWindowname());
                   }
                }
                dataJson.put("list", windowsinfolist);
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "参数不能为空", "");
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public String insert(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String windowno = obj.getString("windowno");
            String content = obj.getString("content");
            JSONObject dataJson =new JSONObject();

            if (StringUtil.isNotBlank(windowno)&&StringUtil.isNotBlank(content)) {
                windowledservice.inset(windowno, content);
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "参数不能为空", "");
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
   

}
