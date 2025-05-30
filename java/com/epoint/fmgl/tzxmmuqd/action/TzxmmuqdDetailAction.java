package com.epoint.fmgl.tzxmmuqd.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.fmgl.tzxmmuqd.api.entity.Tzxmmuqd;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.fmgl.tzxmmuqd.api.ITzxmmuqdService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 投资项目目录清单详情页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2020-09-26 12:07:54]
 */
@RightRelation(TzxmmuqdListAction.class)
@RestController("tzxmmuqddetailaction")
@Scope("request")
public class TzxmmuqdDetailAction  extends BaseController
{
	  @Autowired
      private ITzxmmuqdService service; 
    
    /**
     * 投资项目目录清单实体对象
     */
  	private Tzxmmuqd dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new Tzxmmuqd();  
		  }
    }
   
   
	      public Tzxmmuqd getDataBean()
	      {
	          return dataBean;
	      }
}