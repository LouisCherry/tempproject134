package com.epoint.cs.sjtsproject.action;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.cs.tsproject.api.entity.TsProject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.cs.sjtsproject.api.ISjTsprojectService;
import com.epoint.cs.tsproject.api.ITsProjectService;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 推送数据详情页面对应的后台
 * 
 * @author 18300
 * @version [版本号, 2018-12-13 20:11:11]
 */
@RestController("sjtsprojectdetailaction")
@Scope("request")
public class SjTsProjectDetailAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -2502697142275262377L;

    @Autowired
    private ISjTsprojectService service;

    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;
    
    private String taskname;
    private String itemid;
    private String taskType;
    private String promise_day;
    private String taskDep;
    private String proType;
    private String proTime;
    private String applyType;
    private String proName;
    private String certId;
    private String certType;
    private String address;
    private String legal;
    private String legalId;
    private String proName2;
    private String proNumber;
    private String email;
    private String remark;
    private String proId = null ;
    
    public void pageLoad() {
        String unid = getRequestParameter("guid");
        Record record = service.findByUnid(unid);
        proId = record.get("proId");
        taskname = record.get("taskname");
        itemid = record.get("itemid");
        taskType = record.get("taskType");
        promise_day = record.get("promise_day");
        taskDep = record.get("taskDep");
        proType = record.get("proType");
        proTime = new SimpleDateFormat("YY-MM-dd HH:mm:ss").format(record.get("proTime"));
        applyType = record.get("applyType");
        proName = record.get("proName");
        certId = record.get("certId");
        certType = record.get("certType");
        address = record.get("address");
        legal = record.get("legal");
        legalId = record.get("legalId");
        proName2 = record.get("proName");
        proNumber = record.get("proNumber");
        email = record.get("email");
        remark = record.get("remark");
    }
    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>()
            {

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Record> list = service.findPhase(proId);
                    return list;
                }

            };
        }
        return model;
    }
    public String getTaskname() {
        return taskname;
    }

    public ISjTsprojectService getService() {
        return service;
    }

    public String getItemid() {
        return itemid;
    }

    public String getTaskType() {
        return taskType;
    }

    public String getPromise_day() {
        return promise_day;
    }

    public String getTaskDep() {
        return taskDep;
    }

    public String getProType() {
        return proType;
    }

    public String getProTime() {
        return proTime;
    }

    public String getApplyType() {
        return applyType;
    }

    public String getProName() {
        return proName;
    }

    public String getCertId() {
        return certId;
    }

    public String getCertType() {
        return certType;
    }

    public String getAddress() {
        return address;
    }

    public String getLegal() {
        return legal;
    }

    public String getLegalId() {
        return legalId;
    }

    public String getProName2() {
        return proName2;
    }

    public String getProNumber() {
        return proNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getRemark() {
        return remark;
    }

}
