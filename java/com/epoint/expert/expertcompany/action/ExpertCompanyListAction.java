package com.epoint.expert.expertcompany.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.expert.expertcompany.api.IExpertCompanyService;
import com.epoint.expert.expertcompany.api.entity.ExpertCompany;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;

/**
 * 从业单位表list页面对应的后台
 * 
 * @author cqsong
 * @version [版本号, 2019-08-21 16:09:09]
 */
@RestController("expertcompanylistaction")
@Scope("request")
public class ExpertCompanyListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -8049226566423801239L;

    private static final String CITY_AREA = "市辖区";
    private static final String COUNTY = "县";

    private static final String ENABLE = "1";
    private static final String DISABLE = "0";

    private static final int IS_DEL = 1;

    @Autowired
    private IExpertCompanyService service;

    @Autowired
    private ICodeItemsService codeItemService;

    @Autowired
    private IAttachService attachService;

    /**
     * 从业单位表实体对象
     */
    private ExpertCompany dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<ExpertCompany> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;
    
    private String type;

    /**
    * 单位类型下拉列表model
    */
    private List<SelectItem> typeModel = null;

    public void pageLoad() {
        String expertguid = getRequestParameter("expertguid");
        addViewData("expertguid", expertguid);
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            // 逻辑删除 is_del为1
            ExpertCompany expertCompany = service.find(sel);
            expertCompany.setIs_del(IS_DEL);
            // 删除附件
            if (StringUtil.isNotBlank(expertCompany.getCliengguid())) {
                attachService.deleteAttachByGuid(expertCompany.getCliengguid());
            }
            service.update(expertCompany);
        }
        addCallbackParam("msg", "成功删除！");
    }

    /**
     * 修改企业状态     
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void changeStatus(String expertCompanyGuid) {
        ExpertCompany expertCompany = service.find(expertCompanyGuid);
        String state = expertCompany.getStatus();
        if (ENABLE.equals(state)) {
            expertCompany.setStatus(DISABLE);
            addCallbackParam("msg", "停用成功！");
        }
        if (DISABLE.equals(state)) {
            expertCompany.setStatus(ENABLE);
            addCallbackParam("msg", "启用成功！");
        }
        service.update(expertCompany);

    }

    public DataGridModel<ExpertCompany> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<ExpertCompany>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 5834872773407058979L;

                @Override
                public List<ExpertCompany> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    // 请求的是选择回避单位的列表页面，需要去除该专家已经回避的单位
                    if (StringUtil.isNotBlank(type)) {
                    	conditionSql += " and type = '" + type + "'";
                    }
                    String expertguid = getViewData("expertguid");
                    List<ExpertCompany> list = new ArrayList<>();
                    if (StringUtil.isNotBlank(expertguid)) {
                        list = service.findListRemAlready(
                                ListGenerator.generateSql("Expert_Company", conditionSql, sortField, sortOrder),
                                expertguid, first, pageSize, conditionList.toArray());
                        int count = service.findListRemAlready(
                                ListGenerator.generateSql("Expert_Company", conditionSql, sortField, sortOrder),
                                expertguid, conditionList.toArray()).size();
                        this.setRowCount(count);
                    }
                    else {
                        String sign = getRequestParameter("sign");
                        list = service.findListRemDel(
                                ListGenerator.generateSql("Expert_Company", conditionSql, sortField, sortOrder), sign,
                                first, pageSize, conditionList.toArray());
                        int count = service.findListRemDel(
                                ListGenerator.generateSql("Expert_Company", conditionSql, sortField, sortOrder), sign,
                                conditionList.toArray()).size();
                        this.setRowCount(count);
                    }
                    // 重新渲染省为省市区拼接的内容
                    if (list != null && !list.isEmpty()) {
                        for (ExpertCompany expertCompany : list) {
                            // 省
                            String province = codeItemService.getItemTextByCodeName("行政区划国标",
                                    expertCompany.getProvince());
                            // 市 当为县或者市辖区的时候不需要显示市
                            String city = codeItemService.getItemTextByCodeName("行政区划国标", expertCompany.getCity());
                            city = COUNTY.equals(city) ? "" : city;
                            city = CITY_AREA.equals(city) ? "" : city;
                            // 区
                            String country = codeItemService.getItemTextByCodeName("行政区划国标",
                                    expertCompany.getCountry());
                            expertCompany.setProvince(province + city + country);
                        }
                    }
                    return list;
                }

            };
        }
        return model;
    }

    
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ExpertCompany getDataBean() {
        if (dataBean == null) {
            dataBean = new ExpertCompany();
        }
        return dataBean;
    }

    public void setDataBean(ExpertCompany dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("companyname,type,province,city", "单位名称,单位类型,省,市");
        }
        return exportModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getTypeModel() {
        if (typeModel == null) {
            typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "专家库_单位类型", null, false));
        }
        return this.typeModel;
    }

}
