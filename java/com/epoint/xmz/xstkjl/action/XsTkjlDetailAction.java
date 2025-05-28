package com.epoint.xmz.xstkjl.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xstkjl.api.entity.XsTkjl;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.xstkjl.api.IXsTkjlService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 国土踏勘记录详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:18:51]
 */
@RightRelation(XsTkjlListAction.class)
@RestController("xstkjldetailaction")
@Scope("request")
public class XsTkjlDetailAction  extends BaseController
{
	  @Autowired
      private IXsTkjlService service; 
    
    /**
     * 国土踏勘记录实体对象
     */
  	private XsTkjl dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new XsTkjl();  
		  }
    }
   
   
	      public XsTkjl getDataBean()
	      {
	          return dataBean;
	      }
}