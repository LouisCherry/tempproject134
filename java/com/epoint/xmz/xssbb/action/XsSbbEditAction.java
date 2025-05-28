package com.epoint.xmz.xssbb.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xssbb.api.entity.XsSbb;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.xssbb.api.IXsSbbService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 国土_申报表修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 15:34:58]
 */
@RightRelation(XsSbbListAction.class)
@RestController("xssbbeditaction")
@Scope("request")
public class XsSbbEditAction  extends BaseController
{

	@Autowired
	private IXsSbbService service;
    
    /**
     * 国土_申报表实体对象
     */
  	private XsSbb dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new XsSbb();  
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

	public XsSbb getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(XsSbb dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
