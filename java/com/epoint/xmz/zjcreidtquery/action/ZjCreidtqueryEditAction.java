package com.epoint.xmz.zjcreidtquery.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.zjcreidtquery.api.entity.ZjCreidtquery;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.zjcreidtquery.api.IZjCreidtqueryService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 信用查询调用统计表修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2021-10-09 14:58:46]
 */
@RightRelation(ZjCreidtqueryListAction.class)
@RestController("zjcreidtqueryeditaction")
@Scope("request")
public class ZjCreidtqueryEditAction  extends BaseController
{

	@Autowired
	private IZjCreidtqueryService service;
    
    /**
     * 信用查询调用统计表实体对象
     */
  	private ZjCreidtquery dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new ZjCreidtquery();  
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

	public ZjCreidtquery getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(ZjCreidtquery dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
