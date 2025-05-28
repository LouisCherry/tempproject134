package com.epoint.audittask.workflow.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowTask;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.workflow.domain.AuditTaskRiskpoint;
import com.epoint.basic.audittask.workflow.inter.IAuditTaskRiskpoint;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.convert.ConvertUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.config.WorkflowParticipator;
import com.epoint.workflow.service.config.api.IWorkflowActivityService;
import com.epoint.workflow.service.config.api.IWorkflowParticipatorService;

/**
 * 事项岗位风险点信息表修改页面对应的后台
 * 
 * @author WST-PC
 * @version [版本号, 2016-10-09 09:12:27]
 */
@RestController("jnaudittaskriskpointeditaction")
@Scope("request")
public class JNAuditTaskRiskpointEditAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 3805891682203999765L;
    /**
     * 事项岗位风险点信息表实体对象
     */
    private AuditTaskRiskpoint dataBean = null;
    /**
     * 是否风险点
     */
    private Boolean isRiskPoint;

    /**
     * 工作流活动service
     */
    @Autowired
    private IWorkflowActivityService wfaservice;

    /**
     * 工作流处理人service
     */
    @Autowired
    private IWorkflowParticipatorService participatorService;

    /**
     * 工作流处理人service
     */
    @Autowired
    private IAuditTask audittaskservice;

    @Autowired
    private IOuService ouService;

    @Autowired
    private IAuditTaskRiskpoint auditTaskRiskpointimpl;
    
    @Autowired
    private IAuditOrgaWindow auditWindowImpl;
    
    @Autowired
    private IUserService userService;

    @Override
    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = auditTaskRiskpointimpl.getAuditTaskRiskpointByRowguid(guid, false).getResult();
        if (dataBean == null) {
            dataBean = new AuditTaskRiskpoint();
        }
        isRiskPoint = (StringUtil.isNotBlank(dataBean.getIsriskpoint())
                && ZwfwConstant.CONSTANT_INT_ONE == dataBean.getIsriskpoint());
        this.addCallbackParam("acceptname", dataBean.getAcceptname());
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setIsriskpoint(ConvertUtil.convertBooleanToInteger(isRiskPoint));
        String msg = update(dataBean);
        //判断如果已生成内部工作流，则添加流程处理人
        WorkflowActivity activity = wfaservice.getByActivityGuid(dataBean.getActivityguid());
        AuditTask task = audittaskservice.getAuditTaskByGuid(dataBean.getTaskguid(), false).getResult();

        if (activity != null) {
            participatorService.deleteBySourceGuid(dataBean.getActivityguid());
            String[] acceptguidStrArray = dataBean.getAcceptguid().split(";");
            for (int i = 0; i < acceptguidStrArray.length; i++) {
                WorkflowParticipator wp = new WorkflowParticipator();

                if (StringUtil.isNotBlank(acceptguidStrArray[i])) {
                    wp.setBelongDept(ouService.getOuNameByUserGuid(acceptguidStrArray[i]));
                }
                else {
                    wp.setBelongDept("");
                }
                wp.setBelongTo(10);
                wp.setDataId(acceptguidStrArray[i]);
                wp.setParticipatorGuid(UUID.randomUUID().toString());
                wp.setParticipatorName("人员");
                wp.setMethodGuid("");
                wp.setProcessVersionGuid(task.getPvguid());
                wp.setSourceGuid(dataBean.getActivityguid());
                wp.setModeType(10);
                wp.setType(30);
                participatorService.addWorkflowParticipator(wp);
                // participatorService.addOne(task.getPvguid(), dataBean.getActivityguid(), 10, acceptguidStrArray[i], acceptnameStrArray[i], 30);
            }
        }
        
        // 市级：将配置人员关联的窗口增加该事项
        if ("370800".equals(task.getAreacode()) && StringUtil.isNotBlank(dataBean.getAcceptguid())) {
            List<AuditOrgaWindow> allWindowList = new ArrayList<AuditOrgaWindow>();
            String[] userGuidArray = dataBean.getAcceptguid().split(";");
            for (String userGuid : userGuidArray) {
                List<AuditOrgaWindow> userWindowList = auditWindowImpl.getWindowListByUserGuid(userGuid).getResult();
                if (userWindowList != null && !userWindowList.isEmpty()) {
                    allWindowList.addAll(userWindowList);
                }
                else {
                    String userName = userService.getUserNameByUserGuid(userGuid);
                    addCallbackParam("error", "【" + userName + "】没有配置任何窗口！");
                    break;
                }
            }

            for (AuditOrgaWindow auditOrgaWindow : allWindowList) {
                // 判断该窗口是否配置该事项，没配置的话，将事项加入窗口
                List<AuditOrgaWindowTask> existList = auditWindowImpl.getTaskByWindowAndTaskId(auditOrgaWindow.getRowguid(), task.getTask_id()).getResult();
                if (existList == null || existList.isEmpty()) {
                    AuditOrgaWindowTask auditWindowTask = new AuditOrgaWindowTask();
                    auditWindowTask.setRowguid(UUID.randomUUID().toString());
                    auditWindowTask.setWindowguid(auditOrgaWindow.getRowguid());
                    auditWindowTask.setTaskguid(task.getRowguid());
                    auditWindowTask.setOperatedate(new Date());
                    auditWindowTask.setOrdernum(999);
                    auditWindowTask.setTaskid(task.getTask_id());
                    auditWindowTask.setEnabled("1");// 插入的数据默认为有效
                    auditWindowImpl.insertWindowTask(auditWindowTask);
                }
            }
        }

        addCallbackParam("msg", msg);
    }

    public AuditTaskRiskpoint getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditTaskRiskpoint dataBean) {
        this.dataBean = dataBean;
    }

    public Boolean getIsRiskPoint() {
        if (isRiskPoint == null) {
            isRiskPoint = false;
        }
        return isRiskPoint;
    }

    public void setIsRiskPoint(Boolean isRiskPoint) {
        this.isRiskPoint = isRiskPoint;
    }

    /**
     * 
     *  修改
     *  @param dataBean    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    //action
    public String update(AuditTaskRiskpoint auditTaskRiskpoint) {
        String msg = "";
        AuditCommonResult<String> addResult = auditTaskRiskpointimpl.updateAuditTaskRiskpoint(auditTaskRiskpoint);
        if (!addResult.isSystemCode()) {
            msg = addResult.getSystemDescription();
        }
        else if (!addResult.isBusinessCode()) {
            msg = addResult.getBusinessDescription();
        }
        else {
            msg = "修改成功！";
        }
        return msg;
    }

}
