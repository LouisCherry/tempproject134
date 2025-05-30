package com.epoint.zwdt.xmgxh.basic.spglv3.service;

import java.util.List;
import java.util.Map;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.BaseEntity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zwdt.xmgxh.basic.spglv3.domain.SpglxmsqcljqtfjxxbV3;

/**
 * 住建部_项目其他附件信息表对应的后台service
 * 
 * @author zhpengsy
 * @version [版本号, 2018-11-16 15:09:37]
 */
public class SpglsqcljqtfjxxbV3Service
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public SpglsqcljqtfjxxbV3Service() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglxmsqcljqtfjxxbV3 record) {
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
        T t = baseDao.find(SpglxmsqcljqtfjxxbV3.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglxmsqcljqtfjxxbV3 record) {
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
    public SpglxmsqcljqtfjxxbV3 find(Object primaryKey) {
        return baseDao.find(SpglxmsqcljqtfjxxbV3.class, primaryKey);
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
    public SpglxmsqcljqtfjxxbV3 find(String sql, Object... args) {
        return baseDao.find(sql, SpglxmsqcljqtfjxxbV3.class, args);
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
    public List<SpglxmsqcljqtfjxxbV3> findList(String sql, Object... args) {
        return baseDao.findList(sql, SpglxmsqcljqtfjxxbV3.class, args);
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
    public List<SpglxmsqcljqtfjxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, SpglxmsqcljqtfjxxbV3.class, args);
    }

    public AuditCommonResult<PageData<SpglxmsqcljqtfjxxbV3>> getAllByPage(Map<String, String> map, int first, int pageSize,
                                                                          String sortField, String sortOrder) {
        AuditCommonResult<PageData<SpglxmsqcljqtfjxxbV3>> auditCommonResult = new AuditCommonResult<PageData<SpglxmsqcljqtfjxxbV3>>();
        try {
            SQLManageUtil sqlManageUtil = new SQLManageUtil(SpglxmsqcljqtfjxxbV3.class);
            PageData<SpglxmsqcljqtfjxxbV3> pageData = sqlManageUtil.getDbListByPage(SpglxmsqcljqtfjxxbV3.class, map, first, pageSize,
                    sortField, sortOrder);
            auditCommonResult.setResult(pageData);
        }
        catch (Exception e) {
            e.printStackTrace();
            auditCommonResult.setBusinessFail(e.getMessage());
        }
        return auditCommonResult;
    }

    public List<SpglxmsqcljqtfjxxbV3> getAllRecord(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getListByCondition(baseClass, conditionMap);
    }

    public Integer getAllRecordCount(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        List<SpglxmsqcljqtfjxxbV3> result = getAllRecord(baseClass, conditionMap);
        if (result != null) {
            return result.size();
        }
        else {
            return 0;
        }

    }

    public PageData<SpglxmsqcljqtfjxxbV3> getAllRecordByPage(Class<? extends BaseEntity> baseClass,
                                                             Map<String, String> conditionMap, Integer first, Integer pageSize, String sortField, String sortOrder) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getDbListByPage(baseClass, conditionMap, first, pageSize, sortField, sortOrder);
    }

}
