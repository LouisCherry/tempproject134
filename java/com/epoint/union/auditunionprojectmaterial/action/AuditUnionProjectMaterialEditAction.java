package com.epoint.union.auditunionprojectmaterial.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.union.auditunionprojectmaterial.api.entity.AuditUnionProjectMaterial;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.union.auditunionprojectmaterial.api.IAuditUnionProjectMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 异地通办材料信息修改页面对应的后台
 * 
 * @author zhaoyan
 * @version [版本号, 2020-03-22 11:18:43]
 */
@RightRelation(AuditUnionProjectMaterialListAction.class)
@RestController("auditunionprojectmaterialeditaction")
@Scope("request")
public class AuditUnionProjectMaterialEditAction  extends BaseController
{

	@Autowired
	private IAuditUnionProjectMaterialService service;
    
    /**
     * 异地通办材料信息实体对象
     */
  	private AuditUnionProjectMaterial dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new AuditUnionProjectMaterial();  
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

	public AuditUnionProjectMaterial getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(AuditUnionProjectMaterial dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
