package com.epoint.jn.inproject.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.jn.externalprojectinfoext.api.IExternalProjectInfoExtService;
import com.epoint.jn.externalprojectinfoext.api.entity.ExternalProjectInfoExt;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务管理list页面对应的后台
 * 
 * @author WeiY
 * @version [版本号, 2016-09-26 11:13:04]
 */
@RestController("inprojectinfobyoulistaction")
@Scope("request")
public class InProjectInfoByOUListAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 办件编号
     */
    private  String projectNo;

    /**
     * 辖区编码
     */
    private String areacode = null;
    private ExportModel exportModel;
    private DataGridModel<Record> model;

    @Autowired
    private IExternalProjectInfoExtService externalProjectInfoExtService;
    //日志
    transient Logger log = LogUtil.getLog(InProjectInfoByOUListAction.class);

    @Override
    public void pageLoad() {
        areacode = this.getRequestParameter("areacode");
    }

    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>()
            {
				private static final long serialVersionUID = 1L;
				@SuppressWarnings({"unchecked", "rawtypes", "deprecation" })
                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {

                    List<ExternalProjectInfoExt> list = new ArrayList();
                    List<Record> records = externalProjectInfoExtService.finList(first, pageSize, null, areacode, projectNo);
                    Integer rowCount = externalProjectInfoExtService.finTotal(null,areacode, projectNo);
                    setRowCount(rowCount);
                    return records;
                }
            };
        }
        return model;
    }
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("accept_date,project_no,task_name,accept_user,accept_ou,link_user,link_phone,complete_date",
                    "业务主体受理时间,申办编号,事项名称,受理人,受理单位,联系人,联系电话,办结时间");
        }
        return exportModel;
    }
    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }
}
