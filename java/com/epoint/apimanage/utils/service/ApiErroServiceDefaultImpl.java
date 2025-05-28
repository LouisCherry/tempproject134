package com.epoint.apimanage.utils.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.apimanage.log.entity.ApiManageLog;
import com.epoint.apimanage.utils.api.IApiErroInfoSendService;
import com.epoint.apimanage.utils.api.ICommonDaoService;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.user.api.IUserService;

import com.epoint.frame.service.organ.user.entity.FrameUser;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 默认实现
 */
@Component
@Service
public class ApiErroServiceDefaultImpl implements IApiErroInfoSendService {

    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void sendErroMsg(ApiManageLog apiManageLog) {
        IMessagesCenterService iMessagesCenterService = ContainerFactory.getContainInfo().getComponent(IMessagesCenterService.class);
        IUserService iUserService = ContainerFactory.getContainInfo().getComponent(IUserService.class);
        IConfigService iConfigService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        // 获取需要通知的人 多个,隔开
        String loginids = iConfigService.getFrameConfigValue("apierroremideusers");
        if (StringUtil.isBlank(loginids)) {
            log.error("为配置接口异常提醒人！请配置系统参数apierroremideusers");
            return;
        }
        String content = apiManageLog.getStr("apiname") + "请求发生异常，响应状态为：" + apiManageLog.getStatus()
                + "对应的日志记录： "+ apiManageLog.getRowguid() +"请及时处理！";
        String[] loginarr = loginids.split(",");
        for (String s : loginarr) {
            FrameUser user = iUserService.getUserByUserField("loginid", s);
            if (user != null) {
//            	log.info("apimanager手机号："+user.getMobile());
                // 发送短信提醒
                iMessagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0,
                        new Date(), user.getMobile(), user.getUserGuid(),
                        user.getDisplayName(), user.getUserGuid(),
                        user.getDisplayName(), "", "", "", false, "370800");
                // 发送消息提醒
                iMessagesCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(), content, IMessagesCenterService.MESSAGETYPE_READ,
                        user.getUserGuid(), user.getDisplayName(), user.getUserGuid(), user.getDisplayName(), "", "",
                        user.getOuGuid(), user.getOuGuid(), 1, "", "", "", "", new Date(),
                        "", "", "", "");
            }

        }


    }
}
