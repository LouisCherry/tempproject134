package com.epoint.synctask;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.epoint.audittask.util.SyncCommonUtil;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetask.inter.IAuditSpBasetask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.faq.domain.AuditTaskFaq;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.basic.audittask.workflow.domain.AuditTaskRiskpoint;
import com.epoint.basic.audittask.workflow.inter.IAuditTaskRiskpoint;
import com.epoint.basic.spgl.domain.SpglDfxmsplcjdsxxxb;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.commonvalue.FWConstants;
import com.epoint.composite.auditqueue.handlecentertask.inter.IHandleCenterTask;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.mq.spgl.api.IJnSpglDfxmsplcjdsxxxb;
import com.epoint.newtranslation.service.SyncToRightNewService;
import com.epoint.workflow.service.common.custom.Activity;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.config.WorkflowProcess;
import com.epoint.workflow.service.common.entity.config.WorkflowProcessVersion;
import com.epoint.workflow.service.common.runtime.WorkflowParameter9;
import com.epoint.workflow.service.config.api.IWorkflowActivityService;
import com.epoint.workflow.service.config.api.IWorkflowProcessService;
import com.epoint.workflow.service.config.api.IWorkflowProcessVersionService;

/**
 * 事项同步服务
 * 
 * @author xbn
 * @version 
 */
public class EpointSyncDone
{

    transient Logger log = LogUtil.getLog(EpointSyncDone.class);

    protected SyncToRightNewService service = new SyncToRightNewService();

    /**
     * 数据库操作DAO
     */
    protected ICommonDao commonDaoFrom;
    protected ICommonDao commonDaoTo;

