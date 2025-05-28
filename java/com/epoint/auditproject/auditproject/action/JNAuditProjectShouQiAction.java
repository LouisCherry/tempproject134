package com.epoint.auditproject.auditproject.action;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectcharge.domain.AuditProjectCharge;
import com.epoint.basic.auditproject.auditprojectcharge.inter.IAuditProjectCharge;
import com.epoint.basic.auditproject.auditprojectchargedetail.domain.AuditProjectChargeDetail;
import com.epoint.basic.auditproject.auditprojectchargedetail.inter.IAuditProjectChargeDetail;
import com.epoint.basic.auditresource.auditchargetemp.domain.AuditChargeReturnTemp;
import com.epoint.basic.auditresource.auditchargetemp.inter.IAuditChargeReturnTemp;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handlechargedetail.IHandleChargeDetail;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.exception.security.ReadOnlyException;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 核价页面对应的后台
 *
 * @author Administrator
 */
@RestController("jnauditprojectshouqiaction")
@Scope("request")
public class JNAuditProjectShouQiAction extends BaseController {

    private static final long serialVersionUID = 7738943336465402257L;
    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;
    /**
     * 办件收费记录
     */
    private AuditProjectCharge auditprojectcharge = new AuditProjectCharge();
    /**
     * 减免理由下拉列表model
     */
    private List<SelectItem> jiaofeitypeModel;
    /**
     * 办件信息实体对象
     */
    private AuditProject auditProject;
    /**
     * 事项标识
     */
    private String taskGuid;
    /**
     * 办件标识
     */
    private String projectGuid;
    /**
     * 办件收费记录标识
     */
    private String chargeGuid;
    /**
     * 待办GUid
     */
    private String messageItemGuid;
    /**
     * 缴款总额
     */
    private String realsum = "0.00";
    /**
     * 减免金额
     */
    private String cutsum = "0.00";
    /**
     * 银行代码
     */
    private String bankcode;
    /**
     * 收款流水号
     */
    private String chargeflowno;
    /**
     * 缴费方式
     */
    private Integer jiaofeitype;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IAuditProjectCharge chargeservice;

    @Autowired
    private IHandleProject handleProjectService;

    @Autowired
    private IHandleChargeDetail chargeDetailService;

    @Autowired
    private IHandleConfig handleConfig;

    @Autowired
    private IAuditProjectChargeDetail iAuditProjectChargeDetail;

    /**
     * 文书模板service
     */
    @Autowired
    private IAuditChargeReturnTemp iAuditChargeReturnTemp;

    /**
     * 附件service
     */
    @Autowired
    private IAttachService iAttachService;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;

    @Autowired
    private IMessagesCenterService messageCenterService;
    @Autowired
    private IWFInstanceAPI9 instanceapi;

    public int isfee;

    @Override
    public void pageLoad() {
        projectGuid = getRequestParameter("projectGuid");
        taskGuid = getRequestParameter("taskGuid");
        messageItemGuid = getRequestParameter("messageItemGuid");
        AuditTask auditTask = auditTaskService.getAuditTaskByGuid(taskGuid, true).getResult();
        String fields = " rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,applyway,is_fee,applyertype ";
        auditProject = auditProjectService.getAuditProjectByRowGuid(fields, projectGuid, auditTask.getAreacode())
                .getResult();
        // 获取办件核价并且不是作废的记录
        List<AuditProjectCharge> chargelist = chargeservice.selectChargeByProjectAndIscancel(projectGuid).getResult();
        if (chargelist != null && chargelist.size() > 0) {
            auditprojectcharge = chargelist.get(0);
            chargeGuid = auditprojectcharge.getRowguid();
            realsum = String.format("%.2f", auditprojectcharge.getRealsum());
            cutsum = String.format("%.2f", auditprojectcharge.getCutsum());
            bankcode = auditprojectcharge.getBankcode();
            chargeflowno = auditprojectcharge.getChargeflowno();
            jiaofeitype = ZwfwConstant.CONSTANT_INT_ONE;
            if (auditprojectcharge.getJiaofeitype() != null && auditprojectcharge.getJiaofeitype() != ZwfwConstant.CONSTANT_INT_ZERO) {
                jiaofeitype = auditprojectcharge.getJiaofeitype();
            }
        }
        //是否打印收费回执单
        String isUseJFTZS = handleConfig.getFrameConfig("AS_USE_JFTZS", ZwfwUserSession.getInstance().getCenterGuid()).getResult();
        if (StringUtil.isNotBlank(isUseJFTZS)) {
            addCallbackParam("isUseJFTZS", isUseJFTZS);
        }
        //是否打印收费回执单
        String AS_FEEMODE = handleConfig.getFrameConfig("AS_FEEMODE", ZwfwUserSession.getInstance().getCenterGuid()).getResult();
        if (StringUtil.isNotBlank(AS_FEEMODE)) {
            addCallbackParam("AS_FEEMODE", AS_FEEMODE);
        }
        // 获取是否收讫
        isfee = auditProject.getIs_fee();
        addCallbackParam("is_fee", auditProject.getIs_fee());
        addCallbackParam("chargeguid", chargeGuid);
    }

