package com.epoint.znsb.auditznsbytjlabel.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.znsb.auditznsbytjlabel.api.entity.AuditZnsbYtjlabel;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.znsb.auditznsbytjlabel.api.IAuditZnsbYtjlabelService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 一体机模块标签详情页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2021-04-20 09:47:21]
 */
@RightRelation(AuditZnsbYtjlabelListAction.class)
@RestController("auditznsbytjlabeldetailaction")
@Scope("request")
public class AuditZnsbYtjlabelDetailAction  extends BaseController
{
	  @Autowired
      private IAuditZnsbYtjlabelService service; 
    
    /**
     * 一体机模块标签实体对象
     */
  	private AuditZnsbYtjlabel dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new AuditZnsbYtjlabel();  
		  }
    }
   
   
	      public AuditZnsbYtjlabel getDataBean()
	      {
	          return dataBean;
	      }
}