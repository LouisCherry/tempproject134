package com.epoint.xmz.xsydysyxzyjs.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xsydysyxzyjs.api.entity.XsYdysyxzyjs;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.xsydysyxzyjs.api.IXsYdysyxzyjsService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 国土用地预审与选址意见书详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:34:36]
 */
@RightRelation(XsYdysyxzyjsListAction.class)
@RestController("xsydysyxzyjsdetailaction")
@Scope("request")
public class XsYdysyxzyjsDetailAction  extends BaseController
{
	  @Autowired
      private IXsYdysyxzyjsService service; 
    
    /**
     * 国土用地预审与选址意见书实体对象
     */
  	private XsYdysyxzyjs dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new XsYdysyxzyjs();  
		  }
    }
   
   
	      public XsYdysyxzyjs getDataBean()
	      {
	          return dataBean;
	      }
}