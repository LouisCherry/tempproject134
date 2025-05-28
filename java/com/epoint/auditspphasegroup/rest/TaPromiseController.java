package com.epoint.auditspphasegroup.rest;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditspitem.api.IAuditSpItemService;
import com.epoint.auditspitem.api.entity.AuditSpItem;
import com.epoint.auditspparalleltask.api.IAuditSpParallelTaskService;
import com.epoint.auditspparalleltask.api.entity.AuditSpParallelTask;
import com.epoint.auditspphasebaseinfo.api.IAuditSpPhaseBaseinfoService;
import com.epoint.auditspphasebaseinfo.api.entity.AuditSpPhaseBaseinfo;
import com.epoint.auditspphasegroup.api.IAuditSpPhaseGroupService;
import com.epoint.auditspphasegroup.api.entity.AuditSpPhaseGroup;
import com.epoint.auditspphasetask.api.IAuditSpPhaseTaskService;
import com.epoint.auditspphasetask.api.entity.AuditSpPhaseTask;
import com.epoint.auditsppolicybasicinfo.api.IAuditSpPolicyBasicinfoService;
import com.epoint.auditsppolicybasicinfo.api.entity.AuditSpPolicyBasicinfo;
import com.epoint.auditsprelationtask.api.IAuditSpRelationTaskService;
import com.epoint.auditsprelationtask.api.entity.AuditSpRelationTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.OfficeWebUrlEncryptUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.api.ICodeMainService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.code.entity.CodeMain;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/taItemRest")
public class TaPromiseController
{

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    private IAuditSpItemService iAuditSpItemService;

    @Autowired
    private IAuditSpPolicyBasicinfoService iAuditSpPolicyBasicinfoService;

    @Autowired
    private IAuditSpPhaseBaseinfoService iAuditSpPhaseBaseinfoService;

    @Autowired
    private IAuditSpPhaseGroupService iAuditSpPhaseGroupService;

    @Autowired
    private IAuditSpPhaseTaskService iAuditSpPhaseTaskService;

    @Autowired
    private IAuditSpRelationTaskService iAuditSpRelationTaskService;

    @Autowired
    private IAuditSpParallelTaskService iAuditSpParallelTaskService;

    @Autowired
    private ICodeItemsService iCodeItemsService;

    @Autowired
    private ICodeMainService iCodeMainService;

    @Autowired
    private IConfigService iConfigService;

    @Autowired
    private IAttachService iAttachService;

    @Autowired
    private IOuService iOuService;

    private static final String webUrl = "http://centeros.imwork.net/";

