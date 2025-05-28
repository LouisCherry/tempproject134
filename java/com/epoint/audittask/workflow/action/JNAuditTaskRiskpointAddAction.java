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
import com.epoint.core.utils.convert.ConvertUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.user.api.IUserService;

/**
 * 事项岗位风险点信息表新增页面对应的后台
 * 
 * @author WST-PC
 * @version [版本号, 2016-10-09 09:12:27]
 */
@RestController("jnaudittaskriskpointaddaction")
@Scope("request")
public class JNAuditTaskRiskpointAddAction extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8021507790707170110L;

	/**
	 * 事项岗位风险点信息表实体对象
	 */
	private AuditTaskRiskpoint dataBean = null;
	/**
	 * 是否风险点
	 */
	private Boolean isriskpoint;
	/**
	 * 事项标识
	 */
	private String taskGuid;
	/**
	 * 事项复制taskGuid
	 */
	private String copyTaskGuid;

	@Autowired
	private IAuditTaskRiskpoint auditTaskRiskpointimpl ;
	
	@Autowired
    private IAuditTask audittaskservice;
	
	@Autowired
    private IAuditOrgaWindow auditWindowImpl;
	
	@Autowired
    private IUserService userService;

	@Override
	public void pageLoad() {
		taskGuid = getRequestParameter("taskGuid");
		copyTaskGuid = this.getRequestParameter("copyTaskGuid");
		dataBean = new AuditTaskRiskpoint();
	}

	/**
	 * 保存并关闭
	 * 
	 */
    public void add() {
        if (StringUtil.isNotBlank(copyTaskGuid) && (!"undefined".equals(copyTaskGuid))) {
            dataBean.setTaskguid(copyTaskGuid);
        }
        else {
            dataBean.setTaskguid(taskGuid);
        }
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setAdddate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setIsriskpoint(ConvertUtil.convertBooleanToInteger(isriskpoint));
        dataBean.setActivityguid(UUID.randomUUID().toString());
        dataBean.setRiskpointid(UUID.randomUUID().toString());
        String msg = "";
        AuditCommonResult<String> addResult = auditTaskRiskpointimpl.addAuditTaskRiskpoint(dataBean);
        if (!addResult.isSystemCode()) {
            msg = addResult.getSystemDescription();
        }
        else if (!addResult.isBusinessCode()) {
            msg = addResult.getBusinessDescription();
        }
        else {
            msg = "保存成功！";
        }
        
        // 市级：将配置人员关联的窗口增加该事项
        AuditTask task = audittaskservice.getAuditTaskByGuid(dataBean.getTaskguid(), false).getResult();
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
        dataBean = null;
    }

	/**
	 * 保存并新建
	 * 
	 */
	public void addNew() {
		add();
		dataBean = new AuditTaskRiskpoint();
	}

	public AuditTaskRiskpoint getDataBean() {
		if (dataBean == null) {
			dataBean = new AuditTaskRiskpoint();
		}
		return dataBean;
	}

	public void setDataBean(AuditTaskRiskpoint dataBean) {
		this.dataBean = dataBean;
	}

	public Boolean getIsriskpoint() {
		if (isriskpoint == null) {
			isriskpoint = false;
		}
		return isriskpoint;
	}

	public void setIsriskpoint(Boolean isriskpoint) {
		this.isriskpoint = isriskpoint;
	}

}
