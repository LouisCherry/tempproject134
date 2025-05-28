package com.epoint.mq.spgl.action;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.mq.spgl.api.IJnOptionInService;
import com.epoint.mq.spgl.api.entity.JnOptionIn;
/**
 * 成品油零售经营企业库修改页面对应的后台
 * 
 * @author 18039505500
 * @version [版本号, 2020-06-23 19:57:28]
 */
@RightRelation(JnOptionInListAction.class)
@RestController("jnoptionineditaction")
@Scope("request")
public class JnOptionInEditAction  extends BaseController
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7319735070197062451L;

	@Autowired
	private IJnOptionInService service;
    
    /**
     * 成品油零售经营企业库实体对象
     */
  	private JnOptionIn dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new JnOptionIn();  
	   }
    }

    /**
     * 保存修改
     * 
     */
	public void save() 
	{
	    dataBean.setOperatedate(new Date());
	    service.update(dataBean);
	    addCallbackParam("msg", "修改成功！");
	}

	public JnOptionIn getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(JnOptionIn dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
