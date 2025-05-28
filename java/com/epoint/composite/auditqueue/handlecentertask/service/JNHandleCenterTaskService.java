package com.epoint.composite.auditqueue.handlecentertask.service;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditqueue.auditznsbcentertask.domain.AuditZnsbCentertask;
import com.epoint.basic.auditqueue.auditznsbcentertask.inter.IAuditZnsbCentertask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.hottask.domain.AuditTaskHottask;
import com.epoint.basic.audittask.hottask.inter.IAuditTaskHottask;
import com.epoint.basic.audittask.map.inter.IAuditTaskMap;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.common.rabbitmq.ProducerMQ;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

public class JNHandleCenterTaskService
{
    IAuditZnsbCentertask centertaskservice = (IAuditZnsbCentertask) ContainerFactory.getContainInfo()
            .getComponent(IAuditZnsbCentertask.class);
    IAuditTaskMaterial taskmaterialservice = (IAuditTaskMaterial) ContainerFactory.getContainInfo()
            .getComponent(IAuditTaskMaterial.class);
    IAttachService attachservice = (IAttachService) ContainerFactory.getContainInfo()
            .getComponent(IAttachService.class);
    IAuditOrgaWindowYjs windowservice = (IAuditOrgaWindowYjs) ContainerFactory.getContainInfo()
            .getComponent(IAuditOrgaWindowYjs.class);
    IAuditTask taskservice = (IAuditTask) ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
    IAuditTaskExtension taskextensionservice = (IAuditTaskExtension) ContainerFactory.getContainInfo()
            .getComponent(IAuditTaskExtension.class);
    IAuditTaskHottask hottaskservice = (IAuditTaskHottask) ContainerFactory.getContainInfo()
            .getComponent(IAuditTaskHottask.class);
    IAuditTaskMap taskmapservice = (IAuditTaskMap) ContainerFactory.getContainInfo().getComponent(IAuditTaskMap.class);
    ISendMQMessage sendMQMessageService = (ISendMQMessage) ContainerFactory.getContainInfo()
            .getComponent(ISendMQMessage.class);

