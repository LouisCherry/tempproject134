package com.epoint.auditselfservice.auditselfservicerest.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.License;
import com.epoint.auditselfservice.auditselfservicerest.common.MapMailMergeDataSource;
import com.epoint.basic.auditorga.auditconfig.domain.AuditOrgaConfig;
import com.epoint.basic.auditorga.auditconfig.inter.IAuditOrgaConfig;
import com.epoint.basic.auditqueue.auditznsbcentertask.domain.AuditZnsbCentertask;
import com.epoint.basic.auditqueue.auditznsbcentertask.inter.IAuditZnsbCentertask;
import com.epoint.basic.auditqueue.auditznsbtaskconfigure.domian.AuditZnsbTaskConfigure;
import com.epoint.basic.auditqueue.auditznsbtaskconfigure.inter.IAuditZnsbTaskConfigureService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.dict.domain.AuditTaskDict;
import com.epoint.basic.audittask.dict.inter.IAuditTaskDict;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskCase;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.QrcodeUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/jxselfservicetask"})
public class JxTaskRestController {
    @Autowired
    private IAuditZnsbCentertask centertaskservice;
    @Autowired
    private IAuditTaskDict taskdictservice;
    @Autowired
    private IAuditTask taskservice;
    @Autowired
    private IAuditTaskExtension taskextensionservice;
    @Autowired
    private IAuditTaskMaterial taskmaterialservice;
    @Autowired
    private IAuditTaskResult taskresultservice;
    @Autowired
    private ICodeItemsService codeitemsservice;
    @Autowired
    private IAuditOrgaConfig auditconfigservice;
    @Autowired
    private IAuditTaskCase auditTaskCaseService;
    @Autowired
    private IAuditTaskMaterialCase auditTaskMaterialCaseService;
    @Autowired
    private IAttachService attachservice;
    @Autowired
    private IHandleConfig handleConfigservice;
    @Autowired
    private IAuditZnsbTaskConfigureService auditZnsbTaskConfigureService;
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @RequestMapping(value = {"/getHotTaskList"}, method = {RequestMethod.POST})
    public String getHotTaskList(@RequestBody String params) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            JSONObject dataJson = new JSONObject();
            String centerguid = obj.getString("centerguid");
            String applyertype = obj.getString("applyertype");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");
            ArrayList list = new ArrayList();
            new JSONObject();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("centerguid", centerguid);
            sql.eq("is_hottask", "1");
            sql.eq("is_enable", "1");
            if (StringUtil.isNotBlank(applyertype)) {
                sql.like("applyertype", applyertype);
            }

            PageData hottaskpagedata = (PageData) this.centertaskservice
                    .getCenterTaskPageData(sql.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                            Integer.parseInt(pageSize), "hottaskordernum", "desc")
                    .getResult();
            int totalcount = hottaskpagedata.getRowCount();
            Iterator arg13 = hottaskpagedata.getList().iterator();

            while (arg13.hasNext()) {
                AuditZnsbCentertask hottask = (AuditZnsbCentertask) arg13.next();
                JSONObject hottaskJson = new JSONObject();
                hottaskJson.put("taskname", hottask.getTaskname());
                hottaskJson.put("taskguid", hottask.getTaskguid());
                list.add(hottaskJson);
            }

