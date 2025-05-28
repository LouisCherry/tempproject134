package com.epoint.basic.controller.communication.waithandle;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.DtoContants;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.MessageLogCode;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.commission.api.ICommisssionSetService;
import com.epoint.frame.service.message.commission.entity.FrameCommissionHandle;
import com.epoint.frame.service.message.entity.MessagesCenter;

@RestController("jnwaithandlelistaction")
@Scope("request")
public class JnWaitHandleListAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = -6708262034463708111L;

    /**
     * 表格控件model
     */
    private DataGridModel<MessagesCenter> model;

    /**
     * 消息的特殊标示，多个中间用; 分开，如果不包含，前面用!
     */
    private String clientIdentifier;
    @Autowired
    private IMessagesCenterService messagescenterservice;
    @Autowired
    private ICommisssionSetService commisssionsetservice;
    @Autowired
    private IAuditProject auditProjectService;
    /**
     * 导出模型
     */
    private ExportModel exportModel;
    private Date dateFrom;

    private Date dateTo;

    private String txtTitle;

    private String commissionGuid;

    private String leadGuid;

    private String ouGuid;

    @Override
    public void pageLoad() {
        commissionGuid = getRequestParameter("commissionGuid");
        // 如果是代理人设置
        if (StringUtil.isNotBlank(commissionGuid)) {
            addCallbackParam("commissionGuid", commissionGuid);
            leadGuid = getRequestParameter("leadGuid");
            ouGuid = getRequestParameter("ouguid");
        }
        else {
            leadGuid = userSession.getUserGuid();
            ouGuid = userSession.getOuGuid();
        }
    }

    /**
     * 获取表格数据
     * 
     * @return DataGridModel
     */
    public DataGridModel<MessagesCenter> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<MessagesCenter>()
            {
                @SuppressWarnings("deprecation")
                @Override
                public List<MessagesCenter> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    int count = messagescenterservice.getTotalCount(leadGuid, dateFrom, dateTo, txtTitle,
                            IMessagesCenterService.MESSAGETYPE_WAIT, clientIdentifier, -1, ouGuid,
                            commisssionsetservice.listByUserGuid(userSession.getUserGuid()),
                            commisssionsetservice.listByLeadUserGuid(userSession.getUserGuid()));
                    this.setRowCount(count);
                    String sort = null;
                    if (StringUtil.isNotBlank(sortField)) {
                        sort = " order by " + sortField + " " + sortOrder;
                    }
                    List<MessagesCenter> resultList = messagescenterservice.queryForList(leadGuid, dateFrom, dateTo,
                            txtTitle, IMessagesCenterService.MESSAGETYPE_WAIT, clientIdentifier, sort, -1, ouGuid,
                            commisssionsetservice.listByUserGuid(userSession.getUserGuid()),
                            commisssionsetservice.listByLeadUserGuid(userSession.getUserGuid()), first, pageSize);
                    // 做特殊处理
                    for (MessagesCenter m : resultList) {
                        //获取办件编号，做导出处理
                        String handleUrl = m.getHandleUrl();
                        if (handleUrl.contains("projectguid")) {
                                int indexOf = handleUrl.indexOf("projectguid=");
                                String projectguid = handleUrl.substring(indexOf+12,indexOf+48);
                                AuditProject result = auditProjectService.getAuditProjectByRowGuid(projectguid, null).getResult();
                                if (result != null){
                                    m.put("projectname", result.getProjectname());
                                    m.put("flowsn", result.getFlowsn());
                                    m.put("applyername", result.getApplyername());
                                    m.put("contactmobile", result.getContactmobile());
                                    m.put("contactcertnum", result.getContactcertnum());
                                    m.put("certnum", result.getCertnum());
                                    m.put("operateUserName", m.getTargetDispName());
                                }

                        }
                        else {
                            m.put("projectname","该待办为工改事项，不导出");
                        }
                        
                        String topic = IMessagesCenterService.getTopic(m.getMessageItemGuid(), m.getTitle(),
                                m.getHandleUrl(), null);
                        m.put("topic", l(topic));
                    }
                    return resultList;

                }
            };
        }
        return model;
    }

    /**
     * 删除选择
     */
    public void delete(String ids) {
        if (StringUtil.isBlank(commissionGuid)) {
            if (StringUtil.isNotBlank(ids)) {
                String[] select = ids.split(DtoContants.SPLIT);
                for (String messageItemGuid : select) {
                    messagescenterservice.deleteMessage(messageItemGuid, leadGuid);
                }
                addCallbackParam("msg", l("删除成功"));
                insertSystemLog(l(LOG_OPERATOR_TYPE_DELETE), l(MessageLogCode.getInstance().getSubSystemType()),
                        l("删除代办操作"), "Messages_Center");
            }
        }
        else {
            addCallbackParam("msg", l("当前无法使用此功能!"));
        }

    }

    /**
     * 转为缓办
     * 
     * @param ids
     */
    public void delay(String ids) {
        if (StringUtil.isBlank(commissionGuid)) {
            if (StringUtil.isNotBlank(ids)) {
                String[] select = ids.split(DtoContants.SPLIT);
                for (String messageItemGuid : select) {
                    messagescenterservice.updateMessageType(messageItemGuid, l("缓办"), leadGuid);
                }
                addCallbackParam("msg", l("操作成功"));
                insertSystemLog(l(LOG_OPERATOR_TYPE_MODIFY), l(MessageLogCode.getInstance().getSubSystemType()),
                        l("转为缓办操作"), "Messages_Center");
            }
        }
        else {
            addCallbackParam("msg", l("当前无法使用此功能!"));
        }

    }

    /**
     * 设置代理人
     * 
     * @param ids
     */
    public void addCommision(String ids, String titles) {
        if (StringUtil.isNotBlank(commissionGuid)) {
            if (StringUtil.isNotBlank(ids)) {
                String[] select = ids.split(DtoContants.SPLIT);
                String[] title = titles.split("topic_split");
                boolean result = true;
                int count = 0;
                String message = null;
                for (int i = 0; i < select.length; i++) {
                    if (commisssionsetservice.isExistsMessage(commissionGuid, select[i])) {
                        result = false;
                        count++;
                        if (StringUtil.isBlank(message))
                            message = title[i] + l("已经选择过了") + "!";
                    }
                }
                if (!result) {
                    addCallbackParam("msg", message.replace(l("已经选择过了"), l("等") + count + l("个已经选择过了")));
                }
                else {
                    for (String messageItemGuid : select) {
                        FrameCommissionHandle handle = new FrameCommissionHandle();
                        handle.setCommissionGuid(commissionGuid);
                        handle.setMessageItemsGuid(messageItemGuid);
                        handle.setHandleGuid(UUID.randomUUID().toString());
                        commisssionsetservice.insert(handle);
                    }
                    addCallbackParam("msg", l("添加成功"));
                    addCallbackParam("success", "true");
                }
                insertSystemLog(l(LOG_OPERATOR_TYPE_ADD), l(MessageLogCode.getInstance().getSubSystemType()),
                        l("设置代理人操作"), "Messages_Center");
            }
        }
        else {
            addCallbackParam("msg", l("当前无法使用此功能!"));
        }

    }
    //待办导出
    public ExportModel getExportModelDbsy() {
        if (exportModel == null) {
            exportModel = new ExportModel(
                    "projectname,flowsn,applyername,contactmobile,contactcertnum,certnum,operateUserName",
                    "事项名称,办件编号,申请人,联系电话,联系人身份证号/法人身份证号,办理人身份证/企业统一信用代码,审核人员名称");
        }
        return exportModel;
    }
    

    
    
    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getTxtTitle() {
        return txtTitle;
    }

    public void setTxtTitle(String txtTitle) {
        this.txtTitle = txtTitle;
    }
}
