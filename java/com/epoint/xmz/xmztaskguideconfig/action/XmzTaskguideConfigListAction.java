package com.epoint.xmz.xmztaskguideconfig.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.xmz.xmztaskguideconfig.api.IXmzTaskguideConfigService;
import com.epoint.xmz.xmztaskguideconfig.api.entity.XmzTaskguideConfig;

/**
 * 事项指南配置表list页面对应的后台
 * 
 * @author xczheng0314
 * @version [版本号, 2023-03-21 11:38:56]
 */
@RestController("xmztaskguideconfiglistaction")
@Scope("request")
public class XmzTaskguideConfigListAction extends BaseController
{
    @Autowired
    private IXmzTaskguideConfigService service;

    /**
     * 事项指南配置表实体对象
     */
    private XmzTaskguideConfig dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<XmzTaskguideConfig> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;
    @Autowired
    private IAuditOrgaArea iAuditOrgaArea;
    private String areacode;
    private List<AuditOrgaArea> areaList ;
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
        addCallbackParam("msg", l("成功删除！"));
    }

    public DataGridModel<XmzTaskguideConfig> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<XmzTaskguideConfig>()
            {

                @Override
                public List<XmzTaskguideConfig> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(areacode)) {
                        sql.eq("areacode", areacode);
                    }
                    sql.setOrderDesc("createdate");
                    SQLManageUtil sqlManageUtil=new SQLManageUtil();
                    String s = sqlManageUtil.buildPatchSql(sql.getMap());
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    List<XmzTaskguideConfig> list = service.findList(
                            ListGenerator.generateSql("xmz_taskguide_config", conditionSql+s, sortField, sortOrder),
                            first, pageSize, conditionList.toArray());
                    if (list!=null && !list.isEmpty()) {
                        for (XmzTaskguideConfig xmzTaskguideConfig : list) {
                            String xiaquname = iAuditOrgaArea.getAreaByAreacode(xmzTaskguideConfig.getAreacode()).getResult().getXiaquname();
                            xmzTaskguideConfig.setAreacode(xiaquname);
                        }
                    }
                    int count = service.countXmzTaskguideConfig(
                            ListGenerator.generateSql("xmz_taskguide_config", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }
    public List<AuditOrgaArea> getAreaList() {
        if (areaList == null) {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.in("CityLevel", "'1','2'");
            areaList = iAuditOrgaArea.selectAuditAreaList(sql.getMap()).getResult();
        }
        return areaList;
    }

    public XmzTaskguideConfig getDataBean() {
        if (dataBean == null) {
            dataBean = new XmzTaskguideConfig();
        }
        return dataBean;
    }

    public void setDataBean(XmzTaskguideConfig dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

}
