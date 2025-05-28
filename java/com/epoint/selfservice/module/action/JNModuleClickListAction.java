package com.epoint.selfservice.module.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditqueue.auditznsbqueue.api.IQueueList;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;

/**
 * 今日取号list页面对应的后台
 * 
 * @author W J
 * @version [版本号, 2017-10-26 21:43:22]
 */
@RestController("jnmoduleclicklistaction")
@Scope("request")
public class JNModuleClickListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Record dataBean;
    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    @Autowired
    private IQueueList queuelistservice;
    @Autowired
    private IAuditOrgaServiceCenter centerservice;
    private String centerguid;
    private String clicktimeStart;
    private String clicktimeEnd;
    private List<SelectItem> centerguidModel = new ArrayList<SelectItem>();

    public void pageLoad() {
        clicktimeStart = EpointDateUtil.convertDate2String(EpointDateUtil.addMonth(new Date(), -1), "yyyy-MM-dd");
        clicktimeEnd = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd");
        addCallbackParam("clicktimeEnd", clicktimeEnd);
        addCallbackParam("clicktimeStart", clicktimeStart);
    }

    // 导出
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("modulename,clicknum", "模块名,使用次数");
        }
        return exportModel;
    }

    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {

                    List<Record> recordList = new ArrayList<Record>();
                    if (StringUtil.isNotBlank(centerguid)) {
                        recordList = queuelistservice.getModuleClick(centerguid, clicktimeStart, clicktimeEnd)
                                .getResult();
                    }
                    else {
                        recordList = queuelistservice.getModuleClick(clicktimeStart, clicktimeEnd).getResult();
                    }
                    this.setRowCount(recordList.size());
                    if (recordList.size() > (first + pageSize)) {
                        recordList = recordList.subList(first, (first + pageSize));
                    }
                    else {
                        recordList = recordList.subList(first, recordList.size());
                    }

                    return recordList;
                }

            };
        }
        return model;
    }

    public Record getDataBean() {
        if (dataBean == null) {
            dataBean = new Record();
        }
        return dataBean;
    }

    public void setDataBean(Record dataBean) {
        this.dataBean = dataBean;
    }

    public String getCenterguid() {
        return centerguid;
    }

    public void setCenterguid(String centerguid) {
        this.centerguid = centerguid;
    }

    public String getClicktimeStart() {
        return clicktimeStart;
    }

    public void setClicktimeStart(String clicktimeStart) {
        this.clicktimeStart = clicktimeStart;
    }

    public String getClicktimeEnd() {
        return clicktimeEnd;
    }

    public void setClicktimeEnd(String clicktimeEnd) {
        this.clicktimeEnd = clicktimeEnd;
    }

    public List<SelectItem> getCenterguidModel() {
        centerguidModel.add(new SelectItem("", "所有"));

        SqlConditionUtil sql = new SqlConditionUtil();
        sql.lt("servicecentelevel", "3");
        List<AuditOrgaServiceCenter> centerlist = centerservice.getAuditOrgaServiceCenterByCondition(sql.getMap())
                .getResult();
        for (AuditOrgaServiceCenter center : centerlist) {
            SelectItem a = new SelectItem(center.getRowguid(), center.getCentername());
            centerguidModel.add(a);
        }
        return this.centerguidModel;
    }
}
