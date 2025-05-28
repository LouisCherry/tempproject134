package com.epoint.xmz.jmmparamtable.impl;

import com.epoint.common.util.StringUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.jmmparamtable.api.entity.Jmmparamtable;

import java.util.List;

/**
 * 居民码市县参数配置表对应的后台service
 *
 * @author Administrator
 * @version [版本号, 2023-06-26 14:34:17]
 */
public class JmmparamtableService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public JmmparamtableService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(Jmmparamtable record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(Jmmparamtable.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(Jmmparamtable record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public Jmmparamtable find(Object primaryKey) {
        return baseDao.find(Jmmparamtable.class, primaryKey);
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
    public Jmmparamtable find(String sql, Object... args) {
        return baseDao.find(sql, Jmmparamtable.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<Jmmparamtable> findList(String sql, Object... args) {
        return baseDao.findList(sql, Jmmparamtable.class, args);
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
    public List<Jmmparamtable> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, Jmmparamtable.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countJmmparamtable(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    public Jmmparamtable getJmmParamByAreacode(String areacode) {
        String sql = "select * from jmmparamtable where areacode = ? limit 1";
        return baseDao.find(sql, Jmmparamtable.class, areacode);
    }
    public Jmmparamtable getJmmParamByCondition(String areacode,String scene){
        StringBuffer sql=new StringBuffer("select * from jmmparamtable where areacode = ?");
        if(StringUtil.isNotBlank(scene)){
            sql.append(" and scene = '").append(scene).append("'");
        }
        sql.append(" limit 1");
        return baseDao.find(sql.toString(), Jmmparamtable.class, areacode);
    }
}
