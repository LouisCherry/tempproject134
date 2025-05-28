package com.epoint.xmz.audittaskpopinfo.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.audittaskpopinfo.api.entity.AuditTaskPopInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.audittaskpopinfo.api.IAuditTaskPopInfoService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 弹窗信息维护修改页面对应的后台
 * 
 * @author dahe
 * @version [版本号, 2024-11-26 10:20:20]
 */
@RightRelation(AuditTaskPopInfoListAction.class)
@RestController("audittaskpopinfoeditaction")
@Scope("request")
public class AuditTaskPopInfoEditAction  extends BaseController
{

	@Autowired
	private IAuditTaskPopInfoService service;
    
    /**
     * 弹窗信息维护实体对象
     */
  	private AuditTaskPopInfo dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new AuditTaskPopInfo();  
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
	    addCallbackParam("msg", l("修改成功")+"！");
	}

	public AuditTaskPopInfo getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(AuditTaskPopInfo dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
