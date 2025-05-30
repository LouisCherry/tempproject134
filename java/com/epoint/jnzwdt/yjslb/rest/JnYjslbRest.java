package com.epoint.jnzwdt.yjslb.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditsp.auditspapplymaterial.api.IAuditSpApplymaterialService;
import com.epoint.auditsp.auditspapplymaterial.api.entity.AuditSpApplymaterial;
import com.epoint.auditsp.auditspjointitem.api.IAuditSpJointitemService;
import com.epoint.auditsp.auditspjointitem.api.entity.AuditSpJointitem;
import com.epoint.auditsp.auditsppolicy.api.IAuditSpPolicyService;
import com.epoint.auditsp.auditsppolicy.api.entity.AuditSpPolicy;
import com.epoint.auditsp.auditspresultmaterial.api.IAuditSpResultmaterialService;
import com.epoint.auditsp.auditspresultmaterial.api.entity.AuditSpResultmaterial;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditspsharematerial.domain.AuditSpShareMaterial;
import com.epoint.basic.auditsp.auditspsharematerial.inter.IAuditSpShareMaterial;
import com.epoint.basic.auditsp.auditspsharematerialrelation.domain.AuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditspsharematerialrelation.inter.IAuditSpShareMaterialRelation;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.jnzwdt.jnggtask.api.IJnAuditSpBaseTaskService;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import com.epoint.xmz.jgtaskmaterialrelation.api.IJgTaskmaterialrelationService;
import com.epoint.xmz.jgtaskmaterialrelation.api.entity.JgTaskmaterialrelation;
import com.epoint.yjs.yjszn.api.IYjsZnService;
import com.epoint.yjs.yjszn.api.entity.YjsZn;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/jnyjslb")
public class JnYjslbRest {
    private final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;

    @Autowired
    private IAttachService iAttachService;

    @Autowired
    private IAuditSpApplymaterialService iAuditSpApplymaterialService;

    @Autowired
    private ICodeItemsService iCodeItemsService;

    @Autowired
    private IAuditSpJointitemService iAuditSpJointitemService;

    @Autowired
    private IAuditSpResultmaterialService iAuditSpResultmaterialService;

    @Autowired
    private IAuditSpPolicyService iAuditSpPolicyService;

    @Autowired
    private IYjsZnService iYjsZnService;

