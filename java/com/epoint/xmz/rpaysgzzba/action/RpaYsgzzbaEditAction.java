package com.epoint.xmz.rpaysgzzba.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.rpaysgzzba.api.entity.RpaYsgzzba;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.rpaysgzzba.api.IRpaYsgzzbaService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 院士工作站备案修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-12-20 10:26:25]
 */
@RightRelation(RpaYsgzzbaListAction.class)
@RestController("rpaysgzzbaeditaction")
@Scope("request")
public class RpaYsgzzbaEditAction  extends BaseController
{

	@Autowired
	private IRpaYsgzzbaService service;
    
    /**
     * 院士工作站备案实体对象
     */
  	private RpaYsgzzba dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new RpaYsgzzba();  
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

	public RpaYsgzzba getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(RpaYsgzzba dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
