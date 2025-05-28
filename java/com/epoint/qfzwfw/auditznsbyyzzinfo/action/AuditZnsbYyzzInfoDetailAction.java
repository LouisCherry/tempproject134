package com.epoint.qfzwfw.auditznsbyyzzinfo.action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.qfzwfw.auditznsbyyzzinfo.api.IAuditZnsbYyzzInfoService;
import com.epoint.qfzwfw.auditznsbyyzzinfo.api.entity.AuditZnsbYyzzInfo;

/**
 * 智能设备营业执照表详情页面对应的后台
 * 
 * @author LIUCTT
 * @version [版本号, 2018-06-07 09:51:27]
 */
@RightRelation(AuditZnsbYyzzInfoListAction.class)
@RestController("auditznsbyyzzinfodetailaction")
@Scope("request")
public class AuditZnsbYyzzInfoDetailAction  extends BaseController
{
	  @Autowired
      private IAuditZnsbYyzzInfoService service; 
    
    /**
     * 智能设备营业执照表实体对象
     */
  	private AuditZnsbYyzzInfo dataBean=null;
  
    @Override
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new AuditZnsbYyzzInfo();  
		  }
    }
   
   
	      public AuditZnsbYyzzInfo getDataBean()
	      {
	          return dataBean;
	      }
}