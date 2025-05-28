package com.epoint.xmz.zjknowledge.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.zjknowledge.api.entity.ZjKnowledge;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.zjknowledge.api.IZjKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自建系统知识库表详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2021-10-25 15:12:09]
 */
@RightRelation(ZjKnowledgeListAction.class)
@RestController("zjknowledgedetailaction")
@Scope("request")
public class ZjKnowledgeDetailAction  extends BaseController
{
	  @Autowired
      private IZjKnowledgeService service; 
    
    /**
     * 自建系统知识库表实体对象
     */
  	private ZjKnowledge dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new ZjKnowledge();  
		  }
    }
   
   
	      public ZjKnowledge getDataBean()
	      {
	          return dataBean;
	      }
}