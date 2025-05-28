package com.epoint.xmz.jnvisitrecord.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.jnvisitrecord.api.entity.JnVisitRecord;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import com.epoint.xmz.jncertrecord.api.entity.JnCertRecord;
import com.epoint.xmz.jnvisitrecord.api.IJnVisitRecordService;


/**
 * 网厅访问量统计表list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-09-14 14:54:42]
 */
@RestController("jnvisitrecordlistaction")
@Scope("request")
public class JnVisitRecordListAction  extends BaseController
{
	@Autowired
    private IJnVisitRecordService service;
    
    /**
     * 网厅访问量统计表实体对象
     */
    private JnVisitRecord dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<JnVisitRecord> model;
  	
    /**
     * 导出模型
     */
    private ExportModel exportModel;
    
    public String bapplydate;
    public String eapplydate;
    
    

    public void pageLoad()
    {
    	if (StringUtil.isBlank(bapplydate)) {
            bapplydate = EpointDateUtil.convertDate2String(new Date(), "YYYY-MM-dd");
        }
        if (StringUtil.isBlank(eapplydate)) {
        	eapplydate = EpointDateUtil.convertDate2String(new Date(), "YYYY-MM-dd");
        }
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
    
    public DataGridModel<JnVisitRecord> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<JnVisitRecord>()
            {

                @Override
                public List<JnVisitRecord> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	String sql = "select OperateDate,count(1) as total from jn_visit_record where 1=1 ";
                    
                    if (StringUtil.isNotBlank(bapplydate)) {
                    	sql += " and OperateDate >= '"+EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(bapplydate)) +" 00:00:00'";
                    }
                    
                    if (StringUtil.isNotBlank(eapplydate)) {
                    	sql += " and OperateDate <= '"+EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDateStr(eapplydate)) +" 23:59:59'";
                    }
                    
                    
                    List<JnVisitRecord> list = service.findList(sql, first, pageSize,null);
                    List<JnVisitRecord> records = service.findList(sql, null);
                    this.setRowCount(records.size());
                    return list;
                	
                	
                }

            };
        }
        return model;
    }

    
    public String getBapplydate() {
        return bapplydate;
    }

    public void setBapplydate(String bapplydate) {
        this.bapplydate = bapplydate;
    }

    public String getEapplydate() {
        return eapplydate;
    }

    public void setEapplydate(String eapplydate) {
        this.eapplydate = eapplydate;
    }
    
    public JnVisitRecord getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new JnVisitRecord();
    	}
        return dataBean;
    }

    public void setDataBean(JnVisitRecord dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }
    


}
