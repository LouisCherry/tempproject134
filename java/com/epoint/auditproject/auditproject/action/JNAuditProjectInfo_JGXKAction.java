package com.epoint.auditproject.auditproject.action;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditlogistics.auditlogisticsbasicinfo.domain.AuditLogisticsBasicinfo;
import com.epoint.basic.auditlogistics.auditlogisticsbasicinfo.inter.IAuditLogisticsBasicInfo;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyLegal;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyRegister;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyLegal;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyRegister;
import com.epoint.basic.auditresource.individual.domain.AuditRsIndividualBaseinfo;
import com.epoint.basic.auditresource.individual.inter.IAuditIndividual;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.common.zwfw.workflow.AuditWorkflowBizlogic;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.convert.ConvertUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.jnycsl.utils.HttpUtils;
import com.epoint.jnzwfw.auditproject.auditprojectformjgxk.api.IAuditProjectFormJgxkService;
import com.epoint.jnzwfw.auditproject.auditprojectformjgxk.api.entity.AuditProjectFormJgxk;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkzdanti.api.IAuditProjectFormSgxkzDantiService;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkzdanti.api.entity.AuditProjectFormSgxkzDanti;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.config.WorkflowActivityOperation;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import com.epoint.workflow.service.config.api.IWorkflowActOperationService;
import com.epoint.workflow.service.core.api.IWFInitPageAPI9;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.Base64.Encoder;


/**
 * 办件基本信息工作流页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2016-10-14 11:46:07]
 */
