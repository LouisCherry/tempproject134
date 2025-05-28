package com.epoint.xmz.gtxstkjzd.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.gtxstkjzd.api.entity.GtxsTkjzd;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.gtxstkjzd.api.IGtxsTkjzdService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 国土踏勘界址点详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 18:06:12]
 */
@RightRelation(GtxsTkjzdListAction.class)
@RestController("gtxstkjzddetailaction")
@Scope("request")
public class GtxsTkjzdDetailAction  extends BaseController
{
	  @Autowired
      private IGtxsTkjzdService service; 
    
    /**
     * 国土踏勘界址点实体对象
     */
  	private GtxsTkjzd dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new GtxsTkjzd();  
		  }
    }
   
   
	      public GtxsTkjzd getDataBean()
	      {
	          return dataBean;
	      }
}