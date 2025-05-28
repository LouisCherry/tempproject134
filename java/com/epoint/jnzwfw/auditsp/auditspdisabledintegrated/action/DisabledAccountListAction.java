package com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.IAuditYjsCjrService;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.entity.CjrApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * 残联账号表list页面对应的后台
 *
 * @author ez
 * @version [版本号, 2021-04-15 16:18:31]
 */
@RestController("disabledaccountlistaction")
@Scope("request")
public class DisabledAccountListAction extends BaseController {
    @Autowired
    private IAuditYjsCjrService service;

    /**
     * 残联账号表实体对象
     */
    private CjrApiConfig dataBean;
    
    @Autowired
    private IRoleService iRoleService;

    /**
     * 表格控件model
     */
    private DataGridModel<CjrApiConfig> model;


    @Override
    public void pageLoad() {
    }


    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<CjrApiConfig> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<CjrApiConfig>() {

                @Override
                public List<CjrApiConfig> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    if (!iRoleService.isExistUserRoleName(userSession.getUserGuid(), "中心管理员")) {
                    	conditionSql += " and operateusername = '" +userSession.getDisplayName() +  "'";
                    }
                   
                    List<CjrApiConfig> list = service.findList(CjrApiConfig.class,
                            ListGenerator.generateSql("cjr_api_config", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.count(ListGenerator.generateSql("cjr_api_config", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }


    public CjrApiConfig getDataBean() {
        if (dataBean == null) {
            dataBean = new CjrApiConfig();
        }
        return dataBean;
    }

    public void setDataBean(CjrApiConfig dataBean) {
        this.dataBean = dataBean;
    }


}
