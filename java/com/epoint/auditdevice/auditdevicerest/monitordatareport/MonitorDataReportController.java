package com.epoint.auditdevice.auditdevicerest.monitordatareport;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditdevice.auditdevicerest.monitordatareport.api.IJNMonitorDataReport;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditqueue.auditznsbabnormalshutdown.domain.AuditZnsbAbnormalShutdown;
import com.epoint.basic.auditqueue.auditznsbabnormalshutdown.inter.IAuditZnsbAbnormalShutdown;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbinstrutions.inter.IAuditZnsbInstrutions;
import com.epoint.basic.auditqueue.auditznsbmachineproblem.domain.AuditZnsbMachineproblem;
import com.epoint.basic.auditqueue.auditznsbmachineproblem.inter.IAuditZnsbMachineproblem;
import com.epoint.basic.auditqueue.auditznsbmonitor.domain.AuditZnsbMonitor;
import com.epoint.basic.auditqueue.auditznsbmonitor.inter.IAuditZnsbMonitor;
import com.epoint.basic.auditqueue.auditznsbmonitorpointconfig.inter.IAuditZnsbMonitorpointconfig;
import com.epoint.basic.faces.fileupload.MultipartRequest;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

@RestController
@RequestMapping("/monitorDataReport")
public class MonitorDataReportController
{

    @Autowired
    private IAuditZnsbEquipment equipmentservice;

    @Autowired
    private IAttachService attachservice;

    @Autowired
    private IAuditZnsbMonitor monitorService;

    @Autowired
    private IAuditZnsbInstrutions instrutionsservice;

    @Autowired
    private IAuditZnsbMachineproblem machineproblemservice;
    @Autowired
    private IAuditOrgaServiceCenter auditOrgaServiceCenter;
    @Autowired
    private IConfigService configservice;

    @Autowired
    private IHandleConfig handleConfigservice;

    @Autowired
    private IAuditZnsbAbnormalShutdown abnormalshutdownservice;

    @Autowired
    private IAuditZnsbMonitorpointconfig monitorpointconfigservice;
    @Autowired
    private IJNMonitorDataReport reportService;
    private Logger log = Logger.getLogger(MonitorDataReportController.class);

