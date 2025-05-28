package com.epoint.zoucheng.znsb.auditznsbmodule.inter;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.epoint.basic.auditqueue.auditznsbmodule.domain.FunctionModule;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 
 * 一体机模块
 * 
 * @author Administrator
 * @version [版本号, 2016年11月23日]
 
 
 */
@Service
public interface IZCAuditZnsbModule
{
    /**
     * 插入点击信息
     * @param bean 点击实体类
     * @return
     */
    public AuditCommonResult<String> insert(FunctionModule bean);

    /**
     * 
     *  分页查询点击信息
     *  @param fieldstr
     *  @param conditionMap
     *  @param first
     *  @param pagesize
     *  @param sortField
     *  @param sortOrder
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<PageData<FunctionModule>> getAuditQueueModule(String fieldstr,
            Map<String, String> conditionMap, int first, int pagesize, String sortField, String sortOrder);
    /**
     * 
     *  获取点击总数
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<Integer> getRecordCount();
    /**
     * 
     *  根据mac地址获取点击总数
     *  @param macaddress mac地址
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<Integer> getRecordCountByMacaddress(String macaddress);

   /**
    * 根据centerguid和macaddress查询模块点击数量
    *  @param centerguid  中心guid
    *  @param macaddress mac地址
    *  @return    
    * 
    * 
    */
    public AuditCommonResult<Integer> getRecordCountByCenterguidAndMacaddress(String centerguid, String macaddress);

    /**
     * 根据centerguid查询模块点击数量
     * @param centerguid  中心guid
     * @return
     */
    public AuditCommonResult<Integer> getRecordCountByCenterguid(String centerguid);

    /**
     * 根据centerguid返回不同模块的点击数量
     * @param centerguid  中心guid
     * @return
     */
    public AuditCommonResult<List<Record>> getModuleClickByCenterguid(String centerguid);

    /**
     * 
     *  获取时间段内的设备点击数量
     *  @param macaddress mac地址
     *  @param from 起始时间
     *  @param to 截止时间
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<Integer> getRecordCount( String macaddress, String from, String to);
    /**
     * 
     *  获取时间段内的中心点击数量
     *  @param centerguid 中心guid
     *  @param from 起始时间
     *  @param to 截止时间
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<Integer> getRecordCountByCenterguidAndTime( String centerguid, String from, String to);

    /**
     * 根据centerguid返回不同模块的点击数量
     * @param centerguid 中心guid
     * @return
     */
    public AuditCommonResult<List<Record>> getModuleClick(String centerguid);
    
    /**
     * 返回所有中心不同模块的点击数量
     * @return
     */
    public AuditCommonResult<List<Record>> getModuleClick();
    

    /**
     * 返回所有中心的点击数量TOP10
     * @return
     */
    public AuditCommonResult<List<Record>> getCenterClick();
    
    /**
     * 根据centerguid，时间间隔，返回不同模块的点击数量
     * @return
     */
    public AuditCommonResult<List<Record>> getModuleClick(String centerguid,String startime, String endtime);
    
    /**
     * 根据centerguid返回指定模块的一定时间内每天点击数量
     * @return
     */
    public AuditCommonResult<Integer> getModuleDayClick(String centerguid,String modulename,String startime, String endtime);
    
    /**
     * 返回所有中心不同模块的点击数量
     * @return
     */
    public AuditCommonResult<List<Record>> getModuleClick(String startime, String endtime);
    
    /**
     * 根据macaddress返回不同模块的点击数量
     * @param macaddress mac地址
     * @return
     */
    public AuditCommonResult<List<Record>> getModuleClickByMac(String macaddress);
    
    /**
     * 根据macaddress返回不属于这些一体机的不同模块的点击数量
     * @param macaddress mac地址
     * @param centerguid 中心guid
     * @return
     */
    public AuditCommonResult<List<Record>> getModuleClickWithoutMac(String macaddress,String centerguid);
    /**
     * 
     *  获取时间段内的中心点击记录
     *  @param centerguid 中心guid
     *  @param from 起始时间
     *  @param to 截止时间
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<List<Record>> getRecordByCenterguidAndTime(String centerguid, String from, String to);

    public AuditCommonResult<Integer> updateByMacaddress(String oldmacaddress, String newmacaddress);
    
    
}
