package com.epoint.xmz.xstkdk.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xstkdk.api.entity.XsTkdk;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.xstkdk.api.IXsTkdkService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 国土踏勘地块详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:19:01]
 */
@RightRelation(XsTkdkListAction.class)
@RestController("xstkdkdetailaction")
@Scope("request")
public class XsTkdkDetailAction  extends BaseController
{
	  @Autowired
      private IXsTkdkService service; 
    
    /**
     * 国土踏勘地块实体对象
     */
  	private XsTkdk dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new XsTkdk();  
		  }
    }
   
   
	      public XsTkdk getDataBean()
	      {
	          return dataBean;
	      }
}