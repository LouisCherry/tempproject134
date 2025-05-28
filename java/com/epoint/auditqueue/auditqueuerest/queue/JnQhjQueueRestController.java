package com.epoint.auditqueue.auditqueuerest.queue;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditqueue.qhj.api.IJNTasktypeList;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueueuserinfo.domain.AuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditqueueuserinfo.inter.IAuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditqueue.handlequeue.inter.IHandleQueue;
import com.epoint.composite.auditqueue.handlequeue.inter.IJNHandleQueue;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

@RestController
@RequestMapping("/jnqhjqueue")
public class JnQhjQueueRestController
{
    @Autowired
    private IAuditZnsbEquipment equipmentservice;

    @Autowired
    private IJNHandleQueue jnhandlequeueservice;

    @Autowired
    private IAuditQueueUserinfo userinfoservice;

    @Autowired
    private IAuditQueue queueservice;

    @Autowired
    private IAuditQueueTasktype tasktypeservice;

    @Autowired
    private IHandleQueue handlequeueservice;

    @Autowired
    private IMessagesCenterService messageservice;

    @Autowired
    private IConfigService configService;

    @Autowired
    private IAuditOrgaServiceCenter centerservice;

    @Autowired
    private IAuditOrgaWindow windowservice;

    @Autowired
    private IJNTasktypeList jntasktypeservice;

    @Autowired
    private IOuService ouservice;

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    TestCode128C code128c = new TestCode128C();

