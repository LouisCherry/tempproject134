package com.epoint.xmz.yjsczcapplyer.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.yjsczcapplyer.api.entity.YjsCzcApplyer;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.yjsczcapplyer.api.IYjsCzcApplyerService;


/**
 * 一件事住租车申请人信息表list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-04-18 15:38:32]
 */
@RestController("yjsczcapplyerlistaction")
@Scope("request")
public class YjsCzcApplyerListAction  extends BaseController
{
	@Autowired
    private IYjsCzcApplyerService service;
    
    /**
     * 一件事住租车申请人信息表实体对象
     */
    private YjsCzcApplyer dataBean;
    
    private Date startDate;

    private Date endDate;
  
    /**
     * 表格控件model
     */
    private DataGridModel<YjsCzcApplyer> model;
  	
    /**
     * 导出模型
     */
    private ExportModel exportModel;
    
    

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
    
    public DataGridModel<YjsCzcApplyer> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<YjsCzcApplyer>()
            {

                @Override
                public List<YjsCzcApplyer> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    //搜索条件
                    if (startDate != null) {
                        conditionSql += " and createdate > '" + EpointDateUtil.convertDate2String(startDate, EpointDateUtil.DATE_TIME_FORMAT)+"'";
                    }
                    if (endDate != null) {
                        conditionSql += " and createdate < '" + EpointDateUtil.convertDate2String(endDate, EpointDateUtil.DATE_TIME_FORMAT)+"'";
                    }
                    
                    
                    List<YjsCzcApplyer> list = service.findList(
                            ListGenerator.generateSql("yjs_czc_applyer", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countYjsCzcApplyer(ListGenerator.generateSql("yjs_czc_applyer", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public YjsCzcApplyer getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new YjsCzcApplyer();
    	}
        return dataBean;
    }

    public void setDataBean(YjsCzcApplyer dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("apply,certnum,createdate,name,sex", "申请类型,身份证号,生成日期,姓名,性别");
        }
        return exportModel;
    }
    
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    


}
