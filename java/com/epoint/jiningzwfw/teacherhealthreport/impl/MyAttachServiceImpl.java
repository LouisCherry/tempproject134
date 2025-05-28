package com.epoint.jiningzwfw.teacherhealthreport.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.jiningzwfw.teacherhealthreport.api.IMyAttachService;

/**
 * 处理附件的接口
 * 
 * @author liuhui
 * @version 2022年5月13日
 */
@Component
@Service
public class MyAttachServiceImpl implements IMyAttachService
{

    /**
     * 
     */
    private static final long serialVersionUID = 4483893401813086327L;

    @Override
    public FrameAttachInfo getFrameAttachInfoByCondition(Map<String, String> map) {
        return new MyAttachService().getFrameAttachInfoByCondition(map);
    }

}
