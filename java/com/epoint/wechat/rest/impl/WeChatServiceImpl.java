package com.epoint.wechat.rest.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.wechat.rest.api.IWeChat;

@Component
@Service
public class WeChatServiceImpl implements IWeChat
{

    @Override
    public List<FrameOu> findFirstOu(String areacode, String keyword) {
        return new WeChatService().findFirstOu(areacode, keyword);
    }

    @Override
    public List<FrameOu> findOuByParentguid(String ouguid) {
        return new WeChatService().findOuByParentguid(ouguid);
    }

    @Override
    public List<AuditOrgaWindow> findWindowInfo(String ouguid) {
        return new WeChatService().findWindowInfo(ouguid);
    }

}
