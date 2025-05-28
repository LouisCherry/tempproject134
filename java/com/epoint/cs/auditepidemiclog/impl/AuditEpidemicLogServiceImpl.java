package com.epoint.cs.auditepidemiclog.impl;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import com.epoint.cs.auditepidemiclog.api.entity.AuditEpidemicLog;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditresource.service.AuditResourceService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.cs.auditepidemiclog.api.IAuditEpidemicLogService;
/**
 * 访客登记对应的后台service实现类
 * 
 * @author Mercury
 * @version [版本号, 2020-02-02 19:35:15]
 */
@Component
@Service
public class AuditEpidemicLogServiceImpl implements IAuditEpidemicLogService
{
    /**
     * 
     */
    private static final long serialVersionUID = 9104989010278066599L;

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditEpidemicLog record) {
        return new AuditEpidemicLogService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditEpidemicLogService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditEpidemicLog record) {
        return new AuditEpidemicLogService().update(record);
    }

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public AuditEpidemicLog find(Object primaryKey) {
       return new AuditEpidemicLogService().find(primaryKey);
    }

    /**
     * 查找单条记录
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *            ;String.class;Integer.class;Long.class]
     * @param args
     *            参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public AuditEpidemicLog find(String sql, Object... args) {
        return new AuditEpidemicLogService().find(sql,args);
    }

    /**
     * 查找一个list
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditEpidemicLog> findList(String sql, Object... args) {
       return new AuditEpidemicLogService().findList(sql,args);
    }

    /**
     * 分页查找一个list
     * 
     * @param sql
     *            查询语句
     * @param pageNumber
     *            记录行的偏移量
     * @param pageSize
     *            记录行的最大数目
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditEpidemicLog> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new AuditEpidemicLogService().findList(sql,pageNumber,pageSize,args);
    }
    
     /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
     @Override
    public Integer countAuditEpidemicLog(String sql, Object... args){
        return new AuditEpidemicLogService().countAuditEpidemicLog(sql, args);
    }
     
     @Override
     public AuditCommonResult<PageData<AuditEpidemicLog>> getAuditEpidemicLogPageData(Map<String, String> conditionMap,
             Integer firstResult, Integer maxResults, String sortField, String sortOrder) {
         AuditCommonResult<PageData<AuditEpidemicLog>> result = new AuditCommonResult<PageData<AuditEpidemicLog>>();
         try {
             PageData<AuditEpidemicLog> epidemiclog = new PageData<AuditEpidemicLog>();

             SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditEpidemicLog.class);
             epidemiclog = sqlManageUtil.getDbListByPage(AuditEpidemicLog.class, conditionMap, firstResult, maxResults,
                     sortField, sortOrder);
             result.setResult(epidemiclog);
         }
         catch (Exception e) {
             e.printStackTrace();
             e.printStackTrace();
         }
         return result;
     }
     
     @Override
     public AuditCommonResult<Integer> getAuditEpidemicLogCountByCondition(Map<String, String> conditionMap) {
         AuditCommonResult<Integer> result = new AuditCommonResult<>();
         try {
             SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditEpidemicLog.class);
             Integer count = sqlManageUtil.getListCount(AuditEpidemicLog.class, conditionMap);
             result.setResult(count);
         }
         catch (Exception e) {
             e.printStackTrace();
             e.printStackTrace();
         }
         return result;
     }
     
     @Override
     public AuditCommonResult<List<AuditEpidemicLog>> selectIndividualByCondition(
             Map<String, String> conditionMap) {
         AuditResourceService<AuditEpidemicLog> auditResourceService = new AuditResourceService<AuditEpidemicLog>();
         AuditCommonResult<List<AuditEpidemicLog>> result = new AuditCommonResult<List<AuditEpidemicLog>>();
         try {
             if (!SQLManageUtil.validate(conditionMap)) {
                 Logger.getLogger(AuditEpidemicLogServiceImpl.class).error("未传入任何条件！");
                 return result;
             }
             List<AuditEpidemicLog> list = auditResourceService.getAllRecord(AuditEpidemicLog.class,
                     conditionMap, false);
             result.setResult(list);
         }
         catch (Exception e) {
             result.setSystemFail(e.getMessage());
         }
         return result;
     }

     @Override
     public AuditCommonResult<AuditEpidemicLog> selectLastestInfo(String id) {
         AuditEpidemicLogService auditepidemiclogservice = new AuditEpidemicLogService();
         AuditCommonResult<AuditEpidemicLog> result = new AuditCommonResult<AuditEpidemicLog>();
         try {
             AuditEpidemicLog auditepidemiclog = auditepidemiclogservice.selectLastestInfo(id);
             result.setResult(auditepidemiclog);
         }
         catch (Exception e) {
             result.setSystemFail(e.getMessage());
         }
         return result;
     }
     @Override
     public AuditCommonResult<AuditEpidemicLog> selectLastestInfoAll(String id) {
         AuditEpidemicLogService auditepidemiclogservice = new AuditEpidemicLogService();
         AuditCommonResult<AuditEpidemicLog> result = new AuditCommonResult<AuditEpidemicLog>();
         try {
             AuditEpidemicLog auditepidemiclog = auditepidemiclogservice.selectLastestInfoAll(id);
             result.setResult(auditepidemiclog);
         }
         catch (Exception e) {
             result.setSystemFail(e.getMessage());
         }
         return result;
     }
     
     @Override
     public AuditCommonResult<List<AuditEpidemicLog>> selectEpidemicLogByLikeID(String id) {
         AuditEpidemicLogService auditepidemiclogservice = new AuditEpidemicLogService();
         AuditCommonResult<List<AuditEpidemicLog>> result = new AuditCommonResult<List<AuditEpidemicLog>>();
         try {
             result.setResult(auditepidemiclogservice.selectEpidemicLogByLikeID(id));
         }
         catch (Exception e) {
             result.setSystemFail(e.getMessage());
         }
         return result;
     }
     
     @Override
     public AuditCommonResult<PageData<AuditEpidemicLog>> getEpidemicLogByPage(Map<String, String> conditionMap,
             int first, int pageSize, String sortField, String sortOrder) {
         AuditEpidemicLogService auditepidemiclogservice = new AuditEpidemicLogService();
         AuditCommonResult<PageData<AuditEpidemicLog>> result = new AuditCommonResult<PageData<AuditEpidemicLog>>();
         result.setResult(auditepidemiclogservice.getEpidemicLogByPage(conditionMap, first, pageSize, sortField, sortOrder));
         return result;
     }

}
