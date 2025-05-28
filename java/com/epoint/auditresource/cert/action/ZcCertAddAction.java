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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
import com.epoint.auditresource.cert.util.GenerateNumberUtil;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowUser;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IJNAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditresource.auditcertcatalog.domain.AuditCertCatalog;
import com.epoint.basic.auditresource.auditcertcatalog.inter.IAuditCertCatalog;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditsp.auditspsharematerialrelation.domain.AuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditspsharematerialrelation.inter.IAuditSpShareMaterialRelation;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.basic.certcatalog.certcatalogou.domain.CertCatalogOu;
import com.epoint.cert.basic.certcatalog.certcatalogou.inter.ICertCatalogOu;
import com.epoint.cert.basic.certcatalog.certmetadata.domain.CertMetadata;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.certinfosubextension.domain.CertInfoSubExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.commonservice.DBServcie;
import com.epoint.cert.commonutils.CertConstant;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.cert.external.ICertConfigExternal;
import com.epoint.cert.external.ICertInfoExternal;
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
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
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
import com.epoint.xmz.cxbus.api.ICxBusService;
import com.epoint.xmz.cxbus.api.entity.CxBus;
import com.epoint.xmz.util.HttpSignTest;

/**
 * 证照基本信息表新增页面对应的后台
 *
 * @author dingwei
 * @version [版本号, 2017-11-01 16:01:53]
 */
@RestController("zccertaddaction")
@Scope("request")
public class ZcCertAddAction extends BaseController {

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

    @Autowired
    private ICertConfigExternal iCertConfigExternal;

    @Autowired
    private ICxBusService iCxBusService;

    @Autowired
    private IJNAuditProject iJNAuditProject;

    /**
     * 证照api
     */
    @Autowired
    private ICertInfoExternal iCertInfoExternal;

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
    private ICodeItemsService iCodeItemsService;

    private AuditProject auditProject;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditTaskResult auditTaskResultService;
    @Autowired
    private IAuditTaskExtension taskExtensionService;

    @Autowired
    private IHandleProject handleProjectService;

    @Autowired
    private IHandleSPIMaterial handleSpiMaterialService;

    @Autowired
    private IAuditSpShareMaterialRelation relationService;

    /**
     * 企业库接口
     */
    @Autowired
    private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;

    /**
     * 附件操作服务类
     */
    @Autowired
    private IAttachService attachService;

    @Autowired
    private IAuditProjectMaterial projectMaterialService;

    @Autowired
    private IOnlineMessageInfoService iOnlineMessageInfoService;

    @Autowired
    private IAuditOrgaWindowYjs auditOrgaWindowService;

    @Autowired
    private IOuService ouService;

    @Autowired
    private IOuServiceInternal iOuService;
    @Autowired
    private ICertCatalogOu catalogOuService;
    @Autowired
    private ICertCatalog catalogService;

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

    /**
     * 多持有人信息
     */
    private String certownerinfo;

