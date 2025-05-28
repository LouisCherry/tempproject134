package com.epoint.auditproject.zjxt.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditproject.auditproject.api.IJnProjectService;
import com.epoint.auditproject.zjxt.entity.AuditProjectZjxt;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditproject.auditprojectnumber.inter.IAuditProjectNumber;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;

/**
 * 业务管理list页面对应的后台
 *
 * @author WeiY
 * @version [版本号, 2016-09-26 11:13:04]
 */
@RestController("zjxtprojectlistaction")
@Scope("request")
public class zjxtprojectListAction extends BaseController
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 办件基本信息实体对象
     */
    private AuditProjectZjxt dataBean = new AuditProjectZjxt();

    /**
     * 表格控件model
     */
    private DataGridModel<AuditProjectZjxt> model;
    /**
     * 导出模型
     */
    private ExportModel exportModel;
    /**
     * 申请时间
     */
    private String applyDateStart;
    private String applyDateEnd;
    /**
     * 受理时间
     */
    private String acceptuserdateStart;
    private String acceptuserdateEnd;
    /**
     * 办结时间
     */
    private String banjiedateStart;
    private String banjiedateEnd;
    /**
     * 承诺办结时间
     */
    private String promiseEndDateStart;
    private String promiseEndDateEnd;


    /**
     * 中心唯一标识
     */
    private String centerguid;
    /**
     * 辖区
     */
    private String areaCode;
    /**
     * 办件状态
     */
    private String status;




    @Autowired
    private IJnProjectService auditProjectService;

    @Autowired
    private IAuditProjectSparetime projectSparetimeService;

    @Autowired
    private IAuditOrgaWindowYjs orgaWindowService;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;

    @Autowired
    private IMessagesCenterService messagesCenterService;

    @Autowired
    IWFInstanceAPI9 iwf9;

    @Autowired
    IAuditProjectNumber auditProjectNumber;

    @Autowired
    IAuditOrgaWorkingDay auditOrgaWorkingDay;

    //日志
    transient Logger log = LogUtil.getLog(zjxtprojectListAction.class);

    @Override
    public void pageLoad() {

    }

    public DataGridModel<AuditProjectZjxt> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProjectZjxt>()
            {

                @SuppressWarnings({"unchecked", "rawtypes" })
                @Override
                public List<AuditProjectZjxt> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    ArrayList list = new ArrayList();
//                    if (StringUtil.isNotBlank(areaCode)) {
                        SqlConditionUtil sql = new SqlConditionUtil();
                        // 在查询办件表的时候，增加了分表需要传入辖区参数
//                        switch ("") {
//                            case ZwfwConstant.JK_Type_Dept:
//                                sql.eq("areaCode", areaCode);
//                                break;
//                            case ZwfwConstant.JK_Type_Center:
//                                sql.eq("centerguid", centerguid);
//                                sql.eq("areaCode", areaCode);
//                                break;
//                            case ZwfwConstant.JK_Type_All:
//                                sql.eq("areaCode", areaCode);
//                                break;
//                            default:
//                                sql.eq("areaCode", areaCode);
//                                break;
//                        }
//                        // 1、申请人
//                        if (StringUtil.isNotBlank(dataBean.getApplyername())) {
//                            //过滤%，以免输入%查出全部数据
//                            if("%".equals(dataBean.getApplyername()) || "_".equals(dataBean.getApplyername())){
//                                String applyName = "/" + dataBean.getApplyername();
//                                sql.like("applyername", applyName);
//                            }else{
//                                sql.like("applyername", dataBean.getApplyername().trim());
//                            }
//                        }
//                        // 2、办件名称
                        if (StringUtil.isNotBlank(dataBean.getStr("projectname"))) {
                          //过滤%，以免输入%查出全部数据
                        	sql.like("projectname", dataBean.getStr("projectname"));
                        }
                        if (StringUtil.isNotBlank(dataBean.getStr("datasource"))) {
                            //过滤%，以免输入%查出全部数据
                          	sql.like("datasource", dataBean.getStr("datasource"));
                          }

                    if (StringUtil.isNotBlank(dataBean.getStr("ouname"))) {
                        //过滤%，以免输入%查出全部数据
                        sql.like("ouname", dataBean.getStr("ouname"));
                    }
                    if (StringUtil.isNotBlank(dataBean.getStr("operatedate_s"))) {
                        sql.ge("operatedate", EpointDateUtil.getBeginOfDateStr(dataBean.getStr("operatedate_s")));
                    }
                    if (StringUtil.isNotBlank(dataBean.getStr("operatedate_e"))) {
                        sql.lt("operatedate", EpointDateUtil.getEndOfDateStr(dataBean.getStr("operatedate_e")));
                    }
//                        // 3、办件编号
//                        if (StringUtil.isNotBlank(dataBean.getFlowsn())) {
//                          //过滤%，以免输入%查出全部数据
//                            if("%".equals(dataBean.getFlowsn()) || "_".equals(dataBean.getFlowsn())){
//                                String flowsn = "/" + dataBean.getFlowsn();
//                                sql.like("flowsn", flowsn);
//                            }else{
//                                sql.like("flowsn", dataBean.getFlowsn().trim());
//                            }
//                        }
                        // 4、受理时间
                        if (StringUtil.isNotBlank(acceptuserdateStart)) {
                            sql.ge("acceptuserdate", EpointDateUtil.getBeginOfDateStr(acceptuserdateStart));
                        }
                        if (StringUtil.isNotBlank(acceptuserdateEnd)) {
                            sql.lt("acceptuserdate", EpointDateUtil.getEndOfDateStr(acceptuserdateEnd));
                        }
                        // 5、办结时间
                        if (StringUtil.isNotBlank(banjiedateStart)) {
                            sql.ge("banjiedate", EpointDateUtil.getBeginOfDateStr(banjiedateStart));
                        }
                        if (StringUtil.isNotBlank(banjiedateEnd)) {
                            sql.lt("banjiedate", EpointDateUtil.getEndOfDateStr(banjiedateEnd));
                        }

                        //10、承诺办结时间
                        if (StringUtil.isNotBlank(promiseEndDateStart)) {
                            sql.ge("PROMISEENDDATE", EpointDateUtil.getBeginOfDateStr(promiseEndDateStart));
                        }
                        if (StringUtil.isNotBlank(promiseEndDateEnd)) {
                            sql.lt("PROMISEENDDATE", EpointDateUtil.getEndOfDateStr(promiseEndDateEnd));
                        }
                        // 11、申请时间
                        if (StringUtil.isNotBlank(applyDateStart)) {
                            sql.ge("applydate", EpointDateUtil.getBeginOfDateStr(applyDateStart));
                        }
                        if (StringUtil.isNotBlank(applyDateEnd)) {
                            sql.lt("applydate", EpointDateUtil.getEndOfDateStr(applyDateEnd));
                        }
                        sql.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
                        sql.setOrder(sortField, sortOrder);

                        sql.setSelectFields( "*");
                        PageData<AuditProjectZjxt> pageData = auditProjectService.getAuditProjectZjxtPageData(
                                sql.getMap(), first, pageSize, "", "").getResult();
                        this.setRowCount(pageData.getRowCount());
                        return pageData.getList();
//                    }
//                    else {
//                        this.setRowCount(0);
//                        return list;
//
//                    }
                }
            };
        }
        return model;
    }