    public void initCenterTask(String CenterGuid) throws Exception {
        List<String> taskidlist = (List) this.windowservice.getTaskIdListByCenter(CenterGuid).getResult();
        String taskmap = "";
        String EXAMPLEATTACHGUID = "";
        String FORMATTACHGUID = "";
        Iterator var9 = taskidlist.iterator();

        while (true) {
            AuditTask task;
            do {
                String taskid;
                do {
                    if (!var9.hasNext()) {
                        return;
                    }

                    taskid = (String) var9.next();
                }
                while ((Boolean) this.centertaskservice.ISExistbyTaskId(taskid, CenterGuid).getResult());

                task = (AuditTask) this.taskservice.selectUsableTaskByTaskID(taskid).getResult();
            }
            while (task == null);
            /* if("1".equals(task.getStr("businesstype"))){ */
            if ("0".equals(task.getStr("iswtshow"))) {
                continue;
            }
            AuditZnsbCentertask centertask = new AuditZnsbCentertask();
            centertask.setRowguid(UUID.randomUUID().toString());
            centertask.setTaskguid(task.getRowguid());
            centertask.setTaskname(task.getTaskname());
            centertask.setIs_enable(task.getIs_enable());
            centertask.setItem_id(task.getItem_id());
            centertask.setOrdernum(task.getOrdernum());
            centertask.setOuname(task.getOuname());
            centertask.setOuguid(task.getOuguid());
            centertask.setCenterguid(CenterGuid);
            centertask.setTask_id(task.getTask_id());
            centertask.setApplyertype(task.getApplyertype());
            centertask.setIs_edit("0");
            centertask.set("unid", task.getStr("unid"));
            centertask.setAreacode(task.getAreacode());
            AuditTask atask = taskservice.getAuditTaskByGuid(task.getRowguid(), true).getResult();
            centertask.set("isqcwb", atask.getStr("isqcwb"));
            AuditTaskExtension taskextension = (AuditTaskExtension) this.taskextensionservice
                    .getTaskExtensionByTaskGuid(task.getRowguid(), true).getResult();
            if (taskextension != null) {
                centertask.setWebapplytype(taskextension.getWebapplytype());
                centertask.setReservationmanagement(taskextension.getReservationmanagement());
                centertask.set("is_mpmb", taskextension.getStr("is_mpmb"));
                centertask.set("formid", taskextension.getStr("formid"));
            }

            AuditTaskHottask hottask = (AuditTaskHottask) this.hottaskservice.getDetailbyTaskID(task.getTask_id())
                    .getResult();
            if (hottask != null) {
                centertask.setIs_hottask("1");
                centertask.setHottaskordernum(hottask.getOrdernum());
            }
            else {
                centertask.setIs_hottask("0");
            }

            List<Record> maplist = (List) this.taskmapservice.getDictConnectMap(task.getTask_id()).getResult();
            taskmap = "";

            Record map;
            for (Iterator var13 = maplist.iterator(); var13.hasNext(); taskmap = taskmap + map.get("No") + ";") {
                map = (Record) var13.next();
            }

            centertask.setTaskmap(taskmap);
            List<AuditTaskMaterial> taskmateriallist = (List) this.taskmaterialservice
                    .selectTaskMaterialListByTaskGuid(task.getRowguid(), true).getResult();

            List formattachInfolist;
            int i;
            for (i = 0; i < taskmateriallist.size(); ++i) {
                EXAMPLEATTACHGUID = StringUtil
                        .getNotNullString(((AuditTaskMaterial) taskmateriallist.get(i)).getExampleattachguid());
                if (StringUtil.isNotBlank(EXAMPLEATTACHGUID)) {
                    formattachInfolist = this.attachservice.getAttachInfoListByGuid(EXAMPLEATTACHGUID);
                    if (formattachInfolist != null && formattachInfolist.size() > 0
                            && StringUtil.isNotBlank(EXAMPLEATTACHGUID)) {
                        centertask.setIs_show_sample("1");
                        String RabbitMQMsg = task.getRowguid();
                        this.sendMQMessageService.sendByExchange("znsb_exchange_handle", RabbitMQMsg,
                                "sample." + task.getRowguid() + ".modify");
                        break;
                    }
                }
            }

            for (i = 0; i < taskmateriallist.size(); ++i) {
                FORMATTACHGUID = StringUtil
                        .getNotNullString(((AuditTaskMaterial) taskmateriallist.get(i)).getFormattachguid());
                if (StringUtil.isNotBlank(FORMATTACHGUID)) {
                    formattachInfolist = this.attachservice.getAttachInfoListByGuid(FORMATTACHGUID);
                    if (formattachInfolist != null && formattachInfolist.size() > 0
                            && StringUtil.isNotBlank(FORMATTACHGUID)) {
                        centertask.setIs_show_form("1");
                        break;
                    }
                }
            }

            this.centertaskservice.insert(centertask);
        }

        /* } */
    }

