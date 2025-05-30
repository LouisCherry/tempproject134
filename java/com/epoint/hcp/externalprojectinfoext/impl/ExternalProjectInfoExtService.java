package com.epoint.hcp.externalprojectinfoext.impl;

import java.util.List;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.config.SplitTableConfig;
import com.epoint.frame.service.metadata.sharding.util.ShardingUtil;
import com.epoint.hcp.externalprojectinfoext.api.entity.ExternalProjectInfoExt;

/**
 * 外部办件基本扩展信息表对应的后台service
 * 
 * @author wannengDB
 * @version [版本号, 2022-01-06 14:54:57]
 */
public class ExternalProjectInfoExtService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public ExternalProjectInfoExtService() {
        baseDao = getInstance(ExternalProjectInfoExt.class);
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
    public int insert(ExternalProjectInfoExt record) {
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
        T t = baseDao.find(ExternalProjectInfoExt.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ExternalProjectInfoExt record) {
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
    public ExternalProjectInfoExt find(Object primaryKey) {
        return baseDao.find(ExternalProjectInfoExt.class, primaryKey);
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
    public ExternalProjectInfoExt find(String sql, Object... args) {
        return baseDao.find(sql, ExternalProjectInfoExt.class, args);
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
    public List<ExternalProjectInfoExt> findList(String sql, Object... args) {
        return baseDao.findList(sql, ExternalProjectInfoExt.class, args);
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
    public List<ExternalProjectInfoExt> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, ExternalProjectInfoExt.class, args);
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
    public Integer countExternalProjectInfoExt(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    /**
     * 
     * [分页查询列表数据]
     * 
     * @param first
     * @param pagesize
     * @param ouguid
     * @param areacode
     * @return
     */
    public List<Record> finList(int first, int pagesize, String ouguid, String areacode) {
        String sql = "select * from external_project_info_ext_ext where 1=1 and is_lczj = '9' ";
        if (StringUtil.isNotBlank(ouguid)) {
            sql += " and accept_ou_guid = '" + ouguid + "' ";
        }
        if (StringUtil.isNotBlank(areacode)) {
            sql += " and areacode = '" + areacode + "'";
        }
        // List<Record> list = commonDaoTo.findList(sql, Record.class, ouguid);
        return baseDao.findList(sql, first, pagesize, Record.class, ouguid, areacode);
    }

    /**
     * 
     * [分页查询列表总数]
     * 
     * @param ouguid
     * @param areacode
     * @return
     */
    public Integer finTotal(String ouguid, String areacode) {
        String sql = "select count(1) from external_project_info_ext_ext where is_lczj = '9' ";
        if (StringUtil.isNotBlank(ouguid)) {
            sql += " and ouguid = '" + ouguid + "' ";
        }
        if (StringUtil.isNotBlank(areacode)) {
            sql += " and areacode = '" + areacode + "'";
        }
        return baseDao.queryInt(sql);
    }

    public List<Record> finList(int first, int pagesize, String ouguid, String areacode,String projectno) {
        String sql = "select * from external_project_info_ext_ext where 1=1 ";
        if (StringUtil.isNotBlank(ouguid)) {
            sql += " and accept_ou_guid = '" + ouguid + "' ";
        }
        if (StringUtil.isNotBlank(areacode)) {
            sql += " and areacode = '" + areacode + "'";
        }
        if (StringUtil.isNotBlank(projectno)) {
            sql += " and project_no like '%" + projectno + "%'";
        }
        sql += " order by accept_date DESC";
        // List<Record> list = commonDaoTo.findList(sql, Record.class, ouguid);
        return baseDao.findList(sql, first, pagesize, Record.class, ouguid, areacode);
    }

    /**
     *
     * [分页查询列表总数]
     *
     * @param ouguid
     * @param areacode
     * @return
     */
    public Integer finTotal(String ouguid, String areacode,String projectno) {
        String sql = "select count(1) from external_project_info_ext_ext where is_lczj = '9' ";
        if (StringUtil.isNotBlank(ouguid)) {
            sql += " and ouguid = '" + ouguid + "' ";
        }
        if (StringUtil.isNotBlank(areacode)) {
            sql += " and areacode = '" + areacode + "'";
        }
        if (StringUtil.isNotBlank(projectno)) {
            sql += " and project_no like '%" + projectno + "%'";
        }
        return baseDao.queryInt(sql);
    }
    
    public ExternalProjectInfoExt getExternalProjectByProjectguid(String proejctguid,String month) {
    	String tablename = "external_project_info_ext";
    	 if ("1".equals(month)) {
        	 tablename = " external_project_info_ext_1 ";
        }
        else if ("2".equals(month)) {
        	 tablename = " external_project_info_ext_2 ";
        }
        else if ("3".equals(month)) {
        	tablename = " external_project_info_ext_3 ";
        }
        else if ("4".equals(month)) {
        	tablename = " external_project_info_ext_4 ";
        }
        else if ("5".equals(month)) {
        	tablename = " external_project_info_ext_5 ";
        }
        else if ("6".equals(month)) {
        	tablename = " external_project_info_ext_6 ";
        }
        else if ("7".equals(month)) {
        	tablename = " external_project_info_ext_7 ";
        }
        else if ("8".equals(month)) {
        	tablename = " external_project_info_ext_8 ";
        }
        else if ("9".equals(month)) {
        	tablename = " external_project_info_ext_9 ";
        }
        else if ("10".equals(month)) {
        	tablename = " external_project_info_ext_10 ";
        }
        else if ("11".equals(month)) {
        	tablename = " external_project_info_ext_11 ";
        }
        else if ("12".equals(month)) {
        	tablename = " external_project_info_ext_12 ";
        }
        
    	String sql = "select * from "+tablename
    			+ " where project_guid = ?";
        return baseDao.find(sql,ExternalProjectInfoExt.class, proejctguid);
    }
    
}