    private IWorkflowProcessService iWorkflowProcessService = ContainerFactory.getContainInfo()
            .getComponent(IWorkflowProcessService.class);
    private IJnSpglDfxmsplcjdsxxxb ispgldfxmsplcjdsxxxb = ContainerFactory.getContainInfo()
            .getComponent(IJnSpglDfxmsplcjdsxxxb.class);
    private IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
    private IAuditSpBasetask iauditspbasetask = ContainerFactory.getContainInfo().getComponent(IAuditSpBasetask.class);
    private IOuService iouservice = ContainerFactory.getContainInfo().getComponent(IOuService.class);
    private IHandleCenterTask iHandleCenterTask = ContainerFactory.getContainInfo()
            .getComponent(IHandleCenterTask.class);
    private IAuditOrgaServiceCenter iAuditOrgaServiceCenter = ContainerFactory.getContainInfo()
            .getComponent(IAuditOrgaServiceCenter.class);
    private IAuditTaskMaterial iauditTaskMaterial = ContainerFactory.getContainInfo()
            .getComponent(IAuditTaskMaterial.class);
    private IAuditTaskResult auditTaskResult = ContainerFactory.getContainInfo().getComponent(IAuditTaskResult.class);
    private IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);

    private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "qzurl");
    private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "qzusername");
    private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "qzpassword");
    private static String IS_EDITAFTERIMPORT = ConfigUtil.getConfigValue("datasyncjdbc", "is_editafterimport");
    private static String syncareacode = ConfigUtil.getConfigValue("datasyncjdbc", "syncareacode");

    /**
     * 前置库数据源
     */
    private DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);

    public EpointSyncDone() {
        commonDaoFrom = CommonDao.getInstance(dataSourceConfig);
        commonDaoTo = CommonDao.getInstance();
    }

    public ICommonDao getCommonDaoFrom() {
        return commonDaoFrom;
    }

    public ICommonDao getCommonDaoTo() {
        return commonDaoTo;
    }

    /**
     * 新增事项
     * @throws DocumentException 
     */
    public void insertTask(Record right, Record dvOuInfo) throws DocumentException {
        // 事项
        AuditTask audittask = new AuditTask();
        // 事项扩展
        AuditTaskExtension audittaskex = new AuditTaskExtension();
        // 事项的基本信息添加
        audittask.setOperateusername("同步服务");
        audittask.setOperatedate(new Date());
        String taskguid = UUID.randomUUID().toString();
        audittask.setRowguid(taskguid);
        audittask.setIs_history(0);
        audittask.set("new_item_code", right.getStr("ITEM_CODE"));
        // 是否启用
        audittask.setIs_enable(1);
        // 是否为模板事项
        audittask.setIstemplate(0);
        // 事项审核状态
        if ("1".equals(IS_EDITAFTERIMPORT)) {
            audittask.setIs_editafterimport(1);
        }
        else {
            audittask.setIs_editafterimport(4);
        }
        audittask.setTaskname(right.getStr("NAME"));
        audittask.set("INNER_CODE", right.getStr("ITEM_CODE"));
        // 目录编码
        audittask.set("FOLDER_CODE", right.getStr("FOLDER_CODE"));
        // 目录名称
        audittask.set("FOLDER_NAME", right.getStr("FOLDER_NAME"));

        audittask.set("unid", right.getStr("ID"));
        String taskId = UUID.randomUUID().toString();
        audittask.setTask_id(taskId);
        String versionNew = "";
        if (StringUtil.isNotBlank(right.getStr("VERSION"))) {
            audittask.setVersion(right.getStr("VERSION"));
            versionNew = right.getStr("VERSION");
        }
        else {
            audittask.setVersion("1");
            versionNew = "1";
        }
        audittask.setVersiondate(new Date());
        audittask.setTasksource("2");

        // 解析 ITEM_INFO_XML获取TASKCODE
        String itemInfoXml = right.getStr("ITEM_INFO_XML");
        String taskCode = "";
        String qlName = "";
        int promiseDay = 0;// 承诺时限
        int anticipateDay = 0;
        int procedureTime = 0;
        int chargeFlag = 0;
        String isAgencyorgan = "";// 是否中介机构
        String crossScope = "";// 通办范围
        String handleForm = "";// 办理形式
        String payOnline = "";// 是否可网上支付
        String remark = "";// 备注
        String regionName = "";// 区划名称
        String applyerType = "0,1";
        String bmCate = "";// 服务对象
        String isOnline = "";// 是否网上办理
        String acceptTime = "0";
        String sendTime = "0";// 送达期限
        String agentCode = "";// 承办单位
        String agentName = "";// 承办单位名称
        String isRate = "";// 是否提供星级评价
        String runSystem = "";// 运行系统
        String orgProperty = "";// 实施主体性质
        String authorityDivision = "";// 权限划分
        String exerciseContent = "";// 行使内容
        String limitNumber = "";// 数量限制
        String isFront = "";// 是否前置审批
        String isPublic = "";// 是否公示
        String isProcedure = "";// 是否走特别程序
        String isCatalog = "";// 是否内容分类
        String consultOnline = "";// 是否可网上咨询
        String serviceState = "";// 事项服务状态编码
        String operationStatus = "";// 事项操作状态编码
        String xkitemProperty = "";// 行政许可事项属性
        String isFreeTradeZone = "";// 是否自贸区
        String isOnlineYichuang = "";// 是否一窗办理
        String isWeb = "";// 是否支持自助终端办理
        String isTreeLeft = "";// 是否是目录树的叶子
        String catalogcode = "";// 基本编码
        String fundAccount = "";// 行政管理相对人
        String kind = "";// 执行种类编码 强制执行种类 强制措施方式
        String kindname = "";// 措施方式名称
        String TASKHANDLEITEM = "";
        // 投诉方式
        String complainPhone = "";

        if (itemInfoXml.trim().length() > 0) {
            Document document = DocumentHelper.parseText(itemInfoXml);
            Element root = document.getRootElement();
            // 事项名称
            Node xnName = root.selectSingleNode("key[@label='NAME']");
            if (xnName != null) {
                qlName = xnName.getText();
            }

            // 基本编码(短)
            Node xncatalogcode = root.selectSingleNode("key[@label='CATALOGCODE']");
            if (xncatalogcode != null) {
                catalogcode = xncatalogcode.getText();
            }
            // 事项唯一编码(长)
            Node xntaskcode = root.selectSingleNode("key[@label='TASKCODE']");
            if (xntaskcode != null) {
                taskCode = xntaskcode.getText();
            }
            // 法定时限(工作日） BigDecimal
            Node xnanticipate = root.selectSingleNode("key[@label='LAW_TIME']");
            if (xnanticipate != null) {
                anticipateDay = Integer.parseInt(xnanticipate.getText());
            }

            // 承诺时限(工作日) BigDecimal
            Node xnpromisday = root.selectSingleNode("key[@label='AGREE_TIME']");
            if (xnpromisday != null) {
                promiseDay = Integer.parseInt(xnpromisday.getText());
            }

            // 是否提供星级评价[0 否,1 是] String
            Node xnRate = root.selectSingleNode("key[@label='IS_RATE']");
            if (xnRate != null) {
                isRate = xnRate.getText();
            }

            // 是否收费[0 否,1 是] String
            Node xnChargeFlag = root.selectSingleNode("key[@label='IS_CHARGE']");
            if (xnChargeFlag != null) {
                chargeFlag = Integer.parseInt(xnChargeFlag.getText());
            }

            // 承办单位
            Node agent_name = root.selectSingleNode("key[@label='AGENT_NAME']");
            if (agent_name != null) {
                agentName = agent_name.getText();
            }

            // 是否网上办理（网上公示）[0 否1 是]
            Node xnOnLine = root.selectSingleNode("key[@label='IS_ONLINE']");
            if (xnOnLine != null) {
                isOnline = xnOnLine.getText();
            }
            // 是否前置审批[0 否1 是]
            Node xnFront = root.selectSingleNode("key[@label='IS_FRONT']");
            if (xnFront != null) {
                isFront = xnFront.getText();
            }
            // 是否公示[0 否，1 是]
            Node xnPublic = root.selectSingleNode("key[@label='IS_PUBLIC']");
            if (xnPublic != null) {
                isPublic = xnPublic.getText();
            }
            // 是否走特别程序[0 否，1 是]
            Node xnProcedure = root.selectSingleNode("key[@label='IS_PROCEDURE']");
            if (xnProcedure != null) {
                isProcedure = xnProcedure.getText();
            }

            // 特别程序实施总期限 BigDecimal
            Node xnProcedureTime = root.selectSingleNode("key[@label='PROCEDURE_TIME']");
            if (xnProcedureTime != null) {
                procedureTime = Integer.parseInt(xnProcedureTime.getText());
            }

            // 是否内容分类(0 否，1 是)默认0
            Node xnCatalog = root.selectSingleNode("key[@label='IS_CATALOG']");
            if (xnCatalog != null) {
                isCatalog = xnCatalog.getText();
            }

            // 是否可网上缴费 --该字段不再作为是否网上缴费
//            Node xnPayOnline = root.selectSingleNode("key[@label='PAY_ONLINE']");
//            if (xnPayOnline != null) {
//                payOnline = xnPayOnline.getText();
//            }
            // 是否可网上咨询
            Node xnConsultOnline = root.selectSingleNode("key[@label='CONSULT_ONLINE']");
            if (xnConsultOnline != null) {
                consultOnline = xnConsultOnline.getText();
            }
            // 事项服务状态编码
            Node xnState = root.selectSingleNode("key[@label='STATE']");
            if (xnState != null) {
                serviceState = xnState.getText();
            }
            // 事项操作状态编码
            Node xnStatus = root.selectSingleNode("key[@label='STATUS']");
            if (xnStatus != null) {
                operationStatus = xnStatus.getText();
            }

            // 服务对象 2,3,4,5,6,9 1
            Node xnA = root.selectSingleNode("key[@label='SERVICE_OBJECT']");
            if (xnA != null) {
                bmCate = xnA.getText();
            }
            // 行政许可事项属性
            Node xnProperty = root.selectSingleNode("key[@label='XK_ITEM_PROPERTY']");
            if (xnProperty != null) {
                xkitemProperty = xnProperty.getText();
            }

            // 是否中介机构
            Node xnAgencyorgan = root.selectSingleNode("key[@label='IS_AGENCYORGAN']");
            if (xnAgencyorgan != null) {
                isAgencyorgan = xnAgencyorgan.getText();
            }

            // 实施主体性质 （1，法定机关 2，授权组织 3，受委托组织）
            Node xnORGPROPERTY = root.selectSingleNode("key[@label='ORG_PROPERTY']");
            if (xnORGPROPERTY != null) {
                orgProperty = xnORGPROPERTY.getText();
            }

            // 通办范围 1 全国 2 全省 3 全市 4 全县 5 全镇（乡、街道） 6 跨村（社区）
            Node xn6 = root.selectSingleNode("key[@label='CROSS_SCOPE']");
            if (xn6 != null) {
                crossScope = xn6.getText();
            }
            // 办理形式 窗口办理 网上办理 快递申请 上门服务
            Node xnHandleForm = root.selectSingleNode("key[@label='HANDLE_FORM']");
            if (xnHandleForm != null) {
                handleForm = xnHandleForm.getText();
            }
            // 权限划分
            Node xnAUTHORITYDIVISION = root.selectSingleNode("key[@label='AUTHORITY_DIVISION']");
            if (xnAUTHORITYDIVISION != null) {
                authorityDivision = xnAUTHORITYDIVISION.getText();
            }
            // 行使内容
            Node xnEXERCISECONTENT = root.selectSingleNode("key[@label='EXERCISE_CONTENT']");
            if (xnEXERCISECONTENT != null) {
                exerciseContent = xnEXERCISECONTENT.getText();
            }
            // 是否自贸区
            Node xnFreeTradeZone = root.selectSingleNode("key[@label='IS_FREETRADEZONE']");
            if (xnFreeTradeZone != null) {
                isFreeTradeZone = xnFreeTradeZone.getText();
            }
            // 是否一窗办理
            Node xnOnlineYichuang = root.selectSingleNode("key[@label='IS_ONLINE_YICHUANG']");
            if (xnOnlineYichuang != null) {
                isOnlineYichuang = xnOnlineYichuang.getText();
            }
            // 是否支持自助终端办理
            Node xnWeb = root.selectSingleNode("key[@label='IS_WEB']");
            if (xnWeb != null) {
                isWeb = xnWeb.getText();
            }
            // 是否是目录树的叶子
            Node xnTreeLeft = root.selectSingleNode("key[@label='IS_TREE_LEAF']");
            if (xnTreeLeft != null) {
                isTreeLeft = xnTreeLeft.getText();
            }

            // 区划名称
            Node xnREGIONNAME = root.selectSingleNode("key[@label='REGION_NAME']");
            if (xnREGIONNAME != null) {
                regionName = xnREGIONNAME.getText();
            }

            // 运行系统 （系统层级 1国家级 2省级3省级部门 4市级 5市级部门 6县级 7县级部门）
            Node xnn = root.selectSingleNode("key[@label='RUN_SYSTEM']");
            if (xnn != null) {
                runSystem = xnn.getText();
            }

            // 数量限制
            Node xnLIMITNUMBER = root.selectSingleNode("key[@label='LIMIT_NUMBER']");
            if (xnLIMITNUMBER != null) {
                limitNumber = xnLIMITNUMBER.getText();
            }
            // 行政管理相对人
            Node xnfundAccount = root.selectSingleNode("key[@label='FUND_ACCOUNT']");
            if (xnfundAccount != null) {
                fundAccount = xnfundAccount.getText();
            }
            // 种类编码 强制执行种类 强制措施方式
            Node xnKIND = root.selectSingleNode("key[@label='KIND']");
            if (xnKIND != null) {
                kind = xnKIND.getText();
            }
            // 种类名称
            Node xnKindname = root.selectSingleNode("key[@label='KIND_NAME']");
            if (xnKindname != null) {
                kindname = xnKindname.getText();
            }
            Node xnTASKHANDLEITEM = root.selectSingleNode("key[@label='TASKHANDLEITEM']");
            if (xnTASKHANDLEITEM != null) {
                TASKHANDLEITEM = xnTASKHANDLEITEM.getText();
            }

            // 投诉方式
            Node COMPLAIN_PHONE = root.selectSingleNode("key[@label='COMPLAIN_PHONE']");
            if (COMPLAIN_PHONE != null) {
                complainPhone = COMPLAIN_PHONE.getText();
            }

        }

        if (StringUtil.isNotBlank(TASKHANDLEITEM)) {
            audittask.setItem_id(TASKHANDLEITEM);
        }
        else {
            audittask.setItem_id(taskCode);
        }

        // 中介服务
        audittaskex.set("IS_AGENCYORGAN", isAgencyorgan);
        // 基本编码
        audittask.setCatalogcode(catalogcode);

        audittaskex.set("COMPLAIN_TYPE", complainPhone);

        // 法定时限单位
//        audittask.setAnticipate_type(LAW_TIME);    
        // 承诺时限单位
//        audittask.setPromise_type(AGREE_TIME);
        // 承诺时限
log.info("update promise_day-task:"+audittask.getItem_id()+","+audittask.getPromise_day());
        audittask.setPromise_day(StringUtil.isBlank(promiseDay) ? 1 : promiseDay);
                            log.info("update promise_day-task:"+audittask.getItem_id()+","+audittask.getPromise_day());
        // 法定时限
        audittask.setAnticipate_day(StringUtil.isBlank(anticipateDay) ? 1 : anticipateDay);
        // 是否收费
        audittask.setCharge_flag(StringUtil.isBlank(chargeFlag) ? 0 : chargeFlag);
        // 是否网上办理
        audittaskex.set("IS_ONLINE", isOnline);
        // 是否特殊程序
        audittaskex.set("IS_PROCEDURE", isProcedure);
        // 事项服务状态编码
        audittask.set("STATE", serviceState);

        // 服务对象，默认为个人法人
        if ("1".equals(bmCate)) {
            audittask.setApplyertype("20");
        }
        else if (bmCate.contains("1") && bmCate.length() > 1) {
            audittask.setApplyertype("10,20");
        }
        else if (!bmCate.contains("1")) {
            audittask.setApplyertype("10");
        }
        else {
            audittask.setApplyertype("10,20");
        }

        // audittask.setQl_dept(dvOuInfo.getStr("OUName"));//受理机构
        // audittask.setDept_ql_id(dvOuInfo.getStr("OUCode"));//审核不通过意见
        // audittask.setDecision_dep(dvOuInfo.getStr("OUName"));//决定机构
        // 辖区
        // audittask.setAreacode("100001");
        String Areacode = right.getStr("REGION_CODE");

        if (dvOuInfo == null) {
            audittask.setOuguid(right.getStr("ORG_CODE"));
            audittask.setOuname(right.getStr("ORG_NAME"));
            audittask.setAreacode(Areacode.substring(0, 6));
        }
        else {
            audittask.setAreacode(dvOuInfo.getStr("AREACODE").substring(0, 6));
            audittask.setOuguid(dvOuInfo.getStr("ouguid"));
            audittask.setOuname(dvOuInfo.getStr("ouname"));
        }
        // 审批类别和审批系统中代码项(审批结果)对应
        switch (right.getStr("TYPE")) {
            case "XK":
                audittask.setShenpilb("01");
                break;
            case "GG":
                audittask.setShenpilb("11");
                break;
            case "QT":
                audittask.setShenpilb("10");
                break;
            case "CF":
                audittask.setShenpilb("02");
                break;
            case "QZ":
                audittask.setShenpilb("03");
                break;
            case "ZS":
                audittask.setShenpilb("04");
                break;
            case "GF":
                audittask.setShenpilb("05");
                break;
            case "JC":
                audittask.setShenpilb("09");
                break;
            case "QR":
                audittask.setShenpilb("07");
                break;
            case "JL":
                audittask.setShenpilb("06");
                break;
            case "CJ":
                audittask.setShenpilb("08");
                break;
            case "ZJJG":
                audittask.setShenpilb("15");
                break;
            default:
                audittask.setShenpilb("10");
                break;
        }

        Integer assort = null;
        if (StringUtil.isBlank(right.getStr("ASSORT"))) {
            // 大汉前置空也是即办件
            assort = 1;
        }
        else {
            switch (right.getStr("ASSORT")) {
                case FWConstants.STRONE:
                    assort = 1;
                    break;
                case FWConstants.STRTWO:
                    assort = 2;
                    break;
                case FWConstants.STRTHREE:
                    assort = 3;
                    break;
                case FWConstants.STRFOUR:
                    assort = 4;
                    break;
                default:
                    assort = 1;
                    break;
            }
        }
        // 办件类型 1.即办 .....
        if (assort == 1) {
            audittask.setJbjmode("1");
        }
        audittask.setType(assort);

        audittask.set("charge_lc", "0");

        audittask.setQlfullid(right.getStr("ITEM_CODE"));
        audittask.setFullid(right.getStr("ITEM_CODE"));

        String itemExtInfoXML = right.getStr("ITME_EXT_INFO_XML");
        // 行政强制事项分类（1、强制措施，2、强制执行）
        String serviceType = "";// 服务类型 依申请服务 1 主动服务 2 依申请和主动服务 1,2
        String offerWay = "";// 提供方式
        String taskType = "";// 事项类别
        String decideNameItem = "";// 联办机构 ORG_DECIDE_EMAIL
        String isReserve = "";// 是否开通网上预约
        String promiseDayNote = "";// 承诺时限描述
        String anticipateDayNote = "";// 法定办结时限说明
        String contactperson = ""; // 填报人
        String contactphone = "";// 填报人联系方式
        String wbyxhj = "2";
        String commomQuestion = "";// 常见问题
        String promiseType = "workday";// 承诺时限单位
        String anticipateType = "workday";// 法定时限单位
        String isInHall = "";// 是否进驻政务大厅
        String applyTimes = "";// 到办事现场次数
        String powerSource = "";// 权力来源
        String isPyc = "";// 是否一次办好（1-是，2-否）
        String isRunsys = "";// 是否还在运维（1-是，2-否）
        String isSelfbuidsystem = "";// 是否自建系统（1-是，2-否）
        String handlingModel = "";// 办理方式（1-自办件，2-初审件，3-联办件，4-转报件
        String isEnclosure = "";// 是否带有附件、附表、附图（1-附件，2-附表，3-附图）
        String resultinQuiry = "";// 办理进程及结果查询 通过外国人来华工作管理服务系统在线查询
        String isExistSystem = "";// 是否存在运行系统（1-是，2-否）
        String onlineHref = "";// 系统名称
        String runSysAddr = "";// 系统访问地址
        String rightClassZhuTi = "";// 服务主题 （1-是，2-否）
        String taskClassForPerson = ""; // 面向自然人（1-是，2-否）
        String taskClassForCompany = ""; // 面向法人（1-是，2-否）
        String isPermitReduce = ""; // 是否减免
        String channelType = ""; // 渠道类型
        String limitSceneExplain = ""; // 必须现场办理原因说明
        String searchType = ""; // 程序类型
        String qlJiBie = ""; // 事项行使层级
        String linkStyle = "";// 咨询方式 窗口咨询^电话咨询
        if (itemExtInfoXML.length() > 0) {
            Document document = DocumentHelper.parseText(itemExtInfoXML);
            Element root = document.getRootElement();

            // 是否开通网上预约
            Node xnReserve = root.selectSingleNode("key[@label='IS_RESERVE']");
            if (xnReserve != null) {
                isReserve = xnReserve.getText();
            }
            // 是否进驻政务大厅
            Node xnInhall = root.selectSingleNode("key[@label='IS_IN_HALL']");
            if (xnInhall != null) {
                isInHall = xnInhall.getText();
            }

            // 服务类型 行政强制事项分类（1、强制措施，2、强制执行） 征收种类
            Node xn15 = root.selectSingleNode("key[@label='SUB_TYPE']");
            if (xn15 != null) {
                serviceType = xn15.getText();
            }
            // 法定办结时限说明
            Node xn10 = root.selectSingleNode("key[@label='LAW_TIME_BASIS']");
            if (xn10 != null) {
                anticipateDayNote = xn10.getText();
            }
            // 法定办结期限单位
            Node xnanticipateType = root.selectSingleNode("key[@label='LAW_TIME_UNIT']");
            if (xnanticipateType != null) {
                anticipateType = xnanticipateType.getText();
            }

            // 承诺办结期限单位
            Node xnpromiseType = root.selectSingleNode("key[@label='AGREE_TIME_UNIT']");
            if (xnpromiseType != null) {
                promiseType = xnpromiseType.getText();
            }
            // 承诺办结时限说明
            Node xnpromiseDayNote = root.selectSingleNode("key[@label='PROCESSING_TIME_LIMIT']");
            if (xnpromiseDayNote != null) {
                promiseDayNote = xnpromiseDayNote.getText();
            }
            // 到办事现场次数
            Node xnApplyTimes = root.selectSingleNode("key[@label='APPLY_TIMES']");
            if (xnApplyTimes != null) {
                applyTimes = xnApplyTimes.getText();
            }
            // 权力来源 （1法定本级行使 2上级下放 3上级授权 4同级授权 5上级委托 6同级委托）
            Node xnPowerSource = root.selectSingleNode("key[@label='POWER_SOURCE']");
            if (xnPowerSource != null) {
                powerSource = xnPowerSource.getText();
            }
            // 是否一次办好（1-是，2-否）
            Node xnPyc = root.selectSingleNode("key[@label='ISPYC']");
            if (xnPyc != null) {
                isPyc = xnPyc.getText();
            }
            // 是否还在运维（1-是，2-否）
            Node xnRunsys = root.selectSingleNode("key[@label='ISRUNSYS']");
            if (xnRunsys != null) {
                isRunsys = xnRunsys.getText();
            }
            // 是否自建系统（1-是，2-否）
            Node xnSelfbuidsystem = root.selectSingleNode("key[@label='SELFBUILDSYSTEM']");
            if (xnSelfbuidsystem != null) {
                isSelfbuidsystem = xnSelfbuidsystem.getText();
            }
            // 填报人姓名
            Node xncontactperson = root.selectSingleNode("key[@label='APPLICANTNAME']");
            if (xncontactperson != null) {
                contactperson = xncontactperson.getText();
            }
            // 填报人联系方式
            Node xncontactphone = root.selectSingleNode("key[@label='APPLICANTCONTACT']");
            if (xncontactphone != null) {
                contactphone = xncontactphone.getText();
            }
            // 办理方式（1-自办件，2-初审件，3-联办件，4-转报件
            Node xnhandlingModel = root.selectSingleNode("key[@label='HANDLINGMODE']");
            if (xnhandlingModel != null) {
                handlingModel = xnhandlingModel.getText();
            }
            // 是否带有附件、附表、附图（1-附件，2-附表，3-附图）
            Node xnEnclosure = root.selectSingleNode("key[@label='ISENCLOSURE']");
            if (xnEnclosure != null) {
                isEnclosure = xnEnclosure.getText();
            }
            // 办理进程及结果查询
            Node xnResultinquiry = root.selectSingleNode("key[@label='RESULTINQUIRY']");
            if (xnResultinquiry != null) {
                resultinQuiry = xnResultinquiry.getText();
            }
            // 是否存在运行系统（1-是，2-否）
            Node xnExistSystem = root.selectSingleNode("key[@label='ISEXISTSYSTEM']");
            if (xnExistSystem != null) {
                isExistSystem = xnExistSystem.getText();
            }
            // 系统名称
            Node xnOnlinehref = root.selectSingleNode("key[@label='ONLINE_HREF']");
            if (xnOnlinehref != null) {
                onlineHref = xnOnlinehref.getText();
            }
            // 系统访问地址
            Node xnRunSysAddr = root.selectSingleNode("key[@label='RUNSYSADDR']");
            if (xnRunSysAddr != null) {
                runSysAddr = xnRunSysAddr.getText();
            }
            // 联办机构
            Node xnDecideAgent = root.selectSingleNode("key[@label='ORG_DECIDE_EMAIL']");
            if (xnDecideAgent != null) {
                decideNameItem = xnDecideAgent.getText();
            }
            // 服务主题（1-是，2-否）
            Node xnZhuti = root.selectSingleNode("key[@label='FUWUZHUTI']");
            if (xnZhuti != null) {
                rightClassZhuTi = xnZhuti.getText();
            }
            // 面向法人（1-是，2-否）
            Node xnTaskClassForCompany = root.selectSingleNode("key[@label='SHIFOUMIANXIANGFAREN']");
            if (xnTaskClassForCompany != null) {
                taskClassForCompany = xnTaskClassForCompany.getText();
            }
            // 面向自然人（1-是，2-否）
            Node xnTaskClassForPerson = root.selectSingleNode("key[@label='SHIFOUMIANXIANGZIRAN']");
            if (xnTaskClassForPerson != null) {
                taskClassForPerson = xnTaskClassForPerson.getText();
            }
            // 征收类事项的是否涉及征收(税)费减免的审批
            Node xnISPERMITREDUCE = root.selectSingleNode("key[@label='ISPERMITREDUCE']");
            if (xnISPERMITREDUCE != null) {
                isPermitReduce = xnISPERMITREDUCE.getText();
            }
            // 渠道类型
            Node xnchannelType = root.selectSingleNode("key[@label='CHANNELTYPE']");
            if (xnchannelType != null) {
                channelType = xnchannelType.getText();
            }
            // 必须现场办理原因说明
            Node xnlimitSceneExplain = root.selectSingleNode("key[@label='LIMITSCENEEXPLAIN']");
            if (xnlimitSceneExplain != null) {
                limitSceneExplain = xnlimitSceneExplain.getText();
            }
            // 程序类型 一般程序 简易程序
            Node xnSearchType = root.selectSingleNode("key[@label='SEARCH_TYPE']");
            if (xnSearchType != null) {
                searchType = xnSearchType.getText();
            }
            // 事项行使层级 国家级 省级 市级 县级 镇(乡、街道)级 村(社区)级 分级管理
            Node xnUserlevel = root.selectSingleNode("key[@label='USELEVEL']");
            if (xnUserlevel != null) {
                qlJiBie = xnUserlevel.getText();
            }
            // 咨询方式 咨询方式 窗口咨询^电话咨询
            Node consultUrl = root.selectSingleNode("key[@label='CONSULT_URL']");
            if (consultUrl != null) {
                linkStyle = consultUrl.getText();
            }

        }
        // 法定时限单位
        audittask.set("ANTICIPATE_TYPE", anticipateType);
        // 承诺时限单位
        audittask.setPromise_type(promiseType);
        // ITEM_INFO_XML AGENT_NAME 承办机构
        audittask.set("AGENT_NAME", agentName);
        // 实施编码
        audittask.setTaskcode(taskCode);
        // 服务对象
        audittask.set("SERVICE_OBJECT", bmCate);
        // 是否统建系统
        audittask.set("UNI_OR_OWN", "1");
        // 联办机构
        audittask.set("ORG_DECIDE_EMAIL", decideNameItem);
        // 网上办理形式
        String onlineAddressXML = right.getStr("ONLINEADDRESS_XML");
        String TRANSACT_URL = "http://jizwfw.sd.gov.cn/jnzwdt";
        String wsblfs = "";
        if (onlineAddressXML.trim().length() > 0) {
            Document document = DocumentHelper.parseText(onlineAddressXML);
            Element root = document.getRootElement();

            List<Element> nodeList = root.selectNodes("node");
            for (Element e : nodeList) {

                // 网上办理方式
                Node xnTemp = e.selectSingleNode("key[@label='ONLINE_TYPE']");
                // 网上办理链接地址
                Node addressUrl = e.selectSingleNode("key[@label='ONLINE_ADDRESS']");
                if (addressUrl != null) {
                    TRANSACT_URL = addressUrl.getText();
                }
                if (xnTemp != null) {
                    String str = xnTemp.getText();
                    wsblfs += str + ";";
                }
            }
        }
        // 网上申报网址
        audittask.set("WEBAPPLYURL", TRANSACT_URL);

        String chargeItemInfoXML = right.getStr("CHARGEITEM_INFO_XML");
        String chargeBasis = "";
        String chargeStandard = "";

        if (chargeItemInfoXML.trim().length() > 0) {
            Document document = DocumentHelper.parseText(chargeItemInfoXML);
            Element root = document.getRootElement();

            List<Element> listElement = root.elements("node");

            int i = 1;
            for (Element e : listElement) {
                Node xnT = e.selectSingleNode("key[@label='STATUS']");
                if (xnT != null && "1".equals(xnT.getText())) {
                    String s = String.valueOf(i);
                    chargeBasis += s + "、";

                    Node xnTemp3 = e.selectSingleNode("key[@label='NAME']");
                    if (xnTemp3 != null) {
                        chargeBasis += "收费项目：" + xnTemp3.getText() + "&nbsp;&nbsp;&nbsp;";
                    }

                    Node xnTemp1 = e.selectSingleNode("key[@label='BASIS']");
                    if (xnTemp1 != null) {
                        chargeBasis += "收费依据：" + xnTemp1.getText() + "&nbsp;&nbsp;&nbsp;";
                    }

                    Node xnTemp2 = e.selectSingleNode("key[@label='STANDARD']");
                    if (xnTemp2 != null) {
                        chargeBasis += "收费标准：" + xnTemp2.getText() + "<br>";
                    }
                    i++;
                }
            }

        }
        // 收费信息 需格式化
        audittask.set("CHARGEITEM_INFO", right.getStr("CHARGEITEM_INFO_XML"));

        audittask.setCharge_basis(chargeBasis);
        // 受理地点信息
        audittask.set("ACCEPT_ADDRESS_INFO", right.getStr("ACCEPT_ADDRESS_XML"));
        // 中介服务信息
//        audittask.setService_dept(right.getStr("AGENCYORGAN_INFO"));
        String folderCode = "";
        String folderName = "";
        String businessType = ""; // 对应分类（1-依申请，2-非依申请）
        String folderInfoXML = right.getStr("FOLDER_INFO_XML");
        if (folderInfoXML.trim().length() > 0) {
            Document document = DocumentHelper.parseText(folderInfoXML);
            Element root = document.getRootElement();
            List<Element> listElement = root.elements("node");
            for (Element e : listElement) {
                // 事权字典代码 目录行使层级
                // 父级code
                Node xnCode = e.selectSingleNode("key[@label='CODE']");
                if (xnCode != null) {
                    folderCode = xnCode.getText();
                }
                // 父级name
                Node xnName = e.selectSingleNode("key[@label='NAME']");
                if (xnName != null) {
                    folderName = xnName.getText();
                }
                Node xnBusinessType = e.selectSingleNode("key[@label='BUSINESS_TYPE']");
                if (xnBusinessType != null) {
                    businessType = xnBusinessType.getText();
                }
            }
        }
        // 依申请
        audittask.set("businesstype", businessType);
        // 办理流程
//        audittask.set("in_flow_info", right.getStr("HANDLING_PROCESS_XML"));
        audittask.set("in_flow_info", right.getStr("PLATFORM_PROCESS_XML"));

        // 到办事现场次数
        audittask.set("applyermin_count", applyTimes);
        // 是否一次办好（1-是，0-否）
        audittask.set("ispyc", isPyc);
        // 通办范围
        audittask.set("CROSS_SCOPE", crossScope);

        String webApplyType = "";
        String conductDepth = "";
        String isWldk = "0";
        String onlineConductXML = right.getStr("ONLINECONDUCT_XML");
        if (onlineConductXML.trim().length() > 0) {
            Document document = DocumentHelper.parseText(onlineConductXML);
            Element root = document.getRootElement();

            List<Element> nodeList = root.selectNodes("node");
            for (Element e : nodeList) {
                // 省库 1全程网办 2在线预审 3事项公开 4在线办理
                // 我们审批系统 2全程网办 1在线预审 0事项公开 4在线办理
                // 国脉网办深度 1 互联网咨询 2 互联网收件 3 互联网预审 4 互联网受理 5 互联网办理 6 互联网办理后果信息反馈 7 互联网电子证照反馈 8 其他
                Node xnConductDepth = e.selectSingleNode("key[@label='CONDUCT_DEPTH']");
                // 3^4^5^6
                if (xnConductDepth != null) {
                    conductDepth = xnConductDepth.getText();
                    webApplyType = conductDepth;
                }
                // 是否可网上缴费
                Node xnPayOnline = e.selectSingleNode("key[@label='IS_ONLINE_PAYMENT']");
                if (xnPayOnline != null) {
                    payOnline = xnPayOnline.getText();
                }
                String isDelivery = "";
                Node xnIsDelivery = e.selectSingleNode("key[@label='IS_DELIVERY']");
                if (xnIsDelivery != null) {
                    isDelivery = xnIsDelivery.getText();
                }
                if (StringUtil.isNotBlank(isDelivery) && !"0".equals(isDelivery)) {
                    isWldk = "1";
                }
            }
        }
        // 物流快递(是否支持邮寄材料)
        audittask.set("IS_DELIVERY", isWldk);
        // 网办深度
        audittask.set("wangbanshendu", conductDepth);
        // 联系电话
        audittask.setLink_tel(contactphone);

        /*************************************办事地址和部门  start***************************************************/
        String acceptAddressXML = right.getStr("ACCEPT_ADDRESS_XML");
        String transactADDR = "";
        String superviseStyle = "";
        // String linkStyle = "";
        String slTime = "";
        String ckmc = "";
        String ckxh = "";
        String trafficGuide = "";
        String acceptAddress = "";
        String addressee = "";
        String addresseePhone = "";
        String unitUrl = "";

        if (acceptAddressXML.trim().length() > 0) {
            Document document = DocumentHelper.parseText(acceptAddressXML);
            Element root = document.getRootElement();

            List<Element> listElement = root.elements("node");

            int temp = 1;
            for (Element e : listElement) {
                Node xnT = e.selectSingleNode("key[@label='STATUS']");
                if (xnT != null && "1".equals(xnT.getText())) {

                    // 办事地址
                    Node xnTemp1 = e.selectSingleNode("key[@label='ADDRESS']");
                    if (xnTemp1 != null) {
                        transactADDR = xnTemp1.getText();
                    }
                    // 监督投诉方式 电话投诉^窗口投诉
                    Node xnTemp2 = e.selectSingleNode("key[@label='COMPLAINT_PHONE']");
                    if (xnTemp2 != null) {
                        superviseStyle = xnTemp2.getText();
                    }
                    // 咨询方式 窗口咨询^电话咨询
//                    Node xnTemp3 = e.selectSingleNode("key[@label='PHONE']");
//                    if (xnTemp3 != null) {
//                        linkStyle = xnTemp3.getText();
//                    }
                    // 办公时间
                    Node xnTemp4 = e.selectSingleNode("key[@label='OFFICE_HOUR']");
                    if (xnTemp4 != null) {
                        slTime = xnTemp4.getText();
                    }
                    // 交通引导
                    Node xnTemp5 = e.selectSingleNode("key[@label='TRAFFIC_GUIDE']");
                    if (xnTemp5 != null) {
                        trafficGuide = xnTemp5.getText();
                    }
                    // 窗口名称
                    Node xnTemp6 = e.selectSingleNode("key[@label='NAME']");
                    if (xnTemp6 != null) {
                        ckmc = xnTemp6.getText();
                    }
                    // 窗口编号
                    Node xnTemp7 = e.selectSingleNode("key[@label='WINDOW_NUM']");
                    if (xnTemp7 != null) {
                        ckxh = xnTemp7.getText();
                    }
                    // 材料邮寄收件人
                    Node xnTemp8 = e.selectSingleNode("key[@label='ADDRESSEE']");
                    if (xnTemp8 != null) {
                        addressee = xnTemp8.getText();
                    }
                    // 材料收件人联系电话
                    Node xnTemp9 = e.selectSingleNode("key[@label='ADDRESSEE_PHONE']");
                    if (xnTemp9 != null) {
                        addresseePhone = xnTemp9.getText();
                    }
                    // 材料邮寄收件地址
                    Node xnTemp10 = e.selectSingleNode("key[@label='UNIT_URL']");
                    if (xnTemp10 != null) {
                        unitUrl = xnTemp10.getText();
                    }

                    // String tempString = String.valueOf(temp);
                    // acceptAddress += tempString + "&nbsp;&nbsp;";
                    acceptAddress += transactADDR + "&nbsp;&nbsp;";
                    acceptAddress += ckmc + "&nbsp;&nbsp;";
                    acceptAddress += slTime + "<br>";
                    temp++;
                }
            }
        }

        String linkTel = "";// 咨询电话 服务事项的电话
        String susTel = "";// 投诉电话
        String telType = "";// 投诉电话
        String consultXml = right.getStr("CONSULT_XML");
        if (StringUtil.isNotBlank(consultXml)) {
            if (consultXml.trim().length() > 0) {
                Document document1 = DocumentHelper.parseText(consultXml);
                Element root1 = document1.getRootElement();
                List<Element> nodeList1 = root1.elements("node");
                String phone = "";
                for (Element e : nodeList1) {
                    // 类型0标识咨询，1标识投诉
                    Node xnTemp1 = e.selectSingleNode("key[@label='TYPE']");
                    if (xnTemp1 != null) {
                        telType = xnTemp1.getText();
                    }
                    // 咨询投诉电话
                    Node xnTemp2 = e.selectSingleNode("key[@label='PHONE_NUMBER']");
                    if (xnTemp2 != null) {
                        phone = xnTemp2.getText();
                    }
                    // 咨询电话
                    if (StringUtil.isNotBlank(telType) && "0".equals(telType)) {
                        linkTel = phone;
                    }
                    // 投诉电话
                    if (StringUtil.isNotBlank(telType) && "1".equals(telType)) {
                        susTel = phone;
                    }
                }
            }
        }

        // 监督投诉电话
        audittask.setSupervise_tel(susTel);

        String lawTERM = right.getStr("LAW_TERM_XML");
        String lawXML = right.getStr("LAW_XML");
        String lawRelXML = right.getStr("LAW_REL_XML");
        if (StringUtil.isNotBlank(lawTERM.trim())) {
            Document document1 = DocumentHelper.parseText(lawTERM);
            Element root1 = document1.getRootElement();
            List<Element> nodeList1 = root1.elements("node");

            Document document2 = DocumentHelper.parseText(lawXML);
            Element root2 = document2.getRootElement();
            List<Element> nodeList2 = root2.elements("node");

            Document document3 = DocumentHelper.parseText(lawRelXML);
            Element root3 = document3.getRootElement();
            List<Element> nodeList3 = root3.elements("node");

            String byLaw = "";
            String issueDate = "";

            int i = 1;

            for (Element e : nodeList1) {
                String lawCode = "";
                // String RowGuid =
                // UUID.randomUUID().toString();
                String status = e.selectSingleNode("key[@label='STATUS']").getText();
                if ("1".equals(status)) {
                    // LAW_TERM_XML
                    String name = ""; // 法律名称
                    String office = ""; // 制定机关
                    String content = ""; // 法律内容
                    String basisCode = "";// 文号
                    String yjType = "";// 法定依据类型
                    String ljAddress = "";// 原文地址链接
                    String lawbasis = "";// 法定依据

                    String kx = "";

                    Node xnSue = e.selectSingleNode("key[@label='CREATE_TIME']");
                    if (xnSue != null) {
                        issueDate = xnSue.getText();
                    }
                    Node xnLawcode = e.selectSingleNode("key[@label='LAW_CODE']");
                    if (xnLawcode != null) {
                        lawCode = xnLawcode.getText();
                    }
                    Node xnContent = e.selectSingleNode("key[@label='CONTENT']");
                    if (xnContent != null) {
                        content = xnContent.getText();
                    }

                    int paixu = StringUtil.isBlank(e.selectSingleNode("key[@label='SORT']").getText()) ? 0
                            : Integer.parseInt(e.selectSingleNode("key[@label='SORT']").getText());

                    for (Element e2 : nodeList2) {
                        // LAW_XML
                        String code = e2.selectSingleNode("key[@label='CODE']").getText();
                        if (code.equals(lawCode)) {
                            // 法律文号 2013年7月12日中华人民共和国国务院令第637号
                            Node xnTemp2 = e2.selectSingleNode("key[@label='LAW_NUMBER']");
                            if (xnTemp2 != null) {
                                basisCode = xnTemp2.getText();
                            }
                            // 法律法规名称 《中华人民共和国外国人入境出境管理条例》
                            Node xnTemp = e2.selectSingleNode("key[@label='NAME']");
                            if (xnTemp != null) {
                                name = xnTemp.getText();
                            }
                            if (!name.contains("《")) {
                                if (!name.contains("》")) {
                                    name = "《" + name + "》";
                                }
                                else {
                                    name = "《" + name;
                                }
                            }
                            else {
                                if (!name.contains("》")) {
                                    name += name + "》";
                                }
                            }
                            // 下载地址
                            Node xnFilename = e2.selectSingleNode("key[@label='FILE_NAME']");
                            if (xnFilename != null) {
                                ljAddress = xnFilename.getText();
                            }
                            // 法律依据
                            Node xnLawbasis = e2.selectSingleNode("key[@label='LAW_BASIS']");
                            if (xnLawbasis != null) {
                                lawbasis = xnLawbasis.getText();
                            }
                            // 制定机关 全国人民代表大会常务委员会
                            Node xnOffice = e2.selectSingleNode("key[@label='OFFICE']");
                            if (xnOffice != null) {
                                office = xnOffice.getText();
                            }
                            break;
                        }
                    }

                    for (Element e3 : nodeList3) {
                        // LAW_REL_XML
                        String code = e3.selectSingleNode("key[@label='LAW_CODE']").getText();
                        if (code.equals(lawCode)) {
                            Node xnTemp2 = e3.selectSingleNode("key[@label='TYPE']");
                            if (xnTemp2 != null) {
                                yjType = xnTemp2.getText();
                            }
                            break;
                        }
                    }

                    byLaw += String.valueOf(i) + "&nbsp;&nbsp;&nbsp;";
                    byLaw += name + "&nbsp;";
                    byLaw += "(" + basisCode + ")&nbsp;&nbsp;&nbsp;";
                    byLaw += content + "<br>";
                    i++;
                }
            }
            // 法律依据
            audittask.setBy_law(byLaw);
        }

        // 受理条件
        String applyConditionXML = right.getStr("APPLY_CONDITION_XML");
        String condition = "";
        if (applyConditionXML.trim().length() > 0) {
            Document document = DocumentHelper.parseText(applyConditionXML);
            Element root = document.getRootElement();
            List<Element> listElement = root.elements("node");
            // int b = 1;
            for (Element e : listElement) {
                String status = "";
                Node xnSTATUS = e.selectSingleNode("key[@label='STATUS']");
                if (xnSTATUS != null) {
                    status = xnSTATUS.getText();
                }
                if ("1".equals(status)) {
                    // String bs = String.valueOf(b);
                    Node xnTemp2 = e.selectSingleNode("key[@label='NAME']");
                    if (xnTemp2 != null) {
                        condition += xnTemp2.getText();
                    }
                }
            }
            audittask.setAcceptcondition(condition);
        }
        else {
            audittask.setAcceptcondition("符合相关规定，提交相关申请材料");
        }

        // 办理地址
        audittask.setTransact_addr(acceptAddress);
        // audittask.setTransact_time(right.getStr("SL_Time"));//工作时间
        if (StringUtil.isNotBlank(slTime)) {
            audittask.setTransact_time(slTime);
        }
        else {
            audittask.setTransact_time("周一至周五 上午9:00-12:00 下午13:00-17:00");
        }

        // 外部流程图
        String OUT_FLOW_INFO = right.getStr("OUT_FLOW_XML");
        String taskoutimgguid = UUID.randomUUID().toString();
        if (OUT_FLOW_INFO != null && StringUtil.isNotBlank(OUT_FLOW_INFO)) {
            try {
                syncTaskFlowImg(OUT_FLOW_INFO, taskoutimgguid);
                audittask.setTaskoutimgguid(taskoutimgguid);
            }
            catch (Exception e) {
                log.info("外部流程图同步失败 =====>" + e.getMessage());
            }
        }
        WorkflowProcess workflowProcess = new WorkflowProcess();
        workflowProcess.setProcessName(right.getStr("NAME") + "【版本：" + versionNew + "】");
        workflowProcess.setProcessGuid(UUID.randomUUID().toString());
        WorkflowProcessVersion workflowProcessVersion = iWorkflowProcessService.addWorkflowProcess(workflowProcess,
                "同步业务服务", null);
        audittask.setPvguid(workflowProcessVersion.getProcessVersionGuid());
        audittask.setProcessguid(workflowProcessVersion.getProcessGuid());

        // 事项扩展信息表
        audittaskex.setOperateusername("同步服务");
        // 获取是否网上支付
        audittaskex.setOnlinepayment(payOnline);
        audittaskex.setOperatedate(new Date());
        audittaskex.setRowguid(UUID.randomUUID().toString());
        audittaskex.setTaskguid(taskguid);
        // 中介服务基本信息
        audittaskex.set("server_project_info", right.getStr("SERVE_PROJECT_XML"));

        // 数量限制
        audittaskex.set("LIMIT_NUMBER", limitNumber);
        // 标注文书
        audittaskex.set("PAPER_INFO", right.getStr("PAPER_XML"));
        // 权限划分
        audittaskex.setPowerdeline(authorityDivision);
        // 行使层级
        audittaskex.setUse_level(qlJiBie);
        // 行使内容
        audittaskex.setUse_level_c(exerciseContent);

        // 预约办理
        audittaskex.setIsyybl(isReserve);

        // 运行系统
        audittaskex.set("RUN_SYSTEM", isExistSystem);

        // 事项的子类型。
        audittaskex.set("SUB_TYPE", serviceType);
        // 材料邮寄收件人
        audittaskex.set("ADDRESSEE", addressee);
        // 特别程序种类名称
//        audittaskex.set("PROCEDURE_NAME",right.getStr("PROCEDURE_NAME"));
        // 法律救济
        audittaskex.set("COMPLAIN_INFO", right.getStr("COMPLAIN_WINDOW_REL_XML"));

        // 是否一窗办理
        audittaskex.set("IS_ONLINE_YICHUANG", isOnlineYichuang);
        // 办理形式
        audittaskex.set("HANDLE_FORM", handleForm);
        // 是否进驻大厅（1-是，0-否）
        audittaskex.setIf_jz_hall(isInHall);
        // 咨询方式
        audittaskex.set("CONSULT_URL", linkStyle);
        // 监督投诉方式
        // audittaskex.set("COMPLAIN_TYPE", superviseStyle);
        // 是否公示
        audittaskex.set("IS_PUBLIC", isPublic);
        // 办理方式
        audittaskex.set("HANDLINGMODE", handlingModel);

        /*******************************************办理结果***********************************************/
        String resultXML = right.getStr("RESULT_XML");
        String resultType = ""; // 结果类型
        String resultName = ""; // 结果名称
        String servicemode = ""; // 送达方式
        String resultSchdule = ""; // 有效时限
        String rssamp = "";
        String rsEnable = "";
        String resultExample = "";
        String resultGettype = "";
        String resultCliengguid = "";
        if (resultXML.trim().length() > 0) {
            Document document = DocumentHelper.parseText(resultXML);
            Element root = document.getRootElement();

            List<Element> listElement = root.elements("node");
            for (Element e : listElement) {
                // 结果类型 10证照、20批文、30合同、30登记表、30其他
                Node xnType = e.selectSingleNode("key[@label='RESULT_TYPE']");
                if (xnType != null) {
                    resultType = xnType.getText();
                }
                // 结果名称
                Node xnName = e.selectSingleNode("key[@label='RESULT_NAME']");
                if (xnName != null) {
                    resultName = xnName.getText();
                }
                // 送达方式（1-窗口领取，2-公告送达，3-邮寄送达，4-网站下载，5-其他）
                Node xnModel = e.selectSingleNode("key[@label='SERVICE_MODE']");
                if (xnModel != null) {
                    servicemode = xnModel.getText();
                }
                // 有效时限（1-1年，2-2年，3-3年，4-4年，5-5年，6-无期限，7-其他）
                Node xnTT = e.selectSingleNode("key[@label='RESULT_SCHEDULE']");
                if (xnTT != null) {
                    resultSchdule = xnTT.getText();
                }
                // 文书编号格式
                Node xnTT2 = e.selectSingleNode("key[@label='RSSAMP']");
                if (xnTT2 != null) {
                    rssamp = xnTT2.getText();
                }
                // 文号启用时间
                Node xnTT3 = e.selectSingleNode("key[@label='RSENABLE']");
                if (xnTT3 != null) {
                    rsEnable = xnTT3.getText();
                }
                // 办理结果附件名称
                Node xnTT4 = e.selectSingleNode("key[@label='RESULT_EXAMPLE']");
                if (xnTT4 != null) {
                    resultExample = xnTT4.getText();
                }
                // 办理结果网盘id
                Node xnTT5 = e.selectSingleNode("key[@label='RESULT_GETTYPE']");
                if (xnTT5 != null) {
                    resultGettype = xnTT5.getText();
                    String[] arr = resultGettype.split(";");
                    resultCliengguid = UUID.randomUUID().toString();
                    for (String fileNum : arr) {
                        List<Record> dvFile = new SyncToRightNewService().getfileInfo(fileNum);
                        if (dvFile.size() > 0) {
                            try {
                                String strAttachGuid = UUID.randomUUID().toString();
                                byte[] fileContent = (byte[]) dvFile.get(0).get("file_content");
                                String fileName = dvFile.get(0).getStr("file_name");
                                String[] temp = fileName.split("\\.");
                                String documentType = "." + temp[temp.length - 1].toString();
//                                new SyncToRightNewService().insertAttach(strAttachGuid, fileName, "", documentType,
//                                        "EpointMis", resultCliengguid, "", fileSize, new Date(), fileContent);

                                addAttach(strAttachGuid, resultCliengguid, fileName, documentType, fileContent);
                                // 模板材料
                                log.info("Material_InfoSuccess.log" + "插入结果材料：" + resultCliengguid + ",成功！");
                            }
                            catch (Exception e2) {
                                log.error("ErrorMaterial_Info.log" + "插入结果材料：" + resultCliengguid + ",失败！", e2);
                            }
                        }
                    }

                    // 审批结果信息(待处理：未从权力库得到有效数据)

                    AuditTaskResult taskResult = new AuditTaskResult();
                    taskResult.setRowguid(UUID.randomUUID().toString());
                    taskResult.setTaskguid(taskguid);
                    taskResult.setOperatedate(new Date());
                    taskResult.setOperateusername("同步服务");

                    // 结果类型 10证照、20批文、30合同、30登记表、30其他、 99无 审批50其他
                    if (StringUtil.isNotBlank(resultType)) {
                        if ("30".equals(right.getStr("RESULTTYPE"))) {
                            resultType = "50";
                        }
                    }
                    else {
                        resultType = "99";
                    }
                    taskResult.setSharematerialguid(UUID.randomUUID().toString());
                    taskResult.setResulttype(Integer.parseInt(resultType));
                    // 结果名称
                    taskResult.setResultname(right.getStr("RESULTNAME"));
                    // 送达方式 （1-窗口领取，2-公告送达，3-邮寄送达，4-网站下载，5-其他）
                    taskResult.set("servicemode", right.getStr("servicemode"));
                    // 有效时限 （1-1年，2-2年，3-3年，4-4年，5-5年，6-无期限，7-其他）
                    taskResult.set("resultSchdule", right.getStr("resultSchdule"));
                    // 文书编号格式
                    taskResult.set("rssamp", right.getStr("rssamp"));
                    // 文号启用时间
                    taskResult.set("rsEnable", right.getStr("rsEnable"));
                    // 办理结果样本
                    taskResult.set("resultCliengguid", resultCliengguid);
                    taskResult.setIs_print(0);
                    log.info("新增事项同步审批结果taskResult=" + taskResult);
                    auditTaskResult.addAuditResult(taskResult);
                }
            }
        }

        // 是否允许批量录入
        audittaskex.setIs_allowbatchregister(0);
        audittaskex.setSubjectnature(orgProperty);// 实施主体性质
        audittaskex.setTaskadduserdisplayname("同步服务");

        /****************************主题********************************/
        String titleXml = right.getStr("TITLE_XML");
        String personName = "";
        String companyName = "";
        String classType = "";
        if (titleXml.trim().length() > 0) {
            Document document1 = DocumentHelper.parseText(titleXml);
            Element root1 = document1.getRootElement();
            List<Element> nodeList1 = root1.elements("node");

            for (Element e : nodeList1) {
                // 主题分类 1 个人2 法人
                Node xnTemp1 = e.selectSingleNode("key[@label='CLASS_TYPE']");
                if (xnTemp1 != null) {
                    classType = xnTemp1.getText();
                }
                if ("1".equals(classType)) {
                    // 主题名称
                    Node xnTemp2 = e.selectSingleNode("key[@label='NAME']");
                    if (xnTemp2 != null) {
                        personName += xnTemp2.getText();
                    }
                }
                else {
                    // 主题名称
                    Node xnTemp2 = e.selectSingleNode("key[@label='NAME']");
                    if (xnTemp2 != null) {
                        companyName += xnTemp2.getText();
                    }
                }

            }
        }
        // 法人主题分类
        audittaskex.setTaskclass_forcompany(companyName);
        // 个人主题分类
        audittaskex.setTaskclass_forpersion(personName);
        audittaskex.setIs_simulation(0);
        audittaskex.setIsriskpoint(0);

        audittaskex.setIszijianxitong(0);
        // 是否支持预约办理
        audittaskex.setReservationmanagement("1");
        if (conductDepth.contains("^")) {
            String[] str = conductDepth.split("\\^");
            int[] a = new int[str.length];
            for (int i = 0; i < str.length; i++) {
                if (!"9".equals(str[i])) {
                    a[i] = Integer.parseInt(str[i]);
                }
            }
            Arrays.sort(a);
            int max = a[a.length - 1];
            if (max == 4 || max == 5 || max == 6 || max == 7) {
                audittaskex.setWebapplytype(1);// 网上申报类型：网上申报后直接办理
            }
            else if (max == 2 || max == 3) {
                audittaskex.setWebapplytype(1);// 网上申报类型对应：网上申报后预审
            }
            else if (max == 1) {
                audittaskex.setWebapplytype(0);// 网上申报类型：不允许网上申报
            }
            else {
                audittaskex.setWebapplytype(0);// 网上申报类型：不允许网上申报
            }
        }
        else if (conductDepth.contains(",")) {
            String[] str = conductDepth.split(",");
            int[] a = new int[str.length];
            for (int i = 0; i < str.length; i++) {
                if (!"9".equals(str[i])) {
                    a[i] = Integer.parseInt(str[i]);
                }
            }
            Arrays.sort(a);
            int max = a[a.length - 1];
            if (max == 4 || max == 5 || max == 6 || max == 7) {
                audittaskex.setWebapplytype(1);// 网上申报类型：网上申报后直接办理
            }
            else if (max == 2 || max == 3) {
                audittaskex.setWebapplytype(1);// 网上申报类型对应：网上申报后预审
            }
            else if (max == 1) {
                audittaskex.setWebapplytype(0);// 网上申报类型：不允许网上申报
            }
            else {
                audittaskex.setWebapplytype(0);// 网上申报类型：不允许网上申报
            }
        }
        else {
            if ("4".equals(conductDepth) || "5".equals(conductDepth) || "6".equals(conductDepth)
                    || "7".equals(conductDepth)) {
                audittaskex.setWebapplytype(1);// 网上申报类型：网上申报后直接办理
            }
            else if ("2".equals(conductDepth) || "3".equals(conductDepth)) {
                audittaskex.setWebapplytype(1);// 网上申报类型对应：网上申报后预审
            }
            else if ("1".equals(conductDepth) || "9".equals(conductDepth)) {
                audittaskex.setWebapplytype(0);// 网上申报类型：不允许网上申报
            }
            else {
                audittaskex.setWebapplytype(0);// 网上申报类型：不允许网上申报
            }
        }

        // 网办深度
        audittaskex.set("wangbanshendu", conductDepth);
        // 同步事项情形信息
//        audittaskex.set("CASE_SETTING_INFO", right.getStr("CASE_SETTING_INFO"));
        audittaskex.setSubjectnature("1");
        if ("370800".equals(audittask.getAreacode())) {
            // audittaskex.setNotify_ys("[#=ApplyerName#]你好，您于[#=ApplyDate#]在网上申请的“[#=ProjectName#]”，预审已获通过，请带齐申报资料到中心窗口办理。");
            audittaskex.setNotify_nys(
                    "[#=ApplyerName#]你好，您于[#=ApplyDate#]在网上申请的“[#=ProjectName#]”，由于[#=Reason#]，预审未获通过，特此通知。");
            audittaskex.setNotify_pz("[#=ApplyerName#]你好，您于[#=ApplyDate#]申请的“[#=ProjectName#]”，审批已获批准，特此通知。");
            audittaskex.setNotify_npz("[#=ApplyerName#]你好，您于[#=ApplyDate#]申请的“[#=ProjectName#]”，审批未获批准，特此通知。");
            // audittaskex.setNotify_sl("[#=ApplyerName#]你好，您于[#=ApplyDate#]申请的“[#=ProjectName#]”，经审查符合受理条件，决定受理。");
            audittaskex.setNotify_nsl(
                    "[#=ApplyerName#]你好，您于[#=ApplyDate#]申请的“[#=ProjectName#]”，由于[#=Reason#]，经审查不符合受理条件，决定不予受理");
        }

        commonDaoTo.insert(audittaskex);
        log.info("新增事项信息成功");
        commonDaoTo.insert(audittask);
        log.info("新增事项扩展信息成功");
        commonDaoTo.commitTransaction();
        // 如果是乡镇事项执行下面操作
        if (!"000".equals(Areacode.substring(6, 9))) {
            DoAuditTaskDelegate(audittask, dvOuInfo);
        }

        // 生成默认流程
        handleDefaultFlow(audittask, audittask.getAreacode());
        // 同步材料
        String materialREL = right.getStr("MATERIAL_REL_XML");
        if (StringUtil.isNotBlank(materialREL)) {
            String materialInfo = right.getStr("MATERIAL_XML");
            String materialEXT = right.getStr("MATERIAL_EXT_XML");
            log.info("RightProcess.log,开始新增权力--取出材料成功!");
            insertMateria(materialInfo, materialEXT, materialREL, taskguid, null);
        }

        String question = right.getStr("QUESTION_XML");
        if (question != null && StringUtil.isNotBlank(question)) {
            // 同步常见问题
            syncTaskFaq(question, taskId);
            this.log.info("同步常见问题成功");
        }

    }

    /**
     * 
     *  [更新事项]
     *  [功能详细描述]
     *  @param right  前置库待同步事项
     *  @param dvOuInfo  部门组织架构
     *  @param task    本地库待更新事项
     * @throws DocumentException 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateTask(Record right, Record dvOuInfo, AuditTask task) throws DocumentException {
        // 我方老事项
        AuditTask audittask = (AuditTask) task.clone();
        AuditTaskExtension audittaskext = getTaskEx(task.getRowguid());
        // 我方老事项拓展表
        AuditTaskExtension audittaskex = (AuditTaskExtension) audittaskext.clone();

        // 事项的基本信息添加
        String taskguid = UUID.randomUUID().toString();
        audittask.setRowguid(taskguid);
        audittask.setOperateusername("同步服务");
        audittask.setOperatedate(new Date());
        audittask.set("new_item_code", right.getStr("ITEM_CODE"));
        audittask.setIs_history(0);
        // 是否启用
        audittask.setIs_enable(1);
        // 是否为模板事项
        audittask.setIstemplate(0);
        // 事项审核状态
        if ("1".equals(IS_EDITAFTERIMPORT)) {
            audittask.setIs_editafterimport(1);
        }
        else {
            audittask.setIs_editafterimport(4);
        }

        audittask.setTaskname(right.getStr("NAME"));
        audittask.set("INNER_CODE", right.getStr("ITEM_CODE"));
        // 目录编码
        audittask.set("FOLDER_CODE", right.getStr("FOLDER_CODE"));
        // 目录名称
        audittask.set("FOLDER_NAME", right.getStr("FOLDER_NAME"));

        audittask.set("unid", right.getStr("ID"));
        audittask.setTask_id(task.getTask_id());

        String versionNew = "";
        if (StringUtil.isNotBlank(right.getStr("VERSION"))) {
            audittask.setVersion(right.getStr("VERSION"));
            // 把前置库的版本号同步过来
            versionNew = right.getStr("VERSION");
        }
        else {
            audittask.setVersion("1");
            versionNew = "1";
        }

        audittask.setVersiondate(new Date());
        audittask.setTasksource("2");

        // 解析 ITEM_INFO_XML获取TASKCODE
        String itemInfoXml = right.getStr("ITEM_INFO_XML");
        String taskCode = "";
        String qlName = "";
        Integer promiseDay = 0;// 承诺时限
        Integer anticipateDay = 0;
        Integer procedureTime = 0;
        Integer chargeFlag = 0;
        String isAgencyorgan = "";// 是否中介机构
        String crossScope = "";// 通办范围
        String handleForm = "";// 办理形式
        String payOnline = "";// 是否可网上支付
        String remark = "";// 备注
        String regionName = "";// 区划名称
        String applyerType = "0,1";
        String bmCate = "";// 服务对象
        String isOnline = "";// 是否网上办理
        String acceptTime = "0";
        String sendTime = "0";// 送达期限
        String agentCode = "";// 承办单位
        String agentName = "";// 承办单位名称
        String isRate = "";// 是否提供星级评价
        String runSystem = "";// 运行系统
        String orgProperty = "";// 实施主体性质
        String authorityDivision = "";// 权限划分
        String exerciseContent = "";// 行使内容
        String limitNumber = "";// 数量限制
        String isFront = "";// 是否前置审批
        String isPublic = "";// 是否公示
        String isProcedure = "";// 是否走特别程序
        String isCatalog = "";// 是否内容分类
        String consultOnline = "";// 是否可网上咨询
        String serviceState = "";// 事项服务状态编码
        String operationStatus = "";// 事项操作状态编码
        String xkitemProperty = "";// 行政许可事项属性
        String isFreeTradeZone = "";// 是否自贸区
        String isOnlineYichuang = "";// 是否一窗办理
        String isWeb = "";// 是否支持自助终端办理
        String isTreeLeft = "";// 是否是目录树的叶子
        String catalogcode = "";// 基本编码
        String fundAccount = "";// 行政管理相对人
        String kind = "";// 执行种类编码 强制执行种类 强制措施方式
        String kindname = "";// 措施方式名称
        String TASKHANDLEITEM = "";

        // 投诉方式
        String complainPhone = "";

        if (itemInfoXml.trim().length() > 0) {
            Document document = DocumentHelper.parseText(itemInfoXml);
            Element root = document.getRootElement();
            // 事项名称
            Node xnName = root.selectSingleNode("key[@label='NAME']");
            if (xnName != null) {
                qlName = xnName.getText();
            }

            // 承办单位
            Node agent_name = root.selectSingleNode("key[@label='AGENT_NAME']");
            if (agent_name != null) {
                agentName = agent_name.getText();
            }

            // 基本编码(短)
            Node xncatalogcode = root.selectSingleNode("key[@label='CATALOGCODE']");
            if (xncatalogcode != null) {
                catalogcode = xncatalogcode.getText();
            }
            // 事项唯一编码(长)
            Node xntaskcode = root.selectSingleNode("key[@label='TASKCODE']");
            if (xntaskcode != null) {
                taskCode = xntaskcode.getText();
            }
            // 法定时限(工作日） BigDecimal
            Node xnanticipate = root.selectSingleNode("key[@label='LAW_TIME']");
            if (xnanticipate != null) {
                anticipateDay = Integer.parseInt(xnanticipate.getText());
            }

            // 承诺时限(工作日) BigDecimal
            Node xnpromisday = root.selectSingleNode("key[@label='AGREE_TIME']");
            if (xnpromisday != null) {
                promiseDay = Integer.parseInt(xnpromisday.getText());
            }

            // 是否提供星级评价[0 否,1 是] String
            Node xnRate = root.selectSingleNode("key[@label='IS_RATE']");
            if (xnRate != null) {
                isRate = xnRate.getText();
            }

            // 是否收费[0 否,1 是] String
            Node xnChargeFlag = root.selectSingleNode("key[@label='IS_CHARGE']");
            if (xnChargeFlag != null) {
                chargeFlag = Integer.parseInt(xnChargeFlag.getText());
            }

            // 是否网上办理（网上公示）[0 否1 是]
            Node xnOnLine = root.selectSingleNode("key[@label='IS_ONLINE']");
            if (xnOnLine != null) {
                isOnline = xnOnLine.getText();
            }
            // 是否前置审批[0 否1 是]
            Node xnFront = root.selectSingleNode("key[@label='IS_FRONT']");
            if (xnFront != null) {
                isFront = xnFront.getText();
            }
            // 是否公示[0 否，1 是]
            Node xnPublic = root.selectSingleNode("key[@label='IS_PUBLIC']");
            if (xnPublic != null) {
                isPublic = xnPublic.getText();
            }
            // 是否走特别程序[0 否，1 是]
            Node xnProcedure = root.selectSingleNode("key[@label='IS_PROCEDURE']");
            if (xnProcedure != null) {
                isProcedure = xnProcedure.getText();
            }

            // 特别程序实施总期限 BigDecimal
            Node xnProcedureTime = root.selectSingleNode("key[@label='PROCEDURE_TIME']");
            if (xnProcedureTime != null) {
                procedureTime = Integer.parseInt(xnProcedureTime.getText());
            }

            // 是否内容分类(0 否，1 是)默认0
            Node xnCatalog = root.selectSingleNode("key[@label='IS_CATALOG']");
            if (xnCatalog != null) {
                isCatalog = xnCatalog.getText();
            }

            // 是否可网上缴费 --该字段不再作为是否网上缴费
//            Node xnPayOnline = root.selectSingleNode("key[@label='PAY_ONLINE']");
//            if (xnPayOnline != null) {
//                payOnline = xnPayOnline.getText();
//            }
            // 是否可网上咨询
            Node xnConsultOnline = root.selectSingleNode("key[@label='CONSULT_ONLINE']");
            if (xnConsultOnline != null) {
                consultOnline = xnConsultOnline.getText();
            }
            // 事项服务状态编码
            Node xnState = root.selectSingleNode("key[@label='STATE']");
            if (xnState != null) {
                serviceState = xnState.getText();
            }
            // 事项操作状态编码
            Node xnStatus = root.selectSingleNode("key[@label='STATUS']");
            if (xnStatus != null) {
                operationStatus = xnStatus.getText();
            }

            // 服务对象 2,3,4,5,6,9 1
            Node xnA = root.selectSingleNode("key[@label='SERVICE_OBJECT']");
            if (xnA != null) {
                bmCate = xnA.getText();
            }
            // 行政许可事项属性
            Node xnProperty = root.selectSingleNode("key[@label='XK_ITEM_PROPERTY']");
            if (xnProperty != null) {
                xkitemProperty = xnProperty.getText();
            }

            // 是否中介机构
            Node xnAgencyorgan = root.selectSingleNode("key[@label='IS_AGENCYORGAN']");
            if (xnAgencyorgan != null) {
                isAgencyorgan = xnAgencyorgan.getText();
            }

            // 实施主体性质 （1，法定机关 2，授权组织 3，受委托组织）
            Node xnORGPROPERTY = root.selectSingleNode("key[@label='ORG_PROPERTY']");
            if (xnORGPROPERTY != null) {
                orgProperty = xnORGPROPERTY.getText();
            }

            // 通办范围 1 全国 2 全省 3 全市 4 全县 5 全镇（乡、街道） 6 跨村（社区）
            Node xn6 = root.selectSingleNode("key[@label='CROSS_SCOPE']");
            if (xn6 != null) {
                crossScope = xn6.getText();
            }
            // 办理形式 窗口办理 网上办理 快递申请 上门服务
            Node xnHandleForm = root.selectSingleNode("key[@label='HANDLE_FORM']");
            if (xnHandleForm != null) {
                handleForm = xnHandleForm.getText();
            }
            // 权限划分
            Node xnAUTHORITYDIVISION = root.selectSingleNode("key[@label='AUTHORITY_DIVISION']");
            if (xnAUTHORITYDIVISION != null) {
                authorityDivision = xnAUTHORITYDIVISION.getText();
            }
            // 行使内容
            Node xnEXERCISECONTENT = root.selectSingleNode("key[@label='EXERCISE_CONTENT']");
            if (xnEXERCISECONTENT != null) {
                exerciseContent = xnEXERCISECONTENT.getText();
            }
            // 是否自贸区
            Node xnFreeTradeZone = root.selectSingleNode("key[@label='IS_FREETRADEZONE']");
            if (xnFreeTradeZone != null) {
                isFreeTradeZone = xnFreeTradeZone.getText();
            }
            // 是否一窗办理
            Node xnOnlineYichuang = root.selectSingleNode("key[@label='IS_ONLINE_YICHUANG']");
            if (xnOnlineYichuang != null) {
                isOnlineYichuang = xnOnlineYichuang.getText();
            }
            // 是否支持自助终端办理
            Node xnWeb = root.selectSingleNode("key[@label='IS_WEB']");
            if (xnWeb != null) {
                isWeb = xnWeb.getText();
            }
            // 是否是目录树的叶子
            Node xnTreeLeft = root.selectSingleNode("key[@label='IS_TREE_LEAF']");
            if (xnTreeLeft != null) {
                isTreeLeft = xnTreeLeft.getText();
            }

            // 区划名称
            Node xnREGIONNAME = root.selectSingleNode("key[@label='REGION_NAME']");
            if (xnREGIONNAME != null) {
                regionName = xnREGIONNAME.getText();
            }

            // 运行系统 （系统层级 1国家级 2省级3省级部门 4市级 5市级部门 6县级 7县级部门）
            Node xnn = root.selectSingleNode("key[@label='RUN_SYSTEM']");
            if (xnn != null) {
                runSystem = xnn.getText();
            }

            // 数量限制
            Node xnLIMITNUMBER = root.selectSingleNode("key[@label='LIMIT_NUMBER']");
            if (xnLIMITNUMBER != null) {
                limitNumber = xnLIMITNUMBER.getText();
            }
            // 行政管理相对人
            Node xnfundAccount = root.selectSingleNode("key[@label='FUND_ACCOUNT']");
            if (xnfundAccount != null) {
                fundAccount = xnfundAccount.getText();
            }
            // 种类编码 强制执行种类 强制措施方式
            Node xnKIND = root.selectSingleNode("key[@label='KIND']");
            if (xnKIND != null) {
                kind = xnKIND.getText();
            }
            // 种类名称
            Node xnKindname = root.selectSingleNode("key[@label='KIND_NAME']");
            if (xnKindname != null) {
                kindname = xnKindname.getText();
            }
            Node xnTASKHANDLEITEM = root.selectSingleNode("key[@label='TASKHANDLEITEM']");
            if (xnTASKHANDLEITEM != null) {
                TASKHANDLEITEM = xnTASKHANDLEITEM.getText();
            }

            // 投诉方式
            Node COMPLAIN_PHONE = root.selectSingleNode("key[@label='COMPLAIN_PHONE']");
            if (COMPLAIN_PHONE != null) {
                complainPhone = COMPLAIN_PHONE.getText();
            }

        }

        if (StringUtil.isNotBlank(TASKHANDLEITEM)) {
            audittask.setItem_id(TASKHANDLEITEM);
        }
        else {
            audittask.setItem_id(taskCode);
        }

        // 中介服务
        audittaskex.set("IS_AGENCYORGAN", isAgencyorgan);
        // 基本编码
        audittask.setCatalogcode(catalogcode);

        audittaskex.set("COMPLAIN_TYPE", complainPhone);

        // 法定时限单位
//        audittask.setAnticipate_type(LAW_TIME);    
        // 承诺时限单位
//        audittask.setPromise_type(AGREE_TIME);
        // 承诺时限
log.info("update promise_day-task:"+audittask.getItem_id()+","+audittask.getPromise_day());
        audittask.setPromise_day(promiseDay == null ? 1 : promiseDay);
                            log.info("update promise_day-task:"+audittask.getItem_id()+","+audittask.getPromise_day());
        // 法定时限
        audittask.setAnticipate_day(anticipateDay == null ? 1 : anticipateDay);
        // 是否收费
        audittask.setCharge_flag(chargeFlag == null ? 0 : chargeFlag);
        // 是否网上办理
        audittaskex.set("IS_ONLINE", isOnline);
        // 是否特殊程序
        audittaskex.set("IS_PROCEDURE", isProcedure);
        // 事项服务状态编码
        audittask.set("STATE", serviceState);

        // 服务对象，默认为个人法人
        if ("1".equals(bmCate)) {
            audittask.setApplyertype("20");
        }
        else if (bmCate.contains("1") && bmCate.length() > 1) {
            audittask.setApplyertype("10,20");
        }
        else if (!bmCate.contains("1")) {
            audittask.setApplyertype("10");
        }
        else {
            audittask.setApplyertype("10,20");
        }

        // audittask.setQl_dept(dvOuInfo.getStr("OUName"));//受理机构
        // audittask.setDept_ql_id(dvOuInfo.getStr("OUCode"));//审核不通过意见
        // audittask.setDecision_dep(dvOuInfo.getStr("OUName"));//决定机构
        // 辖区
        // audittask.setAreacode("100001");
        String Areacode = right.getStr("REGION_CODE");

        if (dvOuInfo == null) {
            audittask.setOuguid(right.getStr("ORG_CODE"));
            audittask.setOuname(right.getStr("ORG_NAME"));
            audittask.setAreacode(Areacode.substring(0, 6));
        }
        else {
            audittask.setAreacode(dvOuInfo.getStr("AREACODE").substring(0, 6));
            audittask.setOuguid(dvOuInfo.getStr("ouguid"));
            audittask.setOuname(dvOuInfo.getStr("ouname"));
        }
        // 审批类别和审批系统中代码项(审批类别)对应
        switch (right.getStr("TYPE")) {
            case "XK":
                audittask.setShenpilb("01");
                break;
            case "GG":
                audittask.setShenpilb("11");
                break;
            case "QT":
                audittask.setShenpilb("10");
                break;
            case "CF":
                audittask.setShenpilb("02");
                break;
            case "QZ":
                audittask.setShenpilb("03");
                break;
            case "ZS":
                audittask.setShenpilb("04");
                break;
            case "GF":
                audittask.setShenpilb("05");
                break;
            case "JC":
                audittask.setShenpilb("09");
                break;
            case "QR":
                audittask.setShenpilb("07");
                break;
            case "JL":
                audittask.setShenpilb("06");
                break;
            case "CJ":
                audittask.setShenpilb("08");
                break;
            case "ZJJG":
                audittask.setShenpilb("15");
                break;
            default:
                audittask.setShenpilb("10");
                break;
        }

        Integer assort = null;
        if (StringUtil.isBlank(right.getStr("ASSORT"))) {
            // 大汉前置空也是即办件
            assort = 1;
        }
        else {
            switch (right.getStr("ASSORT")) {
                case FWConstants.STRONE:
                    assort = 1;
                    break;
                case FWConstants.STRTWO:
                    assort = 2;
                    break;
                case FWConstants.STRTHREE:
                    assort = 3;
                    break;
                case FWConstants.STRFOUR:
                    assort = 4;
                    break;
                default:
                    assort = 1;
                    break;
            }
        }
        // 办件类型 1.即办 .....
//        if (assort == 1) {
//            audittask.setJbjmode("1");
//        }
        audittask.setType(assort);

        audittask.set("charge_lc", "0");

        audittask.setQlfullid(right.getStr("ITEM_CODE"));
        audittask.setFullid(right.getStr("ITEM_CODE"));

        String itemExtInfoXML = right.getStr("ITME_EXT_INFO_XML");
        // 行政强制事项分类（1、强制措施，2、强制执行）
        String serviceType = "";// 服务类型 依申请服务 1 主动服务 2 依申请和主动服务 1,2
        String offerWay = "";// 提供方式
        String taskType = "";// 事项类别
        String decideNameItem = "";// 联办机构 ORG_DECIDE_EMAIL
        String isReserve = "";// 是否开通网上预约
        String promiseDayNote = "";// 承诺时限描述
        String anticipateDayNote = "";// 法定办结时限说明
        String contactperson = ""; // 填报人
        String contactphone = "";// 填报人联系方式
        String wbyxhj = "2";
        String commomQuestion = "";// 常见问题
        String promiseType = "workday";// 承诺时限单位
        String anticipateType = "workday";// 法定时限单位
        String isInHall = "";// 是否进驻政务大厅
        String applyTimes = "";// 到办事现场次数
        String powerSource = "";// 权力来源
        String isPyc = "";// 是否一次办好（1-是，2-否）
        String isRunsys = "";// 是否还在运维（1-是，2-否）
        String isSelfbuidsystem = "";// 是否自建系统（1-是，2-否）
        String handlingModel = "";// 办理方式（1-自办件，2-初审件，3-联办件，4-转报件
        String isEnclosure = "";// 是否带有附件、附表、附图（1-附件，2-附表，3-附图）
        String resultinQuiry = "";// 办理进程及结果查询 通过外国人来华工作管理服务系统在线查询
        String isExistSystem = "";// 是否存在运行系统（1-是，2-否）
        String onlineHref = "";// 系统名称
        String runSysAddr = "";// 系统访问地址
        String rightClassZhuTi = "";// 服务主题 （1-是，2-否）
        String taskClassForPerson = ""; // 面向自然人（1-是，2-否）
        String taskClassForCompany = ""; // 面向法人（1-是，2-否）
        String isPermitReduce = ""; // 是否减免
        String channelType = ""; // 渠道类型
        String limitSceneExplain = ""; // 必须现场办理原因说明
        String searchType = ""; // 程序类型
        String qlJiBie = ""; // 事项行使层级
        String linkStyle = "";// 咨询方式 窗口咨询^电话咨询
        if (itemExtInfoXML.length() > 0) {
            Document document = DocumentHelper.parseText(itemExtInfoXML);
            Element root = document.getRootElement();

            // 是否开通网上预约
            Node xnReserve = root.selectSingleNode("key[@label='IS_RESERVE']");
            if (xnReserve != null) {
                isReserve = xnReserve.getText();
            }
            // 是否进驻政务大厅
            Node xnInhall = root.selectSingleNode("key[@label='IS_IN_HALL']");
            if (xnInhall != null) {
                isInHall = xnInhall.getText();
            }

            // 服务类型 行政强制事项分类（1、强制措施，2、强制执行） 征收种类
            Node xn15 = root.selectSingleNode("key[@label='SUB_TYPE']");
            if (xn15 != null) {
                serviceType = xn15.getText();
            }
            // 法定办结时限说明
            Node xn10 = root.selectSingleNode("key[@label='LAW_TIME_BASIS']");
            if (xn10 != null) {
                anticipateDayNote = xn10.getText();
            }
            // 法定办结期限单位
            Node xnanticipateType = root.selectSingleNode("key[@label='LAW_TIME_UNIT']");
            if (xnanticipateType != null) {
                anticipateType = xnanticipateType.getText();
            }

            // 承诺办结期限单位
            Node xnpromiseType = root.selectSingleNode("key[@label='AGREE_TIME_UNIT']");
            if (xnpromiseType != null) {
                promiseType = xnpromiseType.getText();
            }
            // 承诺办结时限说明
            Node xnpromiseDayNote = root.selectSingleNode("key[@label='PROCESSING_TIME_LIMIT']");
            if (xnpromiseDayNote != null) {
                promiseDayNote = xnpromiseDayNote.getText();
            }
            // 到办事现场次数
            Node xnApplyTimes = root.selectSingleNode("key[@label='APPLY_TIMES']");
            if (xnApplyTimes != null) {
                applyTimes = xnApplyTimes.getText();
            }
            // 权力来源 （1法定本级行使 2上级下放 3上级授权 4同级授权 5上级委托 6同级委托）
            Node xnPowerSource = root.selectSingleNode("key[@label='POWER_SOURCE']");
            if (xnPowerSource != null) {
                powerSource = xnPowerSource.getText();
            }
            // 是否一次办好（1-是，2-否）
            Node xnPyc = root.selectSingleNode("key[@label='ISPYC']");
            if (xnPyc != null) {
                isPyc = xnPyc.getText();
            }
            // 是否还在运维（1-是，2-否）
            Node xnRunsys = root.selectSingleNode("key[@label='ISRUNSYS']");
            if (xnRunsys != null) {
                isRunsys = xnRunsys.getText();
            }
            // 是否自建系统（1-是，2-否）
            Node xnSelfbuidsystem = root.selectSingleNode("key[@label='SELFBUILDSYSTEM']");
            if (xnSelfbuidsystem != null) {
                isSelfbuidsystem = xnSelfbuidsystem.getText();
            }
            // 填报人姓名
            Node xncontactperson = root.selectSingleNode("key[@label='APPLICANTNAME']");
            if (xncontactperson != null) {
                contactperson = xncontactperson.getText();
            }
            // 填报人联系方式
            Node xncontactphone = root.selectSingleNode("key[@label='APPLICANTCONTACT']");
            if (xncontactphone != null) {
                contactphone = xncontactphone.getText();
            }
            // 办理方式（1-自办件，2-初审件，3-联办件，4-转报件
            Node xnhandlingModel = root.selectSingleNode("key[@label='HANDLINGMODE']");
            if (xnhandlingModel != null) {
                handlingModel = xnhandlingModel.getText();
            }
            // 是否带有附件、附表、附图（1-附件，2-附表，3-附图）
            Node xnEnclosure = root.selectSingleNode("key[@label='ISENCLOSURE']");
            if (xnEnclosure != null) {
                isEnclosure = xnEnclosure.getText();
            }
            // 办理进程及结果查询
            Node xnResultinquiry = root.selectSingleNode("key[@label='RESULTINQUIRY']");
            if (xnResultinquiry != null) {
                resultinQuiry = xnResultinquiry.getText();
            }
            // 是否存在运行系统（1-是，2-否）
            Node xnExistSystem = root.selectSingleNode("key[@label='ISEXISTSYSTEM']");
            if (xnExistSystem != null) {
                isExistSystem = xnExistSystem.getText();
            }
            // 系统名称
            Node xnOnlinehref = root.selectSingleNode("key[@label='ONLINE_HREF']");
            if (xnOnlinehref != null) {
                onlineHref = xnOnlinehref.getText();
            }
            // 系统访问地址
            Node xnRunSysAddr = root.selectSingleNode("key[@label='RUNSYSADDR']");
            if (xnRunSysAddr != null) {
                runSysAddr = xnRunSysAddr.getText();
            }
            // 联办机构
            Node xnDecideAgent = root.selectSingleNode("key[@label='ORG_DECIDE_EMAIL']");
            if (xnDecideAgent != null) {
                decideNameItem = xnDecideAgent.getText();
            }
            // 服务主题（1-是，2-否）
            Node xnZhuti = root.selectSingleNode("key[@label='FUWUZHUTI']");
            if (xnZhuti != null) {
                rightClassZhuTi = xnZhuti.getText();
            }
            // 面向法人（1-是，2-否）
            Node xnTaskClassForCompany = root.selectSingleNode("key[@label='SHIFOUMIANXIANGFAREN']");
            if (xnTaskClassForCompany != null) {
                taskClassForCompany = xnTaskClassForCompany.getText();
            }
            // 面向自然人（1-是，2-否）
            Node xnTaskClassForPerson = root.selectSingleNode("key[@label='SHIFOUMIANXIANGZIRAN']");
            if (xnTaskClassForPerson != null) {
                taskClassForPerson = xnTaskClassForPerson.getText();
            }
            // 征收类事项的是否涉及征收(税)费减免的审批
            Node xnISPERMITREDUCE = root.selectSingleNode("key[@label='ISPERMITREDUCE']");
            if (xnISPERMITREDUCE != null) {
                isPermitReduce = xnISPERMITREDUCE.getText();
            }
            // 渠道类型
            Node xnchannelType = root.selectSingleNode("key[@label='CHANNELTYPE']");
            if (xnchannelType != null) {
                channelType = xnchannelType.getText();
            }
            // 必须现场办理原因说明
            Node xnlimitSceneExplain = root.selectSingleNode("key[@label='LIMITSCENEEXPLAIN']");
            if (xnlimitSceneExplain != null) {
                limitSceneExplain = xnlimitSceneExplain.getText();
            }
            // 程序类型 一般程序 简易程序
            Node xnSearchType = root.selectSingleNode("key[@label='SEARCH_TYPE']");
            if (xnSearchType != null) {
                searchType = xnSearchType.getText();
            }
            // 事项行使层级 国家级 省级 市级 县级 镇(乡、街道)级 村(社区)级 分级管理
            Node xnUserlevel = root.selectSingleNode("key[@label='USELEVEL']");
            if (xnUserlevel != null) {
                qlJiBie = xnUserlevel.getText();
            }
            // 咨询方式 咨询方式 窗口咨询^电话咨询
            Node consultUrl = root.selectSingleNode("key[@label='CONSULT_URL']");
            if (consultUrl != null) {
                linkStyle = consultUrl.getText();
            }

        }
        // 法定时限单位
        audittask.set("ANTICIPATE_TYPE", anticipateType);
        // 承诺时限单位
        audittask.setPromise_type(promiseType);
        // ITEM_INFO_XML 内的 AGENT_NAME 承办机构
        audittask.set("AGENT_NAME", agentName);
        // 实施编码
        audittask.setTaskcode(taskCode);
        // 服务对象
        audittask.set("SERVICE_OBJECT", bmCate);
        // 是否统建系统
        audittask.set("UNI_OR_OWN", "1");
        // 联办机构
        audittask.set("ORG_DECIDE_EMAIL", decideNameItem);
        // 网上办理形式
        String onlineAddressXML = right.getStr("ONLINEADDRESS_XML");
        String TRANSACT_URL = "http://jizwfw.sd.gov.cn/jnzwdt";
        String wsblfs = "";
        if (onlineAddressXML.trim().length() > 0) {
            Document document = DocumentHelper.parseText(onlineAddressXML);
            Element root = document.getRootElement();

            List<Element> nodeList = root.selectNodes("node");
            for (Element e : nodeList) {

                // 网上办理方式
                Node xnTemp = e.selectSingleNode("key[@label='ONLINE_TYPE']");
                // 网上办理链接地址
                Node addressUrl = e.selectSingleNode("key[@label='ONLINE_ADDRESS']");
                if (addressUrl != null) {
                    TRANSACT_URL = addressUrl.getText();
                }
                if (xnTemp != null) {
                    String str = xnTemp.getText();
                    wsblfs += str + ";";
                }
            }
        }
        // 网上申报网址
        audittask.set("WEBAPPLYURL", TRANSACT_URL);

        String chargeItemInfoXML = right.getStr("CHARGEITEM_INFO_XML");
        String chargeBasis = "";
        String chargeStandard = "";

        if (chargeItemInfoXML.trim().length() > 0) {
            Document document = DocumentHelper.parseText(chargeItemInfoXML);
            Element root = document.getRootElement();

            List<Element> listElement = root.elements("node");

            int i = 1;
            for (Element e : listElement) {
                Node xnT = e.selectSingleNode("key[@label='STATUS']");
                if (xnT != null && "1".equals(xnT.getText())) {
                    String s = String.valueOf(i);
                    chargeBasis += s + "、";

                    Node xnTemp3 = e.selectSingleNode("key[@label='NAME']");
                    if (xnTemp3 != null) {
                        chargeBasis += "收费项目：" + xnTemp3.getText() + "&nbsp;&nbsp;&nbsp;";
                    }

                    Node xnTemp1 = e.selectSingleNode("key[@label='BASIS']");
                    if (xnTemp1 != null) {
                        chargeBasis += "收费依据：" + xnTemp1.getText() + "&nbsp;&nbsp;&nbsp;";
                    }

                    Node xnTemp2 = e.selectSingleNode("key[@label='STANDARD']");
                    if (xnTemp2 != null) {
                        chargeBasis += "收费标准：" + xnTemp2.getText() + "<br>";
                    }
                    i++;
                }
            }

        }
        // 收费信息 需格式化
        audittask.set("CHARGEITEM_INFO", right.getStr("CHARGEITEM_INFO_XML"));

        audittask.setCharge_basis(chargeBasis);
        // 受理地点信息
        audittask.set("ACCEPT_ADDRESS_INFO", right.getStr("ACCEPT_ADDRESS_XML"));
        // 中介服务信息
//        audittask.setService_dept(right.getStr("AGENCYORGAN_INFO"));
        String folderCode = "";
        String folderName = "";
        String businessType = ""; // 对应分类（1-依申请，2-非依申请）
        String folderInfoXML = right.getStr("FOLDER_INFO_XML");
        if (folderInfoXML.trim().length() > 0) {
            Document document = DocumentHelper.parseText(folderInfoXML);
            Element root = document.getRootElement();
            List<Element> listElement = root.elements("node");
            for (Element e : listElement) {
                // 事权字典代码 目录行使层级
                // 父级code
                Node xnCode = e.selectSingleNode("key[@label='CODE']");
                if (xnCode != null) {
                    folderCode = xnCode.getText();
                }
                // 父级name
                Node xnName = e.selectSingleNode("key[@label='NAME']");
                if (xnName != null) {
                    folderName = xnName.getText();
                }
                Node xnBusinessType = e.selectSingleNode("key[@label='BUSINESS_TYPE']");
                if (xnBusinessType != null) {
                    businessType = xnBusinessType.getText();
                }
            }
        }
        // 依申请
        audittask.set("businesstype", businessType);
        // 办理流程
        audittask.set("in_flow_info", right.getStr("PLATFORM_PROCESS_XML"));
//        audittask.set("in_flow_info", right.getStr("HANDLING_PROCESS_XML"));
        // 到办事现场次数
        audittask.set("applyermin_count", applyTimes);
        // 是否一次办好（1-是，0-否）
        audittask.set("ispyc", isPyc);
        // 通办范围
        audittask.set("CROSS_SCOPE", crossScope);

        String webApplyType = "";
        String conductDepth = "";
        String isWldk = "0";
        String onlineConductXML = right.getStr("ONLINECONDUCT_XML");
        if (onlineConductXML.trim().length() > 0) {
            Document document = DocumentHelper.parseText(onlineConductXML);
            Element root = document.getRootElement();

            List<Element> nodeList = root.selectNodes("node");
            for (Element e : nodeList) {
                // 省库 1全程网办 2在线预审 3事项公开 4在线办理
                // 我们审批系统 2全程网办 1在线预审 0事项公开 4在线办理
                // 国脉网办深度 1 互联网咨询 2 互联网收件 3 互联网预审 4 互联网受理 5 互联网办理 6 互联网办理后果信息反馈 7 互联网电子证照反馈 8 其他
                Node xnConductDepth = e.selectSingleNode("key[@label='CONDUCT_DEPTH']");
                // 3^4^5^6
                if (xnConductDepth != null) {
                    conductDepth = xnConductDepth.getText();
                    webApplyType = conductDepth;
                }
                // 是否可网上缴费
                Node xnPayOnline = e.selectSingleNode("key[@label='IS_ONLINE_PAYMENT']");
                if (xnPayOnline != null) {
                    payOnline = xnPayOnline.getText();
                }
                String isDelivery = "";
                Node xnIsDelivery = e.selectSingleNode("key[@label='IS_DELIVERY']");
                if (xnIsDelivery != null) {
                    isDelivery = xnIsDelivery.getText();
                }
                if (StringUtil.isNotBlank(isDelivery) && !"0".equals(isDelivery)) {
                    isWldk = "1";
                }
            }
        }
        // 物流快递(是否支持邮寄材料)
        audittask.set("IS_DELIVERY", isWldk);
        // 网办深度
        audittask.set("wangbanshendu", conductDepth);
        // 联系电话
        audittask.setLink_tel(contactphone);

        /*************************************办事地址和部门  start***************************************************/
        String acceptAddressXML = right.getStr("ACCEPT_ADDRESS_XML");
        String transactADDR = "";
        String superviseStyle = "";
        // String linkStyle = "";
        String slTime = "";
        String ckmc = "";
        String ckxh = "";
        String trafficGuide = "";
        String acceptAddress = "";
        String addressee = "";
        String addresseePhone = "";
        String unitUrl = "";

        if (acceptAddressXML.trim().length() > 0) {
            Document document = DocumentHelper.parseText(acceptAddressXML);
            Element root = document.getRootElement();

            List<Element> listElement = root.elements("node");

            int temp = 1;
            for (Element e : listElement) {
                Node xnT = e.selectSingleNode("key[@label='STATUS']");
                if (xnT != null && "1".equals(xnT.getText())) {

                    // 办事地址
                    Node xnTemp1 = e.selectSingleNode("key[@label='ADDRESS']");
                    if (xnTemp1 != null) {
                        transactADDR = xnTemp1.getText();
                    }
                    // 监督投诉方式 电话投诉^窗口投诉
                    Node xnTemp2 = e.selectSingleNode("key[@label='COMPLAINT_PHONE']");
                    if (xnTemp2 != null) {
                        superviseStyle = xnTemp2.getText();
                    }
                    // 咨询方式 窗口咨询^电话咨询
//                    Node xnTemp3 = e.selectSingleNode("key[@label='PHONE']");
//                    if (xnTemp3 != null) {
//                        linkStyle = xnTemp3.getText();
//                    }
                    // 办公时间
                    Node xnTemp4 = e.selectSingleNode("key[@label='OFFICE_HOUR']");
                    if (xnTemp4 != null) {
                        slTime = xnTemp4.getText();
                    }
                    // 交通引导
                    Node xnTemp5 = e.selectSingleNode("key[@label='TRAFFIC_GUIDE']");
                    if (xnTemp5 != null) {
                        trafficGuide = xnTemp5.getText();
                    }
                    // 窗口名称
                    Node xnTemp6 = e.selectSingleNode("key[@label='NAME']");
                    if (xnTemp6 != null) {
                        ckmc = xnTemp6.getText();
                    }
                    // 窗口编号
                    Node xnTemp7 = e.selectSingleNode("key[@label='WINDOW_NUM']");
                    if (xnTemp7 != null) {
                        ckxh = xnTemp7.getText();
                    }
                    // 材料邮寄收件人
                    Node xnTemp8 = e.selectSingleNode("key[@label='ADDRESSEE']");
                    if (xnTemp8 != null) {
                        addressee = xnTemp8.getText();
                    }
                    // 材料收件人联系电话
                    Node xnTemp9 = e.selectSingleNode("key[@label='ADDRESSEE_PHONE']");
                    if (xnTemp9 != null) {
                        addresseePhone = xnTemp9.getText();
                    }
                    // 材料邮寄收件地址
                    Node xnTemp10 = e.selectSingleNode("key[@label='UNIT_URL']");
                    if (xnTemp10 != null) {
                        unitUrl = xnTemp10.getText();
                    }

                    // String tempString = String.valueOf(temp);
                    // acceptAddress += tempString + "&nbsp;&nbsp;";
                    acceptAddress += transactADDR + "&nbsp;&nbsp;";
                    acceptAddress += ckmc + "&nbsp;&nbsp;";
                    acceptAddress += slTime + "<br>";
                    temp++;
                }
            }
        }

        String linkTel = "";// 咨询电话 服务事项的电话
        String susTel = "";// 投诉电话
        String telType = "";// 投诉电话
        String consultXml = right.getStr("CONSULT_XML");
        if (StringUtil.isNotBlank(consultXml)) {
            if (consultXml.trim().length() > 0) {
                Document document1 = DocumentHelper.parseText(consultXml);
                Element root1 = document1.getRootElement();
                List<Element> nodeList1 = root1.elements("node");
                String phone = "";
                for (Element e : nodeList1) {
                    // 类型0标识咨询，1标识投诉
                    Node xnTemp1 = e.selectSingleNode("key[@label='TYPE']");
                    if (xnTemp1 != null) {
                        telType = xnTemp1.getText();
                    }
                    // 咨询投诉电话
                    Node xnTemp2 = e.selectSingleNode("key[@label='PHONE_NUMBER']");
                    if (xnTemp2 != null) {
                        phone = xnTemp2.getText();
                    }
                    // 咨询电话
                    if (StringUtil.isNotBlank(telType) && "0".equals(telType)) {
                        linkTel = phone;
                    }
                    // 投诉电话
                    if (StringUtil.isNotBlank(telType) && "1".equals(telType)) {
                        susTel = phone;
                    }
                }
            }
        }

        // 监督投诉电话
        audittask.setSupervise_tel(susTel);

        String lawTERM = right.getStr("LAW_TERM_XML");
        String lawXML = right.getStr("LAW_XML");
        String lawRelXML = right.getStr("LAW_REL_XML");
        if (StringUtil.isNotBlank(lawTERM.trim())) {
            Document document1 = DocumentHelper.parseText(lawTERM);
            Element root1 = document1.getRootElement();
            List<Element> nodeList1 = root1.elements("node");

            Document document2 = DocumentHelper.parseText(lawXML);
            Element root2 = document2.getRootElement();
            List<Element> nodeList2 = root2.elements("node");

            Document document3 = DocumentHelper.parseText(lawRelXML);
            Element root3 = document3.getRootElement();
            List<Element> nodeList3 = root3.elements("node");

            String byLaw = "";
            String issueDate = "";

            int i = 1;

            for (Element e : nodeList1) {
                String lawCode = "";
                // String RowGuid =
                // UUID.randomUUID().toString();
                String status = e.selectSingleNode("key[@label='STATUS']").getText();
                if ("1".equals(status)) {
                    // LAW_TERM_XML
                    String name = ""; // 法律名称
                    String office = ""; // 制定机关
                    String content = ""; // 法律内容
                    String basisCode = "";// 文号
                    String yjType = "";// 法定依据类型
                    String ljAddress = "";// 原文地址链接
                    String lawbasis = "";// 法定依据

                    String kx = "";

                    Node xnSue = e.selectSingleNode("key[@label='CREATE_TIME']");
                    if (xnSue != null) {
                        issueDate = xnSue.getText();
                    }
                    Node xnLawcode = e.selectSingleNode("key[@label='LAW_CODE']");
                    if (xnLawcode != null) {
                        lawCode = xnLawcode.getText();
                    }
                    Node xnContent = e.selectSingleNode("key[@label='CONTENT']");
                    if (xnContent != null) {
                        content = xnContent.getText();
                    }

                    int paixu = StringUtil.isBlank(e.selectSingleNode("key[@label='SORT']").getText()) ? 0
                            : Integer.parseInt(e.selectSingleNode("key[@label='SORT']").getText());

                    for (Element e2 : nodeList2) {
                        // LAW_XML
                        String code = e2.selectSingleNode("key[@label='CODE']").getText();
                        if (code.equals(lawCode)) {
                            // 法律文号 2013年7月12日中华人民共和国国务院令第637号
                            Node xnTemp2 = e2.selectSingleNode("key[@label='LAW_NUMBER']");
                            if (xnTemp2 != null) {
                                basisCode = xnTemp2.getText();
                            }
                            // 法律法规名称 《中华人民共和国外国人入境出境管理条例》
                            Node xnTemp = e2.selectSingleNode("key[@label='NAME']");
                            if (xnTemp != null) {
                                name = xnTemp.getText();
                            }
                            if (!name.contains("《")) {
                                if (!name.contains("》")) {
                                    name = "《" + name + "》";
                                }
                                else {
                                    name = "《" + name;
                                }
                            }
                            else {
                                if (!name.contains("》")) {
                                    name += name + "》";
                                }
                            }
                            // 下载地址
                            Node xnFilename = e2.selectSingleNode("key[@label='FILE_NAME']");
                            if (xnFilename != null) {
                                ljAddress = xnFilename.getText();
                            }
                            // 法律依据
                            Node xnLawbasis = e2.selectSingleNode("key[@label='LAW_BASIS']");
                            if (xnLawbasis != null) {
                                lawbasis = xnLawbasis.getText();
                            }
                            // 制定机关 全国人民代表大会常务委员会
                            Node xnOffice = e2.selectSingleNode("key[@label='OFFICE']");
                            if (xnOffice != null) {
                                office = xnOffice.getText();
                            }
                            break;
                        }
                    }

                    for (Element e3 : nodeList3) {
                        // LAW_REL_XML
                        String code = e3.selectSingleNode("key[@label='LAW_CODE']").getText();
                        if (code.equals(lawCode)) {
                            Node xnTemp2 = e3.selectSingleNode("key[@label='TYPE']");
                            if (xnTemp2 != null) {
                                yjType = xnTemp2.getText();
                            }
                            break;
                        }
                    }

                    byLaw += String.valueOf(i) + "&nbsp;&nbsp;&nbsp;";
                    byLaw += name + "&nbsp;";
                    byLaw += "(" + basisCode + ")&nbsp;&nbsp;&nbsp;";
                    byLaw += content + "<br>";
                    i++;
                }
            }
            // 法律依据
            audittask.setBy_law(byLaw);
        }

        // 受理条件
        String applyConditionXML = right.getStr("APPLY_CONDITION_XML");
        String condition = "";
        if (applyConditionXML.trim().length() > 0) {
            Document document = DocumentHelper.parseText(applyConditionXML);
            Element root = document.getRootElement();
            List<Element> listElement = root.elements("node");
            // int b = 1;
            for (Element e : listElement) {
                String status = "";
                Node xnSTATUS = e.selectSingleNode("key[@label='STATUS']");
                if (xnSTATUS != null) {
                    status = xnSTATUS.getText();
                }
                if ("1".equals(status)) {
                    // String bs = String.valueOf(b);
                    Node xnTemp2 = e.selectSingleNode("key[@label='NAME']");
                    if (xnTemp2 != null) {
                        condition += xnTemp2.getText();
                    }
                }
            }
            audittask.setAcceptcondition(condition);
        }
        else {
            audittask.setAcceptcondition("符合相关规定，提交相关申请材料");
        }

        // 办理地址
        audittask.setTransact_addr(acceptAddress);
        // audittask.setTransact_time(right.getStr("SL_Time"));//工作时间
        if (StringUtil.isNotBlank(slTime)) {
            audittask.setTransact_time(slTime);
        }
        else {
            audittask.setTransact_time("周一至周五 上午9:00-12:00 下午13:00-17:00");
        }

        // 外部流程图
        String OUT_FLOW_INFO = right.getStr("OUT_FLOW_XML");
        String taskoutimgguid = UUID.randomUUID().toString();
        if (OUT_FLOW_INFO != null && StringUtil.isNotBlank(OUT_FLOW_INFO)) {
            try {
                syncTaskFlowImg(OUT_FLOW_INFO, taskoutimgguid);
                audittask.setTaskoutimgguid(taskoutimgguid);
            }
            catch (Exception e) {
                log.info("外部流程图同步失败 =====>" + e.getMessage());
            }
        }
        WorkflowProcess workflowProcess = new WorkflowProcess();
        workflowProcess.setProcessName(right.getStr("NAME") + "【版本：" + versionNew + "】");
        workflowProcess.setProcessGuid(UUID.randomUUID().toString());
        WorkflowProcessVersion workflowProcessVersion = iWorkflowProcessService.addWorkflowProcess(workflowProcess,
                "同步业务服务", null);
        audittask.setPvguid(workflowProcessVersion.getProcessVersionGuid());
        audittask.setProcessguid(workflowProcessVersion.getProcessGuid());

        // 事项扩展信息表
        audittaskex.setOperateusername("同步服务");
        // 获取是否网上支付
        audittaskex.setOnlinepayment(payOnline);
        audittaskex.setOperatedate(new Date());
        audittaskex.setRowguid(UUID.randomUUID().toString());
        audittaskex.setTaskguid(taskguid);
        // 中介服务基本信息
        audittaskex.set("server_project_info", right.getStr("SERVE_PROJECT_XML"));

        // 数量限制
        audittaskex.set("LIMIT_NUMBER", limitNumber);
        // 标注文书
        audittaskex.set("PAPER_INFO", right.getStr("PAPER_XML"));
        // 权限划分
        audittaskex.setPowerdeline(authorityDivision);
        // 行使层级
        audittaskex.setUse_level(qlJiBie);
        // 行使内容
        audittaskex.setUse_level_c(exerciseContent);

        // 预约办理
        audittaskex.setIsyybl(isReserve);

        // 运行系统
        audittaskex.set("RUN_SYSTEM", isExistSystem);

        // 事项的子类型。
        audittaskex.set("SUB_TYPE", serviceType);
        // 材料邮寄收件人
        audittaskex.set("ADDRESSEE", addressee);
        // 特别程序种类名称
