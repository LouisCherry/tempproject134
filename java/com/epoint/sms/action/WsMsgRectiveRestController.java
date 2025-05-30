package com.epoint.sms.action;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.frame.service.message.api.IMessagesCenterService;

@RestController
@RequestMapping("/wsmsg")
public class WsMsgRectiveRestController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    
    /**
     * 待办消息API
     */
    @Autowired
    private IMessagesCenterService iMessagesCenterService;

   

    /**
     * 验证码发送接口
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    public String sendCode(@RequestBody String params) {
        try {
            log.info("=======开始调用sendCode接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取手机号
                String mobile = obj.getString("mobile");
                // 1.1、获取手机号
                String type = obj.getString("type");
                // 短信所属区县
                String messageType = "370830";
                // 1.3、短信内容
                String code = obj.getString("code");
                String targetUser = "大厅注册";
                String targetDisplayName = "大厅注册";
                // 5.2、往messages_center表里插数据
                String content = "";//短信内容
                if ("1".equals(type)) {
                	content = "";
                }
                else if("2".equals(type)) {
                	content = "";
                }
                iMessagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0, null,
                        mobile, targetUser, targetDisplayName, "", "", "", "", null, false, messageType);
                log.info("=======结束调用sendCode接口=======");
                return JsonUtils.zwdtRestReturn("1", "验证码发送成功！", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======sendCode接口参数：params【" + params + "】=======");
            log.info("=======sendCode异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "验证码发送出现异常：" + e.getMessage(), "");
        }
    }

}
