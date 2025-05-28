package com.epoint.auditselfservice.auditselfservicerest.common;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditconfig.inter.IAuditOrgaConfig;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditqueue.auditqueueuserinfo.inter.IAuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditznsbselfmachineproject.inter.IAuditZnsbSelfmachineproject;
import com.epoint.basic.auditqueue.auditznsbzzsbdemo.inter.IAuditZnsbZzsbDemoService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskCase;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;

@RestController
@RequestMapping("/gxqselfserviceproject")
public class GxqProjectRestController
{
    @Autowired
    private IAuditOrgaServiceCenter servicecenterservice;

    @Autowired
    private IAttachService attachservice;

    @Autowired
    private IAuditProject projectservice;

    @Autowired
    private IAuditOrgaWindowYjs windowservice;

    @Autowired
    private ICodeItemsService codeitemsservice;

    @Autowired
    private IAuditOnlineIndividual individualservice;

    @Autowired
    private IHandleProject handleProjectservice;

    @Autowired
    private IAuditTask taskservice;

    @Autowired
    private IAuditOnlineProject onlineProjectservice;

    @Autowired
    private IAuditProjectMaterial projectMaterialservice;

    @Autowired
    private IAuditTaskExtension taskExtensionservice;

    @Autowired
    private IAuditTaskMaterial taskMaterialservice;

    @Autowired
    private IAttachService attachService;

    @Autowired
    private IWFInstanceAPI9 wfinstanceapi;

    @Autowired
    private IAuditTaskResult taskresultservice;

    @Autowired
    private IAuditOrgaConfig auditconfigservice;

    @Autowired
    private IAuditTaskCase auditTaskCaseService;

    @Autowired
    private IAuditTaskMaterialCase auditTaskMaterialCaseService;

    @Autowired
    private IHandleConfig handleConfigservice;

    @Autowired
    private IAuditZnsbSelfmachineproject auditZnsbSelfmachineprojectService;

    @Autowired
    private IAuditZnsbZzsbDemoService auditZnsbZzsbDemoService;

    @Autowired
    private IAuditQueueUserinfo userinfoservice;

    @Autowired
    private IAuditOnlineRegister registerservice;

    @Autowired
    private IMessagesCenterService messageCenterService;

    @Autowired
    private IUserService userservice;

    @Autowired
    private IRoleService roleservice;

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 办件操作API
     */
    @Autowired
    private IAuditProjectOperation iAuditProjectOperation;

