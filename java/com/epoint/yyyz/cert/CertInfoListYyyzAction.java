package com.epoint.yyyz.cert;

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
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.cert.basic.certownerinfo.domain.CertOwnerInfo;
import com.epoint.cert.basic.certownerinfo.inter.ICertOwnerInfo;
import com.epoint.cert.commonutils.*;
import com.epoint.common.cert.authentication.*;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.security.crypto.sm.sm4.SM4Util;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.role.api.IRoleService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 证照基本信息表list页面对应的后台
 *
 * @作者 dingwei
 * @version [版本号, 2017年11月1日]
 */
@RestController("certinfolistyyyzaction")
@Scope("request")
public class CertInfoListYyyzAction extends BaseController
{
    private static final long serialVersionUID = 1L;

    /**
     * 证照基本信息表实体对象
     */
    private CertInfo dataBean;

    /**
     * 证照实例bizlogic
     */
    private CertInfoBizlogic certInfoBizlogic = new CertInfoBizlogic();

    /**
     * 证照目录和部门关系api
     */
    @Autowired
    private ICertCatalogOu iCertCatalogou;

    /**
     * 表格控件model
     */
    private DataGridModel<CertInfo> model;

    /**
     * 证照目录的版本唯一标识
     */
    private String certcatalogid;


    /**
     * 日志
     */
    private Logger log = LogUtil.getLog(CertInfoListYyyzAction.class);

    /**
     * 基本信息api
     */
    @Autowired
    private ICertInfo iCertInfo;
    /**
     * 证照目录api
     */
    @Autowired
    private ICertCatalog iCertCatalog;

    /**
     * 框架附件api
     */
    @Autowired
    private IAttachService iAttachService;

    /**
     * 角色service
     */
    @Autowired
    private IRoleService iRoleService;

    /**
     * 元数据api
     */
    @Autowired
    private ICertMetaData iCertMetaData;

    @Autowired
    private IConfigService configService;

    @Autowired
    private ICertCatalog catalogService;

    @Autowired
    private ICertOwnerInfo ownerInfoService;
    /**
     * 是否是父目录 0 不是，1是
     */
    private String ispid;

    /**
     * 当前登录人的ouguid
     */
    private String ouguid;

    /**
     * 操作日志bizlogic
     */
    private CertLogBizlogic certLogBizlogic = new CertLogBizlogic();

    /**
     * 附件bizlogic
     */
    private AttachBizlogic attachBizlogic = new AttachBizlogic();

    /**
     * 左侧树model
     */
    private LazyTreeModal9 treeModel;

    /**
     * 标记登录人的角色
     */
    private boolean isAreaAdministrator;

    /**
     * 是否需要审核
     */
    private String isenablecertinfoaudit;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * MongoDB\HBase通用service
     */
    private NoSQLSevice noSQLSevice = new NoSQLSevice();

    @Override
    public void pageLoad() {
        dataBean = new CertInfo();
        if (StringUtil.isNotBlank(userSession.getBaseOUGuid())) {
            ouguid = userSession.getBaseOUGuid();
        }
        else {
            ouguid = userSession.getOuGuid();
        }
        // 当前角色是否为区域管理员
        isAreaAdministrator = iRoleService.isExistUserRoleName(userSession.getUserGuid(), "区域管理员");
        isenablecertinfoaudit = configService
                .getFrameConfigValue("IsEnableCertInfoAudit_" + CertUserSession.getInstance().getAreaCode());
        addCallbackParam("isenablecertinfoaudit", isenablecertinfoaudit);
        dataBean.setAuditstatus(CertConstant.CERT_CHECK_STATUS_PASS);
        addCallbackParam("ofd", ConfigUtil.getConfigValue("ofdserviceurl"));
        addCallbackParam("ofdpreviewurl", ConfigUtil.getConfigValue("ofdpreviewurl"));
    }

    /**
     * 左树实现
     *
     * @return treeModel
     */
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

