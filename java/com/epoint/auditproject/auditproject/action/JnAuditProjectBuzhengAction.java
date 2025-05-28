package com.epoint.auditproject.auditproject.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.element.domain.AuditTaskElement;
import com.epoint.basic.audittask.element.inter.IAuditTaskElementService;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.DHSendMsgUtil;
import com.epoint.common.util.StarProjectInteractUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.metadata.workingday.api.IWorkingdayService;

@RestController("jnauditprojectBuzhengaction")
@Scope("request")
public class JnAuditProjectBuzhengAction extends BaseController
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 表格控件model
     */
    private DataGridModel<AuditProjectMaterial> model;
    /**
     * 办件查询字段
     */
    private static final String FIELDS = "applyeruserguid,applyertype,task_id,flowsn,contactperson,centerguid,contactcertnum,applyway,taskguid,projectname,applydate,orgwn,taskcaseguid,rowguid,biguid,applyeruserguid,applyername,status,subappguid,businessguid ";
    /**
     * 办件guid
     */
    private String projectguid;
    @Autowired
    private IAuditProjectMaterial projectMaterialService;
    @Autowired
    private IAuditProject auditProjectService;
    @Autowired
    private IHandleProject handleProjectService;
    @Autowired
    private IAuditTaskMaterial taskMaterialService;
    @Autowired
    private IHandleSPIMaterial spiMaterialService;
    @Autowired
    private IAuditSpISubapp auditSpISubappService;
    @Autowired
    IAuditTaskMaterialCase auditTaskMaterialCaseService;
    @Autowired
    private IMessagesCenterService messageCenterService;
    @Autowired
    private IAuditTask auditTaskService;
    @Autowired
    private ISendMQMessage sendMQMessageService;
    @Autowired
    private IAuditTaskElementService auditTaskElementService;

    @Autowired
    private IAuditSpIMaterial auditSpIMaterialService;

    @Autowired
    private IMessagesCenterService messagesCenterService;
    @Autowired
    private IAuditOnlineIndividual auditOnlieIndividualService;
    @Autowired
    IAuditRsCompanyBaseinfo auditRsCompanyBaseinfoService;
    @Autowired
    private IAuditTaskExtension iAuditTaskExtension;
    @Autowired
    private IConfigService iconfigService;
    @Autowired
    private IAuditProjectOperation iAuditProjectOperation;
    @Autowired
    private IWorkingdayService iWorkingdayService;
    /**
     * 办件实体
     */
    private AuditProject auditproject = null;

    /**
     * 补正原因
     */
    private String bzRemark;

    @Override
    public void pageLoad() {
        projectguid = getRequestParameter("projectguid");
        String area = "";
        // 如果是镇村接件
        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                        .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            area = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else {
            area = ZwfwUserSession.getInstance().getAreaCode();
        }
        auditproject = auditProjectService.getAuditProjectByRowGuid(FIELDS, projectguid, area).getResult();
        addCallbackParam("projectstatus", auditproject.getStatus());
    }

    public void updateClick() {
        List<String> select = getDataGridData().getSelectKeys();
        if (select == null) {
            addCallbackParam("message", "请至少选中一份材料！");
        }
        else {
            updateSave();
            String area = "";
            // 如果是镇村接件
            if (ZwfwUserSession.getInstance().getCitylevel() != null
                    && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                            .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
                area = ZwfwUserSession.getInstance().getBaseAreaCode();
            }
            else {
                area = ZwfwUserSession.getInstance().getAreaCode();
            }
            AuditProject auditproject1 = auditProjectService.getAuditProjectByRowGuid(auditproject.getRowguid(), area)
                    .getResult();
            auditproject1.set("bzRemark", bzRemark);
            String bzgzsaddress = handleProjectService
                    .handlePatch(auditproject1, userSession.getDisplayName(), userSession.getUserGuid()).getResult();
            if (StringUtil.isNotBlank(bzgzsaddress)) {
                addCallbackParam("message", bzgzsaddress);
            }
            else {
                addCallbackParam("message", "0");
            }
            // 如果存在待办事宜，则删除待办
            List<MessagesCenter> messagesCenterList = new ArrayList<MessagesCenter>();
            List<MessagesCenter> messagesList = new ArrayList<MessagesCenter>();
            if (auditproject.getStatus() < ZwfwConstant.BANJIAN_STATUS_YSL) {
                messagesList = messageCenterService.getWaitHandleListByPviguid(auditproject.getRowguid());
                if (messagesList != null && !messagesList.isEmpty()) {
                    messagesCenterList.addAll(messagesList);
                }

                if (messagesList == null || messagesList.isEmpty()) {
                    messagesCenterList = messageCenterService.queryForList(userSession.getUserGuid(), null, null, "",
                            IMessagesCenterService.MESSAGETYPE_WAIT, auditproject.getRowguid(), "", -1, "", null, null,
                            0, -1);
                }
            }
            else {
                messagesCenterList = messageCenterService.queryForList(userSession.getUserGuid(), null, null, "",
                        IMessagesCenterService.MESSAGETYPE_WAIT, auditproject.getRowguid(), "", -1, "", null, null, 0,
                        -1);
            }
            if (messagesCenterList != null && !messagesCenterList.isEmpty()) {
                for (MessagesCenter messagescenter : messagesCenterList) {
                    messageCenterService.deleteMessage(messagescenter.getMessageItemGuid(),
                            messagescenter.getTargetUser());
                }
            }

            String msg = projectguid + "." + ZwfwUserSession.getInstance().getAreaCode();
            // 发送mq
            sendMQMessageService.sendByExchange("exchange_handle", msg,
                    "project." + ZwfwUserSession.getInstance().getAreaCode() + ".bz." + auditproject.getTask_id());

            // 发送待办事宜
            /*
             * String title = "【待补办】" + auditproject.getProjectname() + "(" +
             * auditproject.getApplyername() + ")";
             * String messageItemGuid = UUID.randomUUID().toString();
             * String formUrl =
             * StringUtil.isNotBlank(auditTaskExtension.getCustomformurl())
             * ? auditTaskExtension.getCustomformurl() :
             * ZwfwConstant.CONSTANT_FORM_URL;
             * String handleUrl = formUrl+"?processguid="
             * + auditTask.getProcessguid() + "&taskguid=" +
             * auditproject.getTaskguid() + "&projectguid="
             * + auditproject.getRowguid();
             *
             * messageCenterService.insertWaitHandleMessage(messageItemGuid,
             * title,
             * IMessagesCenterService.MESSAGETYPE_WAIT,
             * userSession.getUserGuid(), userSession.getDisplayName(),
             * userSession.getUserGuid(), userSession.getDisplayName(), "待补办",
             * handleUrl, userSession.getOuGuid(),
             * "", ZwfwConstant.CONSTANT_INT_ONE, "", "",
             * auditproject.getRowguid(),
             * auditproject.getRowguid().substring(0, 1), new Date(),
             * auditproject.getPviguid(), userSession.getUserGuid(),
             * "", "");
             */

            // 先判断是否江苏通用
            String isJSTY = ConfigUtil.getConfigValue("isJSTY");
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                // 再判断是否外网办件推送状态
                if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditproject1.getApplyway().toString())
                        || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditproject1.getApplyway().toString())) {
                    AuditCommonResult<AuditTask> taskResult = auditTaskService
                            .getAuditTaskByGuid(auditproject1.getTaskguid(), true);
                    AuditTask task = taskResult.getResult();
                    String url = ConfigUtil.getConfigValue("dhlogin", "zwdturl");
                    // 判断是否配置地址
                    if (StringUtil.isNotBlank(url)) {
                        url += "epointzwdt/pages/onlinedeclaration_js/declarationinfo?declareid=" + task.getTask_id()
                                + "&centerguid=" + auditproject1.getCenterguid() + "&taskguid=" + task.getRowguid()
                                + "&flowsn=" + auditproject1.getFlowsn();
                        StarProjectInteractUtil.updateOrInsertStar(auditproject1.getFlowsn(),
                                ZwdtConstant.STAR_PROJECT_BZ,
                                StringUtil.isBlank(task.getDept_yw_reg_no()) ? task.getItem_id()
                                        : task.getDept_yw_reg_no(),
                                task.getTaskname(), auditproject1.getContactperson(), auditproject1.getContactcertnum(),
                                "补正", url);
                        // 调用大汉统一消息接口
                        try {
                            DHSendMsgUtil.PostDHMessage("您申请的办件有了新的进度!",
                                    auditproject1.getContactperson() + "你好，您于"
                                            + EpointDateUtil.convertDate2String(auditproject1.getApplydate(),
                                                    "yyyy年MM月dd日 HH:mm")
                                            + "在网上申请的“" + auditproject1.getProjectname()
                                            + "”，有材料需要进行补正，请您尽快在江苏政务服务网确认收件地址。",
                                    auditproject1.getContactcertnum());
                        }
                        catch (DocumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            String title = "【材料补正】" + auditproject.getProjectname();

            // 发送材料补正消息通知
            String applyerGuid = "";
            int type = auditproject.getApplyertype();
            String openUrl = iconfigService.getFrameConfigValue("zwdtMsgurl")
                    + "/epointzwmhwz/pages/myspace/detail?projectguid=" + auditproject.getRowguid()
                    + "&tabtype=2&taskguid=" + auditproject.getTaskguid() + "&taskcaseguid="
                    + auditproject.getTaskcaseguid() + "&applyertype=" + type;
            // 个人
            if (type == 20) {
                AuditOnlineIndividual auditonlineindividual = auditOnlieIndividualService
                        .getIndividualByApplyerGuid(auditproject.getApplyeruserguid()).getResult();
                if (auditonlineindividual != null) {
                    applyerGuid = auditonlineindividual.getAccountguid();
                }
            }
            // 法人
            else {
                applyerGuid = auditproject.getOnlineapplyerguid();
            }
            if (StringUtil.isNotBlank(applyerGuid)) {
                messagesCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(), title,
                        IMessagesCenterService.MESSAGETYPE_WAIT, applyerGuid, "",
                        UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(), "",
                        openUrl, UserSession.getInstance().getOuGuid(), UserSession.getInstance().getBaseOUGuid(), 1,
                        null, "", null, null, new Date(), null, UserSession.getInstance().getUserGuid(), null, "");
            }

            // 发送补正短信
            AuditTaskExtension taskExtension = iAuditTaskExtension
                    .getTaskExtensionByTaskGuid(auditproject1.getTaskguid(), false).getResult();
            if (taskExtension != null) {
                String is_sendbz = taskExtension.getStr("is_sendbz");
                if ("1".equals(is_sendbz)) {
                    // 补正时间 取当前时间
                    Date begindate = new Date();
                    Date enddate = iWorkingdayService.getWorkingDayWithOfficeSet(begindate, 6, true);
                    String time = EpointDateUtil.convertDate2String(enddate, "MM月dd日");
                    String bzcontent = taskExtension.getStr("notify_bz");
                    bzcontent = bzcontent.replaceAll("#deadline#", time);
                    if (StringUtil.isNotBlank(auditproject1.getContactmobile())) {
                        messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), bzcontent, new Date(), 0,
                                null, auditproject1.getContactmobile(), UUID.randomUUID().toString(), "",
                                userSession.getUserGuid(), userSession.getDisplayName(), "", "", null, false,
                                auditproject1.getAreacode());
                    }
                }
            }
            log.info("预审不通过,推送mq代码");
            // 推送mq消息,同步操作给
            sendMQMessageService.sendByExchange("zwdt_exchange_handle",
                    auditproject.getRowguid() + "@" + auditproject.getAreacode(), "space.saving.docking.1");
        }
    }

    /**
     * 保存文书信息
     */
    public void updateSave() {
        List<String> select = getDataGridData().getSelectKeys();
        String materials = "";
        if (select != null) {
            for (String rowguid : select) {
                projectMaterialService.updateProjectMaterialAuditStatus(rowguid,
                        Integer.parseInt(ZwfwConstant.Material_AuditStatus_DBZ), projectguid);
                materials += rowguid + ",";
            }
            if (StringUtil.isNotBlank(materials)) {
                materials = materials.substring(0, materials.length() - 1);
            }

            // 并联审批更新补正状态
            if (StringUtil.isNotBlank(auditproject.getBiguid())) {
                // 如果办件状态是已接件且是并联审批办件，办件表中没有保存windowname，需要重新设置
                if (ZwfwConstant.BANJIAN_STATUS_YJJ == auditproject.getStatus()) {
                    auditproject.setWindowname(ZwfwUserSession.getInstance().getWindowName());
                    auditproject.setWindowguid(ZwfwUserSession.getInstance().getWindowGuid());
                    auditProjectService.updateProject(auditproject);
                }

                spiMaterialService.updateIMaterialBuzheng(auditproject.getSubappguid(), materials, projectguid);
                auditSpISubappService.updateSubapp(auditproject.getSubappguid(), ZwfwConstant.LHSP_Status_DBJ, null);
            }
        }
    }

    public DataGridModel<AuditProjectMaterial> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProjectMaterial>()
            {
                /**
                 *
                 */
                private static final long serialVersionUID = -1036251303707688421L;

                @Override
                public List<AuditProjectMaterial> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    List<AuditProjectMaterial> projectMaterials = projectMaterialService
                            .selectProjectMaterial(projectguid).getResult();
                    List<AuditProjectMaterial> materials = new ArrayList<>();
                    if (projectMaterials != null) {
                        // 情形
                        Map<String, Integer> caseMap = null;
                        List<AuditTaskElement> auditTaskElementlist = auditTaskElementService
                                .findAllElementByTaskId(auditproject.getTask_id()).getResult();
                        if (auditproject != null && auditTaskElementlist != null && !auditTaskElementlist.isEmpty()
                                && StringUtil.isBlank(auditproject.getBiguid())) {
                            List<AuditTaskMaterialCase> auditTaskMaterialCases = auditTaskMaterialCaseService
                                    .selectTaskMaterialCaseByCaseGuid(auditproject.getTaskcaseguid()).getResult();
                            caseMap = new HashMap<>(16);
                            // 转成map方便查找
                            if (auditTaskMaterialCases != null && !auditTaskMaterialCases.isEmpty()) {
                                for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                                    caseMap.put(auditTaskMaterialCase.getMaterialguid(),
                                            auditTaskMaterialCase.getNecessity());
                                }
                            }
                        }

                        for (AuditProjectMaterial auditProjectMaterial : projectMaterials) {

                            AuditTaskMaterial taskMaterial = taskMaterialService
                                    .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid())
                                    .getResult();
                            if (caseMap == null
                                    || ZwfwConstant.NECESSITY_SET_YES.equals(taskMaterial.getNecessity().toString())
                                    || (caseMap.containsKey(auditProjectMaterial.getTaskmaterialguid()))) {
                                if (auditProjectMaterial.getAuditstatus()
                                        .equals(ZwfwConstant.Material_AuditStatus_DBZ)) {
                                    auditProjectMaterial
                                            .setTaskmaterial(auditProjectMaterial.getTaskmaterial() + "（待补正）");
                                }
                                // 必需材料才需要考虑容缺情况
                                if (taskMaterial.getNecessity() == 10) {
                                    int isRongque = taskMaterial.getIs_rongque();
                                    auditProjectMaterial.setIs_rongque(isRongque);
                                }
                                else {
                                    auditProjectMaterial.setIs_rongque(0);
                                }
                                AuditSpIMaterial spmaterial = auditSpIMaterialService
                                        .getSpIMaterialByID(auditproject.getBusinessguid(),
                                                auditproject.getSubappguid(), taskMaterial.getMaterialid())
                                        .getResult();
                                if (spmaterial == null
                                        || ZwfwConstant.CONSTANT_STR_ZERO.equals(spmaterial.getResult())) {
                                    Record record = (Record) auditProjectMaterial.clone();
                                    record.set("Ordernum", taskMaterial.getOrdernum());
                                    materials.add((AuditProjectMaterial) record);
                                }
                            }
                        }
                    }
                    materials.sort((a,
                            b) -> (StringUtil.isNotBlank(b.get("Ordernum"))
                                    ? Integer.valueOf(b.get("Ordernum").toString())
                                    : Integer.valueOf(0))
                                            .compareTo(StringUtil.isNotBlank(a.get("Ordernum"))
                                                    ? Integer.valueOf(a.get("Ordernum").toString())
                                                    : 0));

                    this.setRowCount(materials.size());
                    return materials;
                }
            };
        }
        return model;
    }

    public String getBzRemark() {
        return bzRemark;
    }

    public void setBzRemark(String bzRemark) {
        this.bzRemark = bzRemark;
    }
}
