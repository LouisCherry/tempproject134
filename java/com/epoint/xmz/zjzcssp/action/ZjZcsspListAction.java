package com.epoint.xmz.zjzcssp.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.xm.similarity.util.StringUtil;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.zjzcssp.api.entity.ZjZcssp;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.zjzcssp.api.IZjZcsspService;


/**
 * 邹城随手拍表list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2020-10-10 11:34:40]
 */
@RestController("zjzcssplistaction")
@Scope("request")
public class ZjZcsspListAction  extends BaseController
{
	@Autowired
    private IZjZcsspService service;
    
    /**
     * 邹城随手拍表实体对象
     */
    private ZjZcssp dataBean;
    
    private Date startDate;

    private Date endDate;
  
    /**
     * 表格控件model
     */
    private DataGridModel<ZjZcssp> model;
  	
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
    
    public DataGridModel<ZjZcssp> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<ZjZcssp>()
            {

                @Override
                public List<ZjZcssp> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    conditionSql += " and areacode = '370883'";

                    //搜索条件
                    if (startDate != null) {
                        conditionSql += " and createtime>?";
                        conditionList.add(startDate);
                    }
                    if (endDate != null) {
                        conditionSql += " and createtime<?";
                        conditionList.add(endDate);
                    }
                    
                    if (StringUtil.isNotBlank(dataBean.getUsername())) {
                        String Username = "%" + dataBean.getUsername() + "%";
                        conditionSql += " and username like ?";
                        conditionList.add(Username);
                    }
                    
                    if (StringUtil.isNotBlank(dataBean.getContent())) {
                        String content = "%" + dataBean.getContent() + "%";
                        conditionSql += " and content like ?";
                        conditionList.add(content);
                    }
                    
                    List<ZjZcssp> list = service.findList(
                            ListGenerator.generateSql("zj_zcssp", conditionSql, "createtime", "desc"), first, pageSize,
                            conditionList.toArray());
                   int count = service.countZjZcssp(ListGenerator.generateSql("zj_zcssp", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public ZjZcssp getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new ZjZcssp();
    	}
        return dataBean;
    }

    public void setDataBean(ZjZcssp dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
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
