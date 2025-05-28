package com.epoint.jiningzwfw.teacherhealthreport.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.cxbus.api.ICxBusService;

/**
 * 教师资格体检报告list页面对应的后台
 * 
 * @author liuhui
 * @version 2022年5月13日
 */
@RestController("teachercardprojectlistaction")
@Scope("request")
public class TeacherCardProjectListAction extends BaseController
{
    private Logger log = Logger.getLogger(TeacherCardProjectListAction.class);
    private static final long serialVersionUID = -4045631566146210366L;

    @Autowired
    private IAuditProject iAuditProject;
    
    @Autowired
    private ICxBusService iCxBusService;
    
    
    /**
     * 教师资格证办件统计信息
     */
    private Record dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditProject> model;

    /**
     * 县区下拉列表model
     */
    private List<SelectItem> countyModel = null;
    /**
     * 导出模型Model
     */
    private ExportModel exportModel;

    public void pageLoad() {
        if (dataBean == null) {
            dataBean = new Record();
        }

    }

    public DataGridModel<AuditProject> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProject>()
            {
                private static final long serialVersionUID = 2239055340019994361L;

                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    // 封装筛选办件的办件名称条件
                    // 封装筛选办件的办件名称条件
                    List<String> pnList = new ArrayList<>();
                    pnList.add("51a74101-84c0-44f0-aaff-534e6f82675d");
                    pnList.add("b574893d-024c-4672-8aa0-827b856088c6");
                    pnList.add("5e5afa55-d1a3-48ca-88ab-e079cd1b47dd");
                    pnList.add("5d617e32-9691-4cb2-89b4-426477205e54");
                    pnList.add("a2e57190-6b85-46c6-860b-c9b2830f7a89");
                    pnList.add("e7d6f9da-cb5b-4dea-ab74-e17b7113217d");
                    pnList.add("c328ee00-4e7d-4674-8fd1-43513cdc1edc");
                    pnList.add("c73e6826-1880-437e-b820-7c2e42747c99");
                    pnList.add("42163cde-0795-47e7-82eb-1549af70d601");
                    pnList.add("8b2cfa6a-41ff-475f-aac8-51a24646994a");
                    pnList.add("02921679-9926-4c98-a04b-5a2436a51af7");
                    pnList.add("1220893a-6a10-4a5c-b61f-f954f4e70244");
                    pnList.add("ce9acd2c-0d8c-4e68-be79-363c36d893a1");
                    pnList.add("c9024160-5031-4370-be66-96405b6cbafe");
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
//                    sqlConditionUtil.in("projectname", "教师资质证");
                    sqlConditionUtil.in("task_id", StringUtil.joinSql(pnList));
                    if (dataBean.getDate("applydateStart") != null) {
                        sqlConditionUtil.ge("applydate",
                                EpointDateUtil.getBeginOfDate(dataBean.getDate("applydateStart")));
                    }
                    if (dataBean.getDate("applydateEnd") != null) {
                        sqlConditionUtil.le("applydate", EpointDateUtil.getEndOfDate(dataBean.getDate("applydateEnd")));
                    }
                    sqlConditionUtil.ge("status", "12");
                    
                    PageData<AuditProject> projectdata = iAuditProject.getAuditProjectPageDataByCondition(
                            "rowguid,flowsn,applyername,certnum,projectname,ouname,areacode,applydate,contactmobile,address", sqlConditionUtil.getMap(),first,pageSize,null,null,null).getResult();
                    
                    List<AuditProject> result = projectdata.getList();
                    
                    for (AuditProject project :result) {
                		Record record = iCxBusService.getDzbdDetail("formtable20220426153449",project.getRowguid());
                		
                		if (record != null) {
                			project.set("sxzy", record.getStr("sxzy"));
                			project.set("byyx", record.getStr("byyx"));
                			project.set("sfyj", record.getStr("dxkz1"));
                			project.set("yjdz", record.getStr("wbk14"));
                			project.set("xlxz1", record.getStr("xlxz1"));
                		}

                    }
                    
                    this.setRowCount(
                            iAuditProject.getAuditProjectCountByCondition(sqlConditionUtil.getMap()).getResult());
                    return result;
                }
            };
        }
        return model;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getCountyModel() {
        if (countyModel == null) {
            countyModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "县区名称", null, false));
        }
        return this.countyModel;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel(
                    "flowsn,applyername,certnum,projectname,ouname,areacode,applydate,contactmobile,sxzy,byyx,sfyj,yjdz,xlxz1",
                    "办件编号,姓名,身份证,事项名称,所属部门,户籍所属区县,申报时间,联系电话,所学专业,毕业院校,是否邮寄,邮寄地址,教师资格类别");
            exportModel.addColumnWidth("flowsn", 10000);
            exportModel.addColumnWidth("applyername", 10000);
            exportModel.addColumnWidth("certnum", 10000);
            exportModel.addColumnWidth("projectname", 10000);
            exportModel.addColumnWidth("ouname", 10000);
            exportModel.addColumnWidth("areacode", 10000);
            exportModel.addColumnWidth("applydate", 10000);
            exportModel.addColumnWidth("contactmobile", 10000);
        }
        return exportModel;
    }

    public Record getDataBean() {
        return dataBean;
    }

    public void setDataBean(Record dataBean) {
        this.dataBean = dataBean;
    }

}
