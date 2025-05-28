package com.epoint.xmz.spglxmfjxxb.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.spglxmfjxxb.api.entity.SpglXmfjxxb;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.spglxmfjxxb.api.ISpglXmfjxxbService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 项目附件信息表详情页面对应的后台
 * 
 * @author dahe
 * @version [版本号, 2024-10-17 18:08:02]
 */
@RightRelation(SpglXmfjxxbListAction.class)
@RestController("spglxmfjxxbdetailaction")
@Scope("request")
public class SpglXmfjxxbDetailAction  extends BaseController
{
	  @Autowired
      private ISpglXmfjxxbService service; 
    
    /**
     * 项目附件信息表实体对象
     */
  	private SpglXmfjxxb dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new SpglXmfjxxb();  
		  }
    }
   
   
	      public SpglXmfjxxb getDataBean()
	      {
	          return dataBean;
	      }
}