package com.epoint.auditselfservice.auditselfservicerest.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditqueue.auditznsbaccesscabinet.domain.AuditZnsbAccesscabinet;
import com.epoint.basic.auditqueue.auditznsbaccesscabinet.inter.IAuditZnsbAccesscabinet;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsblogoconfig.domain.AuditZnsbLogoconfig;
import com.epoint.basic.auditqueue.auditznsblogoconfig.inter.IAuditZnsbLogoconfigService;
import com.epoint.basic.auditqueue.auditznsbmachinelogo.domain.AuditZnsbMachineLogo;
import com.epoint.basic.auditqueue.auditznsbmachinelogo.inter.IAuditZnsbMachineLogoService;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

@RestController
@RequestMapping("/jnselfservicecommon")
public class JNCommonRestController
{

    @Autowired
    private IAuditZnsbEquipment equipmentservice;

    @Autowired
    private IAuditOrgaServiceCenter servicecenterservice;

    @Autowired
    private IHandleConfig handleConfigservice;
    @Autowired
    private IAuditZnsbAccesscabinet accessCabinetService;
    /*
     * @Autowired private IConfigService configServcie;
     */

    @Autowired
    private IAuditOrgaArea areaService;

    @Autowired
    private IAttachService attachService;

    @Autowired
    private IAuditZnsbMachineLogoService machineLogoService;

    @Autowired
    private IAuditZnsbLogoconfigService logoConfigService;

    /**
     * 初始化
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public String init(@RequestBody String params, @Context HttpServletRequest request) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String macaddress = obj.getString("macaddress");
            String logomachinetype = obj.getString("logomachinetype");
            JSONObject dataJson = new JSONObject();
            String centerguid = "";

            // 根据Macaddress判断是否存在，不存在自动插入一条记录
            if (!equipmentservice.IsTerminalRegister(macaddress).getResult()) {
                AuditZnsbEquipment equipment = new AuditZnsbEquipment();
                equipment.setMacaddress(macaddress);
                equipment.setStatus(QueueConstant.EQUIPMENT_STATUS_ONLINE);
                equipment.setMachinetype(QueueConstant.EQUIPMENT_TYPE_YTJ);
                equipment.setRowguid(UUID.randomUUID().toString());
                equipment.setOperatedate(new Date());
                equipment.setXiaodayindevice("w80");
                equipment.setDadayindevice("Kyocera ECOSYS P4040dn KX");
                equipment.setColordayindevice("HP ColorLaserJet M253-M254 PCL 6");
                equipment.setBawleftpaperone(1);
                equipment.setBawleftpapertwo(1);
                equipment.setColorleftpaper(1);
                equipment.setRmleftpager("2");
                /*
                 * equipment.setXigu(100); equipment.setMohe(100.00);
                 */

