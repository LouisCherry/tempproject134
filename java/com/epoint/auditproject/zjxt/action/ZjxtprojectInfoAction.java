package com.epoint.auditproject.zjxt.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditproject.auditproject.api.IJNAuditProject;
import com.epoint.auditproject.zjxt.entity.AuditProjectProcessZjxt;
import com.epoint.auditproject.zjxt.entity.AuditProjectZjxt;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.jnzwfw.sdwaithandle.api.entity.AuditProjectSamecity;

/**
 * 办件基本信息工作流页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2016-10-14 11:46:07]
 */
@RestController("zjxtprojectinfoaction")
@Scope("request")
public class ZjxtprojectInfoAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -4246072106352276268L;

    /**
     * 办件查询字段
     */
    private String fields = "*";

    
    /**
     * 办件表实体对象
     */
    private AuditProjectZjxt auditproject = null;
    
    private AuditProjectProcessZjxt auditprojectprocess = null;
    /**
     * 事项表实体对象
     */
    private AuditTask audittask = null;
     
    /**
     * 事项拓展信息
     */
    private AuditTaskExtension auditTaskExtension;
    
    /**
     * 事项标识
     */
    private String taskguid;
    
    /**
     * 办件标识
     */
    private String projectguid;
    
    /**
     * 办件流水号
     */
    private String txtflowsn;
    
    /**
     * title
     */
    private String tittle;
   
    
    /**
     * 表格控件model
     */
    private DataGridModel<AuditProjectProcessZjxt> processmodel;
 
    @Autowired
    private IJNAuditProject auditProjectService;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;
    
    
    
    @Autowired
    private ICodeItemsService codeItemService;
    
    /**
     * 同城通办实体
     */
    private AuditProjectSamecity auditProjectSamecity;

    @Override
    public void pageLoad() {
        String areacode = "";
        projectguid = getRequestParameter("projectguid");
        String tctbarea = getRequestParameter("areacode");
        // 如果是镇村接件
        if (ZwfwUserSession.getInstance().getCitylevel()!=null&&(Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ))){
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        }else if(StringUtil.isNotBlank(tctbarea)){
            areacode = tctbarea;
        }else{
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        auditproject = auditProjectService.getAuditProjectZjxtByRowGuid( getRequestParameter("projectguid"));
        //办件删除时提示
        if(auditproject == null){
            addCallbackParam("msg", "办件已删除，待办无效");
            return;
        }
        

        if (auditproject.get("flowsn") == null) {
            txtflowsn = "（办件编号：）";
        }
        else {
            txtflowsn = "（办件编号：" + auditproject.get("flowsn") + "）";
        }
        if (auditproject != null) {
            taskguid = auditproject.get("taskguid");
            //个性化添加审批结果
            audittask = auditTaskService.getAuditTaskByGuid(auditproject.get("taskguid"), false).getResult();
            String area = "";
//            if (auditproject != null && StringUtil.isNotBlank(auditproject.getAcceptareacode())) {
//                area = auditproject.getAcceptareacode();
//            } else {
//                area = ZwfwUserSession.getInstance().getAreaCode();
//            }
         
            auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(auditproject.get("taskguid"), false)
                    .getResult();
        }
    }

       
  
     
    /**
     * 办结状态KEY
     * 
     * @param status
     *            办结状态
     * @return String
     */
    public static String getBanjieStatusKey(int status) {
        String stausKey = "未办结";
        if (StringUtil.isNotBlank(status)) {
            if (status == (ZwfwConstant.BANJIE_TYPE_BYSL)) {
                stausKey = "不予受理";
            }
            if (status == (ZwfwConstant.BANJIE_TYPE_ZYXK)) {
                stausKey = "准予许可";
            }
            if (status == (ZwfwConstant.BANJIE_TYPE_BYXK)) {
                stausKey = "不予许可";
            }
            if (status == (ZwfwConstant.BANJIE_TYPE_CXSQ)) {
                stausKey = "撤销申请";
            }
            if (status == (ZwfwConstant.BANJIE_TYPE_YCZZ)) {
                stausKey = "异常终止";
            }
            
            
            
        }
        return stausKey;
    }
 
  

    public AuditTask getAudittask() {
        return audittask;
    }

    public void setAudittask(AuditTask audittask) {
        this.audittask = audittask;
    }

    public AuditTaskExtension getAuditTaskExtension() {
        return auditTaskExtension;
    }

    public void setAuditTaskExtension(AuditTaskExtension auditTaskExtension) {
        this.auditTaskExtension = auditTaskExtension;
    }
 
    public String getTaskguid() {
        return taskguid;
    }

    public void setTaskguid(String taskguid) {
        this.taskguid = taskguid;
    }

    public String getTxtflowsn() {
        return txtflowsn;
    }

    public void setTxtflowsn(String txtflowsn) {
        this.txtflowsn = txtflowsn;
    }

    
    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }
 

      
    public AuditProjectZjxt getAuditproject() {
		return auditproject;
	}




	public void setAuditproject(AuditProjectZjxt auditproject) {
		this.auditproject = auditproject;
	}




	public DataGridModel<AuditProjectProcessZjxt> getDataGridDanti() {
        // 获得表格对象
        if (processmodel == null) {
        	processmodel = new DataGridModel<AuditProjectProcessZjxt>()
            {
                @Override
                public List<AuditProjectProcessZjxt> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<AuditProjectProcessZjxt> list = auditProjectService.findProcessList(auditproject.get("flowsn").toString());
                    this.setRowCount(list.size());
                    return list;
                }

            };
        }
        return processmodel;
    }
 
}
