package com.epoint.xmz.auditfszldevice.action;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.core.utils.sql.SqlHelper;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.auditfszldevice.api.entity.AuditFszlDevice;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.auditfszldevice.api.IAuditFszlDeviceService;


/**
 * 放射装置表list页面对应的后台
 *
 * @author ljh
 * @version [版本号, 2024-06-20 10:28:22]
 */
@RestController("auditfszldevicelistaction")
@Scope("request")
public class AuditFszlDeviceListAction extends BaseController {
    @Autowired
    private IAuditFszlDeviceService service;

    /**
     * 放射装置表实体对象
     */
    private AuditFszlDevice dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditFszlDevice> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;


    public void pageLoad() {
        addCallbackParam("fszlGuid",getRequestParameter("fszlGuid"));
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

    public DataGridModel<AuditFszlDevice> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditFszlDevice>() {

                @Override
                public List<AuditFszlDevice> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();

                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    sqlc.eq("fszlguid",getRequestParameter("fszlGuid"));
                    String conditionSql = new SqlHelper().getPatchSql(sqlc.getMap());
                    List<AuditFszlDevice> list = service.findList(
                            ListGenerator.generateSql("audit_fszl_device", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.countAuditFszlDevice(ListGenerator.generateSql("audit_fszl_device", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }


    public AuditFszlDevice getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditFszlDevice();
        }
        return dataBean;
    }

    public void setDataBean(AuditFszlDevice dataBean) {
        this.dataBean = dataBean;
    }


}
