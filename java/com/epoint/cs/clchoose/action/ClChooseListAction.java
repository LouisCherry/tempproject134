package com.epoint.cs.clchoose.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.cs.clchoose.api.IClChooseService;
import com.epoint.cs.tsproject.api.entity.TsProject;

/**
 * 推送数据list页面对应的后台
 * 
 * @author 18300
 * @version [版本号, 2018-12-13 20:11:11]
 */
@RestController("clchooselistaction")
@Scope("request")
public class ClChooseListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -3344310068109465486L;

    @Autowired
    private IClChooseService service;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditTaskMaterial> model;

    private String taskid;

    public void pageLoad() {
        taskid = getRequestParameter("taskid");
    }

    public DataGridModel<AuditTaskMaterial> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditTaskMaterial>()
            {

                @Override
                public List<AuditTaskMaterial> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<AuditTaskMaterial> list = service.findList(taskid);
                    return list;
                }

            };
        }
        return model;
    }

}
