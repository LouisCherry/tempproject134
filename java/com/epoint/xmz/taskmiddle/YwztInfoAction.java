package com.epoint.xmz.taskmiddle;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditproject.util.FlowsnUtil;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.jnycsl.utils.WavePushInterfaceUtils;
import com.epoint.xmz.api.ISendMaterials;
import com.epoint.zhenggai.api.ZhenggaiService;

/***
 * 业务中台线下申报对接
 */
@RestController("ywztinfoaction")
@Scope("request")
public class YwztInfoAction extends BaseController
{
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    private static final long serialVersionUID = 1L;

    @Autowired
    private IAttachService iAttachService;

    @Autowired
    private IAuditProject iAuditProject;
    @Autowired
    private IMessagesCenterService iMessagesCenterService;
    @Autowired
    private IAuditTask iAuditTask;
    @Autowired
    private IAuditProjectMaterial iAuditProjectMaterial;

    @Autowired
    private ZhenggaiService zhenggaiImpl;
    
    @Autowired
    private IAuditTaskMaterial iAuditTaskMaterial;
    @Autowired
    private ISendMaterials sendMaterials;

    @Override
    public void pageLoad() {

    }


    /**
     * 设置接件人所在窗口
     */
    public void add(String sbinfo) {

        try {
            log.info("业务中台线下申报入参：" + sbinfo);
            JSONObject paramsinfo = JSONObject.parseObject(sbinfo);
            Record submitinfo = new Record();
            submitinfo.setSql_TableName("ywzt_submit_info");
            submitinfo.set("rowguid", UUID.randomUUID().toString());
            submitinfo.set("operatedate", new Date());
            // 把整个入参存下来
            submitinfo.set("alljson", sbinfo);
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
            AuditTask task = sendMaterials.getAuditTaskByNewItemCode(baseInfo.getString("itemCode"));
//            if ("5687621".equals(baseInfo.getString("itemCode"))) {// 建筑业企业资质（告知承诺方式）新申请
//                taskid = "8aeed14a-178e-4df7-9b65-42dbdd8296ec"; // TODO 暂时写死
//                task = iAuditTask.selectUsableTaskByTaskID(taskid).getResult();
//            }
            if (task == null) {
                addCallbackParam("code", "0");
                addCallbackParam("msg", "接件失败，未找到匹配事项");
                return;
            }
            newProject.setOperatedate(new Date());

            newProject.setOperateusername("业务中台线下推送");
            
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
                }*/
            }else {
            	newProject.setFlowsn(baseInfo.getString("receiveNum"));
            }
            
            newProject.setApplyway(20);
            // 办件状态 已接件
            newProject.setStatus(26);
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


            newProject.setAreacode(task.getAreacode());// TODO 先写死泰安市
            newProject.setIs_test(0);
            newProject.setApplydate(new Date());
            if ("0".equals(applicant.getString("objType"))) {// 自然人
                newProject.setCerttype("22");
                newProject.setCertnum(applicant.getString("certNo"));
                newProject.setApplyername(applicant.getString("name"));
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


            // 关联窗口
            newProject.setWindowguid(ZwfwUserSession.getInstance().getWindowGuid());
            newProject.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
            newProject.setWindowname(ZwfwUserSession.getInstance().getWindowName());
            newProject.setReceivedate(new Date());
            newProject.setReceiveuserguid(userSession.getUserGuid());
            newProject.setReceiveusername(userSession.getDisplayName());
            newProject.setCurrentareacode(ZwfwUserSession.getInstance().getAreaCode());
            newProject.setStatus(26);// 直接设置为已接件
            // TODO 是否直接将业务中台的流水号设置为办件流水号？
            String areaCode = task.getAreacode();

//            String areaCodeSub = areaCode.substring(2, 6);
//            if ("0900".equals(areaCodeSub)) {
//                areaCodeSub = "0901";
//            }
//            if ("0990".equals(areaCodeSub)) {
//                areaCodeSub = "0991";
//            }
//
//            String tasktype = task.getShenpilb();
//            if (StringUtil.isBlank(tasktype)) {
//                tasktype = "00";
//            }
//            String numberFlag = areaCodeSub + tasktype;
//            String flowsn = "";
//            try {
//                flowsn = sendMaterials.getStrFlowSn("办件编号", numberFlag, 6);
//                EpointFrameDsManager.commit();
//            } catch (Exception e) {
//                EpointFrameDsManager.rollback();
//            } finally {
//                EpointFrameDsManager.close();
//            }
//            newProject.setFlowsn(flowsn);
            // 发送受理代办

            if (task != null) {
                String title = "【待受理】" + newProject.getProjectname() + "(" + userSession.getDisplayName() + ")";
                String messageItemGuid = UUID.randomUUID().toString();
                String formUrl = ZwfwConstant.CONSTANT_FORM_URL;
                String handleUrl = formUrl + "?processguid=" + task.getProcessguid() + "&taskguid="
                        + newProject.getTaskguid() + "&projectguid=" + newProject.getRowguid();
                iMessagesCenterService.insertWaitHandleMessage(messageItemGuid, title,
                        IMessagesCenterService.MESSAGETYPE_WAIT, userSession.getUserGuid(), userSession.getDisplayName(),
                        userSession.getUserGuid(), userSession.getDisplayName(), "待受理", handleUrl, userSession.getOuGuid(),
                        "", ZwfwConstant.CONSTANT_INT_ONE, "", "", newProject.getRowguid(),
                        newProject.getRowguid().substring(0, 1), new Date(), newProject.getPviguid(),
                        userSession.getUserGuid(), "", "");
            }
            iAuditProject.addProject(newProject);

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

            addCallbackParam("code", "1");
            addCallbackParam("msg", "接件成功！");
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
            addCallbackParam("code", "0");
            addCallbackParam("msg", "接件操作异常！");
        }
        addCallbackParam("code", "0");
        addCallbackParam("msg", "接件失败！");
    }


}
