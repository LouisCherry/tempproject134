package com.epoint.businesslicense.impl;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.businesslicense.api.entity.BusinessLicenseBaseinfo;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 一业一证基本信息Service
 *
 * @author shibin
 * @description
 * @date 2020年5月19日 下午2:52:35
 */
public class BusinessLicenseBaseinfoService {

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
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(BusinessLicenseBaseinfo record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(BusinessLicenseBaseinfo.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(BusinessLicenseBaseinfo record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public BusinessLicenseBaseinfo find(Object primaryKey) {
        return baseDao.find(BusinessLicenseBaseinfo.class, primaryKey);
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
    public BusinessLicenseBaseinfo find(String sql, Object... args) {
        return baseDao.find(sql, BusinessLicenseBaseinfo.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<BusinessLicenseBaseinfo> findList(String sql, Object... args) {
        return baseDao.findList(sql, BusinessLicenseBaseinfo.class, args);
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
    public List<BusinessLicenseBaseinfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, BusinessLicenseBaseinfo.class, args);
    }

    /**
     * @description
     * @author shibin
     * @date 2020年5月23日 上午1:33:47
     */
    public List<String> findCertRowguidByCertno(String certno, String certrowguid) {

        List<String> attachguidList = new ArrayList<String>();
        IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        String sql = "SELECT a.RowGuid from audit_sp_instance a INNER JOIN cert_info c ON a.certrowguid = c.RowGuid WHERE  CERTNO = ? AND c.STATUS ='10' AND ISHISTORY = '0' ";
        String biGuid = baseDao.queryString(sql, certno);

        //正常发证
        String sql2 = "SELECT SLTIMAGECLIENGGUID from audit_project p INNER JOIN cert_info c ON p.CERTROWGUID = c.RowGuid WHERE BIGUID = ? ";
        List<String> list = baseDao.findList(sql2, String.class, biGuid);

        //审批上传证照结果
        String sql3 = "SELECT rowguid from audit_project p WHERE BIGUID = ? ";
        List<String> listProjectguid = baseDao.findList(sql3, String.class, biGuid);
        if (list != null && list.size() > 0) {
            for (String clientGuid : list) {
                List<FrameAttachInfo> attachList = attachService.getAttachInfoListByGuid(clientGuid);
                for (FrameAttachInfo frameAttachInfo : attachList) {
                    attachguidList.add(frameAttachInfo.getAttachGuid());
                }
            }
        }
        if (listProjectguid != null && listProjectguid.size() > 0) {
            for (String clientGuid : listProjectguid) {
                List<FrameAttachInfo> attachList = attachService.getAttachInfoListByGuid(clientGuid);
                for (FrameAttachInfo frameAttachInfo : attachList) {
                    attachguidList.add(frameAttachInfo.getAttachGuid());
                }
            }
        }

        // 未走办件，直接发证
        if (attachguidList != null && attachguidList.size() > 0) {

        } else {
            String sql4 = "SELECT ATTACHGUID from businesslicense_result b INNER JOIN frame_attachinfo f ON b.clientguid = f.CLIENGGUID WHERE certrowguid = ? ";
            List<String> listAttach = baseDao.findList(sql4, String.class, certrowguid);
            for (String attachguid : listAttach) {
                attachguidList.add(attachguid);
            }
        }
        return attachguidList;
    }


    /**
     * @description
     * @author shibin
     * @date 2020年5月23日 上午1:33:47
     */
    public List<Record> findMaterialsAndCert(String certno, String certrowguid) {

        List<Record> attachReusltList = new ArrayList<Record>();
        IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        String sql = " select rowguid from audit_project where certrowguid = ? ";
        AuditProject project = baseDao.find(sql, AuditProject.class, certrowguid);

        if (project != null) {
            String sql2 = "select * from audit_project_material where projectguid = ? ";
            List<AuditProjectMaterial> materials = baseDao.findList(sql2, AuditProjectMaterial.class, project.getRowguid());
            if (materials != null && !materials.isEmpty()) {
                for (AuditProjectMaterial material : materials) {
                    List<FrameAttachInfo> attachList = attachService.getAttachInfoListByGuid(material.getCliengguid());
                    for (FrameAttachInfo frameAttachInfo : attachList) {
                        Record record = new Record();
                        record.set("attachguid", frameAttachInfo.getAttachGuid());
                        record.set("attachname", frameAttachInfo.getAttachFileName());
                        record.set("uploaddate", EpointDateUtil.convertDate2String(frameAttachInfo.getUploadDateTime(), EpointDateUtil.DATE_TIME_FORMAT));
                        record.set("size", frameAttachInfo.getAttachLength());
                        attachReusltList.add(record);
                    }
                }
            }
        }

        String sql4 = "select * from cert_info b WHERE b.rowguid = ? AND b. STATUS = '10' AND b.ISHISTORY = '0'";
        CertInfo certinfo = baseDao.find(sql4, CertInfo.class, certrowguid);
        if (certinfo != null) {
            String sql5 = "select * from frame_attachinfo where cliengguid = ? ";
            FrameAttachInfo attachinfo = baseDao.find(sql5, FrameAttachInfo.class, certinfo.getCertcliengguid());
            if (attachinfo != null) {
                Record record1 = new Record();
                record1.set("attachguid", attachinfo.getAttachGuid());
                record1.set("attachname", attachinfo.getAttachFileName());
                record1.set("uploaddate", EpointDateUtil.convertDate2String(attachinfo.getUploadDateTime(), EpointDateUtil.DATE_TIME_FORMAT));
                record1.set("size", attachinfo.getAttachLength());
                attachReusltList.add(record1);
            }
        }

        return attachReusltList;
    }


    public List<Record> findMaterialsAndCertSgxkz(String certcliengguid) {
        List<Record> attachReusltList = new ArrayList<Record>();
        String sql5 = "select * from frame_attachinfo where cliengguid = ? ";
        FrameAttachInfo attachinfo = baseDao.find(sql5, FrameAttachInfo.class, certcliengguid);
        if (attachinfo != null) {
            Record record1 = new Record();
            record1.set("attachguid", attachinfo.getAttachGuid());
            record1.set("attachname", attachinfo.getAttachFileName());
            record1.set("uploaddate", EpointDateUtil.convertDate2String(attachinfo.getUploadDateTime(), EpointDateUtil.DATE_TIME_FORMAT));
            record1.set("size", attachinfo.getAttachLength());
            attachReusltList.add(record1);
        }
        return attachReusltList;
    }


    /**
     * @description
     * @author shibin
     * @date 2020年5月23日 上午1:33:47
     */
    public List<CertInfo> getCertListByCertno(String certno) {
        String sql = "select * from cert_info where certname = '医疗机构放射性职业病危害建设项目预评价报告审核' and ishistory = 0 and certownerno = ?";
        return baseDao.findList(sql, CertInfo.class, certno);
    }

    /**
     * @description
     * @author shibin
     * @date 2020年5月23日 下午3:47:05
     */
    public List<String> getCertguidByCerno(String certno) {
        String sql = "SELECT a.RowGuid from audit_sp_instance a INNER JOIN cert_info c ON a.certrowguid = c.RowGuid WHERE ISHISTORY = '1' AND CERTNO = ? AND c.STATUS ='10' ";
        String biGuid = baseDao.queryString(sql, certno);

        String sql2 = "SELECT c.Rowguid from audit_project p INNER JOIN cert_info c ON p.CERTROWGUID = c.RowGuid WHERE BIGUID = ? ";
        return baseDao.findList(sql2, String.class, biGuid);
    }


    public CertInfo getCertInfoByNameOrBh(String certCatalogid, String certno, String cetownername) {
        String sql = "select * from cert_info where certCatalogid = ? and (certno = ? or certownername=?)";
        return baseDao.find(sql, CertInfo.class, certCatalogid, certno, cetownername);
    }

    public String findProjectByCertrowguid(String certrowguid) {
        String sql = "select rowguid from  audit_project where certrowguid =? limit 1";
        return baseDao.find(sql, String.class, certrowguid);
    }
}
