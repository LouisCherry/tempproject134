package com.epoint.sghd.auditjianguan.renlingrecord.action;

import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.sghd.auditjianguan.renlingrecord.api.IRenlingRecordService;
import com.epoint.sghd.auditjianguan.renlingrecord.api.entity.RenlingRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * 认领记录表list页面对应的后台
 *
 * @author lizhenjie
 * @version [版本号, 2022-11-04 19:24:23]
 */
@RestController("renlingrecordlistaction")
@Scope("request")
public class RenlingRecordListAction extends BaseController {
    @Autowired
    private IRenlingRecordService service;

    @Autowired
    private IAuditProject iAuditProject;
    /**
     * 认领记录表实体对象
     */
    private RenlingRecord dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<RenlingRecord> model;
    @Autowired
    private IMessagesCenterService iMessagesCenterService;

    /**
     * 导出模型
     */
    private ExportModel exportModel;


    public void pageLoad() {
        String msgguid = getRequestParameter("MessageItemGuid");
        if (StringUtil.isNotBlank(msgguid)) {
            iMessagesCenterService.deleteMessage(msgguid, userSession.getUserGuid());
        }
    }


    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<RenlingRecord> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<RenlingRecord>() {

                @Override
                public List<RenlingRecord> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<RenlingRecord> list = service.findList(
                            ListGenerator.generateSql("renling_record", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.countRenlingRecord(ListGenerator.generateSql("renling_record", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }


    public RenlingRecord getDataBean() {
        if (dataBean == null) {
            dataBean = new RenlingRecord();
        }
        return dataBean;
    }

    public void setDataBean(RenlingRecord dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("projectname,ouname,username,renlingtime,opiniontime", "办件名称,部门名称,认领人,认领时间,反馈时间");
        }
        return exportModel;
    }


}
