package com.epoint.xmz.rpaysgzzba.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.rpaysgzzba.api.entity.RpaYsgzzba;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.rpaysgzzba.api.IRpaYsgzzbaService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 院士工作站备案详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-12-20 10:26:25]
 */
@RightRelation(RpaYsgzzbaListAction.class)
@RestController("rpaysgzzbadetailaction")
@Scope("request")
public class RpaYsgzzbaDetailAction  extends BaseController
{
	  @Autowired
      private IRpaYsgzzbaService service; 
    
    /**
     * 院士工作站备案实体对象
     */
  	private RpaYsgzzba dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new RpaYsgzzba();  
		  }
    }
   
   
	      public RpaYsgzzba getDataBean()
	      {
	          return dataBean;
	      }
}