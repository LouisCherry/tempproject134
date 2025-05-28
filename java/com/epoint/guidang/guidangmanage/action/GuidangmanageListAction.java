package com.epoint.guidang.guidangmanage.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.guidang.guidangmanage.api.IGuidangmanageService;
import com.epoint.guidang.guidangmanage.entity.Guidangmanage;

/**
 * 归档管理list页面对应的后台
 * 
 * @author chengninghua
 * @version [版本号, 2017-12-15 15:11:49]
 */
@RestController("guidangmanagelistaction")
@Scope("request")
public class GuidangmanageListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -8112686148513398204L;

    @Autowired
    private IGuidangmanageService service;

    /**
     * 归档管理实体对象
     */
    private Guidangmanage dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    private CommonDao dao = CommonDao.getInstance();

    private List<Record> XiaQuModel = null;
    private List<Record> ouModel = null;

    public String xiaqucode;
    public String taskouguid;

    public String getTaskouguid() {
        return taskouguid;
    }

    public void setTaskouguid(String taskouguid) {
        this.taskouguid = taskouguid;
    }

    public String getXiaqucode() {
        return xiaqucode;
    }

    public void setXiaqucode(String xiaqucode) {
        this.xiaqucode = xiaqucode;
    }
    @Override
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

    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>()
            {
                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String sql = "SELECT A.RowGuid,a.serviceip,a.saveyear,b.TaskName,b.OUNAME,c.XiaQuName,a.areacode FROM "
                            + "guidangmanage a INNER JOIN audit_task b ON a.taskid = b.TASK_ID AND "
                            + "b.IS_EDITAFTERIMPORT = 1 AND IFNULL(b.IS_HISTORY, 0) = 0 "
                            + "INNER JOIN audit_orga_area c ON c.xiaqucode=a.areacode";
                    if (StringUtil.isNotBlank(xiaqucode)) {
                        sql += " where a.areacode ='" + xiaqucode + "'";
                    }
                    if (StringUtil.isNotBlank(taskouguid)) {
                        sql += " and b.ouguid ='" + taskouguid + "'";
                    }
                    sql += " ORDER BY areacode asc";
                    int count = dao.findList(sql, Record.class).size();
                    sql += " limit " + first + "," + pageSize + ";";
                    List<Record> list = dao.findList(sql, Record.class);
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public Guidangmanage getDataBean() {
        if (dataBean == null) {
            dataBean = new Guidangmanage();
        }
        return dataBean;
    }

    public void setDataBean(Guidangmanage dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("XiaQuName,OUNAME,TaskName,serviceip,saveyear", "辖区,部门名称,事项名称,保存地址,保存年限");
        }
        return exportModel;
    }

    public List<Record> getXiaQuModel() {
        if (XiaQuModel == null) {
            String sql = "select XiaQuName,XiaQuCode from audit_orga_area  ORDER BY CityLevel ASC";
            XiaQuModel = dao.findList(sql, Record.class);
        }
        return this.XiaQuModel;
    }

    public List<Record> getouModel() {
        if (xiaqucode != null) {
            String sql = "select ouname,a.ouguid from frame_ou a inner join frame_ou_extendinfo b on "
                    + "a.OUGUID=b.OUGUID where IFNULL(oucode,'')!='' and areacode=?1 order by ORDERNUMBER desc";
            ouModel = dao.findList(sql, Record.class, xiaqucode);
        }
        return this.ouModel;
    }

}
