package com.epoint.ces.requesthiklog.action;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.epoint.core.utils.string.StringUtil;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.ces.requesthiklog.api.entity.RequestHikLog;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.ces.requesthiklog.api.IRequestHikLogService;


/**
 * 请求海康日志信息表list页面对应的后台
 *
 * @author shun
 * @version [版本号, 2021-11-22 14:30:42]
 */
@RestController("requesthikloglistaction")
@Scope("request")
public class RequestHikLogListAction extends BaseController {
    @Autowired
    private IRequestHikLogService service;

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

    /**
     * 请求海康日志信息表实体对象
     */
    private RequestHikLog dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<RequestHikLog> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;


    public void pageLoad() {
    }


    public DataGridModel<RequestHikLog> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<RequestHikLog>() {

                @Override
                public List<RequestHikLog> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<>();
                    String sql = "select * from REQUEST_HIK_LOG where 1 = 1 ";
                    String countsql = "select count(1) from REQUEST_HIK_LOG where 1 = 1 ";
                    if (StringUtil.isNotBlank(reqdateStr)) {
                        sql += " and ReqDate > ? ";
                        countsql += " and ReqDate > ? ";
                        conditionList.add(reqdateStr );
                    }
                    if (StringUtil.isNotBlank(reqdateEnd)) {
                        sql += " and ReqDate < ? ";
                        countsql += " and ReqDate < ? ";
                        conditionList.add( reqdateEnd );
                    }


                    List<RequestHikLog> list = service.findList(sql, first, pageSize, conditionList.toArray());
                    int count = service.countRequestHikLog(countsql, conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }


    public RequestHikLog getDataBean() {
        if (dataBean == null) {
            dataBean = new RequestHikLog();
        }
        return dataBean;
    }

    public void setDataBean(RequestHikLog dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }


}
