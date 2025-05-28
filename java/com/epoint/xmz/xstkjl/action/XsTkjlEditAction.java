package com.epoint.xmz.xstkjl.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xstkjl.api.entity.XsTkjl;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.xstkjl.api.IXsTkjlService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 国土踏勘记录修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:18:51]
 */
@RightRelation(XsTkjlListAction.class)
@RestController("xstkjleditaction")
@Scope("request")
public class XsTkjlEditAction  extends BaseController
{

	@Autowired
	private IXsTkjlService service;
    
    /**
     * 国土踏勘记录实体对象
     */
  	private XsTkjl dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new XsTkjl();  
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

	public XsTkjl getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(XsTkjl dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
