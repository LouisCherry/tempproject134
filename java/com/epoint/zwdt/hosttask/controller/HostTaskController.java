package com.epoint.zwdt.hosttask.controller;

import java.lang.invoke.MethodHandles;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.xmz.api.IJnService;
import com.epoint.zwdt.hosttask.api.IHostTask;

@RestController
@RequestMapping("/hosttask")
public class HostTaskController
{

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IHostTask iHostTask;

    @Autowired
    private IAuditTask iAuditTask;
    
    @Autowired
    private IJnService iJnService;

    @Autowired
    private ICodeItemsService iCodeItemsService;
    
    @Autowired
    private IAuditOrgaArea iAuditorgaAtra;
    /**
     * 获取配置文件参数
     */
    private static String tazwfwrootpath = ConfigUtil.getConfigValue("taconfig", "tazwfwrootpath");

    /**
     *  获取高频事项
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/getHostTask", method = RequestMethod.POST)
    public String getHostTask(@RequestBody String params) {
        try {
            log.info("=======开始调用getHostTask接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            // JSONObject data = (JSONObject) obj.get("data");
            String taskname = null;
            String areaCode = null;
            // 1.1、获取事项名称
            if (obj != null) {
                taskname = obj.getString("taskname");
                // 1.2 获取辖区code
                areaCode = obj.getString("areacode");
            }
            //String areaCode = "370900000000";
            List<Record> hottasklist = iHostTask.getListAuditTaskHottask(taskname, areaCode);

            if (hottasklist != null) {
                // 4、定义返回JSON对象
                JSONObject Json = new JSONObject();
                for (Record auditTaskHottask : hottasklist) {
                    if (auditTaskHottask != null) {
                        auditTaskHottask.put("parentTitle", auditTaskHottask.getStr("taskname"));
                        String tasksplb = iCodeItemsService.getItemTextByCodeName("审批类别",
                                iAuditTask.getAuditTaskByGuid(auditTaskHottask.getStr("rowguid"), true).getResult()
                                        .getShenpilb());

                        AuditTask aTask = iHostTask.getAuditTaskByTaskId(auditTaskHottask.getStr("taskid"));
                        Record tasktaian = null;
                        String rootpath = null;
                        String mp4path = null;
                        if (aTask != null) {
                            tasktaian = iHostTask.getQcode(aTask.getTask_id());
                            if (tasktaian != null) {
                                FrameAttachInfo frameAttachInfo = iHostTask
                                        .getFrameAttachInfoByClientguid(tasktaian.get("serviceCodeclientguid"));
                                if (frameAttachInfo != null) {
                                    rootpath = frameAttachInfo.getAttachGuid();
                                }
                                if (StringUtil.isNotBlank(tasktaian.get("outervideoUrl"))) {
                                    mp4path = tasktaian.get("outervideoUrl");
                                }
                            }
                        }
                        auditTaskHottask.put("parentTitle", aTask.getTaskname());
                        auditTaskHottask.put("areacode", auditTaskHottask.getStr("areacode"));
                        //auditTaskHottask.put("mp4path", mp4path);
                        if (StringUtil.isNotBlank(mp4path)) {
                            auditTaskHottask.put("mp4path",
                                    tazwfwrootpath + "tazwdt/pages/hightask/ckplayer.html?guid=" + mp4path);
                            auditTaskHottask.put("target", "_blank");
                            auditTaskHottask.put("style", "1");
                        }
                        else {
                            auditTaskHottask.put("mp4path", "javascript:return false;");
                            auditTaskHottask.put("target", "");
                            auditTaskHottask.put("style", "opacity: 0.2;");
                        }
                        auditTaskHottask.put("attachguid", rootpath);
                        auditTaskHottask.put("rootpath",
                                tazwfwrootpath + "rest/intermediary/getmaterials?attachguid=" + rootpath);
                        auditTaskHottask.put("xk", "[" + tasksplb + "]");
                        auditTaskHottask.put("department",
                                iHostTask.getFrameou(
                                        iAuditTask.getAuditTaskByGuid(auditTaskHottask.getStr("rowguid"), true)
                                                .getResult().getOuguid())
                                        .getOushortName());
                        auditTaskHottask.put("guid", auditTaskHottask.getStr("rowguid"));
                        auditTaskHottask.put("id", auditTaskHottask.getStr("ID"));
                        auditTaskHottask.put("itemCode", auditTaskHottask.getStr("ITEM_ID"));

                    }
                }
                Json.put("total", hottasklist.size());//高频事项总数量
                Json.put("matters", hottasklist);
                log.info("=======结束调用getHostTask接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取高频事项成功", Json.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "获取高频事项失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getHostTask接口参数：params【" + params + "】=======");
            log.info("=======getHostTask异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取高频事项失败：" + e.getMessage(), "");
        }
    }

    /**
     *  获取事项基本信息
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/getTaskBasic", method = RequestMethod.POST)
    public String getTaskBasic(@RequestBody String params) {
        try {
            log.info("=======开始调用getTaskBasic接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            // 1.1、获取事项编码
            String taskguid = obj.getString("taskguid");

            AuditTask taskbasic = iHostTask.getTaskBasic(taskguid);
            if (taskbasic != null) {
                // 4、定义返回JSON对象
                JSONObject applyerJson = new JSONObject();
                applyerJson.put("taskname", taskbasic.getTaskname());
                applyerJson.put("supervisetel", taskbasic.getSupervise_tel());
                applyerJson.put("ouname", taskbasic.getOuname());
                applyerJson.put("linktel", taskbasic.getLink_tel());
                applyerJson.put("promiseday", taskbasic.getPromise_day() + "工作日");
                log.info("=======结束调用getTaskBasic接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取事项基本信息成功", applyerJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "获取事项基本信息失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskBasic接口参数：params【" + params + "】=======");
            log.info("=======getTaskBasic异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取事项基本信息失败：" + e.getMessage(), "");
        }
    }

    
    @RequestMapping(value = "/getQxTasks", method = RequestMethod.POST)
    public String getQxTasks(@RequestBody String params) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            // 1.1、获取事项编码
            String areacode = obj.getString("areacode");
            String taskname = obj.getString("taskname");
            taskname = URLDecoder.decode(taskname, "utf-8");
            AuditOrgaArea orgarea = iAuditorgaAtra.getAreaByAreacode(areacode).getResult();
            String areaname = orgarea.getXiaquname();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.setOrder("ordernum", "desc");
            sql.in("citylevel", "1,2");
            List<AuditOrgaArea> list = iAuditorgaAtra.selectAuditAreaList(sql.getMap()).getResult();
            List<JSONObject> jsonList = new ArrayList<>();
            AuditTask task = new AuditTask();
            for(AuditOrgaArea area : list) {
            	if("车管所".equals(area.getXiaquname())) {
            		continue;
            	}
               
                task = iJnService.getAuditBasicInfoDetail(taskname.trim(), area.getXiaqucode());
                if(task != null ) {
                	 JSONObject json = new JSONObject();
                	 json.put("areacode", area.getXiaqucode());
                     json.put("areaname", area.getXiaquname());
            		 json.put("taskguid", task.getRowguid());
                     json.put("taskid", task.getTask_id());
                     json.put("style", "color:#0085C3;");
                     jsonList.add(json);
                }
                
                if ("城乡居民最低生活保障金给付".equals(taskname) && "370800".equals(area.getXiaqucode())) {
                	 JSONObject json = new JSONObject();
                	 json.put("areacode", area.getXiaqucode());
                     json.put("areaname", area.getXiaquname());
                	 json.put("taskguid", "11111111");
                     json.put("taskid", "11111111");
                     json.put("style", "color:#0085C3;");
                     jsonList.add(json);
                }
                
            }
            JSONObject returnJson = new JSONObject();
            returnJson.put("items", jsonList);
            returnJson.put("areaname", areaname);
            return JsonUtils.zwdtRestReturn("1", "获取辖区事项列表成功", returnJson.toString());
            
        }catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取辖区事项列表异常", "");
        }
    }
    /**
     *  获取事项基本信息
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/getBasicInfo", method = RequestMethod.POST)
    public String getBasicInfo(@RequestBody String params) {
        try {
            log.info("=======开始调用getBasicInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            // 1.1、获取事项编码
            String taskguid = obj.getString("taskguid");
            AuditTask taskbasic = iHostTask.getBasicInfo(taskguid);
            AuditTaskExtension auditTaskExtension = iHostTask.getCbdwname(taskguid);
            if (taskbasic != null) {
                // 4、定义返回JSON对象
                JSONObject applyerJson = new JSONObject();
                String tasksplb = iCodeItemsService.getItemTextByCodeName("申请人类型",
                        taskbasic.getApplyertype().toString());
                String tasklx = iCodeItemsService.getItemTextByCodeName("事项类型", taskbasic.getType().toString());
                String crossscope = "无";
                if (StringUtil.isNotBlank(taskbasic.get("cross_scope"))) {
                    crossscope = iCodeItemsService.getItemTextByCodeName("通办范围", taskbasic.get("cross_scope"));
                }
                // 自助申报
                String href = tazwfwrootpath + "epointzwmhwz/pages/onlinedeclaration/onlinedeclaration?itemCode="
                        + taskbasic.getItem_id() + "&userType=gr&istongjian=1&rowguid=" + taskbasic.getStr("ID")
                        + "&gotoUrl=";
                applyerJson.put("href", href);
                applyerJson.put("taskname", taskbasic.getTaskname());
                
                if (taskbasic.getAnticipate_day() >= 200) {
                    applyerJson.put("anticipateday", "无");// 法定期限
                }
                else {
                    applyerJson.put("anticipateday", taskbasic.getAnticipate_day() + "工作日");// 法定期限
                }
                //applyerJson.put("anticipateday", taskbasic.getAnticipate_day() + "工作日");
                applyerJson.put("promiseday", taskbasic.getPromise_day() + "工作日");
                applyerJson.put("ouname", taskbasic.getOuname());
                applyerJson.put("tasktype", tasklx);
                applyerJson.put("cbdwname", auditTaskExtension.get("cbdwname"));
                applyerJson.put("applyer", tasksplb);
                applyerJson.put("crossscope", crossscope);
                String transact_addr = "2、" + taskbasic.getTransact_addr();
                String transact_time = "1、" + taskbasic.getTransact_time();
                applyerJson.put("transact_time", transact_time);
                applyerJson.put("transact_addr", transact_addr);
                log.info("=======结束调用getBasicInfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取事项基本信息成功", applyerJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "获取事项基本信息失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getBasicInfo接口参数：params【" + params + "】=======");
            log.info("=======getBasicInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取事项基本信息失败：" + e.getMessage(), "");
        }
    }

    /**
     *  获取事项法律依据、受理条件、收费标准及依据
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/getImplementationBasis", method = RequestMethod.POST)
    public String getImplementationBasis(@RequestBody String params) {
        try {
            log.info("=======开始调用getImplementationBasis接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            // 1.1、获取事项编码
            String taskguid = obj.getString("taskguid");
            AuditTask taskbasic = iHostTask.getImplementationBasis(taskguid);
            if (taskbasic != null) {
                // 4、定义返回JSON对象
                JSONObject applyerJson = new JSONObject();
                String bylaw = "<b>" + taskbasic.getBy_law() + "</b>";
                String chargeflag = "<b>不收费</b>";
                String chargestandard = "";
                if ("1".equals(taskbasic.getCharge_flag().toString())) {
                    chargeflag = taskbasic.getCharge_basis();
                    chargestandard = taskbasic.getCharge_standard();
                }
                applyerJson.put("chargebasis", chargeflag);
                applyerJson.put("chargestandard", chargestandard);
                applyerJson.put("bylaw", bylaw);
                applyerJson.put("acceptcondition", "<b>" + taskbasic.getAcceptcondition() + "</b>");
                log.info("=======结束调用getImplementationBasis接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取事项法律依据、受理条件、收费标准及依据成功", applyerJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "获取事项法律依据、受理条件、收费标准及依据失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getImplementationBasis接口参数：params【" + params + "】=======");
            log.info("=======getImplementationBasis异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取事项法律依据、受理条件、收费标准及依据失败：" + e.getMessage(), "");
        }
    }

    /**
     *  获取事项材料
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/geTaskMaterial", method = RequestMethod.POST)
    public String geTaskMaterial(@RequestBody String params) {
        try {
            log.info("=======开始调用geTaskMaterial接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            // 1.1、获取事项编码
            String taskguid = obj.getString("taskguid");
            List<AuditTaskMaterial> taskMaterials = iHostTask.geTaskMaterial(taskguid);
            if (taskMaterials != null) {
                // 4、定义返回JSON对象
                JSONObject applyerJson = new JSONObject();
                //String materialbasic = "";
                //int a = 1;
                StringBuilder buffer = new StringBuilder();
                buffer.append("<table style ='text-align:center' border=1 width =100%");
                buffer.append("<tr><th style ='text-align:center' width =40%>材料名称</th>");
                buffer.append("<th style ='text-align:center'>材料形式</th>");
                buffer.append("<th style ='text-align:center'>材料必要性</th>");
                buffer.append("<th style ='text-align:center'>备注</th>");
                buffer.append("<th style ='text-align:center'>示范文本</th>");
                buffer.append("<th style ='text-align:center'>空白样表</th></tr>");
                for (AuditTaskMaterial auditTaskMaterial : taskMaterials) {
                    String fileremark = "";
                    String necessity = "";
                    String gmmaterialType = "";
                    String clCategory = "";
                    //材料分类    1 表格类   2文本类   3 结果文书类 
                    if (StringUtil.isNotBlank(auditTaskMaterial.get("clCategory"))) {
                        if ("1".equals(auditTaskMaterial.get("clCategory"))) {
                            clCategory = "表格类";
                        }
                        else if ("2".equals(auditTaskMaterial.get("clCategory"))) {
                            clCategory = "文本类";
                        }
                        else {
                            clCategory = "结果文书类";
                        }
                    }
                    if (StringUtil.isNotBlank(auditTaskMaterial.get("gmmaterialType"))) {
                        if ("1".equals(auditTaskMaterial.get("gmmaterialType"))) {
                            gmmaterialType = "原件";
                        }
                        else {
                            gmmaterialType = "复印件";
                        }
                    }
                    if (StringUtil.isNotBlank(auditTaskMaterial.get("File_remark"))) {
                        fileremark = auditTaskMaterial.get("File_remark");
                    }
                    else {
                        fileremark = "暂无";
                    }

                    if (StringUtil.isNotBlank(auditTaskMaterial.getNecessity())) {
                        necessity = iCodeItemsService.getItemTextByCodeName("必需设定",
                                auditTaskMaterial.getNecessity().toString());
                    }
                    //                    String clxsPage = "";
                    //                    String clxsFyj = "";
                    //                    if (StringUtil.isNotBlank(auditTaskMaterial.getPage_num()) && auditTaskMaterial.getPage_num() > 0) {
                    //                        clxsPage = "原件" + auditTaskMaterial.getPage_num() + "份";
                    //                    }
                    //                    if (StringUtil.isNotBlank(auditTaskMaterial.get("FYJNUM"))
                    //                            && auditTaskMaterial.getInt("FYJNUM") > 0) {
                    //                        clxsFyj = "复印件" + auditTaskMaterial.get("FYJNUM") + "份";
                    //                    }
                    String sfwb = "无";
                    String kbyb = "无";
                    if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
                        if (iHostTask
                                .getFrameAttachInfoByClientguid(auditTaskMaterial.getExampleattachguid()) != null) {
                            if (StringUtil.isNotBlank(
                                    iHostTask.getFrameAttachInfoByClientguid(auditTaskMaterial.getExampleattachguid())
                                            .getAttachGuid())) {
                                sfwb = "<a style=color:#3366FF href ='" + tazwfwrootpath
                                        + "'rest/intermediary/getmaterials?attachguid="
                                        + iHostTask.getFrameAttachInfoByClientguid(
                                                auditTaskMaterial.getExampleattachguid()).getAttachGuid()
                                        + ">" + "<u>示范文本</u>" + "</a>";
                            }
                        }

                    }
                    if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
                        if (iHostTask
                                .getFrameAttachInfoByClientguid(auditTaskMaterial.getTemplateattachguid()) != null) {
                            if (StringUtil.isNotBlank(
                                    iHostTask.getFrameAttachInfoByClientguid(auditTaskMaterial.getTemplateattachguid())
                                            .getAttachGuid())) {
                                kbyb = "<a style=color:#3366FF href ='" + tazwfwrootpath
                                        + "'rest/intermediary/getmaterials?attachguid="
                                        + iHostTask.getFrameAttachInfoByClientguid(
                                                auditTaskMaterial.getTemplateattachguid()).getAttachGuid()
                                        + ">" + "<u>空白样表</u>" + "</a>";
                            }
                        }

                    }
                    buffer.append("<tr>");
                    buffer.append("<td>" + auditTaskMaterial.getMaterialname() + "</td>");
                    buffer.append("<td>" + clCategory + "&nbsp;" + gmmaterialType + "&nbsp;" + fileremark + "</td>");
                    buffer.append("<td>" + necessity + "</td>");
                    buffer.append("<td>" + "" + "</td>");
                    buffer.append("<td>" + sfwb + "</td>");
                    buffer.append("<td>" + kbyb + "</td>");
                    buffer.append("</tr>");
                    /* materialbasic = materialbasic + a + "、" + auditTaskMaterial.getMaterialname() +"&nbsp;&nbsp;&nbsp;"+cljz+"&nbsp;"+submittype+"&nbsp;"+clxs+"&nbsp;&nbsp;&nbsp;"+sfwb+"&nbsp;&nbsp;"+kbyb+"</br>";
                    a++;*/
                }
                buffer.append("</table>");
                String str = buffer.toString();
                applyerJson.put("materialbasic", str);
                log.info("=======结束调用geTaskMaterial接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取事项材料成功", applyerJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "获取事项材料失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======geTaskMaterial接口参数：params【" + params + "】=======");
            log.info("=======geTaskMaterial异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取事项材料失败：" + e.getMessage(), "");
        }
    }

    /**
     *  获取二维码
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/getQcode", method = RequestMethod.POST)
    public String getQcode(@RequestBody String params) {
        try {
            log.info("=======开始调用getQcode接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            // 1.1、获取事项编码
            String taskguid = obj.getString("taskguid");
            AuditTask aTask = iAuditTask.getAuditTaskByGuid(taskguid, true).getResult();
            Record tasktaian = null;
            if (aTask != null) {
                tasktaian = iHostTask.getQcode(aTask.getTask_id());
            }

            if (tasktaian != null) {
                // 4、定义返回JSON对象
                JSONObject applyerJson = new JSONObject();
                FrameAttachInfo frameAttachInfo = iHostTask
                        .getFrameAttachInfoByClientguid(tasktaian.get("serviceCodeclientguid"));
                String rootpath = null;
                String mp4path = null;
                if (frameAttachInfo != null) {
                    rootpath = tazwfwrootpath + "rest/intermediary/getmaterials?attachguid="
                            + frameAttachInfo.getAttachGuid();
                }
                if (StringUtil.isNotBlank(tasktaian.get("outervideoUrl"))) {
                    mp4path = tasktaian.get("outervideoUrl");
                }
                applyerJson.put("imgbasic", rootpath);
                //applyerJson.put("guid", mp4path);

                if (StringUtil.isNotBlank(mp4path)) {
                    applyerJson.put("guid", tazwfwrootpath + "tazwdt/pages/hightask/ckplayer.html?guid=" + mp4path);
                    applyerJson.put("target", "_blank");
                    applyerJson.put("style", "1");
                }
                else {
                    applyerJson.put("guid", "javascript:return false;");
                    applyerJson.put("target", "");
                    applyerJson.put("style", "opacity: 0.2;");
                }

                log.info("=======结束调用getQcode接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取二维码成功", applyerJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "获取二维码失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getQcode接口参数：params【" + params + "】=======");
            log.info("=======getQcode异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取二维码失败：" + e.getMessage(), "");
        }
    }

}
