package com.epoint.qypg.spglqypgxxb.impl;

import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.DbKit;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.qypg.spglqypgxxb.api.entity.SpglQypgxxb;

import java.util.List;
import java.util.Map;

/**
 * 区域评估信息表对应的后台service
 *
 * @author Epoint
 * @version [版本号, 2023-09-15 13:56:34]
 */
public class SpglQypgxxbService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public SpglQypgxxbService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglQypgxxb record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param guid BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(SpglQypgxxb.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglQypgxxb record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglQypgxxb find(Object primaryKey) {
        return baseDao.find(SpglQypgxxb.class, primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql  查询语句
     * @param args 参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public SpglQypgxxb find(String sql, Object... args) {
        return baseDao.find(sql, SpglQypgxxb.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql  查询语句
     * @param args 参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglQypgxxb> findList(String sql, Object... args) {
        return baseDao.findList(sql, SpglQypgxxb.class, args);
    }

    /**
     * 分页查找一个list
     *
     * @param sql        查询语句
     * @param pageNumber 记录行的偏移量
     * @param pageSize   记录行的最大数目
     * @param args       参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglQypgxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, SpglQypgxxb.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countSpglQypgxxb(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    public PageData<SpglQypgxxb> getAuditSpDanitemByPage(Map<String, String> conditionMap, int firstResult,
                                                         int maxResults, String sortField, String sortOrder) {
        return new SQLManageUtil().getDbListByPage(SpglQypgxxb.class, conditionMap, firstResult, maxResults, sortField,
                sortOrder);
    }

    public List<SpglQypgxxb> getListByMap(Map<String, String> map) {
        String sql = "select * from spgl_qypgxxb_edit ";
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        sql += sqlManageUtil.buildSql(map);
        return baseDao.findList(sql, SpglQypgxxb.class);
    }

    public PageData<SpglQypgxxb> getNotAssociationPageData(String itemguid, String subappguid, int firstResult, int maxResults,
                                                           String sortField, String sortOrder) {
        String sql = "select * from spgl_qypgxxb_edit where rowguid not in (select qypgguid from spgl_qypgxxb_edit_r where pre_itemguid = ? and subappguid = ? ) ";
        String sql2 = "select count(1) from spgl_qypgxxb_edit where rowguid not in (select qypgguid from spgl_qypgxxb_edit_r where pre_itemguid = ? and subappguid = ?) ";
        if (StringUtil.isNotBlank(sortField)) {
            sql += " order by " + DbKit.checkOrderField(sql, sortField, SpglQypgxxb.class);
            if (StringUtil.isNotBlank(sortOrder)) {
                sql += " " + DbKit.checkOrderDirect(sql, (StringUtil.isNotBlank(sortOrder) ? sortOrder : ""));
            }
        }
        List<SpglQypgxxb> list = baseDao.findList(sql, firstResult, maxResults, SpglQypgxxb.class, itemguid, subappguid);
        PageData<SpglQypgxxb> pageData = new PageData<SpglQypgxxb>();
        pageData.setList(list);
        pageData.setRowCount(baseDao.queryInt(sql2, itemguid, subappguid));
        return pageData;
    }
}
