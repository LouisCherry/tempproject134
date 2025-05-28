package com.epoint.composite.auditqueue.handlequeue.inter;

import java.util.Date;
import java.util.Map;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;

@Service
public interface IJNHandleQueue
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
     * @param appointguid 预约guid 
     * @param centerguid 中心guid
     * @return
     */
    public AuditCommonResult<Map<String, String>> getAPQNO(String appointguid, String centerguid);

    /**
     * 下一位
     * @param WindowGuid 窗口guid
     * @param WindowNo 窗口no
     * @param CenterGuid 中心guid
     * @param UseCall 是否发送短信
     * @return
     */
    public AuditCommonResult<String> getNextQNO(String WindowGuid, String WindowNo, String CenterGuid, String UserGuid,
            Boolean UseCall);
    
    /**
     * 取号
     * @param sfz 身份证
     * @param phone 手机号
     * @param taskguid 事项guid
     * @param centerguid 中心guid
     * @param hallguid 大厅guid
     * @return
     */
    public AuditCommonResult<Map<String, String>> getQNO(String sfz, String phone, String taskguid, String centerguid,
            String hallguid );
    
    /**
     * 取号
     * @param sfz 身份证
     * @param phone 手机号
     * @param taskguid 事项guid
     * @param centerguid 中心guid
     * @param hallguid 大厅guid
     * @param islove 是否是爱心号
     * @return
     */
    public AuditCommonResult<Map<String, String>> getQNO(String sfz, String phone, String taskguid, String centerguid,
            String hallguid, String islove);

    /**
     * 获取时间段内总取号数量(包含历史)
     * @param centerguid 中心guid
     * @param startTime 起始时间
     * @param endTime 截止时间
     * 
     * 
     */
    public AuditCommonResult<Integer> getQueueCount(String centerguid, Date startTime, Date endTime);
    
    /**
     * 
     *  转号-改变事项
     *  @param auditqueueguid 队列guid
     *  @param taskguid 事项guid
     *  @param handleuserguid 处理人guid
     *  @param centerguid 中心guid
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<Map<String, String>> getTurnTaskQNO(String auditqueueguid, String taskguid,String handleuserguid,String centerguid);

    /**
     * 
     *  转号-改变窗口
     *  @param windowguid 窗口guid
     *  @param auditqueueguid 叫号队列guid
     *  @param handleuserguid 处理人guid
     *  @param centerguid 中心guid
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<Map<String, String>> getTurnWindowQNO(String auditqueueguid,String windowguid,String handleuserguid,String centerguid);
}
