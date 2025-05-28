package com.epoint.union.audituniontask.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.union.audituniontask.api.entity.AuditUnionTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.union.audituniontask.api.IAuditUnionTaskService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 异地通办事项配置表详情页面对应的后台
 * 
 * @author zhaoyan
 * @version [版本号, 2020-03-22 16:59:46]
 */
@RightRelation(AuditUnionTaskListAction.class)
@RestController("audituniontaskdetailaction")
@Scope("request")
public class AuditUnionTaskDetailAction  extends BaseController
{
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
      private IAuditUnionTaskService service; 
    
    /**
     * 异地通办事项配置表实体对象
     */
  	private AuditUnionTask dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new AuditUnionTask();  
		  }
    }
   
   
	      public AuditUnionTask getDataBean()
	      {
	          return dataBean;
	      }
}