package com.epoint.xmz.xmyyyzbjinfo.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.xmyyyzbjinfo.api.entity.XmYyyzBjinfo;
import com.epoint.core.BaseEntity;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.dao.CommonDao;

/**
 * 泰安一业一证对接办结信息表对应的后台service
 * 
 * @author LYA
 * @version [版本号, 2020-07-16 15:17:24]
 */
public class XmYyyzBjinfoService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public XmYyyzBjinfoService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(XmYyyzBjinfo record) {
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
        T t = baseDao.find(XmYyyzBjinfo.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(XmYyyzBjinfo record) {
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
    public XmYyyzBjinfo find(Object primaryKey) {
        return baseDao.find(XmYyyzBjinfo.class, primaryKey);
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
    public XmYyyzBjinfo find(String sql,  Object... args) {
        return baseDao.find(sql, XmYyyzBjinfo.class, args);
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
    public List<XmYyyzBjinfo> findList(String sql, Object... args) {
        return baseDao.findList(sql, XmYyyzBjinfo.class, args);
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
    public List<XmYyyzBjinfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, XmYyyzBjinfo.class, args);
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
    public Integer countXmYyyzBjinfo(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
    /**
     * 
     *  [根据申报流水号查询办结表数据信息] 
     *  @param flowsn
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public XmYyyzBjinfo findXmYyyzBjinfoByFlowsn(String flowsn) {
        String sql = "select * from xm_yyyz_bjinfo where flowsn = ?";
        return this.find(sql, flowsn);
    }
}
