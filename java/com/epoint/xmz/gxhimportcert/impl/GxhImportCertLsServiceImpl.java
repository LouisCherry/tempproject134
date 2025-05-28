package com.epoint.xmz.gxhimportcert.impl;

import java.util.List;
import java.util.Map;

import com.epoint.core.dao.ICommonDao;
import org.springframework.stereotype.Component;
import org.xm.similarity.util.StringUtil;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.services.DBServcie;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.gxhimportcert.api.IGxhImportCertLsService;
import com.epoint.xmz.gxhimportcert.api.entity.GxhImportCertLs;

/**
 * 个性化证照导入表对应的后台service实现类
 * 
 * @author dyxin
 * @version [版本号, 2023-06-12 16:30:17]
 */
@Component
@Service
public class GxhImportCertLsServiceImpl implements IGxhImportCertLsService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(GxhImportCertLs record) {
        return new GxhImportCertLsService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new GxhImportCertLsService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(GxhImportCertLs record) {
        return new GxhImportCertLsService().update(record);
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
    public GxhImportCertLs find(Object primaryKey) {
        return new GxhImportCertLsService().find(primaryKey);
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
    public GxhImportCertLs find(String sql, Object... args) {
        return new GxhImportCertLsService().find(sql, args);
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
    public List<GxhImportCertLs> findList(String sql, Object... args) {
        return new GxhImportCertLsService().findList(sql, args);
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
    public List<GxhImportCertLs> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new GxhImportCertLsService().findList(sql, pageNumber, pageSize, args);
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
    public Integer countGxhImportCertLs(String sql, Object... args) {
        return new GxhImportCertLsService().countGxhImportCertLs(sql, args);
    }

    @Override
    public Integer getListCount(Map<String, String> map) {
        SQLManageUtil sql = new SQLManageUtil();
        return sql.getListCount(GxhImportCertLs.class, map);
    }

    @Override
    public PageData<GxhImportCertLs> findPageData(Map<String, String> map, int first, int pageSize, String sortField, String sortOrder) {
        SQLManageUtil sql = new SQLManageUtil();
        return sql.getDbListByPage(GxhImportCertLs.class, map, first, pageSize, sortField, sortOrder);
    }

    @Override
    public List<GxhImportCertLs> findListByCondition(Map<String, String> map) {
        SQLManageUtil sql = new SQLManageUtil();
        return sql.getListByCondition(GxhImportCertLs.class, map);
    }

    @Override
    public GxhImportCertLs getCertByCondition(Map<String, String> map) {
        SQLManageUtil sql = new SQLManageUtil();
        return sql.getBeanByCondition(GxhImportCertLs.class, map);
    }
    
    @Override
    public List<Record> getSyncCertList(String certType) {
        String sql = "select * from ls_cert_sync_type where 1=1 ";
        if (StringUtil.isNotBlank(certType)) {
            sql += " and cert_type like '%" + certType + "%' ";
        }

        ICommonDao iCommonDao = DBServcie.getInstance().getDao();
        List<Record> records = iCommonDao.findList(sql, Record.class, null);
        iCommonDao.close();
        return records;
    }
    
    @Override
    public List<Record> getLsPersonIdNumberList(int offset, int pageSize) {
        String sql = " SELECT username , idnumber , mobile FROM audit_online_register where SUBSTRING(idnumber, 1, 6) = '370832' order by rowguid ";
        ICommonDao iCommonDao = DBServcie.getInstance().getDao();
        List<Record> records = iCommonDao.findList(sql, offset, pageSize, Record.class, null);
        iCommonDao.close();
        return records;
    }
    
    @Override
    public List<Record> getLsCompanyIdNumberList(int offset, int pageSize) {

        String sql = " SELECT a.ORGANNAME,a.CREDITCODE,b.CONTACTMOBILE FROM audit_rs_company_baseinfo a join audit_rs_company_legal b on a.COMPANYID = b.COMPANYID where SUBSTRING(a.creditcode, 3, 6) = '370832' order by a.rowguid ";
        ICommonDao iCommonDao = DBServcie.getInstance().getDao();
        List<Record> records = iCommonDao.findList(sql, offset, pageSize, Record.class, null);
        iCommonDao.close();
        return records;
    }
    
    @Override
    public GxhImportCertLs getCertLsByTyshxydmAndCerttypecode(String tyshxydm, String certtypecode) {
        
        String sql = " select * from gxh_import_cert_ls where tyshxydm = ? and certtypecode = ? ";
        ICommonDao iCommonDao = DBServcie.getInstance().getDao();
        GxhImportCertLs gxhImportCertLs = iCommonDao.find(sql, GxhImportCertLs.class, tyshxydm, certtypecode);
        iCommonDao.close();
        return gxhImportCertLs;
    }

}