    // 判断是否存在个性化模板
    public void hastemplete() {
        String tempDocGuid = "";
        // 2、获取全局配置的文书模板
        Map<String, String> conditionMap = new HashMap<String, String>(16);
        List<AuditChargeReturnTemp> auditDocTemps = iAuditChargeReturnTemp.getAuditChargeReturnTempList(conditionMap)
                .getResult();
        if (auditDocTemps != null && auditDocTemps.size() > 0) {
            tempDocGuid = auditDocTemps.get(0).getDoctempguid();
        }
        if ("".equals(tempDocGuid)) {
            addCallbackParam("hasDoc", "0");
        } else {
            // 4、获取文书模板附件
            List<FrameAttachStorage> attachstoragelist = iAttachService.getAttachListByGuid(tempDocGuid);
            if (attachstoragelist != null && attachstoragelist.size() > 0) {
                addCallbackParam("hasDoc", "1");
            } else {
                addCallbackParam("hasDoc", "0");
            }
        }
    }

    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>() {
                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Record> list = chargeDetailService.getShouQiChargeDetailDatePage(taskGuid, chargeGuid)
                            .getResult();
                    if (list.size() > 0) {
                        for (Record record : list) {
                            if ("2".equals(record.get("pricetype").toString())) {
                                record.set("price", record.get("price") + "%");
                            }
                        }
                    }
                    int count = list.size();
                    this.setRowCount(count);
                    return list;
                }
            };
        }
        return model;
    }

    /**
     * 收讫操作
     */
    public String shouQi() {
        if (isfee == ZwfwConstant.CONSTANT_INT_ONE) {
            throw new ReadOnlyException("收讫");
        }
        // 获取办件核价并且不是作废的记录
        List<AuditProjectCharge> chargelist = chargeservice.selectChargeByProjectAndIscancel(projectGuid).getResult();
        List<AuditProjectChargeDetail> chargelistdetail;
        if (chargelist != null && chargelist.size() > 0) {
            for (AuditProjectCharge info : chargelist) {
                chargelistdetail = iAuditProjectChargeDetail.selectDetailByChargeGuid(info.getRowguid()).getResult();
                for (AuditProjectChargeDetail detail : chargelistdetail) {
                    if ("0".equals(detail.getStr("chargestatus")) || StringUtil.isBlank(detail.getStr("chargestatus"))) {
                        return "error";
                    }
                }
            }
        }
        // 发送待办··
        handleProjectService.handleReceipt(auditProject, chargeGuid, userSession.getDisplayName(),
                userSession.getUserGuid(), messageItemGuid, bankcode, chargeflowno, jiaofeitype);
        // 收讫操作
        String title = "【已缴费】" + auditProject.getProjectname() + "（" + auditProject.getApplyername()
                + "）";
        AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), false)
                .getResult();
        // 如果通过办件主键查询到的待办事宜，则说明该待办为受理前收费产生的
        AuditTaskExtension audittaskextension = auditTaskExtensionService
                .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), false).getResult();
        if (audittaskextension.getCharge_when() == 1 || (audittaskextension.getCharge_when() == 2 && auditTask.getType() == Integer.parseInt(ZwfwConstant.ITEMTYPE_JBJ)
                && ZwfwConstant.JBJMODE_SIMPLE.equals(auditTask.getJbjmode()))) {
            MessagesCenter center = (MessagesCenter) messageCenterService.getWaitHandleListByPviguid(auditProject.getRowguid()).get(0);
            if (center != null) {
                messageCenterService.updateMessageTitleAndShow(center.getMessageItemGuid(), title, center.getTargetUser());
            }
        }
        // 反之，则为办结前收费产生
        else {
            // 如果是办结前收费则会有待办
            List<Integer> status = new ArrayList<Integer>();
            status.add(20);
            ProcessVersionInstance pvi = instanceapi.getProcessVersionInstance(auditProject.getPviguid());
            WorkflowWorkItem workitem = instanceapi.getWorkItemListByPVIGuidAndStatus(pvi, status).get(0);

//                MessagesCenter messagesCenter = messageCenterService.getWaitHandleByPviguid(auditProject.getPviguid());
            if (StringUtil.isBlank(messageItemGuid) && workitem != null) {
                messageItemGuid = workitem.getWaitHandleGuid();
            }
               /*    else {
                    messageItemGuid = getRequestParameter("MessageItemGuid");
                }*/
            if (messageItemGuid != null) {
                messageCenterService.updateMessageTitle(messageItemGuid, title, workitem.getTransactor());
            }
        }
        return "success";
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getJiaofeitypeModel() {
        if (jiaofeitypeModel == null) {
            jiaofeitypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "缴费方式", null, false));
        }
        return this.jiaofeitypeModel;
    }

    public AuditProjectCharge getAuditprojectcharge() {
        return auditprojectcharge;
    }

    public void setAuditprojectcharge(AuditProjectCharge auditprojectcharge) {
        this.auditprojectcharge = auditprojectcharge;
    }

    public AuditProject getAuditProject() {
        return auditProject;
    }

    public void setAuditProject(AuditProject auditProject) {
        this.auditProject = auditProject;
    }

    public String getChargeGuid() {
        return chargeGuid;
    }

    public void setChargeGuid(String chargeGuid) {
        this.chargeGuid = chargeGuid;
    }

    public String getRealsum() {
        return realsum;
    }

    public void setRealsum(String realsum) {
        this.realsum = realsum;
    }

    public String getCutsum() {
        return cutsum;
    }

    public void setCutsum(String cutsum) {
        this.cutsum = cutsum;
    }

    public String getChargeflowno() {
        return chargeflowno;
    }

    public void setChargeflowno(String chargeflowno) {
        this.chargeflowno = chargeflowno;
    }


    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }

    public Integer getJiaofeitype() {
        return jiaofeitype;
    }

    public void setJiaofeitype(Integer jiaofeitype) {
        this.jiaofeitype = jiaofeitype;
    }
}
