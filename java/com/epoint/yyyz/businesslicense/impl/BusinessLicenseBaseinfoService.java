package com.epoint.yyyz.businesslicense.impl;

import java.util.ArrayList;
import java.util.List;

import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.yyyz.businesslicense.api.entity.BusinessLicenseBaseinfo;
import com.epoint.yyyz.businesslicense.api.entity.BusinessLicenseExtension;

/**
 * 一业一证基本信息Service
 * @description
 * @author shibin
 * @date  2020年5月19日 下午2:52:35
 */
public class BusinessLicenseBaseinfoService
{

    private static String downloadUrl = ConfigUtil.getConfigValue("businesslicense", "downloadUrl");

    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public BusinessLicenseBaseinfoService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(BusinessLicenseBaseinfo record) {
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
        T t = baseDao.find(BusinessLicenseBaseinfo.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(BusinessLicenseBaseinfo record) {
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
    public BusinessLicenseBaseinfo find(Object primaryKey) {
        return baseDao.find(BusinessLicenseBaseinfo.class, primaryKey);
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
    public BusinessLicenseBaseinfo find(String sql, Object... args) {
        return baseDao.find(sql, BusinessLicenseBaseinfo.class, args);
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
    public List<BusinessLicenseBaseinfo> findList(String sql, Object... args) {
        return baseDao.findList(sql, BusinessLicenseBaseinfo.class, args);
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
    public List<BusinessLicenseBaseinfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, BusinessLicenseBaseinfo.class, args);
    }

    /**
     * @description
     * @author shibin
     * @date  2020年5月23日 下午2:33:11
     */
    public Record getBusinessBaseinfoByBiguid(String biGuid) {
        String sql = "select * from businesslicense_baseinfo b INNER JOIN businesslicense_extension e on b.RowGuid = e.baseinfoGuid WHERE b.biGuid = ? ";
        return baseDao.find(sql, Record.class, biGuid);
    }

    /**
     * @description
     * @author shibin
     * @date  2020年5月23日 下午2:33:11
     */
    public List<Record> getTaskidsByBusinessguid(String businessguid) {
        String sql = "SELECT task_id,taskname,yyyzbusinessname FROM audit_task WHERE task_id IN (SELECT DISTINCT taskid FROM audit_sp_task WHERE businessguid = ?)";
        sql += "AND IS_ENABLE = 1 AND (IS_HISTORY = 0 OR IS_HISTORY = '' OR IS_HISTORY IS NULL) AND IS_EDITAFTERIMPORT = 1";
        return baseDao.findList(sql, Record.class, businessguid);
    }

    /**
     * @description
     * @author shibin
     * @date  2020年5月23日 下午2:33:11
     */
    public BusinessLicenseExtension getBusinessBaseinfoByBiguidAndBuesiness(String biGuid, String businessguid) {
        String sql = "select b.* from businesslicense_baseinfo a join businesslicense_extension b on a.rowguid = b.baseinfoGuid  where a.biguid = ?1 and a.businessguid = ?2";
        return baseDao.find(sql, BusinessLicenseExtension.class, biGuid, businessguid);
    }

    /**
     * 根据buguid获取申报信息
     * @description
     * @author shibin
     * @date  2020年6月22日 下午5:39:18
     */
    public BusinessLicenseBaseinfo getBaseinfoByBiguid(String biguid) {
        String sql = "SELECT * from businesslicense_baseinfo WHERE biGuid = ?1 ";
        return baseDao.find(sql, BusinessLicenseBaseinfo.class, biguid);
    }

    /**
     * 根据certguid获取申报信息
     * @description
     * @author shibin
     * @date  2020年6月22日 下午6:23:24
     */
    public BusinessLicenseBaseinfo getBaseinfoByCertguid(String guid) {
        String sql = "SELECT b.* from audit_sp_instance a INNER JOIN businesslicense_baseinfo b ON a.RowGuid = b.biGuid WHERE certrowguid = ?1 ";
        return baseDao.find(sql, BusinessLicenseBaseinfo.class, guid);
    }
    
    /**
     * 根据yyyzcliengguid和yyyztype查询附件
     * @description
     * @author shibin
     * @date  2020年6月22日 下午6:22:39
     */
    public FrameAttachInfo getAttachByYyyzCliengguid(String yyyzcliengguid, String yyyztype) {
        String sql = "select * from frame_attachinfo where yyyzguid = ?1 and yyyztype = ?2 limit 1";
        return baseDao.find(sql, FrameAttachInfo.class, yyyzcliengguid, yyyztype);
    }

    /**
     * 根据ouguid和
     *  [一句话功能简述] 
     *  @param ouguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<CertCatalog> findCertCatalogByOuguid(String ouguid) {
        String sql = "select Certcatalogid,Certname,Parentid from cert_catalog where ishistory = 0 and materialtype='1' and isenable = 1 and isparent=1 and CERTCATALOGID IN(select DISTINCT CATALOGID from cert_catalog_ou WHERE OUGUID =?) and Certname = '行业综合许可证' order by tycertcatcode asc, ordernum desc ";
        return baseDao.findList(sql, CertCatalog.class, ouguid);
    }
    
    /**
     * 根据ouguid和
     *  [一句话功能简述] 
     *  @param ouguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<CertCatalog> findCertCatalogZzlbByOuguid(String ouguid) {
        String sql = "select Certcatalogid,Certname,Parentid from cert_catalog where ishistory = 0 and materialtype='1' and isenable = 1 and isparent=1 and CERTCATALOGID IN(select DISTINCT CATALOGID from cert_catalog_ou WHERE OUGUID =?) and Certname = '证照联办综合许可证' order by tycertcatcode asc, ordernum desc ";
        return baseDao.findList(sql, CertCatalog.class, ouguid);
    }

}
