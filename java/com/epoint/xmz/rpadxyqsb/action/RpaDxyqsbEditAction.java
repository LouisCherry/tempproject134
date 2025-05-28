package com.epoint.xmz.rpadxyqsb.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.rpadxyqsb.api.entity.RpaDxyqsb;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.rpadxyqsb.api.IRpaDxyqsbService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 大型仪器设备协作共用网修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-12-20 17:36:22]
 */
@RightRelation(RpaDxyqsbListAction.class)
@RestController("rpadxyqsbeditaction")
@Scope("request")
public class RpaDxyqsbEditAction  extends BaseController
{

	@Autowired
	private IRpaDxyqsbService service;
    
    /**
     * 大型仪器设备协作共用网实体对象
     */
  	private RpaDxyqsb dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new RpaDxyqsb();  
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

	public RpaDxyqsb getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(RpaDxyqsb dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
