package com.epoint.wsxznsb.evaluation;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

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
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;

@RestController
@RequestMapping("/wsxevaluation")
public class WsxEvaluationRestController
{
    @Autowired
    private ICodeItemsService codeitemsservice;

    @Autowired
    private IAuditProject auditprojectservice;

    @Autowired
    private IAuditProjectMaterial auditprojectmaterialservice;
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 获取办件详情
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/getProjectDetail", method = RequestMethod.POST)
    public String getProjectDetail(@RequestBody String params) {
        try {
            JSONObject json = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String projectguid = obj.getString("projectguid");
            String areacode = obj.getString("areacode");
            if (areacode.length() > 6) {
                areacode = areacode.substring(0, 6);
            }
            JSONObject dataJson = new JSONObject();

            String fields = " rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,certnum,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,address,contactcertnum,remark,certtype,applyertype,legal ";
            AuditProject auditProject = auditprojectservice.getAuditProjectByRowGuid(fields, projectguid, areacode)
                    .getResult();
            if (auditProject != null) {
                dataJson.put("projectname", auditProject.getProjectname());// 办件名称
                dataJson.put("applyername", auditProject.getApplyername());// 申请人/申请单位
                dataJson.put("applyercertnum", auditProject.getCertnum());// 申请人证照编号
                dataJson.put("legal", auditProject.getLegal());// 法人代表
                dataJson.put("contactperson", auditProject.getContactperson());// 联系人
                dataJson.put("contactmobile", auditProject.getContactmobile());// 联系人手机
                dataJson.put("contactphone", auditProject.getContactphone());// 联系人电话
                dataJson.put("contactcertnum", auditProject.getContactcertnum());// 联系人身份证
                dataJson.put("contactpostcode", auditProject.getContactpostcode());// 邮编
                dataJson.put("address", auditProject.getAddress());// 地址
                dataJson.put("contactfax", auditProject.getContactfax());// 传真
                dataJson.put("contactemail", auditProject.getContactemail());// 电子邮件
                dataJson.put("certtype", codeitemsservice.getItemTextByCodeName("申请人用来唯一标识的证照类型",
                        String.valueOf(auditProject.getCerttype())));// 证照类型
                dataJson.put("applyertype",
                        codeitemsservice.getItemTextByCodeName("申请人类型", String.valueOf(auditProject.getApplyertype())));// 申请人类型
                SqlConditionUtil conditionsql = new SqlConditionUtil();
                conditionsql.eq("projectguid", projectguid);
                List<JSONObject> materialList = new ArrayList<JSONObject>();
                List<AuditProjectMaterial> auditProjectMaterials = auditprojectmaterialservice
                        .selectProjectMaterialByCondition(conditionsql.getMap()).getResult();
                for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterials) {
                    JSONObject materialJson = new JSONObject();
                    materialJson.put("projectmaterialguid", auditProjectMaterial.getRowguid());
                    materialJson.put("projectmaterialname", auditProjectMaterial.getTaskmaterial());
                    materialJson.put("projectmaterialstatus", codeitemsservice.getItemTextByCodeName("材料提交状态",
                            String.valueOf(auditProjectMaterial.getStatus())));// 材料提交状态
                    materialList.add(materialJson);
                }

                dataJson.put("materiallist", materialList);
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);

        }
        catch (Exception e) {
            log.info(e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
}
