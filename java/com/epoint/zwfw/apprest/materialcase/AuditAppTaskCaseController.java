package com.epoint.zwfw.apprest.materialcase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
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
import com.epoint.basic.controller.api.ApiBaseController;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.util.ZwfwRedisCacheUtil;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/materialcase")
public class AuditAppTaskCaseController extends ApiBaseController {
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * 事项API
     */
    @Autowired
    private IAuditTask iAuditTask;

    /**
     * 办件材料API
     */
    @Autowired
    private IAuditProjectMaterial iAuditProjectMaterial;

    /**
     * 代码项API
     */
    @Autowired
    private ICodeItemsService iCodeItemsService;

    /**
     * 窗口API
     */
    @Autowired
    private IAttachService iAttachService;

    /**
     * 初始化办件API
     */
    @Autowired
    private IHandleProject handleProjectService;

    /**
     * 办件API
     */
    @Autowired
    private IAuditProject auditProjectService;

    /**
     * 事项材料API
     */
    @Autowired
    private IAuditTaskMaterial iAuditTaskMaterial;

    /**
     * 事项情形与材料关系API
     */
    @Autowired
    private IAuditTaskMaterialCase iAuditTaskMaterialCase;

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

    @Autowired
    private IAuditTaskCase auditTaskCaseImpl;

    @Autowired
    private IAuditTaskSelectedOptionService iAuditTaskSelectedOptionService;

    /**
     * 日志
     */

