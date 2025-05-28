package com.epoint.jn.externalprojectinfo.impl;
import java.util.Date;
import java.util.List;

import com.epoint.common.util.StringUtil;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.database.jdbc.config.SplitTableConfig;
import com.epoint.frame.service.metadata.sharding.util.ShardingUtil;
import com.epoint.jn.externalprojectinfo.api.entity.ExternalProjectInfo;
import org.springframework.util.CollectionUtils;

/**
 * 外部办件基本信息表对应的后台service
 * 
 * @author wannengDB
 * @version [版本号, 2022-01-06 14:37:04]
 */
public class ExternalProjectInfoService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public ExternalProjectInfoService() {
        baseDao = getInstance(ExternalProjectInfo.class);
    }
    
    public CommonDao getInstance(Class<? extends BaseEntity> baseClass) {
        CommonDao commonDao = null;
        Entity en = baseClass.getAnnotation(Entity.class);
        SplitTableConfig conf = ShardingUtil.getSplitTableConfig(en.table());
        if (conf != null) {
            commonDao = CommonDao.getInstance(conf);
        }
        else {
            commonDao = CommonDao.getInstance();
        }
        return commonDao;
    }
    
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ExternalProjectInfo record) {
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
        T t = baseDao.find(ExternalProjectInfo.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ExternalProjectInfo record) {
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
    public ExternalProjectInfo find(Object primaryKey) {
        return baseDao.find(ExternalProjectInfo.class, primaryKey);
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
    public ExternalProjectInfo find(String sql,  Object... args) {
        return baseDao.find(sql, ExternalProjectInfo.class, args);
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
//    public List<ExternalProjectInfo> findList(String sql, Object... args) {
//        return baseDao.findList(sql, ExternalProjectInfo.class, args);
//    }

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
    public List<ExternalProjectInfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, ExternalProjectInfo.class, args);
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
    public Integer countExternalProjectInfo(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
    public int insert(ExternalProjectInfo record, int month) {
        String sql = "INSERT INTO external_project_info_"+ month + " "+
                "(BelongXiaQuCode, OperateUserName, OperateDate, Row_ID, YearFlag, RowGuid, accept_date, accept_user_guid, accept_ou_guid, areacode, task_guid, task_id, complete_date) " + 
                "VALUES(NULL, NULL, ?, NULL, NULL, ?, ?, ?, ?, ?, ?, ?, ?);";  
        return baseDao.execute(sql, new Date(),record.getRowguid(),record.getAccept_date(),record.getAccept_user_guid(),record.getAccept_ou_guid(),record.getAreacode(),record.getTask_guid(),record.getTask_id(),record.getComplete_date());
    }

    /**
     * 根据辖区代码，办件guids获取办件列表
     * @param first
     * @param pageSize
     * @param areacode
     * @param projectGuids
     * @return
     */
    public List<ExternalProjectInfo> findList(int first, int pageSize, String areacode, List<String> projectGuids) {
        String sql = "select a.XiaQuCode as areacode,a.OuName,sum(CASE WHEN e.`RowGuid` is not null THEN 1 ELSE 0 end) as allnum " +
                "from external_project_info e right join audit_orga_area a on e.areacode = a.XiaQuCode where 1=1 ";
        if (!CollectionUtils.isEmpty(projectGuids)){
            sql += "and e.rowguid in ('"+
                    StringUtil.join(projectGuids,"','") + "') ";
        }
        if (StringUtil.isNotBlank(areacode)){
            sql += " and a.XiaQuCode like '"+areacode +"%'";
        }
        sql += " group by a.XiaQuCode";

        return baseDao.findList(sql,first,pageSize,ExternalProjectInfo.class);
    }

    public Integer findCount(String areacode, List<String> projectGuids) {
        String sql = "select a.XiaQuCode as areacode,a.OuName,sum(CASE WHEN e.`RowGuid` is not null THEN 1 ELSE 0 end) as allnum " +
                "from external_project_info e right join audit_orga_area a on e.areacode = a.XiaQuCode where 1=1 ";
        if (!CollectionUtils.isEmpty(projectGuids)){
            sql += "and e.rowguid in ('"+
                    StringUtil.join(projectGuids,"','") + "') ";
        }
        if (StringUtil.isNotBlank(areacode)){
            sql += " and a.XiaQuCode like '"+areacode +"%'";
        }
        sql += " group by a.XiaQuCode";
        List<ExternalProjectInfo> list = baseDao.findList(sql, ExternalProjectInfo.class);
        return list.size();
    }
}
