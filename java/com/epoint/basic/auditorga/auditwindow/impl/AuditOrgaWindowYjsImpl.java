package com.epoint.basic.auditorga.auditwindow.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epoint.basic.auditorga.service.AuditOrgaService;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditorga.auditwindow.entiy.AuditOrgaWindowYjs;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowTask;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowUser;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditorga.auditwindow.service.AuditOrgaWindowTaskService;
import com.epoint.basic.auditorga.auditwindow.service.AuditOrgaWindowUserService;
import com.epoint.basic.auditorga.auditwindow.service.AuditOrgaWindowYjsService;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.common.rabbitmq.ProducerMQ;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.service.AuditCommonService;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.entity.FrameUser;

/**
 * 
 * 窗口基础服务实现类
 * 
 * @author Administrator
 * @version [版本号, 2016年11月23日]
 * 
 * 
 */
@Component
@Service
public class AuditOrgaWindowYjsImpl implements IAuditOrgaWindowYjs
{

    // start 数据新增
    @Override
    public AuditCommonResult<String> insertWindow(AuditOrgaWindow auditWindow, String centerGuid) {
        AuditOrgaService<AuditOrgaWindow> auditWindowService = new AuditOrgaService<AuditOrgaWindow>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();

        try {
            // 先判断窗口编号是否存在
            SqlConditionUtil sql = new SqlConditionUtil();
            if (!StringUtil.isBlank(auditWindow.getWindowno())) {
                sql.eq("windowno", auditWindow.getWindowno());
            }
            sql.eq("centerGuid", centerGuid);
            List<AuditOrgaWindow> listWindow = this.getAllWindow(sql.getMap()).getResult();
            if (listWindow.size() > 0) {
                result.setBusinessFail("窗口编号" + ZwfwConstant.BUSINESSERROR_REPEAT);
                return result;
            }

            // 判断窗口Mac是否存在
            if (!StringUtil.isBlank(auditWindow.getMac())) {
                SqlConditionUtil sqlwindow = new SqlConditionUtil();
                sqlwindow.eq("mac", auditWindow.getMac());
                listWindow = this.getAllWindow(sqlwindow.getMap()).getResult();
                if (listWindow.size() > 0) {
                    result.setBusinessFail("窗口电脑MAC" + ZwfwConstant.BUSINESSERROR_REPEAT);
                    return result;
                }
            }

            auditWindowService.addRecord(AuditOrgaWindow.class, auditWindow, true);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> insertWindow(AuditOrgaWindowYjs auditWindow) {
        AuditOrgaService<AuditOrgaWindowYjs> auditWindowService = new AuditOrgaService<AuditOrgaWindowYjs>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();

        try {
            auditWindowService.addRecord(AuditOrgaWindowYjs.class, auditWindow, true);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> insertWindowUser(AuditOrgaWindowUser auditWindowUser) {
        AuditOrgaService<AuditOrgaWindowUser> auditWindowUserService = new AuditOrgaService<AuditOrgaWindowUser>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            // 先判断窗口人员是否存在
            /*
             * AuditOrgaWindow window =
             * this.getWindowByUserGuid(auditWindowUser.getUserguid()).getResult
             * (); if (StringUtil.isNotBlank(window))
             * result.setBusinessFail("窗口人员" +
             * ZwfwConstant.BUSINESSERROR_REPEAT); else {
             * auditWindowUserService.addRecord(AuditOrgaWindowUser.class,
             * auditWindowUser, false); }
             */
            auditWindowUserService.addRecord(AuditOrgaWindowUser.class, auditWindowUser, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> insertWindowTask(AuditOrgaWindowYjs auditWindowTask) {
    	AuditOrgaWindowYjsService auditWindowTaskService = new AuditOrgaWindowYjsService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            
            List<AuditOrgaWindowYjs> windowTaskList = auditWindowTaskService.getYjsListByRowguid(auditWindowTask.getTaskguid(), auditWindowTask.getWindowguid());
            if (windowTaskList.size() > 0) {
                result.setBusinessFail("窗口事项" + ZwfwConstant.BUSINESSERROR_REPEAT);
            }
            else {
                auditWindowTaskService.insertWindow(auditWindowTask);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // end

    // start 数据查询

    @Override
    public AuditCommonResult<List<AuditOrgaWindow>> getWindowByPage(Map<String, String> conditionMap, int first,
            int pageSize, String sortField, String sortOrder) {
        AuditOrgaService<AuditOrgaWindow> auditWindowService = new AuditOrgaService<AuditOrgaWindow>();
        AuditCommonResult<List<AuditOrgaWindow>> result = new AuditCommonResult<List<AuditOrgaWindow>>();
        try {
            List<AuditOrgaWindow> windowList = auditWindowService.getAllRecordByPage(AuditOrgaWindow.class,
                    conditionMap, first, pageSize, sortField, sortOrder);
            result.setResult(windowList);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditOrgaWindow>> getSomeFieldsByPage(Map<String, String> conditionMap, int first,
            int pageSize, String sortField, String sortOrder, String fields) {
        AuditCommonService auditWindowService = new AuditCommonService();
        AuditCommonResult<List<AuditOrgaWindow>> result = new AuditCommonResult<List<AuditOrgaWindow>>();
        try {
            List<AuditOrgaWindow> windowList = auditWindowService.getSomeFieldsByPage(AuditOrgaWindow.class,
                    conditionMap, first, pageSize, sortField, sortOrder, fields);
            result.setResult(windowList);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getWindowCount(Map<String, String> conditionMap) {
        AuditOrgaService<AuditOrgaWindow> auditWindowService = new AuditOrgaService<AuditOrgaWindow>();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(auditWindowService.getAllRecordCount(AuditOrgaWindow.class, conditionMap));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Integer getSomeFieldsCount(Map<String, String> conditionMap, String fields) {
        AuditCommonService auditWindowService = new AuditCommonService();
        return auditWindowService.getListByCondition(AuditOrgaWindow.class, conditionMap, fields).size();
    }

    @Override
    public AuditCommonResult<List<AuditOrgaWindow>> getAllWindow(Map<String, String> conditionMap) {
        AuditOrgaService<AuditOrgaWindow> auditWindowService = new AuditOrgaService<AuditOrgaWindow>();
        AuditCommonResult<List<AuditOrgaWindow>> result = new AuditCommonResult<List<AuditOrgaWindow>>();
        try {
            // List<AuditWindow> returnList =
            // getAllWindow(conditionMap).getResult();
            List<AuditOrgaWindow> returnList = auditWindowService.getAllRecord(AuditOrgaWindow.class, conditionMap);
            result.setResult(returnList);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<AuditOrgaWindow> getWindowByUserGuid(String userGuid) {
        AuditOrgaService<AuditOrgaWindowUser> auditWindowUserService = new AuditOrgaService<AuditOrgaWindowUser>();
        AuditOrgaService<AuditOrgaWindow> auditWindowService = new AuditOrgaService<AuditOrgaWindow>();
        AuditCommonResult<AuditOrgaWindow> result = new AuditCommonResult<AuditOrgaWindow>();
        try {
            AuditOrgaWindow window = null;
            AuditOrgaWindowUser windowUser = null;
            windowUser = auditWindowUserService.getDetail(AuditOrgaWindowUser.class, userGuid, "userguid", false);
            if (StringUtil.isNotBlank(windowUser)) {
                window = auditWindowService.getDetail(AuditOrgaWindow.class, windowUser.getWindowguid(), "rowguid",
                        true);
            }
            result.setResult(window);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public AuditCommonResult<List<AuditOrgaWindow>> getWindowListByUserGuid(String userGuid) {
        AuditOrgaWindowYjsService windowservice = new AuditOrgaWindowYjsService();
        AuditCommonResult<List<AuditOrgaWindow>> result = new AuditCommonResult<List<AuditOrgaWindow>>();
        try {
            result.setResult(windowservice.getWindowListByUserGuid(userGuid));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public AuditCommonResult<List<AuditOrgaWindow>> getWindowListByUserGuidAndCondition(String userGuid,
            Map<String, String> conditionMap) {
        AuditOrgaWindowYjsService windowservice = new AuditOrgaWindowYjsService();
        AuditCommonResult<List<AuditOrgaWindow>> result = new AuditCommonResult<List<AuditOrgaWindow>>();
        try {
            result.setResult(windowservice.getWindowListByUserGuidAndCondition(userGuid, conditionMap));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditOrgaWindow>> getWindowListByTaskId(String taskid) {
        AuditOrgaWindowYjsService windowservice = new AuditOrgaWindowYjsService();
        AuditCommonResult<List<AuditOrgaWindow>> result = new AuditCommonResult<List<AuditOrgaWindow>>();
        try {
            result.setResult(windowservice.getWindowListByTaskId(taskid));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public AuditCommonResult<AuditOrgaWindow> getWindowByWindowGuid(String windowGuid) {
        AuditOrgaService<AuditOrgaWindow> auditWindowService = new AuditOrgaService<AuditOrgaWindow>();
        AuditCommonResult<AuditOrgaWindow> result = new AuditCommonResult<AuditOrgaWindow>();
        try {
            AuditOrgaWindow window = auditWindowService.getDetail(AuditOrgaWindow.class, windowGuid, "rowguid", false);
            result.setResult(window);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public AuditCommonResult<List<AuditOrgaWindowUser>> getUserByWindow(String windowGuid) {
        AuditOrgaService<AuditOrgaWindowUser> auditWindowUserService = new AuditOrgaService<AuditOrgaWindowUser>();
        AuditCommonResult<List<AuditOrgaWindowUser>> result = new AuditCommonResult<List<AuditOrgaWindowUser>>();
        try {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("windowguid", windowGuid);
            List<AuditOrgaWindowUser> windowUserList = auditWindowUserService.getAllRecord(AuditOrgaWindowUser.class,
                    sql.getMap());
            result.setResult(windowUserList);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditOrgaWindowUser>> getUserPageData(Map<String, String> conditionMap, int first,
            int pageSize) {
        AuditOrgaService<AuditOrgaWindowUser> auditWindowUserService = new AuditOrgaService<AuditOrgaWindowUser>();
        AuditCommonResult<PageData<AuditOrgaWindowUser>> result = new AuditCommonResult<PageData<AuditOrgaWindowUser>>();
        try {

            PageData<AuditOrgaWindowUser> windowUserPageData = auditWindowUserService
                    .getRecordPageData(AuditOrgaWindowUser.class, conditionMap, first, pageSize, "", "");
            result.setResult(windowUserPageData);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditOrgaWindowYjs>> getTaskByWindow(String windowGuid) {
    	AuditOrgaWindowYjsService auditWindowTaskService = new AuditOrgaWindowYjsService();
        AuditCommonResult<List<AuditOrgaWindowYjs>> result = new AuditCommonResult<List<AuditOrgaWindowYjs>>();
        try {
            List<AuditOrgaWindowYjs> windowTaskList =auditWindowTaskService.getAllYjs(windowGuid);
            result.setResult(windowTaskList);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditOrgaWindowTask>> getTaskByTaskguid(String taskguid) {
        AuditOrgaService<AuditOrgaWindowTask> auditWindowTaskService = new AuditOrgaService<AuditOrgaWindowTask>();
        AuditCommonResult<List<AuditOrgaWindowTask>> result = new AuditCommonResult<List<AuditOrgaWindowTask>>();
        try {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("taskguid", taskguid);
            List<AuditOrgaWindowTask> windowTaskList = auditWindowTaskService.getAllRecord(AuditOrgaWindowTask.class,
                    sql.getMap());
            result.setResult(windowTaskList);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditOrgaWindowTask>> getTaskByWindowAndTaskId(String windowGuid, String taskId) {
        AuditOrgaService<AuditOrgaWindowTask> auditWindowTaskService = new AuditOrgaService<AuditOrgaWindowTask>();
        AuditCommonResult<List<AuditOrgaWindowTask>> result = new AuditCommonResult<List<AuditOrgaWindowTask>>();
        try {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("windowguid", windowGuid);
            sql.eq("taskid", taskId);
            List<AuditOrgaWindowTask> windowTaskList = auditWindowTaskService.getAllRecord(AuditOrgaWindowTask.class,
                    sql.getMap());
            result.setResult(windowTaskList);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // end

    // start 数据修改
    @Override
    public AuditCommonResult<String> updateAuditWindow(AuditOrgaWindow auditwindow, String preWindowNo, String preMAC,
            String centerGuid) {
        AuditOrgaService<AuditOrgaWindow> auditWindowService = new AuditOrgaService<AuditOrgaWindow>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            // 先判断窗口编号是否存在
            if (preWindowNo != null && !preWindowNo.equalsIgnoreCase(auditwindow.getWindowno())) {
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("windowno", auditwindow.getWindowno());
                sql.eq("centerGuid", centerGuid);
                List<AuditOrgaWindow> listWindow = this.getAllWindow(sql.getMap()).getResult();
                if (listWindow.size() > 0) {
                    result.setBusinessFail("窗口编号" + ZwfwConstant.BUSINESSERROR_REPEAT);
                    return result;
                }
            }
            // 判断窗口Mac是否存在
            if (StringUtil.isNotBlank(auditwindow.getMac()) && !preMAC.equals(auditwindow.getMac())) {
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("mac", auditwindow.getMac());

                List<AuditOrgaWindow> listWindow = this.getAllWindow(sql.getMap()).getResult();
                if (listWindow.size() > 0) {
                    result.setBusinessFail("窗口电脑MAC" + ZwfwConstant.BUSINESSERROR_REPEAT);
                    return result;
                }
            }
            auditWindowService.updateRecord(AuditOrgaWindow.class, auditwindow, "rowguid", true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    // end


    @Override
    public AuditCommonResult<String> deleteWindowTaskByWindowGuid(String windowGuid) {
    	AuditOrgaService<AuditOrgaWindowYjs> auditWindowTaskService = new AuditOrgaService<AuditOrgaWindowYjs>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            auditWindowTaskService.deleteWindowTaskByWindowGuid(windowGuid);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    /**
     * 根据事项标识删除重口配置的事项
     */
    public AuditCommonResult<String> deleteWindowTaskByTaskGuid(String taskGuid) {
    	AuditOrgaWindowYjsService auditWindowTaskService = new AuditOrgaWindowYjsService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            //删除的不是窗口事项，无需处理
            auditWindowTaskService.deleteWindowTaskByTaskGuid(taskGuid);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Void> deleteWindowByWindowGuid(String windowGuid) {
        AuditOrgaService<AuditOrgaWindowTask> auditWindowTaskService = new AuditOrgaService<AuditOrgaWindowTask>();
        AuditCommonResult<Void> result = new AuditCommonResult<Void>();
        try {
            auditWindowTaskService.deleteWindowByWindowGuid(windowGuid);
        }
        catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
        }
        return result;
    }
    // end

    @Override
    public AuditCommonResult<List<AuditOrgaWindowUser>> getWindowUser(Map<String, String> conditionMap) {
        AuditOrgaService<AuditOrgaWindowUser> auditWindowUser = new AuditOrgaService<AuditOrgaWindowUser>();
        AuditCommonResult<List<AuditOrgaWindowUser>> result = new AuditCommonResult<List<AuditOrgaWindowUser>>();
        try {
            List<AuditOrgaWindowUser> returnList = auditWindowUser.getAllRecord(AuditOrgaWindowUser.class,
                    conditionMap);
            result.setResult(returnList);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<FrameOu>> selectFrameOu() {
        AuditOrgaWindowTaskService windowTaskService = new AuditOrgaWindowTaskService();
        AuditCommonResult<List<FrameOu>> result = new AuditCommonResult<List<FrameOu>>();
        try {
            List<FrameOu> returnList = windowTaskService.selectFrameOu();
            result.setResult(returnList);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> updateWindowTask(AuditOrgaWindowTask auditWindowTask) {
        AuditOrgaService<AuditOrgaWindowTask> auditWindowTaskService = new AuditOrgaService<AuditOrgaWindowTask>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            auditWindowTaskService.updateRecord(AuditOrgaWindowTask.class, auditWindowTask, "rowguid", false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> updateWindowTaskByTaskId(String Taskid, String newtaskguid) {
        AuditOrgaService<AuditOrgaWindowTask> auditWindowTaskService = new AuditOrgaService<AuditOrgaWindowTask>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("TASKID", Taskid);
            List<AuditOrgaWindowTask> auditOrgaWindowTask = auditWindowTaskService
                    .getAllRecord(AuditOrgaWindowTask.class, sql.getMap());
            for (AuditOrgaWindowTask windowtaskexp : auditOrgaWindowTask) {
                windowtaskexp.setTaskguid(newtaskguid);
                auditWindowTaskService.updateRecord(AuditOrgaWindowTask.class, windowtaskexp, "rowguid", false);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<String>> getTaskidsByWindow(String windowGuid) {
        AuditOrgaService<AuditOrgaWindowTask> auditWindowTaskService = new AuditOrgaService<AuditOrgaWindowTask>();
        AuditCommonResult<List<String>> result = new AuditCommonResult<List<String>>();
        try {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("windowguid", windowGuid);
            List<AuditOrgaWindowTask> windowTaskList = auditWindowTaskService.getAllRecord(AuditOrgaWindowTask.class,
                    sql.getMap());
            List<String> taskids = new ArrayList<>();
            for (AuditOrgaWindowTask auditWindowTask : windowTaskList) {
                taskids.add(auditWindowTask.getTaskid());
            }
            result.setResult(taskids);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<String>> getTaskGuidsByWindow(String windowGuid) {
        AuditOrgaService<AuditOrgaWindowTask> auditWindowTaskService = new AuditOrgaService<AuditOrgaWindowTask>();
        AuditCommonResult<List<String>> result = new AuditCommonResult<List<String>>();
        try {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("windowguid", windowGuid);
            List<AuditOrgaWindowTask> windowTaskList = auditWindowTaskService.getAllRecord(AuditOrgaWindowTask.class,
                    sql.getMap());
            List<String> taskguids = new ArrayList<>();
            for (AuditOrgaWindowTask auditWindowTask : windowTaskList) {
                taskguids.add(auditWindowTask.getTaskguid());
            }
            result.setResult(taskguids);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditOrgaWindowTask>> getWindowTaskPageData(Map<String, String> conditionMap,
            int first, int pageSize, String sortField, String sortOrder) {
        AuditOrgaService<AuditOrgaWindowTask> auditWindowTaskService = new AuditOrgaService<AuditOrgaWindowTask>();
        AuditCommonResult<PageData<AuditOrgaWindowTask>> result = new AuditCommonResult<PageData<AuditOrgaWindowTask>>();
        try {
            result.setResult(auditWindowTaskService.getRecordPageData(AuditOrgaWindowTask.class, conditionMap, first,
                    pageSize, sortField, sortOrder));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<String>> getTaskIdListByCenter(String centerGuid) {

        AuditOrgaWindowTaskService auditWindowTaskService = new AuditOrgaWindowTaskService();
        AuditCommonResult<List<String>> result = new AuditCommonResult<List<String>>();
        try {

            List<String> taskIDs = auditWindowTaskService.getTaskIdListByCenter(centerGuid);

            result.setResult(taskIDs);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getEnableTaskListByCenter(String centerGuid) {

        AuditOrgaWindowTaskService auditWindowTaskService = new AuditOrgaWindowTaskService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {

            Integer taskIDs = auditWindowTaskService.getEnableTaskListByCenter(centerGuid);

            result.setResult(taskIDs);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getEnableAndChargeTaskListByCenterCount(String centerGuid) {

        AuditOrgaWindowTaskService auditWindowTaskService = new AuditOrgaWindowTaskService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {

            Integer taskIDs = auditWindowTaskService.getEnableAndChargeTaskListByCenterCount(centerGuid);

            result.setResult(taskIDs);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Boolean> isExistbyTaskId(String taskid, String centerguid) {
        AuditOrgaWindowTaskService auditWindowTaskService = new AuditOrgaWindowTaskService();
        AuditCommonResult<Boolean> result = new AuditCommonResult<Boolean>();
        try {
            result.setResult(auditWindowTaskService.isExistbyTaskId(taskid, centerguid));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getWindowCountByUserGuid(String userGuid) {
        AuditOrgaWindowYjsService windowservice = new AuditOrgaWindowYjsService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(windowservice.getWindowCountByUserGuid(userGuid));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> getWindowByMacandUserGuid(String Mac, String userGuid) {
        AuditOrgaWindowYjsService windowservice = new AuditOrgaWindowYjsService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            result.setResult(windowservice.getWindowByMacandUserGuid(Mac, userGuid));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> selectCenterGuidsByTaskId(String taskId) {
        AuditOrgaWindowYjsService windowservice = new AuditOrgaWindowYjsService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            List<Record> centerGuids = windowservice.selectCenterGuidsByTaskId(taskId);
            result.setResult(centerGuids);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<String>> getOUPageDatabyHall(String centerguid, String hallguid, int first,
            int pageSize) {
        AuditOrgaWindowYjsService windowservice = new AuditOrgaWindowYjsService();
        AuditCommonResult<PageData<String>> result = new AuditCommonResult<PageData<String>>();
        try {
            PageData<String> pageData = new PageData<String>();
            List<String> oulist = windowservice.getOUListbyHall(centerguid, hallguid, first, pageSize);
            int dataCount = windowservice.getOUListCountbyHall(centerguid, hallguid);
            pageData.setList(oulist);
            pageData.setRowCount(dataCount);
            result.setResult(pageData);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<String>> getOUbyHall(String centerguid, String hallguid) {
        AuditOrgaWindowYjsService windowservice = new AuditOrgaWindowYjsService();
        AuditCommonResult<List<String>> result = new AuditCommonResult<List<String>>();
        try {

            List<String> oulist = windowservice.getOUListbyHall(centerguid, hallguid);
            result.setResult(oulist);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditOrgaWindow>> getWindowPageData(Map<String, String> conditionMap, int first,
            int pageSize, String sortField, String sortOrder) {
        AuditOrgaService<AuditOrgaWindow> auditWindowService = new AuditOrgaService<AuditOrgaWindow>();

        AuditCommonResult<PageData<AuditOrgaWindow>> result = new AuditCommonResult<PageData<AuditOrgaWindow>>();
        try {
            PageData<AuditOrgaWindow> windowPageData = auditWindowService.getRecordPageData(AuditOrgaWindow.class,
                    conditionMap, first, pageSize, sortField, sortOrder);
            result.setResult(windowPageData);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<FrameUser>> getFrameUserByTaskID(String taskID) {
        AuditOrgaWindowUserService windowUserService = new AuditOrgaWindowUserService();
        AuditCommonResult<List<FrameUser>> result = new AuditCommonResult<List<FrameUser>>();
        try {
            List<FrameUser> returnList = windowUserService.getFrameUserByTaskID(taskID);
            result.setResult(returnList);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getWindowUserByCenterGuid(String centerGuid) {
        AuditOrgaWindowUserService auditOrgaWindowUserService = new AuditOrgaWindowUserService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(auditOrgaWindowUserService.getWindowUserByCenterGuid(centerGuid));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> getOuguidByWindowGuid(String windowguid) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        if (StringUtil.isNotBlank(windowguid)) {
            String ouguid = new AuditOrgaWindowYjsService().getOuguidByWindowGuid(windowguid);
            result.setResult(ouguid);
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<String>> getoulistBycenterguid(String centerGuid) {
        AuditCommonResult<List<String>> result = new AuditCommonResult<List<String>>();
        List<String> stringlist = new ArrayList<String>();
        if (StringUtil.isNotBlank(centerGuid)) {
            List<Record> recordlist = new AuditOrgaWindowYjsService().getoulistBycenterguid(centerGuid);
            if (recordlist.size() > 0) {
                for (Record record : recordlist) {
                    stringlist.add(record.getStr("ouguid"));
                }
            }
        }
        result.setResult(stringlist);
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditOrgaWindow>> getWindowListByOU(String ouguid) {
        AuditCommonResult<List<AuditOrgaWindow>> result = new AuditCommonResult<List<AuditOrgaWindow>>();
        result.setResult(new AuditOrgaWindowYjsService().getWindowListByOU(ouguid));
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditOrgaWindow>> selectByCenter(String conndition, String center) {
        AuditCommonResult<List<AuditOrgaWindow>> result = new AuditCommonResult<List<AuditOrgaWindow>>();
        result.setResult(new AuditOrgaWindowYjsService().selectByCenter(conndition, center));
        return result;
    }


    @Override
    public AuditCommonResult<List<AuditOrgaWindow>> getAllByCenter(String centerGuid) {
        AuditCommonResult<List<AuditOrgaWindow>> result = new AuditCommonResult<List<AuditOrgaWindow>>();
        result.setResult(new AuditOrgaWindowYjsService().getAllByCenter(centerGuid));
        return result;
    }

    @Override
    public AuditCommonResult<Void> deleteWindowByWindowGuid(String windowGuid, String centerGuid) {
        AuditOrgaService<AuditOrgaWindowTask> auditWindowTaskService = new AuditOrgaService<AuditOrgaWindowTask>();
        AuditCommonResult<Void> result = new AuditCommonResult<Void>();
        try {
            auditWindowTaskService.deleteWindowByWindowGuid(windowGuid);
            String RabbitMQMsg = "handleCenterWindowTask:" + "delete" + ";" + windowGuid + ";" + centerGuid
                    + "/com.epoint.auditjob.rabbitmqhandle.MQHandleCenterTask";
            ProducerMQ.TopicPublish("auditorgewindowtask", "delete", RabbitMQMsg, true);
        }
        catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<String>> getWindowGuidByUserName(String windowpeople) {
        AuditCommonResult<List<String>> result = new AuditCommonResult<List<String>>();
        result.setResult(new AuditOrgaWindowUserService().getWindowGuidByUserName(windowpeople));
        return result;
    }

    @Override
    public AuditCommonResult<List<String>> getWindowGuidByTaskName(String windowtask) {
        AuditCommonResult<List<String>> result = new AuditCommonResult<List<String>>();
        result.setResult(new AuditOrgaWindowTaskService().getWindowGuidByTaskName(windowtask));
        return result;
    }

    @Override
    public AuditCommonResult<AuditOrgaWindowTask> findWindowTask(String rowguid) {

        AuditCommonResult<AuditOrgaWindowTask> result = new AuditCommonResult<AuditOrgaWindowTask>();
        result.setResult(new AuditOrgaWindowTaskService().findWindowTask(rowguid));
        return result;
    }

    @Override
    public AuditCommonResult<List<FrameUser>> getFrameUserByWindowGuid(String windowGuid) {
        AuditOrgaWindowUserService windowUserService = new AuditOrgaWindowUserService();
        AuditCommonResult<List<FrameUser>> result = new AuditCommonResult<List<FrameUser>>();
        try {
            List<FrameUser> returnList = windowUserService.getFrameUserByWindow(windowGuid);
            result.setResult(returnList);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 
     * 根据窗口对应中心唯一标识查询对应的第一个窗口
     * 
     * @param centerguid
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public AuditOrgaWindow getFirstWindowByCenterGuid(String centerguid) {
        return new AuditOrgaWindowYjsService().getFirstWindowByCenterGuid(centerguid);
    }

    /**
     * 插入窗口与事项关联数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public void insert(AuditOrgaWindowYjs record) {
        new AuditOrgaWindowYjsService().insertWindow(record);
    }
    
    /**
     * 
     *  根据中心编码删除配置在该中心下的窗口 
     *  @param centerguid    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public int deleteWindowByCenterguid(String centerguid) {
        return new AuditOrgaWindowTaskService().deleteWindowByCenterguid(centerguid);
        
    }
    @Override
    public List<AuditSpBusiness> getBusinessByAreacode(String areacode,String businessname) {
    	return new AuditOrgaWindowYjsService().getBusinessByAreacode(areacode,businessname);
    	
    }
    @Override
    public List<AuditOrgaArea> getAreacodeList(String areacode) {
    	return new AuditOrgaWindowYjsService().getAreacodeList(areacode);
    	
    }
    @Override
    public List<AuditSpBusiness> getBusinessDetailByAreacode(String areacode) {
    	return new AuditOrgaWindowYjsService().getBusinessDetailByAreacode(areacode);
    	
    }
    @Override
    public List<AuditSpBusiness> getBusinessDetailByRowguid(String rowguid) {
    	return new AuditOrgaWindowYjsService().getBusinessDetailByRowguid(rowguid);
    	
    }
    
    public int deleteByGuid(String guid) {
        return new AuditOrgaWindowYjsService().deleteByGuid(guid);
    }
    

}
