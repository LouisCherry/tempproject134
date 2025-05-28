package com.epoint.xmz.xmzfdckfzzzs.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xmzfdckfzzzs.api.entity.XmzFdckfzzzs;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.xmzfdckfzzzs.api.IXmzFdckfzzzsService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 房地产开发资质证书详情页面对应的后台
 * 
 * @author 86177
 * @version [版本号, 2021-05-12 09:40:37]
 */
@RightRelation(XmzFdckfzzzsListAction.class)
@RestController("xmzfdckfzzzsdetailaction")
@Scope("request")
public class XmzFdckfzzzsDetailAction  extends BaseController
{
	  /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
      private IXmzFdckfzzzsService service; 
    
    /**
     * 房地产开发资质证书实体对象
     */
  	private XmzFdckfzzzs dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new XmzFdckfzzzs();  
		  }
    }
   
   
	      public XmzFdckfzzzs getDataBean()
	      {
	          return dataBean;
	      }
}