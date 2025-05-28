package com.epoint.xmz.onlinetaskconfig.action;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.onlinetaskconfig.api.entity.OnlinetaskConfig;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.onlinetaskconfig.api.IOnlinetaskConfigService;


/**
 * 居民办事配置list页面对应的后台
 *
 * @author RaoShaoliang
 * @version [版本号, 2023-10-17 15:38:09]
 */
@RestController("onlinetaskconfiglistaction")
@Scope("request")
public class OnlinetaskConfigListAction extends BaseController {
    @Autowired
    private IOnlinetaskConfigService service;

    /**
     * 居民办事配置实体对象
     */
    private OnlinetaskConfig dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<OnlinetaskConfig> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * 所属分类下拉列表model
     */
    private List<SelectItem> kindModel = null;
    /**
     * 所属b部门下拉列表model
     */
    private List<SelectItem> ounameModel = null;

    private String kind;
    private String ouname;

    public void pageLoad() {
        dataBean = new OnlinetaskConfig();
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

    public DataGridModel<OnlinetaskConfig> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<OnlinetaskConfig>() {
                @Override
                public List<OnlinetaskConfig> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(kind)) {
                        sql.eq("kind", kind);
                    }
                    if (StringUtil.isNotBlank(ouname)) {
                        sql.eq("ouguid", ouname);
                    }
                    SQLManageUtil sqlManageUtil=new SQLManageUtil();
                    String s = sqlManageUtil.buildPatchSql(sql.getMap());
                    conditionSql = conditionSql+s;
                    List<OnlinetaskConfig> list = service.findList(
                            ListGenerator.generateSql("onlinetask_config", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.countOnlinetaskConfig(ListGenerator.generateSql("onlinetask_config", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }
            };
        }
        return model;
    }


    public OnlinetaskConfig getDataBean() {
        if (dataBean == null) {
            dataBean = new OnlinetaskConfig();
        }
        return dataBean;
    }

    public void setDataBean(OnlinetaskConfig dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    public List<SelectItem> getKindModel() {
        if (kindModel == null) {
            kindModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "居民办事分类", null, true));
        }
        return this.kindModel;
    }

    public List<SelectItem> getOunameModel() {
        if (ounameModel == null) {
            ounameModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "居民办事部门", null, true));
        }
        return this.ounameModel;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
    public String getOuname() {
        return ouname;
    }

    public void setOuname(String ouname) {
        this.ouname = ouname;
    }
}
