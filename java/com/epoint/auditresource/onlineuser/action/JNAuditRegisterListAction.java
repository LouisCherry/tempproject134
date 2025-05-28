package com.epoint.auditresource.onlineuser.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.code.MD5Util;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 网上注册用户list页面对应的后台
 * 
 * @author WeiY
 * @version [版本号, 2016-09-26 11:13:04]
 */
@Component
@RestController("jnauditregisterlistaction")
@Scope("request")
public class JNAuditRegisterListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IAuditOnlineRegister auditOnlineRegisterImpl;

    /**
     * 网上注册用户实体对象
     */
    private AuditOnlineRegister dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditOnlineRegister> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;
    
    @Autowired
    private IHandleConfig handleConfigService;
    
    public void pageLoad() {
        
    }

    /**
     * 初始化密码
     * 
     */
    public void initPassword() {
        String rowguid = getDataGridData().getSelectKeys().get(0);
        dataBean = auditOnlineRegisterImpl.getRegisterByRowguid(rowguid).getResult();
        dataBean.setOperatedate(new Date());
        dataBean.setPassword(MD5Util.getMD5("111111"));
        dataBean.setPwdlevel(ZwfwConstant.CONSTANT_STR_ONE);
        auditOnlineRegisterImpl.updateRegister(dataBean);
        addCallbackParam("msg", "密码初始化成功");
        dataBean = null;
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            dataBean = auditOnlineRegisterImpl.getRegisterByRowguid(sel).getResult();
            auditOnlineRegisterImpl.deleteRegister(dataBean);
        }
        addCallbackParam("msg", "删除用户成功！");
    }

    public DataGridModel<AuditOnlineRegister> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditOnlineRegister>()
            {

                @Override
                public List<AuditOnlineRegister> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(dataBean.getUsername())) {
                        sql.like("username", dataBean.getUsername());
                    }
                    if (StringUtil.isNotBlank(dataBean.getMobile())) {
                        sql.like("mobile", dataBean.getMobile());
                    }
                    PageData<AuditOnlineRegister> pageData = auditOnlineRegisterImpl
                            .selectAuditOnlineRegisterByPage(sql.getMap(), first, pageSize, sortField, sortOrder)
                            .getResult();

                    if (pageData != null && pageData.getList() != null) {
                        for (AuditOnlineRegister auditOnlineRegister : pageData.getList()) {
                            // 返回登录名
                            if (StringUtil.isBlank(auditOnlineRegister.getLoginid())) {
                                auditOnlineRegister.put("loginid", "无");
                            }
                            else {
                                auditOnlineRegister.put("loginid", auditOnlineRegister.getLoginid());
                            }
                        }
                        this.setRowCount(pageData.getRowCount());
                        return pageData.getList();
                    }else{
                        this.setRowCount(0);
                        return new ArrayList<AuditOnlineRegister>();
                    }
                }

            };
        }
        return model;
    }

    public AuditOnlineRegister getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditOnlineRegister();
        }
        return dataBean;
    }

    public void setDataBean(AuditOnlineRegister dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

}
