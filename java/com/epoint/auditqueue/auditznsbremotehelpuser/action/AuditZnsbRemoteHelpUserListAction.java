package com.epoint.auditqueue.auditznsbremotehelpuser.action;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;

import com.epoint.basic.auditqueue.auditznsbremotehelpuser.domain.AuditZnsbRemoteHelpUser;
import com.epoint.basic.auditqueue.auditznsbremotehelpuser.inter.IAuditZnsbRemoteHelpUserService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * 一体机绑定好视通账户list页面对应的后台
 * 
 * @author JackLove
 * @version [版本号, 2018-04-12 15:24:50]
 */
@RestController("auditznsbremotehelpuserlistaction")
@Scope("request")
public class AuditZnsbRemoteHelpUserListAction  extends BaseController
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IAuditZnsbRemoteHelpUserService auditZnsbAssessConfigService;
    @Autowired
    private IUserService userService;
    
    /**
     * 一体机绑定好视通账户实体对象
     */
    private AuditZnsbRemoteHelpUser dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<AuditZnsbRemoteHelpUser> model;
  	
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
            auditZnsbAssessConfigService.deletebyRowGuid(sel);
        }
        addCallbackParam("msg", "删除成功！");
    }

	


    public DataGridModel<AuditZnsbRemoteHelpUser> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditZnsbRemoteHelpUser>()
            {

                @Override
                public List<AuditZnsbRemoteHelpUser> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
                    if (StringUtil.isNotBlank(username)){
                        sql.like("account", username);
                    }                                      
                    PageData<AuditZnsbRemoteHelpUser> pageData =auditZnsbAssessConfigService
                            .getFileByPage(sql.getMap(),first, pageSize, "", sortOrder).getResult();
            
                    for(AuditZnsbRemoteHelpUser auditZnsbAssessconfig : pageData.getList()){
                        FrameUser frameUser = userService.getUserByUserField("userguid", auditZnsbAssessconfig.getUserguid());
                        if(frameUser != null){
                          auditZnsbAssessconfig.put("displayName", frameUser.getDisplayName());
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
    
    public AuditZnsbRemoteHelpUser getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new AuditZnsbRemoteHelpUser();
    	}
        return dataBean;
    }

    public void setDataBean(AuditZnsbRemoteHelpUser dataBean)
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
