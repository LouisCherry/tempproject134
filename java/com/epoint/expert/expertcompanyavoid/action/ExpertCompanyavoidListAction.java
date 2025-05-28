package com.epoint.expert.expertcompanyavoid.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.expert.expertcompanyavoid.api.IExpertCompanyavoidService;
import com.epoint.expert.expertcompanyavoid.api.entity.ExpertCompanyavoid;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;

/**
 * 专家回避单位表list页面对应的后台
 * 
 * @author cqsong
 * @version [版本号, 2019-08-21 16:37:07]
 */
@RestController("expertcompanyavoidlistaction")
@Scope("request")
public class ExpertCompanyavoidListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -5709394053910009065L;

    @Autowired
    private IExpertCompanyavoidService service;

    /**
     * 专家回避单位表实体对象
     */
    private ExpertCompanyavoid dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<ExpertCompanyavoid> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    public void pageLoad() {
        String expertguid = getRequestParameter("guid");
        addViewData("expertguid", expertguid);
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

    /**
     * 删除一行数据
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleterow() {
        String rowguid = getRequestParameter("rowguid");
        service.deleteByGuid(rowguid);
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<ExpertCompanyavoid> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<ExpertCompanyavoid>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = -7024735175738605919L;

                @Override
                public List<ExpertCompanyavoid> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    String expertguid = getViewData("expertguid");
                    List<ExpertCompanyavoid> list = service.findListByexpertguid(
                            ListGenerator.generateSql("Expert_CompanyAvoid", conditionSql, sortField, sortOrder),
                            expertguid, first, pageSize, conditionList.toArray());
                    int count = service.findListByexpertguid(
                            ListGenerator.generateSql("Expert_CompanyAvoid", conditionSql, sortField, sortOrder),
                            expertguid, conditionList.toArray()).size();
                    this.setRowCount(count);
                    return list;
                }
            };
        }
        return model;
    }

    public ExpertCompanyavoid getDataBean() {
        if (dataBean == null) {
            dataBean = new ExpertCompanyavoid();
        }
        return dataBean;
    }

    public void setDataBean(ExpertCompanyavoid dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

}
