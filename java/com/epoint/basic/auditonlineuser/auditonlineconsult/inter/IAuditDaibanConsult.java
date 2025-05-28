package com.epoint.basic.auditonlineuser.auditonlineconsult.inter;

import java.util.List;
import java.util.Map;

import com.epoint.basic.auditonlineuser.auditonlineconsult.domain.AuditDaibanConsult;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 咨询投诉相关API
 * 
 * @author WST
 * @version [V7.7, 2018年10月8日]
 */
public interface IAuditDaibanConsult
{

    /**
     * 
     *  获取提问者咨询投诉已答复或未回复数量
     *  
     *  @param askerGuid 提问者标识
     *  @param isAsked 是否已回复
     *  @param cosulttype 咨询投诉类型
     *  @return Integer
     */
    public AuditCommonResult<Integer> getAnswerCount(String askerGuid, Boolean isAsked, String cosulttype);

    /**
     * 
     *  新增咨询投诉记录
     *  
     *  @param auditOnlineConsult 咨询投诉实体
     *  @return String    
     */
    public AuditCommonResult<String> addConsult(AuditDaibanConsult auditOnlineConsult);

    /**
     * 
     *  更新咨询投诉记录
     *  
     *  @param auditOnlineConsult 咨询投诉实体
     *  @return String
     */
    public AuditCommonResult<String> updateConsult(AuditDaibanConsult auditOnlineConsult);

    /**
     * 
     *  根据咨询投诉标识更新咨询投诉某字段的值
     *  
     *  @param rowGuid 咨询投诉标识
     *  @param key 更新的字段
     *  @param value 更新的值
     *  @return String 
     */
    public AuditCommonResult<String> updateConsultByField(String rowGuid, String key, String value);

    /**
     * 
     *  根据咨询投诉标识获取咨询投诉记录
     *  
     *  @param rowguid 咨询投诉标识
     *  @return AuditOnlineConsult
     */
    public AuditCommonResult<AuditDaibanConsult> getConsultByRowguid(String rowguid);

    /**
     * 
     *  删除咨询投诉记录
     *  
     *  @param auditOnlineConsult 咨询投诉实体
     *  @return String 
     */
    public AuditCommonResult<String> deleteConsult(AuditDaibanConsult auditOnlineConsult);

    /**
     * 
     *  根据咨询投诉标识删除咨询投诉记录
     *  
     *  @param rowguid 咨询投诉标识
     *  @return String 
     */
    public AuditCommonResult<String> deleteConsultByRowguid(String rowguid);

    /**
     * 
     *  根据条件获取咨询投诉记录（分页）
     *  
     *  @param conditionMap 搜索条件
     *  @param first 当前页码
     *  @param pageSize 每页显示数量
     *  @param sortField 排序字段
     *  @param sortOrder 排序顺序
     *  @return List
     */
    public AuditCommonResult<List<AuditDaibanConsult>> selectConsultByPage(Map<String, String> conditionMap,
            Integer first, Integer pageSize, String sortField, String sortOrder);
    
    /**
     * 
     *  根据条件获取咨询投诉记录（分页）
     *  
     *  @param conditionMap 搜索条件
     *  @param first 当前页码
     *  @param pageSize 每页显示数量
     *  @param sortField 排序字段
     *  @param sortOrder 排序顺序
     *  @return List
     */
    public AuditCommonResult<PageData<AuditDaibanConsult>> selectConsultByPageData(Map<String, String> conditionMap,
            Integer first, Integer pageSize, String sortField, String sortOrder);

    /**
     * 
     *  根据搜索条件获取咨询投诉数量
     *  
     *  @param conditionMap 搜索条件
     *  @return Integer
     */
    public AuditCommonResult<Integer> getConsultCount(Map<String, String> conditionMap);

    /**
     * 
     *  根据搜索条件获取咨询投诉列表
     *  
     *  @param conditionMap 搜索条件
     *  @return List
     */
    public AuditCommonResult<List<AuditDaibanConsult>> selectAuditOnlineConsultList(Map<String, String> conditionMap);

    /**
     * 
     *  根据条件获取咨询投诉记录（分页）
     *  
     *  @param conditionMap 搜索条件
     *  @param first 当前页码
     *  @param pageSize 每页显示数量
     *  @param sortField 排序字段
     *  @param sortOrder 排序顺序
     *  @return PageData
     */
    public AuditCommonResult<PageData<AuditDaibanConsult>> getAuditOnlineConsultListByPage(
            Map<String, String> conditionMap, int first, int pageSize, String sortField, String sortOrder);
}
