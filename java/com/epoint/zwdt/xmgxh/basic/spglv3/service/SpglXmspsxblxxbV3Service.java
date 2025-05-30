package com.epoint.zwdt.xmgxh.basic.spglv3.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.core.BaseEntity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zwdt.xmgxh.basic.spglv3.domain.SpglXmspsxblxxbV3;

/**
 * 住建部_项目审批事项办理信息表对应的后台service
 * 
 * @author zhpengsy
 * @version [版本号, 2018-11-16 15:08:57]
 */
public class SpglXmspsxblxxbV3Service
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public SpglXmspsxblxxbV3Service() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglXmspsxblxxbV3 record) {
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
        T t = baseDao.find(SpglXmspsxblxxbV3.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglXmspsxblxxbV3 record) {
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
    public SpglXmspsxblxxbV3 find(Object primaryKey) {
        return baseDao.find(SpglXmspsxblxxbV3.class, primaryKey);
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
    public SpglXmspsxblxxbV3 find(String sql, Object... args) {
        return baseDao.find(sql, SpglXmspsxblxxbV3.class, args);
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
    public List<SpglXmspsxblxxbV3> findList(String sql, Object... args) {
        return baseDao.findList(sql, SpglXmspsxblxxbV3.class, args);
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
    public List<SpglXmspsxblxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, SpglXmspsxblxxbV3.class, args);
    }

    public SpglXmspsxblxxbV3 findByProjectguid(String projectguid) {
        String sql = "select * from SPGL_XMSPSXBLXXB_V3 where SPSXSLBM = ?";
        return baseDao.find(sql, SpglXmspsxblxxbV3.class, projectguid);
    }

    public PageData<SpglXmspsxblxxbV3> getAllByPage(String xzqhdm, String gcdm, String shsxmc, Integer sjsczt, int first,
            int pageSize, String sortField, String sortOrder) {
        PageData<SpglXmspsxblxxbV3> pageData = new PageData<SpglXmspsxblxxbV3>();
        ArrayList<Object> conditionList = new ArrayList<Object>();
        String sql = "select t1.*, t2.spsxmc from spgl_xmspsxblxxb_v3 t1 join spgl_spjdxxb_v3"
                + " t2 on t1.spsxbm = t2.spsxbm and t1.spsxbbh = t2.spsxbbh "
                + " where t1.sjyxbs = 1 and t2.sjyxbs = 1";
        if (StringUtil.isNotBlank(xzqhdm)) {
            sql += " and t1.xzqhdm = ?";
            conditionList.add(xzqhdm);
        }
        if (StringUtil.isNotBlank(gcdm)) {
            sql += " and t1.gcdm = ?";
            conditionList.add(gcdm);
        }
        if (StringUtil.isNotBlank(shsxmc)) {
            sql += " and t2.SPSXMC  like ?";
            conditionList.add(shsxmc);
        }
        if (sjsczt != null) {
            sql += " and t1.sjsczt = ?";
            conditionList.add(sjsczt);
        }
        sql+=" order by t1.spsxbm";
        sql += " order by t1.spsxbm";
        List<SpglXmspsxblxxbV3> list = baseDao.findList(sql, first, pageSize, SpglXmspsxblxxbV3.class,
                conditionList.toArray());
        int count = baseDao.findList(sql, SpglXmspsxblxxbV3.class, conditionList.toArray()).size();
        pageData.setList(list);
        pageData.setRowCount(count);
        if (ValidateUtil.isNotBlankCollection(list)) {
            pageData.setList(list);
            pageData.setRowCount(list.size());
        }
        else {
            pageData.setList(new ArrayList<>());
            pageData.setRowCount(0);
        }
        return pageData;
    }


    public boolean isExistFlowsn(String spsxslbm) {
        String sql = "select count(1) from SPGL_XMSPSXBLXXB_V3 where spsxslbm =? and ifnull(sync,0) != 2 and sjyxbs = 1 and sjsczt not in ('2','-1')  ";
        Integer count = baseDao.queryInt(sql, spsxslbm);
        return (count != null && count > 0);
    }
    

    public SpglXmspsxblxxbV3 getSpglXmspsxblxxbBySlbm(String spsxslbm) {
        String sql = "select * from SPGL_XMSPSXBLXXB_V3 where spsxslbm =? and ifnull(sync,0) != 2 and sjyxbs = 1 and sjsczt not in ('2','-1')  ";
        return  baseDao.find(sql, SpglXmspsxblxxbV3.class,spsxslbm);
    }

    public List<SpglXmspsxblxxbV3> getAllRecord(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getListByCondition(baseClass, conditionMap);
    }

    public Integer getAllRecordCount(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        List<SpglXmspsxblxxbV3> result = getAllRecord(baseClass, conditionMap);
        if (result != null) {
            return result.size();
        }
        else {
            return 0;
        }

    }

    public PageData<SpglXmspsxblxxbV3> getAllRecordByPage(Class<? extends BaseEntity> baseClass,
            Map<String, String> conditionMap, Integer first, Integer pageSize, String sortField, String sortOrder) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getDbListByPage(baseClass, conditionMap, first, pageSize, sortField, sortOrder);
    }

}