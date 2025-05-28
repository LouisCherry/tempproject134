package com.epoint.znsb.auditznsbwaterjfinfo.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.znsb.auditznsbwaterjfinfo.api.entity.AuditZnsbWaterjfinfo;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.znsb.auditznsbwaterjfinfo.api.IAuditZnsbWaterjfinfoService;

/**
 * 水务缴费信息新增页面对应的后台
 * 
 * @author HYF
 * @version [版本号, 2021-11-11 14:49:42]
 */
@RightRelation(AuditZnsbWaterjfinfoListAction.class)
@RestController("auditznsbwaterjfinfoaddaction")
@Scope("request")
public class AuditZnsbWaterjfinfoAddAction  extends BaseController
{
	@Autowired
	private IAuditZnsbWaterjfinfoService service;  
    /**
     * 水务缴费信息实体对象
     */
  	private AuditZnsbWaterjfinfo dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new AuditZnsbWaterjfinfo();
    }

    /**
     * 保存并关闭
     * 
     */
	public void add()
    {
        dataBean.setRowguid( UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
	    service.insert(dataBean);
	    addCallbackParam("msg", "保存成功！");
	    dataBean = null;
	}

    /**
     * 保存并新建
     * 
     */
	public void addNew() {
		add();
		dataBean = new AuditZnsbWaterjfinfo();
	}

    public AuditZnsbWaterjfinfo getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new AuditZnsbWaterjfinfo();
        }
        return dataBean;
    }

    public void setDataBean(AuditZnsbWaterjfinfo dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
