
package com.epoint.auditresource.cert.action;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Bookmark;
import com.aspose.words.BookmarkCollection;
import com.aspose.words.DataSet;
import com.aspose.words.DataTable;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.FontSettings;
import com.aspose.words.HeaderFooter;
import com.aspose.words.HeaderFooterType;
import com.aspose.words.HorizontalAlignment;
import com.aspose.words.License;
import com.aspose.words.Paragraph;
import com.aspose.words.RelativeHorizontalPosition;
import com.aspose.words.RelativeVerticalPosition;
import com.aspose.words.SaveFormat;
import com.aspose.words.Section;
import com.aspose.words.SectionCollection;
import com.aspose.words.Shape;
import com.aspose.words.ShapeType;
import com.aspose.words.TextPath;
import com.aspose.words.VerticalAlignment;
import com.aspose.words.WrapType;
import com.epoint.auditresource.cert.bizcommon.GenerateCertCommon;
import com.epoint.auditresource.cert.bizcommon.JnGenerateCertCommon;
import com.epoint.auditresource.cert.bizcommon.WsxGenerateCertCommon;
import com.epoint.auditresource.cert.dzbd.Dzbd4DzzzUtil;
import com.epoint.auditresource.cert.util.GenerateNumberUtil;
import com.epoint.auditresource.cert.util.YQCertInfoUtil;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowUser;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.impl.JNAuditProjectService;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IJNAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditresource.auditcertcatalog.domain.AuditCertCatalog;
import com.epoint.basic.auditresource.auditcertcatalog.inter.IAuditCertCatalog;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspsharematerialrelation.domain.AuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditspsharematerialrelation.inter.IAuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditspspgcjsxk.domain.AuditSpSpGcjsxk;
import com.epoint.basic.auditsp.auditspspgcjsxk.inter.IAuditSpSpGcjsxkService;
import com.epoint.basic.auditsp.auditspspjgys.domain.AuditSpSpJgys;
import com.epoint.basic.auditsp.auditspspjgys.inter.IAuditSpSpJgysService;
import com.epoint.basic.auditsp.auditspsplxydghxk.domain.AuditSpSpLxydghxk;
import com.epoint.basic.auditsp.auditspsplxydghxk.inter.IAuditSpSpLxydghxkService;
import com.epoint.basic.auditsp.auditspspsgxk.domain.AuditSpSpSgxk;
import com.epoint.basic.auditsp.auditspspsgxk.inter.IAuditSpSpSgxkService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.cert.api.IJnCertInfo;
import com.epoint.cert.auditcertrelation.api.IAuditCertRelationService;
import com.epoint.cert.auditcertrelation.api.entity.AuditCertRelation;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.basic.certcatalog.certcatalogou.domain.CertCatalogOu;
import com.epoint.cert.basic.certcatalog.certcatalogou.inter.ICertCatalogOu;
import com.epoint.cert.basic.certcatalog.certmetadata.domain.CertMetadata;
import com.epoint.cert.basic.certcatalog.certmetadata.inter.ICertMetaData;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.certinfosubextension.domain.CertInfoSubExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.cert.commonservice.DBServcie;
import com.epoint.cert.commonutils.CertConstant;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.cert.external.ICertConfigExternal;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.cert.authentication.GenerateBizlogic;
import com.epoint.common.util.AttachUtil;
import com.epoint.common.util.GenerateUtil;
import com.epoint.common.util.QrcodeUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.message.api.IOnlineMessageInfoService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.api.IOuServiceInternal;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.util.JnConstant;
import com.epoint.xmz.certggxkws.api.ICertGgxkwsService;
import com.epoint.xmz.certggxkws.api.entity.CertGgxkws;
import com.epoint.xmz.certhwslysjyxk.api.ICertHwslysjyxkService;
import com.epoint.xmz.certhwslysjyxk.api.entity.CertHwslysjyxk;
import com.epoint.xmz.cxbus.api.ICxBusService;
import com.epoint.xmz.cxbus.api.entity.CxBus;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglJsgcjgysbaxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglJsgcjgysbaxxbV3Service;
import com.epoint.xmz.util.HttpSignTest;
import com.epoint.zczwfw.zccommon.api.IZcCommonService;

/**
 * 证照基本信息表新增页面对应的后台
 *
 * @author dingwei
 * @version [版本号, 2017-11-01 16:01:53]
 */
@RestController("yqcertaddaction")
@Scope("request")
public class YQCertAddAction extends BaseController {

    private static final long serialVersionUID = 1L;

    public final static char[] upper = "零一二三四五六七八九十".toCharArray();

    public static final String zwdturl = ConfigUtil.getConfigValue("zwdtparam", "zwdturl") + "/rest/jncxcltxzdj/";

    /**
     * 证照基本信息表实体对象
     */
    private CertInfo certinfo;

    /**
     * 元数据列表
     */
    private List<CertMetadata> metadataList;

    /**
     * 证照目录
     */
    private CertCatalog certCatalog;

    /**
     * 证照照面信息表实体对象
     */
    private CertInfoExtension dataBean = new CertInfoExtension();

    /**
     * 可信等级下拉列表model
     */
    private List<SelectItem> certLevelModel;

    /**
     * 持证人证件类型下拉列表model
     */
    private List<SelectItem> certOwnerCertTypeModel;

    /**
     * 证照（正本）附件上传
     */
    private FileUploadModel9 certFileUploadModel;

    /**
     * 证照（副本）附件上传
     */
    private FileUploadModel9 certCopyFileUploadModel;

    /**
     * 附件附图上传
     */
    private FileUploadModel9 ftfjUploadModel;

    @Autowired
    private ICertConfigExternal iCertConfigExternal;

    @Autowired
    private ICxBusService iCxBusService;

    @Autowired
    private ICertGgxkwsService iCertGgxkwsService;

    /**
     * 证照api
     */
    @Autowired
    private ICertInfoExternal iCertInfoExternal;

    @Autowired
    private IJNAuditProject iJNAuditProject;

    @Autowired
    private IParticipantsInfoService iParticipantsInfo;

    @Autowired
    private IAuditSpSpSgxkService iAuditSpSpSgxkService;

    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;

    @Autowired

    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;

    @Autowired
    private IAuditSpITask auditSpITaskService;

    /**
     * 打印证照目录api
     */
    @Autowired
    private IAuditCertCatalog iAuditCertCatalog;

    /**
     * 证照附件api
     */
    @Autowired
    private ICertAttachExternal iCertAttachExternal;

    @Autowired
    private IHandleConfig handleConfigService;

    /**
     * 代码项接口
     */
    @Autowired
    private ICodeItemsService codeItemsService;

    /**
     * 事项api
     */
    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private ICertInfo certinfoservice;

    @Autowired
    private ICertCatalog catalogService;

    @Autowired
    private IAuditSpSpLxydghxkService iAuditSpSpLxydghxkService;

    @Autowired
    private ICodeItemsService iCodeItemsService;

    private AuditProject auditProject;

    @Autowired
    private IAuditProject auditProjectService;
    @Autowired
    private IOuServiceInternal iOuService;
    @Autowired
    private IAuditSpSpGcjsxkService iAuditSpSpGcjsxkService;

    @Autowired
    private IAuditTaskResult auditTaskResultService;

    @Autowired
    private IHandleProject handleProjectService;

    @Autowired
    private IHandleSPIMaterial handleSpiMaterialService;

    @Autowired
    private IAuditSpShareMaterialRelation relationService;

    /**
     * 附件操作服务类
     */
    @Autowired
    private IAttachService attachService;
    @Autowired
    private ICertCatalogOu catalogOuService;

    @Autowired
    private IAuditProjectMaterial projectMaterialService;

    @Autowired
    private IOnlineMessageInfoService iOnlineMessageInfoService;

    @Autowired
    private IAuditOrgaWindow auditOrgaWindowService;

    @Autowired
    private IAuditSpSpJgysService iAuditSpSpJgysService;

    @Autowired
    private IOuService ouService;

    @Autowired
    private ICertHwslysjyxkService iCertHwslysjyxkService;



    /**
     * 目录证照模板（副本）文件数量
     */
    private int copyTempletCount = 0;

    /**
     * 证照目录的版本唯一标识(左侧树选择)
     */
    private String certcatalogid;

    private String certrowguid;

    private String certownertype;

    /**
     * 照面信息附件上传map
     */
    private Map<String, FileUploadModel9> modelMap = new HashMap<>();

    /**
     * 照面信息主键
     */
    private String extendguid;

    /**
     * 基本信息和照面信息关联字段
     */
    private Map<String, String> certFilelds;

    private String areacode = "";

    String requestUrl = "";

    /**
     * 多持有人信息
     */
    private String certownerinfo;

    private String certno;

    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;

    @Autowired
    private IJnCertInfo iJnCertInfo;

    @Autowired
    private ICertInfo certInfoService;

    @Autowired
    private IAuditCertRelationService iAuditCertRelationService;

    @Autowired
    private IZcCommonService iZcCommonService;
    
    @Autowired
    private ISpglJsgcjgysbaxxbV3Service iSpglJsgcjgysbaxxbV3Service;
    

    @Override
    public void pageLoad() {
        boolean warn = false;
        certinfo = new CertInfo();
        certcatalogid = getRequestParameter("guid");
        requestUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();

        certrowguid = getRequestParameter("certrowguid");
        certownertype = getRequestParameter("certownertype");

        if (StringUtil.isBlank(certno) && StringUtil.isBlank(getViewData("certno"))) {
            certno = getProjectno();
            addViewData("certno", certno);
        } else {
            certno = getViewData("certno");
        }

        addCallbackParam("certno", certno);

        // 省电子证照pdf签章
        String isUseSginurl = ConfigUtil.getConfigValue("isUseSginurl");
        addCallbackParam("isUseSginurl", isUseSginurl);

        if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(certownertype))) {
            certownertype = ZwfwConstant.CERTOWNERTYPE_ZRR;
        } else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(certownertype))) {
            certownertype = ZwfwConstant.CERTOWNERTYPE_FR;
        }
        //当前办件的电子表单guid
        String dzbdguid = "";
        if (StringUtil.isNotBlank(getRequestParameter("projectguid"))) {
            if (ZwfwUserSession.getInstance().getCitylevel() != null
                    && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                    .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
                areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
            } else {
                areacode = ZwfwUserSession.getInstance().getAreaCode();
            }
            auditProject = auditProjectService.getAuditProjectByRowGuid(getRequestParameter("projectguid"), areacode)
                    .getResult();
            dzbdguid = auditProject.getRowguid();
            if(StringUtil.isNotBlank(auditProject.getBiguid())){
                AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(auditProject.getBusinessguid()).getResult();
                if(auditSpBusiness!=null && StringUtil.isNotBlank(auditSpBusiness.getStr("yjsformid"))){
                    dzbdguid = auditProject.getSubappguid();
                }
            }

            if (StringUtils.isBlank(certrowguid) && StringUtil.isNotBlank(auditProject.getCertrowguid())) {
                String[] certrowguids = auditProject.getCertrowguid().split(";");
                for (String certguid : certrowguids) {
                    CertInfo info = certinfoservice.getCertInfoByRowguid(certguid);
                    if (info!=null && certcatalogid.equals(info.getCertcatalogid())) {
                        certrowguid = certguid;
                    }
                }
            }
            //安许证事项展示特殊按钮
            AuditTask task = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();
            if ("11370800MB28559184300011711200001".equals(task.getItem_id()) || "11370800MB285591843370117002013".equals(task.getItem_id()) ||
                    "11370800MB285591843370117002015".equals(task.getItem_id()) || "11370800MB28559184300011711200002".equals(task.getItem_id()) ||
                    "11370800MB28559184300011711200005".equals(task.getItem_id()) || "11370800MB285591843370117002014".equals(task.getItem_id()) ||
                    "11370800MB285591843370117002016".equals(task.getItem_id()) || "11370800MB28559184300011711200003".equals(task.getItem_id()) ||
                    "11370800MB285591843370117002017".equals(task.getItem_id()) || "11370800MB285591843370117002026".equals(task.getItem_id()) ||
                    "11370800MB285591843370117002024".equals(task.getItem_id()) || "11370800MB285591843370117002011".equals(task.getItem_id()) ||
                    "11370800MB285591843370117002025".equals(task.getItem_id()) || "11370800MB285591843370117002027".equals(task.getItem_id()) ||
                    "11370800MB28559184300011711200004".equals(task.getItem_id())) {
                addCallbackParam("flag", "1");
            }

        }
        log.info("certrowguid=" + certrowguid);
        if (StringUtil.isNotBlank(certrowguid)) {
            addViewData("rowguid", certrowguid);
            // 获得基本信息
            certinfo = iCertInfoExternal.getCertInfoByRowguid(certrowguid);
            log.info("第一次的证照实体：" + certinfo);
            // 验证是否存在基本信息
            if (certinfo == null) {
                // 不存在证照基本信息，提示消息，并关闭页面
                addCallbackParam("msg", "该证照基础信息不存在！");
                return;
            }
            log.info("certinfo.rowguid=" + certinfo.getRowguid());
            // 如果没有证照附件，设置Certcliengguid
            if (StringUtil.isBlank(certinfo.getCertcliengguid())) {
                certinfo.setCertcliengguid(UUID.randomUUID().toString());
            }

            if (StringUtil.isBlank(certinfo.get("ftfjcliengguid"))) {
                certinfo.set("ftfjcliengguid", UUID.randomUUID().toString());
            }

            // 获得元数据配置表所有顶层节点
            metadataList = iCertConfigExternal.selectMetadataByIdAndVersion(certinfo.getCertcatalogid(),
                    String.valueOf(certinfo.getCertcatalogversion()), areacode);
            if (metadataList != null && !metadataList.isEmpty()) {
                metadataList.removeIf(metadata -> {
                    if (StringUtil.isNotBlank(metadata.get("PARENTGUID"))) {
                        return true;
                    } else {
                        return false;
                    }
                });
            }

            // 设置页面展示的持有人类型
            certCatalog = iCertConfigExternal.getCatalogByCatalogid(certinfo.getCertcatalogid(), areacode);
            if (certCatalog != null) {
                if (StringUtil.isNotBlank(certCatalog.getBelongtype())) {
                    if (CertConstant.CERTOWNERTYPE_ZRR.equals(certCatalog.getBelongtype())
                            || CertConstant.CERTOWNERTYPE_FR.equals(certCatalog.getBelongtype())) {
                        String codename = codeItemsService.getItemTextByCodeName("证照持有人类型",
                                certCatalog.getBelongtype());
                        addCallbackParam("codename", codename);
                        addCallbackParam("codevalue", certCatalog.getBelongtype());
                        addCallbackParam("certownertype", certCatalog.getBelongtype());
                        if (!certCatalog.getBelongtype().contains(certownertype)) {
                            warn = true;
                            addCallbackParam("warn", warn);
                        }
                    }
                }

            }

            dataBean = iCertInfoExternal.getCertExtenByCertInfoGuid(certrowguid);
            if (dataBean != null) {
                extendguid = dataBean.getRowguid();
            } else {
                dataBean = new CertInfoExtension();
            }
            dataBean.set("xkzh", getRequestParameter("cxcode"));

            // String creatNo = creatNo();

            // 日期格式转换
            this.convertDate(metadataList, dataBean);
            // 返回渲染的字段(子表修改模式)
            Map<String, Object> generateMapUseSubExtension = this.generateMapUseSubExtension("certaddaction",
                    metadataList, ZwfwConstant.MODE_EDIT, ZwfwConstant.SUB_MODE_EDIT);
            addCallbackParam("controls", generateMapUseSubExtension);
            // 渲染基本信息
            certFilelds = new HashMap<>(16);
            for (CertMetadata certMetadata : metadataList) {
                if (StringUtil.isNotBlank(certMetadata.getRelatedfield())) {
                    certFilelds.put(certMetadata.getRelatedfield(), certMetadata.getFieldname());
                }
            }
            addCallbackParam("basiccontrols", this.generateBasicDefaultMap(metadataList, false, "certinfo") == null ? ""
                    : this.generateBasicDefaultMap(metadataList, false, "certinfo"));
            addCallbackParam("edit", "edit");
            addViewData("edit", "edit");
        }
        else {
            //没有证照需要进行渲染生成证照
            if (StringUtil.isNotBlank(certcatalogid)) {
                // 设置持有人类型
                certCatalog = iCertConfigExternal.getCatalogByCatalogid(certcatalogid, areacode);
                if (certCatalog == null) {
                    return;
                }

                // 获得元数据配置表所有顶层节点
                metadataList = iCertConfigExternal.selectMetadataByIdAndVersion(certcatalogid,
                        String.valueOf(certinfo.getCertcatalogversion()), areacode);
                if (metadataList != null && metadataList.size() > 0) {
                    metadataList.removeIf(metadata -> {
                        if (StringUtil.isNotBlank(metadata.get("PARENTGUID"))) {
                            return true;
                        } else {
                            return false;
                        }
                    });
                }
                if (!isPostback()) {
                    // 设置颁证单位
                    addCallbackParam("certawarddept", userSession.getOuName());
                    addCallbackParam("certawarddeptguid", userSession.getOuGuid());
                    if (StringUtil.isNotBlank(certCatalog.getBelongtype())) {
                        if (CertConstant.CERTOWNERTYPE_ZRR.equals(certCatalog.getBelongtype())
                                || CertConstant.CERTOWNERTYPE_FR.equals(certCatalog.getBelongtype())) {
                            String codename = codeItemsService.getItemTextByCodeName("证照持有人类型",
                                    certCatalog.getBelongtype());
                            addCallbackParam("codename", codename);
                            addCallbackParam("codevalue", certCatalog.getBelongtype());
                            addCallbackParam("certareaguid", certCatalog.getRowguid());
                            if (!certCatalog.getBelongtype().contains(certownertype)) {
                                warn = true;
                                addCallbackParam("warn", warn);
                            }
                        }
                    }

                    //事项名称进行跳转不同的方法处理
                    AuditTask task = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();
                    GenerateCertCommon generateCertCommon = new GenerateCertCommon();
                    WsxGenerateCertCommon wsxGenerateCertCommon = new WsxGenerateCertCommon();
                    JnGenerateCertCommon jnGenerateCertCommon = new JnGenerateCertCommon();
                    switch (task.getItem_id()) {
                        // 用地规划许可
                        case "1137080123456789154370115055000":
                        case "11370800MB23415709400011513100301":
                        case "11370800MB23415709400011513100303":
                        case "11370800MB23415709400011513100302":
                            generateCertCommon.ydghxkz(dataBean, auditProject.getSubappguid());
                            break;
                        // 乡村建设规划许可证
                        case "1137080123456789154370115057000":
                            generateCertCommon.jsgcghxk(dataBean, auditProject.getSubappguid());
                            break;
                        // 建设工程消防设计审查
                        case "1137080123456789074370117043000":
                            generateCertCommon.jsgcxfsjsc(dataBean, auditProject.getSubappguid());
                            break;
                        // 建筑工程施工许可证核发
                        /*
                         * case "11370800004234373R400011710900302":
                         * generateCertCommon.jzgcsgxkz(dataBean,
                         * auditProject.getSubappguid(), areacode);
                         * log.info(task.getItem_id() + dataBean.getRowguid() +
                         * dataBean.toString());
                         * break;
                         */
                        //建设用地、临时建设用地规划许可（汶上县）
                        case "11370830F5034613XX4370115055000":
                        case "11370830004335078G400011513100301":
                        case "11370830004335078G400011513100303":
                        case "11370830004335078G400011513100302":
                            wsxGenerateCertCommon.jsydghxk(dataBean, auditProject.getSubappguid());
                            break;
                        //建筑工程施工许可证核发（汶上县）
                        case "11370830F5034613XX400011710900302":
                            wsxGenerateCertCommon.jzgcsgxkz(dataBean, auditProject.getSubappguid());
                            break;
                        //建设工程规划许可证（汶上县）
                        case "11370830F5034613XX4370115056000":
                        case "11370830004335078G400011513300501":
                        case "11370830004335078G400011513300502":
                        case "11370830004335078G400011513300503":
                            wsxGenerateCertCommon.sjgcghxkz(dataBean, auditProject.getSubappguid());
                            break;
                        //乡村建设规划许可证（汶上县）
                        case "11370830F5034613XX4370115057000":
                            wsxGenerateCertCommon.sjgcghxkz(dataBean, auditProject.getSubappguid());
                            break;
                        //人防工程施工图设计文件核准（汶上县）
                        case "11370830F5034613XX437108000900101":
                            wsxGenerateCertCommon.rfgcsgt(dataBean, auditProject.getSubappguid());
                            break;
                        //防空地下室易地建设审批（汶上县）
                        case "11370830F5034613XX4370143004000":
                            wsxGenerateCertCommon.fkdxs(dataBean, auditProject.getSubappguid());
                            break;
                        //取水许可审批（设区的市级权限）（首次申请）
                        case "11370800MB28559184300011910200501":
                            jnGenerateCertCommon.qsxksp(dataBean, auditProject.getApplyername());
                            break;
                        //公路建设项目施工许可
                        case "11370800MB285591847370118022000":
                            jnGenerateCertCommon.gljsxmskxk(dataBean, auditProject.getApplyername());
                            break;
                        //设置X射线影像诊断建设项目放射性职业病危害预评价报告审核
                        case "11370830F5034613XX400012311200301":
                            Record record = iCxBusService.getDzbdDetail("formtable20240611174116",
                                    dzbdguid);
                            jnGenerateCertCommon.szXsxyxzdypjbgsh(dataBean, record, certinfo);
                            break;
                        //设置X射线影像诊断的建设项目放射性职业病防护设施竣工验收
                        case "11370830F5034613XX400012311300301":
                            record = iCxBusService.getDzbdDetail("formtable20240611181440",
                                    dzbdguid);
                            jnGenerateCertCommon.szXsxzybfhssjgys(dataBean, record, certinfo);
                            break;
                        //设置X射线影像诊断项目许可（新办）
                        case "11370830F5034613XX400012311900301":
                            record = iCxBusService.getDzbdDetail("formtable20240611181440",
                                    dzbdguid);
                            jnGenerateCertCommon.szXsxzdxmxkxb(dataBean, record, auditProject.getAreacode(), certinfo);
                            break;

                        //人力资源服务许可证
                        case "11370828MB28602902400011410700301":
                            record = iCxBusService.getDzbdDetail("cszyzjhdxkbasqb",
                                    dzbdguid);
                            jnGenerateCertCommon.rlzyfwxkxb(dataBean, record);
                            break;
                        case "11370828MB28602902400011410700302":
                            record = iCxBusService.getDzbdDetail("formtable20240927140444",
                                    dzbdguid);
                            jnGenerateCertCommon.rlzyfwxkbg(dataBean, record);
                            break;
                        //劳务派遣经营许可证
                        case "11370828MB28602902400011410800301":
                            //新设
                        case "11370828MB28602902400011410800302":
                            //变更
                            record = iCxBusService.getDzbdDetail("formtable20241008143546",
                                    dzbdguid);
                            jnGenerateCertCommon.lwpqjyxkz(dataBean, record);
                            break;
                        default:
                            break;
                    }
                    // 工改证照设置默认值
                    if (StringUtil.isNotBlank(auditProject.getSubappguid())) {
                        try {
                            new Dzbd4DzzzUtil(dataBean, task, auditProject, certCatalog.getTycertcatcode());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    //字段渲染通用处理
                    dataBean = initCertinfoExtenCommon(dataBean);
                    AuditSpISubapp auditSpISubapp = null;
                    AuditRsItemBaseinfo auditRsItemBaseinfo= null;
                   
                    if (auditProject != null && StringUtils.isNotBlank(auditProject.getSubappguid())){
                        auditSpISubapp = iAuditSpISubapp
                                .getSubappByGuid(auditProject.getSubappguid()).getResult();
                        if (auditSpISubapp != null) {
                            auditRsItemBaseinfo = iAuditRsItemBaseinfo
                                    .getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
                        }
                        if(auditRsItemBaseinfo!=null){
                            // 建设工程竣工验收备案信息,表单渲染到电子证照
                            SpglJsgcjgysbaxxbV3 spglJsgcjgysbaxxbV3 = iSpglJsgcjgysbaxxbV3Service.findDominByCondition("370800", auditRsItemBaseinfo.getItemcode(), auditProject.getFlowsn());
                            if(spglJsgcjgysbaxxbV3!=null && auditProject.getProjectname().contains("房屋建筑和市政基础设施工程竣工验收备案")){
                                dataBean = userJsgcjgysbaxxbCertinfoExtenCommon(dataBean,spglJsgcjgysbaxxbV3);
                            }
                        }
                    }
                    
                    
                    // 济宁关于（项目名称）核准的批复
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("企业投资")) {
                            if ("11370800MB285591847370104001042".equals(task.getItem_id())
                                    || "11370800MB285591847370104001044".equals(task.getItem_id())
                                    || "11370800MB285591843370104001034".equals(task.getItem_id())
                                    || "11370800MB285591843370104001032".equals(task.getItem_id())
                                    || "11370800MB285591847370104001043".equals(task.getItem_id())
                                    || "11370800MB285591843370104001033".equals(task.getItem_id())
                                    || "11370800MB285591847371004005003".equals(task.getItem_id())
                                    || "11370800MB285591843370104001038".equals(task.getItem_id())
                                    || "11370800MB285591847370104001040".equals(task.getItem_id())
                                    || "11370800MB285591843370104001037".equals(task.getItem_id())
                                    || "11370800MB285591847370104001041".equals(task.getItem_id())
                                    || "11370800MB285591847370104001046".equals(task.getItem_id())
                                    || "11370800MB285591843370104001035".equals(task.getItem_id())
                                    || "11370800MB285591843370104001039".equals(task.getItem_id())
                                    || "11370800MB285591843370104001045".equals(task.getItem_id())
                                    || "11370800MB285591843370104001031".equals(task.getItem_id())
                                    || "11370800MB285591846370104001036".equals(task.getItem_id())) {
                                String jsdwname = "";

                                AuditSpSpLxydghxk auditSpSpLxydghxk = iAuditSpSpLxydghxkService
                                        .findAuditSpSpLxydghxkBysubappguid(auditProject.getSubappguid());
                                if (auditSpSpLxydghxk != null) {
                                    dataBean.set("xmmc", auditSpSpLxydghxk.getItemname());
                                    dataBean.set("xmdz", auditSpSpLxydghxk.getItemaddress());
                                    dataBean.set("zbj", auditSpSpLxydghxk.getStr("zbj"));
                                    dataBean.set("zztzbl", auditSpSpLxydghxk.getStr("zztz"));
                                    dataBean.set("fzrq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                                }


                                if (auditRsItemBaseinfo != null) {
                                    dataBean.set("jsnr", auditRsItemBaseinfo.getConstructionscaleanddesc());
                                    dataBean.set("xmztz", auditRsItemBaseinfo.getTotalinvest());
                                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getParentid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                        }
                                    } else {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getRowguid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                        }
                                    }

                                }
                                dataBean.set("jsdw", jsdwname);
                                dataBean.set("fjmc", "无");

                                String idnum = createJsGcZsbh(task.getItem_id());
                                dataBean.set("zsbh", idnum);

                            }
                        }
                    }

                    // 济宁关于（项目名称）可行性研究报告的批复
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("投资")) {

                            if ("11370800MB285591847371004002001".equals(task.getItem_id())) {
                                String jsdwname = "";
                                AuditSpSpLxydghxk auditSpSpLxydghxk = iAuditSpSpLxydghxkService
                                        .findAuditSpSpLxydghxkBysubappguid(auditProject.getSubappguid());
                                if (auditSpSpLxydghxk != null) {
                                    dataBean.set("xmmc", auditSpSpLxydghxk.getItemname());
                                    dataBean.set("xmdm", auditSpSpLxydghxk.getItemcode());
                                    dataBean.set("xmdz", auditSpSpLxydghxk.getItemaddress());
                                    dataBean.set("zjly", iCodeItemsService.getItemTextByCodeName("资金来源",
                                            auditSpSpLxydghxk.getMoneysources()));
                                    dataBean.set("fzrq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                                }
                                
                                if (auditRsItemBaseinfo != null) {
                                    dataBean.set("xmzyjsnr", auditRsItemBaseinfo.getConstructionscaleanddesc());
                                    dataBean.set("xmztz", auditRsItemBaseinfo.getTotalinvest());
                                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getParentid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                        }
                                    } else {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getRowguid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                        }
                                    }

                                }
                                dataBean.set("dwmc", jsdwname);
                                dataBean.set("fjmc", "无");

                                String idnum = createJsGcZsbh(task.getItem_id());
                                dataBean.set("zsbh", idnum);

                            }
                        }
                    }

                    // 鱼台
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("房屋建筑工程和市政基础设施工程竣工验收备案")) {
                            if ("11370827MB2857833K4371017058000".equals(task.getItem_id())) {
                                if ("房屋市政工程竣工验收备案（含人防、档案、消防）意见书".equals(certCatalog.getCertname())) {
                                    String jsdwname = "";
                                    String gcmc = "";
                                    AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService
                                            .findAuditSpSpJgysBySubappGuid(auditProject.getSubappguid());
                                    if (auditSpSpJgys != null) {
                                        gcmc = auditSpSpJgys.getItemname();
                                        dataBean.set("bdcdyh", auditSpSpJgys.getStr("bdcdanyuanhao"));
                                    }
                                    if (auditRsItemBaseinfo != null) {
                                        if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                                            SqlConditionUtil sql = new SqlConditionUtil();
                                            sql.eq("itemGuid", auditRsItemBaseinfo.getParentid());
                                            sql.eq("corptype", "31");
                                            List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                    .getParticipantsInfoListByCondition(sql.getMap());
                                            if (participantsInfoList.size() > 0) {
                                                jsdwname = participantsInfoList.get(0).getCorpname();
                                            }
                                        } else {
                                            SqlConditionUtil sql = new SqlConditionUtil();
                                            sql.eq("itemGuid", auditRsItemBaseinfo.getRowguid());
                                            sql.eq("corptype", "31");
                                            List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                    .getParticipantsInfoListByCondition(sql.getMap());
                                            if (participantsInfoList.size() > 0) {
                                                jsdwname = participantsInfoList.get(0).getCorpname();
                                            }
                                        }
                                    }
                                    String numberName = "Ytjg_Prj_Type_Num";
                                    Calendar calendar = Calendar.getInstance();
                                    String numberFlag = "" + calendar.get(Calendar.YEAR)
                                            + String.format("%02d", calendar.get(Calendar.MONTH) + 1)
                                            + String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
                                    int theYearLength = 0;
                                    int snLength = 2;
                                    String certno = new DBServcie().getFlowSn(numberName, numberFlag, theYearLength,
                                            false, snLength);
                                    dataBean.set("jianshedanwei", jsdwname);
                                    dataBean.set("gongchengmingcheng", gcmc);
                                    String itemtype = "01";
                                    if (auditSpISubapp != null) {
                                        AuditRsItemBaseinfo baseinfo = iAuditRsItemBaseinfo
                                                .getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid())
                                                .getResult();
                                        if (baseinfo != null) {
                                            itemtype = baseinfo.getItemtype();
                                            if (!"01".equals(itemtype)) {
                                                itemtype = "02";
                                            }
                                        }
                                        String issxmce = "0";
                                        String issxmcer = "0";
                                        String issxmcw = "0";
                                        List<AuditSpITask> spITasks = auditSpITaskService
                                                .getSpITaskByBIGuid(auditSpISubapp.getBiguid()).getResult();
                                        for (AuditSpITask auditSpITask : spITasks) {
                                            if (StringUtil.isNotBlank(auditSpITask.getProjectguid())) {
                                                if ("人防工程（防空地下室）竣工验收备案".equals(auditSpITask.getTaskname())) {
                                                    issxmce = "1";
                                                } else if ("特殊建设工程消防验收".equals(auditSpITask.getTaskname())) {
                                                    issxmcer = "1";
                                                } else if ("其他建设工程消防验收备案".equals(auditSpITask.getTaskname())) {
                                                    issxmcw = "1";
                                                }

                                            }
                                        }
                                        dataBean.set("sxmce", issxmce);
                                        dataBean.set("sxmcer", issxmcer);
                                        dataBean.set("sxmcw", issxmcw);
                                    }

                                    String Prj_Finish_Num = "JB" + auditProject.getAreacode() + certno + itemtype;
                                    dataBean.set("bianhao", Prj_Finish_Num);
                                    dataBean.set("riqi", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                                } else {
                                    String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");
                                    AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService
                                            .findAuditSpSpJgysBySubappGuid(auditProject.getSubappguid());
                                    if (auditSpSpJgys != null) {
                                        dataBean.set("bdcdyh", auditSpSpJgys.getStr("bdcdanyuanhao"));
                                        dataBean.set("xmmc", auditSpSpJgys.getItemname());
                                    }
                                    String jsdwname = "";
                                    String numberName = "Ytfwjs_Prj_Type_Num";
                                    Calendar calendar = Calendar.getInstance();
                                    String numberFlag = "" + String.format("%02d", calendar.get(Calendar.MONTH) + 1)
                                            + String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
                                    int theYearLength = 0;
                                    int snLength = 2;
                                    String certno = new DBServcie().getFlowSn(numberName, numberFlag, theYearLength,
                                            false, snLength);
                                    
                                    if (auditRsItemBaseinfo != null) {
                                        if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                                            SqlConditionUtil sql = new SqlConditionUtil();
                                            sql.eq("itemGuid", auditRsItemBaseinfo.getParentid());
                                            sql.eq("corptype", "31");
                                            List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                    .getParticipantsInfoListByCondition(sql.getMap());
                                            if (participantsInfoList.size() > 0) {
                                                jsdwname = participantsInfoList.get(0).getCorpname();
                                            }
                                        } else {
                                            SqlConditionUtil sql = new SqlConditionUtil();
                                            sql.eq("itemGuid", auditRsItemBaseinfo.getRowguid());
                                            sql.eq("corptype", "31");
                                            List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                    .getParticipantsInfoListByCondition(sql.getMap());
                                            if (participantsInfoList.size() > 0) {
                                                jsdwname = participantsInfoList.get(0).getCorpname();
                                            }
                                        }
                                    }

                                    dataBean.set("tzsnh", year);
                                    dataBean.set("tzsbh", certno);
                                    dataBean.set("dwmc", jsdwname);
                                    dataBean.set("fzrq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                                    dataBean.set("tzsrq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));

                                }
                            } else if ("11370800MB285591847371017058000".equals(task.getItem_id())) {
                                AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService
                                        .findAuditSpSpJgysBySubappGuid(auditProject.getSubappguid());
                                if (auditSpSpJgys != null) {
                                    dataBean.set("bdcdyh", auditSpSpJgys.getStr("bdcdanyuanhao"));
                                }
                            }
                        }
                    }

                    // 济宁商品房预售许可
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("商品房预售许可")) {
                            if ("11370800MB285591847370117046000".equals(task.getItem_id())
                                    || "11370827MB2857833K4370117046000".equals(task.getItem_id())
                                    || "11370826MB2858051T4370117046000".equals(task.getItem_id())
                                    || "11370800MB28559184300011711700201".equals(task.getItem_id())
                                    || "11370800MB28559184300011711700202".equals(task.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20230206094334",
                                        dzbdguid);
                                if (record != null) {
                                    dataBean.set("yxsfwjbxx", record.getStr("yxsfwjbxx"));
                                    dataBean.set("fwxsdw", record.getStr("fwyxsdw"));
                                    dataBean.set("yxsfwjgyh", record.getStr("yxsfwjgyx"));
                                    dataBean.set("xqmc", record.getStr("xqztmc"));
                                    dataBean.set("yxsjgzh", record.getStr("yxsfwjgzh"));
                                    dataBean.set("xsfwlb", record.getStr("xsfwlb"));
                                    dataBean.set("yxsfwdz", record.getStr("yxsfwxrdz"));
                                }

                                dataBean.set("fzrq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));

                            }
                        }
                    }

                    // 建筑施工企业安全生产许可证注销事项
                    if (auditProject != null && "11370800MB285591843370117002017".equals(task.getItem_id())) {
                        Record record = iCxBusService.getDzbdDetail("formtable20220422102306",
                                dzbdguid);
                        if (record != null) {
                            dataBean.set("qymc", record.getStr("wbk1"));
                            dataBean.set("tyxydm", record.getStr("tyshxydm"));
                            dataBean.set("xxdz", record.getStr("xxdz"));
                            dataBean.set("frdb", record.getStr("fddbr"));
                            dataBean.set("jjxz", record.getStr("jjxz"));
                            dataBean.set("zsbh", record.getStr("zsbh"));
                            dataBean.set("zzlbjdj", record.getStr("zzlbjdj"));
                        }
                    }

                    // 建筑业企业资质证升级
                    if (auditProject != null) {
                        if ("11370800MB285591843370117100133".equals(task.getItem_id())) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220622114015",
                                    dzbdguid);
                            if (record != null) {
                                dataBean.set("tyxydm", record.getStr("tyxydm"));
                                dataBean.set("xxdz", record.getStr("qydz"));
                                dataBean.set("zzlbjdj", record.getStr("yyejzz"));
                                dataBean.set("qymc", record.getStr("qymc"));
                                dataBean.set("frdb", record.getStr("fddbr"));
                                dataBean.set("jjxz", record.getStr("jjxz"));
                                dataBean.set("zsbh", record.getStr("zsbh"));

                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(new Date());
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                Date enddate = calendar.getTime();
                                dataBean.set("yxq", EpointDateUtil.convertDate2String(enddate, "yyyy年MM月dd日"));
                            }
                        }
                    }

                    // 建筑业企业资质证增项 --扩展到全市范围
                    if (auditProject != null) {
                        if ("建筑业企业资质许可（增项）".equals(task.getTaskname())) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220509094032",
                                    dzbdguid);
                            if (record != null) {
                                dataBean.set("xxdz", record.getStr("qydz"));
                                dataBean.set("zzlbjdj", record.getStr("yyzz"));
                                dataBean.set("zsbh", record.getStr("zsbh"));
                                dataBean.set("tyxydm", record.getStr("tyxydm"));
                                dataBean.set("jjxz", record.getStr("jjxz"));
                                dataBean.set("qymc", record.getStr("qymc"));
                                dataBean.set("frdb", record.getStr("fddbr"));

                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(new Date());
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                Date enddate = calendar.getTime();
                                dataBean.set("yxq", EpointDateUtil.convertDate2String(enddate, "yyyy年MM月dd日"));
                            }
                        }
                    }

                    // 建筑业企业资质（首次申请）|| 建筑业企业资质许可（延续）
                    if (auditProject != null) {
                        if ("建筑业企业资质（首次申请）".equals(task.getTaskname())
                                || "建筑业企业资质许可（延续）".equals(task.getTaskname())) {
                            Record record = iCxBusService.getDzbdDetail("formtable20230619094541",
                                    dzbdguid);
                            if (record != null) {
                                dataBean.set("frdb", record.getStr("fddbr"));
                                dataBean.set("qymc", record.getStr("qymc"));
                                dataBean.set("zzlbjdj", record.getStr("yyejzz"));
                                dataBean.set("xxdz", record.getStr("qydz"));
                                dataBean.set("zsbh", record.getStr("zsbh"));
                                dataBean.set("jjxz", record.getStr("jjxz"));
                                dataBean.set("tyxydm", record.getStr("tyxydm"));

                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(new Date());
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                Date enddate = calendar.getTime();
                                dataBean.set("yxq", EpointDateUtil.convertDate2String(enddate, "yyyy年MM月dd日"));
                            }
                        }
                    }

                    // 济宁港口经营许可
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("港口经营许可")) {
                            if ("11370800MB285591843370118037000".equals(task.getItem_id())
                                    || "11370800MB28559184300011823200201".equals(task.getItem_id())
                                    || "11370800MB28559184300011823200202".equals(task.getItem_id())
                                    || "11370800MB28559184300011823200203".equals(task.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20230213163235",
                                        dzbdguid);
                                if (record != null) {
                                    dataBean.set("fddbr", record.getStr("fddbr"));
                                    dataBean.set("bgdz", record.getStr("dz"));
                                    dataBean.set("gsmc", record.getStr("gsmc"));
                                    dataBean.set("jyqy", record.getStr("jydy"));
                                    dataBean.set("ywlx", record.getStr("ywlx"));
                                }

                                String idnum = createJsGcZsbh(task.getItem_id());
                                dataBean.set("zsbh", idnum);

                                dataBean.set("fzrq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 3); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxqz", yxq);

                            }
                        }
                    }

                    // 济宁防空地下室易地建设审批
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("防空地下室易地建设审批")) {
                            if ("11370800MB285591847370143004000".equals(task.getItem_id())) {
                                String jsdwname = "";

                                AuditSpSpGcjsxk auditSpSpGcjsxk = iAuditSpSpGcjsxkService
                                        .findAuditSpSpGcjsxkBySubappGuid(auditProject.getSubappguid());
                                if (auditSpSpGcjsxk != null) {
                                    dataBean.set("jsxm", auditSpSpGcjsxk.getItemname());
                                    dataBean.set("jsdd", auditSpSpGcjsxk.getItemaddress());
                                    dataBean.set("zjzmj", auditSpSpGcjsxk.getAllbuildarea());
                                    dataBean.set("dsjzmj", auditSpSpGcjsxk.getOverloadarea());
                                    dataBean.set("dxjzmj", auditSpSpGcjsxk.getDownloadarea());
                                    dataBean.set("yjfkdxs", auditSpSpGcjsxk.getRfywzmj());
                                    dataBean.set("fhlb", auditSpSpGcjsxk.getStr("fhlb"));
                                    dataBean.set("jfrq", EpointDateUtil
                                            .convertDate2String(auditSpSpGcjsxk.getDate("fkdxsjsjfrq"), "yyyy-MM-dd"));
                                    dataBean.set("ydjsfjnse", auditSpSpGcjsxk.getStr("ydjsfjnse"));
                                    dataBean.set("rq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));

                                }
                                
                                String itemtype = "01";
                                if (auditRsItemBaseinfo != null) {
                                    itemtype = auditRsItemBaseinfo.getItemtype();
                                    if (!"01".equals(itemtype)) {
                                        itemtype = "02";
                                    }

                                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getParentid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                        }
                                    } else {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getRowguid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                        }
                                    }

                                }
                                dataBean.set("jsdw", jsdwname);
                            }
                        }
                    }

                    // 济宁人防工程施工图设计文件核准
                    /*
                     * if (auditProject != null) {
                     * if
                     * (auditProject.getProjectname().contains("人防工程施工图设计文件核准"))
                     * {
                     * if
                     * ("11370800MB285591847371043008000".equals(task.getItem_id
                     * ())) {
                     * String jsdwname = "";
                     * String sjdwname = "";
                     * String sgdwname = "";
                     *
                     * AuditSpSpGcjsxk auditSpSpGcjsxk = iAuditSpSpGcjsxkService
                     * .findAuditSpSpGcjsxkBySubappGuid(auditProject.
                     * getSubappguid());
                     * if (auditSpSpGcjsxk != null) {
                     * dataBean.set("gcmc", auditSpSpGcjsxk.getItemname());
                     * dataBean.set("gcdz", auditSpSpGcjsxk.getItemaddress());
                     * dataBean.set("zjzmj", auditSpSpGcjsxk.getAllbuildarea());
                     * dataBean.set("dsjzmj",
                     * auditSpSpGcjsxk.getOverloadarea());
                     * dataBean.set("dxjzmj",
                     * auditSpSpGcjsxk.getDownloadarea());
                     * dataBean.set("rfgcjzmj",
                     * auditSpSpGcjsxk.getStr("rfgcjzmj"));
                     * dataBean.set("jjfkd",
                     * auditSpSpGcjsxk.getStr("jjspyjbh"));
                     * dataBean.set("fhlb", auditSpSpGcjsxk.getStr("fhlb"));
                     * dataBean.set("fzrq",
                     * EpointDateUtil.convertDate2String(new Date(),
                     * "yyyy-MM-dd"));
                     *
                     * }
                     *
                     * AuditSpISubapp auditSpISubapp = iAuditSpISubapp
                     * .getSubappByGuid(auditProject.getSubappguid()).getResult(
                     * );
                     * String itemtype = "01";
                     * if (auditSpISubapp != null) {
                     * AuditRsItemBaseinfo auditRsItemBaseinfo =
                     * iAuditRsItemBaseinfo
                     * .getAuditRsItemBaseinfoByRowguid(auditSpISubapp.
                     * getYewuguid()).getResult();
                     * if (auditRsItemBaseinfo != null) {
                     * itemtype = auditRsItemBaseinfo.getItemtype();
                     * if (!"01".equals(itemtype)) {
                     * itemtype = "02";
                     * }
                     *
                     * if
                     * (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())
                     * ) {
                     * SqlConditionUtil sql = new SqlConditionUtil();
                     * sql.eq("itemGuid", auditRsItemBaseinfo.getParentid());
                     * sql.eq("corptype", "31");
                     * List<ParticipantsInfo> participantsInfoList =
                     * iParticipantsInfo
                     * .getParticipantsInfoListByCondition(sql.getMap());
                     * if (participantsInfoList.size() > 0) {
                     * jsdwname = participantsInfoList.get(0).getCorpname();
                     * }
                     * }
                     * else {
                     * SqlConditionUtil sql = new SqlConditionUtil();
                     * sql.eq("itemGuid", auditRsItemBaseinfo.getRowguid());
                     * sql.eq("corptype", "31");
                     * List<ParticipantsInfo> participantsInfoList =
                     * iParticipantsInfo
                     * .getParticipantsInfoListByCondition(sql.getMap());
                     * if (participantsInfoList.size() > 0) {
                     * jsdwname = participantsInfoList.get(0).getCorpname();
                     * }
                     * }
                     *
                     * }
                     * }
                     *
                     * SqlConditionUtil sql = new SqlConditionUtil();
                     * sql.nq("corptype", "999");
                     * sql.eq("subappguid", auditProject.getSubappguid());
                     * List<ParticipantsInfo> participantsInfoList =
                     * iParticipantsInfo
                     * .getParticipantsInfoListByCondition(sql.getMap());
                     * if (!participantsInfoList.isEmpty()) {
                     * for (ParticipantsInfo partic : participantsInfoList) {
                     * if ("31".equals(partic.getCorptype())) {
                     * jsdwname = partic.getCorpname();
                     * }
                     * else if ("2".equals(partic.getCorptype())) {
                     * sjdwname = partic.getCorpname();
                     * }
                     * else if ("3".equals(partic.getCorptype())) {
                     * sgdwname = partic.getCorpname();
                     * }
                     * }
                     * }
                     * dataBean.set("jsdw", jsdwname);
                     * dataBean.set("sjdw", sjdwname);
                     * dataBean.set("scdw", sgdwname);
                     * }
                     * }
                     * }
                     */

                    // 济宁建设项目用地预审与选址意见书
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("建设项目用地预审与选址意见书")) {
                            if ("11370800MB285591847370115001000".equals(task.getItem_id())) {
                                String jsdwname = "";

                                AuditSpSpLxydghxk auditSpSpLxydghxk = iAuditSpSpLxydghxkService
                                        .findAuditSpSpLxydghxkBysubappguid(auditProject.getSubappguid());
                                if (auditSpSpLxydghxk != null) {
                                    dataBean.set("jsxmmc", auditSpSpLxydghxk.getItemname());
                                    dataBean.set("jsxmd", auditSpSpLxydghxk.getItemcode());
                                    dataBean.set("jsxmyj", auditSpSpLxydghxk.getStr("xmjsyj"));
                                    dataBean.set("jsxmnxwz", auditSpSpLxydghxk.getStr("xmnxdz"));
                                    dataBean.set("nydmj", auditSpSpLxydghxk.getStr("nydmj"));
                                    dataBean.set("njsgm", auditSpSpLxydghxk.getStr("njsgm"));
                                }
                                String idnum = createJsGcZsbh(task.getItem_id());
                                if (auditRsItemBaseinfo != null) {
                                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getParentid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();

                                        }
                                    } else {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getRowguid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                        }
                                    }
                                }
                                dataBean.set("jsdwmc", jsdwname);
                                dataBean.set("bh", idnum);
                                dataBean.set("rq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                            }
                        }
                    }

                    // 鱼台
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("特殊建设工程消防验收")) {
                            if ("11370827MB2857833K3370117044000".equals(task.getItem_id())) {
                                String jsdwname = "";
                                String gcmc = "";
                                AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService
                                        .findAuditSpSpJgysBySubappGuid(auditProject.getSubappguid());
                                if (auditSpSpJgys != null) {
                                    gcmc = auditSpSpJgys.getGcmc();
                                }

                                if (auditRsItemBaseinfo != null) {
                                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getParentid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                        }
                                    } else {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getRowguid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                        }
                                    }
                                }

                                String idnum = createJsGcZsbh(task.getItem_id());

                                dataBean.set("nian", EpointDateUtil.convertDate2String(new Date(), "yyyy"));
                                dataBean.set("hao", idnum);
                                dataBean.set("jsdw", jsdwname);
                                dataBean.set("gcmc", gcmc);
                                dataBean.set("rq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                                dataBean.set("spbm", "鱼台县行政审批服务局");
                                dataBean.set("gzrq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                            } else if ("1137083000433521104370117044000".equals(task.getItem_id())
                                    || "1137082600452807284370117044000".equals(task.getItem_id())) {

                                String idnum = createJsGcZsbh(task.getItem_id());
                                dataBean.set("bh", idnum);

                                AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService
                                        .findAuditSpSpJgysBySubappGuid(auditProject.getSubappguid());
                                if (auditSpSpJgys != null) {
                                    dataBean.set("bdcdyh", auditSpSpJgys.getStr("bdcdanyuanhao"));
                                }
                            } else if ("11370800MB285591847370117044000".equals(task.getItem_id())) {
                                AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService
                                        .findAuditSpSpJgysBySubappGuid(auditProject.getSubappguid());
                                if (auditSpSpJgys != null) {
                                    dataBean.set("bdcdyh", auditSpSpJgys.getStr("bdcdanyuanhao"));
                                }
                            }
                        }
                    }

                    // 济宁房屋市政工程竣工验收备案
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("房屋市政工程竣工验收备案")) {
                            if ("11370800MB285591843370718000001".equals(task.getItem_id())) {
                                AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService
                                        .findAuditSpSpJgysBySubappGuid(auditProject.getSubappguid());
                                if (auditSpSpJgys != null) {
                                    dataBean.set("bdcdyh", auditSpSpJgys.getStr("bdcdanyuanhao"));
                                }
                            } else if ("11370800MB285591843370718000002".equals(task.getItem_id())) {
                                AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService
                                        .findAuditSpSpJgysBySubappGuid(auditProject.getSubappguid());
                                if (auditSpSpJgys != null) {
                                    dataBean.set("bdcdyh", auditSpSpJgys.getStr("bdcdanyuanhao"));
                                }
                            }

                        }
                    }

                    // 济宁申请省际普通货物水路运输经营许可
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("申请省际普通货物水路运输经营许可")) {
                            if ("11370800MB285591843370118009001".equals(task.getItem_id())) {
                                String bh = createJsGcZsbh(task.getItem_id());
                                dataBean.set("bh", bh);
                                dataBean.set("JYQX", "三年");

                                String QN = EpointDateUtil.convertDate2String(new Date(), "yyyy");
                                String QY = EpointDateUtil.convertDate2String(new Date(), "MM");
                                String QR = EpointDateUtil.convertDate2String(new Date(), "dd");

                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 3); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();

                                String ZN = EpointDateUtil.convertDate2String(enddate, "yyyy");
                                String ZY = EpointDateUtil.convertDate2String(enddate, "MM");
                                String ZR = EpointDateUtil.convertDate2String(enddate, "dd");

                                dataBean.set("QN", QN);
                                dataBean.set("QY", QY);
                                dataBean.set("QR", QR);
                                dataBean.set("ZN", ZN);
                                dataBean.set("ZY", ZY);
                                dataBean.set("ZR", ZR);

                            }
                        }
                    }

                    // 济宁燃气经营许可证
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("燃气经营许可证")) {
                            if ("11370800MB28559184737011702600001".equals(task.getItem_id())
                                    || "11370800MB28559184737011702600002".equals(task.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20220921174721",
                                        dzbdguid);
                                if (record != null) {
                                    dataBean.set("qymc", record.getStr("qymc"));
                                    dataBean.set("dz", record.getStr("dz"));
                                    dataBean.set("frdbhzsqfzr", record.getStr("frdbhzsqfzr"));
                                    dataBean.set("jyrqlx", record.getStr("jyrqlx"));
                                    dataBean.set("jyrqqy", record.getStr("jyrqqy"));
                                }
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                dataBean.set("yxqs",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 3); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxqz", yxq);

                            } else if ("11370800MB28559184737011702600003".equals(task.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20220921172123",
                                        dzbdguid);
                                if (record != null) {
                                    dataBean.set("qymc", record.getStr("bghqymc"));
                                    dataBean.set("dz", record.getStr("bghzcdz"));
                                    dataBean.set("frdbhzsqfzr", record.getStr("bghfddbr"));
                                    dataBean.set("jyrqlx", record.getStr("jylb"));
                                    dataBean.set("jyrqqy", record.getStr("jyqy"));
                                }
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                dataBean.set("yxqs",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 3); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxqz", yxq);
                            }
                        }
                    }

                    // 济宁农药经营许可
                    String itemids = "";
                    // String tablename = ConfigUtil.getConfigValue("xmzArgs",
                    // "nyjyxkz_tablename");
                    itemids = ConfigUtil.getConfigValue("xmzArgs", "nyjyxkz_itemids");
                    if (StringUtil.isNotBlank(itemids)) {
                        List<String> itemidList = new ArrayList<>(Arrays.asList(itemids.split(";")));
                        if (itemidList != null || itemidList.size() > 0) {
                            for (String itemid : itemidList) {

                                if (auditProject.getProjectname().contains("农药经营许可")) {
                                    if (auditProject != null && StringUtil.isNotBlank(itemid)
                                            && (itemid.equals(task.getItem_id()))) {

                                        Record record = iCxBusService.getDzbdDetail("formtable20230605164813",
                                                dzbdguid);
                                        if (record != null) {

                                            dataBean.set("jyzmc", record.getStr("wbk1"));
                                            dataBean.set("tyshxydm", record.getStr("wbk2"));
                                            dataBean.set("fddbr", record.getStr("wbk3"));
                                            dataBean.set("zs", record.getStr("wbk4"));
                                            dataBean.set("yycs", record.getStr("dxwbk2"));
                                            dataBean.set("cccs", record.getStr("wbk9"));
                                            dataBean.set("jyfw", record.getStr("wbk10"));
                                            dataBean.set("fzjg", record.getStr("wbk11"));

                                            String sqjyfw = record.getStr("dxkz1");
                                            String idnum = createJsGcZsbh(task.getItem_id());
                                            if ("1".equals(sqjyfw)) {
                                                dataBean.set("bh", "3708221" + idnum);
                                            } else {
                                                dataBean.set("bh", "3708222" + idnum);
                                            }
                                        }

                                        dataBean.set("qsrq", EpointDateUtil.convertDate2String(new Date(),
                                                EpointDateUtil.DATE_FORMAT));
                                        dataBean.set("fzrq", EpointDateUtil.convertDate2String(new Date(),
                                                EpointDateUtil.DATE_FORMAT));
                                        Date startdate = new Date();
                                        Calendar calendar = new GregorianCalendar();
                                        calendar.setTime(startdate);
                                        calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                        calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                        Date enddate = calendar.getTime();
                                        String yxq = EpointDateUtil.convertDate2String(enddate,
                                                EpointDateUtil.DATE_FORMAT);
                                        dataBean.set("jzrq", yxq);

                                    }
                                }
                            }
                        }
                    }

                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("农药经营许可")) {
                            if ("11370812MB2868524U4370120006006".equals(task.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20221011143658",
                                        dzbdguid);
                                if (record != null) {
                                    dataBean.set("jyzmc", record.getStr("wbk1"));
                                    dataBean.set("zs", record.getStr("wbk2"));
                                    dataBean.set("yycs", record.getStr("wbk3"));
                                    dataBean.set("cccs", record.getStr("wbk4"));
                                    dataBean.set("tyshxydm", record.getStr("wbk7"));
                                    dataBean.set("fddbr", record.getStr("wbk8"));
                                    dataBean.set("jyfw", record.getStr("dxkz1"));
                                    dataBean.set("fzjg", record.getStr("dxwbk1"));

                                    String sqjyfw = record.getStr("dxkz1");
                                    String idnum = createJsGcZsbh(task.getItem_id());
                                    if ("1".equals(sqjyfw)) {
                                        dataBean.set("bh", "3708221" + idnum);
                                    } else {
                                        dataBean.set("bh", "3708222" + idnum);
                                    }
                                }

                                dataBean.set("qsrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("jzrq", yxq);

                            }
                        }
                    }

                    // 济宁涉水产品
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("涉水产品")) {
                            if ("11370800MB285591843370123018002".equals(task.getItem_id()) ||
                                    "11370800MB28559184300012310400201".equals(task.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20230207102600",
                                        dzbdguid);
                                if (record != null) {
                                    dataBean.set("cpmc", record.getStr("cpmcjxh"));
                                    dataBean.set("cplb", record.getStr("cplb"));
                                    dataBean.set("sqdw", record.getStr("zhzrdwmc"));
                                    dataBean.set("sqdwdz", record.getStr("zhzrdwdz"));
                                    dataBean.set("sjscqy", record.getStr("scqymc"));
                                    dataBean.set("sjscqydz", record.getStr("scqydz"));
                                }
                                String xkzbh = createJsGcZsbh(task.getItem_id());

                                dataBean.set("pzwh", xkzbh);
                                dataBean.set("yxqs",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 4); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxqz", yxq);
                                dataBean.set("newpzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            } else if ("11370800MB285591843370123018001".equals(task.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20230222100438",
                                        dzbdguid);
                                if (record != null) {
                                    dataBean.set("cpmc", record.getStr("cpmcjxh"));
                                    dataBean.set("cplb", record.getStr("cplb"));
                                    dataBean.set("sqdw", record.getStr("sbdw"));
                                    dataBean.set("sqdwdz", record.getStr("zcdz"));
                                    dataBean.set("sjscqy", record.getStr("scqymc"));
                                    dataBean.set("sjscqydz", record.getStr("scqydz"));
                                }
                                String xkzbh = createJsGcZsbh(task.getItem_id());

                                dataBean.set("pzwh", xkzbh);
                                dataBean.set("yxqs",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 4); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxqz", yxq);
                                dataBean.set("newpzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            } else if ("11370800MB285591843370123018013".equals(task.getItem_id())
                                    || "11370800MB285591843370123018012".equals(task.getItem_id())
                                    || "11370800MB285591843370123018011".equals(task.getItem_id())
                                    || "11370800MB285591843370123018006".equals(task.getItem_id())
                                    || "11370800MB285591843370123018010".equals(task.getItem_id())
                                    || "11370800MB28559184300012310400203".equals(task.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20230222092332",
                                        dzbdguid);
                                if (record != null) {
                                    dataBean.set("sjscqy", record.getStr("xscqymc"));
                                    dataBean.set("sjscqydz", record.getStr("xscqydz"));
                                    dataBean.set("cpmc", record.getStr("xcpmc"));
                                    dataBean.set("cplb", record.getStr("cplb"));
                                    dataBean.set("sqdw", record.getStr("xsqdwmc"));
                                    dataBean.set("sqdwdz", record.getStr("xzcdz"));
                                    dataBean.set("pzwh", record.getStr("wsxkpjh"));
                                    dataBean.set("yxqz", EpointDateUtil.convertDate2String(record.getDate("yxq"),
                                            EpointDateUtil.DATE_FORMAT));
                                }
                                dataBean.set("yxqs",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                dataBean.set("newpzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 4); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxqz", yxq);

                            } else if ("11370800MB285591843370123018004".equals(task.getItem_id())
                                    || "11370800MB285591843370123018014".equals(task.getItem_id())
                                    || "11370800MB28559184300012310400202".equals(task.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20230222093946",
                                        dzbdguid);
                                if (record != null) {
                                    dataBean.set("cpmc", record.getStr("cpmcjxhzw"));
                                    dataBean.set("cplb", record.getStr("cplb"));
                                    dataBean.set("sqdw", record.getStr("zhzrdwmc"));
                                    dataBean.set("sqdwdz", record.getStr("zhzrdwdz"));
                                    dataBean.set("sjscqy", record.getStr("shengcqy"));
                                    dataBean.set("pzwh", record.getStr("wsxkpjh"));
                                    dataBean.set("sjscqydz", record.getStr("scqydz"));
                                    dataBean.set("yxqx", EpointDateUtil.convertDate2String(record.getDate("wsxkpjyxq"),
                                            EpointDateUtil.DATE_FORMAT));
                                }

                                dataBean.set("yxqs",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 4); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxqz", yxq);

                                dataBean.set("newpzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            } else if ("11370800MB285591843370123018003".equals(task.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20230222114242",
                                        dzbdguid);
                                if (record != null) {
                                    dataBean.set("cpmc", record.getStr("cpmcjxh"));
                                    dataBean.set("cplb", record.getStr("cplb"));
                                    dataBean.set("sqdw", record.getStr("shenbdw"));
                                    dataBean.set("sqdwdz", record.getStr("zcdz"));
                                    dataBean.set("sjscqy", record.getStr("scqymc"));
                                    dataBean.set("pzwh", record.getStr("wsxkpjh"));
                                    dataBean.set("sjscqydz", record.getStr("scqydz"));
                                    dataBean.set("yxqz", EpointDateUtil.convertDate2String(record.getDate("yxqx"),
                                            EpointDateUtil.DATE_FORMAT));
                                }
                                dataBean.set("yxqs",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 4); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxqz", yxq);
                                dataBean.set("newpzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                            }
                        }
                    }

                    // 济宁城镇污水
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("排水")) {
                            if ("11370800MB28559184737011702000005".equals(task.getItem_id())
                                    || "11370800MB28559184737011702000004".equals(task.getItem_id())
                                    || "11370800MB28559184300011712300201".equals(task.getItem_id())
                                    || "11370800MB28559184300011712300203".equals(task.getItem_id())
                                    || "11370800MB28559184737011702000001".equals(task.getItem_id())
                                    || "11370800MB28559184300011712300201".equals(task.getItem_id())
                                    || "11370800MB28559184300011712300203".equals(task.getItem_id())
                                    || "11370800MB28559184300011712300202".equals(task.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20230209093743",
                                        dzbdguid);
                                if (record != null) {
                                    dataBean.set("pshmc", record.getStr("sqdw"));
                                    dataBean.set("fddbr", record.getStr("faddbr"));
                                    dataBean.set("tyshxydm", record.getStr("shtyxydm"));
                                    dataBean.set("xxdz", record.getStr("psxwfsddxxdz"));
                                    dataBean.set("pshlx", record.getStr("pshywlx"));
                                    dataBean.set("pwskbh", record.getStr("number"));
                                    dataBean.set("psqx", record.getStr("psqxdlmc"));
                                    dataBean.set("psl", record.getStr("psl"));

                                    if (StringUtil.isNotBlank(record.getStr("pskbh"))) {
                                        dataBean.set("pwskbh", record.getStr("pskbh"));
                                    }
                                    if (StringUtil.isNotBlank(record.getStr("psqx"))) {
                                        dataBean.set("psqx", record.getStr("psqx"));
                                    }
                                    if (StringUtil.isNotBlank(record.getStr("paisl"))) {
                                        dataBean.set("psl", record.getStr("paisl"));
                                    }
                                    if (StringUtil.isNotBlank(record.getStr("paiskbh"))) {
                                        dataBean.set("paiwskbh", record.getStr("paiskbh"));
                                    }
                                    if (StringUtil.isNotBlank(record.getStr("paisqx"))) {
                                        dataBean.set("paisqx", record.getStr("paisqx"));
                                    }
                                    if (StringUtil.isNotBlank(record.getStr("paisl1"))) {
                                        dataBean.set("paisl", record.getStr("paisl1"));
                                    }
                                    if (StringUtil.isNotBlank(record.getStr("paiskbh1"))) {
                                        dataBean.set("paiwuskbh", record.getStr("paiskbh1"));
                                    }
                                    if (StringUtil.isNotBlank(record.getStr("paisqx1"))) {
                                        dataBean.set("paishuiqx", record.getStr("paisqx1"));
                                    }
                                    if (StringUtil.isNotBlank(record.getStr("paisl2"))) {
                                        dataBean.set("pshuil", record.getStr("paisl2"));
                                    }
                                    if (StringUtil.isNotBlank(record.getStr("paiskbh2"))) {
                                        dataBean.set("paiwushuikbh", record.getStr("paiskbh2"));
                                    }
                                    if (StringUtil.isNotBlank(record.getStr("paisqx2"))) {
                                        dataBean.set("paishueiqux", record.getStr("paisqx2"));
                                    }
                                    if (StringUtil.isNotBlank(record.getStr("paisl3"))) {
                                        dataBean.set("psliang", record.getStr("paisl3"));
                                    }
                                    if (StringUtil.isNotBlank(record.getStr("paiskbh3"))) {
                                        dataBean.set("paiwushuikobh", record.getStr("paiskbh3"));
                                    }
                                    if (StringUtil.isNotBlank(record.getStr("paisqx3"))) {
                                        dataBean.set("pshuiquxiang", record.getStr("paisqx3"));
                                    }
                                    if (StringUtil.isNotBlank(record.getStr("paisl4"))) {
                                        dataBean.set("paisliang", record.getStr("paisl4"));
                                    }
                                    if (StringUtil.isNotBlank(record.getStr("paiskbh4"))) {
                                        dataBean.set("pwushuikbh", record.getStr("paiskbh4"));
                                    }
                                    if (StringUtil.isNotBlank(record.getStr("paisqx4"))) {
                                        dataBean.set("paishuiquxiang", record.getStr("paisqx4"));
                                    }
                                    if (StringUtil.isNotBlank(record.getStr("paisl5"))) {
                                        dataBean.set("pshuiliang", record.getStr("paisl5"));
                                    }
                                }
                                String xkzbh = createJsGcZsbh(task.getItem_id());

                                dataBean.set("xkzbh", xkzbh);
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                dataBean.set("yxqq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxqz", yxq);

                            }
                        }
                    }

                    // 建设跨河、穿河、穿堤、临河的桥梁、码头、道路、渡口、管道、缆线、取水、排水等工程设施（原河道管理范围内建设项目工程建设方案审批
                    if (auditProject != null) {
                        if (task != null && "11370800MB285591847370119022002".equals(task.getItem_id())) {
                            String xkzbh = createJsGcZsbh(task.getItem_id());
                            dataBean.set("zzbh", creatNo());
                            dataBean.set("fzsj", new Date());

                            if (auditRsItemBaseinfo != null) {
                                dataBean.set("xmmc", auditRsItemBaseinfo.getItemname());
                                ParticipantsInfo participantsInfo = new JNAuditProjectService()
                                        .getItemlegaldept(auditRsItemBaseinfo.getRowguid());
                                if (participantsInfo != null) {
                                    String itemlegaldept = participantsInfo.getItemlegaldept();
                                    if (StringUtil.isBlank(itemlegaldept)) {
                                        itemlegaldept = participantsInfo.getStr("itemlegaldept");
                                    }
                                    dataBean.put("jsgs", itemlegaldept);
                                }
                            }
                        }
                    }

                    // 济宁供热经营许可证
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("供热经营许可证")) {
                            if ("11370800MB28559184737011702800001".equals(task.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20220921140549",
                                        dzbdguid);
                                if (record != null) {
                                    dataBean.set("qymc", record.getStr("qymc"));
                                    dataBean.set("dwfzr", record.getStr("frxm"));
                                    dataBean.set("dwdz", record.getStr("zcdz"));
                                    dataBean.set("yzbm", record.getStr("yzbm"));
                                    dataBean.set("khyh", record.getStr("khyx"));
                                    dataBean.set("yhzh", record.getStr("yxzh"));
                                    dataBean.set("dwdh", record.getStr("dwdh"));
                                }
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxrq", yxq);

                            } else if ("11370800MB28559184737011702800003".equals(task.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20220922140544",
                                        dzbdguid);
                                if (record != null) {
                                    dataBean.set("qymc", record.getStr("dwmc"));
                                    dataBean.set("dwfzr", record.getStr("frxm"));
                                    dataBean.set("dwdz", record.getStr("dwdz"));
                                    dataBean.set("yzbm", record.getStr("yzbm"));
                                    dataBean.set("khyh", record.getStr("khyx"));
                                    dataBean.set("yhzh", record.getStr("yxzh"));
                                    dataBean.set("dwdh", record.getStr("dwdh"));
                                    dataBean.set("yxrq", record.getStr("bgh"));
                                    dataBean.set("fzrq", record.getStr("bgq"));
                                }
                            } else if ("11370800MB28559184737011702800002".equals(task.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20220921174631",
                                        dzbdguid);
                                if (record != null) {
                                    dataBean.set("zsbh", record.getStr("ygrjyxkzbh"));
                                    dataBean.set("qymc", record.getStr("qymc"));
                                    dataBean.set("dwfzr", record.getStr("fddbrxm"));
                                    dataBean.set("dwdz", record.getStr("dwdz"));
                                    dataBean.set("yzbm", record.getStr("yzbm"));
                                    dataBean.set("khyh", record.getStr("khyx"));
                                    dataBean.set("yhzh", record.getStr("yxzh"));
                                    dataBean.set("dwdh", record.getStr("dwdh"));
                                }
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxrq", yxq);

                            }
                        }
                    }

                    // 济宁水路运输
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("水路运输")) {
                            if ("11370800MB285591847370118009006".equals(task.getItem_id())
                                    || "11370800MB285591847370118009007".equals(task.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("slysqyhzbzsq", dzbdguid);
                                if (record != null) {
                                    dataBean.set("FDDBR", record.getStr("fddbr"));
                                    dataBean.set("JJLX", record.getStr("jjlx"));
                                    dataBean.set("JYQX", record.getStr("jyqx"));
                                    dataBean.set("LKYS", record.getStr("lkys"));
                                    dataBean.set("BH", record.getStr("xkzbh"));
                                    dataBean.set("QYMC", record.getStr("qyzwmc"));
                                    dataBean.set("DZ", record.getStr("qydz"));
                                    dataBean.set("HWYS", record.getStr("hyys"));
                                    dataBean.set("PJJGJWH", record.getStr("pzjgjwh"));
                                    dataBean.set("JY", record.getStr("jiany"));
                                }
                                dataBean.set("QN", EpointDateUtil.convertDate2String(new Date(), "yyyy"));
                                dataBean.set("QY", EpointDateUtil.convertDate2String(new Date(), "MM"));
                                dataBean.set("QR", EpointDateUtil.convertDate2String(new Date(), "dd"));
                                dataBean.set("N", EpointDateUtil.convertDate2String(new Date(), "yyyy"));
                                dataBean.set("Y", EpointDateUtil.convertDate2String(new Date(), "MM"));
                                dataBean.set("R", EpointDateUtil.convertDate2String(new Date(), "dd"));
                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();

                                dataBean.set("ZN", EpointDateUtil.convertDate2String(enddate, "yyyy"));
                                dataBean.set("ZY", EpointDateUtil.convertDate2String(enddate, "MM"));
                                dataBean.set("ZR", EpointDateUtil.convertDate2String(enddate, "dd"));
                            }
                        }
                    }

                    // 济宁消毒产品生产企业
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("消毒产品生产企业")) {
                            if ("11370800MB28559184337012302000102".equals(task.getItem_id())
                                    || "11370800MB28559184337012302000103".equals(task.getItem_id())
                                    || "11370800MB28559184337012302000101".equals(task.getItem_id())
                                    || "11370800MB28559184300012310800001".equals(task.getItem_id())
                                    || "11370800MB28559184300012310800002".equals(task.getItem_id())
                                    || "11370800MB28559184300012310800003".equals(task.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20220803163352",
                                        dzbdguid);
                                if (record != null) {
                                    String bh = createJsGcZsbh(task.getItem_id());
                                    dataBean.set("dwmc", record.getStr("dwmc"));
                                    dataBean.set("fddbr", record.getStr("fddbr"));
                                    dataBean.set("zcdz", record.getStr("zcdz"));
                                    dataBean.set("scdz", record.getStr("scdz"));
                                    dataBean.set("scfs", record.getStr("scfs"));
                                    dataBean.set("scxm", record.getStr("scxm"));
                                    dataBean.set("xmlb", record.getStr("xmlb"));
                                    dataBean.set("tyxydm", record.getStr("tyxydm"));
                                    dataBean.set("zsbh", bh);

                                }
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                dataBean.set("yxqs", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 4); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxqz", yxq);

                            } else if ("11370800MB28559184337012302000403".equals(task.getItem_id())
                                    || "11370800MB285591843370123020003".equals(task.getItem_id())
                                    || "11370800MB28559184337012302000402".equals(task.getItem_id())
                                    || "11370800MB28559184337012302000401".equals(task.getItem_id())
                                    || "11370800MB28559184300012310800003".equals(task.getItem_id())
                            ) {
                                Record record = iCxBusService.getDzbdDetail("formtable20220825090801",
                                        dzbdguid);
                                if (record != null) {
                                    dataBean.set("zsbh", record.getStr("zsbh"));
                                    dataBean.set("dwmc", record.getStr("bghdwmc"));
                                    dataBean.set("fddbr", record.getStr("bghfddbr"));
                                    dataBean.set("zcdz", record.getStr("bghzcdz"));
                                    dataBean.set("scdz", record.getStr("bghscdz"));
                                    dataBean.set("scfs", record.getStr("bghscfs"));
                                    dataBean.set("scxm", record.getStr("bghscxm"));
                                    dataBean.set("xmlb", record.getStr("bghsclb"));
                                    dataBean.set("tyxydm", record.getStr("tyxydm"));

                                }
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                dataBean.set("yxqs", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 4); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxqz", yxq);
                            }
                        }
                    }

                    // 危险化学品经营许可证新办
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("危险化学品经营许可证")) {
                            if ("11370800004313485J737012501200002".equals(task.getItem_id())
                                    || "11370800004313485J737012501200001".equals(task.getItem_id())
                                    || "11370800004313485J737012501200003".equals(task.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20220811181013",
                                        dzbdguid);
                                if (record != null) {
                                    dataBean.set("tyxydm", record.getStr("tyxydm"));
                                    dataBean.set("xkfw", record.getStr("xkfw"));
                                    dataBean.set("jyfs", record.getStr("jyfs"));
                                    dataBean.set("fddbr", record.getStr("fddbr"));
                                    dataBean.set("qydz", record.getStr("qydz"));
                                    dataBean.set("qymc", record.getStr("qymc"));
                                }
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                dataBean.set("yxqs", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 3); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxqz", yxq);

                            }
                        }
                    }

                    // 城市大型户外广告设置,在城市建筑物、设施上张挂、张贴宣传品审批
                    if (auditProject != null) {
                        try {
                            Record record = null;
                            if ("11370800MB28559184737011702400002".equals(task.getItem_id())
                                    || "11370800MB28559184737011702400003".equals(task.getItem_id())
                                    || "11370800MB28559184737011702400004".equals(task.getItem_id())) {
                                record = iCxBusService.getDzbdDetail("formtable20230418162643",
                                        dzbdguid);

                            } else if ("11370800MB285591847370117036000".equals(task.getItem_id())
                                    || "11370800MB28559184300011713900501".equals(task.getItem_id())) {
                                record = iCxBusService.getDzbdDetail("formtable20230418111305",
                                        dzbdguid);
                            }
                            if (record != null) {
                                dataBean.set("sqsj", record.getDate("rqxz1"));
                                dataBean.set("zsbh", record.get("wbk1"));
                                dataBean.set("sqdw", record.get("wbk2"));
                                dataBean.set("szqx",
                                        EpointDateUtil.convertDate2String(record.getDate("rqfwxz1"),
                                                EpointDateUtil.DATE_FORMAT) + "~"
                                                + EpointDateUtil.convertDate2String(record.getDate("rqfwxz1end"),
                                                EpointDateUtil.DATE_FORMAT));
                                dataBean.set("fzsj", new Date());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.info("渲染证照失败：" + e.getMessage());
                        }
                    }
                    // 建筑业企业资质（简单变更）（企业名称变更）、建筑业企业资质（简单变更）（企业地址变更）、建筑业企业资质（简单变更）（经济性质变更）、建筑业企业资质（简单变更）（注册资本变更）、
                    // 建筑业企业资质（简单变更）（多项变更）、建筑业企业资质（简单变更）（法人变更）
                    if (auditProject != null) {
                        try {
                            Record record = null;
                            if ("1137080000431212413370117100138".equals(task.getItem_id())
                                    || "1137080000431212413370117100136".equals(task.getItem_id())
                                    || "1137080000431212413370117100139".equals(task.getItem_id())
                                    || "1137080000431212413370117100140".equals(task.getItem_id())
                                    || "1137080000431212413370117100141".equals(task.getItem_id())
                                    || "1137080000431212413370117100137".equals(task.getItem_id())) {
                                record = iCxBusService.getDzbdDetail("formtable20221109165008",
                                        dzbdguid);

                            }
                            if (record != null) {
                                dataBean.set("zsbh", record.getStr("zsbh"));
                                dataBean.set("qymc", record.get("qymc"));
                                dataBean.set("xxdz", record.get("xxdz"));
                                dataBean.set("tyxydm", record.getStr("tyxydm"));
                                dataBean.set("jjxz", record.getStr("jjxz"));
                                dataBean.set("zzlbjdj", record.getStr("zzlbjdj"));
                                dataBean.set("frdb", record.getStr("frdb"));
                                dataBean.set("remark",
                                        "注册资本金：" + record.getStr("zczbj") + " 法人代表：" + record.getStr("frdb"));

                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxq", yxq);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            log.info("渲染证照失败：" + e.getMessage());
                        }
                    }

                    // 济宁人防工程竣工验收备案
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("人防工程竣工验收备案")) {
                            if ("11370800MB285591847371043007000".equals(task.getItem_id())) {
                                AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService
                                        .findAuditSpSpJgysBySubappGuid(auditProject.getSubappguid());
                                if (auditSpSpJgys != null) {
                                    dataBean.set("bdcdyh", auditSpSpJgys.getStr("bdcdanyuanhao"));
                                }
                            } else if ("11370827MB2857833K4371043007000".equals(task.getItem_id())) {

                                String jsdwname = "";
                                String jsdwxmfzr = "";
                                String itemname = "";
                                String itemaddress = "";
                                String kcdwname = "";
                                String kcdwxmfzr = "";
                                String sjdwname = "";
                                String sjdwxmfzr = "";
                                String sgdwname = "";
                                String sgdwxmfzr = "";
                                String jldwname = "";
                                String jldwxmfzr = "";

                                AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService
                                        .findAuditSpSpJgysBySubappGuid(auditProject.getSubappguid());
                                if (auditSpSpJgys != null) {
                                    itemname = auditSpSpJgys.getItemname();
                                    itemaddress = auditSpSpJgys.getItemaddress();
                                    dataBean.set("kgrq", EpointDateUtil.convertDate2String(auditSpSpJgys.getStartdate(),
                                            "yyyy-MM-dd"));
                                    dataBean.set("jgysrq", EpointDateUtil.convertDate2String(auditSpSpJgys.getEnddate(),
                                            "yyyy-MM-dd"));
                                    dataBean.set("bdcdyh", auditSpSpJgys.getStr("bdcdanyuanhao"));

                                }

                                String itemtype = "01";
                                if (auditRsItemBaseinfo != null) {
                                    itemtype = auditRsItemBaseinfo.getItemtype();
                                    if (!"01".equals(itemtype)) {
                                        itemtype = "02";
                                    }

                                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getParentid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                            jsdwxmfzr = participantsInfoList.get(0).getXmfzr();
                                        }
                                    } else {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getRowguid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                            jsdwxmfzr = participantsInfoList.get(0).getXmfzr();
                                        }
                                    }

                                }

                                SqlConditionUtil sql = new SqlConditionUtil();
                                sql.nq("corptype", "999");
                                sql.eq("subappguid", auditProject.getSubappguid());
                                List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                        .getParticipantsInfoListByCondition(sql.getMap());
                                if (!participantsInfoList.isEmpty()) {
                                    for (ParticipantsInfo partic : participantsInfoList) {
                                        if ("31".equals(partic.getCorptype())) {
                                            jsdwname = partic.getCorpname();
                                            jsdwxmfzr = partic.getXmfzr();
                                        } else if ("1".equals(partic.getCorptype())) {
                                            kcdwname = partic.getCorpname();
                                            kcdwxmfzr = partic.getXmfzr();
                                        } else if ("2".equals(partic.getCorptype())) {
                                            sjdwname = partic.getCorpname();
                                            sjdwxmfzr = partic.getXmfzr();
                                        } else if ("3".equals(partic.getCorptype())) {
                                            sgdwname = partic.getCorpname();
                                            sgdwxmfzr = partic.getXmfzr();
                                        } else if ("4".equals(partic.getCorptype())) {
                                            jldwname = partic.getCorpname();
                                            jldwxmfzr = partic.getXmfzr();
                                        }
                                    }
                                }
                                dataBean.set("jsdwxx", jsdwname);
                                dataBean.set("jsdwfzr", jsdwxmfzr);
                                dataBean.set("gcmc", itemname);
                                dataBean.set("gcdd", itemaddress);
                                dataBean.set("kcdwmc", kcdwname);
                                dataBean.set("kcdwxmfzr", kcdwxmfzr);
                                dataBean.set("sjdwmc", sjdwname);
                                dataBean.set("sjdwxmfzr", sjdwxmfzr);
                                dataBean.set("sgdwmc", sgdwname);
                                dataBean.set("sgdwfzr", sgdwxmfzr);
                                dataBean.set("jldwmc", jldwname);
                                dataBean.set("jldwfzr", jldwxmfzr);
                                dataBean.set("rq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                            } else if ("11370830F5034613XX4371043007000".equals(task.getItem_id())
                                    || "11370826MB2858051T4371043007000".equals(task.getItem_id())

                            ) {

                                String jsdwname = "";
                                String jsdwxmfzr = "";
                                String itemname = "";
                                String itemaddress = "";
                                String kcdwname = "";
                                String kcdwxmfzr = "";
                                String sjdwname = "";
                                String sjdwxmfzr = "";
                                String sgdwname = "";
                                String sgdwxmfzr = "";
                                String jldwname = "";
                                String jldwxmfzr = "";

                                AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService
                                        .findAuditSpSpJgysBySubappGuid(auditProject.getSubappguid());
                                if (auditSpSpJgys != null) {
                                    itemname = auditSpSpJgys.getItemname();
                                    itemaddress = auditSpSpJgys.getItemaddress();
                                    dataBean.set("kgrq", EpointDateUtil.convertDate2String(auditSpSpJgys.getStartdate(),
                                            "yyyy-MM-dd"));
                                    dataBean.set("jgysrq", EpointDateUtil.convertDate2String(auditSpSpJgys.getEnddate(),
                                            "yyyy-MM-dd"));
                                    dataBean.set("bdcdyh", auditSpSpJgys.getStr("bdcdanyuanhao"));

                                }
                                
                                String itemtype = "01";
                                if (auditRsItemBaseinfo != null) {
                                    itemtype = auditRsItemBaseinfo.getItemtype();
                                    if (!"01".equals(itemtype)) {
                                        itemtype = "02";
                                    }

                                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getParentid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                            jsdwxmfzr = participantsInfoList.get(0).getXmfzr();
                                        }
                                    } else {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getRowguid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                            jsdwxmfzr = participantsInfoList.get(0).getXmfzr();
                                        }
                                    }

                                }

                                if ("11370826MB2858051T4371043007000".equals(task.getItem_id())) {
                                    String numberName = "Weis_RsgcYs_Type_Num";
                                    Calendar calendar = Calendar.getInstance();
                                    String numberFlag = "" + calendar.get(Calendar.YEAR)
                                            + String.format("%02d", calendar.get(Calendar.MONTH) + 1)
                                            + String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
                                    int theYearLength = 0;
                                    int snLength = 2;
                                    String certno = new DBServcie().getFlowSn(numberName, numberFlag, theYearLength,
                                            false, snLength);

                                    dataBean.set("bh", certno);
                                } else {
                                    String numberName = "Ws_RsgcYs_Type_Num";
                                    Calendar calendar = Calendar.getInstance();
                                    String numberFlag = "" + calendar.get(Calendar.YEAR)
                                            + String.format("%02d", calendar.get(Calendar.MONTH) + 1)
                                            + String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
                                    int theYearLength = 0;
                                    int snLength = 2;
                                    String certno = new DBServcie().getFlowSn(numberName, numberFlag, theYearLength,
                                            false, snLength);

                                    dataBean.set("bh", certno);
                                }

                                SqlConditionUtil sql = new SqlConditionUtil();
                                sql.nq("corptype", "999");
                                sql.eq("subappguid", auditProject.getSubappguid());
                                List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                        .getParticipantsInfoListByCondition(sql.getMap());
                                if (!participantsInfoList.isEmpty()) {
                                    for (ParticipantsInfo partic : participantsInfoList) {
                                        if ("31".equals(partic.getCorptype())) {
                                            jsdwname = partic.getCorpname();
                                            jsdwxmfzr = partic.getXmfzr();
                                        } else if ("1".equals(partic.getCorptype())) {
                                            kcdwname = partic.getCorpname();
                                            kcdwxmfzr = partic.getXmfzr();
                                        } else if ("2".equals(partic.getCorptype())) {
                                            sjdwname = partic.getCorpname();
                                            sjdwxmfzr = partic.getXmfzr();
                                        } else if ("3".equals(partic.getCorptype())) {
                                            sgdwname = partic.getCorpname();
                                            sgdwxmfzr = partic.getXmfzr();
                                        } else if ("4".equals(partic.getCorptype())) {
                                            jldwname = partic.getCorpname();
                                            jldwxmfzr = partic.getXmfzr();
                                        }
                                    }
                                }
                                dataBean.set("jsdwxx", jsdwname);
                                dataBean.set("jsdwfzr", jsdwxmfzr);
                                dataBean.set("gcmc", itemname);
                                dataBean.set("gcdd", itemaddress);
                                dataBean.set("kcdwmc", kcdwname);
                                dataBean.set("kcdwxmfzr", kcdwxmfzr);
                                dataBean.set("sjdwmc", sjdwname);
                                dataBean.set("sjdwxmfzr", sjdwxmfzr);
                                dataBean.set("sgdwmc", sgdwname);
                                dataBean.set("sgdwfzr", sgdwxmfzr);
                                dataBean.set("jldwmc", jldwname);
                                dataBean.set("jldwfzr", jldwxmfzr);
                                dataBean.set("rq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                            }
                        }
                    }
                    // 济宁建设工程（含地下管线工程）档案验收
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("建设工程（含地下管线工程）档案验收")) {
                            if ("1137080000431212413371017014000".equals(task.getItem_id())) {
                                AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService
                                        .findAuditSpSpJgysBySubappGuid(auditProject.getSubappguid());
                                if (auditSpSpJgys != null) {
                                    dataBean.set("bdcdyh", auditSpSpJgys.getStr("bdcdanyuanhao"));
                                }
                            } else if ("11370827004319764X7371017014000".equals(task.getItem_id())) {
                                String jsdwname = "";
                                String itemname = "";
                                String itemaddress = "";
                                String kcdwname = "";
                                String sjdwname = "";
                                String sgdwname = "";
                                String jldwname = "";

                                AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService
                                        .findAuditSpSpJgysBySubappGuid(auditProject.getSubappguid());
                                if (auditSpSpJgys != null) {
                                    itemname = auditSpSpJgys.getItemname();
                                    itemaddress = auditSpSpJgys.getItemaddress();
                                    dataBean.set("kgrq", EpointDateUtil.convertDate2String(auditSpSpJgys.getStartdate(),
                                            "yyyy-MM-dd"));
                                    dataBean.set("jgrq", EpointDateUtil.convertDate2String(auditSpSpJgys.getEnddate(),
                                            "yyyy-MM-dd"));
                                    dataBean.set("bdcdyh", auditSpSpJgys.getStr("bdcdanyuanhao"));

                                }
                                String itemtype = "01";
                                if (auditRsItemBaseinfo != null) {
                                    itemtype = auditRsItemBaseinfo.getItemtype();
                                    if (!"01".equals(itemtype)) {
                                        itemtype = "02";
                                    }

                                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getParentid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                        }
                                    } else {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getRowguid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                        }
                                    }

                                }
                                String numberName = "Ytjsgcdx_Prj_Type_Num";
                                Calendar calendar = Calendar.getInstance();
                                String numberFlag = "" + String.format("%02d", calendar.get(Calendar.MONTH) + 1)
                                        + String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
                                int theYearLength = 0;
                                int snLength = 2;
                                String certno = new DBServcie().getFlowSn(numberName, numberFlag, theYearLength, false,
                                        snLength);
                                String Prj_Finish_Num = auditProject.getAreacode() + certno + itemtype;
                                dataBean.set("bh", Prj_Finish_Num);

                                SqlConditionUtil sql = new SqlConditionUtil();
                                sql.nq("corptype", "999");
                                sql.eq("subappguid", auditProject.getSubappguid());
                                List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                        .getParticipantsInfoListByCondition(sql.getMap());
                                if (!participantsInfoList.isEmpty()) {
                                    for (ParticipantsInfo partic : participantsInfoList) {
                                        if ("31".equals(partic.getCorptype())) {
                                            jsdwname = partic.getCorpname();
                                        } else if ("1".equals(partic.getCorptype())) {
                                            kcdwname = partic.getCorpname();
                                        } else if ("2".equals(partic.getCorptype())) {
                                            sjdwname = partic.getCorpname();
                                        } else if ("3".equals(partic.getCorptype())) {
                                            sgdwname = partic.getCorpname();
                                        } else if ("4".equals(partic.getCorptype())) {
                                            jldwname = partic.getCorpname();
                                        }
                                    }
                                }
                                dataBean.set("gcmc", itemname);
                                dataBean.set("gcdz", itemaddress);
                                dataBean.set("jsdw", jsdwname);
                                dataBean.set("kcdw", kcdwname);
                                dataBean.set("sgdw", sgdwname);
                                dataBean.set("sjdw", sjdwname);
                                dataBean.set("jldw", jldwname);
                                dataBean.set("gzrq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));

                            } else if ("1137083000433521104371017014000".equals(task.getItem_id())
                                    || "1137082600452807284371017014000".equals(task.getItem_id())

                            ) {
                                String jsdwname = "";
                                String itemname = "";
                                String itemaddress = "";
                                String kcdwname = "";
                                String sjdwname = "";
                                String sgdwname = "";
                                String jldwname = "";

                                AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService
                                        .findAuditSpSpJgysBySubappGuid(auditProject.getSubappguid());
                                if (auditSpSpJgys != null) {
                                    itemname = auditSpSpJgys.getItemname();
                                    itemaddress = auditSpSpJgys.getItemaddress();
                                    dataBean.set("kgrq", EpointDateUtil.convertDate2String(auditSpSpJgys.getStartdate(),
                                            "yyyy-MM-dd"));
                                    dataBean.set("jgrq", EpointDateUtil.convertDate2String(auditSpSpJgys.getEnddate(),
                                            "yyyy-MM-dd"));
                                    dataBean.set("bdcdyh", auditSpSpJgys.getStr("bdcdanyuanhao"));

                                }

                                String itemtype = "01";
                                if (auditRsItemBaseinfo != null) {
                                    itemtype = auditRsItemBaseinfo.getItemtype();
                                    if (!"01".equals(itemtype)) {
                                        itemtype = "02";
                                    }

                                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getParentid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                        }
                                    } else {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getRowguid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                        }
                                    }

                                }

                                String idnum = createJsGcZsbh(task.getItem_id());
                                dataBean.set("bh", idnum);

                                SqlConditionUtil sql = new SqlConditionUtil();
                                sql.nq("corptype", "999");
                                sql.eq("subappguid", auditProject.getSubappguid());
                                List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                        .getParticipantsInfoListByCondition(sql.getMap());
                                if (!participantsInfoList.isEmpty()) {
                                    for (ParticipantsInfo partic : participantsInfoList) {
                                        if ("31".equals(partic.getCorptype())) {
                                            jsdwname = partic.getCorpname();
                                        } else if ("1".equals(partic.getCorptype())) {
                                            kcdwname = partic.getCorpname();
                                        } else if ("2".equals(partic.getCorptype())) {
                                            sjdwname = partic.getCorpname();
                                        } else if ("3".equals(partic.getCorptype())) {
                                            sgdwname = partic.getCorpname();
                                        } else if ("4".equals(partic.getCorptype())) {
                                            jldwname = partic.getCorpname();
                                        }
                                    }
                                }
                                dataBean.set("gcmc", itemname);
                                dataBean.set("gcdz", itemaddress);
                                dataBean.set("jsdw", jsdwname);
                                dataBean.set("kcdw", kcdwname);
                                dataBean.set("sgdw", sgdwname);
                                dataBean.set("sjdw", sjdwname);
                                dataBean.set("jldw", jldwname);
                                dataBean.set("gzrq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));

                            }
                        }
                    }

                    // 鱼台
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("建设用地规划许可证")) {
                            if ("11370827MB2857833K4370115055000".equals(task.getItem_id())) {
                                String idnum = createJsGcZsbh(task.getItem_id());
                                if (auditSpISubapp != null) {
                                    AuditRsItemBaseinfo baseinfo = iAuditRsItemBaseinfo
                                            .getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
                                    if (baseinfo != null) {
                                        dataBean.set("xmmc", baseinfo.getItemname());
                                        dataBean.set("ydwz", baseinfo.getConstructionsite());
                                        dataBean.set("ydmj", baseinfo.getLandarea());
                                    }
                                }
                                dataBean.set("bh", idnum);
                                dataBean.set("riqi", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                                dataBean.set("fzjg", "鱼台县行政审批服务局");

                            } else if ("11370800MB285591843370115055000".equals(task.getItem_id())
                                    || "11370800MB28559184337011505500002".equals(task.getItem_id())
                                    || "11370800MB28559184337011505500001".equals(task.getItem_id())
                                    || "11370800MB28559184337011505500003".equals(task.getItem_id())
                                    || "11370800MB2855934R300011513100201".equals(task.getItem_id())) {
                                String jsdwname = "";
                                AuditSpSpLxydghxk auditSpSpLxydghxk = iAuditSpSpLxydghxkService
                                        .findAuditSpSpLxydghxkBysubappguid(auditProject.getSubappguid());
                                if (auditSpSpLxydghxk != null) {
                                    dataBean.set("ydxmmc", auditSpSpLxydghxk.getItemname());
                                    dataBean.set("ydxmbm", auditSpSpLxydghxk.getItemcode());
                                    dataBean.set("pzydjg", auditSpSpLxydghxk.getStr("pzydjg"));
                                    dataBean.set("pzydwh", auditSpSpLxydghxk.getStr("pzydwh"));
                                    dataBean.set("ydxz", auditSpSpLxydghxk.getStr("tdyt"));
                                    dataBean.set("jsgm", auditSpSpLxydghxk.getStr("jsgm"));
                                    dataBean.set("ydwz", auditSpSpLxydghxk.getItemaddress());
                                    dataBean.set("ydmj", auditSpSpLxydghxk.getAreaused());
                                }
                                // String idnum =
                                // createJsGcZsbh(auditTask.getItem_id());
                                if (auditRsItemBaseinfo != null) {
                                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getTdhqfs())) {
                                        String tdhqfs = iCodeItemsService.getItemTextByCodeName("土地获取方式",
                                                auditRsItemBaseinfo.getTdhqfs() + "");
                                        dataBean.set("tdqdfs", tdhqfs);
                                    }
                                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getParentid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();

                                        }
                                    } else {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getRowguid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                        }
                                    }
                                }
                                dataBean.set("yddw", jsdwname);
                                dataBean.set("rq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));

                            }
                        }
                    }

                    // 建筑工程施工许可证核发
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("建筑工程施工许可证核发")) {
                            if ("鱼台防空地下室建设审批意见书".equals(certCatalog.getCertname())) {
                                String jsdwname = "";
                                String idnum = createJsGcZsbh(task.getItem_id());
                                AuditSpSpSgxk sgxk = iAuditSpSpSgxkService
                                        .findAuditSpSpSgxkBysubappguid(auditProject.getSubappguid());
                                if (sgxk != null) {
                                    dataBean.set("xmmc", sgxk.getItemname());
                                    dataBean.set("jsdd", sgxk.getItemaddress());
                                }
                                if (auditRsItemBaseinfo != null) {
                                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getParentid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                        }
                                    } else {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getRowguid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                        }
                                    }
                                }

                                dataBean.set("jsdw", jsdwname);
                                dataBean.set("bh", idnum);
                                dataBean.set("sprq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                                dataBean.set("spjg", "鱼台县行政审批服务局");

                            } else {
                            }
                        }
                    }

                    // 建筑工程施工许可证核发
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("建设工程消防设计审查")) {
                            if ("1137080000431212413370117043000".equals(task.getItem_id())) {
                                String idnum = createJsGcZsbh(task.getItem_id());
                                AuditSpSpSgxk sgxk = iAuditSpSpSgxkService
                                        .findAuditSpSpSgxkBysubappguid(auditProject.getSubappguid());
                                if (sgxk != null) {
                                    dataBean.set("xmmc", sgxk.getItemname());
                                    dataBean.set("xmdz", sgxk.getItemaddress());
                                    dataBean.set("sgcscbh", sgxk.getStr("sgtsxmbh"));
                                }
                                dataBean.set("zsbh", "济建消审字" + idnum);
                                dataBean.set("sqwh", "济建消审受字" + idnum);
                                dataBean.set("fzrq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                                dataBean.set("sqrq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                            }
                        }
                    }

                    // 城市建筑垃圾处置核准
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("建筑垃圾处置核准")) {
                            if ("370811".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(task.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370828".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(task.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370882".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(task.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370892".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(task.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370826".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(task.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370827".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(task.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370829".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(task.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370830".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(task.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370831".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(task.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370832".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(task.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370881".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(task.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370883".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(task.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370890".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(task.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370891".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(task.getItem_id());
                                dataBean.set("zzbh", idnum);
                            }

                            if (!"370800".equals(auditProject.getAreacode())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20220420093558",
                                        dzbdguid);
                                if (record != null) {
                                    dataBean.set("dwmc", record.getStr("dwmc"));
                                    dataBean.set("yslx", record.getStr("yslx"));
                                    dataBean.set("czrqks", EpointDateUtil.convertDate2String(record.getDate("czqxrq"),
                                            EpointDateUtil.DATE_FORMAT));
                                    dataBean.set("czrqjs", EpointDateUtil.convertDate2String(
                                            record.getDate("czqxrqend"), EpointDateUtil.DATE_FORMAT));
                                    dataBean.set("sqsj", EpointDateUtil.convertDate2String(record.getDate("sqrq"),
                                            EpointDateUtil.DATE_FORMAT));
                                    dataBean.set("fzrq",
                                            EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                }
                            }
                        }
                    }

                    // 建设工程规划许可证
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("建设工程规划许可证")) {
                            if ("11370800MB285591843370115056000".equals(task.getItem_id())
                                    || "11370800MB2855934R300011513300301".equals(task.getItem_id())
                                    || "11370826MB2858051T4370115056000".equals(task.getItem_id())
                                    || "11370826MB2858051T437011505600001".equals(task.getItem_id())
                                    || "11370826MB2858051T437011505600005".equals(task.getItem_id())) {
                                String jsdwname = "";
                                String itemname = "";
                                String itemaddress = "";
                                String jsgm = ""; // 建设规模
                                AuditSpSpGcjsxk auditSpSpGcjsxk = iAuditSpSpGcjsxkService
                                        .findAuditSpSpGcjsxkBySubappGuid(auditProject.getSubappguid());
                                if (auditSpSpGcjsxk != null) {
                                    itemname = auditSpSpGcjsxk.getItemname();
                                    jsgm = auditSpSpGcjsxk.getStr("jsgm");
                                    itemaddress = auditSpSpGcjsxk.getItemaddress();

                                    dataBean.set("bdcdyh", auditSpSpGcjsxk.getStr("bdcdanyuanhao"));
                                }

                                dataBean.set("jsdw", jsdwname);
                                dataBean.set("jsxmmc", itemname);
                                dataBean.set("jswz", itemaddress);
                                dataBean.set("jsgm", jsgm);

                                if ("11370826MB2858051T4370115056000".equals(task.getItem_id())
                                        || "11370826MB2858051T437011505600001".equals(task.getItem_id())
                                        || "11370826MB2858051T437011505600005".equals(task.getItem_id())) {
                                    dataBean.set("fzjg", "微山县行政审批服务局");

                                } else {
                                    dataBean.set("fzjg", "济宁市行政审批服务局");

                                }
                                dataBean.set("rq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                            } else if ("11370827MB2857833K4370115056000".equals(task.getItem_id())) {
                                String idnum = createJsGcZsbh(task.getItem_id());
                                dataBean.set("bh", idnum);

                                String jsdwname = "";
                                String itemname = "";
                                String itemaddress = "";
                                String jsgm = ""; // 建设规模

                                AuditSpSpGcjsxk auditSpSpGcjsxk = iAuditSpSpGcjsxkService
                                        .findAuditSpSpGcjsxkBySubappGuid(auditProject.getSubappguid());
                                if (auditSpSpGcjsxk != null) {
                                    itemname = auditSpSpGcjsxk.getItemname();
                                    jsgm = auditSpSpGcjsxk.getStr("Allbuildarea");
                                    itemaddress = auditSpSpGcjsxk.getItemaddress();
                                    dataBean.set("bdcdyh", auditSpSpGcjsxk.getStr("bdcdanyuanhao"));
                                }

                                if (auditRsItemBaseinfo != null) {
                                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getParentid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                        }
                                    } else {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getRowguid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                        }
                                    }
                                }
                                dataBean.set("fzjg", "鱼台县行政审批服务局");
                                dataBean.set("riqi",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                dataBean.set("jsdw", jsdwname);
                                dataBean.set("jsxmmc", itemname);
                                dataBean.set("jswz", itemaddress);
                                dataBean.set("jsgm", jsgm);
                            }

                        }
                    }

                    // 建设工程竣工规划核实
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("建设工程竣工规划核实")) {
                            if ("11370800MB285591847370715007000".equals(task.getItem_id())) {
                                String jsdwname = "";
                                String itemname = "";
                                String itemaddress = "";
                                String jsgm = ""; // 建设规模
                                AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService
                                        .findAuditSpSpJgysBySubappGuid(auditProject.getSubappguid());
                                if (auditSpSpJgys != null) {
                                    itemname = auditSpSpJgys.getItemname();
                                    jsgm = auditSpSpJgys.getStr("jsgm");
                                    itemaddress = auditSpSpJgys.getItemaddress();
                                    dataBean.set("bdcdyh", auditSpSpJgys.getStr("bdcdanyuanhao"));
                                }
                                dataBean.set("jsdw", jsdwname);
                                dataBean.set("jsxmmc", itemname);
                                dataBean.set("jswz", itemaddress);
                                dataBean.set("jsgm", jsgm);
                                dataBean.set("fzjg", "济宁市行政审批服务局");
                                dataBean.set("rq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                            } else if ("11370830F5034613XX4370715007000".equals(task.getItem_id())
                                    || "11370826MB286081384370715007000".equals(task.getItem_id())

                            ) {

                                String idnum = createJsGcZsbh(task.getItem_id());
                                dataBean.set("bh", idnum);

                                String jsdwname = "";
                                String itemname = "";
                                String itemaddress = "";
                                String jsgm = ""; // 建设规模
                                AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService
                                        .findAuditSpSpJgysBySubappGuid(auditProject.getSubappguid());
                                if (auditSpSpJgys != null) {
                                    itemname = auditSpSpJgys.getItemname();
                                    jsgm = auditSpSpJgys.getStr("jsgm");
                                    itemaddress = auditSpSpJgys.getItemaddress();
                                    dataBean.set("bdcdyh", auditSpSpJgys.getStr("bdcdanyuanhao"));
                                }
                                dataBean.set("jsdw", jsdwname);
                                dataBean.set("jsxmmc", itemname);
                                dataBean.set("jswz", itemaddress);
                                dataBean.set("jsgm", jsgm);
                                if ("11370826MB286081384370715007000".equals(task.getItem_id())) {
                                    dataBean.set("fzjg", "微山县行政审批服务局");
                                } else {
                                    dataBean.set("fzjg", "汶上县行政审批服务局");
                                }

                                dataBean.set("rq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            } else if ("11370827MB2857833K4370715007000".equals(task.getItem_id())) {
                                String idnum = createJsGcZsbh(task.getItem_id());
                                dataBean.set("bh", idnum);

                                String jsdwname = "";
                                String itemname = "";
                                String itemaddress = "";
                                String jsgm = ""; // 建设规模

                                if (auditRsItemBaseinfo != null) {
                                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getParentid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                        }
                                    } else {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getRowguid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                        }
                                    }
                                }

                                AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService
                                        .findAuditSpSpJgysBySubappGuid(auditProject.getSubappguid());
                                if (auditSpSpJgys != null) {
                                    itemname = auditSpSpJgys.getItemname();
                                    jsgm = auditSpSpJgys.getStr("Allbuildarea");
                                    itemaddress = auditSpSpJgys.getItemaddress();
                                    dataBean.set("bdcdyh", auditSpSpJgys.getStr("bdcdanyuanhao"));
                                }

                                if (auditRsItemBaseinfo != null) {
                                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getParentid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                        }
                                    } else {
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("itemGuid", auditRsItemBaseinfo.getRowguid());
                                        sql.eq("corptype", "31");
                                        List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                                .getParticipantsInfoListByCondition(sql.getMap());
                                        if (participantsInfoList.size() > 0) {
                                            jsdwname = participantsInfoList.get(0).getCorpname();
                                        }
                                    }
                                }

                                dataBean.set("fzjg", "鱼台县行政审批服务局");
                                dataBean.set("rq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                dataBean.set("jsdw", jsdwname);
                                dataBean.set("jsxmmc", itemname);
                                dataBean.set("jswz", itemaddress);
                                dataBean.set("jsgm", jsgm);
                            }
                        }
                    }

                    // 招标控制价备案
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("依法必须进行招标的相关工程建设项目招标范围、招标方式、招标组织形式核准")) {
                            String jsdwname = "";
                            AuditSpSpLxydghxk auditSpSpLxydghxk = iAuditSpSpLxydghxkService
                                    .findAuditSpSpLxydghxkBysubappguid(auditProject.getSubappguid());
                            if (auditSpSpLxydghxk != null) {
                                dataBean.set("xmmc", auditSpSpLxydghxk.getItemname());
                            }

                            if (auditRsItemBaseinfo != null) {
                                if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                                    SqlConditionUtil sql = new SqlConditionUtil();
                                    sql.eq("itemGuid", auditRsItemBaseinfo.getParentid());
                                    sql.eq("corptype", "31");
                                    List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                            .getParticipantsInfoListByCondition(sql.getMap());
                                    if (participantsInfoList.size() > 0) {
                                        jsdwname = participantsInfoList.get(0).getCorpname();
                                    }
                                } else {
                                    SqlConditionUtil sql = new SqlConditionUtil();
                                    sql.eq("itemGuid", auditRsItemBaseinfo.getRowguid());
                                    sql.eq("corptype", "31");
                                    List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                                            .getParticipantsInfoListByCondition(sql.getMap());
                                    if (participantsInfoList.size() > 0) {
                                        jsdwname = participantsInfoList.get(0).getCorpname();
                                    }
                                }

                            }
                            dataBean.set("xmdw", jsdwname);
                            dataBean.set("fzrq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));

                        }
                    }

                    // 招标控制价备案
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("招标控制价备案")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220810102040",
                                    dzbdguid);
                            if (record != null) {
                                dataBean.set("zbr", record.getStr("zbr"));
                                dataBean.set("gcmc", record.getStr("gcmc"));
                                dataBean.set("gcdd", record.getStr("gcdd"));
                                dataBean.set("jglx", record.getStr("jglx"));
                                dataBean.set("yg", record.getStr("yg"));
                                dataBean.set("cs", record.getStr("cs"));
                                dataBean.set("kd", record.getStr("kd"));
                                dataBean.set("jsgm", record.getStr("jsgm"));
                                dataBean.set("zbkzj", record.getStr("zbkzj"));
                                dataBean.set("zjly", record.getStr("zjly"));
                                dataBean.set("bzdw", record.getStr("bzdw"));
                                dataBean.set("zzzsbh", record.getStr("zzzsbh"));
                                dataBean.set("bzry", record.getStr("bzryxm"));
                                dataBean.set("bzryx", record.getStr("bzryxm1"));
                                dataBean.set("zyyzbh", record.getStr("zyyzbh"));
                                dataBean.set("zyyzb", record.getStr("zyyzbh1"));
                                dataBean.set("bzrym", record.getStr("bzryxm2"));
                                dataBean.set("zyyzh", record.getStr("zyyzbh2"));
                                dataBean.set("lxr", record.getStr("lxr"));
                                dataBean.set("lxdh", record.getStr("lxdh"));

                                String num = GenerateNumberUtil.getCertNum("zbkzjba");
                                if (num.length() == 1) {
                                    num = "00" + num;
                                } else if (num.length() == 2) {
                                    num = "0" + num;
                                }
                                String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");
                                dataBean.set("bh", "〔" + year + "〕" + num);

                                dataBean.set("rq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            }
                        }
                    }

                    // 工程勘察、设计单位乙级资质单位负责人变更
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("工程勘察、设计单位乙级资质单位负责人变更")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220728151745",
                                    dzbdguid);
                            if (record != null) {
                                dataBean.set("zsbh", record.getStr("zsbh"));
                                dataBean.set("qymc", record.getStr("qymc"));
                                dataBean.set("xxdz", record.getStr("qydz"));
                                dataBean.set("jjxz", record.getStr("jjxz"));
                                dataBean.set("tyxydm", record.getStr("tyshxydm"));
                                dataBean.set("zzlbjdj", record.getStr("zzlbjdj"));
                                dataBean.set("frdb", record.getStr("fddbr"));

                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxq", yxq);
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            }
                        }
                    }

                    // 工程勘察、设计单位乙级资质企业名称变更
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("工程勘察、设计单位乙级资质企业名称变更")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220728160252",
                                    dzbdguid);
                            if (record != null) {
                                dataBean.set("zsbh", record.getStr("zsbh"));
                                dataBean.set("zzlbjdj", record.getStr("zzlbjdj"));
                                dataBean.set("tyxydm", record.getStr("tyshxydm"));
                                dataBean.set("qymc", record.getStr("qymc"));
                                dataBean.set("xxdz", record.getStr("qydz"));
                                dataBean.set("jjxz", record.getStr("jjxz"));
                                dataBean.set("frdb", record.getStr("fddbr"));

                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxq", yxq);
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            }
                        }
                    }

                    // 工程勘察、设计单位乙级资质注册资本金变更
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("工程勘察、设计单位乙级资质注册资本金变更")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220728161647",
                                    dzbdguid);
                            if (record != null) {
                                dataBean.set("zsbh", record.getStr("zsbh"));
                                dataBean.set("zzlbjdj", record.getStr("zzlbjdj"));
                                dataBean.set("tyxydm", record.getStr("tyshxydm"));
                                dataBean.set("qymc", record.getStr("qymc"));
                                dataBean.set("xxdz", record.getStr("qydz"));
                                dataBean.set("jjxz", record.getStr("jjxz"));
                                dataBean.set("frdb", record.getStr("fddbr"));

                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxq", yxq);
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            }
                        }
                    }

                    // 工程勘察、设计单位乙级资质法定代表人职务变更
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("工程勘察、设计单位乙级资质法定代表人职务变更")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220728162458",
                                    dzbdguid);
                            if (record != null) {
                                dataBean.set("zsbh", record.getStr("zsbh"));
                                dataBean.set("zzlbjdj", record.getStr("zzlbjdj"));
                                dataBean.set("tyxydm", record.getStr("tyshxydm"));
                                dataBean.set("qymc", record.getStr("qymc"));
                                dataBean.set("xxdz", record.getStr("qydz"));
                                dataBean.set("jjxz", record.getStr("jjxz"));
                                dataBean.set("frdb", record.getStr("fddbr"));

                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxq", yxq);
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            }
                        }
                    }

                    // 工程勘察、设计单位乙级资质技术负责人变更
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("工程勘察、设计单位乙级资质技术负责人变更")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220728163221",
                                    dzbdguid);
                            if (record != null) {
                                dataBean.set("zsbh", record.getStr("zsbh"));
                                dataBean.set("zzlbjdj", record.getStr("zzlbjdj"));
                                dataBean.set("tyxydm", record.getStr("tyshxydm"));
                                dataBean.set("qymc", record.getStr("qymc"));
                                dataBean.set("xxdz", record.getStr("qydz"));
                                dataBean.set("jjxz", record.getStr("jjxz"));
                                dataBean.set("frdb", record.getStr("fddbr"));

                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxq", yxq);
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            }
                        }
                    }

                    // 工程勘察、设计单位乙级资质（多项变更）
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("工程勘察、设计单位乙级资质（多项变更）")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220728154816",
                                    dzbdguid);
                            if (record != null) {
                                dataBean.set("zsbh", record.getStr("zsbh"));
                                dataBean.set("zzlbjdj", record.getStr("zzlbjdj"));
                                dataBean.set("tyxydm", record.getStr("tyshxydm"));
                                dataBean.set("qymc", record.getStr("xqymc"));
                                dataBean.set("xxdz", record.getStr("qydz"));
                                dataBean.set("jjxz", record.getStr("xjjxz"));
                                dataBean.set("frdb", record.getStr("fddbr"));

                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxq", yxq);
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            }
                        }
                    }

                    // 工程勘察、设计单位乙级资质注册地址变更
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("工程勘察、设计单位乙级资质注册地址变更")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220728160928",
                                    dzbdguid);
                            if (record != null) {
                                dataBean.set("zsbh", record.getStr("zsbh"));
                                dataBean.set("zzlbjdj", record.getStr("zzlbjdj"));
                                dataBean.set("tyxydm", record.getStr("tyshxydm"));
                                dataBean.set("qymc", record.getStr("qymc"));
                                dataBean.set("xxdz", record.getStr("xzcdz"));
                                dataBean.set("jjxz", record.getStr("jjxz"));
                                dataBean.set("frdb", record.getStr("fddbr"));

                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxq", yxq);
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            }
                        }
                    }

                    // 工程勘察、设计单位乙级资质经济性质变更
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("工程勘察、设计单位乙级资质经济性质变更")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220728162030",
                                    dzbdguid);
                            if (record != null) {
                                dataBean.set("zsbh", record.getStr("zsbh"));
                                dataBean.set("zzlbjdj", record.getStr("zzlbjdj"));
                                dataBean.set("tyxydm", record.getStr("tyshxydm"));
                                dataBean.set("qymc", record.getStr("qymc"));
                                dataBean.set("xxdz", record.getStr("qydz"));
                                dataBean.set("jjxz", record.getStr("xjjxz"));
                                dataBean.set("frdb", record.getStr("fddbr"));

                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxq", yxq);
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            }
                        }
                    }

                    // 工程勘察、设计单位乙级资质单位负责人职务变更
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("工程勘察、设计单位乙级资质单位负责人职务变更")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220728162704",
                                    dzbdguid);
                            if (record != null) {
                                dataBean.set("zsbh", record.getStr("zsbh"));
                                dataBean.set("zzlbjdj", record.getStr("zzlbjdj"));
                                dataBean.set("tyxydm", record.getStr("tyshxydm"));
                                dataBean.set("qymc", record.getStr("qymc"));
                                dataBean.set("xxdz", record.getStr("qydz"));
                                dataBean.set("jjxz", record.getStr("jjxz"));
                                dataBean.set("frdb", record.getStr("fddbr"));

                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxq", yxq);
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            }
                        }
                    }

                    // 工程勘察、设计单位乙级资质延续、工程勘察、设计单位乙级资质增补
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("工程勘察、设计单位乙级资质延续")
                                || auditProject.getProjectname().contains("工程勘察、设计单位乙级资质增补")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220728163538",
                                    dzbdguid);
                            if (record != null) {
                                dataBean.set("zsbh", record.getStr("zsbh"));
                                dataBean.set("zzlbjdj", record.getStr("zzlbjdj"));
                                dataBean.set("tyxydm", record.getStr("tyshxydm"));
                                dataBean.set("qymc", record.getStr("qymc"));
                                dataBean.set("xxdz", record.getStr("qydz"));
                                dataBean.set("jjxz", record.getStr("jjxz"));
                                dataBean.set("frdb", record.getStr("fddbr"));

                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxq", yxq);
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            }
                        }
                    }

                    // 工程勘察、设计单位乙级资质建立时间变更
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("工程勘察、设计单位乙级资质建立时间变更")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220728161234",
                                    dzbdguid);
                            if (record != null) {
                                dataBean.set("zsbh", record.getStr("zsbh"));
                                dataBean.set("zzlbjdj", record.getStr("zzlbjdj"));
                                dataBean.set("tyxydm", record.getStr("tyshxydm"));
                                dataBean.set("qymc", record.getStr("qymc"));
                                dataBean.set("xxdz", record.getStr("qydz"));
                                dataBean.set("jjxz", record.getStr("jjxz"));
                                dataBean.set("frdb", record.getStr("fddbr"));

                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxq", yxq);
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            }
                        }
                    }

                    // 工程勘察、设计单位乙级资质法定代表人变更
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("工程勘察、设计单位乙级资质法定代表人变更")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220728162309",
                                    dzbdguid);
                            if (record != null) {
                                dataBean.set("zsbh", record.getStr("zsbh"));
                                dataBean.set("zzlbjdj", record.getStr("zzlbjdj"));
                                dataBean.set("tyxydm", record.getStr("tyshxydm"));
                                dataBean.set("qymc", record.getStr("qymc"));
                                dataBean.set("xxdz", record.getStr("qydz"));
                                dataBean.set("jjxz", record.getStr("jjxz"));
                                dataBean.set("frdb", record.getStr("fddbr"));

                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxq", yxq);
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            }
                        }
                    }

                    // 工程勘察、设计单位乙级资质技术负责人职称或职业资格变更
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("工程勘察、设计单位乙级资质技术负责人职称或职业资格变更")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220728162859",
                                    dzbdguid);
                            if (record != null) {
                                dataBean.set("zsbh", record.getStr("zsbh"));
                                dataBean.set("zzlbjdj", record.getStr("zzlbjdj"));
                                dataBean.set("tyxydm", record.getStr("tyshxydm"));
                                dataBean.set("qymc", record.getStr("qymc"));
                                dataBean.set("xxdz", record.getStr("qydz"));
                                dataBean.set("jjxz", record.getStr("jjxz"));
                                dataBean.set("frdb", record.getStr("fddbr"));

                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxq", yxq);
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            }
                        }
                    }


                    //建筑施工企业安全生产许可（变更）11370800MB28559184300011711200004
                    //建筑施工企业安全生产许可[延期(需要重新审查）  11370800MB28559184300011711200002
                    //建筑施工企业安全生产许可[延期（不需要重新审查）]  11370800MB28559184300011711200003
                    if (auditProject != null) {
                        AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), false)
                                .getResult();
                        if (auditTask != null) {
                            if ("11370800MB28559184300011711200004".equals(auditTask.getItem_id()) || "11370800MB28559184300011711200002".equals(auditTask.getItem_id())
                                    || "11370800MB28559184300011711200003".equals(auditTask.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20220429150623",
                                        dzbdguid);
                                if (record != null) {
                                    dataBean.set("qymc", record.getStr("qymc"));
                                    dataBean.set("tyxydm", record.getStr("tyxydm"));
                                    dataBean.set("xxdz", record.getStr("xxdz"));
                                    dataBean.set("jjxz", record.getStr("jjxz"));
                                    dataBean.set("frdb", record.getStr("frdb"));
                                    dataBean.set("zzlbjdj", record.getStr("zizlbjdj"));
                                    dataBean.set("zsbh", record.getStr("zsbh"));

                                    Date startdate = new Date();
                                    Calendar calendar = new GregorianCalendar();
                                    calendar.setTime(startdate);
                                    calendar.add(Calendar.YEAR, 3); // 把日期往后增加一年，整数往后推，负数往前移
                                    calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                    Date enddate = calendar.getTime();
                                    String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                    dataBean.set("yxq", yxq);

                                    dataBean.set("yxqs",
                                            EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                    dataBean.set("yxqz", yxq);

                                    dataBean.set("fzrq",
                                            EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                                }
                            }
                        }
                    }

                    //建筑施工企业安全生产许可（申请）、增补、新申请
                    if (auditProject != null) {
                        AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), false)
                                .getResult();
                        if (auditTask != null) {
                            if ("11370800MB285591843370117002016".equals(auditTask.getItem_id())
                                    || "11370800MB28559184300011711200001".equals(auditTask.getItem_id())
                                    || "11370800MB285591843370117002011".equals(auditTask.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20220422102306",
                                        dzbdguid);
                                if (record != null) {
                                    dataBean.set("qymc", record.getStr("wbk1"));
                                    dataBean.set("tyxydm", record.getStr("tyshxydm"));
                                    dataBean.set("xxdz", record.getStr("xxdz"));
                                    dataBean.set("jjxz", record.getStr("jjxz"));
                                    dataBean.set("frdb", record.getStr("fddbr"));
                                    dataBean.set("zzlbjdj", record.getStr("zzlbjdj"));

                                    if ("11370800MB28559184300011711200001".equals(auditTask.getItem_id())
                                            || "11370800MB285591843370117002011".equals(auditTask.getItem_id())) {
                                        String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");
                                        Integer idnum = record.getInt("idnum");
                                        idnum = idnum + 3623;
                                        String idnumv = String.valueOf(idnum);
                                        dataBean.set("zsbh", "（鲁）JZ安许证字[" + year + "]08" + idnumv);
                                    } else {
                                        dataBean.set("zsbh", record.getStr("zsbh"));
                                    }

                                    Date startdate = new Date();
                                    Calendar calendar = new GregorianCalendar();
                                    calendar.setTime(startdate);
                                    calendar.add(Calendar.YEAR, 3); // 把日期往后增加一年，整数往后推，负数往前移
                                    calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                    Date enddate = calendar.getTime();
                                    String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                    dataBean.set("yxq", yxq);

                                    dataBean.set("yxqs",
                                            EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                    dataBean.set("yxqz", yxq);

                                    dataBean.set("fzrq",
                                            EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                                }
                            }
                        }
                    }

                    // 建筑业企业资质（告知承诺方式）新申请
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("建筑施工企业安全生产许可证延期重审")
                                || auditProject.getProjectname().contains("建筑施工企业安全生产许可证正常延期")
                                || auditProject.getProjectname().contains("建筑施工企业安全生产许可证变更（多项变更）")
                                || auditProject.getProjectname().contains("建筑施工企业安全生产许可证变更（法人变更）")
                                || auditProject.getProjectname().contains("建筑施工企业安全生产许可证变更（经济类型变更）")
                                || auditProject.getProjectname().contains("建筑施工企业安全生产许可证变更（企业地址变更）")
                                || auditProject.getProjectname().contains("建筑施工企业安全生产许可证变更（企业名称变更）")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220429150623",
                                    dzbdguid);
                            if (record != null) {
                                dataBean.set("zsbh", record.getStr("zsbh"));
                                dataBean.set("qymc", record.getStr("qymc"));
                                dataBean.set("tyxydm", record.getStr("tyxydm"));
                                dataBean.set("xxdz", record.getStr("xxdz"));
                                dataBean.set("jjxz", record.getStr("jjxz"));
                                dataBean.set("frdb", record.getStr("frdb"));
                                dataBean.set("zzlbjdj", record.getStr("zizlbjdj"));
                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 3); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxq", yxq);

                                dataBean.set("yxqs",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                dataBean.set("yxqz", yxq);

                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            }
                        }
                    }

                    // 建筑业企业资质（告知承诺方式）新申请
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("建筑业企业资质（告知承诺方式）新申请")
                                || auditProject.getProjectname().contains("建筑业企业资质（告知承诺方式）增项")
                                || auditProject.getProjectname().contains("建筑业企业资质许可注销")
                                || auditProject.getProjectname().contains("建筑业企业资质许可(企业外资退出的重新核定)")
                                || auditProject.getProjectname().contains("建筑业企业资质许可(国有企业改制重组分立的重新核定)")
                                || auditProject.getProjectname().contains("建筑业企业资质许可(企业全资子公司间重组分立的重新核定)")
                                || auditProject.getProjectname().contains("建筑业企业资质许可{企业合并（吸收合并及新设合并）的重新核定}")
                                || auditProject.getProjectname().contains("建筑业企业资质许可（跨省变更的重新核定）")
                                || auditProject.getProjectname().contains("建筑业企业资质许可（遗失补办）")
                                || auditProject.getProjectname().contains("建筑业企业资质许可（换证）")
                                || auditProject.getProjectname().contains("建筑业企业资质许可（延续）")
                                || auditProject.getProjectname().contains("建筑业企业资质许可（升级）")
                                || auditProject.getProjectname().contains("建筑业企业资质（首次申请")
                                || auditProject.getProjectname().contains("建筑业企业资质许可（增项）")
                                || auditProject.getProjectname().contains("建筑业企业资质（简单变更）（企业地址变更）")
                                || auditProject.getProjectname().contains("建筑业企业资质（简单变更）（法人变更）")
                                || auditProject.getProjectname().contains("建筑业企业资质（简单变更）（企业名称变更）")
                                || auditProject.getProjectname().contains("建筑业企业资质（简单变更）（经济性质变更）")
                                || auditProject.getProjectname().contains("建筑业企业资质（简单变更）（注册资本变更）")
                                || auditProject.getProjectname().contains("建筑业企业资质（简单变更）（多项变更）")
                                || auditProject.getProjectname().contains("工程监理企业资质许可乙级延续")
                                || auditProject.getProjectname().contains("工程监理企业资质许可乙级增补")
                                || auditProject.getProjectname().contains("工程监理企业资质许可乙级多项变更")
                                || auditProject.getProjectname().contains("工程监理企业资质许可乙级变更企业名称")
                                || auditProject.getProjectname().contains("工程监理企业资质许可乙级变更地址")
                                || auditProject.getProjectname().contains("工程监理企业资质许可乙级变更注册资本金")
                                || auditProject.getProjectname().contains("工程监理企业资质许可乙级变更经济性质")
                                || auditProject.getProjectname().contains("工程监理企业资质许可乙级变更法定代表人")
                                || auditProject.getProjectname().contains("工程监理企业资质许可乙级变更单位负责人")
                                || auditProject.getProjectname().contains("工程监理企业资质许可乙级变更技术负责人")
                                || auditProject.getProjectname().contains("工程监理企业资质许可丙级注销")
                                || auditProject.getProjectname().contains("工程监理企业资质许可丙级增补")
                                || auditProject.getProjectname().contains("工程监理企业资质许可丙级变更企业名称")
                                || auditProject.getProjectname().contains("工程监理企业资质许可丙级变更地址")
                                || auditProject.getProjectname().contains("工程监理企业资质许可丙级变更注册资本金")
                                || auditProject.getProjectname().contains("工程监理企业资质许可丙级变更经济性质")
                                || auditProject.getProjectname().contains("工程监理企业资质许可丙级变更法定代表人")
                                || auditProject.getProjectname().contains("工程监理企业资质许可丙级变更单位负责人")
                                || auditProject.getProjectname().contains("工程监理企业资质许可丙级变更技术负责人")
                                || auditProject.getProjectname().contains("工程监理企业资质许可丙级多项变更")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220402152431",
                                    dzbdguid);
                            if (record != null) {
                                dataBean.set("qymc", record.getStr("qymc"));
                                dataBean.set("tyxydm", record.getStr("tyxydm"));
                                dataBean.set("xxdz", record.getStr("xxdz"));
                                dataBean.set("jjxz", record.getStr("jjxz"));
                                dataBean.set("frdb", record.getStr("frdb"));
                                dataBean.set("zzlbjdj", record.getStr("zzlbjdj"));
                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("remark",
                                        "注册资本（万元）：" + record.getStr("zczbj") + "  法人代表：" + record.getStr("frdb"));
                                dataBean.set("yxq", yxq);
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            }
                        }
                    }

                    // 建筑业企业资质（告知承诺方式）新申请
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("建筑业企业施工劳务资质备案")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20221109165008",
                                    dzbdguid);
                            if (record != null) {
                                dataBean.set("qymc", record.getStr("qymc"));
                                dataBean.set("tyxydm", record.getStr("tyxydm"));
                                dataBean.set("xxdz", record.getStr("xxdz"));
                                dataBean.set("jjxz", record.getStr("jjxz"));
                                dataBean.set("frdb", record.getStr("frdb"));
                                dataBean.set("zzlbjdj", record.getStr("zzlbjdj"));
                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("remark",
                                        "注册资本（万元）：" + record.getStr("zczbj") + "  法人代表：" + record.getStr("frdb"));
                                dataBean.set("yxq", yxq);
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            }
                        }
                    }

                    // 房地产开发企业二级资质
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("房地产开发企业二级资质延续")
                                || auditProject.getProjectname().contains("房地产开发企业二级资质遗失补办")
                                || auditProject.getProjectname().contains("房地产开发企业二级资质变更")
                                || auditProject.getProjectname().contains("房地产开发企业二级资质注销")
                                || auditProject.getProjectname().contains("房地产开发企业二级资质新申请")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220402152431",
                                    dzbdguid);
                            if (record != null) {
                                dataBean.set("zsbh", record.getStr("zsbh"));
                                dataBean.set("qymc", record.getStr("qymc"));
                                dataBean.set("tyxydm", record.getStr("tyxydm"));
                                dataBean.set("xxdz", record.getStr("xxdz"));
                                dataBean.set("jjxz", record.getStr("jjxz"));
                                dataBean.set("frdb", record.getStr("frdb"));
                                dataBean.set("zzlbjdj", record.getStr("zzlbjdj"));
                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 3); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxq", yxq);
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            }
                            addCallbackParam("showfdcnum", "1");
                        }
                    }

                    // 房地产开发企业二级资质核定
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("房地产开发企业二级资质核定（设区的市级权限）升级")
                                || auditProject.getProjectname().contains("房地产开发企业二级资质核定（设区的市级权限）变更")
                                || auditProject.getProjectname().contains("房地产开发企业二级资质核定（设区的市级权限）延续")
                                || auditProject.getProjectname().contains("房地产开发企业二级资质核定（设区的市级权限）注销")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20230721105849",
                                    dzbdguid);
                            if (record != null) {
                                dataBean.set("zsbh", record.getStr("zsbh"));
                                dataBean.set("qymc", record.getStr("qymc"));
                                dataBean.set("tyxydm", record.getStr("tyxydm"));
                                dataBean.set("xxdz", record.getStr("xxdz"));
                                dataBean.set("jjxz", record.getStr("jjxz"));
                                dataBean.set("frdb", record.getStr("frdb"));
                                dataBean.set("zzlbjdj", record.getStr("zzlbjdj"));
                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 3); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxq", yxq);
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            }
                            addCallbackParam("showfdcnum", "1");
                        }
                    }

                    // 建筑业企业资质（告知承诺方式）新申请
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("建筑施工企业安全生产许可证注销")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220402152431",
                                    dzbdguid);
                            if (record != null) {
                                dataBean.set("qymc", record.getStr("qymc"));
                                dataBean.set("tyxydm", record.getStr("tyxydm"));
                                dataBean.set("xxdz", record.getStr("xxdz"));
                                dataBean.set("jjxz", record.getStr("jjxz"));
                                dataBean.set("frdb", record.getStr("fddbr"));
                                dataBean.set("zzlbjdj", record.getStr("zzlbjdj"));
                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 3); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxq", yxq);

                                dataBean.set("yxqs",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                dataBean.set("yxqz", yxq);

                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            }
                        }
                    }

                    // 建筑业企业资质（告知承诺方式）新申请
                    if (auditProject != null) {
                        AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), false)
                                .getResult();
                        if (auditTask != null) {
                            if (auditProject.getProjectname().contains("建设工程质量检测机构资质审批（地址变更）")
                                    || auditProject.getProjectname().contains("建设工程质量检测机构资质审批（法定代表人变更）")
                                    || auditProject.getProjectname().contains("建设工程质量检测机构资质审批（技术负责人变更）")
                                    || auditProject.getProjectname().contains("建设工程质量检测机构资质审批（名称变更）")
                                    || auditProject.getProjectname().contains("建设工程质量检测机构资质审批（资质延期）")
                                    || "11370800MB28559184300011711100002".equals(auditTask.getItem_id())
                                    || "11370800MB285591847370117003002".equals(auditTask.getItem_id())
                                    || "11370800MB28559184300011711100003".equals(auditTask.getItem_id())
                                    || "11370800MB285591842370117008000".equals(auditTask.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20220408161905",
                                        dzbdguid);
                                if (record != null) {
                                    dataBean.set("zsbh", record.getStr("zsbh"));
                                    dataBean.set("qymc", record.getStr("qymc"));
                                    dataBean.set("tyxydm", record.getStr("tyxydm"));
                                    dataBean.set("xxdz", record.getStr("xxdz"));
                                    // dataBean.set("jjxz", record.getStr("jjxz"));
                                    dataBean.set("fddbr", record.getStr("frdb"));
                                    dataBean.set("zzlbjdj", record.getStr("zizlbjdj"));
                                    Date startdate = new Date();
                                    Calendar calendar = new GregorianCalendar();
                                    calendar.setTime(startdate);
                                    calendar.add(Calendar.YEAR, 3); // 把日期往后增加一年，整数往后推，负数往前移
                                    calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                    Date enddate = calendar.getTime();
                                    String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                    dataBean.set("yxq", yxq);
                                    dataBean.set("fzrq",
                                            EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                                }
                            }
                        }
                    }

                    // 建筑业企业资质（告知承诺方式）新申请
                    if (auditProject != null) {
                        Record record = new Record();
                        if (auditProject.getProjectname().contains("施工图审查机构认定新设立")
                                || auditProject.getProjectname().contains("施工图审查机构认定延续")
                                || auditProject.getProjectname().contains("施工图审查机构认定增项")
                                || auditProject.getProjectname().contains("施工图审查机构认定变更")
                                || auditProject.getProjectname().contains("施工图审查机构认定升级")
                                || auditProject.getProjectname().contains("施工图审查机构认定注销")) {
                            if (auditProject.getProjectname().contains("施工图审查机构认定新设立") || auditProject.getProjectname().contains("施工图审查机构认定延续")) {
                                record = iCxBusService.getDzbdDetail("formtable20240306165745",
                                        dzbdguid);
                            } else if (auditProject.getProjectname().contains("施工图审查机构认定增项") || auditProject.getProjectname().contains("施工图审查机构认定升级")) {
                                record = iCxBusService.getDzbdDetail("formtable20240319102336",
                                        dzbdguid);
                            } else if (auditProject.getProjectname().contains("施工图审查机构认定变更")) {
                                record = iCxBusService.getDzbdDetail("formtable20240319101453",
                                        dzbdguid);
                            } else if (auditProject.getProjectname().contains("施工图审查机构认定注销")) {
                                record = iCxBusService.getDzbdDetail("formtable20240319102835",
                                        dzbdguid);
                            }

                            if (record != null) {
                                dataBean.set("qymc", record.getStr("qymc"));
                                dataBean.set("tyxydm", record.getStr("tyxydm"));
                                dataBean.set("xxdz", record.getStr("xxdz"));
                                dataBean.set("jjxz", record.getStr("jjxz"));
                                dataBean.set("frdb", record.getStr("frdb"));
                                dataBean.set("zzlbjdj", record.getStr("zzlbjdj"));
                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 2); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String yxq = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                                dataBean.set("yxq", yxq);
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            }
                        }
                    }

                    // 公共卫生许可
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("公共场所卫生许可证延续")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220413141101",
                                    dzbdguid);
                            CertGgxkws certggxkws = iCertGgxkwsService.getCertByCertno(record.getStr("zzbh"));
                            if (certggxkws != null && record != null) {
                                dataBean.set("xkxm", certggxkws.getMajortype());
                                dataBean.set("dwmc", certggxkws.getMonitorname());
                                dataBean.set("dz", certggxkws.getManageaddress());
                                dataBean.set("fzr", certggxkws.getLegal());
                                dataBean.set("jyxz", certggxkws.getEcontype());
                                dataBean.set("babh", certggxkws.getCertno());

                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 4); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String fzrq = EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT);
                                dataBean.set("fzrq", fzrq);
                                dataBean.set("yxqs", fzrq);
                                dataBean.set("yxqz",
                                        EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT));
                            }
                        }
                    }

                    // 公共卫生许可
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("公共场所卫生许可证变更单位名称")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220413120417",
                                    dzbdguid);
                            CertGgxkws certggxkws = iCertGgxkwsService.getCertByCertno(record.getStr("zzbh"));
                            if (certggxkws != null && record != null) {
                                dataBean.set("xkxm", certggxkws.getMajortype());
                                dataBean.set("dwmc", record.getStr("dwmc"));
                                dataBean.set("dz", certggxkws.getManageaddress());
                                dataBean.set("fzr", certggxkws.getLegal());
                                dataBean.set("jyxz", certggxkws.getEcontype());
                                dataBean.set("babh", certggxkws.getCertno());
                                dataBean.set("fzrq", EpointDateUtil.convertDate2String(certggxkws.getBegintime(),
                                        EpointDateUtil.DATE_FORMAT));
                                dataBean.set("yxqs", EpointDateUtil.convertDate2String(certggxkws.getBegintime(),
                                        EpointDateUtil.DATE_FORMAT));
                                dataBean.set("yxqz", EpointDateUtil.convertDate2String(certggxkws.getEndtime(),
                                        EpointDateUtil.DATE_FORMAT));
                            }
                        }
                    }

                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("公共场所卫生许可证变更法人")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220414164913",
                                    dzbdguid);
                            CertGgxkws certggxkws = iCertGgxkwsService.getCertByCertno(record.getStr("zzbh"));
                            if (certggxkws != null && record != null) {
                                dataBean.set("xkxm", certggxkws.getMajortype());
                                dataBean.set("dwmc", certggxkws.getMonitorname());
                                dataBean.set("dz", certggxkws.getManageaddress());
                                dataBean.set("fzr", record.getStr("xfddbr"));
                                dataBean.set("jyxz", certggxkws.getEcontype());
                                dataBean.set("babh", certggxkws.getCertno());
                                dataBean.set("fzrq", EpointDateUtil.convertDate2String(certggxkws.getBegintime(),
                                        EpointDateUtil.DATE_FORMAT));
                                dataBean.set("yxqs", EpointDateUtil.convertDate2String(certggxkws.getBegintime(),
                                        EpointDateUtil.DATE_FORMAT));
                                dataBean.set("yxqz", EpointDateUtil.convertDate2String(certggxkws.getEndtime(),
                                        EpointDateUtil.DATE_FORMAT));
                            }
                        }
                    }

                    // 公共卫生许可
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("公共场所卫生许可证新申请") || auditProject.getProjectname().contains("公共场所卫生许可（新办）") ||
                                auditProject.getProjectname().contains("公共场所卫生许可（延续）") || auditProject.getProjectname().contains("公共场所卫生许可（变更）")) {
                            Record record = iCxBusService.getGgwsDzbdDetail(auditProject.getRowguid());
                            if (record != null) {
                                String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");
                                dataBean.set("xkxm", record.getStr("xkjyxm"));
                                dataBean.set("dwmc", record.getStr("sqdw"));
                                dataBean.set("dz", record.getStr("dwdz"));
                                dataBean.set("fzr", record.getStr("fddbr"));
                                dataBean.set("jjxz", record.getStr("ggwsjjxz"));
                                String idnum = "";
                                AuditTask auditTask = auditTaskService
                                        .getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();

                                if (auditTask != null) {
                                    if ("370829".contains(auditTask.getAreacode())) {
                                        idnum = record.getStr("idnum");
                                        if (idnum.length() == 1) {
                                            idnum = "00" + idnum;
                                        } else if (idnum.length() == 2) {
                                            idnum = "0" + idnum;
                                        }
                                        dataBean.set("babh", "嘉审卫公证〔" + year + "〕第" + idnum + "号");
                                    } else {
                                        idnum = createGgZsbh(auditTask.getItem_id(), record.getStr("xkjyxm"));
                                        dataBean.set("babh", idnum);
                                    }
                                }

                                Date startdate = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(startdate);
                                calendar.add(Calendar.YEAR, 4); // 把日期往后增加一年，整数往后推，负数往前移
                                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                                Date enddate = calendar.getTime();
                                String fzrq = EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT);
                                dataBean.set("fzrq", fzrq);
                                dataBean.set("yxqs", fzrq);
                                dataBean.set("yxqz",
                                        EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT));

                            }
                        }
                    }

                    // 建（构）筑物工程建筑垃圾处置核准
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("建（构）筑物工程建筑垃圾处置核准")
                                || auditProject.getProjectname().contains("非建（构）筑物工程建筑垃圾处置核准")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220420093558",
                                    dzbdguid);
                            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), false)
                                    .getResult();

                            if (record != null) {
                                String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");
                                dataBean.set("dwmc", record.getStr("dwmc"));
                                dataBean.set("yslx", record.getStr("yslx"));
                                if ("370800".equals(auditProject.getAreacode())) {
                                    String idnum = record.getStr("idnum");
                                    if (idnum.length() == 1) {
                                        idnum = "000" + idnum;
                                    } else if (idnum.length() == 2) {
                                        idnum = "00" + idnum;
                                    } else if (idnum.length() == 3) {
                                        idnum = "0" + idnum;
                                    }
                                    dataBean.set("zzbh", "JNSP" + year + "（ZT运输）-" + idnum);
                                } else {
                                    String zzbh = createJsGcZsbh(auditTask.getItem_id());
                                    dataBean.set("zzbh", zzbh);
                                }

                                dataBean.set("czrqks", EpointDateUtil.convertDate2String(record.getDate("czqxrq"),
                                        EpointDateUtil.DATE_FORMAT));
                                dataBean.set("czrqjs", EpointDateUtil.convertDate2String(record.getDate("czqxrqend"),
                                        EpointDateUtil.DATE_FORMAT));
                                dataBean.set("sqsj", EpointDateUtil.convertDate2String(record.getDate("sqrq"),
                                        EpointDateUtil.DATE_FORMAT));
                                dataBean.set("fzrq",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            }
                        }
                    }

                    // 申请省际普通货物水路运输业务经营变更名称、法定代表人、注册地址、经济类型等证书内容
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("申请省际普通货物水路运输业务经营变更名称、法定代表人、注册地址、经济类型等证书内容")) {
                            Record record = iCxBusService.getDzbdDetail("formtable20220507171039",
                                    dzbdguid);
                            CertHwslysjyxk certggxkws = iCertHwslysjyxkService.getCertByCertno(record.getStr("zzbh"));
                            if (certggxkws != null && record != null) {
                                dataBean.set("BH", record.getStr("zzbh"));
                                dataBean.set("QYMC", record.getStr("wbk8"));
                                dataBean.set("FDDBR", record.getStr("xfddbr"));
                                dataBean.set("DZ", record.getStr("wbk9"));
                                dataBean.set("JJLX", record.getStr("xfrsfzh"));
                                dataBean.set("LKYS", record.getStr("wbk12"));
                                dataBean.set("HWYS", record.getStr("wbk15"));
                                dataBean.set("JY", record.getStr("wbk16"));
                                Date ksrq = certggxkws.getKsrq();
                                dataBean.set("QN", EpointDateUtil.convertDate2String(ksrq, "yyyy"));
                                dataBean.set("QY", EpointDateUtil.convertDate2String(ksrq, "MM"));
                                dataBean.set("QR", EpointDateUtil.convertDate2String(ksrq, "dd"));
                                dataBean.set("N", EpointDateUtil.convertDate2String(ksrq, "yyyy"));
                                dataBean.set("Y", EpointDateUtil.convertDate2String(ksrq, "MM"));
                                dataBean.set("R", EpointDateUtil.convertDate2String(ksrq, "dd"));
                            }
                        }
                    }

                    // 申请省际普通货物水路运输经营许可
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("申请省际普通货物水路运输经营许可")) {
                            Record record = iCxBusService.getDzbdDetail("slysqykysq", dzbdguid);
                            if (record != null) {
                                String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");
                                String month = EpointDateUtil.convertDate2String(new Date(), "MM");
                                String day = EpointDateUtil.convertDate2String(new Date(), "dd");

                                dataBean.set("QYMC", record.getStr("shenqdwmc"));
                                dataBean.set("FDDBR", auditProject.getLegal());
                                dataBean.set("DZ", record.getStr("shenqdwdz"));
                                dataBean.set("JJLX", record.getStr("jingjlx"));
                                dataBean.set("LKYS", record.getStr("jingyfwzykyhx"));
                                dataBean.set("HWYS", record.getStr("jingyfwzyhyhx"));
                                dataBean.set("JY", record.getStr("jingyfwjy"));

                                dataBean.set("QN", year);
                                dataBean.set("QY", month);
                                dataBean.set("QR", day);
                                dataBean.set("N", year);
                                dataBean.set("Y", month);
                                dataBean.set("R", day);

                            }
                        }
                    }

                    // 申请省际普通货物水路运输业务经营变更名称、法定代表人、注册地址、经济类型等证书内容
                    if (auditProject != null && task != null
                            && "申请省际普通货物水路运输业务经营变更名称、法定代表人、注册地址、经济类型等证书内容".equals(task.getTaskname())) {
                        Record record = iCxBusService.getDzbdDetail("formtable20220507171039",
                                dzbdguid);
                        CertHwslysjyxk certHwslysjyxk = iCertHwslysjyxkService.getCertByCertno(record.getStr("zzbh"));
                        if (record != null && certHwslysjyxk != null) {
                            if (StringUtil.isNotBlank(record.getStr("zzbh"))) {
                                dataBean.set("BH", record.getStr("zzbh"));
                            } else {
                                dataBean.set("BH", certHwslysjyxk.getJyxkzbh());
                            }
                            if (StringUtil.isNotBlank(record.getStr("xqymc"))) {
                                dataBean.set("QYMC", record.getStr("xqymc"));
                            } else {
                                dataBean.set("QYMC", certHwslysjyxk.getJyzmc());
                            }
                            if (StringUtil.isNotBlank(record.getStr("xfddbr"))) {
                                dataBean.set("FDDBR", record.getStr("xfddbr"));
                            } else {
                                dataBean.set("FDDBR", certHwslysjyxk.getFddbr());
                            }
                            if (StringUtil.isNotBlank(record.getStr("xdz"))) {
                                dataBean.set("DZ", record.getStr("xdz"));
                            } else {
                                dataBean.set("DZ", certHwslysjyxk.getJyrdz());
                            }
                            if (StringUtil.isNotBlank(record.getStr("xjjlx"))) {
                                dataBean.set("JJLX", record.getStr("xjjlx"));
                            } else {
                                dataBean.set("JJLX", certHwslysjyxk.getJyfs());
                            }
                            if (StringUtil.isNotBlank(record.getStr("xlkys"))) {
                                dataBean.set("LKYS", record.getStr("xlkys"));
                            } else {
                                dataBean.set("LKYS", certHwslysjyxk.getKyjyfw());
                            }
                            if (StringUtil.isNotBlank(record.getStr("xhwys"))) {
                                dataBean.set("HWYS", record.getStr("xhwys"));
                            } else {
                                dataBean.set("HWYS", certHwslysjyxk.getHyjyfw());
                            }
                            if (StringUtil.isNotBlank(record.getStr("xjy"))) {
                                dataBean.set("JY", record.getStr("xjy"));
                            } else {
                                dataBean.set("JY", certHwslysjyxk.getJyfw());
                            }

                            dataBean.set("JYQX", certHwslysjyxk.getJyqx());
                            dataBean.set("PJJGJWH", certHwslysjyxk.getPzjg());
                            // 开始日期
                            Date ksrq = certHwslysjyxk.getKsrq();
                            if (ksrq != null) {
                                String year = EpointDateUtil.convertDate2String(ksrq, "yyyy");
                                String month = EpointDateUtil.convertDate2String(ksrq, "MM");
                                String day = EpointDateUtil.convertDate2String(ksrq, "dd");
                                dataBean.set("QN", year);
                                dataBean.set("QY", month);
                                dataBean.set("QR", day);
                            }
                            // 截至日期
                            Date jzDate = certHwslysjyxk.getJzrq();
                            if (jzDate != null) {
                                String year = EpointDateUtil.convertDate2String(jzDate, "yyyy");
                                String month = EpointDateUtil.convertDate2String(jzDate, "MM");
                                String day = EpointDateUtil.convertDate2String(jzDate, "dd");
                                dataBean.set("ZN", year);
                                dataBean.set("ZY", month);
                                dataBean.set("ZR", day);
                            }
                            // 发证日期
                            Date fzDate = certHwslysjyxk.getFzrq();
                            if (fzDate != null) {
                                String year = EpointDateUtil.convertDate2String(fzDate, "yyyy");
                                String month = EpointDateUtil.convertDate2String(fzDate, "MM");
                                String day = EpointDateUtil.convertDate2String(fzDate, "dd");
                                dataBean.set("N", year);
                                dataBean.set("Y", month);
                                dataBean.set("R", day);
                            }
                        }
                    }

                    // 船舶营业运输证
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("船舶营业")) {
                            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), false)
                                    .getResult();
                            if ("11370800MB28559184337011800900410".equals(auditTask.getItem_id())
                                    || "11370800MB28559184337011800900409".equals(auditTask.getItem_id())
                                    || "11370800MB28559184337011800900408".equals(auditTask.getItem_id())
                                    || "11370800MB28559184337011800900407".equals(auditTask.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("cbyyyszsq", dzbdguid);
                                if (record != null) {
                                    dataBean.set("CBDJH", record.getStr("syqzsh"));
                                    // 中文船名
                                    String cm = record.getStr("zwcm");
                                    // 曾用名
                                    String cym = record.getStr("cym");
                                    if (StringUtil.isNotBlank(cym)) {
                                        cm = cm + "/" + cym;
                                    }
                                    dataBean.set("CM", cm);
                                    dataBean.set("CBJYR", record.getStr("cbjyr"));
                                    dataBean.set("JYRXKZHM", record.getStr("xkzbh"));
                                    dataBean.set("CJDJH", record.getStr("cjdjh"));
                                    dataBean.set("CBSYR", record.getStr("cbsyr"));
                                    dataBean.set("CBGLR", record.getStr("cbglr"));
                                    dataBean.set("GLRXKZHM", record.getStr("zgzsh"));
                                    dataBean.set("GCRQ", record.getStr("jcrq"));
                                    dataBean.set("gjrq", record.getStr("GJRQ"));
                                    dataBean.set("bcjyfw", record.getStr("BCJYFW"));
                                    dataBean.set("HDJYFW", record.getStr("xkzjyfw"));
                                    dataBean.set("ZJ", record.getStr("zjsl"));
                                    dataBean.set("QW", record.getStr("zjgl"));
                                    dataBean.set("zzd", record.getStr("ZZD"));
                                    dataBean.set("TEU", record.getStr("teubx"));
                                    dataBean.set("lfm", record.getStr("LFM"));
                                    dataBean.set("cw", record.getStr("CW"));
                                    dataBean.set("kw", record.getStr("KW"));

                                    dataBean.set("CJG", record.getStr("cjg"));
                                    dataBean.set("FZJG", record.getStr("FZJG"));
                                    dataBean.set("CBLX", record.getStr("cbzl"));
                                    dataBean.set("CBCL", record.getStr("cbcl"));
                                    dataBean.set("CBZD", record.getStr("zd"));
                                    dataBean.set("GCRQ", record.getStr("jcrq"));

                                }

                                String certNo = createJsGcZsbh(auditTask.getItem_id());
                                dataBean.set("BH", certNo);

                                Date fzrq = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(fzrq);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                Date enddate = calendar.getTime();
                                dataBean.set("FZRQ", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                                dataBean.set("ZN", EpointDateUtil.convertDate2String(enddate, "yyyy"));
                                dataBean.set("ZY", EpointDateUtil.convertDate2String(enddate, "MM"));
                                dataBean.set("ZR", EpointDateUtil.convertDate2String(enddate, "dd"));
                            }
                            // 补发
                            else if ("11370800MB28559184337011800900412".equals(auditTask.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20230223165827",
                                        dzbdguid);
                                if (record != null) {
                                    dataBean.set("JYRXKZHM", record.getStr("xkzbh"));
                                    dataBean.set("CBDJH", record.getStr("syqzsh"));
                                    dataBean.set("CM", record.getStr("cym"));
                                    dataBean.set("CBJYR", record.getStr("cbjyr"));
                                    dataBean.set("CJDJH", record.getStr("cjdjh"));
                                    dataBean.set("CBSYR", record.getStr("cbsyr"));
                                    dataBean.set("CBGLR", record.getStr("cbglr"));
                                    dataBean.set("GLRXKZHM", record.getStr("zgzsh"));
                                    dataBean.set("GCRQ", record.getStr("jcrq"));
                                    dataBean.set("gjrq", record.getStr("GJRQ"));
                                    dataBean.set("bcjyfw", record.getStr("BCJYFW"));
                                    dataBean.set("HDJYFW", record.getStr("xkzjyfw"));
                                    dataBean.set("ZJ", record.getStr("zjsl"));
                                    dataBean.set("QW", record.getStr("zjgl"));
                                    dataBean.set("zzd", record.getStr("ZZD"));
                                    dataBean.set("TEU", record.getStr("teubx"));
                                    dataBean.set("lfm", record.getStr("LFM"));
                                    dataBean.set("CW", record.getStr("CW"));
                                    dataBean.set("KW", record.getStr("KW"));

                                    dataBean.set("CJG", record.getStr("cjg"));
                                    dataBean.set("CBLX", record.getStr("cbzl"));
                                    dataBean.set("CBCL", record.getStr("cbcl"));
                                    dataBean.set("CBZD", record.getStr("zd"));
                                    dataBean.set("ZJ", record.getStr("zjsl"));
                                    dataBean.set("BH", record.getStr("cbyyyszbh"));
                                }

                                Date fzrq = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(fzrq);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                Date enddate = calendar.getTime();
                                dataBean.set("FZRQ", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                                dataBean.set("ZN", EpointDateUtil.convertDate2String(enddate, "yyyy"));
                                dataBean.set("ZY", EpointDateUtil.convertDate2String(enddate, "MM"));
                                dataBean.set("ZR", EpointDateUtil.convertDate2String(enddate, "dd"));
                            }
                            // 换发
                            else if ("11370800MB28559184337011800900411".equals(auditTask.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20230224095448",
                                        dzbdguid);
                                if (record != null) {
                                    dataBean.set("JYRXKZHM", record.getStr("xkzbh"));
                                    dataBean.set("CBDJH", record.getStr("syqzsh"));
                                    dataBean.set("CM", record.getStr("cym"));
                                    dataBean.set("CBJYR", record.getStr("cbjyr"));
                                    dataBean.set("CJDJH", record.getStr("cjdjh"));
                                    dataBean.set("CBSYR", record.getStr("cbsyr"));
                                    dataBean.set("CBGLR", record.getStr("cbglr"));
                                    dataBean.set("GLRXKZHM", record.getStr("zgzsh"));
                                    dataBean.set("GCRQ", record.getStr("jcrq"));
                                    dataBean.set("gjrq", record.getStr("GJRQ"));
                                    dataBean.set("bcjyfw", record.getStr("BCJYFW"));
                                    dataBean.set("HDJYFW", record.getStr("xkzjyfw"));
                                    dataBean.set("ZJ", record.getStr("zjsl"));
                                    dataBean.set("QW", record.getStr("zjgl"));
                                    dataBean.set("zzd", record.getStr("ZZD"));
                                    dataBean.set("TEU", record.getStr("teubx"));
                                    dataBean.set("lfm", record.getStr("LFM"));
                                    dataBean.set("CW", record.getStr("CW"));
                                    dataBean.set("KW", record.getStr("KW"));

                                    dataBean.set("CJG", record.getStr("cjg"));
                                    dataBean.set("CBLX", record.getStr("cbzl"));
                                    dataBean.set("CBCL", record.getStr("cbcl"));
                                    dataBean.set("CBZD", record.getStr("zd"));
                                    dataBean.set("ZJ", record.getStr("zjsl"));
                                }

                                Date fzrq = new Date();
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(fzrq);
                                calendar.add(Calendar.YEAR, 5); // 把日期往后增加一年，整数往后推，负数往前移
                                Date enddate = calendar.getTime();
                                dataBean.set("FZRQ", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                                dataBean.set("ZN", EpointDateUtil.convertDate2String(enddate, "yyyy"));
                                dataBean.set("ZY", EpointDateUtil.convertDate2String(enddate, "MM"));
                                dataBean.set("ZR", EpointDateUtil.convertDate2String(enddate, "dd"));

                                String certNo = createJsGcZsbh(auditTask.getItem_id());
                                dataBean.set("BH", certNo);
                            }
                            // 注销
                            else if ("11370800MB28559184337011800900413".equals(auditTask.getItem_id())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20220630193941",
                                        dzbdguid);
                                if (record != null) {
                                    dataBean.set("bh", record.getStr("cbyyyszbh"));
                                    dataBean.set("cm", record.getStr("cm"));
                                    dataBean.set("cjg", record.getStr("cjg"));
                                    dataBean.set("cblx", record.getStr("cblx"));
                                    dataBean.set("jcrq", record.getStr("jcrq"));
                                    dataBean.set("cbsyr", record.getStr("cbsyr"));
                                    dataBean.set("cbjyr", record.getStr("cbjyr"));
                                    dataBean.set("cbyyzhddjyfw", record.getStr("cbyyzhddjyfw"));
                                    dataBean.set("zxyy", record.getStr("zxyy"));
                                    dataBean.set("qfrq", record.getStr("qfrq"));
                                }
                            }
                        }
                    }

                    // 医疗广告证明文件信息返回
                    if (auditProject != null && "医疗机构放射性职业病危害建设项目预评价报告审核".equals(certCatalog.getCertname())) {
                        Record record = iCxBusService.getDzbdDetail("formtable20211119194634",
                                dzbdguid);
                        if (record != null) {
                            String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");
                            dataBean.set("yljgmc", record.getStr("qingsryljgmc"));
                            dataBean.set("xmmc", record.getStr("qingsrxmmc"));
                            dataBean.set("xmdz", record.getStr("wenbk3"));
                            dataBean.set("xmxq", record.getStr("xmxz") + " " + record.getStr("jsjtnr"));
                            dataBean.set("fddbr", record.getStr("fddbr"));
                            dataBean.set("xmfzr", record.getStr("xmfzr"));
                            dataBean.set("lxr", record.getStr("lxr"));
                            dataBean.set("lxdh", record.getStr("lxdh"));
                            dataBean.set("ypjdw", record.getStr("ypjdw"));
                            dataBean.set("ypjbgbh", record.getStr("ypjbgbh"));
                            if (StringUtil.isNotBlank(record.getStr("danxkz8"))) {
                                dataBean.set("zybwhlb", record.getStr("danxkz8"));
                            }

                            if (StringUtil.isNotBlank(record.getStr("danxkz9"))) {
                                dataBean.set("zybwhlb", record.getStr("danxkz9"));
                            }

                            String idnum = record.getStr("idnum");
                            if (idnum.length() == 1) {
                                idnum = "00" + idnum;
                            } else if (idnum.length() == 2) {
                                idnum = "0" + idnum;
                            }

                            dataBean.set("bh", "济审放预〔" + year + "〕第" + idnum + "号");
                            dataBean.set("rq",
                                    EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                        }
                    }

                    // 济宁市放射诊疗建设项目职业病危害放射防护预评价审核备案凭证
                    if (auditProject != null && "医疗机构放射性职业病危害建设项目竣工验收".equals(certCatalog.getCertname())) {
                        Record record = iCxBusService.getDzbdDetail("formtable20211119221328",
                                dzbdguid);
                        if (record != null) {
                            String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");
                            dataBean.set("yljgmc", record.getStr("yljgmc"));
                            dataBean.set("xmmc", record.getStr("wbk1"));
                            dataBean.set("xmdz", record.getStr("wbk2"));
                            dataBean.set("xmxz", record.getStr("xmxz") + " " + record.getStr("xmjtnr"));
                            dataBean.set("fddbr", record.getStr("wbk3"));
                            dataBean.set("xmfzr", record.getStr("wbk5"));
                            dataBean.set("lxr", record.getStr("wbk4"));
                            dataBean.set("lxdh", record.getStr("wbk6"));
                            dataBean.set("kzxgpjdw", record.getStr("wbk15"));
                            dataBean.set("kzxgpjbgbh", record.getStr("wbk16"));
                            dataBean.set("zybwhlb", record.getStr("dxklb2"));
                            String idnum = record.getStr("idnum");
                            if (idnum.length() == 1) {
                                idnum = "00" + idnum;
                            } else if (idnum.length() == 2) {
                                idnum = "0" + idnum;
                            }
                            dataBean.set("bh", "济审放竣〔" + year + "〕第" + idnum + "号");
                            dataBean.set("rq",
                                    EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                        }
                    }

                    // 济宁市放射诊疗建设项目职业病危害放射防护预评价审核备案凭证
                    if (auditProject != null && "济宁市放射诊疗建设项目职业病危害放射防护设施竣工验收合格证".equals(certCatalog.getCertname())) {
                        Record record = iCxBusService.getDzbdDetail("formtable20211119221328",
                                dzbdguid);
                        if (record != null) {
                            String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");
                            dataBean.set("yljgmc", record.getStr("yljgmc"));
                            dataBean.set("xmmc", record.getStr("wbk1"));
                            dataBean.set("xmdz", record.getStr("wenbk3"));

                            if (StringUtil.isNotBlank(record.getStr("xj"))) {
                                dataBean.set("xmxz", record.getStr("xj") + " " + record.getStr("xjjtnr"));
                            }
                            if (StringUtil.isNotBlank(record.getStr("gj"))) {
                                dataBean.set("xmxz", record.getStr("gj") + " " + record.getStr("gjjtnr"));
                            }
                            if (StringUtil.isNotBlank(record.getStr("kj"))) {
                                dataBean.set("xmxz", record.getStr("kj") + " " + record.getStr("kjjtnr"));
                            }
                            dataBean.set("fddbr", record.getStr("wbk3"));
                            dataBean.set("lxr", record.getStr("wbk4"));
                            dataBean.set("xmfzr", record.getStr("wbk5"));
                            // dataBean.set("lxdh", record.getStr("wbk6"));
                            dataBean.set("zybwhlb", record.getStr("dxklb2"));
                            dataBean.set("kzxgpjdw", record.getStr("wbk15"));
                            dataBean.set("kzxgpjbgbh", record.getStr("wbk16"));
                            String idnum = record.getStr("idnum");
                            if (idnum.length() == 1) {
                                idnum = "00" + idnum;
                            } else if (idnum.length() == 2) {
                                idnum = "0" + idnum;
                            }
                            dataBean.set("bh", "济审放竣〔" + year + "〕第" + idnum + "号");

                            dataBean.set("rq",
                                    EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                        }
                    }

                    // 医疗广告证明文件信息返回
                    if (auditProject != null && "医疗广告审查（中医医疗机构除外）".equals(certCatalog.getCertname())) {
                        Record record = iCxBusService.getYlzmDetailByRowguid(auditProject.getRowguid());
                        if (record == null) {
                            //复制的新表
                            record = iCxBusService.getDzbdDetail("formtable20240112110112",
                                    dzbdguid);
                        }
                        if (record != null) {
                            dataBean.set("yljgdymc", record.getStr("yljgdymc"));
                            dataBean.set("zydjh", record.getStr("zydjh"));
                            dataBean.set("fddbr", record.getStr("fddbr"));
                            dataBean.set("sfzh", record.getStr("sfzh"));
                            dataBean.set("yljgdz", record.getStr("yljgdz"));
                            dataBean.set("qtlbwbk", record.getStr("dxwbk2fbwb"));

                            dataBean.set("zlkm", record.getStr("zlkm"));
                            dataBean.set("cws", record.getStr("cws"));
                            dataBean.set("jzsj", record.getStr("jzsj"));
                            dataBean.set("lxdh", record.getStr("lxdh"));
                            dataBean.set("ysggsc", record.getStr("ysggsc"));
                            dataBean.set("gbggsc", record.getStr("gbggsc"));
                            dataBean.set("scjl", record.getStr("scjl"));
                            dataBean.set("zmyxq", record.getStr("zmyxq"));
                            String idnumnew = record.getStr("idnumnew");
                            if (idnumnew.length() == 1) {
                                idnumnew = "00" + idnumnew;
                            } else if (idnumnew.length() == 2) {
                                idnumnew = "0" + idnumnew;
                            }

                            String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");
                            dataBean.set("zmwh", "济医广〔" + year + "〕第" + idnumnew + "号");

                            dataBean.set("scjl",
                                    "按照《医疗广告管理办法》(国家工商行政管理总局、卫生部令第26号，2006年11月10日发布)的有关规定，经审查，同意发布该医疗广告（具体内容和形式以经审查同意的广告成品样件为准)。");
                            dataBean.set("rq",
                                    EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));

                            // dataBean.set("rq", record.getStr("rq"));

                            String ggfblxresult = "";
                            String ggfblx = record.getStr("ggfblx");
                            if (ggfblx.contains(",")) {
                                String[] ggfblxs = ggfblx.split(",");
                                for (String datail : ggfblxs) {
                                    ggfblxresult += codeItemsService.getItemTextByCodeName("医疗广告_媒体类别", datail) + ";";
                                }
                            } else if (!ggfblx.contains(",") && StringUtil.isNotBlank(ggfblx)) {
                                ggfblxresult = codeItemsService.getItemTextByCodeName("医疗广告_媒体类别", ggfblx);
                            }
                            dataBean.set("ggfblx", ggfblxresult);

                            String yljglx = record.getStr("yljglx");
                            if (StringUtil.isNotBlank(yljglx)) {
                                dataBean.set("yljglx", codeItemsService.getItemTextByCodeName("医疗广告_机构类别", yljglx));
                            } else {
                                dataBean.set("yljglx", "");
                            }

                            String yxqx = record.getStr("yxqx");
                            Date startdate = new Date();

                            String starttime = EpointDateUtil.convertDate2String(startdate, "yyyy年MM月dd日");

                            Calendar calendar = new GregorianCalendar();
                            calendar.setTime(startdate);
                            calendar.add(Calendar.YEAR, 1); // 把日期往后增加一年，整数往后推，负数往前移
                            calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                            Date enddate = calendar.getTime();

                            String endtime = EpointDateUtil.convertDate2String(enddate, "yyyy年MM月dd日");
                            /*
                             * if(StringUtil.isNotBlank(yxqx)) {
                             * dataBean.set("sjyxqx",
                             * codeItemsService.getItemTextByCodeName(
                             * "医疗广告_校验有效期", yxqx) +
                             * "（自"+starttime+"至"+endtime+"）"); }else {
                             * dataBean.set("sjyxqx", ""); }
                             */
                            dataBean.set("sjyxqx", "壹年" + "（自" + starttime + "至" + endtime + "）");

                            String syzxs = record.getStr("syzxs");
                            if (StringUtil.isNotBlank(syzxs)) {
                                dataBean.set("syzxs", codeItemsService.getItemTextByCodeName("医疗广告_所有制形式", syzxs));
                            } else {
                                dataBean.set("syzxs", "");
                            }

                        }
                    }

                    // 申请省际普通货物水路运输经营许可
                    if (auditProject != null && "港航运输经营许可证（正本）".equals(certCatalog.getCertname())) {
                        Record record = iCxBusService.getSlysqykysq(auditProject.getRowguid());

                        if (record != null) {

                            dataBean.set("QYMC", auditProject.getApplyername());
                            dataBean.set("FDDBR", auditProject.getLegal());
                            dataBean.set("DZ", auditProject.getAddress());
                            dataBean.set("LKYS", record.getStr("jingyfwzykyhx"));
                            dataBean.set("HWYS", record.getStr("jingyfwzyhyhx"));
                            dataBean.set("JY", record.getStr("jingyfwjy"));

                            String jingjlx = record.getStr("jingjlx");
                            if (StringUtil.isNotBlank(jingjlx)) {
                                dataBean.set("JJLX", codeItemsService.getItemTextByCodeName("水路经济类型", jingjlx));
                            } else {
                                dataBean.set("JJLX", "");
                            }
                        }
                    }

                    // 申请省际普通货物水路运输经营许可
                    if (auditProject != null && "建筑节能技术产品初次认定".equals(certCatalog.getCertname())) {
                        Record record = iCxBusService.getJzjnjscprdwbxtbdByRowguid(auditProject.getRowguid());
                        AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), false)
                                .getResult();

                        if (record != null && auditTask != null) {
                            Record cpmc = iCxBusService.getCodeItemTextByValue(record.getStr("cpmc"));
                            if (cpmc != null) {
                                dataBean.set("cpmc", cpmc.getStr("itemtext"));
                            }
                            Record zxbz = iCxBusService.getCodeItemTextByValue(record.getStr("zxbz"));
                            if (zxbz != null) {
                                dataBean.set("zxbz", zxbz.getStr("itemtext"));
                            }

                            String idnum = createGgZsbh(auditTask.getItem_id(), record.getStr("cplb"));

                            dataBean.set("zsbh", idnum);
                            dataBean.set("qyjgdm", record.getStr("qyjgdm"));
                            dataBean.set("qyzcdz", record.getStr("qyzcdz"));
                            dataBean.set("qyscdz", record.getStr("qyscdz"));
                            dataBean.set("jcjgmc", record.getStr("jcjgmc"));
                            dataBean.set("bgbh", record.getStr("xsjcbgbh"));
                            dataBean.set("sqdw", record.getStr("sqdw"));
                            dataBean.set("syfw", "工业与民用建筑");
                            dataBean.set("qybzbh", record.getStr("qybzbh"));
                            dataBean.set("qybzcpm", record.getStr("qybzcpm"));
                        }

                        Date fzrq = new Date();
                        Calendar calendar = new GregorianCalendar();
                        calendar.setTime(fzrq);
                        calendar.add(Calendar.YEAR, 3); // 把日期往后增加一年，整数往后推，负数往前移
                        Date enddate = calendar.getTime();

                        dataBean.set("newfzrq", EpointDateUtil.convertDate2String(fzrq, "yyyy-MM-dd"));
                        dataBean.set("newyxqx", EpointDateUtil.convertDate2String(enddate, "yyyy-MM-dd"));

                    }

                    // 城市大型户外广告设置批准书信息返回
                    if (auditProject != null && "城市大型户外广告设置批准书".equals(certCatalog.getCertname())) {
                        Record record = iCxBusService.getCsdxggDetailByRowguid(auditProject.getRowguid());
                        if (record != null) {
                            dataBean.set("wtdlrdh", record.getStr("wtdlrdh"));
                            dataBean.set("ggh", record.getStr("ggh"));
                            dataBean.set("wtdlrsfzh", record.getStr("wtdlrsfzh"));
                            dataBean.set("ctggcph", record.getStr("ctggcph"));
                            dataBean.set("szqxs", record.getStr("szqxs"));
                            dataBean.set("tyxydm", record.getStr("tyxydm"));
                            dataBean.set("fddbrsfzh", record.getStr("fddbrsfzh"));
                            dataBean.set("sznr", record.getStr("sznr"));
                            dataBean.set("lhxg", record.getStr("lhxg"));
                            dataBean.set("mj", record.getStr("mj"));
                            dataBean.set("fddbrdh", record.getStr("fddbrdh"));
                            dataBean.set("ggc", record.getStr("ggc"));
                            dataBean.set("szqxz", record.getStr("szqxz"));
                            dataBean.set("ctggszly", record.getStr("ctggszly"));
                            dataBean.set("jgjcl", record.getStr("jgjcl"));
                            dataBean.set("ctggjdcxszh", record.getStr("ctggjdcxszh"));
                            dataBean.set("fddbrxm", record.getStr("fddbrxm"));
                            dataBean.set("szwz", record.getStr("szwz"));
                            dataBean.set("ggg", record.getStr("ggg"));
                            dataBean.set("sqrq", record.getStr("sqrq"));
                            dataBean.set("wtdlrxm", record.getStr("wtdlrxm"));
                            dataBean.set("sqdwmc", record.getStr("sqdwmc"));

                            String qx = record.getStr("qx");
                            if (StringUtil.isNotBlank(qx)) {
                                dataBean.set("qx", codeItemsService.getItemTextByCodeName("广告期限", qx));
                            } else {
                                dataBean.set("qx", "");
                            }

                            String ctggcllx = record.getStr("ctggcllx");
                            if (StringUtil.isNotBlank(ctggcllx)) {
                                dataBean.set("ctggcllx", codeItemsService.getItemTextByCodeName("车体广告_车辆类型", ctggcllx));
                            } else {
                                dataBean.set("ctggcllx", "");
                            }

                            String ldsggbw = record.getStr("ldsggbw");
                            if (StringUtil.isNotBlank(ldsggbw)) {
                                dataBean.set("ldsggbw", codeItemsService.getItemTextByCodeName("落地式广告设置部位", ldsggbw));
                            } else {
                                dataBean.set("ldsggbw", "");
                            }

                            String szbw = record.getStr("szbw");
                            if (StringUtil.isNotBlank(szbw)) {
                                dataBean.set("szbw", codeItemsService.getItemTextByCodeName("设置部位", szbw));
                            } else {
                                dataBean.set("szbw", "");
                            }

                            String fwcdcq = record.getStr("fwcdcq");
                            if (StringUtil.isNotBlank(fwcdcq)) {
                                dataBean.set("fwcdcq", codeItemsService.getItemTextByCodeName("房屋场地产权", fwcdcq));
                            } else {
                                dataBean.set("fwcdcq", "");
                            }

                            String pzxs = record.getStr("pzxs");
                            if (StringUtil.isNotBlank(pzxs)) {
                                dataBean.set("pzxs", codeItemsService.getItemTextByCodeName("批准形式", pzxs));
                            } else {
                                dataBean.set("pzxs", "");
                            }
                        }
                    }

                    // 城市大型户外广告设置批准书信息返回
                    if (auditProject != null && "图书期刊印刷委托书".equals(certCatalog.getCertname())) {
                        Record record = iCxBusService.getYsDetailByRowguid(auditProject.getRowguid());
                        if (record != null) {
                            dataBean.set("skm", record.getStr("skm"));
                            dataBean.set("wtsbh", record.getStr("wtsbh"));
                            dataBean.set("cbdwmc", record.getStr("cbdwmc"));
                            dataBean.set("cbdwdz", record.getStr("cbdwdz"));
                            dataBean.set("ysqymc", record.getStr("ysqymc"));
                            dataBean.set("ysqydz", record.getStr("ysqydz"));
                            dataBean.set("zfxdwdz", record.getStr("zfxdwdz"));
                            dataBean.set("zfxdwmc", record.getStr("zfxdwmc"));
                            dataBean.set("gjbzsh", record.getStr("gjbzsh"));
                            dataBean.set("banci", record.getStr("banci"));
                            dataBean.set("zgbzkh", record.getStr("zgbzkh"));
                            dataBean.set("kanqi", record.getStr("kanqi"));
                            dataBean.set("kaiben", record.getStr("kaiben"));
                            dataBean.set("yinshu", record.getStr("yinshu"));
                            dataBean.set("zyz", record.getStr("zyz"));
                            dataBean.set("yeshu", record.getStr("yeshu"));
                            dataBean.set("yinzhang", record.getStr("yinzhang"));
                            dataBean.set("zebian", record.getStr("zebian"));
                            dataBean.set("zishu", record.getStr("zishu"));
                            dataBean.set("dingjia", record.getStr("dingjia"));
                            dataBean.set("zebiann", record.getStr("zebiann"));
                            dataBean.set("ygys", record.getStr("ygys"));
                            dataBean.set("banshi", record.getStr("banshi"));
                            dataBean.set("zwyz", record.getStr("zwyz"));
                            dataBean.set("jyfs", record.getStr("jyfs"));
                            dataBean.set("tgsl", record.getStr("tgsl"));

                            dataBean.set("tbss", record.getStr("tbss"));
                            dataBean.set("yzgg", record.getStr("yzgg"));
                            dataBean.set("ysff", record.getStr("ysff"));
                            dataBean.set("yzsl", record.getStr("yzsl"));
                            dataBean.set("ywrq", record.getStr("ywrq"));
                            dataBean.set("zdff", record.getStr("zdff"));
                            dataBean.set("zdcs", record.getStr("zdcs"));
                            dataBean.set("jsrq", record.getStr("jsrq"));
                            dataBean.set("wtfjbrxm", record.getStr("wtfjbrxm"));
                            dataBean.set("wtfjbrdh", record.getStr("wtfjbrdh"));
                            dataBean.set("sfzhm", record.getStr("sfzhm"));
                            dataBean.set("stfjbrxm", record.getStr("stfjbrxm"));
                            dataBean.set("stfjbrdh", record.getStr("stfjbrdh"));
                            dataBean.set("strq", record.getStr("strq"));

                            String tblx = record.getStr("tblx");
                            if (StringUtil.isNotBlank(tblx)) {
                                dataBean.set("tblx", codeItemsService.getItemTextByCodeName("图书和期刊印刷委托书_图版类型", tblx));
                            } else {
                                dataBean.set("tblx", "");
                            }

                            dataBean.set("fzrq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));

                        }
                    }

                    // 建设工程竣工规划核实(金乡县)
                    if (auditProject != null) {
                        if ("11370828MB28631335437071500900101".equals(task.getItem_id())
                                && "建设工程竣工规划核实合格证（金乡）".equals(certCatalog.getCertname())) {
                            AuditSpISubapp subapp = iAuditSpISubapp.getSubappByGuid(auditProject.getSubappguid())
                                    .getResult();
                            if (subapp != null) {
                                AuditRsItemBaseinfo info = iAuditRsItemBaseinfo
                                        .getAuditRsItemBaseinfoByRowguid(subapp.getYewuguid()).getResult();
                                if (info != null) {
                                    dataBean.set("jsdw", info.getItemlegaldept());
                                    dataBean.set("jsxmmc", info.getItemname());
                                }
                            }
                            dataBean.set("fzjg", "金乡县自然资源和规划局");
                            dataBean.set("rq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                        }
                    }

                    // 特殊建设工程消防验收(金乡县)
                    if (auditProject != null) {
                        if ("11370828004332694N4370117044000".equals(task.getItem_id())
                                && "特殊建设工程消防验收意见书（金乡）".equals(certCatalog.getCertname())) {
                            AuditSpISubapp subapp = iAuditSpISubapp.getSubappByGuid(auditProject.getSubappguid())
                                    .getResult();
                            if (subapp != null) {
                                AuditRsItemBaseinfo info = iAuditRsItemBaseinfo
                                        .getAuditRsItemBaseinfoByRowguid(subapp.getYewuguid()).getResult();
                                if (info != null) {
                                    dataBean.set("jsdw", info.getItemlegaldept());
                                    dataBean.set("gcmc", info.getItemname());
                                }
                            }
                            dataBean.set("fzjg", "金乡县自然资源和规划局");
                            dataBean.set("fzrq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                        }
                    }

                    // 其他建设工程消防验收备案抽查(金乡县)
                    if (auditProject != null) {
                        if ("11370828004332694N4371017920000".equals(task.getItem_id())
                                && "建设工程消防验收备案凭证（金乡）".equals(certCatalog.getCertname())) {
                            AuditSpISubapp subapp = iAuditSpISubapp.getSubappByGuid(auditProject.getSubappguid())
                                    .getResult();
                            if (subapp != null) {
                                AuditRsItemBaseinfo info = iAuditRsItemBaseinfo
                                        .getAuditRsItemBaseinfoByRowguid(subapp.getYewuguid()).getResult();
                                if (info != null) {
                                    dataBean.set("jsdw", info.getItemlegaldept());
                                    dataBean.set("gcmc", info.getItemname());
                                }
                            }
                            dataBean.set("fzjg", "金乡县自然资源和规划局");
                            dataBean.set("fzrq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                        }
                    }

                    // 建设工程（含地下管线工程）档案验收(金乡县)
                    if (auditProject != null) {
                        if ("11370828004332694N437101703000101".equals(task.getItem_id())
                                && "建设工程竣工档案验收意见书（金乡）".equals(certCatalog.getCertname())) {
                            AuditSpISubapp subapp = iAuditSpISubapp.getSubappByGuid(auditProject.getSubappguid())
                                    .getResult();
                            if (subapp != null) {
                                AuditRsItemBaseinfo info = iAuditRsItemBaseinfo
                                        .getAuditRsItemBaseinfoByRowguid(subapp.getYewuguid()).getResult();
                                if (info != null) {
                                    dataBean.set("jsdw", info.getItemlegaldept());
                                    dataBean.set("gcmc", info.getItemname());
                                    if (StringUtil.isNotBlank(info.getItemstartdate())) {
                                        dataBean.set("kgrq", EpointDateUtil.convertDate2String(info.getItemstartdate(),
                                                "yyyy-MM-dd"));
                                    }
                                    if (StringUtil.isNotBlank(info.getItemfinishdate())) {
                                        dataBean.set("jgrq", EpointDateUtil.convertDate2String(info.getItemfinishdate(),
                                                "yyyy-MM-dd"));
                                    }
                                    // 勘察单位
                                    List<ParticipantsInfo> kcdw = iParticipantsInfo
                                            .findListBySubappguidAndCorptype(auditProject.getSubappguid(), "1");
                                    if (kcdw != null && !kcdw.isEmpty()) {
                                        dataBean.set("kcdw", kcdw.get(0).getCorpname());
                                    }
                                    // 设计单位
                                    List<ParticipantsInfo> sjdw = iParticipantsInfo
                                            .findListBySubappguidAndCorptype(auditProject.getSubappguid(), "2");
                                    if (sjdw != null && !sjdw.isEmpty()) {
                                        dataBean.set("sjdw", sjdw.get(0).getCorpname());
                                    }
                                    // 监理单位
                                    List<ParticipantsInfo> jldw = iParticipantsInfo
                                            .findListBySubappguidAndCorptype(auditProject.getSubappguid(), "4");
                                    if (jldw != null && !jldw.isEmpty()) {
                                        dataBean.set("jldw", jldw.get(0).getCorpname());
                                    }
                                    // 施工单位
                                    List<ParticipantsInfo> sgdw = iParticipantsInfo
                                            .findListBySubappguidAndCorptype(auditProject.getSubappguid(), "3");
                                    if (sgdw != null && !sgdw.isEmpty()) {
                                        dataBean.set("sgdw", sgdw.get(0).getCorpname());
                                    }
                                }
                            }
                            dataBean.set("fzjg", "金乡县自然资源和规划局");
                            dataBean.set("rq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                        }
                    }

                    // 人防工程竣工验收备案(金乡县)
                    if (auditProject != null) {
                        if ("11370828MB28602902437308000200101".equals(task.getItem_id())
                                && "人防工程竣工验收备案表（金乡）".equals(certCatalog.getCertname())) {
                            AuditSpISubapp subapp = iAuditSpISubapp.getSubappByGuid(auditProject.getSubappguid())
                                    .getResult();
                            if (subapp != null) {
                                AuditRsItemBaseinfo info = iAuditRsItemBaseinfo
                                        .getAuditRsItemBaseinfoByRowguid(subapp.getYewuguid()).getResult();
                                if (info != null) {
                                    dataBean.set("jsdwxx", info.getItemlegaldept());
                                    if (StringUtil.isNotBlank(info.getItemstartdate())) {
                                        dataBean.set("kgrq", EpointDateUtil.convertDate2String(info.getItemstartdate(),
                                                "yyyy-MM-dd"));
                                    }
                                    if (StringUtil.isNotBlank(info.getItemfinishdate())) {
                                        dataBean.set("jgysrq", EpointDateUtil
                                                .convertDate2String(info.getItemfinishdate(), "yyyy-MM-dd"));
                                    }
                                }
                                // 勘察单位
                                List<ParticipantsInfo> kcdw = iParticipantsInfo
                                        .findListBySubappguidAndCorptype(auditProject.getSubappguid(), "1");
                                if (kcdw != null && !kcdw.isEmpty()) {
                                    dataBean.set("kcdwmc", kcdw.get(0).getCorpname());
                                    dataBean.set("kcdwxmfzr", kcdw.get(0).getXmfzr());
                                }
                            }
                            // 查询电子表单内容渲染 -- 人防工程信息表
                            Record record = iCxBusService.getDzbdDetail("peopledefenseinfo",
                                    auditProject.getSubappguid());
                            if (record != null) {
                                dataBean.set("rfmj", record.getStr("rfarea"));
                                dataBean.set("symj", record.getStr("usearea"));
                                dataBean.set("gclb", record.getStr("projecttype"));
                                dataBean.set("jglx", record.getStr("structtype"));
                                dataBean.set("psyt", record.getStr("ordinaryuse"));
                                dataBean.set("zsyt", record.getStr("wartimeuse"));
                                dataBean.set("fhdj", record.getStr("defenselever"));
                                dataBean.set("ltk", record.getStr("connectingport"));
                                dataBean.set("swrk", record.getStr("outsidegate"));
                                dataBean.set("snrk", record.getStr("insidedate"));
                                dataBean.set("fsdazqk", record.getStr("protectiveequipmentinfo"));

                                dataBean.set("sjdwmc", record.getStr("rfdesigndepart"));
                                dataBean.set("sjdwxmfzr", record.getStr("rfdesigndepartleader"));
                                dataBean.set("sgdwmc", record.getStr("rfconstructiondepart"));
                                dataBean.set("sgdwfzr", record.getStr("rfconstructiondepartleader"));
                                dataBean.set("jldwmc", record.getStr("rfsupervisordepart"));
                                dataBean.set("jldwfzr", record.getStr("rfconstructiondepartleader"));

                                dataBean.set("fhsbazdw", record.getStr("fhequipmentdepart"));
                                dataBean.set("fhsbazfzr", record.getStr("azfzr"));
                            }

                            dataBean.set("fzjg", "金乡县自然资源和规划局");
                            dataBean.set("rq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                        }
                    }

                    // 房屋建筑工程和市政基础设施工程竣工验收备案(金乡县)
                    if (auditProject != null) {
                        if ("11370828MB286029024371017058000".equals(task.getItem_id())
                                && "建设工程竣工联合验收意见书(金乡）".equals(certCatalog.getCertname())) {
                            AuditSpISubapp subapp = iAuditSpISubapp.getSubappByGuid(auditProject.getSubappguid())
                                    .getResult();
                            if (subapp != null) {
                                AuditRsItemBaseinfo info = iAuditRsItemBaseinfo
                                        .getAuditRsItemBaseinfoByRowguid(subapp.getYewuguid()).getResult();
                                if (info != null) {
                                    dataBean.set("dwmc", info.getItemlegaldept());
                                    dataBean.set("xmmc", info.getItemname());
                                }
                            }
                            dataBean.set("fzjg", "金乡县自然资源和规划局");
                            dataBean.set("fzrq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                        }
                    }

                    // 房屋建筑工程和市政基础设施工程竣工验收备案(金乡县)
                    if (auditProject != null) {
                        if ("11370828MB286029024371017058000".equals(task.getItem_id())
                                && "房屋建筑工程和市政基础设施工程竣工验收备案表（金乡）".equals(certCatalog.getCertname())) {
                            AuditSpISubapp subapp = iAuditSpISubapp.getSubappByGuid(auditProject.getSubappguid())
                                    .getResult();
                            if (subapp != null) {
                                AuditRsItemBaseinfo info = iAuditRsItemBaseinfo
                                        .getAuditRsItemBaseinfoByRowguid(subapp.getYewuguid()).getResult();
                                if (info != null) {
                                    dataBean.set("jsdwmc", info.getItemlegaldept());
                                    dataBean.set("gcmc", info.getItemname());
                                    if (StringUtil.isNotBlank(info.getItemstartdate())) {
                                        dataBean.set("kgrq", EpointDateUtil.convertDate2String(info.getItemstartdate(),
                                                "yyyy-MM-dd"));
                                    }
                                    if (StringUtil.isNotBlank(info.getItemfinishdate())) {
                                        dataBean.set("jgysrq", EpointDateUtil
                                                .convertDate2String(info.getItemfinishdate(), "yyyy-MM-dd"));
                                    }
                                    AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService
                                            .findAuditSpSpJgysBySubappGuid(auditProject.getSubappguid());
                                    if (auditSpSpJgys != null) {
                                        dataBean.set("jsgm", auditSpSpJgys.getGcgm());
                                        dataBean.set("zjzmj", auditSpSpJgys.getAllbuildarea());
                                        dataBean.set("gczj", auditSpSpJgys.getGczj());
                                        dataBean.set("sgxkzbh", auditSpSpJgys.getJzzh());
                                    }
                                }
                            }
                            dataBean.set("fzjg", "金乡县自然资源和规划局");
                            dataBean.set("sqsj", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                            dataBean.set("clsj", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                        }
                    }

                    // 人力资源服务许可证
                    if (auditProject != null) {
                        if ("11370828MB28602902400011410700301".equals(task.getItem_id()) || "11370828MB28602902400011410700302".equals(task.getItem_id()) ) {
                            //发证日期
                            dataBean.set("fzrq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                        }
                    }

                    // 劳务派遣经营许可证
                    if (auditProject != null) {
                        if ("11370828MB28602902400011410800301".equals(task.getItem_id()) || "11370828MB28602902400011410800302".equals(task.getItem_id()) ) {
                            //生效日期
                            dataBean.set("sxrq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                            //发证日期
                            dataBean.set("fzrq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                            //失效日期
                            Calendar calendar = new GregorianCalendar();
                            calendar.setTime(new Date());
                            calendar.add(Calendar.YEAR, 3); // 把日期往后增加3年，整数往后推，负数往前移
                            Date enddate = calendar.getTime();
                            dataBean.set("sxrqi", EpointDateUtil.convertDate2String(enddate, "yyyy-MM-dd"));
                        }
                    }

                    if (auditProject != null && StringUtil.isNotBlank(auditProject.get("dataObj_baseinfo"))) {
                        JSONObject otherInfoJson = JSONObject.parseObject(auditProject.getStr("dataObj_baseinfo"));
                        Double qyclzbzl = 0.0;
                        Double chzzl = 0.0;
                        Double chzdc = 0.0;
                        Double chzdk = 0.0;
                        Double chzdg = 0.0;

                        for (Map.Entry<String, Object> en : otherInfoJson.entrySet()) {
                            String value = String.valueOf(en.getValue());
                            String key = String.valueOf(en.getKey());
                            String result = "";
                            if ("xclb".equals(key)) {
                                if (value.contains(",")) {
                                    String[] strs = value.split(",");
                                    for (String str : strs) {
                                        result += iCodeItemsService.getCodeItemByCodeID("1016076", str).getItemText()
                                                + ",";
                                    }
                                    result = result.substring(0, result.length() - 1);
                                } else {
                                    result = iCodeItemsService.getCodeItemByCodeID("1016076", value).getItemText();
                                }
                                dataBean.set("xclb", result);
                            } else if ("xcsm".equals(key)) {
                                dataBean.set("xcsm",
                                        iCodeItemsService.getCodeItemByCodeID("1016076", value).getItemText());
                            } else if ("scfs".equals(key)) {
                                dataBean.set("scfs",
                                        iCodeItemsService.getCodeItemByCodeID("1016069", value).getItemText());
                            } else if ("qyclzbzl".equals(key)) {
                                if (StringUtil.isBlank(en.getValue())) {
                                    qyclzbzl += Double.parseDouble("0");
                                    chzzl += Double.parseDouble("0");
                                } else {
                                    qyclzbzl += Double.parseDouble(en.getValue() + "");
                                    chzzl += Double.parseDouble(en.getValue() + "");
                                }
                            } else if ("gclzbzl".equals(key)) {
                                if (StringUtil.isBlank(en.getValue())) {
                                    qyclzbzl += Double.parseDouble("0");
                                    chzzl += Double.parseDouble("0");
                                } else {
                                    qyclzbzl += Double.parseDouble(en.getValue() + "");
                                    chzzl += Double.parseDouble(en.getValue() + "");
                                }
                            } else if ("hwzl".equals(key)) {
                                if (StringUtil.isNotBlank(en.getValue())) {
                                    chzzl += Double.parseDouble(en.getValue() + "");
                                    dataBean.set("hwzl", en.getValue());
                                }
                            } else if ("chzdc".equals(key)) {
                                chzdc += Double.parseDouble(en.getValue() + "");
                                dataBean.set("chzdc", en.getValue());
                            } else if ("chzdk".equals(key)) {
                                chzdk += Double.parseDouble(en.getValue() + "");
                                dataBean.set("chzdk", en.getValue());
                            } else if ("chzdg".equals(key)) {
                                chzdg += Double.parseDouble(en.getValue() + "");
                                dataBean.set("chzdg", en.getValue());
                            }
                            // 法定代表人
                            else if ("mastercs".equals(key)) {
                                dataBean.set("matercs", en.getValue());
                            } else {
                                dataBean.set(en.getKey(), en.getValue());
                            }
                        }
                        if (chzdc <= 20 && chzdk <= 3 && chzdg <= 4.2 && chzzl <= 49) {
                            dataBean.set("djlb", "1");
                        } else if (20 < chzdc && chzdc <= 28 && 3 < chzdk && chzdk <= 3.75 && 4.2 < chzdg && chzdg <= 4.5
                                && 49 < chzzl && chzzl <= 100) {
                            dataBean.set("djlb", "2");
                        } else if (chzdc > 28 || chzdk > 3.75 || chzdg > 4.5 || chzzl > 100) {
                            dataBean.set("djlb", "3");
                        }

                        dataBean.set("bz", "无");
                        dataBean.set("fzdw", "济宁市行政审批服务局");
                        dataBean.set("qfr", "济宁市审批员");
                        dataBean.set("fzrq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));

                        dataBean.set("qyclzbzl", qyclzbzl);
                        dataBean.set("chzzl", chzzl);
                        dataBean.set("xkzh", getRequestParameter("cxcode"));
                    }
                    String now = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd");
                    dataBean.set("pzrq", getUpperDate(now));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                    Date date = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    String begin = sdf.format(date);
                    calendar.add(Calendar.YEAR, 4);
                    Date date2 = calendar.getTime();
                    String dateFormat = sdf.format(date2);
                    dataBean.set("yxqx", begin + "至" + dateFormat);
                    String certownername = auditProject.getApplyername();
                    String certownercerttype = codeItemsService
                            .getCodeItemByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()).getItemValue();
                    String certownerno = auditProject.getCertnum();
                    addCallbackParam("type", auditProject.getApplyertype());
                    if (certCatalog.getBelongtype().contains(certownertype)
                            || CertConstant.CERTOWNERTYPE_HH.equals(certCatalog.getBelongtype())
                            || CertConstant.CERTOWNERTYPE_QT.equals(certCatalog.getBelongtype())) {
                        addCallbackParam("certownername", certownername);
                        addCallbackParam("certownercerttype", certownercerttype);
                        addCallbackParam("certownerno", certownerno);
                        addCallbackParam("certownerno", certownerno);
                        addCallbackParam("certownerno", certownerno);
                    }
                    addCallbackParam("awarddate", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                    // 设置证照正本文件guid
                    certinfo.setCertcliengguid(UUID.randomUUID().toString());
                    addViewData("certcliengguid", certinfo.getCertcliengguid());
                    // 设置证照附件（副本）guid
                    certinfo.setCopycertcliengguid(UUID.randomUUID().toString());
                    addViewData("copycertcliengguid", certinfo.getCopycertcliengguid());

                    // 设置附图附件guid
                    certinfo.set("ftfjcliengguid", UUID.randomUUID().toString());
                    addViewData("ftfjcliengguid", certinfo.get("ftfjcliengguid"));

                    // 返回渲染的字段(子表新增模式)
                    Map<String, Object> generateMapUseSubExtension = this.generateMapUseSubExtension("certaddaction",
                            metadataList, ZwfwConstant.MODE_ADD, ZwfwConstant.SUB_MODE_ADD);
                    addCallbackParam("controls", generateMapUseSubExtension);
                    // 渲染基本信息
                    List<String> repeatFields = new ArrayList<>();
                    for (CertMetadata certMetadata : metadataList) {
                        if (StringUtil.isNotBlank(certMetadata.getRelatedfield())) {
                            repeatFields.add(certMetadata.getRelatedfield());
                        }
                    }
                    addCallbackParam("basiccontrols",
                            this.generateBasicDefaultMap(metadataList, false, "certinfo") == null ? ""
                                    : this.generateBasicDefaultMap(metadataList, false, "certinfo"));

                    // 用作返回证照目录版本唯一标识
                    certinfo.setCertcatalogid(certcatalogid);
                    if (certCatalog.getBelongtype().contains(certownertype)
                            || CertConstant.CERTOWNERTYPE_HH.equals(certCatalog.getBelongtype())
                            || CertConstant.CERTOWNERTYPE_QT.equals(certCatalog.getBelongtype())) {
                        certinfo.setCertownertype(certownertype);
                    } else {
                        certinfo.setCertownertype(certCatalog.getBelongtype());
                    }
                    addCallbackParam("certcatalogid", certcatalogid);

                    // TODO
//                    判断当前事项关联的证照是否配置字段关系
//                    1）获取当前办件结果关联的证照，根据taskguid查询事项结果配置AUDIT_TASK_RESULT获取证照标识zhengzhaoguid
//                    2）根据zhengzhaoguid（rowguid）查询cert_indo，获取certid
//                    3）根据certid查询电子证照字段关联表（audit_cert_relation），是否能查询到记录，查询不到则结束
//                    4）查询到，获取到字段关系relationjson和phaseguid
//                    5）再根据当前办件的projectguid查询audit_project获取subappguid
//                    6）根据phaseguid判断是工程建设许可还是施工许可、立项用地规划许可还是竣工验收许可
//                    7）再根据subappguid查询对应的许可表，获取实例信息回填到证照业务信息中
                    log.info("开始查询当前事项关联的证照是否配置字段关系");
                    AuditCertRelation auditCertRelation = iZcCommonService.find(AuditCertRelation.class,"certid",certcatalogid);
                    log.info("auditCertRelation："+auditCertRelation);
                    if(auditCertRelation != null){
                        String resultJsonStr = auditCertRelation.getRelationjson();
                        if(StringUtil.isNotBlank(resultJsonStr)){
                            JSONObject resultJson = JSONObject.parseObject(resultJsonStr);
                            // 一张表单
                            if(JnConstant.ONEFORM.equals(auditCertRelation.getRelationtype())){
                                if(auditProject != null){
                                    String subappguid = auditProject.getSubappguid();
                                    log.info("subappguid："+subappguid);
                                    if(JnConstant.LXYDGHXK.equals(auditCertRelation.getPhaseguid())){
                                        AuditSpSpLxydghxk auditSpSpLxydghxk = iAuditSpSpLxydghxkService.findAuditSpSpLxydghxkBysubappguid(subappguid);
                                        if(auditSpSpLxydghxk != null){
                                            log.info("auditSpSpLxydghxk:"+auditSpSpLxydghxk);
                                            // 遍历 resultJson
                                            for (String key : resultJson.keySet()) {
                                                String value = resultJson.getString(key);
                                                key = key.replace("$value", "");
                                                dataBean.set(key, auditSpSpLxydghxk.get(value));
                                            }
                                        }
                                    }
                                    else if(JnConstant.SGXK.equals(auditCertRelation.getPhaseguid())){
                                        AuditSpSpSgxk auditSpSpSgxk = iAuditSpSpSgxkService.findAuditSpSpSgxkBysubappguid(subappguid);
                                        if(auditSpSpSgxk != null){
                                            log.info("auditSpSpSgxk:"+auditSpSpSgxk);
                                            // 遍历 resultJson
                                            for (String key : resultJson.keySet()) {
                                                String value = resultJson.getString(key);
                                                key = key.replace("$value", "");
                                                dataBean.set(key, auditSpSpSgxk.get(value));
                                            }
                                        }
                                    }else if(JnConstant.JGYL.equals(auditCertRelation.getPhaseguid())){
                                        AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService.findAuditSpSpJgysBySubappGuid(auditProject.getSubappguid());
                                        if(auditSpSpJgys != null){
                                            log.info("auditSpSpJgys:"+auditSpSpJgys);
                                            // 遍历 resultJson
                                            for (String key : resultJson.keySet()) {
                                                String value = resultJson.getString(key);
                                                key = key.replace("$value", "");
                                                dataBean.set(key, auditSpSpJgys.get(value));
                                            }
                                        }
                                    }
                                    else if(JnConstant.GCJSXK.equals(auditCertRelation.getPhaseguid())){
                                        AuditSpSpGcjsxk auditSpSpGcjsxk = iAuditSpSpGcjsxkService.findAuditSpSpGcjsxkBySubappGuid(auditProject.getSubappguid());
                                        if(auditSpSpGcjsxk != null){
                                            log.info("auditSpSpGcjsxk:"+auditSpSpGcjsxk);
                                            // 遍历 resultJson
                                            for (String key : resultJson.keySet()) {
                                                String value = resultJson.getString(key);
                                                key = key.replace("$value", "");
                                                dataBean.set(key, auditSpSpGcjsxk.get(value));
                                            }
                                        }
                                    }
                                }
                            }
                            // 电子表单
                            else if(JnConstant.EFORM.equals(auditCertRelation.getRelationtype())){
                                Map<String, Object> eFormDateMap = new HashMap<>();
                                String formid = auditCertRelation.getFormid();
                                String pformid = auditCertRelation.getPformid();
                                String isdbhy = auditCertRelation.getIsdbhy();
                                if(StringUtil.isNotBlank(formid)){
                                    String epointsformurl = ConfigUtil.getFrameConfigValue("epointsformurl");
                                    if (StringUtil.isNotBlank(epointsformurl) && !epointsformurl.endsWith("/")) {
                                        epointsformurl = epointsformurl + "/";
                                    }
                                    //查询表单
                                    JSONObject param = new JSONObject();
                                    JSONObject taskparam = new JSONObject();
                                    // 如果是多表合一
                                    if("1".equals(isdbhy)){
//                                        taskparam.put("formId", pformid);
//                                        taskparam.put("formIds", formid);
                                        taskparam.put("formId", formid);
                                    }
                                    else{
                                        taskparam.put("formId", formid);
                                    }
                                    taskparam.put("rowGuid", dzbdguid);
                                    param.put("params", taskparam);
                                    log.info("接口地址==="+epointsformurl);
                                    log.info("表单接口getPageData入参：===" + param.toJSONString());
                                    String result = HttpUtil.doPost(epointsformurl + "rest/sform/getPageData", param);
                                    log.info("表单接口getPageData返回：===" + result);
                                    JSONObject jsonObject = JSONObject.parseObject(result);
                                    int intValue = jsonObject.getJSONObject("status").getIntValue("code");
                                    if(intValue==1){
                                        JSONArray jsonObjectMainList = jsonObject.getJSONObject("custom").getJSONObject("recordData").getJSONArray("mainList");
                                        if(EpointCollectionUtils.isNotEmpty(jsonObjectMainList)){
                                            JSONArray jsonObjectRowList = jsonObjectMainList.getJSONObject(0).getJSONArray("rowList");
                                            // 遍历 jsonObjectStructList
                                            for (int i = 0; i < jsonObjectRowList.size(); i++) {
                                                JSONObject item = jsonObjectRowList.getJSONObject(i);
                                                String fieldchinesename = item.getString("FieldChineseName");
                                                String fieldname = item.getString("FieldName");
                                                String value = item.getString("value");
                                                eFormDateMap.put(fieldname, value);
                                            }
                                        }
                                        else{
                                            log.info("表单接口getPageData返回数据为空 换成subappguid 重新请求一遍");
                                            if(StringUtil.isNotBlank(auditProject.getSubappguid())){
                                                log.info("subappguid："+auditProject.getSubappguid());
                                                param = new JSONObject();
                                                taskparam = new JSONObject();
                                                taskparam.put("formId", formid);
                                                taskparam.put("rowGuid", auditProject.getSubappguid());
                                                param.put("params", taskparam);
                                                log.info("接口地址==="+epointsformurl);
                                                log.info("表单接口getPageData入参2：===" + param.toJSONString());
                                                result = HttpUtil.doPost(epointsformurl + "rest/sform/getPageData", param);
                                                log.info("表单接口getPageData返回2：===" + result);
                                                jsonObject = JSONObject.parseObject(result);
                                                intValue = jsonObject.getJSONObject("status").getIntValue("code");
                                                if(intValue==1){
                                                    jsonObjectMainList = jsonObject.getJSONObject("custom").getJSONObject("recordData").getJSONArray("mainList");
                                                    if(EpointCollectionUtils.isNotEmpty(jsonObjectMainList)){
                                                        JSONArray jsonObjectRowList = jsonObjectMainList.getJSONObject(0).getJSONArray("rowList");
                                                        // 遍历 jsonObjectStructList
                                                        for (int i = 0; i < jsonObjectRowList.size(); i++) {
                                                            JSONObject item = jsonObjectRowList.getJSONObject(i);
                                                            String fieldchinesename = item.getString("FieldChineseName");
                                                            String fieldname = item.getString("FieldName");
                                                            String value = item.getString("value");
                                                            eFormDateMap.put(fieldname, value);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    // 遍历 resultJson
                                    for (String key : resultJson.keySet()) {
                                        String value = resultJson.getString(key);
                                        key = key.replace("$value", "");
                                        dataBean.set(key, eFormDateMap.get(value));
                                    }
                                }
                            }
                        }
                    }

                } else {
                    // 获得证照附件（正本）guid
                    if (StringUtil.isBlank(certinfo.getCertcliengguid())) {
                        certinfo.setCertcliengguid(getViewData("certcliengguid"));
                    }
                    // 获得证照附件（副本）guid
                    if (StringUtil.isBlank(certinfo.getCopycertcliengguid())) {
                        certinfo.setCopycertcliengguid(getViewData("copycertcliengguid"));
                    }
                    // 获得附图附件guid
                    if (StringUtil.isBlank(certinfo.get("ftfjcliengguid"))) {
                        certinfo.set("ftfjcliengguid", getViewData("ftfjcliengguid"));
                    }

                    // 生成/打印证照，已经新增不用再插入
                    if (StringUtil.isNotBlank(getViewData("rowguid"))) {
                        certinfo = iCertInfoExternal.getCertInfoByRowguid(getViewData("rowguid"));
                        dataBean = iCertInfoExternal.getCertExtenByCertInfoGuid(getViewData("rowguid"));
                    }
                }
            }
        }

        if (!isPostback()) {
            if (CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsmultiowners())) {
                String no = certinfo.getCertownerno();
                JSONArray jsonArray = new JSONArray();
                if (StringUtil.isNotBlank(no)) {
                    String[] certownerno = no.split("\\^");
                    String[] certownername = certinfo.getCertownername().split("\\^");
                    String[] certownercerttype = certinfo.getCertownercerttype().split("\\^");
                    for (int i = 0; i < certownerno.length; i++) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("ownerno", certownerno[i]);
                        if (certownername.length > i) {
                            jsonObject.put("ownername", certownername[i]);
                        }
                        if (certownercerttype.length > i) {
                            jsonObject.put("ownertype", certownercerttype[i]);
                        }
                        jsonArray.add(jsonObject);
                    }

                } else {
                    JSONObject jsonObject = new JSONObject();
                    String ownername = auditProject.getApplyername();
                    String ownercerttype = codeItemsService
                            .getCodeItemByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()).getItemValue();
                    String ownerno = auditProject.getCertnum();
                    if (!warn) {
                        jsonObject.put("ownerno", ownerno);
                        jsonObject.put("ownername", ownername);
                        jsonObject.put("ownertype", ownercerttype);
                        jsonArray.add(jsonObject);
                    }
                }
                certownerinfo = jsonArray.toString();
                addCallbackParam("codetype", getCertOwnerCertTypeModel());
            }

            if (StringUtil.isNotBlank(certinfo.get("certawarddeptguid"))) {
                addCallbackParam("certawarddeptguid", certinfo.get("certawarddeptguid"));
            }
            if (StringUtil.isNotBlank(certinfo.getCertawarddept())) {
                addCallbackParam("certawarddept", certinfo.getCertawarddept().replace("^", ","));
            }
        }

        // 回传目录模板文件（副本）个数
        if (StringUtil.isNotBlank(certCatalog.getCopytempletcliengguid())) {
            copyTempletCount = iCertAttachExternal.getAttachList(certCatalog.getCopytempletcliengguid(), areacode)
                    .size();
        }
        addCallbackParam("copyTempletCount", copyTempletCount);
        addCallbackParam("officeweb365url", this.getOfficeConfig());
        addCallbackParam("ismultiowners", certCatalog.getIsmultiowners());
        if (CertConstant.CONSTANT_STR_ONE
                .equals(handleConfigService.getFrameConfig("AS_USE_CERTREST", "").getResult())) {
            addCallbackParam("isrest", "1");
        }

        log.info("搜索当前的证照实体：" + certinfo);

    }

    /**
     * 根据竣工验收，对电子证照照面信息通用渲染
     * @param old
     * @return
     */
    public CertInfoExtension userJsgcjgysbaxxbCertinfoExtenCommon(CertInfoExtension old, SpglJsgcjgysbaxxbV3 spglJsgcjgysbaxxbV3){
        //竣工验收备案编号
        old.set("bh", spglJsgcjgysbaxxbV3.getJgysbabh());
        return old;
    }
    
    /**
     * 电子证照照面信息通用渲染
     * @param old
     * @return
     */
    public CertInfoExtension initCertinfoExtenCommon(CertInfoExtension old){
        old.set("fzrq", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
        old.set("fzjg", userSession.getOuName());
        return old;
    }

    public String creatNo() {
        // 济审服企投〔2023〕27号
        StringBuffer certnobuffer = new StringBuffer();
        certnobuffer.append("济审服企投〔");
        int year = EpointDateUtil.getYearOfDate(new Date());
        certnobuffer.append(year).append("〕");

        boolean flag = false;
        //进入新的一年会没有记录，重置编号
        Record rec = iJNAuditProject.getMaxZjNum("hdglfwljsxm", String.valueOf(year));
        if (rec == null) {
            rec = new Record();
            rec.put("name", "hdglfwljsxm");
            rec.put("maxnum", 0);
            flag = true;
        }
        Integer maxnum = rec.getInt("maxnum");
        maxnum = maxnum + 1;
        if (flag) {
            new JNAuditProjectService().insetZjNum(rec.getStr("maxnum"), rec.getStr("name"), String.valueOf(year));
        } else {
            iJNAuditProject.UpdateMaxZjNum(String.valueOf(maxnum), "hdglfwljsxm", String.valueOf(year));
        }

        String num = String.valueOf(maxnum);
        if (num.length() == 1) {
            num = "00" + num;
        } else if (num.length() == 2) {
            num = "0" + num;
        }
        certnobuffer.append(num).append("号");
        return certnobuffer.toString();
    }

    /**
     * 保存并关闭 存在隐患，微服务时mongodb不回滚
     *
     * @param params      json (key:parentguid value:fieldname)
     * @param cliengguids json (key:fieldname value:cliengguid)
     */
    public void add(String params, String cliengguids) {
        JSONObject cliengguidsObject = JSON.parseObject(cliengguids);
        // 证照实例赋值
        setCertValue(params, cliengguids, cliengguidsObject);
        String msg = "";
        msg = iCertInfoExternal.saveCertinfo(certinfo, dataBean, areacode);
        if ("保存成功！".equals(msg)) {
            certinfo= iCertInfoExternal.getCertInfoByRowguid(certinfo.getRowguid());
            auditProject.setCertrowguid(certinfo.getRowguid());
            addViewData("rowguid", certinfo.getRowguid());
            auditProjectService.updateProject(auditProject);
        }
        addCallbackParam("msg", msg);
    }

    public void setCertValue(String params, String cliengguids, JSONObject cliengguidsObject) {
        JSONObject jsonObject = JSON.parseObject(params);
        CertCatalog certCatalog = iCertConfigExternal.getCatalogByCatalogid(certcatalogid, areacode);
        // 证照基本信息关联字段赋值
        for (CertMetadata certMetadata : metadataList) {
            if (StringUtil.isNotBlank(certMetadata.getRelatedfield())) {
                certinfo.set(certMetadata.getRelatedfield(), dataBean.get(certMetadata.getFieldname()));
            }
        }
        // 多持有人处理
        if (CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsmultiowners())) {

            JSONArray jsonArray = JSONArray.parseArray(certownerinfo);
            StringBuilder certownerno = new StringBuilder();
            StringBuilder certownername = new StringBuilder();
            StringBuilder certownercerttype = new StringBuilder();
            for (Object object : jsonArray) {
                // 新增持有人信息
                JSONObject jsonCert = (JSONObject) object;
                certownerno.append(jsonCert.getString("ownerno") + "^");
                certownername.append(jsonCert.getString("ownername") + "^");
                certownercerttype.append(jsonCert.getString("ownertype") + "^");
            }

            certownername.deleteCharAt(certownername.length() - 1);
            certownercerttype.deleteCharAt(certownercerttype.length() - 1);
            // 设置持有人字段
            certinfo.setCertownerno(certownerno.toString());
            certinfo.setCertownername(certownername.toString());
            certinfo.setCertownercerttype(certownercerttype.toString());
        }
        if (StringUtil.isNotBlank(certinfo.getCertawarddeptguid())) {
            StringBuilder oucodes = new StringBuilder();
            String[] ouguidarr = certinfo.getCertawarddeptguid().split(";");
            for (int i = 0; i < ouguidarr.length; i++) {
                FrameOuExtendInfo ouExtendInfo = ouService.getFrameOuExtendInfo(ouguidarr[i]);
                oucodes.append(
                        (StringUtil.isNotBlank(ouExtendInfo.getStr("orgcode")) ? ouExtendInfo.getStr("orgcode") : "")
                                + "^");
            }
            oucodes.deleteCharAt(oucodes.length() - 1);
            certinfo.setCertawarddeptorgcode(oucodes.toString());
        }
        certinfo.setCertawarddept(certinfo.getCertawarddept().replace(",", "^"));
        // 不为历史记录
        certinfo.setIshistory(ZwfwConstant.CONSTANT_INT_ZERO);

        if (StringUtil.isNotBlank(getViewData("rowguid"))) { // 保存
            // 更新基本信息
            certinfo.setOperatedate(new Date());
            // 排除持有人类型
            // certinfo.remove("certownertype");
            if (dataBean != null && !dataBean.isEmpty()) {
                // 更新照面信息，先删除，再插入，效率低
                try {
                    // 更新操作时间
                    dataBean.setOperatedate(new Date());
                    // 设置子数据
                    setSubExtensionJson(jsonObject, dataBean);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            }
        } else {
            // 新增
            // 新增基本信息
            String rowGuid = UUID.randomUUID().toString();
            certinfo.setRowguid(rowGuid);
            certinfo.setOperatedate(new Date());
            // 版本时间
            certinfo.setVersiondate(new Date());
            certinfo.setOperateusername(userSession.getDisplayName());
            String certid = UUID.randomUUID().toString();
            certinfo.setCertid(certid);
            String ouguid = null;
            // 设置事项的部门标识
            String taskguid = getRequestParameter("taskguid");
            if (StringUtil.isNotBlank(taskguid)) {
                AuditTask auditTask = auditTaskService.getAuditTaskByGuid(taskguid, false).getResult();
                if (auditTask != null) {
                    ouguid = auditTask.getOuguid();
                }
            }
            FrameOuExtendInfo ou = ouService.getFrameOuExtendInfo(ouguid);
            if (ou != null) {
                certinfo.set("certawarddeptorgcode", ou.get("orgcode"));
            }
            certinfo.setOuguid(UserSession.getInstance().getOuGuid());
            if (certCatalog != null) {
                // 证照目录编号
                certinfo.setCertcatcode(certCatalog.getCertcatcode());
                // 通用证照目录编号
                certinfo.setTycertcatcode(certCatalog.getTycertcatcode());
                // 证照类型代码
                certinfo.setCertificatetypecode(certCatalog.getCertificatetypecode());
            }
            // 电子证照编号
            if (StringUtil.isNotBlank(certinfo.getCertownerno())) {
                certinfo.setCertinfocode(certinfo.getCertownerno() + "-" + certinfo.getCertno());
            } else {
                certinfo.setCertinfocode(certinfo.getCertno());
            }

            // 可信等级
            certinfo.setCertlevel(ZwfwConstant.CERT_LEVEL_A);
            if (certCatalog != null) {
                // 关联区域 证照目录的唯一标志（区域）
                certinfo.setCertareaguid(certCatalog.getRowguid());
                // 关联证照目录版本
                certinfo.setCertcatalogversion(certCatalog.getVersion());
                // 关联证照目录唯一版本标识
                certinfo.setCertcatalogid(certCatalog.getCertcatalogid());
                // 设置证照目录的parentid
                certinfo.setParentcertcatalogid(certCatalog.getParentid());

                // 该目录需要年检
                if (certCatalog.getIssurvery() != null && certCatalog.getIssurvery() == ZwfwConstant.CONSTANT_INT_ONE) {
                    // 设置下次年检时间
                    Integer monthInterval = certCatalog.getSurveryrate(); // 年检间隔（月）
                    if (monthInterval != null && monthInterval > 0) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.MONTH, monthInterval);
                        certinfo.setInspectiondate(calendar.getTime());
                    }
                }
            }
            if (certCatalog != null) {
                // 证照名称
                certinfo.setCertname(certCatalog.getCertname());
            }
            // 版本 从1开始
            certinfo.setVersion(ZwfwConstant.CONSTANT_INT_ONE);
            // 不为历史记录
            certinfo.setIshistory(ZwfwConstant.CONSTANT_INT_ZERO);

            // 添加人信息
            certinfo.setAddusername(userSession.getDisplayName());
            certinfo.setAdduserguid(userSession.getUserGuid());
            // 证照状态(默认正常)
            certinfo.setStatus(ZwfwConstant.CERT_STATUS_DRAFT);
            // 设置为证照类型
            certinfo.setMaterialtype(ZwfwConstant.CONSTANT_STR_ONE);

            // 证照附件标识
            int certAttachCount = 0;
            if (StringUtil.isNotBlank(certinfo.getCertcliengguid())) {
                certAttachCount = iCertAttachExternal.getAttachList(certinfo.getCertcliengguid(), areacode).size();
            }
            // 附件不存在，置为空，生成证照标记置为0
            if (certAttachCount == 0) {
                certinfo.setIscreatecert(ZwfwConstant.CONSTANT_INT_ZERO);
            } else {
                certinfo.setIscreatecert(ZwfwConstant.CONSTANT_INT_ONE);
            }
            // 新增照面信息
            try {
                dataBean.setOperatedate(new Date());
                dataBean.setOperateusername(userSession.getDisplayName());
                dataBean.setRowguid(UUID.randomUUID().toString());
                // 关联证照基本信息
                dataBean.setCertinfoguid(certinfo.getRowguid());
                // 设置子数据
                setSubExtensionJson(jsonObject, dataBean);
                // 设置附件
                if (cliengguidsObject != null && !cliengguidsObject.isEmpty()) {
                    Set<String> keySet = cliengguidsObject.keySet();
                    if (ValidateUtil.isNotBlankCollection(keySet)) {
                        for (String fieldName : keySet) {
                            dataBean.set(fieldName, cliengguidsObject.get(fieldName));
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }

    }

    /**
     * 保存修改
     */
    @Deprecated
    public void save() {
        if (certinfo != null) {
            // 照面信息附件非空验证
            String msg = validateImageType();
            if (StringUtil.isNotBlank(msg)) {
                addCallbackParam("msg", msg);
                return;
            }
            // 保存证照
            msg = saveCert();
            // 返回消息
            addCallbackParam("msg", msg);
        }
    }

    public void saveCertInfo(String params, String cliengguids) {
        if (certinfo != null) {
            // 照面信息附件非空验证
            String msg = validateImageType();
            if (StringUtil.isNotBlank(msg)) {
                addCallbackParam("msg", msg);
                return;
            }
            JSONObject cliengguidsObject = JSON.parseObject(cliengguids);
            // 证照实例赋值
            setCertValue(params, cliengguids, cliengguidsObject);
            // 保存证照
            msg = saveCert();
            // 返回消息
            addCallbackParam("msg", msg);
        }
    }

    /**
     * 保存证照
     */
    private String saveCert() {
        if (certinfo == null) {
            return "";
        }
        // 更新基本信息
        certinfo.setOperateusername(userSession.getDisplayName());
        certinfo.setOperatedate(new Date());
        // certinfo.remove("certownertype"); // 排除持有人类型
        certinfo.setAdduserguid(userSession.getUserGuid());
        certinfo.setAddusername(userSession.getDisplayName());
        // 电子证照编号
        if (StringUtil.isNotBlank(certinfo.getCertownerno())) {
            certinfo.setCertinfocode(certinfo.getCertownerno() + "-" + certinfo.getCertno());
        } else {
            certinfo.setCertinfocode(certinfo.getCertno());
        }
        return iCertInfoExternal.saveCertinfo(certinfo, dataBean, areacode);
    }

    /**
     * 照面信息附件非空验证
     *
     * @return
     */
    private String validateImageType() {
        String msg = "";
        // 照面信息附件非空验证
        if (ValidateUtil.isNotBlankCollection(metadataList) && dataBean != null && !dataBean.isEmpty()) {
            Set<String> nameSet = new HashSet<>();
            for (CertMetadata metadata : metadataList) {
                if ("image".equals(metadata.getFieldtype().toLowerCase())
                        && ZwfwConstant.CONSTANT_STR_ONE.equals(metadata.getNotnull())) {
                    String cliengguid = dataBean.getStr(metadata.getFieldname());
                    if (StringUtil.isNotBlank(cliengguid)) {
                        int attchCount = iCertAttachExternal
                                .getAttachList(certCatalog.getCopytempletcliengguid(), areacode).size();
                        if (attchCount == 0) {
                            nameSet.add(metadata.getFieldchinesename());
                        }
                    }
                }
            }
            if (ValidateUtil.isNotBlankCollection(nameSet)) {
                msg = String.format("%s不能为空！", nameSet.toString());
            }
        }
        return msg;
    }

    /**
     * 设置照面信息子数据
     *
     * @param jsonObject
     * @return record 照面信息
     */
    public void setSubExtensionJson(JSONObject jsonObject, Record record) {
        if (jsonObject == null) {
            return;
        }
        Set<String> keySet = jsonObject.keySet();
        if (ValidateUtil.isNotBlankCollection(keySet)) {
            for (String parentguid : keySet) {
                List<CertInfoSubExtension> extensionList = iCertInfoExternal
                        .selectSubExtensionByParentguid("operatedate, subextension", parentguid);
                JSONArray jsonArray = new JSONArray();
                if (ValidateUtil.isNotBlankCollection(extensionList)) {
                    for (CertInfoSubExtension extension : extensionList) {
                        JSONObject subJsonObject = JSON.parseObject(extension.getSubextension());
                        // 设置主键
                        subJsonObject.put("rowguid", UUID.randomUUID().toString());
                        // 操作时间
                        subJsonObject.put("operatedate", extension.getOperatedate());
                        jsonArray.add(subJsonObject);
                    }
                }
                String json = JsonUtil.objectToJson(jsonArray);
                record.set(jsonObject.getString(parentguid), json);
            }
        }
    }

    /**
     * 生成证照
     *
     * @param cliengguids    json (key:fieldname value:cliengguid)
     * @param isGenerateCopy 是否生成副本
     * @throws ParseException
     */
    public void generateCert(String parentguidList, String cliengguids, String isGenerateCopy) throws ParseException {
        try {
            if (certCatalog == null) {
                return;
            }
            String msg = null;
            JSONObject jsonObject = JSON.parseObject(cliengguids);
            if ("edit".equals(getViewData("edit"))) {
                // 推送超限许可证相关信息
                if (StringUtil.isNotBlank(getRequestParameter("cxcode"))) {
                    msg = submitCxcltxz();
                    if ("error".equals(msg)) {
                        addCallbackParam("noorgcode", "推送省里失败，请联系管理员！");
                        return;
                    }
                }
                // 生成PDF
                log.info("parentguidList:" + parentguidList);
                log.info("cliengguids:" + cliengguids);
                log.info("projectguid:" + getRequestParameter("projectguid"));
                setCertValue(parentguidList, cliengguids, jsonObject);
                // 生成证照
                certinfo.set("projectguid", getRequestParameter("projectguid"));
                certinfo.set("cxcode", getRequestParameter("cxcode"));
                if (auditProject != null) {
                    AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), false)
                            .getResult();
                    if (auditTask != null && "11370800MB285591847370119022002".equals(auditTask.getItem_id())) {
                        //
                        certinfo.set("qrcode", "1");
                    }
                }
                certinfo = tsaxandsaveerwm(certinfo,true);
                if(certinfo!=null && StringUtils.isNotBlank(certinfo.getStr("pushText"))){
                    msg=certinfo.getStr("pushText");
                }else{
                    msg = iCertInfoExternal.generateCert(certinfo, dataBean, isGenerateCopy, areacode);
                }
                log.info("证照msg:" + msg);
                // 生成成功将rowguid放入缓存，并更新办件
                if ("生成证照成功！".equals(msg)) {
                    String certrowguids = auditProject.getCertrowguid();
                    log.info("生成证照办件的rowguid：" + certrowguids);
                    if (StringUtil.isNotBlank(certrowguids)) {
//                        String[] certrowguidlist = certrowguids.split(";");
                        AuditTaskResult auditresult = auditTaskResultService.getAuditResultByTaskGuid(auditProject.getTaskguid(), true).getResult();
//                        for (String certrowguid : certrowguidlist) {
//                            CertInfo info = certinfoservice.getCertInfoByRowguid(certrowguid);
//                            if (info.getCertcatalogid().equals(certinfo.getCertcatalogid())) {
//                                certrowguids.replace(certrowguid, certinfo.getRowguid());
//                            }
//                        }
                        if(!certrowguids.contains(certinfo.getRowguid()) && auditresult.getSharematerialguid().contains(certinfo.getCertcatalogid())){
                            certrowguids+=";"+certinfo.getRowguid();
                        }
                    } else {
                        certrowguids = certinfo.getRowguid();
                    }
//                    addViewData("rowguid", certinfo.getRowguid());
                    iJNAuditProject.updateProjectCertRowguid(certrowguids, auditProject.getRowguid());
                    EpointFrameDsManager.commit();

                }
            }
            else {
                // 推送超限许可证相关信息
                if (StringUtil.isNotBlank(getRequestParameter("cxcode"))) {
                    msg = submitCxcltxz();
                    if ("error".equals(msg)) {
                        addCallbackParam("noorgcode", "推送省里失败，请联系管理员！");
                        return;
                    }
                }
                setCertValue(parentguidList, cliengguids, jsonObject);
                certinfo.set("projectguid", getRequestParameter("projectguid"));
                certinfo.set("cxcode", getRequestParameter("cxcode"));
                certinfo = tsaxandsaveerwm(certinfo,true);
                if(certinfo!=null && StringUtils.isNotBlank(certinfo.getStr("pushText"))){
                    msg=certinfo.getStr("pushText");
                }else{
                    msg = iCertInfoExternal.generateCert(certinfo, dataBean, isGenerateCopy, areacode);
                }
                certinfo.setIshistory(0);
                log.info("证照msg:" + msg);
                // 生成成功将rowguid放入缓存，并更新办件
                if ("生成证照成功！".equals(msg)) {
                    if(auditProject!=null){
                        String certrowguids = auditProject.getCertrowguid();
                        AuditTaskResult auditresult = auditTaskResultService.getAuditResultByTaskGuid(auditProject.getTaskguid(), true).getResult();
                        if(auditresult!=null && StringUtils.isNotBlank(certrowguids) && !certrowguids.contains(certinfo.getRowguid()) && auditresult.getSharematerialguid().contains(certinfo.getCertcatalogid())){
                            certrowguids+=";"+certinfo.getRowguid();
                        } else {
                            certrowguids = certinfo.getRowguid();
                        }

                        log.info("证照certrowguids:" + certrowguids + ";projectguid:" + auditProject.getRowguid());
//                    addViewData("rowguid", certinfo.getRowguid());
                        iJNAuditProject.updateProjectCertRowguid(certrowguids, auditProject.getRowguid());
                        EpointFrameDsManager.commit();
                    }

                }
            }
            addCallbackParam("msg", msg);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 安许推送以及保存生成的二维码
     */
    public CertInfo tsaxandsaveerwm(CertInfo certinfo,boolean ifgjfm){
        if ("1".equals(certCatalog.get("is_push"))) {
//            String rowGuid = certinfo.getRowguid();
//            certinfo = iCertInfoExternal.getCertInfoByRowguid(certinfo.getRowguid());
//            if (StringUtil.isBlank(rowGuid)) {
//                rowGuid = UUID.randomUUID().toString();
//                certinfo.setRowguid(rowGuid);
//            }
//            if(certinfo!=null){
//                certInfoService.updateCertInfo(certinfo);
//            }else{
//                certInfoService.addCertInfo(certinfo);
//            }

            //推送安许
            GenerateBizlogic generateBizlogic = new GenerateBizlogic();
            ICertMetaData metaDataService = ContainerFactory.getContainInfo().getComponent(ICertMetaData.class);
            List<CertMetadata> metadataList = metaDataService.selectTopDispinListByCertguid(certinfo.getCertareaguid());
            // 获取word域名数组和域值数组
            Map<String, Object> wordMap = generateBizlogic.getWordInfo(metadataList, dataBean);
            // word域名数组
            String[] fieldNames = (String[]) wordMap.get("fieldNames");
            // word域值数组
            Object[] values = (Object[]) wordMap.get("values");
            // 图片map
            @SuppressWarnings("unchecked")
            Map<String, Record> imageMap = (Map<String, Record>) wordMap.get("imageMap");
            // 子表数据
            DataSet dataSet = (DataSet) wordMap.get("dataSet");

            certinfo = iJnCertInfo.pushfuma(fieldNames, values,certinfo, ifgjfm);
        }
        return certinfo;

    }


    /**
     * 赋码并生成证照 --针对安许证
     *
     * @param cliengguids json (key:fieldname value:cliengguid)
     * @throws ParseException
     */
    public void generateCertNew(String parentguidList, String cliengguids) throws ParseException {
        String isGenerateCopy = "0";
        try {
            if (certCatalog == null) {
                return;
            }
            String msg = null;
            JSONObject jsonObject = JSON.parseObject(cliengguids);
            if ("edit".equals(getViewData("edit"))) {
                setCertValue(parentguidList, cliengguids, jsonObject);
                // 生成证照
                certinfo.set("projectguid", getRequestParameter("projectguid"));
                certinfo.set("cxcode", getRequestParameter("cxcode"));
                certinfo.set("gxh", "1");
                certinfo = tsaxandsaveerwm(certinfo,true);
                if(certinfo!=null && StringUtils.isNotBlank(certinfo.getStr("pushText"))){
                    msg=certinfo.getStr("pushText");
                }else{
                    msg = iCertInfoExternal.generateCert(certinfo, dataBean, isGenerateCopy, areacode);
                }
                log.info("msg"+msg);
                // 生成成功将rowguid放入缓存，并更新办件
                if ("生成证照成功！".equals(msg)) {
                    String certrowguids = auditProject.getCertrowguid();
//                    if (StringUtil.isNotBlank(certrowguids)) {
//                        String[] certrowguidlist = certrowguids.split(";");
//                        for (String certrowguid : certrowguidlist) {
//                            CertInfo info = certinfoservice.getCertInfoByRowguid(certrowguid);
//                            if (info.getCertcatalogid().equals(certinfo.getCertcatalogid())) {
//                                certrowguids.replace(certrowguid, certinfo.getRowguid());
//                            }
//                        }
//                    } else {
//                        certrowguids = certinfo.getRowguid();
//                    }
                    AuditTaskResult auditresult = auditTaskResultService.getAuditResultByTaskGuid(auditProject.getTaskguid(), true).getResult();
                    if(!certrowguids.contains(certinfo.getRowguid()) && auditresult.getSharematerialguid().contains(certinfo.getCertcatalogid())){
                        certrowguids+=";"+certinfo.getRowguid();
                    } else {
                        certrowguids = certinfo.getRowguid();
                    }

//                    addViewData("rowguid", certinfo.getRowguid());
                    iJNAuditProject.updateProjectCertRowguid(certrowguids, auditProject.getRowguid());
                    EpointFrameDsManager.commit();

                }
            } else {
                setCertValue(parentguidList, cliengguids, jsonObject);
                certinfo.set("projectguid", getRequestParameter("projectguid"));
                certinfo.set("cxcode", getRequestParameter("cxcode"));
                certinfo.set("gxh", "1");
                certinfo = tsaxandsaveerwm(certinfo,true);
                if(certinfo!=null && StringUtils.isNotBlank(certinfo.getStr("pushText"))){
                    msg=certinfo.getStr("pushText");
                }else{
                    msg = iCertInfoExternal.generateCert(certinfo, dataBean, isGenerateCopy, areacode);
                }
                log.info("msg"+msg);
                certinfo.setIshistory(0);
                // 生成成功将rowguid放入缓存，并更新办件
                if ("生成证照成功！".equals(msg)) {
                    String certrowguids = auditProject.getCertrowguid();
//                    if (StringUtil.isNotBlank(certrowguids)) {
//                        certrowguids += ";" + certinfo.getRowguid();
//                    } else {
//                        certrowguids = certinfo.getRowguid();
//                    }
                    AuditTaskResult auditresult = auditTaskResultService.getAuditResultByTaskGuid(auditProject.getTaskguid(), true).getResult();
                    if(!certrowguids.contains(certinfo.getRowguid()) && auditresult.getSharematerialguid().contains(certinfo.getCertcatalogid())){
                        certrowguids+=";"+certinfo.getRowguid();
                    } else {
                        certrowguids = certinfo.getRowguid();
                    }
//                    addViewData("rowguid", certinfo.getRowguid());
                    iJNAuditProject.updateProjectCertRowguid(certrowguids, auditProject.getRowguid());
                    EpointFrameDsManager.commit();
                }
            }
            addCallbackParam("msg", msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更正-针对安许证
     *
     * @param cliengguids json (key:fieldname value:cliengguid)
     * @throws ParseException
     */
    public void generateCertGz(String parentguidList, String cliengguids) throws ParseException {
        String isGenerateCopy = "0";
        try {
            if (certCatalog == null) {
                return;
            }
            String msg = null;
            JSONObject jsonObject = JSON.parseObject(cliengguids);
            if ("edit".equals(getViewData("edit"))) {
                setCertValue(parentguidList, cliengguids, jsonObject);
                // 生成证照
                certinfo.set("projectguid", getRequestParameter("projectguid"));
                certinfo.set("cxcode", getRequestParameter("cxcode"));
                certinfo.set("gxh", "1");
                certinfo = tsaxandsaveerwm(certinfo,false);
                if(certinfo!=null && StringUtils.isNotBlank(certinfo.getStr("pushText"))){
                    msg=certinfo.getStr("pushText");
                }else{
                    msg = iCertInfoExternal.generateCert(certinfo, dataBean, isGenerateCopy, areacode);
                }
                log.info("msg"+msg);

                // 生成成功将rowguid放入缓存，并更新办件
                if ("生成证照成功！".equals(msg)) {
                    String certrowguids = auditProject.getCertrowguid();
//                    if (StringUtil.isNotBlank(certrowguids)) {
//                        String[] certrowguidlist = certrowguids.split(";");
//                        for (String certrowguid : certrowguidlist) {
//                            CertInfo info = certinfoservice.getCertInfoByRowguid(certrowguid);
//                            if (info.getCertcatalogid().equals(certinfo.getCertcatalogid())) {
//                                certrowguids.replace(certrowguid, certinfo.getRowguid());
//                            }
//                        }
//                    } else {
//                        certrowguids = certinfo.getRowguid();
//                    }
                    AuditTaskResult auditresult = auditTaskResultService.getAuditResultByTaskGuid(auditProject.getTaskguid(), true).getResult();
                    if(!certrowguids.contains(certinfo.getRowguid()) && auditresult.getSharematerialguid().contains(certinfo.getCertcatalogid())){
                        certrowguids+=";"+certinfo.getRowguid();
                    } else {
                        certrowguids = certinfo.getRowguid();
                    }
//                    addViewData("rowguid", certinfo.getRowguid());
                    iJNAuditProject.updateProjectCertRowguid(certrowguids, auditProject.getRowguid());
                    EpointFrameDsManager.commit();

                }
            } else {
                setCertValue(parentguidList, cliengguids, jsonObject);
                certinfo.set("projectguid", getRequestParameter("projectguid"));
                certinfo.set("cxcode", getRequestParameter("cxcode"));
                certinfo.set("gxh", "1");
                certinfo = tsaxandsaveerwm(certinfo,false);
                if(certinfo!=null && StringUtils.isNotBlank(certinfo.getStr("pushText"))){
                    msg=certinfo.getStr("pushText");
                }else{
                    msg = iCertInfoExternal.generateCert(certinfo, dataBean, isGenerateCopy, areacode);
                }
                log.info("msg"+msg);

                certinfo.setIshistory(0);
                // 生成成功将rowguid放入缓存，并更新办件
                if ("生成证照成功！".equals(msg)) {
                    String certrowguids = auditProject.getCertrowguid();
//                    if (StringUtil.isNotBlank(certrowguids)) {
//                        certrowguids += ";" + certinfo.getRowguid();
//                    } else {
//                        certrowguids = certinfo.getRowguid();
//                    }
                    AuditTaskResult auditresult = auditTaskResultService.getAuditResultByTaskGuid(auditProject.getTaskguid(), true).getResult();
                    if(!certrowguids.contains(certinfo.getRowguid()) && auditresult.getSharematerialguid().contains(certinfo.getCertcatalogid())){
                        certrowguids+=";"+certinfo.getRowguid();
                    } else {
                        certrowguids = certinfo.getRowguid();
                    }
//                    addViewData("rowguid", certinfo.getRowguid());
                    iJNAuditProject.updateProjectCertRowguid(certrowguids, auditProject.getRowguid());
                    EpointFrameDsManager.commit();
                }
            }
            addCallbackParam("msg", msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String submitCxcltxz() {
        String msg = "";
        // 推送企业信息
        String cxtxz = auditProject.getStr("dataObj_baseinfo");
        List<AuditProjectMaterial> projectMaterials = projectMaterialService
                .selectProjectMaterial(auditProject.getRowguid()).getResult();
        // 法人代表身份证图片
        String master_id_img = "无";
        // 授权委托书图片
        String wts_img = "无";
        // 经营许可证主键图片
        String org_cert_img = "无";
        // 车货总体轮廓图主键
        String total_img = "无";
        // 申请人身份证照片主键
        String agent_id_img = "无";
        // 机动车行驶证
        String drive_img = "无";
        String attachurl = "http://112.6.110.176:8080/jnzwfw/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=";
        if (projectMaterials != null && projectMaterials.size() > 0) {
            for (AuditProjectMaterial projectmater : projectMaterials) {
                List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(projectmater.getCliengguid());
                if (projectmater.getTaskmaterial().contains("道路运输经营许可证")) {
                    if (attachs != null && attachs.size() > 0) {
                        org_cert_img = attachurl + attachs.get(0).getAttachGuid();
                    }
                } else if (projectmater.getTaskmaterial().contains("授权委托书")) {
                    if (attachs != null && attachs.size() > 0) {
                        for (FrameAttachInfo attachinfo : attachs) {
                            if (attachinfo.getAttachFileName().contains("身份证")) {
                                agent_id_img = attachurl + attachinfo.getAttachGuid();
                                master_id_img = attachurl + attachinfo.getAttachGuid();
                            } else if (attachinfo.getAttachFileName().contains("委托书")) {
                                wts_img = attachurl + attachinfo.getAttachGuid();
                            }
                        }
                    }
                } else if (projectmater.getTaskmaterial().contains("车辆行驶证")) {
                    if (attachs != null && attachs.size() > 0) {
                        drive_img = attachurl + attachs.get(0).getAttachGuid();
                    }
                } else if (projectmater.getTaskmaterial().contains("前中后视角照片")) {
                    if (attachs != null && attachs.size() > 0) {
                        total_img = attachurl + attachs.get(0).getAttachGuid();
                    }
                }
            }
        }

        if (StringUtil.isNotBlank(cxtxz)) {
            String resulttoken = TARequestUtil.sendPostInner(zwdturl + "getaccesstoken", "{}", "", "");
            log.info("getaccesstoken接口上传返回信息：" + resulttoken);
            JSONObject jsontoken = JSON.parseObject(resulttoken);
            String accesstoken = jsontoken.getJSONObject("custom").getString("accesstoken");

            JSONObject json = JSON.parseObject(cxtxz);
            // 道路运输经营许可证号
            String dlysjyxkzh = json.getString("dlysjyxkzh");
            // 承运单位名称
            String cydwmc = json.getString("cydwmc");
            // 企业类型
            String qylx = json.getString("qylx");
            // 统一社会信用代码
            String tyshxydm = json.getString("tyshxydm");
            // 许可证有效期起
            Date xkzyxqq = EpointDateUtil.convertString2Date(json.getString("xkzyxqq"), "yyyy-MM-dd");
            // 许可证有效期止
            Date xkzyxqz = EpointDateUtil.convertString2Date(json.getString("xkzyxqz"), "yyyy-MM-dd");
            // 经办人姓名
            String jbrxm = json.getString("jbrxm");
            // 经办人身份证号
            String jbrsfzh = json.getString("jbrsfzh");
            // 经办人性别
            String jbrxb = json.getString("jbrxb");
            // 经办人电话
            String jbrdh = json.getString("jbrdh");
            // 行驶时间开始日期
            Date xssjksrq = EpointDateUtil.convertString2Date(json.getString("xssjksrq"), "yyyy-MM-dd");
            // 行驶时间结束时间
            Date xssjjssj = EpointDateUtil.convertString2Date(json.getString("xssjjssj"), "yyyy-MM-dd");
            // 出发地
            String cfd = json.getString("cfd");
            // 途径地
            String tjd = json.getString("tjd");
            // 目的地
            String mdd = json.getString("mdd");
            // 通行路线
            String txlx = json.getString("txlx");
            // 货物类型名称
            String hwlxmc = json.getString("hwlxmc");
            // 货物名称
            String hwmc = json.getString("hwmc");
            // 货物重量
            String hwzl = json.getString("hwzl");
            // 货物长度
            String hwcd = json.getString("hwcd");
            // 货物最大宽
            String hwzdk = json.getString("hwzdk");
            // 货物最大高
            String whzdg = json.getString("whzdg");
            // 牵引车辆号牌
            String qyclhp = json.getString("qyclhp");
            // 牵引车辆厂牌型号
            String qyclcpxh = json.getString("qyclcpxh");
            // 牵引车辆识别码
            String qyclsbm = json.getString("qyclsbm");
            // 牵引车辆整备质量
            String qyclzbzl = json.getString("qyclzbzl");
            // 牵引车长
            String qycc = json.getString("qycc");
            // 牵引车宽
            String qyck = json.getString("qyck");
            // 牵引车高
            String qycg = json.getString("qycg");
            // 牵引车轮胎数
            String qyclts = json.getString("qyclts");
            // 牵引车车轴数
            String qycczs = json.getString("qycczs");
            // 挂车辆类型
            String gcllx = json.getString("gcllx");
            // 挂车辆号牌
            String gclhp = json.getString("gclhp");
            // 挂车量厂牌型号
            String gclcpxh = json.getString("gclcpxh");
            // 挂车车辆识别代码
            String gcclsbdm = json.getString("gcclsbdm");
            // 挂车辆整备质量（t）
            String gclzbzl = json.getString("gclzbzl");
            // 挂车长（m）
            String gcc = json.getString("gcc");
            // 挂车宽（m）
            String gck = json.getString("gck");
            // 挂车高（m）
            String gcg = json.getString("gcg");
            // 挂车轮胎数
            String gclts = json.getString("gclts");
            // 挂车轴数
            String gczs = json.getString("gczs");
            // 挂车轴线
            String gczx = json.getString("gczx");
            // 车货总重量
            String chzzl = json.getString("chzzl");
            // 车货最大长
            String chzdc = json.getString("chzdc");
            // 车货最大高
            String chzdg = json.getString("chzdg");
            // 车货最大宽
            String chzdk = json.getString("chzdk");
            // 轴荷分布
            String zhfb = json.getString("zhfb");
            // 轮胎数
            String lts = json.getString("lts");
            // 轴数
            String zs = json.getString("zs");
            // 牵引车辆类型
            String qycllx = json.getString("qycllx");
            // 轴距
            String zj = json.getString("zj").trim();
            // 注册地址
            String regAddress = json.getString("regaddress");
            // 法定代表人
            String master = json.getString("mastercs");
            // 法人公民身份证号码
            String masterId = json.getString("masterId");

            Integer applyertype = auditProject.getApplyertype();
            String now = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHHmmss");
            String qlcguid = "371" + now;
            String applyguid = "370" + now;
            String applicantId = "373" + now;
            String gcguid = "372" + now;
            ;

            JSONObject jsonnew = new JSONObject();
            jsonnew.put("applicantId", applicantId);
            jsonnew.put("orgCertNo", dlysjyxkzh);
            jsonnew.put("name", auditProject.getApplyername());
            if (applyertype == 20) {
                jsonnew.put("type", "2");
            } else {
                jsonnew.put("type", "1");
            }
            jsonnew.put("creditCode", auditProject.getCertnum());
            jsonnew.put("userId", auditProject.getApplyername());
            jsonnew.put("deptId", auditProject.getAreacode());
            jsonnew.put("distCode", "山东省济宁市");
            SqlConditionUtil sqlutil = new SqlConditionUtil();
            sqlutil.eq("creditcode", auditProject.getCertnum());

            jsonnew.put("regAddress", regAddress);
            jsonnew.put("master", master);
            jsonnew.put("masterId", masterId);

            jsonnew.put("masterIdImg", master_id_img);
            jsonnew.put("contact",
                    StringUtil.isBlank(auditProject.getContactperson()) ? "无" : auditProject.getContactperson());
            jsonnew.put("mobile",
                    StringUtil.isBlank(auditProject.getContactmobile()) ? "无" : auditProject.getContactmobile());
            jsonnew.put("wtsImg", wts_img);
            jsonnew.put("orgCertImg", org_cert_img);
            jsonnew.put("startDate", EpointDateUtil.convertDate2String(xkzyxqq, "yyyy-MM-dd"));
            jsonnew.put("endDate", EpointDateUtil.convertDate2String(xkzyxqz, "yyyy-MM-dd"));
            jsonnew.put("licStatus", "0");
            jsonnew.put("descInfo", "");

            JSONObject json1 = new JSONObject();
            json1.put("detail", jsonnew.toString());
            json1.put("accesstoken", accesstoken);
            String result = TARequestUtil.sendPostInner(zwdturl + "submitcompany", json1.toJSONString(), "", "");
            log.info("submitcompany接口上传返回信息：" + result);
            if (StringUtil.isNotBlank(gclhp) && !"20".equals(qycllx)) {
                JSONObject jsonnew4 = new JSONObject();
                jsonnew4.put("vehicleId", gcguid);
                jsonnew4.put("vehicleKind", gcllx);
                jsonnew4.put("vehicleNo", gclhp);
                jsonnew4.put("vin", gcclsbdm);
                jsonnew4.put("model", gclcpxh);
                jsonnew4.put("length", gcc);
                jsonnew4.put("width", gck);
                jsonnew4.put("height", gcg);
                jsonnew4.put("weight", gclzbzl);
                jsonnew4.put("driveImg", drive_img);
                jsonnew4.put("axles", gczs);
                jsonnew4.put("tyles", gclts);
                jsonnew4.put("axes", gczx);
                JSONObject json4 = new JSONObject();
                json4.put("detail", jsonnew4.toString());
                json4.put("accesstoken", accesstoken);
                String resultsign = TARequestUtil.sendPostInner(zwdturl + "addbus", json4.toJSONString(), "", "");
                if (StringUtil.isNotBlank(resultsign)) {
                    JSONObject results = JSONObject.parseObject(resultsign);
                    JSONObject custom = results.getJSONObject("custom");
                    if ("1".equals(custom.getString("code"))) {
                        String desc = custom.getString("desc");
                        if (desc != null) {
                            if (desc.contains(",")) {
                                String[] descs = desc.split(",");
                                String oldgcguid = descs[1];
                                log.info("addbus接口返回挂车编号：" + oldgcguid);
                                gcguid = oldgcguid;
                                JSONObject jsonnew5 = new JSONObject();
                                jsonnew5.put("vehicleId", oldgcguid);
                                jsonnew5.put("vehicleKind", gcllx);
                                jsonnew5.put("vehicleNo", gclhp);
                                jsonnew5.put("vin", gcclsbdm);
                                jsonnew5.put("model", gclcpxh);
                                jsonnew5.put("length", gcc);
                                jsonnew5.put("width", gck);
                                jsonnew5.put("height", gcg);
                                jsonnew5.put("weight", gclzbzl);
                                jsonnew5.put("driveImg", drive_img);
                                jsonnew5.put("axles", gczs);
                                jsonnew5.put("tyles", gclts);
                                jsonnew5.put("axes", gczx);
                                JSONObject json5 = new JSONObject();
                                json5.put("detail", jsonnew5.toString());
                                json5.put("accesstoken", accesstoken);

                                String result1 = TARequestUtil.sendPostInner(zwdturl + "updatebus",
                                        json5.toJSONString(), "", "");
                                if (StringUtil.isNotBlank(result1)) {
                                    JSONObject result2 = JSONObject.parseObject(result1);
                                    JSONObject custom2 = result2.getJSONObject("custom");
                                    if ("1".equals(custom2.getString("code"))) {
                                        log.info("推送省平台成功：" + oldgcguid);
                                    } else {
                                        log.info("推送省平台失败：" + oldgcguid);
                                    }
                                } else {
                                    log.info("推送省平台失败：" + oldgcguid);
                                }

                                /*
                                 * CxBus busty =
                                 * iCxBusService.getCxbusByGuid(gcguid); if
                                 * (busty == null) { CxBus bus = new CxBus();
                                 * bus.setRowguid(UUID.randomUUID().toString());
                                 * bus.setVehicleid(gcguid);
                                 * bus.setVehiclekind(gcllx);
                                 * bus.setVehicleno(gclhp);
                                 * bus.setGcclsbdm(gcclsbdm);
                                 * bus.setModel(gclcpxh); bus.setcxlength(gcc);
                                 * bus.setWidth(gck); bus.setHeight(gcg);
                                 * bus.setWeight(gclzbzl);
                                 * bus.setDriveimg(drive_img);
                                 * bus.setAxles(gczs); bus.setTyles(gclts);
                                 * bus.setAxes(gczx); iCxBusService.insert(bus);
                                 * }else { busty.setVehiclekind(gcllx);
                                 * busty.setVehicleno(gclhp);
                                 * busty.setGcclsbdm(gcclsbdm);
                                 * busty.setModel(gclcpxh);
                                 * busty.setcxlength(gcc); busty.setWidth(gck);
                                 * busty.setHeight(gcg);
                                 * busty.setWeight(gclzbzl);
                                 * busty.setDriveimg(drive_img);
                                 * busty.setAxles(gczs); busty.setTyles(gclts);
                                 * busty.setAxes(gczx);
                                 * iCxBusService.update(busty); }
                                 */

                            }
                        } else {
                            CxBus bus = new CxBus();
                            bus.setRowguid(UUID.randomUUID().toString());
                            bus.setVehicleid(gcguid);
                            bus.setVehiclekind(gcllx);
                            bus.setVehicleno(gclhp);
                            bus.setGcclsbdm(gcclsbdm);
                            bus.setModel(gclcpxh);
                            bus.setcxlength(gcc);
                            bus.setWidth(gck);
                            bus.setHeight(gcg);
                            bus.setWeight(gclzbzl);
                            bus.setDriveimg(drive_img);
                            bus.setAxles(gczs);
                            bus.setTyles(gclts);
                            bus.setAxes(gczx);
                            iCxBusService.insert(bus);
                        }
                    }
                }
                log.info("addbus接口上传返回信息：" + resultsign);

            }

            if (StringUtil.isNotBlank(qyclhp)) {
                JSONObject jsonnew3 = new JSONObject();
                jsonnew3.put("vehicleId", qlcguid);
                jsonnew3.put("vehicleKind", qycllx);
                jsonnew3.put("vehicleNo", qyclhp);
                jsonnew3.put("vin", qyclsbm);
                jsonnew3.put("model", qyclcpxh);
                jsonnew3.put("length", qycc);
                jsonnew3.put("width", qyck);
                jsonnew3.put("height", qycg);
                jsonnew3.put("weight", qyclzbzl);
                jsonnew3.put("driveImg", drive_img);
                jsonnew3.put("axles", qycczs);
                jsonnew3.put("tyles", qyclts);
                jsonnew3.put("axes", 0);
                JSONObject json3 = new JSONObject();
                json3.put("detail", jsonnew3.toString());
                json3.put("accesstoken", accesstoken);
                String resultsign = TARequestUtil.sendPostInner(zwdturl + "addbus", json3.toJSONString(), "", "");
                if (StringUtil.isNotBlank(resultsign)) {
                    JSONObject results = JSONObject.parseObject(resultsign);
                    JSONObject custom = results.getJSONObject("custom");
                    if ("1".equals(custom.getString("code"))) {
                        String desc = custom.getString("desc");
                        if (desc != null) {
                            if (desc.contains(",")) {
                                String[] descs = desc.split(",");
                                String oldqlcguid = descs[1];
                                log.info("addbus接口返回牵引车编号：" + oldqlcguid);
                                qlcguid = oldqlcguid;
                                JSONObject jsonnew5 = new JSONObject();
                                jsonnew5.put("vehicleId", oldqlcguid);
                                jsonnew5.put("vehicleKind", qycllx);
                                jsonnew5.put("vehicleNo", qyclhp);
                                jsonnew5.put("vin", qyclsbm);
                                jsonnew5.put("model", qyclcpxh);
                                jsonnew5.put("length", qycc);
                                jsonnew5.put("width", qyck);
                                jsonnew5.put("height", qycg);
                                jsonnew5.put("weight", qyclzbzl);
                                jsonnew5.put("driveImg", drive_img);
                                jsonnew5.put("axles", qycczs);
                                jsonnew5.put("tyles", qyclts);
                                jsonnew5.put("axes", 0);
                                JSONObject json5 = new JSONObject();
                                json5.put("detail", jsonnew5.toString());
                                json5.put("accesstoken", accesstoken);

                                String result1 = TARequestUtil.sendPostInner(zwdturl + "updatebus",
                                        json5.toJSONString(), "", "");
                                if (StringUtil.isNotBlank(result1)) {
                                    JSONObject result2 = JSONObject.parseObject(result1);
                                    JSONObject custom2 = result2.getJSONObject("custom");
                                    if ("1".equals(custom2.getString("code"))) {
                                        log.info("推送省平台成功：" + oldqlcguid);
                                    } else {
                                        log.info("推送省平台失败：" + oldqlcguid);
                                    }
                                } else {
                                    log.info("推送省平台失败：" + oldqlcguid);
                                }

                                /*
                                 * CxBus busty =
                                 * iCxBusService.getCxbusByGuid(oldqlchp); if
                                 * (busty == null) { CxBus bus = new CxBus();
                                 * bus.setRowguid(UUID.randomUUID().toString());
                                 * bus.setVehicleid(qlcguid);
                                 * bus.setVehiclekind(gcllx);
                                 * bus.setVehicleno(gclhp);
                                 * bus.setGcclsbdm(gcclsbdm);
                                 * bus.setModel(gclcpxh); bus.setcxlength(gcc);
                                 * bus.setWidth(gck); bus.setHeight(gcg);
                                 * bus.setWeight(gclzbzl);
                                 * bus.setDriveimg(drive_img);
                                 * bus.setAxles(gczs); bus.setTyles(gclts);
                                 * bus.setAxes(gczx); iCxBusService.insert(bus);
                                 * }else { busty.setVehiclekind(gcllx);
                                 * busty.setVehicleno(gclhp);
                                 * busty.setGcclsbdm(gcclsbdm);
                                 * busty.setModel(gclcpxh);
                                 * busty.setcxlength(gcc); busty.setWidth(gck);
                                 * busty.setHeight(gcg);
                                 * busty.setWeight(gclzbzl);
                                 * busty.setDriveimg(drive_img);
                                 * busty.setAxles(gczs); busty.setTyles(gclts);
                                 * busty.setAxes(gczx);
                                 * iCxBusService.update(busty); }
                                 */
                            }
                        } else {
                            CxBus bus = new CxBus();
                            bus.setRowguid(UUID.randomUUID().toString());
                            bus.setVehicleid(qlcguid);
                            bus.setVehiclekind(qycllx);
                            bus.setVehicleno(qyclhp);
                            bus.setGcclsbdm(qyclsbm);
                            bus.setModel(qyclcpxh);
                            bus.setcxlength(qycc);
                            bus.setWidth(qyck);
                            bus.setHeight(qycg);
                            bus.setWeight(qyclzbzl);
                            bus.setDriveimg(drive_img);
                            bus.setAxles(qycczs);
                            bus.setTyles(qyclts);
                            bus.setAxes("0");
                            iCxBusService.insert(bus);
                        }
                    }
                }
                log.info("addbus接口上传返回信息：" + resultsign);
            }

            auditProject.setRemark(applyguid + ";" + qlcguid + ";" + gcguid);
            auditProjectService.updateProject(auditProject);

            if (StringUtil.isNotBlank(jbrxm)) {
                JSONObject jsonnew4 = new JSONObject();
                jsonnew4.put("proxyId", applyguid);
                jsonnew4.put("proxyCard", jbrsfzh);
                jsonnew4.put("proxyName", jbrxm);
                if ("男".equals(jbrxb)) {
                    jbrxb = "1";
                } else {
                    jbrxb = "0";
                }
                jsonnew4.put("proxySex", jbrxb);
                jsonnew4.put("proxyPhone", jbrdh);
                jsonnew4.put("proxyCardImg", agent_id_img);
                jsonnew4.put("proxyAuthorizationImg", wts_img);
                JSONObject json4 = new JSONObject();
                json4.put("detail", jsonnew4.toString());
                json4.put("accesstoken", accesstoken);
                String result2 = TARequestUtil.sendPostInner(zwdturl + "addapplyuser", json4.toJSONString(), "", "");
                log.info("addapplyuser接口上传返回信息：" + result2);
            }

            JSONObject jsonnew2 = new JSONObject();
            jsonnew2.put("requestId", applyguid);
            jsonnew2.put("reqNo", dataBean.getStr("xkzh"));
            jsonnew2.put("reqStatus", "52");
            jsonnew2.put("deptId", auditProject.getAreacode());
            jsonnew2.put("startDate", EpointDateUtil.convertDate2String(xssjksrq, "yyyy-MM-dd"));
            jsonnew2.put("endDate", EpointDateUtil.convertDate2String(xssjjssj, "yyyy-MM-dd"));
            jsonnew2.put("beginDist", cfd);
            jsonnew2.put("passDists", cfd + "," + tjd.replace("[", "").replace("]", "").replace("\"", "") + "," + mdd);
            jsonnew2.put("endDist", mdd);
            jsonnew2.put("roads", txlx);
            jsonnew2.put("cargoCate", hwlxmc);
            jsonnew2.put("cargoType", codeItemsService.getItemTextByCodeName("货物类型", hwlxmc));
            jsonnew2.put("cargoInfo", hwmc);
            jsonnew2.put("cargoWeight", hwzl);
            jsonnew2.put("cargoLength", hwcd);
            jsonnew2.put("cargoWidth", hwzdk);
            jsonnew2.put("cargoHeight", whzdg);
            jsonnew2.put("totalWeight", dataBean.getStr("chzzl"));
            jsonnew2.put("totalLength", chzdc);
            jsonnew2.put("totalWidth", chzdk);
            jsonnew2.put("totalHeight", chzdg);
            jsonnew2.put("tractorNo", qlcguid);
            if ("20".equals(qycllx)) {
                jsonnew2.put("trailerNo", "");
            } else {
                jsonnew2.put("trailerNo", gcguid);
            }

            String[] strs = zhfb.split("\\+");
            int total = 0;
            for (String s : strs) {
                String count = s.substring(0, 1);
                if (s.contains("*")) {
                    total += Integer.parseInt(count) * 2;
                } else {
                    total += Integer.parseInt(count);
                }
            }
            jsonnew2.put("axles", total);
            jsonnew2.put("tyles", lts);
            jsonnew2.put("wheelbases", zj);
            jsonnew2.put("axlesLoad", zhfb);
            jsonnew2.put("totalImg", total_img);
            jsonnew2.put("orgCertNo", dlysjyxkzh);
            jsonnew2.put("orgCertImg", org_cert_img);
            jsonnew2.put("applicantName", auditProject.getApplyername());
            jsonnew2.put("creditCode", auditProject.getCertnum());
            jsonnew2.put("proxyId", applyguid);
            jsonnew2.put("agentName", jbrxm);
            jsonnew2.put("agentSex", jbrxb);
            jsonnew2.put("agentId", jbrsfzh);
            jsonnew2.put("agentIdImg", agent_id_img);
            jsonnew2.put("agentTel", jbrdh);
            jsonnew2.put("authorizeImg", wts_img);
            jsonnew2.put("category", dataBean.getStr("djlb"));
            jsonnew2.put("planDoc", "无");

            JSONObject json2 = new JSONObject();
            json2.put("detail", jsonnew2.toString());
            json2.put("accesstoken", accesstoken);
            log.info("addapply入参：" + json2.toJSONString());
            String result4 = TARequestUtil.sendPostInner(zwdturl + "addapply", json2.toJSONString(), "", "");
            if (StringUtil.isNotBlank(result4)) {
                JSONObject results = JSONObject.parseObject(result4);
                JSONObject custom = results.getJSONObject("custom");
                if ("0".equals(custom.getString("code"))) {
                    if (StringUtil.isNotBlank(custom.getString("desc"))) {
                        if (custom.getString("desc").contains("申请单号相同")) {

                        } else {
                            return "error";
                        }
                    } else {
                        return "error";
                    }

                }
            }
            log.info("addapply接口上传返回信息：" + result4);

            JSONObject jsonnew4 = new JSONObject();
            jsonnew4.put("reqNo", dataBean.getStr("xkzh"));
            jsonnew4.put("optSeq", "1");
            jsonnew4.put("optCode", "true");
            jsonnew4.put("optBy", auditProject.getAcceptusername());
            jsonnew4.put("optDesc", "同意");
            JSONObject json4 = new JSONObject();
            json4.put("detail", jsonnew4.toString());
            json4.put("accesstoken", accesstoken);
            String result2 = TARequestUtil.sendPostInner(zwdturl + "addprocess", json4.toJSONString(), "", "");
            log.info("addapplyuser接口上传返回信息：" + result2);

            JSONObject jsonnew5 = new JSONObject();
            jsonnew5.put("reqNo", dataBean.getStr("xkzh"));
            jsonnew5.put("optSeq", "2");
            jsonnew5.put("optCode", "true");
            jsonnew5.put("optBy", auditProject.getAcceptusername());
            jsonnew5.put("optDesc", "办结");
            JSONObject json5 = new JSONObject();
            json5.put("detail", jsonnew5.toString());
            json5.put("accesstoken", accesstoken);
            String result3 = TARequestUtil.sendPostInner(zwdturl + "addprocess", json5.toJSONString(), "", "");
            log.info("addapplyuser接口上传返回信息：" + result3);
        }
        return msg;
    }

    /**
     * 打印证照
     *
     * @param isPrintCopy 是否打印副本
     */
    public void printCert(String isPrintCopy) {
        // 如果证照目录存在，同步本地的auditcertcatalog表
        if (certCatalog != null) {
            AuditCertCatalog catalog = iAuditCertCatalog.getAuditCertCatalogByCatalogid(certCatalog.getCertcatalogid())
                    .getResult();
            if (catalog == null) {
                String cliengguid = UUID.randomUUID().toString();
                String copycliengguid = UUID.randomUUID().toString();
                iAuditCertCatalog.createAuditCatalogByCertCatalog(certCatalog.getCertcatalogid(), cliengguid,
                        copycliengguid, certCatalog.getVersion());
                // 将模板附件保存至本地附件库
                // List<JSONObject> attachList =
                // iCertAttachExternal.getAttachList(certCatalog.getTempletcliengguid(),
                // areacode);
                List<JSONObject> attachList = null;
                if (certCatalog.getTdTempletcliengguid() != null ||  !("".equals(certCatalog.getTdTempletcliengguid()))) {
                    attachList = iCertAttachExternal.getAttachList(certCatalog.getTdTempletcliengguid(), areacode);
                    if (attachList.size() == 0) {
                        attachList = iCertAttachExternal.getAttachList(certCatalog.getTempletcliengguid(), areacode);
                    }
                } else {
                    attachList = iCertAttachExternal.getAttachList(certCatalog.getTempletcliengguid(), areacode);
                }
                if (attachList != null && attachList.size() > 0) {
                    for (JSONObject json : attachList) {
                        AttachUtil.saveFileInputStream(UUID.randomUUID().toString(), cliengguid,
                                json.getString("attachname"), json.getString("contentype"), "证照检索新增",
                                json.getLong("size"),
                                iCertAttachExternal.getAttach(json.getString("attachguid"), areacode),
                                userSession.getUserGuid(), userSession.getDisplayName());
                    }
                }
                // 如果存在模板副本，将副本也放入本地附件库
                if (StringUtil.isNotBlank(certCatalog.getTdCopytempletcliengguid())) {
                    List<JSONObject> copyAttachList = iCertAttachExternal
                            .getAttachList(certCatalog.getTdCopytempletcliengguid(), areacode);
                    if (copyAttachList != null && copyAttachList.size() > 0) {
                        for (JSONObject json : copyAttachList) {
                            AttachUtil.saveFileInputStream(UUID.randomUUID().toString(), copycliengguid,
                                    json.getString("attachname"), json.getString("contentype"), "证照检索新增",
                                    json.getLong("size"),
                                    iCertAttachExternal.getAttach(json.getString("attachguid"), areacode),
                                    userSession.getUserGuid(), userSession.getDisplayName());
                        }
                    }
                } else if (StringUtil.isNotBlank(certCatalog.getCopytempletcliengguid())) {
                    List<JSONObject> copyAttachList = iCertAttachExternal
                            .getAttachList(certCatalog.getCopytempletcliengguid(), areacode);
                    if (copyAttachList != null && copyAttachList.size() > 0) {
                        for (JSONObject json : copyAttachList) {
                            AttachUtil.saveFileInputStream(UUID.randomUUID().toString(), copycliengguid,
                                    json.getString("attachname"), json.getString("contentype"), "证照检索新增",
                                    json.getLong("size"),
                                    iCertAttachExternal.getAttach(json.getString("attachguid"), areacode),
                                    userSession.getUserGuid(), userSession.getDisplayName());
                        }
                    }
                }
            } else {
                if (catalog.getVersion() != null && !catalog.getVersion().equals(certCatalog.getVersion())) {
                    String cliengguid = UUID.randomUUID().toString();
                    String copycliengguid = UUID.randomUUID().toString();
                    iAuditCertCatalog.updateAuditCertCatalog(catalog, cliengguid, copycliengguid,
                            certCatalog.getVersion());
                    // 将模板附件保存至本地附件库
                    // List<JSONObject> attachList =
                    // iCertAttachExternal.getAttachList(certCatalog.getTempletcliengguid(),
                    // areacode);
                    List<JSONObject> attachList = null;
                    if (certCatalog.getTdTempletcliengguid() != null ||  !("".equals(certCatalog.getTdTempletcliengguid()))) {
                        attachList = iCertAttachExternal.getAttachList(certCatalog.getTdTempletcliengguid(), areacode);
                        if (attachList.size() == 0) {
                            attachList = iCertAttachExternal.getAttachList(certCatalog.getTempletcliengguid(),
                                    areacode);
                        }
                    } else {
                        attachList = iCertAttachExternal.getAttachList(certCatalog.getTempletcliengguid(), areacode);
                    }
                    if (attachList != null && attachList.size() > 0) {
                        for (JSONObject json : attachList) {
                            AttachUtil.saveFileInputStream(UUID.randomUUID().toString(), cliengguid,
                                    json.getString("attachname"), json.getString("contentype"), "证照检索新增",
                                    json.getLong("size"),
                                    iCertAttachExternal.getAttach(json.getString("attachguid"), areacode),
                                    userSession.getUserGuid(), userSession.getDisplayName());
                        }
                    }
                    // 如果存在模板副本，将副本也放入本地附件库
                    if (StringUtil.isNotBlank(certCatalog.getTdCopytempletcliengguid())) {
                        List<JSONObject> copyAttachList = iCertAttachExternal
                                .getAttachList(certCatalog.getTdCopytempletcliengguid(), areacode);
                        if (copyAttachList != null && copyAttachList.size() > 0) {
                            for (JSONObject json : copyAttachList) {
                                AttachUtil.saveFileInputStream(UUID.randomUUID().toString(), copycliengguid,
                                        json.getString("attachname"), json.getString("contentype"), "证照检索新增",
                                        json.getLong("size"),
                                        iCertAttachExternal.getAttach(json.getString("attachguid"), areacode),
                                        userSession.getUserGuid(), userSession.getDisplayName());
                            }
                        }
                    } else if (StringUtil.isNotBlank(certCatalog.getCopytempletcliengguid())) {
                        List<JSONObject> copyAttachList = iCertAttachExternal
                                .getAttachList(certCatalog.getCopytempletcliengguid(), areacode);
                        if (copyAttachList != null && copyAttachList.size() > 0) {
                            for (JSONObject json : copyAttachList) {
                                AttachUtil.saveFileInputStream(UUID.randomUUID().toString(), copycliengguid,
                                        json.getString("attachname"), json.getString("contentype"), "证照检索新增",
                                        json.getLong("size"),
                                        iCertAttachExternal.getAttach(json.getString("attachguid"), areacode),
                                        userSession.getUserGuid(), userSession.getDisplayName());
                            }
                        }
                    }
                }
            }
        }
        // 目录校验
        else {
            addCallbackParam("msg", "该证照对应的目录不存在！");
            return;
        }
        AuditCertCatalog auditCertCatalog = iAuditCertCatalog
                .getAuditCertCatalogByCatalogid(certCatalog.getCertcatalogid()).getResult();

        // 获取证照信息
        CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(getViewData("rowguid"));
        // 获取证照信息
        if (certinfo == null || certinfo.isEmpty()) {
            addCallbackParam("msg", "该证照不存在！");
            return;
        }

        // 添加提示信息
        if (copyTempletCount == 0) { // 不支持副本打印
            // 判断是否生成证照
            if (StringUtil.isBlank(certinfo.getCertcliengguid())) {
                addCallbackParam("msg", "请先生成证照");
                return;
            }
            List<JSONObject> attachInfoList = iCertAttachExternal.getAttachList(certinfo.getCertcliengguid(), areacode);
            if (ValidateUtil.isBlankCollection(attachInfoList) && ZwfwConstant.CONSTANT_STR_ZERO
                    .equals(handleConfigService.getFrameConfig("AS_USE_CERTREST", "").getResult())) {
                addCallbackParam("msg", "请先生成证照");
                return;
            }
            // 未先生成证照，提示
            boolean isGenerate = false;
            for (JSONObject attchInfo : attachInfoList) {
                if ("系统生成".equals(attchInfo.get("clienginfo"))) {
                    isGenerate = true;
                    break;
                }
            }
            if (!isGenerate && ZwfwConstant.CONSTANT_STR_ZERO
                    .equals(handleConfigService.getFrameConfig("AS_USE_CERTREST", "").getResult())) {
                addCallbackParam("msg", "请先生成证照");
                return;
            }
        } else { // 支持副本打印
            String cliengGuid = certinfo.getCertcliengguid();
            String tip = "正本";
            String cliengInfo = "系统生成";
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(isPrintCopy)) { // 打印副本
                cliengGuid = certinfo.getCopycertcliengguid();
                tip = "副本";
                cliengInfo = "系统生成（副本）";
            }
            if (StringUtil.isBlank(cliengGuid)) {
                addCallbackParam("msg", String.format("请先生成证照（%s）", tip));
                return;
            }

            List<JSONObject> attachInfoList = iCertAttachExternal.getAttachList(cliengGuid, areacode);
            if (ValidateUtil.isBlankCollection(attachInfoList) && ZwfwConstant.CONSTANT_STR_ZERO
                    .equals(handleConfigService.getFrameConfig("AS_USE_CERTREST", "").getResult())) {
                addCallbackParam("msg", String.format("请先生成证照（%s）", tip));
                return;
            }
            // 未先生成证照，提示
            boolean isGenerate = false;
            for (JSONObject attchInfo : attachInfoList) {
                if (cliengInfo.equals(attchInfo.get("clienginfo"))) {
                    isGenerate = true;
                    break;
                }
            }
            if (!isGenerate && ZwfwConstant.CONSTANT_STR_ZERO
                    .equals(handleConfigService.getFrameConfig("AS_USE_CERTREST", "").getResult())) {
                addCallbackParam("msg", String.format("请先生成证照（%s）", tip));
                return;
            }
        }

        // 打印正/副本
        Map<String, String> returnMap = null;
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(isPrintCopy)) { // 打印副本
            // 是否打印过（副本）
            if (StringUtil.isNotBlank(certinfo.getCopyprintdocguid())) { // 已打印
                addCallbackParam("attachguid", certinfo.getCopyprintdocguid());
                return;
            }
            // 没打印，打印
            returnMap = generateCertDoc(auditCertCatalog.getCopytempletcliengguid(), true);

        } else { // 打印正本
            // 是否打印过（正本）
            if (StringUtil.isNotBlank(certinfo.getPrintdocguid())) { // 已打印
                addCallbackParam("attachguid", certinfo.getPrintdocguid());
                return;
            }
            // 没打印，打印
            returnMap = generateCertDoc(auditCertCatalog.getTempletcliengguid(), false);
        }

        if (ValidateUtil.isNotBlankMap(returnMap)) {
            String isSuccess = returnMap.get("issuccess");
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(isSuccess)) { // 打印成功
                addCallbackParam("attachguid", returnMap.get("attachguid"));
                addCallbackParam("filename", certinfo.getCertname());
            } else { // 打印失败
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
    public Map<String, String> generateCertDoc(String cliengGuid, boolean isPrintCopy) {
        Map<String, String> returnMap = null;
        try {
            Map<String, Object> wordMap = getWordInfo(metadataList, dataBean);
            // word域名数组
            String[] fieldNames = (String[]) wordMap.get("fieldNames");
            // word域值数组
            Object[] values = (Object[]) wordMap.get("values");
            // 图片map
            @SuppressWarnings("unchecked")
            Map<String, Record> imageMap = (Map<String, Record>) wordMap.get("imageMap");
            // system.out.println("imageMap:" + wordMap.get("imageMap"));
            // 子表数据
            DataSet dataSet = (DataSet) wordMap.get("dataSet");
            returnMap = generatePrintDocSupportCopy(cliengGuid, isPrintCopy, userSession.getUserGuid(),
                    userSession.getDisplayName(), fieldNames, values, imageMap, dataSet, certinfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnMap;
    }

    /**
     * 生成打印的证照doc（支持副本打印）
     *
     * @param cliengGuid      证照模板（正/副本）guid
     * @param isPrintCopy     是否打印副本
     * @param userGuid        用户Guid
     * @param userDisPlayName 用户名称
     * @param fieldNames      word域
     * @param values          word域值
     * @param imageMap        图片map
     * @param dataSet         子表数据
     * @param certinfo        基本信息
     * @return
     */
    public Map<String, String> generatePrintDocSupportCopy(String cliengGuid, Boolean isPrintCopy, String userGuid,
                                                           String userDisPlayName, String[] fieldNames, Object[] values, Map<String, Record> imageMap, DataSet dataSet,
                                                           CertInfo certinfo) {
        // 返回值Map
        Map<String, String> returnMap = new HashMap<String, String>();
        // 检测模本文件
        Map<String, Object> templetMap = checkCatalogTemplet(cliengGuid, isPrintCopy);
        if (StringUtil.isNotBlank(templetMap.get("msg"))) {
            returnMap.put("issuccess", "0");
            returnMap.put("msg", (String) templetMap.get("msg"));
            return returnMap;
        }
        // system.out.println("模本文件获取到");
        // 获得模本文件attachStorage
        FrameAttachStorage frameAttachStorage = (FrameAttachStorage) templetMap.get("frameAttachStorage");

        ByteArrayOutputStream outputStream = null;
        ByteArrayInputStream inputStream = null;
        try {
            // 获取证书文件
            String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
            License license = new License();
            license.setLicense(licenseName);
            FontSettings.setFontsFolder(ClassPathUtil.getClassesPath() + "font" + File.separator, true);
            outputStream = new ByteArrayOutputStream();
            Document doc = new Document(frameAttachStorage.getContent());

            // 填充word域值
            doc.getMailMerge().execute(fieldNames, values);
            // 子表的渲染
            doc.getMailMerge().executeWithRegions(dataSet);
            // 插入图片（包括二维码）
            String qrcodeurl = ConfigUtil.getConfigValue("cert_qrcode_yq");
            // system.out.println("qrcodeurl:" + qrcodeurl);
            if (StringUtil.isBlank(qrcodeurl)) {
                qrcodeurl = certinfo.getCertname();
            } else {
                qrcodeurl += "?projectguid=" + auditProject.getRowguid() + "";
            }
            insertImagetoBookmark(doc, imageMap, true, qrcodeurl);
            // 添加水印(读取epointframe.properties的配置)
            String watermarkText = ConfigUtil.getConfigValue("cert_watermark_text");
            if (StringUtil.isNotBlank(watermarkText)) {
                // 转码
                watermarkText = new String(watermarkText.getBytes("ISO-8859-1"), "UTF-8");
                // 默认宽度500，高度100
                int watermarkWidth = GenerateUtil.parseInteger(ConfigUtil.getConfigValue("cert_watermark_width"), 500);
                int watermarkHeight = GenerateUtil.parseInteger(ConfigUtil.getConfigValue("cert_watermark_height"),
                        100);
                insertWatermarkText(doc, watermarkText, watermarkWidth, watermarkHeight);
            }

            // 保存成word
            doc.save(outputStream, SaveFormat.DOC);

            inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            // 打印正/副本
            String attachGuid = certinfo.getPrintdocguid();
            String clienginfo = "证照打印文件";
            if (isPrintCopy) { // 打印副本
                attachGuid = certinfo.getCopyprintdocguid();
                clienginfo = "证照打印文件（副本）";
            }
            boolean isPrint = false;
            FrameAttachInfo frameAttachInfo = null;
            if (StringUtil.isNotBlank(attachGuid)) {
                frameAttachInfo = attachService.getAttachInfoDetail(attachGuid);
                if (frameAttachInfo != null) {
                    isPrint = true;
                }
            }
            // 是否已打印
            if (isPrint) { // 已打印，更新
                attachService.updateAttach(frameAttachInfo, inputStream);
            } else { // 未打印，上传
                // 生成Attchguid
                if (StringUtil.isBlank(attachGuid)) {
                    attachGuid = UUID.randomUUID().toString();
                }
                // 实际插入的attachguid不同
                attachGuid = AttachUtil.saveFileInputStream(attachGuid, UUID.randomUUID().toString(),
                        frameAttachStorage.getAttachFileName(), ".doc", clienginfo, inputStream.available(),
                        inputStream, userGuid, userDisPlayName).getAttachGuid();
                // 是否打印副本
                if (!isPrintCopy) {
                    auditProject.setCertdocguid(attachGuid);
                } else {
                    auditProject.setCertdocguid(attachGuid);
                }
                auditProjectService.updateProject(auditProject);
            }

            // 打印成功
            returnMap.put("issuccess", "1");
            returnMap.put("attachguid", attachGuid);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return returnMap;
    }

    /**
     * 获得word填充数组
     *
     * @param certMetadataList
     * @param dataBean
     * @return
     */
    public Map<String, Object> getWordInfo(List<CertMetadata> certMetadataList, Record dataBean) {
        Map<String, Object> wordInfo = new HashMap<>();
        // word域名数组
        String[] fieldNames = new String[0];
        List<String> nameList = new ArrayList<>();
        // word域值数组
        Object[] values = new Object[0];
        List<String> valueList = new ArrayList<>();
        // 图片map
        Map<String, Record> imageMap = new HashMap<>();
        // 子表表格数据
        DataSet dataSet = new DataSet();

        // fieldNames和values值处理
        if (ValidateUtil.isNotBlankCollection(certMetadataList) && dataBean != null) {
            for (int i = 0; i < certMetadataList.size(); i++) {
                CertMetadata certMetadata = certMetadataList.get(i);
                // 是否有子表结构
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(certMetadata.getIsrelatesubtable())) { // 有子表
                    List<Record> recordList = new ArrayList<>();
                    String subJson = dataBean.getStr(certMetadata.getFieldname());
                    // JSON转list
                    try {
                        recordList = JsonUtil.jsonToList(subJson, Record.class);
                    } catch (Exception e) {
                        recordList = new ArrayList<>();
                    }

                    // 查询子元数据
                    List<CertMetadata> subMetaDataList = iCertConfigExternal
                            .selectSubDispinListByCertguid(certMetadata.getRowguid());

                    // system.out.println("subMetaDataList:" + subMetaDataList);
                    try {
                        if (ValidateUtil.isNotBlankCollection(subMetaDataList)
                                && ValidateUtil.isNotBlankCollection(recordList)) {
                            // 构造dataTable
                            DataTable dataTable = new DataTable(certMetadata.getFieldname());
                            // 创建column
                            List<String> columnList = new ArrayList<>();
                            for (CertMetadata subMetadata : subMetaDataList) {
                                dataTable.getColumns().add(subMetadata.getFieldname());
                                columnList.add(subMetadata.getFieldname());

                                // 如果配置了代码项渲染成代码项文本
                                for (Record record : recordList) {
                                    String itemText = "";
                                    if (StringUtil.isNotBlank(subMetadata.getDatasource_codename())) {
                                        itemText = codeItemsService
                                                .getItemTextByCodeName(subMetadata.getDatasource_codename(),
                                                        record.get(subMetadata.getFieldname().toLowerCase()) == null
                                                                ? ""
                                                                : record.get(subMetadata.getFieldname().toLowerCase()));
                                    }
                                    if (StringUtil.isNotBlank(itemText)) {
                                        record.put(subMetadata.getFieldname().toLowerCase(), itemText);
                                    }
                                }
                            }
                            // 每行数据赋值
                            for (Record record : recordList) {
                                List<Object> rowValueList = new ArrayList<>();
                                for (String column : columnList) {
                                    rowValueList.add(record.get(column.toLowerCase()));
                                }
                                Object[] rowValueArr = new Object[rowValueList.size()];
                                rowValueList.toArray(rowValueArr);
                                dataTable.getRows().add(rowValueArr);
                            }
                            dataSet.getTables().add(dataTable);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else { // 无子表
                    // 分离图片类型和普通类型
                    if ("image".equals(certMetadata.getFieldtype().toLowerCase())) { // 文件类型
                        String cliengguid = dataBean.getStr(certMetadata.getFieldname());
                        int attachCount = attachService.getAttachCountByClientGuid(cliengguid);
                        if (StringUtil.isNotBlank(cliengguid) && attachCount > 0) {
                            List<FrameAttachStorage> frameAttachStorageList = attachService
                                    .getAttachListByGuid(cliengguid);
                            Record imageReocrd = new Record();
                            imageReocrd.put("certmetadata", certMetadata);
                            imageReocrd.put("attachStorage", frameAttachStorageList.get(0));
                            imageMap.put(certMetadata.getFieldchinesename(), imageReocrd);
                        }
                    } else {
                        nameList.add(certMetadata.getFieldchinesename());
                        // 判断是否属于日期类型，如果是日期类型，则进行格式转换
                        if ("datetime".equals(certMetadata.getFieldtype().toLowerCase())) {
                            String dateStr = dataBean.getStr(certMetadata.getFieldname());
                            if (StringUtil.isNotBlank(dateStr)) {
                                // 先获取用户配置的日期格式
                                String dateformat = certMetadata.getDateFormat();
                                if (dateStr.contains(" ")) {
                                    dateStr = dateStr.split(" ")[0];
                                    Date date = EpointDateUtil.convertString2Date(dateStr.replace("/", "-"),
                                            "yyyy-MM-dd");
                                    if (StringUtil.isNotBlank(dateformat)) {
                                        try {
                                            valueList.add(EpointDateUtil.convertDate2String(date, dateformat));
                                        } catch (Exception e) {
                                            // TODO: handle exception
                                        }
                                    } else {
                                        valueList.add(EpointDateUtil.convertDate2String(date, "yyyy年MM月dd日"));
                                    }

                                } else {
                                    valueList.add(dateStr);
                                }
                            } else {
                                valueList.add(null);
                            }
                        } else {
                            valueList.add(dataBean.get(certMetadata.getFieldname()));
                        }
                    }
                }
            }
        }
        fieldNames = new String[nameList.size()];
        nameList.toArray(fieldNames);
        values = new Object[valueList.size()];
        valueList.toArray(values);
        wordInfo.put("fieldNames", fieldNames);
        wordInfo.put("values", values);
        wordInfo.put("imageMap", imageMap);
        wordInfo.put("dataSet", dataSet);
        return wordInfo;
    }

    /**
     * 插入水印
     *
     * @param doc
     * @param watermarkText 水印内容
     * @param width         宽度
     * @param height        高度
     */
    public static void insertWatermarkText(Document doc, String watermarkText, double width, double height) {
        Shape watermark = new Shape(doc, ShapeType.TEXT_PLAIN_TEXT);
        // 获得文本路径对象
        TextPath textPath = watermark.getTextPath();
        textPath.setText(watermarkText);
        // 不加粗
        textPath.setBold(false);
        // textPath.setSpacing(5000);
        // textPath.setSize(2);
        textPath.setFontFamily("宋体");
        watermark.setWidth(width);
        watermark.setHeight(height);
        // Text will be directed from the bottom-left to the top-right corner.
        watermark.setRotation(-40);
        // 填充颜色为浅灰
        watermark.getFill().setColor(Color.lightGray);
        watermark.setStrokeColor(Color.lightGray);
        // 设置透明度
        watermark.getFill().setOpacity(0.3);

        // 居中显示
        watermark.setRelativeHorizontalPosition(RelativeHorizontalPosition.PAGE);
        watermark.setRelativeVerticalPosition(RelativeVerticalPosition.PAGE);
        watermark.setWrapType(WrapType.NONE);
        // 显示在下方
        watermark.setVerticalAlignment(VerticalAlignment.BOTTOM);
        // 显示在右方
        watermark.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        // 创建一个新的段落，并把水印添加到段落
        Paragraph watermarkPara = new Paragraph(doc);
        watermarkPara.appendChild(watermark);

        // 水印插入到doc的每一页
        SectionCollection sections = doc.getSections();
        if (sections != null && sections.getCount() > 0) {
            for (Section section : sections) {
                insertParagraphIntoHeader(watermarkPara, section, HeaderFooterType.HEADER_PRIMARY);
                insertParagraphIntoHeader(watermarkPara, section, HeaderFooterType.HEADER_FIRST);
                insertParagraphIntoHeader(watermarkPara, section, HeaderFooterType.HEADER_EVEN);
            }
        }
    }

    /**
     * 水印段落插入header
     *
     * @param watermarkPara 段落
     * @param sect          文档的部分
     * @param headerType
     */
    public static void insertParagraphIntoHeader(Paragraph watermarkPara, Section sect, int headerType) {
        try {
            HeaderFooter header = sect.getHeadersFooters().get(headerType);
            // 如果没有header，创建
            if (header == null) {
                header = new HeaderFooter(sect.getDocument(), headerType);
                sect.getHeadersFooters().add(header);
            }
            // 水印插入到header
            header.appendChild(watermarkPara.deepClone(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入图片到书签
     *
     * @param doc
     * @param imageMap    图片map
     * @param isUseQrcode 是否使用二维码
     * @param qrcodeText  二维码文本
     */
    public static void insertImagetoBookmark(Document doc, Map<String, Record> imageMap, boolean isUseQrcode,
                                             String qrcodeText) {
        // system.out.println("开始插入图片到书签");
        // system.out.println("imageMap" + imageMap);
        // DocumentBuilder 类似一个文档操作工具，用来操作已经实例化的Document 对象，DocumentBuilder
        // 里有许多方法 例如插入文本、插入图片、插入段落、插入表格等等
        DocumentBuilder build = null;
        InputStream qrCodeInputStream = null;
        try {
            build = new DocumentBuilder(doc);
            // 是否添加二维码
            if (isUseQrcode && StringUtil.isNotBlank(qrcodeText)) {
                // 添加二维码 (读取epointframe.properties的配置)
                int qcodeWidth = GenerateUtil.parseInteger(ConfigUtil.getConfigValue("cert_qrcode_width_yq"), 100);
                int qcodeHeight = GenerateUtil.parseInteger(ConfigUtil.getConfigValue("cert_qrcode_height_yq"), 100);
                qrCodeInputStream = QrcodeUtil.getQrCode(qrcodeText, qcodeWidth, qcodeHeight);
                // 书签存在
                if (qrCodeInputStream != null && getBookmark(doc, "二维码") != null) {
                    // system.out.println("开始插入指定的标签");
                    // 指定对应的书签
                    build.moveToBookmark("二维码");
                    // 插入附件流信息
                    build.insertImage(qrCodeInputStream);
                }
            }

            // 插入照面信息的附件字段
            if (ValidateUtil.isNotBlankMap(imageMap)) {
                Set<String> keySet = imageMap.keySet();
                for (String fieldChineseName : keySet) {
                    // 是否存在对应的书签
                    if (getBookmark(doc, fieldChineseName) != null) {
                        // 指定对应的书签
                        build.moveToBookmark(fieldChineseName);
                        Record record = imageMap.get(fieldChineseName);
                        FrameAttachStorage storage = (FrameAttachStorage) record.get("attachStorage");
                        CertMetadata certMetadata = (CertMetadata) record.get("certmetadata");
                        if (storage != null && storage.getContent() != null) {
                            InputStream content = null;
                            try {
                                content = storage.getContent();
                                // 插入附件流信息
                                build.insertImage(storage.getContent(), RelativeHorizontalPosition.MARGIN, 0,
                                        RelativeVerticalPosition.MARGIN, 0,
                                        GenerateUtil.mmToPixel(certMetadata.getWidth() / 1.85),
                                        GenerateUtil.mmToPixel(certMetadata.getHeight()), WrapType.SQUARE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (content != null) {
                                    content.close();
                                }
                            }
                        }
                    }
                }
            }
            // system.out.println("结束插入图片到书签");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (qrCodeInputStream != null) {
                    qrCodeInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获得书签
     *
     * @param doc
     * @param bookmarkName
     * @return
     */
    private static Bookmark getBookmark(Document doc, String bookmarkName) {
        if (doc == null || StringUtil.isBlank(bookmarkName)) {
            return null;
        }
        BookmarkCollection bookmarkList = doc.getRange().getBookmarks();
        if (bookmarkList != null && bookmarkList.getCount() > 0) {
            for (Bookmark bookmark : bookmarkList) {
                if (StringUtil.isNotBlank(bookmark.getName()) && bookmark.getName().trim().equals(bookmarkName)) {
                    // system.out.println("获得标签成功");
                    return bookmark;
                }
            }
        }
        return null;
    }

    /**
     * 检测目录模板文件
     *
     * @param cliengGuid     模板文件（正/副本）guid
     * @param isGenerateCopy 是否生成副本
     * @return
     */
    public Map<String, Object> checkCatalogTemplet(String cliengGuid, boolean isGenerateCopy) {
        Map<String, Object> returnMap = new HashMap<>();
        String tip = "正本";
        if (isGenerateCopy) {
            tip = "副本";
        }

        // 模版文件cliengguid的非空验证
        if (StringUtil.isBlank(cliengGuid)) {
            returnMap.put("msg", String.format("未正确配置证照类型模版文件（%s）！", tip));
            return returnMap;
        }

        // 模版文件校验（正副本）
        List<FrameAttachStorage> frameAttachStorageList = attachService.getAttachListByGuid(cliengGuid);
        if (ValidateUtil.isBlankCollection(frameAttachStorageList)) {
            returnMap.put("msg", String.format("当前证照对应的证照类型模版文件（%s）为空，请联系管理员维护！", tip));
            return returnMap;
        }
        FrameAttachStorage frameAttachStorage = frameAttachStorageList.get(0);
        if (StringUtil.isBlank(frameAttachStorage.getContent())) {
            returnMap.put("msg", String.format("当前证照对应的证照类型模版文件（%s）内容为空，请联系管理员维护！", tip));
            return returnMap;
        }
        returnMap.put("frameAttachStorage", frameAttachStorage);
        return returnMap;
    }

    public void putCert() {
        if (certinfo != null && StringUtil.isNotBlank(certinfo.getCertcliengguid())) {
            if (StringUtil.isBlank(getViewData("rowguid"))) {
                addCallbackParam("msg", "请先保存证照信息！");
                return;
            }
            markFaFang(certinfo.getCertcliengguid());
            /*
             * AuditTask auditTask =
             * auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(),
             * false).getResult();
             * // 河道管理范围内建设项目工程建设方案审查 事项
             * if (auditTask != null &&
             * "11370800MB285591847370119022002".equals(auditTask.getItem_id()))
             * {
             * sendCertInfoToQzk();
             * }
             */

            addCallbackParam("msg", "ok");
        } else {
            addCallbackParam("msg", "请先生成证照");
        }
    }

    /**
     * 推送证照数据到前置库中
     */
    private void sendCertInfoToQzk() {
        Record record = new Record();
        record.set("Rowguid", UUID.randomUUID().toString());
        record.set("LICENSE_NUMBER", dataBean.getStr("zzbh"));
        record.set("CERTIFICATE_DATE", new Date());
        record.set("VALID_PERIOD_BEGIN", certinfo.getAwarddate()); //
        record.set("VALID_PERIOD_END", EpointDateUtil.convertString2Date("9999-12-30 23:59:59"));
        record.set("DEPT_ORGANIZE_CODE", "11370800MB28559184");
        record.set("DEPT_NAME", "济宁市行政审批服务局");
        record.set("DISTRICTS_CODE", "370800000000");
        record.set("DISTRICTS_NAME", "济宁市");
        record.set("HOLDER_TYPE", "法人");
        record.set("HOLDER_NAME", auditProject.getApplyername());
        record.set("CERTIFICATE_TYPE", "统一信用代码");
        record.set("CERTIFICATE_NO", auditProject.getCertnum());
        record.set("CONTACT_PHONE", auditProject.getContactmobile());
        record.set("PROJECT_NAME", auditProject.getProjectname());
        record.set("STATE", "insert新增");
        record.set("PERMIT_CONTENT", "");
        record.set("CERTIFICATE_LEVEL", "A");
        record.set("GUANYUXX", dataBean.getStr("xmmc")); //
        record.set("DANWEIMINGCHEN", dataBean.getStr("jsgs")); //
        record.set("BAOSONGBAOGAO", dataBean.getStr("bsfhpjbg")); //
        record.set("DUIXXSHENCHA", dataBean.getStr("scfhpjbg")); //
        record.set("FUJIANMINGCHEN", dataBean.getStr("fjfhpjbg")); //
        record.set("OperateDate", new Date());
        YQCertInfoUtil.insertInfo(record);
    }

    /**
     * 发放审批结果
     */
    public void markFaFang(String cliengguid) {
        AuditTaskResult auditResult = auditTaskResultService
                .getAuditResultByTaskGuid(getRequestParameter("taskguid"), true).getResult();

        // 自建系统办件在发放前自动暂停
        if ("1".equals(auditProject.getIs_pause())) {
            handleProjectService.handleRecover(auditProject, userSession.getDisplayName(), userSession.getUserGuid(),
                    null, null);
        }

        // 生成审批结果
        attachService.copyAttachByClientGuid(cliengguid, null, null, getRequestParameter("projectguid"));

        // 1、更新办件发证信息
        auditProject.setIs_cert(ZwfwConstant.CONSTANT_INT_ONE);
        auditProject.setCertificatedate(new Date());
        auditProject.setCertificateuserguid(userSession.getUserGuid());
        auditProject.setCertificateusername(userSession.getDisplayName());
        EpointFrameDsManager.begin(null);
        auditProjectService.updateProject(auditProject);
        EpointFrameDsManager.commit();
        // 更新证照信息，草稿状态变更为正式状态
        certinfo.setStatus(ZwfwConstant.CERT_STATUS_COMMON);
        certCatalog = iCertConfigExternal.getCatalogByCatalogid(certinfo.getCertcatalogid(), areacode);
        // if (certCatalog.getIsoneCertInfo() == ZwfwConstant.CONSTANT_INT_ZERO)
        // {//TODO
        // // 不允许多实例，通过持有人证件号码，目录唯一标识，持有人类型来判断
        // SqlUtils sql = new SqlUtils();
        // sql.eq("ishistory", ZwfwConstant.CONSTANT_STR_ZERO);
        // sql.eq("status", ZwfwConstant.CERT_STATUS_COMMON);
        // sql.eq("certcatalogid", certCatalog.getCertcatalogid());
        // sql.eq("certownerno",certinfo.getCertownerno());
        // sql.eq("certownertype",certinfo.getCertownertype());
        // List<CertInfo> readycertinfos=
        // iCertInfo.getListByCondition(sql.getMap());
        // if(readycertinfos!=null&&readycertinfos.size()>0){
        // CertInfo readycertinfo=readycertinfos.get(0);
        // // 更新实体信息。生成版本，前面版本设置为历史版本
        // certinfo.setCertid(readycertinfo.getCertid());
        // certinfo.setVersion(readycertinfo.getVersion() + 1);
        // certinfo.setIshistory(ZwfwConstant.CONSTANT_INT_ZERO);
        // // 上一个版本变更为历史
        // readycertinfo.setIshistory(ZwfwConstant.CONSTANT_INT_ONE);
        // iCertInfoExternal.submitCertInfo(readycertinfo,dataBean,certinfo.getCertcliengguid(),ZwfwConstant.Material_ZZ,areacode);
        // }
        // }
        iCertInfoExternal.updateCert(areacode, certinfo.getRowguid(), certinfo.getCertcliengguid(),
                certinfo.getCopycertcliengguid());
        // 如果是在主题内进行共享的，那么还需要更新主题中的结果状态
        if (StringUtil.isNotBlank(auditProject.getBiguid()) && StringUtil.isNotBlank(auditProject.getSubappguid())) {

            List<AuditTaskResult> resultlist = auditTaskResultService
                    .selectResultByTaskID("rowguid", auditProject.getTask_id()).getResult();
            StringBuffer materilids = new StringBuffer();
            materilids.append("'");
            for (AuditTaskResult auditTaskResult : resultlist) {
                materilids.append(auditTaskResult.getRowguid());
                materilids.append("','");
            }
            ;
            materilids.append("'");
            AuditSpShareMaterialRelation auditSpShareMaterialRelation = relationService
                    .getRelationByIDS(auditProject.getBusinessguid(), materilids.toString()).getResult();
            if (auditSpShareMaterialRelation != null) {
                handleSpiMaterialService.updateResultStatusAndAttachAndCert(auditProject.getSubappguid(),
                        auditSpShareMaterialRelation.getSharematerialguid(), 10, getRequestParameter("projectguid"),
                        certinfo.getRowguid());
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("biguid", auditProject.getBiguid());
                List<AuditProject> auditProjects = auditProjectService.getAuditProjectListByCondition(sql.getMap())
                        .getResult();
                if (auditProjects.size() > 0) {
                    for (AuditProject auditproject : auditProjects) {
                        List<AuditProjectMaterial> projectMaterials = projectMaterialService
                                .selectProjectMaterial(auditproject.getRowguid()).getResult();
                        for (AuditProjectMaterial auditProjectMaterial : projectMaterials) {
                            if (StringUtil.isNotBlank(auditProjectMaterial.getSharematerialiguid())
                                    && auditProjectMaterial.getSharematerialiguid()
                                    .equals(auditResult.getSharematerialguid())) {
                                List<AuditOrgaWindow> windowList = auditOrgaWindowService
                                        .getWindowListByTaskId(auditproject.getTask_id()).getResult();
                                for (AuditOrgaWindow window : windowList) {
                                    List<AuditOrgaWindowUser> users = auditOrgaWindowService
                                            .getUserByWindow(window.getRowguid()).getResult();
                                    for (AuditOrgaWindowUser user : users) {
                                        iOnlineMessageInfoService.insertMessage(UUID.randomUUID().toString(),
                                                UserSession.getInstance().getUserGuid(),
                                                UserSession.getInstance().getDisplayName(), user.getUserguid(), "",
                                                user.getUserguid(),
                                                "办件 " + auditproject.getProjectname() + "的前置材料已发放完成！", new Date());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 生成基本信息字段map(渲染证照基本信息，根据对象名称)
     *
     * @param recordName 实体名称
     * @return
     */
    public Map<String, Object> generateBasicDefaultMap(List<CertMetadata> certMetadataList, boolean isdetail,
                                                       String recordName) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> recordList = new ArrayList<Map<String, Object>>();
        List<CodeItems> codeItems = codeItemsService.listCodeItemsByCodeName("证照基本信息字段");
        if (CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsmultiowners())) {
            // 多持有人去除持有人信息
            codeItems.removeIf(x -> "certownercerttype".equals(x.getItemValue())
                    || "certownerno".equals(x.getItemValue()) || "certownername".equals(x.getItemValue()));
        }
        HashMap<String, String> repeatFieldMap = new HashMap<String, String>();

        for (CertMetadata certMetadata : certMetadataList) {
            if (StringUtil.isNotBlank(certMetadata.getRelatedfield())) {
                repeatFieldMap.put(certMetadata.getRelatedfield(), certMetadata.getFieldname());
            }
        }
        for (CodeItems codeItem : codeItems) {
            Map<String, Object> recordMap = new HashMap<String, Object>();
            if (!isdetail && repeatFieldMap.containsKey(codeItem.getItemValue())) {
                // 关联了元数据，填写的值动态刷新上去
                recordMap.put("onvaluechanged", "valuechanged('" + codeItem.getItemValue() + "','"
                        + repeatFieldMap.get(codeItem.getItemValue()) + "')");
            }
            // 起始和截止日期
            if ("expiredatefrom".equals(codeItem.getItemValue()) || "expiredateto".equals(codeItem.getItemValue())
                    || "awarddate".equals(codeItem.getItemValue())) {
                recordMap.put("type", "datepicker");
                if ("awarddate".equals(codeItem.getItemValue())) {
                    // 设置必填
                    recordMap.put("required", true);
                    recordMap.put("ondrawdate", "todateDraw");
                    recordMap.put("requiredErrorText", codeItem.getItemText() + "不能为空!");
                }
                // 有效期（起始）添加校验及隐藏今天按钮
                if ("expiredateto".equals(codeItem.getItemValue())) {
                    recordMap.put("ondrawdate", "todateDraw");
                    recordMap.put("showTodayButton", false);
                } else if ("expiredatefrom".equals(codeItem.getItemValue())) { // 有效期（截止）添加校验及隐藏今天按钮
                    recordMap.put("onvalidation", "fromdateValidate");
                    recordMap.put("showTodayButton", false);
                }
                // 详细页面时间去除时分秒
                if (isdetail) {
                    recordMap.put("dataOptions", "{'format' : 'yyyy-MM-dd'}");
                }
            } else {
                // 文本框类型
                recordMap.put("type", "textbox");
                // 设置必填
                recordMap.put("required", true);
                // 设置提示信息
                recordMap.put("requiredErrorText", codeItem.getItemText() + "不能为空!");
            }

            if ("certawarddept".equals(codeItem.getItemValue())) {
                recordMap.put("type", "buttonedit");
                recordMap.put("allowInput", "false");
                recordMap.put("onbuttonclick", "selectOu");
                recordMap.put("selectOnFocus", "true");
            }
            if ("certownertype".equals(codeItem.getItemValue())) {
                // 下拉框类型
                recordMap.put("type", "combobox");
                JSONArray jsonArray = JSONArray.parseArray(getCodeData("证照持有人类型"));
                jsonArray.removeIf(a -> (CertConstant.CERTOWNERTYPE_HH.equals(((JSONObject) a).getString("id"))
                        || CertConstant.CERTOWNERTYPE_QT.equals(((JSONObject) a).getString("id"))));
                recordMap.put("dataData", jsonArray.toJSONString());
                recordMap.put("dataOptions", "{'code' : '证照持有人类型'}");
                recordMap.put("onvaluechanged", "certownertypechanged");
            }
            if ("certownercerttype".equals(codeItem.getItemValue())) {
                // 下拉框类型
                recordMap.put("type", "combobox");
                recordMap.put("dataData", getCodeData("申请人用来唯一标识的证照类型"));
                recordMap.put("dataOptions", "{'code' : '申请人用来唯一标识的证照类型'}");
                recordMap.put("action", "getCertOwnerCertTypeModel");
            }

            // 证照编号、持有者姓名、持有者证件号码 长度限制（最大50）
            String itemValue = codeItem.getItemValue();
            if ("certno".equals(itemValue) || "certownername".equals(itemValue) || "certownerno".equals(itemValue)) {
                recordMap.put("vType", "maxLength:50");
            }

            if ("certownerno".equals(codeItem.getItemValue())) {
                recordMap.put("onvalidation", "onCertnumValidation");
            }

            recordMap.put("width", ZwfwConstant.CONSTANT_INT_ONE);
            recordMap.put("bind", recordName + "." + codeItem.getItemValue().toLowerCase());

            // 设置字段名称
            recordMap.put("fieldName", codeItem.getItemValue());
            recordMap.put("label", codeItem.getItemText());
            // 详情页面
            if (isdetail) {
                recordMap.put("type", "outputtext");
                recordMap.put("required", false);
            }
            recordList.add(recordMap);
        }

        // 查看页面添加下次年检时间
        if (isdetail) {
            Map<String, Object> recordMap = new HashMap<String, Object>();
            // 文本框类型
            recordMap.put("type", "outputtext");
            recordMap.put("width", ZwfwConstant.CONSTANT_INT_ONE);
            recordMap.put("bind", recordName + ".inspectiondate");
            // 设置字段名称
            recordMap.put("fieldName", "inspectiondate");
            recordMap.put("id", "inspectiondate");
            recordMap.put("dataOptions", "{'format':'yyyy-MM-dd'}");
            recordMap.put("label", "下次年检时间");
            recordList.add(recordMap);
        }

        map.put("data", recordList);
        return map;
    }

    /**
     * 实体日期格式转换、错误代码项标注
     */
    public void convertDate(List<CertMetadata> metadataList, Record record) {
        if (ValidateUtil.isBlankCollection(metadataList) || record == null) {
            return;
        }

        for (CertMetadata metadata : metadataList) {
            // 展示显示的字段 时间格式YYYY-MM-DD
            if (ZwfwConstant.INPUT_TYPE_DATE_TIME.equals(metadata.getFieldtype())) {
                Date fieldDate = record.getDate(metadata.getFieldname());
                if (fieldDate != null) {
                    record.set(metadata.getFieldname(), EpointDateUtil.convertDate2String(fieldDate));
                }
            }
            // 显示错误代码项
            if (StringUtil.isNotBlank(metadata.getDatasource_codename())) {
                String value = record.getStr(metadata.getFieldname());
                if (StringUtil.isNotBlank(value)) {
                    String itemName = codeItemsService.getItemTextByCodeName(metadata.getDatasource_codename(), value);
                    if (StringUtil.isBlank(itemName)) {
                        // bug10823，bug10825
                        /*
                         * record.set(metadata.getFieldname(), value + "(" +
                         * metadata .getDatasource_codename() + "列表选择错误)");
                         */
                        record.set(metadata.getFieldname(), value);
                        metadata.setDatasource_codename(null);
                    }
                }
            }
        }
    }

    /**
     * 生成控件map数据(使用拓展子信息表)
     *
     * @param actionName
     * @param certMetadataList
     * @param parentMode       父表模式
     * @param subMode          子表模式
     * @return
     */
    public Map<String, Object> generateMapUseSubExtension(String actionName, List<CertMetadata> certMetadataList,
                                                          String parentMode, String subMode) {
        if (ValidateUtil.isBlankCollection(certMetadataList)) {
            return new HashMap<String, Object>();
        }

        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> recordList = new ArrayList<Map<String, Object>>();
        for (CertMetadata metadata : certMetadataList) {
            Map<String, Object> metaDataMap = generateMetaDataMap(actionName, metadata, parentMode, subMode);
            recordList.add(metaDataMap);
        }
        map.put("data", recordList);
        return map;
    }

    /**
     * 生成元数据map
     *
     * @param actionName
     * @param metadata
     * @param parentMode 父表模式
     * @param subMode    子表模式
     * @return
     */
    public Map<String, Object> generateMetaDataMap(String actionName, CertMetadata metadata, String parentMode,
                                                   String subMode) {
        Map<String, Object> recordMap = new HashMap<String, Object>();
        // 展示子节点 属性(label type, fieldName, parentguid, onclick, iconCls)
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(metadata.getIsrelatesubtable())) {
            // 设置类型
            recordMap.put("type", "templWithChlid");
            // 设置parentguid
            String parentguid = "";
            // 子表模式
            if (ZwfwConstant.SUB_MODE_ADD.equals(subMode)) { // 新增模式
                parentguid = UUID.randomUUID().toString();
                recordMap.put("onclick",
                        "openAddSublist('" + metadata.getRowguid() + "', '" + metadata.getFieldchinesename() + "', '"
                                + parentguid + "','" + getRequestParameter("projectguid") + "')");
                recordMap.put("iconCls", "icon-edit");
            } else if (ZwfwConstant.SUB_MODE_EDIT.equals(subMode)) { // 修改模式
                recordMap.put("onclick",
                        "openEditSublist('" + metadata.getRowguid() + "', '" + metadata.getFieldchinesename() + "', '"
                                + metadata.getFieldname() + "','" + getRequestParameter("projectguid") + "')");
                recordMap.put("iconCls", "icon-edit");
            } else if (ZwfwConstant.SUB_MODE_DETAIL.equals(subMode)) { // 查看模式
                recordMap.put("onclick", "openDetailSublist('" + metadata.getRowguid() + "', '"
                        + metadata.getFieldchinesename() + "', '" + metadata.getFieldname() + "')");
                recordMap.put("iconCls", "icon-search");
            }

            recordMap.put("parentguid", parentguid);
        } else { // 不展示子节点
            // 详情页面处理
            if (ZwfwConstant.MODE_DETAIL.equals(parentMode)) {
                // 类型
                if ("webuploader".equals(metadata.getFielddisplaytype().toLowerCase())) {
                    // 使用uc-oauploader控件
                    recordMap.put("type", "oauploader");
                } else if ("webeditor".equals(metadata.getFielddisplaytype().toLowerCase())) {
                    recordMap.put("type", metadata.getFielddisplaytype());
                } else if ("checkbox".equals(metadata.getFielddisplaytype().toLowerCase())) {
                    recordMap.put("type", metadata.getFielddisplaytype());
                } else {
                    recordMap.put("type", "outputtext");
                }

                // 数据代码
                if (StringUtil.isNotBlank(metadata.getDatasource_codename())) {
                    recordMap.put("dataOptions", "{'code' : '" + metadata.getDatasource_codename() + "'}");
                }
                // 详细页面时间去除时分秒
                if ("DateTime".equals(metadata.getFieldtype())) {
                    recordMap.put("dataOptions", "{'format' : 'yyyy-MM-dd'}");
                }
                // 展示子节点
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(metadata.getIsrelatesubtable())) {
                    recordMap.put("type", recordMap.get("type") + "WithChlid");
                    recordMap.put("onclick",
                            "ondetail('" + metadata.getRowguid() + "', '" + metadata.getFieldchinesename() + "')");
                }
            } else { // 新增、修改
                if ("webuploader".equals(metadata.getFielddisplaytype().toLowerCase())) {
                    // 使用uc-oauploader控件
                    recordMap.put("type", "oauploader");
                } else {
                    recordMap.put("type", metadata.getFielddisplaytype());
                }
                // 是否必填
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(metadata.getNotnull())) {
                    recordMap.put("required", true);
                    // 设置提示信息
                    recordMap.put("requiredErrorText", metadata.getFieldchinesename() + "不能为空!");
                } else {
                    recordMap.put("required", false);
                }
                // 代码项值渲染
                if (StringUtil.isNotBlank(metadata.getDatasource_codename())) {
                    recordMap.put("dataData", getCodeData(metadata.getDatasource_codename()));
                }
            }

            String vType = "";

            // 字符串类型进行长度校验
            if ("Nvarchar".equals(metadata.getFieldtype())) {
                // 字段长度校验
                if (StringUtil.isBlank(metadata.getMaxlength())
                        || ZwfwConstant.CONSTANT_STR_ZERO.equals(metadata.getMaxlength().toString())) {
                    // 默认长度50
                    vType = "maxLength:50";
                } else {
                    vType = "maxLength:" + metadata.getMaxlength();
                }
            }

            // int类型在表单中的校验
            if ("Integer".equals(metadata.getFieldtype())) {
                vType = "int";
            } else if ("Numeric".equals(metadata.getFieldtype())) { // 数字类型在表单中校验
                vType = "float;" + vType;
            }

            // 自定义校验规则
            String onvalidation = metadata.getOnvalidation();
            if (StringUtil.isNotBlank(onvalidation)) {
                if (onvalidation.startsWith("mini:")) { // miniui自带规则
                    onvalidation = onvalidation.replace("mini:", "");
                    vType += ";" + onvalidation;
                } else if (onvalidation.startsWith("custom:")) { // 自定义校验规则
                    onvalidation = onvalidation.replace("custom:", "");
                    recordMap.put("onvalidation", onvalidation);
                }
            }
            recordMap.put("vType", vType);

            // 文件上传
            if ("webuploader".equals(metadata.getFielddisplaytype().toLowerCase())) {
                // 根据父表模式，渲染附件控件
                if (ZwfwConstant.MODE_ADD.equals(parentMode)) {
                    // 生成cliengguid
                    recordMap.put("action", String.format("%s.getFileUploadModel(%s,%s)", actionName,
                            metadata.getFieldname(), UUID.randomUUID().toString()));
                } else {
                    recordMap.put("action",
                            String.format("%s.getEditFileUploadModel(%s)", actionName, metadata.getFieldname()));
                }
                // 附件只支持图片类型
                recordMap.put("limitType", "bmp,gif,jpeg,tiff,psd,png,jpg,jfif,raw");
                recordMap.put("previewExt", "bmp,gif,jpeg,tiff,psd,png,jpg,jfif,raw");
                recordMap.put("editext", "bmp,gif,jpeg,tiff,psd,png,jpg,jfif,raw");
                recordMap.put("fileNumLimit", "1");
                // recordMap.put("fileSingleSizeLimit", "5120");
                // recordMap.put("mimeTypes", "image/*");
            }

            if (metadata.getControlwidth() == ZwfwConstant.CONSTANT_INT_ONE) {
                recordMap.put("width", ZwfwConstant.CONSTANT_INT_ONE);
            }

            if (metadata.getControlwidth() == ZwfwConstant.CONSTANT_INT_TWO) {
                recordMap.put("width", ZwfwConstant.CONSTANT_INT_TWO);
            }

            // 如果是webeditor控件，设置双倍宽。
            if ("webeditor".equals(metadata.getFielddisplaytype().toLowerCase())) {
                recordMap.put("width", ZwfwConstant.CONSTANT_INT_TWO);
            }
            recordMap.put("bind", "dataBean." + metadata.getFieldname().toLowerCase());
        }

        // 设置字段名称
        recordMap.put("fieldName", metadata.getFieldname());
        recordMap.put("label", metadata.getFieldchinesename());
        return recordMap;
    }

    /**
     * 获取office365服务器地址
     */
    public String getOfficeConfig() {

        String officeweb365url = handleConfigService.getFrameConfig("AS_OfficeWeb365_Server", "").getResult();
        if (!(officeweb365url.startsWith("http://") || officeweb365url.startsWith("https://"))) {
            officeweb365url = "http://" + officeweb365url;
        }
        return officeweb365url;
    }

    public FileUploadModel9 getFileUploadModel(String fieldName, String cliengguid) {
        FileUploadModel9 uploadModel9 = null;
        if (certCatalog != null && StringUtil.isNotBlank(fieldName) && StringUtil.isNotBlank(cliengguid)) {
            if (modelMap.containsKey(cliengguid)) {
                uploadModel9 = modelMap.get(cliengguid);
            } else {
                uploadModel9 = this.miniOaUploadAttach(cliengguid, null);
                modelMap.put(cliengguid, uploadModel9);
            }
        }
        return uploadModel9;
    }

    public FileUploadModel9 getEditFileUploadModel(String fieldName) {
        FileUploadModel9 uploadModel9 = null;
        if (dataBean != null && StringUtil.isNotBlank(fieldName)) {
            String cliengguid = dataBean.getStr(fieldName);
            if (StringUtil.isNotBlank(cliengguid)) {
                if (modelMap.containsKey(cliengguid)) {
                    uploadModel9 = modelMap.get(cliengguid);
                } else {
                    uploadModel9 = this.miniOaUploadAttach(cliengguid, null);
                    modelMap.put(cliengguid, uploadModel9);
                }
            }
        }
        return uploadModel9;
    }

    /**
     * 获取代码项内容
     *
     * @param codeName
     * @return
     */
    public String getCodeData(String codeName) {
        StringBuffer rtnString = new StringBuffer("[");
        List<CodeItems> codeItemList = codeItemsService.listCodeItemsByCodeName(codeName);
        if (ValidateUtil.isBlankCollection(codeItemList)) {
            return "";
        }

        for (CodeItems codeItems : codeItemList) {
            rtnString.append("{id:'" + codeItems.getItemValue() + "',text:'" + codeItems.getItemText() + "'},");
        }
        rtnString = new StringBuffer(rtnString.substring(0, rtnString.length() - 1));
        rtnString.append("]");
        return rtnString.toString();
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getCertLevelModel() {
        if (certLevelModel == null) {
            certLevelModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "可信等级", null, false));
        }
        return this.certLevelModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getCertOwnerCertTypeModel() {
        if (certOwnerCertTypeModel == null) {
            certOwnerCertTypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "申请人用来唯一标识的证照类型", null, false));
            // 自然人去除组织机构代码证、统一社会信用代码
            if ("001".equals(certinfo.getCertownertype())) {
                certOwnerCertTypeModel.removeIf(selectItem -> {
                    if ("1".equals(selectItem.getValue().toString().substring(0, 1))) {
                        return true;
                    } else {
                        return false;
                    }
                });
                // 法人保留组织机构代码证、统一社会信用代码
            } else if ("002".equals(certinfo.getCertownertype())) {
                certOwnerCertTypeModel.removeIf(selectItem -> {
                    if ("2".equals(selectItem.getValue().toString().substring(0, 1))) {
                        return true;
                    } else {
                        return false;
                    }
                });
            }
        }
        return this.certOwnerCertTypeModel;
    }

    /**
     * 基本信息附件上传
     *
     * @return
     */
    public FileUploadModel9 getCertFileUploadModel() {
        if (certinfo != null && certFileUploadModel == null) {
            certFileUploadModel = this.miniOaUploadAttach(certinfo.getCertcliengguid(), null);
        }
        return certFileUploadModel;
    }

    /**
     * 基本信息附件上传
     *
     * @return
     */
    public FileUploadModel9 getCopyCertFileUploadModel() {
        if (certinfo != null && certCopyFileUploadModel == null) {
            certCopyFileUploadModel = this.miniOaUploadAttach(certinfo.getCopycertcliengguid(), null);
        }
        return certCopyFileUploadModel;
    }

    // 附图附件上传
    public FileUploadModel9 getFtfjUploadModel() {
        if (certinfo != null && ftfjUploadModel == null) {
            ftfjUploadModel = this.miniOaUploadAttach(certinfo.get("ftfjcliengguid"), null);
        }
        return ftfjUploadModel;
    }

    /**
     * 通过mini-oauploader控件上传附件
     *
     * @param clientGuid
     * @param clientInfo
     * @return fileUploadModel
     */
    public FileUploadModel9 miniOaUploadAttach(String clientGuid, String clientInfo) {
        return miniOaUploadAttach(null, clientGuid, ZwfwConstant.CERT_SYS_FLAG, clientInfo, null,
                UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName());
    }

    /**
     * 通过oauploader控件上传附件
     *
     * @param fileUploadModel
     * @param clientGuid
     * @param clientTag
     * @param clientInfo
     * @param attachHandler
     * @param userGuid
     * @param userName
     */
    public FileUploadModel9 miniOaUploadAttach(FileUploadModel9 fileUploadModel, String clientGuid, String clientTag,
                                               String clientInfo, AttachHandler9 attachHandler, String userGuid, String userName) {
        if (attachHandler == null) {
            attachHandler = new AttachHandler9() {
                private static final long serialVersionUID = -6274679633876738096L;

                @Override
                public void afterSaveAttachToDB(Object arg0) {
                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
                    return true;
                }
            };
        }
        String customerFilePath = handleConfigService.getFrameConfig("AS_CUSTOMER_FILE_PATH", "").getResult();
        if (fileUploadModel == null) {
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(clientGuid, clientTag, clientInfo,
                    attachHandler, userGuid, userName));
            fileUploadModel.setCustomerFilePath(customerFilePath);
        } else {
            if (StringUtil.isBlank(fileUploadModel.getCustomerFilePath())) {
                fileUploadModel.setCustomerFilePath(customerFilePath);
            }
        }
        fileUploadModel.setUploadType("file");
        return fileUploadModel;
    }

    public CertInfo getCertinfo() {
        return certinfo;
    }

    public void setCertinfo(CertInfo certinfo) {
        this.certinfo = certinfo;
    }

    public CertInfoExtension getDataBean() {
        return dataBean;
    }

    public void setDataBean(CertInfoExtension dataBean) {
        this.dataBean = dataBean;
    }

    public String getExtendguid() {
        return extendguid;
    }

    public void setExtendguid(String extendguid) {
        this.extendguid = extendguid;
    }

    public String getCertownerinfo() {
        return certownerinfo;
    }

    public void setCertownerinfo(String certownerinfo) {
        this.certownerinfo = certownerinfo;
    }

    public String getUpperDate(String date) {
        // 支持yyyy-MM-dd、yyyy/MM/dd、yyyyMMdd等格式
        if (date == null)
            return null;
        // 非数字的都去掉
        date = date.replaceAll("\\D", "");
        if (date.length() != 8)
            return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {// 年
            sb.append(upper[Integer.parseInt(date.substring(i, i + 1))]);
        }
        sb.append("年");// 拼接年
        int month = Integer.parseInt(date.substring(4, 6));
        if (month <= 10) {
            sb.append(upper[month]);
        } else {
            sb.append("十").append(upper[month % 10]);
        }
        sb.append("月");// 拼接月

        int day = Integer.parseInt(date.substring(6));
        if (day <= 10) {
            sb.append(upper[day]);
        } else if (day < 20) {
            sb.append("十").append(upper[day % 10]);
        } else {
            sb.append(upper[day / 10]).append("十");
            int tmp = day % 10;
            if (tmp != 0)
                sb.append(upper[tmp]);
        }
        sb.append("日");// 拼接日
        return sb.toString();
    }

    /**
     * 生成办件编号（好差评用）
     *
     * @return
     */
    public String getProjectno() {
        String numberName = "自动生成证照编码";
        Calendar calendar = Calendar.getInstance();
        String numberFlag = "" + calendar.get(Calendar.YEAR) + String.format("%02d", calendar.get(Calendar.MONTH) + 1)
                + String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
        int theYearLength = 0;
        int snLength = 4;
        String certno = new DBServcie().getFlowSn(numberName, numberFlag, theYearLength, false, snLength);
        EpointFrameDsManager.commit();
        return certno;
    }

    public void createZsbh() {
        String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");
        Integer maxnum = 0;
        if ("370890".equals(auditProject.getAreacode())) {
            maxnum = Integer.valueOf(GenerateNumberUtil.getCertNumNotYear("gxjzsg"));
        } else if ("370892".equals(auditProject.getAreacode())) {
            maxnum = Integer.valueOf(GenerateNumberUtil.getCertNumNotYear("jksg"));
            if (maxnum - 80223399 == 0) {
                maxnum = 80225301;
            }
            iJNAuditProject.UpdateMaxZjNumNew(String.valueOf(maxnum), "jksg");
        } else {
            maxnum = Integer.valueOf(GenerateNumberUtil.getCertNumNotYear("jzsg"));
            if (maxnum - 80220099 == 0) {
                maxnum = 80222001;
            }
            if (maxnum - 80222099 == 0) {
                maxnum = 80224001;
            }
            if (maxnum - 80224099 == 0) {
                maxnum = 80226001;
            }
            if (maxnum - 80226099 == 0) {
                maxnum = 80228001;
            }
            iJNAuditProject.UpdateMaxZjNumNew(String.valueOf(maxnum), "jzsg");
        }
        addCallbackParam("maxnum", "0" + maxnum);
    }

    public String createGgZsbh(String itemguid, String jyxm) {
        String result = "";
        String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");
        String nian = year.substring(year.length() - 2, year.length());
        // 泗水
        if ("11370831MB285634767370123021001".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("ssggws");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }
            result = "鲁泗卫公字[" + year + "]第H" + num + "号";
        }
        // 曲阜
        else if ("11370881MB2796110U7370123021001".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("qfggws");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }
            result = "鲁曲卫公字[" + year + "]第" + num + "号";
        }
        // 金乡
        else if ("11370828MB286029024370123021001".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("jxggws");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            result = "鲁济金卫公字[" + year + "]第" + num + "号";
        }
        // 兖州
        else if ("11370812MB2868524U7370123021001".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("yzggws");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }
            result = "鲁兖卫公字[" + year + "]第370882-" + num + "号";
        }
        // 高新
        else if ("1838000000000000004370123021001".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("gxggws");
            if (num.length() == 1) {
                num = "0000" + num;
            } else if (num.length() == 2) {
                num = "000" + num;
            } else if (num.length() == 3) {
                num = "00" + num;
            } else if (num.length() == 4) {
                num = "0" + num;
            }

            result = "鲁济高新卫公证字[" + year + "]第G" + num + "号";
        }
        // 经开
        else if ("TE37087320203090114370123021001".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("jkggws");
            if (num.length() == 1) {
                num = "00" + num;
            } else if (num.length() == 2) {
                num = "0" + num;
            }

            result = "济经卫公证字[" + year + "]第370873" + num + "号";
        }
        // 梁山
        else if ("11370832MB2855272B7370123021001".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("lsggws");
            if (num.length() == 3) {
                num = "00" + num;
            } else if (num.length() == 4) {
                num = "0" + num;
            }

            result = "(鲁)卫(公证)字[" + year + "]第370832————" + num + "号";
        }
        // 任城
        else if ("11370811MB279691104370123021001".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("rcggws");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            String newjyxm = "";
            if ("理发店".equals(jyxm) || "美容店".equals(jyxm)) {
                newjyxm = "A";
            } else if ("宾馆".equals(jyxm) || "旅店".equals(jyxm) || "招待所".equals(jyxm)) {
                newjyxm = "B";
            } else if ("公共浴室".equals(jyxm) || "游泳馆".equals(jyxm)) {
                newjyxm = "C";
            } else if ("影剧院".equals(jyxm) || "录像厅".equals(jyxm) || "游艺厅".equals(jyxm) || "舞厅".equals(jyxm)
                    || "音乐厅".equals(jyxm) || "展览馆".equals(jyxm) || "博物馆".equals(jyxm) || "美术馆".equals(jyxm)
                    || "图书馆".equals(jyxm) || "候诊室".equals(jyxm) || "候车室".equals(jyxm) || "商场".equals(jyxm)
                    || "书店".equals(jyxm)) {
                newjyxm = "D";
            }
            result = "鲁济任卫公证字[" + year + "]第GA22" + newjyxm + num + "号";
        }
        // 微山
        else if ("11370826MB2858051T4370123021001".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("wsggws");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }
            result = "鲁微卫公证字" + nian + "C" + num + "号";
        }
        // 鱼台
        else if ("11370827MB2857833K4370123021001".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("ytggws");
            if (num.length() == 1) {
                num = "00000" + num;
            } else if (num.length() == 2) {
                num = "0000" + num;
            } else if (num.length() == 3) {
                num = "000" + num;
            } else if (num.length() == 4) {
                num = "00" + num;
            } else if (num.length() == 5) {
                num = "0" + num;
            }
            result = "鲁济鱼卫公证字[" + year + "]第370827-" + num + "号";
        }
        // 邹城
        else if ("11370883MB2857374H7370123021001".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("zcggws");
            if (num.length() == 1) {
                num = "0000" + num;
            } else if (num.length() == 2) {
                num = "000" + num;
            } else if (num.length() == 3) {
                num = "00" + num;
            } else if (num.length() == 4) {
                num = "0" + num;
            }
            result = "邹公卫证字〔" + year + "〕第" + num + "号";
        }
        // 汶上
        else if ("11370830F5034613XX4370123021001".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("wensggws");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }
            result = "鲁文卫公证字(" + year + ")第370830" + num + "号";
        }
        // 北龙湖
        else if ("TE370800SPJ12345607370123021001".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("tbhggws");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            String newjyxm = "";
            if ("美容店".equals(jyxm) || "理发店".equals(jyxm)) {
                newjyxm = "TA";
            } else if ("招待所".equals(jyxm) || "旅店".equals(jyxm) || "宾馆".equals(jyxm)) {
                newjyxm = "TB";
            } else if ("公共浴室".equals(jyxm) || "游泳场（馆）".equals(jyxm)) {
                newjyxm = "TC";
            } else {
                newjyxm = "TD";
            }

            result = "鲁济太卫公证字[" + year + "]第" + newjyxm + num + "号";
        } else if ("1137080000431212413370717666897".equals(itemguid)) {
            String newjyxm = "";
            if ("01".equals(jyxm)) {
                String num = GenerateNumberUtil.getCertNumNotYear("jnjzjnrdzl");
                if (num.length() == 1) {
                    num = "000" + num;
                } else if (num.length() == 2) {
                    num = "00" + num;
                } else if (num.length() == 3) {
                    num = "0" + num;
                }
                newjyxm = "HA";
                result = newjyxm + num + "号";
            } else if ("02".equals(jyxm)) {
                String num = GenerateNumberUtil.getCertNumNotYear("jnjzjnrqkl");
                if (num.length() == 1) {
                    num = "000" + num;
                } else if (num.length() == 2) {
                    num = "00" + num;
                } else if (num.length() == 3) {
                    num = "0" + num;
                }
                newjyxm = "HB";
                result = newjyxm + num + "号";
            } else if ("03".equals(jyxm)) {
                String num = GenerateNumberUtil.getCertNumNotYear("jnjzjnrqbl");
                if (num.length() == 1) {
                    num = "000" + num;
                } else if (num.length() == 2) {
                    num = "00" + num;
                } else if (num.length() == 3) {
                    num = "0" + num;
                }
                newjyxm = "HC";
                result = newjyxm + num + "号";
            } else if ("04".equals(jyxm)) {
                String num = GenerateNumberUtil.getCertNumNotYear("jnjzjnrqtl");
                if (num.length() == 1) {
                    num = "000" + num;
                } else if (num.length() == 2) {
                    num = "00" + num;
                } else if (num.length() == 3) {
                    num = "0" + num;
                }
                newjyxm = "HD";
                result = newjyxm + num + "号";
            } else if ("05".equals(jyxm)) {
                String num = GenerateNumberUtil.getCertNumNotYear("jnjzjnrmcl");
                if (num.length() == 1) {
                    num = "000" + num;
                } else if (num.length() == 2) {
                    num = "00" + num;
                } else if (num.length() == 3) {
                    num = "0" + num;
                }
                newjyxm = "HE";
                result = newjyxm + num + "号";
            }
        }
        return result;
    }

    public String createJsGcZsbh(String itemguid) {
        String result = "";
        String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");

        if ("11370800MB285591847371004002001".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("jnkxxyjbgpf");
            if (num.length() == 1) {
                num = "00" + num;
            } else if (num.length() == 2) {
                num = "0" + num;
            }

            result = "济审政投〔" + year + "〕" + num + "号";
        } else if ("11370800MB28559184337011800900410".equals(itemguid)
                || "11370800MB28559184337011800900409".equals(itemguid)
                || "11370800MB28559184337011800900408".equals(itemguid)
                || "11370800MB28559184337011800900407".equals(itemguid)
                || "11370800MB28559184337011800900411".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("jncbyyys");
            if (num.length() == 1) {
                num = "0000" + num;
            } else if (num.length() == 2) {
                num = "000" + num;
            } else if (num.length() == 3) {
                num = "00" + num;
            } else if (num.length() == 4) {
                num = "0" + num;
            }
            result = "鲁SJ(" + year + ")10" + num + "号";
        } else if ("11370800MB285591843370123018002".equals(itemguid)
                || "11370800MB285591843370123018001".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("jnsjyyswsaq");
            if (num.length() == 1) {
                num = "00" + num;
            } else if (num.length() == 2) {
                num = "0" + num;
            }

            result = "鲁卫水字【" + year + "】第H" + num + "号";
        } else if ("11370812MB2868524U4370120006006".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNumNotYear("yznyjyxk");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            result = num;
        }
        else if ("11370800MB285591843370118037000".equals(itemguid)
                || "11370800MB28559184300011823200201".equals(itemguid)
                || "11370800MB28559184300011823200202".equals(itemguid)
                || "11370800MB28559184300011823200203".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNumNotYear("jngkjyxk");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            result = "(鲁宁)港经证(" + num + ")号";
        }
        else if ("11370800MB28559184300011712300201".equals(itemguid)
                || "11370800MB28559184300011712300203".equals(itemguid)
                || "11370800MB28559184737011702000001".equals(itemguid)
                || "11370800MB28559184737011702000005".equals(itemguid)
                || "11370800MB28559184737011702000004".equals(itemguid)
                || "11370800MB28559184300011712300201".equals(itemguid)
                || "11370800MB28559184300011712300203".equals(itemguid)
                || "11370800MB28559184300011712300202".equals(itemguid)
        ) {
            String num = GenerateNumberUtil.getCertNum("jnpshczws");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            result = "JNSP" + year + "（YB排）-" + num;
        } else if ("11370800MB285591843370118009001".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNumNotYear("jnghslysbh");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            result = "济审航XK" + num;
        } else if ("1137083000433521104371017014000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("wsjsgcjgdays");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            result = year + num;
        } else if ("1137082600452807284371017014000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("weisjsgcjgdays");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            result = year + num;
        } else if ("1137083000433521104370117044000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("wstsjsgcys");
            if (num.length() == 1) {
                num = "00" + num;
            } else if (num.length() == 2) {
                num = "0" + num;
            }

            result = "汶建消验字" + year + "第" + num + "号";
        } else if ("1137082600452807284370117044000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("weistsjsgcys");
            if (num.length() == 1) {
                num = "00" + num;
            } else if (num.length() == 2) {
                num = "0" + num;
            }

            result = "微建消验字" + year + "第" + num + "号";
        } else if ("11370826MB286081384370715007000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("weisjsgcjg");
            if (num.length() == 1) {
                num = "0000" + num;
            } else if (num.length() == 2) {
                num = "000" + num;
            } else if (num.length() == 3) {
                num = "00" + num;
            } else if (num.length() == 4) {
                num = "0" + num;
            }

            result = "核字第370826" + year + num + "号";
        } else if ("11370830F5034613XX4370715007000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("wsjsgcjg");
            if (num.length() == 1) {
                num = "0000" + num;
            } else if (num.length() == 2) {
                num = "000" + num;
            } else if (num.length() == 3) {
                num = "00" + num;
            } else if (num.length() == 4) {
                num = "0" + num;
            }

            result = "核字第370830" + year + num + "号";
        } else if ("11370800MB285591847370104001042".equals(itemguid)
                || "11370800MB285591847370104001044".equals(itemguid)
                || "11370800MB285591843370104001034".equals(itemguid)
                || "11370800MB285591843370104001032".equals(itemguid)
                || "11370800MB285591847370104001043".equals(itemguid)
                || "11370800MB285591843370104001033".equals(itemguid)
                || "11370800MB285591847371004005003".equals(itemguid)
                || "11370800MB285591843370104001038".equals(itemguid)
                || "11370800MB285591847370104001040".equals(itemguid)
                || "11370800MB285591843370104001037".equals(itemguid)
                || "11370800MB285591847370104001041".equals(itemguid)
                || "11370800MB285591847370104001046".equals(itemguid)
                || "11370800MB285591843370104001035".equals(itemguid)
                || "11370800MB285591843370104001039".equals(itemguid)
                || "11370800MB285591843370104001045".equals(itemguid)
                || "11370800MB285591843370104001031".equals(itemguid)
                || "11370800MB285591846370104001036".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("jnxmhzpf");
            if (num.length() == 1) {
                num = "00" + num;
            } else if (num.length() == 2) {
                num = "0" + num;
            }

            result = "济审服企投〔" + year + "〕" + num + "号";
        } else if ("11370827MB2857833K3370117044000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNumNotYear("yttsjsgcys");
            if (num.length() == 1) {
                num = "00" + num;
            } else if (num.length() == 2) {
                num = "0" + num;
            }

            result = num;
        } else if ("1137080000431212413370117043000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("jnjsgcxfsj");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            result = "〔" + year + "〕第" + num + "号";
        } else if ("11370800MB285591847370115001000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("jnjsxmydysyd");
            if (num.length() == 1) {
                num = "0000" + num;
            } else if (num.length() == 2) {
                num = "000" + num;
            } else if (num.length() == 3) {
                num = "00" + num;
            } else if (num.length() == 4) {
                num = "0" + num;
            }

            result = "370800" + year + num;
        } else if ("11370827MB2857833K4370115055000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("ytjsydghxk");
            if (num.length() == 1) {
                num = "0000" + num;
            } else if (num.length() == 2) {
                num = "000" + num;
            } else if (num.length() == 3) {
                num = "00" + num;
            } else if (num.length() == 4) {
                num = "0" + num;
            }

            result = "370827" + year + num;
        } else if ("11370811MB279691104370117038000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("rccsjzlj");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            result = "RCSP" + year + "（ZT运输）-" + num;
        } else if ("11370828MB286029024370117038000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("jxcsjzlj");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }
            result = "金SP" + year + "（ZT运输）-" + num;
        } else if ("11370812MB2868524U4370117038000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("yzcsjzlj");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            result = "YZSP" + year + "（ZT运输）-" + num + "号";
        } else if ("TE3708732020309011437011703800001".equals(itemguid)
                || "TE3708732020309011437011703800002".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("jkcsjzlj");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            result = "JKSP" + year + "（ZT运输）-" + num + "号";
        } else if ("11370827MB2857833K4370115056000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("ytjsgcgh");
            if (num.length() == 1) {
                num = "00" + num;
            } else if (num.length() == 2) {
                num = "0" + num;
            }

            result = "370827" + year + num;
        } else if ("11370827MB2857833K4370715007000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("ytjsgcjggh");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            result = "370827" + year + num;
        } else if ("11370827MB2857833K4370117046000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("ytfwysxk");
            if (num.length() == 1) {
                num = "00" + num;
            } else if (num.length() == 2) {
                num = "0" + num;
            }

            result = num;
        } else if ("11370827MB2857833K4370117045000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("ytfwdxsjs");
            if (num.length() == 1) {
                num = "00" + num;
            } else if (num.length() == 2) {
                num = "0" + num;
            }

            result = "370827" + year + num;
        } else if ("11370826MB2858051T4370117038000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("wscsjzlj");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            result = "微SP" + year + "（ZT运输）-" + num + "号";
        } else if ("11370827MB2857833K4370117038000".equals(itemguid) || "11370827MB2857833K400011712200311".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("ytcsjzlj");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            result = "YTSP" + year + "（ZT运输）-" + num + "号";
        } else if ("11370829771010370B4370117038000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("jxcsjzlj");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            result = "嘉SP" + year + "（ZT运输）-" + num + "号";
        } else if ("11370830F5034613XX4370117038000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("wenscsjzlj");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            result = "汶SP" + year + "（ZT运输）-" + num + "号";
        } else if ("11370831MB285634767370117038000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("sscsjzlj");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            result = "SSSP" + year + "（ZT运输）-" + num + "号";
        } else if ("11370832MB2855272B2370117038000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("lscsjzlj");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            result = "LSSP" + year + "（ZT运输）-" + num + "号";
        } else if ("11370881MB2796110U4370117038000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("qfcsjzlj");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            result = "QFSP" + year + "（ZT运输）-" + num + "号";
        } else if ("11370883MB2857374H4370117038000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("zccsjzlj");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            result = "ZCSP" + year + "（ZT运输）-" + num + "号";
        } else if ("1237080079619134XQ4370117038000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("gxcsjzlj");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            result = "GXSP" + year + "（ZT运输）-" + num + "号";
        } else if ("12370800069995325T4370117038000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("tbhcsjzlj");
            if (num.length() == 1) {
                num = "000" + num;
            } else if (num.length() == 2) {
                num = "00" + num;
            } else if (num.length() == 3) {
                num = "0" + num;
            }

            result = "TBHSP" + year + "（ZT运输）-" + num + "号";
        }
        else if ("11370800MB28559184337012302000102".equals(itemguid)
                || "11370800MB28559184300012310800003".equals(itemguid)
                || "11370800MB28559184300012310800002".equals(itemguid)
                || "11370800MB28559184300012310800001".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("jnxdcpqyxsq");
            if (num.length() == 1) {
                num = "0" + num;
            }

            result = "鲁卫消证字（" + year + "）第07" + num + "号";
        }
        return result;

    }

    public String signPdf() throws Exception {
        //个性化  办结不允许电子签章
        log.info("进入签章方法：" + auditProject.getRowguid());
        if (auditProject != null) {
            AuditTask task = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();
            if (task != null && ("11370800MB28559184300011820600301".equals(task.getItem_id()) || "11370800MB28559184737011800700001".equals(task.getItem_id()))) {
                addCallbackParam("data", "无需电子签章");
                return "无需电子签章";
            } else {
                String msg = "";
                // 获得基本信息
                certinfo = iCertInfoExternal.getCertInfoByRowguid(certrowguid);
                if (certinfo != null) {
                    String ouguid = null;
                    FrameOu frameOu = iOuService.getOuByOuGuid(certinfo.getOuguid());
                    if (frameOu != null) {
                        ouguid = certinfo.getOuguid();
                    } else {
                        ////存在一些历史数据是存放oucode
                        List<FrameOuExtendInfo> frameOuExtendInfoList = iOuService.getAllFrameOuExtendInfo();
                        for (int i = 0; i < frameOuExtendInfoList.size(); i++) {
                            if (certinfo.getOuguid().equals(frameOuExtendInfoList.get(i).getStr("orgcode"))) {
                                frameOu = iOuService.getOuByOuGuid(frameOuExtendInfoList.get(i).getOuGuid());
                                if (frameOu != null) {
                                    ouguid = frameOu.getOuguid();
                                }
                            }
                        }
                    }
                    CertCatalogOu certCatalogOu = catalogOuService.getCertCataLogOuByCatalogid("*", false, ouguid,
                            certinfo.getCertcatalogid());
                    if (certCatalogOu == null) {
                        msg = "生成证照失败，部门没有证照类型的权限！";
                        return msg;
                    }

                    CertCatalog catalog = catalogService.getLatestCatalogDetailByCatalogid(
                            "useid,onsize,outsize,xsignrecord,ysignrecord,signcode,copysigncode", certinfo.getCertcatalogid());

                    String xrecord = catalog.getStr("xsignrecord");
                    String yrecord = catalog.getStr("ysignrecord");
                    String onsize = catalog.getStr("onsize");
                    String outsize = catalog.getStr("outsize");

                    if (StringUtil.isBlank(onsize)) {
                        onsize = "1";
                    }
                    if (StringUtil.isBlank(outsize)) {
                        outsize = "1";
                    }
                    if (StringUtil.isBlank(xrecord)) {
                        xrecord = "350";
                    }
                    if (StringUtil.isBlank(yrecord)) {
                        yrecord = "220";
                    }

                    log.info("省电子签章模板：" + catalog);
                    if (catalog != null) {
                        if (StringUtil.isNotBlank(catalog.getStr("signcode"))) {
                            Security.addProvider(new BouncyCastleProvider());
                            log.info("开始电子签章");
                            String msgs = HttpSignTest.signPdf(onsize, xrecord, yrecord, catalog.getStr("signcode"),
                                    catalog.getStr("useid"), certinfo.getCertcliengguid(), "position");
                            if (!"签章完成".equals(msgs)) {
                                msg = msgs;
                            } else {
                                msg = "签章成功！";
                            }
                        }

                        if (StringUtil.isNotBlank(catalog.getStr("copysigncode"))) {
                            Security.addProvider(new BouncyCastleProvider());
                            log.info("开始电子签章副本");
                            String msgs = HttpSignTest.signPdf(outsize, xrecord, yrecord, catalog.getStr("copysigncode"),
                                    catalog.getStr("useid"), certinfo.getCopycertcliengguid(), "position");
                            if (!"签章完成".equals(msgs)) {
                                msg = msgs;
                            } else {
                                msg = "签章成功！";
                            }
                        }

                        return msg;

                    } else {
                        msg = "生成证照失败，证照目录未配置签章！";
                        return msg;
                    }
                } else {
                    return "未查询到证照信息！";
                }
            }
        } else {
            return "办件为空！";
        }

    }



}
