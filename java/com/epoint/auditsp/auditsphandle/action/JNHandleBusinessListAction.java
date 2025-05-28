package com.epoint.auditsp.auditsphandle.action;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditorga.auditwindow.entiy.AuditOrgaWindowYjs;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 主题配置表list页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2017-03-02 10:51:09]
 */
@SuppressWarnings("unchecked")
@RestController("jnhandlebusinesslistaction")
@Scope("request")
public class JNHandleBusinessListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 7487597167764665058L;


    /**
     * 主题类别，这里主要是为了区分是建设项目的还是一般并联审批的下拉列表model
     */
    private List<SelectItem> businesstypeModel = null;
    /**
     * 主题配置表实体对象
     */
    private AuditSpBusiness dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditSpBusiness> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    private String businessname;

    private String businesstype;

    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;
    
    @Autowired
    private IAuditOrgaWindowYjs iAuditOrgaWindowYjs;

    @Autowired
    private IAuditSpBusiness businseeImpl;

    @Autowired
    private IAuditOrgaArea orgaAreaService;
    private String itemguid="";
    @Override
    public void pageLoad() {
        businesstype=getRequestParameter("businesstype");
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String yewutag : select) {
            //删除主题，同时删除关联联系
             AuditCommonResult<String> delResult = businseeImpl.deleteAuditSpBusinessAndRelation(yewutag);
             if (!delResult.isSystemCode()) {
                 //系统运行异常信息
                  delResult.getSystemDescription();
             }
             else if (!delResult.isBusinessCode()) {
                 //系统业务逻辑异常信息
                 delResult.getBusinessDescription();
             }
        }
        addCallbackParam("msg", "成功删除！");
    }

    /**
     * 保存修改--主要是报存排序   
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void saveAuditSpBusiness() {
        List<AuditSpBusiness> auditSpBusinesskList = this.getDataGridData().getWrappedData();
        for (AuditSpBusiness auditSpBusiness : auditSpBusinesskList) {
            if (auditSpBusiness.getOrdernumber() == null) {// null会跑到最前
                auditSpBusiness.setOrdernumber(0);
            }
            for (AuditSpBusiness asp : auditSpBusinesskList) {
                businseeImpl.saveUpdateAuditSpBusiness(asp);
            }
        }
        addCallbackParam("msg", "保存成功");
    }

    public DataGridModel<AuditSpBusiness> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditSpBusiness>()
            {
                @Override
                public List<AuditSpBusiness> fetchData(int first, int pageSize, String sortField, String sortOrder) {

                    SqlConditionUtil sql = new SqlConditionUtil();

                    if (StringUtil.isNotBlank(businessname)) {
                    	sql.like("businessname+'&'+note", businessname);
                    }
                    if (StringUtil.isNotBlank(businesstype)) {
                    	sql.eq("businesstype", businesstype);
                    }
                    
                    if ("2".equals(businesstype)) {
                        List<AuditOrgaWindowYjs> windows = iAuditOrgaWindowYjs.getTaskByWindow(ZwfwUserSession.getInstance().getWindowGuid()).getResult();
                        
                        StringBuilder tasks = new StringBuilder();
                        tasks.append("'");
                        for (AuditOrgaWindowYjs window : windows) {
                            tasks.append(window.getTaskguid());
                            tasks.append("','");
                        }
                        tasks.append("'");
                        sql.in("rowguid", tasks.toString());   
                    }
                  
                    
                    
                    sql.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
                    sql.isBlankOrValue("del", "0");
                    sortField="ordernumber";
                    sortOrder="desc";
                    PageData<AuditSpBusiness> pageData = businseeImpl
                            .getAuditSpBusinessPageData(sql.getMap(), first, pageSize, sortField, sortOrder).getResult();

                    if (pageData != null && pageData.getList() != null) {
                        // 查询下去编码
                        for (AuditSpBusiness asp : pageData.getList()) {
                            AuditOrgaArea auditOrgaArea = orgaAreaService.getAreaByAreacode(asp.getAreacode()).getResult();
                            if (auditOrgaArea != null) {
                                asp.put("areaname", auditOrgaArea.getXiaquname());
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
    
    /**
     * 
     *  获取详细页面地址
     * 
     *  @param biGuid  
     */
    public void checkRegister(String businessGuid) {
        itemguid=UUID.randomUUID().toString();
        String registerUrl = "";
        AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid).getResult();
        String businessType = auditSpBusiness.getBusinesstype();
        String filepath = auditSpBusiness.getHandleURL();
        if(ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpBusiness.getDel())){
            addCallbackParam("del", "主题已被禁用！");
        }else{
            if (StringUtil.isNotBlank(filepath)) {
                if ("1".equals(businessType)) {
                    // 建设项目
                    registerUrl = filepath + "?businessGuid=" + businessGuid+"&itemguid="+itemguid;
                }
                else if ("2".equals(businessType)) {
                	 registerUrl = filepath + "/auditspintegratedadd?businessGuid="+ businessGuid;
                }
            }else {
                if ("1".equals(businessType)) {
                    // 建设项目
                    registerUrl = "epointzwfw/auditsp/auditsphandle/handleitemregister?businessGuid=" + businessGuid+"&itemguid="+itemguid;
                }
                else if ("2".equals(businessType)) {
                    // 一般并联审批
                    registerUrl = "epointzwfw/auditsp/auditspintegrated/auditspintegratedadd?businessGuid=" + businessGuid+"&itemguid="+itemguid;
                }
            }
            this.addCallbackParam("businessGuid", businessGuid);
            this.addCallbackParam("msg", registerUrl);
        }
       
    }

    public List<SelectItem> getBusinesstypeModel() {
        if (businesstypeModel == null) {
            businesstypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "auditsp_ztlb", null, false));
        }
        return this.businesstypeModel;
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

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    public String getBusinessname() {
        return businessname;
    }

    public void setBusinessname(String businessname) {
        this.businessname = businessname;
    }

    public String getBusinesstype() {
        return businesstype;
    }

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype;
    }

}
