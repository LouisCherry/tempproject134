package com.epoint.xmz.xsbzgzxx.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xsbzgzxx.api.entity.XsBzgzxx;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.xsbzgzxx.api.IXsBzgzxxService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 国土补正告知详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:26:12]
 */
@RightRelation(XsBzgzxxListAction.class)
@RestController("xsbzgzxxdetailaction")
@Scope("request")
public class XsBzgzxxDetailAction  extends BaseController
{
	  @Autowired
      private IXsBzgzxxService service; 
    
    /**
     * 国土补正告知实体对象
     */
  	private XsBzgzxx dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new XsBzgzxx();  
		  }
    }
   
   
	      public XsBzgzxx getDataBean()
	      {
	          return dataBean;
	      }
}