    /**
     * @return String 返回类型
     * @throws
     * @Description: 获取阶段事项接口
     * @author cyc
     * @date 2023年3月17日 下午6:49:35
     */
    @RequestMapping(value = "/getPhaseTaskList", method = RequestMethod.POST)
    public String getPhaseTaskList(@RequestBody
    String param) {
        // JSONObject json = JSON.parseObject(param);
        // JSONObject obj = (JSONObject) json.get("params");
        // 返回结果集
        JSONObject result = new JSONObject();
        try {
            JSONArray phaseListArray = new JSONArray();
            // 查询所有的普通阶段list
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.setOrderAsc("ordernum");
            List<AuditSpPhaseBaseinfo> phaseList = iAuditSpPhaseBaseinfoService
                    .getAuditSpPhaseBaseinfoListByCondition(sql.getMap());
            if (EpointCollectionUtils.isNotEmpty(phaseList)) {
                for (AuditSpPhaseBaseinfo auditSpPhaseBaseinfo : phaseList) {
                    JSONObject phaseObject = new JSONObject();
                    phaseObject.put("id", auditSpPhaseBaseinfo.getRowguid());
                    phaseObject.put("name", auditSpPhaseBaseinfo.getPhasename());
                    phaseObject.put("cols", auditSpPhaseBaseinfo.getCols());
                    sql.clear();
                    sql.eq("phaseguid", auditSpPhaseBaseinfo.getRowguid());
                    List<AuditSpPhaseGroup> sPgroups = iAuditSpPhaseGroupService
                            .getAuditSpPhaseGroupListByCondition(sql.getMap());
                    JSONArray dataArray = new JSONArray();
                    if (EpointCollectionUtils.isNotEmpty(sPgroups)) {
                        for (AuditSpPhaseGroup auditSpPhaseGroup : sPgroups) {
                            JSONObject dataObject = new JSONObject();
                            dataObject.put("id", auditSpPhaseGroup.getRowguid());
                            dataObject.put("order", auditSpPhaseGroup.getBigordernum());
                            dataObject.put("direction", auditSpPhaseGroup.getBigtype());
                            dataObject.put("toonly", auditSpPhaseGroup.getToonly());
                            if ("1".equals(auditSpPhaseGroup.getIsmerge())) {
                                dataObject.put("isMerge", true);
                            }
                            else {
                                dataObject.put("isMerge", false);
                            }
                            JSONArray itemArray = new JSONArray();
                            sql.clear();
                            sql.eq("groupguid", auditSpPhaseGroup.getRowguid());
                            List<AuditSpPhaseTask> phaseTasks = iAuditSpPhaseTaskService
                                    .getAuditSpPhaseTaskListByCondition(sql.getMap());
                            List<AuditSpRelationTask> relationGroups = new ArrayList<>();
                            List<String> afterphasetaskguids = new ArrayList<>();

                            sql.clear();
                            sql.eq("phasegroupguid", auditSpPhaseGroup.getRowguid());
                            relationGroups = iAuditSpRelationTaskService
                                    .getAuditSpRelationTaskListByCondition(sql.getMap());
                            if (EpointCollectionUtils.isNotEmpty(relationGroups)) {
                                afterphasetaskguids = relationGroups.stream().map(a -> a.getGroupguid())
                                        .collect(Collectors.toList());
                            }
                            dataObject.put("children", afterphasetaskguids);// 关联的分组
                            if (EpointCollectionUtils.isNotEmpty(phaseTasks)) {
                                for (AuditSpPhaseTask auditSpPhaseTask : phaseTasks) {
                                    JSONObject taskObject = new JSONObject();
                                    taskObject.put("id", auditSpPhaseTask.getRowguid());
                                    taskObject.put("important", auditSpPhaseTask.getIsimportant());
                                    taskObject.put("dep", auditSpPhaseTask.getOuname());
                                    taskObject.put("day", auditSpPhaseTask.getPromise_day());
                                    taskObject.put("href", auditSpPhaseTask.getGuideurl());
                                    taskObject.put("order", auditSpPhaseTask.getSmallordernum());
                                    taskObject.put("label", auditSpPhaseTask.getTaskname());
                                    taskObject.put("direction", auditSpPhaseTask.getSmalltype());
                                    itemArray.add(taskObject);
                                }
                            }
                            dataObject.put("items", itemArray);
                            dataArray.add(dataObject);
                        }
                    }
                    phaseObject.put("data", dataArray);
                    phaseListArray.add(phaseObject);
                }
            }
            // 查询并行阶段

            JSONObject parallelObject = new JSONObject();
            parallelObject.put("name", "并行审批事项");
            JSONArray parallelTaskArray = new JSONArray();
            sql.clear();
            List<AuditSpParallelTask> parallelTaskList = iAuditSpParallelTaskService
                    .getAuditSpParallelTaskListByCondition(sql.getMap());
            if (EpointCollectionUtils.isNotEmpty(parallelTaskList)) {
                for (AuditSpParallelTask auditSpParallelTask : parallelTaskList) {
                    JSONObject parallelTaskObject = new JSONObject();
                    parallelTaskObject.put("id", auditSpParallelTask.getRowguid());
                    parallelTaskObject.put("important", auditSpParallelTask.getIsimportant());
                    /*
                     * FrameOu ouByOuGuid =
                     * iOuService.getOuByOuGuid(auditSpParallelTask.getOuguid())
                     * ;
                     * if (ouByOuGuid != null) {
                     * parallelTaskObject.put("dep",
                     * ouByOuGuid.getOushortName());
                     * }
                     */
                    parallelTaskObject.put("dep", auditSpParallelTask.getOuname());
                    parallelTaskObject.put("day", auditSpParallelTask.getPromise_day());
                    parallelTaskObject.put("href", auditSpParallelTask.getGuideurl());
                    parallelTaskObject.put("order", auditSpParallelTask.getOrdernum());
                    parallelTaskObject.put("label", auditSpParallelTask.getTaskname());
                    parallelTaskObject.put("stage", auditSpParallelTask.getPhaseguid());
                    parallelTaskArray.add(parallelTaskObject);
                }
            }
            parallelObject.put("data", parallelTaskArray);
            phaseListArray.add(parallelObject);
            result.put("phaselist", phaseListArray);
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取阶段事项接口出现异常: " + e.getMessage(), "");
        }
        return JsonUtils.zwdtRestReturn("1", "获取阶段事项成功", result);
    }

