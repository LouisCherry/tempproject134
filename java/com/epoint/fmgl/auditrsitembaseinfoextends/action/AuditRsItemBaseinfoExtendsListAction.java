package com.epoint.fmgl.auditrsitembaseinfoextends.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.fmgl.auditrsitembaseinfoextends.api.IAuditRsItemBaseinfoExtendsService;
import com.epoint.fmgl.auditrsitembaseinfoextends.api.entity.AuditRsItemBaseinfoExtends;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.jnzwdt.tzzxjgpt.rest.GetItemCodeUtil2;

/**
 * 赋码项目基本信息表list页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2020-09-23 15:19:11]
 */
@RestController("auditrsitembaseinfoextendslistaction")
@Scope("request")
public class AuditRsItemBaseinfoExtendsListAction extends BaseController
{
    @Autowired
    private IAuditRsItemBaseinfoExtendsService service;

    /**
     * 赋码项目基本信息表实体对象
     */
    private AuditRsItemBaseinfoExtends dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditRsItemBaseinfoExtends> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
    * 状态下拉列表model
    */
    private List<SelectItem> statusModel = null;
    String creditcode = null;

    public void pageLoad() {
        creditcode = getRequestParameter("creditcode");
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
    
    public void get(String id) {
        AuditRsItemBaseinfoExtends itemBaseinfoExtend =service.find(id);
        String itemcode = itemBaseinfoExtend.getItemcode();
        if(StringUtil.isNotBlank(itemcode)&&!"赋码审核中".equals(itemcode)&!"赋码失败（驳回）".equals(itemcode)&&!"赋码失败（作废）".equals(itemcode)) {
            addCallbackParam("msg", "已存在赋码，不可再去获取！");
            return;
        }
        String resultdata = GetItemCodeUtil2.getItemCode(id);
        JSONObject jsondata = JSONObject.parseObject(resultdata);
        String code = jsondata.getString("code");
        if("1".equals(code)) {
            String text = jsondata.getString("text");
            addCallbackParam("msg", text);
        }else {
            addCallbackParam("msg", "获取赋码失败！");
        }
    }

    public DataGridModel<AuditRsItemBaseinfoExtends> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditRsItemBaseinfoExtends>()
            {

                @Override
                public List<AuditRsItemBaseinfoExtends> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();

                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    if (StringUtil.isNotBlank(creditcode)) {
                        conditionSql+=" and lerepcertno='"+creditcode+"'";
                    }
                    List<AuditRsItemBaseinfoExtends> list = service.findList(ListGenerator
                            .generateSql("audit_rs_item_baseinfo_extends", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray());
                    int count = service.countAuditRsItemBaseinfoExtends(
                            ListGenerator.generateSql("audit_rs_item_baseinfo_extends", conditionSql),
                            conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public AuditRsItemBaseinfoExtends getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditRsItemBaseinfoExtends();
        }
        return dataBean;
    }

    public void setDataBean(AuditRsItemBaseinfoExtends dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    public List<SelectItem> getStatusModel() {
        if (statusModel == null) {
            statusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_状态", null, true));
        }
        return this.statusModel;
    }

}
