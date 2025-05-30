package com.epoint.auditqueue.auditqueuerest.queue;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.audithall.domain.AuditOrgaHall;
import com.epoint.basic.auditorga.audithall.inter.IAuditOrgaHall;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowUser;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueueappointment.domain.AuditQueueAppointment;
import com.epoint.basic.auditqueue.auditqueueappointment.inter.IAuditQueueAppointment;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktypetask.inter.IAuditQueueTasktypeTask;
import com.epoint.basic.auditqueue.auditqueueuserinfo.inter.IAuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.domain.AuditQueueWindowTasktype;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.inter.IAuditQueueWindowTasktype;
import com.epoint.basic.auditqueue.auditqueuexianhaotime.domain.AuditQueueXianhaotime;
import com.epoint.basic.auditqueue.auditqueuexianhaotime.inter.IAuditQueueXianhaotime;
import com.epoint.basic.auditqueue.auditznsbappconfig.domain.AuditZnsbAppConfig;
import com.epoint.basic.auditqueue.auditznsbappconfig.inter.IAuditZnsbAppConfig;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbterminalapp.domain.AuditZnsbTerminalApp;
import com.epoint.basic.auditqueue.auditznsbterminalapp.inter.IAuditZnsbTerminalApp;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditqueue.handlequeue.inter.IHandleQueue;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.frame.service.organ.user.entity.FrameUserExtendInfo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/dvmqueue" })
public class DvmQueueRestController
{

    @Autowired
    private IHandleQueue handlequeueservice;
  
    @Autowired
    private IAuditQueueTasktype tasktypeservice;
    @Autowired
    private IAuditQueueTasktypeTask tasktyptaskeservice;

    @RequestMapping(value = {"/getQno" }, method = {RequestMethod.POST })
    public String getQno(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String taskname = obj.getString("taskname");
            String sql = "select  linkedtaskguid  from audit_dmv_task where taskname = '" + taskname + "'";
            ICommonDao commonDao = CommonDao.getInstance();
            String taskid = commonDao.queryString(sql);
            JSONObject dataJson = new JSONObject();
            if (StringUtil.isNotBlank(taskid)) {

                List<String> tasktypeguids = tasktyptaskeservice.getTaskTypeguidbyTaskID(taskid).getResult();
                if (tasktypeguids.size() > 0) {
                    AuditQueueTasktype tasktype = tasktypeservice.getTasktypeByguid(tasktypeguids.get(0)).getResult();
                    if (tasktype != null) {
                        Map<String, String> msg = (Map) this.handlequeueservice
                                .getQNO("", "", tasktype.getRowguid(), tasktype.getCenterguid(), "all").getResult();
                        if (msg.get("msg") != null && "success".equals(msg.get("msg"))) {
                            dataJson.put("qno", msg.get("qno"));
                            return JsonUtils.zwdtRestReturn("1", "", dataJson);
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", (String) msg.get("msg"), "");
                        }
                    
                    }else{
                        return JsonUtils.zwdtRestReturn("0", "事项未关联事项分类", "");
                    }
                }else{
                    return JsonUtils.zwdtRestReturn("0", "事项未关联事项分类", "");
                }

            }else{
                return JsonUtils.zwdtRestReturn("0", "无此事项", "");
            }

        }
        catch (JSONException var13) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + var13.getMessage(), "");
        }
    }

}
