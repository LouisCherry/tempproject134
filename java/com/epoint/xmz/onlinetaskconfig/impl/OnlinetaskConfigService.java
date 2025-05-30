package com.epoint.xmz.onlinetaskconfig.impl;
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
import com.epoint.xmz.onlinetaskconfig.api.entity.OnlinetaskConfig;

/**
 * 居民办事配置对应的后台service
 * 
 * @author RaoShaoliang
 * @version [版本号, 2023-10-17 15:38:09]
 */
public class OnlinetaskConfigService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public OnlinetaskConfigService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(OnlinetaskConfig record) {
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
        T t = baseDao.find(OnlinetaskConfig.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(OnlinetaskConfig record) {
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
    public OnlinetaskConfig find(Object primaryKey) {
        return baseDao.find(OnlinetaskConfig.class, primaryKey);
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
    public OnlinetaskConfig find(String sql,  Object... args) {
        return baseDao.find(sql, OnlinetaskConfig.class, args);
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
    public List<OnlinetaskConfig> findList(String sql, Object... args) {
        return baseDao.findList(sql, OnlinetaskConfig.class, args);
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
    public List<OnlinetaskConfig> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, OnlinetaskConfig.class, args);
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
    public Integer countOnlinetaskConfig(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }

    public List<OnlinetaskConfig> getAllConfg() {
        String sql = "select * from onlinetask_config where 1=1 ";
        return baseDao.findList(sql,OnlinetaskConfig.class);
    }

    public List<OnlinetaskConfig> getAllConfgByOu(String ou) {
        String sql = "select * from onlinetask_config where ouguid = ? ";
        return baseDao.findList(sql,OnlinetaskConfig.class,ou);
    }

    public List<OnlinetaskConfig> getConfigByOuname(String ouname) {
        String sql = "select * from onlinetask_config where ouname like '%"+ouname+"%' ";
        return baseDao.findList(sql,OnlinetaskConfig.class);
    }
}
