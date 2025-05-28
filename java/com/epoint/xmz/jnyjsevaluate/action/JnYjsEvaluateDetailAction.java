package com.epoint.xmz.jnyjsevaluate.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.jnyjsevaluate.api.entity.JnYjsEvaluate;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.jnyjsevaluate.api.IJnYjsEvaluateService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 一件事评价表详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-11-11 14:59:29]
 */
@RightRelation(JnYjsEvaluateListAction.class)
@RestController("jnyjsevaluatedetailaction")
@Scope("request")
public class JnYjsEvaluateDetailAction  extends BaseController
{
	  @Autowired
      private IJnYjsEvaluateService service; 
    
    /**
     * 一件事评价表实体对象
     */
  	private JnYjsEvaluate dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new JnYjsEvaluate();  
		  }
    }
   
   
	      public JnYjsEvaluate getDataBean()
	      {
	          return dataBean;
	      }
}