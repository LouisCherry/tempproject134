package com.epoint.cert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.aspose.words.DataSet;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.basic.certcatalog.certcatalogou.domain.CertCatalogOu;
import com.epoint.cert.basic.certcatalog.certcatalogou.inter.ICertCatalogOu;
import com.epoint.cert.basic.certcatalog.certmetadata.domain.CertMetadata;
import com.epoint.cert.basic.certcatalog.certmetadata.inter.ICertMetaData;
import com.epoint.cert.basic.certinfo.CertInfoListAction;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.cert.basic.certownerinfo.domain.CertOwnerInfo;
import com.epoint.cert.basic.certownerinfo.inter.ICertOwnerInfo;
import com.epoint.cert.commonutils.CertConstant;
import com.epoint.cert.commonutils.FormatUtil;
import com.epoint.cert.commonutils.NoSQLSevice;
import com.epoint.cert.commonutils.SqlUtils;
import com.epoint.cert.commonutils.ValidateUtil;
import com.epoint.common.cert.authentication.AttachBizlogic;
import com.epoint.common.cert.authentication.CertInfoBizlogic;
import com.epoint.common.cert.authentication.CertLogBizlogic;
import com.epoint.common.cert.authentication.CertUserSession;
import com.epoint.common.cert.authentication.GenerateBizlogic;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.role.api.IRoleService;

@RestController("jnprodcertinfolistaction")
@Scope("request")
public class JNProdCertInfoListAction extends BaseController
{
    private static final long serialVersionUID = 1L;
    private CertInfo dataBean;
    private CertInfoBizlogic certInfoBizlogic = new CertInfoBizlogic();

    @Autowired
    private ICertCatalogOu iCertCatalogou;

    private DataGridModel<CertInfo> model;

    private String certcatalogid;

    private Logger log = LogUtil.getLog(CertInfoListAction.class);

    @Autowired
    private ICertInfo iCertInfo;

    @Autowired
    private ICertCatalog iCertCatalog;

    @Autowired
    private IAttachService iAttachService;

    @Autowired
    private IRoleService iRoleService;

    @Autowired
    private ICertMetaData iCertMetaData;

    @Autowired
    private IConfigService configService;

    @Autowired
    private ICertCatalog catalogService;

    @Autowired
    private ICertOwnerInfo ownerInfoService;

    private String ispid;

    private String ouguid;

    private CertLogBizlogic certLogBizlogic = new CertLogBizlogic();

    private AttachBizlogic attachBizlogic = new AttachBizlogic();

    private LazyTreeModal9 treeModel;

    private boolean isAreaAdministrator;

    private String isenablecertinfoaudit;

    private ExportModel exportModel;

    private NoSQLSevice noSQLSevice = new NoSQLSevice();

    /**
     * 是否中心管理员角色
     */
    private boolean isZxglyRole;

    public void pageLoad() {
        dataBean = new CertInfo();
        if (StringUtil.isNotBlank(userSession.getBaseOUGuid())) {
            ouguid = userSession.getBaseOUGuid();
        }
        else {
            ouguid = userSession.getOuGuid();
        }

        isAreaAdministrator = iRoleService.isExistUserRoleName(userSession.getUserGuid(), "区域管理员");
        isZxglyRole = iRoleService.isExistUserRoleName(userSession.getUserGuid(), "中心管理员");

        isenablecertinfoaudit = configService
                .getFrameConfigValue("IsEnableCertInfoAudit_" + CertUserSession.getInstance().getAreaCode());
        addCallbackParam("isenablecertinfoaudit", isenablecertinfoaudit);
        dataBean.setAuditstatus("10");
        addCallbackParam("ofd", ConfigUtil.getConfigValue("ofdserviceurl"));
        addCallbackParam("ofdpreviewurl", ConfigUtil.getConfigValue("ofdpreviewurl"));
    }

    public LazyTreeModal9 getTreeModel() {
        if (treeModel == null) {
            treeModel = certInfoBizlogic.getLeftCatalogTree(isAreaAdministrator,
                    CertUserSession.getInstance().getAreaCode(), ouguid);
            treeModel.setRootName("证照类型");
            treeModel.setRootGuid("f9root");
            treeModel.setRootCode("f9root");
            treeModel.setRootSelect(false);
        }
        return treeModel;
    }

