package com.epoint.cs.yyzzprint.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.core.grammar.Record;
import com.epoint.core.BaseEntity;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.dao.CommonDao;
import com.epoint.cs.yyzzprint.api.entity.Yyzzprint;

/**
 * 营业执照打印记录表对应的后台service
 * 
 * @author admin
 * @version [版本号, 2020-04-23 11:25:13]
 */
public class YyzzprintService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public YyzzprintService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(Yyzzprint record) {
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
        T t = baseDao.find(Yyzzprint.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(Yyzzprint record) {
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
    public Yyzzprint find(Object primaryKey) {
        return baseDao.find(Yyzzprint.class, primaryKey);
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
    public Yyzzprint find(String sql,  Object... args) {
        return baseDao.find(sql, Yyzzprint.class, args);
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
    public List<Yyzzprint> findList(String sql, Object... args) {
        return baseDao.findList(sql, Yyzzprint.class, args);
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
    public List<Yyzzprint> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, Yyzzprint.class, args);
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
    public Integer countYyzzprint(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
    
    public int getPrintCount(String idnum){
        ICommonDao commondao = CommonDao.getInstance();
        String sql ="select count(1) from yyzzprint where creditcode='"+idnum+"'";
        return commondao.queryInt(sql);
    }
    public Yyzzprint getPrint(String idnum){
        ICommonDao commondao = CommonDao.getInstance();
        String sql ="select * from yyzzprint where reason='"+idnum+"'  order by operatedate desc";
        return commondao.find(sql, Yyzzprint.class);
    }
    
    public Yyzzprint getPrintdeatil(String idnum){
        ICommonDao commondao = CommonDao.getInstance();
        String sql ="select * from yyzzprint where creditcode='"+idnum+"'";
        return commondao.find(sql, Yyzzprint.class);
    }
}
