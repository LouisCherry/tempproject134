package com.epoint.xmz.yjsczcapplyer.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.yjsczcapplyer.api.entity.YjsCzcApplyer;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.yjsczcapplyer.api.IYjsCzcApplyerService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 一件事住租车申请人信息表详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-04-18 15:38:32]
 */
@RightRelation(YjsCzcApplyerListAction.class)
@RestController("yjsczcapplyerdetailaction")
@Scope("request")
public class YjsCzcApplyerDetailAction  extends BaseController
{
	  @Autowired
      private IYjsCzcApplyerService service; 
    
    /**
     * 一件事住租车申请人信息表实体对象
     */
  	private YjsCzcApplyer dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new YjsCzcApplyer();  
		  }
    }
   
   
	      public YjsCzcApplyer getDataBean()
	      {
	          return dataBean;
	      }
}