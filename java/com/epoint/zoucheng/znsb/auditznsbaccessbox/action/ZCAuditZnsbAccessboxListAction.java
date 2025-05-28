package com.epoint.zoucheng.znsb.auditznsbaccessbox.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditqueue.auditznsbaccessbox.domain.AuditZnsbAccessbox;
import com.epoint.basic.auditqueue.auditznsbaccessbox.inter.IAuditZnsbAccessbox;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.zoucheng.znsb.auditznsbaccessbox.inter.IZCAuditZnsbAccessbox;

/**
 * 智能化存取盒表list页面对应的后台
 * 
 * @author 54201
 * @version [版本号, 2019-02-20 14:45:09]
 */
@RestController("zcauditznsbaccessboxlistaction")
@Scope("request")
public class ZCAuditZnsbAccessboxListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 2556270353196272535L;

    @Autowired
    private IZCAuditZnsbAccessbox service;

    /**
     * 智能化存取盒表实体对象
     */
    private AuditZnsbAccessbox dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditZnsbAccessbox> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;
    
    private String cabinetguid = "";

    /**
    * 存取盒状态下拉列表model
    */
    private List<SelectItem> boxstatusModel = null;

    public void pageLoad() {
        cabinetguid = getRequestParameter("cabinetguid");
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

    public DataGridModel<AuditZnsbAccessbox> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditZnsbAccessbox>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = -5716002137982609896L;

                @Override
                public List<AuditZnsbAccessbox> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    if (StringUtil.isNotBlank(dataBean.getBoxno())) {
                        conditionSql+=" and boxno like '%"+dataBean.getBoxno()+"%'";
                    }
                    if (StringUtil.isNotBlank(dataBean.getBoxstatus())) {
                        conditionSql+=" and boxstatus='"+dataBean.getBoxstatus()+"' ";
                    }
                    if (StringUtil.isNotBlank(cabinetguid)) {
                        conditionSql+=" and cabinetguid='"+cabinetguid+"' ";
                    }
                    if (StringUtil.isBlank(sortField)) {
                        sortField="boxno";
                        sortOrder="asc";
                    }
                    List<AuditZnsbAccessbox> list = service.findList(
                            ListGenerator.generateSql("Audit_Znsb_AccessBox", conditionSql, sortField, sortOrder),
                            first, pageSize, conditionList.toArray());
                    int count = service.findList(
                            ListGenerator.generateSql("Audit_Znsb_AccessBox", conditionSql, sortField, sortOrder),
                            conditionList.toArray()).size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public AuditZnsbAccessbox getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditZnsbAccessbox();
        }
        return dataBean;
    }

    public void setDataBean(AuditZnsbAccessbox dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getBoxstatusModel() {
        if (boxstatusModel == null) {
            boxstatusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "存取盒状态", null, true));
        }
        return this.boxstatusModel;
    }
    
    /**
     * 初始化模块数据
     */
    public void initBox() {
      //删除历史数据
        service.deletebyCabinetGuid(cabinetguid);
        //初始化数据
        String msg = service.initBox(cabinetguid).getResult();
        if("success".equals(msg)){
            addCallbackParam("msg", "初始化数据成功！");
        }else{
            addCallbackParam("msg", msg);
        }
    }

}
