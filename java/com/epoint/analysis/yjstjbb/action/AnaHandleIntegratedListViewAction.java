package com.epoint.analysis.yjstjbb.action;

import java.util.List;

import com.epoint.basic.faces.export.ExportModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 申报查询列表页面对应的后台action
 * @author Administrator
 *
 */
@RestController("anahandleintegratedlistviewaction")
@Scope("request")
public class AnaHandleIntegratedListViewAction extends BaseController
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
    
    //统计报表页面传过来的申请方式
    private String applyerway;
    
    private String startDate;
    
    private String endDate;
    
    private String areaCode;
    
    //申报或办结
    private String type;

    private ExportModel exportModel;
    //列表字段
    //private final static String FIELDS = "a.rowguid,a.businessguid,a.itemname,a.applyername,b.acceptdate,b.FINISHDATE,TIMESTAMPDIFF(MINUTE,b.acceptdate,b.FINISHDATE) as processtime,b.status,b.acceptusername";
    private final static String FIELDS = "a.*,b.acceptdate,b.FINISHDATE,TIMESTAMPDIFF(MINUTE,b.acceptdate,b.FINISHDATE) as processtime,b.status as yjsstatus,b.acceptusername";

    /**
     * 申报名称
     */
    private String itemname;

    @Autowired
    private IAuditSpInstance spInstanceService;

    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;

    @Override
    public void pageLoad() {
        type = getRequestParameter("type");
        applyerway = getRequestParameter("applyerway");
        startDate = getRequestParameter("startDate");
        endDate = getRequestParameter("endDate");
        areaCode = getRequestParameter("areaCode");
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
        
        //若配置了主题处理地址，则一般并联审批展示个人申报详情
        if (StringUtil.isNotBlank(filepath)) {
            if ("1".equals(businessType)) {
                // 建设项目
                registerUrl = "epointzwfw/auditsp/auditsphandle/handlebilistviewdetail?guid=" + biGuid;
            }
            else if ("2".equals(businessType)) {
                // 一般并联审批
                //个人申报详情
                registerUrl = filepath  + "/auditspintegrateddetail" + "?guid=" + biGuid;
            }
        }
        //没有配置主题处理地址，默认一般并联审批展示企业申报详情
        else {
            if ("1".equals(businessType)) {
                // 建设项目
                registerUrl = "epointzwfw/auditsp/auditsphandle/handlebilistviewdetail?guid=" + biGuid;
            }
            else if ("2".equals(businessType)) {
                // 一般并联审批
                //企业申报详情
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
                /**
                 * 
                 */
                private static final long serialVersionUID = 2196016255341333981L;

                @Override
                public List<AuditSpInstance> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.setLeftJoinTable("audit_sp_i_subapp b","a.RowGuid","b.BIGUID");
                    //页面上搜索条件
                    if (StringUtil.isNotBlank(applyername)) {
                    	sql.like("a.applyername", applyername);
                    }
                    if (StringUtil.isNotBlank(itemname)) {
                    	sql.like("a.itemname", itemname);
                    }
                    //查询方式
                    if (StringUtil.isNotBlank(applyerway)) {
                        if (!"00".equals(applyerway)) {
                            sql.eq("a.APPLYERWAY", applyerway);
                        }
                    }
                    if (StringUtil.isNotBlank(startDate) && StringUtil.isNotBlank(endDate)) {
                        if ("sb".equals(type)) {
                            sql.between("b.createdate", EpointDateUtil.convertString2Date(startDate,"yyyy-MM-dd"), EpointDateUtil.convertString2Date(endDate,"yyyy-MM-dd"));
                        }
                        else if ("bj".equals(type)) {
                            sql.between("b.finishdate", EpointDateUtil.convertString2Date(startDate,"yyyy-MM-dd"), EpointDateUtil.convertString2Date(endDate,"yyyy-MM-dd"));
                        }
                    }
                    sql.eq("businesstype", "2");
                    sql.eq("areacode", areaCode);
                    if ("sb".equals(type)) {
                        sql.gt("b.status", "10");
                    }
                    else if ("bj".equals(type)) {
                        sql.eq("b.status", "40");
                    }

                    sql.setSelectFields(FIELDS);
                    sortField = "createdate";
                    sortOrder = "desc";
                    PageData<AuditSpInstance> pageData = spInstanceService
                            .getAuditSpInstanceByPage(sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }
        return model;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("itemname,applyername,createdate,acceptdate,acceptusername,finishdate,processtime,yjsstatus",
                    "一件事名称,申请主体名称,申请时间,接件时间,接件人员,办结时间,办件时长,一件事办件状态");
        }
        return exportModel;
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
