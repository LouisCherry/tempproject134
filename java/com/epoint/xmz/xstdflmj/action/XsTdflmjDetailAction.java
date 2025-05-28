package com.epoint.xmz.xstdflmj.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xstdflmj.api.entity.XsTdflmj;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.xstdflmj.api.IXsTdflmjService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 国土土地分类面积详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:18:56]
 */
@RightRelation(XsTdflmjListAction.class)
@RestController("xstdflmjdetailaction")
@Scope("request")
public class XsTdflmjDetailAction  extends BaseController
{
	  @Autowired
      private IXsTdflmjService service; 
    
    /**
     * 国土土地分类面积实体对象
     */
  	private XsTdflmj dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new XsTdflmj();  
		  }
    }
   
   
	      public XsTdflmj getDataBean()
	      {
	          return dataBean;
	      }
}