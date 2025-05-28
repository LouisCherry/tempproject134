package com.epoint.evainstance;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.evainstance.entity.Evainstance;

public abstract interface IEvainstanceService extends Serializable
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(Evainstance record);

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
    public int update(Evainstance record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public Evainstance find(Object primaryKey);

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
    public Evainstance find(String sql,Object... args);

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
    public List<Evainstance> findList(String sql, Object... args);

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
    public List<Evainstance> findList(String sql, int pageNumber, int pageSize,Object... args);

     /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
     public Integer countEvainstance(String sql, Object... args);
    public abstract List<Record> getEvalDetail(String paramString);

    public abstract int addEvainstance(Evainstance paramEvainstance);

    public abstract Record findProject(String paramString1, String paramString2);

    public abstract boolean isExistEvaluate(String paramString, int paramInt);

    public abstract void insertEvainstanceState(Record paramRecord);

    public abstract void updateEvainstanceState(Record paramRecord);

    public abstract int getMaxServicenum(String paramString);

    public abstract boolean findProService(String paramString1, String paramString2);

    public abstract boolean isExistProService(String paramString, int paramInt);

    public abstract Evainstance findEvaluate(String paramString, int paramInt);

    public abstract Record getServiceByProjectno(String paramString, int paramInt);

    public abstract Record findAuditProjectByFlown(String projectno);
    AuditCommonResult<PageData<Evainstance>> getEvaluateservicePageData(String var1, Map<String, String> var2,
            Integer var3, Integer var4, String var5, String var6, String var7);

    public List<Record> getServiceByProjectno(String projectno);

    public Record getZhibiao(String string);

    public int findFcbmyTotal(String areacode);

    public int findBmyTotal(String areacode);

    public List<Evainstance> getPageDate(Evainstance data);


}
