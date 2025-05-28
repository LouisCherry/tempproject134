package com.epoint.auditqueue.hardware.rest;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditqueue.hardware.api.IHardwarecall;
import com.epoint.auditqueue.hardware.service.HardwareCallService;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.domain.AuditOnlineEvaluat;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.inter.IAuditOnlineEvaluat;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowUser;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueueorgawindow.domain.AuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueueorgawindow.inter.IAuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueuetasktypetask.inter.IAuditQueueTasktypeTask;
import com.epoint.basic.auditqueue.auditqueueuserinfo.inter.IAuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbterminalapp.inter.IAuditZnsbTerminalApp;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.QueueConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditqueue.handlerabbitmq.inter.IHandleRabbitMQ;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.znsb.composite.auditqueue.handletoolbar.inter.JxIHandleToolBar;


@SuppressWarnings("unused")
@RestController
@RequestMapping("/queueHardwareCallNO")
public class QueueHardwareCallRestController
{
    @Autowired
    private IUserService userservice;

    @Autowired
    private IOuService ouservice;

    @Autowired
    private IAuditQueueOrgaWindow queuewindowservice;

    @Autowired
    private IAuditQueue queueservice;

    @Autowired
    private IAuditOrgaWindowYjs windowservice;

    @Autowired
    private IAuditProject projectservice;

    @Autowired
    private IAuditOrgaServiceCenter orgacenterservice;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;

    @Autowired
    private IAuditQueueTasktypeTask tasktypetaskservice;

    @Autowired
    private IAuditQueueUserinfo userinfoservice;

    @Autowired
    private IAuditOrgaWorkingDay workingDayService;

    @Autowired
    private IAuditProjectSparetime sparetimeService;

    @Autowired
    private IAuditZnsbEquipment equipmentservice;

    @Autowired
    private IAuditZnsbTerminalApp appservice;

        @Autowired
    private JxIHandleToolBar handletoolbarservice;

/*    @Autowired
    private IWzHandleToolBar handletoolbarservice;*/

    @Autowired
    private IHandleConfig handleConfigservice;

    @Autowired
    private IHandleRabbitMQ handlemqservice;

    @Autowired
    private IAuditOnlineEvaluat evaluateservice;

    @Autowired
    private IAttachService attachservice;

    @Autowired
    private IConfigService configservice;

