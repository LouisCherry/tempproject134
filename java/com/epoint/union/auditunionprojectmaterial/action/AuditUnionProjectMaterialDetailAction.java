package com.epoint.union.auditunionprojectmaterial.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.union.auditunionprojectmaterial.api.entity.AuditUnionProjectMaterial;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.union.auditunionprojectmaterial.api.IAuditUnionProjectMaterialService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 异地通办材料信息详情页面对应的后台
 * 
 * @author zhaoyan
 * @version [版本号, 2020-03-22 11:18:43]
 */
@RightRelation(AuditUnionProjectMaterialListAction.class)
@RestController("auditunionprojectmaterialdetailaction")
@Scope("request")
public class AuditUnionProjectMaterialDetailAction  extends BaseController
{
	  @Autowired
      private IAuditUnionProjectMaterialService service; 
    
    /**
     * 异地通办材料信息实体对象
     */
  	private AuditUnionProjectMaterial dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new AuditUnionProjectMaterial();  
		  }
    }
   
   
	      public AuditUnionProjectMaterial getDataBean()
	      {
	          return dataBean;
	      }
}