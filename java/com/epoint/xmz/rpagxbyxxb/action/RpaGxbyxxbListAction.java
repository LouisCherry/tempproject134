package com.epoint.xmz.rpagxbyxxb.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.rpagxbyxxb.api.entity.RpaGxbyxxb;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.rpagxbyxxb.api.IRpaGxbyxxbService;


/**
 * 高校毕业信息表list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-12-23 16:40:09]
 */
@RestController("rpagxbyxxblistaction")
@Scope("request")
public class RpaGxbyxxbListAction  extends BaseController
{
	@Autowired
    private IRpaGxbyxxbService service;
    
    /**
     * 高校毕业信息表实体对象
     */
    private RpaGxbyxxb dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<RpaGxbyxxb> model;
  	
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
    
    public DataGridModel<RpaGxbyxxb> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<RpaGxbyxxb>()
            {

                @Override
                public List<RpaGxbyxxb> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<RpaGxbyxxb> list = service.findList(
                            ListGenerator.generateSql("rpa_gxbyxxb", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countRpaGxbyxxb(ListGenerator.generateSql("rpa_gxbyxxb", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public RpaGxbyxxb getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new RpaGxbyxxb();
    	}
        return dataBean;
    }

    public void setDataBean(RpaGxbyxxb dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("byyx,certno,certnum,dwmc,name,overyear,shsj,shzt,xl,zy", "毕业院校,协议书编号,身份证号,单位名称,姓名,毕业年度,审核时间,审核状态,学历,专业");
        }
        return exportModel;
    }
    


}
