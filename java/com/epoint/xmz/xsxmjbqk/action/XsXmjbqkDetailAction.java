package com.epoint.xmz.xsxmjbqk.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xsxmjbqk.api.entity.XsXmjbqk;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.xsxmjbqk.api.IXsXmjbqkService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 国土项目基本情况详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 15:46:23]
 */
@RightRelation(XsXmjbqkListAction.class)
@RestController("xsxmjbqkdetailaction")
@Scope("request")
public class XsXmjbqkDetailAction  extends BaseController
{
	  @Autowired
      private IXsXmjbqkService service; 
    
    /**
     * 国土项目基本情况实体对象
     */
  	private XsXmjbqk dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new XsXmjbqk();  
		  }
    }
   
   
	      public XsXmjbqk getDataBean()
	      {
	          return dataBean;
	      }
}