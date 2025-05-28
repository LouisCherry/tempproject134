package com.epoint.jnzwfw.auditproject.auditprojectformsgxkzdanti.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkzdanti.api.IAuditProjectFormSgxkzDantiService;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkzdanti.api.entity.AuditProjectFormSgxkzDanti;
import com.epoint.basic.controller.BaseController;

/**
 * 施工许可单体记录表新增页面对应的后台
 * 
 * @author zhaoy
 * @version [版本号, 2019-06-02 22:59:17]
 */
@RightRelation(AuditProjectFormSgxkzDantiListAction.class)
@RestController("auditprojectformsgxkzdantiaddaction")
@Scope("request")
public class AuditProjectFormSgxkzDantiAddAction  extends BaseController
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Autowired
	private IAuditProjectFormSgxkzDantiService service;  
    
    private List<SelectItem> seismicIntensityScaleModel = null;
    
    private List<SelectItem> iSSuperHightBuilding = null;
    
    /**
     * 施工许可单体记录表实体对象
     */
  	private AuditProjectFormSgxkzDanti dataBean=null;
  
  

    public void pageLoad()
    {
        String projectguid = getRequestParameter("projectguid");
        dataBean=new AuditProjectFormSgxkzDanti();
        dataBean.setProjectguid(projectguid);
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
		dataBean = new AuditProjectFormSgxkzDanti();
	}

    public AuditProjectFormSgxkzDanti getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new AuditProjectFormSgxkzDanti();
        }
        return dataBean;
    }

    public void setDataBean(AuditProjectFormSgxkzDanti dataBean)
    {
        this.dataBean = dataBean;
    }
    @SuppressWarnings("unchecked")
    public List<SelectItem> getSeismicIntensityScaleModel() {
        if (seismicIntensityScaleModel == null) {
            seismicIntensityScaleModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "抗震设防烈度", null, false));
        }
        return this.seismicIntensityScaleModel;
    }
    
    @SuppressWarnings("unchecked")
    public List<SelectItem> getISSuperHightBuildingModel() {
        if (iSSuperHightBuilding == null) {
            iSSuperHightBuilding = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
        }
        return this.iSSuperHightBuilding;
    }

    
}