    /**
     * @return String 返回类型
     * @throws
     * @Description: 获取政策配套列表接口
     * @author cyc
     * @date 2023年3月17日 下午6:49:35
     */
    @RequestMapping(value = "/getPolicyList", method = RequestMethod.POST)
    public String getPolicyList(@RequestBody
    String param) {

        // 返回结果集
        JSONObject result = new JSONObject();
        try {
            String select = "";
            JSONObject json = new JSONObject();
            try {
                json = JSON.parseObject(param);
            }
            catch (Exception e) {
                json = new JSONObject();
            }
            if (json.containsKey("params")) {
                JSONObject obj = (JSONObject) json.get("params");
                String rowguid = obj.getString("rowguid");
                AuditSpItem auditSpItemUpdate = iAuditSpItemService.find(rowguid);
                String policyArrstr = JSONObject.parseObject(auditSpItemUpdate.getSelectedpolicyguid())
                        .getString("policyArr");
                if (StringUtil.isNotBlank(policyArrstr)) {
                    JSONArray policyArr = JSONArray.parseArray(policyArrstr);
                    for (int i = 0; i < policyArr.size(); i++) {
                        select += policyArr.get(i) + ";";
                    }
                }
            }
            CodeMain codeMain = iCodeMainService.getCodeMainByName("项目类别");
            List<CodeItems> codeItems = iCodeItemsService.listCodeItemsByCodeID(String.valueOf(codeMain.getCodeID()));
            JSONArray itemArray = new JSONArray();
            if (EpointCollectionUtils.isNotEmpty(codeItems)) {
                for (CodeItems itemTypeItem : codeItems) {
                    JSONObject itemObject = new JSONObject();
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("itemtype", itemTypeItem.getItemValue());
                    List<AuditSpPolicyBasicinfo> spPolicys = iAuditSpPolicyBasicinfoService
                            .getAuditSpPolicyListByCondition(sql.getMap());
                    JSONArray spPolicyArray = new JSONArray();
                    if (EpointCollectionUtils.isNotEmpty(spPolicys)) {
                        for (AuditSpPolicyBasicinfo auditSpPolicyBasicinfo : spPolicys) {
                            if (StringUtil.isNotBlank(select)
                                    && !select.contains(auditSpPolicyBasicinfo.getRowguid())) {
                                continue;
                            }
                            JSONObject policyObject = new JSONObject();
                            policyObject.put("id", auditSpPolicyBasicinfo.getRowguid());
                            policyObject.put("label", auditSpPolicyBasicinfo.getTitle());
                            if (StringUtil.isNotBlank(auditSpPolicyBasicinfo.getClientguid())) {
                                List<FrameAttachInfo> attachInfoListByGuid = iAttachService
                                        .getAttachInfoListByGuid(auditSpPolicyBasicinfo.getClientguid());

                                if (!attachInfoListByGuid.isEmpty()) {
                                    // 综管预览
                                    String baseUrl = "http://59.206.216.99:8080/epoint-web-zwfw"
                                            + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=";
                                    String AS_OfficeWeb365_Server = iConfigService
                                            .getFrameConfigValue("AS_OfficeWeb365_Server");
                                    String url1 = "http://" + AS_OfficeWeb365_Server + "/?"
                                            + OfficeWebUrlEncryptUtil.getEncryptUrl(
                                                    baseUrl + attachInfoListByGuid.get(0).getAttachGuid(),
                                                    attachInfoListByGuid.get(0).getContentType());
                                    policyObject.put("href", url1);

                                }
                            }
                            spPolicyArray.add(policyObject);
                        }
                    }
                    itemObject.put("policylist", spPolicyArray);
                    itemObject.put("id", itemTypeItem.getItemValue());
                    itemObject.put("name", itemTypeItem.getItemText());
                    itemArray.add(itemObject);
                }
            }
            result.put("itemtypelist", itemArray);
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取政策配套列表出现异常: " + e.getMessage(), "");
        }
        return JsonUtils.zwdtRestReturn("1", "获取政策配套列表成功", result);
    }

