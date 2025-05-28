package com.epoint.auditsp;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.jn.xf.XfprojectClientHandle;
import com.epoint.zbxfdj.controller.TsProjectDataRest;

@RestController
@RequestMapping("/jnauditsp")
public class JNAuditSpController
{
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    private IAuditProject iauditproject;
    @Autowired
    private IAuditProjectMaterial iauditprojectmaterial;
    @Autowired
    private IAuditSpIMaterial iauditspimaterial;
    @Autowired
    private IAuditTaskMaterial iaudittaskmaterial;
    @Autowired
    private IAttachService iAttachService;

    @Autowired
    private TsProjectDataRest tsprojectdatarest;

    @RequestMapping(value = "/buzhengWc", method = RequestMethod.POST)
    public String buzhengWc(@RequestBody String params) {
        log.info("=======开始调用buzhengWc接口=======");
        try {
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            // 1、接口的入参转化为JSON对象·
            JSONObject obj = jsonObject.getJSONObject("params");
            // 办件唯一标识
            String subappguid = obj.getString("subappguid");
            buzhengwc(subappguid);
            log.info("=======结束调用buzhengWc接口=======");
            return JsonUtils.zwdtRestReturn("1", "网厅补正完成后事件接口！", "");
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======buzhengWc接口参数：params【" + params + "】=======");
            log.info("=======buzhengWc异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "buzhengWc异常信息：" + e.getMessage(), "");
        }
    }

    public void buzhengwc(String subappguid) {
        // 查询补正的办件
        SqlConditionUtil sqlc = new SqlConditionUtil();
        sqlc.eq("subappguid", subappguid);
        sqlc.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_DBB));
        sqlc.setSelectFields("rowguid,areacode,projectname,subappguid,taskguid");
        List<AuditProject> projectlist = iauditproject.getAuditProjectListByCondition(sqlc.getMap()).getResult();
        for (AuditProject auditProject : projectlist) {
            // 判断是否是消防事项
            if (XfprojectClientHandle.isxf(auditProject.getProjectname())) {
                log.info("=======查询到消防事项进行材料更新=======");
                // 更新办件状态
                buzhengXfProject(auditProject);
            }
        }
    }

    public void buzhengXfProject(AuditProject auditproject) {
        SqlConditionUtil sUtil = new SqlConditionUtil();
        sUtil.eq("auditstatus", ZwfwConstant.Material_AuditStatus_DBZ);
        sUtil.eq("projectguid", auditproject.getRowguid());
        List<AuditProjectMaterial> bzAuditProjectMaterialList = iauditprojectmaterial
                .selectProjectMaterialByCondition(sUtil.getMap()).getResult();
        List<AuditTaskMaterial> materiallist = iaudittaskmaterial
                .getUsableMaterialListByTaskguid(auditproject.getTaskguid()).getResult();
        Map<String, String> guid_id = materiallist.stream()
                .collect(Collectors.toMap(AuditTaskMaterial::getRowguid, AuditTaskMaterial::getMaterialid));
        log.info("=======处理材料=======");

        for (AuditProjectMaterial projectMaterial : bzAuditProjectMaterialList) {
            if (projectMaterial != null) {
                AuditSpIMaterial spIMaterial = null;
                if (StringUtil.isNotBlank(projectMaterial.getSharematerialiguid())) {
                    spIMaterial = iauditspimaterial.getSpIMaterialByMaterialGuid(auditproject.getSubappguid(),
                            projectMaterial.getSharematerialiguid()).getResult();
                }
                else {
                    spIMaterial = iauditspimaterial.getSpIMaterialByMaterialGuid(auditproject.getSubappguid(),
                            guid_id.get(projectMaterial.getTaskmaterialguid())).getResult();
                }
                if (spIMaterial != null) {
                    log.info("=======删除材料=======");
                    // 复制附件之前，需要先将办件材料的附件清掉
                    iAttachService.deleteAttachByGuid(projectMaterial.getCliengguid());
                    // 将工改材料的附件复制一份到办件材料
                    log.info("=======复制材料=======");
                    iAttachService.copyAttachByClientGuid(spIMaterial.getCliengguid(), null, null,
                            projectMaterial.getCliengguid());
                    // 更新办件材料状态为 已补正
                    log.info("=======更新材料=======");
                    iauditprojectmaterial.updateProjectMaterialAuditStatus(projectMaterial.getRowguid(),
                            Integer.parseInt(ZwfwConstant.Material_AuditStatus_YBZ), auditproject.getRowguid());
                    iauditprojectmaterial.updateStatus(projectMaterial.getRowguid(), projectMaterial.getProjectguid(),
                            Integer.valueOf(spIMaterial.getStatus()));
                }
            }
        }
        // todo 调用消防对接的接口
        tsprojectdatarest.sendBZlist(auditproject.getRowguid());
    }

}
