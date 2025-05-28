package com.epoint.xmz.xsxkfqxzdxq.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xsxkfqxzdxq.api.entity.XsXkfqxzdxq;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.xsxkfqxzdxq.api.IXsXkfqxzdxqService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 国土许可分区县占地详情详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:34:32]
 */
@RightRelation(XsXkfqxzdxqListAction.class)
@RestController("xsxkfqxzdxqdetailaction")
@Scope("request")
public class XsXkfqxzdxqDetailAction  extends BaseController
{
	  @Autowired
      private IXsXkfqxzdxqService service; 
    
    /**
     * 国土许可分区县占地详情实体对象
     */
  	private XsXkfqxzdxq dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new XsXkfqxzdxq();  
		  }
    }
   
   
	      public XsXkfqxzdxq getDataBean()
	      {
	          return dataBean;
	      }
}