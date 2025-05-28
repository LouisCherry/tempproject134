package com.epoint.jiningzwfw.projectstatistics.evaInstance.action;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.bizlogic.mis.CommonService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.export.Exporter;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.systemparameters.entity.FrameConfigCateGory;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.jiningzwfw.projectstatistics.evaInstance.service.IEvaInstanceStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController("evainstancedetaillistaction")
@Scope("request")
public class EvaInstanceDetailListAction extends BaseController
{
    private static final long serialVersionUID = 1L;
    private DataGridModel<Record> modelall;

    private ExportModel exportModel;

    @Autowired
    private IEvaInstanceStatisticsService service;

    @Override
    public void pageLoad() {

    }

    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (modelall == null) {
            modelall = new DataGridModel<Record>()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String deptCode = getRequestParameter("deptcode");
                    String type = getRequestParameter("type");
                    String lyqd = getRequestParameter("lyqd");
                    String sf = getRequestParameter("sf");
                    String month = getRequestParameter("month");
                    SqlConditionUtil sql = new SqlConditionUtil();

                    // 部门id
                    if (StringUtil.isNotBlank(deptCode)){
                        sql.eq("deptcode", deptCode);
                    }else {
                        sql.isBlank("deptcode");
                    }
                    // 满意度
                    if (StringUtil.isNotBlank(type)){
                        String satisfaction = "0";
                        switch (type){
                            case "fcmynum":
                                satisfaction = "5";
                                break;
                            case "mynum":
                                satisfaction = "4";
                                break;
                            case "jbmynum":
                                satisfaction = "3";
                                break;
                            case "bmynum":
                                satisfaction = "2";
                                break;
                            case "fcbmynum":
                                satisfaction = "1";
                                break;
                        }
                        sql.eq("satisfaction", satisfaction);
                    }
                    // 来源渠道
                    if (StringUtil.isNotBlank(lyqd)){
                        sql.eq("pf", lyqd);
                    }
                    // 是否同步
                    if (StringUtil.isNotBlank(sf)){
                        sql.eq("sbsign", sf);
                    }

                    List<Record> list = service.findPage(sql.getMap(),month,first,pageSize);

                    int sum = service.count(sql.getMap(), month);
                    setRowCount(sum);
                    return list;
                }
            };
        }
        return modelall;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("proDepart,taskname,applicant,satisfaction,pf,assesstime",
                    "部门名称,事项名称,评价人,满意度,来源渠道,创建时间");
        }
        return exportModel;
    }

    /**
     * 导出数据
     * @param model2
     */
    public void export(ExportModel model2) {

        String deptCode = getRequestParameter("deptcode");
        String type = getRequestParameter("type");
        String lyqd = getRequestParameter("lyqd");
        String sf = getRequestParameter("sf");
        String month = getRequestParameter("month");
        SqlConditionUtil sql = new SqlConditionUtil();

        // 部门id
        if (StringUtil.isNotBlank(deptCode)){
            sql.eq("deptcode", deptCode);
        }else {
            sql.isBlank("deptcode");
        }
        // 满意度
        if (StringUtil.isNotBlank(type)){
            String satisfaction = "0";
            switch (type){
                case "fcmynum":
                    satisfaction = "5";
                    break;
                case "mynum":
                    satisfaction = "4";
                    break;
                case "jbmynum":
                    satisfaction = "3";
                    break;
                case "bmynum":
                    satisfaction = "2";
                    break;
                case "fcbmynum":
                    satisfaction = "1";
                    break;
            }
            sql.eq("satisfaction", satisfaction);
        }
        // 来源渠道
        if (StringUtil.isNotBlank(lyqd)){
            sql.eq("pf", lyqd);
        }
        // 是否同步
        if (StringUtil.isNotBlank(sf)){
            sql.eq("sbsign", sf);
        }

        List<Record> list = service.findDetailList(sql.getMap(),month);

        model2.setData(list);

        model2.setOutputFileName("部门好差评信息列表");

        model2.setRowCount(list.size());

        // 调用工具类进行导出
        getRequestContext().setRequestComplete(true);

        try {
            Exporter.export(getRequestContext().getRes(), model2);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