                equipmentservice.insertEquipment(equipment);
            }

            AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(macaddress).getResult();
            AuditZnsbAccesscabinet accesscabinet = accessCabinetService.getDetailByEquipmentMac(macaddress).getResult();
            if (StringUtil.isNotBlank(equipment)) {
                // 根据macaddress获取中心guid
                centerguid = equipment.getCenterguid();

                if (StringUtil.isNotBlank(centerguid)) {
                    if (StringUtil.isNotBlank(accesscabinet)) {
                        dataJson.put("cabinetguid", accesscabinet.getRowguid());
                        dataJson.put("cabinetno", accesscabinet.getCabinetno());
                        dataJson.put("cabinetmsg", accesscabinet.getLedcontent());
                    }
                    else {
                        dataJson.put("cabinetguid", "");
                    }
                    dataJson.put("centerguid", centerguid);
                    // 增加中心名称
                    dataJson.put("centername",
                            servicecenterservice.findAuditServiceCenterByGuid(centerguid).getResult().getCentername());
                    //
                    dataJson.put("xiaodayindevice", equipment.getXiaodayindevice());
                    dataJson.put("dadayindevice", equipment.getDadayindevice());
                    dataJson.put("colordayindevice", equipment.getColordayindevice());
                    dataJson.put("machineguid", equipment.getRowguid());
                    dataJson.put("machineno", equipment.getMachineno());
                    dataJson.put("homepageurl", equipment.getHomepageurl());
                    dataJson.put("xzcenterguid", "");
                    dataJson.put("xzcentername", "");
                    if (StringUtil.isNotBlank(equipment.getStr("xzcenterguid"))) {
                        AuditOrgaServiceCenter xzAuditOrgaServiceCenter = servicecenterservice
                                .findAuditServiceCenterByGuid(equipment.getStr("xzcenterguid")).getResult();
                        if (xzAuditOrgaServiceCenter != null) {
                            dataJson.put("xzcenterguid", equipment.getStr("xzcenterguid"));
                            dataJson.put("xzcentername", xzAuditOrgaServiceCenter.getCentername());
                        }
                    }

                    // logo后台可配置开始
                    List<String> logourllist = new ArrayList<String>();
                    // 获取个性化的logo，就不再查询通用logo地址
                    String fields = "logoguid";
                    boolean getCommon = false;
                    // 首先通过设备rowguid查询logo个性化表记录，如果有，则表示改设备被个性化了
                    AuditZnsbLogoconfig logoconfig = logoConfigService
                            .getConfigbyMacinerowguid(equipment.getRowguid(), fields).getResult();
                    if (StringUtil.isNotBlank(logoconfig)) {
                        String logoguid = logoconfig.getLogoguid();
                        AuditZnsbMachineLogo machineLogo = machineLogoService.find(logoguid);
                        // 通过获取到的设备logo，判断当前设备个性化的类型是不是和当前设备logo类型一致，一致则获取logo，不一致，就不获取该个性化，进而继续判断改设备有无通用。
                        if (StringUtil.isNotBlank(machineLogo)
                                && QueueConstant.CONSTANT_STR_ONE.equals(machineLogo.getIsuniversal())
                                && machineLogo.getMachinetype().equals(logomachinetype)) {
                            List<FrameAttachInfo> logoattachlist = attachService
                                    .getAttachInfoListByGuid(machineLogo.getLogocliengguid());
                            for (FrameAttachInfo frameAttachInfo : logoattachlist) {
                                logourllist.add(QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                                        + "/rest/auditattach/readAttach?attachguid=" + frameAttachInfo.getAttachGuid());
                            }
                        }
                        else {
                            getCommon = true;
                        }

                    }
                    else {
                        getCommon = true;
                    }
                    if (getCommon) {
                        // 获取通用的logo
                        AuditZnsbMachineLogo commonMachineLogo = machineLogoService
                                .getLogoByMachineTypeAndCenterGuid(logomachinetype, centerguid).getResult();
                        if (StringUtil.isNotBlank(commonMachineLogo)
                                && QueueConstant.CONSTANT_STR_ZERO.equals(commonMachineLogo.getIsuniversal())) {
                            List<FrameAttachInfo> commonlogoattachlist = attachService
                                    .getAttachInfoListByGuid(commonMachineLogo.getLogocliengguid());
                            for (FrameAttachInfo frameAttachInfo : commonlogoattachlist) {
                                logourllist.add(QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                                        + "/rest/auditattach/readAttach?attachguid=" + frameAttachInfo.getAttachGuid());
                            }
                        }
                    }
                    dataJson.put("logourl", logourllist);

                    // logo后台可配置结束

                    AuditOrgaServiceCenter auditCenter = servicecenterservice.findAuditServiceCenterByGuid(centerguid)
                            .getResult();
                    if (StringUtil.isNotBlank(auditCenter)) {
                        dataJson.put("areacode", auditCenter.getBelongxiaqu());
                        // 乡镇智能化（使用baseAreaCode替代areacode）
                        AuditOrgaArea orga = areaService.getAreaByAreacode(auditCenter.getBelongxiaqu()).getResult();
                        if (StringUtil.isNotBlank(orga)) {
                            // 保存当前的areacode
                            dataJson.put("currentareacode", auditCenter.getBelongxiaqu());
                            // 保存父级areacode
                            if (StringUtil.isNotBlank(orga.getBaseAreaCode())) {
                                dataJson.put("areacode", orga.getBaseAreaCode());
                            }
                        }
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "未分配辖区！", "");
                    }
                    // 判断是否启用
                    if (QueueConstant.EQUIPMENT_STATUS_OFFLINE.equals(equipment.getStatus())) {
                        return JsonUtils.zwdtRestReturn("0", "设备已离线！", "");
                    }
                    // 判断是否24小时无人值守设备
                    if (QueueConstant.EQUIPMENT_TYPE_YTJ24.equals(equipment.getMachinetype())) {
                        dataJson.put("is_24hours", QueueConstant.Common_yes_String);
                    }
                    else {
                        dataJson.put("is_24hours", QueueConstant.Common_no_String);
                    }
                    // 上传申报材料方式类型 0 原来 1 扫码上传 2 云盘上传
                    String useMobileUploadMaterial = handleConfigservice
                            .getFrameConfig("AS_IS_USE_MOBILE_UPLOAD_MATERIAL", centerguid).getResult();
                    dataJson.put("useMobileUploadMaterial", useMobileUploadMaterial);
                    // 通知公告功能相关中心参数获取
                    String usenotice = handleConfigservice.getFrameConfig("AS_IS_USE_NOTICE", centerguid).getResult();
                    dataJson.put("usenotice", usenotice);
                    String noticephone = handleConfigservice.getFrameConfig("NOTICE_PHONE", centerguid).getResult();
                    dataJson.put("noticephone", noticephone);
                    String noticeplaywaittime = handleConfigservice.getFrameConfig("NOTICE_PLAY_WAIT_TIME", centerguid)
                            .getResult();
                    dataJson.put("noticeplaywaittime", noticeplaywaittime);
                    // 天气预报功能开启参数获取
                    String usecityweather = handleConfigservice.getFrameConfig("AS_USE_CITY_WEATHER", centerguid)
                            .getResult();
                    dataJson.put("usecityweather", usecityweather);
                    // 一体机事项搜索功能开启参数获取
                    String usetasksearch = handleConfigservice.getFrameConfig("AS_USE_TASK_SEARCH", centerguid)
                            .getResult();
                    dataJson.put("usetasksearch", usetasksearch);
                    String loginface = handleConfigservice.getFrameConfig("AS_SELFSERVICE_LOGINFACE", centerguid)
                            .getResult();
                    dataJson.put("loginface", StringUtil.isNotBlank(loginface) ? loginface : "0");
                    // 存取柜
                    String cqgloginface = handleConfigservice.getFrameConfig("AS_ACCESSCABINET_LOGINFACE", centerguid)
                            .getResult();
                    dataJson.put("cqgloginface", StringUtil.isNotBlank(cqgloginface) ? cqgloginface : "0");

                    // 是否启ai审批
                    String useaisp = handleConfigservice.getFrameConfig("AS_USE_AISP", centerguid).getResult();
                    dataJson.put("useaisp", StringUtil.isNotBlank(useaisp) ? useaisp : "0");
                    // 是否使用电子身份证
                    String useecard = handleConfigservice.getFrameConfig("AS_USE_ECARD", centerguid).getResult();
                    dataJson.put("useecard", StringUtil.isNotBlank(useecard) ? useecard : "0");
                    // 是否启用自主申报二维码 1920版本
                    String applyqrcode = handleConfigservice.getFrameConfig("AS_SELFSERVICE_APPLYQRCODE", centerguid)
                            .getResult();
                    dataJson.put("applyqrcode", StringUtil.isNotBlank(applyqrcode) ? applyqrcode : "1");

                    String isusedahanlogin = handleConfigservice.getFrameConfig("AS_ZNSB_ISUSEDAHANLOGIN", centerguid)
                            .getResult();
                    dataJson.put("isusedahanlogin", StringUtil.isNotBlank(isusedahanlogin) ? isusedahanlogin : "0");
                    // led屏幕显示内容
                    String ledcontent = handleConfigservice.getFrameConfig("AS_ZNSB_LEDCONTENT", centerguid)
                            .getResult();
                    if (StringUtil.isBlank(ledcontent)) {
                        // "第一行内容;第二行内容;1表示直接展示;3表示滚动展示"
                        ledcontent = "国泰新点软件;股份有限公司;1;3";
                    }
                    dataJson.put("ledcontent", ledcontent);
                    // 是否开启语音识别
                    String usevoice = handleConfigservice.getFrameConfig("AS_ZNSB_USE_VOICERECOGNITION", centerguid)
                            .getResult();
                    dataJson.put("usevoice", usevoice);
                    // 一体机所属公司
                    dataJson.put("ytjType",
                            StringUtil.isNotBlank(equipment.get("ytjstype")) ? equipment.get("ytjstype") : "1");
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该设备已注册，但未配置，请先进系统配置！", "");
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该设备未注册！", "");
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e, "");
        }
    }

}
