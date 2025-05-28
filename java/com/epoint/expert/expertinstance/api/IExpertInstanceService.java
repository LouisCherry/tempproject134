package com.epoint.expert.expertinstance.api;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.epoint.expert.expertinstance.api.entity.ExpertInstance;


/**
 * 专家抽取实例表对应的后台service接口
 * 
 * @author Lee
 * @version [版本号, 2019-08-21 15:41:50]
 */
public interface IExpertInstanceService extends Serializable
{ 
   
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ExpertInstance record);

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
    public int update(ExpertInstance record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public ExpertInstance find(Object primaryKey);

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
    public ExpertInstance find(String sql,Object... args);

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
    public List<ExpertInstance> findList(String sql, Object... args);

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
    public List<ExpertInstance> findList(String sql, int pageNumber, int pageSize,Object... args);

    /**
     * 
     *  获取抽取规则排除的专家
     *  @param bidTime
     *  @param conditionDay
     *  @param conditionTimes
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<String> getExtractRule(Date bidTime, Integer conditionDay, Integer conditionTimes);
    
    /**
     * 根据抽取实例guid删除抽取结果
     *  [一句话功能简述]
     *  [功能详细描述]
     *  @param instanceGuid    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleteResultByInstanceGuid(String instanceGuid);
    


    
    /**
     * 根据抽取实例guid删除短信
     *  [一句话功能简述]
     *  [功能详细描述]
     *  @param instanceGuid    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleteSmsByInstanceGuid(String instanceGuid);
    
    /**
     * 根据抽取实例guid删除抽取规则
     *  [一句话功能简述]
     *  [功能详细描述]
     *  @param instanceGuid    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleteRuleByInstanceGuid(String instanceGuid);
}