    /**
     * 删除选定(逻辑删除)
     */
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
        else if (CertConstant.CONSTANT_INT_ZERO.equals(certcatalog.getIsenable())) {
            addCallbackParam("msg", "当前证照对应的证照类型未启用，操作失败！");
            return;
        }
        List<String> selectList = getDataGridData().getSelectKeys();
        StringBuilder msg = new StringBuilder();
        if (ValidateUtil.isNotBlankCollection(selectList)) {
            for (String key : selectList) {
                CertInfo certinfo = iCertInfo.getCertInfoByRowguid(key);
                if (CertConstant.CONSTANT_STR_ONE.equals(isenablecertinfoaudit)) {
                    SqlUtils sqlUtils = new SqlUtils();
                    sqlUtils.eq("ishistory", CertConstant.CONSTANT_STR_NEGATIVE_ONE);
                    sqlUtils.eq("status", CertConstant.CERT_STATUS_COMMON);
                    // 是否有待审核、审核不通过数据
                    sqlUtils.eq("auditstatus", "30");
                    sqlUtils.eq("certid", certinfo.getCertid());
                    if (iCertInfo.getCountByCondition(sqlUtils.getMap()) > 0) {
                        msg.append(certinfo.getCertno() + "、");
                        continue;
                    }
                    else if (CertConstant.CERT_CHECK_STATUS_WAIT_REPORT.equals(certinfo.getAuditstatus())
                            || CertConstant.CERT_CHECK_STATUS_NOT_PASS.equals(certinfo.getAuditstatus())) {
                        iCertInfo.deleteCertInfoByRowguid(certinfo.getRowguid());
                    }
                    else {
                        // 移为历史数据
                        certinfo.setIshistory(CertConstant.CONSTANT_INT_ONE);
                        //删除标识，便于同步的时候查找
                        //licdata_sync_sign=3表示已删除，且删除的数据原来没同步过。licdata_sync_sign=4表示已删除，且删除的数据原来同步成功了。
                        if (CertConstant.CONSTANT_STR_ONE.equals(certinfo.getLicdata_sync_sign())) {
                            certinfo.setLicdata_sync_sign("4");
                        }
                        else {
                            certinfo.setLicdata_sync_sign("3");
                        }
                        iCertInfo.updateCertInfo(certinfo);
                    }
                }
                else {
                    // 移为历史数据
                    certinfo.setIshistory(CertConstant.CONSTANT_INT_ONE);
                    //删除标识，便于同步的时候查找
                    //licdata_sync_sign=3表示已删除，且删除的数据原来没同步过。licdata_sync_sign=4表示已删除，且删除的数据原来同步成功了。
                    if (CertConstant.CONSTANT_STR_ONE.equals(certinfo.getLicdata_sync_sign())) {
                        certinfo.setLicdata_sync_sign("4");
                    }
                    else {
                        certinfo.setLicdata_sync_sign("3");
                    }
                    iCertInfo.updateCertInfo(certinfo);
                }
                // 增加操作日志
                certLogBizlogic.addInternalLog(userSession.getUserGuid(), userSession.getDisplayName(), certinfo,
                        CertConstant.LOG_OPERATETYPE_DELETE, "", null);
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

    /**
     * 获得证照基本信息列表
     *
     * @return
     */
    public DataGridModel<CertInfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<CertInfo>()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 9179330200184695904L;

                @Override
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
                    String orgcode = CertUserSession.getInstance().getOrgcode();
                    // 默认使用orgcode过滤， 只能变更自己颁发的证照,兼容老Ouguid数据
                    sqlUtils.in("OUGUID", "'" + ouguid + "'" + (StringUtil.isNotBlank(orgcode)?(",'" + orgcode + "'"):""));

                    // 版本唯一标识筛选条件
                    if (CertConstant.CONSTANT_STR_ZERO.equals(getIspid())) {
                        // 不是父目录
                        if (StringUtil.isNotBlank(certcatalogid)) {
                            CertCatalog catalog = iCertCatalog.getLatestCatalogDetailByCatalogid(certcatalogid);
                            if (catalog != null) {
                                sqlUtils.eq("CERTCATALOGID", certcatalogid);
                            }
                            else {
                                this.setRowCount(0);
                                return certinfoList;
                            }
                        }
                    }
                    else {
                        // 是父目录，查出该父目录下所有的证照实例
                        if (StringUtil.isNotBlank(certcatalogid)) {
                            parentcertcatalogid = certcatalogid;
                        }
                    }
                    // 持有人姓名
                    if (StringUtil.isNotBlank(dataBean.getCertownername())) {
                        sqlUtils.like("CERTOWNERNAME", FormatUtil.getdecryptSM4ToData(dataBean.getCertownername() , certcatalogid , 0));
                    }
                    // 证照编号
                    if (StringUtil.isNotBlank(dataBean.getCertno())) {
                        sqlUtils.like("CERTNO", dataBean.getCertno());
                    }
                    //辖区
                    sqlUtils.eq("AREACODE", CertUserSession.getInstance().getAreaCode());
                    // 额外查询条件，针对实例，where 语句后面的sql条件
                    String extraauthority = "";
                    // 当目录版本id不为空或者搜索条件不为空时候展示实例数据
                    if (StringUtil.isNotBlank(certcatalogid) || StringUtil.isNotBlank(dataBean.getCertno())
                            || StringUtil.isNotBlank(dataBean.getCertownername())) {
                        // 额外的权限控制
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
                        // 默认排序为证照编号升序，操作时间降序
                        sqlUtils.setOrderAsc("certno");
                        sqlUtils.setOrderDesc("operateDate");
                        PageData<CertInfo> pageData = iCertInfo.getListByPage(sqlUtils.getMap(), first, pageSize,
                                sortField, sortOrder, extraauthority, parentcertcatalogid);
                        certinfoList = pageData.getList();
                        //将多持有者中^符号更换为,显示,无需逻辑判断
                        if (ValidateUtil.isNotBlankCollection(certinfoList)) {
                            for (CertInfo certinfo : certinfoList) {
                                certinfo.setCertownername(FormatUtil.getdecryptSM4ToData(certinfo.getCertownername() , certcatalogid , 0));
                            }
                        }
                        this.setRowCount(pageData.getRowCount());
                    }
                    else {
                        this.setRowCount(0);
                    }

                    return certinfoList;
                }
            };
        }
        return model;
    }

    /**
     * 证照变更提示
     *
     * @param guid
     *            证照基本信息guid
     */
    public void checkCatalogDraft(String guid) {
        CertInfo certinfo = iCertInfo.getCertInfoByRowguid(guid);
        SqlUtils sql = new SqlUtils();
        sql.eq("certcatalogid", certcatalogid);
        sql.eq("ishistory", CertConstant.CONSTANT_STR_ZERO);
        sql.eq("isenable", CertConstant.CONSTANT_STR_ZERO);
        if (iCertCatalog.getCertCatalogCount(sql.getMap()) > 0) {
            addCallbackParam("isNotEnable", "当前证照对应的证照类型未启用，操作失败！");
            return;
        }
        if (certinfo != null && CertConstant.CONSTANT_INT_ZERO.equals(certinfo.getIshistory())) {
            // 是否有草稿
            int draftCount = getDraftCount(certinfo);
            if (draftCount == 0) { // 生成新草稿
                addCallbackParam("msg", "0");
            }
            else {
                addCallbackParam("msg", "1");
            }
        }
    }

    /**
     * 证照变更提示
     *
     * @param guid
     *            证照基本信息guid
     */
    public void certChangePrompt(String guid) {
        if (!canWrite()) {
            addCallbackParam("canWrite", false);
            return;
        }
        CertInfo certinfo = iCertInfo.getCertInfoByRowguid(guid);
        if (certinfo != null && CertConstant.CONSTANT_INT_ZERO.equals(certinfo.getIshistory())) {
            // 是否有审核数据
            if (CertConstant.CONSTANT_STR_ONE.equals(isenablecertinfoaudit)) {
                SqlUtils sqlUtils = new SqlUtils();
                sqlUtils.eq("ishistory", CertConstant.CONSTANT_STR_NEGATIVE_ONE);
                sqlUtils.eq("status", CertConstant.CERT_STATUS_COMMON);
                // 是否有待审核、审核不通过数据
                sqlUtils.in("auditstatus", "'30', '40'");
                sqlUtils.eq("certid", certinfo.getCertid());
                if (iCertInfo.getCountByCondition(sqlUtils.getMap()) > 0) {
                    addCallbackParam("isaudit", "1");
                    return;
                }
            }
            // 是否有草稿
            int draftCount = getDraftCount(certinfo);
            if (draftCount == 0) { // 生成新草稿
                CertInfo draft = generateDraft(certinfo);
                if (draft != null) {
                    addCallbackParam("loaddraft", "0");
                    addCallbackParam("certinfoGuid", draft.getRowguid());
                }
            }
            else { // 加载草稿
                List<CertInfo> draftList = getDraftList(certinfo);
                if (ValidateUtil.isNotBlankCollection(draftList)) {
                    addCallbackParam("loaddraft", "1");
                    addCallbackParam("displayname", draftList.get(0).getOperateusername());
                    addCallbackParam("certinfoGuid", draftList.get(0).getRowguid());
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

    /**
     * 废弃草稿
     *
     * @param guid
     *            证照基本信息guid
     */
    public void abandonDraft(String guid) {
        CertInfo certinfo = iCertInfo.getCertInfoByRowguid(guid);
        if (certinfo != null && CertConstant.CONSTANT_INT_ZERO.equals(certinfo.getIshistory())) {
            // 删除多余草稿
            List<CertInfo> draftList = getDraftList(certinfo);
            if (ValidateUtil.isNotBlankCollection(draftList)) {
                for (CertInfo draft : draftList) {
                    // 删除基本信息
                    iCertInfo.deleteCertInfoByRowguid(draft.getRowguid());
                    // 删除照面信息
                    Map<String, Object> filter = new HashMap<>();
                    filter.put("certinfoguid", draft.getRowguid());
                    CertInfoExtension certExtension = noSQLSevice.find(CertInfoExtension.class, filter, false);
                    if (certExtension != null) {
                        // 删除照面信息附件
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
                        // 删除照面信息
                        noSQLSevice.delete(certExtension);
                    }
                    // 删除附件（正本）
                    if (StringUtil.isNotBlank(draft.getCertcliengguid())) {
                        iAttachService.deleteAttachByGuid(draft.getCertcliengguid());
                    }
                    // 删除附件（副本）
                    if (StringUtil.isNotBlank(draft.getCopycertcliengguid())) {
                        iAttachService.deleteAttachByGuid(draft.getCopycertcliengguid());
                    }
                    // 删除缩略图
                    if (StringUtil.isNotBlank(draft.getSltimagecliengguid())) {
                        iAttachService.deleteAttachByGuid(draft.getSltimagecliengguid());
                    }
                    // 删除doc（正本）
                    if (StringUtil.isNotBlank(draft.getPrintdocguid())) {
                        iAttachService.deleteAttachByGuid(draft.getPrintdocguid());
                    }
                    // 删除doc（副本）
                    if (StringUtil.isNotBlank(draft.getCopyprintdocguid())) {
                        iAttachService.deleteAttachByGuid(draft.getCopyprintdocguid());
                    }
                }
            }
            // 生成新草稿
            CertInfo draft = generateDraft(certinfo);
            if (draft != null) {
                addCallbackParam("certinfoGuid", draft.getRowguid());
            }
        }
    }

    /**
     * 获得草稿数量
     *
     * @return
     */
    private int getDraftCount(CertInfo certinfo) {
        int count = 0;
        SqlUtils sqlUtils = new SqlUtils();
        int version = certinfo.getVersion() + 1;
        String certid = certinfo.getCertid();
        // 草稿状态auditstatus = 20, ishistory = -1
        sqlUtils.eq("ishistory", CertConstant.CONSTANT_STR_NEGATIVE_ONE);
        sqlUtils.eq("auditstatus", CertConstant.CERT_CHECK_STATUS_WAIT_REPORT);
        sqlUtils.eq("version", String.valueOf(version));
        sqlUtils.eq("certid", certid);
        sqlUtils.eq("status", CertConstant.CERT_STATUS_COMMON);
        count = iCertInfo.getCountByCondition(sqlUtils.getMap());
        return count;
    }

    /**
     * 获得草稿
     *
     * @return
     */
    private List<CertInfo> getDraftList(CertInfo certinfo) {
        SqlUtils sqlUtils = new SqlUtils();
        int version = certinfo.getVersion() + 1;
        String certid = certinfo.getCertid();
        // 草稿状态auditstatus = 20, ishistory = -1
        sqlUtils.eq("ishistory", CertConstant.CONSTANT_STR_NEGATIVE_ONE);
        sqlUtils.eq("auditstatus", CertConstant.CERT_CHECK_STATUS_WAIT_REPORT);
        sqlUtils.eq("version", String.valueOf(version));
        sqlUtils.eq("certid", certid);
        sqlUtils.eq("status", CertConstant.CERT_STATUS_COMMON);
        return iCertInfo.getListByCondition(sqlUtils.getMap());
    }

    /**
     * 生成草稿
     *
     * @param certinfo
     * @return
     */
    private CertInfo generateDraft(CertInfo certinfo) {
        // 生成基本信息(草稿)
        CertInfo draft = (CertInfo) certinfo.clone();
        // 设置新主键
        draft.setRowguid(UUID.randomUUID().toString());
        draft.setOperatedate(new Date());
        // 版本+1
        draft.setVersion(certinfo.getVersion() + 1);
        // 草稿状态auditstatus = 20, ishistory = -1
        draft.setIshistory(CertConstant.CONSTANT_INT_NEGATIVE_ONE);
        draft.setAuditstatus(CertConstant.CERT_CHECK_STATUS_WAIT_REPORT);
        draft.setOperateusername(userSession.getDisplayName());
        // 状态为草稿
        draft.setStatus(CertConstant.CERT_STATUS_COMMON);
        // 未同步
        draft.setIspublish(CertConstant.CONSTANT_INT_ZERO);
        draft.setCertificateidentifier(null);
        draft.setLicdata_sync_sign(null);
        draft.setLicdata_sync_date(null);
        draft.setLicdata_sync_error_desc(null);
        // 拷贝证照附件（正本）
        int count = iAttachService.getAttachCountByClientGuid(certinfo.getCertcliengguid());
        draft.setCertcliengguid(UUID.randomUUID().toString());
        if (count > 0) {
            iAttachService.copyAttachByClientGuid(certinfo.getCertcliengguid(), null, null, draft.getCertcliengguid());
        }
        // 拷贝证照附件（副本）
        int copyCount = iAttachService.getAttachCountByClientGuid(certinfo.getCopycertcliengguid());
        draft.setCopycertcliengguid(UUID.randomUUID().toString());
        if (copyCount > 0) {
            iAttachService.copyAttachByClientGuid(certinfo.getCopycertcliengguid(), null, null,
                    draft.getCopycertcliengguid());
        }
        // 拷贝缩略图
        if(StringUtil.isNotBlank(certinfo.getSltimagecliengguid())){
            int sltCount = iAttachService.getAttachCountByClientGuid(certinfo.getSltimagecliengguid());
            draft.setSltimagecliengguid(UUID.randomUUID().toString());
            if (sltCount > 0) {
                iAttachService.copyAttachByClientGuid(certinfo.getSltimagecliengguid(), null, null,
                        draft.getSltimagecliengguid());
            }
        }
        // 不拷贝打印doc
        draft.setPrintdocguid(null);
        draft.setCopyprintdocguid(null);
        CertCatalog certCatalog = catalogService.getCatalogDetailByrowguid(draft.getCertareaguid());
        //多持有人,证件号加^
        if (CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsmultiowners())) {
            if (StringUtil.isNotBlank(draft.getCertownerno()) && !draft.getCertownerno().endsWith("^")) {
                draft.setCertownerno(draft.getCertownerno() + "^");
            }
        }
        iCertInfo.addCertInfo(draft);

        //多持有人复制
        List<CertOwnerInfo> ownerInfos = ownerInfoService.selectOwnerInfoByCertInfoGuid(certinfo.getRowguid());
        for (CertOwnerInfo ownerInfo : ownerInfos) {
            ownerInfo.setRowguid(UUID.randomUUID().toString());
            ownerInfo.setCertinfoguid(draft.getRowguid());
            ownerInfoService.addCertOwnerInfo(ownerInfo);
        }

        // 拷贝照面信息
        try {
            Map<String, Object> filter = new HashMap<>();
            filter.put("certinfoguid", certinfo.getRowguid());
            CertInfoExtension certExtension = noSQLSevice.find(CertInfoExtension.class, filter, false);
            if (certExtension != null) {
                // 设置新的certinfoguid
                certExtension.setCertinfoguid(draft.getRowguid());
                certExtension.setRowguid(UUID.randomUUID().toString());
                // 设置操作时间
                certExtension.setOperatedate(new Date());
                certExtension.setOperateusername(userSession.getDisplayName());
                // 拷贝照面信息附件
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
            e.printStackTrace();
            log.error(String.format("MongoDb复制照面信息 {certinfoguid = %s} 失败!", draft.getRowguid()));
            throw new RuntimeException();
        }
        return draft;
    }

    public void getOFDGuid(String rowGuid) {
        CertInfo certInfo = iCertInfo.getCertInfoByRowguid(rowGuid);
        boolean flag = false;
        List<FrameAttachInfo> frameAttachInfos = iAttachService.getAttachInfoListByGuid(certInfo.getCertcliengguid());
        for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
            if (StringUtil.toLowerCase(frameAttachInfo.getAttachFileName()).endsWith("ofd")
                    && "系统生成".equals(frameAttachInfo.getCliengInfo())) {
                addCallbackParam("attachguid", frameAttachInfo.getAttachGuid());
                flag = true;
                break;
            }
//            if ("系统生成".equals(frameAttachInfo.getCliengInfo())) {
//                addCallbackParam("attachguid", frameAttachInfo.getAttachGuid());
//                flag = true;
//                break;
//            }
        }
        List<FrameAttachInfo> copyFrameAttachInfos = iAttachService
                .getAttachInfoListByGuid(certInfo.getCopycertcliengguid());
        for (FrameAttachInfo frameAttachInfo : copyFrameAttachInfos) {
            if (StringUtil.toLowerCase(frameAttachInfo.getAttachFileName()).endsWith("ofd")
                    && "系统生成（副本）".equals(frameAttachInfo.getCliengInfo())) {
                addCallbackParam("copyattachguid", frameAttachInfo.getAttachGuid());
                return;
            }
        }
        if (!flag) {
            addCallbackParam("msg", "请先生成ofd文件！");
        }
    }

    /**
     * 判断是与否有写权限
     */
    public boolean canWrite() {
        boolean canWrite = false;
        SqlUtils sql = new SqlUtils();
        sql.eq("catalogid", certcatalogid);
        sql.eq("ouguid", ouguid);
        List<CertCatalogOu> certcatalogous = iCertCatalogou.getCertCatalogOuListByCondition(false, sql.getMap());
        if (ValidateUtil.isNotBlankCollection(certcatalogous)) {
            CertCatalogOu certcatalogou = certcatalogous.get(0);
            if (certcatalogou != null && certcatalogou.getWriteauthority() != null) {
                canWrite = certcatalogou.getWriteauthority().equals(1) ? true : false;
            }
        }
        return canWrite;
    }

    /**
     * 判断是否配置了ocr识别
     */
    public void canOcr(String certname){
        String[] certnames = configService.getFrameConfigValue("AS_OCR_CERTCATALOG").split(";");
        if(!Arrays.asList(certnames).contains(certname)){
            addCallbackParam("isocr" , 0);
        }else{
            if(certname.contains("身份证")){
                addCallbackParam("ocrcategory" , 0);
            }else if(certname.contains("营业执照")){
                addCallbackParam("ocrcategory" , 1);
            }else{
                addCallbackParam("ocrcategory" , 2);
            }
        }
    }

    public void checkCert(String rowGuid) {
        CertInfo certInfo = iCertInfo.getCertInfoByRowguid(rowGuid);
        CertCatalog certCatalog = catalogService.getCatalogDetailByrowguid(certInfo.getCertareaguid());
        if (certCatalog != null) {
            if (StringUtil.isNotBlank(certCatalog.getTempletcliengguid())
                    && iAttachService.getAttachCountByClientGuid(certCatalog.getTempletcliengguid()) > 0) {
                if (StringUtil.isNotBlank(certCatalog.getCopytempletcliengguid())
                        && iAttachService.getAttachCountByClientGuid(certCatalog.getCopytempletcliengguid()) > 0) {
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
        // 获取证照信息
        if (certinfo == null || certinfo.isEmpty()) {
            addCallbackParam("msg", "该证照不存在！");
            return;
        }
        int copyTempletCount = iAttachService.getAttachCountByClientGuid(certCatalog.getCopytempletcliengguid());
        // 添加提示信息
        if (copyTempletCount == 0) {
            // 不支持副本打印
            // 判断是否生成证照
            if (StringUtil.isBlank(certinfo.getCertcliengguid())) {
                addCallbackParam("msg", "请先生成证照");
                return;
            }
            List<FrameAttachInfo> attachInfoList = iAttachService.getAttachInfoListByGuid(certinfo.getCertcliengguid());
            if (ValidateUtil.isBlankCollection(attachInfoList)) {
                addCallbackParam("msg", "请先生成证照");
                return;
            }
            // 未先生成证照，提示
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
            // 支持副本打印
            String cliengGuid = certinfo.getCertcliengguid();
            String cliengInfo = "系统生成";
            String tip = "正本";
            if (CertConstant.CONSTANT_STR_ONE.equals(isPrintCopy)) {
                // 打印副本
                cliengGuid = certinfo.getCopycertcliengguid();
                tip = "副本";
                cliengInfo = "系统生成（副本）";
            }
            if (StringUtil.isBlank(cliengGuid)) {
                addCallbackParam("msg", String.format("请先生成证照（%s）", tip));
                return;
            }
            List<FrameAttachInfo> attachInfoList = iAttachService.getAttachInfoListByGuid(cliengGuid);
            if (ValidateUtil.isBlankCollection(attachInfoList)) {
                addCallbackParam("msg", String.format("请先生成证照（%s）", tip));
                return;
            }
            // 未先生成证照，提示
            boolean isGenerate = false;
            for (FrameAttachInfo attchInfo : attachInfoList) {
                if (cliengInfo.equals(attchInfo.getCliengInfo())) {
                    isGenerate = true;
                    break;
                }
            }
            if (!isGenerate) {
                addCallbackParam("msg", String.format("请先生成证照（%s）", tip));
                return;
            }
        }

        String cliengGuid;
        // 打印正/副本
        Map<String, String> returnMap = null;
        if (CertConstant.CONSTANT_STR_ONE.equals(isPrintCopy)) {
            // 打印副本
            // 是否打印过（副本）
            if (StringUtil.isNotBlank(certinfo.getCopyprintdocguid())) {
                // 已打印
                addCallbackParam("certinfoguid", certinfo.getRowguid());
                addCallbackParam("attachguid", certinfo.getCopyprintdocguid());
                FrameAttachInfo info = iAttachService.getAttachInfoDetail(certinfo.getCopyprintdocguid());
                if (info != null && StringUtil.isNotBlank(info.getContentType())) {
                    addCallbackParam("contenttype", info.getContentType());
                }
                addCallbackParam("filename", certinfo.getCertname() + "(副本)");
                return;
            }
            // 没打印，打印(判断是否有套打模版副本附件)
            //先默认cliengGuid为副本模版
            cliengGuid = certCatalog.getCopytempletcliengguid();
            //套打模版（副本）cliengguid不为空
            if (StringUtil.isNotBlank(certCatalog.getTdCopytempletcliengguid())) {
                //查询附件数量
                int TdCopyTempletCount = iAttachService
                        .getAttachCountByClientGuid(certCatalog.getTdCopytempletcliengguid());
                if (TdCopyTempletCount > 0) {
                    cliengGuid = certCatalog.getTdCopytempletcliengguid();
                }
            }
            returnMap = generateCertDoc(cliengGuid, true, certinfo);

        }
        else {
            // 打印正本
            // 是否打印过（正本）
            if (StringUtil.isNotBlank(certinfo.getPrintdocguid())) {
                // 已打印
                addCallbackParam("certinfoguid", certinfo.getRowguid());
                addCallbackParam("attachguid", certinfo.getPrintdocguid());
                FrameAttachInfo info = iAttachService.getAttachInfoDetail(certinfo.getPrintdocguid());
                if (info != null && StringUtil.isNotBlank(info.getContentType())) {
                    addCallbackParam("contenttype", info.getContentType());
                }
                addCallbackParam("filename", certinfo.getCertname());
                return;
            }
            // 没打印，打印(判段是否有套打模版正本附件)
            //先默认cliengGuid为正本模版
            cliengGuid = certCatalog.getTempletcliengguid();
            //套打模版正本cliengguid不为空
            if (StringUtil.isNotBlank(certCatalog.getTdTempletcliengguid())) {
                //查询附件数量
                int TdTempletCount = iAttachService.getAttachCountByClientGuid(certCatalog.getTdTempletcliengguid());
                if (TdTempletCount > 0) {
                    cliengGuid = certCatalog.getTdTempletcliengguid();
                }
            }
            returnMap = generateCertDoc(cliengGuid, false, certinfo);
        }

        if (ValidateUtil.isNotBlankMap(returnMap)) {
            String isSuccess = returnMap.get("issuccess");
            if (CertConstant.CONSTANT_STR_ONE.equals(isSuccess)) {
                // 打印成功
                addCallbackParam("certinfoguid", certinfo.getRowguid());
                addCallbackParam("attachguid", returnMap.get("attachguid"));
                addCallbackParam("contenttype", returnMap.get("contenttype"));
                addCallbackParam("filename", certinfo.getCertname());
            }
            else {
                // 打印失败
                addCallbackParam("msg", returnMap.get("msg"));
            }
        }
    }

    /**
     * 打印证照doc
     * @param cliengGuid 证照模板文件（正/副本）guid
     * @param isPrintCopy 是否打印副本
     * @return 返回Map issuccess（1 打印成功, 0 打印失败）
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> generateCertDoc(String cliengGuid, boolean isPrintCopy, CertInfo certInfo) {
        Map<String, String> returnMap = null;
        List<CertMetadata> metadataList = iCertMetaData.selectTopDispinListByCertguid(certInfo.getCertareaguid());
        // 获得照面信息
        Map<String, Object> filter = new HashMap<>();
        // 设置基本信息guid
        filter.put("certinfoguid", certInfo.getRowguid());
        CertInfoExtension dataBean = noSQLSevice.find(CertInfoExtension.class, filter, false);
        if (dataBean == null) {
            dataBean = new CertInfoExtension();
            dataBean.setRowguid(UUID.randomUUID().toString());
            dataBean.setCertinfoguid(certInfo.getRowguid());
        }
        // 日期格式转换
        certInfoBizlogic.convertDate(metadataList, dataBean);
        GenerateBizlogic generateBizlogic = new GenerateBizlogic();
        try {
            EpointFrameDsManager.begin(null);
            Map<String, Object> wordMap = generateBizlogic.getWordInfo(metadataList, dataBean);
            // word域名数组
            String[] fieldNames = (String[]) wordMap.get("fieldNames");
            // word域值数组
            Object[] values = (Object[]) wordMap.get("values");
            // 图片map
            Map<String, Record> imageMap = (Map<String, Record>) wordMap.get("imageMap");
            // 子表数据
            DataSet dataSet = (DataSet) wordMap.get("dataSet");
            returnMap = generateBizlogic.generatePrintDocSupportCopy(cliengGuid, isPrintCopy, userSession.getUserGuid(),
                    userSession.getDisplayName(), fieldNames, values, imageMap, dataSet, certInfo);
        }
        catch (Exception e) {
            e.printStackTrace();
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
