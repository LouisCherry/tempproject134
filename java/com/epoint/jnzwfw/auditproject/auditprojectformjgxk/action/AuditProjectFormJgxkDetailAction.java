package com.epoint.jnzwfw.auditproject.auditprojectformjgxk.action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.jnzwfw.auditproject.auditprojectformjgxk.api.IAuditProjectFormJgxkService;
import com.epoint.jnzwfw.auditproject.auditprojectformjgxk.api.entity.AuditProjectFormJgxk;

/**
 * 竣工信息表详情页面对应的后台
 * 
 * @author 86180
 * @version [版本号, 2019-07-08 15:07:59]
 */
@RightRelation(AuditProjectFormJgxkListAction.class)
@RestController("auditprojectformjgxkdetailaction")
@Scope("request")
public class AuditProjectFormJgxkDetailAction  extends BaseController
{
	  @Autowired
      private IAuditProjectFormJgxkService service; 
    
    /**
     * 竣工信息表实体对象
     */
  	private AuditProjectFormJgxk dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new AuditProjectFormJgxk();  
		  }
    }
   
   
	      public AuditProjectFormJgxk getDataBean()
	      {
	          return dataBean;
	      }
}