    @Override
    public void pageLoad() {
        log.info("邹城个性化证照上传后台执行！！！");
        boolean warn = false;
        certinfo = new CertInfo();
        certcatalogid = getRequestParameter("guid");

        certrowguid = getRequestParameter("certrowguid");
        certownertype = getRequestParameter("certownertype");
        addCallbackParam("certno", getProjectno());
        if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(certownertype))) {
            certownertype = ZwfwConstant.CERTOWNERTYPE_ZRR;
        } else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(certownertype))) {
            certownertype = ZwfwConstant.CERTOWNERTYPE_FR;
        }
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

            certrowguid = auditProject.getCertrowguid();
        }
        if (StringUtil.isNotBlank(certrowguid)) {
            addViewData("rowguid", certrowguid);
            // 获得基本信息
            certinfo = iCertInfoExternal.getCertInfoByRowguid(certrowguid);
            // 验证是否存在基本信息
            if (certinfo == null) {
                // 不存在证照基本信息，提示消息，并关闭页面
                addCallbackParam("msg", "该证照基础信息不存在！");
                return;
            }
            // 如果没有证照附件，设置Certcliengguid
            if (StringUtil.isBlank(certinfo.getCertcliengguid())) {
                certinfo.setCertcliengguid(UUID.randomUUID().toString());
            }
            // 获得元数据配置表所有顶层节点
            metadataList = iCertConfigExternal.selectMetadataByIdAndVersion(certinfo.getCertcatalogid(),
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
            // 日期格式转换
            this.convertDate(metadataList, dataBean);
            // 返回渲染的字段(子表修改模式)
            addCallbackParam("controls", this.generateMapUseSubExtension("certaddaction", metadataList,
                    ZwfwConstant.MODE_EDIT, ZwfwConstant.SUB_MODE_EDIT));
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
        } else {
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

                    // 济宁市放射诊疗建设项目职业病危害放射防护预评价审核备案凭证
                    if (auditProject != null && "济宁市放射诊疗建设项目职业病危害放射防护预评价审核备案凭证".equals(certCatalog.getCertname())) {
                        Record record = iCxBusService.getDzbdDetail("formtable20211119194634",
                                auditProject.getRowguid());
                        if (record != null) {
                            String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");
                            dataBean.set("yljgmc", record.getStr("qingsryljgmc1"));
                            dataBean.set("xmmc", record.getStr("qingsrxmmc1"));
                            dataBean.set("xmdz", record.getStr("wenbk3"));

                            if (StringUtil.isNotBlank(record.getStr("dxkz5"))) {
                                dataBean.set("xmxq", record.getStr("dxkz5") + " " + record.getStr("wbk4"));
                            }
                            if (StringUtil.isNotBlank(record.getStr("dxkz6"))) {
                                dataBean.set("xmxq", record.getStr("dxkz6") + " " + record.getStr("wbk5"));
                            }
                            if (StringUtil.isNotBlank(record.getStr("dxkz7"))) {
                                dataBean.set("xmxq", record.getStr("dxkz7") + " " + record.getStr("wbk6"));
                            }
                            dataBean.set("fddbr", record.getStr("fddbr"));
                            dataBean.set("xmfzr", record.getStr("xmfzr"));
                            dataBean.set("lxr", record.getStr("lxr"));
                            dataBean.set("lxdh", record.getStr("lxdh"));
                            dataBean.set("ypjdw", record.getStr("ypjdw"));
                            dataBean.set("ypjbgbh", record.getStr("ypjbgbh"));
                            dataBean.set("zybwhlb", record.getStr("dxkz8"));
                            String idnum = record.getStr("idnum");
                            if (idnum.length() == 1) {
                                idnum = "00" + idnum;
                            } else if (idnum.length() == 2) {
                                idnum = "0" + idnum;
                            }
                            dataBean.set("bh", "济审放预〔" + year + "〕第" + idnum + "号");

                        }
                    }

                    // 在城市建筑物、设施上张挂、张贴宣传品审批
                    if (auditProject != null && "在城市建筑物、设施上张挂、张贴宣传品审批".equals(auditProject.getProjectname())) {
                        if ("邹城市临时性户外广告设置审批表".equals(certCatalog.getCertname())) {
                            Record record = iCxBusService.getDzbdDetail("ZCSLSXHWGGSZSPB", auditProject.getRowguid());
                            if (record != null) {
                                // 申请单位
                                dataBean.set("sqdw", record.getStr("sqdw"));
                                // 负责人
                                dataBean.set("fzr", record.getStr("fzr"));
                                // 单位地址
                                dataBean.set("dwdz", record.getStr("dwdz"));
                                // 联系电话
                                dataBean.set("lxdh", record.getStr("lxdh"));
                                // 设置位置
                                dataBean.set("szwz", record.getStr("szwz"));
                                // 拱门数量
                                dataBean.set("gmsl", record.getStr("dtqmhgmgs"));
                                // 条幅数量
                                dataBean.set("tfsl", record.getStr("tfhbfgs"));
                                // 彩旗数量
                                dataBean.set("cqsl", record.getStr("cq"));
                                // 开始日期
                                dataBean.set("ksrq", record.getStr("ksrq"));
                                // 结束日期
                                dataBean.set("jsrq", record.getStr("jsrq"));
                                // 办理人签字
                                dataBean.set("blrqz", record.getStr("sqdwqz"));
                            }
                        }
                    }

                    // 济宁市放射诊疗建设项目职业病危害放射防护预评价审核备案凭证
                    if (auditProject != null && "济宁市放射诊疗建设项目职业病危害放射防护设施竣工验收合格证".equals(certCatalog.getCertname())) {
                        Record record = iCxBusService.getDzbdDetail("formtable20211119221328",
                                auditProject.getRowguid());
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

                        }
                    }

                    // 医疗广告证明文件信息返回
                    if (auditProject != null && "医疗广告审查（中医医疗机构除外）".equals(certCatalog.getCertname())) {
                        Record record = iCxBusService.getYlzmDetailByRowguid(auditProject.getRowguid());
                        if (record != null) {
                            dataBean.set("yljgdymc", record.getStr("yljgdymc"));
                            dataBean.set("zydjh", record.getStr("zydjh"));
                            dataBean.set("fddbr", record.getStr("fddbr"));
                            dataBean.set("sfzh", record.getStr("sfzh"));
                            dataBean.set("yljgdz", record.getStr("yljgdz"));
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

                            dataBean.set("zmwh", "济医广〔2022〕第" + idnumnew + "号");

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
                            if (StringUtil.isNotBlank(yxqx)) {
                                dataBean.set("sjyxqx", codeItemsService.getItemTextByCodeName("医疗广告_校验有效期", yxqx) + "（自"
                                        + starttime + "至" + endtime + "）");
                            } else {
                                dataBean.set("sjyxqx", "");
                            }

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
                            dataBean.set("BH", record.getStr("qit"));
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

                    // 公共卫生许可的事项
                    /*
                     * log.info("证照页面业务数据初始化后台加载的证照是：" +
                     * certCatalog.getCertname());
                     * if(auditProject != null &&
                     * StringUtil.isNotBlank(certCatalog.getCertname()) &&
                     * certCatalog.getCertname().contains("卫生许可")) {
                     * log.info("邹城个性化证照上传后台执行，公共卫生许可证，进入。。。");
                     * // 包含卫生许可，则进行加下来的操作
                     * String taskguid = auditProject.getTaskguid();
                     * AuditTaskExtension result =
                     * taskExtensionService.getTaskExtensionByTaskGuid(taskguid,
                     * false).getResult();
                     * if(result != null) {
                     * String formid = result.getStr("formid");
                     * log.info("邹城个性化证照上传后台执行，公共卫生许可证，formid:" + formid);
                     * // 由于目前只有两种事项是需要进行配置此操作的，所以在进行查表的时候需要进行区别对待，
                     * // 实施在进行建电子表单的时候是直接进行新增的，没有进行对数据表的新增，导致表名是默认的，
                     * // 公共场所卫生许可证新申请 254 formtable20211213224502
                     * // 公共场所卫生许可证延续 274 formtable20220111193910
                     * // 只进行判断上述两种电子表单
                     * // 电子表单中获取公共卫生许可相关信息
                     * Record record =
                     * iCxBusService.getGgwsxk(formid,auditProject.getRowguid())
                     * ;
                     * if (record != null) {
                     * // sqdw,fddbr,dwdz,duoxklb1 xksx
                     * dataBean.set("dwmc", record.getStr("sqdw"));
                     * dataBean.set("fzr", record.getStr("fddbr"));
                     * dataBean.set("dz", record.getStr("dwdz"));
                     * String xksx = record.getStr("xksx");
                     * if(StringUtil.isNotBlank(xksx)) {
                     * dataBean.set("xkxm",
                     * codeItemsService.getItemTextByCodeName("申请经营项目", xksx));
                     * }else {
                     * dataBean.set("xkxm", "");
                     * }
                     * }
                     * }
                     *
                     * }
                     */

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

                    // 城市建筑垃圾处置核准
                    if (auditProject != null) {
                        if (auditProject.getProjectname().contains("建筑垃圾处置核准")) {
                            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), false)
                                    .getResult();
                            if ("370811".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(auditTask.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370828".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(auditTask.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370882".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(auditTask.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370892".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(auditTask.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370826".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(auditTask.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370827".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(auditTask.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370829".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(auditTask.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370830".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(auditTask.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370831".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(auditTask.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370832".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(auditTask.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370881".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(auditTask.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370883".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(auditTask.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370890".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(auditTask.getItem_id());
                                dataBean.set("zzbh", idnum);
                            } else if ("370891".equals(auditProject.getAreacode())) {
                                String idnum = createJsGcZsbh(auditTask.getItem_id());
                                dataBean.set("zzbh", idnum);
                            }

                            if (!"370800".equals(auditProject.getAreacode())) {
                                Record record = iCxBusService.getDzbdDetail("formtable20220420093558",
                                        auditProject.getRowguid());
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

                    // 申请省际普通货物水路运输经营许可
                    if (auditProject != null && "建筑节能技术产品初次认定".equals(certCatalog.getCertname())) {
                        Record record = iCxBusService.getJzjnjscprdwbxtbdByRowguid(auditProject.getRowguid());
                        if (record != null) {
                            Record cpmc = iCxBusService.getCodeItemTextByValue(record.getStr("cpmc"));
                            if (cpmc != null) {
                                dataBean.set("cpmc", cpmc.getStr("itemtext"));
                            }
                            Record zxbz = iCxBusService.getCodeItemTextByValue(record.getStr("zxbz"));
                            if (zxbz != null) {
                                dataBean.set("zxbz", zxbz.getStr("itemtext"));
                            }
                            dataBean.set("qyzcdz", record.getStr("qyzcdz"));
                            dataBean.set("qyscdz", record.getStr("qyscdz"));
                            dataBean.set("jcjgmc", record.getStr("jcjgmc"));
                            dataBean.set("bgbh", record.getStr("xsjcbgbh"));
                            dataBean.set("sqdw", record.getStr("sqdw"));
                            dataBean.set("syfw", "工业与民用建筑");
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
                                dataBean.set("mastercs", en.getValue());
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

                    // 返回渲染的字段(子表新增模式)
                    addCallbackParam("controls", this.generateMapUseSubExtension("certaddaction", metadataList,
                            ZwfwConstant.MODE_ADD, ZwfwConstant.SUB_MODE_ADD));
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
                } else {
                    // 获得证照附件（正本）guid
                    if (StringUtil.isBlank(certinfo.getCertcliengguid())) {
                        certinfo.setCertcliengguid(getViewData("certcliengguid"));
                    }
                    // 获得证照附件（副本）guid
                    if (StringUtil.isBlank(certinfo.getCopycertcliengguid())) {
                        certinfo.setCopycertcliengguid(getViewData("copycertcliengguid"));
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
     * @param parentguidList json (key:parentguid value:fieldname)
     * @param cliengguids    json (key:fieldname value:cliengguid)
     * @param isGenerateCopy 是否生成副本
     * @throws ParseException
     */
    public void generateCert(String parentguidList, String cliengguids, String isGenerateCopy) throws ParseException {
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
            setCertValue(parentguidList, cliengguids, jsonObject);
            // 生成证照
            certinfo.set("projectguid", getRequestParameter("projectguid"));
            certinfo.set("cxcode", getRequestParameter("cxcode"));
            msg = iCertInfoExternal.generateCert(certinfo, dataBean, isGenerateCopy, areacode);
            // 生成成功将rowguid放入缓存，并更新办件
            if ("生成证照成功！".equals(msg)) {
                certinfo= iCertInfoExternal.getCertInfoByRowguid(certinfo.getRowguid());
                auditProject.setCertrowguid(certinfo.getRowguid());
                addViewData("rowguid", certinfo.getRowguid());
                auditProjectService.updateProject(auditProject);

            }
        } else {
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
            msg = iCertInfoExternal.generateCert(certinfo, dataBean, isGenerateCopy, areacode);
            // 生成成功将rowguid放入缓存，并更新办件
            if ("生成证照成功！".equals(msg)) {
                certinfo= iCertInfoExternal.getCertInfoByRowguid(certinfo.getRowguid());
                auditProject.setCertrowguid(certinfo.getRowguid());
                addViewData("rowguid", certinfo.getRowguid());
                auditProjectService.updateProject(auditProject);
            }
        }
        addCallbackParam("msg", msg);
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
        String attachurl = "http://112.6.110.176:28080/jnzwfw/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=";
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
            String zj = json.getString("zj");
            // 注册地址
            String regAddress = json.getString("regaddress");
            // 法定代表人
            String master = json.getString("mastercs");
            // 法人公民身份证号码
            String masterId = json.getString("masterId");

            Integer applyertype = auditProject.getApplyertype();
            String qlcguid = auditProject.getAreacode() + (int) (Math.random() * (99999 - 00001 + 1)) + 00001;

            String applyguid = auditProject.getAreacode() + (int) (Math.random() * (99999 - 00001 + 1)) + 00001;
            String applicantId = auditProject.getAreacode() + (int) (Math.random() * (99999 - 00001 + 1)) + 00001;
            String gcguid = "";
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
                gcguid = auditProject.getAreacode() + (int) (Math.random() * (99999 - 00001 + 1)) + 00001;
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
                                gcguid = descs[1];
                                log.info("addbus接口返回挂车编号：" + gcguid);
                                CxBus busty = iCxBusService.getCxbusByGuid(gcguid);
                                if (busty == null) {
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
                                } else {
                                    busty.setVehiclekind(gcllx);
                                    busty.setVehicleno(gclhp);
                                    busty.setGcclsbdm(gcclsbdm);
                                    busty.setModel(gclcpxh);
                                    busty.setcxlength(gcc);
                                    busty.setWidth(gck);
                                    busty.setHeight(gcg);
                                    busty.setWeight(gclzbzl);
                                    busty.setDriveimg(drive_img);
                                    busty.setAxles(gczs);
                                    busty.setTyles(gclts);
                                    busty.setAxes(gczx);
                                    iCxBusService.update(busty);
                                }

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
                                qlcguid = descs[1];
                                log.info("addbus接口返回牵引车编号：" + qlcguid);
                                CxBus busty = iCxBusService.getCxbusByGuid(qlcguid);
                                if (busty == null) {
                                    CxBus bus = new CxBus();
                                    bus.setRowguid(UUID.randomUUID().toString());
                                    bus.setVehicleid(qlcguid);
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
                                } else {
                                    busty.setVehiclekind(gcllx);
                                    busty.setVehicleno(gclhp);
                                    busty.setGcclsbdm(gcclsbdm);
                                    busty.setModel(gclcpxh);
                                    busty.setcxlength(gcc);
                                    busty.setWidth(gck);
                                    busty.setHeight(gcg);
                                    busty.setWeight(gclzbzl);
                                    busty.setDriveimg(drive_img);
                                    busty.setAxles(gczs);
                                    busty.setTyles(gclts);
                                    busty.setAxes(gczx);
                                    iCxBusService.update(busty);
                                }
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
            jsonnew2.put("trailerNo", gcguid);
            jsonnew2.put("axles", zs);
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
                    return "error";
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
        //如果证照目录存在，同步本地的auditcertcatalog表
        if (certCatalog != null) {
            AuditCertCatalog catalog = iAuditCertCatalog.getAuditCertCatalogByCatalogid(certCatalog.getCertcatalogid())
                    .getResult();
            if (catalog == null) {
                String cliengguid = UUID.randomUUID().toString();
                String copycliengguid = UUID.randomUUID().toString();
                iAuditCertCatalog.createAuditCatalogByCertCatalog(certCatalog.getCertcatalogid(),
                        cliengguid, copycliengguid,
                        certCatalog.getVersion());
                //将模板附件保存至本地附件库
//                List<JSONObject> attachList = iCertAttachExternal.getAttachList(certCatalog.getTempletcliengguid(),
//                        areacode);
                List<JSONObject> attachList = null;
                if (certCatalog.getTdTempletcliengguid() != null || certCatalog.getTdTempletcliengguid() != "") {
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
                //如果存在模板副本，将副本也放入本地附件库
                if (StringUtil.isNotBlank(certCatalog.getTdCopytempletcliengguid())) {
                    List<JSONObject> copyAttachList = iCertAttachExternal
                            .getAttachList(certCatalog.getTdCopytempletcliengguid(), areacode);
                    if (copyAttachList != null && copyAttachList.size() > 0) {
                        for (JSONObject json : copyAttachList) {
                            AttachUtil.saveFileInputStream(UUID.randomUUID().toString(),
                                    copycliengguid, json.getString("attachname"),
                                    json.getString("contentype"), "证照检索新增", json.getLong("size"),
                                    iCertAttachExternal.getAttach(json.getString("attachguid"), areacode),
                                    userSession.getUserGuid(), userSession.getDisplayName());
                        }
                    }
                } else if (StringUtil.isNotBlank(certCatalog.getCopytempletcliengguid())) {
                    List<JSONObject> copyAttachList = iCertAttachExternal
                            .getAttachList(certCatalog.getCopytempletcliengguid(), areacode);
                    if (copyAttachList != null && copyAttachList.size() > 0) {
                        for (JSONObject json : copyAttachList) {
                            AttachUtil.saveFileInputStream(UUID.randomUUID().toString(),
                                    copycliengguid, json.getString("attachname"),
                                    json.getString("contentype"), "证照检索新增", json.getLong("size"),
                                    iCertAttachExternal.getAttach(json.getString("attachguid"), areacode),
                                    userSession.getUserGuid(), userSession.getDisplayName());
                        }
                    }
                }
            } else {
                if (catalog.getVersion() != null && !catalog.getVersion().equals(certCatalog.getVersion())) {
                    String cliengguid = UUID.randomUUID().toString();
                    String copycliengguid = UUID.randomUUID().toString();
                    iAuditCertCatalog.updateAuditCertCatalog(catalog, cliengguid,
                            copycliengguid, certCatalog.getVersion());
                    //将模板附件保存至本地附件库
//                    List<JSONObject> attachList = iCertAttachExternal.getAttachList(certCatalog.getTempletcliengguid(),
//                            areacode);
                    List<JSONObject> attachList = null;
                    if (certCatalog.getTdTempletcliengguid() != null || certCatalog.getTdTempletcliengguid() != "") {
                        attachList = iCertAttachExternal.getAttachList(certCatalog.getTdTempletcliengguid(), areacode);
                        if (attachList.size() == 0) {
                            attachList = iCertAttachExternal.getAttachList(certCatalog.getTempletcliengguid(), areacode);
                        }
                    } else {
                        attachList = iCertAttachExternal.getAttachList(certCatalog.getTempletcliengguid(), areacode);
                    }
                    if (attachList != null && attachList.size() > 0) {
                        for (JSONObject json : attachList) {
                            AttachUtil.saveFileInputStream(UUID.randomUUID().toString(),
                                    cliengguid, json.getString("attachname"),
                                    json.getString("contentype"), "证照检索新增", json.getLong("size"),
                                    iCertAttachExternal.getAttach(json.getString("attachguid"), areacode),
                                    userSession.getUserGuid(), userSession.getDisplayName());
                        }
                    }
                    //如果存在模板副本，将副本也放入本地附件库
                    if (StringUtil.isNotBlank(certCatalog.getTdCopytempletcliengguid())) {
                        List<JSONObject> copyAttachList = iCertAttachExternal
                                .getAttachList(certCatalog.getTdCopytempletcliengguid(), areacode);
                        if (copyAttachList != null && copyAttachList.size() > 0) {
                            for (JSONObject json : copyAttachList) {
                                AttachUtil.saveFileInputStream(UUID.randomUUID().toString(),
                                        copycliengguid, json.getString("attachname"),
                                        json.getString("contentype"), "证照检索新增", json.getLong("size"),
                                        iCertAttachExternal.getAttach(json.getString("attachguid"), areacode),
                                        userSession.getUserGuid(), userSession.getDisplayName());
                            }
                        }
                    } else if (StringUtil.isNotBlank(certCatalog.getCopytempletcliengguid())) {
                        List<JSONObject> copyAttachList = iCertAttachExternal
                                .getAttachList(certCatalog.getCopytempletcliengguid(), areacode);
                        if (copyAttachList != null && copyAttachList.size() > 0) {
                            for (JSONObject json : copyAttachList) {
                                AttachUtil.saveFileInputStream(UUID.randomUUID().toString(),
                                        copycliengguid, json.getString("attachname"),
                                        json.getString("contentype"), "证照检索新增", json.getLong("size"),
                                        iCertAttachExternal.getAttach(json.getString("attachguid"), areacode),
                                        userSession.getUserGuid(), userSession.getDisplayName());
                            }
                        }
                    }
                }
            }
        }
        //目录校验
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
            //system.out.println("imageMap:" + wordMap.get("imageMap"));
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
        //system.out.println("模本文件获取到");
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
            //system.out.println("qrcodeurl:" + qrcodeurl);
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

                    //system.out.println("subMetaDataList:" + subMetaDataList);
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
        //system.out.println("开始插入图片到书签");
        //system.out.println("imageMap" + imageMap);
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
                    //system.out.println("开始插入指定的标签");
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
            //system.out.println("结束插入图片到书签");
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
                    //system.out.println("获得标签成功");
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
            addCallbackParam("msg", "ok");
        } else {
            addCallbackParam("msg", "请先生成证照");
        }
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
        auditProjectService.updateProject(auditProject);
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
     * @param certMetadataList
     * @param isdetail
     * @param recordName       实体名称
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
     *
     * @param metadataList
     * @param record
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
                         * metadata
                         * .getDatasource_codename() + "列表选择错误)");
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
                recordMap.put("onclick", "openAddSublist('" + metadata.getRowguid() + "', '"
                        + metadata.getFieldchinesename() + "', '" + parentguid + "')");
                recordMap.put("iconCls", "icon-edit");
            } else if (ZwfwConstant.SUB_MODE_EDIT.equals(subMode)) { // 修改模式
                recordMap.put("onclick", "openEditSublist('" + metadata.getRowguid() + "', '"
                        + metadata.getFieldchinesename() + "', '" + metadata.getFieldname() + "')");
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
                + calendar.get(Calendar.DAY_OF_MONTH);
        int theYearLength = 0;
        int snLength = 4;
        String certno = new DBServcie().getFlowSn(numberName, numberFlag, theYearLength, false, snLength);
        EpointFrameDsManager.commit();
        return certno;
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
            if (num.length() == 3) {
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
        else if ("11370883MB2857374H7370123021001".equals(itemguid) || "11370883MB2857374H400012310600201".equals(itemguid)) {
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
        }

        return result;

    }

    public String createJsGcZsbh(String itemguid) {
        String result = "";
        String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");

        if ("11370827MB2857833K3370117044000".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNumNotYear("yttsjsgcys");
            if (num.length() == 1) {
                num = "00" + num;
            } else if (num.length() == 2) {
                num = "0" + num;
            }

            result = num;
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
        } else if ("TE37087320203090114370117038000".equals(itemguid)) {
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
            String num = GenerateNumberUtil.getCertNumNotYear("ytfwysxk");
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
        } else if ("11370827MB2857833K4370117038000".equals(itemguid)) {
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
        else if ("11370800MB285591843370123020001".equals(itemguid)
                || "11370800MB28559184300012310800001".equals(itemguid)
                || "11370800MB28559184300012310800002".equals(itemguid)
                || "11370800MB28559184300012310800003".equals(itemguid)) {
            String num = GenerateNumberUtil.getCertNum("jnxdcpqyxsq");
            if (num.length() == 1) {
                num = "0" + num;
            }

            result = "鲁卫消证字（" + year + "）第07" + num + "号";
        }
        return result;

    }

    public String signPdf() throws Exception {
        String msg = "";
        // 获得基本信息
        certinfo = iCertInfoExternal.getCertInfoByRowguid(certrowguid);
        if (certinfo != null) {
            String ouguid = null;
            FrameOu frameOu = iOuService.getOuByOuGuid(certinfo.getOuguid());
            if (frameOu != null) {
                ouguid = certinfo.getOuguid();
            } else {
                //存在一些历史数据是存放oucode`
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

}
