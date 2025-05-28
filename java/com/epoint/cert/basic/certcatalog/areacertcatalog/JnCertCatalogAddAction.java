package com.epoint.cert.basic.certcatalog.areacertcatalog;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.ConstValue9;
import com.epoint.basic.faces.tree.EpointTreeHandler9;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.basic.certcatalog.catalogauthorization.inter.ICertCatalogAuthorization;
import com.epoint.cert.basic.certcatalog.certmetadata.domain.CertMetadata;
import com.epoint.cert.basic.certcatalog.certmetadata.inter.ICertMetaData;
import com.epoint.cert.commonutils.CertConstant;
import com.epoint.cert.commonutils.NoSQLSevice;
import com.epoint.cert.commonutils.SqlUtils;
import com.epoint.cert.commonutils.ValidateUtil;
import com.epoint.common.cert.authentication.*;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.exception.security.ReadOnlyException;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 证照新增页面对应的后台
 *
 * @作者 李然
 * @version [版本号, 2017年10月28日]
 */
@SuppressWarnings("unchecked")
@RestController("jncertcatalogaddaction")
@Scope("request")
public class JnCertCatalogAddAction extends BaseController
{

    private static final long serialVersionUID = -1654394495446583890L;

    /**
     * 证照目录表实体对象
     */
    private CertCatalog dataBean;

    /**
     * 证照所属行业下拉列表model
     */
    private List<SelectItem> industryModel = null;

    /**
     * 证照定义机构代码下拉列表model
     */
    private List<SelectItem> authoritycodeModel = null;

    /**
     * 证照持有人类型下拉列表model
     */
    private List<SelectItem> belongtypeModel = null;

    /**
     * 父证照目录下拉列表model
     */
    private List<SelectItem> parentModel = null;

    /**
     * 证照分类下拉列表model
     */
    private List<SelectItem> kindModel = null;

    /**
     * 证照定义机构级别下拉列表model
     */
    private List<SelectItem> levelModel = null;

    /**
     * 证照颁发机构级别下拉列表model
     */
    private List<SelectItem> issuingLevelModel = null;

    /**
     * 是否需要调用第三方证照接口下拉列表model
     */
    private List<SelectItem> isuseapiModel = null;

    /**
     * 证照附件（正本）上传model
     */
    private FileUploadModel9 certFileUploadModel;

    /**
     * 证照附件（副本）上传model
     */
    private FileUploadModel9 copyCertFileUploadModel;

    /**
     * 套打证照附件（正本）上传model
     */
    private FileUploadModel9 tdcertFileUploadModel;

    /**
     * 套打证照附件（副本）上传model
     */
    private FileUploadModel9 tdcopyCertFileUploadModel;

    /**
     * 证照彩色附件上传model
     */
    private FileUploadModel9 colourFileUploadModel;

    /**
     * 证照灰色附件上传model
     */
    private FileUploadModel9 grayFileUploadModel;

    /**
     * 证照空白底图上传model
     */
    private FileUploadModel9 blankFileUploadModel;

    /**
     * 证照示例底图上传model
     */
    private FileUploadModel9 exampleFileUploadModel;

    /**
     * 证照目录接口
     */
    @Autowired
    private ICertCatalog iCertCatalog;

    /**
     * 元数据API
     */
    @Autowired
    private ICertMetaData iCertMetaData;

    /**
     * 附件API
     */
    @Autowired
    private IAttachService iAttachService;

    /**
     * 系统参数API
     */
    @Autowired
    private IConfigService iConfigService;

    /**
     * 消息api
     */
    @Autowired
    private IMessagesCenterService messagesService;

    /**
     * 角色api
     */
    @Autowired
    private IRoleService roleService;

    /**
     * 人员api
     */
    @Autowired
    private IUserService userService;

    /**
     * 部门api
     */
    @Autowired
    private IOuService ouService;

    /**
     * 代码项api
     */
    @Autowired
    private ICodeItemsService codeItemsService;

    @Autowired
    private ICertCatalogAuthorization certCatalogAuthorizationService;

    /**
     * 代码项bizlogic
     */
    private CodeBizlogic codeBizlogic = new CodeBizlogic();

