package com.epoint.xmz.xsxkjl.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xsxkjl.api.entity.XsXkjl;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.xsxkjl.api.IXsXkjlService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 国土许可记录详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:26:17]
 */
@RightRelation(XsXkjlListAction.class)
@RestController("xsxkjldetailaction")
@Scope("request")
public class XsXkjlDetailAction  extends BaseController
{
	  @Autowired
      private IXsXkjlService service; 
    
    /**
     * 国土许可记录实体对象
     */
  	private XsXkjl dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new XsXkjl();  
		  }
    }
   
   
	      public XsXkjl getDataBean()
	      {
	          return dataBean;
	      }
}