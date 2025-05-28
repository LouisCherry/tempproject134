package com.epoint.expert.expertinstance.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.xm.similarity.util.StringUtil;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.expert.expertinstance.api.IExpertInstanceService;
import com.epoint.expert.expertinstance.api.entity.ExpertInstance;
import com.epoint.expert.expertiresult.api.IExpertIResultService;
import com.epoint.expert.expertisms.api.IExpertISmsService;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;

/**
 * 专家抽取实例表list页面对应的后台
 * 
 * @author Lee
 * @version [版本号, 2019-08-21 15:41:50]
 */
@RestController("expertinstancelistwithadvancedsearchaction")
@Scope("request")
public class ExpertInstanceListWithAdvancedSearchAction extends BaseController
{
    @Autowired
    private IExpertInstanceService service;
    @Autowired
    private IExpertISmsService smsService;
    @Autowired
    private IExpertIResultService resultService;

    /**
     * 专家抽取实例表实体对象
     */
    private ExpertInstance dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<ExpertInstance> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    private Date startDate;

    private Date endDate;

    private String cqExpertName;

    private String pbExpertName;

    private String cqCompanyName;

    private String pbCompanyName;

    public void pageLoad() {
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            //级联删除
            service.deleteResultByInstanceGuid(sel);
            service.deleteRuleByInstanceGuid(sel);
            service.deleteSmsByInstanceGuid(sel);
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<ExpertInstance> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<ExpertInstance>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<ExpertInstance> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);

                    //搜索条件
                    if (startDate != null) {
                        conditionSql += " and ExtractTime>?";
                        conditionList.add(startDate);
                    }
                    if (endDate != null) {
                        conditionSql += " and ExtractTime<?";
                        conditionList.add(endDate);
                    }
                    if (StringUtil.isNotBlank(dataBean.getTaskname())) {
                        String taskname = "%" + dataBean.getTaskname() + "%";
                        conditionSql += " and taskname like ?";
                        conditionList.add(taskname);
                    }
                    if (StringUtil.isNotBlank(cqExpertName)) {
                        String expertName = "%" + cqExpertName + "%";
                        conditionSql += " and rowguid in (select instanceGuid from Expert_I_Result where ExpertName like ? and Is_Attend=1) ";
                        conditionList.add(expertName);
                    }
                    if (StringUtil.isNotBlank(pbExpertName)) {
                        String pbexpertName = "%" + pbExpertName + "%";
                        conditionSql += " and rowguid in (select instanceGuid from Expert_I_Rule where ObjectName like ? and ObjectType=2) ";
                        conditionList.add(pbexpertName);
                    }
                    if (StringUtil.isNotBlank(cqCompanyName)) {
                        String companyName = "%" + cqCompanyName + "%";
                        conditionSql += " and  rowguid in (SELECT   a.InstanceGuid FROM expert_i_result a INNER JOIN expert_info b INNER JOIN Expert_Company c WHERE a.ExpertGuid = b.RowGuid and b.ComanyGuid=c.rowguid and c.CompanyName like ?)";
                        conditionList.add(companyName);
                    }
                    if (StringUtil.isNotBlank(pbCompanyName)) {
                        String pbcompanyName = "%" + pbCompanyName + "%";
                        conditionSql += " and  rowguid in (SELECT InstanceGuid FROM expert_i_rule where ObjectName like ? and ObjectType='1')";
                        conditionList.add(pbcompanyName);
                    }
                    List<ExpertInstance> list = service.findList(
                            ListGenerator.generateSql("Expert_Instance", conditionSql, "ExtractTime", "desc"), first,
                            pageSize, conditionList.toArray());
                    int count = service
                            .findList(ListGenerator.generateSql("Expert_Instance", conditionSql, sortField, sortOrder),
                                    conditionList.toArray())
                            .size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public ExpertInstance getDataBean() {
        if (dataBean == null) {
            dataBean = new ExpertInstance();
        }
        return dataBean;
    }

    public void setDataBean(ExpertInstance dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCqExpertName() {
        return cqExpertName;
    }

    public void setCqExpertName(String cqExpertName) {
        this.cqExpertName = cqExpertName;
    }

    public String getPbExpertName() {
        return pbExpertName;
    }

    public void setPbExpertName(String pbExpertName) {
        this.pbExpertName = pbExpertName;
    }

    public String getCqCompanyName() {
        return cqCompanyName;
    }

    public void setCqCompanyName(String cqCompanyName) {
        this.cqCompanyName = cqCompanyName;
    }

    public String getPbCompanyName() {
        return pbCompanyName;
    }

    public void setPbCompanyName(String pbCompanyName) {
        this.pbCompanyName = pbCompanyName;
    }

}
