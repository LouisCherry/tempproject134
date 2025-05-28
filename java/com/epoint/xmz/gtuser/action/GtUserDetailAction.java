package com.epoint.xmz.gtuser.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.gtuser.api.entity.GtUser;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.gtuser.api.IGtUserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 国土用户管理表详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 12:05:00]
 */
@RightRelation(GtUserListAction.class)
@RestController("gtuserdetailaction")
@Scope("request")
public class GtUserDetailAction  extends BaseController
{
	  @Autowired
      private IGtUserService service; 
    
    /**
     * 国土用户管理表实体对象
     */
  	private GtUser dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new GtUser();  
		  }
    }
   
   
	      public GtUser getDataBean()
	      {
	          return dataBean;
	      }
}