package com.epoint.yjs.yjszn.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.yjs.yjszn.api.IYjsZnService;
import com.epoint.yjs.yjszn.api.entity.YjsZn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 一件事指南配置详情页面对应的后台
 * 
 * @author panshunxing
 * @version [版本号, 2024-10-08 15:22:37]
 */
@RightRelation(YjsZnListAction.class)
@RestController("yjszndetailaction")
@Scope("request")
public class YjsZnDetailAction  extends BaseController
{
	  @Autowired
      private IYjsZnService service; 
    
    /**
     * 一件事指南配置实体对象
     */
  	private YjsZn dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new YjsZn();  
		  }
    }
   
   
	      public YjsZn getDataBean()
	      {
	          return dataBean;
	      }
}