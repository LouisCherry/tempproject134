package com.epoint.znsb.auditznsbwater.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.znsb.auditznsbwater.api.entity.Auditznsbwater;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.znsb.auditznsbwater.api.IAuditznsbwaterService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 水务对账信息修改页面对应的后台
 * 
 * @author HYF
 * @version [版本号, 2021-11-11 16:08:59]
 */
@RightRelation(AuditznsbwaterListAction.class)
@RestController("auditznsbwatereditaction")
@Scope("request")
public class AuditznsbwaterEditAction  extends BaseController
{

	@Autowired
	private IAuditznsbwaterService service;
    
    /**
     * 水务对账信息实体对象
     */
  	private Auditznsbwater dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new Auditznsbwater();  
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

	public Auditznsbwater getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(Auditznsbwater dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
