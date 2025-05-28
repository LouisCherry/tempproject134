package com.epoint.jiningzwfw.epointsphmzc.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.jiningzwfw.epointsphmzc.api.entity.EpointSpHmzc;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.jiningzwfw.epointsphmzc.api.IEpointSpHmzcService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 惠企政策库详情页面对应的后台
 * 
 * @author 86180
 * @version [版本号, 2019-10-08 23:39:45]
 */
@RightRelation(EpointSpHmzcListAction.class)
@RestController("epointsphmzcdetailaction")
@Scope("request")
public class EpointSpHmzcDetailAction  extends BaseController
{
	  @Autowired
      private IEpointSpHmzcService service; 
    
    /**
     * 惠企政策库实体对象
     */
  	private EpointSpHmzc dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new EpointSpHmzc();  
		  }
    }
   
   
	      public EpointSpHmzc getDataBean()
	      {
	          return dataBean;
	      }
}