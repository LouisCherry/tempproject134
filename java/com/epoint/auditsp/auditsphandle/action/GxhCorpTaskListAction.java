package com.epoint.auditsp.auditsphandle.action;

import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.auditspitaskcorp.api.IAuditSpITaskCorpService;
import com.epoint.xmz.thirdreporteddata.auditspitaskcorp.api.entity.AuditSpITaskCorp;
import com.epoint.xmz.thirdreporteddata.basic.basedata.participatsinfo.api.GxhIParticipantsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 参建单位信息表list页面对应的后台
 *
 * @author eiai9
 * @version [版本号, 2018-05-18 13:51:00]
 */
@RestController("gxhcorptasklistaction")
@Scope("request")
public class GxhCorpTaskListAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IParticipantsInfoService service;
    @Autowired
    private GxhIParticipantsInfoService gxhIParticipantsInfoService;

    @Autowired
    private IAuditSpITaskCorpService iAuditSpITaskCorpService;

    /**
     * 参建单位信息表实体对象
     */
    private ParticipantsInfo dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<ParticipantsInfo> model;


    /**
     * 企业类型
     */
    private List<SelectItem> corpTypeModel = null;

    private String taskGuid;
    private String subappGuid;

    public void pageLoad() {
        taskGuid = getRequestParameter("taskguid");
        subappGuid = getRequestParameter("subappGuid");
        addCallbackParam("subappGuid", subappGuid);
    }

    /**
     * 删除选定
     */
    public void deleteSelect(String id) {
        ParticipantsInfo participantsInfo = service.find(id);
        service.deleteByGuid(participantsInfo.getRowguid());
        // 删除单位与关联关系
        this.cancelSelect(id);
        addCallbackParam("msg", "成功删除！");
    }

    /**
     * 关联
     */
    public void select(String id) {
        // 关联 把单位数据保存到audit_sp_i_task_corp
        ParticipantsInfo participantsInfo = service.find(id);
        AuditSpITaskCorp auditSpITaskCorp = new AuditSpITaskCorp();
        auditSpITaskCorp.setRowguid(UUID.randomUUID().toString());
        auditSpITaskCorp.setCorptyppe(participantsInfo.getCorptype());
        auditSpITaskCorp.setCreatedate(new Date());
        auditSpITaskCorp.setSubappguid(participantsInfo.getSubappguid());
        auditSpITaskCorp.setTaskguid(taskGuid);
        auditSpITaskCorp.setCorpguid(participantsInfo.getRowguid());
        iAuditSpITaskCorpService.insert(auditSpITaskCorp);
        addCallbackParam("msg", "关联成功！");
    }

    /**
     * 取消关联
     */
    public void cancelSelect(String id) {
        // 取消关联 把单位数据audit_sp_i_task_corp删除
        ParticipantsInfo participantsInfo = service.find(id);
        AuditSpITaskCorp auditSpITaskCorp = iAuditSpITaskCorpService.deleteInfoBySubappguidAndTaskGuid(
                participantsInfo.getSubappguid(), taskGuid);
        if (auditSpITaskCorp != null) {
            iAuditSpITaskCorpService.deleteByGuid(auditSpITaskCorp.getRowguid());
            addCallbackParam("msg", "取消关联成功！");
        }

    }

    /**
     * 批量关联
     */
    public void allSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        Date date = new Date();
        for (String sel : select) {
            // 关联 把单位数据保存到audit_sp_i_task_corp
            ParticipantsInfo participantsInfo = service.find(sel);
            AuditSpITaskCorp auditSpITaskCorp = new AuditSpITaskCorp();
            auditSpITaskCorp.setRowguid(UUID.randomUUID().toString());
            auditSpITaskCorp.setCorptyppe(participantsInfo.getCorptype());
            auditSpITaskCorp.setCreatedate(date);
            auditSpITaskCorp.setSubappguid(participantsInfo.getSubappguid());
            auditSpITaskCorp.setTaskguid(taskGuid);
            auditSpITaskCorp.setCorpguid(participantsInfo.getRowguid());
            iAuditSpITaskCorpService.insert(auditSpITaskCorp);

        }
        addCallbackParam("msg", "批量关联成功！");
    }

    public DataGridModel<ParticipantsInfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<ParticipantsInfo>() {
                /**
                 *
                 */
                private static final long serialVersionUID = -732498039315885100L;

                @Override
                public List<ParticipantsInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    PageData<ParticipantsInfo> pageData = gxhIParticipantsInfoService.getListByTaskguidAndSubappguid(
                            first, pageSize, taskGuid, subappGuid, dataBean.getCorpname(), dataBean.getCorpcode(),
                            dataBean.get("legal"), "");
                    List<ParticipantsInfo> list = pageData.getList();
                    for (ParticipantsInfo participantsInfo : list) {
                        participantsInfo.set("status",
                                "0".equals(participantsInfo.getStr("status")) ? "未关联" : "已关联");
                        participantsInfo.set("corptype_int", participantsInfo.getCorptype());
                    }
                    int count = pageData.getRowCount();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public ParticipantsInfo getDataBean() {
        if (dataBean == null) {
            dataBean = new ParticipantsInfo();
        }
        return dataBean;
    }

    public void setDataBean(ParticipantsInfo dataBean) {
        this.dataBean = dataBean;
    }


    /**
     * 查询企业类型
     */
    public String findCorpType(String guid) {
        return service.find(guid).getCorptype();
    }

    /**
     * 企业类型代码项
     */
    public List<SelectItem> getCorpTypeModel() {
        if (corpTypeModel == null) {
            List<SelectItem> result = new ArrayList<SelectItem>();
            result.add(new SelectItem("", "所有"));
            result.add(new SelectItem("31", "建设单位31"));
            result.add(new SelectItem("1", "勘察单位1"));
            result.add(new SelectItem("2", "设计单位2"));
            result.add(new SelectItem("3", "施工单位3"));
            result.add(new SelectItem("4", "监理单位4"));
            result.add(new SelectItem("10", "质量监督单位10"));
            corpTypeModel = result;
        }
        return this.corpTypeModel;
    }
}