//        audittaskex.set("PROCEDURE_NAME",right.getStr("PROCEDURE_NAME"));
        // 法律救济
        audittaskex.set("COMPLAIN_INFO", right.getStr("COMPLAIN_WINDOW_REL_XML"));

        // 是否一窗办理
        audittaskex.set("IS_ONLINE_YICHUANG", isOnlineYichuang);
        // 办理形式
        audittaskex.set("HANDLE_FORM", handleForm);
        // 是否进驻大厅（1-是，0-否）
        audittaskex.setIf_jz_hall(isInHall);
        // 咨询方式
        audittaskex.set("CONSULT_URL", linkStyle);
        // 监督投诉方式
        // audittaskex.set("COMPLAIN_TYPE", superviseStyle);
        // 是否公示
        audittaskex.set("IS_PUBLIC", isPublic);
        // 办理方式
        audittaskex.set("HANDLINGMODE", handlingModel);

        /*******************************************办理结果***********************************************/
        String resultXML = right.getStr("RESULT_XML");
        String resultType = ""; // 结果类型
        String resultName = ""; // 结果名称
        String servicemode = ""; // 送达方式
        String resultSchdule = ""; // 有效时限
        String rssamp = "";
        String rsEnable = "";
        String resultExample = "";
        String resultGettype = "";
        String resultCliengguid = "";
        if (resultXML.trim().length() > 0) {
            Document document = DocumentHelper.parseText(resultXML);
            Element root = document.getRootElement();

            List<Element> listElement = root.elements("node");
            for (Element e : listElement) {
                // 结果类型 10证照、20批文、30合同、30登记表、30其他
                Node xnType = e.selectSingleNode("key[@label='RESULT_TYPE']");
                if (xnType != null) {
                    resultType = xnType.getText();
                }
                // 结果名称
                Node xnName = e.selectSingleNode("key[@label='RESULT_NAME']");
                if (xnName != null) {
                    resultName = xnName.getText();
                }
                // 送达方式（1-窗口领取，2-公告送达，3-邮寄送达，4-网站下载，5-其他）
                Node xnModel = e.selectSingleNode("key[@label='SERVICE_MODE']");
                if (xnModel != null) {
                    servicemode = xnModel.getText();
                }
                // 有效时限（1-1年，2-2年，3-3年，4-4年，5-5年，6-无期限，7-其他）
                Node xnTT = e.selectSingleNode("key[@label='RESULT_SCHEDULE']");
                if (xnTT != null) {
                    resultSchdule = xnTT.getText();
                }
                // 文书编号格式
                Node xnTT2 = e.selectSingleNode("key[@label='RSSAMP']");
                if (xnTT2 != null) {
                    rssamp = xnTT2.getText();
                }
                // 文号启用时间
                Node xnTT3 = e.selectSingleNode("key[@label='RSENABLE']");
                if (xnTT3 != null) {
                    rsEnable = xnTT3.getText();
                }
                // 办理结果附件名称
                Node xnTT4 = e.selectSingleNode("key[@label='RESULT_EXAMPLE']");
                if (xnTT4 != null) {
                    resultExample = xnTT4.getText();
                }
                // 办理结果网盘id
                Node xnTT5 = e.selectSingleNode("key[@label='RESULT_GETTYPE']");
                if (xnTT5 != null) {
                    resultGettype = xnTT5.getText();
                    String[] arr = resultGettype.split(";");
                    resultCliengguid = UUID.randomUUID().toString();
                    for (String fileNum : arr) {
                        List<Record> dvFile = new SyncToRightNewService().getfileInfo(fileNum);
                        if (dvFile.size() > 0) {
                            try {
                                String strAttachGuid = UUID.randomUUID().toString();
                                byte[] fileContent = (byte[]) dvFile.get(0).get("file_content");
                                int fileSize = fileContent.length;
                                String fileName = dvFile.get(0).getStr("file_name");
                                String[] temp = fileName.split("\\.");
                                String documentType = "." + temp[temp.length - 1].toString();
//                                new SyncToRightNewService().insertAttach(strAttachGuid, fileName, "", documentType,
//                                        "EpointMis", resultCliengguid, "", fileSize, new Date(), fileContent);
                                addAttach(strAttachGuid, resultCliengguid, fileName, documentType, fileContent);
                                // 模板材料
                                log.info("Material_InfoSuccess.log" + "插入结果材料：" + resultCliengguid + ",成功！");
                            }
                            catch (Exception e2) {
                                log.error("ErrorMaterial_Info.log" + "插入结果材料：" + resultCliengguid + ",失败！", e2);
                            }
                        }
                    }

                    /* log.info("开始同步审批结果，老事项taskguid=" + task.getRowguid());
                    AuditTaskResult taskResult = getTaskResult(task.getRowguid());
                    log.info("开始同步审批结果，老事项taskResult=" + taskResult);
                    if (taskResult != null) {
                        taskResult.setRowguid(UUID.randomUUID().toString());
                        taskResult.setOperateusername("同步服务");
                        taskResult.setOperatedate(new Date());
                        // taskguid 新的事项rowguid // 现在的问题是
                        taskResult.setTaskguid(taskguid);
                        // 结果类型 10证照、20批文、30合同、30登记表、30其他、 99无 审批50其他
                    //                        if (StringUtil.isNotBlank(resultType)) {
                    //                            if ("30".equals(right.getStr("RESULTTYPE"))) {
                    //                                resultType = "50";
                    //                            }
                    //                        }
                    //                        else {
                    //                            resultType = "99";
                    //                        }
                        // 注释Sharematerialguid，这个字段应该不用动
                        // taskResult.setSharematerialguid(UUID.randomUUID().toString());
                        // taskResult.setResulttype(Integer.parseInt(resultType));
                        // 结果名称
                        // taskResult.setResultname(right.getStr("RESULTNAME"));
                        // 送达方式 （1-窗口领取，2-公告送达，3-邮寄送达，4-网站下载，5-其他）
                        // taskResult.set("servicemode", right.getStr("servicemode"));
                        // 有效时限 （1-1年，2-2年，3-3年，4-4年，5-5年，6-无期限，7-其他）
                        // taskResult.set("resultSchdule", right.getStr("resultSchdule"));
                        // 文书编号格式
                        // taskResult.set("rssamp", right.getStr("rssamp"));
                        // 文号启用时间
                        // taskResult.set("rsEnable", right.getStr("rsEnable"));
                        // 办理结果样本
                        // taskResult.set("resultCliengguid", resultCliengguid);
                        // taskResult.setIs_print(0);
                        log.info("开始同步审批结果，新事项对应taskResult=" + taskResult);
                        auditTaskResult.addAuditResult(taskResult);
                    }*/

                }
            }
        }

        log.info("开始同步审批结果，老事项taskguid=" + task.getRowguid());
        AuditTaskResult taskResult = getTaskResult(task.getRowguid());
        log.info("开始同步审批结果，老事项taskResult=" + taskResult);
        if (taskResult != null) {
            taskResult.setRowguid(UUID.randomUUID().toString());
            taskResult.setOperateusername("同步服务");
            taskResult.setOperatedate(new Date());
            // taskguid 新的事项rowguid
            taskResult.setTaskguid(taskguid);
            log.info("开始同步审批结果，新事项对应taskResult=" + taskResult);
            auditTaskResult.addAuditResult(taskResult);
        }

        // 是否允许批量录入
        audittaskex.setIs_allowbatchregister(0);
        audittaskex.setSubjectnature(orgProperty);// 实施主体性质
        audittaskex.setTaskadduserdisplayname("同步服务");

        /****************************主题********************************/
        String titleXml = right.getStr("TITLE_XML");
        String personName = "";
        String companyName = "";
        String classType = "";
        if (titleXml.trim().length() > 0) {
            Document document1 = DocumentHelper.parseText(titleXml);
            Element root1 = document1.getRootElement();
            List<Element> nodeList1 = root1.elements("node");

            for (Element e : nodeList1) {
                // 主题分类 1 个人2 法人
                Node xnTemp1 = e.selectSingleNode("key[@label='CLASS_TYPE']");
                if (xnTemp1 != null) {
                    classType = xnTemp1.getText();
                }
                if ("1".equals(classType)) {
                    // 主题名称
                    Node xnTemp2 = e.selectSingleNode("key[@label='NAME']");
                    if (xnTemp2 != null) {
                        personName += xnTemp2.getText();
                    }
                }
                else {
                    // 主题名称
                    Node xnTemp2 = e.selectSingleNode("key[@label='NAME']");
                    if (xnTemp2 != null) {
                        companyName += xnTemp2.getText();
                    }
                }

            }
        }
        // 法人主题分类
        audittaskex.setTaskclass_forcompany(companyName);
        // 个人主题分类
        audittaskex.setTaskclass_forpersion(personName);
        audittaskex.setIs_simulation(0);
        audittaskex.setIsriskpoint(0);

        audittaskex.setIszijianxitong(0);
        // 是否支持预约办理
        audittaskex.setReservationmanagement("1");
        if (conductDepth.contains("^")) {
            String[] str = conductDepth.split("\\^");
            int[] a = new int[str.length];
            for (int i = 0; i < str.length; i++) {
                if (!"9".equals(str[i])) {
                    a[i] = Integer.parseInt(str[i]);
                }
            }
            Arrays.sort(a);
            int max = a[a.length - 1];
            if (max == 4 || max == 5 || max == 6 || max == 7) {
                audittaskex.setWebapplytype(1);// 网上申报类型：网上申报后直接办理
            }
            else if (max == 2 || max == 3) {
                audittaskex.setWebapplytype(1);// 网上申报类型对应：网上申报后预审
            }
            else if (max == 1) {
                audittaskex.setWebapplytype(0);// 网上申报类型：不允许网上申报
            }
            else {
                audittaskex.setWebapplytype(0);// 网上申报类型：不允许网上申报
            }
        }
        else if (conductDepth.contains(",")) {
            String[] str = conductDepth.split(",");
            int[] a = new int[str.length];
            for (int i = 0; i < str.length; i++) {
                if (!"9".equals(str[i])) {
                    a[i] = Integer.parseInt(str[i]);
                }
            }
            Arrays.sort(a);
            int max = a[a.length - 1];
            if (max == 4 || max == 5 || max == 6 || max == 7) {
                audittaskex.setWebapplytype(1);// 网上申报类型：网上申报后直接办理
            }
            else if (max == 2 || max == 3) {
                audittaskex.setWebapplytype(1);// 网上申报类型对应：网上申报后预审
            }
            else if (max == 1) {
                audittaskex.setWebapplytype(0);// 网上申报类型：不允许网上申报
            }
            else {
                audittaskex.setWebapplytype(0);// 网上申报类型：不允许网上申报
            }
        }
        else {
            if ("4".equals(conductDepth) || "5".equals(conductDepth) || "6".equals(conductDepth)
                    || "7".equals(conductDepth)) {
                audittaskex.setWebapplytype(1);// 网上申报类型：网上申报后直接办理
            }
            else if ("2".equals(conductDepth) || "3".equals(conductDepth)) {
                audittaskex.setWebapplytype(1);// 网上申报类型对应：网上申报后预审
            }
            else if ("1".equals(conductDepth) || "9".equals(conductDepth)) {
                audittaskex.setWebapplytype(0);// 网上申报类型：不允许网上申报
            }
            else {
                audittaskex.setWebapplytype(0);// 网上申报类型：不允许网上申报
            }
        }

        // 网办深度
        audittaskex.set("wangbanshendu", conductDepth);
        // 同步事项情形信息
