package com.epoint.xmz.taskmiddle;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditproject.util.FlowsnUtil;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.jnycsl.utils.WavePushInterfaceUtils;
import com.epoint.xmz.api.ISendMaterials;
import com.epoint.xmz.userfeedback.api.IUserFeedbackService;
import com.epoint.zhenggai.api.ZhenggaiService;

/**
 * 提供给业务中台接口
 */
@RestController
@RequestMapping("/taskmiddleapi")
public class TaskMiddleApi
{
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IAuditTask iAuditTask;
    @Autowired
    private IAuditProject iAuditProject;
    @Autowired
    private IAuditProjectMaterial iAuditProjectMaterial;
    @Autowired
    private IAttachService iAttachService;
    @Autowired
    private IAuditTaskMaterial iAuditTaskMaterial;
    @Autowired
    private IAuditOrgaServiceCenter iAuditOrgaServiceCenter;
    
    @Autowired
    private IAuditOnlineProject iAuditOnlineProject;
    @Autowired
    private IUserFeedbackService iUserFeedbackService;
    
    @Autowired
    private ZhenggaiService zhenggaiImpl;
    
    
    @Autowired
    private ISendMaterials iSendMaterials;

    /**
     * 申报提交接口
     * @param params
     * @return
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public String submit(@RequestBody String params) {
        log.info("========开始调用submit接口=========入参：" + params);
        // 定义返回结果
        JSONObject resultjson = new JSONObject();
        try {
            JSONObject paramsinfo = JSONObject.parseObject(params);
            Record submitinfo = new Record();
            submitinfo.setSql_TableName("ywzt_submit_info");
            submitinfo.set("rowguid", UUID.randomUUID().toString());
            submitinfo.set("operatedate", new Date());
            // 把整个入参存下来
            submitinfo.set("alljson", params);
            // 先获取基本信息
            JSONObject baseInfo = paramsinfo.getJSONObject("baseInfo");
            submitinfo.set("dataId", baseInfo.getString("dataId"));// 数据id
            submitinfo.set("receiveNum", baseInfo.getString("receiveNum"));// 业务流水号
            // 申请人信息
            JSONObject applicant = paramsinfo.getJSONObject("applicant");
            submitinfo.set("baseInfo", baseInfo.toJSONString());
            submitinfo.set("applicant", applicant.toJSONString());
            // 其他信息
            JSONObject otherinfo = paramsinfo.getJSONObject("otherInfo");
            submitinfo.set("otherInfo", otherinfo.toJSONString());
            // 材料信息
            JSONArray materials = paramsinfo.getJSONArray("materials");
            submitinfo.set("materials", materials.toJSONString());
            // 情形数据
            JSONArray cases = paramsinfo.getJSONArray("cases");
            submitinfo.set("cases", cases.toJSONString());
            // 业务表单信息
            JSONObject formData = paramsinfo.getJSONObject("formData");
            submitinfo.set("formData", formData.toJSONString());
            //邮寄信息
            JSONObject mailSend = paramsinfo.getJSONObject("mailSend");
            submitinfo.set("mailSend",mailSend.toJSONString());

            // 保存推送数据
            CommonDao.getInstance().insert(submitinfo);
            // 申报来源
            String source = baseInfo.getString("source");
            // 生成办件数据
            AuditProject newProject = new AuditProject();
            String projectguid = UUID.randomUUID().toString();
            newProject.setRowguid(projectguid);
            
        
            
            // 关联下推送数据
            newProject.set("ywzt_source", source);
            newProject.set("ywzt_receivenum", baseInfo.getString("receiveNum"));
            // 获取事项 TODO  先写死
            String taskid = "";
            AuditTask task = iSendMaterials.getAuditTaskByNewItemCode(baseInfo.getString("itemCode"));
//            if ("5687621".equals(baseInfo.getString("itemCode"))) {// 建筑业企业资质（告知承诺方式）新申请
//                taskid = "8aeed14a-178e-4df7-9b65-42dbdd8296ec"; // TODO 暂时写死
//                task = iAuditTask.selectUsableTaskByTaskID(taskid).getResult();
//            }
            if (task == null) {
                resultjson.put("state", "300");
                resultjson.put("errorCode", "");
                resultjson.put("errorMsg", "未找到匹配事项！");
            }
            
            // 来源（外网还是其他系统）
            String resource = "1";
            // 获取事项ID
            String unid = zhenggaiImpl.getunidbyTaskid(task.getTask_id());
            // 请求接口获取受理编码
            if(StringUtil.isNotBlank(unid)){
				String result = FlowsnUtil.createReceiveNum(unid,task.getRowguid());
				if (!"error".equals(result)) {
					log.info("========================>获取受理编码成功！" + result);
					newProject.setFlowsn(result);
				} else {
					log.info("========================>获取受理编码失败！");
				}
            }
            else {
            	newProject.setFlowsn(baseInfo.getString("receiveNum"));
            }
            	
                /*// 构造获取受理编码的请求入参
                String params2Get = "?itemId=" + unid + "&applyFrom=" + resource;
                try {
                    JSONObject jsonObj = WavePushInterfaceUtils.createReceiveNum(params2Get, task.getShenpilb());
                    if (jsonObj != null && "200".equals(jsonObj.getString("state"))) {
                    	log.info("========================>获取受理编码成功！" + jsonObj.getString("receiveNum") + "#####"
                                + jsonObj.getString("password"));
                        String receiveNum = jsonObj.getString("receiveNum");
                        newProject.setFlowsn(receiveNum);
                    }
                    else {
                    	log.info("========================>获取受理编码失败！");
                    }
                }
                catch (IOException e) {
                	log.info("接口请求报错！========================>" + e.getMessage());
                    e.printStackTrace();
                }
            }else {
            	newProject.setFlowsn(baseInfo.getString("receiveNum"));
            }*/
            
            
            newProject.setOperatedate(new Date());
            
            newProject.setAcceptareacode(task.getAreacode());
            

            if ("pc".equals(source)) {
                newProject.setOperateusername("业务中台线上推送");
                newProject.setApplyway(10);
                // 办件状态 带预审
                newProject.setStatus(12);
            }
            else {
                newProject.setOperateusername("业务中台线下推送");
                newProject.setApplyway(20);
                // 办件状态 已接件
                newProject.setStatus(26);
            }
            newProject.setInsertdate(new Date());
            newProject.setTaskguid(task.getRowguid());
            newProject.setTask_id(task.getTask_id());
            newProject.setTaskid(task.getTask_id());
            newProject.setOuguid(task.getOuguid());
            newProject.setOuname(task.getOuname());
            newProject.setIs_delay(20);
            newProject.setProjectname(task.getTaskname());
            newProject.setPromise_day(task.getPromise_day());
            newProject.setTasktype(task.getType());
            // 设置中心
            SqlConditionUtil sql=new SqlConditionUtil();
            sql.eq("ouguid", task.getOuguid());
            List<AuditOrgaServiceCenter> centerList = iAuditOrgaServiceCenter.getAuditOrgaServiceCenterByCondition(sql.getMap()).getResult();
            if (centerList != null && centerList.size() > 0) {
                AuditOrgaServiceCenter object =  centerList.get(0);
                newProject.setCenterguid(object.getRowguid());
            }
            else {
                // 默认一个吧
                newProject.setCenterguid("8adfc4f0-ce41-453e-94a7-14356a44db79");
            }

            newProject.setAreacode(task.getAreacode());// TODO 先写死泰安市
            newProject.setIs_test(0);
            newProject.setApplydate(new Date());
            if ("0".equals(applicant.getString("objType"))) {// 自然人
                newProject.setCerttype("22");
                newProject.setCertnum(applicant.getString("certNo"));
                newProject.setApplyername(applicant.getString("userName"));
                newProject.setApplyertype(20);
                newProject.setApplyeruserguid(applicant.getString("userId"));
            }
            else {// 法人
                newProject.setCerttype("16");
                newProject.setCertnum(applicant.getString("enterpriseCreditCode"));
                newProject.setApplyername(applicant.getString("enterpriseName"));
                newProject.setApplyertype(10);
                newProject.setApplyeruserguid(applicant.getString("userId"));
                newProject.setLegal(applicant.getString("legalName"));
                newProject.setLegalid(applicant.getString("legalCertNo"));
            }
            newProject.setContactperson(applicant.getString("linkmanName"));
            newProject.setContactcertnum(applicant.getString("linkmanCertNo"));
            newProject.setContactmobile(applicant.getString("phone"));
            newProject.setContactphone(applicant.getString("linkmanPhone"));
            newProject.setAddress(applicant.getString("linkmanAddress"));
            // 如果是个人
            if ("0".equals(applicant.getString("objType"))) {// 自然人
                newProject.setContactperson(applicant.getString("name"));
            }
            iAuditProject.addProject(newProject);
            
            if ("16".equals(newProject.getCerttype())) {
            	AuditRsCompanyBaseinfo company = iUserFeedbackService.getCompanyByCreditcode(newProject.getCertnum());
            	if (company != null) {
               	  // 3、获取用户申报的事项
                   AuditOnlineProject auditOnlineProject = iAuditOnlineProject.getOnlineProjectByApplyerGuid(projectguid, company.getCompanyid()).getResult();
                   if (auditOnlineProject == null) {
                   	auditOnlineProject = new AuditOnlineProject();
                   	auditOnlineProject.setRowguid(UUID.randomUUID().toString());
                   	auditOnlineProject.setOperatedate(new Date());
                   	auditOnlineProject.setSourceguid(projectguid);
                   	auditOnlineProject.setCreatedate(newProject.getOperatedate());
                   	auditOnlineProject.setTaskguid(newProject.getTaskguid());
                   	auditOnlineProject.setProjectname(newProject.getProjectname());
                   	auditOnlineProject.setType("0");
                   	auditOnlineProject.setAreacode(newProject.getAreacode());
                   	auditOnlineProject.setCenterguid("");
                   	auditOnlineProject.setApplyername(newProject.getApplyername());
                   	auditOnlineProject.setApplyerguid(company.getCompanyid());
                   	auditOnlineProject.setStatus(newProject.getStr("Status"));
                   	auditOnlineProject.setOuguid(newProject.getOuguid());
                   	auditOnlineProject.setOuname(newProject.getOuname());
                   	auditOnlineProject.setApplydate(newProject.getApplydate());
                   	auditOnlineProject.setIs_fee(0);
                   	auditOnlineProject.setIs_charge(0);
                   	auditOnlineProject.setIs_evaluat(0);
                   	auditOnlineProject.setApplyertype(newProject.getApplyertype()+"");
                   	auditOnlineProject.setTasktype(newProject.getTasktype()+"");
                   	auditOnlineProject.set("epidemic", "1");
                   	iAuditOnlineProject.addProject(auditOnlineProject);
                   }
               
            	}
            	
            }else {
            	 AuditOnlineIndividual individual = iUserFeedbackService.getOnlineProjectByIdNumber(newProject.getCertnum());
                 
                 if (individual != null) {
                 	  // 3、获取用户申报的事项
                     AuditOnlineProject auditOnlineProject = iAuditOnlineProject.getOnlineProjectByApplyerGuid(projectguid, individual.getApplyerguid()).getResult();
                     if (auditOnlineProject == null) {
                     	auditOnlineProject = new AuditOnlineProject();
                     	auditOnlineProject.setRowguid(UUID.randomUUID().toString());
                     	auditOnlineProject.setOperatedate(new Date());
                     	auditOnlineProject.setSourceguid(projectguid);
                     	auditOnlineProject.setCreatedate(newProject.getOperatedate());
                     	auditOnlineProject.setTaskguid(newProject.getTaskguid());
                     	auditOnlineProject.setProjectname(newProject.getProjectname());
                     	auditOnlineProject.setType("0");
                     	auditOnlineProject.setAreacode(newProject.getAreacode());
                     	auditOnlineProject.setCenterguid("");
                     	auditOnlineProject.setApplyername(individual.getClientname());
                     	auditOnlineProject.setApplyerguid(individual.getApplyerguid());
                     	auditOnlineProject.setStatus(newProject.getStr("Status"));
                     	auditOnlineProject.setOuguid(newProject.getOuguid());
                     	auditOnlineProject.setOuname(newProject.getOuname());
                     	auditOnlineProject.setApplydate(newProject.getApplydate());
                     	auditOnlineProject.setIs_fee(0);
                     	auditOnlineProject.setIs_charge(0);
                     	auditOnlineProject.setIs_evaluat(0);
                     	auditOnlineProject.setApplyertype(newProject.getApplyertype()+"");
                     	auditOnlineProject.setTasktype(newProject.getTasktype()+"");
                     	auditOnlineProject.set("epidemic", "1");
                     	iAuditOnlineProject.addProject(auditOnlineProject);
                     }
                 }
            }
            
           

            //初始化办件数据并且返回办件材料数据
            List<AuditProjectMaterial> projectMaterials = new ArrayList<>();
            List<AuditTaskMaterial> taskMaterials = iAuditTaskMaterial.selectTaskMaterialListByTaskGuid(task.getRowguid(), false).getResult();
            if (taskMaterials != null && taskMaterials.size() > 0) {
                for (AuditTaskMaterial taskMaterial : taskMaterials) {
                    AuditProjectMaterial projectMaterial = new AuditProjectMaterial();
                    projectMaterial.setRowguid(UUID.randomUUID().toString());
                    projectMaterial.setOperatedate(new Date());
                    projectMaterial.setTaskguid(newProject.getTaskguid());
                    projectMaterial.setProjectguid(newProject.getRowguid());
                    projectMaterial.setTaskmaterialguid(taskMaterial.getRowguid());
                    projectMaterial.setStatus(10);
                    projectMaterial.setAuditstatus("10");
                    projectMaterial.setIs_rongque(0);
                    projectMaterial.setCliengguid(UUID.randomUUID().toString());
                    projectMaterial.setAttachfilefrom("1");
                    projectMaterial.setTaskmaterial(taskMaterial.getMaterialname());
                    iAuditProjectMaterial.addProjectMateiral(projectMaterial);
                    projectMaterials.add(projectMaterial);
                }
            }
            // 处理材料数据
            if (materials != null && materials.size() > 0) {
                for (AuditProjectMaterial projectMaterial : projectMaterials) {
                    for (Object material : materials) {
                        JSONObject materialinfo = (JSONObject) material;
                        if (projectMaterial.getTaskmaterial().equals(materialinfo.getString("documentName"))) {
                            FrameAttachInfo attachInfo = iAttachService.getAttachInfoDetail(materialinfo.getString("filePath"));
                            if (attachInfo != null) {
                                attachInfo.setCliengGuid(projectMaterial.getCliengguid());
                                iAttachService.updateAttach(attachInfo, null);
                                // 更新办件材料提交状态
                                projectMaterial.setStatus(20);
                                // 关联下推送过来的材料
                                projectMaterial.set("ywztcode", materialinfo.getString("documentCode"));
                                iAuditProjectMaterial.updateProjectMaterial(projectMaterial);
                            }
                        };
                    }
                }
            }
            resultjson.put("state", "200");
            resultjson.put("errorCode", "");
            resultjson.put("errorMsg", "");
            return resultjson.toJSONString();
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("调用submit异常" + e);
            resultjson.put("state", "300");
            resultjson.put("errorCode", "E04");
            resultjson.put("errorMsg", "");
            return resultjson.toJSONString();
        }
    }

    /**
     * 申报详情查询接口
     * @param params
     * @return
     */
    @RequestMapping(value = "/querySubmitInfo", method = RequestMethod.POST)
    public String querySubmitInfo(@RequestBody String params) {
        log.info("========开始调用querySubmitInfo接口=========入参：" + params);
        // 定义返回结果
        JSONObject resultjson = new JSONObject();
        try {
            // 返回结果
            JSONObject datainfo = new JSONObject();
            resultjson.put("state", "200");
            resultjson.put("data", datainfo);
            resultjson.put("errorCode", "");
            resultjson.put("errorMsg", "");
            return resultjson.toJSONString();
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("调用querySubmitInfo异常" + e);
            resultjson.put("state", "300");
            resultjson.put("errorCode", "E04");
            resultjson.put("errorMsg", "");
            return resultjson.toJSONString();
        }
    }

    /**
     * 补齐补正提交接口
     * @param params
     * @return
     */
    @RequestMapping(value = "/submitPatch", method = RequestMethod.POST)
    public String submitPatch(@RequestBody String params) {
        log.info("========开始调用submitPatch接口=========入参：" + params);
        // 定义返回结果
        JSONObject resultjson = new JSONObject();
        try {

            resultjson.put("state", "200");
            resultjson.put("errorCode", "");
            resultjson.put("errorMsg", "");
            return resultjson.toJSONString();
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("调用submitPatch异常" + e);
            resultjson.put("state", "300");
            resultjson.put("errorCode", "E04");
            resultjson.put("errorMsg", "");
            return resultjson.toJSONString();
        }
    }


    /**
     * 补齐补正详情查询接口
     * @param params
     * @return
     */
    @RequestMapping(value = "/queryPatchInfo", method = RequestMethod.POST)
    public String queryPatchInfo(@RequestBody String params) {
        log.info("========开始调用queryPatchInfo接口=========入参：" + params);
        // 定义返回结果
        JSONObject resultjson = new JSONObject();
        try {
            // 返回结果
            JSONObject datainfo = new JSONObject();
            resultjson.put("state", "200");
            resultjson.put("data", datainfo);
            resultjson.put("errorCode", "");
            resultjson.put("errorMsg", "");
            return resultjson.toJSONString();
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("调用queryPatchInfo异常" + e);
            resultjson.put("state", "300");
            resultjson.put("errorCode", "E04");
            resultjson.put("errorMsg", "");
            return resultjson.toJSONString();
        }
    }

    /**
     * 材料库提交接口
     * @param params
     * @return
     */
    @RequestMapping(value = "/submitMaterial", method = RequestMethod.POST)
    public String submitMaterial(@RequestBody String params) {
        log.info("========开始调用submitMaterial接口=========入参：" + params);
        // 定义返回结果
        JSONObject resultjson = new JSONObject();
        try {

            resultjson.put("state", "200");
            resultjson.put("errorCode", "");
            resultjson.put("errorMsg", "");
            return resultjson.toJSONString();
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("调用submitMaterial异常" + e);
            resultjson.put("state", "300");
            resultjson.put("errorCode", "E04");
            resultjson.put("errorMsg", "");
            return resultjson.toJSONString();
        }
    }

    /**
     * 材料库查询接口
     * @param params
     * @return
     */
    @RequestMapping(value = "/queryMaterialInfo", method = RequestMethod.POST)
    public String queryMaterialInfo(@RequestBody String params) {
        log.info("========开始调用queryMaterialInfo接口=========入参：" + params);
        // 定义返回结果
        JSONObject resultjson = new JSONObject();
        try {
            // 返回结果
            JSONObject datainfo = new JSONObject();
            resultjson.put("state", "200");
            resultjson.put("data", datainfo);
            resultjson.put("errorCode", "");
            resultjson.put("errorMsg", "");
            return resultjson.toJSONString();
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("调用queryMaterialInfo异常" + e);
            resultjson.put("state", "300");
            resultjson.put("errorCode", "E04");
            resultjson.put("errorMsg", "");
            return resultjson.toJSONString();
        }
    }


}