            dataJson.put("hottasklist", list);
            dataJson.put("totalcount", StringUtil.getNotNullString(Integer.valueOf(totalcount)));
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException arg15) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + arg15.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/getTaskDict"}, method = {RequestMethod.POST})
    public String getTaskDict(@RequestBody String params) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            JSONObject dataJson = new JSONObject();
            String no = obj.getString("no");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");
            ArrayList list = new ArrayList();
            new JSONObject();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("LENGTH(no)", "4");
            sql.like("No", no);
            PageData dictpagedata = (PageData) this.taskdictservice.getAuditTaskDictPageData(sql.getMap(),
                    Integer.valueOf(Integer.parseInt(currentPage) * Integer.parseInt(pageSize)),
                    Integer.valueOf(Integer.parseInt(pageSize)), "NO", "asc").getResult();
            int totalcount = dictpagedata.getRowCount();
            Iterator arg12 = dictpagedata.getList().iterator();

            while (arg12.hasNext()) {
                AuditTaskDict dict = (AuditTaskDict) arg12.next();
                JSONObject dictJson = new JSONObject();
                dictJson.put("classname", dict.getClassname());
                dictJson.put("dictid", dict.getDictid());
                dictJson.put("no", dict.getNo());
                list.add(dictJson);
            }

            dataJson.put("dictlist", list);
            dataJson.put("totalcount", StringUtil.getNotNullString(Integer.valueOf(totalcount)));
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException arg14) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + arg14.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/getTaskList"}, method = {RequestMethod.POST})
    public String getTaskList(@RequestBody String params) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            JSONObject dataJson = new JSONObject();
            String centerguid = obj.getString("centerguid");
            String applyertype = obj.getString("applyertype");
            String ouguid = obj.getString("ouguid");
            String no = obj.getString("no");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");
            String searchTaskName = obj.getString("searchTaskName");
            ArrayList list = new ArrayList();
            new JSONObject();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("centerguid", centerguid);
            sql.eq("is_enable", "1");
            if (StringUtil.isNotBlank(applyertype)) {
                sql.like("applyertype", applyertype);
            }

            if (StringUtil.isNotBlank(ouguid)) {
                sql.eq("ouguid", ouguid);
            }

            if (StringUtil.isNotBlank(no)) {
                sql.like("taskmap", no + ";");
            }

            if (StringUtil.isNotBlank(searchTaskName)) {
                sql.like("taskname", searchTaskName);
            }

            PageData hottaskpagedata = (PageData) this.centertaskservice
                    .getCenterTaskPageData(sql.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                            Integer.parseInt(pageSize), "ordernum", "desc")
                    .getResult();
            int totalcount = hottaskpagedata.getRowCount();
            Iterator arg16 = hottaskpagedata.getList().iterator();

            while (arg16.hasNext()) {
                AuditZnsbCentertask hottask = (AuditZnsbCentertask) arg16.next();
                JSONObject hottaskJson = new JSONObject();
                hottaskJson.put("taskname", hottask.getTaskname());
                hottaskJson.put("taskguid", hottask.getTaskguid());
                list.add(hottaskJson);
            }

            dataJson.put("tasklist", list);
            dataJson.put("totalcount", StringUtil.getNotNullString(Integer.valueOf(totalcount)));
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException arg18) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + arg18.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/getTaskDetail"}, method = {RequestMethod.POST})
    public String getTaskDetail(@RequestBody String params) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            JSONObject dataJson = new JSONObject();
            String taskguid = obj.getString("taskguid");
            String centerguid = obj.getString("centerguid");
            String taskurl = "";
            AuditTask task = (AuditTask) this.taskservice.getAuditTaskByGuid(taskguid, true).getResult();
            if (StringUtil.isNotBlank(centerguid)) {
                SqlConditionUtil taskextension = new SqlConditionUtil();
                taskextension.eq("centerguid", centerguid);
                taskextension.eq("configname", "AS_TASK_URL");
                AuditOrgaConfig taskresult = (AuditOrgaConfig) this.auditconfigservice
                        .getCenterConfig(taskextension.getMap()).getResult();
                if (StringUtil.isNotBlank(taskresult)) {
                    taskurl = taskresult.getConfigvalue();
                } else {
                    taskurl = "";
                }
            }

            if (task != null) {
                if (StringUtil.isNotBlank(taskurl)) {
                    dataJson.put("showqrcode", "1");
                } else {
                    dataJson.put("showqrcode", "0");
                }

                dataJson.put("taskguid", task.getRowguid());
                dataJson.put("taskname", task.getTaskname());
                dataJson.put("itemid", task.getItem_id());
                dataJson.put("ouname", task.getOuname());
                if (StringUtil.isNotBlank(task.getStr("accept_address_info"))) {
                    List<Record> hours = SyncbanlididiansjService.syncbanlididiansj(task.getStr("accept_address_info"));
                    if (hours.size() > 0 && hours.get(0) != null) {

                        dataJson.put("transacttime", hours.get(0).getStr("officehour"));
                    }
                    else {
                        dataJson.put("transacttime", task.getTransact_time());
                    }
                }
                else {
                    dataJson.put("transacttime", task.getTransact_time());
                }
                dataJson.put("transactaddr", task.getTransact_addr());
                if ("1".equals(task.getType().toString())) {
                    dataJson.put("promiseday", "即办");
                    dataJson.put("anticipateday", "即办");
                } else {
                    dataJson.put("promiseday", task.getPromise_day() + "个工作日");
                    dataJson.put("anticipateday", task.getAnticipate_day() + "个工作日");
                }

                dataJson.put("linktel", task.getLink_tel());
                dataJson.put("supervisetel", task.getSupervise_tel());
                dataJson.put("condition", task.getAcceptcondition());
                dataJson.put("bylaw", task.getBy_law());
                dataJson.put("type", task.getType());
                dataJson.put("typetext",
                        this.codeitemsservice.getItemTextByCodeName("事项类型", String.valueOf(task.getType())));
                dataJson.put("applyertype", task.getApplyertype());
                if (!"10;".equals(task.getApplyertype()) && !"10".equals(task.getApplyertype())) {
                    if (!"20;".equals(task.getApplyertype()) && !"20".equals(task.getApplyertype())) {
                        dataJson.put("applyertypetext", "个人、企业");
                    } else {
                        dataJson.put("applyertypetext", "个人");
                    }
                } else {
                    dataJson.put("applyertypetext", "企业");
                }

                dataJson.put("chargeflag", task.getCharge_flag());
                dataJson.put("chargeflagtext",
                        this.codeitemsservice.getItemTextByCodeName("是否", String.valueOf(task.getCharge_flag())));
                AuditTaskExtension taskextension1 = (AuditTaskExtension) this.taskextensionservice
                        .getTaskExtensionByTaskGuid(taskguid, true).getResult();
                if (taskextension1 != null) {
                    dataJson.put("chargewhen", taskextension1.getCharge_when());
                    dataJson.put("chargewhentext", this.codeitemsservice.getItemTextByCodeName("何时收费",
                            String.valueOf(taskextension1.getCharge_when())));
                }

                AuditTaskResult taskresult1 = (AuditTaskResult) this.taskresultservice
                        .getAuditResultByTaskGuid(taskguid, true).getResult();
                if (taskresult1 != null) {
                    if (taskresult1.getResulttype().intValue() == 10) {
                        dataJson.put("iscert", "1");
                        dataJson.put("iscerttext", "是");
                    } else {
                        dataJson.put("iscert", "0");
                        dataJson.put("iscerttext", "否");
                    }
                } else {
                    dataJson.put("iscert", "0");
                    dataJson.put("iscerttext", "否");
                }

                AuditZnsbCentertask centertask = (AuditZnsbCentertask) this.centertaskservice
                        .getDetailbyTaskGuid(taskguid).getResult();
                if (centertask != null) {
                    dataJson.put("reservationmanagement", centertask.getReservationmanagement());
                    dataJson.put("webapplytype", centertask.getWebapplytype());
                    dataJson.put("isshowform", centertask.getIs_show_form());
                }

                List auditTaskCases = (List) this.auditTaskCaseService.selectTaskCaseByTaskGuid(taskguid).getResult();
                ArrayList caseconditionList = new ArrayList();
                new JSONObject();
                if (auditTaskCases.size() > 0) {
                    Iterator list = auditTaskCases.iterator();

                    while (list.hasNext()) {
                        AuditTaskCase matericalJson = (AuditTaskCase) list.next();
                        JSONObject conditionJson = new JSONObject();
                        conditionJson.put("taskcaseguid", matericalJson.getRowguid());
                        conditionJson.put("casename", matericalJson.getCasename());
                        caseconditionList.add(conditionJson);
                    }
                }

                dataJson.put("caselist", caseconditionList);
                ArrayList list1 = new ArrayList();
                new JSONObject();
                List taskmateriallist = (List) this.taskmaterialservice.selectTaskMaterialListByTaskGuid(taskguid, true)
                        .getResult();
                Iterator attachInfolist = taskmateriallist.iterator();

                while (attachInfolist.hasNext()) {
                    AuditTaskMaterial taskmaterial = (AuditTaskMaterial) attachInfolist.next();
                    JSONObject matericalJson1 = new JSONObject();
                    matericalJson1.put("materialname", taskmaterial.getMaterialname());
                    matericalJson1.put("materialid", taskmaterial.getMaterialid());
                    matericalJson1.put("necessity", taskmaterial.getNecessity());
                    matericalJson1.put("xsreview", taskmaterial.getXsreviewkp());
                    list1.add(matericalJson1);
                }

                dataJson.put("taskurl", taskurl);
                dataJson.put("matericallist", list1);
                if (StringUtil.isNotBlank(task.getTaskoutimgguid())) {
                    List attachInfolist1 = this.attachservice.getAttachInfoListByGuid(task.getTaskoutimgguid());
                    if (attachInfolist1 != null && attachInfolist1.size() > 0) {
                        dataJson.put("taskoutimgguid", ((FrameAttachInfo) attachInfolist1.get(0)).getAttachGuid());
                    } else {
                        dataJson.put("taskoutimgguid", "");
                    }
                } else {
                    dataJson.put("taskoutimgguid", "");
                }
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException arg19) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + arg19.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/getTaskCaseMaterialsByCaseguid"}, method = {RequestMethod.POST})
    public String getTaskCaseMaterialsByCaseguid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String taskcaseguid = obj.getString("taskcaseguid");
            String taskguid = obj.getString("taskguid");
            ArrayList taskMaterialList = new ArrayList();
            if (StringUtil.isNotBlank(taskcaseguid)) {
                HashMap dataJson = null;
                List auditTaskMaterialCases = (List) this.auditTaskMaterialCaseService
                        .selectTaskMaterialCaseByCaseGuid(taskcaseguid).getResult();
                dataJson = new HashMap(16);
                if (auditTaskMaterialCases != null && auditTaskMaterialCases.size() > 0) {
                    Iterator auditTaskMaterials = auditTaskMaterialCases.iterator();

                    while (auditTaskMaterials.hasNext()) {
                        AuditTaskMaterialCase materialJson = (AuditTaskMaterialCase) auditTaskMaterials.next();
                        dataJson.put(materialJson.getMaterialguid(), materialJson.getNecessity());
                    }
                }

                List auditTaskMaterials1 = (List) this.taskmaterialservice
                        .selectTaskMaterialListByTaskGuid(taskguid, true).getResult();
                new JSONObject();
                Iterator arg11 = auditTaskMaterials1.iterator();

                while (arg11.hasNext()) {
                    AuditTaskMaterial auditTaskMaterial = (AuditTaskMaterial) arg11.next();
                    if (dataJson.containsKey(auditTaskMaterial.getRowguid())) {
                        JSONObject materialJson1 = new JSONObject();
                        materialJson1.put("materialguid", auditTaskMaterial.getRowguid());
                        materialJson1.put("materialname", auditTaskMaterial.getMaterialname());
                        materialJson1.put("necessity", auditTaskMaterial.getNecessity());
                        taskMaterialList.add(materialJson1);
                    }
                }
            }

            JSONObject dataJson1 = new JSONObject();
            dataJson1.put("matericallist", taskMaterialList);
            return JsonUtils.zwdtRestReturn("1", "", dataJson1);
        } catch (Exception arg13) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + arg13.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/getTaskDetailDoc"}, method = {RequestMethod.POST})
    public String getTaskDetailDoc(@RequestBody String params, @Context HttpServletRequest request) throws Exception {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            JSONObject dataJson = new JSONObject();
            String taskguid = obj.getString("taskguid");
            String centerguid = obj.getString("centerguid");
            AuditTask task = (AuditTask) this.taskservice.getAuditTaskByGuid(taskguid, true).getResult();
            if (task != null) {
                String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
                (new License()).setLicense(licenseName);
                String doctem = "";
                String taskurl = "";
                if (StringUtil.isNotBlank(centerguid)) {
                    SqlConditionUtil downconfig = new SqlConditionUtil();
                    downconfig.eq("centerguid", centerguid);
                    downconfig.eq("configname", "AS_TASK_URL");
                    AuditOrgaConfig doc = (AuditOrgaConfig) this.auditconfigservice.getCenterConfig(downconfig.getMap())
                            .getResult();
                    if (StringUtil.isNotBlank(doc)) {
                        taskurl = doc.getConfigvalue();
                        dataJson.put("taskurl", doc.getConfigvalue());
                    } else {
                        taskurl = "";
                    }
                }

                String arg27 = (String) this.handleConfigservice.getFrameConfig("AS_LOCAL_DOWNLOAD_URL", centerguid)
                        .getResult();
                if (StringUtil.isNotBlank(arg27)) {
                    if (StringUtil.isBlank(taskurl)) {
                        doctem = arg27
                                + "/znsb/equipmentdisplay/selfservicemachine/print/tasktempletwithoutqrcode.docx";
                    } else {
                        doctem = arg27 + "/znsb/equipmentdisplay/selfservicemachine/print/tasktemplet.docx";
                    }
                } else if (StringUtil.isBlank(taskurl)) {
                    doctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                            + "/znsb/equipmentdisplay/selfservicemachine/print/tasktempletwithoutqrcode.docx";
                } else {
                    doctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                            + "/znsb/equipmentdisplay/selfservicemachine/print/tasktemplet.docx";
                }

                Document arg28 = new Document(doctem);
                //system.out.println(doctem);
                String[] fieldNames = null;
                Object[] values = null;
                HashMap map = new HashMap(16);
                map.put("TaskName", task.getTaskname());
                map.put("ItemID", task.getItem_id());
                map.put("OUName", task.getOuname());
                map.put("SUPERVISETEL", task.getSupervise_tel());
                if (StringUtil.isNotBlank(task.getStr("accept_address_info"))) {
                    List<Record> hours = SyncbanlididiansjService.syncbanlididiansj(task.getStr("accept_address_info"));
                    if (hours.size() > 0 && hours.get(0) != null) {

                        map.put("BLTime", hours.get(0).getStr("officehour"));
                    }
                    else {
                        map.put("BLTime", task.getTransact_time());
                    }
                }
                else {
                    map.put("BLTime", task.getTransact_time());
                }
                map.put("BLLocation", task.getTransact_addr());
                map.put("BLOUName", task.getOuname());
                map.put("LINKTEL", task.getLink_tel());
                if ("1".equals(task.getCharge_flag().toString())) {
                    map.put("ChargeFlag", "是");
                    AuditTaskExtension num = (AuditTaskExtension) this.taskextensionservice
                            .getTaskExtensionByTaskGuid(taskguid, true).getResult();
                    if (num != null) {
                        map.put("ChargeWhen", this.codeitemsservice.getItemTextByCodeName("何时收费",
                                String.valueOf(num.getCharge_when())));
                    } else {
                        map.put("ChargeWhen", "无");
                    }
                } else {
                    map.put("ChargeFlag", "否");
                    map.put("ChargeWhen", "无");
                }

                if ("1".equals(task.getType().toString())) {
                    map.put("AnticipateDay", "即办");
                    map.put("PromiseDay", "即办");
                } else {
                    map.put("AnticipateDay", task.getAnticipate_day() + "个工作日");
                    map.put("PromiseDay", task.getPromise_day() + "个工作日");
                }

                fieldNames = new String[map.size()];
                values = new Object[map.size()];
                int arg29 = 0;

                for (Iterator taskmateriallist = map.entrySet().iterator(); taskmateriallist.hasNext(); ++arg29) {
                    Entry dataList = (Entry) taskmateriallist.next();
                    fieldNames[arg29] = (String) dataList.getKey();
                    values[arg29] = dataList.getValue();
                }

                arg28.getMailMerge().execute(fieldNames, values);
                List arg30 = (List) this.taskmaterialservice.selectTaskMaterialListByTaskGuid(taskguid, true)
                        .getResult();
                ArrayList arg31 = new ArrayList();
                HashMap data1 = null;
                if (arg30.size() == 0) {
                    data1 = new HashMap(16);
                    data1.put("TASKMATERIAL", "无");
                    arg31.add(data1);
                } else {
                    for (int qrCode = 0; qrCode < arg30.size(); ++qrCode) {
                        data1 = new HashMap(16);
                        data1.put("TASKMATERIAL",
                                qrCode + 1 + "、" + ((AuditTaskMaterial) arg30.get(qrCode)).getMaterialname());
                        arg31.add(data1);
                    }
                }

                arg28.getMailMerge().executeWithRegions(new MapMailMergeDataSource(arg31, "Material"));
                InputStream arg32 = QrcodeUtil.getQrCode(taskurl + "?taskguid=" + taskguid, 150, 150);
                if (StringUtil.isNotBlank(taskurl) && arg32 != null) {
                    DocumentBuilder outputStream = new DocumentBuilder(arg28);
                    outputStream.moveToBookmark("EWM");
                    outputStream.insertImage(arg32);
                }

                ByteArrayOutputStream arg33 = new ByteArrayOutputStream();
                arg28.save(arg33, 10);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(arg33.toByteArray());
                long size = (long) inputStream.available();
                FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                frameAttachInfo.setAttachFileName("事项.doc");
                frameAttachInfo.setCliengTag("事项打印");
                frameAttachInfo.setUploadUserGuid("");
                frameAttachInfo.setUploadUserDisplayName("");
                frameAttachInfo.setUploadDateTime(new Date());
                frameAttachInfo.setContentType("application/msword");
                frameAttachInfo.setAttachLength(Long.valueOf(size));
                dataJson.put("taskdocattachguid",
                        this.attachservice.addAttach(frameAttachInfo, inputStream).getAttachGuid());
                arg33.close();
                inputStream.close();
                arg32.close();
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException arg26) {
            this.log.info("接口getTaskDetailDoc异常" + arg26.getMessage());
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + arg26.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/getqrcodeurl"}, method = {RequestMethod.POST})
    public String getQrcodeUrl(@RequestBody String params, @Context HttpServletRequest request) throws Exception {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            JSONObject dataJson = new JSONObject();
            String taskguid = obj.getString("taskguid");
            String centerguid = obj.getString("centerguid");
            String taskurl = "";
            String qrcodeurl;
            if (StringUtil.isNotBlank(centerguid)) {
                qrcodeurl = (String) this.handleConfigservice.getFrameConfig("AS_TASK_URL", centerguid).getResult();
                if (StringUtil.isNotBlank(qrcodeurl)) {
                    taskurl = qrcodeurl;
                } else {
                    taskurl = "";
                }
            }

            qrcodeurl = taskurl + "?taskguid=" + taskguid;
            dataJson.put("taskurl", qrcodeurl);
            dataJson.put("isshowqrcode", StringUtil.isNotBlank(taskurl) ? "1" : "0");
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException arg9) {
            this.log.info("接口getTaskDetailDoc异常" + arg9.getMessage());
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + arg9.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/getTaskDetailDoc_en"}, method = {RequestMethod.POST})
    public String getTaskDetailDoc_en(@RequestBody String params, @Context HttpServletRequest request)
            throws Exception {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            JSONObject dataJson = new JSONObject();
            String taskguid = obj.getString("taskguid");
            String centerguid = obj.getString("centerguid");
            AuditTask task = (AuditTask) this.taskservice.getAuditTaskByGuid(taskguid, true).getResult();
            if (task != null) {
                String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
                (new License()).setLicense(licenseName);
                String doctem = "";
                String taskurl = "";
                if (StringUtil.isNotBlank(centerguid)) {
                    SqlConditionUtil downconfig = new SqlConditionUtil();
                    downconfig.eq("centerguid", centerguid);
                    downconfig.eq("configname", "AS_TASK_URL");
                    AuditOrgaConfig doc = (AuditOrgaConfig) this.auditconfigservice.getCenterConfig(downconfig.getMap())
                            .getResult();
                    if (StringUtil.isNotBlank(doc)) {
                        taskurl = doc.getConfigvalue();
                        dataJson.put("taskurl", doc.getConfigvalue());
                    } else {
                        taskurl = "";
                    }
                }

                String arg27 = (String) this.handleConfigservice.getFrameConfig("AS_LOCAL_DOWNLOAD_URL", centerguid)
                        .getResult();
                if (StringUtil.isNotBlank(arg27)) {
                    if (StringUtil.isBlank(taskurl)) {
                        doctem = arg27
                                + "/znsb/equipmentdisplay/selfservicemachine_en/print/tasktempletwithoutqrcode.docx";
                    } else {
                        doctem = arg27 + "/znsb/equipmentdisplay/selfservicemachine_en/print/tasktemplet.docx";
                    }
                } else if (StringUtil.isBlank(taskurl)) {
                    doctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                            + "/znsb/equipmentdisplay/selfservicemachine_en/print/tasktempletwithoutqrcode.docx";
                } else {
                    doctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                            + "/znsb/equipmentdisplay/selfservicemachine_en/print/tasktemplet.docx";
                }

                Document arg28 = new Document(doctem);
                //system.out.println(doctem);
                String[] fieldNames = null;
                Object[] values = null;
                HashMap map = new HashMap(16);
                map.put("TaskName", task.getTaskname());
                map.put("ItemID", task.getItem_id());
                map.put("OUName", task.getOuname());
                map.put("SUPERVISETEL", task.getSupervise_tel());
                map.put("BLTime", task.getTransact_time());
                map.put("BLLocation", task.getTransact_addr());
                map.put("BLOUName", task.getOuname());
                map.put("LINKTEL", task.getLink_tel());
                if ("1".equals(task.getCharge_flag().toString())) {
                    map.put("ChargeFlag", "Yes");
                    AuditTaskExtension num = (AuditTaskExtension) this.taskextensionservice
                            .getTaskExtensionByTaskGuid(taskguid, true).getResult();
                    if (num != null) {
                        map.put("ChargeWhen", this.codeitemsservice.getItemTextByCodeName("何时收费",
                                String.valueOf(num.getCharge_when())));
                    } else {
                        map.put("ChargeWhen", "None");
                    }
                } else {
                    map.put("ChargeFlag", "No");
                    map.put("ChargeWhen", "None");
                }

                if ("1".equals(task.getType().toString())) {
                    map.put("AnticipateDay", "Immediately");
                    map.put("PromiseDay", "Immediately");
                } else {
                    map.put("AnticipateDay", task.getAnticipate_day() + "Workday");
                    map.put("PromiseDay", task.getPromise_day() + "Workday");
                }

                fieldNames = new String[map.size()];
                values = new Object[map.size()];
                int arg29 = 0;

                for (Iterator taskmateriallist = map.entrySet().iterator(); taskmateriallist.hasNext(); ++arg29) {
                    Entry dataList = (Entry) taskmateriallist.next();
                    fieldNames[arg29] = (String) dataList.getKey();
                    values[arg29] = dataList.getValue();
                }

                arg28.getMailMerge().execute(fieldNames, values);
                List arg30 = (List) this.taskmaterialservice.selectTaskMaterialListByTaskGuid(taskguid, true)
                        .getResult();
                ArrayList arg31 = new ArrayList();
                HashMap data1 = null;
                if (arg30.size() == 0) {
                    data1 = new HashMap(16);
                    data1.put("TASKMATERIAL", "无");
                    arg31.add(data1);
                } else {
                    for (int qrCode = 0; qrCode < arg30.size(); ++qrCode) {
                        data1 = new HashMap(16);
                        data1.put("TASKMATERIAL",
                                qrCode + 1 + "、" + ((AuditTaskMaterial) arg30.get(qrCode)).getMaterialname());
                        arg31.add(data1);
                    }
                }

                arg28.getMailMerge().executeWithRegions(new MapMailMergeDataSource(arg31, "Material"));
                InputStream arg32 = QrcodeUtil.getQrCode(taskurl + "?taskguid=" + taskguid, 150, 150);
                if (StringUtil.isNotBlank(taskurl) && arg32 != null) {
                    DocumentBuilder outputStream = new DocumentBuilder(arg28);
                    outputStream.moveToBookmark("EWM");
                    outputStream.insertImage(arg32);
                }

                ByteArrayOutputStream arg33 = new ByteArrayOutputStream();
                arg28.save(arg33, 10);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(arg33.toByteArray());
                long size = (long) inputStream.available();
                FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                frameAttachInfo.setAttachFileName("事项.doc");
                frameAttachInfo.setCliengTag("事项打印");
                frameAttachInfo.setUploadUserGuid("");
                frameAttachInfo.setUploadUserDisplayName("");
                frameAttachInfo.setUploadDateTime(new Date());
                frameAttachInfo.setContentType("application/msword");
                frameAttachInfo.setAttachLength(Long.valueOf(size));
                dataJson.put("taskdocattachguid",
                        this.attachservice.addAttach(frameAttachInfo, inputStream).getAttachGuid());
                arg33.close();
                inputStream.close();
                arg32.close();
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException arg26) {
            this.log.info("接口getTaskDetailDoc异常" + arg26.getMessage());
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + arg26.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/checkconfigure"}, method = {RequestMethod.POST})
    public String hzcheckconfigure(@RequestBody String params) throws Exception {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            JSONObject dataJson = new JSONObject();
            String taskguid = obj.getString("taskguid");
            AuditZnsbCentertask auditZnsbCentertask = (AuditZnsbCentertask) this.centertaskservice
                    .getDetailbyTaskGuid(taskguid).getResult();
            if (StringUtil.isNotBlank(auditZnsbCentertask)) {
                String taskid = auditZnsbCentertask.getTask_id();
                if (StringUtil.isNotBlank(taskid)) {
                    AuditZnsbTaskConfigure auditZnsbTaskConfigure = (AuditZnsbTaskConfigure) this.auditZnsbTaskConfigureService
                            .getDetailByTaskId(taskid).getResult();
                    if (StringUtil.isNotBlank(auditZnsbTaskConfigure)) {
                        if ("1".equals(auditZnsbTaskConfigure.getIs_enable().toString())) {
                            dataJson.put("msg", "1");
                            dataJson.put("url", auditZnsbTaskConfigure.getUrlconf());
                        } else {
                            dataJson.put("msg", "0");
                        }

                        dataJson.put("isusecabinet", auditZnsbTaskConfigure.getIsusecabinet());
                    }
                }
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException arg8) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + arg8.getMessage(), "");
        }
    }
}