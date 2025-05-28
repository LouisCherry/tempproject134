package com.epoint.jnzwfw.auditproject.auditprojectformjgxk.action;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.jnzwfw.auditproject.auditprojectformjgxk.api.IAuditProjectFormJgxkService;
import com.epoint.jnzwfw.auditproject.auditprojectformjgxk.api.entity.AuditProjectFormJgxk;
/**
 * 竣工信息表修改页面对应的后台
 * 
 * @author 86180
 * @version [版本号, 2019-07-08 15:07:59]
 */
@RightRelation(AuditProjectFormJgxkListAction.class)
@RestController("auditprojectformjgxkeditaction")
@Scope("request")
public class AuditProjectFormJgxkEditAction  extends BaseController
{

	@Autowired
	private IAuditProjectFormJgxkService service;
    
    /**
     * 竣工信息表实体对象
     */
  	private AuditProjectFormJgxk dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new AuditProjectFormJgxk();  
	   }
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

	public AuditProjectFormJgxk getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(AuditProjectFormJgxk dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
