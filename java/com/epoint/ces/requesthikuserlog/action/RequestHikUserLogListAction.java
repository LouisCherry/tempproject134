package com.epoint.ces.requesthikuserlog.action;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.epoint.ces.requesthiklog.api.IRequestHikLogService;
import com.epoint.ces.requesthiklog.api.entity.RequestHikLog;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.ces.requesthikuserlog.api.entity.RequestHikUserLog;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.ces.requesthikuserlog.api.IRequestHikUserLogService;


/**
 * 请求海康日志信息人员考勤记录表list页面对应的后台
 *
 * @author shun
 * @version [版本号, 2021-11-22 14:32:24]
 */
@RestController("requesthikuserloglistaction")
@Scope("request")
public class RequestHikUserLogListAction extends BaseController {
    @Autowired
    private IRequestHikUserLogService service;

    /**
     * 请求海康日志信息人员考勤记录表实体对象
     */
    private RequestHikUserLog dataBean;

    private String rxh;

    public String getRxh() {
        return rxh;
    }

    public void setRxh(String rxh) {
        this.rxh = rxh;
    }

    //请求时间
    private String ReqDate;

    public String getReqDate() {
        return ReqDate;
    }

    public void setReqDate(String reqDate) {
        ReqDate = reqDate;
    }

    public String getRespDate() {
        return RespDate;
    }

    public void setRespDate(String respDate) {
        RespDate = respDate;
    }

    //响应时间
    private String RespDate;

    private String reqdateStr;

    private String reqdateEnd;

    public String getReqdateStr() {
        return reqdateStr;
    }

    public void setReqdateStr(String reqdateStr) {
        this.reqdateStr = reqdateStr;
    }

    public String getReqdateEnd() {
        return reqdateEnd;
    }

    public void setReqdateEnd(String reqdateEnd) {
        this.reqdateEnd = reqdateEnd;
    }

    @Autowired
    private IRequestHikLogService requestHikLogService;

    /**
     * 表格控件model
     */
    private DataGridModel<RequestHikUserLog> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;


    public void pageLoad() {

        String guid = getRequestParameter("guid");
        if (StringUtil.isNotBlank(guid)){
            RequestHikLog requestHikLog = requestHikLogService.find(guid);
            setReqDate(EpointDateUtil.convertDate2String(requestHikLog.getReqdate(),"yyyy-MM-dd HH:mm:ss"));
            setRespDate(requestHikLog.getRespdate().toString());
        }
    }


    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<RequestHikUserLog> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<RequestHikUserLog>() {

                @Override
                public List<RequestHikUserLog> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String guid = getRequestParameter("guid");
                    List<Object> conditionList = new ArrayList<Object>();

                    String sql = "select * from REQUEST_HIK_USER_LOG where 1 =1 ";
                    String countsql = "select count(1) from REQUEST_HIK_USER_LOG where 1 =1 ";

                    if (StringUtil.isNotBlank(reqdateStr)) {
                        sql += " and RHDate > ? ";
                        countsql += " and RHDate > ? ";
                        conditionList.add(reqdateStr);
                    }
                    if (StringUtil.isNotBlank(reqdateEnd)) {
                        sql += " and BackDate < ? ";
                        countsql += " and BackDate < ? ";
                        conditionList.add( reqdateEnd );
                    }

                    if (StringUtil.isNotBlank(guid)) {
                        sql += " and HikLogGuid = ? ";
                        countsql += " and HikLogGuid = ? ";
                        conditionList.add(guid);
                    }

                    if (StringUtil.isNotBlank(rxh)){
                        sql += " and XH like ? ";
                        countsql += " and XH like ? ";
                        conditionList.add(rxh + "%");
                    }

                    List<RequestHikUserLog> list = service.findList(
                            sql, first, pageSize,
                            conditionList.toArray());
                    int count = service.countRequestHikUserLog(countsql, conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }


    public RequestHikUserLog getDataBean() {
        if (dataBean == null) {
            dataBean = new RequestHikUserLog();
        }
        return dataBean;
    }

    public void setDataBean(RequestHikUserLog dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }


}
