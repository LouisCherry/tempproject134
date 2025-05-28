package com.epoint.xmz.xmztaskguideconfig.impl;
import java.util.List;
import java.util.Map;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.xmztaskguideconfig.api.entity.XmzTaskguideConfig;

/**
 * 事项指南配置表对应的后台service
 * 
 * @author xczheng0314
 * @version [版本号, 2023-03-21 11:38:55]
 */
public class XmzTaskguideConfigService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public XmzTaskguideConfigService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(XmzTaskguideConfig record) {
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
        T t = baseDao.find(XmzTaskguideConfig.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(XmzTaskguideConfig record) {
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
    public XmzTaskguideConfig find(Object primaryKey) {
        return baseDao.find(XmzTaskguideConfig.class, primaryKey);
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
    public XmzTaskguideConfig find(String sql,  Object... args) {
        return baseDao.find(sql, XmzTaskguideConfig.class, args);
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
    public List<XmzTaskguideConfig> findList(String sql, Object... args) {
        return baseDao.findList(sql, XmzTaskguideConfig.class, args);
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
    public List<XmzTaskguideConfig> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, XmzTaskguideConfig.class, args);
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
    public Integer countXmzTaskguideConfig(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
    public List<XmzTaskguideConfig> selectTaskGuidefigList(Map<String, String> map, int pagenum, int pagesize) {
        SQLManageUtil sqlManageUtil=new SQLManageUtil();
        String s = sqlManageUtil.buildPatchSql(map);
        String sql = "select * from xmz_taskguide_config where 1=1 "+s+ " order by createdate desc";
        return baseDao.findList(sql, pagenum*pagesize, pagesize, XmzTaskguideConfig.class);
    }
    public List<AuditTask> selectTaskList(String areacode) {
        String sql = "select a.RowGuid ,a.TaskName from AUDIT_TASK a inner join frame_ou_extendinfo foe on a.OUGUID = foe .OUGUID where 1=1  and IS_HISTORY=0 and IS_EDITAFTERIMPORT=1 and IS_ENABLE=1 and foe.areacode=? ;"; 
        return baseDao.findList(sql, AuditTask.class ,areacode);
    }
}
