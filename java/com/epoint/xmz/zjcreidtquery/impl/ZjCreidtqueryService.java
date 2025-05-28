package com.epoint.xmz.zjcreidtquery.impl;
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
import com.epoint.xmz.zjcreidtquery.api.entity.ZjCreidtquery;

/**
 * 信用查询调用统计表对应的后台service
 * 
 * @author 1
 * @version [版本号, 2021-10-09 14:58:46]
 */
public class ZjCreidtqueryService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public ZjCreidtqueryService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ZjCreidtquery record) {
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
        T t = baseDao.find(ZjCreidtquery.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ZjCreidtquery record) {
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
    public ZjCreidtquery find(Object primaryKey) {
        return baseDao.find(ZjCreidtquery.class, primaryKey);
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
    public ZjCreidtquery find(String sql,  Object... args) {
        return baseDao.find(sql, ZjCreidtquery.class, args);
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
    public List<ZjCreidtquery> findList(String sql, Object... args) {
        return baseDao.findList(sql, ZjCreidtquery.class, args);
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
    public List<ZjCreidtquery> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, ZjCreidtquery.class, args);
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
    public Integer countZjCreidtquery(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
    
    public List<Record> finList(int first, int pagesize, Date starttime, Date endtime,String type,String areacode) {
        String sql = "select areacode,areaname,createtime,type,count(1) as total from zj_creidtquery where 1=1 ";
        if (StringUtil.isNotBlank(starttime)) {
        	sql += " and createtime >= '" + EpointDateUtil.convertDate2String(starttime, EpointDateUtil.DATE_FORMAT) +" 00:00:00'";
        }
        if (StringUtil.isNotBlank(endtime)) {
        	sql += " and createtime <= '" +EpointDateUtil.convertDate2String(endtime, EpointDateUtil.DATE_FORMAT)+ " 23:59:59'";
        }
        if (StringUtil.isNotBlank(type)) {
        	sql += " and type = '" + type + "'";
        }
        if (StringUtil.isNotBlank(areacode)) {
        	sql += " and areacode = '"+areacode+"'";
        }
        
        if((first!=0&&pagesize!=0)||(first==0&&pagesize!=0)){
            sql+=" group by areacode,type limit "+first+","+pagesize;
        }
        List<Record> list = baseDao.findList(sql, Record.class);
        
        
        return list;
    }
    
    
    public Integer finTotal( Date starttime, Date endtime,String type,String areacode) {
    	 String sql = "select count(1) from zj_creidtquery where 1=1 ";
    	 
         if (StringUtil.isNotBlank(starttime)) {
         	sql += " and createtime >= '" + EpointDateUtil.convertDate2String(starttime, EpointDateUtil.DATE_FORMAT) +" 00:00:00'";
         }
         if (StringUtil.isNotBlank(endtime)) {
         	sql += " and createtime <= '" +EpointDateUtil.convertDate2String(endtime, EpointDateUtil.DATE_FORMAT)+ " 23:59:59'";
         }
         
         if (StringUtil.isNotBlank(type)) {
         	sql += " and type = '" + type + "'";
         }
         if (StringUtil.isNotBlank(areacode)) {
         	sql += " and areacode = '"+areacode+"'";
         }
         
         
        int i= baseDao.queryInt(sql);
        return i;
    }
    
}
