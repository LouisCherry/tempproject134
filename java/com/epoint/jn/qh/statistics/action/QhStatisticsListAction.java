package com.epoint.jn.qh.statistics.action;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.jn.qh.statistics.api.IQhStatistics;
import com.epoint.jn.qh.statistics.api.QhStatistics;


/**
 * 取号量统计list页面对应的后台
 * 
 * @author 夜雨清尘
 * @version [版本号, 2019-06-06 16:10:26]
 */
@RestController("qhstatisticslistaction")
@Scope("request")
public class QhStatisticsListAction  extends BaseController
{

    private static final long serialVersionUID = 3509582475405607625L;

    @Autowired
    private IQhStatistics service;
    
    /**
     * 取号量统计实体对象
     */
    private QhStatistics dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<QhStatistics> model;
  	
    /**
     * 导出模型
     */
    private ExportModel exportModel;
    
    private Date startdate;
    
    private Date enddate;

    public void pageLoad()
    {          
    }

	

    /**
     * 删除选定
     * 
     */
    public void deleteSelect()
    {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
             service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }
    
    public DataGridModel<QhStatistics> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<QhStatistics>()
            {

                @Override
                public List<QhStatistics> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    String startStr=EpointDateUtil.convertDate2String(startdate,"yyyy-MM-dd");
                    String endStr=EpointDateUtil.convertDate2String(enddate,"yyyy-MM-dd");
                  
                    if(StringUtil.isBlank(startStr)){
                        if(StringUtil.isBlank(endStr)){
                            conditionSql+=" and to_days(createdate) = to_days(now())";
                        }else{
                            conditionSql+=" and '"+endStr+"' >= date_format(createdate,'%Y-%m-%d')";  
                        }
                    }else{
                        conditionSql+=" and '"+startStr+"' <= date_format(createdate,'%Y-%m-%d')";
                        if(StringUtil.isBlank(endStr)){
                            conditionSql+=" and to_days(createdate) <= to_days(now()";
                        }else{
                            conditionSql+=" and '"+endStr+"' >= date_format(createdate,'%Y-%m-%d')";  
                        }
                    }
                    conditionSql+=" and centerguid = '"+ZwfwUserSession.getInstance().getCenterGuid()+"'";
                    conditionSql+=" order by createdate desc";  
                    List<QhStatistics> list = service.findList(
                            ListGenerator.generateSql("qh_statistics", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.findList(ListGenerator.generateSql("qh_statistics", conditionSql, sortField, sortOrder), conditionList.toArray()).size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public QhStatistics getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new QhStatistics();
    	}
        return dataBean;
    }

    public void setDataBean(QhStatistics dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel =new ExportModel(
                    "machinename,machineno,macaddress,createdate,count",
                    "设备名称,设备编号,mac地址,时间,取号量");
        }
        return exportModel;
    }



    public Date getStartdate() {
        return startdate;
    }



    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

}