    /**
     * 获取一件事联办的办事指南接口
     */
    @RequestMapping(value = "/getDetail", method = RequestMethod.POST)
    public String getDetail(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject paramsObj = jsonObject.getJSONObject("params");
                String businessguid = paramsObj.getString("businessguid");

                JSONObject custom = new JSONObject();
                if (StringUtil.isNotBlank(businessguid)) {
                    // 根据businessguid从数据库中获取AuditSpBusiness实体信息，该实体对应一件事联办的业务相关数据
                    AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessguid).getResult();
                    if (auditSpBusiness != null) {
                        custom.putAll(auditSpBusiness);

                        // 办理流程图片地址
                        String taskoutimg = ""; // 办理流程图地址
                        String taskOutImgGuid = auditSpBusiness.getTaskoutimgguid();
                        if (StringUtil.isNotBlank(taskOutImgGuid)) {
                            FrameAttachStorage frameAttachStorage = iAttachService.getAttach(taskOutImgGuid);
                            if (frameAttachStorage != null) {
                                taskoutimg = WebUtil.getRequestCompleteUrl(request)
                                        + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                        + frameAttachStorage.getAttachGuid();
                            }
                        }
                        custom.put("flowimg", taskoutimg);


                        // 办理指南
                        String manageguid = auditSpBusiness.getStr("management");
                        if(StringUtils.isNotBlank(manageguid)){
                            FrameAttachInfo frameAttachInfos = iAttachService.getAttachInfoDetail(manageguid);
                            JSONArray blznArray = new JSONArray();
                            if (frameAttachInfos != null) {
                                JSONObject newBlzn = new JSONObject();
                                newBlzn.put("attachurl",
                                        WebUtil.getRequestCompleteUrl(request)
                                                + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                                + frameAttachInfos.getAttachGuid());
                                newBlzn.put("attachname", frameAttachInfos.getAttachFileName());
                                blznArray.add(newBlzn);

                            }
                            custom.put("blzn", blznArray);
                        }




                        // 申报材料
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append("select * from audit_sp_applymaterial where businessguid=? order by ordernum desc");
                       List<AuditSpApplymaterial> auditSpApplymaterials =  iAuditSpApplymaterialService.findList(stringBuffer.toString(),auditSpBusiness.getRowguid());
                       if(auditSpApplymaterials != null && auditSpApplymaterials.size() > 0) {
                           for (AuditSpApplymaterial auditSpApplymaterial : auditSpApplymaterials) {
                               String material_type = iCodeItemsService.getItemTextByCodeName("国标_材料类型",auditSpApplymaterial.getMaterial_type());
                               String material_form = iCodeItemsService.getItemTextByCodeName("国标_材料形式",auditSpApplymaterial.getMaterial_form());
                               String source_type = iCodeItemsService.getItemTextByCodeName("来源渠道",auditSpApplymaterial.getSource_type());
                               auditSpApplymaterial.setMaterial_type(material_type);
                               auditSpApplymaterial.setMaterial_form(material_form);
                               auditSpApplymaterial.setSource_type(source_type);
                               if(StringUtils.isNotBlank(auditSpApplymaterial.getExample_guid())){
                                   List<FrameAttachInfo> frameAttachInfos1 = iAttachService.getAttachInfoListByGuid(auditSpApplymaterial.getExample_guid());
                                   if (frameAttachInfos1 != null && frameAttachInfos1.size() > 0) {
                                       auditSpApplymaterial.put("templateurl",
                                               WebUtil.getRequestCompleteUrl(request)
                                                       + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                                       + frameAttachInfos1.get(0).getAttachGuid());
                                   }
                               }


                           }
                           custom.put("applicationlist", auditSpApplymaterials);
                       }


                        // 联办事项
                        StringBuffer stringBuffer1 = new StringBuffer();
                        stringBuffer1.append("select * from audit_sp_jointitem where businessguid=?");
                        List<AuditSpJointitem> auditSpJointitems =  iAuditSpJointitemService.findList(stringBuffer1.toString(),auditSpBusiness.getRowguid());
                        if(auditSpJointitems != null && auditSpJointitems.size() > 0) {
                            custom.put("linkmatter", auditSpJointitems);
                        }

                        // 结果材料
                        StringBuffer stringBuffer2 = new StringBuffer();
                        stringBuffer2.append("select * from audit_sp_resultmaterial where businessguid=? order by ordernum desc");
                        List<AuditSpResultmaterial> auditSpResultmaterials =  iAuditSpResultmaterialService.findList(stringBuffer2.toString(),auditSpBusiness.getRowguid());
                        if(auditSpResultmaterials != null && auditSpResultmaterials.size() > 0) {
                            for (AuditSpResultmaterial auditSpResultmaterial : auditSpResultmaterials) {
                                if(StringUtils.isNotBlank(auditSpResultmaterial.getResultguid())){
                                    List<FrameAttachInfo> frameAttachInfos1 = iAttachService.getAttachInfoListByGuid(auditSpResultmaterial.getResultguid());
                                    if (frameAttachInfos1 != null && frameAttachInfos1.size() > 0) {
                                        auditSpResultmaterial.put("templateurl",
                                                WebUtil.getRequestCompleteUrl(request)
                                                        + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                                        + frameAttachInfos1.get(0).getAttachGuid());
                                    }
                                }
                            }
                            custom.put("resultlist", auditSpResultmaterials);
                        }
                    } else {
                        // 如果根据businessguid未查询到对应的业务实体，返回相应提示信息
                        return JsonUtils.zwdtRestReturn("0", "未查询到对应业务信息", "");
                    }
                } else {
                    // 如果businessguid为空，返回相应提示信息
                    return JsonUtils.zwdtRestReturn("0", "业务标识不能为空", "");
                }
                log.info("=======结束调用getDetail接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取成功！", custom.toString());

            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            log.info("接口调用失败！错误信息", e);
            return JsonUtils.zwdtRestReturn("0", "接口异常", "");
        }
    }

    /**
     * 获取一件事联办的政策解读
     */
    @RequestMapping(value = "/getexplainlist", method = RequestMethod.POST)
    public String getExplainlist(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getexplainlist接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject paramsObj = jsonObject.getJSONObject("params");
                String businessguid = paramsObj.getString("businessguid");

                JSONObject custom = new JSONObject();
                if (StringUtil.isNotBlank(businessguid)) {
                    // 根据businessguid从数据库中获取AuditSpBusiness实体信息，该实体对应一件事联办的业务相关数据
                    AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessguid).getResult();
                    if (auditSpBusiness != null) {

                        // 联办事项
                        StringBuffer stringBuffer1 = new StringBuffer();
                        stringBuffer1.append("select * from audit_sp_policy where businessguid=?");
                        List<AuditSpPolicy> auditSpPolicies =  iAuditSpPolicyService.findList(stringBuffer1.toString(),auditSpBusiness.getRowguid());
                        if(auditSpPolicies != null && auditSpPolicies.size() > 0) {
                            custom.put("list", auditSpPolicies);
                        }

                    } else {
                        // 如果根据businessguid未查询到对应的业务实体，返回相应提示信息
                        return JsonUtils.zwdtRestReturn("0", "未查询到对应业务信息", "");
                    }
                } else {
                    // 如果businessguid为空，返回相应提示信息
                    return JsonUtils.zwdtRestReturn("0", "业务标识不能为空", "");
                }
                log.info("=======结束调用getexplainlist接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取成功！", custom.toString());

            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            log.info("接口调用失败！错误信息", e);
            return JsonUtils.zwdtRestReturn("0", "接口异常", "");
        }
    }

    /**
     * 获取一件事联办的视频
     */
    @RequestMapping(value = "/getvideolist", method = RequestMethod.POST)
    public String getVideolist(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getvideolist接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject paramsObj = jsonObject.getJSONObject("params");
                String businessguid = paramsObj.getString("businessguid");

                JSONObject custom = new JSONObject();
                if (StringUtil.isNotBlank(businessguid)) {
                    // 根据businessguid从数据库中获取AuditSpBusiness实体信息，该实体对应一件事联办的业务相关数据
                    AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessguid).getResult();
                    if (auditSpBusiness != null) {

                        // 联办事项
                        StringBuffer stringBuffer1 = new StringBuffer();
                        stringBuffer1.append("select * from YJS_ZN where businessguid=?");
                        List<YjsZn> yjsZns = iYjsZnService.findList(stringBuffer1.toString(),businessguid);
                        if(yjsZns != null && yjsZns.size() > 0) {
                            FrameAttachInfo frameAttachInfos = iAttachService.getAttachInfoDetail(yjsZns.get(0).getAttachguid());
                            custom.put("videosrc", WebUtil.getRequestCompleteUrl(request)
                                    + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                    + frameAttachInfos.getAttachGuid());
                        }

                    } else {
                        // 如果根据businessguid未查询到对应的业务实体，返回相应提示信息
                        return JsonUtils.zwdtRestReturn("0", "未查询到对应业务信息", "");
                    }
                } else {
                    // 如果businessguid为空，返回相应提示信息
                    return JsonUtils.zwdtRestReturn("0", "业务标识不能为空", "");
                }
                log.info("=======结束调用getvideolist接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取成功！", custom.toString());

            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            log.info("接口调用失败！错误信息", e);
            return JsonUtils.zwdtRestReturn("0", "接口异常", "");
        }
    }


}
