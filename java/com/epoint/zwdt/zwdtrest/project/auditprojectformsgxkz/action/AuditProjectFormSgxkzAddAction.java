package com.epoint.zwdt.zwdtrest.project.auditprojectformsgxkz.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.zwdt.zwdtrest.project.auditprojectformsgxkz.api.IAuditProjectFormSgxkzService;
import com.epoint.zwdt.zwdtrest.project.auditprojectformsgxkz.api.entity.AuditProjectFormSgxkz;
import com.epoint.basic.controller.BaseController;

/**
 * 施工许可证表单新增页面对应的后台
 * 
 * @author zhaoy
 * @version [版本号, 2019-06-02 15:13:10]
 */
@RightRelation(AuditProjectFormSgxkzListAction.class)
@RestController("auditprojectformsgxkzaddaction")
@Scope("request")
public class AuditProjectFormSgxkzAddAction  extends BaseController
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Autowired
	private IAuditProjectFormSgxkzService service;  
    /**
     * 施工许可证表单实体对象
     */
  	private AuditProjectFormSgxkz dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new AuditProjectFormSgxkz();
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
		dataBean = new AuditProjectFormSgxkz();
	}

    public AuditProjectFormSgxkz getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new AuditProjectFormSgxkz();
        }
        return dataBean;
    }

    public void setDataBean(AuditProjectFormSgxkz dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
