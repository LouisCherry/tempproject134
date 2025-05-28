package com.epoint.zczwfw.evaluateproject.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.StringUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.zczwfw.evaluateprojecterr.api.IEvaluateProjectErrService;
import com.epoint.zczwfw.evaluateprojecterr.api.entity.EvaluateProjectErr;

/**
 * 评价办件导入通知页面对应的后台
 * 
 * @author yrchan
 * @version [版本号, 2022-04-12 15:52:28]
 */
@RestController("evaluateprojectimpdetailaction")
@Scope("request")
public class EvaluateProjectImpDetailAction extends BaseController
{
    private static final long serialVersionUID = 1863083467286041254L;

    /**
     * 评价办件异常信息表API
     */
    @Autowired
    private IEvaluateProjectErrService iEvaluateProjectErrService;

    /**
     * 相关消息API
     */
    @Autowired
    private IMessagesCenterService iMessagesCenterService;

    /**
     * 表格控件model
     */
    private DataGridModel<EvaluateProjectErr> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    private String impGuid;

    public void pageLoad() {
        impGuid = getRequestParameter("impGuid");
        // 总数量
        String totalCount = getRequestParameter("totalCount");
        int total = Integer.valueOf(totalCount);

        // 导入失败数量
        SqlConditionUtil countSql = new SqlConditionUtil();
        countSql.eq("imp_guid", impGuid);
        int errCount = iEvaluateProjectErrService.countByCondition(EvaluateProjectErr.class, countSql.getMap());
        // 导入成功数量
        int successCount = total - errCount;
        if (successCount < 0) {
            successCount = 0;
        }
        addCallbackParam("successCount", successCount);
        addCallbackParam("errCount", errCount);

    }

    public DataGridModel<EvaluateProjectErr> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<EvaluateProjectErr>()
            {

                private static final long serialVersionUID = 4513112501517567356L;

                @Override
                public List<EvaluateProjectErr> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    if (StringUtil.isNotBlank(impGuid)) {
                        conditionSql = " and imp_guid = ? ";
                        conditionList.add(impGuid);
                    }

                    List<EvaluateProjectErr> list = iEvaluateProjectErrService.findList(
                            ListGenerator.generateSql("evaluate_project_err", conditionSql, sortField, sortOrder),
                            first, pageSize, conditionList.toArray());
                    int count = iEvaluateProjectErrService.countEvaluateProjectErr(
                            ListGenerator.generateSql("evaluate_project_err", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    /**
     * 
     * 删除代办
     */
    public void knowSelect() {
        if (StringUtil.isNotBlank(impGuid)) {
            iMessagesCenterService.deleteMessageByIdentifier(impGuid, userSession.getUserGuid());
        }
        addCallbackParam("msg", "代办删除成功！");
    }

    /**
     * 导出
     * 
     * @return
     */
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel(
                    "accept_user,accept_department,task_name,apply_object,apply_id,accept_date,handle_date,link_user,link_phone",
                    "受理人,受理所属部门,事项名称,申请人或申请单位,身份证号/统一社会信用代码,受理时间,办结时间,联系人,联系电话");
        }
        return exportModel;
    }

}
