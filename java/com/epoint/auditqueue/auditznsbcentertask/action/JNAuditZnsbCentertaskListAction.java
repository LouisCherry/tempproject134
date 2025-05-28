package com.epoint.auditqueue.auditznsbcentertask.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;


import com.epoint.basic.auditqueue.auditznsbcentertask.domain.AuditZnsbCentertask;
import com.epoint.basic.auditqueue.auditznsbcentertask.inter.IAuditZnsbCentertask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditqueue.handlecentertask.inter.IHandleCenterTask;
import com.epoint.composite.auditqueue.handlecentertask.inter.IJNHandleCenterTask;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;


/**
 * 中心事项list页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2017-05-09 15:10:25]
 */
@RestController("jnauditznsbcentertasklistaction")
@Scope("request")
public class JNAuditZnsbCentertaskListAction  extends BaseController
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3661455508640591098L;

	@Autowired
	private IAuditZnsbCentertask centertaskservice ;
	@Autowired
	private IHandleCenterTask handlecentertaskservice ;
	@Autowired
    private IJNHandleCenterTask jnhandlecentertaskservice ;
    
    /**
     * 中心事项实体对象
     */
    private AuditZnsbCentertask dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<AuditZnsbCentertask> model;
  	
    /**
     * 导出模型
     */
    private ExportModel exportModel;
    /**
     * 事项名称，搜索用
     */
    private String taskName;
    /**
     * 事项编码，搜索用
     */
    private String item_id;
    

    @Override
    public void pageLoad()
    {

        addCallbackParam("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
    }

	
	/**
	 * 初始化
	 */
	public void initCenterTask() {
	  //删除历史数据
        centertaskservice.deletebyCenterGuid(ZwfwUserSession.getInstance().getCenterGuid());
        //初始化数据
        jnhandlecentertaskservice.initCenterTask(ZwfwUserSession.getInstance().getCenterGuid());
		addCallbackParam("msg", "初始化数据成功！");
	}
   
    
    public DataGridModel<AuditZnsbCentertask> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditZnsbCentertask>()
            {

                @Override
                public List<AuditZnsbCentertask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
				
					sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
					 if (StringUtil.isNotBlank(taskName)) {
	                        sql.like("taskname", taskName);
	                    }
	                    if (StringUtil.isNotBlank(item_id)) {
	                        sql.like("item_id", item_id);
	                    }
					PageData<AuditZnsbCentertask> pageData = centertaskservice
			                .getCenterTaskPageData(sql.getMap(), first,
							pageSize, "ouguid desc, ordernum", "desc").getResult();
					this.setRowCount(pageData.getRowCount());
				
					return pageData.getList();
                }

            };
        }
        return model;
    }
      
    public AuditZnsbCentertask getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new AuditZnsbCentertask();
    	}
        return dataBean;
    }

    public void setDataBean(AuditZnsbCentertask dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }
    

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

}
