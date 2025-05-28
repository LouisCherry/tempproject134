package com.epoint.expert.expertcompany.api;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.epoint.expert.expertcompany.api.entity.ExpertCompany;

/**
 * 从业单位表对应的后台service接口
 * 
 * @author cqsong
 * @version [版本号, 2019-08-21 16:09:09]
 */
public interface IExpertCompanyService extends Serializable
{ 
   
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ExpertCompany record);

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
    public int update(ExpertCompany record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public ExpertCompany find(Object primaryKey);

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
    public ExpertCompany find(String sql,Object... args);

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
    public List<ExpertCompany> findList(String sql, Object... args);

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
    public List<ExpertCompany> findList(String sql, int pageNumber, int pageSize,Object... args);
    /**
     * 
     *  根据条件获取列表
     *  @param conditionMap
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<ExpertCompany> findListByCondition(Map<String, String> conditionMap) ;
    
    /**
     * 分页查找一个list 当被逻辑删除的不需要展示（id_del = 1）
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
    public List<ExpertCompany> findListRemDel(String sql,String sign, int pageNumber, int pageSize, Object... args);
    
    /**
     * 查找一个list 当被逻辑删除的不需要展示（id_del = 1）
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
    public List<ExpertCompany> findListRemDel(String sql,String sign, Object... args);
    
    /**
     * 查找一个list 当被逻辑删除的不需要展示（id_del = 1），未被当前专家选择的并且启用的
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
    public List<ExpertCompany> findListRemAlready(String sql, String expertguid, Object... args);
    
    /**
     * 获取专家还没有回避的公司
     *  @param expertguid
     *  @param pageNumber
     *  @param pageSize
     *  @param args
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<ExpertCompany> findListRemAlready(String sql, String expertguid, int pageNumber, int pageSize, Object... args);
    
}


