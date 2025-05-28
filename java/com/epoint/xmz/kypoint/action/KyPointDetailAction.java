package com.epoint.xmz.kypoint.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.kypoint.api.entity.KyPoint;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.kypoint.api.IKyPointService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 勘验要点表详情页面对应的后台
 * 
 * @author RaoShaoliang
 * @version [版本号, 2023-07-10 16:28:23]
 */
@RightRelation(KyPointListAction.class)
@RestController("kypointdetailaction")
@Scope("request")
public class KyPointDetailAction  extends BaseController
{
	  @Autowired
      private IKyPointService service; 
    
    /**
     * 勘验要点表实体对象
     */
  	private KyPoint dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new KyPoint();  
		  }
    }
   
   
	      public KyPoint getDataBean()
	      {
	          return dataBean;
	      }
}