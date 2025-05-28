package com.epoint.composite.auditqueue.jxhandlequeue.inter;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.common.service.AuditCommonResult;

import java.util.Date;
import java.util.Map;

@Service
public interface ISSHandleQueue
{

    /**
     * 获取事项等待人数
     * @param TaskGuid 事项guid
     * @param redis 是否使用redis
     * @return
     */
    public AuditCommonResult<Integer> getTaskWaitNum(String TaskGuid, Boolean redis);

    /**
     * 取预约号
     * @param appointguid
     * @param centerguid
     * @return
     */
    public AuditCommonResult<Map<String, String>> getAPQNO(String appointguid, String centerguid);

    /**
     * 下一位
     * @param WindowGuid
     * @param WindowNo
     * @param CenterGuid
     * @param UseCall
     * @return
     */
    public AuditCommonResult<String> getNextQNO(String WindowGuid, String WindowNo, String CenterGuid, String UserGuid,
                                                Boolean UseCall);


    public AuditCommonResult<String> getNextQNO(String WindowGuid, String WindowNo, String CenterGuid, String UserGuid,
                                                Boolean UseCall, Boolean isAutoAssign);

    /**
     * 取号
     * @param sfz
     * @param phone
     * @param taskguid
     * @param centerguid
     * @param hallguid
     * @return
     */
    public AuditCommonResult<Map<String, String>> getQNO(String sfz, String phone, String taskguid, String centerguid,
                                                         String hallguid);



    /**
     *
     * @param centerguid
     * @param startTime
     * @param endTime
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditCommonResult<Integer> getQueueCount(String centerguid, Date startTime, Date endTime);

    /**
     *
     * @param centerguid
     * @param startTime
     * @param endTime
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditCommonResult<AuditQueue> getQueueDetail(String fieldstr, String qno, String centerguid);
}
