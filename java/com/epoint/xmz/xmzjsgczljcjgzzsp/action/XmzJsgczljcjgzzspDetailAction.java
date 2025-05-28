package com.epoint.xmz.xmzjsgczljcjgzzsp.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xmzjsgczljcjgzzsp.api.entity.XmzJsgczljcjgzzsp;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.xmzjsgczljcjgzzsp.api.IXmzJsgczljcjgzzspService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 建设工程质量检测机构资质审批表详情页面对应的后台
 * 
 * @author 86177
 * @version [版本号, 2021-05-08 17:01:26]
 */
@RightRelation(XmzJsgczljcjgzzspListAction.class)
@RestController("xmzjsgczljcjgzzspdetailaction")
@Scope("request")
public class XmzJsgczljcjgzzspDetailAction  extends BaseController
{
	  @Autowired
      private IXmzJsgczljcjgzzspService service; 
    
    /**
     * 建设工程质量检测机构资质审批表实体对象
     */
  	private XmzJsgczljcjgzzsp dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new XmzJsgczljcjgzzsp();  
		  }
    }
   
   
	      public XmzJsgczljcjgzzsp getDataBean()
	      {
	          return dataBean;
	      }
}