//        audittaskex.set("CASE_SETTING_INFO", right.getStr("CASE_SETTING_INFO"));
        audittaskex.setSubjectnature("1");
        if ("370800".equals(audittask.getAreacode())) {
            // audittaskex.setNotify_ys("[#=ApplyerName#]你好，您于[#=ApplyDate#]在网上申请的“[#=ProjectName#]”，预审已获通过，请带齐申报资料到中心窗口办理。");
            audittaskex.setNotify_nys(
                    "[#=ApplyerName#]你好，您于[#=ApplyDate#]在网上申请的“[#=ProjectName#]”，由于[#=Reason#]，预审未获通过，特此通知。");
            audittaskex.setNotify_pz("[#=ApplyerName#]你好，您于[#=ApplyDate#]申请的“[#=ProjectName#]”，审批已获批准，特此通知。");
            audittaskex.setNotify_npz("[#=ApplyerName#]你好，您于[#=ApplyDate#]申请的“[#=ProjectName#]”，审批未获批准，特此通知。");
            // audittaskex.setNotify_sl("[#=ApplyerName#]你好，您于[#=ApplyDate#]申请的“[#=ProjectName#]”，经审查符合受理条件，决定受理。");
            audittaskex.setNotify_nsl(
                    "[#=ApplyerName#]你好，您于[#=ApplyDate#]申请的“[#=ProjectName#]”，由于[#=Reason#]，经审查不符合受理条件，决定不予受理");
        }

        int count = 0;
        count += commonDaoTo.insert(audittaskex);
        this.log.info("新增事项信息成功");
        count += commonDaoTo.insert(audittask);
        this.log.info("新增事项扩展信息成功");

        if ("1".equals(IS_EDITAFTERIMPORT) && count == 2) {
            deleteTaskByTaskguid(task.getRowguid());
            log.info("原事项自动置为历史版本=======>" + task.getTaskname() + "：" + task.getRowguid());
            if (audittask != null) {
                AuditOrgaServiceCenter center = null;
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("ouguid", audittask.getOuguid());
                List<AuditOrgaServiceCenter> centers = iAuditOrgaServiceCenter
                        .getAuditOrgaServiceCenterByCondition(sql.getMap()).getResult();
                if (centers != null && centers.size() > 0) {
                    center = centers.get(0);
                    if (StringUtil.isNotBlank(center.getRowguid()) && StringUtil.isNotBlank(audittask.getTask_id())) {
                        iHandleCenterTask.initCenterTaskbyTaskids(center.getRowguid(), audittask.getTask_id());
                    }
                }
                AuditTask tasknew = iAuditTask.getAuditTaskByGuid(audittask.getRowguid(), true).getResult();

                List<SpglDfxmsplcjdsxxxb> list = ispgldfxmsplcjdsxxxb
                        .getNeedAddNewVersionByItemId(tasknew.getItem_id());
                log.info("事项itemid:" + tasknew.getItem_id() + ",待同步的spgl事项表数据：" + list);
                for (SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb1 : list) {
                    SpglDfxmsplcjdsxxxb spgldfxmsplcjdsxxxb = new SpglDfxmsplcjdsxxxb();
                    spgldfxmsplcjdsxxxb.setRowguid(UUID.randomUUID().toString());
                    spgldfxmsplcjdsxxxb.setDfsjzj(spglDfxmsplcjdsxxxb1.getDfsjzj());
                    spgldfxmsplcjdsxxxb.setXzqhdm(spglDfxmsplcjdsxxxb1.getXzqhdm());
                    spgldfxmsplcjdsxxxb.setSplcbm(spglDfxmsplcjdsxxxb1.getSplcbm());
                    spgldfxmsplcjdsxxxb.setSplcbbh(spglDfxmsplcjdsxxxb1.getSplcbbh());
                    spgldfxmsplcjdsxxxb.setSpjdxh(spglDfxmsplcjdsxxxb1.getSpjdxh());
                    spgldfxmsplcjdsxxxb.setSpsxbbh(Double.valueOf(audittask.getVersion()));
                    spgldfxmsplcjdsxxxb.setSpsxmc(audittask.getTaskname());
                    spgldfxmsplcjdsxxxb.setSpsxbm(audittask.getItem_id());

                    AuditSpBasetask basetask = iauditspbasetask
                            .getAuditSpBasetaskByrowguid(spglDfxmsplcjdsxxxb1.getDfsjzj()).getResult();
                    if (basetask != null) {
                        spgldfxmsplcjdsxxxb.setDybzspsxbm(basetask.getTaskcode());
                        spgldfxmsplcjdsxxxb.setSflcbsx(
                                StringUtil.isNotBlank(basetask.getSflcbsx()) ? Integer.valueOf(basetask.getSflcbsx())
                                        : ZwfwConstant.CONSTANT_INT_ZERO); // 是否里程碑事项。默认0
                    }
                    else {
                        spgldfxmsplcjdsxxxb.setSflcbsx(0);
                        spgldfxmsplcjdsxxxb.setDybzspsxbm("9990");
                    }
                    String istellpromise = audittask.getStr("is_tellpromise");
                    if (StringUtil.isBlank(istellpromise)) {
                        istellpromise = "0";
                    }
                    spgldfxmsplcjdsxxxb.setSfsxgzcnz(Integer.parseInt(istellpromise));// 是否实行告知承诺制 默认0

                    spgldfxmsplcjdsxxxb.setBjlx(audittask.getType());
                    if (audittask.getApplyertype().indexOf(ZwfwConstant.APPLAYERTYPE_QY) != -1
                            && audittask.getApplyertype().indexOf(ZwfwConstant.APPLAYERTYPE_GR) != -1) {
                        spgldfxmsplcjdsxxxb.setSqdx(3);
                    }
                    else {
                        if (audittask.getApplyertype().indexOf(ZwfwConstant.APPLAYERTYPE_GR) != -1) {
                            spgldfxmsplcjdsxxxb.setSqdx(1);
                        }
                        else if (audittask.getApplyertype().indexOf(ZwfwConstant.APPLAYERTYPE_QY) != -1) {
                            spgldfxmsplcjdsxxxb.setSqdx(2);
                        }
                    }
                    spgldfxmsplcjdsxxxb.setBljgsdfs("2"); // 办理送达方式 默认 申请对象窗口领取
                    spgldfxmsplcjdsxxxb.setSpsxblsx(audittask.getPromise_day());
                    spgldfxmsplcjdsxxxb.setSpbmbm(iouservice.getOuByOuGuid(audittask.getOuguid()).getOucode());
                    spgldfxmsplcjdsxxxb.setSpbmmc(audittask.getOuname());
                    spgldfxmsplcjdsxxxb.setQzspsxbm(null);
                    spgldfxmsplcjdsxxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                    spgldfxmsplcjdsxxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                    ispgldfxmsplcjdsxxxb.insert(spgldfxmsplcjdsxxxb);
                }
            }
        }
        commonDaoTo.commitTransaction();
        // 如果是乡镇事项执行下面操作
        if (!"000".equals(Areacode.substring(6, 9))) {
            DoAuditTaskDelegate(audittask, dvOuInfo);
        }

        // 生成默认流程
        handleDefaultFlow(audittask, audittask.getAreacode());
        // 同步材料
        String materialREL = right.getStr("MATERIAL_REL_XML");
        if (StringUtil.isNotBlank(materialREL)) {
            String materialInfo = right.getStr("MATERIAL_XML");
            String materialEXT = right.getStr("MATERIAL_EXT_XML");
            log.info("RightProcess.log,开始新增权力--取出材料成功!");
            insertMateria(materialInfo, materialEXT, materialREL, taskguid, task.getRowguid());
        }

        String question = right.getStr("COMMON_QUESTION");
        if (question != null && StringUtil.isNotBlank(question)) {
            // 同步常见问题
            syncTaskFaq(question, task.getTask_id());
            this.log.info("同步常见问题成功");
        }
    }

    /**
     * 
     * 是否生成默认流程
     * 
     * @param ywGuid
     * @param auditTaskNew
     * @param areaCode
     */
    public void handleDefaultFlow(AuditTask auditTaskNew, String areaCode) {
        IAuditTaskRiskpoint iAuditTaskRiskpoint = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskRiskpoint.class);
        IWorkflowActivityService iWorkflowActivityService = ContainerFactory.getContainInfo()
                .getComponent(IWorkflowActivityService.class);
        IWorkflowProcessVersionService iWorkflowProcessVersionService = ContainerFactory.getContainInfo()
                .getComponent(IWorkflowProcessVersionService.class);
        List<AuditTaskRiskpoint> auditTaskRiskpoints = new ArrayList<AuditTaskRiskpoint>();
        // 受理
        String firstActivityGuid = UUID.randomUUID().toString();
        // 审核
        String thirdActivityGuid = UUID.randomUUID().toString();
        // 办结
        String fifthActivityGuid = UUID.randomUUID().toString();
        for (int i = 0; i < FWConstants.COMMONTHREE; i++) {
            AuditTaskRiskpoint auditTaskRiskpoint = new AuditTaskRiskpoint();
            auditTaskRiskpoint.setRowguid(UUID.randomUUID().toString());
            if (i == 0) {
                auditTaskRiskpoint.setActivityname("受理");
                auditTaskRiskpoint.setActivityguid(firstActivityGuid);
                auditTaskRiskpoint.setOrdernum(50);
            }
            else if (i == 1) {
                auditTaskRiskpoint.setActivityname("审核");
                auditTaskRiskpoint.setActivityguid(thirdActivityGuid);
                auditTaskRiskpoint.setOrdernum(30);
            }
            else if (i == 2) {
                auditTaskRiskpoint.setOrdernum(10);
                auditTaskRiskpoint.setActivityname("办结");
                auditTaskRiskpoint.setActivityguid(fifthActivityGuid);
            }
            auditTaskRiskpoint.setAdddate(new Date());
            auditTaskRiskpoint.setTaskguid(auditTaskNew.getRowguid());
            auditTaskRiskpoint.setOperatedate(new Date());
            auditTaskRiskpoint.setOperateusername("同步岗位服务");
            auditTaskRiskpoint.setRiskpointid(UUID.randomUUID().toString());
            auditTaskRiskpoints.add(auditTaskRiskpoint);
            iAuditTaskRiskpoint.addAuditTaskRiskpoint(auditTaskRiskpoint);
            if (i == 0) {
                auditTaskRiskpoint.set("nextactivityguid", thirdActivityGuid);
            }
            else if (i == 1) {
                auditTaskRiskpoint.set("nextactivityguid", fifthActivityGuid);
            }
            else if (i == 2) {
                List<WorkflowActivity> activitylist = iWorkflowActivityService
                        .selectAllByProcessVersionGuid(auditTaskNew.getPvguid(), " and activityName = '结束'");
                auditTaskRiskpoint.set("nextactivityguid", activitylist.get(0).getActivityGuid());
            }
        }
        List<Activity> activitiList = SyncCommonUtil.sortActivities(auditTaskRiskpoints, auditTaskNew);
        WorkflowParameter9 workFlowParameter9 = new WorkflowParameter9();
        workFlowParameter9.setProcessVersionGuid(auditTaskNew.getPvguid());
        // 生成流程
        iWorkflowProcessVersionService.designProcess(auditTaskNew.getPvguid(), activitiList, true);
        // 流程配置修改
        SyncCommonUtil.handleWorkFlow(auditTaskNew.getPvguid(), areaCode);
    }

    @SuppressWarnings("unchecked")
    protected void insertMateria(String materialINFO, String materialEXT, String materialREL, String newtaskguid,
            String oldtaskGuid) throws DocumentException {
        // 申报材料
        // 事项材料对应关系
        Document document1 = DocumentHelper.parseText(materialREL);
        Element root1 = document1.getRootElement();
        List<Element> nodeList1 = root1.elements("node");
        // 材料基本信息
        Document document2 = DocumentHelper.parseText(materialINFO);
        Element root2 = document2.getRootElement();
        List<Element> nodeList2 = root2.elements("node");
        // 材料扩展信息
        Document document3 = DocumentHelper.parseText(materialEXT);
        Element root3 = document3.getRootElement();
        List<Element> nodeList3 = root3.elements("node");

        for (Element e : nodeList1) {
            String rowguid = UUID.randomUUID().toString();// 材料生成xml唯一标识 ROWGUID
                                                          // VARCHAR2(50)
            String materialName = "";// 材料名称 MATERIALNAME VARCHAR2(2000)
            String materialKind = "";// 材料形式 MaterialKind VARCHAR2(2) 代码项：材料形式 电子/纸质

            String isMustSubmit = "";// 是否必须提交 Is_MustSubmit
            String clMB = "";// 材料模版
            String clTXSL = "";// 材料填写示例
            int materialNum = 0;// 份数
            int ordernum = 0;// 排序
            String fyjNum = "";// 复印件份数
            String clJZ = "";// 材料介质
            String ywCategory = "";// 业务分类

            // 新增字段1031
            String materialBasis = "";// 设定依据
            String orginal = "";// 来源渠道
            String remark = "";// 备注信息
            String fileExplanation = ""; // 填报须知
            String gmmaterialType = "";// 材料类型 1原件/2复印件
            String reduceBasis = "";// 提交方式（1-窗口提交，2-网上提交，3-快递邮寄，4-部门共享核验）
            String hasReview = "";// 材料是否可承诺
            String paperStandard = "";// 纸质材料份数
            String clCategory = "";// 材料分类 1 表格类 2文本类 3 结果文书类
            String what = "";// 纸质材料规格
            String extendFild = "";// 2021-12-22 拓展字段,电子证照关联字段
            clMB = UUID.randomUUID().toString();
            clTXSL = UUID.randomUUID().toString();

            String materialCode = e.selectSingleNode("key[@label='MATERIAL_CODE']").getText();// 材料编码（事项材料信息表）

            // ----------同步材料start-----------
            try {

                Node xnTemp1 = e.selectSingleNode("key[@label='MUST']");
                if (xnTemp1 != null) {
                    isMustSubmit = xnTemp1.getText();
                }
                Node xnTemp2 = e.selectSingleNode("key[@label='ORIGIN']");
                if (xnTemp2 != null) {
                    materialNum = Integer.parseInt(xnTemp2.getText());
                }
                Node xnTemp3 = e.selectSingleNode("key[@label='COPY']");
                if (xnTemp3 != null) {
                    fyjNum = xnTemp3.getText();
                }

                for (Element e2 : nodeList2) {
                    String code = e2.selectSingleNode("key[@label='CODE']").getText();
                    if (code.equals(materialCode)) {
                        Node xnTemp4 = e2.selectSingleNode("key[@label='NAME']");
                        if (xnTemp4 != null) {
                            materialName = xnTemp4.getText();
                        }
                        Node xnTemp5 = e2.selectSingleNode("key[@label='DOCUMENT_FORMAT']");
                        if (xnTemp5 != null) {
                            materialKind = xnTemp5.getText();
                        }
                        Node xnTemp6 = e2.selectSingleNode("key[@label='MATERIAL_TYPE']");
                        if (xnTemp6 != null) {
                            gmmaterialType = xnTemp6.getText();
                        }
                        Node xnTemp7 = e2.selectSingleNode("key[@label='REDUCE_BASIS']");
                        if (xnTemp7 != null) {
                            reduceBasis = xnTemp7.getText();
                        }
                        Node xnTemp8 = e2.selectSingleNode("key[@label='HAS_REVIEW']");
                        if (xnTemp8 != null) {
                            hasReview = xnTemp8.getText();
                        }
                        Node xnTemp9 = e2.selectSingleNode("key[@label='BUSINESS_TYPE']");
                        if (xnTemp9 != null) {
                            ywCategory = xnTemp9.getText();
                        }
                        Node xnTemp10 = e2.selectSingleNode("key[@label='REMARK']");
                        if (xnTemp10 != null) {
                            remark = xnTemp10.getText();
                        }
                        Node xnTemp11 = e2.selectSingleNode("key[@label='FILLING_EXPLANATION']");
                        if (xnTemp11 != null) {
                            fileExplanation = xnTemp11.getText();
                        }

                        Node xnTemp12 = e2.selectSingleNode("key[@label='TYPE']");
                        if (xnTemp12 != null) {
                            clCategory = xnTemp12.getText();
                        }
                        Node xnTemp13 = e2.selectSingleNode("key[@label='SORT_ORDER']");
                        if (xnTemp13 != null) {
                            ordernum = Integer.parseInt(xnTemp13.getText());
                        }
                        break;
                    }
                }
                String materialDescription = "";
                for (Element e3 : nodeList3) {
                    String code = e3.selectSingleNode("key[@label='CODE']").getText();// 材料编码
                    if (code.equals(materialCode)) {
                        Node xnTemp = e3.selectSingleNode("key[@label='PUBLISHER']");
                        if (xnTemp != null) {
                            clJZ = xnTemp.getText();
                        }
                        Node xnTempRemark = e3.selectSingleNode("key[@label='REMARK']");
                        if (xnTempRemark != null) {
                            materialDescription = xnTempRemark.getText();
                        }

                        // 设定依据
                        Node xnMaterialBasis = e3.selectSingleNode("key[@label='MATERIAL_BASIS']");
                        if (xnMaterialBasis != null) {
                            materialBasis = xnMaterialBasis.getText();
                        }
                        // 来源渠道
                        Node xnOrginal = e3.selectSingleNode("key[@label='ORIGINAL']");
                        if (xnOrginal != null) {
                            orginal = xnOrginal.getText();
                        }
                        // 纸质材料份数
                        Node xnpaperStandard = e3.selectSingleNode("key[@label='PAPER_STANDARD']");
                        if (xnpaperStandard != null) {
                            paperStandard = xnpaperStandard.getText();
                        }
                        // 纸质材料规格
                        Node xnWHAT = e3.selectSingleNode("key[@label='WHAT']");
                        if (xnWHAT != null) {
                            what = xnWHAT.getText();
                        }

                        // 材料模板
                        Node xnTempBlank = e3.selectSingleNode("key[@label='BLANK']");
                        if (xnTempBlank != null) {

                            // String filePath = NewCommonUtil.trimEnd(xnTempBlank.getText(), ';');
                            String filePath = xnTempBlank.getText();
                            String[] arr = filePath.split(";");
                            for (String fileNum : arr) {
                                List<Record> dvFile = service.getfileInfo(fileNum);
                                if (dvFile.size() > 0) {
                                    try {
                                        String strAttachGuid = UUID.randomUUID().toString();

                                        byte[] fileContent = null;
                                        int fileSize = 0;
                                        if (StringUtil.isNotBlank(dvFile.get(0).get("file_content"))) {
                                            fileContent = (byte[]) dvFile.get(0).get("file_content");
                                            fileSize = fileContent.length;
                                        }

                                        String fileName = dvFile.get(0).getStr("file_name");
                                        String[] temp = fileName.split("\\.");
                                        String documentType = "." + temp[temp.length - 1].toString();

//                                        service.insertAttach(strAttachGuid, fileName, "", documentType, "EpointMis",
//                                                clMB, "", fileSize, new Date(), fileContent);
                                        addAttach(strAttachGuid, clMB, fileName, documentType, fileContent);
                                        // 模板材料
                                        log.info("Material_InfoSuccess.log" + "插入模板材料：" + clMB + "，权力行" + newtaskguid
                                                + "  成功！。");
                                    }
                                    catch (Exception e2) {
                                        log.error("ErrorMaterial_Info.log" + "权力行" + newtaskguid, e2);
                                    }
                                }
                            }
                        }

                        // 填写示例
                        Node xnTempSample = e3.selectSingleNode("key[@label='SAMPLE']");
                        if (xnTempSample != null) {
                            // String filePath = NewCommonUtil.trimEnd(xnTempSample.getText(), ';');
                            String filePath = xnTempSample.getText();
                            String[] arr = filePath.split(";");
                            for (String fileNum : arr) {
                                List<Record> dvFile = service.getfileInfo(fileNum);
                                if (dvFile.size() > 0) {
                                    try {
                                        String strAttachGuid = UUID.randomUUID().toString();
                                        byte[] fileContent = null;
                                        int fileSize = 0;
                                        if (StringUtil.isNotBlank(dvFile.get(0).get("file_content"))) {
                                            fileContent = (byte[]) dvFile.get(0).get("file_content");
                                            fileSize = fileContent.length;
                                        }

                                        String fileName = dvFile.get(0).getStr("file_name");
                                        String[] temp = fileName.split("\\.");
                                        String documentType = "." + temp[temp.length - 1].toString();

//                                        service.insertAttach(strAttachGuid, fileName, "", documentType, "EpointMis",
//                                                clTXSL, "", fileSize, new Date(), fileContent);
                                        addAttach(strAttachGuid, clTXSL, fileName, documentType, fileContent);

                                        // 模板材料
                                        log.info("Sample_InfoSuccess.log" + "插入填写示例：" + clTXSL + "，权力行" + newtaskguid
                                                + "  成功！。");
                                    }
                                    catch (Exception e2) {
                                        log.error("ErrorSample_Info.log" + "权力行" + newtaskguid, e2);
                                    }
                                }
                            }
                        }
                        // 扩展字段，电子证照相关字段
                        Node extendField = e3.selectSingleNode("key[@label='EXTENDFIELD']");
                        if (extendField != null) {
                            extendFild = extendField.getText();
                        }
                        break;
                    }
                }

                log.info("rightMATERIAL.log" + "权力插入材料唯一标示：下放材料标识" + materialCode + "，权力行" + newtaskguid + "  成功！。");
            }
            catch (Exception e2) {
                log.error("rightMATERIALError.log" + "权力解析材料异常材料唯一标示：" + materialCode + "，权力行" + newtaskguid + "：", e2);
            }

            AuditTaskMaterial auditTaskMaterial = new AuditTaskMaterial();
            auditTaskMaterial.setTaskguid(newtaskguid);
            auditTaskMaterial.setOperateusername("同步服务");
            auditTaskMaterial.setOperatedate(new Date());
            auditTaskMaterial.setOrdernum(0);
            auditTaskMaterial.setRowguid(UUID.randomUUID().toString());
            // 材料类型 表格：10 附件 20
            auditTaskMaterial.setType(20);
            if ("1".equals(materialKind)) {
                auditTaskMaterial.setSubmittype("20");
            }
            else {
                auditTaskMaterial.setSubmittype("35");
            }
            auditTaskMaterial.setMaterialname(materialName);
            // 1.来源渠道:(审批系统对应代码项:1-申请人自备;2-政府部门核发;3-其他)
            if ("10".equals(orginal)) {
                orginal = "1";
            }
            if ("20".equals(orginal)) {
                orginal = "2";
            }
            else {
                orginal = "3";
            }
            auditTaskMaterial.setFile_source(orginal);
            // auditTaskMaterial.set("isxtendfield", extendFild);
            if ("1".equals(isMustSubmit)) {
                auditTaskMaterial.setNecessity(10);
            }
            else {
                auditTaskMaterial.setNecessity(20);
            }
            // 需注意可能是旧材料的id
            auditTaskMaterial.setMaterialid(UUID.randomUUID().toString());
            auditTaskMaterial.setPage_num(materialNum);
            auditTaskMaterial.set("COPY_NUM", fyjNum);
            auditTaskMaterial.setExampleattachguid(clTXSL);
            auditTaskMaterial.setTemplateattachguid(clMB);
            commonDaoTo.insert(auditTaskMaterial);
            commonDaoTo.commitTransaction();
            this.log.info("同步材料成功");

            // ----------同步材料end-----------
        }
    }

    /**
     * API:同步常见问题
     */
    public void syncTaskFaq(String questionXml, String taskId) {
        List<AuditTaskFaq> list = getTaskfaq(taskId);
        if (list != null && list.size() > 0) {
            for (AuditTaskFaq faq : list) {
                int result = commonDaoTo.delete(faq);
                if (result > 0) {
                    log.info("====>>>删除常见问题成功");
                }
                else {
                    log.info("====>>>删除常见问题失败");
                }
            }
        }
        String question; // 问题
        String answer; // 回答
        String sortOrder; // 排序
        if (questionXml.trim().length() > 0) {
            Document document1;
            try {
                document1 = DocumentHelper.parseText(questionXml);

                Element root1 = document1.getRootElement();
                List<Element> nodeList1 = root1.elements("node");
                for (Element e : nodeList1) {
                    try {
                        question = ""; // 问题
                        answer = ""; // 回答
                        sortOrder = ""; // 排序

                        Node xnTemp1 = e.selectSingleNode("key[@label='QUESTION']");
                        if (xnTemp1 != null) {
                            question = xnTemp1.getText();
                        }
                        Node xnTemp2 = e.selectSingleNode("key[@label='ANSWER']");
                        if (xnTemp2 != null) {
                            answer = xnTemp2.getText();
                        }
                        Node xnTemp3 = e.selectSingleNode("key[@label='SORT_ORDER']");
                        if (xnTemp3 != null) {
                            sortOrder = xnTemp3.getText();
                        }

                        AuditTaskFaq auditTaskFaq = new AuditTaskFaq();
                        String strRowGuid = UUID.randomUUID().toString();
                        auditTaskFaq.setRowguid(strRowGuid);
                        auditTaskFaq.setOperateusername("同步服务");
                        auditTaskFaq.setOperatedate(new Date());
                        auditTaskFaq.setTaskid(taskId);
                        auditTaskFaq.setQuestion(question);
                        auditTaskFaq.setOrdernum(1);
                        auditTaskFaq.setAnswer(answer);
                        commonDaoTo.insert(auditTaskFaq);
                        commonDaoTo.commitTransaction();

                        log.info("QuestionProcess.log" + "开始新增权力--常见问题解析成功!，行" + strRowGuid + ",");
                    }
                    catch (Exception e2) {
                        log.error("QuestionProcessProcessError.log" + "开始新增权力--常见问题解析异常", e2);
                    }
                }
            }
            catch (DocumentException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

    }

    /**
     * 同步外部流程图
     * @param OUT_FLOW_INFO
     * @param taskId
     */
    public void syncTaskFlowImg(String OUT_FLOW_INFO, String taskId) {
        // 解析XML
        Document document = null;
        try {
            document = DocumentHelper.parseText(OUT_FLOW_INFO);
        }
        catch (DocumentException e) {
            log.info("====>>>解析同步外部流程图失败");
        }

        // 解析外部流程图
        Element root = document.getRootElement();
        List<Element> nodeList = root.elements("node");
        for (Element e : nodeList) {
            // String status = e.selectSingleNode("key[@label='STATUS']").getText();
            // if ("1".equals(status)) {
            // 上传流程图网盘ID
            Node xnTemp = e.selectSingleNode("key[@label='URL']");
            if (xnTemp != null) {
                String cliengGuid = UUID.randomUUID().toString();
                String url = xnTemp.getText();
                String[] arr = url.split(";");
                for (String str : arr) {
                    List<Record> dvFile = service.getfileInfo(str);
                    if (dvFile.size() > 0) {
                        try {
                            String strAttachGuid = UUID.randomUUID().toString();

                            byte[] fileContent = null;
                            int fileSize = 0;
                            if (StringUtil.isNotBlank(dvFile.get(0).get("file_content"))) {
                                fileContent = (byte[]) dvFile.get(0).get("file_content");
                                fileSize = fileContent.length;
                            }

                            String fileName = dvFile.get(0).getStr("file_name");
                            String documentType = ".jpg";

//                            service.insertAttach(strAttachGuid, fileName, "", // upFileName.ContentType,
//                                    documentType, "EpointMis", cliengGuid, "", fileSize, new Date(), fileContent);

                            addAttach(strAttachGuid, cliengGuid, fileName, documentType, fileContent);

                            log.info("rightOut_Flow.log" + "权力插入外部流程图：" + cliengGuid + "，权力行" + taskId + "  成功！。");

                        }
                        catch (Exception e2) {
                            log.error("rightError.log" + "权力外部流程图获取异常（没有此图），行" + taskId + "：XML：" + OUT_FLOW_INFO
                                    + "||||", e2);
                        }
                    }
                }
            }
            // }
        }
    }

    /**
     * 同步法律依据
     * @param OUT_FLOW_INFO
     * @param taskId
     */
    public static String syncLaw(String law) {
        String result = "";
        // 解析XML
        Document document = null;
        try {
            document = DocumentHelper.parseText(law);
        }
        catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        List<Element> datas = root.elements("DATAAREA");
        int i = 1;
        for (Element data : datas) {
            Element nodeList = data.element("LAWS");
            List<Element> s = nodeList.elements("LAW");
            for (Element p : s) {
                Element NAME = p.element("NAME");
                Element CONTENT = p.element("CONTENT");
                result += String.valueOf(i) + "、" + NAME.getStringValue() + CONTENT.getStringValue();
                i++;
            }
        }
        return result;

    }

    /**
     * 山东附件库
     * @param url
     * @return
     * @throws Exception
     */
    public static Map<String, Object> downloadFile(String url) throws Exception {
        Map<String, Object> map = new HashMap<>();
        HttpClient client = new HttpClient();
        client.getParams().setContentCharset("UTF-8");
        GetMethod getMethod = new GetMethod(url);
        client.executeMethod(getMethod);
        long len = getMethod.getResponseContentLength();
        InputStream inputStream = getMethod.getResponseBodyAsStream();
        map.put("length", len);
        map.put("stream", inputStream);
        return map;
    }

    public void addAttach(String attachGuid, String CliengGuid, String filename, String fileType, byte[] pic) {
        try {
            if (pic.length > 0) {
                FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                frameAttachInfo.setAttachGuid(attachGuid);
                frameAttachInfo.setCliengGuid(CliengGuid);
                frameAttachInfo.setAttachFileName(filename);
                frameAttachInfo.setCliengTag("事项同步附件");
                frameAttachInfo.setUploadUserGuid("");
                frameAttachInfo.setUploadUserDisplayName("");
                frameAttachInfo.setUploadDateTime(new Date());
                frameAttachInfo.setContentType(fileType);
                frameAttachInfo.setAttachLength(Long.valueOf((long) pic.length));
                ByteArrayInputStream input = new ByteArrayInputStream(pic);
                attachService.addAttach(frameAttachInfo, input);
                input.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Record> getQLSXnew() {
        String areacode = StringUtil.getNotNullString(syncareacode);
        String sql = "";
        if (StringUtil.isBlank(areacode)) {
            sql = "select * from (select * from JI_SXFF.qlt_qlsx_hb_new where gt_state ='0' ORDER BY update_date,region_code,ITEM_CODE,version asc) where rownum<=40";
        }
        else {
            sql = "select * from (select * from JI_SXFF.qlt_qlsx_hb_new where nvl(gt_state,'0')='0' and region_code='"
                    + areacode + "' ORDER BY update_date,region_code,ITEM_CODE,version asc) where rownum<=40";
        }
        return commonDaoFrom.findList(sql, Record.class);
    }

    public Record getOuInfo(String ouCode) {
        String sql = "select OUName,o.OUGuid,OUCODE,e.AREACODE from Frame_OU o INNER JOIN frame_ou_extendinfo e on o.OUGUID = e.OUGUID where OUCode=?";
        return commonDaoTo.find(sql, Record.class, ouCode);
    }

    public AuditTaskResult getTaskResult(String taskguid) {
        String sql = "SELECT * FROM audit_task_result WHERE TASKGUID =?";
        return commonDaoTo.find(sql, AuditTaskResult.class, taskguid);
    }

    public int updateTaskResult(String taskguid, String resultguid) {
        String sql = "UPDATE audit_task_result SET TASKGUID=? WHERE rowguid =?";
        return commonDaoTo.execute(sql, taskguid, resultguid);
    }

    public String getItemidFromRight(String TASKHANDLEITEM, String ITEM_CODE) {
        if (StringUtil.isNotBlank(TASKHANDLEITEM)) {
            return TASKHANDLEITEM;
        }
        else {
            return ITEM_CODE;
        }
    }

    public void updateSyncSign(String innerCode, String ORG_CODE) {
        String sql = "Update JI_SXFF.qlt_qlsx_hb_new set gt_state='1',EX_DATE=SYSDATE where ITEM_CODE=?1 and ORG_CODE=?2";
        commonDaoFrom.execute(sql, innerCode, ORG_CODE);
        commonDaoFrom.commitTransaction();
        commonDaoFrom.close();
    }

    public void updateSyncSignbyoucode(String orgCode) {
        String sql = "Update JI_SXFF.qlt_qlsx_hb_new set gt_state='1',EX_DATE=SYSDATE where ORG_CODE=?";
        commonDaoFrom.execute(sql, orgCode);
        commonDaoFrom.commitTransaction();
        commonDaoFrom.close();
    }

    /**
     * API:获取事项
     */
    public List<AuditTask> getAuditTask1(String innerCode) {
        String sql = "SELECT * FROM audit_task where item_id=? and Is_editafterimport=1 and IFNULL(IS_HISTORY,0)= 0 and is_enable = 1 ";
        return commonDaoTo.findList(sql, AuditTask.class, innerCode);
    }

    /**
     * API:获取待确认事项中事项
     */
    public AuditTask getAuditTask4(String innerCode) {
        String sql = "SELECT * FROM audit_task where item_id=? and Is_editafterimport=4"
                + " AND task_id is not null and IFNULL(IS_HISTORY,0)= 0 ORDER BY Operatedate DESC LIMIT 1";
        return commonDaoTo.find(sql, AuditTask.class, innerCode);
    }

    /**
     * API:更新：获取原事项扩展信息
     */
    public AuditTaskExtension getTaskEx(String taskguid) {
        String sql = "SELECT * FROM AUDIT_TASK_EXTENSION where taskguid=?";
        return commonDaoTo.find(sql, AuditTaskExtension.class, taskguid);
    }

    public int deleteTaskByTaskguid(String taskguid) {
        String sql = "update AUDIT_TASK set OperateDate = NOW(), OperateUserName = '同步服务自动置为历史版本', IS_EDITAFTERIMPORT =5, IS_HISTORY=1 where rowguid =?";

        return commonDaoTo.execute(sql, taskguid);
    }

    public void updateQlsxSync(String orgcode, String string) {
        String sql = "Update JI_SXFF.qlt_qlsx_hb_new set gt_state='" + string + "',EX_DATE=SYSDATE where ORG_CODE='"
                + orgcode + "'";
        commonDaoFrom.execute(sql);
        commonDaoFrom.commitTransaction();
        commonDaoFrom.close();
    }

    public void updateJnQlsxSync(String rowguid, String string) {
        String sql = "Update JI_SXFF.qlt_qlsx_hb_new set gt_state='" + string + "',EX_DATE=SYSDATE where ROWGUID='"
                + rowguid + "'";
        commonDaoFrom.execute(sql);
        commonDaoFrom.commitTransaction();
        commonDaoFrom.close();

    }

    private List<AuditTaskFaq> getTaskfaq(String taskid) {
        String sql = "select * from audit_task_faq where taskid=?";
        return commonDaoTo.findList(sql, AuditTaskFaq.class, taskid);
    }

    public void DoAuditTaskDelegate(AuditTask task, Record ouinfo) {
        // 标准版设计audittaskdelegate永远一条记录，在原基础update（先select 后update）
        // 此处考虑直接删除后新增
        String sql = "delete from AUDIT_TASK_DELEGATE where Taskid=?";
        commonDaoTo.execute(sql, task.getTask_id());
        AuditTaskDelegate audittaskdelegate = new AuditTaskDelegate();
        audittaskdelegate.setOperateusername("同步作业");
        audittaskdelegate.setOperatedate(new Date());
        audittaskdelegate.setRowguid(UUID.randomUUID().toString());
        audittaskdelegate.setTaskguid(task.getRowguid());// 没实际用处 主要用task_id进行关联
        audittaskdelegate.setTaskid(task.getTask_id());
        audittaskdelegate.setOuguid(task.getOuguid());
        audittaskdelegate.setOuname(task.getOuname());

        // 调整辖区编码
        audittaskdelegate.setAreacode(ouinfo.getStr("AREACODE"));
        audittaskdelegate.setDelegatetype("10");// 代表乡镇法定事项
        audittaskdelegate.setPromise_day(task.getPromise_day());
        audittaskdelegate.setLink_tel(task.getLink_tel());
        audittaskdelegate.setSupervise_tel(task.getSupervise_tel());
        audittaskdelegate.setApplyaddress(task.getStr("ACCEPT_ADDRESS_INFO"));
        audittaskdelegate.setDcnum(1);// 到场次数 默认1
        audittaskdelegate.setStatus("1");// 事项状态
        audittaskdelegate.setDelegatelevel("");// 对乡镇法定事项没有影响
        audittaskdelegate.setUsecurrentinfo("");// 对乡镇法定事项没有影响
        audittaskdelegate.setAcceptcondition(task.getAcceptcondition());
        audittaskdelegate.setXzorder(1);
        audittaskdelegate.setIsallowaccept("1");
        commonDaoTo.insert(audittaskdelegate);
    }

}