    /**
     * @return String 返回类型
     * @throws
     * @Description: 保存项目信息接口
     * @author cyc
     * @date 2023年3月17日 下午6:49:35
     */
    @RequestMapping(value = "/initSpTaskI", method = RequestMethod.POST)
    public String initSpTaskI(@RequestBody
    String param) {

        JSONObject json = JSON.parseObject(param);
        JSONObject obj = (JSONObject) json.get("params");
        String rowguid = obj.getString("rowguid");
        String itemcode = obj.getString("itemcode");
        String itemname = obj.getString("itemname");
        String jurisdictions = obj.getString("jurisdictions");
        String projecttype = obj.getString("projecttype");
        String phone = obj.getString("phone");
        String imgData = obj.getString("imgData");
        if (StringUtil.isBlank(itemname)) {
            return JsonUtils.zwdtRestReturn("0", "必填参数itemname必填校验失败", "");
        }
        JSONObject jsonObject = obj.getJSONObject("constructdate");
        String buildunit = obj.getString("buildunit");
        String buildstarttime = jsonObject.getString("startDate");
        String buildendtime = jsonObject.getString("endDate");

        String serveuser = obj.getString("serveuser");
        String buildcontent = obj.getString("buildcontent");

        String itemflowinfo = obj.getString("approvalArr");
        if (StringUtil.isBlank(itemflowinfo)) {
            return JsonUtils.zwdtRestReturn("0", "必填参数itemflowinfo必填校验失败", "");
        }
        String selectedphasetaskid = obj.getString("approvalSelectArr");
        if (StringUtil.isBlank(selectedphasetaskid)) {
            return JsonUtils.zwdtRestReturn("0", "必填参数selectedphasetaskid必填校验失败", "");
        }
        String selectedpolicyguid = obj.getString("policyArr");
        if (StringUtil.isBlank(selectedpolicyguid)) {
            return JsonUtils.zwdtRestReturn("0", "必填参数selectedpolicyguid必填校验失败", "");
        }
        try {

            if (StringUtil.isNotBlank(rowguid)) {
                AuditSpItem auditSpItemUpdate = iAuditSpItemService.find(rowguid);
                auditSpItemUpdate.setItemcode(itemcode);
                auditSpItemUpdate.setItemname(itemname);
                auditSpItemUpdate.setBuildunit(buildunit);
                auditSpItemUpdate.setBuildstarttime(
                        EpointDateUtil.convertString2Date(buildstarttime, EpointDateUtil.DATE_FORMAT));
                auditSpItemUpdate
                        .setBuildendtime(EpointDateUtil.convertString2Date(buildendtime, EpointDateUtil.DATE_FORMAT));
                auditSpItemUpdate.setServeuser(serveuser);
                auditSpItemUpdate.setBuildcontent(buildcontent);
                auditSpItemUpdate.setItemflowinfo(itemflowinfo);
                auditSpItemUpdate.setSelectedphasetaskid(selectedphasetaskid);
                auditSpItemUpdate.setSelectedpolicyguid(selectedpolicyguid);
                auditSpItemUpdate.setOperatedate(new Date());
                auditSpItemUpdate.set("jurisdictions", jurisdictions);
                auditSpItemUpdate.set("projecttype", projecttype);
                auditSpItemUpdate.set("phone", phone);
                auditSpItemUpdate.set("imgData", imgData);
                if (StringUtil.isNotBlank(itemcode)) {
                    String biguid = iAuditSpItemService.getlctguidByitem(itemcode);
                    if (StringUtil.isNotBlank(biguid)) {
                        auditSpItemUpdate.set("biguid", biguid);
                    }
                }
                iAuditSpItemService.update(auditSpItemUpdate);
            }
            else {
                AuditSpItem spItem = new AuditSpItem();
                spItem.setRowguid(StringUtil.isNotBlank(rowguid) ? rowguid : UUID.randomUUID().toString());
                spItem.setItemcode(itemcode);
                spItem.setItemname(itemname);
                spItem.setBuildunit(buildunit);
                if (buildstarttime != null) {
                    spItem.setBuildstarttime(
                            EpointDateUtil.convertString2Date(buildstarttime, EpointDateUtil.DATE_FORMAT));
                }
                if (buildendtime != null) {
                    spItem.setBuildendtime(EpointDateUtil.convertString2Date(buildendtime, EpointDateUtil.DATE_FORMAT));
                }

                spItem.setServeuser(serveuser);
                spItem.setBuildcontent(buildcontent);
                spItem.setItemflowinfo(itemflowinfo);
                spItem.setSelectedphasetaskid(selectedphasetaskid);
                spItem.setSelectedpolicyguid(selectedpolicyguid);
                spItem.setOperatedate(new Date());
                spItem.set("jurisdictions", jurisdictions);
                spItem.set("projecttype", projecttype);
                if (StringUtil.isNotBlank(itemcode)) {
                    String biguid = iAuditSpItemService.getlctguidByitem(itemcode);
                    if (StringUtil.isNotBlank(biguid)) {
                        spItem.set("biguid", biguid);
                    }
                }
                spItem.set("phone", phone);
                spItem.set("imgData", imgData);
                iAuditSpItemService.insert(spItem);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "保存项目信息出现异常: " + e.getMessage(), "");
        }
        return JsonUtils.zwdtRestReturn("1", "保存项目信息成功", "");
    }

