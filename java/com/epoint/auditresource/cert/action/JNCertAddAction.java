package com.epoint.auditresource.cert.action;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.*;
import com.aspose.words.Shape;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowUser;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditresource.auditcertcatalog.domain.AuditCertCatalog;
import com.epoint.basic.auditresource.auditcertcatalog.inter.IAuditCertCatalog;
import com.epoint.basic.auditsp.auditspsharematerialrelation.domain.AuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditspsharematerialrelation.inter.IAuditSpShareMaterialRelation;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
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
import com.epoint.cert.basic.certcatalog.certmetadata.domain.CertMetadata;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.certinfosubextension.domain.CertInfoSubExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.cert.common.api.IGenerateCert;
import com.epoint.cert.commonutils.CertConstant;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.cert.external.ICertConfigExternal;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.util.*;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.memory.EHCacheUtil;
import com.epoint.core.utils.reflect.ReflectUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.message.api.IOnlineMessageInfoService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkz.api.IAuditProjectFormSgxkzService;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkz.api.entity.AuditProjectFormSgxkz;
import com.itextpdf.text.pdf.security.CertificateUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.List;


/**
 * 证照基本信息表新增页面对应的后台
 *
 * @author dingwei
 * @version [版本号, 2017-11-01 16:01:53]
 */
@RestController("jncertaddaction")
@Scope("request")
public class JNCertAddAction extends BaseController
{

    private static final long serialVersionUID = 1L;

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
    private ICertInfo certInfoService;

    @Autowired
    private ICertConfigExternal iCertConfigExternal;

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


    private AuditProject auditProject;

    @Autowired
    private IAuditProject auditProjectService;

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
    private IAuditProjectMaterial projectMaterialService;

    @Autowired
    private IOnlineMessageInfoService iOnlineMessageInfoService;

    @Autowired
    private IAuditOrgaWindowYjs auditOrgaWindowService;
    
    @Autowired
    private IOuService ouService;
    
    @Autowired
    private IAuditTaskResult iAuditTaskResult;
    
    @Autowired
    private IAuditProjectFormSgxkzService iAuditProjectFormSgxkzService;
    
    /**
     * 事项api
     */
    @Autowired
    private IAuditTask auditTaskService;
    

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
    
    private String areacode ="";
    
    private String taskguid = "";

