package com.epoint.smsinfo.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.BaseEntity;
import com.epoint.smsinfo.api.ISmsInfoService;
import com.epoint.smsinfo.api.entity.EvaluateProject;
import com.epoint.smsinfo.api.entity.SmsInfo;

/**
 * 上行短信内容对应的后台service实现类
 * 
 * @author yrchan
 * @version [版本号, 2022-04-11 16:23:40]
 */
@Component
@Service
public class SmsInfoServiceImpl implements ISmsInfoService
{
    private static final long serialVersionUID = -3134286873832804430L;

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SmsInfo record) {
        return new SmsInfoService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SmsInfoService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SmsInfo record) {
        return new SmsInfoService().update(record);
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
        return new SmsInfoService().find(primaryKey);
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
        return new SmsInfoService().find(sql, args);
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
        return new SmsInfoService().findList(sql, args);
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
        return new SmsInfoService().findList(sql, pageNumber, pageSize, args);
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
    public Integer countSmsInfo(String sql, Object... args) {
        return new SmsInfoService().countSmsInfo(sql, args);
    }

    /**
     * 
     * 查询办结时间为同一天且同一个手机号的评价办件信息
     * 
     * @param link_phone
     * @param handleDate
     * @return
     */
    @Override
    public List<EvaluateProject> listEvaluateProjectByPhoneAndHandleDate(String phone, String handleDate) {
        return new SmsInfoService().listEvaluateProjectByPhoneAndHandleDate(phone, handleDate);
    }

    /**
     * 更新评价办件信息表Evaluate_Project数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int updateEvaluateProject(EvaluateProject record) {
        return new SmsInfoService().updateEvaluateProject(record);
    }

    /**
     * 通过条件查询一条评价办件信息表Evaluate_Project数据
     * 
     * @param map
     *            条件
     * @return
     */
    @Override
    public EvaluateProject findEvaluateProjectByCondition(Map<String, Object> map) {
        return new SmsInfoService().findEvaluateProjectByCondition(map);
    }

    /**
     * 
     * 数量查询
     * 
     * @param baseClass
     * @param map
     * @return
     */
    @Override
    public <T extends BaseEntity> Integer countByCondition(Class<T> baseClass, Map<String, Object> map) {
        return new SmsInfoService().countByCondition(baseClass, map);
    }

}