    /**
     * 操作日志bizlogic
     */
    private CertLogBizlogic certLogBizlogic = new CertLogBizlogic();

    /**
     * 操作附件bizlogic
     */
    private AttachBizlogic attachBizlogic = new AttachBizlogic();

    /**
     * 证照目录bizlogic
     */
    private CertcatalogBizlogic certcatalogBizlogic = new CertcatalogBizlogic();

    /**
     * 配置参数（是否开启目录审核）
     */
    private String configValue;

    /**
     * 持有人类型(用来实现证照分类的联动)
     */
    private String belongType;

    /**
     * 部门树
     */
    private LazyTreeModal9 treeModel;

    private String parentcatalogindustry;

    private String yyyzcertcode;
    /**
     * MongoDB\HBase通用service
     */
    private NoSQLSevice noSQLSevice = new NoSQLSevice();

    @Override
    public void pageLoad() {
        //持证人类型
        belongType = getRequestParameter("belongType");
        // 是否开启目录审核
        configValue = iConfigService.getFrameConfigValue("IsEnableCatalogAudit");
        //目录申请不用审核
        if (CertConstant.CONSTANT_STR_ONE.equals(getRequestParameter("isapply"))) {
            configValue = CertConstant.CONSTANT_STR_ZERO;
        }
        // 证照模板文件
        if (StringUtil.isBlank(getViewData("templetcliengguid"))) {
            addViewData("templetcliengguid", UUID.randomUUID().toString());
        }
        if (StringUtil.isBlank(getViewData("copytempletcliengguid"))) {
            addViewData("copytempletcliengguid", UUID.randomUUID().toString());
        }
        // 套打证照模板文件
        if (StringUtil.isBlank(getViewData("tdtempletcliengguid"))) {
            addViewData("tdtempletcliengguid", UUID.randomUUID().toString());
        }
        if (StringUtil.isBlank(getViewData("tdcopytempletcliengguid"))) {
            addViewData("tdcopytempletcliengguid", UUID.randomUUID().toString());
        }
        // 证照模板文件(是用数维时需要是用)
        if (StringUtil.isBlank(getViewData("templetattachguid"))) {
            addViewData("templetattachguid", UUID.randomUUID().toString());
        }
        if (StringUtil.isBlank(getViewData("copytempletattachguid"))) {
            addViewData("copytempletattachguid", UUID.randomUUID().toString());
        }
        // 封面底图文件
        if (StringUtil.isBlank(getViewData("colourcliengguid"))) {
            addViewData("colourcliengguid", UUID.randomUUID().toString());
        }
        if (StringUtil.isBlank(getViewData("graycliengguid"))) {
            addViewData("graycliengguid", UUID.randomUUID().toString());
        }

        // 底图文件
        if (StringUtil.isBlank(getViewData("blankcliengguid"))) {
            addViewData("blankcliengguid", UUID.randomUUID().toString());
        }
        if (StringUtil.isBlank(getViewData("examplecliengguid"))) {
            addViewData("examplecliengguid", UUID.randomUUID().toString());
        }

        //所属行业
        String industry = "";
        String parentid = getRequestParameter("parentid");
        if (StringUtil.isNotBlank(parentid)) {
            SqlUtils sqlUtils = new SqlUtils();
            sqlUtils.eq("ishistory", CertConstant.CONSTANT_STR_ZERO);
            sqlUtils.eq("certcatalogid", parentid);
            List<CertCatalog> certCataloglist = iCertCatalog.getListByCondition(sqlUtils.getMap());
            if (ValidateUtil.isNotBlankCollection(certCataloglist)) {
                CertCatalog certCatalog = certCataloglist.get(0);
                if (ValidateUtil.isNotNull(certCatalog)) {
                    parentcatalogindustry = certCatalog.getIndustry();
                }
            }
        }
        dataBean = new CertCatalog();
        if (!isPostback()) {
            if (StringUtil.isBlank(parentid)) {
                //parentid为空，则是新增父目录
                industry = getRequestParameter("industry");
                dataBean.setIsparent(CertConstant.CONSTANT_INT_ONE);
                dataBean.setParentid("");
                // 初始化证照持有人类型
                initAdd();
            }
            else {
                //parentid不为为空，则是新增子目录
                SqlUtils sqlUtils = new SqlUtils();
                sqlUtils.eq("ishistory", CertConstant.CONSTANT_STR_ZERO);
                sqlUtils.eq("certcatalogid", parentid);
                List<CertCatalog> certCataloglist = iCertCatalog.getListByCondition(sqlUtils.getMap());
                if (ValidateUtil.isNotBlankCollection(certCataloglist)) {
                    CertCatalog catalog = certCataloglist.get(0);
                    dataBean.setIsparent(CertConstant.CONSTANT_INT_ZERO);
                    if (ValidateUtil.isNotNull(catalog)) {
                        dataBean.setParentid(parentid);
                        industry = catalog.getIndustry();
                        belongType = catalog.getBelongtype();
                        dataBean.setBelongtype(belongType);
                        dataBean.setKind(catalog.getKind());
                        addCallbackParam("kind", catalog.getKind());
                        dataBean.setCertwidth(catalog.getCertwidth());
                        dataBean.setCertheight(catalog.getCertheight());
                    }
                    else {
                        initAdd();
                    }
                }
            }
            if (StringUtil.isNotBlank(industry)) {
                dataBean.setIndustry(industry);
            }
            //新增元数据移入新增页面中，需要在页面初始化的时候就设置目录的rowguid用作与元数据关联
            String rowguid = UUID.randomUUID().toString();
            addViewData("rowguid", rowguid);
            addCallbackParam("rowguid", rowguid);
            if (StringUtil.isBlank(dataBean.getIsuploadlevelccert())) {
                dataBean.setIsuploadlevelccert(CertConstant.CONSTANT_INT_ZERO);
            }
            if (StringUtil.isBlank(dataBean.getIsoneCertInfo())) {
                dataBean.setIsoneCertInfo(CertConstant.CONSTANT_INT_ZERO);
            }
            if (StringUtil.isBlank(dataBean.getIsmultiowners())) {
                dataBean.setIsmultiowners(CertConstant.CONSTANT_INT_ZERO);
            }
            if (StringUtil.isBlank(dataBean.getIsstandardcert())) {
                dataBean.setIsstandardcert(CertConstant.CONSTANT_INT_ZERO);
            }
            // 设置排序号
            dataBean.setOrdernum(CertConstant.CONSTANT_INT_ZERO);
            // 证照横向尺寸 默认0
            dataBean.setCertwidth(CertConstant.CONSTANT_INT_ZERO);
            // 证照纵向尺寸 默认0
            dataBean.setCertheight(CertConstant.CONSTANT_INT_ZERO);
        }
        else {
            dataBean.setRowguid(getViewData("rowguid"));
        }
        // 回传模板文件（正本）
        addCallbackParam("templetcliengguid", getViewData("templetcliengguid"));
        // 回传模板文件（副本）
        addCallbackParam("copytempletcliengguid", getViewData("copytempletcliengguid"));
        // 回传套打模板文件（正本）
        addCallbackParam("tdtempletcliengguid", getViewData("tdtempletcliengguid"));
        // 回传套打模板文件（副本）
        addCallbackParam("tdcopytempletcliengguid", getViewData("tdcopytempletcliengguid"));
        if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("ofdserviceurl"))) {
            addCallbackParam("suwell", CertConstant.CONSTANT_STR_ONE);
        }
        // 回传模板文件（正本）
        addCallbackParam("templetattachguid", getViewData("templetattachguid"));
        // 回传模板文件（副本）
        addCallbackParam("copytempletattachguid", getViewData("copytempletattachguid"));
        addCallbackParam("ofd", ConfigUtil.getConfigValue("ofdserviceurl"));
        addCallbackParam("ofdpreviewurl", ConfigUtil.getConfigValue("ofdpreviewurl"));
        addCallbackParam("officeweb365url", new ConfigBizlogic().getOfficeConfig());
    }

    /**
     * 归档
     */
    public void add() {
        if (dataBean == null || dataBean.isEmpty()) {
            return;
        }
        if (StringUtil.isNotBlank(parentcatalogindustry) && !parentcatalogindustry.equals(dataBean.getIndustry())) {
            throw new ReadOnlyException("industry");
        }
        // 证照目录编号是否重复
        int count = iCertCatalog.getCertCatalogCountByTyCertCatcode(dataBean.getTycertcatcode(), null);
        if (count >= 1) {
            addCallbackParam("msg", "证照类型地方代码重复!");
            addCallbackParam("isClose", "0");
            return;
        }

        //国标，需要验证是否重复
        if (CertConstant.CONSTANT_INT_ONE.equals(dataBean.getIsstandardcert())) {
            SqlUtils sqlUtils = new SqlUtils();
            sqlUtils.eq("certificatetypecode", dataBean.getCertificatetypecode());
            sqlUtils.eq("materialtype", "1");
            sqlUtils.nq("certcatalogid", dataBean.getCertcatalogid());
            if (iCertCatalog.getCertCatalogCount(sqlUtils.getMap()) > 0) {
                addCallbackParam("msg", "证照类型代码重复!");
                addCallbackParam("isClose", "0");
                return;
            }
        }

        String msg = checkMetaDataConfig(dataBean);
        if (StringUtil.isNotBlank(msg)) {
            addCallbackParam("nosub", msg);
            return;
        }
        saveOrChange(false);

        if (StringUtil.isNotBlank(dataBean.getParentid())) {
            certCatalogAuthorizationService.deleteCatalogAuthorizationBycatalogid(dataBean.getParentid());
        }
        // 是否开启目录审核
        if (CertConstant.CONSTANT_STR_ONE.equals(configValue)) { // 启用目录审核
            //增加操作日志（新增待上报）
            certLogBizlogic.addInternalLog(userSession.getUserGuid(), userSession.getDisplayName(), dataBean,
                    CertConstant.LOG_OPERATETYPE_ADD_REPORT, "", null);
            FrameRole frameRole = roleService.getRoleByRoleField("rolename", "目录审核员");
            if (frameRole != null) {
                String roleguid = frameRole.getRoleGuid();
                // 2、获取该角色的对应的人员
                List<FrameUser> listUser = userService.listUserByOuGuid("", roleguid, "", "", false, true, false, 3);
                if (listUser != null && listUser.size() > 0) {
                    // 3、发送待办给审核人员
                    for (FrameUser frameUser : listUser) {
                        String title = "【证照类型审核】" + dataBean.getCertname();
                        // 处理页面
                        String handleurl = "epointcert/certcatalog/areacertcatalog/catalogaudit/certcatalogaudit?guid="
                                + dataBean.getRowguid();
                        String messageItemGuid = UUID.randomUUID().toString();
                        messagesService.insertWaitHandleMessage(messageItemGuid, title,
                                IMessagesCenterService.MESSAGETYPE_WAIT, frameUser.getUserGuid(),
                                frameUser.getDisplayName(), userSession.getUserGuid(), userSession.getDisplayName(),
                                "证照类型审核", handleurl, frameUser.getOuGuid(), "", CertConstant.CONSTANT_INT_ONE, "", "",
                                dataBean.getRowguid(), "", new Date(), "", frameUser.getUserGuid(), "", "");
                    }
                }
            }
            addCallbackParam("msg", "上报成功!");
        }
        else {
            if (StringUtil.isNotBlank(dataBean.getParentid())) {
                CertCatalog catalog = iCertCatalog.getLatestCatalogDetailByEnale(dataBean.getParentid(),
                        CertConstant.CONSTANT_INT_ZERO);
                if (catalog != null && CertConstant.CONSTANT_INT_ZERO.equals(catalog.getIsenable())) {
                    catalog.setIsenable(CertConstant.CONSTANT_INT_ONE);
                    iCertCatalog.updateCatalog(catalog);
                }
            }
            //增加操作日志（新增）
            certLogBizlogic.addInternalLog(userSession.getUserGuid(), userSession.getDisplayName(), dataBean,
                    CertConstant.LOG_OPERATETYPE_ADD, "", null);
            addCallbackParam("msg", "新增成功!");
        }
        addCallbackParam("isClose", "1");
    }

    /**
     *  保存草稿
     */
    public void saveDraft() {
        if (dataBean == null || dataBean.isEmpty()) {
            return;
        }
        if (StringUtil.isNotBlank(parentcatalogindustry) && !parentcatalogindustry.equals(dataBean.getIndustry())) {
            throw new ReadOnlyException("industry");
        }
        // 证照目录编号是否重复
        int count = iCertCatalog.getCertCatalogCountByTyCertCatcode(dataBean.getTycertcatcode(), null);
        if (count >= 1) {
            addCallbackParam("msg", "证照类型地方代码重复!");
            addCallbackParam("isClose", "0");
            return;
        }
        //国标，需要验证是否重复
        if (CertConstant.CONSTANT_INT_ONE.equals(dataBean.getIsstandardcert())) {
            SqlUtils sqlUtils = new SqlUtils();
            sqlUtils.eq("certificatetypecode", dataBean.getCertificatetypecode());
            sqlUtils.eq("materialtype", "1");
            sqlUtils.nq("certcatalogid", dataBean.getCertcatalogid());
            if (iCertCatalog.getCertCatalogCount(sqlUtils.getMap()) > 0) {
                addCallbackParam("msg", "证照类型代码重复!");
                addCallbackParam("isClose", "0");
                return;
            }
        }
        saveOrChange(true);
        addCallbackParam("msg", "保存成功!");
        addCallbackParam("isClose", "1");
    }

    /**
     * 直接关闭按钮,删除已配置的元数据
     * @param 页面初始化时的rowguid
     */
    public void closeDialog(String rowGuid) {
        if (StringUtil.isNotBlank(rowGuid)) {
            //设删除元数据
            SqlUtils sql = new SqlUtils();
            sql.eq("CERTGUID", rowGuid);
            List<CertMetadata> certMetadatas = iCertMetaData.selectListByCondition(sql.getMap());
            if (ValidateUtil.isNotBlankCollection(certMetadatas)) {
                for (CertMetadata certMetadata : certMetadatas) {
                    iCertMetaData.deleteMetaData(certMetadata);
                }
            }
            // 删除附件
            iAttachService.deleteAttachByGuid(getViewData("templetcliengguid"));
            iAttachService.deleteAttachByGuid(getViewData("copytempletcliengguid"));
            iAttachService.deleteAttachByGuid(getViewData("tdtempletcliengguid"));
            iAttachService.deleteAttachByGuid(getViewData("tdcopytempletcliengguid"));
            iAttachService.deleteAttachByGuid(getViewData("templetattachguid"));
            iAttachService.deleteAttachByGuid(getViewData("copytempletattachguid"));
            iAttachService.deleteAttachByGuid(getViewData("colourcliengguid"));
            iAttachService.deleteAttachByGuid(getViewData("graycliengguid"));
            iAttachService.deleteAttachByGuid(getViewData("blankcliengguid"));
            iAttachService.deleteAttachByGuid(getViewData("examplecliengguid"));

            // 删除演示拓展信息
            certcatalogBizlogic.deleteDemoExtension(rowGuid);
        }
    }

    /**
     *  保存草稿或归档
     */
    private void saveOrChange(boolean isDraft) {
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setOperatedate(new Date());
        dataBean.setUserguid(userSession.getUserGuid());

        if (CertConstant.CONSTANT_STR_ONE.equals(configValue)) { // 启用目录审核
            //设置为草稿状态
            dataBean.setIshistory(CertConstant.CONSTANT_INT_NEGATIVE_ONE);
            if (isDraft) {
                // 审核状态为待上报
                dataBean.setAuditstatus(CertConstant.CERT_CHECK_STATUS_WAIT_REPORT);
            }
            else {
                // 审核状态为待审核
                dataBean.setAuditstatus(CertConstant.CERT_CHECK_STATUS_WAIT_CHECK);
            }
        }
        else { // 不启用目录审核
            if (isDraft) {
                // 审核状态置为待上报
                dataBean.setAuditstatus(CertConstant.CERT_CHECK_STATUS_WAIT_REPORT);
                //设置为草稿状态
                dataBean.setIshistory(CertConstant.CONSTANT_INT_NEGATIVE_ONE);
            }
            else {
                // 审核状态置为审核通过
                dataBean.setAuditstatus(CertConstant.CERT_CHECK_STATUS_PASS);
                dataBean.setIshistory(CertConstant.CONSTANT_INT_ZERO);
                // 归档，删除演示拓展信息
                certcatalogBizlogic.deleteDemoExtension(dataBean.getRowguid());
            }
        }
        dataBean.setTempletcliengguid(getViewData("templetcliengguid"));
        dataBean.setCopytempletcliengguid(getViewData("copytempletcliengguid"));
        dataBean.setTdTempletcliengguid(getViewData("tdtempletcliengguid"));
        dataBean.setTdCopytempletcliengguid(getViewData("tdcopytempletcliengguid"));
        dataBean.setColourcliengguid(getViewData("colourcliengguid"));
        dataBean.setGraycliengguid(getViewData("graycliengguid"));
        dataBean.setBlankcliengguid(getViewData("blankcliengguid"));
        dataBean.setExamplecliengguid(getViewData("examplecliengguid"));
        // 自动生成证照目录编码
        dataBean.setCertcatcode(certcatalogBizlogic.generateCertcatcode(dataBean));
        //证照定义机构
        dataBean.setCertificatedefineauthorityname(
                codeItemsService.getItemTextByCodeName("证照定义机构", dataBean.getCertificatedefineauthoritycode()));
        if (CertConstant.CONSTANT_INT_ZERO.equals(dataBean.getIsstandardcert()) && !isDraft) {
            // 不是国标证照，且不是草稿 需要自动生成证照类型代码
            dataBean.setCertificatetypecode(
                    iCertCatalog.generateCertTyCertcatcode(dataBean.getCertificatedefineauthoritycode()));
            //由于可以手填，需要验证唯一
            SqlUtils sqlUtils = new SqlUtils();
            sqlUtils.eq("certificatetypecode", dataBean.getCertificatetypecode());
            sqlUtils.eq("materialtype", "1");
            sqlUtils.nq("certcatalogid", dataBean.getCertcatalogid());
            //如果重复，需要再次生成直到唯一
            while (iCertCatalog.getCertCatalogCount(sqlUtils.getMap()) > 0) {
                dataBean.setCertificatetypecode(
                        iCertCatalog.generateCertTyCertcatcode(dataBean.getCertificatedefineauthoritycode()));
                sqlUtils.eq("certificatetypecode", dataBean.getCertificatetypecode());
            }
        }

        // 版本 从1开始
        dataBean.setVersion(CertConstant.CONSTANT_INT_ONE);
        //设置证照目录唯一版本标识
        dataBean.setCertcatalogid(UUID.randomUUID().toString());
        // 设置类型为证照
        dataBean.setMaterialtype(CertConstant.CONSTANT_STR_ONE);
        // 默认启用
        dataBean.setIsenable(CertConstant.CONSTANT_INT_ONE);
        // 默认待发布
        dataBean.setIspublish(CertConstant.CONSTANT_INT_ZERO);
        // 排序号（默认为0）
        if (dataBean.getOrdernum() == null) {
            dataBean.setOrdernum(CertConstant.CONSTANT_INT_ZERO);
        }

        iCertCatalog.addCertCatalog(dataBean);
    }

    /**
     *  检测元数据配置 （元数据预览时）
     *  @param certCatalog
     *  @param isReviewMetadata 是否预览元数据
     *  @return 没有错误信息返回null
     */
    public void checkMetaDataConfig(String catalogguid) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("catalogguid", catalogguid);
        Record extension = noSQLSevice.find(CertConstant.SQL_TABLE_DEMO_EXTENSION, filter, false);
        boolean isAdd = false;
        if (extension == null) {
            isAdd = true;
        }
        addCallbackParam("isAdd", isAdd);
        // 检测元数据配置(中英文是否重复，只检测顶层的)
        Set<String> chineseNameSet = new HashSet<>();
        Set<String> englishNameSet = new HashSet<>();
        List<CertMetadata> metadataList = iCertMetaData.selectTopDispinListByCertguid(catalogguid);
        if (ValidateUtil.isBlankCollection(metadataList)) {
            addCallbackParam("msg", "请先配置业务信息！");
            return;
        }
        for (CertMetadata metadata : metadataList) {
            chineseNameSet.add(metadata.getFieldchinesename());
            englishNameSet.add(metadata.getFieldname());
            if (CertConstant.CONSTANT_STR_ONE.equals(metadata.getIsrelatesubtable())) {
                if (iCertMetaData.selectSubDispinListByCertguid(metadata.getRowguid()).size() == 0) {
                    addCallbackParam("msg", "业务信息存在未配置子表，请先完善！");
                    return;
                }
            }
        }
        if (chineseNameSet.size() != metadataList.size() || englishNameSet.size() != metadataList.size()) {
            addCallbackParam("msg", "业务信息配置有误，请先完善！");
            return;
        }
    }

    /**
     *  检测元数据配置（归档时）
     *  @param certCatalog
     *  @return 没有错误信息返回null
     */
    private String checkMetaDataConfig(CertCatalog certCatalog) {
        // 检测元数据配置(中英文是否重复，只检测顶层的)
        Set<String> chineseNameSet = new HashSet<>();
        Set<String> englishNameSet = new HashSet<>();
        List<CertMetadata> metadataList = iCertMetaData.selectTopDispinListByCertguid(certCatalog.getRowguid());
        if (ValidateUtil.isNotBlankCollection(metadataList)) {
            for (CertMetadata metadata : metadataList) {
                chineseNameSet.add(metadata.getFieldchinesename());
                englishNameSet.add(metadata.getFieldname());
                if (CertConstant.CONSTANT_STR_ONE.equals(metadata.getIsrelatesubtable())) {
                    if (iCertMetaData.selectSubDispinListByCertguid(metadata.getRowguid()).size() == 0) {
                        return "业务信息存在未配置子表，请完善！";
                    }
                }
            }
            if (chineseNameSet.size() != metadataList.size() || englishNameSet.size() != metadataList.size()) {
                return "业务信息配置有误，请完善后归档！";
            }
        }
        return null;
    }

    /**
    * 初始化证照持有人类型及对应的证照分类
    */
    public void initAdd() {
        List<SelectItem> belongtypeModel = getBelongtypeModel();
        if (ValidateUtil.isNotBlankCollection(belongtypeModel)) {
            belongType = (String) belongtypeModel.get(0).getValue();
            dataBean.setBelongtype(belongType);
        }
    }

    public void checkOrgcode(String ouguid) {
        boolean hasOrgcode = false;
        FrameOuExtendInfo ouExtendInfo = ouService.getFrameOuExtendInfo(ouguid);
        if (ouExtendInfo != null && StringUtil.isNotBlank(ouExtendInfo.getStr("orgcode"))) {
            hasOrgcode = true;
        }
        addCallbackParam("hasOrgcode", hasOrgcode);
    }

    public TreeModel getTreeModel() {
        if (treeModel == null) {
            int tableName = ConstValue9.FRAMEOU;
            treeModel = new LazyTreeModal9(new EpointTreeHandler9(tableName));
            treeModel.setRootName("所有部门");
        }
        return treeModel;
    }

    public List<SelectItem> getIndustryModel() {
        if (industryModel == null) {
            industryModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "证照所属行业", null, false));
        }
        return this.industryModel;
    }

    public List<SelectItem> getAuthoritycodeModel() {
        if (authoritycodeModel == null) {
            authoritycodeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "证照定义机构", null, false));
        }
        return this.authoritycodeModel;
    }

    public List<SelectItem> getBelongtypeModel() {
        if (belongtypeModel == null) {
            belongtypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "证照持有人类型", null, false));
        }
        return this.belongtypeModel;
    }

    public List<SelectItem> getParentModel() {
        if (parentModel == null) {
            parentModel = new ArrayList<>();
            if (StringUtil.isNotBlank(dataBean.getIndustry())) {
                List<CertCatalog> certCatalogs = iCertCatalog.selectParentCatalog("certname,certcatalogid",
                        dataBean.getIndustry());
                if (ValidateUtil.isBlankCollection(certCatalogs)) {
                    return new ArrayList<>();
                }
                else {
                    for (CertCatalog certCatalog : certCatalogs) {
                        parentModel.add(new SelectItem(certCatalog.getCertcatalogid(), certCatalog.getCertname()));
                    }
                }
            }
        }
        return this.parentModel;
    }

    public List<SelectItem> getKindModel() {
        if (kindModel == null) {
            kindModel = new ArrayList<>();
            if ("001".equals(dataBean.getBelongtype()) || "002".equals(dataBean.getBelongtype())) {
                kindModel = codeBizlogic.getSelectedItemsListByPrefix("证照分类", dataBean.getBelongtype(), false);
            }
            else {
                kindModel = codeBizlogic.getSelectedItemsListByPrefix("证照分类", "003", false);
            }
        }
        return this.kindModel;
    }

    public List<SelectItem> getLevelModel() {
        if (levelModel == null) {
            levelModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "机构级别", null, false));
        }
        return this.levelModel;
    }

    public List<SelectItem> getIssuingLevelModel() {
        if (issuingLevelModel == null) {
            issuingLevelModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "机构级别", null, false));
        }
        return this.issuingLevelModel;
    }

    public List<SelectItem> getIsuseapiModel() {
        if (isuseapiModel == null) {
            isuseapiModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
        }
        return this.isuseapiModel;
    }

    public FileUploadModel9 getCertFileUploadModel() {
        if (certFileUploadModel == null) {
            certFileUploadModel = attachBizlogic.miniOaUploadAttach(getViewData("templetcliengguid"), "证照模板文件");
        }
        return certFileUploadModel;
    }

    public FileUploadModel9 getCopyCertFileUploadModel() {
        if (copyCertFileUploadModel == null) {
            copyCertFileUploadModel = attachBizlogic.miniOaUploadAttach(getViewData("copytempletcliengguid"),
                    "证照模板文件（副本）");
        }
        return copyCertFileUploadModel;
    }

    public FileUploadModel9 getTdCertFileUploadModel() {
        if (tdcertFileUploadModel == null) {
            tdcertFileUploadModel = attachBizlogic.miniOaUploadAttach(getViewData("tdtempletcliengguid"), "套打证照模板文件");
        }
        return tdcertFileUploadModel;
    }

    public FileUploadModel9 getTdCopyCertFileUploadModel() {
        if (tdcopyCertFileUploadModel == null) {
            tdcopyCertFileUploadModel = attachBizlogic.miniOaUploadAttach(getViewData("tdcopytempletcliengguid"),
                    "套打证照模板文件（副本）");
        }
        return tdcopyCertFileUploadModel;
    }

    /**
     *  彩色封面底图model
     *  @return
     */
    public FileUploadModel9 getColourFileUploadModel() {
        if (colourFileUploadModel == null) {
            colourFileUploadModel = attachBizlogic.miniOaUploadAttach(getViewData("colourcliengguid"), "彩色封面底图");
        }
        return colourFileUploadModel;
    }

    /**
     *  灰色封面底图model
     *  @return
     */
    public FileUploadModel9 getGrayFileUploadModel() {
        if (grayFileUploadModel == null) {
            grayFileUploadModel = attachBizlogic.miniOaUploadAttach(getViewData("graycliengguid"), "灰色封面底图");
        }
        return grayFileUploadModel;
    }

    /**
     *  空白底图model
     *  @return
     */
    public FileUploadModel9 getBlankFileUploadModel() {
        if (blankFileUploadModel == null) {
            blankFileUploadModel = attachBizlogic.miniOaUploadAttach(getViewData("blankcliengguid"), "空白底图");
        }
        return blankFileUploadModel;
    }

    /**
     *  示例底图model
     *  @return
     */
    public FileUploadModel9 getExampleFileUploadModel() {
        if (exampleFileUploadModel == null) {
            exampleFileUploadModel = attachBizlogic.miniOaUploadAttach(getViewData("examplecliengguid"), "示例底图");
        }
        return exampleFileUploadModel;
    }

    public CertCatalog getDataBean() {
        if (dataBean == null) {
            dataBean = new CertCatalog();
        }
        return dataBean;
    }

    public void setDataBean(CertCatalog dataBean) {
        this.dataBean = dataBean;
    }

	public String getYyyzcertcode() {
		return yyyzcertcode;
	}

	public void setYyyzcertcode(String yyyzcertcode) {
		this.yyyzcertcode = yyyzcertcode;
	}
    
    
}
