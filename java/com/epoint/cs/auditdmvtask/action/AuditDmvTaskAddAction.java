package com.epoint.cs.auditdmvtask.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.cs.auditdmvtask.api.entity.AuditDmvTask;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.cs.auditdmvtask.api.IAuditDmvTaskService;

/**
 * 车管所事项新增页面对应的后台
 * 
 * @author admin
 * @version [版本号, 2021-03-02 11:37:44]
 */
@RightRelation(AuditDmvTaskListAction.class)
@RestController("auditdmvtaskaddaction")
@Scope("request")
public class AuditDmvTaskAddAction  extends BaseController
{
	@Autowired
	private IAuditDmvTaskService service;  
    /**
     * 车管所事项实体对象
     */
  	private AuditDmvTask dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new AuditDmvTask();
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
		dataBean = new AuditDmvTask();
	}

    public AuditDmvTask getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new AuditDmvTask();
        }
        return dataBean;
    }

    public void setDataBean(AuditDmvTask dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
