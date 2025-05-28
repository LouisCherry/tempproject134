package com.epoint.basic.auditonlineuser.auditonlineconsult.inter;

import java.util.List;
import java.util.Map;

import com.epoint.basic.auditonlineuser.auditonlineconsult.domain.AuditDaibanConsultExt;
import com.epoint.common.service.AuditCommonResult;

/**
 * 咨询追问追答相关API
 * 
 * @author WST
 * @version [V7.7, 2018年10月8日]
 */
public interface IAuditDaibanConsultExt
{

    /**
     * 
     *  新增追问追答记录
     *  
     *  @param auditOnlineConsultExt 追问追答实体
     *  @return  String
     */
    public AuditCommonResult<String> addConsultExt(AuditDaibanConsultExt auditOnlineConsultExt);

    /**
     * 
     *  更新追问追答记录
     *  
     *  @param auditOnlineConsultExt 追问追答实体
     *  @return String
     */
    public AuditCommonResult<String> updateConsultExt(AuditDaibanConsultExt auditOnlineConsultExt);

    /**
     * 
     *  根据追问追答标识获取追问追答记录
     *  
     *  @param rowguid 追问追答标识
     *  @return AuditOnlineConsultExt
     */
    public AuditCommonResult<AuditDaibanConsultExt> getConsultExtByRowguid(String rowguid);

    /**
     * 
     *  删除追问追答记录
     *  
     *  @param auditOnlineConsultExt 咨询意见表实体
     *  @return String
     */
    public AuditCommonResult<String> deleteConsultExt(AuditDaibanConsultExt auditOnlineConsultExt);

    /**
     * 
     *  根据追问追答标识删除追问追答记录
     *  
     *  @param rowguid 追问追答标识
     *  @return String
     */
    public AuditCommonResult<String> deleteConsultExtByRowguid(String rowguid);

    /**
     * 
     *  根据条件获取追问追答记录（分页）
     *  
     *  @param conditionMap 搜索条件
     *  @param first 当前页码
     *  @param pageSize 每页显示数量
     *  @param sortField 排序字段
     *  @param sortOrder 排序顺序
     *  @return List
     */
    public AuditCommonResult<List<AuditDaibanConsultExt>> selectConsultExtByPage(Map<String, String> conditionMap,
            Integer first, Integer pageSize, String sortField, String sortOrder);

    /**
     * 
     *  根据搜索条件获取追问追答数量
     *  
     *  @param conditionMap 搜索条件
     *  @return Integer
     */
    public AuditCommonResult<Integer> getConsultExtCount(Map<String, String> conditionMap);

    /**
     * 
     *  根据条件获取追问追答列表
     *  
     *  @param conditionMap 搜索条件
     *  @return List
     */
    public AuditCommonResult<List<AuditDaibanConsultExt>> selectConsultExtList(Map<String, String> conditionMap);

}
