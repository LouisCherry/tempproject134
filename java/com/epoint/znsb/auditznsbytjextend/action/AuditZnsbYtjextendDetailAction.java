package com.epoint.znsb.auditznsbytjextend.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.znsb.auditznsbytjextend.api.entity.AuditZnsbYtjextend;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.znsb.auditznsbytjextend.api.IAuditZnsbYtjextendService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 一体机模块额外配置详情页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2021-04-20 10:11:49]
 */
@RightRelation(AuditZnsbYtjextendListAction.class)
@RestController("auditznsbytjextenddetailaction")
@Scope("request")
public class AuditZnsbYtjextendDetailAction  extends BaseController
{
	  @Autowired
      private IAuditZnsbYtjextendService service; 
    
    /**
     * 一体机模块额外配置实体对象
     */
  	private AuditZnsbYtjextend dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new AuditZnsbYtjextend();  
		  }
    }
   
   
	      public AuditZnsbYtjextend getDataBean()
	      {
	          return dataBean;
	      }
}