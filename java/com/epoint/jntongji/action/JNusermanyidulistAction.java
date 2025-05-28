package com.epoint.jntongji.action;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.bizlogic.mis.CommonService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.zhenggai.api.ZhenggaiService;
@RestController("jnusermanyidulistAction")
@Scope("request")
public class JNusermanyidulistAction extends BaseController
{
    private static final long serialVersionUID = 1L;
    private DataGridModel<AuditProject> modelall;

    private ExportModel exportModel;
    private String startdate;
    private String enddate;
  
    private CommonService commservice = new CommonService();

    @Override
    public void pageLoad() {

        this.startdate = this.getRequestParameter("startdate");
        if ("kong".equals(startdate)) {
            startdate = "";
        }

        this.enddate = this.getRequestParameter("enddate");

        if ("kong".equals(enddate)) {
            enddate = "";
        }

    }

    public DataGridModel<AuditProject> getDataGridDataTask() {
        // 获得表格对象
        if (modelall == null) {
            modelall = new DataGridModel<AuditProject>()
            {
                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
               
                    String centerguid=ZwfwUserSession.getInstance().getCenterGuid();
                 	
                    String strsql =  " SELECT ouname,ACCEPTUSERNAME AS RECEIVEUSERNAME,ACCEPTUSERGUID,"
                                   + " SUM(case when satisfied = '1' OR satisfied = '2' or satisfied = '3' OR satisfied = '0' THEN 1 else 0 END) AS manyi,"
                                   + " SUM(case when satisfied = '5' or satisfied = '4' THEN 1 else 0 END) AS bumanyi,"
                                   + " SUM(case when satisfied is NOT NULL THEN 1 else 0 END) AS zj"
                                   + " from audit_online_evaluat e INNER JOIN audit_project p"
                                   + " on p.RowGuid = e.ClientIdentifier where clienttype =10"
                                   + " and ACCEPTUSERGUID is NOT NULL and CENTERGUID='"+centerguid+"'" ;
                    
                    if (StringUtil.isNotBlank(startdate)) {
                        strsql += " and ACCEPTUSERDATE>=str_to_date('" + startdate + "', '%Y-%m-%d %H:%i:%s')";
                    }
                    
                    if (StringUtil.isNotBlank(enddate)) {
                        strsql += " and ACCEPTUSERDATE<=str_to_date('" + enddate + "', '%Y-%m-%d %H:%i:%s')";
                    }
                    strsql +=   " GROUP BY ouguid";
                    List<AuditProject> list = commservice.findList(strsql, first, pageSize, AuditProject.class);
                   
                    List<AuditProject> list1=commservice.findList(strsql, AuditProject.class);
                    
                    this.setRowCount(list1.size());
                    //控制double类型小数点后的位数（一位）
                    DecimalFormat df = new DecimalFormat( "0.0");
                    for (Iterator<AuditProject> iterator = list.iterator(); iterator.hasNext();) {
                        AuditProject auditProject = (AuditProject) iterator.next();
                        
                            auditProject.put("fmanyilv",(df.format(Double.valueOf(1.0*(Integer)auditProject.get("manyi")/(Integer)auditProject.get("zj"))*100)) + "%");
                    }
                    return list;
                }
            };
        }
        return modelall;
    }

    public ExportModel getExportModel() {
       if (exportModel == null) {
            exportModel = new ExportModel("ouname,zj,manyi,bumanyi,fmanyilv",
                    "部门,总评价数,满意数,不满意数,好评率");
        }
        return exportModel;
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


}
