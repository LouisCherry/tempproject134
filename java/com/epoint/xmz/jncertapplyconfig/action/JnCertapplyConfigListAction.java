package com.epoint.xmz.jncertapplyconfig.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.xmz.jncertapplyconfig.api.IJnCertapplyConfigService;
import com.epoint.xmz.jncertapplyconfig.api.entity.JnCertapplyConfig;

/**
 * 证照申办配置表list页面对应的后台
 * 
 * @author future
 * @version [版本号, 2023-03-21 15:06:55]
 */
@RestController("jncertapplyconfiglistaction")
@Scope("request")
public class JnCertapplyConfigListAction extends BaseController
{
    @Autowired
    private IJnCertapplyConfigService service;

    /**
     * 证照申办配置表实体对象
     */
    private JnCertapplyConfig dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<JnCertapplyConfig> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
    * 申请人类型单选按钮组model
    */
    private List<SelectItem> applyuser_typeModel = null;
    /**
     * 证照分类下拉列表model
     */
    private List<SelectItem> cert_typeModel = null;
    /**
     * 标签分类复选框组model
     */
    private List<SelectItem> tagtypeModel = null;
    /**
     * 跳转类型单选按钮组model
     */
    private List<SelectItem> target_typeModel = null;

    public void pageLoad() {
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

    public DataGridModel<JnCertapplyConfig> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<JnCertapplyConfig>()
            {

                @Override
                public List<JnCertapplyConfig> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);

                    // 证照类型
                    if (StringUtil.isNotBlank(dataBean.getCert_type())) {
                        conditionSql = conditionSql + "  and cert_type = ?";
                        conditionList.add(dataBean.getCert_type());
                    }

                    if (StringUtil.isNotBlank(dataBean.getCertname())) {
                        conditionSql = conditionSql + "  and certname like ?";
                        conditionList.add("%"+dataBean.getCertname()+"%");
                    }

                    conditionSql = conditionSql+"  and belongxiaqucode = ?";
                    conditionList.add(ZwfwUserSession.getInstance().getAreaCode());

                    List<JnCertapplyConfig> list = service.findList(
                            ListGenerator.generateSql("jn_certapply_config", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray());
                    int count = service.countJnCertapplyConfig(
                            ListGenerator.generateSql("jn_certapply_config", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public JnCertapplyConfig getDataBean() {
        if (dataBean == null) {
            dataBean = new JnCertapplyConfig();
        }
        return dataBean;
    }

    public void setDataBean(JnCertapplyConfig dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel(
                    "applyuser_type,apply_condition,banli_free,certname,cert_type,contact_phone,shouli_address,tagtype,target_type,target_url,task_relation",
                    "申请人类型,申报条件,办理费用,证照名称,证照分类,咨询电话,受理机构地址,标签分类,跳转类型,跳转地址,关联事项");
        }
        return exportModel;
    }

    public List<SelectItem> getApplyuser_typeModel() {
        if (applyuser_typeModel == null) {
            applyuser_typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "申请人类型", null, true));
        }
        return this.applyuser_typeModel;
    }

    public List<SelectItem> getCert_typeModel() {
        if (cert_typeModel == null) {
            cert_typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "证照配置分类", null, true));
        }
        return this.cert_typeModel;
    }

    public List<SelectItem> getTagtypeModel() {
        if (tagtypeModel == null) {
            tagtypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("复选框组", "证照配置标签维护", null, true));
        }
        return this.tagtypeModel;
    }

    public List<SelectItem> getTarget_typeModel() {
        if (target_typeModel == null) {
            target_typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "证照配置跳转类型", null, true));
        }
        return this.target_typeModel;
    }

}
