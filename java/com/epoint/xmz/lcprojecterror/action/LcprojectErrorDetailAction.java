package com.epoint.xmz.lcprojecterror.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.lcprojecterror.api.entity.LcprojectError;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.lcprojecterror.api.ILcprojectErrorService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 浪潮推送失败记录表详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2021-06-29 10:03:18]
 */
@RightRelation(LcprojectErrorListAction.class)
@RestController("lcprojecterrordetailaction")
@Scope("request")
public class LcprojectErrorDetailAction  extends BaseController
{
	  @Autowired
      private ILcprojectErrorService service; 
    
    /**
     * 浪潮推送失败记录表实体对象
     */
  	private LcprojectError dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new LcprojectError();  
		  }
    }
   
   
	      public LcprojectError getDataBean()
	      {
	          return dataBean;
	      }
}