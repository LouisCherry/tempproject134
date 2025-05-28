package com.epoint.znsb.auditznsbwaterjfinfo.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.znsb.auditznsbwaterjfinfo.api.entity.AuditZnsbWaterjfinfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.znsb.auditznsbwaterjfinfo.api.IAuditZnsbWaterjfinfoService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 水务缴费信息修改页面对应的后台
 * 
 * @author HYF
 * @version [版本号, 2021-11-11 14:49:42]
 */
@RightRelation(AuditZnsbWaterjfinfoListAction.class)
@RestController("auditznsbwaterjfinfoeditaction")
@Scope("request")
public class AuditZnsbWaterjfinfoEditAction  extends BaseController
{

	@Autowired
	private IAuditZnsbWaterjfinfoService service;
    
    /**
     * 水务缴费信息实体对象
     */
  	private AuditZnsbWaterjfinfo dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new AuditZnsbWaterjfinfo();  
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

	public AuditZnsbWaterjfinfo getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(AuditZnsbWaterjfinfo dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
