package com.epoint.xmz.certczqcgxdj.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import com.epoint.xmz.certczqcgxdj.api.entity.CertCzqcgxdj;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.certczcjsybj.api.entity.CertCzcjsybj;
import com.epoint.xmz.certczqcgxdj.api.ICertCzqcgxdjService;

/**
 * 出租汽车更新登记计划库对应的后台service实现类
 * 
 * @author dyxin
 * @version [版本号, 2023-05-22 13:17:25]
 */
@Component
@Service
public class CertCzqcgxdjServiceImpl implements ICertCzqcgxdjService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(CertCzqcgxdj record) {
        return new CertCzqcgxdjService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new CertCzqcgxdjService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(CertCzqcgxdj record) {
        return new CertCzqcgxdjService().update(record);
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
    public CertCzqcgxdj find(Object primaryKey) {
        return new CertCzqcgxdjService().find(primaryKey);
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
    public CertCzqcgxdj find(String sql, Object... args) {
        return new CertCzqcgxdjService().find(sql, args);
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
    public List<CertCzqcgxdj> findList(String sql, Object... args) {
        return new CertCzqcgxdjService().findList(sql, args);
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
    public List<CertCzqcgxdj> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new CertCzqcgxdjService().findList(sql, pageNumber, pageSize, args);
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
    @Override
    public Integer countCertCzqcgxdj(String sql, Object... args) {
        return new CertCzqcgxdjService().countCertCzqcgxdj(sql, args);
    }

    @Override
    public PageData<CertCzqcgxdj> findPageData(Map<String, String> map, int first, int pageSize, String sortField,
            String sortOrder) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        if (StringUtil.isBlank(sortOrder) || StringUtil.isBlank(sortField)) {
            return sqlManageUtil.getDbListByPage(CertCzqcgxdj.class, map, first, pageSize, "indexdesc", "desc");
        }
        return sqlManageUtil.getDbListByPage(CertCzqcgxdj.class, map, first, pageSize, sortField, sortOrder);
    }

    @Override
    public int getCount(Map<String, String> map) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        return sqlManageUtil.getListCount(CertCzqcgxdj.class, map);
    }

}
