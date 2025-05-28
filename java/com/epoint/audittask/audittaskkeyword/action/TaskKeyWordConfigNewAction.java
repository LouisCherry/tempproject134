package com.epoint.audittask.audittaskkeyword.action;

import com.epoint.audittask.audittaskkeyword.api.IAuditTaskKeywordService;
import com.epoint.audittask.audittaskkeyword.api.entity.AuditTaskKeyword;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 事项配置关键词页面对应的后台
 *
 * @author wry
 * @version [版本号, 2016-09-28 15:18:27]
 */
@RestController("taskkeywordconfignewaction")
@Scope("request")
public class TaskKeyWordConfigNewAction extends BaseController {

    @Autowired
    private ICodeItemsService codeItemsService;
    @Autowired
    private IAuditTaskKeywordService auditTaskKeywordService;
    @Autowired
    private IAuditTask iAuditTask;

    private String taskguid;
    private String keyword;

    @Override
    public void pageLoad() {
        taskguid = getRequestParameter("taskguid");
        AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskguid, true).getResult();
        if (auditTask != null) {
            AuditTaskKeyword auditTaskKeyword = auditTaskKeywordService.findByTaskid(auditTask.getTask_id());
            if (auditTaskKeyword != null) {
                keyword = auditTaskKeyword.getKeywordvalue();
            }
        }
    }

    public void save() {
        String taskid = "";
        String areacode = "";
        AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskguid, true).getResult();
        if (auditTask != null) {
            auditTaskKeywordService.deleteByTaskid(taskid);
            taskid = auditTask.getTask_id();
            areacode = auditTask.getAreacode();
            AuditTaskKeyword auditTaskKeyword = new AuditTaskKeyword();
            auditTaskKeyword.setRowguid(UUID.randomUUID().toString());
            auditTaskKeyword.setOperateusername(UserSession.getInstance().getDisplayName());
            auditTaskKeyword.setOperateusername(UserSession.getInstance().getUserGuid());
            auditTaskKeyword.setTaskid(taskid);
            auditTaskKeyword.set("areacode", areacode);
            auditTaskKeyword.setKeywordname(keyword);
            auditTaskKeyword.setKeywordvalue(keyword);
            auditTaskKeywordService.insert(auditTaskKeyword);
        }

        addCallbackParam("msg", "保存成功！");
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
