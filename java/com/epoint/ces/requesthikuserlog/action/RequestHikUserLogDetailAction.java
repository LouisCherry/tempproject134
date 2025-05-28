package com.epoint.ces.requesthikuserlog.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.ces.requesthikuserlog.api.entity.RequestHikUserLog;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.ces.requesthikuserlog.api.IRequestHikUserLogService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 请求海康日志信息人员考勤记录表详情页面对应的后台
 * 
 * @author shun
 * @version [版本号, 2021-11-22 14:32:24]
 */
@RightRelation(RequestHikUserLogListAction.class)
@RestController("requesthikuserlogdetailaction")
@Scope("request")
public class RequestHikUserLogDetailAction  extends BaseController
{
	  @Autowired
      private IRequestHikUserLogService service; 
    
    /**
     * 请求海康日志信息人员考勤记录表实体对象
     */
  	private RequestHikUserLog dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new RequestHikUserLog();  
		  }
    }
   
   
	      public RequestHikUserLog getDataBean()
	      {
	          return dataBean;
	      }
}