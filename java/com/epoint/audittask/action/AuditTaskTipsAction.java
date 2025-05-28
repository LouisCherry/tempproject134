package com.epoint.audittask.action;

import com.epoint.audittask.audittaskkeyword.api.IAuditTaskKeywordService;
import com.epoint.audittask.audittaskkeyword.api.entity.AuditTaskKeyword;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;


import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.xmz.audittaskpopinfo.api.IAuditTaskPopInfoService;
import com.epoint.xmz.audittaskpopinfo.api.entity.AuditTaskPopInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * 事项配置关键词页面对应的后台
 *
 * @author wry
 * @version [版本号, 2016-09-28 15:18:27]
 */
@RestController("audittasktipsaction")
@Scope("request")
public class AuditTaskTipsAction extends BaseController {

    @Autowired
    private IAuditTaskPopInfoService service;
    @Autowired
    private IAuditTask iAuditTask;
    private String taskguid;
    private AuditTaskPopInfo dataBean=null;
    @Override
    public void pageLoad() {
        taskguid = getRequestParameter("taskguid");
        AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskguid, true).getResult();
        if (auditTask != null) {
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.eq("taskid",auditTask.getTask_id());
            dataBean = service.find(sqlConditionUtil.getMap());
        }
        if(dataBean==null)
        {
            dataBean=new AuditTaskPopInfo();
        }
    }

    public void save() {
        AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskguid, true).getResult();
        if (auditTask != null) {

            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.eq("taskid",auditTask.getTask_id());
            AuditTaskPopInfo auditTaskPopInfo = service.find(sqlConditionUtil.getMap());
            if(auditTaskPopInfo==null){
                dataBean.setRowguid(UUID.randomUUID().toString());
                dataBean.setOperateusername(UserSession.getInstance().getDisplayName());
                dataBean.setOperateuserguid(UserSession.getInstance().getUserGuid());
                dataBean.setOperatedate(new Date());
                dataBean.setTaskid(auditTask.getTask_id());
                dataBean.setCreate_by(UserSession.getInstance().getUserGuid());
                dataBean.setCreate_time(new Date());
                service.insert(dataBean);
            }
            else{
                dataBean.setOperateusername(UserSession.getInstance().getDisplayName());
                dataBean.setOperatedate(new Date());
                dataBean.setOperateuserguid(UserSession.getInstance().getUserGuid());
                service.update(dataBean);
            }
        }

        addCallbackParam("msg", "保存成功！");
    }




    public AuditTaskPopInfo getDataBean()
    {
        return dataBean;
    }

    public void setDataBean(AuditTaskPopInfo dataBean)
    {
        this.dataBean = dataBean;
    }
}
