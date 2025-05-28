package com.epoint.cs.auditepidemiclog.action;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.cs.auditepidemiclog.api.entity.AuditEpidemicLog;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.cs.auditepidemiclog.api.IAuditEpidemicLogService;

/**
 * 访客登记list页面对应的后台
 * 
 * @author Mercury
 * @version [版本号, 2020-02-02 19:35:15]
 */
@RestController("auditepidemicloglistaction")
@Scope("request")
public class AuditEpidemicLogListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 7988052430941229804L;

    @Autowired
    private IAuditEpidemicLogService service;

    /**
     * 访客登记实体对象
     */
    private AuditEpidemicLog dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditEpidemicLog> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
    * 登记状态单选按钮组model
    */
    private List<SelectItem> statusModel = null;

    private String search_ENTRYTIME;
    private String search_EXITTIME;

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

    public DataGridModel<AuditEpidemicLog> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditEpidemicLog>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = -6868733384231213L;

                @Override
                public List<AuditEpidemicLog> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(dataBean.getName())) {
                        sql.like("name", dataBean.getName());
                    }
                    if (StringUtil.isNotBlank(dataBean.getId())) {
                        sql.like("id", dataBean.getId());
                    }
                    if (StringUtil.isNotBlank(dataBean.getAddress())) {
                        sql.like("address", dataBean.getAddress());
                    }
                    if (StringUtil.isNotBlank(dataBean.getCentername())) {
                        sql.like("centername", dataBean.getCentername());
                    }
                    if (StringUtil.isNotBlank(dataBean.getTel())) {
                        sql.like("tel", dataBean.getTel());
                    }
                    if (StringUtil.isNotBlank(dataBean.getStatus())) {
                        if ("1".equals(dataBean.getStatus())) {
                            sql.eq("status", "1");
                        }
                        if ("2".equals(dataBean.getStatus())) {
                            sql.eq("status", "2");
                        }
                    }
                    else {

                    }
                    if (StringUtil.isNotBlank(dataBean.getEntrytime())) {
                        if (StringUtil.isNotBlank(dataBean.getExittime())) {
                            sql.between("entrytime", dataBean.getEntrytime(), dataBean.getExittime());
                            sql.between("exittime", dataBean.getEntrytime(), dataBean.getExittime());
                        }
                        else {
                            sql.ge("entrytime", dataBean.getEntrytime());
                        }
                    }
                    else if (StringUtil.isNotBlank(dataBean.getExittime())) {
                        sql.le("exittime", dataBean.getExittime());
                    }
                    PageData<AuditEpidemicLog> pageData = service
                            .getAuditEpidemicLogPageData(sql.getMap(), first, pageSize, sortField, sortOrder)
                            .getResult();
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }

            };
        }
        return model;
    }

    public JSONObject getEchartsData() {

        String[] arr = getBeforeSevenDay();
        int count = 0;
        JSONObject Jsondata = new JSONObject();
        JSONArray handletypeJson = new JSONArray();

        for (int i = 0; i < arr.length; i++) {
            JSONObject handleJson = new JSONObject();
            handleJson.put("name", arr[i]);
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("entrytime", arr[i]);
            count = service.getAuditEpidemicLogPageData(sql.getMap(), 0, 0, "", "").getResult().getRowCount();
            handleJson.put("value", count);
            handletypeJson.add(handleJson);
            Jsondata.put("handletype", handletypeJson);
        }

        return Jsondata;

    }

    public AuditEpidemicLog getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditEpidemicLog();
        }
        return dataBean;
    }

    public void setDataBean(AuditEpidemicLog dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("name,id,address,tel,entrytime,exittime,temperature,status,centername,",
                    "姓名,身份证号,住址,联系电话,进厅时间,出厅时间,体温,登记状态,中心名称");
        }
        return exportModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getStatusModel() {
        if (statusModel == null) {
            statusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "登记状态", null, true));
        }
        return this.statusModel;
    }

    //获取前七天日期
    public static String[] getBeforeSevenDay() {
        String[] arr = new String[7];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = null;
        for (int i = 0; i < 7; i++) {
            c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, -i - 1);
            arr[6 - i] = sdf.format(c.getTime());

        }
        return arr;
    }

    public String getSearch_ENTRYTIME() {
        return search_ENTRYTIME;
    }

    public void setSearch_ENTRYTIME(String search_ENTRYTIME) {
        this.search_ENTRYTIME = search_ENTRYTIME;
    }

    public String getSearch_EXITTIME() {
        return search_EXITTIME;
    }

    public void setSearch_EXITTIME(String search_EXITTIME) {
        this.search_EXITTIME = search_EXITTIME;
    }

}
