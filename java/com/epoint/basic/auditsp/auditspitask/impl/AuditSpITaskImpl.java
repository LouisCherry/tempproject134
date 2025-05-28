package com.epoint.basic.auditsp.auditspitask.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspitask.service.AuditSpITaskService;
import com.epoint.basic.auditsp.auditspsharematerialrelation.domain.AuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditspsharematerialrelation.inter.IAuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.service.AuditSpService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;

@Service
@Component
public class AuditSpITaskImpl implements IAuditSpITask
{
    @Override
    public AuditCommonResult<String> addTaskInstance(String businessGuid, String biGuid, String phaseGuid,
            String taskGuid, String taskName, String subappGuid, Integer orderNum) {
        AuditSpService<AuditSpITask> auditSpService = new AuditSpService<AuditSpITask>();
        AuditSpITask auditSpITask = new AuditSpITask();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
//            auditSpITask.setProjectguid(UUID.randomUUID().toString());
            auditSpITask.setOperatedate(new Date());
            auditSpITask.setBiguid(biGuid);
            auditSpITask.setBusinessguid(businessGuid);
            auditSpITask.setPhaseguid(phaseGuid);
            auditSpITask.setRowguid(UUID.randomUUID().toString());
            auditSpITask.setSubappguid(subappGuid);
            auditSpITask.setTaskguid(taskGuid);
            auditSpITask.setTaskname(taskName);
            auditSpITask.setOrdernumber(orderNum);
            auditSpService.addRecord(AuditSpITask.class, auditSpITask, false);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> addTaskInstance(String businessGuid, String biGuid, String phaseGuid,
            String taskGuid, String taskName, String subappGuid, Integer orderNum, String areacode) {
        AuditSpService<AuditSpITask> auditSpService = new AuditSpService<AuditSpITask>();
        AuditSpITask auditSpITask = new AuditSpITask();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
//            auditSpITask.setProjectguid(UUID.randomUUID().toString());
            auditSpITask.setOperatedate(new Date());
            auditSpITask.setBiguid(biGuid);
            auditSpITask.setBusinessguid(businessGuid);
            auditSpITask.setPhaseguid(phaseGuid);
            auditSpITask.setRowguid(UUID.randomUUID().toString());
            auditSpITask.setSubappguid(subappGuid);
            auditSpITask.setTaskguid(taskGuid);
            auditSpITask.setTaskname(taskName);
            auditSpITask.setOrdernumber(orderNum);
            auditSpITask.setAreacode(areacode);
            auditSpService.addRecord(AuditSpITask.class, auditSpITask, false);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> addTaskInstance(String businessGuid, String biGuid, String phaseGuid,
            String taskGuid, String taskName, String subappGuid, Integer orderNum, String areacode, String reviewguid) {
        AuditSpService<AuditSpITask> auditSpService = new AuditSpService<AuditSpITask>();
        AuditSpITask auditSpITask = new AuditSpITask();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
//            auditSpITask.setProjectguid(UUID.randomUUID().toString());
            auditSpITask.setOperatedate(new Date());
            auditSpITask.setBiguid(biGuid);
            auditSpITask.setBusinessguid(businessGuid);
            auditSpITask.setPhaseguid(phaseGuid);
            auditSpITask.setRowguid(UUID.randomUUID().toString());
            auditSpITask.setSubappguid(subappGuid);
            auditSpITask.setTaskguid(taskGuid);
            auditSpITask.setTaskname(taskName);
            auditSpITask.setOrdernumber(orderNum);
            auditSpITask.setAreacode(areacode);
            auditSpITask.setReviewguid(reviewguid);
            auditSpService.addRecord(AuditSpITask.class, auditSpITask, false);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> addTaskInstance(String businessGuid, String biGuid, String phaseGuid,
            String taskGuid, String taskName, String subappGuid, Integer orderNum, String areacode, String reviewguid,
            String sflcbsx) {
        AuditSpService<AuditSpITask> auditSpService = new AuditSpService<AuditSpITask>();
        AuditSpITask auditSpITask = new AuditSpITask();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
//            auditSpITask.setProjectguid(UUID.randomUUID().toString());
            auditSpITask.setOperatedate(new Date());
            auditSpITask.setBiguid(biGuid);
            auditSpITask.setBusinessguid(businessGuid);
            auditSpITask.setPhaseguid(phaseGuid);
            auditSpITask.setRowguid(UUID.randomUUID().toString());
            auditSpITask.setSubappguid(subappGuid);
            auditSpITask.setTaskguid(taskGuid);
            auditSpITask.setTaskname(taskName);
            auditSpITask.setOrdernumber(orderNum);
            auditSpITask.setAreacode(areacode);
            auditSpITask.setReviewguid(reviewguid);
            auditSpITask.setSflcbsx(sflcbsx);
            auditSpService.addRecord(AuditSpITask.class, auditSpITask, false);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditSpITask>> getTaskInstanceBySubappGuid(String subappGuid) {
        AuditSpService<AuditSpITask> auditSpService = new AuditSpService<AuditSpITask>();
        AuditCommonResult<List<AuditSpITask>> result = new AuditCommonResult<List<AuditSpITask>>();
        try {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("subappguid", subappGuid);
            result.setResult(auditSpService.getAllRecord(AuditSpITask.class, sql.getMap()));

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> updateProjectGuid(String subappGuid, String taskGuid, String projectGuid) {
        AuditCommonResult<String> result = new AuditCommonResult<>();
        AuditSpITaskService spITaskService = new AuditSpITaskService();
        try {
            spITaskService.updateProjectGuid(subappGuid, taskGuid, projectGuid);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> updateProjectGuid(String rowguid, String projectGuid) {
        AuditCommonResult<String> result = new AuditCommonResult<>();
        AuditSpITaskService spITaskService = new AuditSpITaskService();
        try {
            spITaskService.updateProjectGuid(rowguid, projectGuid);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditSpITask>> getSpITaskByBIGuid(String biGuid) {
        AuditSpService<AuditSpITask> auditSpService = new AuditSpService<AuditSpITask>();
        AuditCommonResult<List<AuditSpITask>> result = new AuditCommonResult<List<AuditSpITask>>();
        try {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("biguid", biGuid);
            result.setResult(auditSpService.getAllRecord(AuditSpITask.class, sql.getMap()));

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditSpITask>> getSpITaskByPhaseGuid(String phaseGuid) {
        AuditSpService<AuditSpITask> auditSpService = new AuditSpService<AuditSpITask>();
        AuditCommonResult<List<AuditSpITask>> result = new AuditCommonResult<List<AuditSpITask>>();
        try {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("phaseguid", phaseGuid);
            result.setResult(auditSpService.getAllRecord(AuditSpITask.class, sql.getMap()));

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<String>> getTaskIDBySubappGuid(String subappGuid) {
        AuditCommonResult<List<String>> result = new AuditCommonResult<List<String>>();
        AuditSpITaskService spITaskService = new AuditSpITaskService();
        try {
            List<String> idlist = spITaskService.getTaskIDBySubappGuid(subappGuid);
            result.setResult(idlist);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<String>> getTaskIDByReviewguid(String reviewguid) {
        AuditCommonResult<List<String>> result = new AuditCommonResult<List<String>>();
        AuditSpITaskService spITaskService = new AuditSpITaskService();
        try {
            List<String> idlist = spITaskService.getTaskIDByReviewguid(reviewguid);
            result.setResult(idlist);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<String>> getProjectguidsBySubappGuid(String subappGuid) {
        AuditCommonResult<List<String>> result = new AuditCommonResult<List<String>>();
        AuditSpITaskService spITaskService = new AuditSpITaskService();
        try {
            List<String> idlist = spITaskService.getProjectguidsBySubappGuid(subappGuid);
            result.setResult(idlist);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<AuditSpITask> getAuditSpITaskDetail(String rowguid) {
        AuditCommonResult<AuditSpITask> result = new AuditCommonResult<AuditSpITask>();
        SQLManageUtil sqlm = new SQLManageUtil();
        result.setResult(sqlm.getBeanByOneField(AuditSpITask.class, "rowguid", rowguid));
        return result;
    }

    @Override
    public void updateAuditSpITask(AuditSpITask auditspitask) {
        AuditSpITaskService spITaskService = new AuditSpITaskService();
        spITaskService.updateAuditSpITask(auditspitask);
    }

    @Override
    public AuditCommonResult<List<AuditSpITask>> getAuditSpITaskListByCondition(Map<String, String> condition) {
        SQLManageUtil sqlm = new SQLManageUtil();
        AuditCommonResult<List<AuditSpITask>> result = new AuditCommonResult<List<AuditSpITask>>();
        result.setResult(sqlm.getListByCondition(AuditSpITask.class, condition));
        return result;
    }

    @Override
    public AuditCommonResult<List<String>> getStringListByCondition(String field, Map<String, String> condition) {
        AuditSpITaskService spITaskService = new AuditSpITaskService();
        AuditCommonResult<List<String>> result = new AuditCommonResult<>();
        result.setResult(spITaskService.getStringListByCondition(field, condition));
        return result;
    }

    @Override
    public AuditCommonResult<AuditSpITask> getAuditSpITaskByProjectGuid(String projectguid) {
        AuditCommonResult<AuditSpITask> result = new AuditCommonResult<>();
        SQLManageUtil sqlm = new SQLManageUtil();
        result.setResult(sqlm.getBeanByOneField(AuditSpITask.class, "projectguid", projectguid));
        return result;
    }

    @Override
    public AuditCommonResult<Integer> deleteSpITaskBySubappGuid(String subappGuid) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        AuditSpITaskService spITaskService = new AuditSpITaskService();
        try {
            int idlist = spITaskService.deleteSpITaskBySubappGuid(subappGuid);
            result.setResult(idlist);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Integer countByCondition(Map<String, String> conditionMap) {
        return new AuditSpITaskService().countByCondition(conditionMap);
    }

    @Override
    public AuditCommonResult<List<AuditSpITask>> getInitProjectTaskInstanceBySubappGuid(String subappGuid) {
        IAuditSpIMaterial auditSpIMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpIMaterial.class);
        IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        IAuditSpShareMaterialRelation spShareMaterialRelationService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpShareMaterialRelation.class);

        AuditSpService<AuditSpITask> auditSpService = new AuditSpService<AuditSpITask>();
        AuditCommonResult<List<AuditSpITask>> result = new AuditCommonResult<List<AuditSpITask>>();
        try {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("subappguid", subappGuid);
            sql.isBlankOrValue("pushstatus", "0");
            // 未推送生成办件的实例事项
            List<AuditSpITask> spITaskList = auditSpService.getAllRecord(AuditSpITask.class, sql.getMap());
            List<AuditSpITask> pushSpITaskList = new ArrayList<>();

            // 获取结果材料未提交的共享材料
            sql.clear();
            sql.eq("result", "1");// 结果材料
            sql.eq("status", ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT + "");// 未提交
            sql.eq("subappguid", subappGuid);
            List<AuditSpIMaterial> spIMaterialList = auditSpIMaterialService.getSpIMaterialByCondition(sql.getMap())
                    .getResult();

            // 存在结果材料未提交的情况：改材料所属的实例事项不推送生成办件
            if (spIMaterialList != null && !spIMaterialList.isEmpty()
                    && ValidateUtil.isNotBlankCollection(spITaskList)) {
                // 获取实例事项对应的所有办理事项
                List<String> taskguids = spITaskList.stream().map(s -> s.getTaskguid()).collect(Collectors.toList());
                sql.clear();
                sql.in("rowguid", "'" + StringUtil.join(taskguids, "','") + "'");
                sql.setSelectFields("rowguid,task_id");
                List<AuditTask> taskList = auditTaskService.getAllTask(sql.getMap()).getResult();

                // 主题guid
                String businessguid = spITaskList.get(0).getBusinessguid();

                // 获取主题下所有的共享材料
                sql.clear();
                sql.eq("businessguid", businessguid);
                sql.setSelectFields("rowguid,sharematerialguid,taskid,materialtype,businessguid");
                List<AuditSpShareMaterialRelation> allShareMaterialList = spShareMaterialRelationService
                        .getAuditSpShareMaterialByMap(sql.getMap()).getResult();

                // 过滤获取结果材料
                List<AuditSpShareMaterialRelation> resultShareMaterialList = allShareMaterialList.stream()
                        .filter(a -> "20".equals(a.getMaterialtype())).collect(Collectors.toList());
                // 遍历结果材料，获取结果材料对应的后置事项，为后置实例事项设置前置事项标识（taskid）
                for (AuditSpShareMaterialRelation auditSpShareMaterialRelation : resultShareMaterialList) {
                    // 获取该结果材料对应的后置共享材料
                    List<AuditSpShareMaterialRelation> noResultShareMaterialList = allShareMaterialList.stream()
                            .filter(a -> (!"20".equals(a.getMaterialtype()) && auditSpShareMaterialRelation
                                    .getSharematerialguid().equals(a.getSharematerialguid())))
                            .collect(Collectors.toList());

                    // 获取后置事项的taskid
                    List<String> taskids = noResultShareMaterialList.stream().map(n -> n.getTaskid())
                            .collect(Collectors.toList());

                    // 遍历后置事项
                    for (String taskid : taskids) {
                        // 获取后置事项对应的办理事项
                        List<AuditTask> tasks = taskList.stream().filter(t -> t.getTask_id().equals(taskid))
                                .collect(Collectors.toList());
                        if (ValidateUtil.isBlankCollection(tasks)) {
                            continue;
                        }
                        AuditTask task = tasks.get(0);
                        // 获取后置事项对应的实例事项
                        AuditSpITask spITask = spITaskList.stream()
                                .filter(s -> s.getTaskguid().equals(task.getRowguid())).collect(Collectors.toList())
                                .get(0);

                        // 为后置实例事项设置对应的前置事项标识
                        spITask.setPre_taskid(auditSpShareMaterialRelation.getTaskid());
                        this.updateAuditSpITask(spITask);

                        // 判断后置事项是否存在结果材料未提交
                        List<String> sharematerialguids = noResultShareMaterialList.stream()
                                .filter(n -> taskid.equals(n.getTaskid())).map(n -> n.getSharematerialguid())
                                .collect(Collectors.toList());
                        List<AuditSpIMaterial> filterSpIMaterialList = spIMaterialList.stream()
                                .filter(s -> sharematerialguids.contains(s.getMaterialguid()))
                                .collect(Collectors.toList());
                        if (filterSpIMaterialList != null && !filterSpIMaterialList.isEmpty()) {
                        }
                        else {
                            pushSpITaskList.add(spITask);
                        }
                    }

                    // 非后置事项
                    List<AuditTask> noRearTasklist = taskList.stream().filter(t -> !taskids.contains(t.getTask_id()))
                            .collect(Collectors.toList());
                    List<String> noRearTaskguids = noRearTasklist.stream().map(n -> n.getRowguid())
                            .collect(Collectors.toList());
                    pushSpITaskList.addAll(spITaskList.stream().filter(s -> noRearTaskguids.contains(s.getTaskguid()))
                            .collect(Collectors.toList()));
                }
            }
            // 不存在结果材料未提交的情况，全部实例事项都推送生成办件
            else {
                pushSpITaskList = spITaskList;
            }
            pushSpITaskList = pushSpITaskList.stream().filter(distinctByKey(AuditSpITask::getTaskguid))
                    .collect(Collectors.toList());

            result.setResult(pushSpITaskList);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @Override
    public AuditCommonResult<String> addTaskInstance(String businessGuid, String biGuid, String phaseGuid,
            String taskGuid, String taskName, String subappGuid, Integer orderNum, String areacode, String reviewguid,
            String sflcbsx, String townshipcode) {
        AuditSpService<AuditSpITask> auditSpService = new AuditSpService<AuditSpITask>();
        AuditSpITask auditSpITask = new AuditSpITask();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            if (StringUtil.isNotBlank(townshipcode)) {
                auditSpITask.set("township", "1");
                auditSpITask.set("townareacode", townshipcode);
            }
            else {
                auditSpITask.set("township", "0");
            }
//            auditSpITask.setProjectguid(UUID.randomUUID().toString());
            auditSpITask.setOperatedate(new Date());
            auditSpITask.setBiguid(biGuid);
            auditSpITask.setBusinessguid(businessGuid);
            auditSpITask.setPhaseguid(phaseGuid);
            auditSpITask.setRowguid(UUID.randomUUID().toString());
            auditSpITask.setSubappguid(subappGuid);
            auditSpITask.setTaskguid(taskGuid);
            auditSpITask.setTaskname(taskName);
            auditSpITask.setOrdernumber(orderNum);
            auditSpITask.setAreacode(areacode);
            auditSpITask.setReviewguid(reviewguid);
            auditSpITask.setSflcbsx(sflcbsx);
            auditSpService.addRecord(AuditSpITask.class, auditSpITask, false);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
