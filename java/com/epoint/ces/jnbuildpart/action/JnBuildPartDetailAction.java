package com.epoint.ces.jnbuildpart.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.ces.jnbuildpart.api.entity.JnBuildPart;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.ces.jnbuildpart.api.IJnBuildPartService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 建筑业企业资质数据库详情页面对应的后台
 * 
 * @author 86180
 * @version [版本号, 2020-04-15 11:11:06]
 */
@RightRelation(JnBuildPartListAction.class)
@RestController("jnbuildpartdetailaction")
@Scope("request")
public class JnBuildPartDetailAction  extends BaseController
{
	  @Autowired
      private IJnBuildPartService service; 
    
    /**
     * 建筑业企业资质数据库实体对象
     */
  	private JnBuildPart dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new JnBuildPart();  
		  }
    }
   
   
	      public JnBuildPart getDataBean()
	      {
	          return dataBean;
	      }
}