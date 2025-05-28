package com.epoint.znsb.auditznsbmedical.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.znsb.auditznsbmedical.api.entity.AuditZnsbMedical;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.znsb.auditznsbmedical.api.IAuditZnsbMedicalService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 静态医疗信息查询修改页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2021-04-27 09:56:34]
 */
@RightRelation(AuditZnsbMedicalListAction.class)
@RestController("auditznsbmedicaleditaction")
@Scope("request")
public class AuditZnsbMedicalEditAction  extends BaseController
{

	@Autowired
	private IAuditZnsbMedicalService service;
    
    /**
     * 静态医疗信息查询实体对象
     */
  	private AuditZnsbMedical dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new AuditZnsbMedical();  
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

	public AuditZnsbMedical getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(AuditZnsbMedical dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
