package com.epoint.union.auditunionprojectmaterial.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.union.auditunionprojectmaterial.api.entity.AuditUnionProjectMaterial;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.union.auditunionprojectmaterial.api.IAuditUnionProjectMaterialService;

/**
 * 异地通办材料信息新增页面对应的后台
 * 
 * @author zhaoyan
 * @version [版本号, 2020-03-22 11:18:43]
 */
@RightRelation(AuditUnionProjectMaterialListAction.class)
@RestController("auditunionprojectmaterialaddaction")
@Scope("request")
public class AuditUnionProjectMaterialAddAction  extends BaseController
{
	@Autowired
	private IAuditUnionProjectMaterialService service;  
    /**
     * 异地通办材料信息实体对象
     */
  	private AuditUnionProjectMaterial dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new AuditUnionProjectMaterial();
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
		dataBean = new AuditUnionProjectMaterial();
	}

    public AuditUnionProjectMaterial getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new AuditUnionProjectMaterial();
        }
        return dataBean;
    }

    public void setDataBean(AuditUnionProjectMaterial dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
