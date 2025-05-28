package com.epoint.auditselfservice.auditselfservicerest.baidumap;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbselfmachinebaidumap.inter.IJNBaiDuMap;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

@RestController
@RequestMapping("/jnbaidumap")
public class JNBaiDuMapRestController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NEAR_YTJ_COUNT = 6;
    @Autowired
    private IJNBaiDuMap baidumapservice;

    @Autowired
    private IHandleConfig handleConfig;
    @Autowired
    private IAuditOrgaServiceCenter serviceCenter;
    @Autowired
    private IAuditZnsbEquipment equipmentservice;
    @Autowired
    private ICodeItemsService codeService;
    @Autowired
    private IConfigService configService;

    /**
     * 
     * 获取该中心及该中心下的所有设备分布位置 （在地图上展示）
     * 
     * @param params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = {"/getMachineLocationList" }, method = {RequestMethod.POST })
    public String getMachineLocationList(@RequestBody String params) {
        log.info("================调用getMachineLocationList接口开始=======================");
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String centerguid = obj.getString("centerguid");
            JSONObject dataJson = new JSONObject();
            List<AuditZnsbEquipment> machineLocationList = baidumapservice.getMachineLocationList(centerguid);
            // 判断
            if (!"all".equals(centerguid)) {
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("rowguid", centerguid);
                List<AuditOrgaServiceCenter> result = serviceCenter.getAuditOrgaServiceCenterByCondition(sql.getMap())
                        .getResult();
                AuditOrgaServiceCenter centerlocation = new AuditOrgaServiceCenter();
                if (result != null && !result.isEmpty()) {
                    centerlocation = result.get(0);
                }
                // 获取中心的经度纬度
                String longitude = handleConfig.getFrameConfig("AS_CENTER_LONGITUDE", centerguid).getResult();
                String latitude = handleConfig.getFrameConfig("AS_CENTER_LATITUDE", centerguid).getResult();
                centerlocation.put("longitude", longitude);
                centerlocation.put("latitude", latitude);
                dataJson.put("centerlocation", centerlocation);
            }
            dataJson.put("centerList", machineLocationList);
            log.info("=======结束调用getMachineLocationList接口=======");
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getMachineLocationList接口参数：params【" + params + "】=======");
            log.info("=======getMachineLocationList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "getMachineLocationList接口出现异常：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/getCurrentMachineLocation" }, method = {RequestMethod.POST })
    public String getCurrentMachineLocation(@RequestBody String params) {
        log.info("================调用getCurrentMachineLocation接口开始=======================");
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String macaddress = obj.getString("macaddress");
            JSONObject dataJson = new JSONObject();
            AuditZnsbEquipment machine = baidumapservice.getCurrentMachineLocation(macaddress);
            String phone = handleConfig.getFrameConfig("AS_CENTER_PHONE", machine.getCenterguid()).getResult();
            dataJson.put("machine", machine);
            dataJson.put("phone", phone);
            log.info("=======结束getCurrentMachineLocation接口=======");
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCurrentMachineLocation接口参数：params【" + params + "】=======");
            log.info("=======getCurrentMachineLocation异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "getCurrentMachineLocation接口出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 
     * 获取该地区某个中心的所有设备及设备的相关信息
     * 
     * @param params
     * @param request
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getMachineListByCenterGuid", method = RequestMethod.POST)
    public String getMachineListByCenterGuid(@RequestBody String params, HttpServletRequest request) {
        try {
            log.info("=======开始调用getMachineListByCenterGuid接口=======");
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String centerguid = obj.getString("centerguid");
            SqlConditionUtil sql = new SqlConditionUtil();
            if (StringUtil.isNotBlank(centerguid)) {
                sql.eq("centerguid", centerguid);
            }
            // 一体机分布图，查询机器类型为一体机
            sql.eq("machinetype", "3");
            List<AuditZnsbEquipment> equipmentlist = equipmentservice.getEquipmentByType(sql.getMap()).getResult();
            List<JSONObject> machinelist = new ArrayList<JSONObject>();
            for (AuditZnsbEquipment equipment : equipmentlist) {
                JSONObject data = new JSONObject();
                data.put("machinename", equipment.getMachinename());
                data.put("machinestatus", codeService.getItemTextByCodeName("设备当前状态",
                        StringUtil.isNotBlank(equipment.getCurrentStatus()) ? equipment.getCurrentStatus() : "0"));
                data.put("centerguid", equipment.getCenterguid());
                data.put("macaddress", equipment.getMacaddress());
                machinelist.add(data);
            }
            JSONObject dataJson = new JSONObject();
            dataJson.put("machinelist", machinelist);
            dataJson.put("totalcount", machinelist.size());
            dataJson.put("showstatu", getShowMachineStatu());
            log.info("=======结束调用getMachineListByCenterGuid接口=======");
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            log.info("=======getMachineListByCenterGuid接口参数：params【" + params + "】=======");
            log.info("=======getMachineListByCenterGuid异常信息：" + e + "=======");
            return JsonUtils.zwdtRestReturn("0", " getMachineListByCenterGuid信息出现异常：" + e.getMessage(), "");
        }

    }

    public String getShowMachineStatu() {
        return configService.getFrameConfigValue("AS_YTJ_FENBU_SHOW_STATU");
    }
}
