package com.epoint.jiningzwfw.zntbtask.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.jiningzwfw.zntbtask.api.IZndbTaskService;
import com.epoint.jiningzwfw.zntbtask.api.entity.ZndbTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 智能导办办理事项表list页面对应的后台
 *
 * @author 19273
 * @version [版本号, 2023-09-18 14:43:48]
 */
@RestController("zndbtasklistaction")
@Scope("request")
public class ZndbTaskListAction extends BaseController {
    @Autowired
    private IZndbTaskService service;

    /**
     * 智能导办办理事项表实体对象
     */
    private ZndbTask dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<ZndbTask> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * 事项类别下拉列表model
     */
    private List<SelectItem> tasktypeModel = null;
    /**
     * 工程阶段下拉列表model
     */
    private List<SelectItem> phaseModel = null;
    /**
     * 办件类型下拉列表model
     */
    private List<SelectItem> typeModel = null;

    private String tasktype;
    private String phase;
    private String name;
    private String type;


    public void pageLoad() {
    }


    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", l("成功删除！"));
    }

    public DataGridModel<ZndbTask> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<ZndbTask>() {

                @Override
                public List<ZndbTask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> list1 = new ArrayList<Object>();
                    String conditionSql = "";
                    if (StringUtil.isNotBlank(tasktype)) {
                        conditionSql += " and tasktype = '" + tasktype + "' ";
                    }
                    if (StringUtil.isNotBlank(phase)) {
                        conditionSql += " and phase = '" + phase + "' ";
                    }
                    if (StringUtil.isNotBlank(name)) {
                        conditionSql += " and name like '%" + name + "%' ";
                    }
                    if (StringUtil.isNotBlank(type)) {
                        conditionSql += " and type = '" + type + "' ";
                    }
                    conditionSql += "  ORDER BY phase ,CAST(sxindex AS UNSIGNED) ASC ";
                    List<ZndbTask> list = service.findList(
                            ListGenerator.generateSql("zndb_task", conditionSql, sortField, sortOrder), first, pageSize,
                            list1.toArray());
                    int count = service.countZndbTask(ListGenerator.generateSql("zndb_task", conditionSql), list1.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }


    public ZndbTask getDataBean() {
        if (dataBean == null) {
            dataBean = new ZndbTask();
        }
        return dataBean;
    }

    public void setDataBean(ZndbTask dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }


    public List<SelectItem> getTasktypeModel() {
        if (tasktypeModel == null) {
            tasktypeModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, true));
        }
        return this.tasktypeModel;
    }

    public List<SelectItem> getPhaseModel() {
        if (phaseModel == null) {
            phaseModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "审批阶段", null, true));
        }
        return this.phaseModel;
    }

    public List<SelectItem> getTypeModel() {
        if (typeModel == null) {
            typeModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "智能导办办件类型", null, true));
        }
        return this.typeModel;
    }

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
