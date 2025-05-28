package com.epoint.xmz.rpagxbyxxb.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.rpagxbyxxb.api.entity.RpaGxbyxxb;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.rpagxbyxxb.api.IRpaGxbyxxbService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 高校毕业信息表详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-12-23 16:40:09]
 */
@RightRelation(RpaGxbyxxbListAction.class)
@RestController("rpagxbyxxbdetailaction")
@Scope("request")
public class RpaGxbyxxbDetailAction  extends BaseController
{
	  @Autowired
      private IRpaGxbyxxbService service; 
    
    /**
     * 高校毕业信息表实体对象
     */
  	private RpaGxbyxxb dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new RpaGxbyxxb();  
		  }
    }
   
   
	      public RpaGxbyxxb getDataBean()
	      {
	          return dataBean;
	      }
}