    /**
     * 取号
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getQno", method = RequestMethod.POST)
    public String getQno(@RequestBody String params, HttpServletRequest request) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String cardno = obj.getString("cardno");
            String phone = obj.getString("phone");
            String tasktypeguid = obj.getString("tasktypeguid");
            String centerguid = obj.getString("centerguid");
            String hallguid = obj.getString("hallguid");
            String macaddress = obj.getString("macaddress");
            // 当前号是否是爱心取号模式
            String islove = obj.getString("islove");
            JSONObject dataJson = new JSONObject();

            double alertlength = 0.0;
            int leftpaperpiece = 0;
            String operatorusermobile = "";

            AuditZnsbEquipment equipment = equipmentservice
                    .getDetailbyMacaddress(macaddress, " Alertlength,Leftpaperpiece,operatorusermobile,MACHINENO")
                    .getResult();
            if (StringUtil.isNotBlank(equipment)) {
                alertlength = StringUtil.isBlank(equipment.getAlertlength()) ? 0.0 : equipment.getAlertlength();
                leftpaperpiece = StringUtil.isBlank(equipment.getLeftpaperpiece()) ? 0 : equipment.getLeftpaperpiece();
                operatorusermobile = equipment.getOperatorusermobile();
                // 发送预警短信
                if (StringUtil.isNotBlank(operatorusermobile)) {
                    if (leftpaperpiece == 1) {
                        messageservice.insertSmsMessage(UUID.randomUUID().toString(),
                                "设备编号：" + equipment.getMachineno() + "，剩余纸张数量为0，请管理员赶紧添加纸张！", new Date(), 0, new Date(),
                                operatorusermobile, UUID.randomUUID().toString(), "", "", "", "", "", "", false, "");
                    }
                    if (leftpaperpiece == (int) (alertlength + 1)) {
                        messageservice.insertSmsMessage(UUID.randomUUID().toString(),
                                "设备编号：" + equipment.getMachineno() + "，剩余纸张数量已达报警长度，请管理员注意！", new Date(), 0, new Date(),
                                operatorusermobile, UUID.randomUUID().toString(), "", "", "", "", "", "", false, "");
                    }
                }
            }

            // 判断黑名单功能是否开启
            String useBlackList = configService.getFrameConfigValue("AS_IS_USE_BLACKLIST");
            if (StringUtil.isNotBlank(useBlackList) && QueueConstant.CONSTANT_STR_ONE.equals(useBlackList)) {
                // 获取当前身份证对应的用户信息
                AuditQueueUserinfo userinfo = userinfoservice.getUserinfo(cardno).getResult();
                if (StringUtil.isNotBlank(userinfo)) {
                    // 判断是永久禁用，还是封禁一段时间
                    String is_foreverclose = userinfo.getIs_foreverclose();
                    Date blacklisttodate = userinfo.getBlacklisttodate();
                    if (StringUtil.isNotBlank(blacklisttodate)
                            && QueueConstant.CONSTANT_STR_ZERO.equals(is_foreverclose)) {
                        return JsonUtils.zwdtRestReturn("0", "您当前在黑名单中,无法取号,解禁时间为"
                                + EpointDateUtil.convertDate2String(blacklisttodate, "yyyy-MM-dd"), "");
                    }
                    else if ((StringUtil.isNotBlank(is_foreverclose)
                            && QueueConstant.CONSTANT_STR_ONE.equals(is_foreverclose))) {
                        return JsonUtils.zwdtRestReturn("0", "您当前在黑名单中被永久禁用,无法取号", "");
                    }
                }
            }
            String existqno = queueservice.existQno(cardno, tasktypeguid).getResult();
            if (StringUtil.isBlank(existqno)) {
                Map<String, String> msg = new HashMap<String, String>();
                msg = jnhandlequeueservice.getQNO(cardno, phone, tasktypeguid, centerguid, hallguid, islove)
                        .getResult();
                if (msg.get("msg") != null && "success".equals(msg.get("msg"))) {
                    dataJson.put("qno", msg.get("qno"));
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", msg.get("msg"), "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "您有一个号码事项尚未办理，编号为" + existqno + ",请勿重复取号！", "");
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            log.info(e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 打印
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/printQno", method = RequestMethod.POST)
    public String printQno(@RequestBody String params, HttpServletRequest request) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String qno = obj.getString("qno");
            String macaddress = obj.getString("macaddress");
            String centerguid = obj.getString("centerguid");
            JSONObject dataJson = new JSONObject();

            // 减纸
            equipmentservice.descLeftPiece(macaddress);
            // 总打印+1
            String fieldstr = " Taskguid,Qno,Handlewindowno,Flowno,identitycardnum ";
            AuditQueue auditqueue = queueservice.getQNODetailByQNO(fieldstr, qno, centerguid).getResult();

            if (auditqueue == null) {
                try {
                    Thread.sleep(1500);
                    auditqueue = queueservice.getQNODetailByQNO(fieldstr, qno, centerguid).getResult();
                    log.info("小票打印 auditqueue 后：" + auditqueue);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (StringUtil.isNotBlank(auditqueue)) {
                AuditQueueTasktype auditqueuetasktype = tasktypeservice
                        .getAuditQueueTasktypeByRowguid(auditqueue.getTaskguid()).getResult();
                if (StringUtil.isNotBlank(auditqueuetasktype)) {
                    dataJson.put("qno", auditqueue.getQno());
                    dataJson.put("PaiDuiPrint",
                            centerservice.findAuditServiceCenterByGuid(centerguid).getResult().getCentername());
                    dataJson.put("taskname", auditqueuetasktype.getTasktypename());
                    dataJson.put("waitnum", StringUtil.getNotNullString(
                            handlequeueservice.getTaskWaitNum(auditqueue.getTaskguid(), true).getResult()));
                    dataJson.put("windowno", auditqueue.getHandlewindowno());
                    dataJson.put("time", EpointDateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
                    String barCode = code128c.getCode(auditqueue.getFlowno(), "");
                    dataJson.put("flowno", code128c.kiCode128C(barCode, 40, auditqueue.getFlowno() + ".jpg"));
                    dataJson.put("flownonum", auditqueue.getFlowno());
                    dataJson.put("identitycardnum", auditqueue.getIdentitycardnum());
                    return JsonUtils.zwdtRestReturn("1", "", dataJson);
                }
                return JsonUtils.zwdtRestReturn("0", "auditqueuetasktype 错误", dataJson);
            }
            return JsonUtils.zwdtRestReturn("0", "auditqueue 错误", dataJson);
        }
        catch (JSONException e) {
            log.info(e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 获取事项类别列表
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getTaskTypeList", method = RequestMethod.POST)
    public String getTaskTypeList(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String centerguid = obj.getString("centerguid");
            String ouguid = obj.getString("ouguid");
            Integer currentPage = Integer.valueOf(obj.getString("currentpage"));
            Integer pageSize = Integer.valueOf(obj.getString("pagesize"));

            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject tasktypeJson = new JSONObject();

            List<Record> lists = jntasktypeservice.getWindowLinkedTskType(centerguid, ouguid, currentPage * pageSize,
                    pageSize);

            int totalcount = jntasktypeservice.getWindowLinkedTskTypeCount(centerguid, ouguid);
            for (Record tasktype : lists) {
                tasktypeJson = new JSONObject();
                tasktypeJson.put("tasktypeguid", tasktype.getStr("rowguid"));
                tasktypeJson.put("tasktypename", tasktype.getStr("tasktypename"));
                tasktypeJson.put("taskwaitnum", StringUtil.getNotNullString(
                        handlequeueservice.getTaskWaitNum(tasktype.getStr("rowguid"), true).getResult()));
                list.add(tasktypeJson);
            }

            dataJson.put("tasktypelist", list);
            dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            log.info(e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取取号部门列表
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getOUList", method = RequestMethod.POST)
    public String getOUList(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String centerguid = obj.getString("centerguid");
            String hallguid = obj.getString("hallguid");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");

            FrameOu ou = null;
            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject ouJson = new JSONObject();
            List<String> ouguidlist = new ArrayList<String>();

            // 多大厅情况
            String[] hallguids = hallguid.split(",");
            for (String eachhallguid : hallguids) {
                ouguidlist.addAll(windowservice.getOUbyHall(centerguid, eachhallguid).getResult());
            }

            // 使用 HashSet 去重
            Set<String> set = new HashSet<>(ouguidlist);

            // 将去重后的 Set 转回 ArrayList
            ArrayList<String> uniqueList = new ArrayList<>(set);

            for (String ouguid : uniqueList) {
                ouJson = new JSONObject();
                ouJson.put("ouguid", ouguid);

                ou = ouservice.getOuByOuGuid(ouguid);
                if (StringUtil.isNotBlank(ou)) {
                    ouJson.put("ouname",
                            StringUtil.isNotBlank(ou.getOushortName()) ? ou.getOushortName() : ou.getOuname());
                    ouJson.put("ougnum",
                            StringUtil.isNotBlank(ouservice.getOuByOuGuid(ouguid).getOrderNumber())
                                    ? ouservice.getOuByOuGuid(ouguid).getOrderNumber()
                                    : 0);
                }
                else {
                    ouJson.put("ouname", "");
                    ouJson.put("ougnum", 0);
                }

                list.add(ouJson);
            }
            // 根据ougnum对部门数据进行降序排序
            Collections.sort(list,
                    (JSONObject l1, JSONObject l2) -> l2.getIntValue("ougnum") - l1.getIntValue("ougnum"));
            // 截取对应页面的部门list数据
            int firstint = Integer.parseInt(currentPage) * Integer.parseInt(pageSize);
            int endint = (firstint + Integer.parseInt(pageSize)) >= list.size() ? list.size()
                    : (firstint + Integer.parseInt(pageSize));
            List<JSONObject> rtnlist = list.subList(firstint, endint);

            int totalcount = list.size();

            dataJson.put("oulist", rtnlist);
            dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            log.info(e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

}
