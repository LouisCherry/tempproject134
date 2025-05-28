package com.epoint.xmz.xsyjsfj.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xsyjsfj.api.entity.XsYjsfj;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.xsyjsfj.api.IXsYjsfjService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 国土上传预审批复函详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:34:42]
 */
@RightRelation(XsYjsfjListAction.class)
@RestController("xsyjsfjdetailaction")
@Scope("request")
public class XsYjsfjDetailAction  extends BaseController
{
	  @Autowired
      private IXsYjsfjService service; 
    
    /**
     * 国土上传预审批复函实体对象
     */
  	private XsYjsfj dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new XsYjsfj();  
		  }
    }
   
   
	      public XsYjsfj getDataBean()
	      {
	          return dataBean;
	      }
}