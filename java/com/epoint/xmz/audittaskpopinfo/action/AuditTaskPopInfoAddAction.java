package com.epoint.xmz.audittaskpopinfo.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.audittaskpopinfo.api.entity.AuditTaskPopInfo;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.audittaskpopinfo.api.IAuditTaskPopInfoService;

/**
 * 弹窗信息维护新增页面对应的后台
 * 
 * @author dahe
 * @version [版本号, 2024-11-26 10:20:20]
 */
@RightRelation(AuditTaskPopInfoListAction.class)
@RestController("audittaskpopinfoaddaction")
@Scope("request")
public class AuditTaskPopInfoAddAction  extends BaseController
{
	@Autowired
	private IAuditTaskPopInfoService service;  
    /**
     * 弹窗信息维护实体对象
     */
  	private AuditTaskPopInfo dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new AuditTaskPopInfo();
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
	    addCallbackParam("msg", l("保存成功！"));
	    dataBean = null;
	}

    /**
     * 保存并新建
     * 
     */
	public void addNew() {
		add();
		dataBean = new AuditTaskPopInfo();
	}

    public AuditTaskPopInfo getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new AuditTaskPopInfo();
        }
        return dataBean;
    }

    public void setDataBean(AuditTaskPopInfo dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
