package com.epoint.jnzwfw.ysbanjian;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.newshow2.api.Newshow2Service;


/**
 * 施工许可证表单list页面对应的后台
 * 
 * @author zhaoy
 * @version [版本号, 2019-06-02 15:13:10]
 */
@RestController("auditprojectycaction")
@Scope("request")
public class AuditProjectYcAction  extends BaseController
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private Newshow2Service service;
    
    /**
     * 窗口API
     */
    @Autowired
    private IAuditOrgaWindowYjs iAuditOrgaWindow;
    
    /**
     * 施工许可证表单实体对象
     */
    private Record dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;
  	
    /**
     * 中心API
     */
    @Autowired
    private IAuditOrgaServiceCenter iAuditOrgaServiceCenter;
    
    /**
     * 办件
     */
    @Autowired
    private IAuditProject auditProjectServcie;

    public void pageLoad()
    {
        
    }

    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>()
            {

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Record> list = service.getYcProjectList(first, pageSize);
                    int count = service.getYcProjectCount();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public String updateYcProject() {
        List<Record> list = service.getYcProjectList(0, 10000);
        if (list != null && list.size() > 0 ) {
            String taskid = "";
            String projectguid = "";
            String areacode = "";
            // 获取办件信息
            String fields = " rowguid,taskguid,applyername,applyeruserguid,areacode,status,centerguid,applyway,taskcaseguid,pviguid,projectname,applyertype,certnum,certtype,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,address,contactcertnum,legal,flowsn,tasktype,ouname,ouguid,hebingshoulishuliang,remark,windowguid,biguid,banjiedate,is_pause,spendtime,if_express,applydate";
            int i = 0;
            for (Record info : list) {
                taskid = info.getStr("task_id");
                projectguid = info.getStr("rowguid");
                areacode = info.getStr("areacode");
                if (taskid != null) {
                    List<Record> centerGuids = iAuditOrgaWindow.selectCenterGuidsByTaskId(taskid).getResult();
                    if (centerGuids != null && centerGuids.size() > 0) {
                        for (Record record : centerGuids) {
                            AuditOrgaServiceCenter auditOrgaServiceCenter = iAuditOrgaServiceCenter
                                    .findAuditServiceCenterByGuid(record.get("centerGuid")).getResult();
                            AuditProject auditProject = auditProjectServcie.getAuditProjectByRowGuid(fields, projectguid, areacode).getResult();
                            if (auditProject != null) {
                                auditProject.setCenterguid( auditOrgaServiceCenter.getRowguid());
                                auditProjectServcie.updateProject(auditProject);
                                i++;
                            }
                        }
                    }
                }
            }
            return "已批量执行完成，共执行了"+i + "条";
        }else {
            return null;
        }
    }
    
    public Record getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new Record();
    	}
        return dataBean;
    }

    public void setDataBean(Record dataBean)
    {
        this.dataBean = dataBean;
    }


}
