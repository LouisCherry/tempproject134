package com.epoint.auditsp.yqxm.impl;

import com.epoint.auditsp.yqxm.api.entity.StSpglGcJdXxb;
import com.epoint.auditsp.yqxm.api.entity.StSpglGcjbxxb;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.entity.CodeItems;

import java.util.ArrayList;
import java.util.List;

/**
 * 工程基本信息表对应的后台service
 *
 * @author scr
 * @version [版本号, 2019-07-02 19:33:49]
 */
public class StSpglGcjbxxbService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public StSpglGcjbxxbService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(StSpglGcjbxxb record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(StSpglGcjbxxb.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(StSpglGcjbxxb record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public StSpglGcjbxxb find(Object primaryKey) {
        return baseDao.find(StSpglGcjbxxb.class, primaryKey);
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
    public StSpglGcjbxxb find(String sql, Object... args) {
        return baseDao.find(sql, StSpglGcjbxxb.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<StSpglGcjbxxb> findList(String sql, Object... args) {
        return baseDao.findList(sql, StSpglGcjbxxb.class, args);
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
    public List<StSpglGcjbxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, StSpglGcjbxxb.class, args);
    }

    // 城市
    public List<CodeItems> findCity(String codename) {
        String sql = "select * from code_items  where   CODEID=(select CODEID from code_main  where codename=?)  and ITEMVALUE like '34%' and itemtext!='安徽省'  and (itemtext LIKE '%市'or itemtext like '%区' or itemtext like '%广德县%' or itemtext like '%宿松县%')";
        return baseDao.findList(sql, CodeItems.class, codename);
    }

    /**
     * 查找工程基本信息 [功能详细描述]
     *
     * @param xmdm
     * @param gcdm
     * @param xzqhdm
     * @return
     * @see [类、类#方法、类#成员]
     */
    public StSpglGcjbxxb findGcjbxx(String xmdm, String gcdm, String xzqhdm) {
        List<String> params = new ArrayList<>();
        String sql = "select * from ST_SPGL_GCJBXXB where SJYXBS=1 ";
        if (StringUtil.isNotBlank(xmdm)) {
            sql += " and xmdm = ? ";
            params.add(xmdm);
        }
        if (StringUtil.isNotBlank(gcdm)) {
            sql += " and gcdm = ? ";
            params.add(gcdm);
        }
        if (StringUtil.isNotBlank(xzqhdm)) {
            sql += " and xzqhdm = ? ";
            params.add(xzqhdm);
        }
        return baseDao.find(sql, StSpglGcjbxxb.class, params.toArray());
    }

    // 查询国标行业
    public String findGbhy(String gbhycode) {
        String sql = "select DISTINCT item_name from validate_item  where item_code = ? ";
        return baseDao.queryString(sql, gbhycode);
    }

    public List<StSpglGcJdXxb> findGcJdXxb(String sql, String sXZQHDM, String sGCDM) {
        return baseDao.findList(sql, StSpglGcJdXxb.class, sXZQHDM, sGCDM);
    }

}
