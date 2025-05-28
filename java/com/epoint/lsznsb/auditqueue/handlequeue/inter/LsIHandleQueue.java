package com.epoint.lsznsb.auditqueue.handlequeue.inter;

import java.util.Map;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;

@Service
public interface LsIHandleQueue
{
    /**
     * 取号
     * 
     * @param sfz
     *            身份证
     * @param phone
     *            手机号
     * @param taskguid
     *            事项guid
     * @param centerguid
     *            中心guid
     * @param hallguid
     *            大厅guid
     * @param islove
     *            是否是爱心号
     * @return
     */
    public AuditCommonResult<Map<String, String>> getQNO(String sfz, String phone, String taskguid, String centerguid,
            String hallguid, String islove);

    /**
     * 下一位
     * 
     * @param WindowGuid
     *            窗口guid
     * @param WindowNo
     *            窗口no
     * @param CenterGuid
     *            中心guid
     * @param UseCall
     *            是否发送短信
     * @return
     */
    public AuditCommonResult<String> getNextQNO(String WindowGuid, String WindowNo, String CenterGuid, String UserGuid,
            Boolean UseCall);
}
