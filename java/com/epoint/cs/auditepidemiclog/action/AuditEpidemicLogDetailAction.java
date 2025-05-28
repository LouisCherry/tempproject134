package com.epoint.cs.auditepidemiclog.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.cs.auditepidemiclog.api.entity.AuditEpidemicLog;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.cs.auditepidemiclog.api.IAuditEpidemicLogService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 访客登记详情页面对应的后台
 * 
 * @author Mercury
 * @version [版本号, 2020-02-02 19:35:15]
 */
@RightRelation(AuditEpidemicLogListAction.class)
@RestController("auditepidemiclogdetailaction")
@Scope("request")
public class AuditEpidemicLogDetailAction  extends BaseController
{
	  /**
     * 
     */
    private static final long serialVersionUID = 7094920469896218163L;

    @Autowired
      private IAuditEpidemicLogService service; 
    
    /**
     * 访客登记实体对象
     */
  	private AuditEpidemicLog dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new AuditEpidemicLog();  
		  }
    }
   
   
	      public AuditEpidemicLog getDataBean()
	      {
	          return dataBean;
	      }
}