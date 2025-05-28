package com.epoint.xmz.zjcreidtquery.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.zjcreidtquery.api.entity.ZjCreidtquery;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.zjcreidtquery.api.IZjCreidtqueryService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 信用查询调用统计表详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2021-10-09 14:58:46]
 */
@RightRelation(ZjCreidtqueryListAction.class)
@RestController("zjcreidtquerydetailaction")
@Scope("request")
public class ZjCreidtqueryDetailAction  extends BaseController
{
	  @Autowired
      private IZjCreidtqueryService service; 
    
    /**
     * 信用查询调用统计表实体对象
     */
  	private ZjCreidtquery dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new ZjCreidtquery();  
		  }
    }
   
   
	      public ZjCreidtquery getDataBean()
	      {
	          return dataBean;
	      }
}