    @Autowired
    private IHardwarecall hardwarecallservice;

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 硬件叫号器登录
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            String loginid = obj.getString("loginid");
            String password = obj.getString("password");
            String hardwareno = obj.getString("hardwareno");
            JSONObject dataJson = new JSONObject();
            //根据叫号器的编号找到对应的窗口
            AuditOrgaWindow window = hardwarecallservice.getWindowByMac(hardwareno);
            if (window == null) {
                return JsonUtils.zwdtRestReturn("0", "该叫号器的编号未绑定到窗口", "");
            }
            else {
                String centerguid = window.getCenterguid();
                String openHard = "0";
                openHard = handleConfigservice.getFrameConfig("AS_ZNSB_OPENHARD",centerguid).getResult();
                List<AuditOrgaWindowUser> list = hardwarecallservice
                        .getWindowOrgaWindowUserByWindowguid(window.getRowguid());
                if (list.size() == 0) {
                    return JsonUtils.zwdtRestReturn("0", "该窗口没有关联人员", "");
                }
                else if (list.size() > 1 && "0".equals(openHard)) {
                    return JsonUtils.zwdtRestReturn("0", "该窗口关联了多个人员，无法匹配", "");
                }
                else {
                    // 存入窗口redis缓存,默认为空闲状态
                    String userguid = list.get(0).getUserguid();
                    String name = list.get(0).getUsername();
                    handletoolbarservice.initQueueLogin(window.getRowguid(), userguid, name,
                            QueueConstant.Window_WorkStatus_Free);
                    // 评价pad推送
                    //handlemqservice.sendMQLoginbyEvaluate(window.getRowguid());
                    // 在线
                    handlemqservice.sendMQOnLinebyEvaluate(window.getRowguid());
                }
            }
            dataJson.put("success", "登录成功");
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 下一位
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/nextQueue", method = RequestMethod.POST)
    public String nextQueue(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String hardwareno = obj.getString("hardwareno");

            JSONObject dataJson = new JSONObject();
            //根据叫号器的编号找到对应的窗口
            AuditOrgaWindow window = hardwarecallservice.getWindowByMac(hardwareno);
            String windowguid = "";
            String windowno = "";
            String centerguid = "";
            if(StringUtil.isNotBlank(window)){
                windowguid = window.getRowguid();
                windowno = window.getWindowno();
                centerguid = window.getCenterguid();
            }

            if (window != null) {
                //2021-05-31 鱼台新增一个参数配置 可配置多个人员
                String openHard = "0";
                openHard = handleConfigservice.getFrameConfig("AS_ZNSB_OPENHARD",centerguid).getResult();
                //评价器取消暂停
                this.handlemqservice.sendMQOnLinebyEvaluate(windowguid);
                
                AuditQueueOrgaWindow queuewindow = queuewindowservice.getDetailbyWindowguid(windowguid).getResult();
                if(StringUtil.isNotBlank(queuewindow) && StringUtil.isNotBlank(queuewindow.getUserguid())){
                    
                }else{
                    List<AuditOrgaWindowUser> list = hardwarecallservice
                            .getWindowOrgaWindowUserByWindowguid(window.getRowguid());
                    if (list.size() == 0) {
                        return JsonUtils.zwdtRestReturn("0", "该窗口没有关联人员", "");
                    }
                    else if (list.size() > 1 && "0".equals(openHard)) {
                        return JsonUtils.zwdtRestReturn("0", "该窗口关联了多个人员，无法匹配", "");
                    }
                    else {
                        // 存入窗口redis缓存,默认为空闲状态
                        String userguid = list.get(0).getUserguid();
                        String name = list.get(0).getUsername();
                        handletoolbarservice.initQueueLogin(window.getRowguid(), userguid, name,
                                QueueConstant.Window_WorkStatus_Free);
                      
                    }
                }
                
                //下一位的时候发送当前号的评价信息
                //根据handlewindowguid以及状态未1找到当前窗口正在处理的排队号
                HardwareCallService hardservice = new HardwareCallService();
                String qno = hardservice.getQno(window.getRowguid());
                //String centerguid = window.getCenterguid();
                String fieldstr = " Rowguid ";
                if(StringUtil.isNotBlank(qno)){
                	 
                     
                	//发送评价
                	AuditQueue queue = queueservice.getQNODetailByQNO(fieldstr, qno, centerguid).getResult();
                    if (queue != null) {
                        // 先判断是否已评价
                        if (!evaluateservice.isExistEvaluate(queue.getRowguid()).getResult()) {
                        	AuditOnlineEvaluat evaluat;
                        	evaluat = new AuditOnlineEvaluat();
            				evaluat.setRowguid(UUID.randomUUID().toString());
            				evaluat.setEvaluatetype("30");
            				evaluat.setClientidentifier(queue.getRowguid());
            				evaluat.setClienttype("30");
            				evaluat.setEvaluatedate(new Date());
            				evaluat.setSatisfied("0");
            				this.evaluateservice.addAuditOnineEvaluat(evaluat);                	                        	
                            // rabbitmq推送
                            handlemqservice.sendMQEvaluatebyEvaluate(windowguid, queue.getRowguid(),
                                    ZwfwConstant.Evaluate_clienttype_QNO);
                        }
                        else {

                            //return JsonUtils.zwdtRestReturn("0", "该排队号已评价，请勿重复评价！", "");
                        }
                    }
                }
                                                             
                IAuditQueueOrgaWindow windowservicetemp = (IAuditQueueOrgaWindow) ContainerFactory.getContainInfo()
        				.getComponent(IAuditQueueOrgaWindow.class);
                windowservicetemp.updateWindowWorkStatus(window.getRowguid(), "1");
    			
                String nextqno = handletoolbarservice.nextQueue(hardservice.getQno(window.getRowguid()),
                        window.getRowguid(), window.getWindowno(), window.getCenterguid(),
                        queuewindowservice.getDetailbyWindowguid(window.getRowguid()).getResult().getUserguid(), true)
                        .getResult();
                dataJson.put("handleno", nextqno);
//                log.info("呼叫器"+hardwareno+"呼叫号:" + nextqno + "=======当前等待人数：=======" + StringUtil
//                        .getNotNullString(queueservice.getWindowWaitNum(window.getRowguid(), true).getResult()));
                dataJson.put("waitnum", StringUtil
                        .getNotNullString(queueservice.getWindowWaitNum(window.getRowguid(), true).getResult()));
            
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该叫号器的编号未绑定到窗口", "");
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (

        JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 重新呼叫
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/reCallQueue", method = RequestMethod.POST)
    public String reCallQueue(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String hardwareno = obj.getString("hardwareno");

            JSONObject dataJson = new JSONObject();
            //根据叫号器的编号找到对应的窗口
            AuditOrgaWindow window = hardwarecallservice.getWindowByMac(hardwareno);
            HardwareCallService hardservice = new HardwareCallService();
            //根据handlewindowguid以及状态未1找到当前窗口正在处理的排队号
            String qno = hardservice.getQno(window.getRowguid());
            // 重新呼叫
            log.info("呼叫器"+hardwareno+"重呼号:"+qno);
            IAuditQueueOrgaWindow windowservicetemp = (IAuditQueueOrgaWindow) ContainerFactory.getContainInfo()
    				.getComponent(IAuditQueueOrgaWindow.class);
            windowservicetemp.updateWindowWorkStatus(window.getRowguid(), "1");
            handletoolbarservice.reCallQueue(qno, window.getRowguid(), window.getWindowno(), window.getCenterguid());

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 评价
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/pingJiaQueue", method = RequestMethod.POST)
    public String pingJiaQueue(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String hardwareno = obj.getString("hardwareno");
            //根据叫号器的编号找到对应的窗口
            AuditOrgaWindow window = hardwarecallservice.getWindowByMac(hardwareno);
            String windowguid = window.getRowguid();
            String windowno = window.getWindowno();
            HardwareCallService hardservice = new HardwareCallService();
            //根据handlewindowguid以及状态未1找到当前窗口正在处理的排队号
            String qno = hardservice.getQno(window.getRowguid());
            String centerguid = window.getCenterguid();
            String fieldstr = " Rowguid ";
            JSONObject dataJson = new JSONObject();
            AuditQueue queue = queueservice.getQNODetailByQNO(fieldstr, qno, centerguid).getResult();
            if (queue != null) {
                // 先判断是否已评价
                if (!evaluateservice.isExistEvaluate(queue.getRowguid()).getResult()) {
                    // rabbitmq推送
                    handlemqservice.sendMQEvaluatebyEvaluate(windowguid, queue.getRowguid(),
                            ZwfwConstant.Evaluate_clienttype_QNO);
                }
                else {

                    return JsonUtils.zwdtRestReturn("0", "该排队号已评价，请勿重复评价！", "");
                }
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 自动刷新
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/autoRefreshQueue", method = RequestMethod.POST)
    public String autoRefreshQueue(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String hardwareno = obj.getString("hardwareno");
            //根据叫号器的编号找到对应的窗口
            AuditOrgaWindow window = hardwarecallservice.getWindowByMac(hardwareno);
            JSONObject dataJson = new JSONObject();
            if (window != null) {
               /* if (queuewindowservice.getDetailbyWindowguid(window.getRowguid()).getResult().getWorkstatus() == "1"
                        || queuewindowservice.getDetailbyWindowguid(window.getRowguid()).getResult()
                                .getWorkstatus() == "2") {
                    String userguid = queuewindowservice.getDetailbyWindowguid(window.getRowguid()).getResult()
                            .getUserguid();
                    String windowguid = window.getRowguid();
                    String windowno = window.getWindowno();
                    HardwareCallService hardservice = new HardwareCallService();
                    //根据handlewindowguid以及状态未1找到当前窗口正在处理的排队号
                    String qno = hardservice.getQno(window.getRowguid());
                    String centerguid = window.getCenterguid();
                    String waitnum = StringUtil
                            .getNotNullString(queueservice.getWindowWaitNum(windowguid, true).getResult());
                    String nextqno;

                    // 判断当前窗口状态是否为空闲，并且当前无办理人员
                    AuditQueueOrgaWindow queuewindow = queuewindowservice.getDetailbyWindowguid(windowguid).getResult();

                    if (queuewindow != null
                            && QueueConstant.Window_WorkStatus_Free.equals(queuewindow.getWorkstatus())) {
                        if ((StringUtil.isBlank(qno) || QueueConstant.Window_Bar_status_None.equals(qno))
                                && StringUtil.isNotBlank(queuewindow.getWaitnum())
                                && Integer.parseInt(queuewindow.getWaitnum()) > 0) {
                            // 取出下一个号
                            nextqno = handletoolbarservice.getNextQNO(windowguid, windowno, centerguid, userguid, true)
                                    .getResult();

                        }
                        else {
                            nextqno = qno;

                        }
                    }
                    else {
                        nextqno = qno;

                    }
                    dataJson.put("handleno", nextqno);
                    dataJson.put("waitnum", handletoolbarservice.getWindowWaitNumAuto(windowguid, waitnum).getResult());
                }*/
                            
                dataJson.put("waitnum", StringUtil
                        .getNotNullString(queueservice.getWindowWaitNum(window.getRowguid(), true).getResult()));
                List<AuditOrgaWindowUser> list = hardwarecallservice
                        .getWindowOrgaWindowUserByWindowguid(window.getRowguid());
              /*  if (list.size() == 0) {
                    return JsonUtils.zwdtRestReturn("0", "该窗口没有关联人员", "");
                }
                else if (list.size() > 1) {
                    return JsonUtils.zwdtRestReturn("0", "该窗口关联了多个人员，无法匹配", "");
                }
                else {
                    // 存入窗口redis缓存,默认为空闲状态
                    String userguid = list.get(0).getUserguid();
                    String name = list.get(0).getUsername();
                    handletoolbarservice.initQueueLogin(window.getRowguid(), userguid, name,
                            QueueConstant.Window_WorkStatus_Free);
                }*/
                return JsonUtils.zwdtRestReturn("1", "", dataJson);
            }
            else {
                    return JsonUtils.zwdtRestReturn("0", "该叫号器的编号未绑定到窗口", "");            
            }
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    /**
     * 暂停
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/stopQueue", method = RequestMethod.POST)
    public String stopQueue(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String hardwareno = obj.getString("hardwareno");

            JSONObject dataJson = new JSONObject();
            //根据叫号器的编号找到对应的窗口
            AuditOrgaWindow window = hardwarecallservice.getWindowByMac(hardwareno);
           /* HardwareCallService hardservice = new HardwareCallService(); */  
        	handletoolbarservice.pauseQueue( window.getRowguid(), window.getWindowno(), window.getCenterguid());
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }


}