    /**
     * 获取多情形材料列表(办件申报须知多情形下调用)
     * 
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getTaskCaseMaterialsByCaseguid", method = RequestMethod.POST)
    public String getTaskCaseMaterialsByCaseguid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskCaseMaterialsByCaseguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");

            JSONObject obj = (JSONObject) jsonObject.get("params");
            // 1.1、获取事项多情形标识
            String taskCaseGuid = obj.getString("faqcaseguid");
            String projectguid = obj.getString("projectguid");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");
            String areacode = obj.getString("areacode");
            // 1.2、获取事项标识
            // String taskGuid = obj.getString("taskguid");
            List<JSONObject> materialJsonList = new ArrayList<JSONObject>();
            // 2、获取多情形材料列表
            String isbigshow = "0";
            String fields = " rowguid,taskguid";
            AuditProject auditProject = projectservice.getAuditProjectByRowGuid(fields, projectguid, areacode)
                    .getResult();
            if (auditProject != null) {

                if (StringUtil.isNotBlank(auditProject.getTaskguid())) {
                    AuditTask task = taskservice.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
                    if (task != null) {
                        isbigshow = task.getStr("isbigshow");
                    }
                }
            }

            if (StringUtil.isNotBlank(taskCaseGuid)) {
                AuditTaskCase auditTaskCase = auditTaskCaseService.getAuditTaskCaseByRowguid(taskCaseGuid).getResult();
                String taskGuid = "";
                if (auditTaskCase != null) {
                    taskGuid = auditTaskCase.getTaskguid();
                }
                SqlConditionUtil sql = new SqlConditionUtil();
                if (StringUtil.isNotBlank(taskGuid)) {
                    sql.eq("taskGuid", taskGuid);
                }
                sql.eq("necessity", ZwfwConstant.NECESSITY_SET_YES);
                // 获取事项材料表中的必要材料
                List<AuditTaskMaterial> auditTaskMaterials = taskMaterialservice
                        .selectMaterialListByCondition(sql.getMap()).getResult();
                for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                    if (auditTaskMaterial != null) {
                        materialJsonList.add(this.getMaterialJson(auditTaskMaterial, projectguid));
                    }
                }
                // 获取情形材料（情形表中仅有非必要材料）
                List<AuditTaskMaterialCase> auditTaskMaterialCases = auditTaskMaterialCaseService
                        .selectTaskMaterialCaseByCaseGuid(taskCaseGuid).getResult();
                for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                    if (auditTaskMaterialCase != null) {
                        // int necessity = auditTaskMaterialCase.getNecessity();
                        // 3、当内网不勾选材料情形时，默认不显示该材料
                        if (ZwdtConstant.INT_NO == auditTaskMaterialCase.getNecessity()) {
                            continue;
                        }
                        // 4、获取情形与材料关系获取事项材料信息
                        AuditTaskMaterial auditTaskMaterial = taskMaterialservice
                                .getAuditTaskMaterialByRowguid(auditTaskMaterialCase.getMaterialguid()).getResult();
                        if (auditTaskMaterial != null) {
                            // 兼容旧版，情形材料中的必要材料不返回
                            if (ZwfwConstant.NECESSITY_SET_NO.equals(auditTaskMaterial.getNecessity().toString())) {
                                materialJsonList.add(this.getMaterialJson(auditTaskMaterial, projectguid));
                            }
                        }
                    }
                }
            }
            // 5、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            List<String> taskmaterialarray = new ArrayList<String>();
            List<String> statusarray = new ArrayList<String>();
            // 截取对应页面的部门list数据
            if (StringUtil.isNotBlank(currentPage) && StringUtil.isNotBlank(pageSize)) {
                int firstint = Integer.parseInt(currentPage) * Integer.parseInt(pageSize);
                int endint = (firstint + Integer.parseInt(pageSize)) >= materialJsonList.size()
                        ? materialJsonList.size() : (firstint + Integer.parseInt(pageSize));
                List<JSONObject> rtnlist = materialJsonList.subList(firstint, endint);
                dataJson.put("materiallist", rtnlist);
                dataJson.put("totalcount", StringUtil.getNotNullString(materialJsonList.size()));
                // 拼接所有材料guid和材料状态
                for (JSONObject json : materialJsonList) {
                    taskmaterialarray.add(json.getString("taskmaterialguid"));
                    statusarray.add(json.getString("status"));
                }
                if (!materialJsonList.isEmpty()) {

                    dataJson.put("taskmaterialarray", taskmaterialarray);
                    dataJson.put("statusarray", statusarray);
                }
            }
            else {
                dataJson.put("materiallist", materialJsonList);
            }
            dataJson.put("isbigshow", isbigshow);
            log.info("=======结束调用getTaskCaseMaterialsByCaseguid接口=======");
            return JsonUtils.zwdtRestReturn("1", "多情形材料信息获取成功", dataJson.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======declareProjectNotice接口参数：params【" + params + "】=======");
            log.info("=======declareProjectNotice异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "申报须知信息获取失败：" + e.getMessage(), "");
        }
    }

    public JSONObject getMaterialJson(AuditTaskMaterial auditTaskMaterial, String projectguid) {
        JSONObject materialJson = new JSONObject();
        AuditProjectMaterial auditProjectMaterial = projectMaterialservice
                .selectProjectMaterialByMaterialGuid(projectguid, auditTaskMaterial.getRowguid()).getResult();
        materialJson.put("projectmaterialguid", auditProjectMaterial.getRowguid());// 材料标识
        materialJson.put("projectmaterialname", auditTaskMaterial.getMaterialname());// 材料名称
        materialJson.put("status", auditProjectMaterial.getStatus());// 材料状态
        materialJson.put("taskmaterialguid", auditTaskMaterial.getRowguid());// 事项材料主键
        materialJson.put("materialid", auditTaskMaterial.getMaterialid());// 7.10版本新增返回值:
        materialJson.put("bigshowtype", StringUtil.getNotNullString(auditTaskMaterial.getStr("bigshowtype"))); // 事项材料ID
        int necessity = auditTaskMaterial.getNecessity();
        if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_NO)) {
            materialJson.put("necessary", "0");// 是否必须
        }
        else if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES)) {
            // 不考虑纸质必填的
            if (WorkflowKeyNames9.SubmitType_PaperSubmit == Integer.parseInt(auditTaskMaterial.getSubmittype())) {
                materialJson.put("necessary", "0");
            }
            else {
                materialJson.put("necessary", "1");// 是否必须
            }
        }
        // 首先判断情形，若存在情形，则所有材料为必须材料
        materialJson.put("necessary", "1");

        materialJson.put("submittype", auditTaskMaterial.getSubmittype());
        materialJson.put("clientguid", auditProjectMaterial.getCliengguid());
        materialJson.put("submitcount", attachService.getAttachCountByClientGuid(auditProjectMaterial.getCliengguid()));
        // 获取每个材料对应的附件guid
        List<FrameAttachInfo> attachInfoList = attachService
                .getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
        List<String> attachguidList = new ArrayList<String>();
        for (FrameAttachInfo attachinfo : attachInfoList) {
            attachguidList.add(attachinfo.getAttachGuid());
        }
        if (!attachguidList.isEmpty()) {
            materialJson.put("attachguid", StringUtil.join(attachguidList, ";"));
        }
        else {
            materialJson.put("attachguid", "");
        }
        // 获取排序值用于排序
        materialJson.put("ordernum", auditTaskMaterial.getOrdernum());
        return materialJson;
    }

    /**
     * 获取办件材料信息的接口
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/getMaterialList", method = RequestMethod.POST)
    public String getInitMaterialList(@RequestBody String params) {
        try {

            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(jsonObject.getString("token"));
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String projectGuid = obj.getString("projectguid");
            String type = obj.getString("type"); // 0:办件所有材料 1：需要补正材料 2:原件提交
            String caseguid = obj.getString("caseguid");
            String areacode = obj.getString("areacode");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");
            // 添加参数
            String usestore = obj.getString("usestore");

            String taskmaterialguids = "";
            // 2、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("projectguid", projectGuid);

            if ("0".equals(type)) {
                sql.eq("auditstatus", ZwfwConstant.Material_AuditStatus_WTJ);
            }
            else if ("1".equals(type)) {
                sql.eq("auditstatus", ZwfwConstant.Material_AuditStatus_DBZ);
            }
            else if ("2".equals(type)) {
                //
            }
            String isbigshow = "0";
            // 如果情形不为空，判断当前办件的情形跟传入的是否一致，不一致则更新
            if (StringUtil.isNotBlank(caseguid)) {
                String fields = " rowguid,areacode,taskcaseguid,taskguid";
                AuditProject auditProject = projectservice.getAuditProjectByRowGuid(fields, projectGuid, areacode)
                        .getResult();
                if (auditProject != null) {
                    if (!caseguid.equals(auditProject.getTaskcaseguid())) {
                        auditProject.setTaskcaseguid(caseguid);
                        projectservice.updateProject(auditProject);

                    }
                    if (StringUtil.isNotBlank(auditProject.getTaskguid())) {
                        AuditTask task = taskservice.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
                        if (task != null) {
                            isbigshow = task.getStr("isbigshow");
                        }
                    }
                }

                List<AuditTaskMaterialCase> auditTaskMaterialCases = auditTaskMaterialCaseService
                        .selectTaskMaterialCaseByCaseGuid(caseguid).getResult();
                StringBuilder bld = new StringBuilder();
                for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                    bld.append("'" + auditTaskMaterialCase.getMaterialguid() + "',");

                }
                taskmaterialguids = bld.toString();
                if (StringUtil.isNotBlank(taskmaterialguids)) {
                    sql.in("taskmaterialguid", taskmaterialguids.substring(0, taskmaterialguids.length() - 1));
                }
            }
            // 3、获取办件材料
            List<AuditProjectMaterial> ProjectMateriallist = projectMaterialservice
                    .selectProjectMaterialByCondition(sql.getMap()).getResult();
            int totalcount = ProjectMateriallist.size();
            List<JSONObject> projectJsonlist = new ArrayList<JSONObject>();
            for (AuditProjectMaterial auditProjectMaterial : ProjectMateriallist) {
                AuditTaskMaterial auditTaskMaterial = taskMaterialservice
                        .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid()).getResult();

                JSONObject materialJson = new JSONObject();
                materialJson.put("projectmaterialguid", auditProjectMaterial.getRowguid());// 材料标识
                materialJson.put("projectmaterialname", auditTaskMaterial.getMaterialname());// 材料名称
                materialJson.put("status", auditProjectMaterial.getStatus());// 材料状态
                materialJson.put("taskmaterialguid", auditTaskMaterial.getRowguid());// 事项材料主键
                materialJson.put("materialid", auditTaskMaterial.getMaterialid());// 7.10版本新增返回值:
                materialJson.put("bigshowtype", StringUtil.getNotNullString(auditTaskMaterial.getStr("bigshowtype")));
                int necessity = auditTaskMaterial.getNecessity();
                if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_NO)) {
                    materialJson.put("necessary", "0");// 是否必须
                }
                else if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES)) {
                    // 不考虑纸质必填的
                    if (WorkflowKeyNames9.SubmitType_PaperSubmit == Integer
                            .parseInt(auditTaskMaterial.getSubmittype())) {
                        materialJson.put("necessary", "0");
                    }
                    else {
                        materialJson.put("necessary", "1");// 是否必须
                    }
                }
                materialJson.put("submittype", auditTaskMaterial.getSubmittype());
                materialJson.put("clientguid", auditProjectMaterial.getCliengguid());
                materialJson.put("submitcount",
                        attachService.getAttachCountByClientGuid(auditProjectMaterial.getCliengguid()));
                // 获取每个材料对应的附件guid
                List<FrameAttachInfo> attachInfoList = attachService
                        .getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
                List<String> attachguidList = new ArrayList<String>();
                for (FrameAttachInfo attachinfo : attachInfoList) {
                    attachguidList.add(attachinfo.getAttachGuid());
                }
                if (!attachguidList.isEmpty()) {
                    materialJson.put("attachguid", StringUtil.join(attachguidList, ";"));
                }
                else {
                    materialJson.put("attachguid", "");
                }
                // 获取排序值用于排序
                materialJson.put("ordernum",
                        StringUtil.isBlank(auditTaskMaterial.getOrdernum()) ? 0 : auditTaskMaterial.getOrdernum());
                if (StringUtil.isBlank(usestore)
                        || ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT == auditProjectMaterial.getStatus()) {
                    projectJsonlist.add(materialJson);
                }
                else {
                    totalcount--;
                }
            }

            // 先排序
            Collections.sort(projectJsonlist, new MyComparator());
            // 再分页
            List<JSONObject> pageJsonlist = Page(projectJsonlist, Integer.parseInt(pageSize),
                    Integer.parseInt(currentPage));
            dataJson.put("materiallist", pageJsonlist);
            dataJson.put("isbigshow", isbigshow);
            dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));
            return JsonUtils.zwdtRestReturn("1", "获取初始化材料列表成功", dataJson);

        }
        catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "获取初始化材料列表失败：" + e, "");
        }
    }

    // 排序
    public class MyComparator implements Comparator<JSONObject>
    {
        @Override
        public int compare(JSONObject o1, JSONObject o2) {
            String key1 = o1.getString("ordernum");
            String key2 = o2.getString("ordernum");
            int int1 = Integer.parseInt(key1);
            int int2 = Integer.parseInt(key2);

            return int2 - int1;
        }
    }

    // 分页
    public static List<JSONObject> Page(List<JSONObject> dataList, int pageSize, int currentPage) {
        List<JSONObject> currentPageList = new ArrayList<>();
        if (dataList != null && !dataList.isEmpty()) {
            int currIdx = (currentPage >= 1 ? currentPage * pageSize : 0);
            for (int i = 0; i < pageSize && i < dataList.size() - currIdx; i++) {
                JSONObject data = dataList.get(currIdx + i);
                currentPageList.add(data);
            }
        }
        return currentPageList;
    }

}
