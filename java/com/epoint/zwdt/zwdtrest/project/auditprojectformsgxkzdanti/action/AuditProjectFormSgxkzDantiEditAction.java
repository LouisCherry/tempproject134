package com.epoint.zwdt.zwdtrest.project.auditprojectformsgxkzdanti.action;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.zwdt.zwdtrest.project.auditprojectformsgxkzdanti.api.IAuditProjectFormSgxkzDantiService;
import com.epoint.zwdt.zwdtrest.project.auditprojectformsgxkzdanti.api.entity.AuditProjectFormSgxkzDanti;
import com.epoint.basic.controller.BaseController;
/**
 * 施工许可单体记录表修改页面对应的后台
 * 
 * @author zhaoy
 * @version [版本号, 2019-06-02 22:59:17]
 */
@RightRelation(AuditProjectFormSgxkzDantiListAction.class)
@RestController("auditprojectformsgxkzdantieditaction")
@Scope("request")
public class AuditProjectFormSgxkzDantiEditAction  extends BaseController
{

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
	private IAuditProjectFormSgxkzDantiService service;
    
    /**
     * 施工许可单体记录表实体对象
     */
  	private AuditProjectFormSgxkzDanti dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new AuditProjectFormSgxkzDanti();  
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

	public AuditProjectFormSgxkzDanti getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(AuditProjectFormSgxkzDanti dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
