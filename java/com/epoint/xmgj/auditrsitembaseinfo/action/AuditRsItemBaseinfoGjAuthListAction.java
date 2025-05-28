package com.epoint.xmgj.auditrsitembaseinfo.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import com.epoint.xmgj.projectauthorization.api.entity.ProjectAuthorization;
import com.tzwy.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmgj.auditrsitembaseinfo.api.entity.AuditRsItemBaseinfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmgj.auditrsitembaseinfo.api.IAuditRsItemBaseinfoService;
import com.epoint.xmgj.projectauthorization.api.IProjectAuthorizationService;

/**
 * 项目表list页面对应的后台
 * 
 * @author pansh
 * @version [版本号, 2025-02-12 17:31:53]
 */
@RestController("auditrsitembaseinfogjauthlistaction")
@Scope("request")
public class AuditRsItemBaseinfoGjAuthListAction extends BaseController
{
	@Autowired
    private IAuditRsItemBaseinfoService service;
    
    /**
     * 项目表实体对象
     */
    private AuditRsItemBaseinfo dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<AuditRsItemBaseinfo> model;
  	
    /**
     * 导出模型
     */
    private ExportModel exportModel;


    private String userguid;

    private String rowguid;
    @Autowired
    private IProjectAuthorizationService iProjectAuthorizationService;
    

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
        addCallbackParam("msg", l("成功删除！"));
    }
    
    public DataGridModel<AuditRsItemBaseinfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditRsItemBaseinfo>()
            {

                @Override
                public List<AuditRsItemBaseinfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    conditionSql +=" and parentid is null";
                    List<AuditRsItemBaseinfo> list = service.findList(
                            ListGenerator.generateSql("AUDIT_RS_ITEM_BASEINFO", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    for (AuditRsItemBaseinfo item : list) {
                        List<String> userguids = iProjectAuthorizationService.getUsersByProjectId(item.getRowguid());
                        item.put("userguids", StringUtils.join(userguids, ";"));
                    }
                   int count = service.countAuditRsItemBaseinfo(ListGenerator.generateSql("AUDIT_RS_ITEM_BASEINFO", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public AuditRsItemBaseinfo getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new AuditRsItemBaseinfo();
    	}
        return dataBean;
    }

    public void setDataBean(AuditRsItemBaseinfo dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("itemcode,itemname,itemlegaldept,itemstartdate,itemfinishdate,totalinvest", "项目代码,项目名称,建设单位,建设开始时间,建设结束时间,总投资（万元）");
        }
        return exportModel;
    }

    public void add(){
        if(StringUtils.isNotBlank(rowguid) && StringUtils.isNotBlank(userguid)){
            //先删除，再保存
            iProjectAuthorizationService.deleteByProjectId(rowguid);
            String[] users = userguid.split(";");
            if(users.length > 0){
                for(String user : users){
                    ProjectAuthorization projectAuthorization = new ProjectAuthorization();
                    projectAuthorization.setAuthorizationdate(new Date());
                    projectAuthorization.setProjectid(rowguid);
                    projectAuthorization.setUserid(user);
                    projectAuthorization.setRowguid(UUID.randomUUID().toString());
                    projectAuthorization.setAuthorizedby(userSession.getDisplayName());
                    iProjectAuthorizationService.insert(projectAuthorization);
                }
            }

        }


    }

    public String getUserguid() {
        return userguid;
    }

    public void setUserguid(String userguid) {
        this.userguid = userguid;
    }

    public String getRowguid() {
        return rowguid;
    }

    public void setRowguid(String rowguid) {
        this.rowguid = rowguid;
    }
}
