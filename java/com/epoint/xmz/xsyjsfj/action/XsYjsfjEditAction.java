package com.epoint.xmz.xsyjsfj.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xsyjsfj.api.entity.XsYjsfj;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.xsyjsfj.api.IXsYjsfjService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 国土上传预审批复函修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:34:42]
 */
@RightRelation(XsYjsfjListAction.class)
@RestController("xsyjsfjeditaction")
@Scope("request")
public class XsYjsfjEditAction  extends BaseController
{

	@Autowired
	private IXsYjsfjService service;
    
    /**
     * 国土上传预审批复函实体对象
     */
  	private XsYjsfj dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new XsYjsfj();  
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

	public XsYjsfj getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(XsYjsfj dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
