package com.epoint.depassetinfo.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.bizlogic.mis.CommonService;
import com.epoint.basic.controller.BaseController;
import com.epoint.depassetinfo.api.IDeptaskservice;
import com.epoint.depassetinfo.domain.Deptask;

/**
 * xbn
 * 
 * @author 
 * @version [版本号, 2018-03-27 08:13:16]
 */
@SuppressWarnings("serial")
@RestController("depinfoaddaction")
@Scope("request")
public class DepinfoAddAction  extends BaseController
{
	private CommonService service = new CommonService();    
    /**
     * 采购基本信息表实体对象
     */
  	private Deptask dataBean=null;
  	
   @Autowired
   private IDeptaskservice depservice;
  
    private AuditTask auditTask;
    public void pageLoad()
    {
        dataBean=new Deptask();
        String taskguid=getRequestParameter("taskguid");
        auditTask=depservice.gettaskbyguid(taskguid);
   
        
    }

    /**
     * 保存并关闭
     * 
     */
	public void add()
    {
	  
        dataBean.setRowguid( UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setTask(auditTask.getTaskname());
        dataBean.setTaskguid(auditTask.getRowguid());
        dataBean.setItemid(auditTask.getItem_id());
        dataBean.setUnid(auditTask.get("unid"));
        dataBean.setDeptask(dataBean.getDeptask());
        dataBean.setOuguid(auditTask.getOuguid());
        service.insert(dataBean);
	    addCallbackParam("msg", "保存成功！");
	    dataBean = null;
	}

    /**
     * 保存并新建
     * 
     */
	public void addNew() {
		 add();
		 dataBean = new Deptask();
		 String taskguid=getRequestParameter("taskguid");
	     auditTask=depservice.gettaskbyguid(taskguid);
	}

    public Deptask getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new Deptask();
        }
        return dataBean;
    }

    public void setDataBean(Deptask dataBean)
    {
        this.dataBean = dataBean;
    }

	public AuditTask getAuditTask() {
		return auditTask;
	}

	public void setAuditTask(AuditTask auditTask) {
		this.auditTask = auditTask;
	}
    
}
