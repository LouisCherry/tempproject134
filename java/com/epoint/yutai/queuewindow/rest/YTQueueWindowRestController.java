package com.epoint.yutai.queuewindow.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueueorgawindow.domain.AuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueueorgawindow.inter.IAuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueuewindow.domain.AuditQueueWindow;
import com.epoint.basic.auditqueue.auditqueuewindow.inter.IAuditQueueWindow;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.domain.AuditQueueWindowTasktype;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.yutai.queuewindow.api.IYTAuditQueue;
import com.epoint.zhenggai.api.ZhenggaiService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping({"/ytqueueWindow" })
public class YTQueueWindowRestController
{
    @Autowired
    private IAuditOrgaWindowYjs windowservice;
    @Autowired
    private IAuditQueueOrgaWindow queuewindowservice;
    @Autowired
    private IAuditQueue queueservice;

    @Autowired
    private IAuditQueueWindow auditqueuewindowservice;
    @Autowired
    private IOuService ouservice;

    @Autowired
    private IUserService userservice;

    @Autowired
    private IAuditZnsbEquipment equipmentservice;
    @Autowired
    private IYTAuditQueue iytAuditQueue;

    @Autowired
    private ZhenggaiService zhenggaiImpl;

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 获取大厅等待情况
     *
     * @params params
     * @return
     *
     *
     */
    @RequestMapping(value = "/getHallQueue", method = RequestMethod.POST)
    public String getHallQueue(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String hallguid = obj.getString("hallguid");
            String MacAddress = obj.getString("MacAddress");
            List<JSONObject> list = new ArrayList<JSONObject>();
            List<String> waitnolist = new ArrayList<String>();
            Integer totalwait = 0;
            JSONObject queueJson = new JSONObject();

            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("LOBBYTYPE", hallguid);
            sql.eq("IS_USEQUEUE", "1");
            List<String> listwinguid=new ArrayList<String>();
            AuditZnsbEquipment equipmnet=equipmentservice.getDetailbyMacaddress(MacAddress).getResult();
            listwinguid=findwindowguid(equipmnet.getRowguid());
            sql.in("rowguid", "'"+StringUtil.join(listwinguid, "','")+"'");
            List<AuditOrgaWindow> lstRecord = new ArrayList<AuditOrgaWindow>();
            List<AuditOrgaWindow> windowList = windowservice.getAllWindow(sql.getMap()).getResult();
            AuditQueueOrgaWindow queuewindow;
            for (AuditOrgaWindow window : windowList) {
                try{
                    queuewindow = queuewindowservice.getDetailbyWindowguid(window.getRowguid()).getResult();
                    if(StringUtil.isNotBlank(queuewindow)){
                        if (StringUtil.isNotBlank(queuewindow.getCurrenthandleqno())
                                || queueservice.getWindowWaitNum(window.getRowguid(), true).getResult() > 0) {
                            lstRecord.add(window);
                        }
                    }else{
                        log.info("大厅异常窗口" + window.getRowguid());
                    }
                }catch (Exception e){
                    log.info("==============大厅异常窗口" + window.getRowguid() + "执行异常："+ e);
                }

            }
            Collections.sort(lstRecord, new Comparator<AuditOrgaWindow>()
            {
                @Override
                public int compare(AuditOrgaWindow o1, AuditOrgaWindow o2) {
                    if (o1.getWindowno() == null) {
                        return -1;
                    }
                    else if (o2.getWindowno() == null) {
                        return 1;
                    }
                    else {
                        return o1.getWindowno().compareTo(o2.getWindowno());
                    }
                }
            });

            for (AuditOrgaWindow Record : lstRecord) {
                queueJson = new JSONObject();
                queuewindow = queuewindowservice.getDetailbyWindowguid(Record.getRowguid()).getResult();
                queueJson.put("currentno", StringUtil
                        .getNotNullString(queueservice.getCurrentHandleNO(Record.getRowguid(), true).getResult()));

                queueJson.put("windowno", Record.getWindowno());
                queueJson.put("windowname", Record.getWindowname());
                queueJson.put("ouname",
                        StringUtil.isNotBlank(ouservice.getOuByOuGuid(Record.getOuguid()).getOushortName())
                                ? ouservice.getOuByOuGuid(Record.getOuguid()).getOushortName()
                                : ouservice.getOuByOuGuid(Record.getOuguid()).getOuname());
                String waitcount = StringUtil
                        .getNotNullString(queueservice.getWindowWaitNum(Record.getRowguid(), true).getResult());
                String waitqno = "";
                List<String> waitqnoList = iytAuditQueue.getWaitQno(queuewindow.getQueuevalue(),Record.getRowguid());
                if(waitqnoList.size() > 0){
                    waitqno = waitqnoList.get(0);
                }
                queueJson.put("waitqno", waitqno);
                queueJson.put("waitcount", waitcount);
                totalwait += Integer.parseInt(waitcount);
                list.add(queueJson);
            }
            String[] hallguids = hallguid.split(",");
            for (String eachhallguid : hallguids) {
                waitnolist.addAll(
                        queueservice.getQnoByHallAndStatus(eachhallguid, QueueConstant.Qno_Status_Init).getResult());
            }
            dataJson.put("queuelist", list);
            dataJson.put("waitnolist", waitnolist);
            dataJson.put("totalwait", totalwait);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    // 从audit_queue_window表中获取windowguid
    public List<String> findwindowguid(String equmentguid){
        List<String> windowguid=zhenggaiImpl.getwindowbyguid(equmentguid);
        return windowguid;
    }
}
