package com.epoint.cs.sjtsproject.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.cs.sjtsproject.api.ISjTsprojectService;
import com.epoint.cs.tsproject.api.entity.TsProject;

/**
 * 推送数据list页面对应的后台
 * 
 * @author 18300
 * @version [版本号, 2018-12-13 20:11:11]
 */
@RestController("sjtsprojectlistaction")
@Scope("request")
public class SjTsProjectListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -3344310068109465486L;

    @Autowired
    private ISjTsprojectService service;

    /**
     * 推送数据实体对象
     */
    private TsProject dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    private String taskname1;
    private String taskid1;
    private String ouname1;
    private String applyuser1;
    private String starttime1;
    private String endtime1;

    public void pageLoad() {
    }

    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>()
            {

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Record> list = service.findList(taskname1, taskid1, ouname1, applyuser1, starttime1, endtime1);
                    for (Record record : list) {
                        if (record.get("proId") != null) {
                            List<Record> list2 = service.findApply(record.get("proId"));
                            if (list2 != null) {
                                for (Record record2 : list2) {
                                    record.set("applyuser", record2.get("dealName"));
                                    record.set("applytime", record2.get("dealTime"));
                                }
                            }
                            List<Record> list3 = service.findBanjie(record.get("proId"));
                            if (list2 != null) {
                                for (Record record2 : list3) {
                                    record.set("banjiename", record2.get("dealName"));
                                    record.set("banjietime", record2.get("dealTime"));
                                }
                            }
                            String status = service.findStatus(record.get("proId"));
                            record.set("prostatus", status);
                        }
                    }
                    return list;
                }

            };
        }
        return model;
    }

    public TsProject getDataBean() {
        if (dataBean == null) {
            dataBean = new TsProject();
        }
        return dataBean;
    }

    public void setDataBean(TsProject dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel(
                    "accepttime,accpetname,applyname,applytime,banjiename,banjietime,flow,status,taskname",
                    "受理时间,受理人员,申请人,申请时间,办结人员,办结时间,办件编号,办件状态,事项名称");
        }
        return exportModel;
    }

    public String getTaskname1() {
        return taskname1;
    }

    public void setTaskname1(String taskname1) {
        this.taskname1 = taskname1;
    }

    public String getTaskid1() {
        return taskid1;
    }

    public void setTaskid1(String taskid1) {
        this.taskid1 = taskid1;
    }

    public String getOuname1() {
        return ouname1;
    }

    public void setOuname1(String ouname1) {
        this.ouname1 = ouname1;
    }

    public String getApplyuser1() {
        return applyuser1;
    }

    public void setApplyuser1(String applyuser1) {
        this.applyuser1 = applyuser1;
    }

    public String getStarttime1() {
        return starttime1;
    }

    public void setStarttime1(String starttime1) {
        this.starttime1 = starttime1;
    }

    public String getEndtime1() {
        return endtime1;
    }

    public void setEndtime1(String endtime1) {
        this.endtime1 = endtime1;
    }

}
