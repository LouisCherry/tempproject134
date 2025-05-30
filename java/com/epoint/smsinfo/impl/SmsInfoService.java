package com.epoint.smsinfo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epoint.core.BaseEntity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.core.utils.sql.SqlHelper;
import com.epoint.smsinfo.api.entity.EvaluateProject;
import com.epoint.smsinfo.api.entity.SmsInfo;
import com.epoint.smsinfo.util.EnhancedSqlConditionUtil;

/**
 * 上行短信内容对应的后台service
 * 
 * @author yrchan
 * @version [版本号, 2022-04-11 16:23:40]
 */
public class SmsInfoService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public SmsInfoService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SmsInfo record) {
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
        T t = baseDao.find(SmsInfo.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SmsInfo record) {
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
    public SmsInfo find(Object primaryKey) {
        return baseDao.find(SmsInfo.class, primaryKey);
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
    public SmsInfo find(String sql, Object... args) {
        return baseDao.find(sql, SmsInfo.class, args);
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
    public List<SmsInfo> findList(String sql, Object... args) {
        return baseDao.findList(sql, SmsInfo.class, args);
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
    public List<SmsInfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, SmsInfo.class, args);
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
    public Integer countSmsInfo(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    /**
     * 
     * 查询办结时间为同一天且同一个手机号的评价办件信息
     * 
     * @param link_phone
     * @param handleDate
     * @return
     */
    public List<EvaluateProject> listEvaluateProjectByPhoneAndHandleDate(String phone, String handleDate) {
        String sql = "select * from evaluate_project where link_phone = ? and handle_date >= ? and handle_date <= ? ";
        return baseDao.findList(sql, EvaluateProject.class, phone, handleDate + " 00:00:00", handleDate + " 23:59:59");
    }

    /**
     * 更新评价办件信息表Evaluate_Project数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int updateEvaluateProject(EvaluateProject record) {
        return baseDao.update(record);
    }

    /**
     * 通过条件查询一条评价办件信息表Evaluate_Project数据
     * 
     * @param map
     *            条件
     * @return
     */
    public EvaluateProject findEvaluateProjectByCondition(Map<String, Object> map) {
        List<Object> params = new ArrayList<>();
        List<EvaluateProject> list = baseDao.findList(
                new SqlHelper().getSqlComplete(EvaluateProject.class, map, params), 0, 1, EvaluateProject.class,
                params.toArray());
        if (list.isEmpty()) {
            return null;
        }
        else {
            return list.get(0);
        }
    }

    /**
     * 
     * 数量查询
     * 
     * @param baseClass
     * @param map
     * @return
     */
    public <T extends BaseEntity> Integer countByCondition(Class<T> baseClass, Map<String, Object> map) {
        // 清除排序条件
        map.entrySet().removeIf(item -> item.getKey().contains("sort"));
        SqlConditionUtil conditionUtil = new EnhancedSqlConditionUtil(map);
        conditionUtil.setSelectFields("count(1)");
        List<Object> params = new ArrayList<>();
        return baseDao.queryInt(new SqlHelper().getSqlComplete(baseClass, conditionUtil.getMap(), params),
                params.toArray());
    }
}
