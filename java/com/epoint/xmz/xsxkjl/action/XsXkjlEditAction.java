package com.epoint.xmz.xsxkjl.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xsxkjl.api.entity.XsXkjl;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.xsxkjl.api.IXsXkjlService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 国土许可记录修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:26:17]
 */
@RightRelation(XsXkjlListAction.class)
@RestController("xsxkjleditaction")
@Scope("request")
public class XsXkjlEditAction  extends BaseController
{

	@Autowired
	private IXsXkjlService service;
    
    /**
     * 国土许可记录实体对象
     */
  	private XsXkjl dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new XsXkjl();  
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

	public XsXkjl getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(XsXkjl dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