//    public List<SelectItem> getWindowNameModel() {
//        if (windownameModel == null) {
//            windownameModel = new ArrayList<SelectItem>();
//            SqlConditionUtil sql = new SqlConditionUtil();
//            sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
//            List<AuditOrgaWindow> list = orgaWindowService.getAllWindow(sql.getMap()).getResult();
//            for (AuditOrgaWindow auditWindow : list) {
//                windownameModel.add(new SelectItem(auditWindow.getRowguid(), auditWindow.getWindowname()));
//            }
//            windownameModel.add(0, new SelectItem("all", "所有窗口"));
//        }
//        return this.windownameModel;
//    }



    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("windowname,ouname,projectname,flowsn,applyername,applydate,ACCEPTUSERDATE,sparetime,banjiedate,status",
                    "窗口名称,办件所属部门,事项名称,办件编号,申请人,申请时间,受理时间,办结剩余时间,办结时间,办件状态");
        }
        return exportModel;
    }





    public String getCenterguid() {
        return centerguid;
    }

    public void setCenterguid(String centerguid) {
        this.centerguid = centerguid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAcceptuserdateStart() {
        return acceptuserdateStart;
    }

    public void setAcceptuserdateStart(String acceptuserdateStart) {
        this.acceptuserdateStart = acceptuserdateStart;
    }

    public String getAcceptuserdateEnd() {
        return acceptuserdateEnd;
    }

    public void setAcceptuserdateEnd(String acceptuserdateEnd) {
        this.acceptuserdateEnd = acceptuserdateEnd;
    }

    public String getBanjiedateStart() {
        return banjiedateStart;
    }

    public void setBanjiedateStart(String banjiedateStart) {
        this.banjiedateStart = banjiedateStart;
    }

    public String getBanjiedateEnd() {
        return banjiedateEnd;
    }

    public void setBanjiedateEnd(String banjiedateEnd) {
        this.banjiedateEnd = banjiedateEnd;
    }



    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }


    public String getPromiseEndDateStart() {
        return promiseEndDateStart;
    }

    public void setPromiseEndDateStart(String promiseEndDateStart) {
        this.promiseEndDateStart = promiseEndDateStart;
    }

    public String getPromiseEndDateEnd() {
        return promiseEndDateEnd;
    }

    public void setPromiseEndDateEnd(String promiseEndDateEnd) {
        this.promiseEndDateEnd = promiseEndDateEnd;
    }

    public String getApplyDateStart() {
        return applyDateStart;
    }

    public void setApplyDateStart(String applyDateStart) {
        this.applyDateStart = applyDateStart;
    }

    public String getApplyDateEnd() {
        return applyDateEnd;
    }

    public void setApplyDateEnd(String applyDateEnd) {
        this.applyDateEnd = applyDateEnd;
    }

	public AuditProjectZjxt getDataBean() {
		return dataBean;
	}

	public void setDataBean(AuditProjectZjxt dataBean) {
		this.dataBean = dataBean;
	}




}