    /**
     * 上传截屏图片
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/uploadscreenshot", method = RequestMethod.POST)
    public String uploadscreenshot(HttpServletRequest request) {
        try {
            // 从multipartRequest获取POST的流文件并保存到数据库
            request = (MultipartRequest) request;
            String macaddress = request.getParameter("macaddress");
            savescreenshotAttach((MultipartRequest) request, macaddress);
            return JsonUtils.zwdtRestReturn("1", "上传成功", "");
        }
        catch (Exception e) {
            log.error(e);
            return JsonUtils.zwdtRestReturn("0", "上传失败：" + e.getMessage(), "");
        }
    }

    public void savescreenshotAttach(MultipartRequest multipartRequest, String macaddress) throws IOException {
        FrameAttachInfo frameAttachInfo = null;
        Map<String, List<FileItem>> map = multipartRequest.getFileParams();
        for (Map.Entry<String, List<FileItem>> en : map.entrySet()) {
            List<FileItem> fileItems = en.getValue();
            if (fileItems != null && !fileItems.isEmpty()) {
                for (FileItem fileItem : fileItems) {
                    if (!fileItem.isFormField()) {// 是文件流而不是表单数据

                        // 根据Macaddress判断是否存在
                        AuditZnsbEquipment equipment = reportService.getMachinelikebyMacaddress(macaddress);
                        if (StringUtil.isNotBlank(equipment)) {

                            String fileName;
                            // 从文件流中获取文件名
                            fileName = fileItem.getName();
                            int index = fileName.lastIndexOf('\\');
                            fileName = fileName.substring(++index);

                            // 从文件流中获取文件类型
                            String contentType = fileItem.getContentType();
                            // 获取流大小
                            long size = fileItem.getSize();
                            // 获取流
                            InputStream inputStream = fileItem.getInputStream();

                            String CliengGuid;
                            if (StringUtil.isNotBlank(equipment.getScreenShotGuid())) {
                                CliengGuid = equipment.getScreenShotGuid();
                                // 删除原先图片
                                attachservice.deleteAttachByGuid(CliengGuid);
                            }
                            else {
                                CliengGuid = UUID.randomUUID().toString();
                                // 更新设备表图片guid
                                equipmentservice.updateScreenShotGuid(macaddress, CliengGuid);
                            }
                            // 附件信息
                            frameAttachInfo = new FrameAttachInfo();
                            frameAttachInfo.setCliengGuid(CliengGuid);
                            frameAttachInfo.setAttachFileName(fileName);
                            frameAttachInfo.setCliengTag("智能化截屏图片");
                            frameAttachInfo.setUploadUserGuid("");
                            frameAttachInfo.setUploadUserDisplayName("");
                            frameAttachInfo.setUploadDateTime(new Date());
                            frameAttachInfo.setContentType(contentType);
                            frameAttachInfo.setAttachLength(size);
                            attachservice.addAttach(frameAttachInfo, inputStream);

                        }
                    }
                }
            }
        }

    }

    @RequestMapping(value = "/getMonitorInfo", method = RequestMethod.POST)
    public String getMonitorInfo(@RequestBody String params, HttpServletRequest request) throws Exception {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String macaddress = obj.getString("macaddress");
            String monitortime = obj.getString("monitortime");
            String machinetype = obj.getString("machinetype");
            String centerguid = "";
            Boolean ishistory = false;
            String CurrentStatus = QueueConstant.EQUIPMENT_CurrentStatus_Normal;
            Date monitordate;

            String monitorguid = UUID.randomUUID().toString();

            if (StringUtil.isNotBlank(monitortime)) {
                // 如果存在时间，则代表是历史数据
                monitordate = EpointDateUtil.convertString2Date(monitortime);
                ishistory = true;
            }
            else {
                monitordate = new Date();
            }
            JSONObject dataJson = new JSONObject();
            AuditZnsbEquipment testequipment = reportService.getMachinelikebyMacaddress(macaddress);
            if (testequipment != null) {
                macaddress = testequipment.getMacaddress();
            }
            // 根据Macaddress判断是否存在
            if (!equipmentservice.IsTerminalRegister(macaddress).getResult()) {
                // 判断是否配置运维监控独立部署参数
                if (QueueConstant.CONSTANT_STR_ONE
                        .equals(handleConfigservice.getFrameConfig("AS_ZNSB_INDEPENDENT", "").getResult())) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("belongxiaqu", "333333");
                    List<AuditOrgaServiceCenter> centerlist = auditOrgaServiceCenter
                            .getAuditOrgaServiceCenterByCondition(sql.getMap()).getResult();
                    // 判断是否存在独立部署中心
                    if (!centerlist.isEmpty()) {
                        // 自动配置设备信息
                        AuditZnsbEquipment auditZnsbEquipment = new AuditZnsbEquipment();
                        String equipmentguid = UUID.randomUUID().toString();
                        int machineno = equipmentservice.getTotalCountByCenter(centerlist.get(0).getRowguid())
                                .getResult() + 1;
                        auditZnsbEquipment.setMachinename(
                                configservice.getFrameConfigValue("AS_ZNSB_PROJECTMANAGENO") + machineno);
                        auditZnsbEquipment
                                .setMachineno(configservice.getFrameConfigValue("AS_ZNSB_PROJECTMANAGENO") + machineno);
                        auditZnsbEquipment.setMacaddress(macaddress);
                        auditZnsbEquipment.setMachinetype(machinetype);
                        auditZnsbEquipment.setCenterguid(centerlist.get(0).getRowguid());
                        auditZnsbEquipment.setCentername(centerlist.get(0).getCentername());
                        auditZnsbEquipment.setStatus(QueueConstant.CONSTANT_STR_ONE);// 状态
                        auditZnsbEquipment.setRowguid(equipmentguid);
                        auditZnsbEquipment.setOperatedate(new Date());
                        equipmentservice.insertEquipment(auditZnsbEquipment);
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "独立部署中心未配置！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该设备未注册！", "");
                }
            }
            JSONArray monitorListJson = obj.getJSONArray("monitorlist");
            AuditZnsbMonitor monitor;
            for (int i = 0; i < monitorListJson.size(); i++) {
                monitor = new AuditZnsbMonitor();
                monitor.setRowguid(UUID.randomUUID().toString());
                monitor.setMacaddress(macaddress);
                monitor.setMonitortime(monitordate);
                monitor.setMonitorpoint(monitorListJson.getJSONObject(i).getString("monitorpoint"));
                monitor.setStatus(monitorListJson.getJSONObject(i).getString("status"));
                monitor.setMonitornote(monitorListJson.getJSONObject(i).getString("monitornote"));
                monitor.setMonitortype(monitorListJson.getJSONObject(i).getString("monitortype"));
                if (!ishistory) {
                    monitor.setMonitorguid(monitorguid);
                }
                // 判断是否存在异常,如果有一个检测点为异常则设备状态为异常即使存在预警
                if (!QueueConstant.EQUIPMENT_CurrentStatus_Normal
                        .equals(monitorListJson.getJSONObject(i).getString("status"))
                        && !QueueConstant.EQUIPMENT_CurrentStatus_Error.equals(CurrentStatus)) {
                    CurrentStatus = monitorListJson.getJSONObject(i).getString("status");
                }
                // 存在问题，插入问题表，同类问题，一天只插入一次。
                if (!QueueConstant.EQUIPMENT_CurrentStatus_Normal
                        .equals(monitorListJson.getJSONObject(i).getString("status"))
                        && !machineproblemservice
                                .existTodaySameProblem(macaddress,
                                        monitorListJson.getJSONObject(i).getString("monitorpoint"), monitordate)
                                .getResult()) {

                    AuditZnsbEquipment equipment = equipmentservice
                            .getDetailbyMacaddress(macaddress, "centerguid,machinetype").getResult();
                    if (StringUtil.isBlank(centerguid)) {
                        centerguid = equipment.getCenterguid();
                    }
                    AuditZnsbMachineproblem Machineproblem = new AuditZnsbMachineproblem();
                    Machineproblem.setRowguid(UUID.randomUUID().toString());
                    Machineproblem.setMacaddress(macaddress);
                    Machineproblem.setMonitorPoint(monitorListJson.getJSONObject(i).getString("monitorpoint"));
                    Machineproblem.setMonitorNote(monitorListJson.getJSONObject(i).getString("monitornote"));
                    Machineproblem.setCreateTime(monitordate);
                    Machineproblem.setMachinestatus(monitorListJson.getJSONObject(i).getString("status"));
                    Machineproblem.setProblemstatus(QueueConstant.Common_no_String);
                    Machineproblem.setProblemcontent(monitorListJson.getJSONObject(i).getString("monitorpoint")
                            + monitorListJson.getJSONObject(i).getString("monitornote"));
                    Machineproblem.setCenterguid(centerguid);

                    // 判断是否为忽略问题
                    String isignore = monitorpointconfigservice.getIsIgnoreByPointAndTypeAndCenterguid(
                            monitorListJson.getJSONObject(i).getString("monitorpoint"), equipment.getMachinetype(),
                            centerguid).getResult();
                    Machineproblem.setIsignore(isignore);

                    machineproblemservice.insertMachineproblem(Machineproblem);
                }
                monitorService.insert(monitor);
            }
            if (!ishistory) {
                equipmentservice.updateMonitorStatus(macaddress, CurrentStatus, monitorguid);
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);

        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/uploadInstrutionsResult", method = RequestMethod.POST)
    public String getInstrutionsInfo(@RequestBody String params, HttpServletRequest request) throws Exception {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String instrutionsguid = obj.getString("instrutionsguid");
            JSONObject dataJson = new JSONObject();

            instrutionsservice.updateStatus(instrutionsguid, QueueConstant.CONSTANT_STR_ONE, new Date());

            return JsonUtils.zwdtRestReturn("1", "", dataJson);

        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/keepalive", method = RequestMethod.POST)
    public String keepalive(@RequestBody String params, HttpServletRequest request) throws Exception {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String macaddress = obj.getString("macaddress");

            JSONObject dataJson = new JSONObject();
            AuditZnsbEquipment testequipment = reportService.getMachinelikebyMacaddress(macaddress);
            if (testequipment != null) {
                macaddress = testequipment.getMacaddress();
            }
            // 根据Macaddress判断是否存在
            if (equipmentservice.IsTerminalRegister(macaddress).getResult()) {
                equipmentservice.keepalive(macaddress);

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该设备未注册！", "");
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);

        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/uploadabnormalshutdown", method = RequestMethod.POST)
    public String uploadabnormalshutdown(@RequestBody String params, HttpServletRequest request) throws Exception {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String macaddress = obj.getString("macaddress");
            String shutdowntime = obj.getString("shutdowntime");
            JSONObject dataJson = new JSONObject();

            AuditZnsbEquipment equipment = reportService.getMachinelikebyMacaddress(macaddress);
            if (StringUtil.isNotBlank(equipment) && StringUtil.isNotBlank(equipment.getCenterguid())) {
                // 根据时间跟macaddress判断是否已经存过改数据
                if (!abnormalshutdownservice
                        .exitAbnormalShutdown(macaddress, EpointDateUtil.convertString2Date(shutdowntime))
                        .getResult()) {
                    AuditZnsbAbnormalShutdown AbnormalShutdown = new AuditZnsbAbnormalShutdown();
                    AbnormalShutdown.setRowguid(UUID.randomUUID().toString());
                    AbnormalShutdown.setCenterGuid(equipment.getCenterguid());
                    AbnormalShutdown.setMacAddress(macaddress);
                    AbnormalShutdown.setShutDownTime(EpointDateUtil.convertString2Date(shutdowntime));
                    abnormalshutdownservice.insert(AbnormalShutdown);
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该设备未注册或者配置！", "");
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);

        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
}
