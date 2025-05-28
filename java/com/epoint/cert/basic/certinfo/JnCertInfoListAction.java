package com.epoint.cert.basic.certinfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.aspose.words.DataSet;
import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.FetchHandler9;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.SimpleFetchHandler9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.util.DataUtil;
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
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.role.api.IRoleService;

/**
 * 证照基本信息表list页面对应的后台
 *
 * @version [版本号, 2017年11月1日]
 * @作者 dingwei
 */
@RestController("jncertinfolistaction")
@Scope("request")
public class JnCertInfoListAction extends BaseController {
    private static final long serialVersionUID = 1L;

    /**
     * 日志
     */
    private Logger log = LogUtil.getLog(CertInfoListAction.class);

    /**
     * 证照目录和部门关系api
     */
    @Autowired
    private ICertCatalogOu iCertCatalogou;
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

    @Autowired
    private IAuditOrgaArea areaService;
    @Autowired
    private ICodeItemsService codeItemsService;
    @Autowired
    private IOuService iOuService;

    /**
     * 证照基本信息表实体对象
     */
    private CertInfo dataBean;

    /**
     * 证照实例bizlogic
     */
    private CertInfoBizlogic certInfoBizlogic = new CertInfoBizlogic();

    /**
     * 表格控件model
     */
    private DataGridModel<CertInfo> model;
    private List<SelectItem> belongToAreaModel;
    private List<SelectItem> qlkindModel;
    /**
     * 证照目录的版本唯一标识
     */
    private String certcatalogid;
    /**
     * 是否是父目录 0 不是，1是
     */
    private String ispid;

    private String hideareacode;

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
    private Date beginTime;
    private Date endTime;
    /**
     * MongoDB\HBase通用service
     */
    private NoSQLSevice noSQLSevice = new NoSQLSevice();

    @Override
    public void pageLoad() {
        dataBean = new CertInfo();
        if (StringUtil.isNotBlank(userSession.getBaseOUGuid())) {
            ouguid = userSession.getBaseOUGuid();
        } else {
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
            treeModel = getLeftCatalogTree();
            treeModel.setRootName("证照类型");
            treeModel.setRootGuid("f9root");
            treeModel.setRootCode("f9root");
            treeModel.setRootSelect(false);
        }
        return treeModel;
    }

