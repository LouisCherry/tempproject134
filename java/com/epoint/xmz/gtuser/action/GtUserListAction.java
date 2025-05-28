package com.epoint.xmz.gtuser.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.gtuser.api.entity.GtUser;
import com.epoint.xmz.spglgt.GtProjectInfoPush;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.gtuser.api.IGtUserService;


/**
 * 国土用户管理表list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 12:05:00]
 */
@RestController("gtuserlistaction")
@Scope("request")
public class GtUserListAction  extends BaseController
{
	@Autowired
    private IGtUserService service;
    
    /**
     * 国土用户管理表实体对象
     */
    private GtUser dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<GtUser> model;
  	
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
    
    public void getResult(String rowguid) {
    	GtUser user = service.find(rowguid);
    	if (user != null) {
    		
    	}
//    	GtProjectInfoPush.queryDownloadProgress(user.getSign(), user.getu, requestID, methodName);
    }
    
    public DataGridModel<GtUser> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<GtUser>()
            {

                @Override
                public List<GtUser> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<GtUser> list = service.findList(
                            ListGenerator.generateSql("gt_user", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countGtUser(ListGenerator.generateSql("gt_user", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public GtUser getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new GtUser();
    	}
        return dataBean;
    }

    public void setDataBean(GtUser dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("datapackagetype,loginname,optype,phone,regioncode,stage,username", "群组名称,登录名,操作类型,手机号,所在行政区编码,用途管制阶段的简称,用户名");
        }
        return exportModel;
    }
    


}
