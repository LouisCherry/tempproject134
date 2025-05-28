package com.epoint.expert.expertcompanyavoid.impl;
import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.expert.expertcompanyavoid.api.entity.ExpertCompanyavoid;

/**
 * 专家回避单位表对应的后台service
 * 
 * @author cqsong
 * @version [版本号, 2019-08-21 16:37:07]
 */
public class ExpertCompanyavoidService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public ExpertCompanyavoidService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ExpertCompanyavoid record) {
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
        T t = baseDao.find(ExpertCompanyavoid.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ExpertCompanyavoid record) {
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
    public ExpertCompanyavoid find(Object primaryKey) {
        return baseDao.find(ExpertCompanyavoid.class, primaryKey);
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
    public ExpertCompanyavoid find(String sql,  Object... args) {
        return baseDao.find(sql, ExpertCompanyavoid.class, args);
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
    public List<ExpertCompanyavoid> findList(String sql, Object... args) {
        return baseDao.findList(sql, ExpertCompanyavoid.class, args);
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
    public List<ExpertCompanyavoid> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, ExpertCompanyavoid.class, args);
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
    public List<ExpertCompanyavoid> findListByexpert(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, ExpertCompanyavoid.class, args);
    }

    /**
     * 
     *  获取回避公司对应的专家
     *  @param companyGuids
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<String> getExpertGuidsByAvoidCompanyGuid(String companyGuids) {
        String sql="select distinct expertguid from expert_companyavoid where companyguid in (?)";
        return baseDao.findList(sql, String.class, companyGuids);
    }
    
    /**
     * 根据专家信息分页查找一个list
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
    public List<ExpertCompanyavoid> findListByexpertguid(String sql,String expertguid, int pageNumber, int pageSize, Object... args) {
        sql += " and expertguid = '" + expertguid + "' ";
        return baseDao.findList(sql, pageNumber, pageSize, ExpertCompanyavoid.class, args);
    }
    
    /**
     * 根据专家信息查找一个list
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<ExpertCompanyavoid> findListByexpertguid(String sql,String expertguid, Object... args) {
        sql += " and expertguid = '" + expertguid + "' ";
        return baseDao.findList(sql, ExpertCompanyavoid.class, args);
    }
    
    /**
     * 根据专家guid删除回避信息
     *  @param expertGuid    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleteByExpertGuid(String expertGuid) {
        String sql = "delete from expert_companyavoid where ExpertGuid = ?";
        baseDao.execute(sql, expertGuid);
    }

}
