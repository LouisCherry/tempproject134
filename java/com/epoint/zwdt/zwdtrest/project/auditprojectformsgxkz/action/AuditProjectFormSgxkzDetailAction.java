package com.epoint.zwdt.zwdtrest.project.auditprojectformsgxkz.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.zwdt.zwdtrest.project.auditprojectformsgxkz.api.IAuditProjectFormSgxkzService;
import com.epoint.zwdt.zwdtrest.project.auditprojectformsgxkz.api.entity.AuditProjectFormSgxkz;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 施工许可证表单详情页面对应的后台
 * 
 * @author zhaoy
 * @version [版本号, 2019-06-02 15:13:10]
 */
@RightRelation(AuditProjectFormSgxkzListAction.class)
@RestController("auditprojectformsgxkzdetailaction")
@Scope("request")
public class AuditProjectFormSgxkzDetailAction  extends BaseController
{
	  /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
      private IAuditProjectFormSgxkzService service; 
    
    /**
     * 施工许可证表单实体对象
     */
  	private AuditProjectFormSgxkz dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new AuditProjectFormSgxkz();  
		  }
    }
   
   
	      public AuditProjectFormSgxkz getDataBean()
	      {
	          return dataBean;
	      }
}