package com.epoint.cs.tsproject.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.cs.tsproject.api.entity.TsProject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.cs.tsproject.api.ITsProjectService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 推送数据详情页面对应的后台
 * 
 * @author 18300
 * @version [版本号, 2018-12-13 20:11:11]
 */
@RightRelation(TsProjectListAction.class)
@RestController("tsprojectdetailaction")
@Scope("request")
public class TsProjectDetailAction  extends BaseController
{
	  @Autowired
      private ITsProjectService service; 
    
    /**
     * 推送数据实体对象
     */
  	private TsProject dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new TsProject();  
		  }
    }
   
   
	      public TsProject getDataBean()
	      {
	          return dataBean;
	      }
}