    /**
     * @return String 返回类型
     * @throws
     * @Description: 保存项目信息接口
     * @author cyc
     * @date 2023年3月17日 下午6:49:35
     */
    @RequestMapping(value = "/saveitem", method = RequestMethod.POST)
    public String saveitem(@RequestBody
    String param) {
        log.info("==============调用saveitemstart==================");
        JSONObject json = JSON.parseObject(param);
        JSONObject obj = (JSONObject) json.get("params");
        String rowguid = obj.getString("rowguid");
        String rowguid2 = obj.getString("rowguid2");
        String itemcode = obj.getString("itemcode");
        String itemname = obj.getString("itemname");
        String jurisdictions = obj.getString("jurisdictions");
        String totalmoney = obj.getString("totalmoney");
        String itemlevel = obj.getString("itemlevel");
        String bbryname = obj.getString("bbryname");
        String bbryphone = obj.getString("bbryphone");
        String bbrydw = obj.getString("bbrydw");
        String itemyear = obj.getString("itemyear");
        String itemyiju = obj.getString("itemyiju");

        String shibbryname = obj.getString("shibbryname");
        String shibbryphone = obj.getString("shibbryphone");
        String shibbrydw = obj.getString("shibbrydw");
        String shiitemyear = obj.getString("shiitemyear");
        String shiitemyiju = obj.getString("shiitemyiju");

        String xianbbryname = obj.getString("xianbbryname");
        String xianbbryphone = obj.getString("xianbbryphone");
        String xianbbrydw = obj.getString("xianbbrydw");
        String xianitemyear = obj.getString("xianitemyear");
        String xianitemyiju = obj.getString("xianitemyiju");

        String itemtype = obj.getString("itemtype");
        String itemimageprogress = obj.getString("itemimageprogress");

        JSONObject jsonObject = obj.getJSONObject("constructdate");
        String buildunit = obj.getString("buildunit");
        String buildstarttime = jsonObject.getString("startDate");
        String buildendtime = jsonObject.getString("endDate");
        if (StringUtil.isBlank(itemname)) {
            return JsonUtils.zwdtRestReturn("0", "项目名称必填！", "");
        }
        try {
            if (StringUtil.isNotBlank(rowguid)) {
                AuditSpItem auditSpItemUpdate = iAuditSpItemService.find(rowguid);
                if (auditSpItemUpdate != null) {
                    auditSpItemUpdate.setItemcode(itemcode);
                    auditSpItemUpdate.setItemname(itemname);
                    auditSpItemUpdate.setOperatedate(new Date());
                    auditSpItemUpdate.set("jurisdictions", jurisdictions);
                    auditSpItemUpdate.setBuildunit(buildunit);
                    if (StringUtil.isNotBlank(buildstarttime)) {
                        auditSpItemUpdate.setBuildstarttime(
                                EpointDateUtil.convertString2Date(buildstarttime, EpointDateUtil.DATE_FORMAT));
                    }
                    if (StringUtil.isNotBlank(buildendtime)) {
                        auditSpItemUpdate.setBuildendtime(
                                EpointDateUtil.convertString2Date(buildendtime, EpointDateUtil.DATE_FORMAT));
                    }

                    auditSpItemUpdate.set("totalmoney", totalmoney);
                    auditSpItemUpdate.set("itemlevel", itemlevel);
                    auditSpItemUpdate.set("bbryname", bbryname);
                    auditSpItemUpdate.set("bbryphone", bbryphone);
                    auditSpItemUpdate.set("bbrydw", bbrydw);
                    auditSpItemUpdate.set("itemyear", itemyear);
                    auditSpItemUpdate.set("itemyiju", itemyiju);

                    auditSpItemUpdate.set("shibbryname", shibbryname);
                    auditSpItemUpdate.set("shibbryphone", shibbryphone);
                    auditSpItemUpdate.set("shibbrydw", shibbrydw);
                    auditSpItemUpdate.set("shiitemyear", shiitemyear);
                    auditSpItemUpdate.set("shiitemyiju", shiitemyiju);

                    auditSpItemUpdate.set("xianbbryname", xianbbryname);
                    auditSpItemUpdate.set("xianbbryphone", xianbbryphone);
                    auditSpItemUpdate.set("xianbbrydw", xianbbrydw);
                    auditSpItemUpdate.set("xianitemyear", xianitemyear);
                    auditSpItemUpdate.set("xianitemyiju", xianitemyiju);
                    auditSpItemUpdate.set("itemtype", itemtype);
                    auditSpItemUpdate.set("itemimageprogress", itemimageprogress);
                    auditSpItemUpdate.set("sfzditem", "1");
                    auditSpItemUpdate.set("issync", null);

                    if (StringUtil.isNotBlank(itemcode)) {
                        String biguid = iAuditSpItemService.getlctguidByitem(itemcode);
                        if (StringUtil.isNotBlank(biguid)) {
                            auditSpItemUpdate.set("biguid", biguid);
                        }
                    }
                    iAuditSpItemService.update(auditSpItemUpdate);
                    return JsonUtils.zwdtRestReturn("1", "修改项目信息成功", "");
                }
                else {
                    AuditSpItem spItem = new AuditSpItem();
                    spItem.setRowguid(rowguid);
                    spItem.setItemcode(itemcode);
                    spItem.setItemname(itemname);
                    spItem.setOperatedate(new Date());
                    spItem.set("jurisdictions", jurisdictions);
                    spItem.setBuildunit(buildunit);
                    if (buildstarttime != null) {
                        spItem.setBuildstarttime(
                                EpointDateUtil.convertString2Date(buildstarttime, EpointDateUtil.DATE_FORMAT));
                    }
                    if (buildendtime != null) {
                        spItem.setBuildendtime(
                                EpointDateUtil.convertString2Date(buildendtime, EpointDateUtil.DATE_FORMAT));
                    }

                    if (StringUtil.isNotBlank(itemcode)) {
                        String biguid = iAuditSpItemService.getlctguidByitem(itemcode);
                        if (StringUtil.isNotBlank(biguid)) {
                            spItem.set("biguid", biguid);
                        }
                    }
                    spItem.set("totalmoney", totalmoney);
                    spItem.set("itemlevel", itemlevel);
                    spItem.set("bbryname", bbryname);
                    spItem.set("bbryphone", bbryphone);
                    spItem.set("bbrydw", bbrydw);
                    spItem.set("itemyear", itemyear);
                    spItem.set("itemyiju", itemyiju);

                    spItem.set("shibbryname", shibbryname);
                    spItem.set("shibbryphone", shibbryphone);
                    spItem.set("shibbrydw", shibbrydw);
                    spItem.set("shiitemyear", shiitemyear);
                    spItem.set("shiitemyiju", shiitemyiju);

                    spItem.set("xianbbryname", xianbbryname);
                    spItem.set("xianbbryphone", xianbbryphone);
                    spItem.set("xianbbrydw", xianbbrydw);
                    spItem.set("xianitemyear", xianitemyear);
                    spItem.set("xianitemyiju", xianitemyiju);
                    spItem.set("itemtype", itemtype);
                    spItem.set("itemimageprogress", itemimageprogress);
                    spItem.set("sfzditem", "1");
                    spItem.set("issync", null);
                    iAuditSpItemService.insert(spItem);
                    return JsonUtils.zwdtRestReturn("1", "保存项目信息成功", "");
                }

            }

        }
        catch (Exception e) {
            e.printStackTrace();
            // 异常时，保存信息
            log.info("=======saveitem接口参数：params【" + param + "】=======");
            log.info("=======saveitem异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "保存项目信息出现异常: " + e.getMessage(), "");
        }
        return JsonUtils.zwdtRestReturn("0", "保存项目信息出现异常", "");
    }

    /**
     * @return String 返回类型
     * @throws
     * @Description: 查询项目信息接口
     * @author cyc
     * @date 2023年3月17日 下午6:49:35
     */
    @RequestMapping(value = "/querySpTaskI", method = RequestMethod.POST)
    public String querySpTaskI(@RequestBody
    String param) {
        JSONObject json = JSON.parseObject(param);
        JSONObject obj = (JSONObject) json.get("params");
        String rowguid = obj.getString("rowguid");
        String itemcode = obj.getString("itemcode");
        SqlConditionUtil sql = new SqlConditionUtil();
        if (StringUtil.isBlank(itemcode)) {
            if (StringUtil.isBlank(rowguid)) {
                return JsonUtils.zwdtRestReturn("0", "必填参数rowguid必填校验失败", "");
            }
            sql.eq("rowguid", rowguid);
        }
        else {
            sql.like("itemcode", itemcode);
        }

        // 返回结果集
        JSONObject result = new JSONObject();
        try {
            List<AuditSpItem> items = iAuditSpItemService.getAuditSpItemListByCondition(sql.getMap());
            JSONObject itemObject = new JSONObject();
            if (EpointCollectionUtils.isNotEmpty(items)) {
                AuditSpItem spItem = items.get(0);
                itemObject.put("rowguid", spItem.getRowguid());
                itemObject.put("itemcode", spItem.getItemcode());
                itemObject.put("itemname", spItem.getItemname());
                itemObject.put("buildunit", spItem.getBuildunit());
                itemObject.put("jurisdictions", spItem.get("jurisdictions"));
                itemObject.put("jurisdictionstext",
                        iCodeItemsService.getItemTextByCodeName("阶段配置辖区", spItem.get("jurisdictions")));
                itemObject.put("projecttype", spItem.get("projecttype"));
                itemObject.put("phone", spItem.get("phone"));
                itemObject.put("imgdata", spItem.get("imgData"));
                itemObject.put("buildstarttime",
                        EpointDateUtil.convertDate2String(spItem.getBuildstarttime(), EpointDateUtil.DATE_FORMAT));
                itemObject.put("buildendtime",
                        EpointDateUtil.convertDate2String(spItem.getBuildendtime(), EpointDateUtil.DATE_FORMAT));
                itemObject.put("serveuser", spItem.getServeuser());
                itemObject.put("buildcontent", spItem.getBuildcontent());
                itemObject.put("approvalArr", JSONUtils.parse(spItem.getItemflowinfo()));
                itemObject.put("approvalSelectArr", JSONUtils.parse(spItem.getSelectedphasetaskid()));
                if (StringUtil.isNotBlank(spItem.getSelectedpolicyguid())) {
                    itemObject.put("policyArr", JSONUtils.parse(spItem.getSelectedpolicyguid()));
                }

                String code = iConfigService.getFrameConfigValue("qrcode") + spItem.getRowguid();
                itemObject.put("code", code);

            }
            result.put("iteminfo", itemObject);
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "查询项目信息出现异常: " + e.getMessage(), "");
        }
        return JsonUtils.zwdtRestReturn("1", "查询项目信息成功", result);
    }