    @Override
    public void pageLoad() {
        
        certinfo = new CertInfo();
        certcatalogid = getRequestParameter("guid");
        certrowguid = getRequestParameter("certrowguid");
        certownertype = getRequestParameter("certownertype");
        String certno = "";
        String projectguid = getRequestParameter("projectguid");
        String isRender = getRequestParameter("isRender");
        if(StringUtil.isNotBlank(getRequestParameter("projectguid"))){            
            if (ZwfwUserSession.getInstance().getCitylevel()!=null&&(Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ))){
                areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
            }else{
                areacode = ZwfwUserSession.getInstance().getAreaCode();
            }
            auditProject = auditProjectService.getAuditProjectByRowGuid(projectguid, areacode).getResult();
            taskguid = auditProject.getTaskguid();
            AuditTaskResult result = iAuditTaskResult.getAuditResultByTaskGuid(taskguid, false).getResult();
            if(result!=null){
                certcatalogid = result.getSharematerialguid();
            }else{
                addCallbackParam("msg", "未配置证照结果！无法上传证照");
                return;
            }
            certrowguid = auditProject.getCertrowguid();
        }
        certownertype = auditProject.getApplyertype().toString();
        if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(certownertype))) {
            certownertype = ZwfwConstant.CERTOWNERTYPE_ZRR;
        } else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(certownertype))) {
            certownertype = ZwfwConstant.CERTOWNERTYPE_FR;
        }
        if(StringUtil.isNotBlank(certrowguid)){
            addViewData("rowguid", certrowguid);
            // 获得基本信息
            certinfo = iCertInfoExternal.getCertInfoByRowguid(certrowguid);
            // 验证是否存在基本信息
            if (certinfo == null) {
                // 不存在证照基本信息，提示消息，并关闭页面
                addCallbackParam("msg", "该证照基本信息不存在！");
                return;
            }
            
            //如果没有证照附件，设置Certcliengguid
            if (StringUtil.isBlank(certinfo.getCertcliengguid())) {
                certinfo.setCertcliengguid(UUID.randomUUID().toString());
            }
            // 获得元数据配置表所有顶层节点
            if (StringUtil.isNotBlank(getViewData("certcliengguid"))) {
                metadataList = EHCacheUtil.get("CERTMETADATA" + getViewData("certcliengguid"));
            }
            if(metadataList == null){
                metadataList=iCertConfigExternal.selectMetadataByIdAndVersion(certinfo.getCertcatalogid(),String.valueOf(certinfo.getCertcatalogversion()),areacode);
                EHCacheUtil.put("CERTMETADATA" + getViewData("certcliengguid"), metadataList);
            }
            if(metadataList != null && metadataList.size() > 0){
                metadataList.removeIf(metadata -> {
                    if (StringUtil.isNotBlank(metadata.get("PARENTGUID"))) {
                        return true;
                    } else {
                        return false;
                    }
                });
            }
            // 设置页面展示的持有人类型
            certCatalog = EHCacheUtil.get("CERTCATALOG_" + getViewData("certcliengguid"));
            if(certCatalog==null){
                certCatalog = iCertConfigExternal.getCatalogByCatalogid(certinfo.getCertcatalogid(),areacode);
                EHCacheUtil.put("CERTCATALOG_" + getViewData("certcliengguid"), certCatalog);
            }

            if (certCatalog != null) {
                if(StringUtil.isNotBlank(certCatalog.getBelongtype())){
                    String codename = codeItemsService.getItemTextByCodeName("证照持有人类型", certCatalog.getBelongtype());
                    addCallbackParam("codename", codename);
                    addCallbackParam("codevalue",  certCatalog.getBelongtype());
                    addCallbackParam("certownertype", certCatalog.getBelongtype());
                    if(!(certCatalog.getBelongtype().contains(certownertype)
                            || CertConstant.CERTOWNERTYPE_HH.equals(certCatalog.getBelongtype())
                            || CertConstant.CERTOWNERTYPE_QT.equals(certCatalog.getBelongtype()))){
                        addCallbackParam("warn", true);
                    }
                }

            }

            dataBean = iCertInfoExternal.getCertExtenByCertInfoGuid(certrowguid);
            if (dataBean != null) {
                extendguid = dataBean.getRowguid();
            }

            // 日期格式转换
            this.convertDate(metadataList, dataBean);
            // 返回渲染的字段(子表修改模式)
            addCallbackParam("controls",
                    this.generateMapUseSubExtension("certaddaction" ,metadataList,
                            ZwfwConstant.MODE_EDIT, ZwfwConstant.SUB_MODE_EDIT));
            //渲染基本信息
            addCallbackParam("basiccontrols", this.generateBasicDefaultMap(metadataList, false, "certinfo")==null?"":this.generateBasicDefaultMap(metadataList, false, "certinfo"));
            addCallbackParam("edit", "edit");
            addViewData("edit", "edit");
        }else{
            if (StringUtil.isNotBlank(certcatalogid)) {
                // 设置持有人类型
                if(StringUtil.isNotBlank(getViewData("certcliengguid"))){
                    certCatalog = EHCacheUtil.get("CERTCATALOG_" + getViewData("certcliengguid"));
                    metadataList = EHCacheUtil.get("CERTMETADATA" + getViewData("certcliengguid"));
                }
                if(certCatalog==null){
                    certCatalog = iCertConfigExternal.getCatalogByCatalogid(certcatalogid,areacode);
                }
                if (certCatalog == null) {
                    return;
                }
                // 获得元数据配置表所有顶层节点
                if(metadataList==null){
                    metadataList=iCertConfigExternal.selectMetadataByIdAndVersion(certcatalogid,String.valueOf(certinfo.getCertcatalogversion()),areacode);
                }
                if(metadataList != null && metadataList.size() > 0){
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
                    if(StringUtil.isNotBlank(certCatalog.getBelongtype())){
                        String codename = codeItemsService.getItemTextByCodeName("证照持有人类型", certCatalog.getBelongtype());
                        addCallbackParam("codename", codename);
                        addCallbackParam("codevalue",  certCatalog.getBelongtype());
                        addCallbackParam("certareaguid", certCatalog.getRowguid());
                        if(!(certCatalog.getBelongtype().contains(certownertype)
                                || CertConstant.CERTOWNERTYPE_HH.equals(certCatalog.getBelongtype())
                                || CertConstant.CERTOWNERTYPE_QT.equals(certCatalog.getBelongtype()))){
                            addCallbackParam("warn", true);
                        }
                    }
                    //第一次加载的时候生成证照编号
                    if(StringUtil.isBlank(certinfo.getCertno()) 
                            && StringUtil.isNotBlank(isRender)){
                        AuditProjectFormSgxkz sgxkz = iAuditProjectFormSgxkzService.getRecordBy(projectguid);
                        certno += areacode + EpointDateUtil.convertDate2String(new Date(), "yyyyMMdd");
                        certno = getCertno(certno);
                        if(sgxkz!=null){
                            certno += sgxkz.getXiangmufenlei();
                            dataBean.putAll(sgxkz);
                        }
                        dataBean.put("bh", certno);
                        dataBean.put("bzdw", userSession.getOuName());
                        certinfo.setCertno(certno);
                        System.err.println("施工许可证照编号："+certno);
                    }
                    String certownername = auditProject.getApplyername();
                    String certownercerttype = codeItemsService.getCodeItemByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()).getItemValue();
                    String certownerno = auditProject.getCertnum();
                    addCallbackParam("type", auditProject.getApplyertype());
                    if(certCatalog.getBelongtype().contains(certownertype)
                            || CertConstant.CERTOWNERTYPE_HH.equals(certCatalog.getBelongtype())
                            || CertConstant.CERTOWNERTYPE_QT.equals(certCatalog.getBelongtype())){
                        addCallbackParam("certownername", certownername);
                        addCallbackParam("certownercerttype", certownercerttype);
                        addCallbackParam("certownerno", certownerno);
                    }
                    addCallbackParam("awarddate",EpointDateUtil.convertDate2String(new Date(),"yyyy-MM-dd"));
                    // 设置证照正本文件guid
                    certinfo.setCertcliengguid(UUID.randomUUID().toString());
                    addViewData("certcliengguid", certinfo.getCertcliengguid());
                    // 设置证照附件（副本）guid
                    certinfo.setCopycertcliengguid(UUID.randomUUID().toString());
                    addViewData("copycertcliengguid", certinfo.getCopycertcliengguid());
                    EHCacheUtil.put("CERTCATALOG_" + getViewData("certcliengguid"), certCatalog);
                    EHCacheUtil.put("CERTMETADATA" + getViewData("certcliengguid"), metadataList);
                    // 返回渲染的字段(子表新增模式)
                    addCallbackParam("controls",
                            this.generateMapUseSubExtension("certaddaction", metadataList,
                                    ZwfwConstant.MODE_ADD, ZwfwConstant.SUB_MODE_ADD));
                    // 渲染基本信息
                    addCallbackParam("basiccontrols", this.generateBasicDefaultMap(metadataList, false, "certinfo")==null?"":this.generateBasicDefaultMap(metadataList, false, "certinfo"));
                    
                    // 用作返回证照目录版本唯一标识
                    certinfo.setCertcatalogid(certcatalogid);
                    if(certCatalog.getBelongtype().contains(certownertype)
                            || CertConstant.CERTOWNERTYPE_HH.equals(certCatalog.getBelongtype())
                            || CertConstant.CERTOWNERTYPE_QT.equals(certCatalog.getBelongtype())){
                        certinfo.setCertownertype(certownertype);
                    }else{
                        certinfo.setCertownertype(certCatalog.getBelongtype());
                    }
                    addCallbackParam("certcatalogid", certcatalogid);
                }
                else {
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
        // 回传目录模板文件（副本）个数
        if (StringUtil.isNotBlank(certCatalog.getCopytempletcliengguid())) {
            if(StringUtil.isBlank(getViewData("copyTempletCount"))){
                copyTempletCount = iCertAttachExternal.getAttachList(certCatalog.getCopytempletcliengguid(), areacode).size();
                addViewData("copyTempletCount", String.valueOf(copyTempletCount));
            }else{
                copyTempletCount = Integer.parseInt(getViewData("copyTempletCount"));
            }
        }
        addCallbackParam("copyTempletCount", copyTempletCount);
        addCallbackParam("officeweb365url", this.getOfficeConfig());
    }
    
    /**
     * 
     *  [个性化办件编号]
     *  [功能详细描述]
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public String getCertno(String certno){

        Object[] args = new Object[]{"个性化证照编号", certno, 0, 0, 0, 0, 2};
        String flowsn="";
        try {
            flowsn = String.valueOf(CommonDao.getInstance("common").executeProcudureWithResult(args.length + 1, 12,
                    "Common_Getflowsn", args));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flowsn;
    }

    /**
     * 保存并关闭 存在隐患，微服务时mongodb不回滚
     *
     * @param params json (key:parentguid value:fieldname)
     * @param cliengguids json (key:fieldname value:cliengguid)
     */
    public void add(String params, String cliengguids) {
        JSONObject cliengguidsObject = JSON.parseObject(cliengguids);
        //证照实例赋值
        setCertValue(params , cliengguids , cliengguidsObject);
        String msg = "";
        msg = iCertInfoExternal.saveCertinfo(certinfo,dataBean,areacode);
        if("保存成功！".equals(msg)){
            certinfo= iCertInfoExternal.getCertInfoByRowguid(certinfo.getRowguid());
            auditProject.setCertrowguid(certinfo.getRowguid());
            addViewData("rowguid" , certinfo.getRowguid());
            auditProjectService.updateProject(auditProject);
        }
        addCallbackParam("msg", msg);
    }
    
    
    public void setCertValue(String params, String cliengguids,JSONObject cliengguidsObject){
        JSONObject jsonObject = JSON.parseObject(params);
        certCatalog = EHCacheUtil.get("CERTCATALOG_" + getViewData("certcliengguid"));
        if(certCatalog==null){
            certCatalog = iCertConfigExternal.getCatalogByCatalogid(certcatalogid,areacode);
        }
        // 证照基本信息关联字段赋值
        for (CertMetadata certMetadata : metadataList) {
            if (StringUtil.isNotBlank(certMetadata.getRelatedfield())) {
                certinfo.set(certMetadata.getRelatedfield(), dataBean.get(certMetadata.getFieldname()));
            }
        }
        if (StringUtil.isNotBlank(getViewData("rowguid"))) { // 保存
            // 更新基本信息
            certinfo.setOperatedate(new Date());
            // 排除持有人类型
            certinfo.remove("certownertype");
            if (dataBean != null && !dataBean.isEmpty()) {
                // 更新照面信息，先删除，再插入，效率低
                try {
                    // 更新操作时间
                    dataBean.setOperatedate(new Date());
                    // 设置子数据
                    setSubExtensionJson(jsonObject, dataBean);
                }
                catch (Exception e) {
                    throw new RuntimeException();
                }
            }
        }else{
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
            if(certCatalog!=null){
                // 证照目录编号
                certinfo.setCertcatcode(certCatalog.getCertcatcode());
                // 通用证照目录编号
                certinfo.setTycertcatcode(certCatalog.getTycertcatcode());                
            }
            // 电子证照编号
            if (StringUtil.isNotBlank(certinfo.getCertownerno())) {
                certinfo.setCertinfocode(certinfo.getCertownerno()+ "-" + certinfo.getCertno());
            }
            else {
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
                if (certCatalog.getIssurvery()!=null &&  certCatalog.getIssurvery() == ZwfwConstant.CONSTANT_INT_ONE) {
                    // 设置下次年检时间
                    Integer monthInterval = certCatalog.getSurveryrate(); // 年检间隔（月）
                    if (monthInterval != null && monthInterval > 0) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.MONTH, monthInterval);
                        certinfo.setInspectiondate(calendar.getTime());
                    }
                }
            }
            if(certCatalog!=null){
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
            }
            else {
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
            }
            catch (Exception e) {
                throw new RuntimeException();
            }
        }
        
    }

    /**
     * 保存修改
     */
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

    /**
     *  保存证照
     */
    private String saveCert() {
        if (certinfo == null) {
            return "";
        }
        // 更新基本信息
        certinfo.setOperateusername(userSession.getDisplayName());
        certinfo.setOperatedate(new Date());
        certinfo.remove("certownertype"); // 排除持有人类型
        certinfo.setAdduserguid(userSession.getUserGuid());
        certinfo.setAddusername(userSession.getDisplayName());
        // 电子证照编号
        if (StringUtil.isNotBlank(certinfo.getCertownerno())) {
            certinfo.setCertinfocode(certinfo.getCertownerno() + "-" + certinfo.getCertno());
        }
        else {
            certinfo.setCertinfocode(certinfo.getCertno());
        }    
        return iCertInfoExternal.saveCertinfo(certinfo,dataBean,areacode);
    }

    /**
     *  照面信息附件非空验证
     *  @return
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
                        int attchCount = iCertAttachExternal.getAttachList(certCatalog.getCopytempletcliengguid(), areacode).size();
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
     * @param cliengguids json (key:fieldname value:cliengguid)
     * @param isGenerateCopy 是否生成副本
     * @throws ParseException
     */
    public void generateCert(String parentguidList, String cliengguids, String isGenerateCopy) throws ParseException {
        if (certCatalog == null) {
            return;
        }
        String msg = null;
        JSONObject jsonObject = JSON.parseObject(cliengguids);
        if("edit".equals(getViewData("edit"))) {
            // 生成PDF
            setCertValue(parentguidList,cliengguids,jsonObject);
            //生成证照
            certinfo.setAuditstatus(CertConstant.CERT_CHECK_STATUS_WAIT_REPORT);
            certInfoService.updateCertInfo(certinfo);
            certinfo.set("projectguid", getRequestParameter("projectguid"));
            msg = iCertInfoExternal.generateCert(certinfo, dataBean, isGenerateCopy, areacode);
            //生成成功将rowguid放入缓存，并更新办件
            if("生成证照成功！".equals(msg)){
                certinfo= iCertInfoExternal.getCertInfoByRowguid(certinfo.getRowguid());
                auditProject.setCertrowguid(certinfo.getRowguid());
                addViewData("rowguid" , certinfo.getRowguid());
                auditProjectService.updateProject(auditProject);
            }
        } else {
            setCertValue(parentguidList,cliengguids,jsonObject);
            certinfo.set("projectguid", getRequestParameter("projectguid"));
            msg = iCertInfoExternal.generateCert(certinfo, dataBean, isGenerateCopy, areacode);
            //生成成功将rowguid放入缓存，并更新办件
            if("生成证照成功！".equals(msg)){
                certinfo= iCertInfoExternal.getCertInfoByRowguid(certinfo.getRowguid());
                auditProject.setCertrowguid(certinfo.getRowguid());
                addViewData("rowguid" , certinfo.getRowguid());
                auditProjectService.updateProject(auditProject);
            }
        }
        addCallbackParam("msg", msg);
    }

    /**
     * 打印证照
     * @param isPrintCopy 是否打印副本
     */
    public void printCert(String isPrintCopy) {
        //如果证照目录存在，同步本地的auditcertcatalog表
        if (certCatalog != null) {
            AuditCertCatalog catalog = iAuditCertCatalog.getAuditCertCatalogByCatalogid(certCatalog.getCertcatalogid())
                    .getResult();
            if (catalog == null) {
                String cliengguid= UUID.randomUUID().toString();
                String copycliengguid= UUID.randomUUID().toString();
                iAuditCertCatalog.createAuditCatalogByCertCatalog(certCatalog.getCertcatalogid(),
                        cliengguid, copycliengguid,
                        certCatalog.getVersion());
                //将模板附件保存至本地附件库
//                List<JSONObject> attachList = iCertAttachExternal.getAttachList(certCatalog.getTempletcliengguid(),
//                        areacode);
                List<JSONObject> attachList = null;
                if(certCatalog.getTdTempletcliengguid() != null || certCatalog.getTdTempletcliengguid() != "") {
                    attachList = iCertAttachExternal.getAttachList(certCatalog.getTdTempletcliengguid(), areacode);
                    if(attachList.size()==0)
                    {
                        attachList = iCertAttachExternal.getAttachList(certCatalog.getTempletcliengguid(), areacode);
                    }
                }else {
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
                }else if (StringUtil.isNotBlank(certCatalog.getCopytempletcliengguid())) {
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
            else {
                if (catalog.getVersion() != null && !catalog.getVersion().equals(certCatalog.getVersion())) {
                    String cliengguid= UUID.randomUUID().toString();
                    String copycliengguid= UUID.randomUUID().toString();
                    iAuditCertCatalog.updateAuditCertCatalog(catalog, cliengguid,
                            copycliengguid, certCatalog.getVersion());
                    //将模板附件保存至本地附件库
//                    List<JSONObject> attachList = iCertAttachExternal.getAttachList(certCatalog.getTempletcliengguid(),
//                            areacode);
                    List<JSONObject> attachList = null;
                    if(certCatalog.getTdTempletcliengguid() != null || certCatalog.getTdTempletcliengguid() != "") {
                        attachList = iCertAttachExternal.getAttachList(certCatalog.getTdTempletcliengguid(), areacode);
                        if(attachList.size()==0)
                        {
                            attachList = iCertAttachExternal.getAttachList(certCatalog.getTempletcliengguid(), areacode);
                        }
                    }else {
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
                    }else if (StringUtil.isNotBlank(certCatalog.getCopytempletcliengguid())) {
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
        }
        else { // 支持副本打印
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

        }
        else { // 打印正本
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
            }
            else { // 打印失败
                addCallbackParam("msg", returnMap.get("msg"));
            }
        }
    }
    /**
     * 打印证照doc
     *
     * @param cliengGuid 证照模板文件（正/副本）guid
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
            // 子表数据
            DataSet dataSet = (DataSet) wordMap.get("dataSet");
            returnMap = generatePrintDocSupportCopy(cliengGuid, isPrintCopy, userSession.getUserGuid(),
                    userSession.getDisplayName(), fieldNames, values, imageMap, dataSet, certinfo);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return returnMap;
    }
    
    /**
     * 生成打印的证照doc（支持副本打印）
     *
     * @param cliengGuid
     *            证照模板（正/副本）guid
     * @param isPrintCopy
     *            是否打印副本
     * @param userGuid
     *            用户Guid
     * @param userDisPlayName
     *            用户名称
     * @param fieldNames
     *            word域
     * @param values
     *            word域值
     * @param imageMap
     *            图片map
     * @param dataSet
     *            子表数据
     * @param certinfo
     *            基本信息
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
            String qrcodeurl=ConfigUtil.getConfigValue("cert_qrcode");
            if(StringUtil.isBlank(qrcodeurl)){
                qrcodeurl=certinfo.getCertname();
            }else{
                qrcodeurl+=certinfo.getCertno();
            }
            insertImagetoBookmark(doc, imageMap, true,qrcodeurl);
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
            }
            else { // 未打印，上传
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
                }
                else {
                    auditProject.setCertdocguid(attachGuid);
                }
                auditProjectService.updateProject(auditProject);
            }
            
            // 打印成功
            returnMap.put("issuccess", "1");
            returnMap.put("attachguid", attachGuid);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
            catch (IOException e) {
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
                    }
                    catch (Exception e) {
                        recordList = new ArrayList<>();
                    }
        
                    // 查询子元数据
                    List<CertMetadata> subMetaDataList = iCertConfigExternal
                            .selectSubDispinListByCertguid(certMetadata.getRowguid());
                    
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
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else { // 无子表
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
                    }
                    else {
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
                                        }
                                        catch (Exception e) {
                                            // TODO: handle exception
                                        }
                                    }
                                    else {
                                        valueList.add(EpointDateUtil.convertDate2String(date, "yyyy年MM月dd日"));
                                    }

                                }
                                else {
                                    valueList.add(dateStr);
                                }
                            }
                            else {
                                valueList.add(null);
                            }
                        }
                        else {
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
     * @param watermarkText
     *            水印内容
     * @param width
     *            宽度
     * @param height
     *            高度
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
     * @param watermarkPara
     *            段落
     * @param sect
     *            文档的部分
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
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    /**
     * 插入图片到书签
     *
     * @param doc
     * @param imageMap
     *            图片map
     * @param isUseQrcode
     *            是否使用二维码
     * @param qrcodeText
     *            二维码文本
     */
    public static void insertImagetoBookmark(Document doc, Map<String, Record> imageMap, boolean isUseQrcode,
            String qrcodeText) {
        // DocumentBuilder 类似一个文档操作工具，用来操作已经实例化的Document 对象，DocumentBuilder
        // 里有许多方法 例如插入文本、插入图片、插入段落、插入表格等等
        DocumentBuilder build = null;
        InputStream qrCodeInputStream = null;
        try {
            build = new DocumentBuilder(doc);
            // 是否添加二维码
            if (isUseQrcode && StringUtil.isNotBlank(qrcodeText)) {
                // 添加二维码 (读取epointframe.properties的配置)
                int qcodeWidth = GenerateUtil.parseInteger(ConfigUtil.getConfigValue("cert_qrcode_width"), 100);
                int qcodeHeight = GenerateUtil.parseInteger(ConfigUtil.getConfigValue("cert_qrcode_height"), 100);
                qrCodeInputStream = QrcodeUtil.getQrCode(qrcodeText, qcodeWidth,qcodeHeight);
                // 书签存在
                if (qrCodeInputStream != null && getBookmark(doc, "二维码") != null) {
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
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            finally {
                                if (content != null) {
                                    content.close();
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (qrCodeInputStream != null) {
                    qrCodeInputStream.close();
                }
            }
            catch (IOException e) {
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
                    return bookmark;
                }
            }
        }
        return null;
    }
    

    
   
    /**
     * 检测目录模板文件
     *
     * @param cliengGuid
     *            模板文件（正/副本）guid
     * @param isGenerateCopy
     *            是否生成副本
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
            returnMap.put("msg", String.format("未正确配置证照目录模版文件（%s）！", tip));
            return returnMap;
        }

        // 模版文件校验（正副本）
        List<FrameAttachStorage> frameAttachStorageList = attachService.getAttachListByGuid(cliengGuid);
        log.info("打印证照模板对应的frameAttachStorageList："+frameAttachStorageList);
        if (ValidateUtil.isBlankCollection(frameAttachStorageList)) {
            returnMap.put("msg", String.format("当前证照对应的证照目录模版文件（%s）为空，请联系管理员维护！", tip));
            return returnMap;
        }
        FrameAttachStorage frameAttachStorage = frameAttachStorageList.get(0);
        if (StringUtil.isBlank(frameAttachStorage.getContent())) {
            returnMap.put("msg", String.format("当前证照对应的证照目录模版文件（%s）内容为空，请联系管理员维护！", tip));
            return returnMap;
        }
        returnMap.put("frameAttachStorage", frameAttachStorage);
        return returnMap;
    }


    public void putCert() {
        if(certinfo!=null && StringUtil.isNotBlank(certinfo.getCertcliengguid())){
            if(StringUtil.isBlank(getViewData("rowguid"))){
                addCallbackParam("msg", "请先保存证照信息！");
                return;
            }
            markFaFang(certinfo.getCertcliengguid());
            addCallbackParam("msg", "ok");
        }else{
            addCallbackParam("msg", "请先生成证照");
        }
    }

    /**
     *
     * 发放审批结果
     *
     */
    public void markFaFang(String cliengguid) {
    	String taskguid = getRequestParameter("taskguid");
    	AuditTaskResult auditResult;
    	if (StringUtil.isNotBlank(taskguid)) {
    		  auditResult = auditTaskResultService.getAuditResultByTaskGuid(taskguid, true).getResult();
    	}else {
    		  auditResult = auditTaskResultService.getAuditResultByTaskGuid(auditProject.getTaskguid(), true).getResult();
    	}


        //自建系统办件在发放前自动暂停
        if("1".equals(auditProject.getIs_pause())){
            handleProjectService.handleRecover(auditProject, userSession.getDisplayName(), userSession.getUserGuid(), null, null);
        }

        //生成审批结果
        attachService.copyAttachByClientGuid(cliengguid, null, null, getRequestParameter("projectguid"));

        // 1、更新办件发证信息
        auditProject.setIs_cert(ZwfwConstant.CONSTANT_INT_ONE);
        auditProject.setCertificatedate(new Date());
        auditProject.setCertificateuserguid(userSession.getUserGuid());
        auditProject.setCertificateusername(userSession.getDisplayName());
        auditProjectService.updateProject(auditProject);
        //更新证照信息，草稿状态变更为正式状态
        certinfo.setStatus(ZwfwConstant.CERT_STATUS_COMMON);
//        if (certCatalog.getIsoneCertInfo() == ZwfwConstant.CONSTANT_INT_ZERO) {//TODO
//         // 不允许多实例，通过持有人证件号码，目录唯一标识，持有人类型来判断
//            SqlUtils sql = new SqlUtils();
//            sql.eq("ishistory", ZwfwConstant.CONSTANT_STR_ZERO);
//            sql.eq("status", ZwfwConstant.CERT_STATUS_COMMON);
//            sql.eq("certcatalogid", certCatalog.getCertcatalogid());
//            sql.eq("certownerno",certinfo.getCertownerno());
//            sql.eq("certownertype",certinfo.getCertownertype());
//            List<CertInfo> readycertinfos= iCertInfo.getListByCondition(sql.getMap());
//            if(readycertinfos!=null&&readycertinfos.size()>0){
//                CertInfo readycertinfo=readycertinfos.get(0);
//                // 更新实体信息。生成版本，前面版本设置为历史版本
//                certinfo.setCertid(readycertinfo.getCertid());
//                certinfo.setVersion(readycertinfo.getVersion() + 1);
//                certinfo.setIshistory(ZwfwConstant.CONSTANT_INT_ZERO);
//                // 上一个版本变更为历史
//                readycertinfo.setIshistory(ZwfwConstant.CONSTANT_INT_ONE);
//                iCertInfoExternal.submitCertInfo(readycertinfo,dataBean,certinfo.getCertcliengguid(),ZwfwConstant.Material_ZZ,areacode);
//            }
//        }
        iCertInfoExternal.updateCert(areacode,certinfo.getRowguid(),certinfo.getCertcliengguid(),certinfo.getCopycertcliengguid());
        //如果是在主题内进行共享的，那么还需要更新主题中的结果状态
        if(StringUtil.isNotBlank(auditProject.getBiguid())&&StringUtil.isNotBlank(auditProject.getSubappguid()))
        {
            AuditSpShareMaterialRelation auditSpShareMaterialRelation = relationService.getRelationByID(auditProject.getBusinessguid(), auditResult.getRowguid()).getResult();
            if(auditSpShareMaterialRelation!=null){
                handleSpiMaterialService.updateResultStatusAndAttachAndCert(auditProject.getSubappguid(),auditSpShareMaterialRelation.getSharematerialguid(),10,getRequestParameter("projectguid"),certinfo.getRowguid());
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("biguid", auditProject.getBiguid());
                List<AuditProject> auditProjects = auditProjectService.getAuditProjectListByCondition(sql.getMap()).getResult();
                if(auditProjects.size()>0){
                    for (AuditProject auditproject : auditProjects) {
                        List<AuditProjectMaterial> projectMaterials = projectMaterialService
                                .selectProjectMaterial(auditproject.getRowguid()).getResult();
                        for(AuditProjectMaterial auditProjectMaterial : projectMaterials){
                            if(StringUtil.isNotBlank(auditProjectMaterial.getSharematerialiguid())&&auditProjectMaterial.getSharematerialiguid().equals(auditResult.getSharematerialguid())){
                                List<AuditOrgaWindow> windowList =  auditOrgaWindowService.getWindowListByTaskId(auditproject.getTask_id()).getResult();
                                for(AuditOrgaWindow window : windowList){
                                    List<AuditOrgaWindowUser> users = auditOrgaWindowService.getUserByWindow(window.getRowguid()).getResult();
                                    for(AuditOrgaWindowUser user : users){
                                        iOnlineMessageInfoService.insertMessage(UUID.randomUUID().toString(),
                                                UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(),
                                                user.getUserguid(), "", user.getUserguid(),
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
     *  生成基本信息字段map(渲染证照基本信息，根据对象名称)
     *  @param metadata
     *  @param isdetail
     *  @param recordName 实体名称
     *  @return
     */
    public Map<String, Object> generateBasicDefaultMap(List<CertMetadata> certMetadataList, boolean isdetail, String recordName) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> recordList = new ArrayList<Map<String, Object>>();
        List<CodeItems> codeItems = codeItemsService.listCodeItemsByCodeName("证照基本信息字段");

        HashMap<String,String> repeatFieldMap=new HashMap<String,String>();

        for (CertMetadata certMetadata : certMetadataList) {
            if (StringUtil.isNotBlank(certMetadata.getRelatedfield())) {
                repeatFieldMap.put(certMetadata.getRelatedfield(),certMetadata.getFieldname());
            }
        }
        for (CodeItems codeItem : codeItems) {
            Map<String, Object> recordMap = new HashMap<String, Object>();
            if(!isdetail&&repeatFieldMap.containsKey(codeItem.getItemValue())){
                //关联了元数据，填写的值动态刷新上去
                recordMap.put("onvaluechanged", "valuechanged('"+codeItem.getItemValue()+"','"+repeatFieldMap.get(codeItem.getItemValue())+"')");
            }
            //起始和截止日期
            if("expiredatefrom".equals(codeItem.getItemValue()) || "expiredateto".equals(codeItem.getItemValue())
                    || "awarddate".equals(codeItem.getItemValue())){
                recordMap.put("type", "datepicker");
                if("awarddate".equals(codeItem.getItemValue())){
                    //设置必填
                    recordMap.put("required", true);
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
                if(isdetail){
                    recordMap.put("dataOptions", "{'format' : 'yyyy-MM-dd'}");
                }
            }else{
                //文本框类型
                recordMap.put("type", "textbox");
                //设置必填
                recordMap.put("required", true);
                // 设置提示信息
                recordMap.put("requiredErrorText", codeItem.getItemText() + "不能为空!");
            }
            if("certownertype".equals(codeItem.getItemValue())){
                //下拉框类型
                recordMap.put("type", "combobox");
                recordMap.put("dataData", getCodeData("证照持有人类型"));
                recordMap.put("dataOptions", "{'code' : '证照持有人类型'}");
                recordMap.put("onvaluechanged", "certownertypechanged");
            }
            if("certownercerttype".equals(codeItem.getItemValue())){
                //下拉框类型
                recordMap.put("type", "combobox");
                recordMap.put("dataData", getCodeData("申请人用来唯一标识的证照类型"));
                recordMap.put("dataOptions", "{'code' : '申请人用来唯一标识的证照类型'}");
                recordMap.put("action", "getCertOwnerCertTypeModel");
            }

            // 证照编号、持有者姓名、持有者证件号码 长度限制（最大50）
            String itemValue = codeItem.getItemValue();
            if ("certno".equals(itemValue) || "certownername".equals(itemValue)
                    || "certownerno".equals(itemValue)) {
                recordMap.put("vType", "maxLength:50");
            }
            
            if("certownerno".equals(codeItem.getItemValue())){
                recordMap.put("onvalidation", "onCertnumValidation");
            }

            recordMap.put("width", ZwfwConstant.CONSTANT_INT_ONE);
            recordMap.put("bind", recordName + "." + codeItem.getItemValue().toLowerCase());

            // 设置字段名称
            recordMap.put("fieldName", codeItem.getItemValue());
            recordMap.put("label", codeItem.getItemText());
            //详情页面
            if(isdetail){
                recordMap.put("type", "outputtext");
                recordMap.put("required", false);
            }
            recordList.add(recordMap);
        }

        // 查看页面添加下次年检时间
        if (isdetail) {
            Map<String, Object> recordMap = new HashMap<String, Object>();
            //文本框类型
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
     *  实体日期格式转换、错误代码项标注
     *  @param metadataList
     *  @param certInfoExtension
     */
    public void convertDate(List<CertMetadata> metadataList, Record record) {
        if (ValidateUtil.isBlankCollection(metadataList) || record == null) {
            return;
        }

        for (CertMetadata metadata : metadataList) {
            // 展示显示的字段 时间格式YYYY-MM-DD
            if(ZwfwConstant.INPUT_TYPE_DATE_TIME.equals(metadata.getFieldtype())){
                Date fieldDate = record.getDate(metadata.getFieldname());
                if (fieldDate != null) {
                    record.set(metadata.getFieldname(), EpointDateUtil.convertDate2String(fieldDate));
                }
            }
            // 显示错误代码项
            if(StringUtil.isNotBlank(metadata.getDatasource_codename())) {
                String value = record.getStr(metadata.getFieldname());
                if (StringUtil.isNotBlank(value)) {
                    String itemName = codeItemsService.getItemTextByCodeName(metadata
                            .getDatasource_codename(), value);
                    if (StringUtil.isBlank(itemName)) {
                        // bug10823，bug10825
                        /*record.set(metadata.getFieldname(), value + "(" + metadata
                                .getDatasource_codename() + "列表选择错误)");*/
                        record.set(metadata.getFieldname(), value);
                        metadata.setDatasource_codename(null);
                    }
                }
            }
        }
    }
    
    /**
     *  生成控件map数据(使用拓展子信息表)
     *  @param actionName
     *  @param certMetadataList
     *  @param parentMode 父表模式
     *  @param subMode 子表模式
     *  @return
     */
    public Map<String, Object> generateMapUseSubExtension(String actionName, List<CertMetadata> certMetadataList, String parentMode, String subMode) {
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
     *  生成元数据map
     *  @param actionName
     *  @param metadata
     *  @param parentMode 父表模式
     *  @param subMode 子表模式
     *  @return
     */
    public Map<String, Object> generateMetaDataMap(String actionName, CertMetadata metadata, String parentMode, String subMode) {
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
                recordMap.put("onclick", "openAddSublist('" + metadata.getRowguid()
                    + "', '" + metadata.getFieldchinesename() + "', '" + parentguid + "')");
                recordMap.put("iconCls", "icon-edit");
            } else if (ZwfwConstant.SUB_MODE_EDIT.equals(subMode)) { // 修改模式
                recordMap.put("onclick", "openEditSublist('" + metadata.getRowguid()
                    + "', '" + metadata.getFieldchinesename() + "', '" + metadata.getFieldname() + "')");
                recordMap.put("iconCls", "icon-edit");
            } else if (ZwfwConstant.SUB_MODE_DETAIL.equals(subMode)) { // 查看模式
                recordMap.put("onclick", "openDetailSublist('" + metadata.getRowguid()
                    + "', '" + metadata.getFieldchinesename() + "', '" + metadata.getFieldname() + "')");
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
                }else if ("checkbox".equals(metadata.getFielddisplaytype().toLowerCase())) {
                    recordMap.put("type", metadata.getFielddisplaytype());
                }  
                else {
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
                    recordMap.put("onclick", "ondetail('" + metadata.getRowguid()
                        + "', '" + metadata.getFieldchinesename() + "')");
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
            if("Nvarchar".equals(metadata.getFieldtype())){
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
            } else if("Numeric".equals(metadata.getFieldtype())) { //数字类型在表单中校验
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
                    recordMap.put("action", String.format("%s.getFileUploadModel(%s,%s)",
                            actionName, metadata.getFieldname(), UUID.randomUUID().toString()));
                } else {
                    recordMap.put("action", String.format("%s.getEditFileUploadModel(%s)", actionName, metadata.getFieldname()));
                }
                // 附件只支持图片类型
                recordMap.put("limitType", "bmp,gif,jpeg,tiff,psd,png,jpg,jfif,raw");
                recordMap.put("previewExt", "bmp,gif,jpeg,tiff,psd,png,jpg,jfif,raw");
                recordMap.put("editext", "bmp,gif,jpeg,tiff,psd,png,jpg,jfif,raw");
                recordMap.put("fileNumLimit", "1");
                //recordMap.put("fileSingleSizeLimit", "5120");
                //recordMap.put("mimeTypes", "image/*");
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
     *  获取office365服务器地址
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
            }
            else {
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
     *  基本信息附件上传
     *  @return
     */
    public FileUploadModel9 getCertFileUploadModel() {
        if (certinfo != null && certFileUploadModel == null) {
        	if (StringUtil.isNotBlank(certinfo.getCertcliengguid())) {
                certFileUploadModel = this.miniOaUploadAttach(certinfo.getCertcliengguid(), null);
        	}
        }
        return certFileUploadModel;
    }

    /**
     *  基本信息附件上传
     *  @return
     */
    public FileUploadModel9 getCopyCertFileUploadModel() {
        if (certinfo != null && certCopyFileUploadModel == null) {
        	if (StringUtil.isNotBlank(certinfo.getCopycertcliengguid())) {
                certCopyFileUploadModel = this.miniOaUploadAttach(certinfo.getCopycertcliengguid(), null);
        	}
        }
        return certCopyFileUploadModel;
    }
    
    /**
     *  通过mini-oauploader控件上传附件
     *  @param clientGuid
     *  @param clientInfo
     *  @return fileUploadModel
     */
    public FileUploadModel9 miniOaUploadAttach(String clientGuid, String clientInfo) {
        return miniOaUploadAttach(null, clientGuid, ZwfwConstant.CERT_SYS_FLAG, clientInfo, null,
                UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName());
    }


    /**
     *  通过oauploader控件上传附件
     *  @param fileUploadModel
     *  @param clientGuid
     *  @param clientTag
     *  @param clientInfo
     *  @param attachHandler
     *  @param userGuid
     *  @param userName
     */
    public FileUploadModel9 miniOaUploadAttach(FileUploadModel9 fileUploadModel, String clientGuid, String clientTag,
            String clientInfo, AttachHandler9 attachHandler, String userGuid, String userName) {
        if (attachHandler == null) {
            attachHandler = new AttachHandler9()
            {
                private static final long serialVersionUID = -6274679633876738096L;

                @Override
                public void afterSaveAttachToDB(Object arg0) {
                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage arg0) {
                    // TODO Auto-generated method stub
                    return false;
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
}