    public void deleteSelect() {
        if (!canWrite()) {
            addCallbackParam("msg", "没有写权限！");
            return;
        }
        CertCatalog certcatalog = iCertCatalog.getLatestCatalogDetailByEnale(certcatalogid,
                CertConstant.CONSTANT_INT_ZERO);

        if (certcatalog == null) {
            addCallbackParam("msg", "该证照对应的证照类型不存在！");
            return;
        }
        if (CertConstant.CONSTANT_INT_ZERO.equals(certcatalog.getIsenable())) {
            addCallbackParam("msg", "当前证照对应的证照类型未启用，操作失败！");
            return;
        }
        List<String> selectList = getDataGridData().getSelectKeys();
        StringBuilder msg = new StringBuilder();
        if (ValidateUtil.isNotBlankCollection(selectList)) {
            for (String key : selectList) {
                CertInfo certinfo = iCertInfo.getCertInfoByRowguid(key);
                if ("1".equals(isenablecertinfoaudit)) {
                    SqlUtils sqlUtils = new SqlUtils();
                    sqlUtils.eq("ishistory", "-1");
                    sqlUtils.eq("status", "10");

                    sqlUtils.eq("auditstatus", "30");
                    sqlUtils.eq("certid", certinfo.getCertid());
                    if (iCertInfo.getCountByCondition(sqlUtils.getMap()) > 0) {
                        msg.append(certinfo.getCertno() + "、");
                        continue;
                    }
                    if (("20".equals(certinfo.getAuditstatus())) || ("40".equals(certinfo.getAuditstatus()))) {
                        iCertInfo.deleteCertInfoByRowguid(certinfo.getRowguid());
                    }
                    else {
                        certinfo.setIshistory(CertConstant.CONSTANT_INT_ONE);

                        if ("1".equals(certinfo.getLicdata_sync_sign())) {
                            certinfo.setLicdata_sync_sign("4");
                        }
                        else {
                            certinfo.setLicdata_sync_sign("3");
                        }
                        iCertInfo.updateCertInfo(certinfo);
                    }
                }
                else {
                    certinfo.setIshistory(CertConstant.CONSTANT_INT_ONE);

                    if ("1".equals(certinfo.getLicdata_sync_sign())) {
                        certinfo.setLicdata_sync_sign("4");
                    }
                    else {
                        certinfo.setLicdata_sync_sign("3");
                    }
                    iCertInfo.updateCertInfo(certinfo);
                }

                certLogBizlogic.addInternalLog(userSession.getUserGuid(), userSession.getDisplayName(), certinfo, "删除",
                        "", null);
            }
        }

        if (msg.length() > 0) {
            msg = new StringBuilder("编号为" + msg.substring(0, msg.length() - 1) + "的证照存在待审核数据，无法删除！");
        }
        else {
            msg.append("成功删除！");
        }
        addCallbackParam("msg", msg.toString());
    }

