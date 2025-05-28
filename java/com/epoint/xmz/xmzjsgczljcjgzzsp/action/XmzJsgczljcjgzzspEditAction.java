package com.epoint.xmz.xmzjsgczljcjgzzsp.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xmzjsgczljcjgzzsp.api.entity.XmzJsgczljcjgzzsp;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.xmzjsgczljcjgzzsp.api.IXmzJsgczljcjgzzspService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 建设工程质量检测机构资质审批表修改页面对应的后台
 * 
 * @author 86177
 * @version [版本号, 2021-05-08 17:01:26]
 */
@RightRelation(XmzJsgczljcjgzzspListAction.class)
@RestController("xmzjsgczljcjgzzspeditaction")
@Scope("request")
public class XmzJsgczljcjgzzspEditAction  extends BaseController
{

	@Autowired
	private IXmzJsgczljcjgzzspService service;
    
    /**
     * 建设工程质量检测机构资质审批表实体对象
     */
  	private XmzJsgczljcjgzzsp dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new XmzJsgczljcjgzzsp();  
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

	public XmzJsgczljcjgzzsp getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(XmzJsgczljcjgzzsp dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
