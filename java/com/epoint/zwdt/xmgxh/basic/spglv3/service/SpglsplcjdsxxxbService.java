package com.epoint.zwdt.xmgxh.basic.spglv3.service;

import java.util.List;
import java.util.Map;

import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zwdt.xmgxh.basic.spglv3.domain.Spglsplcjblsxxxb;

/**
 * 住建部_地方项目审批流程阶段事项信息表对应的后台service
 * 
 * @author zhpengsy
 * @version [版本号, 2018-11-16 15:10:10]
 */
public class SpglsplcjdsxxxbService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public SpglsplcjdsxxxbService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(Spglsplcjblsxxxb record) {
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
        T t = baseDao.find(Spglsplcjblsxxxb.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(Spglsplcjblsxxxb record) {
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
    public Spglsplcjblsxxxb find(Object primaryKey) {
        return baseDao.find(Spglsplcjblsxxxb.class, primaryKey);
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
    public Spglsplcjblsxxxb find(String sql, Object... args) {
        return baseDao.find(sql, Spglsplcjblsxxxb.class, args);
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
    public List<Spglsplcjblsxxxb> findList(String sql, Object... args) {
        return baseDao.findList(sql, Spglsplcjblsxxxb.class, args);
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
    public List<Spglsplcjblsxxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, Spglsplcjblsxxxb.class, args);
    }
    
    public void deleteBySpGuid(String guid) {
        Entity en = Spglsplcjblsxxxb.class.getAnnotation(Entity.class);
        String sql = "delete from "+en.table()+" where SPLCBM = ?";
        baseDao.execute(sql, guid);
    }
    
    /**
     * 获取所有记录，如果没有缓存数据，就从数据库中获取
     * 
     * @param baseClass
     * @param useCache
     * @return
     */
    public List<Spglsplcjblsxxxb> getAllRecord(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getListByCondition(baseClass, conditionMap);
    }

    public Integer getAllRecordCount(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        List<Spglsplcjblsxxxb> result = getAllRecord(baseClass, conditionMap);
        if (result != null) {
            return result.size();
        }
        else {
            return 0;
        }

    }

    /**
     * 获取分页数据
     * 
     * @param baseClass
     * @param conditionMap
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    public PageData<Spglsplcjblsxxxb> getAllRecordByPage(Class<? extends BaseEntity> baseClass,
                                                         Map<String, String> conditionMap, Integer first, Integer pageSize, String sortField, String sortOrder) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getDbListByPage(baseClass, conditionMap, first, pageSize, sortField, sortOrder);
    }
    
    public boolean isExistSplcSx(Double splcbbh, String splcbm, Double spsxbbh, String spsxbm) {
        String  sql = "select count(1) from  SPGL_SPLCJBLSXXXB_V3 where splcbbh=? and splcbm=? and spsxbbh=? and spsxbm=? and ifnull(sync,0) != 2 and sjyxbs = 1 and sjsczt not in ('2','-1') ";
        Integer count = baseDao.queryInt(sql, splcbbh,splcbm,spsxbbh,spsxbm);
        
        return (count!=null && count>0);
    }

    public Spglsplcjblsxxxb getSpglsplcjdsxxxb(Double splcbbh, String splcbm, Double spsxbbh, String spsxbm) {
        String  sql = "select * from  SPGL_SPLCJBLSXXXB_V3 where splcbbh=? and splcbm=? and spsxbbh=? and spsxbm=? and ifnull(sync,0) != 2 and sjyxbs = 1 and sjsczt not in ('2','-1') ";
        return  baseDao.find(sql, Spglsplcjblsxxxb.class, splcbbh,splcbm,spsxbbh,spsxbm);

    }
    public List<Spglsplcjblsxxxb> getNeedAddNewVersionByItemId(String item_id) {
        String sql = "select XZQHDM,SPLCBM,SPJDXH,SPSXBM , max(SPLCBBH) SPLCBBH  from SPGL_SPLCJBLSXXXB_V3 where  spsxbm = ?  and sjsczt in ('0','1','3') and ifnull(sync,0) != 2 and ifnull(sync,0) != -1 and sjyxbs = 1 GROUP BY XZQHDM,SPLCBM,SPJDXH,SPSXBM";
        return baseDao.findList(sql, Spglsplcjblsxxxb.class, item_id,item_id);
    }
    public Spglsplcjblsxxxb querybbh(Double splcbbh, String splcbm, String spsxbm) {
        String sql = "select * from  SPGL_SPLCJBLSXXXB_V3 where splcbbh=? and splcbm=? and spsxbm=? and ifnull(sync,0) != 2 and sjyxbs = 1 and sjsczt not in ('2','-1') ORDER BY OPERATEDATE desc limit 1";
        Spglsplcjblsxxxb spglsplcjblsxxxb = baseDao.find(sql,Spglsplcjblsxxxb.class, splcbbh, splcbm, spsxbm);
        return spglsplcjblsxxxb;
    }

    public Record getnewlcbbh(String splcbm, String spsxbm) {
        String  sql = "select * from  SPGL_SPLCJBLSXXXB_V3 where  splcbm=?  and spsxbm=? and ifnull(sync,0) != 2 and sjyxbs = 1 and sjsczt not in ('2','-1') order by SPLCBBH desc,spsxbbh desc limit 1 ";
        return baseDao.find(sql,Record.class, splcbm,spsxbm);
    }
}
