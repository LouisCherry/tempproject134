package com.epoint.basic.auditsp.dantiinfo.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditsp.dantiinfo.api.IJnDantiinfoService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 单体库
 */
@RestController("dantilibrarylistaction")
@Scope("request")
public class DantiLibraryListAction extends BaseController
{
    private static final long serialVersionUID = 7292762752363027253L;
    @Autowired
    private ICodeItemsService iCodeItemsService;

    @Autowired
    private IJnDantiinfoService dantiinfoService;

    /**
     * 单体信息表
     */
    private DantiInfo dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<DantiInfo> model;

    /**
     * 工程类别下拉列表model
     */
    private List<SelectItem> gclbModel = null;

    /**
     * 是否赋码下拉列表model
     */
    private List<SelectItem> isfmModel = null;

    private String itemcode;

    private String dantiname;

    private String dtbm;

    private String gclb;

    private String isfm;

    public void pageLoad() {

    }

    public DataGridModel<DantiInfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<DantiInfo>()
            {
                private static final long serialVersionUID = -6455344876944357468L;

                @Override
                public List<DantiInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    Record record = new Record();
                    if (StringUtil.isNotBlank(itemcode)) {
                        record.set("itemcode", itemcode);
                    }
                    if (StringUtil.isNotBlank(dantiname)) {
                        record.set("dantiname", dantiname);
                    }
                    if (StringUtil.isNotBlank(dtbm)) {
                        record.set("dtbm", dtbm);
                    }
                    if (StringUtil.isNotBlank(gclb)) {
                        record.set("gclb", gclb);
                    }
                    if (StringUtil.isNotBlank(isfm)) {
                        record.set("isfm", isfm);
                    }
                    PageData<DantiInfo> pageData = dantiinfoService.pageDantiList(record, first, pageSize, sortField,
                            sortOrder);
                    List<DantiInfo> list = pageData.getList();
                    int count = pageData.getRowCount();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public List<SelectItem> getGclbModel() {
        if (gclbModel == null) {
            gclbModel = DataUtil.convertMap2ComboBox(CodeModalFactory.factory("下拉列表", "国标_工程类别", null, true));
        }
        return this.gclbModel;
    }

    public List<SelectItem> getIsfmModel() {
        if (isfmModel == null) {
            isfmModel = DataUtil.convertMap2ComboBox(CodeModalFactory.factory("下拉列表", "是否", null, true));
        }
        return this.isfmModel;
    }

    public DantiInfo getDataBean() {
        if (dataBean == null) {
            dataBean = new DantiInfo();
        }
        return dataBean;
    }

    public void setDataBean(DantiInfo dataBean) {
        this.dataBean = dataBean;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getDantiname() {
        return dantiname;
    }

    public void setDantiname(String dantiname) {
        this.dantiname = dantiname;
    }

    public String getDtbm() {
        return dtbm;
    }

    public void setDtbm(String dtbm) {
        this.dtbm = dtbm;
    }

    public String getGclb() {
        return gclb;
    }

    public void setGclb(String gclb) {
        this.gclb = gclb;
    }

    public String getIsfm() {
        return isfm;
    }

    public void setIsfm(String isfm) {
        this.isfm = isfm;
    }

    public String getNameByCode(String code) {
        if (StringUtil.isBlank(code)) {
            return "";
        }
        String result = "";
        CodeItems codeitem = iCodeItemsService.getCodeItemByCodeName("国标_工程类别", String.valueOf(code));
        if (codeitem != null) {
            result += codeitem.getItemText();
            String codefl = code.substring(0, 2).replaceAll("0", "");
            CodeItems codeitemfl = iCodeItemsService.getCodeItemByCodeName("国标_工程行业分类", String.valueOf(codefl));
            if (codeitemfl != null) {
                result = codeitemfl.getItemText() + "-" + result;
            }
        }
        return result;
    }

}
