package com.epoint.jiningzwfw.projectstatistics.projectdaysperou.action;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.jiningzwfw.projectstatistics.projectdaysperou.api.IAuditOuProjectDaysService;
import com.epoint.jiningzwfw.projectstatistics.projectdaysperou.api.entity.AuditOuProjectDays;
import com.epoint.jiningzwfw.projectstatistics.projecttaskr.api.IProjectTaskRService;
import com.epoint.jiningzwfw.projectstatistics.projecttaskr.api.entity.ProjectTaskR;
import com.epoint.jiningzwfw.projectstatistics.tasktemp.api.IAuditTaskOnTempService;
import com.epoint.jiningzwfw.projectstatistics.tasktemp.api.entity.AuditTaskOnTemp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.*;


/**
 * 每日部门办件统计分析表list页面对应的后台
 *
 * @author yangyi
 * @version [版本号, 2021-06-30 17:37:23]
 */
@RestController("auditouprojectdayslistaction")
@Scope("request")
public class AuditOuProjectDaysListAction extends BaseController {
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
    private DataGridModel<AuditOuProjectDays> model;

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



    public DataGridModel<AuditOuProjectDays> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditOuProjectDays>() {

                @Override
                public List<AuditOuProjectDays> fetchData(int first, int pageSize, String sortField, String sortOrder) {
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
                    String sql = "select * from AUDIT_OU_PROJECT_DAYS where 1=1 ";
                    //1.判断areaCode是否为空 为空 则查询所有 不为空 则查询 指定辖区
                    if (StringUtil.isNotBlank(areaCode)) {
                       sql += " and areacode='" + areaCode + "' ";
                    }
                    //2。判断传入时间是否为空
                    if (StringUtil.isNotBlank(startDate)) {
                        sql +=" and (SDate between  '" + startDate + "' and '" + endDate +"') ";
                    } 
                    else {
                        //查询近一月
                        Date now = new Date();
                        Date lastThirty = EpointDateUtil.addDay(now, -30);
                        date = EpointDateUtil.convertDate2String(lastThirty, "yyyy-MM-dd hh:mm:ss");
                        sql +=" and (SDate between  '" + date + "' and '" + EpointDateUtil.convertDate2String(now,"yyyy-MM-dd hh:mm:ss") +"') ";
                    }
                    //3.判断传入部门是否为空
                    if (StringUtil.isNotBlank(ouguids)) {
                        if (!ouguids.contains("'")) {
                            StringBuilder stringBuilder = new StringBuilder();
                            String[] ous = ouguids.split(";");
                            for (String s : ous) {
                                if (StringUtil.isBlank(stringBuilder.toString())) {
                                    stringBuilder.append("'").append(s).append("'");
                                } else {
                                    stringBuilder.append(",").append("'").append(s).append("'");
                                }
                            }
                            sql += " and ouguid in(" + stringBuilder.toString() + ") ";
                        }
                        else {
                            sql += " and ouguid in(" + ouguids + ") ";
                        }
                    }
                    sql += " order by TOCNum desc";
                    //获取每个部门的依事项数
                    String countTaskSql = "select count(*) as totalCount , ouguid from audit_task_on_temp a where businesstype ='1' ";
                    if (StringUtil .isNotBlank(areaCode)){
                        countTaskSql += " and areacode='"+areaCode+"' ";
                    }
                    countTaskSql += " group by ouguid";
                    List<AuditTaskOnTemp> tasks = iAuditTaskOnTempService.findList(countTaskSql,null);
                    HashMap<String,Integer> map = new HashMap<>();
                    for (AuditTaskOnTemp task : tasks) {
                        map.put(task.getOuguid(),task.getInt("totalCount"));
                    }

                    String countProjectsSql= "select count(*) as totalCount ,OUGUID from audit_task_on_temp a where a.businesstype ='1' ";
                    if (StringUtil.isNotBlank(areaCode)){
                        countProjectsSql += " and a.areacode='" +areaCode + "' ";
                    }
                    if (StringUtil.isNotBlank(ouguids)){
                        if (!ouguids.contains("'")) {
                                StringBuilder stringBuilder = new StringBuilder();
                                String[] ous = ouguids.split(";");
                                for (String s : ous) {
                                    if (StringUtil.isBlank(stringBuilder.toString())) {
                                        stringBuilder.append("'").append(s).append("'");
                                    } else {
                                        stringBuilder.append(",").append("'").append(s).append("'");
                                    }
                                }
                                countProjectsSql += " and a.ouguid in(" + stringBuilder.toString() + ") ";
                        }
                        else {
                            countProjectsSql += " and a.ouguid in(" + ouguids+ ") ";
                        }
                    }
                    if (com.epoint.common.util.StringUtil.isNotBlank(startDate)){
                        countProjectsSql +=" and a.TASK_ID  in (select TASK_ID from project_task_r ab where (SDate between '"+startDate+"' and '"+endDate+"'))  ";
                    }
                    else{
                        //查询近一月
                        Date now = new Date();

                        Date lastThirty = EpointDateUtil.addDay(now, -30);
                        date = EpointDateUtil.convertDate2String(lastThirty, "yyyy-MM-dd hh:mm:ss");
                        countProjectsSql +=" and a.TASK_ID  in (select TASK_ID from project_task_r b where (SDate between '"+date+"' and '"+EpointDateUtil.convertDate2String(now,"yyyy-MM-dd hh:mm:ss")+"'))  ";
                    }
                    countProjectsSql += " group by OUGUID ";
                    List<ProjectTaskR > projectTaskRS = iProjectTaskRService.findList(countProjectsSql,null);
                    HashMap<String,Integer> map1 = new HashMap<>();
                    for (ProjectTaskR projectTaskR : projectTaskRS) {
                        map1.put(projectTaskR.getOuguid(),projectTaskR.getInt("totalCount"));
                    }

                    List<AuditOuProjectDays> list = service.findList(sql,first,pageSize,null);
                    if (ValidateUtil.isNotBlankCollection(list)){
                        for (AuditOuProjectDays auditOuProjectDays : list) {
                            Integer TOCNum = auditOuProjectDays.getTocnum();//办件办结量
                            Integer ExtTOCNum = auditOuProjectDays.getExttocnum();//外网申报数
                            Integer OnNum = auditOuProjectDays.getOnnum();//按期办理量
                            Integer ExtConNum = auditOuProjectDays.getExtconnum();//网上咨询量
                            Integer ExtConReplyNum = auditOuProjectDays.getExtconreplynum();//网上按期答复量
                            String exttocnumpercent = "0.00%";
                            String onnumpercent = "0.00%";
                            String evanumpercent = "0.00%";
                            if (TOCNum != 0) {
                                //1.外网申报率 exttocnumpercent 外网申报数/办件办结量
                                exttocnumpercent = decimalFormat.format((float) ExtTOCNum / TOCNum);
                                //2.办件按期办理率 onnumpercent按期办理数/办件办结量
                                onnumpercent = decimalFormat.format((float) OnNum / TOCNum);
                            }
                            if (ExtConNum != 0) {
                                //3.网上咨询按期答复率 evanumpercent 网上按期答复量/网上咨询量
                                evanumpercent = decimalFormat.format((float) ExtConReplyNum / ExtConNum);
                            }
                            auditOuProjectDays.put("exttocnumpercent", exttocnumpercent);
                            auditOuProjectDays.put("onnumpercent", onnumpercent);
                            auditOuProjectDays.put("evanumpercent", evanumpercent);
                            auditOuProjectDays.put("totalCount", map.get(auditOuProjectDays.getOuguid())==null?0:map.get(auditOuProjectDays.getOuguid()));
                            auditOuProjectDays.put("totalCountOfProject", map1.get(auditOuProjectDays.getOuguid())==null?0:map1.get(auditOuProjectDays.getOuguid()));

                        }
                    }
                    String countSql = sql.replace("*","count(*) as totalCount ");

                    this.setRowCount(service.find(countSql,null).getInt("totalCount"));
                    return list;
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
            exportModel = new ExportModel("ouname,totalCount,totalCountOfProject,tocnum,exttocnum,exttocnumpercent" +
                    "evanum,evanumpercent,extnotacceptednum,onnum,onnumpercent",
                    "部门名称,依申请事项总数,有办件的依申请事项,办件办结量,外网申报数,外网申报率,评价量,网上咨询按期答复率,外网申报未受理量" +
                            ",按期办理量,办件按期办理率");
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
