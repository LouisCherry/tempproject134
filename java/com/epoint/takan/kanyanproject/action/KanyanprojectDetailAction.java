package com.epoint.takan.kanyanproject.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.takan.kanyanproject.api.entity.Kanyanproject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.takan.kanyanproject.api.IKanyanprojectService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 勘验项目详情页面对应的后台
 * 
 * @author panshunxing
 * @version [版本号, 2024-09-20 03:15:13]
 */
@RightRelation(KanyanprojectListAction.class)
@RestController("kanyanprojectdetailaction")
@Scope("request")
public class KanyanprojectDetailAction  extends BaseController
{
	  @Autowired
      private IKanyanprojectService service; 
    
    /**
     * 勘验项目实体对象
     */
  	private Kanyanproject dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new Kanyanproject();  
		  }
    }
   
   
	      public Kanyanproject getDataBean()
	      {
	          return dataBean;
	      }
}