    public DataGridModel<CertInfo> getDataGridData() {
        if (model == null) {
            model = new DataGridModel<CertInfo>()
            {
                private static final long serialVersionUID = 9179330200184695904L;

                public List<CertInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String parentcertcatalogid = "";
                    List<CertInfo> certinfoList = new ArrayList<CertInfo>();
                    SqlUtils sqlUtils = new SqlUtils();
                    // 类型是证照的
                    sqlUtils.eq("materialtype", CertConstant.CONSTANT_STR_ONE);
                    // 状态正常
                    sqlUtils.eq("STATUS", CertConstant.CERT_STATUS_COMMON);
                    // 证照等级是a或者b
                    sqlUtils.in("certlevel", "'A','B'");
                    if (CertConstant.CERT_CHECK_STATUS_PASS.equals(dataBean.getAuditstatus())) {
                        // 不为历史记录
                        sqlUtils.eq("ISHISTORY", CertConstant.CONSTANT_STR_ZERO);
                    }
                    else {
                        sqlUtils.eq("ISHISTORY", CertConstant.CONSTANT_STR_NEGATIVE_ONE);
                        sqlUtils.eq("auditstatus", dataBean.getAuditstatus());
                    }

                    if (isZxglyRole) {

                    }
                    else {
                        String orgcode = CertUserSession.getInstance().getOrgcode();
                        // 默认使用orgcode过滤， 只能变更自己颁发的证照,兼容老Ouguid数据
                        sqlUtils.in("OUGUID",
                                "'" + ouguid + "'" + (StringUtil.isNotBlank(orgcode) ? ",'" + orgcode + "'" : ""));
                    }

                    // 版本唯一标识筛选条件
                    if (CertConstant.CONSTANT_STR_ZERO.equals(getIspid())) {
                        // 不是父目录
                        if (StringUtil.isNotBlank(certcatalogid)) {
                            CertCatalog catalog = iCertCatalog.getLatestCatalogDetailByCatalogid(certcatalogid);
                            if (catalog != null) {
                                sqlUtils.eq("CERTCATALOGID", certcatalogid);
                            }
                            else {
                                setRowCount(0);
                                return certinfoList;
                            }

                        }

                    }
                    else if (StringUtil.isNotBlank(certcatalogid)) {
                        // 是父目录，查出该父目录下所有的证照实例
                        parentcertcatalogid = certcatalogid;
                    }

                    if (StringUtil.isNotBlank(dataBean.getCertownername())) {
                        sqlUtils.like("CERTOWNERNAME",
                                FormatUtil.getdecryptSM4ToData(dataBean.getCertownername(), certcatalogid, 0));
                    }

                    if (StringUtil.isNotBlank(dataBean.getCertno())) {
                        sqlUtils.like("CERTNO", dataBean.getCertno());
                    }
                    // 辖区
                    sqlUtils.eq("AREACODE", CertUserSession.getInstance().getAreaCode());
                    // 额外查询条件，针对实例，where 语句后面的sql条件
                    String extraauthority = "";

                    if ((StringUtil.isNotBlank(certcatalogid)) || (StringUtil.isNotBlank(dataBean.getCertno()))
                            || (StringUtil.isNotBlank(dataBean.getCertownername()))) {
                        SqlUtils sqlutilextra = new SqlUtils();
                        sqlutilextra.eq("catalogid", certcatalogid);
                        sqlutilextra.eq("ouguid", ouguid);
                        List<CertCatalogOu> certcatalogous = iCertCatalogou.getCertCatalogOuListByCondition(false,
                                sqlutilextra.getMap());
                        if (ValidateUtil.isNotBlankCollection(certcatalogous)) {
                            CertCatalogOu certcatalogou = certcatalogous.get(0);
                            if (certcatalogou != null) {
                                extraauthority = certcatalogou.getExtraauthority();
                            }
                        }

                        sqlUtils.setOrderAsc("certno");
                        sqlUtils.setOrderDesc("operateDate");
                        PageData<CertInfo> pageData = iCertInfo.getListByPage(sqlUtils.getMap(), Integer.valueOf(first),
                                Integer.valueOf(pageSize), sortField, sortOrder, extraauthority, parentcertcatalogid);

                        certinfoList = pageData.getList();

                        if (ValidateUtil.isNotBlankCollection(certinfoList)) {
                            for (CertInfo certinfo : certinfoList) {
                                if (StringUtil.isNotBlank(certinfo.getCertownername())) {
                                    certinfo.setCertownername(FormatUtil
                                            .getdecryptSM4ToData(certinfo.getCertownername(), certcatalogid, 0));
                                }
                            }
                        }
                        setRowCount(pageData.getRowCount());
                    }
                    else {
                        setRowCount(0);
                    }

                    return certinfoList;
                }
            };
        }
        return model;
    }

    public void checkCatalogDraft(String guid) {
        CertInfo certinfo = iCertInfo.getCertInfoByRowguid(guid);
        SqlUtils sql = new SqlUtils();
        sql.eq("certcatalogid", certcatalogid);
        sql.eq("ishistory", "0");
        sql.eq("isenable", "0");
        if (iCertCatalog.getCertCatalogCount(sql.getMap()) > 0) {
            addCallbackParam("isNotEnable", "当前证照对应的证照类型未启用，操作失败！");
            return;
        }
        if ((certinfo != null) && (CertConstant.CONSTANT_INT_ZERO.equals(certinfo.getIshistory()))) {
            int draftCount = getDraftCount(certinfo);
            if (draftCount == 0) {
                addCallbackParam("msg", "0");
            }
            else {
                addCallbackParam("msg", "1");
            }
        }
    }

    public void certChangePrompt(String guid) {
        if (!canWrite()) {
            addCallbackParam("canWrite", Boolean.valueOf(false));
            return;
        }
        CertInfo certinfo = iCertInfo.getCertInfoByRowguid(guid);
        if ((certinfo != null) && (CertConstant.CONSTANT_INT_ZERO.equals(certinfo.getIshistory()))) {
            if ("1".equals(isenablecertinfoaudit)) {
                SqlUtils sqlUtils = new SqlUtils();
                sqlUtils.eq("ishistory", "-1");
                sqlUtils.eq("status", "10");

                sqlUtils.in("auditstatus", "'30', '40'");
                sqlUtils.eq("certid", certinfo.getCertid());
                if (iCertInfo.getCountByCondition(sqlUtils.getMap()) > 0) {
                    addCallbackParam("isaudit", "1");
                    return;
                }
            }

            int draftCount = getDraftCount(certinfo);
            if (draftCount == 0) {
                CertInfo draft = generateDraft(certinfo);
                if (draft != null) {
                    addCallbackParam("loaddraft", "0");
                    addCallbackParam("certinfoGuid", draft.getRowguid());
                }
            }
            else {
                List<CertInfo> draftList = getDraftList(certinfo);
                if (ValidateUtil.isNotBlankCollection(draftList)) {
                    addCallbackParam("loaddraft", "1");
                    addCallbackParam("displayname", ((CertInfo) draftList.get(0)).getOperateusername());
                    addCallbackParam("certinfoGuid", ((CertInfo) draftList.get(0)).getRowguid());
                    addCallbackParam("certname", certinfo.getCertname());
                }
            }
        }
        else if (certinfo != null) {
            CertInfo oldCertinfo = iCertInfo.getUseCertInfoByCertId(certinfo.getCertid());
            if (oldCertinfo != null) {
                addCallbackParam("oldcertguid", oldCertinfo.getRowguid());
            }
        }
    }

    public void abandonDraft(String guid) {
        CertInfo certinfo = iCertInfo.getCertInfoByRowguid(guid);
        if ((certinfo != null) && (CertConstant.CONSTANT_INT_ZERO.equals(certinfo.getIshistory()))) {
            List<CertInfo> draftList = getDraftList(certinfo);
            if (ValidateUtil.isNotBlankCollection(draftList)) {
                for (CertInfo draft : draftList) {
                    iCertInfo.deleteCertInfoByRowguid(draft.getRowguid());

                    Map<String, Object> filter = new HashMap<String, Object>();
                    filter.put("certinfoguid", draft.getRowguid());
                    CertInfoExtension certExtension = noSQLSevice.find(CertInfoExtension.class,
                            filter, false);
                    if (certExtension != null) {

                        List<CertMetadata> metadataList = iCertMetaData
                                .selectTopDispinListByCertguid(draft.getCertareaguid());
                        if (ValidateUtil.isNotBlankCollection(metadataList)) {
                            for (CertMetadata metadata : metadataList) {
                                if ("image".equals(StringUtil.toLowerCase(metadata.getFieldtype()))) {
                                    String cliengguid = certExtension.getStr(metadata.getFieldname());
                                    if (StringUtil.isNotBlank(cliengguid)) {
                                        iAttachService.deleteAttachByGuid(cliengguid);
                                    }
                                }
                            }
                        }

                        noSQLSevice.delete(certExtension);
                    }

                    if (StringUtil.isNotBlank(draft.getCertcliengguid())) {
                        iAttachService.deleteAttachByGuid(draft.getCertcliengguid());
                    }

                    if (StringUtil.isNotBlank(draft.getCopycertcliengguid())) {
                        iAttachService.deleteAttachByGuid(draft.getCopycertcliengguid());
                    }

                    if (StringUtil.isNotBlank(draft.getSltimagecliengguid())) {
                        iAttachService.deleteAttachByGuid(draft.getSltimagecliengguid());
                    }

                    if (StringUtil.isNotBlank(draft.getPrintdocguid())) {
                        iAttachService.deleteAttachByGuid(draft.getPrintdocguid());
                    }

                    if (StringUtil.isNotBlank(draft.getCopyprintdocguid())) {
                        iAttachService.deleteAttachByGuid(draft.getCopyprintdocguid());
                    }
                }
            }

            CertInfo draft = generateDraft(certinfo);
            if (draft != null) {
                addCallbackParam("certinfoGuid", draft.getRowguid());
            }
        }
    }

    private int getDraftCount(CertInfo certinfo) {
        int count = 0;
        SqlUtils sqlUtils = new SqlUtils();
        int version = certinfo.getVersion().intValue() + 1;
        String certid = certinfo.getCertid();

        sqlUtils.eq("ishistory", "-1");
        sqlUtils.eq("auditstatus", "20");
        sqlUtils.eq("version", String.valueOf(version));
        sqlUtils.eq("certid", certid);
        sqlUtils.eq("status", "10");
        count = iCertInfo.getCountByCondition(sqlUtils.getMap());
        return count;
    }

    private List<CertInfo> getDraftList(CertInfo certinfo) {
        SqlUtils sqlUtils = new SqlUtils();
        int version = certinfo.getVersion().intValue() + 1;
        String certid = certinfo.getCertid();

        sqlUtils.eq("ishistory", "-1");
        sqlUtils.eq("auditstatus", "20");
        sqlUtils.eq("version", String.valueOf(version));
        sqlUtils.eq("certid", certid);
        sqlUtils.eq("status", "10");
        return iCertInfo.getListByCondition(sqlUtils.getMap());
    }

    private CertInfo generateDraft(CertInfo certinfo) {
        CertInfo draft = (CertInfo) certinfo.clone();

        draft.setRowguid(UUID.randomUUID().toString());
        draft.setOperatedate(new Date());

        draft.setVersion(Integer.valueOf(certinfo.getVersion().intValue() + 1));

        draft.setIshistory(CertConstant.CONSTANT_INT_NEGATIVE_ONE);
        draft.setAuditstatus("20");
        draft.setOperateusername(userSession.getDisplayName());

        draft.setStatus("10");

        draft.setIspublish(CertConstant.CONSTANT_INT_ZERO);
        draft.setCertificateidentifier(null);
        draft.setLicdata_sync_sign(null);
        draft.setLicdata_sync_date(null);
        draft.setLicdata_sync_error_desc(null);

        int count = iAttachService.getAttachCountByClientGuid(certinfo.getCertcliengguid());
        draft.setCertcliengguid(UUID.randomUUID().toString());
        if (count > 0) {
            iAttachService.copyAttachByClientGuid(certinfo.getCertcliengguid(), null, null, draft.getCertcliengguid());
        }

        int copyCount = iAttachService.getAttachCountByClientGuid(certinfo.getCopycertcliengguid());
        draft.setCopycertcliengguid(UUID.randomUUID().toString());
        if (copyCount > 0) {
            iAttachService.copyAttachByClientGuid(certinfo.getCopycertcliengguid(), null, null,
                    draft.getCopycertcliengguid());
        }

        if (StringUtil.isNotBlank(certinfo.getSltimagecliengguid())) {
            int sltCount = iAttachService.getAttachCountByClientGuid(certinfo.getSltimagecliengguid());
            draft.setSltimagecliengguid(UUID.randomUUID().toString());
            if (sltCount > 0) {
                iAttachService.copyAttachByClientGuid(certinfo.getSltimagecliengguid(), null, null,
                        draft.getSltimagecliengguid());
            }
        }

        draft.setPrintdocguid(null);
        draft.setCopyprintdocguid(null);
        CertCatalog certCatalog = catalogService.getCatalogDetailByrowguid(draft.getCertareaguid());

        if ((CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsmultiowners()))
                && (StringUtil.isNotBlank(draft.getCertownerno())) && (!draft.getCertownerno().endsWith("^"))) {
            draft.setCertownerno(draft.getCertownerno() + "^");
        }

        iCertInfo.addCertInfo(draft);

        List<CertOwnerInfo> ownerInfos = ownerInfoService.selectOwnerInfoByCertInfoGuid(certinfo.getRowguid());
        for (CertOwnerInfo ownerInfo : ownerInfos) {
            ownerInfo.setRowguid(UUID.randomUUID().toString());
            ownerInfo.setCertinfoguid(draft.getRowguid());
            ownerInfoService.addCertOwnerInfo(ownerInfo);
        }

        try {
            Map<String, Object> filter = new HashMap<>();
            filter.put("certinfoguid", certinfo.getRowguid());
            CertInfoExtension certExtension = noSQLSevice.find(CertInfoExtension.class, filter, false);
            if (certExtension != null) {
                certExtension.setCertinfoguid(draft.getRowguid());
                certExtension.setRowguid(UUID.randomUUID().toString());

                certExtension.setOperatedate(new Date());
                certExtension.setOperateusername(userSession.getDisplayName());

                List<CertMetadata> metadataList = iCertMetaData.selectTopDispinListByCertguid(draft.getCertareaguid());
                if (ValidateUtil.isNotBlankCollection(metadataList)) {
                    for (CertMetadata metadata : metadataList) {
                        if ("image".equals(StringUtil.toLowerCase(metadata.getFieldtype()))) {
                            String cliengguid = certExtension.getStr(metadata.getFieldname());
                            if (StringUtil.isNotBlank(cliengguid)) {
                                certExtension.set(metadata.getFieldname(), UUID.randomUUID().toString());
                                attachBizlogic.copyAttach(cliengguid, certExtension.getStr(metadata.getFieldname()));
                            }
                        }
                    }
                }
                noSQLSevice.insert(certExtension);
            }
        }
        catch (Exception e) {
            log.info(e);
            log.error(String.format("MongoDb复制照面信息 {certinfoguid = %s} 失败!", new Object[] {draft.getRowguid() }));
            throw new RuntimeException();
        }
        return draft;
    }

    public void getOFDGuid(String rowGuid) {
        CertInfo certInfo = iCertInfo.getCertInfoByRowguid(rowGuid);
        boolean flag = false;
        List<FrameAttachInfo> frameAttachInfos = iAttachService.getAttachInfoListByGuid(certInfo.getCertcliengguid());
        for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
            if ((StringUtil.toLowerCase(frameAttachInfo.getAttachFileName()).endsWith("ofd"))
                    && ("系统生成".equals(frameAttachInfo.getCliengInfo()))) {
                addCallbackParam("attachguid", frameAttachInfo.getAttachGuid());
                flag = true;
                break;
            }
        }

        List<FrameAttachInfo> copyFrameAttachInfos = iAttachService
                .getAttachInfoListByGuid(certInfo.getCopycertcliengguid());
        for (FrameAttachInfo frameAttachInfo : copyFrameAttachInfos) {
            if ((StringUtil.toLowerCase(frameAttachInfo.getAttachFileName()).endsWith("ofd"))
                    && ("系统生成（副本）".equals(frameAttachInfo.getCliengInfo()))) {
                addCallbackParam("copyattachguid", frameAttachInfo.getAttachGuid());
                return;
            }
        }
        if (!flag) {
            addCallbackParam("msg", "请先生成ofd文件！");
        }
    }

    public boolean canWrite() {
        boolean canWrite = false;
        SqlUtils sql = new SqlUtils();
        sql.eq("catalogid", certcatalogid);
        sql.eq("ouguid", ouguid);
        List<CertCatalogOu> certcatalogous = iCertCatalogou.getCertCatalogOuListByCondition(false, sql.getMap());
        if (ValidateUtil.isNotBlankCollection(certcatalogous)) {
            CertCatalogOu certcatalogou = certcatalogous.get(0);
            if ((certcatalogou != null) && (certcatalogou.getWriteauthority() != null)) {
                canWrite = certcatalogou.getWriteauthority().equals(Integer.valueOf(1));
            }
        }
        return canWrite;
    }

    public void canOcr(String certname) {
        String[] certnames = configService.getFrameConfigValue("AS_OCR_CERTCATALOG").split(";");
        if (!Arrays.asList(certnames).contains(certname)) {
            addCallbackParam("isocr", Integer.valueOf(0));

        }
        else if (certname.contains("身份证")) {
            addCallbackParam("ocrcategory", Integer.valueOf(0));
        }
        else if (certname.contains("营业执照")) {
            addCallbackParam("ocrcategory", Integer.valueOf(1));
        }
        else {
            addCallbackParam("ocrcategory", Integer.valueOf(2));
        }
    }

    public void checkCert(String rowGuid) {
        CertInfo certInfo = iCertInfo.getCertInfoByRowguid(rowGuid);
        CertCatalog certCatalog = catalogService.getCatalogDetailByrowguid(certInfo.getCertareaguid());
        if (certCatalog != null) {
            if ((StringUtil.isNotBlank(certCatalog.getTempletcliengguid()))
                    && (iAttachService.getAttachCountByClientGuid(certCatalog.getTempletcliengguid()) > 0)) {
                if ((StringUtil.isNotBlank(certCatalog.getCopytempletcliengguid()))
                        && (iAttachService.getAttachCountByClientGuid(certCatalog.getCopytempletcliengguid()) > 0)) {
                    addCallbackParam("hascopy", "1");
                }
            }
            else {
                addCallbackParam("msg", "请配置证照模版！");
            }
        }
        else {
            addCallbackParam("msg", "对应的证照目录不存在！");
        }
    }

    public void printCert(String rowGuid, String isPrintCopy) {
        CertInfo certinfo = iCertInfo.getCertInfoByRowguid(rowGuid);
        CertCatalog certCatalog = catalogService.getCatalogDetailByrowguid(certinfo.getCertareaguid());

        if ((certinfo == null) || (certinfo.isEmpty())) {
            addCallbackParam("msg", "该证照不存在！");
            return;
        }
        int copyTempletCount = iAttachService.getAttachCountByClientGuid(certCatalog.getCopytempletcliengguid());

        if (copyTempletCount == 0) {

            if (StringUtil.isBlank(certinfo.getCertcliengguid())) {
                addCallbackParam("msg", "请先生成证照");
                return;
            }
            List<FrameAttachInfo> attachInfoList = iAttachService.getAttachInfoListByGuid(certinfo.getCertcliengguid());
            if (ValidateUtil.isBlankCollection(attachInfoList)) {
                addCallbackParam("msg", "请先生成证照");
                return;
            }

            boolean isGenerate = false;
            for (FrameAttachInfo attchInfo : attachInfoList) {
                if ("系统生成".equals(attchInfo.getCliengInfo())) {
                    isGenerate = true;
                    break;
                }
            }
            if (!isGenerate) {
                addCallbackParam("msg", "请先生成证照");
                return;
            }
        }
        else {
            String cliengGuid = certinfo.getCertcliengguid();
            String cliengInfo = "系统生成";
            String tip = "正本";
            if ("1".equals(isPrintCopy)) {
                cliengGuid = certinfo.getCopycertcliengguid();
                tip = "副本";
                cliengInfo = "系统生成（副本）";
            }
            if (StringUtil.isBlank(cliengGuid)) {
                addCallbackParam("msg", String.format("请先生成证照（%s）", new Object[] {tip }));
                return;
            }
            List<FrameAttachInfo> attachInfoList = iAttachService.getAttachInfoListByGuid(cliengGuid);
            if (ValidateUtil.isBlankCollection(attachInfoList)) {
                addCallbackParam("msg", String.format("请先生成证照（%s）", new Object[] {tip }));
                return;
            }

            boolean isGenerate = false;
            for (FrameAttachInfo attchInfo : attachInfoList) {
                if (cliengInfo.equals(attchInfo.getCliengInfo())) {
                    isGenerate = true;
                    break;
                }
            }
            if (!isGenerate) {
                addCallbackParam("msg", String.format("请先生成证照（%s）", new Object[] {tip }));
                return;
            }
        }

        Map<String, String> returnMap = null;
        if ("1".equals(isPrintCopy)) {

            if (StringUtil.isNotBlank(certinfo.getCopyprintdocguid())) {
                addCallbackParam("certinfoguid", certinfo.getRowguid());
                addCallbackParam("attachguid", certinfo.getCopyprintdocguid());
                FrameAttachInfo info = iAttachService.getAttachInfoDetail(certinfo.getCopyprintdocguid());
                if ((info != null) && (StringUtil.isNotBlank(info.getContentType()))) {
                    addCallbackParam("contenttype", info.getContentType());
                }
                addCallbackParam("filename", certinfo.getCertname() + "(副本)");
                return;
            }

            String cliengGuid = certCatalog.getCopytempletcliengguid();

            if (StringUtil.isNotBlank(certCatalog.getTdCopytempletcliengguid())) {

                int TdCopyTempletCount = iAttachService
                        .getAttachCountByClientGuid(certCatalog.getTdCopytempletcliengguid());
                if (TdCopyTempletCount > 0) {
                    cliengGuid = certCatalog.getTdCopytempletcliengguid();
                }
            }
            returnMap = generateCertDoc(cliengGuid, true, certinfo);

        }
        else {

            if (StringUtil.isNotBlank(certinfo.getPrintdocguid())) {
                addCallbackParam("certinfoguid", certinfo.getRowguid());
                addCallbackParam("attachguid", certinfo.getPrintdocguid());
                FrameAttachInfo info = iAttachService.getAttachInfoDetail(certinfo.getPrintdocguid());
                if ((info != null) && (StringUtil.isNotBlank(info.getContentType()))) {
                    addCallbackParam("contenttype", info.getContentType());
                }
                addCallbackParam("filename", certinfo.getCertname());
                return;
            }

            String cliengGuid = certCatalog.getTempletcliengguid();

            if (StringUtil.isNotBlank(certCatalog.getTdTempletcliengguid())) {
                int TdTempletCount = iAttachService.getAttachCountByClientGuid(certCatalog.getTdTempletcliengguid());
                if (TdTempletCount > 0) {
                    cliengGuid = certCatalog.getTdTempletcliengguid();
                }
            }
            returnMap = generateCertDoc(cliengGuid, false, certinfo);
        }

        if (ValidateUtil.isNotBlankMap(returnMap)) {
            String isSuccess = returnMap.get("issuccess");
            if ("1".equals(isSuccess)) {
                addCallbackParam("certinfoguid", certinfo.getRowguid());
                addCallbackParam("attachguid", returnMap.get("attachguid"));
                addCallbackParam("contenttype", returnMap.get("contenttype"));
                addCallbackParam("filename", certinfo.getCertname());
            }
            else {
                addCallbackParam("msg", returnMap.get("msg"));
            }
        }
    }

    public Map<String, String> generateCertDoc(String cliengGuid, boolean isPrintCopy, CertInfo certInfo) {
        Map<String, String> returnMap = null;
        List<CertMetadata> metadataList = iCertMetaData.selectTopDispinListByCertguid(certInfo.getCertareaguid());

        Map<String, Object> filter = new HashMap<String, Object>();

        filter.put("certinfoguid", certInfo.getRowguid());
        CertInfoExtension dataBean = noSQLSevice.find(CertInfoExtension.class, filter, false);
        if (dataBean == null) {
            dataBean = new CertInfoExtension();
            dataBean.setRowguid(UUID.randomUUID().toString());
            dataBean.setCertinfoguid(certInfo.getRowguid());
        }

        certInfoBizlogic.convertDate(metadataList, dataBean);
        GenerateBizlogic generateBizlogic = new GenerateBizlogic();
        try {
            EpointFrameDsManager.begin(null);
            Map<String, Object> wordMap = generateBizlogic.getWordInfo(metadataList, dataBean);

            String[] fieldNames = (String[]) wordMap.get("fieldNames");

            Object[] values = (Object[]) wordMap.get("values");

            Map<String, Record> imageMap = (Map<String, Record>) wordMap.get("imageMap");

            DataSet dataSet = (DataSet) wordMap.get("dataSet");
            returnMap = generateBizlogic.generatePrintDocSupportCopy(cliengGuid, Boolean.valueOf(isPrintCopy),
                    userSession.getUserGuid(), userSession.getDisplayName(), fieldNames, values, imageMap, dataSet,
                    certInfo);
        }
        catch (Exception e) {
            log.info(e);
        }
        finally {
            EpointFrameDsManager.commit();
            EpointFrameDsManager.close();
        }
        return returnMap;
    }

    public void checkEnable() {
        CertCatalog certcatalog = iCertCatalog.getLatestCatalogDetailByEnale(certcatalogid,
                CertConstant.CONSTANT_INT_ZERO);

        if (certcatalog == null) {
            addCallbackParam("disenable", "该证照对应的证照类型不存在！");
        }
        else if (CertConstant.CONSTANT_INT_ZERO.equals(certcatalog.getIsenable())) {
            addCallbackParam("disenable", "当前证照对应的证照类型未启用，操作失败！");
        }
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    public CertInfo getDataBean() {
        if (dataBean == null) {
            dataBean = new CertInfo();
        }
        return dataBean;
    }

    public void setDataBean(CertInfo dataBean) {
        this.dataBean = dataBean;
    }

    public String getIspid() {
        return ispid;
    }

    public void setIspid(String ispid) {
        this.ispid = ispid;
    }

    public String getCertcatalogid() {
        return certcatalogid;
    }

    public void setCertcatalogid(String certcatalogid) {
        this.certcatalogid = certcatalogid;
    }
}

