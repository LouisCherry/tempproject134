package com.epoint.xmz.kyperson.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.core.BaseEntity;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.dao.CommonDao;
import com.epoint.xmz.kyperson.api.entity.KyPerson;
import com.epoint.xmz.kypoint.api.entity.KyPoint;

/**
 * 勘验人员表对应的后台service
 * 
 * @author RaoShaoliang
 * @version [版本号, 2023-07-10 16:34:44]
 */
public class KyPersonService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public KyPersonService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(KyPerson record) {
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
        T t = baseDao.find(KyPerson.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(KyPerson record) {
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
    public KyPerson find(Object primaryKey) {
        return baseDao.find(KyPerson.class, primaryKey);
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
    public KyPerson find(String sql,  Object... args) {
        return baseDao.find(sql, KyPerson.class, args);
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
    public List<KyPerson> findList(String sql, Object... args) {
        return baseDao.findList(sql, KyPerson.class, args);
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
    public List<KyPerson> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, KyPerson.class, args);
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
    public Integer countKyPerson(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
    public PageData<KyPerson> paginatorList(Map<String, String> conditionMap, int pageNumber, int pageSize) {
        List<String> params = new ArrayList<>();
        String sql = new SQLManageUtil().buildSqlComoplete(KyPerson.class, conditionMap, params);
        List<KyPerson> list = baseDao.findList(sql, pageNumber, pageSize,
                KyPerson.class, params.toArray());
        int count = list.size();
        return new PageData<KyPerson>(list, count);
    }
    public List<KyPerson> getPersonByProjectguid(String projectguid) {
        String sql = "select * from ky_person where kyguid = ? ";
        return baseDao.findList(sql, KyPerson.class, projectguid);
    }
}
