package com.epoint.jiningzwfw.projectstatistics.projectdaysperou.action;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.jiningzwfw.projectstatistics.projectdaysperou.api.IAuditOuProjectDaysService;
import com.epoint.jiningzwfw.projectstatistics.projectdaysperou.api.entity.AuditOuProjectDays;
import com.epoint.jiningzwfw.projectstatistics.projecttaskr.api.IProjectTaskRService;
import com.epoint.jiningzwfw.projectstatistics.tasktemp.api.IAuditTaskOnTempService;
import com.epoint.jiningzwfw.projectstatistics.tasktemp.api.entity.AuditTaskOnTemp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;


/**
 * 每日部门办件统计分析表list页面对应的后台
 *
 * @author yangyi
 * @version [版本号, 2021-06-30 17:37:23]
 */
@RestController("noprojectslistaction")
@Scope("request")
public class NoProjectsListAction extends BaseController {
    @Autowired
    private IAuditOuProjectDaysService service;
    @Autowired
    private IConfigService iConfigService;
    @Autowired
    private IAuditOrgaArea iAuditOrgaArea;
    @Autowired
    private IAuditTaskOnTempService iAuditTaskOnTempService;
    @Autowired
    private IProjectTaskRService iProjectTaskRService;

    /**
     * 每日部门办件统计分析表实体对象
     */
    private AuditOuProjectDays dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditTaskOnTemp> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * 结束时间
     */
    private String endDate;

    /**
     * 开始时间
     */
    private String startDate;


    /**
     * 查询部门
     */
    private String ouguids;
    private String date;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00%");


    public void pageLoad() {
    }



    public DataGridModel<AuditTaskOnTemp> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditTaskOnTemp>() {

                @Override
                public List<AuditTaskOnTemp> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    //1.判断登录人员登录账号是否包含在系统参数中
                    String account = UserSession.getInstance().getLoginID();
                    String logins = iConfigService.getFrameConfigValue("Y_APPLAYALL_USERS_LOGIN");
                    String areaCode = "";
                    if (!logins.contains(account)) {
                        sqlConditionUtil.eq("OuGuid", UserSession.getInstance().getOuGuid());
                        sqlConditionUtil.setSelectFields(" BaseAreaCode ");
                        AuditOrgaArea auditOrgaArea = iAuditOrgaArea.getAuditArea(sqlConditionUtil.getMap()).getResult();
                        if (ValidateUtil.isNotNull(auditOrgaArea)) {
                            areaCode = auditOrgaArea.getBaseAreaCode();
                        }
                    }
                    String conditionSql = "select * from audit_task_on_temp b where b.businesstype ='1' ";
                    //拼接参数
                    if (StringUtil.isNotBlank(startDate)){
                        conditionSql +=" and b.TASK_ID not in (select TASK_ID from project_task_r a where (SDate between '"+startDate+"' and '"+endDate+"'))  ";
                    }
                    else{
                        //查询近一月
                        Date now = new Date();

                        Date lastThirty = EpointDateUtil.addDay(now, -30);
                        date = EpointDateUtil.convertDate2String(lastThirty, "yyyy-MM-dd hh:mm:ss");
                        conditionSql +=" and b.TASK_ID not in (select TASK_ID from project_task_r a where (SDate between '"+EpointDateUtil.convertDate2String(now,"yyyy-MM-dd hh:mm:ss")+"' and '"+date+"'))  ";
                    }
                    if (StringUtil.isNotBlank(areaCode)){
                        conditionSql += " b.AreaCode='" + areaCode +"' ";
                    }
                    if (StringUtil.isNotBlank(ouguids)){
                        if (!ouguids.contains("'")) {
                            StringBuilder stringBuilder = new StringBuilder();
                            String[] ous = ouguids.split(";");
                            for (String s : ous) {
                                if (com.epoint.core.utils.string.StringUtil.isBlank(stringBuilder.toString())) {
                                    stringBuilder.append("'").append(s).append("'");
                                } else {
                                    stringBuilder.append(",").append("'").append(s).append("'");
                                }
                            }
                            conditionSql += " and b.ouguid in(" + stringBuilder.toString() + ") ";
                        }
                        else{
                            conditionSql += " and b.ouguid in(" + ouguids + ") ";
                        }
                    }
                    List<AuditTaskOnTemp> results = iAuditTaskOnTempService.findList(conditionSql,first,pageSize,null);
                    String countSql = conditionSql.replace("*"," count(*) as totalCount ");

                    this.setRowCount(iAuditTaskOnTempService.find(countSql,null).getInt("totalCount"));
                    return results;
                }

            };
        }
        return model;
    }


    public AuditOuProjectDays getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditOuProjectDays();
        }
        return dataBean;
    }

    public void setDataBean(AuditOuProjectDays dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("item_id,taskname,ouname",
                    "事项编码,事项名称,事项所属部门");
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

    public String getOuguids() {
        return ouguids;
    }

    public void setOuguids(String ouguids) {
        this.ouguids = ouguids;
    }

}
