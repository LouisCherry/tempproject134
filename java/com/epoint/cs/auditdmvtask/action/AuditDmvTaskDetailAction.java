package com.epoint.cs.auditdmvtask.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.cs.auditdmvtask.api.entity.AuditDmvTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.cs.auditdmvtask.api.IAuditDmvTaskService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 车管所事项详情页面对应的后台
 * 
 * @author admin
 * @version [版本号, 2021-03-02 11:37:44]
 */
@RightRelation(AuditDmvTaskListAction.class)
@RestController("auditdmvtaskdetailaction")
@Scope("request")
public class AuditDmvTaskDetailAction  extends BaseController
{
	  @Autowired
      private IAuditDmvTaskService service; 
    
    /**
     * 车管所事项实体对象
     */
  	private AuditDmvTask dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new AuditDmvTask();  
		  }
    }
   
   
	      public AuditDmvTask getDataBean()
	      {
	          return dataBean;
	      }
}