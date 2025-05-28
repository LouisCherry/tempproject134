package com.epoint.jiningzwfw.teacherhealthreport.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.dataimport.DataImportModel9;
import com.epoint.basic.faces.dataimport.ImportExcelHandler;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.jiningzwfw.teacherhealthreport.api.ITeacherHealthReportService;
import com.epoint.jiningzwfw.teacherhealthreport.api.entity.TeacherHealthReport;
import com.epoint.util.ZwfwSqlCondition;
import com.epoint.util.ZwfwSqlCondition.SqlType;

/**
 * 教师资格体检报告list页面对应的后台
 * 
 * @author liuhui
 * @version 2022年5月13日
 */
@RestController("teacherhealthreportlistaction")
@Scope("request")
public class TeacherHealthReportListAction extends BaseController
{
    private static final long serialVersionUID = 5002805872508564404L;

    private Logger log = Logger.getLogger(TeacherHealthReportListAction.class);
    @Autowired
    private ITeacherHealthReportService service;
    @Autowired
    private ICodeItemsService iCodeItemsService;
    @Autowired
    private IConfigService iConfigService;
    /**
     * 教师资格体检报告实体对象
     */
    private TeacherHealthReport dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<TeacherHealthReport> model;

    /**
     * 县区下拉列表model
     */
    private List<SelectItem> countyModel = null;

    /**
     * 导入数据模型
     */
    private DataImportModel9 importModel;