    public LazyTreeModal9 getLeftCatalogTree() {
        // 我已评估无sonar问题
        LazyTreeModal9 lazyTreeModal9 = new LazyTreeModal9(new SimpleFetchHandler9()
        {

            @Override
            public <T> List<T> search(String condition) {
                List certCatalogs = new ArrayList();
                if (StringUtil.isNotBlank(condition)) {
                    SqlUtils sqlUtils = new SqlUtils();
                    sqlUtils.eq("ishistory", "0");
                    sqlUtils.eq("materialtype", "1");
                    sqlUtils.eq("isenable", "1");
                    sqlUtils.in("CERTCATALOGID", "select DISTINCT CATALOGID from cert_catalog_ou");
                    sqlUtils.setOrder("tycertcatcode asc,ordernum", "desc");
                    sqlUtils.like("certname", condition);

                    certCatalogs = iCertCatalog.getListByCondition(sqlUtils.getMap());
                }
                return certCatalogs;
            }

            @Override
            public <T> List<T> fetchData(int level, TreeData treeData) {
                List list = new ArrayList();

                SqlUtils sqlUtils = new SqlUtils();
                sqlUtils.eq("ishistory", "0");
                sqlUtils.eq("materialtype", "1");
                sqlUtils.eq("isenable", "1");
                sqlUtils.in("CERTCATALOGID", "select DISTINCT CATALOGID from cert_catalog_ou");
                sqlUtils.setOrder("tycertcatcode asc,ordernum", "desc");

                // 一开始加载或者点击节点的时候触发
                if (level == FetchHandler9.FETCH_ONELEVEL) {
                    // 最开始加载的时候，把所有的窗口部门加载出来，最开始treeData的guid为空
                    if (StringUtil.isBlank(treeData.getObjectGuid())) {
                        list = CommonDao.getInstance().findList(
                                "select o.OUNAME,oe.AREACODE from frame_ou o,frame_ou_extendinfo oe where o.OUGUID=oe.OUGUID and IFNULL(PARENTOUGUID,'')='' order by o.ORDERNUMBER desc",
                                FrameOu.class);
                    }
                    else {
                        // 如果点击的是部门的checkbox，则返回该部门下所有的证照的list
                        if (treeData.getObjectcode().startsWith("frameou")) {
                            // 父目录
                            sqlUtils.eq("isparent", "1");
                            sqlUtils.in("CERTCATALOGID",
                                    "select DISTINCT CATALOGID from cert_catalog_ou where areacode='"
                                            + treeData.getObjectGuid() + "'");
                            List<CertCatalog> certCatalogs = iCertCatalog.getListByCondition(sqlUtils.getMap());
                            if (certCatalogs != null && !certCatalogs.isEmpty()) {
                                for (CertCatalog catalog : certCatalogs) {
                                    catalog.set("areacode", treeData.getObjectGuid());
                                }
                            }
                            list.addAll(certCatalogs);
                        }
                        // 如果点击的是证照的checkbox，则返回该是事项的list
                        else if (treeData.getObjectcode().startsWith("certcatalog")) {
                            String areacode = treeData.getObjectcode().split(",")[1];
                            // 父目录
                            sqlUtils.eq("isparent", "0");
                            sqlUtils.eq("PARENTID", treeData.getObjectGuid());
                            List<CertCatalog> certCatalogs = iCertCatalog.getListByCondition(sqlUtils.getMap());
                            if (certCatalogs != null && !certCatalogs.isEmpty()) {
                                for (CertCatalog catalog : certCatalogs) {
                                    catalog.set("areacode", areacode);
                                }
                            }
                            list.addAll(certCatalogs);
                        }
                    }
                }
                else {
                    if (treeData.getObjectcode().startsWith("certcatalog")) {
                        String areacode = treeData.getObjectcode().split(",")[1];
                        sqlUtils.eq("isparent", "0");
                        sqlUtils.eq("PARENTID", treeData.getObjectGuid());
                        List<CertCatalog> certCatalogs = iCertCatalog.getListByCondition(sqlUtils.getMap());
                        if (certCatalogs != null && !certCatalogs.isEmpty()) {
                            for (CertCatalog catalog : certCatalogs) {
                                catalog.set("areacode", areacode);
                            }
                        }
                        list.addAll(certCatalogs);
                    }
                }

                return list;
            }

            @Override
            public int fetchChildCount(TreeData treeData) {
                int count = 0;

                SqlUtils sqlUtils = new SqlUtils();
                sqlUtils.eq("ishistory", "0");
                sqlUtils.eq("materialtype", "1");
                sqlUtils.eq("isenable", "1");
                sqlUtils.in("CERTCATALOGID", "select DISTINCT CATALOGID from cert_catalog_ou");

                if (treeData.getObjectcode().startsWith("frameou")) {
                    sqlUtils.eq("isparent", "1");
                    sqlUtils.in("CERTCATALOGID", "select DISTINCT CATALOGID from cert_catalog_ou where areacode='"
                            + treeData.getObjectGuid() + "'");
                    count = iCertCatalog.getCertCatalogCount(sqlUtils.getMap());
                }
                else if (treeData.getObjectcode().startsWith("certcatalog")) {
                    sqlUtils.eq("isparent", "0");
                    sqlUtils.eq("PARENTID", treeData.getObjectGuid());
                    count = iCertCatalog.getCertCatalogCount(sqlUtils.getMap());
                }

                return count;
            }

            @Override
            public List<TreeData> changeDBListToTreeDataList(List<?> dataList) {
                List<TreeData> treeList = new ArrayList<TreeData>();
                SqlUtils squtil = new SqlUtils();
                if (dataList != null) {
                    for (Object obj : dataList) {
                        squtil.clear();
                        squtil.eq("ishistory", "0");
                        squtil.eq("materialtype", "1");
                        squtil.eq("isenable", "1");
                        squtil.in("CERTCATALOGID", "select DISTINCT CATALOGID from cert_catalog_ou");

                        if (obj instanceof FrameOu) {
                            FrameOu frameOu = (FrameOu) obj;
                            TreeData treeData = new TreeData();
                            treeData.setObjectGuid(frameOu.getStr("areacode"));
                            treeData.setTitle(frameOu.getOuname());
                            treeData.setObjectParentGuid("0");
                            // treeData.setNoClick(true);
                            // 没有子节点的不允许点击
                            int childCount = 0;
                            squtil.eq("isparent", "1");
                            squtil.in("CERTCATALOGID", "select DISTINCT CATALOGID from cert_catalog_ou where areacode='"
                                    + treeData.getObjectGuid() + "'");
                            childCount = iCertCatalog.getCertCatalogCount(squtil.getMap());
                            if (childCount == 0) {
                                treeData.setNoClick(true);
                            }
                            else {
                                // objectcode的作用是来区分是点击了部门还是事项
                                treeData.setObjectcode("frameou" + "," + frameOu.getStr("areacode"));
                                treeList.add(treeData);
                            }
                        }
                        if (obj instanceof CertCatalog) {
                            CertCatalog catalog = (CertCatalog) obj;
                            TreeData treeData = new TreeData();
                            treeData.setObjectGuid(catalog.getCertcatalogid());
                            treeData.setTitle(catalog.getCertname());

                            int childCount = 0;
                            squtil.eq("isparent", "0");
                            squtil.eq("PARENTID", treeData.getObjectGuid());
                            childCount = iCertCatalog.getCertCatalogCount(squtil.getMap());
                            if (childCount > 0) {
                                treeData.setLeaf(true);
                            }
                            treeData.setObjectcode("certcatalog" + "," + catalog.getStr("areacode"));
                            treeList.add(treeData);
                        }
                    }
                }

                return treeList;
            }
        });
        return lazyTreeModal9;
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
        } else if (CertConstant.CONSTANT_INT_ZERO.equals(certcatalog.getIsenable())) {
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
                    } else if (CertConstant.CERT_CHECK_STATUS_WAIT_REPORT.equals(certinfo.getAuditstatus())
                            || CertConstant.CERT_CHECK_STATUS_NOT_PASS.equals(certinfo.getAuditstatus())) {
                        iCertInfo.deleteCertInfoByRowguid(certinfo.getRowguid());
                    } else {
                        // 移为历史数据
                        certinfo.setIshistory(CertConstant.CONSTANT_INT_ONE);
                        //删除标识，便于同步的时候查找
                        //licdata_sync_sign=3表示已删除，且删除的数据原来没同步过。licdata_sync_sign=4表示已删除，且删除的数据原来同步成功了。
                        if (CertConstant.CONSTANT_STR_ONE.equals(certinfo.getLicdata_sync_sign())) {
                            certinfo.setLicdata_sync_sign("4");
                        } else {
                            certinfo.setLicdata_sync_sign("3");
                        }
                        iCertInfo.updateCertInfo(certinfo);
                    }
                } else {
                    // 移为历史数据
                    certinfo.setIshistory(CertConstant.CONSTANT_INT_ONE);
                    //删除标识，便于同步的时候查找
                    //licdata_sync_sign=3表示已删除，且删除的数据原来没同步过。licdata_sync_sign=4表示已删除，且删除的数据原来同步成功了。
                    if (CertConstant.CONSTANT_STR_ONE.equals(certinfo.getLicdata_sync_sign())) {
                        certinfo.setLicdata_sync_sign("4");
                    } else {
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
        } else {
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
            model = new DataGridModel<CertInfo>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 9179330200184695904L;

                @Override
                public List<CertInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String areacode = "";
                    String isounode = "0";
                    if (StringUtil.isNotBlank(hideareacode) && !"f9root".equals(hideareacode)) {
                        areacode = hideareacode.split(",")[1];
                        if (hideareacode.startsWith("frameou")) {
                            isounode = "1";
                        }
                    }
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
                    } else {
                        sqlUtils.eq("ISHISTORY", CertConstant.CONSTANT_STR_NEGATIVE_ONE);
                        sqlUtils.eq("auditstatus", dataBean.getAuditstatus());
                    }
//                    String orgcode = CertUserSession.getInstance().getOrgcode();
//                    // 默认使用orgcode过滤， 只能变更自己颁发的证照,兼容老Ouguid数据
//                    sqlUtils.in("OUGUID",
//                            "'" + ouguid + "'" + (StringUtil.isNotBlank(orgcode) ? (",'" + orgcode + "'") : ""));

                    // 版本唯一标识筛选条件
                    if (CertConstant.CONSTANT_STR_ZERO.equals(getIspid())) {
                        // 不是父目录
                        if (StringUtil.isNotBlank(certcatalogid)) {
                            CertCatalog catalog = iCertCatalog.getLatestCatalogDetailByCatalogid(certcatalogid);
                            if (catalog != null) {
                                sqlUtils.eq("CERTCATALOGID", certcatalogid);
                            } else {
                                this.setRowCount(0);
                                return certinfoList;
                            }
                        }
                    } else {
                        // 是父目录，查出该父目录下所有的证照实例
                        if (StringUtil.isNotBlank(certcatalogid)) {
                            parentcertcatalogid = certcatalogid;
                        }
                        if ("1".equals(isounode)) {
                            parentcertcatalogid = "";
                        }
                    }
                    // 持有人姓名
                    if (StringUtil.isNotBlank(dataBean.getCertownername())) {
                        sqlUtils.like("CERTOWNERNAME",
                                FormatUtil.getdecryptSM4ToData(dataBean.getCertownername(), certcatalogid, 0));
                    }
                    // 证照编号
                    if (StringUtil.isNotBlank(dataBean.getCertno())) {
                        sqlUtils.like("CERTNO", dataBean.getCertno());
                    }
                    if (StringUtil.isNotBlank(dataBean.getCertname())) {
                        sqlUtils.like("certname", dataBean.getCertname());
                    }
                    if (StringUtil.isNotBlank(dataBean.getAreacode())) {
                        sqlUtils.eq("areacode", dataBean.getAreacode());
                    }
                    if (StringUtil.isNotBlank(dataBean.getCertawarddept())) {
                        sqlUtils.like("certawarddept", dataBean.getCertawarddept());
                    }
                    if (StringUtil.isNotBlank(dataBean.getCertawarddept())) {
                        sqlUtils.like("certawarddept", dataBean.getCertawarddept());
                    }
                    if (beginTime != null && endTime != null) {
                        sqlUtils.between("awarddate", beginTime, endTime);
                    }
                    if (StringUtil.isNotBlank(areacode)) {
                        // 辖区
                        sqlUtils.eq("AREACODE", areacode);
                    }
                    // 额外查询条件，针对实例，where 语句后面的sql条件
                    String extraauthority = "";
                    // 当目录版本id不为空或者搜索条件不为空时候展示实例数据
                    // 额外的权限控制
                    SqlUtils sqlutilextra = new SqlUtils();
                    if (StringUtil.isNotBlank(certcatalogid)) {
                        sqlutilextra.eq("catalogid", certcatalogid);
                    }
                    if (StringUtil.isNotBlank(areacode)) {
                        sqlutilextra.eq("areacode", areacode);
                    }
                    // sqlutilextra.eq("ouguid", ouguid);
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
                            if (StringUtil.isNotBlank(certinfo.getCertownername())) {
                                certinfo.setCertownername(
                                        FormatUtil.getdecryptSM4ToData(certinfo.getCertownername(), certcatalogid, 0));
                            }
                        }
                    }
                    this.setRowCount(pageData.getRowCount());


                    return certinfoList;
                }
            };
        }
        return model;
    }

    /**
     * 证照变更提示
     *
     * @param guid 证照基本信息guid
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
            } else {
                addCallbackParam("msg", "1");
            }
        }
    }

    /**
     * 证照变更提示
     *
     * @param guid 证照基本信息guid
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
            } else { // 加载草稿
                List<CertInfo> draftList = getDraftList(certinfo);
                if (ValidateUtil.isNotBlankCollection(draftList)) {
                    addCallbackParam("loaddraft", "1");
                    addCallbackParam("displayname", draftList.get(0).getOperateusername());
                    addCallbackParam("certinfoGuid", draftList.get(0).getRowguid());
                    addCallbackParam("certname", certinfo.getCertname());
                }
            }
        } else if (certinfo != null) {
            CertInfo oldCertinfo = iCertInfo.getUseCertInfoByCertId(certinfo.getCertid());
            if (oldCertinfo != null) {
                addCallbackParam("oldcertguid", oldCertinfo.getRowguid());
            }
        }
    }

    /**
     * 废弃草稿
     *
     * @param guid 证照基本信息guid
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
        if (StringUtil.isNotBlank(certinfo.getSltimagecliengguid())) {
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
        } catch (Exception e) {
            log.info(e);
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
    public void canOcr(String certname) {
        String[] certnames = configService.getFrameConfigValue("AS_OCR_CERTCATALOG").split(";");
        if (!Arrays.asList(certnames).contains(certname)) {
            addCallbackParam("isocr", 0);
        } else {
            if (certname.contains("身份证")) {
                addCallbackParam("ocrcategory", 0);
            } else if (certname.contains("营业执照")) {
                addCallbackParam("ocrcategory", 1);
            } else {
                addCallbackParam("ocrcategory", 2);
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
            } else {
                addCallbackParam("msg", "请配置证照模版！");
            }
        } else {
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
        } else {
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

        } else {
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
            } else {
                // 打印失败
                addCallbackParam("msg", returnMap.get("msg"));
            }
        }
    }

    /**
     * 打印证照doc
     *
     * @param cliengGuid  证照模板文件（正/副本）guid
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
        } catch (Exception e) {
            log.info(e);
        } finally {
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
        } else if (CertConstant.CONSTANT_INT_ZERO.equals(certcatalog.getIsenable())) {
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

    public Date getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getHideareacode() {
        return hideareacode;
    }

    public void setHideareacode(String hideareacode) {
        this.hideareacode = hideareacode;
    }

    /**
     * 导出
     * type=1,导出第一页；type=2,导出选择记录，type=3,导出全部
     */
    public void exportAllData(String cataguid, String type) {
        if (StringUtil.isBlank(cataguid)) {
            addCallbackParam("msg", "请先选择证照类型后，在进行导出！");
            return;
        }
        CertCatalog certCatalog = iCertCatalog.getLatestCatalogDetailByCatalogid(cataguid);
        if (certCatalog == null) {
            addCallbackParam("msg", "当前证照类型已被删除，请刷新后在重试！");
            return;
        }
        if (StringUtil.isBlank(type)) {
            type = "1";
        }
        StringBuilder nameBuilder = new StringBuilder(certCatalog.getCertname() + "统计");
        nameBuilder.append("_").append(".xls");
        String defaultFileName = nameBuilder.toString();
        StringBuilder pathBuilder = new StringBuilder();
        String newFile = request.getServletContext().getRealPath("epointtemp");
        pathBuilder.append(newFile).append(File.separator).append(defaultFileName);
        log.info(org.apache.commons.lang3.StringUtils.center("开始导出清单事权数据", 30, "="));
        HSSFWorkbook wb = new HSSFWorkbook();
        try {
            //开始构建表头
            HSSFSheet sheet = wb.createSheet(certCatalog.getCertname() + "统计");//创建sheet页
            HSSFFont curFont = wb.createFont();                    //设置字体
            curFont.setCharSet(HSSFFont.DEFAULT_CHARSET);                //设置中文字体，那必须还要再对单元格进行编码设置
            curFont.setFontHeightInPoints((short) 12);                //字体大小

            HSSFFont greenFont = wb.createFont();
            BeanUtils.copyProperties(curFont, greenFont);
            greenFont.setColor(IndexedColors.GREEN.getIndex());
            HSSFFont redFont = wb.createFont();
            BeanUtils.copyProperties(curFont, redFont);
            redFont.setColor(IndexedColors.RED.getIndex());

            HSSFRow hssfRowTopHead = sheet.createRow(0);//第一行  表头
            String[] headers = new String[]{"证照名称", "证照编号", "证照颁发机构", "证照颁发日期", "持证主体", "面向持证主体类别",
                    "持证主体代码类型", "持证主体代码", "证照有效期起始日期", "证照有效期截止日期"};//固定列
            List<String> allHeaders = new ArrayList<>(Arrays.asList(headers));
            int areaStartIndex = 10;//辖区表头开始索引值(从第9列开始)
            Map<String, String> map = new HashMap<>();
            List<CertMetadata> certMetadata = iCertMetaData.selectDispinListByCertguid(certCatalog.getRowguid());
            if (EpointCollectionUtils.isNotEmpty(certMetadata)) {
                for (CertMetadata metadata : certMetadata) {
                    allHeaders.add(metadata.getFieldchinesename());
                    map.put(metadata.getFieldchinesename(), metadata.getFieldname());
                }
            }

            //创建第一行表头的单元格
            HSSFFont curFontBold = wb.createFont();
            BeanUtils.copyProperties(curFont, curFontBold);
            curFontBold.setBold(true);//加粗
            for (int i = 0; i < allHeaders.size(); i++) {
                HSSFCell box = hssfRowTopHead.createCell(i);
                box.setCellValue(allHeaders.get(i));
                setDefaultStyleOfBox(box, curFontBold);
            }
            sheet.setDefaultColumnWidth((short) 20);

            HSSFCellStyle nonePowerStyle = wb.createCellStyle();

            nonePowerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());//灰色背景  无事权
            nonePowerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            nonePowerStyle.setAlignment(HorizontalAlignment.CENTER);
            nonePowerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            HSSFCellStyle hasPowerStyle = wb.createCellStyle();
            hasPowerStyle.cloneStyleFrom(nonePowerStyle);
            hasPowerStyle.setFillForegroundColor(IndexedColors.WHITE1.getIndex());//白色背景 有事权


            int rowCount = 1;//行号
            List<CertInfo> list = new ArrayList<>();
            //TODO 查询数据
            if (StringUtil.isBlank(type) || "1".equals(type)){
                list=exportSlectData();
            }
            else if ("2".equals(type)){
                list=exportFirstPageData();
            }
            else {
                list=exportAllDatalist();
            }
            if (EpointCollectionUtils.isNotEmpty(list)) {
                for (CertInfo certInfo : list) {
                    HSSFRow currentRow = sheet.createRow(rowCount);

//                    HSSFCell indexCell = currentRow.createCell(0);//项目号（序号）
//                    indexCell.setCellValue(String.valueOf(rowCount));
//                    setDefaultStyleOfBox(indexCell,curFont);

                    HSSFCell catalogNameCell = currentRow.createCell(0);//禁止或许可事项
                    catalogNameCell.setCellValue(certInfo.getCertname());
                    setDefaultStyleOfBox(catalogNameCell, curFont);

                    HSSFCell catalogCodeCell = currentRow.createCell(1);//负面清单事项编码
                    catalogCodeCell.setCellValue(certInfo.getCertno());
                    setDefaultStyleOfBox(catalogCodeCell, curFont);

                    HSSFCell catalogDescCell = currentRow.createCell(2);//禁止或许可准入措施描述
                    catalogDescCell.setCellValue(certInfo.getCertawarddept());
                    setDefaultStyleOfBox(catalogDescCell, curFont);

                    HSSFCell catalogZwfwNameCell = currentRow.createCell(3);//政务服务事项名称
                    catalogZwfwNameCell.setCellValue(certInfo.getAwarddate() == null ? "" : EpointDateUtil.convertDate2String(certInfo.getAwarddate()));
                    setDefaultStyleOfBox(catalogZwfwNameCell, curFont);

                    HSSFCell catalogZwfwCodeCell = currentRow.createCell(4);//政务服务事项编码
                    catalogZwfwCodeCell.setCellValue(certInfo.getCertownername());
                    setDefaultStyleOfBox(catalogZwfwCodeCell, curFont);

                    HSSFCell catalogTaskTypeCell = currentRow.createCell(5);//证照持有人类型
                    if (StringUtil.isNotBlank(certInfo.getCertownertype())) {
                        catalogTaskTypeCell.setCellValue(codeItemsService.getItemTextByCodeName("证照持有人类型", certInfo.getCertownertype()));
                    }
                    setDefaultStyleOfBox(catalogTaskTypeCell, curFont);

                    HSSFCell catalogLevelCell = currentRow.createCell(6);//行使层级
                    if (StringUtil.isNotBlank(certInfo.getCertownercerttype())) {
                        catalogLevelCell.setCellValue(codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", certInfo.getCertownercerttype()));
                    }
                    setDefaultStyleOfBox(catalogLevelCell, curFont);

                    HSSFCell catalogLevelCell8 = currentRow.createCell(7);//行使层级
                    catalogLevelCell8.setCellValue(certInfo.getCertownerno());
                    setDefaultStyleOfBox(catalogLevelCell8, curFont);

                    HSSFCell catalogLevelCell9 = currentRow.createCell(8);//行使层级
                    catalogLevelCell9.setCellValue(certInfo.getExpiredatefrom() == null ? "" : EpointDateUtil.convertDate2String(certInfo.getExpiredatefrom()));
                    setDefaultStyleOfBox(catalogLevelCell9, curFont);

                    HSSFCell catalogLevelCell10 = currentRow.createCell(9);//行使层级
                    catalogLevelCell10.setCellValue(certInfo.getExpiredatefrom() == null ? "" : EpointDateUtil.convertDate2String(certInfo.getExpiredatefrom()));
                    setDefaultStyleOfBox(catalogLevelCell10, curFont);

                    //开始处理业务数据的单元格
                    // 获取照面信息
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", certInfo.getRowguid());
                    //报连接泄露
                    NoSQLSevice noSQLSevice1 = new NoSQLSevice();
                    CertInfoExtension certInfoExtension = noSQLSevice1.find(CertInfoExtension.class, filter, false);
//                    CertInfoExtension certInfoExtension=new CertInfoExtension();
                    if (certInfoExtension == null) {
                        continue;
                    }
                    // 获得元数据配置表所有顶层节点
                    certInfoBizlogic.convertDate(certMetadata, certInfoExtension);
                    if (allHeaders.size() <= 10) {
                        continue;
                    }
                    for (int i = areaStartIndex; i < allHeaders.size(); i++) {
                        String header = allHeaders.get(i);
                        HSSFCell catalogLevelCelli = currentRow.createCell(i);//行使层级
                        catalogLevelCelli.setCellValue(certInfoExtension.getStr(map.get(header)));
                        setDefaultStyleOfBox(catalogLevelCelli, curFont);
                    }
                    rowCount++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            wb = null;
        } finally {
            log.info(StringUtils.center("导出数据完成", 30, "="));
            if (wb != null) {
                try {
                    wb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (wb == null) {
            addCallbackParam("msg", "导出失败，请联系管理员！");
            return;
        }

        //保存到附件
        File file = FileManagerUtil.createFile(pathBuilder.toString());
        OutputStream outputStream1 = null;
        FileInputStream fileInputStream = null;

        try {
            // 写excel表格
            outputStream1 = new FileOutputStream(pathBuilder.toString());
            wb.write(outputStream1);
            // 存入附件表中
            fileInputStream = new FileInputStream(file);
            String fileTyle = file.getName().substring(file.getName().lastIndexOf('.'));
            String attachFileName = file.getName();
            FrameAttachInfo attachInfo = new FrameAttachInfo();
            attachInfo.setAttachFileName(attachFileName);
            attachInfo.setAttachGuid(UUID.randomUUID().toString());
            attachInfo.setContentType(fileTyle);
            attachInfo.setUploadDateTime(new Date());
            IAttachService iAttachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
            iAttachService.addAttach(attachInfo, fileInputStream);
            // 附件下载地址
            String attachDownPageUrl = iAttachService.getAttachDownPath(attachInfo);
            HttpServletRequest request = getRequestContext().getReq();
            attachDownPageUrl = WebUtil.getRequestCompleteUrl(request) + "/" + attachDownPageUrl;
            addCallbackParam("msg", "导出成功！");
            addCallbackParam("path", attachDownPageUrl);
        } catch (IOException e) {
            log.error("证照导出异常!", e);
            addCallbackParam("msg", "导出失败，请联系管理员！");
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (outputStream1 != null) {
                    outputStream1.close();

                }
                if (wb != null) {
                    wb.close();
                }
                // 文件删除
                if (file.exists()) {
                    boolean delete = file.delete();
                    if (delete) {
                        log.info("文件 " + file.getName() + "删除成功");
                    } else {
                        log.info("删除失败");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public List<CertInfo> exportSlectData() {
        List<String> selectKeys = getDataGridData().getSelectKeys();
        List<CertInfo> list = new ArrayList<>();
        if (EpointCollectionUtils.isNotEmpty(selectKeys)) {
            for (String key : selectKeys) {
                CertInfo certInfoByRowguid = iCertInfo.getCertInfoByRowguid(key);
                if (certInfoByRowguid != null) {
                    list.add(certInfoByRowguid);
                }

            }
        }
        return list;

    }

    public List<CertInfo> exportFirstPageData() {
        return getDataGridData().getWrappedData();

    }
    public List<CertInfo> exportAllDatalist() {
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
        } else {
            sqlUtils.eq("ISHISTORY", CertConstant.CONSTANT_STR_NEGATIVE_ONE);
            sqlUtils.eq("auditstatus", dataBean.getAuditstatus());
        }
//                    String orgcode = CertUserSession.getInstance().getOrgcode();
//                    // 默认使用orgcode过滤， 只能变更自己颁发的证照,兼容老Ouguid数据
//                    sqlUtils.in("OUGUID",
//                            "'" + ouguid + "'" + (StringUtil.isNotBlank(orgcode) ? (",'" + orgcode + "'") : ""));

        // 版本唯一标识筛选条件
        if (CertConstant.CONSTANT_STR_ZERO.equals(getIspid())) {
            // 不是父目录
            if (StringUtil.isNotBlank(certcatalogid)) {
                CertCatalog catalog = iCertCatalog.getLatestCatalogDetailByCatalogid(certcatalogid);
                if (catalog != null) {
                    sqlUtils.eq("CERTCATALOGID", certcatalogid);
                } else {
                    return certinfoList;
                }
            }
        } else {
            // 是父目录，查出该父目录下所有的证照实例
            if (StringUtil.isNotBlank(certcatalogid)) {
                parentcertcatalogid = certcatalogid;
            }
        }
        // 持有人姓名
        if (StringUtil.isNotBlank(dataBean.getCertownername())) {
            sqlUtils.like("CERTOWNERNAME",
                    FormatUtil.getdecryptSM4ToData(dataBean.getCertownername(), certcatalogid, 0));
        }
        // 证照编号
        if (StringUtil.isNotBlank(dataBean.getCertno())) {
            sqlUtils.like("CERTNO", dataBean.getCertno());
        }
        if (StringUtil.isNotBlank(dataBean.getCertname())) {
            sqlUtils.like("certname", dataBean.getCertname());
        }
        if (StringUtil.isNotBlank(dataBean.getAreacode())) {
            sqlUtils.eq("areacode", dataBean.getAreacode());
        }
        if (StringUtil.isNotBlank(dataBean.getCertawarddept())) {
            sqlUtils.like("certawarddept", dataBean.getCertawarddept());
        }
        if (StringUtil.isNotBlank(dataBean.getCertawarddept())) {
            sqlUtils.like("certawarddept", dataBean.getCertawarddept());
        }
        if (beginTime != null && endTime != null) {
            sqlUtils.between("awarddate", beginTime, endTime);
        }
        //辖区
//                    sqlUtils.eq("AREACODE", CertUserSession.getInstance().getAreaCode());
        // 额外查询条件，针对实例，where 语句后面的sql条件
        String extraauthority = "";
        // 当目录版本id不为空或者搜索条件不为空时候展示实例数据
        // 额外的权限控制
        SqlUtils sqlutilextra = new SqlUtils();
        if (StringUtil.isNotBlank(certcatalogid)) {
            sqlutilextra.eq("catalogid", certcatalogid);
        }
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
        PageData<CertInfo> pageData = iCertInfo.getListByPage(sqlUtils.getMap(), 0, 10000,
                "", "", extraauthority, parentcertcatalogid);
        certinfoList = pageData.getList();
        //将多持有者中^符号更换为,显示,无需逻辑判断
        if (ValidateUtil.isNotBlankCollection(certinfoList)) {
            for (CertInfo certinfo : certinfoList) {
                if (StringUtil.isNotBlank(certinfo.getCertownername())) {
                    certinfo.setCertownername(
                            FormatUtil.getdecryptSM4ToData(certinfo.getCertownername(), certcatalogid, 0));
                }
            }
        }


        return certinfoList;
    }
    private void setDefaultStyleOfBox(HSSFCell cell, HSSFFont font) {
        CellUtil.setFont(cell, font);
        CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);

        CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);//垂直居中
        CellUtil.setCellStyleProperty(cell, CellUtil.BORDER_LEFT, BorderStyle.THIN);
        CellUtil.setCellStyleProperty(cell, CellUtil.BORDER_TOP, BorderStyle.THIN);
        CellUtil.setCellStyleProperty(cell, CellUtil.BORDER_RIGHT, BorderStyle.THIN);
        CellUtil.setCellStyleProperty(cell, CellUtil.BORDER_BOTTOM, BorderStyle.THIN);
    }

    public List<SelectItem> getBelongToAreaModel() {
        if (belongToAreaModel == null) {
            belongToAreaModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "所属区县", null, false));
        }
        return this.belongToAreaModel;
    }

    public List<SelectItem> getQlkindModel() {
        if (qlkindModel == null) {
            qlkindModel = new ArrayList<>();
            SelectItem selectItem1 = new SelectItem();
            selectItem1.setValue(1);
            selectItem1.setText("导出当前选择数据");
            qlkindModel.add(selectItem1);
            SelectItem selectItem = new SelectItem();
            selectItem.setValue(2);
            selectItem.setText("导出第一页");
            qlkindModel.add(selectItem);
            SelectItem selectItem2 = new SelectItem();
            selectItem2.setValue(3);
            selectItem2.setText("导出全部");
            qlkindModel.add(selectItem2);
        }
        return this.qlkindModel;
    }
}
