package com.epoint.yyyz.cert;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.DataSet;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.cert.JNGenerateCert;
import com.epoint.cert.basic.certarea.domain.CertArea;
import com.epoint.cert.basic.certarea.inter.ICertArea;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.basic.certcatalog.certcatalogou.domain.CertCatalogOu;
import com.epoint.cert.basic.certcatalog.certcatalogou.inter.ICertCatalogOu;
import com.epoint.cert.basic.certcatalog.certmetadata.domain.CertMetadata;
import com.epoint.cert.basic.certcatalog.certmetadata.inter.ICertMetaData;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.certinfosubextension.domain.CertInfoSubExtension;
import com.epoint.cert.basic.certinfo.certinfosubextension.inter.ICertInfoSubExtension;
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
import com.epoint.common.cert.authentication.ConfigBizlogic;
import com.epoint.common.cert.authentication.GenerateBizlogic;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.exception.security.ReadOnlyException;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.reflect.ReflectUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
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
import com.epoint.yyyz.businesslicense.api.IBusinessLicenseResult;
import com.epoint.yyyz.businesslicense.api.entity.BusinessLicenseResult;

/**
 * 证照基本信息表新增页面对应的后台
 *
 * @author dingwei
 * @version [版本号, 2017-11-01 16:01:53]
 */
