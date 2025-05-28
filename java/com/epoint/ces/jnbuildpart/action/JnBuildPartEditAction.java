package com.epoint.ces.jnbuildpart.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.ces.jnbuildpart.api.entity.JnBuildPart;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.ces.jnbuildpart.api.IJnBuildPartService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 建筑业企业资质数据库修改页面对应的后台
 * 
 * @author 86180
 * @version [版本号, 2020-04-15 11:11:06]
 */
@RightRelation(JnBuildPartListAction.class)
@RestController("jnbuildparteditaction")
@Scope("request")
public class JnBuildPartEditAction  extends BaseController
{

	@Autowired
	private IJnBuildPartService service;
    
    /**
     * 建筑业企业资质数据库实体对象
     */
  	private JnBuildPart dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new JnBuildPart();  
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

	public JnBuildPart getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(JnBuildPart dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
