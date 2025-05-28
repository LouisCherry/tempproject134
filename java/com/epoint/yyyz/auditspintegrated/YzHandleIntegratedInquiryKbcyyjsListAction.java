package com.epoint.yyyz.auditspintegrated;

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
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 申报查询列表页面对应的后台（开办餐饮一件事）
 * @author Administrator
 *
 */
@RestController("yzhandleintegratedinquirykbcyyjslistaction")
@Scope("request")
public class YzHandleIntegratedInquiryKbcyyjsListAction extends BaseController
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

    @Autowired
    private IConfigService configServicce;

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

                    String yjsguid = getRequestParameter("yjsguid");

                    //可通过urlparam实现动态查看各模块，否则展示 开办餐饮一件事筛选
                    if(StringUtils.isBlank(yjsguid)){
                        yjsguid = "kbcyyjsguids";
                    }
                    String kbcyyjsguids = configServicce.getFrameConfigValue(yjsguid);
                    if(StringUtils.isNotBlank(kbcyyjsguids)){
                        String[] ar = kbcyyjsguids.split(",");
                        sql.in("a.businessguid", "'" + StringUtil.join(ar, "','") + "'");
                    }


                    // 页面上搜索条件
                    if (StringUtil.isNotBlank(applyername)) {
                    	sql.like("a.applyername", applyername);
                    }
                    if (StringUtil.isNotBlank(itemname)) {
                        sql.like("itemname", itemname);
                    }
                    sql.eq("businesstype", "3");
                    //市里可以看所有的
                    if(!"370800".equals(ZwfwUserSession.getInstance().getAreaCode())){
                        sql.eq("areacode", ZwfwUserSession.getInstance().getAreaCode()+"000000");
                    }

                    sql.eq("b.status", ZwfwConstant.LHSP_Status_DYS);
                    sortField = "createdate";
                    sortOrder = "desc";
                    PageData<AuditSpInstance> pageData = spInstanceService
                            .getAuditSpInstanceYSByPage(sql.getMap(), first, pageSize, sortField, sortOrder)
                            .getResult();
                    if(pageData!=null && pageData.getList().size()>0){
                        List<AuditSpInstance> auditSpInstances = pageData.getList();
                        for (AuditSpInstance auditSpInstance : auditSpInstances) {
                            String biguid = auditSpInstance.getRowguid();
                            List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappByBIGuid(biguid).getResult();
                            AuditSpISubapp auditSpISubapp = auditSpISubapps.get(0);
                            auditSpInstance.put("subAppGuid", auditSpISubapp.getRowguid());
                        }
                        this.setRowCount(pageData.getRowCount());
                        return pageData.getList();
                    }else{
                        this.setRowCount(0);
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
