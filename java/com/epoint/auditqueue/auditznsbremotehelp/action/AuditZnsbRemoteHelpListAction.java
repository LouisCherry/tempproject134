package com.epoint.auditqueue.auditznsbremotehelp.action;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;

import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbremotehelp.domain.AuditZnsbRemoteHelp;
import com.epoint.basic.auditqueue.auditznsbremotehelp.inter.IAuditZnsbRemoteHelpService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 一体机绑定好视通账户list页面对应的后台
 * 
 * @author JackLove
 * @version [版本号, 2018-04-12 15:24:50]
 */
@RestController("auditznsbremotehelplistaction")
@Scope("request")
public class AuditZnsbRemoteHelpListAction  extends BaseController
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IAuditZnsbRemoteHelpService auditZnsbRemoteHelpService;
    @Autowired
    private IAuditZnsbEquipment equipmentservice ;
    
    /**
     * 一体机绑定好视通账户实体对象
     */
    private AuditZnsbRemoteHelp dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<AuditZnsbRemoteHelp> model;
  	
    /**
     * 导出模型
     */
    private ExportModel exportModel;
    
    String username = "";
    

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
            auditZnsbRemoteHelpService.deletebyRowGuid(sel);
        }
        addCallbackParam("msg", "删除成功！");
    }

	


    public DataGridModel<AuditZnsbRemoteHelp> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditZnsbRemoteHelp>()
            {

                @Override
                public List<AuditZnsbRemoteHelp> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
                    if (StringUtil.isNotBlank(username)){
                        sql.like("account", username);
                    }
                    PageData<AuditZnsbRemoteHelp> pageData =auditZnsbRemoteHelpService
                            .getFileByPage(sql.getMap(),first, pageSize, "", sortOrder).getResult();
                    for(AuditZnsbRemoteHelp auditZnsbRemoteHelp :pageData.getList()){
                        if(StringUtil.isNotBlank(auditZnsbRemoteHelp)){
                            if(StringUtil.isNotBlank(auditZnsbRemoteHelp.getMachineguid())){
                                AuditZnsbEquipment auditZnsbEquipment = equipmentservice.getEquipmentByRowguid(auditZnsbRemoteHelp.getMachineguid()).getResult();
                                if(StringUtil.isNotBlank(auditZnsbEquipment)){
                                    auditZnsbRemoteHelp.put("aioname", auditZnsbEquipment.getMachinename());
                                }
                            }
                        }                  
                    }
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }

            };
        }
        return model;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public AuditZnsbRemoteHelp getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new AuditZnsbRemoteHelp();
    	}
        return dataBean;
    }

    public void setDataBean(AuditZnsbRemoteHelp dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }
    


}
