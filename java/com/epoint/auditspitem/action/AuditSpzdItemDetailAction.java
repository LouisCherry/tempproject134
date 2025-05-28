package com.epoint.auditspitem.action;

import com.epoint.auditspitem.api.IAuditSpItemService;
import com.epoint.auditspitem.api.entity.AuditSpItem;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.majoritem.itemplan.api.IItemPlanService;
import com.epoint.majoritem.itemplan.api.entity.ItemPlan;
import com.epoint.majoritem.itemzczctype.api.IItemZczcTypeService;
import com.epoint.majoritem.itemzczctype.api.entity.ItemZczcType;
import com.epoint.majoritem.itmeservelog.api.IItmeServelogService;
import com.epoint.majoritem.itmeservelog.api.entity.ItmeServelog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 项目信息表详情页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:30:48]
 */

@RestController("auditspzditemdetailaction")
@Scope("request")
public class AuditSpzdItemDetailAction extends BaseController
{
    @Autowired
    private IAuditSpItemService service;

    /**
     * 项目信息表实体对象
     */
    private AuditSpItem dataBean = null;

    private List<SelectItem> areCodeModel;

    private String jurisdictions;

    private String projecttype;
    private String constructdate;

    /**
     * 表格控件model
     */
    private DataGridModel<ItemPlan> model;
    /**
     * 表格控件model
     */
    private DataGridModel<ItemZczcType> model2;

    /**
     * 表格控件model
     */
    private DataGridModel<ItmeServelog> model3;

    @Autowired
    private IItemPlanService iItemPlanService;

    @Autowired
    private IItemZczcTypeService iItemZczcTypeService;

    @Autowired
    private IItmeServelogService iItmeServelogService;

    private List<SelectItem> itemyearModel;
    private List<SelectItem> itemlevelModel;
    private List<SelectItem> itemtypeModel;
    private String shenglevel="0";
    private String shilevel="0";
    private String xianlevel="0";

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditSpItem();
        }
        if (StringUtil.isBlank(getViewData("itemguid"))) {
            addViewData("itemguid", guid);
        }
        jurisdictions = dataBean.get("jurisdictions");
        projecttype = dataBean.get("projecttype");
        if (StringUtil.isNotBlank(dataBean.getBuildstarttime()) && StringUtil.isNotBlank(dataBean.getBuildendtime())) {
            constructdate = EpointDateUtil.convertDate2String(dataBean.getBuildstarttime()) + "~"
                    + EpointDateUtil.convertDate2String(dataBean.getBuildendtime());
        }
        else {
            constructdate = "";

        }
        if (dataBean.getStr("itemlevel").contains("00101")){
            shenglevel="1";
        }
        if (dataBean.getStr("itemlevel").contains("00102")){
            shilevel="1";
        }
        if (dataBean.getStr("itemlevel").contains("00103")){
            xianlevel="1";
        }
        addCallbackParam("shenglevel",shenglevel);
        addCallbackParam("shilevel",shilevel);
        addCallbackParam("xianlevel",xianlevel);
        addCallbackParam("constructdate",constructdate);
    }

    public AuditSpItem getDataBean() {
        return dataBean;
    }

    public List<SelectItem> getAreCodeModel() {
        if (areCodeModel == null) {
            areCodeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "阶段配置辖区", null, false));
        }
        return this.areCodeModel;
    }
    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            iItemPlanService.deleteByGuid(sel);
        }
        addCallbackParam("msg", l("成功删除！"));
    }



    public DataGridModel<ItemPlan> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<ItemPlan>() {

                @Override
                public List<ItemPlan> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    conditionSql+=" and itemguid = '"+getViewData("itemguid")+"' ";
                    List<ItemPlan> list = iItemPlanService.findList(
                            ListGenerator.generateSql("item_plan", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = iItemPlanService.countItemPlan(ListGenerator.generateSql("item_plan", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    /**
     * 删除选定
     */
    public void deleteSelect2() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", l("成功删除！"));
    }

    public DataGridModel<ItemZczcType> getDataGridData2() {
        // 获得表格对象
        if (model2 == null) {
            model2 = new DataGridModel<ItemZczcType>() {

                @Override
                public List<ItemZczcType> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    conditionSql+=" and itemguid = '"+getViewData("itemguid")+"' ";
                    List<ItemZczcType> list = iItemZczcTypeService.findList(
                            ListGenerator.generateSql("item_zczc_type", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = iItemZczcTypeService.countItemZczcType(ListGenerator.generateSql("item_zczc_type", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model2;
    }

    /**
     * 删除选定
     */
    public void deleteSelect3() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", l("成功删除！"));
    }

    public DataGridModel<ItmeServelog> getDataGridData3() {
        // 获得表格对象
        if (model3 == null) {
            model3 = new DataGridModel<ItmeServelog>() {

                @Override
                public List<ItmeServelog> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    conditionSql+=" and itemguid = '"+getViewData("itemguid")+"' ";
                    List<ItmeServelog> list = iItmeServelogService.findList(
                            ListGenerator.generateSql("itme_servelog", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = iItmeServelogService.countItmeServelog(ListGenerator.generateSql("itme_servelog", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model3;
    }




    public void setDataBean(AuditSpItem dataBean) {
        this.dataBean = dataBean;
    }


    public List<SelectItem> itemyearModel() {
        if (itemyearModel == null) {
            itemyearModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "重点项目年份", null, false));
        }
        return this.itemyearModel;
    }


    public List<SelectItem> itemlevelModel() {
        if (itemlevelModel == null) {
            itemlevelModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "重点项目项目等级", null, false));
        }
        return this.itemlevelModel;
    }

    public List<SelectItem> itemtypeModel() {
        if (itemtypeModel == null) {
            itemtypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "重点项目项目类型", null, false));
        }
        return this.itemtypeModel;
    }

    public String getJurisdictions() {
        return jurisdictions;
    }

    public void setJurisdictions(String jurisdictions) {
        this.jurisdictions = jurisdictions;
    }

    public String getProjecttype() {
        return projecttype;
    }

    public void setProjecttype(String projecttype) {
        this.projecttype = projecttype;
    }

    public String getConstructdate() {
        return constructdate;
    }

    public void setConstructdate(String constructdate) {
        this.constructdate = constructdate;
    }

}
