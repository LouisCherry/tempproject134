package com.epoint.zwdt.xmgxh.basic.spglv3.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.BaseEntity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zwdt.xmgxh.basic.spglv3.domain.SpglXmjbxxbV3;

/**
 * 住建部_项目基本信息表对应的后台service
 * 
 * @author zhpengsy
 * @version [版本号, 2018-11-16 15:08:41]
 */
public class SpglXmjbxxbV3Service
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public SpglXmjbxxbV3Service() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglXmjbxxbV3 record) {
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
        T t = baseDao.find(SpglXmjbxxbV3.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglXmjbxxbV3 record) {
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
    public SpglXmjbxxbV3 find(Object primaryKey) {
        return baseDao.find(SpglXmjbxxbV3.class, primaryKey);
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
    public SpglXmjbxxbV3 find(String sql, Object... args) {
        return baseDao.find(sql, SpglXmjbxxbV3.class, args);
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
    public List<SpglXmjbxxbV3> findList(String sql, Object... args) {
        return baseDao.findList(sql, SpglXmjbxxbV3.class, args);
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
    public List<SpglXmjbxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, SpglXmjbxxbV3.class, args);
    }

    public SpglXmjbxxbV3 findByLshAndSplclx(String lsh, String splclx) {
        String sql = "select * from spgl_xmjbxxb_v3 where lsh = ? and splclx = ?";
        return baseDao.find(sql, SpglXmjbxxbV3.class, lsh, splclx);
    }

    public boolean isExistGcdm(String gcdm) {
        String sql = "select count(1) from spgl_xmjbxxb_v3 where gcdm =? and ifnull(sync,0) != 2 and sjyxbs = 1 and sjsczt not in ('2','-1') ";
        Integer count = baseDao.queryInt(sql, gcdm);
        return (count != null && count > 0);
    }

    public List<SpglXmjbxxbV3> getAllRecord(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getListByCondition(baseClass, conditionMap);
    }

    public Integer getAllRecordCount(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        List<SpglXmjbxxbV3> result = getAllRecord(baseClass, conditionMap);
        if (result != null) {
            return result.size();
        }
        else {
            return 0;
        }

    }

    public PageData<SpglXmjbxxbV3> getAllRecordByPage(Class<? extends BaseEntity> baseClass,
            Map<String, String> conditionMap, Integer first, Integer pageSize, String sortField, String sortOrder) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getDbListByPage(baseClass, conditionMap, first, pageSize, sortField, sortOrder);

    }

    public PageData<SpglXmjbxxbV3> getAllXmByPage(Class<? extends BaseEntity> baseClass,
            Map<String, String> conditionMap, Integer first, Integer pageSize, String sortField, String sortOrder) {
        PageData<SpglXmjbxxbV3> result = new PageData<>();
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        String sql = "select * from SPGL_XMJBXXB_V3 where xmdm=gcdm "+ sqlManageUtil.buildPatchSql(conditionMap);
        result.setList(baseDao.findList(sql,first,pageSize,SpglXmjbxxbV3.class));
        sql = "select count(1) from SPGL_XMJBXXB_V3 where xmdm=gcdm "+ sqlManageUtil.buildPatchSql(conditionMap);
        result.setRowCount(baseDao.queryInt(sql));
        return result;
        
    }

    public List<String> getGcdmByFailed() {
        String sql = "select DISTINCT(GCDM) from  Spgl_Zrztxxb  where sjsczt in ('2','-1')";
        List<String> gcdmlist = baseDao.findList(sql, String.class);
        sql = "select DISTINCT(GCDM) from  Spgl_Xmjbxxb_V3  where sjsczt in ('2','-1')";
        gcdmlist.addAll(baseDao.findList(sql, String.class));
        sql = "select DISTINCT(GCDM)  from  Spgl_XmspsxblxxbV3  where sjsczt in ('2','-1')";
        gcdmlist.addAll(baseDao.findList(sql, String.class));
        sql = "select DISTINCT(GCDM)  from  Spgl_XmspsxblxxxxbV3  where sjsczt in ('2','-1')";
        gcdmlist.addAll(baseDao.findList(sql, String.class));
        sql = "select DISTINCT(GCDM)  from  Spgl_XmspsxzqyjxxbV3  where sjsczt in ('2','-1')";
        gcdmlist.addAll(baseDao.findList(sql, String.class));
        sql = "select DISTINCT(GCDM)  from  Spgl_XmsqcljqtfjxxbV3  where sjsczt in ('2','-1')";
        gcdmlist.addAll(baseDao.findList(sql, String.class));
        sql = "select DISTINCT(GCDM)  from  Spgl_XmspsxbltbcxxxbV3  where sjsczt in ('2','-1')";
        gcdmlist.addAll(baseDao.findList(sql, String.class));
        sql = "select DISTINCT(GCDM)  from  Spgl_XmspsxpfwjxxbV3  where sjsczt in ('2','-1')";
        gcdmlist.addAll(baseDao.findList(sql, String.class));
        gcdmlist = gcdmlist.stream().distinct().collect(Collectors.toList());
        //查找工程代码对应的项目代码
        if(gcdmlist.isEmpty()){
            return gcdmlist;
        }
        else{
            //根据工程代码过滤
            sql = "select DISTINCT(XMDM) from  Spgl_Xmjbxxb_V3 where  GCDM in ("+StringUtil.joinSql(gcdmlist)+")"; 
            return baseDao.findList(sql, String.class);
        }
    }

}
