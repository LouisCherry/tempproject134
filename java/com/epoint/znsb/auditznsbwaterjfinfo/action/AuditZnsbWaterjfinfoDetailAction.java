package com.epoint.znsb.auditznsbwaterjfinfo.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.znsb.auditznsbwaterjfinfo.api.entity.AuditZnsbWaterjfinfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.znsb.auditznsbwaterjfinfo.api.IAuditZnsbWaterjfinfoService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 水务缴费信息详情页面对应的后台
 * 
 * @author HYF
 * @version [版本号, 2021-11-11 14:49:42]
 */
@RightRelation(AuditZnsbWaterjfinfoListAction.class)
@RestController("auditznsbwaterjfinfodetailaction")
@Scope("request")
public class AuditZnsbWaterjfinfoDetailAction  extends BaseController
{
	  @Autowired
      private IAuditZnsbWaterjfinfoService service; 
    
    /**
     * 水务缴费信息实体对象
     */
  	private AuditZnsbWaterjfinfo dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new AuditZnsbWaterjfinfo();  
		  }
    }
   
   
	      public AuditZnsbWaterjfinfo getDataBean()
	      {
	          return dataBean;
	      }
}