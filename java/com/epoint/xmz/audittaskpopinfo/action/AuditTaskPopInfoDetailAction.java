package com.epoint.xmz.audittaskpopinfo.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.audittaskpopinfo.api.entity.AuditTaskPopInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.audittaskpopinfo.api.IAuditTaskPopInfoService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 弹窗信息维护详情页面对应的后台
 * 
 * @author dahe
 * @version [版本号, 2024-11-26 10:20:20]
 */
@RightRelation(AuditTaskPopInfoListAction.class)
@RestController("audittaskpopinfodetailaction")
@Scope("request")
public class AuditTaskPopInfoDetailAction  extends BaseController
{
	  @Autowired
      private IAuditTaskPopInfoService service; 
    
    /**
     * 弹窗信息维护实体对象
     */
  	private AuditTaskPopInfo dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new AuditTaskPopInfo();  
		  }
    }
   
   
	      public AuditTaskPopInfo getDataBean()
	      {
	          return dataBean;
	      }
}