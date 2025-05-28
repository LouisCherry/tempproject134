package com.epoint.mq.spgl.impl;

import com.epoint.basic.spgl.domain.SpglDfxmsplcjdsxxxb;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

import java.util.List;
import java.util.Map;

/**
 * 住建部_地方项目审批流程阶段事项信息表对应的后台service
 *
 * @author zhpengsy
 * @version [版本号, 2018-11-16 15:10:10]
 */
public class JnSpglDfxmsplcjdsxxxbService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public JnSpglDfxmsplcjdsxxxbService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglDfxmsplcjdsxxxb record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(SpglDfxmsplcjdsxxxb.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglDfxmsplcjdsxxxb record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglDfxmsplcjdsxxxb find(Object primaryKey) {
        return baseDao.find(SpglDfxmsplcjdsxxxb.class, primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *              ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public SpglDfxmsplcjdsxxxb find(String sql, Object... args) {
        return baseDao.find(sql, SpglDfxmsplcjdsxxxb.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglDfxmsplcjdsxxxb> findList(String sql, Object... args) {
        return baseDao.findList(sql, SpglDfxmsplcjdsxxxb.class, args);
    }

    /**
     * 分页查找一个list
     *
     * @param sql        查询语句
     * @param pageNumber 记录行的偏移量
     * @param pageSize   记录行的最大数目
     * @param clazz      可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args       参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglDfxmsplcjdsxxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, SpglDfxmsplcjdsxxxb.class, args);
    }

    public void deleteBySpGuid(String guid) {
        Entity en = SpglDfxmsplcjdsxxxb.class.getAnnotation(Entity.class);
        String sql = "delete from " + en.table() + " where SPLCBM = ?";
        baseDao.execute(sql, guid);
    }

    /**
     * 获取所有记录，如果没有缓存数据，就从数据库中获取
     *
     * @param baseClass
     * @param useCache
     * @return
     */
    public List<SpglDfxmsplcjdsxxxb> getAllRecord(Class<? extends BaseEntity> baseClass,
                                                  Map<String, String> conditionMap) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getListByCondition(baseClass, conditionMap);
    }

    public Integer getAllRecordCount(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        List<SpglDfxmsplcjdsxxxb> result = getAllRecord(baseClass, conditionMap);
        if (result != null) {
            return result.size();
        } else {
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
    public PageData<SpglDfxmsplcjdsxxxb> getAllRecordByPage(Class<? extends BaseEntity> baseClass,
                                                            Map<String, String> conditionMap, Integer first, Integer pageSize, String sortField, String sortOrder) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getDbListByPage(baseClass, conditionMap, first, pageSize, sortField, sortOrder);
    }

    public boolean isExistSplcSx(Double splcbbh, String splcbm, Double spsxbbh, String spsxbm) {
        String sql = "select count(1) from  SPGL_DFXMSPLCJDSXXXB where splcbbh=? and splcbm=? and spsxbbh=? and spsxbm=? and ifnull(sync,0) != 2 and sjyxbs = 1 and sjsczt not in ('2','-1') ";
        Integer count = baseDao.queryInt(sql, splcbbh, splcbm, spsxbbh, spsxbm);
        if (count != null && count > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<SpglDfxmsplcjdsxxxb> getNeedAddNewVersionByItemId(String item_id) {
        String sql = "select * from SPGL_DFXMSPLCJDSXXXB where  spsxbm = ?  and sjsczt in ('0','1','3') and ifnull(sync,0) != 2 and ifnull(sync,0) != -1 and sjyxbs= 1 GROUP BY XZQHDM,SPLCBM,SPLCBBH,SPJDXH,SPSXBM ";
        return baseDao.findList(sql, SpglDfxmsplcjdsxxxb.class, item_id);
    }

    /**
     * @return Record    返回类型
     * @throws
     * @Description: 查basetask表信息
     * @author male
     * @date 2020年7月21日 下午4:48:53
     */
    public Record getAuditSpBaseTaskInfo(String dfsjzj) {
        String sql = "select * from audit_sp_basetask where rowguid = ? ";
        return baseDao.find(sql, Record.class, dfsjzj);
    }

    public String getMaxSpsxbbh(Double splcbbh, String splcbm, String spsxbm) {
        String sql = "select spsxbbh from SPGL_DFXMSPLCJDSXXXB where splcbbh=? and splcbm=? and spsxbm=? and ifnull(sync,0) != 2 and sjyxbs = 1 and sjsczt not in ('2','-1') order by OperateDate desc limit 1 ";
        return baseDao.queryString(sql, splcbbh, splcbm, spsxbm);

    }

    public String getMaxSpsxbbhV3(Double splcbbh, String splcbm, String spsxbm) {
        String sql = "select spsxbbh from SPGL_SPLCJBLSXXXB_V3 where splcbbh=? and splcbm=? and spsxbm=? and ifnull(sync,0) != 2 and sjyxbs = 1 and sjsczt not in ('2','-1') order by SPSXBBH desc limit 1 ";
        return baseDao.queryString(sql, splcbbh, splcbm, spsxbm);
    }

    public List<String> getBasetaskBySplclxAndPhaseid(String splclx, String phaseid) {
        String sql = "select DISTINCT basetaskguid from audit_sp_task a,audit_sp_business b,audit_sp_phase c " +
                "where a.PHASEGUID = c.RowGuid and b.RowGuid = a.BUSINESSGUID and b.RowGuid = c.BUSINEDSSGUID " +
                "and b.splclxv3 = ? and c.PHASEID = ?";
        return baseDao.findList(sql, String.class, splclx, phaseid);
    }

}
