package com.epoint.xmz.gxhimportcert.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import com.epoint.xmz.gxhimportcert.api.entity.GxhImportCert;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.gxhimportcert.api.IGxhImportCertService;

/**
 * 个性化证照导入表对应的后台service实现类
 * 
 * @author dyxin
 * @version [版本号, 2023-06-12 16:30:17]
 */
@Component
@Service
public class GxhImportCertServiceImpl implements IGxhImportCertService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(GxhImportCert record) {
        return new GxhImportCertService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new GxhImportCertService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(GxhImportCert record) {
        return new GxhImportCertService().update(record);
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
    public GxhImportCert find(Object primaryKey) {
        return new GxhImportCertService().find(primaryKey);
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
    public GxhImportCert find(String sql, Object... args) {
        return new GxhImportCertService().find(sql, args);
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
    public List<GxhImportCert> findList(String sql, Object... args) {
        return new GxhImportCertService().findList(sql, args);
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
    public List<GxhImportCert> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new GxhImportCertService().findList(sql, pageNumber, pageSize, args);
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
    public Integer countGxhImportCert(String sql, Object... args) {
        return new GxhImportCertService().countGxhImportCert(sql, args);
    }

    @Override
    public Integer getListCount(Map<String, String> map) {
        SQLManageUtil sql = new SQLManageUtil();
        return sql.getListCount(GxhImportCert.class, map);
    }

    @Override
    public PageData<GxhImportCert> findPageData(Map<String, String> map, int first, int pageSize, String sortField,
            String sortOrder) {
        SQLManageUtil sql = new SQLManageUtil();
        return sql.getDbListByPage(GxhImportCert.class, map, first, pageSize, sortField, sortOrder);
    }

    @Override
    public List<GxhImportCert> findListByCondition(Map<String, String> map) {
        SQLManageUtil sql = new SQLManageUtil();
        return sql.getListByCondition(GxhImportCert.class, map);
    }

    @Override
    public GxhImportCert getCertByCondition(Map<String, String> map) {
        SQLManageUtil sql = new SQLManageUtil();
        return sql.getBeanByCondition(GxhImportCert.class, map);
    }

}
