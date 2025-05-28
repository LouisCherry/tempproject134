package com.epoint.xmz.jntaskrelation.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.jntaskrelation.api.entity.JnTaskRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.jntaskrelation.api.IJnTaskRelationService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 事项关联乡镇表详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-09 16:26:57]
 */
@RightRelation(JnTaskRelationListAction.class)
@RestController("jntaskrelationdetailaction")
@Scope("request")
public class JnTaskRelationDetailAction  extends BaseController
{
	  @Autowired
      private IJnTaskRelationService service; 
    
    /**
     * 事项关联乡镇表实体对象
     */
  	private JnTaskRelation dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new JnTaskRelation();  
		  }
    }
   
   
	      public JnTaskRelation getDataBean()
	      {
	          return dataBean;
	      }
}