    public void pageLoad() {
        if (dataBean == null) {
            dataBean = new TeacherHealthReport();
        }
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<TeacherHealthReport> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<TeacherHealthReport>()
            {
                private static final long serialVersionUID = 2239055340019994361L;

                @Override
                public List<TeacherHealthReport> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    ZwfwSqlCondition condition = ZwfwSqlCondition
                            .getSearchCondition(getRequestContext().getComponents());
                    if (StringUtil.isNotBlank(dataBean.getCounty())) {
                        condition.put("county", dataBean.getCounty(), SqlType.EXACT);
                    }
                    List<TeacherHealthReport> list = service.findList(ListGenerator.generateSql("teacher_health_report",
                            condition.getSql(), sortField, sortOrder), first, pageSize, condition.getParams());
                    int count = service.countTeacherHealthReport(
                            ListGenerator.generateSql("teacher_health_report", condition.getSql()),
                            condition.getParams());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public DataImportModel9 getImportModel() {
        if (importModel == null) {
            importModel = new DataImportModel9(new ImportExcelHandler()
            {
                private static final long serialVersionUID = 6708324975872876657L;
                private List<TeacherHealthReport> teacherHealthReportsList = new ArrayList<TeacherHealthReport>();
                private StringBuffer stringBuffer = new StringBuffer();
                private String healthReportImportcount = iConfigService
                        .getFrameConfigValue("health_report_importcount");

                @Override
                public String saveExcelData(String filename, String sheetName, int sheet, int curRow, int totalRows,
                        Object[] data) {
                    String message = null;
                    if (StringUtil.isBlank(healthReportImportcount)) {
                        return "未查询单次导入的数据数量的限制参数,请联系系统管理员,配置单次导入的数据数量的限制参数！";
                    }
                    if (Integer.parseInt(healthReportImportcount) < totalRows) {
                        return "单次导入的数据数量不能超过" + Integer.parseInt(healthReportImportcount);
                    }
                    // 当前行数
                    if (curRow > 0) {
                        // 对导入的表分部门判断
                        if (data != null) {
                            // 对数据进行非空校验
                            // 字段 姓名 - 身份证 - 体检单位 - 体检报告编号 - 是否合格 - 医院名称 - 县区
                            if (StringUtil.isNotBlank(data[0]) && StringUtil.isNotBlank(data[1])
                                    && StringUtil.isNotBlank(data[2]) && StringUtil.isNotBlank(data[3])
                                    && StringUtil.isNotBlank(data[4]) && StringUtil.isNotBlank(data[5])
                                    && StringUtil.isNotBlank(data[6]) && StringUtil.isNotBlank(data[7])) {
                                stringBuffer.append(sheetName + ":第" + (curRow + 1) + "行");
                                // 对身份证进行校验
                                if (!data[1].toString().matches("\\d{15}(\\d{2}[0-9xX])?")) {
                                    log.info(sheetName + ":第" + (curRow + 1) + "行身份证信息有误");
                                    message = sheetName + ":第" + (curRow + 1) + "行身份证信息有误";
                                    stringBuffer.append(message);
                                }
                                else {
                                    // 身份证符合规范 - 检验看是否已经存在该人员的信息
                                    SqlConditionUtil sql = new SqlConditionUtil();
                                    sql.eq("sfz", data[1].toString());
                                    TeacherHealthReport teacherHealthReport = service
                                            .getTeacherHealthReportByCondition(sql.getMap());
                                    if (teacherHealthReport != null) {
                                        message = sheetName + ":第" + (curRow + 1) + "行身份证编号为：" + data[1].toString()
                                                + "的人员信息已存在";
                                        stringBuffer.append(message);
                                    }
                                }
                            }
                            else {
                                log.info(sheetName + ":第" + (curRow + 1) + "行存在必填项未填写");
                                message = sheetName + ":第" + (curRow + 1) + "行存在必填项未填写";
                                stringBuffer.append(message);
                            }
                            if ((sheetName + ":第" + (curRow + 1) + "行").equals(stringBuffer.toString())) {
                                TeacherHealthReport teacherHealthReport = new TeacherHealthReport();
                                teacherHealthReport.setRowguid(UUID.randomUUID().toString());
                                teacherHealthReport.setName(data[0].toString());
                                teacherHealthReport.setSfz(data[1].toString());
                                teacherHealthReport.setTjdw(data[2].toString());
                                teacherHealthReport.setReportnumber(data[3].toString());
                                teacherHealthReport.setTjdate(EpointDateUtil.convertString2Date(data[4].toString(),
                                        EpointDateUtil.DATE_FORMAT));
                                teacherHealthReport
                                        .setIspass("是".equals(data[5].toString()) ? ZwfwConstant.CONSTANT_STR_ONE
                                                : ZwfwConstant.CONSTANT_STR_ZERO);
                                teacherHealthReport.setHospitalname(data[6].toString());
                                List<CodeItems> listCodeItemsByCodeName = iCodeItemsService
                                        .listCodeItemsByCodeName("县区名称");
                                if (listCodeItemsByCodeName != null && !listCodeItemsByCodeName.isEmpty()) {
                                    List<CodeItems> collect = listCodeItemsByCodeName.stream()
                                            .filter(x -> x.getItemText().equals(data[7].toString()))
                                            .collect(Collectors.toList());
                                    if (collect != null && !collect.isEmpty()) {
                                        teacherHealthReport.setCounty(collect.get(0).getItemValue());
                                        teacherHealthReportsList.add(teacherHealthReport);
                                        stringBuffer = new StringBuffer();
                                    }
                                    else {
                                        log.info(sheetName + ":第" + (curRow + 1) + "行县区名称在代码项中不存在，请联系管理员添加;");
                                        message = sheetName + ":第" + (curRow + 1) + "行县区名称在代码项中不存在，请联系管理员添加;";
                                        stringBuffer.append(message);
                                    }
                                }
                                else {
                                    log.info("==========代码项为县区名称的代码项不存在==========");
                                }
                            }
                            else {
                                stringBuffer.append(";");
                            }

                            // 表中数据
                            if (teacherHealthReportsList != null && !teacherHealthReportsList.isEmpty()) {
                                if (teacherHealthReportsList.size() < totalRows && teacherHealthReportsList
                                        .size() >= Integer.parseInt(healthReportImportcount)) {
                                    try {
                                        EpointFrameDsManager.begin(null);
                                        for (TeacherHealthReport teacherHealthReport : teacherHealthReportsList) {
                                            service.insert(teacherHealthReport);
                                        }
                                        EpointFrameDsManager.commit();
                                        teacherHealthReportsList = new ArrayList<>();
                                    }
                                    catch (Exception e) {
                                        log.info("==========保存手动导入的信息异常：" + e.getMessage() + "==========", e);
                                        EpointFrameDsManager.rollback();
                                    }
                                    finally {
                                        EpointFrameDsManager.close();
                                    }
                                }
                                else if (teacherHealthReportsList.size() >= totalRows || curRow == totalRows) {
                                    try {
                                        EpointFrameDsManager.begin(null);
                                        for (TeacherHealthReport teacherHealthReport : teacherHealthReportsList) {
                                            service.insert(teacherHealthReport);
                                        }
                                        EpointFrameDsManager.commit();
                                    }
                                    catch (Exception e) {
                                        log.info("==========保存手动导入的信息异常：" + e.getMessage() + "==========", e);
                                        EpointFrameDsManager.rollback();
                                    }
                                    finally {
                                        EpointFrameDsManager.close();
                                    }
                                }
                            }

                            // 判断是否
                            if (curRow == totalRows && StringUtil.isNotBlank(stringBuffer.toString())) {
                                importModel.setMessage(stringBuffer.toString());
                                message = stringBuffer.toString();
                            }
                        }
                    }
                    return message;
                }

                @Override
                public void refreshTable() {
                }
            });
        }
        return importModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getCountyModel() {
        if (countyModel == null) {
            countyModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "县区名称", null, false));
        }
        return this.countyModel;
    }

    public TeacherHealthReport getDataBean() {
        return dataBean;
    }

    public void setDataBean(TeacherHealthReport dataBean) {
        this.dataBean = dataBean;
    }

}
