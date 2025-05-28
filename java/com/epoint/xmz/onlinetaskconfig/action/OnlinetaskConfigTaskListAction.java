package com.epoint.xmz.onlinetaskconfig.action;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.onlinetaskconfig.api.IOnlinetaskConfigService;
import com.epoint.xmz.onlinetaskconfig.api.entity.OnlinetaskConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 居民办事配置list页面对应的后台
 *
 * @author RaoShaoliang
 * @version [版本号, 2023-10-17 15:38:09]
 */
@RestController("onlinetaskconfigtasklistaction")
@Scope("request")
public class OnlinetaskConfigTaskListAction extends BaseController {
    @Autowired
    private IOnlinetaskConfigService service;

    /**
     * 居民办事配置实体对象
     */
    private OnlinetaskConfig dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;

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
    private String configguid;

    public void pageLoad() {
        configguid = getRequestParameter("configguid");
        addCallbackParam("configguid",configguid);
        dataBean = new OnlinetaskConfig();
    }


    /**
     * 删除选定
     */
    public void deleteSelect() {
        CommonDao commonDao=CommonDao.getInstance();
        String sql ="delete from onlinetask_config_task where taskguid = ? and configguid = ?";
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            commonDao.execute(sql,sel,configguid);
        }
        addCallbackParam("msg", l("成功删除！"));
    }

    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>() {
                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    CommonDao commonDao = CommonDao.getInstance();
                    String sql = "select * from onlinetask_config_task where configguid = ? ";
                    List<Record> list = commonDao.findList(sql, first, pageSize, Record.class,configguid);
                    String sql3 ="select * from audit_task where rowguid = ? ";
                    for (Record record : list) {
                        AuditTask auditTask = commonDao.find(sql3, AuditTask.class, record.getStr("taskguid"));
                        record.set("taskname",auditTask.getTaskname());
                    }
                    String sql2 ="select count(*) from onlinetask_config_task where configguid = ?";
                    Integer queryInt = commonDao.queryInt(sql2, configguid);
                    PageData<Record> rtnlist=new PageData<>();
                    rtnlist.setList(list);
                    rtnlist.setRowCount(queryInt);
                    this.setRowCount(rtnlist.getRowCount());
                    return rtnlist.getList();
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
