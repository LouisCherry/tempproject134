package com.epoint.xmz.jnyjsevaluate.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.BaseEntity;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.dao.CommonDao;
import com.epoint.xmz.jnyjsevaluate.api.entity.JnYjsEvaluate;

/**
 * 一件事评价表对应的后台service
 * 
 * @author 1
 * @version [版本号, 2022-11-11 14:59:29]
 */
public class JnYjsEvaluateService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public JnYjsEvaluateService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(JnYjsEvaluate record) {
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
        T t = baseDao.find(JnYjsEvaluate.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(JnYjsEvaluate record) {
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
    public JnYjsEvaluate find(Object primaryKey) {
        return baseDao.find(JnYjsEvaluate.class, primaryKey);
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
    public JnYjsEvaluate find(String sql,  Object... args) {
        return baseDao.find(sql, JnYjsEvaluate.class, args);
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
    public List<JnYjsEvaluate> findList(String sql, Object... args) {
        return baseDao.findList(sql, JnYjsEvaluate.class, args);
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
    public List<JnYjsEvaluate> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, JnYjsEvaluate.class, args);
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
    public Integer countJnYjsEvaluate(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
    
    public List<Record> findEvaluateList( int pageNumber, int pageSize,String applydateStart,String applydateEnd) {
    	String sql = "select  SUM(CASE WHEN satisfaction = '5' THEN 1 ELSE 0 END) satisfy5,"
    			+"SUM(CASE WHEN satisfaction = '2' THEN 1 ELSE 0 END) satisfy4,"
    			+"SUM(CASE WHEN satisfaction = '3' THEN 1 ELSE 0 END) satisfy3,"
    			+"SUM(CASE WHEN satisfaction = '2' THEN 1 ELSE 0 END) satisfy2,"
    			+"SUM(CASE WHEN satisfaction = '1' THEN 1 ELSE 0 END) satisfy1,businessname,businessguid"
    			+" from jn_yjs_evaluate where 1=1 ";
    	
    	// 申请时间条件判断
        if (StringUtil.isNotBlank(applydateStart) && StringUtil.isNotBlank(applydateEnd)) {
        	sql += " and operatedate >= '" + EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(applydateStart), "yyyy-MM-dd HH:mm:ss") ;
        	sql	+= "' and operatedate <= '" + EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDateStr(applydateEnd), "yyyy-MM-dd HH:mm:ss")+"'";
        }
        else if (StringUtil.isNotBlank(applydateStart) && StringUtil.isBlank(applydateEnd)) {
            sql += " and operatedate >= '" +EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(applydateStart), "yyyy-MM-dd HH:mm:ss") + "'";
        }
        else if (StringUtil.isBlank(applydateStart) && StringUtil.isNotBlank(applydateEnd)) {
        	 sql += " and operatedate <= '" +EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(applydateStart), "yyyy-MM-dd HH:mm:ss") + "'";
        }
        
        sql += " group by businessname";
        return baseDao.findList(sql, pageNumber, pageSize, Record.class);
    }
    
    public Integer countEvaluate(String applydateStart,String applydateEnd){
    	String sql = "select  SUM(CASE WHEN satisfaction = '5' THEN 1 ELSE 0 END) satisfy5,"
    			+"SUM(CASE WHEN satisfaction = '2' THEN 1 ELSE 0 END) satisfy4,"
    			+"SUM(CASE WHEN satisfaction = '3' THEN 1 ELSE 0 END) satisfy3,"
    			+"SUM(CASE WHEN satisfaction = '2' THEN 1 ELSE 0 END) satisfy2,"
    			+"SUM(CASE WHEN satisfaction = '1' THEN 1 ELSE 0 END) satisfy1,businessname,businessguid"
    			+" from jn_yjs_evaluate where 1=1 ";
    	
    	// 申请时间条件判断
        if (StringUtil.isNotBlank(applydateStart) && StringUtil.isNotBlank(applydateEnd)) {
        	sql += " and operatedate >= '" + EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(applydateStart), "yyyy-MM-dd HH:mm:ss");
        	sql	+= "' and operatedate <= '" + EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDateStr(applydateEnd), "yyyy-MM-dd HH:mm:ss")+"'";
        }
        else if (StringUtil.isNotBlank(applydateStart) && StringUtil.isBlank(applydateEnd)) {
            sql += " and operatedate >= '" +EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(applydateStart), "yyyy-MM-dd HH:mm:ss") + "'";
        }
        else if (StringUtil.isBlank(applydateStart) && StringUtil.isNotBlank(applydateEnd)) {
        	 sql += " and operatedate <= '" +EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(applydateStart), "yyyy-MM-dd HH:mm:ss") + "'";
        }
        
        sql += " group by businessname";
    	
    	
    	String newsql = "select count(1) from ("+sql + ") s";
        return baseDao.queryInt(newsql);
    }
    
}
