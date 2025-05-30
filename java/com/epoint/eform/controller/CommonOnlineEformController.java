package com.epoint.eform.controller;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditresource.auditrssharemaerial.domain.AuditRsShareMetadata;
import com.epoint.basic.auditresource.auditrssharemaerial.inter.IAuditRsShareMetadata;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;

@RestController
@RequestMapping("/commonOnlineEformController")
public class CommonOnlineEformController
{

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private IAuditRsShareMetadata iAuditRsShareMetadata;
    @Autowired
    private ICodeItemsService iCodeItemsService;
    @Autowired
    private IAuditProject iAuditProject;

    @RequestMapping(value = "/validateMust", method = RequestMethod.POST)
    public String validateMust(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用validateMust接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                String msg = "";
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String dataObj_baseinfo = obj.getString("dataObj_baseinfo");// 事项guid
                JSONObject dataObj_baseinfo_json = JSONObject.parseObject(dataObj_baseinfo);
                String dataObj_baseinfo_other = obj.getString("dataObj_baseinfo_other");// 事项guid
                JSONObject dataObj_baseinfo_other_json = JSONObject.parseObject(dataObj_baseinfo_other);
                String projectguid = obj.getString("projectguid");
                AuditProject project = iAuditProject.getAuditProjectByRowGuid(projectguid, null).getResult();
                boolean flag = true;
                if (project != null) {

                    if (project.getProjectname().contains("超限事项许可证")
                            || project.getProjectname().contains("在设区的市范围内跨区、县进行公路超限运输许可")
                            || project.getProjectname().contains("超限运输车辆行驶公路许可新办")) {
                        // 行驶时间开始日期
                        Date xssjksrq = dataObj_baseinfo_json.getDate("xssjksrq");
                        // 行驶时间结束时间
                        Date xssjjssj = dataObj_baseinfo_json.getDate("xssjjssj");
                        // 货物重量
                        Double hwzl = dataObj_baseinfo_json.getDoubleValue("hwzl");
                        // 牵引车辆整备质量
                        Double qyclzbzl = dataObj_baseinfo_json.getDoubleValue("qyclzbzl");
                        // 牵引车车轴数
                        int qycczs = dataObj_baseinfo_json.getIntValue("qycczs");
                        // 挂车辆类型
                        String gcllx = dataObj_baseinfo_json.getString("gcllx");
                        // 挂车辆整备质量（t）
                        Double gclzbzl = dataObj_baseinfo_json.getDoubleValue("gclzbzl");
                        // 挂车轴数
                        int gczs = dataObj_baseinfo_json.getIntValue("gczs");
                        // 挂车轴线
                        String gczx = dataObj_baseinfo_json.getString("gczx");
                        // 车货总重量
                        Double chzzl = dataObj_baseinfo_json.getDoubleValue("chzzl");
                        // 轴荷分布
                        String zhfb = dataObj_baseinfo_json.getString("zhfb");
                        // 轴数
                        int zs = dataObj_baseinfo_json.getIntValue("zs");
                        // 牵引车辆类型
                        String qycllx = dataObj_baseinfo_json.getString("qycllx");
                        // 轴距
                        String zj = dataObj_baseinfo_json.getString("zj");
                        // 货物长度
                        Double hwcd = dataObj_baseinfo_json.getDoubleValue("hwcd");
                        // 货物最大宽
                        Double hwzdk = dataObj_baseinfo_json.getDoubleValue("hwzdk");
                        // 货物最大高
                        Double whzdg = dataObj_baseinfo_json.getDoubleValue("whzdg");
                        // 车货最大长
                        Double chzdc = dataObj_baseinfo_json.getDoubleValue("chzdc");
                        // 车货最大高
                        Double chzdg = dataObj_baseinfo_json.getDoubleValue("chzdg");
                        // 车货最大宽
                        Double chzdk = dataObj_baseinfo_json.getDoubleValue("chzdk");
                        // 挂车长（m）
                        Double gcc = dataObj_baseinfo_json.getDoubleValue("gcc");
                        // 挂车宽（m）
                        Double gck = dataObj_baseinfo_json.getDoubleValue("gck");
                        // 挂车高（m）
                        Double gcg = dataObj_baseinfo_json.getDoubleValue("gcg");
                        // 牵引车长
                        Double qycc = dataObj_baseinfo_json.getDoubleValue("qycc");
                        // 牵引车宽
                        Double qyck = dataObj_baseinfo_json.getDoubleValue("qyck");
                        // 牵引车高
                        Double qycg = dataObj_baseinfo_json.getDoubleValue("qycg");
                        // 经办人身份证号
                        String jbrsfzh = dataObj_baseinfo_json.getString("jbrsfzh");
                        // 统一社会信用代码
                        String tyshxydm = dataObj_baseinfo_json.getString("tyshxydm");
                        // 牵引车辆识别码
                        String qyclsbm = dataObj_baseinfo_json.getString("qyclsbm");
                        // 挂车车辆识别代码
                        String gcclsbdm = dataObj_baseinfo_json.getString("gcclsbdm");

                        String tjd = dataObj_baseinfo_json.getString("tjd");

                        chzzl = qyclzbzl + gclzbzl + hwzl;

                        zs = qycczs + gczs;

                        if (StringUtil.isNotBlank(xssjjssj) && StringUtil.isNotBlank(xssjksrq)) {
                            if (xssjjssj.getTime() < xssjksrq.getTime()) {
                                msg = "“行驶时间结束日期”应大于等于“行驶时间开始日期”！";
                            }

                            if (xssjjssj.getTime() - xssjksrq.getTime() > 24 * 60 * 60 * 1000 * 4) {
                                msg = "通行天数单次最多为5天！";
                            }
                        }

                        if (hwzl < 0) {
                            msg = "货物重量应大于等于0!";
                        }

                        if (qyclzbzl + gclzbzl <= 1 || chzzl <= 1) {
                            msg = "车辆整备质量、车货总质量应在1吨以上!";
                        }

                        if (hwcd <= 0.1 || hwzdk <= 0.1 || whzdg <= 0.1) {
                            msg = "货物长度、宽度、高度应在0.1米以上!";
                        }

                        if (chzdc <= 1 || chzdg <= 1 || chzdk <= 1) {
                            msg = "车货最大长、最大宽、最大高在1米以上!";
                        }

                        if (hwzl > chzzl) {
                            msg = "货物总重应小于车货总重！";
                        }

                        if (chzdc < hwcd || chzdg < whzdg || chzdk < hwzdk) {
                            msg = "货物的长、宽、高应小于总长、宽、高！";
                        }

                        if ("37".equals(gcllx) && !"0".equals(gczx)) {
                            msg = "挂车为普通平板车,轴线应为0！";
                        }

                        if ("38".equals(gcllx) && "36".equals(qycllx)) {
                            if (!zhfb.contains("*")) {
                                msg = "采用多轮多轴液压平板车运输，则“轴荷分布”中必须包括“18”！";
                            }
                            String regex = "[0-9]*\\+[0-9]*\\+[0-9]*\\*[0-9]*";
                            Pattern p = Pattern.compile(regex);
                            Matcher m1 = p.matcher(zhfb);
                            if (!m1.matches()) {
                                msg = "采用多轮多轴液压平板车运输，轴荷分布需满足10+20+3*18格式!";
                            }

                        }

                        if ("37".equals(gcllx) && "36".equals(qycllx)) {
                            if (zhfb.contains("*")) {
                                msg = "挂车为普通平板车，轴荷分布不应该包括“18”";
                            }

                            String regex = "[0-9]*\\+[0-9]*\\+[0-9]*";
                            Pattern p = Pattern.compile(regex);
                            Matcher m1 = p.matcher(zhfb);
                            if (!m1.matches()) {
                                msg = "采用普通平板车运输，轴荷分布需满足10+20+30格式!";
                            }

                        }

                        String regex2 = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$";
                        Pattern p2 = Pattern.compile(regex2);
                        Matcher m2 = p2.matcher(jbrsfzh);
                        if (!m2.matches()) {
                            msg = "身份证校验失败！";
                        }

                        String regex3 = "^([0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}|[1-9]\\d{14})$";
                        Pattern p3 = Pattern.compile(regex3);
                        Matcher m3 = p3.matcher(tyshxydm);
                        if (!m3.matches()) {
                            msg = "统一社会信用代码校验失败！";
                        }

                        String regex4 = "[\\dA-HJ-NPR-Z]{17}";
                        Pattern p4 = Pattern.compile(regex4);
                        Matcher m4 = p4.matcher(qyclsbm);
                        Matcher m5 = p4.matcher(gcclsbdm);
                        if (!m4.matches()) {
                            msg = "牵引车辆识别代码校验失败！";
                        }

                        if (!m5.matches() && !"20".equals(qycllx)) {
                            msg = "挂车车辆识别代码校验失败！";
                        }

                        if ("20".equals(qycllx) && StringUtil.isNotBlank(gcllx)) {
                            msg = "“车辆类型”为 普通货车时，不可选择挂车！";
                        }

                        if (zs == 2 && chzzl > 20) {
                            msg = "总轴数为2，车货总重应小于等于20吨！";
                        }
                        else if (zs == 3 && chzzl > 30) {
                            msg = "总轴数为3，车货总重应小于等于30吨！";
                        }
                        else if (zs == 4 && chzzl > 40) {
                            msg = "总轴数为4，车货总重应小于等于40吨！";
                        }
                        else if (zs == 5 && chzzl > 50) {
                            msg = "总轴数为5，车货总重应小于等于50吨！";
                        }
                        else if (zs == 6 && chzzl > 60) {
                            msg = "总轴数为6，车货总重应小于等于60吨！";
                        }

                        if (StringUtil.isBlank(tjd)) {
                            msg = "途径地不能为空！";
                        }

                        // 轴荷分布：10+20+50，取首位数值1+2+5=8-1=7，轴距就只能填写7位数值1.5-2-3-4-5-6-7“-”为一个数值结束
                        String[] zhfbs = zhfb.split("\\+");
                        String[] zjs = zj.split("-");
                        if (zhfbs.length > 0) {
                            try {
                                int zfsum = 0;// 轴荷分布首位数值和
                                for (String str : zhfbs) {
                                    zfsum += Integer.parseInt(str.substring(0, 1));
                                }
                                if (zfsum - 1 != zjs.length) {
                                    msg = "轴距填写不规范，请检查！";
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                                msg = "轴荷分布填写不规范，请检查！";
                            }
                        }

                        if (StringUtil.isNotBlank(msg)) {
                            flag = false;
                        }

                    }
                }
                JSONObject dataJson = new JSONObject();
                log.info("dataObj_baseinfo:" + dataObj_baseinfo);
                if (flag) {
                    project.set("dataObj_baseinfo", dataObj_baseinfo);
                    project.set("dataObj_baseinfo_other", dataObj_baseinfo_other);
                    iAuditProject.updateProject(project);
                    dataJson.put("msg", msg);
                }
                else {
                    dataJson.put("msg", msg);
                }
                log.info("=======结束调用validateMust接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取电子表单验证失败", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======validateMust接口参数：params【" + params + "】=======");
            log.info("=======validateMust异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取电子表单验证失败：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getEformByTaskGuid", method = RequestMethod.POST)
    public String getEformByTaskGuid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getEformByTaskGuid接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String taskguid = obj.getString("taskguid");// 事项guid
                String projectguid = obj.getString("projectguid");// 事项guid
                String type = obj.getString("type");
                JSONObject dataJson = new JSONObject();
                AuditTask task = iAuditTask.getAuditTaskByGuid(taskguid, false).getResult();
                AuditProject project = iAuditProject.getAuditProjectByRowGuid(projectguid, null).getResult();
                if (task != null) {
                    String task_id = task.getTask_id();
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("MATERIALGUID", task_id);
                    sql.eq("dispinadd", type);
                    sql.setOrderAsc("ordernum");
                    List<AuditRsShareMetadata> list = iAuditRsShareMetadata
                            .selectAuditRsShareMetadataByCondition(sql.getMap()).getResult();
                    for (AuditRsShareMetadata auditRsShareMetadata : list) {

                        //
                        if (project != null) {
                            if ("1".equals(auditRsShareMetadata.get("isrelatetable"))) {
                                String relatetablefield = auditRsShareMetadata.getRelatetablefield();
                                String dataobject = project.getStr("dataObj_baseinfo");
                                if (StringUtil.isNotBlank(dataobject)) {
                                    JSONObject jsonobject = JSON.parseObject(dataobject);
                                    auditRsShareMetadata.put("value", jsonobject.getString(relatetablefield));
                                }
                            }
                        }

                        if (StringUtil.isNotBlank(auditRsShareMetadata.getDatasource_codename())) {
                            List<CodeItems> listCodeItems = iCodeItemsService
                                    .listCodeItemsByCodeName(auditRsShareMetadata.getDatasource_codename());
                            List<JSONObject> codeItemsJson = new ArrayList<>();
                            for (CodeItems codeItems : listCodeItems) {
                                JSONObject codeItemsJosn = new JSONObject();
                                codeItemsJosn.put("key", codeItems.getItemText());
                                codeItemsJosn.put("value", codeItems.getItemValue());
                                if (codeItems.getItemValue().equals(auditRsShareMetadata.get("defaultvalue"))) {
                                    codeItemsJosn.put("checked", "checked");
                                }
                                codeItemsJson.add(codeItemsJosn);
                            }
                            auditRsShareMetadata.put("codeItems", codeItemsJson);
                        }
                    }
                    dataJson.put("data", list);
                }
                return JsonUtils.zwdtRestReturn("1", "获取事项电子表单数据成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getEformByTaskGuid接口参数：params【" + params + "】=======");
            log.info("=======getEformByTaskGuid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取事项电子表单失败：" + e.getMessage(), "");
        }
    }

}
