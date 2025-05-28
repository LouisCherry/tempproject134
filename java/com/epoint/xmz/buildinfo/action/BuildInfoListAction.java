package com.epoint.xmz.buildinfo.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.buildinfo.api.entity.BuildInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.buildinfo.api.IBuildInfoService;


/**
 * 工改二阶段建筑表list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-09-08 14:14:05]
 */
@RestController("buildinfolistaction")
@Scope("request")
public class BuildInfoListAction  extends BaseController
{
	@Autowired
    private IBuildInfoService service;
    
    /**
     * 工改二阶段建筑表实体对象
     */
    private BuildInfo dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<BuildInfo> model;
  	
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
    
    public DataGridModel<BuildInfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<BuildInfo>()
            {

                @Override
                public List<BuildInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<BuildInfo> list = service.findList(
                            ListGenerator.generateSql("build_info", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countBuildInfo(ListGenerator.generateSql("build_info", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public BuildInfo getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new BuildInfo();
    	}
        return dataBean;
    }

    public void setDataBean(BuildInfo dataBean)
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
