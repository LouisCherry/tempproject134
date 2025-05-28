package com.epoint.znsb.auditznsbmedical.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.znsb.auditznsbmedical.api.entity.AuditZnsbMedical;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.znsb.auditznsbmedical.api.IAuditZnsbMedicalService;

/**
 * 静态医疗信息查询新增页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2021-04-27 09:56:34]
 */
@RightRelation(AuditZnsbMedicalListAction.class)
@RestController("auditznsbmedicaladdaction")
@Scope("request")
public class AuditZnsbMedicalAddAction  extends BaseController
{
	@Autowired
	private IAuditZnsbMedicalService service;  
    /**
     * 静态医疗信息查询实体对象
     */
  	private AuditZnsbMedical dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new AuditZnsbMedical();
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
		dataBean = new AuditZnsbMedical();
	}

    public AuditZnsbMedical getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new AuditZnsbMedical();
        }
        return dataBean;
    }

    public void setDataBean(AuditZnsbMedical dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