@RestController("jnauditprojectinfojgxkction")
@Scope("request")
public class JNAuditProjectInfo_JGXKAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = -4246072106352276268L;
    /**
     * 办件查询字段
     */
    private String fields = " xiangmubm,xiangmuname,AREAALL,AREABUILD,INVESTMENT,PROJECTCONTENT,PROJECTALLOWEDNO,ACCEPTUSERGUID,rowguid,taskguid,applyername,applyeruserguid,areacode,status,centerguid,applyway,taskcaseguid,pviguid,projectname,applyertype,certnum,certtype,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,address,contactcertnum,legal,flowsn,tasktype,ouname,ouguid,hebingshoulishuliang,remark,windowguid,biguid,banjiedate,is_pause,is_test,if_express,spendtime,subappguid,businessguid,applydate,currentareacode,handleareacode,acceptareacode,legalid,task_id,form_xml";

    private String workitemGuid;

    private String activityGuid;

    private String processVersionInstanceGuid;

    // 数据表名
    private String SQLTableName = "AUDIT_PROJECT";
    /**
     * 办件表实体对象
     */
    private AuditProject auditproject = null;
    /**
     * 事项表实体对象
     */
    private AuditTask audittask = null;
    /**
     * 事项扩展表实体对象
     */
    private AuditTaskExtension auditTaskExtension = null;
    /**
     * 事项标识
     */
    private String taskguid;
    /**
     * 办件流水号
     */
    private String txtflowsn;
    /**
     * 审批结果
     */
    private String lbresult;
    /**
     * 是否显示测试办件字段
     */
    private String showtest = ZwfwConstant.CONSTANT_STR_ZERO;
    /**
     * 证照类型Model
     */
    private List<SelectItem> certtypeModel = null;

    private List<SelectItem> suozaishengfenModel = null;

    private List<SelectItem> suozaichengshiModel = null;

    private List<SelectItem> suozaiquxianModel = null;

    private List<SelectItem> jianshexingzhiModel = null;

    private List<SelectItem> xiangmufenleiModel = null;
    /**
     * 是否测试办件Model
     */
    private List<SelectItem> is_testModel = null;

    /**
     * 是否使用物流Model
     */
    private List<SelectItem> ifexpressModel = null;

    private List<SelectItem> applyertypeModel = null;

    private ProcessVersionInstance pVersionInstance;

    private AuditSpInstance auditSpi;
    private AuditSpBusiness auditBusiness;

    private AuditProjectFormJgxk auditProjectFormJgxk;

    @Autowired
    private IConfigService configServicce;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;

    @Autowired
    private IAuditTaskResult auditResultService;

    @Autowired
    private ICodeItemsService codeItemService;

    @Autowired
    private IHandleConfig handleConfigService;

    @Autowired
    private IWFInitPageAPI9 wfapi;

    @Autowired
    private IWFInstanceAPI9 wfinstance;

    @Autowired
    private IWorkflowActOperationService wfopera;

    // 组合服务
    @Autowired
    private IHandleProject handleProjectService;

    @Autowired
    private IAuditIndividual individualService;

    @Autowired
    private IAuditRsCompanyLegal iAuditRsCompanyLegal;

    @Autowired
    private IAuditRsCompanyBaseinfo companyService;

    @Autowired
    private IAuditRsCompanyRegister rsCompanyRegisterService;

    @Autowired
    private IAuditSpInstance auditSpiService;
    @Autowired
    private IAuditSpBusiness auditSpBusinessService;

    @Autowired
    private IAuditRsItemBaseinfo itemService;

    @Autowired
    private IAuditProjectFormJgxkService iAuditProjectFormJgxkService;

    @Autowired
    private IAuditProjectFormSgxkzDantiService iAuditProjectFormSgxkzDantiService;

    @Autowired
    IAttachService attachService;
    private AuditLogisticsBasicinfo auditLogisticsBasicinfo = null;

    /**
     * 省Model
     */
    private List<SelectItem> provModel = null;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditProjectFormSgxkzDanti> model;

    /**
     * 市Model
     */
    private List<SelectItem> cityModel = null;

    /**
     * 区Model
     */
    private List<SelectItem> countryModel = null;

    @Autowired
    private IAuditLogisticsBasicInfo iAuditLogisticsBasicInfo;

    @Autowired
    private IAuditProjectMaterial projectMaterialService;

    private String ccode = "";

    @Autowired
    private IAuditOrgaArea auditAreaService;

    private String messageItemGuid;
    @Autowired
    private IMessagesCenterService messagesCenterService;

    @Autowired
    private IAuditOrgaServiceCenter auditOrgaServiceCenterService;

    @Autowired
    private IAuditTaskDelegate auditTaskDelegateService;

    @Autowired
    private IWFInstanceAPI9 instanceapi;


    private String gabase64;
    private String attachguid;

    private String attachname;

    @Override
    public void pageLoad() {
        auditProjectFormJgxk = new AuditProjectFormJgxk();
        String str = configServicce.getFrameConfigValue("AS_PROJECT_SELECTFIELDS");
        //联系人是否启用刷卡
        String contactcertcard = configServicce.getFrameConfigValue("AS_PROJECT_CONTACTCERT_CARD");
        addCallbackParam("contactcertcard", contactcertcard);
        if (StringUtil.isNotBlank(str)) {
            fields = str;
        }
        String projectguid = getRequestParameter("projectguid");
        workitemGuid = getRequestParameter("WorkItemGuid");
        processVersionInstanceGuid = getRequestParameter("ProcessVersionInstanceGuid");
        taskguid = getRequestParameter("taskguid");
        setMessageItemGuid(getRequestParameter("MessageItemGuid"));
        String areacode = "";
        if (ZwfwUserSession.getInstance().getCitylevel() != null && Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ)) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        } else {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        if (StringUtil.isNotBlank(areacode)) {
            String templateTaskGuid = auditTaskService.getTemplateTaskGuid(areacode).getResult();
            if (StringUtil.isBlank(templateTaskGuid)) {
                addCallbackParam("noTemp", 1);
            }
        }
        AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid(projectguid, areacode)
                .getResult();
        if (StringUtil.isNotBlank(projectguid) && auditProject == null) {

            addCallbackParam("isExist", 1);

            audittask = new AuditTask();
        } else {
            if (StringUtil.isNotBlank(workitemGuid)) {
                pVersionInstance = wfinstance.getProcessVersionInstance(processVersionInstanceGuid);
                activityGuid = wfinstance.getWorkItem(pVersionInstance, workitemGuid).getActivityGuid();
            }
            // 排队叫号系统传递过来的参数
//            String certNum = getRequestParameter("certNum");
//            String phoneNum = getRequestParameter("phoneNum");

            // 1、获取事项信息
            if (StringUtil.isNotBlank(taskguid)) {
                // TODO 暂时修改一下
                audittask = auditTaskService.getAuditTaskByGuid(taskguid, true).getResult();
                auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(taskguid, true).getResult();
            }
            // 1、获取相关数据中的办件标识的值
            if (StringUtil.isNotBlank(projectguid)) {
                auditproject = auditProjectService
                        .getAuditProjectByRowGuid(fields, projectguid, audittask.getAreacode())
                        .getResult();
            }
            if (StringUtil.isBlank(projectguid) && StringUtil.isBlank(taskguid)
                    && StringUtil.isNotBlank(processVersionInstanceGuid)) {
                auditproject = auditProjectService.getAuditProjectByPVIGuid(fields, processVersionInstanceGuid,
                        ZwfwUserSession.getInstance().getAreaCode()).getResult();
                if (auditproject != null) {
                    audittask = auditTaskService.getAuditTaskByGuid(auditproject.getTaskguid(), true).getResult();
                    projectguid = auditproject.getRowguid();
                    if (audittask != null) {
                        taskguid = audittask.getRowguid();
                        auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(taskguid, true)
                                .getResult();
                    }
                }
            }
            if (audittask != null) {
                if (ZwfwUserSession.getInstance().getCitylevel() != null && ZwfwUserSession.getInstance().getCitylevel().equals(ZwfwConstant.AREA_TYPE_XZJ)
                        || (auditProject != null && StringUtil.isNotBlank(auditProject.getAcceptareacode()))) {
                    String area = "";
                    if (auditProject != null && StringUtil.isNotBlank(auditProject.getAcceptareacode())) {
                        area = auditProject.getAcceptareacode();
                    } else {
                        area = ZwfwUserSession.getInstance().getAreaCode();
                    }
                    AuditTaskDelegate delegate = auditTaskDelegateService
                            .findByTaskIDAndAreacode(audittask.getTask_id(), area).getResult();
                    if (delegate != null) {
                        if (StringUtil.isNotBlank(delegate.getUsecurrentinfo())
                                && "是".equals(delegate.getUsecurrentinfo())) {
                            if (StringUtil.isNotBlank(delegate.getPromise_day())) {
log.info("update promise_day-task:"+audittask.getItem_id()+","+audittask.getPromise_day()+",user:"+userSession.getUserGuid());
                                audittask.setPromise_day(delegate.getPromise_day());
                            log.info("update promise_day-task:"+audittask.getItem_id()+","+audittask.getPromise_day()+",user:"+userSession.getUserGuid());
                            }
                            if (StringUtil.isNotBlank(delegate.getAcceptcondition())) {
                                audittask.setAcceptcondition(delegate.getAcceptcondition());
                            }
                        }
                    }
                }
            }

            if (auditproject != null) {

                @SuppressWarnings("serial")
                List<AuditOrgaArea> auditAreas = auditAreaService.selectAuditAreaList(new HashMap<String, String>(16) {
                    {
                        put("xiaqucode=", auditproject.getAreacode());
                    }
                }).getResult();
                AuditOrgaServiceCenter auditCenter = auditOrgaServiceCenterService
                        .findAuditServiceCenterByGuid(auditproject.getCenterguid()).getResult();
                //中心或辖区被删
                if ((auditAreas != null && auditAreas.size() > 0) && auditCenter != null) {
                    addCallbackParam("isarea", "1");
                } else {
                    addCallbackParam("isarea", "0");
                    return;
                }
                // ***************************************************************

                // 获取事项审批结果信息
                AuditTaskResult auditResult = auditResultService.getAuditResultByTaskGuid(taskguid, true).getResult();
                lbresult = auditResult == null ? "无"
                        : codeItemService.getItemTextByCodeName("审批结果类型", String.valueOf(auditResult.getResulttype()));
                // 发送短信系统参数配置
                // String asissendmessage =
                // frameconfigservice.getFrameConfigValue("AS_IS_SENDMESSAGE");

                if (!isPostback()) {
                    // 根据系统参数判断是否显示[测试办件]选择框
                    String asneedbjtest = handleConfigService
                            .getFrameConfig("AS_NEED_BJ_TEST", ZwfwUserSession.getInstance().getCenterGuid())
                            .getResult();
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(asneedbjtest)) {
                        showtest = ZwfwConstant.CONSTANT_STR_ONE;
                    }

                    if (StringUtil.isNotBlank(activityGuid)) {
                        JSONObject jsonobject = new JSONObject();
                        try {
                            jsonobject.put("activityGuid", activityGuid);
                            jsonobject.put("issingleform", true);
                            addCallbackParam("accessRight",
                                    wfapi.init_getTablePropertyControl(jsonobject.toString()).toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //生成竣工许可实体
                    auditProjectFormJgxk = iAuditProjectFormJgxkService.getRecordBy(projectguid);
                    if (auditProjectFormJgxk == null) {
                        auditProjectFormJgxk = new AuditProjectFormJgxk();
                        auditProjectFormJgxk.setRowguid(UUID.randomUUID().toString());
                        auditProjectFormJgxk.setProjectguid(projectguid);
                        auditProjectFormJgxk.setOperatedate(new Date());
                        auditProjectFormJgxk.setOperateusername(userSession.getDisplayName());
                        String form_xml = auditProject.getStr("form_xml");
                        if (StringUtil.isNotBlank(form_xml)) {
                            parseXmltoEntity(form_xml, projectguid);
                        }
                    }
                }
                // 如果是并联审批的办件，那就需要返回项目信息
                if (StringUtil.isNotBlank(auditproject.getBiguid())) {
                    auditSpi = auditSpiService.getDetailByBIGuid(auditproject.getBiguid()).getResult();
                    auditBusiness = auditSpBusinessService.getAuditSpBusinessByRowguid(auditSpi.getBusinessguid())
                            .getResult();
                    // 如果是建设项目并联审批，需要返回项目名称
                    String xmName = "";
                    String xmNum = "";
                    String certtype = "";
                    String type = ZwfwConstant.CONSTANT_STR_ONE;
                    String handleUrl = "";
                    AuditRsItemBaseinfo arib = itemService.getAuditRsItemBaseinfoByRowguid(auditSpi.getYewuguid())
                            .getResult();
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditBusiness.getBusinesstype())) {
                        xmName = auditSpi.getItemname();
                        xmNum = arib.getItemcode();
                        certtype = arib.getItemlegalcerttype();
                        handleUrl = "epointzwfw/auditsp/auditsphandle/handlebilistviewdetail?guid="
                                + auditproject.getBiguid();
                    } else {
                        xmName = auditBusiness.getBusinessname();
                        type = ZwfwConstant.CONSTANT_STR_ZERO;
                        certtype = ZwfwConstant.CERT_TYPE_ZZJGDMZ;
                        handleUrl = "epointzwfw/auditsp/auditspintegrated/auditspintegrateddetail?guid="
                                + auditproject.getBiguid();
                    }
                    // 这里还需要检测一下材料同步，如果本地材料与共享材料不一致了，需要进行提醒
                    // handleMaterialService.syncShareMaterial(projectguid,
                    // auditproject.getSubappguid());

                    addCallbackParam("xmName", xmName);
                    addCallbackParam("xmNum", xmNum);
                    addCallbackParam("certtype", certtype);
                    addCallbackParam("type", type);
                    addCallbackParam("handleUrl", handleUrl);
                }
                addCallbackParam("projectGuid", projectguid);
                addCallbackParam("biguid", auditproject.getBiguid());
                addCallbackParam("ifexpress", auditTaskExtension.getIf_express());
                addCallbackParam("ifex", auditproject.getIf_express());
                addCallbackParam("pviguid", processVersionInstanceGuid);
                addCallbackParam("status", auditproject.getStatus());
                //是否自建系统
                addCallbackParam("isZiJianXiTong", auditTaskExtension.getIszijianxitong());
                //合并受理
                addCallbackParam("ifhebing", auditTaskExtension.getIs_allowbatchregister());

                addCallbackParam("webapplytype", auditTaskExtension.getWebapplytype());
                if (StringUtil.isNotBlank(auditproject.getIf_express())) {
                    if (auditproject.getIf_express().equals(ZwfwConstant.CONSTANT_STR_ONE)) {
                        String isJSTY = ConfigUtil.getConfigValue("isJSTY");
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                            Map<String, String> conditionMap = new HashMap<String, String>(16);
                            conditionMap.put("projectguid = ", auditproject.getRowguid());
                            List<AuditLogisticsBasicinfo> list = iAuditLogisticsBasicInfo
                                    .getAuditLogisticsBasicinfoList(conditionMap, null, null).getResult();
                            if (list != null && list.size() > 0) {
                                for (AuditLogisticsBasicinfo logisticsBasicinfo : list) {
                                    if (!"1".equals(logisticsBasicinfo.getNet_type())) {
                                        auditLogisticsBasicinfo = logisticsBasicinfo;
                                    }
                                }
                            }
                        } else {
                            auditLogisticsBasicinfo = iAuditLogisticsBasicInfo
                                    .getAuditLogisticsBasicinfoListByFlowsn(projectguid).getResult();
                        }

                    }
                }
                String acode = auditproject.getAreacode();
                ccode = acode.substring(0, 4) + "00";
                if (auditLogisticsBasicinfo == null) {
                    auditLogisticsBasicinfo = new AuditLogisticsBasicinfo();
                    auditLogisticsBasicinfo.setRcv_prov("320000");
                    auditLogisticsBasicinfo.setRcv_city(ccode);
                    if (acode.equals(ccode)) {
                        acode = acode.substring(0, 4) + "01";
                    }
                    auditLogisticsBasicinfo.setRcv_country(acode);
                }
                // 办件流水号显示
                if (auditproject != null) {
                    txtflowsn = StringUtil.isBlank(auditproject.getFlowsn()) ? ""
                            : "（办件编号：" + auditproject.getFlowsn() + "）";
                }

            } else {
                addCallbackParam("isfail", "1");
            }

        }

        String gpyattachguid = UUID.randomUUID().toString();
        addCallbackParam("gpyattachguid", gpyattachguid);
    }

    /**
     * [一句话功能简述]
     * [功能详细描述]
     *
     * @param form_xml
     * @param projectguid
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    public void parseXmltoEntity(String form_xml, String projectguid) {
        Document document;
        try {
            document = DocumentHelper.parseText(form_xml.replace("\\\"", "\""));
            Element rootElement = document.getRootElement();
            List<Element> listAttr = rootElement.elements();//当前节点的所有属性的list
            for (Element attr : listAttr) {//遍历当前节点的所有属性
                String name = attr.getName();//属性名称
                String value = attr.getText();//属性的值
                if ("Length".equals(name)) {
                    auditProjectFormJgxk.set("FactLength", value);
                } else {
                    auditProjectFormJgxk.set(name, value);
                }
            }
            iAuditProjectFormJgxkService.insert(auditProjectFormJgxk);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void deleteDanti(String rowguid) {
        iAuditProjectFormSgxkzDantiService.deleteByGuid(rowguid);
        addCallbackParam("ok", "已删除");
    }

    public void initProject() {
        String cardno = getRequestParameter("cardno");
        String qno = getRequestParameter("qno");
        String taskid = this.getRequestParameter("taskid");
        String delegatetype = this.getRequestParameter("delegatetype");
        AuditTask audittask = auditTaskService.getUseTaskAndExtByTaskid(taskid).getResult();
        if (audittask != null) {
            //事项是否启用
            if (ZwfwConstant.CONSTANT_INT_ZERO == audittask.getIs_enable()) {
                addCallbackParam("isenable", audittask.getIs_enable());
                return;
            }
            //乡镇事项登记页面对市级事项进行权限判断
            if (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                    .parseInt(ZwfwConstant.AREA_TYPE_XQJ)) {
                // 如果是镇村接件
                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                AuditTaskDelegate auditTaskDelegate = auditTaskDelegateService.findByTaskIDAndAreacode(taskid, areacode)
                        .getResult();
                if (ZwfwConstant.CONSTANT_STR_ZERO.equals(auditTaskDelegate.getStatus())) {
                    addCallbackParam("hasDelegate", ZwfwConstant.CONSTANT_STR_ZERO);
                    return;
                }
            }
            auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(audittask.getRowguid(), false)
                    .getResult();
            String formUrl = StringUtil.isNotBlank(auditTaskExtension.getCustomformurl())
                    ? auditTaskExtension.getCustomformurl() : ZwfwConstant.CONSTANT_FORM_URL;
            String taskguid = audittask.getRowguid();
            String cityLevel = ZwfwUserSession.getInstance().getCitylevel();
            String acceptareacode = ZwfwUserSession.getInstance().getAreaCode();
            String projectGuid = handleProjectService
                    .InitProject(taskguid, "", userSession.getDisplayName(), userSession.getUserGuid(),
                            ZwfwUserSession.getInstance().getWindowGuid(),
                            ZwfwUserSession.getInstance().getWindowName(),
                            ZwfwUserSession.getInstance().getCenterGuid(), cardno, qno, acceptareacode, cityLevel, delegatetype)
                    .getResult();
            String url = "";
            if (ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(audittask.getType()))
                    && !ZwfwConstant.JBJMODE_STANDARD.equals(audittask.getJbjmode())) {
                url = formUrl + "?taskguid=" + taskguid + "&projectguid=" + projectGuid + "&taskid=";
            } else {
                url = formUrl + "?processguid=" + audittask.getProcessguid() + "&taskguid=" + taskguid + "&projectguid="
                        + projectGuid + "&taskid=";
            }
            addCallbackParam("url", url);
        }
    }

    /**
     * 根据证照编号获取申请人列表
     *
     * @param query 输入的证照号
     * @return
     */
    public List<SelectItem> searchHistory(String query) {
        if (auditproject == null) {
            return null;
        }
        List<SelectItem> list = new ArrayList<SelectItem>();
        String applyerType = auditproject.getApplyertype().toString();
        String certType = auditproject.getCerttype().toString();
        if (StringUtil.isNotBlank(query)) {
            if (StringUtil.isNotBlank(applyerType)) {
                if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)) {
                    List<AuditRsIndividualBaseinfo> individuallist = individualService
                            .selectIndividualByLikeIDNumber(query).getResult();
                    for (AuditRsIndividualBaseinfo auditIndividual : individuallist) {
                        String str = auditIndividual.getIdnumber() + "（" + auditIndividual.getClientname() + "）";
                        SelectItem selectItem = new SelectItem();
                        selectItem.setText(str);
                        selectItem.setValue(auditIndividual.getIdnumber());
                        list.add(selectItem);
                    }
                }
                // 申请人类型：企业
                else if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyerType)) {
                    List<AuditRsCompanyBaseinfo> companylist = new ArrayList<AuditRsCompanyBaseinfo>();
                    // 组织机构代码证
                    if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(certType)) {
                        companylist = companyService.selectCompanyByLikeOrganCode(query).getResult();
                        for (AuditRsCompanyBaseinfo auditCompany : companylist) {
                            String str = auditCompany.getOrgancode() + "（" + auditCompany.getOrganname() + "）";
                            SelectItem selectItem = new SelectItem();
                            selectItem.setText(str);
                            selectItem.setValue(auditCompany.getOrgancode());
                            list.add(selectItem);
                        }
                    }
                    // 统一社会信用代码
                    else if (ZwfwConstant.CERT_TYPE_TYSHXYDM.equals(certType)) {
                        companylist = companyService.selectCompanyByLikeCreditcode(query).getResult();
                        for (AuditRsCompanyBaseinfo auditCompany : companylist) {
                            String str = auditCompany.getCreditcode() + "（" + auditCompany.getOrganname() + "）";
                            SelectItem selectItem = new SelectItem();
                            selectItem.setText(str);
                            selectItem.setValue(auditCompany.getCreditcode());
                            list.add(selectItem);
                        }
                    }
                    // 工商营业执照
                    else if (ZwfwConstant.CERT_TYPE_GSYYZZ.equals(certType)) {
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.like("businesslicense", query);
                        sql.eq("is_history", "0");
                        sql.setSelectCounts(10);
                        companylist = companyService.selectAuditRsCompanyBaseinfoByCondition(sql.getMap()).getResult();
                        if (companylist != null) {
                            for (AuditRsCompanyBaseinfo auditCompany : companylist) {
                                String str = auditCompany.getBusinesslicense() + "（" + auditCompany.getOrganname()
                                        + "）";
                                SelectItem selectItem = new SelectItem();
                                selectItem.setText(str);
                                selectItem.setValue(auditCompany.getBusinesslicense());
                                list.add(selectItem);
                            }
                        }
                    }

                }
            }
        }
        return list;

    }

    /**
     * 根据申请人名获取申请人列表
     *
     * @param applyername 申请人名称
     * @return
     */
    public List<SelectItem> searchApplyerNameHistory(String applyername) {
        if (auditproject == null) {
            return null;
        }
        List<SelectItem> list = new ArrayList<SelectItem>();
        String applyerType = auditproject.getApplyertype().toString();
        String certType = auditproject.getCerttype().toString();
        if (StringUtil.isNotBlank(applyername)) {
            if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)) {
                List<AuditRsIndividualBaseinfo> individuallist = individualService
                        .selectIndividualByLikeClientName(applyername).getResult();
                for (AuditRsIndividualBaseinfo auditIndividual : individuallist) {
                    String str = auditIndividual.getClientname() + "（" + auditIndividual.getIdnumber() + "）";
                    SelectItem selectItem = new SelectItem();
                    selectItem.setText(str);
                    selectItem.setValue(auditIndividual.getIdnumber());
                    list.add(selectItem);
                }
            }
            // 申请人类型：企业
            else if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyerType)) {
                List<AuditRsCompanyBaseinfo> companylist = new ArrayList<AuditRsCompanyBaseinfo>();
                companylist = companyService.selectCompanyByLikeOrganName(applyername, certType).getResult();
                for (AuditRsCompanyBaseinfo auditCompany : companylist) {

                    String str = "";
                    SelectItem selectItem = new SelectItem();
                    // 证照类型：统一社会信用代码
                    if (ZwfwConstant.CERT_TYPE_TYSHXYDM.equals(certType)) {
                        str = auditCompany.getOrganname() + "（" + (StringUtil.isNotBlank(auditCompany.getCreditcode())
                                ? auditCompany.getCreditcode() : "") + "）";
                        selectItem.setText(str);
                        selectItem.setValue(auditCompany.getCreditcode());
                    } // 证照类型：组织机构代码证
                    else if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(certType)) {
                        str = auditCompany.getOrganname() + "（" + (StringUtil.isNotBlank(auditCompany.getOrgancode())
                                ? auditCompany.getOrgancode() : "") + "）";
                        selectItem.setText(str);
                        selectItem.setValue(auditCompany.getOrgancode());
                    } else if (ZwfwConstant.CERT_TYPE_GSYYZZ.equals(certType)) {
                        str = auditCompany.getOrganname() + "（"
                                + (StringUtil.isNotBlank(auditCompany.getBusinesslicense())
                                ? auditCompany.getBusinesslicense() : "")
                                + "）";
                        selectItem.setText(str);
                        selectItem.setValue(auditCompany.getBusinesslicense());
                    }
                    list.add(selectItem);
                }
            }
        }
        return list;
    }

    /**
     * 根据证照编号获取申请人详细信息
     *
     * @param certnum
     */
    public void selectApplyer(String certnum, String certType) {
        // 申请人类型：个人
        StringBuilder ownerguid = new StringBuilder(""); // 证照所有人标识
        String applyerType = auditproject.getApplyertype().toString();
        if (StringUtil.isBlank(certType)) {
            certType = auditproject.getCerttype().toString();
        }
        if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)) {
            if (StringUtil.isNotBlank(certnum)) {

                AuditRsIndividualBaseinfo auditIndividual = individualService
                        .getAuditRsIndividualBaseinfoByIDNumber(certnum).getResult();
                if (auditIndividual != null) {
                    ownerguid.append(auditIndividual.getPersonid());
                    // 设置办件信息
                    HashMap<String, String> map = new HashMap<String, String>(16);
                    map.put("applyername", auditIndividual.getClientname());
                    map.put("address", auditIndividual.getDeptaddress());
                    map.put("contactperson", auditIndividual.getContactperson());
                    map.put("contactphone", auditIndividual.getContactphone());
                    map.put("contactmobile", auditIndividual.getContactmobile());
                    map.put("contactfax", auditIndividual.getContactfax());
                    map.put("contactpostcode", auditIndividual.getContactpostcode());
                    map.put("contactemail", auditIndividual.getContactemail());
                    map.put("certnum", auditIndividual.getIdnumber());
                    map.put("contactcertnum", auditIndividual.getContactcertnum());
                    addCallbackParam("msg", map);
                }
            }
        }
        // 申请人类型：企业
        else {
            AuditRsCompanyBaseinfo auditCompany = null;
            HashMap<String, String> map = new HashMap<String, String>(16);
            // 组织机构代码证
            if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(certType)) {
                auditCompany = companyService.getCompanyByOneField("organcode", certnum).getResult();
                if (auditCompany != null) {
                    map.put("certnum", auditCompany.getOrgancode());
                    map.put("certtype", ZwfwConstant.CERT_TYPE_ZZJGDMZ);
                } else {
                    auditCompany = companyService.getCompanyByOneField("creditcode", certnum).getResult();
                    map.put("certnum", auditCompany.getCreditcode());
                    map.put("certtype", ZwfwConstant.CERT_TYPE_TYSHXYDM);
                }
            }
            // 营业执照号
            else if (ZwfwConstant.CERT_TYPE_GSYYZZ.equals(certType)) {
                auditCompany = companyService.getCompanyByOneField("businesslicense", certnum).getResult();
                map.put("certnum", auditCompany.getBusinesslicense());
            }
            //统一社会信用代码
            else {
                auditCompany = companyService.getCompanyByOneField("creditcode", certnum).getResult();
                if (auditCompany != null) {
                    map.put("certnum", auditCompany.getCreditcode());
                    map.put("certtype", ZwfwConstant.CERT_TYPE_TYSHXYDM);
                } else {
                    auditCompany = companyService.getCompanyByOneField("organcode", certnum).getResult();
                    map.put("certnum", auditCompany.getOrgancode());
                    map.put("certtype", ZwfwConstant.CERT_TYPE_ZZJGDMZ);
                }
            }
            ownerguid.append(auditCompany.getCompanyid());
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("companyid", auditCompany.getCompanyid());
            sql.eq("is_history", "0");

            map.put("applyername", auditCompany.getOrganname());
            map.put("legal", auditCompany.getOrganlegal());
            map.put("contactcertnum", auditCompany.getContactcertnum());
            map.put("legalid", auditCompany.getOrgalegal_idnumber());
            List<AuditRsCompanyRegister> auditRsCompanyRegisters = rsCompanyRegisterService
                    .selectAuditRsCompanyRegisterByCondition(sql.getMap()).getResult();
            if (auditRsCompanyRegisters != null && auditRsCompanyRegisters.size() > 0) {
                // 设置办件信息
                map.put("address", auditRsCompanyRegisters.get(0).getBusinessaddress());
                map.put("contactphone", auditRsCompanyRegisters.get(0).getContactphone());
            }
            // 返回法人信息
            AuditRsCompanyLegal auditRsCompanyLegal = new AuditRsCompanyLegal();
            List<AuditRsCompanyLegal> list2 = iAuditRsCompanyLegal.selectAuditRsCompanyLegalByCondition(sql.getMap())
                    .getResult();
            if (list2 != null && list2.size() > 0) {
                auditRsCompanyLegal = list2.get(0);
            }
            if (!auditRsCompanyLegal.isEmpty()) {
                map.put("contactperson", auditRsCompanyLegal.getContactperson());
                map.put("contactmobile", auditRsCompanyLegal.getContactmobile());
                map.put("contactfax", auditRsCompanyLegal.getContactfax());
                map.put("contactpostcode", auditRsCompanyLegal.getContactpostcode());
                map.put("contactemail", auditRsCompanyLegal.getContactemail());
            }

            addCallbackParam("msg", map);
        }
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getapplyertypeModel() {
        if (applyertypeModel == null) {
            applyertypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "申请人类型", null, false));
            if (StringUtil.isNotBlank(audittask.getApplyertype())) {
                applyertypeModel.removeIf(a -> !audittask.getApplyertype().contains(a.getValue().toString()));
            }
        }
        return this.applyertypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getCerttypeModel() {
        if (certtypeModel == null && auditproject != null) {
            List<SelectItem> silist = new ArrayList<SelectItem>();
            List<SelectItem> blspsilist = new ArrayList<SelectItem>();
            certtypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "申请人用来唯一标识的证照类型", null, false));
