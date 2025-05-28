package com.epoint.auditsp.auditspintegrated.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditorga.auditwindow.entiy.AuditOrgaWindowYjs;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;

/**
 * 申报查询列表页面对应的后台
 * 
 * @author Administrator
 *
 */
@RestController("jnhandleintegratedlistviewaction")
@Scope("request")
public class JNHandleIntegratedListViewAction extends BaseController
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
    private IAuditOrgaWindowYjs iAuditOrgaWindowYjs;

    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;
    @Autowired
    private ICodeItemsService iCodeItemsService;

    @Override
    public void pageLoad() {

    }

    /**
     * 
     * 获取详细页面地址
     * 
     * @param biGuid
     */
    public void checkDetail(String biGuid) {
        String registerUrl = "";
        AuditSpInstance auditSpInstance = spInstanceService.getDetailByBIGuid(biGuid).getResult();
        AuditSpBusiness auditSpBusiness = iAuditSpBusiness
                .getAuditSpBusinessByRowguid(auditSpInstance.getBusinessguid()).getResult();
        String businessType = auditSpBusiness.getBusinesstype();
        String filepath = auditSpBusiness.getHandleURL();
        // 若配置了主题处理地址，则一般并联审批展示个人申报详情
        if (StringUtil.isNotBlank(filepath)) {
            if ("1".equals(businessType)) {
                // 建设项目
                registerUrl = "epointzwfw/auditsp/auditsphandle/handlebilistviewdetail?guid=" + biGuid;
            }
            else if ("2".equals(businessType)) {
                // 一般并联审批
                // 个人申报详情
                registerUrl = filepath + "/auditspintegrateddetail" + "?guid=" + biGuid;
            }
        }
        // 没有配置主题处理地址，默认一般并联审批展示企业申报详情
        else {
            if ("1".equals(businessType)) {
                // 建设项目
                registerUrl = "epointzwfw/auditsp/auditsphandle/handlebilistviewdetail?guid=" + biGuid;
            }
            else if ("2".equals(businessType)) {
                // 一般并联审批
                // 企业申报详情
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
                    // 页面上搜索条件
                    if (StringUtil.isNotBlank(applyername)) {
                        sql.like("a.applyername", applyername);
                    }
                    if (StringUtil.isNotBlank(itemname)) {
                        sql.like("itemname", itemname);
                    }
                    sql.eq("businesstype", "2");
                    sql.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
                    List<AuditOrgaWindowYjs> windows = iAuditOrgaWindowYjs
                            .getTaskByWindow(ZwfwUserSession.getInstance().getWindowGuid()).getResult();
                    StringBuilder tasks = new StringBuilder();
                    tasks.append("'");
                    for (AuditOrgaWindowYjs window : windows) {
                        tasks.append(window.getTaskguid());
                        tasks.append("','");
                    }
                    tasks.append("'");
                    sql.in("b.businessguid", tasks.toString());

                    sql.nq("b.status", "10");
                    sortField = "createdate";
                    sortOrder = "desc";
                    PageData<AuditSpInstance> pageData = spInstanceService
                            .getAuditSpInstanceYSByPage(sql.getMap(), first, pageSize, sortField, sortOrder)
                            .getResult();
                    if (pageData != null) {
                        for (AuditSpInstance auditSpInstance : pageData.getList()) {
                            List<AuditSpISubapp> list = iAuditSpISubapp.getSubappByBIGuid(auditSpInstance.getRowguid())
                                    .getResult();
                            if (!list.isEmpty()) {
                                if (!"10".equals(list.get(0).getStatus())) {
                                    auditSpInstance.put("status", iCodeItemsService.getItemTextByCodeName("一件事办件状态",
                                            list.get(0).getStatus()));
                                }
                            }
                        }
                        this.setRowCount(pageData.getRowCount());
                        return pageData.getList();
                    }
                    else {
                        return null;
                    }

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
