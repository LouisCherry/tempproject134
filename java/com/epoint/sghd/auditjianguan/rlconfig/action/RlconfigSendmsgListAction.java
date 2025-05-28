package com.epoint.sghd.auditjianguan.rlconfig.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.services.DBServcie;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.message.entity.MessagesCenterHistroy;
import com.epoint.sghd.auditjianguan.rlconfig.api.IRlconfigSendmsg;
import com.epoint.util.SqlUtils;

/**
 * 前四阶段信息配置表list页面对应的后台
 *
 * @author lzhming
 * @version [版本号, 2023-03-17 08:57:44]
 */
@RestController("rlconfigsendmsglistaction")
@Scope("request")
public class RlconfigSendmsgListAction extends BaseController
{
    @Autowired
    private IRlconfigSendmsg service;

    @Autowired
    private IMessagesCenterService iMessagesCenterService;

    /**
     * 前四阶段信息配置表实体对象
     */
    private MessagesCenter rlconfigsendmsg;

    /**
     * 表格控件model
     */
    private DataGridModel<Object> model;

    private ExportModel exportModel;

    public void pageLoad() {
        if (rlconfigsendmsg == null) {
            rlconfigsendmsg = new MessagesCenter();
        }
    }

    /**
     * 删除选定
     */
    /*public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }*/

    public DataGridModel<Object> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Object>()
            {

                @Override
                public List<Object> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlUtils sqlConditionUtil = new SqlUtils();
                    sqlConditionUtil.eq("fromuser", "TaskJnRenLingSendmsgJob");
                    if (StringUtil.isNotBlank(rlconfigsendmsg.getTargetDispName())) {
                        sqlConditionUtil.like("targetdispname", rlconfigsendmsg.getTargetDispName());
                    }
                    if (StringUtil.isNotBlank(rlconfigsendmsg.getStr("ouname"))) {
                        sqlConditionUtil.like("baseouguid", rlconfigsendmsg.getStr("ouname"));
                    }
                    if (StringUtil.isNotBlank(rlconfigsendmsg.get("generatedateStart"))) {
                        sqlConditionUtil.ge("generatedate", rlconfigsendmsg.getDate("generatedateStart"));
                    }
                    if (StringUtil.isNotBlank(rlconfigsendmsg.get("generatedateEnd"))) {
                        sqlConditionUtil.le("generatedate", rlconfigsendmsg.getDate("generatedateEnd"));
                    }

                    PageData<Object> pageData = DBServcie.getInstance(MessagesCenterHistroy.class).getListByPage(
                            MessagesCenterHistroy.class, sqlConditionUtil.getMap(), first, pageSize, sortField,
                            sortOrder);
                    List<Object> list = pageData.getList();
                    setRowCount(pageData.getRowCount());
                    return list;
                }

            };
        }
        return model;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("generatedate,targetdispname,baseouguid,content", "发送时间,接收人,接收人部门,短信发送内容");
        }
        return exportModel;
    }

    public MessagesCenter getRlconfigsendmsg() {
        return rlconfigsendmsg;
    }

    public void setRlconfigsendmsg(MessagesCenter rlconfigsendmsg) {
        this.rlconfigsendmsg = rlconfigsendmsg;
    }

}