//            Collections.reverse(certtypeModel);
            for (SelectItem selectitem : certtypeModel) {
                if (ZwfwConstant.APPLAYERTYPE_GR.equals(auditproject.getApplyertype().toString())) {
                    // 申请人类型：个人 只显示身份证 2开头为个人，1开头为企业
                    if ("2".equals(selectitem.getValue().toString().substring(0, 1))) {
                        silist.add(selectitem);
                    }
                } else if (ZwfwConstant.APPLAYERTYPE_QY.equals(auditproject.getApplyertype().toString())) {
                    // 申请人类型：企业 不显示身份证
                    if ("1".equals(selectitem.getValue().toString().substring(0, 1))) {
                        silist.add(selectitem);
                    }
                } else if ("30".equals(auditproject.getApplyertype().toString())) {
                    // 申请人类型：企业 不显示身份证
                    if ("3".equals(selectitem.getValue().toString().substring(0, 1))) {
                        silist.add(selectitem);
                    }
                }
            }
            if (!isPostback()) {
                // 如果是并联审批的办件，那就需要返回项目信息
                if (StringUtil.isNotBlank(auditproject.getBiguid())) {
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditBusiness.getBusinesstype())) {

                        AuditSpInstance auditSpi = auditSpiService.getDetailByBIGuid(auditproject.getBiguid())
                                .getResult();

                        AuditRsItemBaseinfo arib = itemService.getAuditRsItemBaseinfoByRowguid(auditSpi.getYewuguid())
                                .getResult();
                        for (SelectItem selectitem : certtypeModel) {
                            if (ZwfwConstant.CERT_TYPE_SFZ.equals(arib.getItemlegalcerttype())) {
                                // 申请人类型：个人 只显示身份证
                                if ("2".equals(selectitem.getValue().toString().substring(0, 1))) {
                                    blspsilist.add(selectitem);
                                }
                            } else {
                                // 申请人类型：企业 不显示身份证
                                if ("1".equals(selectitem.getValue().toString().substring(0, 1))) {
                                    blspsilist.add(selectitem);
                                }
                            }
                        }
                    } else {
                        for (SelectItem selectitem : certtypeModel) {
                            // 申请人类型：企业 不显示身份证
                            if ("1".equals(selectitem.getValue().toString().substring(0, 1))) {
                                blspsilist.add(selectitem);
                            }
                        }
                    }
                }
            }
            if (blspsilist.size() > 0) {
                certtypeModel = blspsilist;
            } else {
                certtypeModel = silist;
            }

            if (ZwfwConstant.APPLAYERTYPE_QY.equals(auditproject.getApplyertype().toString())) {
                Collections.reverse(certtypeModel);
            }
        }
        return this.certtypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getis_testModel() {
        if (is_testModel == null) {
            is_testModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.is_testModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getXiangmufenleiModel() {
        if (xiangmufenleiModel == null) {
            xiangmufenleiModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "项目分类", null, false));
        }
        return this.xiangmufenleiModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getSuozaishengfenModel() {
        if (suozaishengfenModel == null) {
            suozaishengfenModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "所在省份", null, false));
        }
        return this.suozaishengfenModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getSuozaichengshiModel() {
        if (suozaichengshiModel == null) {
            suozaichengshiModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "所在城市", null, false));
        }
        return this.suozaichengshiModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getSuozaiquxianModel() {
        if (suozaiquxianModel == null) {
            suozaiquxianModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "所在区县", null, false));
        }
        return this.suozaiquxianModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getJianshexingzhiModel() {
        if (jianshexingzhiModel == null) {
            jianshexingzhiModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "建设性质", null, false));
        }
        return this.jianshexingzhiModel;
    }

    public void buzheng() {
        auditproject.setStatus(ZwfwConstant.BANJIAN_STATUS_YSL);
        auditProjectService.updateProject(auditproject);
        projectMaterialService.updateAllAuditStatus(auditproject.getRowguid());
        // 修改待办标题
        if (StringUtil.isNotBlank(auditproject.getPviguid())) {
            ProcessVersionInstance pVersionInstance = wfinstance.getProcessVersionInstance(auditproject.getPviguid());
            List<Integer> status = new ArrayList<Integer>();
            status.add(WorkflowKeyNames9.WorkItemStatus_Active);
            status.add(WorkflowKeyNames9.WorkItemStatus_Inactive);
            List<WorkflowWorkItem> workflowWorkItems = wfinstance.getWorkItemListByPVIGuidAndStatus(pVersionInstance,
                    status);
            if (workflowWorkItems != null && workflowWorkItems.size() > 0) {
                for (WorkflowWorkItem workflowWorkItem : workflowWorkItems) {
                    if (WorkflowKeyNames9.WorkItemStatus_Active == workflowWorkItem.getStatus()) {
                        messagesCenterService.updateMessageTitleAndShow(workflowWorkItem.getWaitHandleGuid(),
                                "【已补正】" + auditproject.getProjectname() + "(" + auditproject.getApplyername() + ")",
                                workflowWorkItem.getTransactor());
                    }
                }
            }
        }
    }

    /**
     * 保存并关闭
     */
    public void submit() {
        wfinstance.singlematerialSubmit(pVersionInstance, SQLTableName, auditproject.getRowguid(),
                userSession.getUserGuid(), true);
    }

    public AuditProject getAuditproject() {
        return auditproject;
    }

    public void setAuditproject(AuditProject auditproject) {
        this.auditproject = auditproject;
    }

    public void setAudittask(AuditTask audittask) {
        this.audittask = audittask;
    }

    public AuditTask getAudittask() {
        return audittask;
    }

    public String getLbresult() {
        return lbresult;
    }

    public void setLbresult(String lbresult) {
        this.lbresult = lbresult;
    }

    public AuditLogisticsBasicinfo getAuditLogisticsBasicinfo() {
        return auditLogisticsBasicinfo;
    }

    public void setAuditLogisticsBasicinfo(AuditLogisticsBasicinfo auditLogisticsBasicinfo) {
        this.auditLogisticsBasicinfo = auditLogisticsBasicinfo;
    }

    /**
     * 系统方法，误删 获取按钮打开页面或者默认处理
     *
     * @param operationGuid  操作按钮guid
     * @param transitionGuid 变迁guid
     * @return
     * @throws JSONException
     */
    public String getPageUrlOfOperate(String operationGuid, String transitionGuid) throws JSONException {
        JSONObject jsonobject = new JSONObject();
        try {
            jsonobject.put("workitemguid", workitemGuid);
            jsonobject.put("transitionguid", transitionGuid);
            jsonobject.put("userguid", userSession.getUserGuid());
            jsonobject.put("operationguid", operationGuid);
            jsonobject.put("ishandleendactivity", true);
            jsonobject.put("pviguid", processVersionInstanceGuid);
            if (StringUtil.isNotBlank(getRequestParameter("batchHandleGuid"))) {
                String otherurlparam = "batchHandleGuid=" + getRequestParameter("batchHandleGuid");
                jsonobject.put("otherurlparam", otherurlparam);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 获取操作处理页面或者默认处理
        JSONObject returnobject = JSONObject.parseObject(wfapi.init_getPageUrlOfOperate(jsonobject.toString()));
        WorkflowActivityOperation operation = (StringUtil.isNotBlank(operationGuid))
                ? wfopera.getByOperateGuid(operationGuid) : new WorkflowActivityOperation();
        String message = null;
        String url = null;
        try {

            if (returnobject.containsKey("isdefoperation")) {
                boolean isdefault = ConvertUtil.convertStringToBoolean(returnobject.get("isdefoperation").toString());
                if (isdefault) {
                    return getoperate(jsonobject.toString());
                }
            }
            if (returnobject.containsKey("url")) {
                url = returnobject.get("url").toString();
                if (StringUtil.isNotBlank(url)) {
                    returnobject.put("operationtype", operation.getOperationType());
                    return returnobject.toString();
                }
            }
            if (returnobject.containsKey("message")) {
                message = returnobject.get("message").toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return finaloperate(message, operation);
    }

    /**
     * 系统方法，误删 操作处理页面返回json后回调
     *
     * @param data json数据
     * @return
     */
    public String getoperate(String data) {
        return new AuditWorkflowBizlogic().operate(data, UserSession.getInstance().getUserGuid(), auditproject);
    }

    /**
     * 系统方法，误删 返回前台关闭页面或者输出信息
     *
     * @param message
     * @return
     */
    private String finaloperate(String message, WorkflowActivityOperation operation) {
        JSONObject returnjsonobject = new JSONObject();
        try {
            returnjsonobject.put("operationtype", operation.getOperationType());
            returnjsonobject.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return returnjsonobject.toString();
    }

    /**
     * 系统方法，误删 解锁
     *
     * @param tag "norm"：点击按钮解锁 其他：关闭框口解锁
     * @return
     */
    public void workItem_Unlock(String pviguid, String workitemguid, String tag) {
        String msg = "";
        ProcessVersionInstance pvi = wfinstance
                .getProcessVersionInstance(getRequestParameter("ProcessVersionInstanceGuid"));

        if (pvi != null) {
            if (StringUtil.isBlank(tag)) {
                WorkflowWorkItem wi = instanceapi.getWorkItem(pvi, workitemGuid);
                if (wi != null && userSession.getUserGuid().equals(wi.getLockerUserGuid())) {
                    instanceapi.workItemUnlock(pvi, workitemGuid);
                }
            } else {
                instanceapi.workItemUnlock(pvi, workitemGuid);
                // 这个解锁成功与否并没有什么意义，先去掉 edit by jxw 2018/1/4 
                if ("norm".equals(tag)) {
                    msg = "refresh";
                }
            }
        }
        addCallbackParam("msg", msg);
    }

    public String getShowtest() {
        return showtest;
    }

    public void setShowtest(String showtest) {
        this.showtest = showtest;
    }

    public String getTxtflowsn() {
        return txtflowsn;
    }

    public void setTxtflowsn(String txtflowsn) {
        this.txtflowsn = txtflowsn;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getIfexpressModel() {
        if (ifexpressModel == null) {
            ifexpressModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return ifexpressModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getProvModel() {
        if (provModel == null) {
            List<SelectItem> silist = new ArrayList<SelectItem>();
            provModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "行政区划国标", null, false));
            for (SelectItem selectitem : provModel) {
                if (selectitem.getValue().toString().endsWith("0000")) {
                    silist.add(selectitem);
                }
            }
            provModel = silist;
        }
        return this.provModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getCityModel() {
        String filter = this.getRequestParameter("filter");
        if (StringUtil.isBlank(filter)) {
            filter = "320000";
        }
        if (cityModel == null) {
            List<SelectItem> silist = new ArrayList<SelectItem>();
            cityModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "行政区划国标", null, false));
            if (StringUtil.isNotBlank(filter)) {
                for (SelectItem selectitem : cityModel) {
                    if (!selectitem.getValue().toString().equals(filter)
                            && selectitem.getValue().toString().startsWith(filter.substring(0, 2))
                            && selectitem.getValue().toString().endsWith("00")) {
                        silist.add(selectitem);
                    }
                }
                cityModel = silist;
            }

        }
        return cityModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getCountryModel() {
        String filter = this.getRequestParameter("filter");
        if (StringUtil.isBlank(filter)) {
            filter = ccode;
        }
        if (countryModel == null) {
            List<SelectItem> silist = new ArrayList<SelectItem>();
            countryModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "行政区划国标", null, false));
            if (StringUtil.isNotBlank(filter)) {
                for (SelectItem selectitem : countryModel) {
                    if (!selectitem.getValue().toString().equals(filter)
                            && selectitem.getValue().toString().startsWith(filter.substring(0, 4))) {
                        silist.add(selectitem);
                    }
                }
                countryModel = silist;
            }

        }
        return countryModel;
    }

    public void getgongandetail(String certnum, String name) throws IOException, DocumentException {
        Map<String, String> headerRequest = new HashMap<String, String>();
        headerRequest.put("ApiKey", "560862313637216256");
        Map<String, String> bodyRequest = new HashMap<String, String>();
        bodyRequest.put("password", "dzzw1637");
        bodyRequest.put("username", "dzzw");
        bodyRequest.put("servicetype", "jnrk");
        bodyRequest.put("procedure_name", "jnszwptrk_ck");
        String str = HttpUtils.getHttp("http://59.206.96.178:9960/gateway/api/1/jnsgaj_jmsfzxx1?username=dzzw&password=dzzw1637&servicetype=jnrk&procedure_name=jnszwptrk_ck&mmm=idcard%7CVarChar%7C" + certnum + "-name%7CVarChar%7C" + URLEncoder.encode(name, "utf-8"), bodyRequest, headerRequest);
        JSONObject responseJsonObj = new JSONObject();
        responseJsonObj = (JSONObject) JSONObject.parse(str);
        if (responseJsonObj != null && "200".equals(responseJsonObj.getString("code"))) {
            String data = responseJsonObj.getString("data");
            String data1 = data.replaceAll("\r\n", "");
            String data2 = data1.replaceAll("\\\\", "");
            Document document = DocumentHelper.parseText(data2);
            Element rootElement = document.getRootElement();
            Element detailElement = rootElement.element("t");
            String name1 = detailElement.elementText("CNAME");
            String SEX = detailElement.elementText("SEX");
            String NATION = detailElement.elementText("NATION");
            String ADDR = detailElement.elementText("ADDR");
            String BIRTHDAY = detailElement.elementText("BIRTHDAY");
            addCallbackParam("name", name1);
            addCallbackParam("SEX", SEX);
            addCallbackParam("NATION", NATION);
            addCallbackParam("ADDR", ADDR);
            addCallbackParam("BIRTHDAY", BIRTHDAY);


        } else {
            String msg = "请求数据失败，请检查申请人身份证编号是否填写正确";
            addCallbackParam("msg", msg);
        }
    }


    public void getgonganphoto(String certnum) throws IOException, DocumentException {
        Map<String, String> headerRequest = new HashMap<String, String>();
        headerRequest.put("ApiKey", "559771001471107072");
        Map<String, String> bodyRequest = new HashMap<String, String>();
        bodyRequest.put("password", "dzzw1637");
        bodyRequest.put("username", "dzzw");
        bodyRequest.put("servicetype", "jnrk");
        bodyRequest.put("procedure_name", "t_huji_photo_by_idcard");
        bodyRequest.put("mmm", "idcard%7CVarChar%7C" + certnum);
        String str = HttpUtils.getHttp("http://59.206.96.178:9960/gateway/api/1/jnsgaj_czrkzpxx?username=dzzw&password=dzzw1637&servicetype=jnrk&procedure_name=t_huji_photo_by_idcard&mmm=idcard%7CVarChar%7C" + certnum, bodyRequest, headerRequest);
        JSONObject responseJsonObj = new JSONObject();
        responseJsonObj = (JSONObject) JSONObject.parse(str);
        if (responseJsonObj != null && "200".equals(responseJsonObj.getString("code"))) {
            String data = responseJsonObj.getString("data");
            String data1 = data.replaceAll("\r\n", "");
            String data2 = data1.replaceAll("\\\\", "");
            Document document = DocumentHelper.parseText(data2);
            Element rootElement = document.getRootElement();
            Element detailElement = rootElement.element("t");
            if (StringUtil.isNotBlank(detailElement)) {
                String imagetype = detailElement.elementText("imagetype");
                String photo = detailElement.elementText("photo");
                String msg = uploadImageXy(photo, imagetype);
                if ("0".equals(msg)) {
                    addCallbackParam("msg", "获取公安照片异常，请检查申请人证照是否填写正确");
                } else {

                    List<FrameAttachInfo> list = attachService.getAttachInfoListByGuid(getRequestParameter("projectguid"));
                    attachguid = list.get(0).getAttachGuid();
                    attachname = list.get(0).getAttachFileName();
                    addCallbackParam("attachguid", attachguid);
                    addCallbackParam("attachname", attachname);

                }
            } else {
                addCallbackParam("msg", "不是省内身份证获取照片失败");
            }
        } else {
            String msg = "请求数据失败，请检查申请人身份证编号是否填写正确";
            addCallbackParam("msg", msg);
        }
    }


    public String uploadImageXy(String base64, String imagetype) {

        byte[] bt = Base64Util.decodeBuffer(base64);
        try {
            String attachguid = UUID.randomUUID().toString();
            FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
            frameAttachInfo.setAttachGuid(attachguid);
            frameAttachInfo.setCliengGuid(getRequestParameter("projectguid"));
            frameAttachInfo.setAttachFileName("身份证照片." + imagetype);
            frameAttachInfo.setCliengTag(getRequestParameter("projectguid").substring(0, 1));
            frameAttachInfo.setUploadUserGuid(userSession.getUserGuid());
            frameAttachInfo.setUploadUserDisplayName(userSession.getDisplayName());
            frameAttachInfo.setUploadDateTime(new Date());
            frameAttachInfo.setContentType("application/octet-stream");
            InputStream in = new ByteArrayInputStream(bt);
            frameAttachInfo.setAttachLength((long) bt.length);
            attachService.addAttach(frameAttachInfo, in);

        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }


        return "1";
    }

    public void photoTognphoto(String gaattachguid, String gpyattachguid) {
        FrameAttachInfo frameAttachInfo = attachService.getAttachInfoDetail(gaattachguid);
        FrameAttachInfo frameAttachInfo1 = attachService.getAttachInfoDetail(gpyattachguid);
        String gapathfile = frameAttachInfo.getFilePath() + frameAttachInfo.getAttachFileName();
        String gpypathfile = frameAttachInfo1.getFilePath() + frameAttachInfo1.getAttachFileName();
        String gabase64 = fileToBase64(gapathfile);
        String gpybase64 = fileToBase64(gpypathfile);
        String url = "http://59.206.96.143:25965/Match.ashx";
        String requestBodyParams = "{" + "\"image1\":" + "\"" + gabase64 + "\"," + "\"image2\":" + "\"" + gpybase64 + "\"}";
        try {
            String str = HttpUtils.postHttp(url, requestBodyParams);
            JSONObject responseJsonObj = new JSONObject();
            responseJsonObj = (JSONObject) JSONObject.parse(str);
            String Success = responseJsonObj.getString("Success");
            if ("true".equals(Success)) {
                String SimilScore = responseJsonObj.getString("SimilScore");
                addCallbackParam("SimilScore", SimilScore);
            } else {
                String ErrorMsg = responseJsonObj.getString("ErrorMsg");
                addCallbackParam("ErrorMsg", ErrorMsg);
            }
        } catch (IOException e) {
            addCallbackParam("ErrorMsg", "调取异常，联系管理员");
            e.printStackTrace();
        }

    }

    public static String fileToBase64(String path) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(path);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }

    public List<FrameAttachInfo> getAttachList(String cliengGuid) {
        List<FrameAttachInfo> list = attachService.getAttachInfoListByGuid(cliengGuid);
        return list;
    }

    public String getAttachguid() {
        return attachguid;
    }

    public void setAttachguid(String attachguid) {
        this.attachguid = attachguid;
    }

    public String getAttachname() {
        return attachname;
    }

    public void setAttachname(String attachname) {
        this.attachname = attachname;
    }

    public String getMessageItemGuid() {
        return messageItemGuid;
    }

    public void setMessageItemGuid(String messageItemGuid) {
        this.messageItemGuid = messageItemGuid;
    }

    public String getGabase64() {
        return gabase64;
    }

    public void setGabase64(String gabase64) {
        this.gabase64 = gabase64;
    }

    public AuditProjectFormJgxk getAuditProjectFormJgxk() {
        return auditProjectFormJgxk;
    }

    public void setAuditProjectFormJgxk(AuditProjectFormJgxk auditProjectFormJgxk) {
        this.auditProjectFormJgxk = auditProjectFormJgxk;
    }

}
