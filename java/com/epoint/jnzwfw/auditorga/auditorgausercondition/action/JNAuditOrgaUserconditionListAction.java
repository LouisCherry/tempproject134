package com.epoint.jnzwfw.auditorga.auditorgausercondition.action;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.jnzwfw.auditorga.auditorgausercondition.api.entity.AuditOrgaUsercondition;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.jnzwfw.auditorga.auditorgausercondition.api.IAuditOrgaUserconditionService;


/**
 * 人员在岗信息表list页面对应的后台
 * 
 * @author zhaoy
 * @version [版本号, 2019-05-04 17:10:14]
 */
@RestController("jnauditorgauserconditionlistaction")
@Scope("request")
public class JNAuditOrgaUserconditionListAction  extends BaseController
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
    private AuditOrgaUsercondition dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<AuditOrgaUsercondition> model;
  	
    /**
     * 导出模型
     */
    private ExportModel exportModel;
    
    private List<SelectItem> DepartmentModel = null;
    private List<SelectItem> UserstateModel = null;
    
    private String username;
    
    private String department;
    
    private String userstate;
  
    public void pageLoad()
    {
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect()
    {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
             service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }
    
    public DataGridModel<AuditOrgaUsercondition> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditOrgaUsercondition>()
            {

                @Override
                public List<AuditOrgaUsercondition> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<AuditOrgaUsercondition> list = service.findList(
                            ListGenerator.generateSql("audit_orga_usercondition", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.findList(ListGenerator.generateSql("audit_orga_usercondition", conditionSql, sortField, sortOrder), 
                    conditionList.toArray()).size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }
    
    public void saveAll(){
        List<AuditOrgaUsercondition> list = getDataGridData().getWrappedData();
        Iterator<AuditOrgaUsercondition> ite = list.iterator();
        while (ite.hasNext()){
            AuditOrgaUsercondition record = ite.next();
            if(record.getBoolean("_checked") != null && record.getBoolean("_checked")){
                AuditOrgaUsercondition condition = new AuditOrgaUsercondition();
                condition.setOrdernumber(record.getOrdernumber());
                condition.setRowguid(record.getRowguid());
                service.update(condition);
            }
        }
        this.addCallbackParam("msg", "保存成功");
    }

    
    public AuditOrgaUsercondition getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new AuditOrgaUsercondition();
    	}
        return dataBean;
    }

    public void setDataBean(AuditOrgaUsercondition dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("department,username,userstate", "科室名称,人员名称,在岗情况");
        }
        return exportModel;
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



    public String getUsername() {
        return username;
    }



    public void setUsername(String username) {
        this.username = username;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUserstate() {
        return userstate;
    }

    public void setUserstate(String userstate) {
        this.userstate = userstate;
    }

}
