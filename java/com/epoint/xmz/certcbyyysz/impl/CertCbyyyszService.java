package com.epoint.xmz.certcbyyysz.impl;
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
import com.epoint.xmz.certcbyyysz.api.entity.CertCbyyysz;
import com.epoint.xmz.certggxkws.api.entity.CertGgxkws;

/**
 * 船舶营业运输证本地库对应的后台service
 * 
 * @author 1
 * @version [版本号, 2022-06-15 14:41:13]
 */
public class CertCbyyyszService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public CertCbyyyszService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(CertCbyyysz record) {
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
        T t = baseDao.find(CertCbyyysz.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(CertCbyyysz record) {
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
    public CertCbyyysz find(Object primaryKey) {
        return baseDao.find(CertCbyyysz.class, primaryKey);
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
    public CertCbyyysz find(String sql,  Object... args) {
        return baseDao.find(sql, CertCbyyysz.class, args);
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
    public List<CertCbyyysz> findList(String sql, Object... args) {
        return baseDao.findList(sql, CertCbyyysz.class, args);
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
    public List<CertCbyyysz> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, CertCbyyysz.class, args);
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
    public Integer countCertCbyyysz(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
    
    public CertCbyyysz getCertByCertno(String certno) {
    	String sql = "select * from cert_cbyyysz where jyxkzbh = ? and is_enable = '1'";
        return baseDao.find(sql,CertCbyyysz.class, certno);
    }
    public CertCbyyysz getYywszBhCertByCertno(String certno) {
    	String sql = "select * from cert_cbyyysz where yyzbh = ? and is_enable = '1'";
    	return baseDao.find(sql,CertCbyyysz.class, certno);
    }
}
