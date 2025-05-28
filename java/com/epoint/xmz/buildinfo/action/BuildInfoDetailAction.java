package com.epoint.xmz.buildinfo.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.buildinfo.api.entity.BuildInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.buildinfo.api.IBuildInfoService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 工改二阶段建筑表详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-09-08 14:14:05]
 */
@RightRelation(BuildInfoListAction.class)
@RestController("buildinfodetailaction")
@Scope("request")
public class BuildInfoDetailAction  extends BaseController
{
	  @Autowired
      private IBuildInfoService service; 
    
    /**
     * 工改二阶段建筑表实体对象
     */
  	private BuildInfo dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new BuildInfo();  
		  }
    }
   
   
	      public BuildInfo getDataBean()
	      {
	          return dataBean;
	      }
}