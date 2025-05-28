package com.epoint.znsb.auditznsbwater.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.znsb.auditznsbwater.api.entity.Auditznsbwater;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.znsb.auditznsbwater.api.IAuditznsbwaterService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 水务对账信息详情页面对应的后台
 * 
 * @author HYF
 * @version [版本号, 2021-11-11 16:08:59]
 */
@RightRelation(AuditznsbwaterListAction.class)
@RestController("auditznsbwaterdetailaction")
@Scope("request")
public class AuditznsbwaterDetailAction  extends BaseController
{
	  @Autowired
      private IAuditznsbwaterService service; 
    
    /**
     * 水务对账信息实体对象
     */
  	private Auditznsbwater dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new Auditznsbwater();  
		  }
    }
   
   
	      public Auditznsbwater getDataBean()
	      {
	          return dataBean;
	      }
}