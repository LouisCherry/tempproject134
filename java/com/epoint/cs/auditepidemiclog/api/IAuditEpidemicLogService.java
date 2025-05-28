package com.epoint.cs.auditepidemiclog.api;
import java.io.Serializable;
import java.util.List;
import java.util.Map;


import com.epoint.common.service.AuditCommonResult;
import com.epoint.cs.auditepidemiclog.api.entity.AuditEpidemicLog;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 访客登记对应的后台service接口
 * 
 * @author Mercury
 * @version [版本号, 2020-02-02 19:35:15]
 */
public interface IAuditEpidemicLogService extends Serializable
{ 
   
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditEpidemicLog record);

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditEpidemicLog record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public AuditEpidemicLog find(Object primaryKey);

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
    public AuditEpidemicLog find(String sql,Object... args);

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
    public List<AuditEpidemicLog> findList(String sql, Object... args);

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
    public List<AuditEpidemicLog> findList(String sql, int pageNumber, int pageSize,Object... args);

	 /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
	 public Integer countAuditEpidemicLog(String sql, Object... args);
	 

	 public AuditCommonResult<PageData<AuditEpidemicLog>> getAuditEpidemicLogPageData(Map<String, String> conditionMap,
	            Integer firstResult, Integer maxResults, String sortField, String sortOrder);
	 
	 /**
	     * 
	     * 对来访人数量根据条件查询
	     * @param conditionMap
	     *             条件集合
	     * @return 办件集
	     */
	    public AuditCommonResult<Integer> getAuditEpidemicLogCountByCondition(Map<String, String> conditionMap);
	    
	    /**
	      * 
	      *  根据条件查询人员库
	      *  @param conditionMap
	      *              查询条件
	      *  @return list
	      */
	    public AuditCommonResult<List<AuditEpidemicLog>> selectIndividualByCondition(
	            Map<String, String> conditionMap);
	    
	    /**
	     * 
	     *  查询当前人员最后一次的登记信息
	     *  @param id
	     *              id
	     *  @return AuditEpidemicLog
	     */
	   public AuditCommonResult<AuditEpidemicLog> selectLastestInfo(String id);
	   
	   /**
	    * 
	    *  检索id相关的人员信息
	    *  @param id
	    *              id
	    *  @return list
	    */
	   public AuditCommonResult<List<AuditEpidemicLog>> selectEpidemicLogByLikeID(String id);
	   /**
	    * 
	    *  查询当前人员最后一次的登记信息
	    *  @param id
	    *              id
	    *  @return AuditEpidemicLog
	    */
	   public AuditCommonResult<AuditEpidemicLog> selectLastestInfoAll(String id);
	   /**
	    * 
	    * 获取登记信息分页
	    * 
	    * @param conditionMap
	    *          查询条件
	    * @param first
	    *          分页起始值
	    * @param pageSize
	    *          每页数量
	    * @param sortField
	    *          排序值
	    * @param sortOrder
	    *          排序字段
	    * @return 审批事项
	    */
	   public AuditCommonResult<PageData<AuditEpidemicLog>> getEpidemicLogByPage(Map<String, String> conditionMap,
	            int first, int pageSize, String sortField, String sortOrder);

}
