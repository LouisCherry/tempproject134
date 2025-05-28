package com.epoint.auditorga.auditonlineconsult.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditonlineuser.auditonlineconsult.domain.AuditDaibanConsult;
import com.epoint.basic.auditonlineuser.auditonlineconsult.inter.IAuditDaibanConsult;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.organ.ou.api.IOuService;

/**
 * 咨询投诉list页面对应的后台
 * 
 * @author yangjl
 * @version [版本号, 2017-04-12 10:13:31]
 */
@RestController("jnauditonlinedaibanlistaction")
@Scope("request")
public class JNAuditOnlineDaibanListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 8713148757403132395L;
    @Autowired
    private IAuditDaibanConsult onlineConsult;
    @Autowired
    private IAuditTask auditTask;
    @Autowired
    private IAuditSpBusiness auditspbusiness;
    @Autowired
    private IOuService ouService;
    @Autowired
    private IAttachService iAttachService;
    /**
     * 咨询投诉实体对象
     */
    private AuditDaibanConsult dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditDaibanConsult> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    private String startDate;

    private String endDate;

    private String consulttype;
    
    private String isou; 

    /**
     * 已答复、未答复类型  0、2(未答复，追问未答复)  1、3(已答复，追问已答复)
     */
    private String status;

    private String center;

    private String department;

    @Override
    public void pageLoad() {
        addCallbackParam("messageItemGuid", getRequestParameter("messageItemGuid"));
        status = getRequestParameter("status");
        consulttype = getRequestParameter("consulttype");
        department = getRequestParameter("department");
        center = getRequestParameter("center");
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            // 删除的时候要清空和部门扩展表的关系
        	AuditDaibanConsult consult = onlineConsult.getConsultByRowguid(sel).getResult();
            onlineConsult.deleteConsult(consult);
        }
        String msg = "成功删除！";
        addCallbackParam("msg", msg);
      
    }

    public DataGridModel<AuditDaibanConsult> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditDaibanConsult>()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = -3072629597132333294L;

                @Override
                public List<AuditDaibanConsult> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    //页面上搜索条件 咨询人、咨询内容、咨询时间
                    if (dataBean != null && StringUtil.isNotBlank(dataBean.getAskerusername())) {
                        sql.like("askerusername", dataBean.getAskerusername());
                        sql.eq("IsAnonymous", "0");
                    }
                    else if (dataBean != null && StringUtil.isNotBlank(dataBean.getIsAnonymous())) {
                        if ("true".equals(dataBean.getIsAnonymous())) {
                            sql.eq("IsAnonymous", "1");
                        }
                    }
                    if (dataBean != null && StringUtil.isNotBlank(dataBean.getQuestion())) {
                        sql.like("question", dataBean.getQuestion());
                    }
                    if (StringUtil.isNotBlank(department)) {
                    	String areacode1 ="";
                        if (ZwfwUserSession.getInstance().getCitylevel()!=null&&Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ)){
                            areacode1 = ZwfwUserSession.getInstance().getBaseAreaCode();
                		}else{
                		    areacode1 = ZwfwUserSession.getInstance().getAreaCode();
                		}
                        sql.eq("areacode", areacode1);
                    }
                    if (dataBean != null && StringUtil.isNotBlank(startDate)) {
                        sql.ge("askdate", EpointDateUtil.convertString2Date(startDate));
                    }
                    if (dataBean != null && StringUtil.isNotBlank(endDate)) {
                        sql.le("askdate", EpointDateUtil.getEndOfDateStr(endDate));
                    }
                    if (StringUtil.isNotBlank(status) && "0".equals(status)) {
                        sql.in("status", ZwfwConstant.ZIXUN_TYPE_DDF + "," + ZwfwConstant.ZIXUN_TYPE_ZWDDF);
                    }
                    else if (StringUtil.isNotBlank(status) && "1".equals(status)) {
                        sql.in("status", ZwfwConstant.ZIXUN_TYPE_YDF + "," + ZwfwConstant.ZIXUN_TYPE_ZWYDF);
                    }
                    //设置类型  1为咨询  2为投诉
                    sql.eq("consulttype", consulttype);
                    sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
                    //部门
                    if (StringUtil.isNotBlank(department)) {
                        sql.eq("ouguid", userSession.getOuGuid());
                        /*AuditOrgaServiceCenter result = serviceCenter
                                .findAuditServiceCenterByGuid(ZwfwUserSession.getInstance().getCenterGuid())
                                .getResult();
                        if (result == null) {
                            sql.eq("ouguid", userSession.getOuGuid());
                        }
                        else {
                            sql.eq("ouguid", result.getOuguid());
                        }*/

                    }
                    PageData<AuditDaibanConsult> pageData = getPageData(sql.getMap(), first, pageSize,
                            sortField, sortOrder);
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }
        return model;
    }

    public AuditDaibanConsult getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditDaibanConsult();
        }
        return dataBean;
    }

    public void setDataBean(AuditDaibanConsult dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getConsulttype() {
        return consulttype;
    }

    public void setConsulttype(String consulttype) {
        this.consulttype = consulttype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    
    
    /**
     * 查consult表分页数据
     * @param conditionMap
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    public PageData<AuditDaibanConsult> getPageData(Map<String, String> conditionMap, Integer first, Integer pageSize,
            String sortField, String sortOrder) {
        PageData<AuditDaibanConsult> pageData = new PageData<AuditDaibanConsult>();
        List<AuditDaibanConsult> consultList = onlineConsult
                .selectConsultByPage(conditionMap, first, pageSize, sortField, sortOrder).getResult();
        for (AuditDaibanConsult consult : consultList) {
            if (StringUtil.isNotBlank(consult.getTaskguid())) {
                AuditTask result = auditTask.getAuditTaskByGuid(consult.getTaskguid(), false).getResult();
                if (result == null) {
                    consult.put("taskname", "无关联事项");
                }
                else {
                    consult.put("taskname", result.getTaskname());
                }
            }else{
                AuditSpBusiness result = auditspbusiness.getAuditSpBusinessByRowguid(consult.getBusinessguid()).getResult();
                if (result == null) {
                    consult.put("taskname", "无关联事项");
                }
                else {
                    consult.put("taskname", result.getBusinessname());
                }
            }
            if ("1".equals(consult.getIsAnonymous())) {
                consult.setAskerusername("匿名用户");
            }
            if (StringUtil.isNotBlank(consult.getOuguid())) {
                consult.put("ouname", ouService.getOuByOuGuid(consult.getOuguid()).getOuname());
            }
            else {
                consult.put("ouname", "中心");
            }
            consult.setClientguid(getTempUrl(consult.getClientguid()));
        }
        Integer count = onlineConsult.getConsultCount(conditionMap).getResult();
        pageData.setList(consultList);
        pageData.setRowCount(count);
        return pageData;
    }
    
    /**
     * 获取附件，如果有附件将附件信息显示在页面列表上
     * @param cliengguid
     * @return
     */
    public String getTempUrl(String cliengguid) {
        String wsmbName = "";
        if (StringUtil.isNotBlank(cliengguid) && cliengguid != null) {
            List<FrameAttachInfo> listFrameAttachInfo = iAttachService.getAttachInfoListByGuid(cliengguid);
            // 有附件
            if (listFrameAttachInfo.size() > 0) {
                for (FrameAttachInfo frameAttachInfo : listFrameAttachInfo) {
                    String strURL = "onclick=\"goToAttach('" + frameAttachInfo.getAttachGuid() + "')\"";
                    wsmbName += "<a style=\"color:blue;text-decoration:underline\" href=\"javascript:void(0)\" "
                            + strURL + ">" + frameAttachInfo.getAttachFileName() + "</a>&nbsp;&nbsp;</br>";
                }
            }
            else {
                wsmbName = "无附件！";
            }
        }
        else {
            wsmbName = "无附件！";
        }
        return wsmbName;
    }
    
    public String getIsou() {
        return isou;
    }

    public void setIsou(String isou) {
        this.isou = isou;
    }


}
