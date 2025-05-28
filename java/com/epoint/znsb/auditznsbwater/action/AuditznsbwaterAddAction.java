package com.epoint.znsb.auditznsbwater.action;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.znsb.auditznsbwater.api.entity.Auditznsbwater;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.znsb.auditznsbwater.api.IAuditznsbwaterService;

/**
 * 水务对账信息新增页面对应的后台
 * 
 * @author HYF
 * @version [版本号, 2021-11-11 16:08:59]
 */
@RightRelation(AuditznsbwaterListAction.class)
@RestController("auditznsbwateraddaction")
@Scope("request")
public class AuditznsbwaterAddAction  extends BaseController
{
	@Autowired
	private IAuditznsbwaterService service;  
    /**
     * 水务对账信息实体对象
     */
  	private Auditznsbwater dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new Auditznsbwater();
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
        String date = dataBean.getWaterinfo().substring(0,10);
        List<Auditznsbwater> waterlist = service.getLisrtByname(date);
        if(waterlist != null && !waterlist.isEmpty()){
            addCallbackParam("msg", "存在相同的信息！");
            return;
        }

        dataBean.setWaterinfo(date);
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
		dataBean = new Auditznsbwater();
	}

    public Auditznsbwater getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new Auditznsbwater();
        }
        return dataBean;
    }

    public void setDataBean(Auditznsbwater dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
