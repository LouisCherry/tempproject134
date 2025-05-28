package com.epoint.audittask.material.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.element.domain.AuditTaskElement;
import com.epoint.basic.audittask.element.inter.IAuditTaskElementService;
import com.epoint.basic.audittask.material.domain.AuditTaskCase;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.audittask.option.domain.AuditTaskOption;
import com.epoint.basic.audittask.option.inter.IAuditTaskOptionService;
import com.epoint.basic.audittask.selectedoption.domain.AuditTaskSelectedOption;
import com.epoint.basic.audittask.selectedoption.inter.IAuditTaskSelectedOptionService;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.util.ZwfwRedisCacheUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.security.crypto.MDUtils;
import com.epoint.core.utils.string.StringUtil;

/**
 * 事项的情形list页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2016-10-14 16:13:58]
 */
@RestController("audittaskanswercaseaction")
@Scope("request")
public class AuditTaskAnswerCaseAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 日志
     */

    @Autowired
    private IAuditTask audittaskImpl;

    @Autowired
    private IAuditTaskCase auditTaskCaseImpl;
    @Autowired
    private IAuditTaskMaterialCase auditTaskMaterialCaseimpl;

    @Autowired
    private IAuditTaskMaterial auditTaskMaterialimpl;

    @Autowired
    private IAuditTaskSelectedOptionService iAuditTaskSelectedOptionService;

    /**
     * 事项下放相关信息
     */
    @Autowired
    private IAuditTaskOptionService iAuditTaskOptionService;

    /**
     * 事项下放相关信息
     */
    @Autowired
    private IAuditTaskElementService iAuditTaskElementService;

    @Override
    public void pageLoad() {
    }

    public String checkIsExistElement() {
        String taskguid = getRequestParameter("taskGuid");
        AuditTask audit = audittaskImpl.selectTaskByRowGuid(taskguid).getResult();
        String taskId = audit.getTask_id();
        try {
            // 1、接口的入参转化为JSON对象
            // 1.1、获取事项唯一标识
            // 2、根据taskId判断事项要素是否满足条件（选项配有材料或者后置要素的要素选项配有材料）
            boolean isExist = iAuditTaskElementService.checkELementMaterial(taskId).getResult();
            // 2.1、 根据isExist的值，判定是否需要进行场景选择
            return (isExist) ? ZwfwConstant.CONSTANT_STR_ONE : ZwfwConstant.CONSTANT_STR_ZERO;
        }
        catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    public void getTaskElementByOption() {
        String taskguid = getRequestParameter("taskGuid");
        String optionGuid = getRequestParameter("optionguid");
        String taskedit = getRequestParameter("taskedit");
        String type = getRequestParameter("type");
        AuditTask audit = audittaskImpl.selectTaskByRowGuid(taskguid).getResult();
        String taskId = audit.getTask_id();
        SqlConditionUtil sql = new SqlConditionUtil();
        JSONObject dataJson = new JSONObject();
        // 定义存储要素信息的List
        List<JSONObject> subElementJsonList = new ArrayList<JSONObject>();
        if (("checkbox").equals(type)) {
            if (StringUtil.isBlank(optionGuid)) {
                sql.eq("preoptionguid", "");
            }
            else {
                String guids[] = optionGuid.split(";");
                String options = null;
                List<String> list = new ArrayList<>();
                if (guids != null && guids.length > 0) {
                    for (int i = 0; i < guids.length; i++) {
                        list.add(guids[i]);
                    }
                    options = "'" + StringUtil.join(list, "','") + "'";
                    sql.in("preoptionguid", options);
                }
            }
        }
        else {
            if (StringUtil.isNotBlank(optionGuid)) {
                sql.eq("preoptionguid", optionGuid);
            }
        }
        if (StringUtil.isNotBlank(taskedit) && ZwfwConstant.CONSTANT_STR_ONE.equals(taskedit)) {
            sql.eq("draft", "1");
        }
        else {
            sql.isBlankOrValue("draft", "0");
        }
        if (StringUtil.isNotBlank(taskId)) {
            sql.eq("taskId", taskId);
        }
        sql.setOrder("ordernum desc,operatedate", "asc");
        try {
            // 1、接口的入参转化为JSON对象
            // 1.1、获取要素选项唯一标识
            // 1.2、获取事项唯一标识
            // 2、获取事项的要素列表
            List<AuditTaskElement> auditTaskElements = iAuditTaskElementService.findListByCondition(sql.getMap())
                    .getResult();
            if (auditTaskElements != null && !auditTaskElements.isEmpty()) {
                for (AuditTaskElement auditTaskElement : auditTaskElements) {
                    // 定义存储要素信息的Json
                    JSONObject subElementJson = new JSONObject();
                    // 2.1、查询要素选项信息
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
                            optionJsonList.add(optionJson);
                        }
                        subElementJson.put("optionlist", optionJsonList); // 要素选项列表
                        subElementJson.put("elementquestion", auditTaskElement.getElementquestion()); // 要素问题
                        subElementJson.put("elementname", auditTaskElement.getElementname()); // 要素问题
                        subElementJson.put("elementguid", auditTaskElement.getRowguid()); // 要素唯一标识
                        subElementJson.put("preoptionguid", auditTaskElement.getPreoptionguid()); // 要素唯一标识
                        if (StringUtil.isNotBlank(auditTaskElement.getMultiselect())
                                && ZwfwConstant.CONSTANT_STR_ZERO.equals(auditTaskElement.getMultiselect())) {
                            subElementJson.put("type", "radio");
                            subElementJson.put("multitype", "单选");
                        }
                        else {
                            subElementJson.put("type", "checkbox");
                            subElementJson.put("multitype", "多选");
                        }
                        subElementJson.put("multiselect", auditTaskElement.getMultiselect());
                        subElementJsonList.add(subElementJson);
                    }
                }
            }
            dataJson.put("subelementlist", subElementJsonList);// 要素及要素选项列表
            this.addCallbackParam("data", dataJson);
        }
        catch (Exception e) {
            e.printStackTrace();
            this.addCallbackParam("msg", "事项后置要素获取失败：");
        }
    }

    /**
     * 获取事项下要素及要素选项
     * 
     * @param params
     *            接口的入参
     * @return
     */
    public void getTaskElement() {
        try {
            String taskguid = getRequestParameter("taskGuid");
            String taskedit = getRequestParameter("taskedit");
            SqlConditionUtil sql = new SqlConditionUtil();
            AuditTask audit = audittaskImpl.selectTaskByRowGuid(taskguid).getResult();
            String taskId = audit.getTask_id();

            if (StringUtil.isNotBlank(taskedit) && ZwfwConstant.CONSTANT_STR_ONE.equals(taskedit)) {
                sql.eq("draft", "1");
            }
            else {
                sql.isBlankOrValue("draft", "0");
            }
            sql.rightLike("preoptionguid", "start");
            if (StringUtil.isNotBlank(taskId)) {
                sql.eq("taskId", taskId);
            }
            sql.setOrder("ordernum desc,operatedate", "asc");
            // 1、接口的入参转化为JSON对象
            // 1.1、获取事项唯一标识
            // 2、获取事项的情景要素列表 (没有前置要素的)
            List<AuditTaskElement> auditTaskElements = iAuditTaskElementService.findListByCondition(sql.getMap())
                    .getResult();
            // 定义存储要素信息的List
            List<JSONObject> elementJsonList = new ArrayList<JSONObject>();
            if (auditTaskElements != null && !auditTaskElements.isEmpty()) {
                for (AuditTaskElement auditTaskElement : auditTaskElements) {
                    // 定义存储要素信息的json
                    JSONObject elementJson = new JSONObject();
                    // 2.1 根据事项要素唯一标识查询事项要素选项
                    List<AuditTaskOption> auditTaskOptions = iAuditTaskOptionService
                            .findListByElementIdAndTaskid(auditTaskElement.getRowguid()).getResult();
                    if (auditTaskOptions != null && auditTaskOptions.size() > 1) {
                        // 定义存储要素选项信息的List
                        List<JSONObject> optionJsonList = new ArrayList<JSONObject>();
                        for (AuditTaskOption auditTaskOption : auditTaskOptions) {
                            // 定义存储要素选项信息的json
                            JSONObject optionJson = new JSONObject();
                            optionJson.put("optionname", auditTaskOption.getOptionname());
                            optionJson.put("optionguid", auditTaskOption.getRowguid());
                            optionJsonList.add(optionJson);
                        }
                        elementJson.put("optionlist", optionJsonList); // 要素选项列表
                        elementJson.put("elementname", auditTaskElement.getElementname()); // 要素问题
                        elementJson.put("elementquestion", auditTaskElement.getElementquestion()); // 要素问题
                        elementJson.put("elementguid", auditTaskElement.getRowguid()); // 要素唯一标识
                        if (StringUtil.isNotBlank(auditTaskElement.getMultiselect())
                                && ZwfwConstant.CONSTANT_STR_ZERO.equals(auditTaskElement.getMultiselect())) {
                            elementJson.put("type", "radio");
                            elementJson.put("multitype", "单选");
                        }
                        else {
                            elementJson.put("type", "checkbox");
                            elementJson.put("multitype", "多选");
                        }
                        elementJson.put("multiselect", auditTaskElement.getMultiselect());
                        elementJsonList.add(elementJson);
                    }
                }
            }
            // 定义返回的JSON
            JSONObject dataJson = new JSONObject();
            dataJson.put("elementlist", elementJsonList);// 要素及要素选项列表
            this.addCallbackParam("data", dataJson);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskElement异常信息：" + e.getMessage() + "=======");
            this.addCallbackParam("msg", "事项要素获取失败：");
        }
    }

    @SuppressWarnings("unchecked")
    public void getCaseGuidBySelectedOptions() {
        try {
            String selectedoptions = getRequestParameter("selectedoptions");
            String projectguid = getRequestParameter("projectguid");
            String taskguid = getRequestParameter("taskguid");
            List<String> optionlist = new ArrayList<>();
            AuditTask audit = audittaskImpl.selectTaskByRowGuid(taskguid).getResult();
            String taskId = audit.getTask_id();
            // 1、接口的入参转化为JSON对象
            // 1.1、获取用户选择信息
            String faqCaseGuid = "";
            String opguids = null;
            // 将选择得数据添加到selected option表中去
            if (StringUtil.isNotBlank(projectguid)) {
                AuditTaskSelectedOption auditTaskSelectedOption = iAuditTaskSelectedOptionService
                        .findByProjectGuid(projectguid).getResult();
                if (auditTaskSelectedOption == null) {
                    AuditTaskSelectedOption SelectedOption = new AuditTaskSelectedOption();
                    SelectedOption.setInsertdate(new Date());
                    SelectedOption.setRowguid(UUID.randomUUID().toString());
                    SelectedOption.setProjectguid(projectguid);
                    SelectedOption.setSelectedoptions(selectedoptions);
                    iAuditTaskSelectedOptionService.insert(SelectedOption);
                }
                else {
                    auditTaskSelectedOption.setInsertdate(new Date());
                    auditTaskSelectedOption.setSelectedoptions(selectedoptions);
                    iAuditTaskSelectedOptionService.update(auditTaskSelectedOption);
                }
            }
            // 按照传过来的数据进行解析，取出optionguidlist
            if (StringUtil.isNotBlank(selectedoptions)) {
                JSONObject jsonObject = JSONObject.parseObject(selectedoptions);
                List<Map<String, Object>> ja = (List<Map<String, Object>>) jsonObject.get("selectedoptions");
                if(ja.size() != 0) {
                    for (int s = 0; s < ja.size(); s++) {
                        List<Map<String, Object>> maplist = (List<Map<String, Object>>) ja.get(s).get("optionlist");
                        for (int i = 0; i < maplist.size(); i++) {
                            optionlist.add(maplist.get(i).get("optionguid").toString());
                            if (StringUtil.isBlank(opguids)) {
                                opguids = maplist.get(i).get("optionguid") + ";";
                            }
                            else {
                                opguids += maplist.get(i).get("optionguid") + ";";
                            }
                        }
                    }
                    String rtncaseguid = auditTaskCaseImpl
                            .findCaseByOptionguidList(0, taskId, taskguid, opguids, optionlist).getResult();
                    JSONObject dataJson = new JSONObject();
                    if (StringUtil.isNotBlank(rtncaseguid) && !ZwfwConstant.CONSTANT_STR_ZERO.equals(rtncaseguid)) {
                        // 若不为空，则说明已存在情形与其相似，返回此情形
                        // new 代表需要进行更新与新增
                        faqCaseGuid = rtncaseguid;
                        AuditTaskCase auditTaskCase = auditTaskCaseImpl.getAuditTaskCaseByRowguid(rtncaseguid).getResult();
                        if (auditTaskCase.getIs_oldcase() == 0) {
                            dataJson.put("caozuo", "new");
                            dataJson.put("faqcaseguid", faqCaseGuid);
                        }
                        else {
                            this.addCallbackParam("msg", "根据所选场景比对到常用情形:" + auditTaskCase.getCasename());
                            dataJson.put("caozuo", "success");
                            dataJson.put("casename", auditTaskCase.getCasename());
                            dataJson.put("faqcaseguid", faqCaseGuid); // 是否配置要素或要素下是否配置材料并标识
                        }
                        this.addCallbackParam("data", dataJson);
                    }
                    // 未查询到常用情形，直接添加，然后返回caseguid
                    else if (ZwfwConstant.CONSTANT_STR_ZERO.equals(rtncaseguid)) {
                        String caseguid = saveNewCase(selectedoptions, taskguid, projectguid, "", "", "0");
    
                        faqCaseGuid = ZwfwConstant.CONSTANT_STR_ZERO;
                        // 若为空则需添加新情形，返回caseguid
                        dataJson.put("caozuo", "new");
                        dataJson.put("faqcaseguid", caseguid); // 是否配置要素或要素下是否配置材料并标识
                        this.addCallbackParam("data", dataJson);
                    }
                    else {
                        this.addCallbackParam("msg", "匹配情形失败！");
                        faqCaseGuid = ZwfwConstant.CONSTANT_STR_ZERO;
                        dataJson.put("caozuo", "default");
                        dataJson.put("faqcaseguid", faqCaseGuid); // 是否配置要素或要素下是否配置材料并标识
                        this.addCallbackParam("data", dataJson);
                    }
                }
                else {
                    this.addCallbackParam("msg", "情形未配置，请重新配置情形！");
                }
            }
            else {
                this.addCallbackParam("msg", "初始化材料出错！");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCaseGuidBySelectedOptions异常信息：" + e.getMessage() + "=======");
        }
    }

    /**
     * 根据guids添加新的情形
     * 
     * @param guids
     * @return
     */
    // 0 为就情形 1为多情形
    @SuppressWarnings("unchecked")
    public String saveNewCase(String selectedoptions, String taskGuid, String projectguid, String casename,
            String casedetail, String is_oldcase) {

        // selectedoptions = getRequestParameter("selectedoptions");
        // taskGuid = getRequestParameter("taskguid");
        AuditTask audit = audittaskImpl.selectTaskByRowGuid(taskGuid).getResult();
        String taskId = audit.getTask_id();
        // casename = getRequestParameter("casename");
        // casedetail = getRequestParameter("casedetail");
        // is_oldcase = getRequestParameter("is_oldcase");

        String caseguid = "";
        String opguids = null;
        List<String> optionlist = new ArrayList<>();
        // 按照传过来的数据进行解析，取出optionguidlist
        if (StringUtil.isNotBlank(selectedoptions)) {
            JSONObject jsonObject = JSONObject.parseObject(selectedoptions);
            List<Map<String, Object>> ja = (List<Map<String, Object>>) jsonObject.get("selectedoptions");
            for (int s = 0; s < ja.size(); s++) {
                List<Map<String, Object>> maplist = (List<Map<String, Object>>) ja.get(s).get("optionlist");
                for (int i = 0; i < maplist.size(); i++) {
                    // 判断是否为最底层选项
                    // 是则添加到optionlist中去
                    optionlist.add(maplist.get(i).get("optionguid").toString());
                    if (StringUtil.isBlank(opguids)) {
                        opguids = maplist.get(i).get("optionguid") + ";";
                    }
                    else {
                        opguids += maplist.get(i).get("optionguid") + ";";
                    }
                }
            }
        }
        List<String> rtnguids = new ArrayList<>();
        if (!optionlist.isEmpty()) {
            String allmaterials = null;
            // 根据传过来的guids列表进行遍历，讲选项guid保存在json中
            for (String guid : optionlist) {
                if (StringUtil.isNotBlank(guid)) {
                    AuditTaskOption auditTaskOption = iAuditTaskOptionService.find(guid).getResult();
                    if (auditTaskOption != null && StringUtil.isNotBlank(auditTaskOption.getMaterialids())) {
                        if (StringUtil.isBlank(allmaterials)) {
                            allmaterials = auditTaskOption.getMaterialids();
                        }
                        else {
                            allmaterials += auditTaskOption.getMaterialids();
                        }
                    }
                }
            }
            if (StringUtil.isNotBlank(allmaterials)) {
                String[] materialid = allmaterials.split(";");
                if (materialid.length > 0) {
                    for (int i = 0; i < materialid.length; i++) {
                        if (!rtnguids.contains(materialid[i])) {
                            rtnguids.add(materialid[i]);
                        }
                    }
                }
            }
        }
        String rowguid = UUID.randomUUID().toString();
        if (auditTaskCaseImpl.CheckCaseName(taskGuid, rowguid, casename).getResult()) {
            caseguid = UUID.randomUUID().toString();
            AuditTaskCase auditTaskCase = new AuditTaskCase();
            auditTaskCase.setOperatedate(new Date());
            auditTaskCase.setRowguid(caseguid);
            auditTaskCase.setCasename(casename);
            auditTaskCase.setTaskguid(taskGuid);
            auditTaskCase.setTaskid(taskId);
            auditTaskCase.setOrdernum(0);
            auditTaskCase.setCasedetail(casedetail);
            // 判断是否为常用情形 1：是 0：否
            auditTaskCase.setIs_oldcase(Integer.parseInt(is_oldcase));
            auditTaskCase.setSelectedoptions(opguids);
            auditTaskCaseImpl.addAuditTaskCase(auditTaskCase);
            if (ValidateUtil.isNotBlankCollection(rtnguids)) {
                // 查询到的材料guid列表进行遍历，添加到关系表中
                for (String guid : rtnguids) {
                    AuditTaskMaterialCase auditMaterialcaseMaterial = new AuditTaskMaterialCase();
                    auditMaterialcaseMaterial.setOperatedate(new Date());
                    auditMaterialcaseMaterial.setRowguid(UUID.randomUUID().toString());
                    auditMaterialcaseMaterial.setTaskcaseguid(auditTaskCase.getRowguid());
                    auditMaterialcaseMaterial.setMaterialid(guid);
                    auditMaterialcaseMaterial.setIs_nooption(1);
                    AuditTaskMaterial auditTaskMaterial = auditTaskMaterialimpl
                            .selectMaterialByTaskIdAndMaterialId(taskId, guid).getResult();
                    if (auditTaskMaterial != null) {
                        auditMaterialcaseMaterial.setMaterialguid(auditTaskMaterial.getRowguid());
                        auditMaterialcaseMaterial.setNecessity(auditTaskMaterial.getNecessity());
                    }
                    auditMaterialcaseMaterial.setTaskguid(auditTaskCase.getTaskguid());
                    auditMaterialcaseMaterial.setTaskid(taskId);
                    auditTaskMaterialCaseimpl.addAuditTaskMaterialCase(auditMaterialcaseMaterial);
                }
            }

            this.addCallbackParam("faqCaseGuid", auditTaskCase.getRowguid());
        }
        else {
            this.addCallbackParam("msg", "情形名称重复！");
        }
        return caseguid;

    }

    @SuppressWarnings("unchecked")
    public void getAllMaterials() {
        String selectedoptions = getRequestParameter("selectedoptions");
        String taskGuid = getRequestParameter("taskguid");
        AuditTask audit = audittaskImpl.selectTaskByRowGuid(taskGuid).getResult();
        String taskId = audit.getTask_id();
        List<String> optionlist = new ArrayList<>();
        List<Map<String, Object>> rtnlist = new ArrayList<>();
        int numtemp = 0;
        // 按照传过来的数据进行解析，取出optionguidlist
        if (StringUtil.isNotBlank(selectedoptions)) {
            JSONObject jsonObject = JSONObject.parseObject(selectedoptions);
            List<Map<String, Object>> ja = (List<Map<String, Object>>) jsonObject.get("selectedoptions");
            for (int s = 0; s < ja.size(); s++) {
                List<Map<String, Object>> maplist = (List<Map<String, Object>>) ja.get(s).get("optionlist");
                for (int i = 0; i < maplist.size(); i++) {
                    optionlist.add(maplist.get(i).get("optionguid").toString());
                }
            }
        }
        if (!optionlist.isEmpty()) {
            String allmaterials = null;
            // 根据传过来的guids列表进行遍历，讲选项guid保存在json中
            // 此处代码可用sql简化，
            for (String guid : optionlist) {
                if (StringUtil.isNotBlank(guid)) {
                    AuditTaskOption auditTaskOption = iAuditTaskOptionService.find(guid).getResult();
                    if (auditTaskOption != null && StringUtil.isNotBlank(auditTaskOption.getMaterialids())) {
                        if (StringUtil.isBlank(allmaterials)) {
                            allmaterials = auditTaskOption.getMaterialids();
                        }
                        else {
                            allmaterials += auditTaskOption.getMaterialids();
                        }
                    }
                }
            }
            if (StringUtil.isNotBlank(allmaterials)) {
                String[] materialid = allmaterials.split(";");
                if (materialid.length > 0) {
                    // 去重复判断
                    List<String> qcf = new ArrayList<>();
                    for (int i = 0; i < materialid.length; i++) {
                        numtemp++;
                        if (!(qcf.contains(materialid[i]))) {
                            qcf.add(materialid[i]);
                            Map<String, Object> map = new HashMap<>();
                            AuditTaskMaterial auditTaskMaterial = auditTaskMaterialimpl
                                    .selectMaterialByTaskIdAndMaterialId(taskId, materialid[i]).getResult();
                            if (auditTaskMaterial != null) {
                                map.put("rowguid", auditTaskMaterial.getRowguid());
                                map.put("materialid", auditTaskMaterial.getMaterialid());
                                map.put("materialname", auditTaskMaterial.getMaterialname());
                                map.put("num", numtemp);
                                rtnlist.add(map);
                            }
                        }
                    }
                }
            }
        }
        SqlConditionUtil sql = new SqlConditionUtil();
        if (StringUtil.isNotBlank(taskGuid)) {
            sql.eq("taskGuid", taskGuid);
        }
        sql.eq("necessity", "10");
        List<AuditTaskMaterial> olist = auditTaskMaterialimpl.selectMaterialListByCondition(sql.getMap()).getResult();
        if (!olist.isEmpty()) {
            for (AuditTaskMaterial auditTaskMaterial : olist) {
                numtemp++;
                Map<String, Object> map = new HashMap<>();
                map.put("rowguid", auditTaskMaterial.getRowguid());
                map.put("materialid", auditTaskMaterial.getMaterialid());
                map.put("materialname", auditTaskMaterial.getMaterialname());
                map.put("num", numtemp);
                rtnlist.add(map);
            }
        }
        this.addCallbackParam("materiallist", rtnlist);
    }

    public void searchMaterials() {
        SqlConditionUtil sql = new SqlConditionUtil();
        String copyTaskGuid = getRequestParameter("copyTaskGuid");
        String taskGuid = getRequestParameter("taskGuid");
        if (StringUtil.isBlank(copyTaskGuid) || "undefined".equals(copyTaskGuid)) {
            copyTaskGuid = taskGuid;
        }
        String searchdata = getRequestParameter("searchdata");
        List<Map<String, Object>> rtnlist = new ArrayList<>();
        if (StringUtil.isNotBlank(searchdata)) {
            sql.like("materialname", searchdata);
        }
        if (StringUtil.isNotBlank(copyTaskGuid)) {
            sql.eq("taskguid", copyTaskGuid);
        }
        List<AuditTaskMaterial> materialslist = auditTaskMaterialimpl.selectMaterialListByCondition(sql.getMap())
                .getResult();
        if (!materialslist.isEmpty()) {
            for (AuditTaskMaterial auditTaskMaterial : materialslist) {
                if (10 != auditTaskMaterial.getNecessity()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("materialname", auditTaskMaterial.getMaterialname());
                    map.put("materialid", auditTaskMaterial.getMaterialid());
                    rtnlist.add(map);
                }
            }
        }
        this.addCallbackParam("materiallist", rtnlist);
    }

    public void showData() {
        String copyTaskGuid = getRequestParameter("copyTaskGuid");
        String taskGuid = getRequestParameter("taskGuid");
        if (StringUtil.isBlank(copyTaskGuid) || "undefined".equals(copyTaskGuid)) {
            copyTaskGuid = taskGuid;
        }
        AuditTask audit = audittaskImpl.selectTaskByRowGuid(taskGuid).getResult();
        String taskId = audit.getTask_id();
        String taskname = audit.getTaskname();
        JSONObject rtnjson = new JSONObject();
        Map<String, Object> custommap = new HashMap<>();
        Map<String, Object> statusmap = new HashMap<>();
        List<Map<String, Object>> elementlist = new ArrayList<>();
        SqlConditionUtil sqlc = new SqlConditionUtil();
        sqlc.eq("taskid", taskId);
        sqlc.isBlank("preoptionguid");
        sqlc.eq("draft", ZwfwConstant.CONSTANT_STR_ONE);
        sqlc.setOrderDesc("ordernum");
        sqlc.setOrderAsc("operatedate");
        List<AuditTaskElement> elelist = iAuditTaskElementService.findListByCondition(sqlc.getMap()).getResult();
        if (elelist != null & !elelist.isEmpty()) {
            for (int i = 0; i < elelist.size(); i++) {
                AuditTaskElement auditTaskElement = elelist.get(i);
                Map<String, Object> elemap = new HashMap<>();
                elemap.put("showSortUp", i != 0);
                elemap.put("showSortDown", i != elelist.size());
                elemap.put("id", auditTaskElement.getRowguid());
                elemap.put("multiple", ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskElement.getMultiselect()));
                elemap.put("type", auditTaskElement.getElementname());
                elemap.put("question", auditTaskElement.getElementquestion());
                List<AuditTaskOption> optlist = iAuditTaskOptionService
                        .findListByElementIdAndTaskid(auditTaskElement.getRowguid()).getResult();
                List<Map<String, Object>> optionlist = new ArrayList<>();
                for (int j = 0; j < optlist.size(); j++) {
                    AuditTaskOption auditTaskOption = optlist.get(j);
                    Map<String, Object> optmap = new HashMap<>();
                    optmap.put("checkbox", ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskElement.getMultiselect()));
                    optmap.put("showDelete", false);
                    optmap.put("id", auditTaskOption.getRowguid());
                    optmap.put("content", auditTaskOption.getOptionname());
                    String materialids = auditTaskOption.getMaterialids();
                    List<Map<String, Object>> materiallist = new ArrayList<>();
                    if (StringUtil.isNotBlank(materialids)) {
                        String[] material = materialids.split(";");
                        for (int k = 0; k < material.length; k++) {
                            Map<String, Object> matermap = new HashMap<>();
                            AuditTaskMaterial auditTaskMaterial = auditTaskMaterialimpl
                                    .selectTaskMaterialByTaskGuidAndMaterialId(copyTaskGuid, material[k]).getResult();
                            if (auditTaskMaterial != null) {
                                matermap.put("id", material[k]);
                                matermap.put("name", auditTaskMaterial.getMaterialname());
                                matermap.put("url", "");
                                materiallist.add(matermap);
                            }
                        }
                    }
                    optmap.put("relates", materiallist);
                    List<Map<String, Object>> childlisttemp = new ArrayList<>();
                    List<Map<String, Object>> childlist = new ArrayList<>();
                    childlist = getAllChildElement(auditTaskOption.getRowguid(), childlisttemp, taskId, copyTaskGuid);
                    optmap.put("collection", childlist);
                    optionlist.add(optmap);
                }
                elemap.put("options", optionlist);
                elementlist.add(elemap);
            }
        }
        custommap.put("title", taskname);
        custommap.put("collection", elementlist);
        statusmap.put("code", 200);
        rtnjson.put("custom", custommap);
        rtnjson.put("status", statusmap);
        this.addCallbackParam("data", rtnjson);
    }

    public List<Map<String, Object>> getAllChildElement(String optionguid, List<Map<String, Object>> childlist,
            String taskId, String copyTaskGuid) {
        List<Map<String, Object>> elementlist = new ArrayList<>();
        List<AuditTaskElement> templist = iAuditTaskElementService.findListByPreoptionGuidAndTaskId(optionguid, taskId)
                .getResult();
        for (int t = 0; t < templist.size(); t++) {
            if (templist.get(t) != null) {
                Map<String, Object> elemap = new HashMap<>();
                elemap.put("showSortUp", false);
                elemap.put("showSortDown", false);
                elemap.put("id", templist.get(t).getRowguid());
                elemap.put("type", templist.get(t).getElementname());
                elemap.put("question", templist.get(t).getElementquestion());
                elemap.put("multiple", ZwfwConstant.CONSTANT_STR_ONE.equals(templist.get(t).getMultiselect()));
                List<AuditTaskOption> optlist = iAuditTaskOptionService
                        .findListByElementIdAndTaskid(templist.get(t).getRowguid()).getResult();
                List<Map<String, Object>> optionlist = new ArrayList<>();
                for (int j = 0; j < optlist.size(); j++) {
                    AuditTaskOption auditTaskOption = optlist.get(j);
                    Map<String, Object> optmap = new HashMap<>();
                    optmap.put("checkbox", !ZwfwConstant.CONSTANT_STR_ONE.equals(templist.get(t).getMultiselect()));
                    optmap.put("showDelete", false);
                    optmap.put("id", auditTaskOption.getRowguid());
                    optmap.put("content", auditTaskOption.getOptionname());
                    String materialids = auditTaskOption.getMaterialids();
                    List<Map<String, Object>> materiallist = new ArrayList<>();
                    if (StringUtil.isNotBlank(materialids)) {
                        String[] material = materialids.split(";");
                        for (int k = 0; k < material.length; k++) {
                            Map<String, Object> matermap = new HashMap<>();
                            AuditTaskMaterial auditTaskMaterial = auditTaskMaterialimpl
                                    .selectTaskMaterialByTaskGuidAndMaterialId(copyTaskGuid, material[k]).getResult();
                            if (auditTaskMaterial != null) {
                                matermap.put("id", material[k]);
                                matermap.put("name", auditTaskMaterial.getMaterialname());
                                matermap.put("url", "");
                                materiallist.add(matermap);
                            }
                        }
                    }
                    optmap.put("relates", materiallist);
                    List<Map<String, Object>> childlisttemp = new ArrayList<>();
                    List<Map<String, Object>> childrenlist = new ArrayList<>();
                    childrenlist = getAllChildElement(auditTaskOption.getRowguid(), childlisttemp, taskId,
                            copyTaskGuid);
                    optmap.put("collection", childrenlist);
                    optionlist.add(optmap);
                }
                elemap.put("options", optionlist);
                elementlist.add(elemap);
            }
        }
        return elementlist;
    }

    public void getMaterialsModal() {
        String copyTaskGuid = getRequestParameter("copyTaskGuid");
        String taskGuid = getRequestParameter("taskGuid");
        String selectm = getRequestParameter("selectm");
        String martemp[] = null;
        //转换成json
        List<String> selectmlist = JsonUtil.jsonToList(selectm, String.class);
        if (StringUtil.isBlank(copyTaskGuid) || "undefined".equals(copyTaskGuid)) {
            copyTaskGuid = taskGuid;
        }
        String optionguid = getRequestParameter("optionguid");
        AuditTaskOption auditTaskOption = iAuditTaskOptionService.find(optionguid).getResult();
        List<Map<String, Object>> rtnlist = new ArrayList<>();
        List<AuditTaskMaterial> materialslist = auditTaskMaterialimpl.getUsableMaterialListByTaskguid(copyTaskGuid)
                .getResult();
        List<Map<String, Object>> rtnselectedlist = new ArrayList<>();
        if (!materialslist.isEmpty()) {
            //如果参数中有选中的材料默认使用参数中的
            if (selectmlist != null && !selectmlist.isEmpty()) {
                martemp = selectmlist.toArray(new String[selectmlist.size()]);
            }
            else {
                if (auditTaskOption != null && StringUtil.isNotBlank(auditTaskOption.getMaterialids())) {
                    martemp = auditTaskOption.getMaterialids().split(";");
                }
            }
            for (AuditTaskMaterial auditTaskMaterial : materialslist) {
                // 区分必要与非必要材料、非必要材料添加到list中去
                if (20 == auditTaskMaterial.getNecessity()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("materialname", auditTaskMaterial.getMaterialname());
                    map.put("materialid", auditTaskMaterial.getMaterialid());
                    rtnlist.add(map);
                    if (martemp != null) {
                        for (int i = 0; i < martemp.length; i++) {
                            if (martemp[i].equals(auditTaskMaterial.getMaterialid())) {
                                Map<String, Object> marmap = new HashMap<>();
                                marmap.put("materialid", auditTaskMaterial.getMaterialid());
                                marmap.put("materialname", auditTaskMaterial.getMaterialname());
                                rtnselectedlist.add(marmap);
                            }
                        }
                    }
                }
            }
        }
        this.addCallbackParam("materiallist", rtnlist);
        this.addCallbackParam("selectedlist", rtnselectedlist);
        // return this.materialsModal;
    }
    //

    public void saveOrder(String guids, String type) {
        if (StringUtil.isNotBlank(guids) && guids.length() > 1) {
            String[] guid = guids.split(";");
            int num = guid.length;
            // 选项
            for (int i = 0; i < num; i++) {
                if ("option".equals(type)) {
                    iAuditTaskOptionService.updateByField("ordernum", String.valueOf(num - i), guid[i]);
                }
                else {
                    iAuditTaskElementService.updateByField("ordernum", String.valueOf(num - i), guid[i]);
                }
            }
            addCallbackParam("msg", "成功！");
        }
        else {
            addCallbackParam("msg", "未获取排序标识！");
        }
    }

    public void savePreoptionguid(String preoptionguid, String rowguid) {
        if (StringUtil.isNotBlank(preoptionguid) && StringUtil.isNotBlank(rowguid)) {
            iAuditTaskElementService.updateByField("preoptionguid", preoptionguid, rowguid);
            addCallbackParam("msg", "成功");
        }
        else {
            addCallbackParam("msg", "未获取参数");
        }
    }

    public void saveText(String rowguid, String text, String type) {
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(type)) {
            AuditTaskOption auditTaskOption = iAuditTaskOptionService.find(rowguid).getResult();
            if (iAuditTaskOptionService
                    .CheckOptionName(auditTaskOption.getTaskid(), rowguid, text, auditTaskOption.getElementguid())
                    .getResult()) {
                iAuditTaskOptionService.updateByField("optionname", text, rowguid);
            }
            else {
                addCallbackParam("msg", "同一问题下选项名称重复！");
            }
        }
        else if (ZwfwConstant.CONSTANT_STR_TWO.equals(type)) {
            iAuditTaskElementService.updateByField("elementquestion", text, rowguid);
        }
        else if (ZwfwConstant.CONSTANT_STR_THREE.equals(type)) {
            iAuditTaskElementService.updateByField("elementname", text, rowguid);
        }
        else {
            addCallbackParam("msg", "未获取到类型！");
        }
    }

    public void delObj(String rowguid, String type) {
        if (StringUtil.isNotBlank(rowguid) && StringUtil.isNotBlank(type)) {
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(type)) {
                delEleAndOption(rowguid, type);
            }
            else if (ZwfwConstant.CONSTANT_STR_TWO.equals(type)) {
                delEleAndOption(rowguid, type);
            }
            else {
                addCallbackParam("msg", "类型获取异常！");
            }
        }
        else {
            addCallbackParam("msg", "数据异常！");
        }
    }

    public void delEleAndOption(String rowguid, String type) {
        // 删除选项
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(type)) {
            // 查询选项有无子选项
            iAuditTaskOptionService.deleteByGuid(rowguid);
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("preoptionguid", rowguid);
            List<AuditTaskElement> list = iAuditTaskElementService.findListByCondition(sqlc.getMap()).getResult();
            for (AuditTaskElement auditTaskElement : list) {
                delEleAndOption(auditTaskElement.getRowguid(), ZwfwConstant.CONSTANT_STR_TWO);
            }
        }
        else {
            iAuditTaskElementService.deleteByGuid(rowguid);
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("elementguid", rowguid);
            List<AuditTaskOption> list = iAuditTaskOptionService.findListByCondition(sqlc.getMap()).getResult();
            for (AuditTaskOption auditTaskOption : list) {
                delEleAndOption(auditTaskOption.getRowguid(), ZwfwConstant.CONSTANT_STR_ONE);
            }
        }
    }

    public void reSet(String taskid) {
        // 删除草稿的要素，重新复制
        SqlConditionUtil sqlc = new SqlConditionUtil();
        sqlc.eq("taskid", taskid);
        sqlc.eq("draft", ZwfwConstant.CONSTANT_STR_ONE);
        List<AuditTaskElement> liste = iAuditTaskElementService.findListByCondition(sqlc.getMap()).getResult();
        for (AuditTaskElement auditTaskElement : liste) {
            // 删除要素
            iAuditTaskElementService.deleteByGuid(auditTaskElement.getRowguid());
            // 删除情形
            iAuditTaskOptionService.deleteByOneField("elementguid", auditTaskElement.getRowguid());
        }
        copyelement(taskid);
    }

    public void save(String taskid) {
        String msg = "";
        if (StringUtil.isNotBlank(taskid)) {
            try {
                // 删除草稿的要素，重新复制
                SqlConditionUtil sqlc = new SqlConditionUtil();
                sqlc.eq("taskid", taskid);
                sqlc.isBlankOrValue("draft", ZwfwConstant.CONSTANT_STR_ZERO);
                List<AuditTaskElement> liste = iAuditTaskElementService.findListByCondition(sqlc.getMap()).getResult();
                for (AuditTaskElement auditTaskElement : liste) {
                    // 删除要素
                    iAuditTaskElementService.deleteByGuid(auditTaskElement.getRowguid());
                    // 删除情形
                    iAuditTaskOptionService.deleteByOneField("elementguid", auditTaskElement.getRowguid());
                }
                // 修改草稿为正式事项
                sqlc.clear();
                sqlc.eq("taskid", taskid);
                sqlc.eq("draft", ZwfwConstant.CONSTANT_STR_ONE);
                liste = iAuditTaskElementService.findListByCondition(sqlc.getMap()).getResult();
                for (AuditTaskElement auditTaskElement : liste) {
                    iAuditTaskElementService.updateByField("draft", ZwfwConstant.CONSTANT_STR_ZERO,
                            auditTaskElement.getRowguid());
                }
                msg = "成功";
            }
            catch (Exception e) {
                msg = "数据异常！";
            }
        }
        else {
            msg = "未获取去到事项标识";
        }
        addCallbackParam("msg", msg);
    }

    public void copyelement(String taskid) {
        // 查询是否有草稿情形
        String msg;
        if (StringUtil.isBlank(taskid)) {
            addCallbackParam("msg", "未获取到事项版本唯一标识！");
            return;
        }
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("taskid", taskid);
        sql.eq("draft", ZwfwConstant.CONSTANT_STR_ONE);
        Integer count = iAuditTaskElementService.findCountByCondition(sql.getMap()).getResult();
        if (count != null && count > 0) {
            msg = "成功！";
        }
        else {
            // 没有草稿，重新复制草稿
            sql.clear();
            sql.eq("taskid", taskid);
            sql.isBlankOrValue("draft", ZwfwConstant.CONSTANT_STR_ZERO);
            List<AuditTaskElement> liste = iAuditTaskElementService.findListByCondition(sql.getMap()).getResult();
            for (AuditTaskElement auditTaskElement : liste) {
                auditTaskElement.setOperateusername("复制");
                auditTaskElement.setDraft(ZwfwConstant.CONSTANT_STR_ONE);
                auditTaskElement.setRowguid(MDUtils.md5Hex(auditTaskElement.getRowguid()));

                if (StringUtil.isNotBlank(auditTaskElement.getPreoptionguid())
                        && auditTaskElement.getPreoptionguid().indexOf("start") == -1) {
                    auditTaskElement.setPreoptionguid(MDUtils.md5Hex(auditTaskElement.getPreoptionguid()));
                }
                iAuditTaskElementService.insert(auditTaskElement);
            }
            List<AuditTaskOption> listo = iAuditTaskOptionService.findListByTaskid(taskid).getResult();
            for (AuditTaskOption auditTaskOption : listo) {
                auditTaskOption.setOperateusername("复制");
                auditTaskOption.setRowguid(MDUtils.md5Hex(auditTaskOption.getRowguid()));
                auditTaskOption.setElementguid(MDUtils.md5Hex(auditTaskOption.getElementguid()));
                auditTaskOption.setCopyfrom(auditTaskOption.getRowguid());
                iAuditTaskOptionService.insert(auditTaskOption);
            }
            msg = "成功！";

        }
        addCallbackParam("msg", msg);
    }

    public void addelement(String taskid, String optionguid) {
        String rowguid = UUID.randomUUID().toString();
        String msg = "";
        if (StringUtil.isNotBlank(taskid)) {
            try {
                AuditTaskElement element = new AuditTaskElement();
                element.setOperatedate(new Date());
                element.setOperateusername(UserSession.getInstance().getDisplayName());
                element.setRowguid(rowguid);
                element.setTaskid(taskid);
                element.setOrdernum(0);
                element.setMultiselect("0");
                element.setDraft(ZwfwConstant.CONSTANT_STR_ONE);
                if (StringUtil.isNotBlank(optionguid)) {
                    element.setPreoptionguid(optionguid);
                }
                iAuditTaskElementService.insert(element);

                msg = "成功！";
                addCallbackParam("guid", rowguid);
            }
            catch (Exception e) {
                msg = "添加数据异常！";
            }
        }
        else {
            msg = "未获取事项标识！";
        }
        addCallbackParam("msg", msg);
    }

    public void addoption(String taskid, String elementguid) {
        String rowguid = UUID.randomUUID().toString();
        String msg = "";
        if (StringUtil.isNotBlank(taskid) && StringUtil.isNotBlank(elementguid)) {
            try {
                AuditTaskOption option = new AuditTaskOption();
                option.setOperatedate(new Date());
                option.setOperateusername(UserSession.getInstance().getDisplayName());
                option.setRowguid(rowguid);
                option.setTaskid(taskid);
                option.setOrdernum(0);
                option.setElementguid(elementguid);
                iAuditTaskOptionService.insert(option);
                msg = "成功！";
                addCallbackParam("guid", rowguid);
            }
            catch (Exception e) {
                msg = "添加数据异常！";
            }
        }
        else {
            msg = "未获取事项标识或要素标识！";
        }
        addCallbackParam("msg", msg);
    }

    /**
     * 修改多选
     */
    public void editmultiselect(String type, String rowguid) {
        String msg = "";
        if (StringUtil.isNotBlank(type) && StringUtil.isNotBlank(rowguid)) {
            try {
                if ("true".equals(type)) {
                    iAuditTaskElementService.updateByField("multiselect", ZwfwConstant.CONSTANT_STR_ZERO, rowguid);
                }
                else {
                    iAuditTaskElementService.updateByField("multiselect", ZwfwConstant.CONSTANT_STR_ONE, rowguid);
                }
                msg = "成功！";
            }
            catch (Exception e) {
                msg = "数据异常！";
            }
        }
        else {
            msg = "参数未获取！";
        }
        addCallbackParam("msg", msg);

    }

    public void refreshNewMaterial() {
        String optionguid = getRequestParameter("optionguid");
        String materialstemp = getRequestParameter("materialstemp");
        String materialsnametemp = getRequestParameter("materialsnametemp");
        AuditTaskOption auditTaskOption = iAuditTaskOptionService.find(optionguid).getResult();
        if (auditTaskOption != null) {
            auditTaskOption.setMaterialids(materialstemp);
            auditTaskOption.setMaterialnames(materialsnametemp);
            iAuditTaskOptionService.update(auditTaskOption);
            this.addCallbackParam("msg", "修改成功！");
        }
        else {
            this.addCallbackParam("error", "修改失败，请重新尝试！");
        }
    }

    public void delmaterial(String materialid, String materialname, String optionguid) {
        String msg = "";
        if (StringUtil.isNotBlank(materialid) && StringUtil.isNotBlank(optionguid)) {
            try {
                AuditTaskOption option = iAuditTaskOptionService.find(optionguid).getResult();
                String materials = option.getMaterialids();
                String materialnames = option.getMaterialnames();
                materials = materials.replaceAll(materialid + ";", "");
                materialnames = materialnames.replaceAll(materialname + ";", "");
                option.setMaterialids(materials);
                option.setMaterialnames(materialnames);
                iAuditTaskOptionService.update(option);
                msg = "成功！";
            }
            catch (Exception e) {
                msg = "数据异常！";
            }

        }
        else {
            msg = "参数未获取！";
        }
        addCallbackParam("msg", msg);
    }

    public void selectTaskCase() {
        String taskGuid = getRequestParameter("taskGuid");
        List<Map<String, Object>> rtnlist = new ArrayList<>();
        List<AuditTaskCase> auditTaskCaselist = auditTaskCaseImpl.selectTaskCaseByTaskGuid(taskGuid, "1").getResult();
        if (!auditTaskCaselist.isEmpty()) {
            for (AuditTaskCase auditTaskCase : auditTaskCaselist) {
                Map<String, Object> map = new HashMap<>();
                map.put("value", auditTaskCase.getRowguid());
                map.put("text", auditTaskCase.getCasename());
                rtnlist.add(map);
            }
            this.addCallbackParam("data", rtnlist);
        }
        else {
            this.addCallbackParam("msg", "暂无常用情形");
        }
    }

    private Map<String, AuditTaskElement> mape;
    private Map<String, AuditTaskOption> mapo;

    // 核验时获取所有未绑定选项的材料list
    public void getAllUnBindMaterials() {
        String taskguid = getRequestParameter("copytaskguid");
        String taskId = getRequestParameter("taskid");
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("taskid", taskId);
        sql.eq("draft", ZwfwConstant.CONSTANT_STR_ONE);
        List<AuditTaskElement> elementlist = iAuditTaskElementService.findListByCondition(sql.getMap()).getResult();
        List<AuditTaskOption> optionlist1 = new ArrayList<>();
        List<String> materiallist = new ArrayList<>();
        String rtnMaterialname = "";
        // 获取所有已选中材料list
        if (!elementlist.isEmpty()) {
            for (AuditTaskElement auditTaskElement : elementlist) {
                List<AuditTaskOption> optionlist = iAuditTaskOptionService
                        .findListByElementIdAndTaskid(auditTaskElement.getRowguid()).getResult();
                //添加进optionlist
                optionlist1.addAll(optionlist);
                if (!optionlist.isEmpty()) {
                    for (AuditTaskOption auditTaskOption : optionlist) {
                        String materials = auditTaskOption.getMaterialids();
                        if (StringUtil.isNotBlank(materials)) {
                            String[] temp = materials.split(";");
                            if (temp.length > 0) {
                                for (int i = 0; i < temp.length; i++) {
                                    if (temp[i] != null && !materiallist.contains(temp[i])) {
                                        materiallist.add(temp[i]);
                                    }
                                }
                            }
                        }
                    }

                }
            }
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("taskguid", taskguid);
            sqlc.eq("necessity", "20");
            List<AuditTaskMaterial> materialslist = auditTaskMaterialimpl.selectMaterialListByCondition(sqlc.getMap())
                    .getResult();
            //去除主项才材料
            List<String> level1list = materialslist.stream().map(AuditTaskMaterial::getPid)
                    .filter(a -> StringUtil.isNotBlank(a)).collect(Collectors.toList());
            materialslist.removeIf(a -> {
                return level1list.contains(a.getMaterialid());
            });

            if (materialslist != null & !materialslist.isEmpty()) {
                for (AuditTaskMaterial auditTaskMaterial : materialslist) {
                    Boolean flag = true;
                    for (String materialid : materiallist) {
                        if (auditTaskMaterial.getMaterialid().equals(materialid)) {
                            flag = false;
                        }
                    }
                    if (flag) {
                        if (StringUtil.isBlank(rtnMaterialname)) {
                            rtnMaterialname = auditTaskMaterial.getMaterialname();
                        }
                        else {
                            rtnMaterialname += "、" + auditTaskMaterial.getMaterialname();
                        }
                    }
                }
            }

            //处理重复材料的逻辑
            //将问题和选项转换成map
            mape = elementlist.stream()
                    .collect(Collectors.toMap(AuditTaskElement::getRowguid, AuditTaskElement -> AuditTaskElement));
            List<String> preoptionlist = elementlist.stream().map(AuditTaskElement::getPreoptionguid)
                    .collect(Collectors.toList());
            mapo = optionlist1.stream()
                    .collect(Collectors.toMap(AuditTaskOption::getRowguid, AuditTaskOption -> AuditTaskOption));
            //查找出，所有根节点的选项，过滤没有关联问题的选项
            List<AuditTaskOption> optionlist = optionlist1.stream().filter(a -> !preoptionlist.contains(a.getRowguid()))
                    .collect(Collectors.toList());
            List<String> noticematerial = new ArrayList<>();
            for (AuditTaskOption auditTaskOption : optionlist) {
                List<String> nomalmaterial = new ArrayList<>();
                List<String> noticematerial1 = new ArrayList<>();
                getnoticemateiral(auditTaskOption, nomalmaterial, noticematerial1);
                noticematerial.addAll(noticematerial1);
            }

            //去重
            noticematerial = noticematerial.stream().distinct().collect(Collectors.toList());

            //材料转换成键值对
            Map<String, String> materialidmap = materialslist.stream()
                    .collect(Collectors.toMap(AuditTaskMaterial::getMaterialid, AuditTaskMaterial::getMaterialname));
            if (!noticematerial.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String string : noticematerial) {
                    if (sb.length() == 0) {
                        if (StringUtil.isNotBlank(materialidmap.get(string))) {
                            sb.append(materialidmap.get(string));
                        }
                    }
                    else {
                        if (StringUtil.isNotBlank(materialidmap.get(string))) {
                            sb.append("、").append(materialidmap.get(string));
                        }
                    }
                }
                this.addCallbackParam("noticematerial", sb.toString());

            }

            if (StringUtil.isBlank(rtnMaterialname)) {
                this.addCallbackParam("msg", "材料已全部绑定！");
            }
            else {
                this.addCallbackParam("data", rtnMaterialname);
            }
            if (materialslist.isEmpty()) {
                this.addCallbackParam("msg", "暂未有材料需要绑定！");
            }
        }
        else {
            this.addCallbackParam("msg", "事项尚未配置情形！");
        }

        // 获取事项所有非必要材料

    }

    //递归查找材料
    public void getnoticemateiral(AuditTaskOption auditTaskOption, List<String> nomalmaterial,
            List<String> noticematerial) {
        String mateiral = auditTaskOption.getMaterialids();
        if (StringUtil.isNotBlank(mateiral)) {
            String[] mateirals = mateiral.split(";");
            if (mateirals.length > 0) {
                for (int i = 0; i < mateirals.length; i++) {
                    if (nomalmaterial.contains(mateirals[i])) {
                        //重复材料
                        if (!noticematerial.contains(mateirals[i])) {
                            noticematerial.add(mateirals[i]);
                        }
                    }
                    else {
                        //正常材料
                        nomalmaterial.add(mateirals[i]);
                    }
                }
            }
        }
        //查找选项
        AuditTaskElement ele = mape.get(auditTaskOption.getElementguid());
        if (ele.getIsDelete() != null && ele.getIsDelete() == ZwfwConstant.CONSTANT_INT_ONE) {
            noticematerial = new ArrayList<>();
            nomalmaterial = new ArrayList<>();
        }
        String proptionguid = ele.getPreoptionguid();
        if (StringUtil.isNotBlank(proptionguid) && proptionguid.indexOf("start") == -1) {
            AuditTaskOption option = mapo.get(proptionguid);
            if (option != null) {
                getnoticemateiral(option, nomalmaterial, noticematerial);
            }
        }
    }

    public void searchCases() {
        SqlConditionUtil sql = new SqlConditionUtil();
        String taskGuid = getRequestParameter("taskGuid");
        String searchdata = getRequestParameter("searchdata");
        List<Map<String, Object>> rtnlist = new ArrayList<>();
        if (StringUtil.isNotBlank(searchdata)) {
            sql.like("casename", searchdata);
        }
        if (StringUtil.isNotBlank(taskGuid)) {
            sql.eq("taskguid", taskGuid);
        }
        sql.eq("is_oldcase", "1");
        sql.setOrderDesc("ordernum");
        List<AuditTaskCase> caselist = auditTaskCaseImpl.selectCaseListByCondition(sql.getMap()).getResult();
        if (!caselist.isEmpty()) {
            for (AuditTaskCase auditTaskCase : caselist) {
                Map<String, Object> map = new HashMap<>();
                map.put("value", auditTaskCase.getRowguid());
                map.put("text", auditTaskCase.getCasename());
                rtnlist.add(map);
            }
        }
        this.addCallbackParam("caselist", rtnlist);
    }

    public void getSelectedOptionsByprojectguid() {
        String projectguid = getRequestParameter("projectguid");
        String selectedoptions = "";
        AuditTaskSelectedOption auditTaskSelectedOption = iAuditTaskSelectedOptionService.findByProjectGuid(projectguid)
                .getResult();
        if (auditTaskSelectedOption != null) {
            selectedoptions = auditTaskSelectedOption.getSelectedoptions();
        }
        this.addCallbackParam("data", selectedoptions);
    }

    public void getShareElement() {
        String taskguid = getRequestParameter("taskguid");
        AuditTask audit = audittaskImpl.selectTaskByRowGuid(taskguid).getResult();
        Map<String, String> sharesel = new HashMap<>();
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("taskid", audit.getTask_id());
        sql.isBlankOrValue("draft", ZwfwConstant.CONSTANT_STR_ZERO);
        List<AuditTaskElement> liste = iAuditTaskElementService.findListByCondition(sql.getMap()).getResult();
        if (!liste.isEmpty()) {
            Map<String, List<AuditTaskElement>> shareselarr = liste.stream()
                    .filter(a -> StringUtil.isNotBlank(a.getSharevalue()))
                    .collect(Collectors.groupingBy(a -> a.getSharevalue()));
            for (Map.Entry<String, List<AuditTaskElement>> entry : shareselarr.entrySet()) {
                List<AuditTaskElement> value = entry.getValue();
                if (value != null && !value.isEmpty()) {
                    sharesel.put(entry.getKey(), value.stream().map(a -> a.getRowguid()).collect(Collectors.joining(";")));
                }
            }
        }
        this.addCallbackParam("sharesel", sharesel);
    }

    public void deleteTaskcase(String caseid, String taskGuid) {
        AuditTaskCase auditTaskCase = auditTaskCaseImpl.getAuditTaskCaseByRowguid(caseid).getResult();
        if (auditTaskCase != null) {
            auditTaskCaseImpl.deleteAuditTaskCase(auditTaskCase);
            List<AuditTaskMaterialCase> list = auditTaskMaterialCaseimpl.selectTaskMaterialCaseByCaseGuid(caseid)
                    .getResult();
            for (AuditTaskMaterialCase auditTaskMaterialCase : list) {
                auditTaskMaterialCaseimpl.deleteAuditTaskMaterialCase(auditTaskMaterialCase);
            }
            this.addCallbackParam("msg", "删除成功！");
        }
        else {
            this.addCallbackParam("err", "未找到相应情形！");
        }
    }

    // ###############################################pad端对接方法#######################################################################
    public void layerAdd() {
        try {
            String taskGuid = getRequestParameter("taskGuid");
            String taskid = getRequestParameter("taskid");
            String areacode = getRequestParameter("areacode");
            String projectguid = getRequestParameter("projectguid");
            String certnum = getRequestParameter("certnum");
            String sqr = getRequestParameter("sqr");
            String contactcertnum = getRequestParameter("contactcertnum");
            String contactperson = getRequestParameter("contactperson");
            JSONObject applyerinfo = new JSONObject();
            if (StringUtil.isNotBlank(certnum)) {
                applyerinfo.put("certnum", certnum);
                applyerinfo.put("sqr", sqr);
                applyerinfo.put("contactcertnum", contactcertnum);
                applyerinfo.put("contactperson", contactperson);
            }
            JSONObject json = new JSONObject();
            json.put("taskGuid", taskGuid);
            json.put("taskid", taskid);
            json.put("projectguid", projectguid);
            json.put("status", "7");
            json.put("areacode", areacode);
            String windowguid = ZwfwUserSession.getInstance().getWindowGuid();
            AuditCommonResult<String> rtnstr = auditTaskCaseImpl.sendMQLoginbyEvaluate(windowguid, json);
            if ("err".equals(rtnstr.getResult())) {
                this.addCallbackParam("err", "网络连接错误,信息发送失败！");
            }
            else {
                if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                    // 2.1、如果有系统使用redis，则将二维码存放在redis中
                    ZwfwRedisCacheUtil redisUtil = null;
                    try {
                        redisUtil = new ZwfwRedisCacheUtil(false);
                        redisUtil.putByString("scan_" + projectguid, ZwfwConstant.CONSTANT_STR_ZERO, 300);
                        // 判断redis中是否已存在申请人信息
                        String info = redisUtil.getByString("applyerinfo_" + projectguid);
                        if (StringUtil.isNotBlank(info)) {
                            redisUtil.del("applyerinfo_" + projectguid);
                        }
                        if (StringUtil.isNotBlank(certnum)) {
                            redisUtil.putByString("applyerinfo_" + projectguid, applyerinfo, 600);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        if (redisUtil != null) {
                            redisUtil.close();
                        }
                    }
                }
                this.addCallbackParam("msg", "发送成功");
            }
        }
        catch (JSONException e) {
            this.addCallbackParam("err", "出现异常：" + e.getMessage());
        }

    }

    public void layerEdit() {
        try {
            String taskGuid = getRequestParameter("taskGuid");
            String taskid = getRequestParameter("taskid");
            String areacode = getRequestParameter("areacode");
            String projectguid = getRequestParameter("projectguid");
            JSONObject json = new JSONObject();
            json.put("taskGuid", taskGuid);
            json.put("taskid", taskid);
            json.put("projectguid", projectguid);
            json.put("status", "8");
            json.put("areacode", areacode);
            String windowguid = ZwfwUserSession.getInstance().getWindowGuid();
            AuditCommonResult<String> rtnstr = auditTaskCaseImpl.sendMQLoginbyEvaluate(windowguid, json);
            if ("err".equals(rtnstr.getResult())) {
                this.addCallbackParam("err", "网络连接错误,信息发送失败！");
            }
            else {
                if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                    // 2.1、如果有系统使用redis，则将二维码存放在redis中
                    ZwfwRedisCacheUtil redisUtil = null;
                    try {
                        redisUtil = new ZwfwRedisCacheUtil(false);
                        redisUtil.putByString("scan_" + projectguid, ZwfwConstant.CONSTANT_STR_ZERO, 300);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        if (redisUtil != null) {
                            redisUtil.close();
                        }
                    }
                }
                this.addCallbackParam("msg", "发送成功");
            }
        }
        catch (JSONException e) {
            this.addCallbackParam("err", "出现异常：" + e.getMessage());
        }

    }

    public void materialConfirm() {
        try {
            String taskGuid = getRequestParameter("taskGuid");
            String taskid = getRequestParameter("taskid");
            String areacode = getRequestParameter("areacode");
            String projectguid = getRequestParameter("projectguid");
            JSONObject json = new JSONObject();
            json.put("taskGuid", taskGuid);
            json.put("taskid", taskid);
            json.put("projectguid", projectguid);
            json.put("status", "9");
            json.put("areacode", areacode);
            String windowguid = ZwfwUserSession.getInstance().getWindowGuid();
            AuditCommonResult<String> rtnstr = auditTaskCaseImpl.sendMQLoginbyEvaluate(windowguid, json);
            if ("err".equals(rtnstr.getResult())) {
                this.addCallbackParam("err", "网络连接错误,信息发送失败！");
            }
            else {
                this.addCallbackParam("msg", "发送成功");
            }
        }
        catch (JSONException e) {
            this.addCallbackParam("err", "出现异常：" + e.getMessage());
        }

    }

    // 轮询查看redis中状态
    public void scanProject() {
        try {
            String projectguid = getRequestParameter("projectguid");
            JSONObject dataJson = new JSONObject();
            if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                ZwfwRedisCacheUtil redisUtil = null;
                try {
                    redisUtil = new ZwfwRedisCacheUtil(false);
                    String status = redisUtil.getByString("scan_" + projectguid);
                    if (StringUtil.isNotBlank(status)) {
                        dataJson.put("status", status);
                        redisUtil.del("scan_" + projectguid);
                    }
                    else {
                        dataJson.put("status", ZwfwConstant.CONSTANT_STR_ZERO);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if (redisUtil != null) {
                        redisUtil.close();
                    }
                }
            }
            this.addCallbackParam("data", dataJson);
        }
        catch (JSONException e) {
            this.addCallbackParam("err", "出现异常：" + e.getMessage());
        }

    }

    // 根据projectguid和areacode查询pad端添加的caseguid和params
    public void getPADData() {
        try {
            String projectguid = getRequestParameter("projectguid");
            String data = "";
            // 1.1根据办件标识和区域编码查询相应信息
            if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                ZwfwRedisCacheUtil redisUtil = null;
                try {
                    redisUtil = new ZwfwRedisCacheUtil(false);
                    data = redisUtil.getByString("data_" + projectguid);
                    if (StringUtil.isNotBlank(data)) {
                        redisUtil.del("data_" + projectguid);
                    }
                    // 此处需要删除redis中的数据
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if (redisUtil != null) {
                        redisUtil.close();
                    }
                }
                this.addCallbackParam("data", data);
            }
            else {
                this.addCallbackParam("msg", "未开启redis！");
            }

            /*
             * AuditProject auditProject =
             * auditProjectService.getAuditProjectByRowGuid(projectguid,
             * areacode).getResult(); if(auditProject!=null){
             * if(StringUtil.isNotBlank(auditProject.getTaskcaseguid())){
             * datajson.put("caseguid", auditProject.getTaskcaseguid()); } }
             * AuditTaskSelectedOption auditTaskSelectedOption =
             * iAuditTaskSelectedOptionService.findByProjectGuid(projectguid).
             * getResult(); if(auditTaskSelectedOption!=null){
             * if(StringUtil.isNotBlank(auditTaskSelectedOption.
             * getSelectedoptions())){ datajson.put("selectedoptions",
             * auditTaskSelectedOption.getSelectedoptions()); } }
             */
        }
        catch (JSONException e) {
            this.addCallbackParam("err", "出现异常：" + e.getMessage());
        }

    }

    public void sendShouliInfo() {
        try {
            String areacode = getRequestParameter("areacode");
            String projectguid = getRequestParameter("projectguid");
            JSONObject json = new JSONObject();
            json.put("projectguid", projectguid);
            json.put("status", "10");
            json.put("areacode", areacode);
            String windowguid = ZwfwUserSession.getInstance().getWindowGuid();
            AuditCommonResult<String> rtnstr = auditTaskCaseImpl.sendMQLoginbyEvaluate(windowguid, json);
            if ("err".equals(rtnstr.getResult())) {
                this.addCallbackParam("err", "网络连接错误,信息发送失败！");
            }
            else {
                // 清除redis中所有的缓存
                if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                    // 2.1、如果有系统使用redis，则将二维码存放在redis中
                    ZwfwRedisCacheUtil redisUtil = null;
                    try {
                        redisUtil = new ZwfwRedisCacheUtil(false);
                        redisUtil.putByString("scan_" + projectguid, ZwfwConstant.CONSTANT_STR_ZERO, 300);
                        // 判断redis中是否已存在申请人信息
                        String info = redisUtil.getByString("applyerinfo_" + projectguid);
                        if (StringUtil.isNotBlank(info)) {
                            redisUtil.del("applyerinfo_" + projectguid);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        if (redisUtil != null) {
                            redisUtil.close();
                        }
                    }
                }
                this.addCallbackParam("msg", "发送成功");
            }
        }
        catch (JSONException e) {
            this.addCallbackParam("err", "出现异常：" + e.getMessage());
        }
    }

    public void getSelectedOptionsBycaseguid() {
        String caseguid = getRequestParameter("caseguid");
        String selectedoptions = "";
        AuditTaskCase auditTaskCase = auditTaskCaseImpl.getAuditTaskCaseByRowguid(caseguid).getResult();
        if (auditTaskCase != null) {
            selectedoptions = auditTaskCase.getSelectedoptions();
        }
        this.addCallbackParam("data", selectedoptions);
    }

}