    public void initCenterTaskbyTaskids(String CenterGuid, String Taskids) throws Exception {

        List<String> taskidlist = Arrays.asList(Taskids.replace("[", "").replace("]", "").replace(" ", "").split(","));

        AuditZnsbCentertask centertask;
        AuditTask task;
        AuditTaskExtension taskextension;
        String taskmap = "";
        String EXAMPLEATTACHGUID = "";
        String FORMATTACHGUID = "";

        for (String taskid : taskidlist) {
            // 判断taskid是否在audit_orga_windowtask表中，有则继续，无则删除audit_znsb_centertask表中数据
            if (!windowservice.isExistbyTaskId(taskid, CenterGuid).getResult()) {
                centertaskservice.deletebyTaskId(taskid, CenterGuid);
            }
            // 判断taskid是否在audit_znsb_centertask表中，有则不做处理，无则新增

            task = taskservice.selectUsableTaskByTaskID(taskid).getResult();
            if (task != null) {
                centertask = new AuditZnsbCentertask();
                centertask.setRowguid(UUID.randomUUID().toString());
                centertask.setTaskguid(task.getRowguid());
                centertask.setTaskname(task.getTaskname());
                centertask.setIs_enable(task.getIs_enable());
                centertask.setItem_id(task.getItem_id());
                centertask.setOrdernum(task.getOrdernum());
                centertask.setOuname(task.getOuname());
                centertask.setOuguid(task.getOuguid());
                centertask.setCenterguid(CenterGuid);
                centertask.setTask_id(task.getTask_id());
                centertask.setApplyertype(task.getApplyertype());
                centertask.setIs_edit(QueueConstant.CONSTANT_STR_ZERO);
                centertask.setAreacode(task.getAreacode());
                taskextension = taskextensionservice.getTaskExtensionByTaskGuid(task.getRowguid(), true).getResult();
                if (taskextension != null) {
                    centertask.setWebapplytype(taskextension.getWebapplytype());

                    centertask.setReservationmanagement(taskextension.getReservationmanagement());
                }
                // 判断是否是热门事项

                AuditTaskHottask hottask = hottaskservice.getDetailbyTaskID(task.getTask_id()).getResult();
                if (hottask != null) {
                    centertask.setIs_hottask(QueueConstant.CONSTANT_STR_ONE);
                    centertask.setHottaskordernum(hottask.getOrdernum());
                }
                else {
                    centertask.setIs_hottask(QueueConstant.CONSTANT_STR_ZERO);
                }
                // 分类表处理
                List<Record> maplist = taskmapservice.getDictConnectMap(task.getTask_id()).getResult();
                taskmap = "";
                for (Record map : maplist) {
                    taskmap += map.get("No") + ";";
                }
                centertask.setTaskmap(taskmap);

                List<AuditTaskMaterial> taskmateriallist = taskmaterialservice
                        .selectTaskMaterialListByTaskGuid(task.getRowguid(), true).getResult();
                for (int i = 0; i < taskmateriallist.size(); i++) {
                    // 样表处理
                    EXAMPLEATTACHGUID = StringUtil.getNotNullString(taskmateriallist.get(i).getExampleattachguid());
                    if (StringUtil.isNotBlank(EXAMPLEATTACHGUID)) {
                        List<FrameAttachInfo> attachInfolist = attachservice.getAttachInfoListByGuid(EXAMPLEATTACHGUID);
                        if (attachInfolist != null && attachInfolist.size() > 0
                                && StringUtil.isNotBlank(EXAMPLEATTACHGUID)) {
                            centertask.setIs_show_sample(QueueConstant.CONSTANT_STR_ONE);
                            // 样表word转图片处理,推送rabbitmq信息，异步处理
                            ProducerMQ.send("auditsample", "handlesample:" + task.getRowguid()
                                    + "/com.epoint.auditjob.rabbitmqhandle.MQHandleCenterTask");
                            break;
                        }
                    }

                }
                for (int i = 0; i < taskmateriallist.size(); i++) {
                    // 自助填表处理
                    FORMATTACHGUID = StringUtil.getNotNullString(taskmateriallist.get(i).getFormattachguid());
                    if (StringUtil.isNotBlank(FORMATTACHGUID)) {
                        List<FrameAttachInfo> formattachInfolist = attachservice
                                .getAttachInfoListByGuid(FORMATTACHGUID);
                        if (formattachInfolist != null && formattachInfolist.size() > 0
                                && StringUtil.isNotBlank(FORMATTACHGUID)) {
                            centertask.setIs_show_form(QueueConstant.CONSTANT_STR_ONE);
                            break;
                        }
                    }
                }
                centertaskservice.insert(centertask);

            }

        }

    }
}
