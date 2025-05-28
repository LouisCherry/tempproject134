package com.epoint.jiningzwfw.teacherhealthreport.api;

import java.io.Serializable;
import java.util.Map;

import com.epoint.frame.service.attach.entity.FrameAttachInfo;

/**
 * 处理附件的接口
 * 
 * @author liuhui
 * @version 2022年5月20日
 */
public interface IMyAttachService extends Serializable
{
    public FrameAttachInfo getFrameAttachInfoByCondition(Map<String, String> map);
}
