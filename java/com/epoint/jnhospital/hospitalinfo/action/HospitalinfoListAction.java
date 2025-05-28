package com.epoint.jnhospital.hospitalinfo.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.jnhospital.hospitalinfo.api.IHospitalinfoService;
import com.epoint.jnhospital.hospitalinfo.api.entity.Hospitalinfo;

/**
 * 定点医院名单list页面对应的后台
 * 
 * @author JFei
 * @version [版本号, 2019-09-05 11:21:46]
 */
@RestController("hospitalinfolistaction")
@Scope("request")
public class HospitalinfoListAction extends BaseController
{
    @Autowired
    private IHospitalinfoService service;

    /**
     * 定点医院名单实体对象
     */
    private Hospitalinfo dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<Hospitalinfo> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

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

    public DataGridModel<Hospitalinfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Hospitalinfo>()
            {

                @Override
                public List<Hospitalinfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    List<Hospitalinfo> list = service.findList(
                            ListGenerator.generateSql("hospitalinfo", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray());
                    int count = service
                            .findList(ListGenerator.generateSql("hospitalinfo", conditionSql, sortField, sortOrder),
                                    conditionList.toArray())
                            .size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public Hospitalinfo getDataBean() {
        if (dataBean == null) {
            dataBean = new Hospitalinfo();
        }
        return dataBean;
    }

    public void setDataBean(Hospitalinfo dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

}
