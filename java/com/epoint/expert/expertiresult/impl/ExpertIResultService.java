package com.epoint.expert.expertiresult.impl;

import java.util.List;
import java.util.Map;

import com.epoint.common.services.DBServcie;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.expert.expertiresult.api.entity.ExpertIResult;
import com.epoint.expert.expertirule.api.entity.ExpertIRule;

/**
 * 专家抽取结果表对应的后台service
 * 
 * @author Lee
 * @version [版本号, 2019-08-21 15:42:03]
 */
public class ExpertIResultService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public ExpertIResultService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ExpertIResult record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(ExpertIResult.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ExpertIResult record) {
        return baseDao.update(record);
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
    public ExpertIResult find(Object primaryKey) {
        return baseDao.find(ExpertIResult.class, primaryKey);
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
    public ExpertIResult find(String sql, Object... args) {
        return baseDao.find(sql, ExpertIResult.class, args);
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
    public List<ExpertIResult> findList(String sql, Object... args) {
        return baseDao.findList(sql, ExpertIResult.class, args);
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
    public List<ExpertIResult> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, ExpertIResult.class, args);
    }

    /**
     * 
     *  根据条件获取列表
     *  @param conditionMap
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<ExpertIResult> findListByCondition(Map<String, String> conditionMap) {
        return DBServcie.getInstance().getListByCondition(ExpertIResult.class, conditionMap);
    }

    /**
     * 
     *  根据抽取实例guid删除抽取结果
     *  @param instanceGuid
     *  @param is_auto
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int deleteByInstanceguid(String instanceGuid, String is_auto) {
        String sql = "delete from expert_i_result where instanceguid=? and is_auto=?";
        return baseDao.execute(sql, instanceGuid, is_auto);
    }

    /**
     * 
     * 根据实例获取结果
     *  @param instanceGuid
     *  @param is_auto
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<String> selectExpertByInstanceguid(String instanceGuid, String is_auto) {
        String sql = "select expertguid from expert_i_result where instanceguid=? and is_auto=?";
        return baseDao.findList(sql, String.class, instanceGuid, is_auto);
    }
}
