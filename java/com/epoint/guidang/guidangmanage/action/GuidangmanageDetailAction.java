package com.epoint.guidang.guidangmanage.action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.guidang.guidangmanage.api.IGuidangmanageService;
import com.epoint.guidang.guidangmanage.entity.Guidangmanage;


/**
 * 归档管理详情页面对应的后台
 * 
 * @author chengninghua
 * @version [版本号, 2017-12-15 15:11:49]
 */
@RightRelation(GuidangmanageListAction.class)
@RestController("guidangmanagedetailaction")
@Scope("request")
public class GuidangmanageDetailAction  extends BaseController
{
	  /**
     * 
     */
    private static final long serialVersionUID = -5866759165042774979L;

    @Autowired
      private IGuidangmanageService service; 
    
    /**
     * 归档管理实体对象
     */
  	private Guidangmanage dataBean=null;
  	@Override
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new Guidangmanage();  
		  }
    }
   
   
	      public Guidangmanage getDataBean()
	      {
	          return dataBean;
	      }
}