    /**
     * @return String 返回类型
     * @throws
     * @Description: 查询项目信息接口
     * @author cyc
     * @date 2023年3月17日 下午6:49:35
     */
    @RequestMapping(value = "/queryitem", method = RequestMethod.POST)
    public String queryitem(@RequestBody
    String param) {
        JSONObject json = JSON.parseObject(param);
        JSONObject obj = (JSONObject) json.get("params");
        String rowguid = obj.getString("rowguid");
        String itemcode = obj.getString("itemcode");
        SqlConditionUtil sql = new SqlConditionUtil();
        if (StringUtil.isBlank(itemcode)) {
            if (StringUtil.isBlank(rowguid)) {
                return JsonUtils.zwdtRestReturn("0", "必填参数rowguid必填校验失败", "");
            }
            sql.eq("rowguid", rowguid);
        }
        else {
            sql.like("itemcode", itemcode);
        }

        // 返回结果集
        JSONObject result = new JSONObject();
        try {
            List<AuditSpItem> items = iAuditSpItemService.getAuditSpItemListByCondition(sql.getMap());
            JSONObject itemObject = new JSONObject();
            if (EpointCollectionUtils.isNotEmpty(items)) {
                AuditSpItem spItem = items.get(0);
                itemObject.put("rowguid", spItem.getRowguid());
                itemObject.put("itemcode", spItem.getItemcode());
                itemObject.put("itemname", spItem.getItemname());
                itemObject.put("buildunit", spItem.getBuildunit());
                itemObject.put("jurisdictions", spItem.get("jurisdictions"));
                itemObject.put("jurisdictionstext",
                        iCodeItemsService.getItemTextByCodeName("阶段配置辖区", spItem.get("jurisdictions")));
                itemObject.put("projecttype", spItem.get("projecttype"));
                itemObject.put("phone", spItem.get("phone"));
                itemObject.put("imgdata", spItem.get("imgData"));
                itemObject.put("buildstarttime",
                        EpointDateUtil.convertDate2String(spItem.getBuildstarttime(), EpointDateUtil.DATE_FORMAT));
                itemObject.put("buildendtime",
                        EpointDateUtil.convertDate2String(spItem.getBuildendtime(), EpointDateUtil.DATE_FORMAT));
                itemObject.put("serveuser", spItem.getServeuser());
                itemObject.put("buildcontent", spItem.getBuildcontent());
                String code = iConfigService.getFrameConfigValue("qrcode") + spItem.getRowguid();

                itemObject.put("code", code);
                if ("1".equals(spItem.getStr("sfzditem"))) {
                    itemObject.put("totalmoney", spItem.getStr("totalmoney"));
                    itemObject.put("itemlevel", spItem.getStr("itemlevel"));
                    itemObject.put("bbryname", spItem.getStr("bbryname"));
                    itemObject.put("bbryphone", spItem.getStr("bbryphone"));
                    itemObject.put("bbrydw", spItem.getStr("bbrydw"));
                    itemObject.put("itemyear", spItem.getStr("itemyear"));
                    itemObject.put("itemyiju", spItem.getStr("itemyiju"));
                    itemObject.put("itemtype", spItem.getStr("itemtype"));
                    itemObject.put("itemimageprogress", spItem.getStr("itemimageprogress"));
                }

            }
            result.put("iteminfo", itemObject);
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "查询项目信息出现异常: " + e.getMessage(), "");
        }
        return JsonUtils.zwdtRestReturn("1", "查询项目信息成功", result);
    }

    /**
     * @return String 返回类型
     * @throws
     * @Description: 列表项目类型分类数据量
     * @author lzm
     * @date 2023年3月17日 下午6:49:35
     */
    @RequestMapping(value = "/getGroupProjecttypecount", method = RequestMethod.POST)
    public String getGroupProjecttypecount(@RequestBody
    String params) {
        try {
            log.info("=======开始调用getGroupProjecttypecount接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject result = new JSONObject();
            // 1、接口的入参转化为JSON对象
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String jurisdictions = obj.getString("jurisdictions"); // 辖区编码
            String projecttype = obj.getString("projecttype"); // 项目分类
            // 根据项目类型分类获取相应数据量
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.like("jurisdictions", jurisdictions);
            sql.like("projecttype", projecttype);
            List<AuditSpItem> list = iAuditSpItemService.getAuditSpItemListByCondition(sql.getMap());
            JSONObject data = new JSONObject();
            int province = 0;
            int city = 0;
            int green = 0;
            if (!list.isEmpty()) {
                for (AuditSpItem auditSpItem : list) {
                    if (auditSpItem.getStr("projecttype").contains("0")) {
                        province++;
                    }
                    if (auditSpItem.getStr("projecttype").contains("1")) {
                        city++;
                    }
                    if (auditSpItem.getStr("projecttype").contains("2")) {
                        green++;
                    }
                }
            }
            data.put("province", province);
            data.put("city", city);
            data.put("green", green);
            result.put("data", data);
            log.info("=======结束调用getGroupProjecttypecount接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取成功！", result.toJSONString());
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取失败,原因: " + e.getMessage(), "");
        }
    }

    /**
     * @return String 返回类型
     * @throws
     * @Description: 列表项目类型分类数据量
     * @author lzm
     * @date 2023年3月17日 下午6:49:35
     */
    @RequestMapping(value = "/getzditemcount", method = RequestMethod.POST)
    public String getzditemcount(@RequestBody
    String params) {
        try {
            log.info("=======开始调用getzditemcount接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject result = new JSONObject();
            // 1、接口的入参转化为JSON对象
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String jurisdictions = obj.getString("jurisdictions"); // 辖区编码
            String projecttype = obj.getString("projecttype"); // 项目分类
            // 根据项目类型分类获取相应数据量
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("sfzditem", "1");
            List<AuditSpItem> list = iAuditSpItemService.getAuditSpItemListByCondition(sql.getMap());
            JSONObject data = new JSONObject();
            int province = 0;
            int city = 0;
            int green = 0;
            if (!list.isEmpty()) {
                for (AuditSpItem auditSpItem : list) {
                    if (auditSpItem.getStr("itemlevel").contains("00101")) {
                        province++;
                    }
                    if (auditSpItem.getStr("itemlevel").contains("00102")) {
                        city++;
                    }
                    if (auditSpItem.getStr("itemlevel").contains("00103")) {
                        green++;
                    }
                }
            }
            data.put("province", province);
            data.put("city", city);
            data.put("green", green);
            result.put("data", data);
            log.info("=======结束调用getGroupProjecttypecount接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取成功！", result.toJSONString());
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取失败,原因: " + e.getMessage(), "");
        }
    }

}
