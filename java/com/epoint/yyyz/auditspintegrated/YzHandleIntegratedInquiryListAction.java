package com.epoint.yyyz.auditspintegrated;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 申报查询列表页面对应的后台
 * @author Administrator
 *
 */
@RestController("yzhandleintegratedinquirylistaction")
@Scope("request")
public class YzHandleIntegratedInquiryListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 8713148757403132395L;
    private AuditSpBusiness dataBean;
    /**
     * 表格控件model
     */
    private DataGridModel<AuditSpInstance> model;
    /**
     * 建设单位
     */
    private String applyername;
    /**
     * 申报名称
     */
    private String itemname;
    @Autowired
    private IAuditSpInstance spInstanceService;
    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;
    
    @Override
    public void pageLoad() {

    }

    /**
     * 
     *  获取详细页面地址
     * 
     *  @param biGuid  
     */
    public void checkDetail(String biGuid) {
        String registerUrl = "";
        AuditSpInstance auditSpInstance = spInstanceService.getDetailByBIGuid(biGuid).getResult();
        AuditSpBusiness auditSpBusiness = iAuditSpBusiness
                .getAuditSpBusinessByRowguid(auditSpInstance.getBusinessguid()).getResult();
        String businessType = auditSpBusiness.getBusinesstype();
        String filepath = auditSpBusiness.getHandleURL();
        if (StringUtil.isNotBlank(filepath)) {
            registerUrl = filepath + (filepath.substring(filepath.lastIndexOf("/"), filepath.length()) + "detail")
                    + "?guid=" + biGuid;
        }
        else {
            if ("1".equals(businessType)) {
                // 建设项目
                registerUrl = "epointzwfw/auditsp/auditsphandle/handlebilistviewdetail?guid=" + biGuid;
            }
            else if ("2".equals(businessType)) {
                // 一般并联审批
                registerUrl = "epointzwfw/auditsp/auditspintegrated/auditspintegrateddetail?guid=" + biGuid;
            }
        }
        this.addCallbackParam("msg", registerUrl);
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> guids = getDataGridData().getSelectKeys();
        for (String guid : guids) {
            spInstanceService.deleteAuditSpInstanceByGuid(guid);
        }
        addCallbackParam("msg", "删除成功！");
    }

    public DataGridModel<AuditSpInstance> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditSpInstance>()
            {
                @Override
                public List<AuditSpInstance> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    //页面上搜索条件
                    if (StringUtil.isNotBlank(applyername)) {
                    	sql.like("b.applyername", applyername);
                    }
                    if (StringUtil.isNotBlank(itemname)) {
                    	sql.like("itemname", itemname);
                    }
                    sql.eq("businesstype", "3");
                    sql.eq("a.areacode", ZwfwUserSession.getInstance().getAreaCode()+"000000");
                    sql.eq("b.status", ZwfwConstant.LHSP_Status_DYS);
                    sortField = "createdate";
                    sortOrder = "desc";
                    PageData<AuditSpInstance> pageData = spInstanceService
                            .getAuditSpInstanceYSByPage(sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    List<AuditSpInstance> auditSpInstances = pageData.getList();
                    for (AuditSpInstance auditSpInstance : auditSpInstances) {
                        String biguid = auditSpInstance.getRowguid();
                        List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappByBIGuid(biguid).getResult();
                        AuditSpISubapp auditSpISubapp = auditSpISubapps.get(0);
                        auditSpInstance.put("subAppGuid", auditSpISubapp.getRowguid());                     
                    }
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }
        return model;
    }

    public AuditSpBusiness getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpBusiness();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpBusiness dataBean) {
        this.dataBean = dataBean;
    }

    public String getApplyername() {
        return applyername;
    }

    public void setApplyername(String applyername) {
        this.applyername = applyername;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }
}