@RestController("certinfoaddyyyzaction")
@Scope("request")
public class CertInfoAddYyyzAction extends BaseController
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
     * 证照附件（正本）上传
     */
    private FileUploadModel9 certFileUploadModel;

    /**
     * 证照附件（副本）上传
     */
    private FileUploadModel9 copyCertFileUploadModel;

    /**
     * 照面信息附件上传map
     */
    private Map<String, FileUploadModel9> modelMap = new HashMap<>();


    /**
     * 生成证照bizlogic
     */
    private GenerateBizlogic generateBizlogic = new GenerateBizlogic();

    /**
     * 证照信息bizlogic
     */
    private CertInfoBizlogic certInfoBizlogic = new CertInfoBizlogic();

    /**
     * 操作日志bizlogic
     */
    private CertLogBizlogic certLogBizlogic = new CertLogBizlogic();

    /**
     * 证照目录api
     */
    @Autowired
    private ICertCatalog iCertCatalog;

    /**
     * 元数据api
     */
    @Autowired
    private ICertMetaData iCertMetaData;

    /**
     * 证照基本信息api
     */
    @Autowired
    private ICertInfo iCertInfo;

    /**
     * 证照目录关联api
     */
    @Autowired
    private ICertCatalogOu iCertCatalogOu;

    /**
     * 子拓展信息api
     */
    @Autowired
    private ICertInfoSubExtension iCertInfoSubExtension;

    /**
     * 附件操作服务service
     */
    @Autowired
    private IAttachService iAttachService;

    /**
     * 代码项接口
     */
    @Autowired
    private ICodeItemsService codeItemsService;

    /**
     * 系统参数接口
     */
    @Autowired
    private IConfigService configService;

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
     * 部门api
     */
    @Autowired
    private IOuService ouService;

    /**
     * 人员api
     */
    @Autowired
    private IUserService userService;

    @Autowired
    private ICertArea areaService;

    @Autowired
    private ICertOwnerInfo ownerInfoService;

    /**
     * 日志
     */
    private Logger log = LogUtil.getLog(CertInfoAddYyyzAction.class);

    /**
     * 目录证照模板（副本）文件数量
     */
    private int copyTempletCount = 0;

    /**
     * 证照目录的版本唯一标识(左侧树选择)
     */
    private String certcatalogid;

    /**
     * 是否可以添加
     */
    private boolean canAdd = true;

    /**
     * 是否保存数据
     */
    private boolean isSave = false;

    /**
     * 证照持有人类型（获得目录的）
     */
    private String certOwnerTypeInit;

    /**
     * 当前登录人的ouguid
     */
    private String ouguid;

    private String isenablecertinfoaudit;

    /**
     * 多持有人信息
     */
    private String certownerinfo;

    /**
     * 证书数据
     */
    private String certData;

    /**
     * key数据
     */
    private String keyString;
    /**
     * 行业标识
     */
    public String businessguid;
    /**
     * 证照标识
     */
    public String certrowguid;
    
    
    /**
     * 行业标识下拉列表model
     */
    private List<SelectItem> businessguidModel;

    /**
     * MongoDB\HBase通用service
     */
    private NoSQLSevice noSQLSevice = new NoSQLSevice();
    
    //一业一证行业相关方法
    
    private DataGridModel<AuditSpTask> modelTask = null;
    
    private List<AuditSpTask> spTasks = null;
    
    @Autowired
    private IBusinessLicenseResult businessLicenseResult;
    
    @Autowired
    private IAuditSpTask auditSpTaskService;
    @Autowired
    private IAuditSpBusiness auditSpBusiness;
    
    public DataGridModel<AuditSpTask> getDataGridTask() {
    	spTasks = auditSpTaskService.getAllAuditSpTaskByBusinessGuid(businessguid).getResult();
        // 获得表格对象
        if (modelTask == null) {
            modelTask = new DataGridModel<AuditSpTask>()
            {
                @Override
                public List<AuditSpTask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	AuditSpBusiness business = auditSpBusiness.getAuditSpBusinessByRowguid(businessguid).getResult();
                    if (spTasks != null && spTasks.size() > 0) {
                    	for (AuditSpTask task : spTasks) {
                    		String sql = "select * from BusinessLicense_result where certrowguid=?1 and task_id=?2 ";
                    		BusinessLicenseResult result = businessLicenseResult.find(sql, certinfo.getRowguid(), task.getTaskid());
                    		String cliengguid = "";
                    		if(result == null) {
                    			result = new BusinessLicenseResult();
                    			cliengguid = UUID.randomUUID().toString();
                    			result.setOperatedate(new Date());
                    			result.setOperateusername(userSession.getDisplayName());
                    			result.setRowguid(UUID.randomUUID().toString());
                    			result.setTaskid(task.getTaskid());
                    			result.setCertrowguid(certinfo.getRowguid());
                    			result.setClientguid(cliengguid);
                    			businessLicenseResult.insert(result);
                    		}else {
                    			cliengguid = result.getClientguid();
                    		}
                    		task.set("businessname", business.getBusinessname());
                    		task.set("cliengguid", cliengguid);
						}
                    }
                    this.setRowCount(spTasks.size());
                    return spTasks;
                }
            };
        }
        return modelTask;
    }
    
    @Override
    public void pageLoad() {
        certinfo = new CertInfo();
        certcatalogid = getRequestParameter("guid");
        if (StringUtil.isNotBlank(userSession.getBaseOUGuid())) {
            ouguid = userSession.getBaseOUGuid();
        }
        else {
            ouguid = userSession.getOuGuid();
        }

        if (StringUtil.isNotBlank(certcatalogid)) {
            // 设置持有人类型
            certCatalog = iCertCatalog.getLatestCatalogDetailByEnale(certcatalogid , CertConstant.CONSTANT_INT_ZERO);
            if (certCatalog == null) {
                addCallbackParam("msg", "该证照对应的证照类型不存在！");
                return;
            }else if(CertConstant.CONSTANT_INT_ZERO.equals(certCatalog.getIsenable())){
                addCallbackParam("msg", "该证照对应的证照类型未启用！");
                return;
            }
            // 获得元数据配置表所有顶层节点
            metadataList = iCertMetaData.selectTopDispinListByCertguid(certCatalog.getRowguid());

            if (!isPostback()) {
                // 设置颁证单位
                addCallbackParam("certawarddept", userSession.getOuName());
                addCallbackParam("certawarddeptValue", userSession.getOuGuid());
                if (StringUtil.isNotBlank(certCatalog.getBelongtype())) {
                    if(CertConstant.CERTOWNERTYPE_ZRR.equals(certCatalog.getBelongtype()) || CertConstant.CERTOWNERTYPE_FR.equals(certCatalog.getBelongtype())){
                        String codename = codeItemsService.getItemTextByCodeName("证照持有人类型", certCatalog.getBelongtype());
                        addCallbackParam("codename", codename);
                        certOwnerTypeInit = certCatalog.getBelongtype();
                        addCallbackParam("codevalue", certOwnerTypeInit);
                    }
                }

                // 设置证照附件（正本）guid
                certinfo.setCertcliengguid(UUID.randomUUID().toString());
                addViewData("certcliengguid", certinfo.getCertcliengguid());
                // 设置证照附件（副本）guid
                certinfo.setCopycertcliengguid(UUID.randomUUID().toString());
                addViewData("copycertcliengguid", certinfo.getCopycertcliengguid());
                // 设置rowguid
                certinfo.setRowguid(UUID.randomUUID().toString());
                addViewData("certrowguid", certinfo.getRowguid());

                // 返回渲染的字段(子表新增模式)
                addCallbackParam("controls", certInfoBizlogic.generateMapUseSubExtension("certinfoaddaction",
                        metadataList, CertConstant.MODE_ADD, CertConstant.SUB_MODE_ADD));

                // 设置照面信息关联颁证单位的元数据
                for (CertMetadata certMetadata : metadataList) {
                    if ("certawarddept".equals(certMetadata.getRelatedfield())) {
                        // 如果元数据关联了基本信息中颁证单位字段
                        addCallbackParam("metadatacertawarddept", certMetadata.getFieldname());
                        break;
                    }
                }
                addCallbackParam("basiccontrols",
                        certInfoBizlogic.generateBasicDefaultMap(metadataList, false, "certinfo", certCatalog.getIsmultiowners()));
                // 用作返回证照目录版本唯一标识
                certinfo.setCertcatalogid(certcatalogid);
                addCallbackParam("certcatalogid", certcatalogid);
                if(CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsmultiowners())){
                    addCallbackParam("codetype", getCertOwnerCertTypeModel());
                }
                //是否要对ofd签章
                if(StringUtil.isNotBlank(ConfigUtil.getConfigValue("ofdserviceurl"))){
                    List<FrameAttachInfo> frameAttachInfos = iAttachService.getAttachInfoListByGuid(certCatalog.getTempletcliengguid());
                    if(frameAttachInfos.size()>0 && StringUtil.toLowerCase(frameAttachInfos.get(0).getAttachFileName()).endsWith("ofd")){
                        if(CertConstant.CONSTANT_STR_ONE.equals(ConfigUtil.getConfigValue("ofdautoseal"))){
                            addCallbackParam("ofdautoseal", "1");
                        }
                    }
                }
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
                    certinfo = iCertInfo.getCertInfoByRowguid(getViewData("rowguid"));
                    Map<String, Object> filter = new HashMap<String, Object>();
                    filter.put("certinfoguid", getViewData("rowguid"));
                    dataBean = noSQLSevice.find(CertInfoExtension.class, filter, false);
                }
            }
        }
        // 回传目录模板文件（副本）个数
        if (StringUtil.isNotBlank(certCatalog.getCopytempletcliengguid())) {
            copyTempletCount = iAttachService.getAttachCountByClientGuid(certCatalog.getCopytempletcliengguid());
        }
        addCallbackParam("copyTempletCount", copyTempletCount);

        addCallbackParam("officeweb365url", new ConfigBizlogic().getOfficeConfig());

        isenablecertinfoaudit = configService
                .getFrameConfigValue("IsEnableCertInfoAudit_" + CertUserSession.getInstance().getAreaCode());
        addCallbackParam("isenablecertinfoaudit", isenablecertinfoaudit);
        addCallbackParam("ofd", ConfigUtil.getConfigValue("ofdserviceurl"));
        addCallbackParam("ofdpreviewurl", ConfigUtil.getConfigValue("ofdpreviewurl"));
        addCallbackParam("ismultiowners", certCatalog.getIsmultiowners());
    }

    /**
     * 保存并关闭 存在隐患，微服务时mongodb不回滚
     *
     * @param params
     *            json (key:parentguid value:fieldname)
     * @param cliengguids
     *            json (key:fieldname value:cliengguid)
     * @param issavestr
     *            true保存 false保存并上报
     */
    public void add(String params, String cliengguids, String issavestr, String ouguids) {
        boolean issave = Boolean.valueOf(issavestr);
        // 全台传来的json
        JSONObject jsonObject = JSON.parseObject(params);
        JSONObject cliengguidsObject = JSON.parseObject(cliengguids);

        if(StringUtil.isNotBlank(certinfo.getCertownertype()) && StringUtil.isNotBlank(certCatalog.getBelongtype())){
            if(CertConstant.CERTOWNERTYPE_ZRR.equals(certCatalog.getBelongtype()) || CertConstant.CERTOWNERTYPE_FR.equals(certCatalog.getBelongtype())){
                if(!certinfo.getCertownertype().equals(certCatalog.getBelongtype())){
                    throw new ReadOnlyException("certownertype");
                }
            }
        }

        // 校验证照目录（是否为空、是否启用）
        String catalogMsg = validateCatalog();
        if (StringUtil.isNotBlank(catalogMsg)) {
            addCallbackParam("msg", catalogMsg);
            // 不可添加
            canAdd = false;
            addCallbackParam("canAdd", canAdd);
            return;
        }

        // 照面信息附件非空验证
        String msg = validateImageType(cliengguidsObject);
        if (StringUtil.isNotBlank(msg)) {
            // 不可添加
            canAdd = false;
            addCallbackParam("canAdd", canAdd);
            addCallbackParam("msg", msg);
            return;
        }
        // 写权限控制
        CertCatalogOu catalogOu = iCertCatalogOu.getCertCataLogOuByCatalogid("*", false, ouguid, certcatalogid);
        if (catalogOu == null || !CertConstant.CONSTANT_INT_ONE.equals(catalogOu.getWriteauthority())) {
            addCallbackParam("msg", "该证照类型没有权限新增，保存失败！");
            // 不可添加
            canAdd = false;
            addCallbackParam("canAdd", canAdd);
            return;
        }
        // 证照编码重复，不能新增
        if(!certinfo.getCertno().equals(getViewData("certno"))){
            boolean existCertno = iCertInfo.isExistCertno(certCatalog.getCertcatalogid(), certinfo.getCertno());
            if (existCertno) {
                addCallbackParam("msg", "证照编号重复，保存失败！");
                // 不可添加
                canAdd = false;
                addCallbackParam("canAdd", canAdd);
                return;
            }
            addViewData("certno", certinfo.getCertno());
        }
        // 证照目录是否是多实例数据验证
        if (CertConstant.CONSTANT_INT_ZERO.equals(certCatalog.getIsoneCertInfo())) {
            //TODO 多持有人 暂时用循环处理
            if(CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsmultiowners())){
                JSONArray jsonArray = JSONArray.parseArray(certownerinfo);
                for (Object object : jsonArray) {
                    JSONObject jsonCert = (JSONObject)object;
                    // 不允许多实例，通过持有人证件号码，目录唯一标识，持有人类型来判断
                    SqlUtils sql = new SqlUtils();
                    // 除删除状态下的数据，其余都要进行判断
                    sql.nq("ishistory", CertConstant.CONSTANT_STR_ONE);
                    sql.nq("status", CertConstant.CERT_STATUS_CANCEL);
                    // 排除本身这条记录
                    if (StringUtil.isNotBlank(certinfo.getRowguid())) {
                        sql.nq("rowguid", certinfo.getRowguid());
                    }
                    int oldcertinfocount = iCertInfo.getCertInfoListMultiOwner(sql.getMap(), certCatalog.getCertcatalogid(),
                            FormatUtil.getEncryptSM4ToData(jsonCert.getString("ownerno") , certcatalogid , "")
                            , FormatUtil.getEncryptSM4ToData(jsonCert.getString("ownertype") , certcatalogid , "")).size();
                    if (oldcertinfocount > 0) {
                        addCallbackParam("msg", "该证照类型下同一持证主体代码类型、持证主体代码，只能存在一条证照实例！");
                        // 不可添加
                        canAdd = false;
                        addCallbackParam("canAdd", canAdd);
                        return;
                    }
                }
            }else{
                // 不允许多实例，通过持有人证件号码，目录唯一标识，持有人类型来判断
                SqlUtils sql = new SqlUtils();
                // 除删除状态下的数据，其余都要进行判断
                sql.nq("ishistory", CertConstant.CONSTANT_STR_ONE);
                sql.nq("status", CertConstant.CERT_STATUS_CANCEL);
                // 排除本身这条记录
                if (StringUtil.isNotBlank(certinfo.getRowguid())) {
                    sql.nq("rowguid", certinfo.getRowguid());
                }
                int oldcertinfocount = iCertInfo.getCertInfoListMultiOwner(sql.getMap(), certCatalog.getCertcatalogid(),
                        FormatUtil.getEncryptSM4ToData(certinfo.getCertownerno(),certcatalogid , ""),
                        FormatUtil.getEncryptSM4ToData(certinfo.getCertownercerttype(), certcatalogid , "")).size();
                if (oldcertinfocount > 0) {
                    addCallbackParam("msg", "该证照类型下同一持证主体代码类型、持证主体代码，只能存在一条证照实例！");
                    // 不可添加
                    canAdd = false;
                    addCallbackParam("canAdd", canAdd);
                    return;
                }
            }
        }
        // 校验子表必填
        boolean isSubExtensionValidatePass = true;
        Set<String> subExtensionNameSet = new HashSet<>();
        if (StringUtil.isNotBlank(params)) {
            Set<String> guidList = jsonObject.keySet();
            for (String parentguid : guidList) {
                // 非空父元数据进行校验
                String fieldName = jsonObject.getString(parentguid);
                SqlUtils sqlUtils = new SqlUtils();
                sqlUtils.eq("certguid", certCatalog.getRowguid());
                sqlUtils.eq("fieldname", fieldName);
                List<CertMetadata> parentMetadatas = iCertMetaData.selectListByCondition(sqlUtils.getMap());
                if (ValidateUtil.isNotBlankCollection(parentMetadatas)) {
                    CertMetadata parentMetadata = parentMetadatas.get(0);
                    if (CertConstant.CONSTANT_STR_ONE.equals(parentMetadata.getNotnull())) {
                        int count = iCertInfoSubExtension.getSubExtensionCount(parentguid);
                        if (count == 0) {
                            isSubExtensionValidatePass = false;
                            subExtensionNameSet.add(parentMetadata.getFieldchinesename());
                        }
                    }
                }
            }
        }
        // 子表校验
        if (!isSubExtensionValidatePass) {
            addCallbackParam("msg", String.format("子表%s不能为空，保存失败！", subExtensionNameSet.toString()));
            canAdd = false;
            addCallbackParam("canAdd", canAdd);
            return;
        }
        
        // 判断是否存在附件，有则认为已生成证照
        if (iAttachService.getAttachCountByClientGuid(certinfo.getCertcliengguid()) > 0) {
            certinfo.setIscreatecert(CertConstant.CONSTANT_INT_ONE);
        }
        else {
            certinfo.setIscreatecert(CertConstant.CONSTANT_INT_ZERO);
        }

        // 判断是否存在附件，有则认为已生成证照
        if (iAttachService.getAttachCountByClientGuid(certinfo.getCertcliengguid()) > 0) {
            certinfo.setIscreatecert(CertConstant.CONSTANT_INT_ONE);
        }
        else {
            certinfo.setIscreatecert(CertConstant.CONSTANT_INT_ZERO);
        }
        if(StringUtil.isNotBlank(ouguids)) {
            StringBuilder oucodes = new StringBuilder();
            certinfo.setCertawarddeptguid(ouguids);
            String[] ouguidarr = ouguids.split(";");
            for(int i = 0 ; i < ouguidarr.length ; i++){
                FrameOuExtendInfo ouExtendInfo = ouService.getFrameOuExtendInfo(ouguidarr[i]);
                oucodes.append((StringUtil.isNotBlank(ouExtendInfo.getStr("orgcode"))?ouExtendInfo.getStr("orgcode") : "") + "^");
            }
            oucodes.deleteCharAt(oucodes.length()-1);
            certinfo.setCertawarddeptorgcode(oucodes.toString());
        }
        certinfo.setCertawarddept(certinfo.getCertawarddept().replace(",", "^"));
        if (!issave) { // 点击新增按钮
            if (CertConstant.CONSTANT_STR_ONE.equals(isenablecertinfoaudit)) { // 开启审核
                // 状态更新为待审核
                certinfo.setIshistory(CertConstant.CONSTANT_INT_NEGATIVE_ONE);
                certinfo.setAuditstatus(CertConstant.CERT_CHECK_STATUS_WAIT_CHECK);
            }
            else { // 不开启审核
                certinfo.setIshistory(CertConstant.CONSTANT_INT_ZERO);
                certinfo.setAuditstatus(CertConstant.CERT_CHECK_STATUS_PASS);
            }
        }
        else { // 点击生成证照按钮
            if (CertConstant.CONSTANT_STR_ONE.equals(isenablecertinfoaudit)) { // 开启审核
                // 状态更新为待上报
                certinfo.setIshistory(CertConstant.CONSTANT_INT_NEGATIVE_ONE);
                certinfo.setAuditstatus(CertConstant.CERT_CHECK_STATUS_WAIT_REPORT);
            }
            else { // 不开启审核
                certinfo.setIshistory(CertConstant.CONSTANT_INT_ZERO);
                certinfo.setAuditstatus(CertConstant.CERT_CHECK_STATUS_PASS);
            }
        }

        String rowGuid = certinfo.getRowguid();
        if (StringUtil.isBlank(rowGuid)){
            rowGuid = UUID.randomUUID().toString();
        }
        //多持有人处理
        if(CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsmultiowners())){
            //先删除
            ownerInfoService.deleteOwnerInfoByCertInfoGuid(rowGuid);

            JSONArray jsonArray = JSONArray.parseArray(certownerinfo);
            StringBuilder certownerno = new StringBuilder();
            StringBuilder certownername =  new StringBuilder();
            StringBuilder certownercerttype =  new StringBuilder();
            for (Object object : jsonArray) {
                //新增持有人信息
                JSONObject jsonCert = (JSONObject) object;
                CertOwnerInfo certOwnerInfo = new CertOwnerInfo();
                certOwnerInfo.setRowguid(UUID.randomUUID().toString());
                certOwnerInfo.setCertinfoguid(rowGuid);
                certOwnerInfo.setCertownerno(FormatUtil.getEncryptSM4ToData(jsonCert.getString("ownerno") , certcatalogid , ""));
                certOwnerInfo.setCertownername(FormatUtil.getEncryptSM4ToData(jsonCert.getString("ownername") , certcatalogid , ""));
                certOwnerInfo.setCertownercerttype(jsonCert.getString("ownertype"));
                ownerInfoService.addCertOwnerInfo(certOwnerInfo);

                certownerno.append(jsonCert.getString("ownerno") + "^");
                certownername.append(jsonCert.getString("ownername") + "^");
                certownercerttype.append(jsonCert.getString("ownertype") + "^");
            }

            certownername.deleteCharAt(certownername.length() - 1);
            certownercerttype.deleteCharAt(certownercerttype.length() - 1);
            //设置持有人字段
            certinfo.setCertownerno(certownerno.toString());
            certinfo.setCertownername(certownername.toString());
            certinfo.setCertownercerttype(certownercerttype.toString());
        }

        // 保存或新增
        if (StringUtil.isNotBlank(getViewData("rowguid"))) { // 保存
            // 更新基本信息
            certinfo.setOperatedate(new Date());
            certinfo.setCertownername(FormatUtil.getEncryptSM4ToData(certinfo.getCertownername() , certcatalogid , ""));
            certinfo.setCertownerno(FormatUtil.getEncryptSM4ToData(certinfo.getCertownerno() , certcatalogid , ""));
            iCertInfo.updateCertInfo(certinfo);

            if (dataBean != null && !dataBean.isEmpty()) {
                // 更新照面信息，先删除，再插入，效率低
                try {
                    // 更新操作时间
                    dataBean.setOperatedate(new Date());
                    // 设置子数据
                    setSubExtensionJson(jsonObject, dataBean);
                    noSQLSevice.update(dataBean);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    log.error(String.format("MongoDb更新照面信息 {certinfoguid = %s} 失败!", certinfo.getRowguid()));
                    throw new RuntimeException();
                }
            }
        }
        else { // 新增

            // 新增基本信息
            addViewData("rowguid", rowGuid);
            certinfo.setRowguid(rowGuid);
            certinfo.setOperatedate(new Date());
            // 版本时间
            certinfo.setVersiondate(new Date());
            certinfo.setOperateusername(userSession.getDisplayName());
            certinfo.setCertid(UUID.randomUUID().toString());
//            // 设置部门标识,这里修改成设成orgcode,如果code为空，就设置guid
//            if (StringUtil.isBlank(CertUserSession.getInstance().getOrgcode())) {
//                certinfo.setOuguid(ouguid);
//            }
//            else {
//                certinfo.setOuguid(CertUserSession.getInstance().getOrgcode());
//            }
            certinfo.setOuguid(UserSession.getInstance().getOuGuid());
            // 设置区域标识
            certinfo.setAreacode(CertUserSession.getInstance().getAreaCode());
            // 证照目录编号
            certinfo.setCertcatcode(certCatalog.getCertcatcode());
            // 通用证照目录编号
            certinfo.setTycertcatcode(certCatalog.getTycertcatcode());
            // 证照类型代码
            certinfo.setCertificatetypecode(certCatalog.getCertificatetypecode());

            // 可信等级
            certinfo.setCertlevel(CertConstant.CERT_LEVEL_A);
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
                if (CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIssurvery())) {
                    // 设置下次年检时间
                    Integer monthInterval = certCatalog.getSurveryrate(); // 年检间隔（月）
                    if (monthInterval != null && monthInterval > 0) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.MONTH, monthInterval);
                        certinfo.setInspectiondate(calendar.getTime());
                    }
                }
            }
            // 证照名称
            certinfo.setCertname(certCatalog.getCertname());
            // 版本 从1开始
            certinfo.setVersion(CertConstant.CONSTANT_INT_ONE);

            // 添加人信息
            certinfo.setAddusername(userSession.getDisplayName());
            certinfo.setAdduserguid(userSession.getUserGuid());
            // 证照状态(默认正常)
            certinfo.setStatus(CertConstant.CERT_STATUS_COMMON);

            // 设置为证照类型
            certinfo.setMaterialtype(CertConstant.CONSTANT_STR_ONE);

            // 设置材料来源
            certinfo.setMaterialsource(CertConstant.CONSTANT_STR_ONE);

            // 证照附件标识
            int certAttachCount = 0;
            if (StringUtil.isNotBlank(certinfo.getCertcliengguid())) {
                certAttachCount = iAttachService.getAttachCountByClientGuid(certinfo.getCertcliengguid());
            }
            // 附件不存在，置为空，生成证照标记置为0
            if (certAttachCount == 0) {
                certinfo.setIscreatecert(CertConstant.CONSTANT_INT_ZERO);
            }
            else {
                certinfo.setIscreatecert(CertConstant.CONSTANT_INT_ONE);
            }
            certinfo.setCertawarddeptcode(catalogOu.getCertawarddeptcode());
            // 电子证照编号
            certinfo.setCertinfocode(certInfoBizlogic.generateCertinfoCode(certinfo));
            // 未同步
            certinfo.setIspublish(CertConstant.CONSTANT_INT_ZERO);
            //未开启审核
            if (!CertConstant.CONSTANT_STR_ONE.equals(isenablecertinfoaudit)) {
                //设置证照标识
                certinfo.setCertificateidentifier(certInfoBizlogic.generateIdentifier(certinfo, certCatalog.getCertificatetypecode()));
            }
            certinfo.setCertownername(FormatUtil.getEncryptSM4ToData(certinfo.getCertownername() , certcatalogid , ""));
            certinfo.setCertownerno(FormatUtil.getEncryptSM4ToData(certinfo.getCertownerno() , certcatalogid , ""));
            iCertInfo.addCertInfo(certinfo);

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
                noSQLSevice.insert(dataBean);

            }
            catch (Exception e) {
                e.printStackTrace();
                log.error("MongoDb插入照面信息失败");
                throw new RuntimeException();
            }

            // 不开启审核添加新增日志
            if (!CertConstant.CONSTANT_STR_ONE.equals(isenablecertinfoaudit)) {
                // 添加新增操作日志
                certLogBizlogic.addInternalLog(userSession.getUserGuid(), userSession.getDisplayName(), certinfo,
                        CertConstant.LOG_OPERATETYPE_ADD, "", null);
            }
            else {
                // 添加日志（新增并上报）
                certLogBizlogic.addInternalLog(userSession.getUserGuid(), userSession.getDisplayName(), certinfo,
                        CertConstant.LOG_OPERATETYPE_ADD_REPORT, "", null);
            }
        }
        if (!issave) {
            FrameRole frameRole = roleService.getRoleByRoleField("rolename", "证照审核员");
            if (frameRole != null) {
                String roleguid = frameRole.getRoleGuid();
                // 2、获取该角色的对应的人员
                String ouguid = "";
                CertArea area = areaService.getCertAreaByareacode(CertUserSession.getInstance().getAreaCode());
                if (area != null) {
                    ouguid = area.getOuguid();
                }
                List<FrameUser> listUser = userService.listUserByOuGuid(area.getOuguid(), roleguid, "", "", false, true, false, 3);
                if (listUser != null && listUser.size() > 0) {
                    // 3、发送待办给审核人员
                    for (FrameUser frameUser : listUser) {
                        String title = "【证照审核】" + certinfo.getCertname();
                        // 处理页面
                        String handleurl = "epointcert/certinfo/cert/certinfoaudit?guid=" + certinfo.getRowguid();
                        String messageItemGuid = UUID.randomUUID().toString();
                        messagesService.insertWaitHandleMessage(messageItemGuid, title,
                                IMessagesCenterService.MESSAGETYPE_WAIT, frameUser.getUserGuid(),
                                frameUser.getDisplayName(), userSession.getUserGuid(), userSession.getDisplayName(), "证照审核",
                                handleurl, frameUser.getOuGuid(), "", CertConstant.CONSTANT_INT_ONE, "", "",
                                certinfo.getRowguid(), "", new Date(), "", frameUser.getUserGuid(), "", "");
                    }
                }
            }
            addCallbackParam("msg", "上报成功！");
        }
        else {
            addCallbackParam("msg", "保存成功！");
        }
        // 是否可添加msg
        addCallbackParam("canAdd", canAdd);
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
                List<CertInfoSubExtension> extensionList = iCertInfoSubExtension
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
     * 删除子表(临时表)数据
     *
     * @param params
     *            JSON (key:parentguid value:fieldname)
     */
    public void deleteSubExtension(String params) {
        // 未新增
        if (StringUtil.isBlank(getViewData("rowguid")) && certinfo != null) {
            // 删除证照附件（正/副本）
            if (StringUtil.isNotBlank(certinfo.getCertcliengguid())) {
                iAttachService.deleteAttachByGuid(certinfo.getCertcliengguid());
            }
            if (StringUtil.isNotBlank(certinfo.getCopycertcliengguid())) {
                iAttachService.deleteAttachByGuid(certinfo.getCopycertcliengguid());
            }
        }
        // 删除字表（临时表）数据
        JSONObject jsonObject = JSON.parseObject(params);
        if (jsonObject == null) {
            addCallbackParam("msg", "保存成功！");
            return;
        }
        Set<String> parentguidList = jsonObject.keySet();
        if (ValidateUtil.isNotBlankCollection(parentguidList)) {
            for (String parentguid : parentguidList) {
                // 删除子表
                iCertInfoSubExtension.deleteExtensionByParentguid(parentguid);
            }
        }
        addCallbackParam("msg", "保存成功！");
    }

    /**
     * 生成证照
     *
     * @param params
     *            JSON (key:parentguid value:fieldname)
     * @param cliengguids
     *            JSON (key:fieldname value:cliengguid)
     * @param isGenerateCopy
     *            是否生成副本
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public void generateCert(String parentguidList, String cliengguids, String isGenerateCopy, String ouguids) throws ParseException {
        // 校验证照目录（是否为空、是否启用）
        String catalogMsg = validateCatalog();
        if (StringUtil.isNotBlank(catalogMsg)) {
            addCallbackParam("disenable", catalogMsg);
            return;
        }
        // 模板文件guid
        String cliengGuid = certCatalog.getTempletcliengguid();
        boolean isCopy = false;
        if (CertConstant.CONSTANT_STR_ONE.equals(isGenerateCopy)) { 
            // 生成副本
            cliengGuid = certCatalog.getCopytempletcliengguid();
            isCopy = true;
            // 删除打印附件（副本）
            if (StringUtil.isNotBlank(certinfo.getCopyprintdocguid())) {
                iAttachService.deleteAttachByAttachGuid(certinfo.getCopyprintdocguid());
            }
            // 清空打印附件guid
            certinfo.setCopyprintdocguid(null);
        }
        else {
            // 删除打印附件（正本）
            if (StringUtil.isNotBlank(certinfo.getPrintdocguid())) {
                iAttachService.deleteAttachByAttachGuid(certinfo.getPrintdocguid());
            }
            // 清空打印附件guid
            certinfo.setPrintdocguid(null);
        }

        // 保存证照
        isSave = true;
        add(parentguidList, cliengguids, String.valueOf(isSave), ouguids);
        if (canAdd) {
            // 获取word域名数组和域值数组
            Map<String, Object> wordMap = generateBizlogic.getWordInfo(metadataList, dataBean);
            // word域名数组
            String[] fieldNames = (String[]) wordMap.get("fieldNames");
            // word域值数组
            Object[] values = (Object[]) wordMap.get("values");
            // 图片map
            Map<String, Record> imageMap = (Map<String, Record>) wordMap.get("imageMap");
            // 子表数据
            DataSet dataSet = (DataSet) wordMap.get("dataSet");
            // 生成正/副本
            Map<String, String> returnMap = generateBizlogic.generateLicenseSupportCopy(cliengGuid, isCopy,
                    UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(), fieldNames,
                    values, imageMap, dataSet, certinfo, keyString, certData);
            // 设置提示信息
            addCallbackParam("msg", returnMap.get("msg"));
        }
    }

    /**
     * 打印证照
     * 
     * @param isPrintCopy
     *            是否打印副本
     */
    public void printCert(String isPrintCopy) {
        // 验证目录
        String catalogMsg = validateCatalog();
        if (StringUtil.isNotBlank(catalogMsg)) {
            addCallbackParam("msg", catalogMsg);
            return;
        }
        // 获取证照信息
        if (certinfo == null || certinfo.isEmpty()) {
            addCallbackParam("msg", "该证照不存在！");
            return;
        }
        // 目录校验
        if (certCatalog == null) {
            addCallbackParam("msg", "该证照对应的目录不存在！");
            return;
        }
        // 添加提示信息
        if (copyTempletCount == 0) { 
            // 不支持副本打印
            // 判断是否生成证照
            if (StringUtil.isBlank(certinfo.getCertcliengguid())) {
                addCallbackParam("msg", "请先生成证照");
                return;
            }
            List<FrameAttachInfo> attachInfoList = iAttachService
                    .getAttachInfoListByGuid(certinfo.getCertcliengguid());
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
            String tip = "正本";
            String cliengInfo = "系统生成";
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
                if(info != null && StringUtil.isNotBlank(info.getContentType())){
                    addCallbackParam("contenttype", info.getContentType());
                }
                addCallbackParam("filename", certinfo.getCertname());
                return;
            }
            // 没打印，打印(判断是否有套打模版副本附件)
            //先默认cliengGuid为副本模版
            String cliengGuid=certCatalog.getCopytempletcliengguid();
            //套打模版（副本）cliengguid不为空
            if (StringUtil.isNotBlank(certCatalog.getTdCopytempletcliengguid())) {
                //查询附件数量
                int TdCopyTempletCount = iAttachService
                        .getAttachCountByClientGuid(certCatalog.getTdCopytempletcliengguid());
                if (TdCopyTempletCount > 0) {
                    cliengGuid = certCatalog.getTdCopytempletcliengguid();
                }
            }
            returnMap = generateCertDoc(cliengGuid, true);
        }
        else { 
            // 打印正本
            // 是否打印过（正本）
            if (StringUtil.isNotBlank(certinfo.getPrintdocguid())) { 
                // 已打印
                addCallbackParam("certinfoguid", certinfo.getRowguid());
                addCallbackParam("attachguid", certinfo.getPrintdocguid());
                FrameAttachInfo info = iAttachService.getAttachInfoDetail(certinfo.getPrintdocguid());
                if(info != null && StringUtil.isNotBlank(info.getContentType())){
                    addCallbackParam("contenttype", info.getContentType());
                }
                addCallbackParam("filename", certinfo.getCertname());
                return;
            }
            // 没打印，打印(判段是否有套打模版正本附件)
            //先默认cliengGuid为正本模版
            String cliengGuid = certCatalog.getTempletcliengguid();
            //套打模版正本cliengguid不为空
            if (StringUtil.isNotBlank(certCatalog.getTdTempletcliengguid())) {
                //查询附件数量
                int TdTempletCount = iAttachService.getAttachCountByClientGuid(certCatalog.getTdTempletcliengguid());
                if (TdTempletCount > 0) {
                    cliengGuid = certCatalog.getTdTempletcliengguid();
                }
            }
            returnMap = generateCertDoc(cliengGuid, false);
        }

        if (ValidateUtil.isNotBlankMap(returnMap)) {
            String isSuccess = returnMap.get("issuccess");
            if (CertConstant.CONSTANT_STR_ONE.equals(isSuccess)) { // 打印成功
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
     *
     * @param cliengGuid
     *            证照模板文件（正/副本）guid
     * @param isPrintCopy
     *            是否打印副本
     * @return 返回Map issuccess（1 打印成功, 0 打印失败）
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> generateCertDoc(String cliengGuid, boolean isPrintCopy) {
        Map<String, String> returnMap = null;
        try {
            Map<String, Object> wordMap = generateBizlogic.getWordInfo(metadataList, dataBean);
            // word域名数组
            String[] fieldNames = (String[]) wordMap.get("fieldNames");
            // word域值数组
            Object[] values = (Object[]) wordMap.get("values");
            // 图片map
            Map<String, Record> imageMap = (Map<String, Record>) wordMap.get("imageMap");
            // 子表数据
            DataSet dataSet = (DataSet) wordMap.get("dataSet");
            String certClass = ConfigUtil.getConfigValue("cert_reflectgenratecert");
            if (StringUtil.isNotBlank(certClass) && ReflectUtil.exist(certClass)) {
            	JNGenerateCert generateBizlogic = (JNGenerateCert) ReflectUtil.getObjByClassName(certClass);
            	returnMap = generateBizlogic.generatePrintDocSupportCopy(cliengGuid, isPrintCopy, userSession.getUserGuid(),
            			userSession.getDisplayName(), fieldNames, values, imageMap, dataSet, certinfo);

            }else {
            	returnMap = generateBizlogic.generatePrintDocSupportCopy(cliengGuid, isPrintCopy, userSession.getUserGuid(),
            			userSession.getDisplayName(), fieldNames, values, imageMap, dataSet, certinfo);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return returnMap;
    }

    /**
     * 照面信息附件非空验证
     *
     * @return
     */
    private String validateImageType(JSONObject cliengguidsObject) {
        String msg = "";
        // 照面信息附件非空验证
        if (ValidateUtil.isNotBlankCollection(metadataList) && cliengguidsObject != null
                && !cliengguidsObject.isEmpty()) {
            Set<String> nameSet = new HashSet<>();
            Set<String> keySet = cliengguidsObject.keySet();
            if (ValidateUtil.isNotBlankCollection(keySet)) {
                for (String fieldName : keySet) {
                    String cliengguid = cliengguidsObject.getString(fieldName);
                    if (StringUtil.isNotBlank(cliengguid)) {
                        // 获得对应的元数据
                        CertMetadata imageMetadata = null;
                        for (CertMetadata metadata : metadataList) {
                            if (metadata.getFieldname().equals(fieldName)) {
                                imageMetadata = metadata;
                                break;
                            }
                        }
                        // 附件类型非空验证
                        if (imageMetadata != null && CertConstant.CONSTANT_STR_ONE.equals(imageMetadata.getNotnull())) {
                            int attchCount = iAttachService.getAttachCountByClientGuid(cliengguid);
                            if (attchCount == 0) {
                                nameSet.add(imageMetadata.getFieldchinesename());
                            }
                        }
                    }
                }
                if (ValidateUtil.isNotBlankCollection(nameSet)) {
                    msg = String.format("%s不能为空！", nameSet.toString());
                }
            }
        }

        return msg;
    }

    /**
     * 验证证照目录
     * 
     * @return
     */
    private String validateCatalog() {
        String msg = "";
        if (certCatalog == null) {
            msg = "该证照对应的证照类型不存在！";
        }
        else if (CertConstant.CONSTANT_INT_ZERO.equals(certCatalog.getIsenable())) {
            msg = "当前证照对应的证照类型未启用，操作失败！";
        }
        return msg;
    }

    public void getOFDGuid(){
        List<FrameAttachInfo> frameAttachInfos = iAttachService.getAttachInfoListByGuid(certinfo.getCertcliengguid());
        for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
            if(StringUtil.toLowerCase(frameAttachInfo.getAttachFileName()).endsWith("ofd") && "系统生成".equals(frameAttachInfo.getCliengInfo())){
                addCallbackParam("attachguid", frameAttachInfo.getAttachGuid());
            }
        }
        List<FrameAttachInfo> copyFrameAttachInfos = iAttachService.getAttachInfoListByGuid(certinfo.getCopycertcliengguid());
        for (FrameAttachInfo frameAttachInfo : copyFrameAttachInfos) {
            if (StringUtil.toLowerCase(frameAttachInfo.getAttachFileName()).endsWith("ofd")
                    && "系统生成（副本）".equals(frameAttachInfo.getCliengInfo())) {
                addCallbackParam("copyattachguid", frameAttachInfo.getAttachGuid());
                return;
            }
        }
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
    public List<SelectItem> getBusinessguidModel() {
        if (businessguidModel == null) {
        	businessguidModel = new ArrayList<SelectItem>();
        	SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("BUSINESSTYPE", "3");
            sqlc.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
        	List<AuditSpBusiness> businesslist = auditSpBusiness.getAllAuditSpBusiness(sqlc.getMap()).getResult();
        	for (AuditSpBusiness auditSpBusiness : businesslist) {
        		SelectItem item = new SelectItem();
        		item.setText(auditSpBusiness.getBusinessname());
        		item.setValue(auditSpBusiness.getRowguid());
        		businessguidModel.add(item);
			}
        }
        return this.businessguidModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getCertOwnerCertTypeModel() {
        String certOwnerType = "";
        // 第一次加载页面
        if (StringUtil.isNotBlank(certOwnerTypeInit) && !certOwnerTypeInit.contains(",")) {
            certOwnerType = certOwnerTypeInit;
        }
        else { // 级联刷新
            certOwnerType = certinfo.getCertownertype();
        }
        if (certOwnerCertTypeModel == null) {
            certOwnerCertTypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "申请人用来唯一标识的证照类型", null, false));
            // 自然人去除组织机构代码证、统一社会信用代码
            if ("001".equals(certOwnerType)) {
                certOwnerCertTypeModel.removeIf(selectItem -> {
                    if (selectItem.getValue().toString().startsWith("1")) {
                        return true;
                    }
                    else {
                        return false;
                    }
                });
                // 法人保留组织机构代码证、统一社会信用代码
            }
            else if ("002".equals(certOwnerType)) {
                certOwnerCertTypeModel.removeIf(selectItem -> {
                    if (selectItem.getValue().toString().startsWith("2")) {
                        return true;
                    }
                    else {
                        return false;
                    }
                });
            }
        }
        return this.certOwnerCertTypeModel;
    }

    /**
     * 基本信息附件（正本）上传
     *
     * @return
     */
    public FileUploadModel9 getCertFileUploadModel() {
        if (certCatalog != null && certFileUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object arg0) {
                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attach) {
                    return true;
                }
            };
            certFileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(certinfo.getCertcliengguid(),
                    CertConstant.CERT_SYS_FLAG, null, handler, userSession.getUserGuid(), userSession.getDisplayName()))
            {
                private static final long serialVersionUID = 1L;

                @Override
                public void deleteAttach(String attachGuid) {
                    FrameAttachInfo frameAttachInfo = iAttachService.getAttachInfoDetail(attachGuid);
                    if ("系统生成".equals(frameAttachInfo.getCliengInfo()) && StringUtil.isNotBlank(certinfo.getSltimagecliengguid())) {
                        // 如果删除生成的附件，同步删除缩略图
                        iAttachService.deleteAttachByGuid(certinfo.getSltimagecliengguid());
                    }
                    iAttachService.deleteAttachByAttachGuid(attachGuid);
                }
            };
            certFileUploadModel.setCustomerFilePath(new ConfigBizlogic().getCustomerFilePath());
            certFileUploadModel.setUploadType("file");
        }
        return certFileUploadModel;
    }

    /**
     * 基本信息附件（副本）上传
     *
     * @return
     */
    public FileUploadModel9 getCopyCertFileUploadModel() {
        if (certCatalog != null && copyCertFileUploadModel == null) {
            copyCertFileUploadModel = new AttachBizlogic().miniOaUploadAttach(certinfo.getCopycertcliengguid(), null);
        }
        return copyCertFileUploadModel;
    }

    /**
     * 照面信息附件上传(返回不同的model)
     *
     * @param fieldName
     * @return
     */
    public FileUploadModel9 getFileUploadModel(String fieldName, String cliengguid) {
        FileUploadModel9 uploadModel9 = null;
        if (certCatalog != null && StringUtil.isNotBlank(fieldName) && StringUtil.isNotBlank(cliengguid)) {
            if (modelMap.containsKey(cliengguid)) {
                uploadModel9 = modelMap.get(cliengguid);
            }
            else {
                uploadModel9 = new AttachBizlogic().miniOaUploadAttach(cliengguid, null);
                modelMap.put(cliengguid, uploadModel9);
            }
        }
        return uploadModel9;
    }

    public void getExpireDate(String expiredate){
        if(StringUtil.isNotBlank(expiredate)){
            String[] separators = configService.getFrameConfigValue("AS_DATE_SEPARATOR").split(";");
            for(String separator : separators){
                if(expiredate.contains(separator)){
                    String[] date = expiredate.split(separator);
                    if(date.length > 1){
                        addCallbackParam("expiredatefrom" , date[0]);
                        addCallbackParam("expiredateto" , date[1]);
                    }
                }
            }
        }
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

    public String getCertownerinfo() {
        return certownerinfo;
    }

    public void setCertownerinfo(String certownerinfo) {
        this.certownerinfo = certownerinfo;
    }

    public String getCertData() {
        return certData;
    }

    public void setCertData(String certData) {
        this.certData = certData;
    }

    public String getKeyString() {
        return keyString;
    }

    public void setKeyString(String keyString) {
        this.keyString = keyString;
    }
    
    
    public String getBusinessguid() {
        return businessguid;
    }

    public void setBusinessguid(String businessguid) {
        this.businessguid = businessguid;
    }
    
    public String getCertrowguid() {
        return certrowguid;
    }

    public void setCertrowguid(String certrowguid) {
        this.certrowguid = certrowguid;
    }
}
