package com.epoint.jnzwfw.auditproject.auditprojectformjgxk.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.jnzwfw.auditproject.auditprojectformjgxk.api.entity.AuditProjectFormJgxk;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.jnzwfw.auditproject.auditprojectformjgxk.api.IAuditProjectFormJgxkService;

/**
 * 竣工信息表新增页面对应的后台
 * 
 * @author 86180
 * @version [版本号, 2019-07-08 15:07:59]
 */
@RightRelation(AuditProjectFormJgxkListAction.class)
@RestController("auditprojectformjgxkaddaction")
@Scope("request")
public class AuditProjectFormJgxkAddAction  extends BaseController
{
	@Autowired
	private IAuditProjectFormJgxkService service;  
    /**
     * 竣工信息表实体对象
     */
  	private AuditProjectFormJgxk dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new AuditProjectFormJgxk();
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
		dataBean = new AuditProjectFormJgxk();
	}

    public AuditProjectFormJgxk getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new AuditProjectFormJgxk();
        }
        return dataBean;
    }

    public void setDataBean(AuditProjectFormJgxk dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
