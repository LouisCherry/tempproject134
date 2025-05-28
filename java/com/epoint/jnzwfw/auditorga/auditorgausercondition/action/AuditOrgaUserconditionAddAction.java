package com.epoint.jnzwfw.auditorga.auditorgausercondition.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.jnzwfw.auditorga.auditorgausercondition.api.entity.AuditOrgaUsercondition;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.BaseController;
import com.epoint.jnzwfw.auditorga.auditorgausercondition.api.IAuditOrgaUserconditionService;

/**
 * 人员在岗信息表新增页面对应的后台
 * 
 * @author zhaoy
 * @version [版本号, 2019-05-04 17:10:14]
 */
@RightRelation(JNAuditOrgaUserconditionListAction.class)
@RestController("auditorgauserconditionaddaction")
@Scope("request")
public class AuditOrgaUserconditionAddAction  extends BaseController
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Autowired
	private IAuditOrgaUserconditionService service;  
    /**
     * 人员在岗信息表实体对象
     */
  	private AuditOrgaUsercondition dataBean=null;
  	
  	private List<SelectItem> DepartmentModel = null;
    private List<SelectItem> UserstateModel = null;

    public void pageLoad()
    {
        dataBean=new AuditOrgaUsercondition();
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
		dataBean = new AuditOrgaUsercondition();
	}

    public AuditOrgaUsercondition getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new AuditOrgaUsercondition();
        }
        return dataBean;
    }

    public void setDataBean(AuditOrgaUsercondition dataBean)
    {
        this.dataBean = dataBean;
    }
    
    @SuppressWarnings("unchecked")
    public List<SelectItem> getDepartmentModel() {
        if (this.DepartmentModel == null) {
            this.DepartmentModel = DataUtil
                    .convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "科室名称", (String) null, false));
        }

        return this.DepartmentModel;
    }
    
    @SuppressWarnings("unchecked")
    public List<SelectItem> getUserstateModel() {
        if (this.UserstateModel == null) {
            this.UserstateModel = DataUtil
                    .convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "在岗情况", (String) null, false));
        }

        return this.UserstateModel;
    }

}
