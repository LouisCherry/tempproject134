package com.epoint.cs.auditdmvtask.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.cs.auditdmvtask.api.entity.AuditDmvTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.cs.auditdmvtask.api.IAuditDmvTaskService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 车管所事项修改页面对应的后台
 * 
 * @author admin
 * @version [版本号, 2021-03-02 11:37:44]
 */
@RightRelation(AuditDmvTaskListAction.class)
@RestController("auditdmvtaskeditaction")
@Scope("request")
public class AuditDmvTaskEditAction  extends BaseController
{

	@Autowired
	private IAuditDmvTaskService service;
    
    /**
     * 车管所事项实体对象
     */
  	private AuditDmvTask dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new AuditDmvTask();  
	   }
	   addCallbackParam("linkedtaskname", dataBean.getLinkedtaskname());
    }

    /**
     * 保存修改
     * 
     */
	public void save() 
	{
	    dataBean.setOperatedate(new Date());
	    service.update(dataBean);
	    addCallbackParam("msg", "修改成功！");
	}

	public AuditDmvTask getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(AuditDmvTask dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
