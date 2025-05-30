package com.epoint.zwdt.zwdtrest.kantan;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.takan.kanyanmain.api.IKanyanmainService;
import com.epoint.takan.kanyanmain.api.entity.Kanyanmain;
import com.epoint.takan.kanyanproject.api.IKanyanprojectService;
import com.epoint.takan.kanyanproject.api.entity.Kanyanproject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;

/**
 * 勘探相关接口
 */
@RestController
@RequestMapping("/jnkantan")
public class JnKantanController {

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * 踏勘API
     */
    @Autowired
    private IKanyanmainService iKanyanmainService;

    @Autowired
    private IKanyanprojectService iKanyanprojectService;

    @Autowired
    private IAuditProject iAuditProject;

    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private ICodeItemsService iCodeItemsService;

    @Autowired
    private IAuditProjectMaterial iAuditProjectMaterial;

    @Autowired
    private IAttachService iAttachService;


    /**
     * 接受勘探结果
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/receivekantan", method = RequestMethod.POST)
    public String receivekantan(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用receivekantan接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                //办件编号
                String flowsn = obj.getString("flowsn");
                // 勘验小项名称
                String name = obj.getString("name");
                //视频地址
                String videourls = obj.getString("videourls");
                JSONArray projects = obj.getJSONArray("projects");
                Kanyanmain kanyanmain = null;
                if (StringUtils.isNotBlank(flowsn) && StringUtils.isNotBlank(name)) {
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("flowsn", flowsn);
                    kanyanmain = iKanyanmainService.find(sqlConditionUtil.getMap());
                    if (kanyanmain != null) {
                        kanyanmain.setName(name);
                        kanyanmain.setOperatedate(new Date());
                        if (videourls != null) {
                            kanyanmain.setVideourls(videourls);
                        }
                        iKanyanmainService.update(kanyanmain);
                    } else {
                        kanyanmain = new Kanyanmain();
                        kanyanmain.setRowguid(UUID.randomUUID().toString());
                        kanyanmain.setFlowsn(flowsn);
                        kanyanmain.setOperatedate(new Date());
                        kanyanmain.setName(name);
                        String fields = "rowguid,projectname";
                        AuditProject auditProject = iAuditProject.getAuditProjectByFlowsn(fields, flowsn, "370800").getResult();
                        if (auditProject != null) {
                            kanyanmain.setTaskname(auditProject.getProjectname());
                            kanyanmain.setProjctguid(auditProject.getRowguid());
                        }
                        if (videourls != null) {
                            kanyanmain.setVideourls(videourls);
                        }
                        iKanyanmainService.insert(kanyanmain);
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "参数不全！", "");
                }
                if (kanyanmain != null && projects != null && !projects.isEmpty()) {
                    //清空数据
                    iKanyanprojectService.deleteByMainGuid(kanyanmain.getRowguid());
                    for (int i = 0; i < projects.size(); i++) {
                        JSONObject project = projects.getJSONObject(i);
                        //勘验项目
                        String projectname = project.getString("projectname");
                        //勘验项目要点
                        String mainpoint = project.getString("mainpoint");
                        //审查内容
                        String shenchacontent = project.getString("shenchacontent");
                        //条款号
                        String tiaokuannum = project.getString("tiaokuannum");
                        //勘验截图clientguid
                        String kanyanjietuclientguid = project.getString("kanyanjietuclientguid");
                        //审核人审查结果
                        String scrresult = project.getString("scrresult");
                        //审核人审查意见
                        String scrcomment = project.getString("scrcomment");
                        //专家审查结果
                        String zjresult = project.getString("zjresult");
                        //专家审查意见
                        String zjcomment = project.getString("zjcomment");
                        //普通图片地址
                        String urls = project.getString("urls");
                        //法律依据
                        String bylaw = project.getString("bylaw");
                        //视频地址
                        String videourls1 = project.getString("videourls");
                        Kanyanproject kanyanproject = new Kanyanproject();
                        kanyanproject.setRowguid(UUID.randomUUID().toString());
                        if (projectname != null) {
                            kanyanproject.setProjectname(projectname);
                        }
                        if (mainpoint != null) {
                            kanyanproject.setMainpoint(mainpoint);
                        }
                        if (shenchacontent != null) {
                            kanyanproject.setShenchacontent(shenchacontent);
                        }
                        if (tiaokuannum != null) {
                            kanyanproject.setTiaokuannum(tiaokuannum);
                        }
                        if (kanyanjietuclientguid != null) {
                            kanyanproject.setKanyanjietuclientguid(kanyanjietuclientguid);
                        }
                        if (scrresult != null) {
                            kanyanproject.setScrresult(scrresult);
                        }
                        if (scrcomment != null) {
                            kanyanproject.setScrcomment(scrcomment);
                        }
                        if (zjresult != null) {
                            kanyanproject.setZjresult(zjresult);
                        }
                        if (zjcomment != null) {
                            kanyanproject.setZjcomment(zjcomment);
                        }
                        if (bylaw != null) {
                            kanyanproject.setBylaw(bylaw);
                        }

                        if(urls!=null){
                            kanyanproject.setUrls(urls);
                        }
                        if (videourls1 != null) {
                            kanyanproject.setVideourls(videourls1);
                        }
                        kanyanproject.setOperatedate(new Date());
                        kanyanproject.setKanyaguid(kanyanmain.getRowguid());
                        iKanyanprojectService.insert(kanyanproject);
                    }
                }

                //接受主信息
                log.info("=======结束调用receivekantan接口=======");
                return JsonUtils.zwdtRestReturn("1", "接受勘探结果成功", "");
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======receivekantan接口参数：params【" + params + "】=======");
            log.info("=======receivekantan异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "提交失败：" + e.getMessage(), "");
        }
    }


    /**
     * 发起勘验
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/startkantan", method = RequestMethod.POST)
    public String startkantan(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用startkantan接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                //办件编号
                String flowsn = obj.getString("flowsn");
                AuditProject auditProject = iAuditProject.getAuditProjectByFlowsn("*", flowsn, "370800").getResult();
                //是否复勘
                String iffukan = obj.getString("iffukan");
                String username = obj.getString("username");
                String ouname = obj.getString("ouname");
                String mobile = obj.getString("mobile");
                String gettoken = ConfigUtil.getConfigValue("takan", "gettoken");
                String loginName = ConfigUtil.getConfigValue("takan", "tokenuser");
                String password = ConfigUtil.getConfigValue("takan", "tokenpass");
                String addTaskManagementSystem = ConfigUtil.getConfigValue("takan", "addTaskManagementSystem");
                JSONObject postparams = new JSONObject();
                postparams.put("loginName", loginName);
                postparams.put("password", password);
                String rtnstr = com.epoint.core.utils.httpclient.HttpUtil.doPostJson(gettoken, postparams.toJSONString());
                log.info("rtnstr:" + rtnstr);
                if (StringUtil.isNotBlank(rtnstr)) {
                    JSONObject rtnjson = JSON.parseObject(rtnstr);
                    if ("0".equals(rtnjson.getString("code")) && "200".equals(rtnjson.getString("httpStatus"))) {
                        String data = rtnjson.getString("data");
                        if (StringUtils.isNotBlank(data) && auditProject != null) {
                            Map<String, String> headers = new HashMap<String, String>();
                            headers.put("token", data);
                            JSONObject finalParams = new JSONObject();
                            finalParams.put("itemCode", auditProject.getFlowsn());//业务唯一标识（办件编号）
                            finalParams.put("applyDept", auditProject.getApplyername());//申请单位/申请人
                            finalParams.put("documentNo", auditProject.getCertnum());//证件号码(身份证号/统一社会信用代码)
                            finalParams.put("applyDate", auditProject.getApplydate());//申请时间
                            finalParams.put("acceptanceDate", auditProject.getAcceptuserdate());//受理时间
                            finalParams.put("acceptanceUser", auditProject.getAcceptusername());//受理人员
                            finalParams.put("applyType", ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(auditProject.getApplyertype())) ? "0" : "1");//申请类型 0个人 1企业法人
                            finalParams.put("applyTel", auditProject.getContactphone());//申请人手机号码
                            AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
                            if (auditTask != null) {
                                finalParams.put("transactId", auditTask.getItem_id());//事项编码
                                finalParams.put("transactName", auditTask.getTaskname());//事项名称
                                finalParams.put("belongCounties", auditTask.getAreacode());//所属区县
                            } else {
                                return JsonUtils.zwdtRestReturn("0", "办理项Id不能为空,办理项名称不能为空,所属区县不能为空！", "");
                            }
                            // 一窗账号姓名（点击发起勘验的一窗账号）
                            if(StringUtils.isNotBlank(username)){
                                finalParams.put("promoterUser", username);
                            }

                            //l 一窗账号所属部门
                            if(StringUtils.isNotBlank(ouname)){
                                finalParams.put("promoterDept", ouname);
                            }

                            //l 一窗账号对应手机号（没有则不推）
                            if(StringUtils.isNotBlank(mobile)){
                                finalParams.put("promoterTel", mobile);
                            }

                            finalParams.put("belongDepart", auditProject.getOuname());//所属部门
                            finalParams.put("address", auditProject.getAddress());//具体地址
                            if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(auditProject.getApplyertype()))) {
                                finalParams.put("legal", auditProject.getApplyername());//法人代表
                                finalParams.put("legalNo", auditProject.getCertnum());//法人身份证号
                                if(StringUtils.isNotBlank(auditProject.getLegalid())){
                                    finalParams.put("legalNo", auditProject.getLegalid());
                                    finalParams.put("legal", auditProject.getLegal());//法人代表
                                }
                            }

                            //申请材料明细
                            JSONArray listjsonarray = new JSONArray();
                            List<AuditProjectMaterial> auditProjectMaterialList = iAuditProjectMaterial.selectProjectMaterial(auditProject.getRowguid()).getResult();
                            if (auditProjectMaterialList != null && auditProjectMaterialList.size() > 0) {
                                for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterialList) {
                                    JSONObject jsonObject1 = new JSONObject();
                                    jsonObject1.put("materialsName", auditProjectMaterial.getTaskmaterial());
                                    jsonObject1.put("materialsSource", "办件");
                                    jsonObject1.put("materialsModality", "文件");
                                    JSONArray fileList = new JSONArray();
                                    if (StringUtils.isNotBlank(auditProjectMaterial.getCliengguid())) {
                                        List<FrameAttachInfo> attachInfos = iAttachService.getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
                                        if (CollectionUtils.isNotEmpty(attachInfos)) {
                                            for (FrameAttachInfo frameAttachInfo : attachInfos) {
                                                JSONObject frameob = new JSONObject();
                                                frameob.put("file", IOUtils.toByteArray(iAttachService.getInputStreamByInfo(frameAttachInfo)));
                                                frameob.put("fileName", frameAttachInfo.getAttachFileName());
                                                fileList.add(frameob);
                                            }
                                        }
                                    }
                                    jsonObject1.put("fileList", fileList);
                                    listjsonarray.add(jsonObject1);
                                }
                            }
                            finalParams.put("list", listjsonarray);
                            if (StringUtils.isNotBlank(iffukan) && "1".equals(iffukan)) {
                                finalParams.put("resurveyFlag", "1");
                            } else {
                                finalParams.put("resurveyFlag", "0");
                            }
//                            log.info("finalParams.toJSONString():" + finalParams.toJSONString());

                            SSLContext sslContext = SSLContext.getInstance("SSL");
                            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

                            OkHttpClient.Builder newBuilder = new OkHttpClient.Builder();
                            newBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
                            newBuilder.hostnameVerifier((hostname, session) -> true);

                            OkHttpClient client = newBuilder.build();
                            MediaType mediaType = MediaType.parse("application/json");
                            okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, finalParams.toJSONString());
                            Request request1 = new Request.Builder()
                                    .url(addTaskManagementSystem)
                                    .method("POST", body)
                                    .addHeader("token", data)
                                    .addHeader("Content-Type", "application/json")
                                    .build();
                            Response response = null;
                            String resultStr = "";
                            try {
                                response = client.newCall(request1).execute();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                resultStr = response.body().string();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }


//                            String resultStr = HttpUtil.doPostJsonSSL(addTaskManagementSystem, finalParams.toJSONString(), headers);
                            log.info("resultStr:" + resultStr);
                        }
                    }
                }

                //接受主信息
                log.info("=======结束调用startkantan接口=======");
                return JsonUtils.zwdtRestReturn("1", "发起勘验成功", "");
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======startkantan接口参数：params【" + params + "】=======");
            log.info("=======startkantan异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "发起勘验获取失败：" + e.getMessage(), "");
        }
    }

    TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }
    };


}
