package com.epoint.cpy.jnaicpy.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.cpy.jnaicpy.api.entity.JnAiCpy;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.cpy.jnaicpy.api.IJnAiCpyService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 成品油零售经营企业库修改页面对应的后台
 * 
 * @author 18039505500
 * @version [版本号, 2020-06-23 19:57:28]
 */
@RightRelation(JnAiCpyListAction.class)
@RestController("jnaicpyeditaction")
@Scope("request")
public class JnAiCpyEditAction  extends BaseController
{

	@Autowired
	private IJnAiCpyService service;
    
    /**
     * 成品油零售经营企业库实体对象
     */
  	private JnAiCpy dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new JnAiCpy();  
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

	public JnAiCpy getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(JnAiCpy dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
