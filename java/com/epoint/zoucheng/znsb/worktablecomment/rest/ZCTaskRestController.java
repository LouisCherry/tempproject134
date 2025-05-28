package com.epoint.zoucheng.znsb.worktablecomment.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.epoint.auditproject.auditdoc.action.MapMailMergeDataSource;
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
import com.epoint.basic.audittask.element.domain.AuditTaskElement;
import com.epoint.basic.audittask.element.inter.IAuditTaskElementService;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskCase;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.audittask.option.domain.AuditTaskOption;
import com.epoint.basic.audittask.option.inter.IAuditTaskOptionService;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.QrcodeUtil;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;

@RestController
@RequestMapping("/zcselfservicetask")
public class ZCTaskRestController
{

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
    
    /**
     * 事项要素选项相关
     */
    @Autowired
    private IAuditTaskOptionService iAuditTaskOptionService;

    /**
     * 事项要素相关
     */
    @Autowired
    private IAuditTaskElementService iAuditTaskElementService;
    
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 获取热门事项
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getHotTaskList", method = RequestMethod.POST)
    public String getHotTaskList(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String searchname = obj.getString("searchname");
            String centerguid = obj.getString("centerguid");
            String applyertype = obj.getString("applyertype");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");

            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject hottaskJson = new JSONObject();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("centerguid", centerguid);
            sql.eq("is_hottask", QueueConstant.CONSTANT_STR_ONE);
            sql.eq("is_enable", QueueConstant.CONSTANT_STR_ONE);
            if (StringUtil.isNotBlank(applyertype)) {
                sql.like("applyertype", applyertype);
            }
            if (StringUtil.isNotBlank(searchname)) {
                sql.like("taskname", searchname);
            }
            PageData<AuditZnsbCentertask> hottaskpagedata = centertaskservice
                    .getCenterTaskPageData(sql.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                            Integer.parseInt(pageSize), "hottaskordernum", "desc")
                    .getResult();

            int totalcount = hottaskpagedata.getRowCount();
            for (AuditZnsbCentertask hottask : hottaskpagedata.getList()) {
                hottaskJson = new JSONObject();

                hottaskJson.put("taskname", hottask.getTaskname());
                hottaskJson.put("taskguid", hottask.getTaskguid());
                list.add(hottaskJson);
            }

            dataJson.put("hottasklist", list);
            dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 获取事项分类
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getTaskDict", method = RequestMethod.POST)
    public String getTaskDict(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();

            String no = obj.getString("no");
            String applyertype = obj.getString("applyertype");
            String searchname = obj.getString("searchname");
            String centerguid = obj.getString("centerguid");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");

            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject dictJson = new JSONObject();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("LENGTH(no)", "4");
            sql.like("No", no);
            if(StringUtil.isNotBlank(searchname)){
                sql.like("CLASS_NAME", searchname);
            }
            PageData<AuditTaskDict> dictpagedata = taskdictservice.getAuditTaskDictPageData(sql.getMap(),
                    Integer.parseInt(currentPage) * Integer.parseInt(pageSize), Integer.parseInt(pageSize), "NO", "asc")
                    .getResult();

            int totalcount = dictpagedata.getRowCount();
            SqlConditionUtil tasksql = new SqlConditionUtil();
            tasksql.eq("centerguid", centerguid);
            tasksql.eq("is_enable", QueueConstant.CONSTANT_STR_ONE);
            for (AuditTaskDict dict : dictpagedata.getList()) {
                dictJson = new JSONObject();

                dictJson.put("classname", dict.getClassname());
                dictJson.put("dictid", dict.getDictid());
                dictJson.put("no", dict.getNo());
              
                if (StringUtil.isNotBlank(applyertype)) {
                    tasksql.like("applyertype", applyertype);
                }
                if (StringUtil.isNotBlank(dict.getNo())) {
                    tasksql.like("taskmap", dict.getNo() + ";");
                }
                List<AuditZnsbCentertask> result = centertaskservice.getCenterTaskList(tasksql.getMap()).getResult();
                if(result!=null&&!result.isEmpty()){
                    dictJson.put("tasknum", result.size());
                }
                else{
                    dictJson.put("tasknum", QueueConstant.CONSTANT_STR_ZERO);
                }
               
                list.add(dictJson);
            }

            dataJson.put("dictlist", list);
            dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 根据事项名称模糊查询出事项个数
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getTaskListCount", method = RequestMethod.POST)
    public String getTaskListCount(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String centerguid = obj.getString("centerguid");
            String searchTaskName = obj.getString("searchname");
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("centerguid", centerguid);
            sql.like("taskname", searchTaskName);
            List<AuditZnsbCentertask> result = centertaskservice.getCenterTaskList(sql.getMap()).getResult();
            dataJson.put("totalcount", result.size());
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
    
    /**
     * 获取事项列表
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getTaskList", method = RequestMethod.POST)
    public String getTaskList(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String centerguid = obj.getString("centerguid");
            String applyertype = obj.getString("applyertype");
            String ouguid = obj.getString("ouguid");
            String no = obj.getString("no");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");
            // 增加搜索框
            String searchTaskName = obj.getString("searchname");

            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject hottaskJson = new JSONObject();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("centerguid", centerguid);
            sql.eq("is_enable", QueueConstant.CONSTANT_STR_ONE);
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
            PageData<AuditZnsbCentertask> hottaskpagedata = centertaskservice
                    .getCenterTaskPageData(sql.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                            Integer.parseInt(pageSize), "ordernum", "desc")
                    .getResult();

            int totalcount = hottaskpagedata.getRowCount();
            for (AuditZnsbCentertask hottask : hottaskpagedata.getList()) {
                hottaskJson = new JSONObject();

                hottaskJson.put("taskname", hottask.getTaskname());
                hottaskJson.put("taskguid", hottask.getTaskguid());
                list.add(hottaskJson);
            }

            dataJson.put("tasklist", list);
            dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 获取事项详情
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getTaskDetail", method = RequestMethod.POST)
    public String getTaskDetail(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String taskguid = obj.getString("taskguid");
            String centerguid = obj.getString("centerguid");
            String taskurl = "";
            AuditTask task = taskservice.getAuditTaskByGuid(taskguid, true).getResult();

            if (StringUtil.isNotBlank(centerguid)) {
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("centerguid", centerguid);
                sql.eq("configname", "AS_TASK_URL");
                AuditOrgaConfig config = auditconfigservice.getCenterConfig(sql.getMap()).getResult();
                if (StringUtil.isNotBlank(config)) {
                    taskurl = config.getConfigvalue();
                }
                else {
                    taskurl = "";
                }
            }

            if (task != null) {
                if (StringUtil.isNotBlank(taskurl)) {
                    dataJson.put("showqrcode", QueueConstant.Common_yes_String);
                }
                else {
                    dataJson.put("showqrcode", QueueConstant.Common_no_String);
                }
                dataJson.put("taskguid", task.getRowguid());
                dataJson.put("taskname", task.getTaskname());
                dataJson.put("itemid", task.getItem_id());
                dataJson.put("ouname", task.getOuname());
                
                //xml解析
                String xml = task.getStr("accept_address_info");
                Record backString =  readStringXmlOut(xml);
                if(StringUtil.isNotBlank(backString)){
                    if(StringUtil.isNotBlank(backString.getStr("hour"))){
                        dataJson.put("transacttime", backString.getStr("hour"));
                    }else{
                        dataJson.put("transacttime", task.getTransact_time());
                    }
                    
                    if(StringUtil.isNotBlank(backString.getStr("address"))){
                        dataJson.put("transactaddr", backString.getStr("address"));
                    }else{
                        dataJson.put("transactaddr", task.getTransact_addr());
                    }
                }else{
                    dataJson.put("transacttime", task.getTransact_time());
                    dataJson.put("transactaddr", task.getTransact_addr());
                }
                if ("1".equals(task.getType().toString())) {
                    dataJson.put("promiseday", "即办");
                    dataJson.put("anticipateday", "即办");
                }
                else {
                    dataJson.put("promiseday", task.getPromise_day() + "个工作日");
                    dataJson.put("anticipateday", task.getAnticipate_day() + "个工作日");
                }

                dataJson.put("linktel", task.getLink_tel());
                dataJson.put("supervisetel", task.getSupervise_tel());
                dataJson.put("condition", task.getAcceptcondition());
                dataJson.put("bylaw", task.getBy_law());

                dataJson.put("type", task.getType());
                dataJson.put("typetext",
                        codeitemsservice.getItemTextByCodeName("事项类型", String.valueOf(task.getType())));

                dataJson.put("applyertype", task.getApplyertype());
                if ("10;".equals(task.getApplyertype()) || "10".equals(task.getApplyertype())) {
                    dataJson.put("applyertypetext", "企业");
                }
                else if ("20;".equals(task.getApplyertype()) || "20".equals(task.getApplyertype())) {
                    dataJson.put("applyertypetext", "个人");
                }
                else {
                    dataJson.put("applyertypetext", "个人、企业");
                }

                dataJson.put("chargeflag", task.getCharge_flag());

                dataJson.put("chargeflagtext",
                        codeitemsservice.getItemTextByCodeName("是否", String.valueOf(task.getCharge_flag())));

                AuditTaskExtension taskextension = taskextensionservice.getTaskExtensionByTaskGuid(taskguid, true)
                        .getResult();
                if (taskextension != null) {
                    dataJson.put("chargewhen", taskextension.getCharge_when());
                    dataJson.put("chargewhentext", codeitemsservice.getItemTextByCodeName("何时收费",
                            String.valueOf(taskextension.getCharge_when())));
                }

                AuditTaskResult taskresult = taskresultservice.getAuditResultByTaskGuid(taskguid, true).getResult();
                if (taskresult != null) {
                    if (taskresult.getResulttype() == ZwfwConstant.LHSP_RESULTTYPE_ZZ) {
                        dataJson.put("iscert", "1");
                        dataJson.put("iscerttext", "是");
                    }
                    else {
                        dataJson.put("iscert", "0");
                        dataJson.put("iscerttext", "否");
                    }
                }
                else {
                    dataJson.put("iscert", "0");
                    dataJson.put("iscerttext", "否");
                }

                AuditZnsbCentertask centertask = centertaskservice.getDetailbyTaskGuid(taskguid).getResult();
                if (centertask != null) {
                    dataJson.put("reservationmanagement", centertask.getReservationmanagement());
                    dataJson.put("webapplytype", centertask.getWebapplytype());
                    dataJson.put("isshowform", centertask.getIs_show_form());
                }

                List<AuditTaskCase> auditTaskCases = auditTaskCaseService.selectTaskCaseByTaskGuid(taskguid)
                        .getResult();

                List<JSONObject> caseconditionList = new ArrayList<JSONObject>();
                JSONObject conditionJson = new JSONObject();
                if (!auditTaskCases.isEmpty()) {
                    for (AuditTaskCase auditTaskCase : auditTaskCases) {
                        conditionJson = new JSONObject();
                        conditionJson.put("taskcaseguid", auditTaskCase.getRowguid());// 多情形标识
                        conditionJson.put("casename", auditTaskCase.getCasename());// 多情形名称
                        caseconditionList.add(conditionJson);
                    }
                }
                dataJson.put("caselist", caseconditionList);

                List<JSONObject> list = new ArrayList<JSONObject>();
                JSONObject matericalJson = new JSONObject();
                List<AuditTaskMaterial> taskmateriallist = taskmaterialservice
                        .selectTaskMaterialListByTaskGuid(taskguid, true).getResult();

                for (AuditTaskMaterial taskmaterial : taskmateriallist) {
                    matericalJson = new JSONObject();

                    matericalJson.put("materialname", taskmaterial.getMaterialname());
                    matericalJson.put("materialid", taskmaterial.getMaterialid());
                    matericalJson.put("necessity", taskmaterial.getNecessity());
                    matericalJson.put("xsreview", taskmaterial.getXsreviewkp());
                    list.add(matericalJson);
                }

                dataJson.put("taskurl", taskurl);
                dataJson.put("matericallist", list);

                if (StringUtil.isNotBlank(task.getTaskoutimgguid())) {
                    List<FrameAttachInfo> attachInfolist = attachservice
                            .getAttachInfoListByGuid(task.getTaskoutimgguid());
                    if (attachInfolist != null && !attachInfolist.isEmpty()) {
                        dataJson.put("taskoutimgguid", attachInfolist.get(0).getAttachGuid());
                    }
                    else {
                        dataJson.put("taskoutimgguid", "");
                    }

                }
                else {
                    dataJson.put("taskoutimgguid", "");
                }
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 获取多情形材料列表
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/getTaskCaseMaterialsByCaseguid", method = RequestMethod.POST)
    public String getTaskCaseMaterialsByCaseguid(@RequestBody String params, @Context HttpServletRequest request) {
        try {

            // 1、入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);

            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String taskcaseguid = obj.getString("taskcaseguid");
            String taskguid = obj.getString("taskguid");
            List<JSONObject> taskMaterialList = new ArrayList<JSONObject>();
            // 2、获取多情形材料列表
            if (StringUtil.isNotBlank(taskcaseguid)) {
                Map<String, Integer> caseMap = null;
                List<AuditTaskMaterialCase> auditTaskMaterialCases = auditTaskMaterialCaseService
                        .selectTaskMaterialCaseByCaseGuid(taskcaseguid).getResult();
                caseMap = new HashMap<>(16);
                // 转成map方便查找
                if (auditTaskMaterialCases != null && !auditTaskMaterialCases.isEmpty()) {
                    for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                        caseMap.put(auditTaskMaterialCase.getMaterialguid(), auditTaskMaterialCase.getNecessity());
                    }
                }
                // 3、获取事项配置材料
                List<AuditTaskMaterial> auditTaskMaterials = taskmaterialservice
                        .selectTaskMaterialListByTaskGuid(taskguid, true).getResult();
                JSONObject materialJson = new JSONObject();
                for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                    if (caseMap.containsKey(auditTaskMaterial.getRowguid())) {
                        materialJson = new JSONObject();
                        materialJson.put("materialguid", auditTaskMaterial.getRowguid());// 材料标识
                        materialJson.put("materialname", auditTaskMaterial.getMaterialname());// 材料名称
                        materialJson.put("necessity", auditTaskMaterial.getNecessity());//是否必需
                        taskMaterialList.add(materialJson);
                    }
                }
            }
            // 4、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            dataJson.put("matericallist", taskMaterialList);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);

        }
        catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取事项详情Doc
     * 
     * @params params
     * @return
     * @throws Exception
     * 
     * 
     */
    @RequestMapping(value = "/getTaskDetailDoc", method = RequestMethod.POST)
    public String getTaskDetailDoc(@RequestBody String params, @Context HttpServletRequest request) throws Exception {

        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String taskguid = obj.getString("taskguid");
            String centerguid = obj.getString("centerguid");

            AuditTask task = taskservice.getAuditTaskByGuid(taskguid, true).getResult();
            if (task != null) {

                String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
                new License().setLicense(licenseName);
                String doctem = "";
                String taskurl = "";
                if (StringUtil.isNotBlank(centerguid)) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("centerguid", centerguid);
                    sql.eq("configname", "AS_TASK_URL");
                    AuditOrgaConfig config = auditconfigservice.getCenterConfig(sql.getMap()).getResult();
                    if (StringUtil.isNotBlank(config)) {
                        taskurl = config.getConfigvalue();
                        dataJson.put("taskurl", config.getConfigvalue());
                    }
                    else {
                        taskurl = "";
                    }
                }
                String downconfig = handleConfigservice.getFrameConfig("AS_LOCAL_DOWNLOAD_URL", centerguid).getResult();
                if (StringUtil.isNotBlank(downconfig)) {
                    if (StringUtil.isBlank(taskurl)) {
                        doctem = downconfig
                                + "/jiningzwfw/individuation/zoucheng/equipmentdisplay/selfservicemachine/print/tasktempletwithoutqrcode.docx";
                    }
                    else {
                        doctem = downconfig + "/jiningzwfw/individuation/zoucheng/equipmentdisplay/selfservicemachine/print/tasktemplet.docx";
                    }

                }
                else {
                    if (StringUtil.isBlank(taskurl)) {
                        doctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                                + "/jiningzwfw/individuation/zoucheng/equipmentdisplay/selfservicemachine/print/tasktempletwithoutqrcode.docx";
                    }
                    else {
                        doctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                                + "/jiningzwfw/individuation/zoucheng/equipmentdisplay/selfservicemachine/print/tasktemplet.docx";
                    }

                }
                Document doc = new Document(doctem);
                log.info(doctem);
                String[] fieldNames = null;
                Object[] values = null;
                // 获取域与对应的值
                Map<String, String> map = new HashMap<String, String>(16);
                map.put("TaskName", task.getTaskname());
                map.put("ItemID", task.getItem_id());
                map.put("OUName", task.getOuname());
                map.put("SUPERVISETEL", task.getSupervise_tel());
                
                //xml解析
                String xml = task.getStr("accept_address_info");
                
                Record backString =  readStringXmlOut(xml);
                if(StringUtil.isNotBlank(backString)){
                    if(StringUtil.isNotBlank(backString.getStr("hour"))){
                        map.put("BLTime", backString.getStr("hour"));
                    }else{
                        map.put("BLTime", task.getTransact_time());
                    }
                    
                    if(StringUtil.isNotBlank(backString.getStr("address"))){
                        map.put("BLLocation", backString.getStr("address"));
                    }else{
                        map.put("BLLocation", task.getTransact_addr());
                    }
                }else{
                    map.put("BLTime", task.getTransact_time());
                    map.put("BLLocation", task.getTransact_addr());
                }
     
                map.put("BLOUName", task.getOuname());
                map.put("LINKTEL", task.getLink_tel());
                if (QueueConstant.Common_yes_String.equals(task.getCharge_flag().toString())) {
                    map.put("ChargeFlag", "是");
                    AuditTaskExtension taskextension = taskextensionservice.getTaskExtensionByTaskGuid(taskguid, true)
                            .getResult();
                    if (taskextension != null) {
                        map.put("ChargeWhen", codeitemsservice.getItemTextByCodeName("何时收费",
                                String.valueOf(taskextension.getCharge_when())));
                    }
                    else {
                        map.put("ChargeWhen", "无");
                    }
                }
                else {
                    map.put("ChargeFlag", "否");
                    map.put("ChargeWhen", "无");
                }
                if ("1".equals(task.getType().toString())) {
                    map.put("AnticipateDay", "即办");
                    map.put("PromiseDay", "即办");
                }
                else {
                    map.put("AnticipateDay", task.getAnticipate_day() + "个工作日");
                    map.put("PromiseDay", task.getPromise_day() + "个工作日");
                }

                fieldNames = new String[map.size()];
                values = new Object[map.size()];
                int num = 0;

                for (Entry<String, String> entry : map.entrySet()) {
                    fieldNames[num] = entry.getKey();
                    values[num] = entry.getValue();
                    num++;
                }
                // 替换域
                doc.getMailMerge().execute(fieldNames, values);
                // 替换表格--实际是替换材料
                List<AuditTaskMaterial> taskmateriallist = taskmaterialservice
                        .selectTaskMaterialListByTaskGuid(taskguid, true).getResult();
                List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
                Map<String, Object> data1 = null;
                if (taskmateriallist.isEmpty()) {
                    data1 = new HashMap<String, Object>(16);
                    data1.put("TASKMATERIAL", "无");
                    dataList.add(data1);
                }
                else {
                    for (int i = 0; i < taskmateriallist.size(); i++) {
                        data1 = new HashMap<String, Object>(16);
                        data1.put("TASKMATERIAL", (i + 1) + "、" + taskmateriallist.get(i).getMaterialname());
                        dataList.add(data1);
                    }
                }

                doc.getMailMerge().executeWithRegions(new MapMailMergeDataSource(dataList, "Material"));

                // 添加二维码
                InputStream qrCode = QrcodeUtil.getQrCode(taskurl + "?taskguid=" + taskguid, 150, 150);
                if (StringUtil.isNotBlank(taskurl)) {
                    if (qrCode != null) {
                        DocumentBuilder build = new DocumentBuilder(doc);
                        // 指定对应的书签
                        build.moveToBookmark("EWM");
                        // 插入附件流信息
                        build.insertImage(qrCode);
                    }
                }

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                doc.save(outputStream, SaveFormat.DOC);// 保存成word
                ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                long size = inputStream.available();

                // 附件信息
                FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                frameAttachInfo.setAttachFileName("事项.doc");
                frameAttachInfo.setCliengTag("事项打印");
                frameAttachInfo.setUploadUserGuid("");
                frameAttachInfo.setUploadUserDisplayName("");
                frameAttachInfo.setUploadDateTime(new Date());
                frameAttachInfo.setContentType("application/msword");
                frameAttachInfo.setAttachLength(size);
                dataJson.put("taskdocattachguid",
                        attachservice.addAttach(frameAttachInfo, inputStream).getAttachGuid());

                outputStream.close();
                inputStream.close();
                if (qrCode != null) {
                    qrCode.close();
                }
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            log.info("接口getTaskDetailDoc异常" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 获取事项二维码地址
     * 
     * @params params
     * @return
     * @throws Exception
     * 
     * 
     */
    @RequestMapping(value = "/getqrcodeurl", method = RequestMethod.POST)
    public String getQrcodeUrl(@RequestBody String params, @Context HttpServletRequest request) throws Exception {

        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String taskguid = obj.getString("taskguid");
            String centerguid = obj.getString("centerguid");

            String taskurl = "";
            if (StringUtil.isNotBlank(centerguid)) {
                String config = handleConfigservice.getFrameConfig("AS_TASK_URL", centerguid).getResult();
                if (StringUtil.isNotBlank(config)) {
                    taskurl = config;

                }
                else {
                    taskurl = "";
                }
            }

            String qrcodeurl = taskurl + "?taskguid=" + taskguid;

            dataJson.put("taskurl", qrcodeurl);
            //taskurl为空时 不展示二维码
            dataJson.put("isshowqrcode", StringUtil.isNotBlank(taskurl) ? "1" : "0");

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            log.info("接口getTaskDetailDoc异常" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
    * 获取事项详情Doc_en
    * 
    * @params params
    * @return
    * @throws Exception
    * 
    * 
    */
    @RequestMapping(value = "/getTaskDetailDoc_en", method = RequestMethod.POST)
    public String getTaskDetailDoc_en(@RequestBody String params, @Context HttpServletRequest request)
            throws Exception {

        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String taskguid = obj.getString("taskguid");
            String centerguid = obj.getString("centerguid");

            AuditTask task = taskservice.getAuditTaskByGuid(taskguid, true).getResult();
            if (task != null) {

                String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
                new License().setLicense(licenseName);
                String doctem = "";
                String taskurl = "";
                if (StringUtil.isNotBlank(centerguid)) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("centerguid", centerguid);
                    sql.eq("configname", "AS_TASK_URL");
                    AuditOrgaConfig config = auditconfigservice.getCenterConfig(sql.getMap()).getResult();
                    if (StringUtil.isNotBlank(config)) {
                        taskurl = config.getConfigvalue();
                        dataJson.put("taskurl", config.getConfigvalue());
                    }
                    else {
                        taskurl = "";
                    }
                }
                String downconfig = handleConfigservice.getFrameConfig("AS_LOCAL_DOWNLOAD_URL", centerguid).getResult();
                if (StringUtil.isNotBlank(downconfig)) {
                    if (StringUtil.isBlank(taskurl)) {
                        doctem = downconfig
                                + "/znsb/equipmentdisplay/selfservicemachine_en/print/tasktempletwithoutqrcode.docx";
                    }
                    else {
                        doctem = downconfig + "/znsb/equipmentdisplay/selfservicemachine_en/print/tasktemplet.docx";
                    }

                }
                else {
                    if (StringUtil.isBlank(taskurl)) {
                        doctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                                + "/znsb/equipmentdisplay/selfservicemachine_en/print/tasktempletwithoutqrcode.docx";
                    }
                    else {
                        doctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                                + "/znsb/equipmentdisplay/selfservicemachine_en/print/tasktemplet.docx";
                    }

                }
                Document doc = new Document(doctem);
                log.info(doctem);
                String[] fieldNames = null;
                Object[] values = null;
                // 获取域与对应的值
                Map<String, String> map = new HashMap<String, String>(16);
                map.put("TaskName", task.getTaskname());
                map.put("ItemID", task.getItem_id());
                map.put("OUName", task.getOuname());
                map.put("SUPERVISETEL", task.getSupervise_tel());
                map.put("BLTime", task.getTransact_time());
                map.put("BLLocation", task.getTransact_addr());
                map.put("BLOUName", task.getOuname());
                map.put("LINKTEL", task.getLink_tel());
                if (QueueConstant.Common_yes_String.equals(task.getCharge_flag().toString())) {
                    map.put("ChargeFlag", "Yes");
                    AuditTaskExtension taskextension = taskextensionservice.getTaskExtensionByTaskGuid(taskguid, true)
                            .getResult();
                    if (taskextension != null) {
                        map.put("ChargeWhen", codeitemsservice.getItemTextByCodeName("何时收费",
                                String.valueOf(taskextension.getCharge_when())));
                    }
                    else {
                        map.put("ChargeWhen", "None");
                    }
                }
                else {
                    map.put("ChargeFlag", "No");
                    map.put("ChargeWhen", "None");
                }
                if ("1".equals(task.getType().toString())) {
                    map.put("AnticipateDay", "Immediately");
                    map.put("PromiseDay", "Immediately");
                }
                else {
                    map.put("AnticipateDay", task.getAnticipate_day() + "Workday");
                    map.put("PromiseDay", task.getPromise_day() + "Workday");
                }

                fieldNames = new String[map.size()];
                values = new Object[map.size()];
                int num = 0;

                for (Entry<String, String> entry : map.entrySet()) {
                    fieldNames[num] = entry.getKey();
                    values[num] = entry.getValue();
                    num++;
                }
                // 替换域
                doc.getMailMerge().execute(fieldNames, values);
                // 替换表格--实际是替换材料
                List<AuditTaskMaterial> taskmateriallist = taskmaterialservice
                        .selectTaskMaterialListByTaskGuid(taskguid, true).getResult();
                List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
                Map<String, Object> data1 = null;
                if (taskmateriallist.isEmpty()) {
                    data1 = new HashMap<String, Object>(16);
                    data1.put("TASKMATERIAL", "无");
                    dataList.add(data1);
                }
                else {
                    for (int i = 0; i < taskmateriallist.size(); i++) {
                        data1 = new HashMap<String, Object>(16);
                        data1.put("TASKMATERIAL", (i + 1) + "、" + taskmateriallist.get(i).getMaterialname());
                        dataList.add(data1);
                    }
                }

                doc.getMailMerge().executeWithRegions(new MapMailMergeDataSource(dataList, "Material"));

                // 添加二维码
                InputStream qrCode = QrcodeUtil.getQrCode(taskurl + "?taskguid=" + taskguid, 150, 150);
                if (StringUtil.isNotBlank(taskurl)) {
                    if (qrCode != null) {
                        DocumentBuilder build = new DocumentBuilder(doc);
                        // 指定对应的书签
                        build.moveToBookmark("EWM");
                        // 插入附件流信息
                        build.insertImage(qrCode);
                    }
                }

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                doc.save(outputStream, SaveFormat.DOC);// 保存成word
                ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                long size = inputStream.available();

                // 附件信息
                FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                frameAttachInfo.setAttachFileName("事项.doc");
                frameAttachInfo.setCliengTag("事项打印");
                frameAttachInfo.setUploadUserGuid("");
                frameAttachInfo.setUploadUserDisplayName("");
                frameAttachInfo.setUploadDateTime(new Date());
                frameAttachInfo.setContentType("application/msword");
                frameAttachInfo.setAttachLength(size);
                dataJson.put("taskdocattachguid",
                        attachservice.addAttach(frameAttachInfo, inputStream).getAttachGuid());

                outputStream.close();
                inputStream.close();
                if (qrCode != null) {
                    qrCode.close();
                }

            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            log.info("接口getTaskDetailDoc异常" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     *检查事项配置
     */
    @RequestMapping(value = "/checkconfigure", method = RequestMethod.POST)
    public String hzcheckconfigure(@RequestBody String params) throws Exception {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String taskguid = obj.getString("taskguid");
            AuditZnsbCentertask auditZnsbCentertask = centertaskservice.getDetailbyTaskGuid(taskguid).getResult();
            if (StringUtil.isNotBlank(auditZnsbCentertask)) {
                String taskid = auditZnsbCentertask.getTask_id();
                if (StringUtil.isNotBlank(taskid)) {
                    AuditZnsbTaskConfigure auditZnsbTaskConfigure = auditZnsbTaskConfigureService
                            .getDetailByTaskId(taskid).getResult();
                    if (StringUtil.isNotBlank(auditZnsbTaskConfigure)) {
                        if (QueueConstant.CONSTANT_STR_ONE.equals(auditZnsbTaskConfigure.getIs_enable().toString())) {
                            dataJson.put("msg", "1");
                            dataJson.put("url", auditZnsbTaskConfigure.getUrlconf());
                        }
                        else {
                            dataJson.put("msg", "0");
                        }
                        dataJson.put("isusecabinet", auditZnsbTaskConfigure.getIsusecabinet());
                    }
                }
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 判断该事项是否配有要素且要素选项配有材料
     * 
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/checkIsExistElement", method = RequestMethod.POST)
    public String checkIsExistElement(@RequestBody String params) {
        try {
            log.info("=======开始调用checkIsExistElement接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);

            JSONObject obj = (JSONObject) jsonObject.get("params");
            // 1.1、获取事项唯一标识
            String taskguid = obj.getString("taskguid");
            AuditTask task = taskservice.selectTaskByRowGuid(taskguid).getResult();
            if(StringUtil.isNotBlank(task)){
                // 2、根据taskId判断事项要素是否满足条件（选项配有材料或者后置要素的要素选项配有材料）
                boolean isExist = iAuditTaskElementService.checkELementMaterial(task.getTask_id()).getResult();
                // 2.1、 根据isExist的值，判定是否需要进行场景选择
                String isNeedFaq = (isExist) ? ZwfwConstant.CONSTANT_STR_ONE : ZwfwConstant.CONSTANT_STR_ZERO;
                JSONObject dataJson = new JSONObject();
                dataJson.put("isneedfaq", isNeedFaq);
                log.info("=======结束调用checkIsExistElement接口=======");
                return JsonUtils.zwdtRestReturn("1", "判断事项是否配有要素成功", dataJson.toString());
            }else{
                return JsonUtils.zwdtRestReturn("0", "事项存在异常", "");
            }
          
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkIsExistElement接口参数：params【" + params + "】=======");
            log.info("=======checkIsExistElement异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "判断事项是否配有要素失败：" + e.getMessage(), "");
        }
    }

    
    /**
     * 获取事项下要素及要素选项
     * 
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getTaskElement", method = RequestMethod.POST)
    public String getTaskElement(@RequestBody String params) {
        try {
            log.info("=======开始调用getTaskElement接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);

            JSONObject obj = (JSONObject) jsonObject.get("params");
            // 1.1、获取事项唯一标识
            String taskguid = obj.getString("taskguid");
            AuditTask task = taskservice.selectTaskByRowGuid(taskguid).getResult();
            if (StringUtil.isNotBlank(task)) {
                // 2、获取事项的情景要素列表 (没有前置要素的)
                // 2.1、 拼接查询条件
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.isBlankOrValue("draft", "0"); // 不为草稿
                sqlConditionUtil.eq("taskid", task.getTask_id()); // 事项id
                sqlConditionUtil.rightLike("preoptionguid", "start");
                List<AuditTaskElement> auditTaskElements = iAuditTaskElementService
                        .findListByCondition(sqlConditionUtil.getMap()).getResult();
                // 定义存储要素信息的List
                List<JSONObject> elementJsonList = new ArrayList<JSONObject>();
                if (auditTaskElements != null && auditTaskElements.size() > 0) {
                    for (AuditTaskElement auditTaskElement : auditTaskElements) {
                        // 定义单多选标志位，默认单选
                        String type = "radio";
                        String elementName = "[单选]";
                        // 定义存储要素信息的json
                        JSONObject elementJson = new JSONObject();
                        // 2.2 根据事项要素唯一标识查询事项要素选项
                        List<AuditTaskOption> auditTaskOptions = iAuditTaskOptionService
                                .findListByElementIdWithoutNoName(auditTaskElement.getRowguid()).getResult();
                        if (auditTaskOptions != null && auditTaskOptions.size() > 1) {
                            // 定义存储要素选项信息的List
                            List<JSONObject> optionJsonList = new ArrayList<JSONObject>();
                            for (AuditTaskOption auditTaskOption : auditTaskOptions) {
                                // 定义存储要素选项信息的json
                                JSONObject optionJson = new JSONObject();
                                optionJson.put("optionname", auditTaskOption.getOptionname());
                                optionJson.put("optionguid", auditTaskOption.getRowguid());
                              //  optionJson.put("optionnote", auditTaskOption.getOptionnote());
                                optionJsonList.add(optionJson);
                            }
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskElement.getMultiselect())) {
                                type = "checkbox";
                                elementName = "[多选]";
                            }
                            elementJson.put("type", type); // 单选还是多选的标志位
                            elementJson.put("optionlist", optionJsonList); // 要素选项列表
                            elementJson.put("elementquestion", elementName + auditTaskElement.getElementname()); // 要素问题
                            elementJson.put("elementname", auditTaskElement.getElementname()); // 要素名称
                            elementJson.put("elementguid", auditTaskElement.getRowguid()); // 要素唯一标识
                            elementJson.put("elementnote", auditTaskElement.getElementnote()); // 要素备注
                            elementJsonList.add(elementJson);
                        }
                    }
                }
                // 定义返回的JSON
                JSONObject dataJson = new JSONObject();
                dataJson.put("elementlist", elementJsonList);// 要素及要素选项列表
                log.info("=======结束调用getTaskElement接口=======");
                return JsonUtils.zwdtRestReturn("1", "事项要素获取成功", dataJson.toString());

            }
            else {
                log.info("=======结束调用getTaskElement接口=======");
                return JsonUtils.zwdtRestReturn("0", "事项信息异常", "");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskElement接口参数：params【" + params + "】=======");
            log.info("=======getTaskElement异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "事项要素获取失败：" + e.getMessage(), "");
        }
    }
    
    /**
     * 获取事项下要素及要素选项
     * 
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getTaskElementByOption", method = RequestMethod.POST)
    public String getTaskElementByOption(@RequestBody String params) {
        try {
            log.info("=======开始调用getTaskElementByOption接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);

            JSONObject obj = (JSONObject) jsonObject.get("params");
            // 1.1、获取要素选项唯一标识
            String optionGuid = obj.getString("optionguid");
            // 1.2、获取事项唯一标识
            String taskguid = obj.getString("taskguid");
            AuditTask task = taskservice.selectTaskByRowGuid(taskguid).getResult();
            if (StringUtil.isNotBlank(task)) {
                // 2、获取事项的要素列表 
                // 2.1、 拼接查询条件
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.isBlankOrValue("draft", "0"); // 不为草稿
                sqlConditionUtil.eq("taskid", task.getTask_id()); // 事项id
                if (optionGuid.indexOf(";") != -1) {
                    String guids[] = optionGuid.split(";");
                    String options = null;
                    List<String> list = new ArrayList<>();
                    if (guids != null && guids.length > 0) {
                        for (int i = 0; i < guids.length; i++) {
                            list.add(guids[i]);
                        }
                        options = "'" + StringUtil.join(list, "','") + "'";
                        sqlConditionUtil.in("preoptionguid", options);
                    }
                }
                else {
                    if (StringUtil.isNotBlank(optionGuid)) {
                        sqlConditionUtil.eq("preoptionguid", optionGuid);
                    }
                    else {
                        sqlConditionUtil.eq("preoptionguid", "preoptionguid");
                    }
                }
                List<AuditTaskElement> auditTaskElements = iAuditTaskElementService
                        .findListByCondition(sqlConditionUtil.getMap()).getResult();
                // 定义存储要素信息的List
                List<JSONObject> subElementJsonList = new ArrayList<JSONObject>();
                if (auditTaskElements != null && auditTaskElements.size() > 0) {
                    for (AuditTaskElement auditTaskElement : auditTaskElements) {
                        // 定义单多选标志位，默认单选
                        String type = "radio";
                        String elementName = "[单选]";
                        // 定义存储要素信息的Json
                        JSONObject subElementJson = new JSONObject();
                        // 2.2、查询要素选项信息
                        List<AuditTaskOption> auditTaskOptions = iAuditTaskOptionService
                                .findListByElementIdWithoutNoName(auditTaskElement.getRowguid()).getResult();
                        if (auditTaskOptions != null && auditTaskOptions.size() > 1) {
                            // 定义存储要素选项信息的List
                            List<JSONObject> optionJsonList = new ArrayList<JSONObject>();
                            for (AuditTaskOption auditTaskOption : auditTaskOptions) {
                                // 定义存储要素选项的Json
                                JSONObject optionJson = new JSONObject();
                                optionJson.put("optionname", auditTaskOption.getOptionname());
                                optionJson.put("optionguid", auditTaskOption.getRowguid());
                              //  optionJson.put("optionnote", auditTaskOption.getOptionnote());
                                optionJsonList.add(optionJson);
                            }
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskElement.getMultiselect())) {
                                type = "checkbox";
                                elementName = "[多选]";
                            }
                            subElementJson.put("type", type); // 单选还是多选的标志位
                            subElementJson.put("optionlist", optionJsonList); // 要素选项列表
                            subElementJson.put("subelementquestion",
                                    elementName + auditTaskElement.getElementquestion()); // 要素问题
                            subElementJson.put("subelementguid", auditTaskElement.getRowguid()); // 要素唯一标识
                            subElementJson.put("subelementname", auditTaskElement.getElementname()); // 要素名称
                            subElementJson.put("elementnote", auditTaskElement.getElementnote()); // 要素名称
                            subElementJsonList.add(subElementJson);
                        }
                    }
                }
                JSONObject dataJson = new JSONObject();
                dataJson.put("subelementlist", subElementJsonList);// 要素及要素选项列表
                log.info("=======结束调用getTaskElementByOption接口=======");
                return JsonUtils.zwdtRestReturn("1", "事项后置要素获取成功", dataJson.toString());
            }
            else {
                log.info("=======结束调用getTaskElementByOption接口=======");
                return JsonUtils.zwdtRestReturn("0", "事项信息异常", "");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskElementByOption接口参数：params【" + params + "】=======");
            log.info("=======getTaskElementByOption异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "事项后置要素获取失败：" + e.getMessage(), "");
        }
    }
    
    /**
     * 根据用户选择信息，获取情形唯一标识
     * 
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getCaseGuidBySelectedOptions", method = RequestMethod.POST)
    public String getCaseGuidBySelectedOptions(@RequestBody String params) {
        try {
            log.info("=======开始调用getCaseGuidBySelectedOptions接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);

            JSONObject obj = (JSONObject) jsonObject.get("params");
            // 1.1、获取用户选择信息
            String selectedOptions = obj.getString("selectedoptions");
            // 1.2、获取事项唯一标识
            String taskGuid = obj.getString("taskguid");
            // 1.3、获取事项id
            AuditTask task = taskservice.selectTaskByRowGuid(taskGuid).getResult();
            if (StringUtil.isNotBlank(task)) {
                String faqCaseGuid = "";
                if (StringUtil.isNotBlank(selectedOptions)) {
                    // 2、调用接口，通过用户选择信息获取faqCaseGuid
                    faqCaseGuid = auditTaskCaseService.checkOrAddNewCase(task.getTask_id(), taskGuid, selectedOptions)
                            .getResult();
                    if (ZwfwConstant.CONSTANT_STR_ZERO.equals(faqCaseGuid)) {
                        return JsonUtils.zwdtRestReturn("0", "获取事项情形标识失败！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户选择信息失败！", "");
                }
                JSONObject dataJson = new JSONObject();
                dataJson.put("faqcaseguid", faqCaseGuid); // 是否配置要素或要素下是否配置材料并标识
                log.info("=======结束调用getCaseGuidBySelectedOptions接口=======");
                return JsonUtils.zwdtRestReturn("1", "情形唯一标识获取成功", dataJson.toString());
            }
            else {
                log.info("=======结束调用getCaseGuidBySelectedOptions接口=======");
                return JsonUtils.zwdtRestReturn("0", "事项信息异常", "");
            }

        }catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCaseGuidBySelectedOptions接口参数：params【" + params + "】=======");
            log.info("=======getCaseGuidBySelectedOptions异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "情形唯一标识获取失败：" + e.getMessage(), "");
        }
    }
    
    /**
     * 根据情形guid获取情形信息
     * 
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getSelectedOptionsByCaseGuid", method = RequestMethod.POST)
    public String getSelectedOptionsByCaseGuid(@RequestBody String params) {
        try {
            log.info("=======开始调用getCaseGuidBySelectedOptions接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);

            JSONObject obj = (JSONObject) jsonObject.get("params");
            String faqcaseguid = obj.getString("faqcaseguid");
          
            AuditTaskCase audittaskcase = auditTaskCaseService.getAuditTaskCaseByRowguid(faqcaseguid).getResult();
            if (StringUtil.isNotBlank(audittaskcase)) {
                JSONObject dataJson = new JSONObject();
                dataJson.put("audittaskcase", audittaskcase);
                log.info("=======结束调用getCaseGuidBySelectedOptions接口=======");
                return JsonUtils.zwdtRestReturn("1", "事项情形信息获取成功", dataJson.toString());
            }
            else {
                log.info("=======结束调用getCaseGuidBySelectedOptions接口=======");
                return JsonUtils.zwdtRestReturn("0", "事项情形信息异常", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getSelectedOptionsByCaseGuid接口参数：params【" + params + "】=======");
            log.info("=======getSelectedOptionsByCaseGuid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "事项情形信息获取失败：" + e.getMessage(), "");
        }
    }
    
    public static Record readStringXmlOut(String xml) {
        
        Record record = null;
         org.dom4j.Document doc = null;
       try {
           if(StringUtil.isNotBlank(xml)){
               doc = DocumentHelper.parseText(xml); // 将字符串转为XML
               Node hour=doc.selectSingleNode("//OFFICE_HOUR");
               Node address=doc.selectSingleNode("//ACCEPT_ADDRESSS");
               //根据节点对象获取相应信息       
               record = new Record();
               String backhour=hour.getText();
               record.put("hour", backhour);

               String backaddress=address.getText();
               record.put("address", backaddress);
           }
          
      } catch (DocumentException e) {
           e.printStackTrace();
          } catch (Exception e) {
            e.printStackTrace();
          }
          return record;
     }
}
