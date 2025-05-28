package com.epoint.jiningzwfw.baobiao.action;

import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;

import com.epoint.basic.bizlogic.mis.CommonService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;

import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;

import com.epoint.jiningzwfw.baobiao.api.ITaskBanjianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;


import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController("taskbanjianlistaction")
@Scope("request")
public class TaskBanjianListAction extends BaseController
{
    private static final long serialVersionUID = 1L;
    private DataGridModel<Record> modelall;

    private ExportModel exportModel;

    private String startdate;
    private String enddate;

    private String applyername ;
    private String taskname   ;
    private String certnum    ;
    private String status     ;
    private String contactmobile      ;
    private String iscunzhen      ;
    private String flowsn;
    private CommonService commservice = new CommonService();

    private TreeModel treeModel = null;


    @Autowired
    private ITaskBanjianService service;

    private String areacode = "";



    /**
     * Evalevel下拉列表model
     */
    private List<SelectItem> evalevelModel = null;
    private List<SelectItem> statusModel = null;
    @Override
    public void pageLoad() {

        startdate= EpointDateUtil.convertDate2String(EpointDateUtil.addMonth(new Date(),-1),"yyyy-MM-dd HH:mm:ss");

        enddate=EpointDateUtil.convertDate2String(new Date(),"yyyy-MM-dd HH:mm:ss");
        iscunzhen="0";
        areacode="370800";
    }

    public DataGridModel<Record> getDataGridDataTask() {
        // 获得表格对象
        if (modelall == null) {
            modelall = new DataGridModel<Record>()
            {
                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {


                    String strsql =  " select\n" +
                            "    a.rowguid,a.APPLYDATE ,a.pviguid,\n" +
                            "    a.APPLYERNAME ,\n" +
                            "    case\n" +
                            "        when (a.CONTACTMOBILE is null) then a.CONTACTPHONE\n" +
                            "        else a.CONTACTMOBILE\n" +
                            "    end as mobile ,\n" +
                            "     a.CONTACTPHONE,\n" +
                            "     CONTACTMOBILE,\n" +
                            "    a.CERTNUM ,\n" +
                            "    b.taskname ,\n" +
                            "    case\n" +
                            "        when (a.ouname is null) then b.OUNAME \n" +
                            "        else a.ouname\n" +
                            "    end as ouname ,\n" +
                            "    a.flowsn ,\n" +
                            "    a.status ,\n" +
                            "    a.AREACODE \n" +
                            "from\n" +
                            "    audit_project a\n" +
                            "left join \n" +
                            "    audit_task b \n" +
                            "    on\n" +
                            "    a.TASKGUID = b.RowGuid\n" +
                            "where\n" +
                            "    1 = 1\n" +
                            "    and a.STATUS >= 12 and a.STATUS != 20\n" +
                            "    and (a.is_lczj != '2' or a.is_lczj is null) " ;

                    String countsql =  " select count(*) \n" +
                            "from\n" +
                            "    audit_project a\n" +
                            "left join \n" +
                            "    audit_task b \n" +
                            "    on\n" +
                            "    a.TASKGUID = b.RowGuid\n" +
                            "where\n" +
                            "    1 = 1\n" +
                            "    and a.STATUS >= 12 and a.STATUS != 20\n" +
                            "    and (a.is_lczj != '2' or a.is_lczj is null) " ;

                    if(StringUtil.isNotBlank(startdate)&&StringUtil.isNotBlank(enddate)){
                        strsql += " and a.APPLYDATE between '"+startdate+"' and '"+enddate+"'";
                        countsql += " and a.APPLYDATE between '"+startdate+"' and '"+enddate+"'";
                    }
                    if(StringUtil.isNotBlank(applyername)){
                        strsql += " and a.applyername like '%"+applyername+"%'";
                        countsql += " and a.applyername like '%"+applyername+"%'";
                    }
                    if(StringUtil.isNotBlank(taskname)){
                        strsql += " and b.taskname like '%"+taskname+"%'";
                        countsql += " and b.taskname like '%"+taskname+"%'";
                    }
                    if(StringUtil.isNotBlank(certnum)){
                        strsql += " and a.certnum like '%"+certnum+"%'";
                        countsql += " and a.certnum like '%"+certnum+"%'";
                    }
                    if(StringUtil.isNotBlank(status)){
                        strsql += " and a.status = '"+status+"'";
                        countsql += " and a.status = '"+status+"'";
                    }
                    if(StringUtil.isNotBlank(areacode)){
                        strsql += " and a.areacode in("+areacode+")";
                        countsql += " and a.areacode in("+areacode+")";
                    }
                    if(StringUtil.isNotBlank(contactmobile)){
                        strsql += " and (a.contactmobile like '%"+contactmobile+"%' or a.CONTACTPHONE like '%"+contactmobile+"%')";
                        countsql += " and (a.contactmobile like '%"+contactmobile+"%' or a.CONTACTPHONE like '%"+contactmobile+"%')";
                    }
                    if("0".equals(iscunzhen)){
                        strsql += " and length(a.AREACODE) <= 6 ";
                        countsql += " and length(a.AREACODE) <= 6 ";
                    }
                    if (StringUtil.isNotBlank(flowsn)) {
                        strsql += " and a.flowsn like '%"+flowsn+"%'";
                        countsql += " and a.flowsn like '%"+flowsn+"%'";
                    }
                    strsql +=   " order by\n" +
                            "    a.applydate\n" +
                            "    desc ";
                    List<Record> list = commservice.findList(strsql, first, pageSize, Record.class);

                    int count = service.countTask(countsql);


                    this.setRowCount(count);


                    return list;
                }
            };
        }
        return modelall;
    }


    public List<SelectItem> getEvalevelModel() {
        if (evalevelModel == null) {
            evalevelModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "所属区县", null, false));

        }
        return this.evalevelModel;
    }

    public List<SelectItem> getStatusModel() {
        if (statusModel == null) {
            List<SelectItem> silist = new ArrayList<SelectItem>();
            statusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "办件状态", null, false));
            for (SelectItem selectitem : statusModel) {
                if (!"8".equals(selectitem.getValue())&&!"10".equals(selectitem.getValue())&&!"20".equals(selectitem.getValue())) {
                    silist.add(selectitem);
                }
            }
            statusModel= silist;
        }
        return this.statusModel;
    }

    public ExportModel getExportModel() {
       if (exportModel == null) {
            exportModel = new ExportModel("taskname,APPLYDATE,APPLYERNAME,mobile,CERTNUM,ouname,flowsn,status,AREACODE",
                    "事项名称,申请时间,申请人,联系方式,证件号码,所属部门,办件编号,办件状态,所属县区");
        }
        return exportModel;
    }



    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }





    public String getApplyername() {
        return applyername;
    }

    public void setApplyername(String applyername) {
        this.applyername = applyername;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getCertnum() {
        return certnum;
    }

    public void setCertnum(String certnum) {
        this.certnum = certnum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getContactmobile() {
        return contactmobile;
    }

    public void setContactmobile(String contactmobile) {
        this.contactmobile = contactmobile;
    }

    public String getIscunzhen() {
        return iscunzhen;
    }

    public void setIscunzhen(String iscunzhen) {
        this.iscunzhen = iscunzhen;
    }

    public String getFlowsn() {
        return flowsn;
    }

    public void setFlowsn(String flowsn) {
        this.flowsn = flowsn;
    }
    
}
