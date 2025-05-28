package com.epoint.cpy.jnaicpy.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.cpy.jnaicpy.api.entity.JnAiCpy;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.cpy.jnaicpy.api.IJnAiCpyService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 成品油零售经营企业库详情页面对应的后台
 * 
 * @author 18039505500
 * @version [版本号, 2020-06-23 19:57:28]
 */
@RightRelation(JnAiCpyListAction.class)
@RestController("jnaicpydetailaction")
@Scope("request")
public class JnAiCpyDetailAction  extends BaseController
{
	  @Autowired
      private IJnAiCpyService service; 
    
    /**
     * 成品油零售经营企业库实体对象
     */
  	private JnAiCpy dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new JnAiCpy();  
		  }
    }
   
   
	      public JnAiCpy getDataBean()
	      {
	          return dataBean;
	      }
}