    /**
     * 获取APP待审批办件列表
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getprojectinfo", method = RequestMethod.POST)
    public String getProjectinfo(@RequestBody String params, HttpServletRequest request) {
        try {
            log.info("=======开始调用getprojectinfo接口=======");
            // 1、入参转化为JSON对象
            JSONObject json = JSON.parseObject(params);
//            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject jsonObject = (JSONObject) json.get("params");
            String projectguid = jsonObject.getString("projectguid");// 办件guid
            String areacode = jsonObject.getString("areacode");// 区域编码
            // 定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            // 1.1根据办件标识和区域编码查询相应信息
            AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid(projectguid, areacode).getResult();
            if (auditProject != null) {
                // 1.2根据所查询出来的办件信息，查询事项信息
                AuditTask auditTask = iAuditTask.selectTaskByRowGuid(auditProject.getTaskguid()).getResult();
                // 1.3添加返回数据
                if (auditTask != null) {
                    dataJson.put("taskname", auditTask.getTaskname());// 事项名称
                    dataJson.put("taskcode", auditTask.getItem_id());// 事项编码
                    dataJson.put("tasktype",
                            iCodeItemsService.getItemTextByCodeName("事项类型", String.valueOf(auditTask.getType())));// 1承诺件
                    // 0即办件
                    dataJson.put("promisedate", auditTask.getPromise_day());// 承诺期限
                    dataJson.put("ouname", auditTask.getOuname());// 事项所属部门
                }
                if (StringUtil.isNotBlank(auditProject.getCertnum())) {
                    dataJson.put("applyername", auditProject.getApplyername());// 申请人姓名
                    dataJson.put("applyercertnum", auditProject.getCertnum());// 申请人身份证号
                    dataJson.put("contactname", auditProject.getContactperson());// 联系人姓名
                    dataJson.put("contactcertnum", auditProject.getContactcertnum());// 联系人身份证号
                } else {
                    if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                        ZwfwRedisCacheUtil redisUtil = null;
                        try {
                            redisUtil = new ZwfwRedisCacheUtil(false);
                            String info = redisUtil.getByString("applyerinfo_" + projectguid);
                            if (StringUtil.isNotBlank(info)) {
                                JSONObject infojson = (JSONObject) JSONObject.parse(info);
                                dataJson.put("applyername", infojson.get("sqr"));// 申请人姓名
                                dataJson.put("applyercertnum", infojson.get("certnum"));// 申请人身份证号
                                dataJson.put("contactname", infojson.get("contactperson"));// 联系人姓名
                                dataJson.put("contactcertnum", infojson.get("contactcertnum"));// 联系人身份证号
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (redisUtil != null) {
                                redisUtil.close();
                            }
                        }

                    }
                }
            }
            // dataJson.put("custom", map);
            log.info("=======结束调用getprojectinfo接口=======");
            return JsonUtils.zwfwAPPRestReturn("1", "获取配置参数信息成功！", dataJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getprojectinfo接口参数：params【" + params + "】=======");
            log.info("=======getprojectinfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwfwAPPRestReturn("0", "获取配置参数信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getmaterialcase", method = RequestMethod.POST)
    public String getMaterialcase(@RequestBody String params, HttpServletRequest request) {
        try {
            log.info("=======开始调用getmaterialcase接口=======");
            // 1、入参转化为JSON对象
            JSONObject json = JSON.parseObject(params);
//          JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject jsonObject = (JSONObject) json.get("params");
            // 定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            String preoptionguid = jsonObject.getString("preoptionguid");// 如果为空则获取第一层
            String projectguid = jsonObject.getString("projectguid");// 办件guid
            String areacode = jsonObject.getString("areacode");// 区域编码
            // 定义返回JSON对象
            // 1.1根据办件标识和区域编码查询相应信息
            AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid(projectguid, areacode).getResult();
            // 定义存储要素信息的List
            List<JSONObject> subElementJsonList = new ArrayList<JSONObject>();
            SqlConditionUtil sql = new SqlConditionUtil();
            if (auditProject != null) {
                // 1.2根据所查询出来的办件信息，查询事项信息
                AuditTask auditTask = iAuditTask.selectTaskByRowGuid(auditProject.getTaskguid()).getResult();
                if (StringUtil.isBlank(preoptionguid) || "".equals(preoptionguid) || preoptionguid == null) {
                    sql.rightLike("preoptionguid", "start");
                } else {
                    String guids[] = preoptionguid.split(";");
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
                sql.isBlankOrValue("draft", "0");
                if (StringUtil.isNotBlank(auditTask.getTask_id())) {
                    sql.eq("taskId", auditTask.getTask_id());
                }
                sql.setOrder("ordernum desc,operatedate", "asc");

                List<AuditTaskElement> auditTaskElements = iAuditTaskElementService.findListByCondition(sql.getMap())
                        .getResult();
                if (auditTaskElements != null && auditTaskElements.size() > 0) {
                    for (AuditTaskElement auditTaskElement : auditTaskElements) {
                        // 定义存储要素信息的Json
                        JSONObject subElementJson = new JSONObject();
                        // 2.1、查询要素选项信息
                        List<AuditTaskOption> auditTaskOptions = iAuditTaskOptionService
                                .findListByElementIdWithoutNoName(auditTaskElement.getRowguid()).getResult();
                        if (auditTaskOptions != null && auditTaskOptions.size() > 1) {
                            // 定义存储要素选项信息的List
                            List<JSONObject> optionJsonList = new ArrayList<>();
                            for (AuditTaskOption auditTaskOption : auditTaskOptions) {
                                // 定义存储要素选项的Json
                                JSONObject optionJson = new JSONObject();
                                optionJson.put("optionname", auditTaskOption.getOptionname());
                                optionJson.put("optionguid", auditTaskOption.getRowguid());
                                optionJsonList.add(optionJson);
                            }
                            subElementJson.put("optionlist", optionJsonList); // 要素选项列表
                            subElementJson.put("elementquestion", auditTaskElement.getElementname()); // 要素问题
                            subElementJson.put("elementguid", auditTaskElement.getRowguid()); // 要素唯一标识
                            if (StringUtil.isNotBlank(auditTaskElement.getMultiselect())
                                    && ZwfwConstant.CONSTANT_STR_ZERO.equals(auditTaskElement.getMultiselect())) {
                                subElementJson.put("type", "radio");
                                subElementJson.put("multitype", "单选");
                            } else {
                                subElementJson.put("type", "checkbox");
                                subElementJson.put("multitype", "多选");
                            }
                            subElementJsonList.add(subElementJson);
                        }
                    }
                }
                // 1.3添加返回数据
            }
            dataJson.put("elementlist", subElementJsonList);
            log.info("=======结束调用getmaterialcase接口=======");

            return JsonUtils.zwdtRestReturn("1", "获取配置参数信息成功！", dataJson);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getmaterialcase接口参数：params【" + params + "】=======");
            log.info("=======getmaterialcase异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwfwAPPRestReturn("0", "获取配置参数信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取APP办件基础详情
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/submitmaterialcase", method = RequestMethod.POST)
    public String submitmaterialcase(@RequestBody String params, HttpServletRequest request) {
        try {
            log.info("=======开始调用submitmaterialcase接口=======");
            // 1、入参转化为JSON对象
            JSONObject json = JSON.parseObject(params);
//          JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject jsonObject = (JSONObject) json.get("params");
            String projectguid = jsonObject.getString("projectguid");// 办件guid
            String areacode = jsonObject.getString("areacode");// 区域编码
            // 1.3遍历所有optionlist获取所有optionguid
            if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                ZwfwRedisCacheUtil redisUtil = null;
                try {
                    redisUtil = new ZwfwRedisCacheUtil(false);
                    String status = redisUtil.getByString("scan_" + projectguid);

                    if (StringUtil.isNotBlank(status)) {
                        redisUtil.del("scan_" + projectguid);
                    }
                    // 清楚redis中的data数据
                    String data = redisUtil.getByString("data_" + projectguid);
                    if (StringUtil.isNotBlank(data)) {
                        redisUtil.del("data_" + projectguid);
                    }
                    redisUtil.putByString("data_" + projectguid, jsonObject);
                    redisUtil.putByString("scan_" + projectguid, ZwfwConstant.CONSTANT_INT_ONE, 300);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (redisUtil != null) {
                        redisUtil.close();
                    }
                }

            }
            log.info("=======结束调用submitmaterialcase接口=======");
            return JsonUtils.zwfwAPPRestReturn("1", "提交成功！", "");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======submitmaterialcase接口参数：params【" + params + "】=======");
            log.info("=======submitmaterialcase异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwfwAPPRestReturn("0", "提交失败：" + e.getMessage(), "");
        }

    }

    /**
     * 获取办件材料列表接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getmateriallist", method = RequestMethod.POST)
    public String getMateriallist(@RequestBody String params, HttpServletRequest request) {
        try {
            log.info("=======开始调用getmateriallist接口=======");
            // 1、入参转化为JSON对象
            JSONObject json = JSON.parseObject(params);
            // 定义返回JSON对象
            JSONObject dataJson = new JSONObject();
//          JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject jsonObject = (JSONObject) json.get("params");
            String projectguid = jsonObject.getString("projectguid");// 办件guid
            String areacode = jsonObject.getString("areacode");// 区域编码
            // 1.1根据办件标识和区域编码查询相应信息
            AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid(projectguid, areacode).getResult();
            List<Map<String, Object>> malist = new ArrayList<>();
            if (StringUtil.isNotBlank(auditProject.getTaskcaseguid())) {
                // 添加必须材料
                List<AuditTaskMaterial> useblelist = iAuditTaskMaterial
                        .getUsableMaterialListByTaskguid(auditProject.getTaskguid()).getResult();
                // 过滤非必须
                useblelist = useblelist.stream()
                        .filter(a -> Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES) == a.getNecessity())
                        .collect(Collectors.toList());
                for (AuditTaskMaterial auditTaskMaterial : useblelist) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("materialname", auditTaskMaterial.getMaterialname());
                    map.put("materialtype", iCodeItemsService.getItemTextByCodeName("材料提交方式",
                            String.valueOf(auditTaskMaterial.getSubmittype())));
                    map.put("materialguid", auditTaskMaterial.getMaterialid());
                    String page_num = "";
                    if (StringUtil.isNotBlank(auditTaskMaterial.getPage_num())) {
                        page_num = "（" + String.valueOf(auditTaskMaterial.getPage_num()) + "份）";
                    }
                    String pagename = "";
                    if (StringUtil.isNotBlank(auditTaskMaterial.get("materials_type"))) {
                        pagename = this.iCodeItemsService.getItemTextByCodeName("事项同步材料类型",
                                (String) auditTaskMaterial.get("materials_type"));
                    }
                    map.put("matreialnum", pagename + page_num);
                    malist.add(map);
                }

                List<AuditTaskMaterialCase> materiallist = iAuditTaskMaterialCase
                        .selectTaskMaterialCaseByCaseGuid(auditProject.getTaskcaseguid()).getResult();
                for (AuditTaskMaterialCase auditTaskMaterialCase : materiallist) {
                    Map<String, Object> map = new HashMap<>();
                    if (StringUtil.isNotBlank(auditTaskMaterialCase.getMaterialid())) {
                        AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                .selectTaskMaterialByTaskGuidAndMaterialId(auditProject.getTaskguid(),
                                        auditTaskMaterialCase.getMaterialid())
                                .getResult();
                        map.put("materialname", auditTaskMaterial.getMaterialname());
                        map.put("materialtype", iCodeItemsService.getItemTextByCodeName("材料提交方式",
                                String.valueOf(auditTaskMaterial.getSubmittype())));
                        String page_num = "";
                        if (StringUtil.isNotBlank(auditTaskMaterial.getPage_num())) {
                            page_num = "（" + String.valueOf(auditTaskMaterial.getPage_num()) + "份）";
                        }
                        String pagename = "";
                        if (StringUtil.isNotBlank(auditTaskMaterial.get("materials_type"))) {
                            pagename = this.iCodeItemsService.getItemTextByCodeName("事项同步材料类型",
                                    (String) auditTaskMaterial.get("materials_type"));
                        }
                        map.put("matreialnum", pagename + page_num);
                    }
                    map.put("materialguid", auditTaskMaterialCase.getMaterialid());
                    malist.add(map);
                }
            }

            log.info("=======结束调用getmateriallist接口=======");
            dataJson.put("elementanswers", malist);
            return JsonUtils.zwdtRestReturn("1", "获取材料列表成功！", dataJson);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getmateriallist接口参数：params【" + params + "】=======");
            log.info("=======getmateriallist异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwfwAPPRestReturn("0", "获取材料列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取办件材料列表接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    public String changeStatus(@RequestBody String params, HttpServletRequest request) {
        try {
            log.info("=======开始调用changeStatus接口=======");
            // 1、入参转化为JSON对象
            JSONObject json = JSON.parseObject(params);
            // 定义返回JSON对象
            JSONObject dataJson = new JSONObject();
//          JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject jsonObject = (JSONObject) json.get("params");
            String projectguid = jsonObject.getString("projectguid");// 办件guid
            String areacode = jsonObject.getString("areacode");// 区域编码
            if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                ZwfwRedisCacheUtil redisUtil = null;
                try {
                    redisUtil = new ZwfwRedisCacheUtil(false);
                    String status = redisUtil.getByString("scan_" + projectguid);
                    if (StringUtil.isNotBlank(status)) {
                        redisUtil.del("scan_" + projectguid);
                    }
                    redisUtil.putByString("scan_" + projectguid, ZwfwConstant.CONSTANT_INT_TWO, 300);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (redisUtil != null) {
                        redisUtil.close();
                    }
                }

            }
            log.info("=======结束调用changeStatus接口=======");
            return JsonUtils.zwdtRestReturn("1", "取消成功！", dataJson);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======changeStatus接口参数：params【" + params + "】=======");
            log.info("=======changeStatus异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwfwAPPRestReturn("0", "取消失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取办件材料列表接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/toendapply", method = RequestMethod.POST)
    public String toEndApply(@RequestBody String params, HttpServletRequest request) {
        try {
            log.info("=======开始调用changeStatus接口=======");
            // 1、入参转化为JSON对象
            JSONObject json = JSON.parseObject(params);
            // 定义返回JSON对象
            JSONObject dataJson = new JSONObject();
//          JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject jsonObject = (JSONObject) json.get("params");
            String projectguid = jsonObject.getString("projectguid");// 办件guid
            String areacode = jsonObject.getString("areacode");// 区域编码
            if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                ZwfwRedisCacheUtil redisUtil = null;
                try {
                    redisUtil = new ZwfwRedisCacheUtil(false);
                    String status = redisUtil.getByString("scan_" + projectguid);
                    if (StringUtil.isNotBlank(status)) {
                        redisUtil.del("scan_" + projectguid);
                    }
                    redisUtil.putByString("scan_" + projectguid, ZwfwConstant.CONSTANT_INT_TWO, 300);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (redisUtil != null) {
                        redisUtil.close();
                    }
                }

            }
            log.info("=======结束调用changeStatus接口=======");
            return JsonUtils.zwdtRestReturn("1", "取消成功！", dataJson);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======changeStatus接口参数：params【" + params + "】=======");
            log.info("=======changeStatus异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwfwAPPRestReturn("0", "取消失败：" + e.getMessage(), "");
        }
    }

    public void setData(String projectguid, String params, String areacode) {
        // 1、入参转化为JSON对象
        JSONObject json = JSON.parseObject(params);
        JSONObject jsonObject = (JSONObject) json.get("params");
        // 1.1获取所有optiolist集合
        List<Map<String, Object>> ja = (List<Map<String, Object>>) jsonObject.get("selectedoptions");
        // 1.2根据办件标识和区域编码查询相应信息
        AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid(projectguid, areacode).getResult();
        String opguids = null;// 讲所有选项guid保存在opguids，用于保存在taskcase中去
        List<String> optionlist = new ArrayList<>();
        for (int s = 0; s < ja.size(); s++) {
            List<Map<String, Object>> maplist = (List<Map<String, Object>>) ja.get(s).get("optionlist");
            for (int i = 0; i < maplist.size(); i++) {
                // 判断是否为最底层选项
                // 是则添加到optionlist中去
                optionlist.add(maplist.get(i).get("optionguid").toString());
                if (StringUtil.isBlank(opguids)) {
                    opguids = maplist.get(i).get("optionguid") + ";";
                } else {
                    opguids += maplist.get(i).get("optionguid") + ";";
                }
            }
        }
        // 1.4根据所有optionguid值获取所有材料值
        List<String> rtnguids = new ArrayList<>();// 材料guid列表
        if (optionlist.size() > 0) {
            String allmaterials = null;
            // 根据传过来的guids列表进行遍历，讲选项guid保存在json中
            for (String guid : optionlist) {
                if (StringUtil.isNotBlank(guid)) {
                    AuditTaskOption auditTaskOption = iAuditTaskOptionService.find(guid).getResult();
                    if (auditTaskOption != null && StringUtil.isNotBlank(auditTaskOption.getMaterialids())) {
                        if (StringUtil.isBlank(allmaterials)) {
                            allmaterials = auditTaskOption.getMaterialids();
                        } else {
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
        // 2 添加情形
        if (StringUtil.isNotBlank(auditProject.getTaskguid())) {
            // 2.1根据所查询出来的办件信息，查询事项信息
            AuditTask auditTask = iAuditTask.selectTaskByRowGuid(auditProject.getTaskguid()).getResult();
            AuditTaskCase auditTaskCase = new AuditTaskCase();
            auditTaskCase.setOperatedate(new Date());
            auditTaskCase.setRowguid(UUID.randomUUID().toString());
            auditTaskCase.setTaskguid(auditProject.getTaskguid());
            auditTaskCase.setTaskid(auditTask.getTask_id());
            auditTaskCase.setOrdernum(0);
            // 判断是否为常用情形 1：是 0：否
            auditTaskCase.setIs_oldcase(0);
            auditTaskCase.setSelectedoptions(opguids);
            auditTaskCaseImpl.addAuditTaskCase(auditTaskCase);
            // 2.2保存selectedoption对象到
            AuditTaskSelectedOption auditTaskSelectedOption = new AuditTaskSelectedOption();
            auditTaskSelectedOption.setInsertdate(new Date());
            auditTaskSelectedOption.setRowguid(UUID.randomUUID().toString());
            auditTaskSelectedOption.setProjectguid(projectguid);
            auditTaskSelectedOption.setSelectedoptions(params);
            iAuditTaskSelectedOptionService.update(auditTaskSelectedOption);
            // 2.3获取所有材料对象列表
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("taskGuid", auditProject.getTaskguid());
            sql.eq("necessity", "10");
            List<AuditTaskMaterial> olist = iAuditTaskMaterial.selectMaterialListByCondition(sql.getMap()).getResult();
            if (rtnguids.size() > 0) {
                // 查询到的材料guid列表进行遍历，添加到关系表中
                for (String guid : rtnguids) {
                    AuditTaskMaterialCase auditMaterialcaseMaterial = new AuditTaskMaterialCase();
                    auditMaterialcaseMaterial.setOperatedate(new Date());
                    auditMaterialcaseMaterial.setRowguid(UUID.randomUUID().toString());
                    auditMaterialcaseMaterial.setTaskcaseguid(auditTaskCase.getRowguid());
                    auditMaterialcaseMaterial.setMaterialid(guid);
                    auditMaterialcaseMaterial.setIs_nooption(1);
                    AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                            .selectMaterialByTaskIdAndMaterialId(auditTask.getTask_id(), guid).getResult();
                    if (auditTaskMaterial != null) {
                        auditMaterialcaseMaterial.setMaterialguid(auditTaskMaterial.getRowguid());
                        auditMaterialcaseMaterial.setNecessity(auditTaskMaterial.getNecessity());
                    }
                    auditMaterialcaseMaterial.setTaskguid(auditTaskCase.getTaskguid());
                    auditMaterialcaseMaterial.setTaskid(auditTask.getTask_id());
                    iAuditTaskMaterialCase.addAuditTaskMaterialCase(auditMaterialcaseMaterial);
                }
            }
            // 查询所有未关联选项的材料id，添加到情形中去
            if (olist.size() > 0) {
                for (AuditTaskMaterial auditTaskMaterial : olist) {
                    AuditTaskMaterialCase auditMaterialcaseMaterial = new AuditTaskMaterialCase();
                    auditMaterialcaseMaterial.setOperatedate(new Date());
                    auditMaterialcaseMaterial.setRowguid(UUID.randomUUID().toString());
                    auditMaterialcaseMaterial.setTaskcaseguid(auditTaskCase.getRowguid());
                    auditMaterialcaseMaterial.setMaterialid(auditTaskMaterial.getMaterialid());
                    auditMaterialcaseMaterial.setIs_nooption(0);
                    auditMaterialcaseMaterial.setMaterialguid(auditTaskMaterial.getRowguid());
                    auditMaterialcaseMaterial.setNecessity(auditTaskMaterial.getNecessity());
                    auditMaterialcaseMaterial.setTaskguid(auditTaskCase.getTaskguid());
                    auditMaterialcaseMaterial.setTaskid(auditTask.getTask_id());
                    iAuditTaskMaterialCase.addAuditTaskMaterialCase(auditMaterialcaseMaterial);
                }
            }
        }
    }

}
