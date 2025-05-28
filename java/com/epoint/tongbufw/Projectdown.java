package com.epoint.tongbufw;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.tazwfw.electricity.rest.action.ElectricityController;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;

@DisallowConcurrentExecution
public class Projectdown implements Job
{
    transient Logger log = LogUtil.getLog(Projectdown.class);

    private IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
    
    private ISendMQMessage sendMQMessageService = ContainerFactory.getContainInfo().getComponent(ISendMQMessage.class);
    
    private static String prov_filedown = ConfigUtil.getConfigValue("epointframe", "prov_filedown");
    
    /**
     * 程序入口
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            long startTime = System.currentTimeMillis();
            doService();
            //结束之间
            long endTime = System.currentTimeMillis();
            //程序块运行时间
            log.info("ProjectDown2Time运行时间为：" + (endTime - startTime)/1000 + "秒");
            EpointFrameDsManager.commit();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }

    //同步一窗受理系统的办件信息
    private void doService() {
        try {
            ProjectdownService service = new ProjectdownService();
            List<AuditProject> projectList = service.getProjectbytsflag();
            IAuditProjectSparetime iAuditProjectSparetime = ContainerFactory.getContainInfo().getComponent(IAuditProjectSparetime.class);

            log.info("开始同步挂起办件");

            List<Record> Gqlist = service.getProjectGqcqzkByFlag();
            log.info("浪潮暂停办件计时=====" + Gqlist.size());
            for (Record gq : Gqlist) {
              if ((StringUtil.isNotBlank(gq.getStr("RECEIVE_NUMBER"))) && 
                ("23".equals(gq.getStr("STATUS")))) {
                Record project = service.getProjectByFlowsn(gq.getStr("RECEIVE_NUMBER"));
                if (project != null) {
                   AuditProjectSparetime sparetime = iAuditProjectSparetime.getSparetimeByProjectGuid(project.getStr("projectguid")).getResult();
                  if ("1".equals(gq.getStr("ACTIVE"))){
                		//接办分离 特殊操作结果
                  	String msg =  project.getStr("projectguid") + ".10";
                     sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                             + project.getStr("areacode") + ".specialresult." + project.getStr("task_id"));
                     if (sparetime != null) {
                     	sparetime.setPause("1");
                     	iAuditProjectSparetime.updateSpareTime(sparetime);
                     	EpointFrameDsManager.commit();
                     }
                     
                  	service.updatePauseByGuid(project.getStr("projectguid"), 1);
                  }
                  else if ("0".equals(gq.getStr("ACTIVE"))) {
                	  if (sparetime != null) {
                       	sparetime.setPause("0");
                       	iAuditProjectSparetime.updateSpareTime(sparetime);
                       	EpointFrameDsManager.commit();
                       }
                    service.updatePauseByGuid(project.getStr("projectguid"), 0);
                  }
                  service.updateGqflag(gq.getStr("id"), "1");
                }
              }
              else {
                service.updateGqflag(gq.getStr("id"), "2");
              }
            }
            
            
            //办件状态更新办件办结信息
            log.info("开始同步 办件流程");
            for (AuditProject project : projectList) {
                String flowsn = project.getFlowsn();
                String pviguid = project.getPviguid();
                //查找前置库对应流水号的流程信息
                List<Record> recordList = service.getprocbyflowsn(flowsn);
                if (recordList != null && recordList.size() > 0) {
                    for (Record record : recordList) {
                        //插入流程信息表
                        WorkflowWorkItem workflow = new WorkflowWorkItem();
                        workflow.setActivityName(record.getStr("CURRENT_NODE_NAME"));
                        workflow.setWorkItemGuid(UUID.randomUUID().toString());
                        workflow.setProcessVersionInstanceGuid(pviguid);
                        workflow.setTransactorName(record.getStr("USER_NAME"));
                        workflow.setCreateDate(record.getDate("RECEIVE_TIME"));
                        workflow.setEndDate(record.getDate("SEND_TIME"));
                        workflow.setOpinion(record.getStr("OPINION"));
                        workflow.setOperatorForDisplayName(record.getStr("USER_NAME"));
                        workflow.setOperationDate(new Date());
                        workflow.set("projectguid", project.getRowguid());

                        workflow.setOperatorName(record.getStr("OPINION"));
                        int insertresult = service.insertbyrecord(workflow);
                        if (insertresult == 1) {
                            int res = service.updateflag(record.getStr("id"));
                            if (res == 1) {
                                log.info("=============>>>" + workflow + "更新流程信息成功");
                            }
                            else {
                                log.info("=============>>>" + workflow + "更新流程信息失败");
                            }
                            log.info("=============>>>" + workflow + "插入流程信息成功");
                        }
                        else {
                            log.info("=============>>>插入流程信息失败");
                        }
                        String status = record.getStr("status");
                        int intstatus = 80;
                       
                        switch (status) {
                            case "99":
                                intstatus = 90;//办结
                                
                                
                                ISendMQMessage sendMQMessageService1 = ContainerFactory.getContainInfo().getComponent(ISendMQMessage.class);

                              
                               if ("1".equals(project.getStr("IS_PAUSE"))) {
                            	   String msg2 =  project.getRowguid() + ".11";
                                   sendMQMessageService.sendByExchange("exchange_handle", msg2, "project."
                                           + project.getAreacode() + ".specialresult." + project.getTask_id());
                               }
                               
                                String msg1 = project.getRowguid() + "." + project.getAreacode() + "."
                                        + project.getApplyername();
                                sendMQMessageService1.sendByExchange("exchange_handle", msg1, "project."
                                        + project.getAreacode() + ".sendresult." + project.getTask_id());
                                
                                break;
                            case "98":
                                intstatus = 97;//不予受理
                                
                                break;
                            case "97":
                                intstatus = 99;//异常终止
                                break;
                            case "96":
                                intstatus = 98;//撤销申请
                                break;
                            case "21":
                                intstatus = 37;//已受理待补正
                                break;
                            default:
                                break;
                        }
                        //最后一个流程信息更新办件状态
                        if (intstatus>=90) {
                            //办件状态更新办件办结信息
                            log.info("开始同步 办件结果 " + flowsn);
                            int BANJIERESULT = 40;
                            //clflag 办件办结状态，历史遗留字段，不建议使用但不删除
                            project.set("clflag", "1");
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            int i = service.updateProjectByRowguid(project.getRowguid(), formatter.format(record.getDate("SEND_TIME")), intstatus, record.getStr("USER_NAME"), BANJIERESULT, record.getStr("USER_NAME"));
                            log.info("同步办件服务修改了 " + i + "条办件");
                            AuditProjectSparetime sparetime = iAuditProjectSparetime.getSparetimeByProjectGuid(project.getRowguid()).getResult();
                            if (sparetime != null) {
                            	sparetime.setPause("0");
                            	iAuditProjectSparetime.updateSpareTime(sparetime);
                            	EpointFrameDsManager.commit();
                            }
                            service.updatePauseByGuid(project.getRowguid(), 0);
                            service.updateOnlineProjectByRowguid(intstatus+"", project.getRowguid());
                            log.info("网厅对应办件修改成功了 " + i + "条办件");
                           
                            log.info("取消办件挂机状态成功 " + project.getFlowsn());
                            //对办理结果材料进行更新
                            getWtResult(project.getRowguid(),flowsn);
                            // 调用电力接口推送办件状态及信息
                            if (StringUtil.isNotBlank(project.getBusinessguid())&&"4".equals(project.getStr("is_lczj"))) {
                                log.info("调用电力接口推送办件状态及信息=========>>>>>>>>办结：" + project.getStatus());
                                IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
                                AuditTask auditTask = auditTaskService.getAuditTaskByGuid(project.getTaskguid(),false).getResult();
                                new ElectricityController().pushStatusToEc(project, auditTask, record.getStr("OPINION"));
                            }
                            log.info("结束同步 办件结果 " + intstatus);
                        }
                        else {
                            int j = service.updateProjectByRowguid(project.getRowguid(), null, intstatus, null, 0, null);
                            project.setStatus(intstatus);
                            if (j > 0) {
                                log.info("更新最后一个流程的办件状态成功 " + project.getStatus());
                            }
                          
                        }

                    }
                    log.info("=============>>>更新所有流程信息成功");

                }
                else {
                	 //service.updateProjectIssynacwaveByRowguid("10", project.getRowguid());
                	 //log.info("============>>>该办件在系统中并未找到，flowsn:"+flowsn);
                }
            }
            
        }
        catch (Exception e) {
            log.info("同步失败 =====" + e.getMessage());
        }
    }
    
    public void getWtResult(String projectGuid, String flowsn) {
        try {
                String materurl1 = "http://59.206.96.200:8090/web/approval/getMaterial?receiveNumber="+flowsn;
                String materresults = HttpUtil.doGet(materurl1);
                JSONObject objdata = JSONObject.parseObject(materresults);
                JSONObject data = objdata.getJSONObject("data");
                JSONObject fileobject = new JSONObject();
                String fileName = "";
                String fileUrl = "";
                int count = 0;
                JSONArray jsonFileArray = data.getJSONArray("FILES");
                for (int j=0;j<jsonFileArray.size();j++) {
                    fileobject = jsonFileArray.getJSONObject(j);
                    fileUrl = "http://59.206.96.197:8080/WebDiskServerDemo/doc?doc_id="+fileobject.getString("FILE_PATH");
                    List<FrameAttachInfo> attachlist = attachService.getAttachInfoListByGuid(projectGuid);
                    fileName = fileobject.getString("FILE_NAME");
                    if (attachlist!= null && attachlist.size() > 0) {
                        for (FrameAttachInfo attach : attachlist) {
                           if (fileName.equals(attach.getAttachFileName())) {
                               attach.setUploadDateTime(new Date());
                               Map<String, Object> map = new HashMap<>();
                               map = downloadFile(fileUrl);
                               if(map!=null){
                                   attach.setAttachLength((Long)map.get("length"));
                                   attachService.updateAttach(attach, (InputStream)map.get("stream"));
                               }
                               break;
                           }
                           //system.out.println("对附件进行了更新");
                        }
                    }else {
                        FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                        frameAttachInfo.setAttachGuid( UUID.randomUUID().toString());
                        frameAttachInfo.setCliengGuid(projectGuid);
                        frameAttachInfo.setAttachFileName(fileName);
                        frameAttachInfo.setCliengTag("浪潮结果附件");
                        frameAttachInfo.setUploadDateTime(new Date());
                        Map<String, Object> map = new HashMap<>();
                        map = downloadFile(fileUrl);
                        if(map!=null){
                            frameAttachInfo.setAttachLength((Long)map.get("length"));
                            attachService.addAttach(frameAttachInfo, (InputStream)map.get("stream"));
                        }
                    }
                    count++;
        }
                // 8、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("total", count);
                log.info("=======结束调用getWtResult接口=======");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     *  将inputstream转化为byte
     *  @param inStream
     *  @return
     *  @throws Exception    
     */
    public static byte[] inputStream2Byte(InputStream inStream)
            throws Exception {
        int count = 0;
        while (count == 0) {
            count = inStream.available();
        }
        byte[] b = new byte[count];
        inStream.read(b);
        return b;
    }
    
    
    /**
     * 山东附件库
     * @param url
     * @return
     * @throws Exception
     */
    public static Map<String, Object> downloadFile(String url) throws Exception {
        Map<String, Object> map = new HashMap<>();
        HttpClient client = new HttpClient();
        client.getParams().setContentCharset("UTF-8");
        GetMethod getMethod = new GetMethod(url);
        client.executeMethod(getMethod);
        long len = getMethod.getResponseContentLength();
        InputStream inputStream = getMethod.getResponseBodyAsStream();
        map.put("length", len);
        map.put("stream", inputStream);
        return map;
    }
    
    
    
   /* *//**
     * 
     *  调用浪潮获取附件接口将附件存到附件表中
     *  @param Urlcn
     *  @return
     *  @throws Exception    
     *//*
    public  InputStream downloadFile(String Urlcn) throws Exception {
            String FileURL ="http://59.206.96.197:8080/WebDiskServerDemo/doc?doc_id="+Urlcn;
            URL url = new URL(FileURL);
            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setConnectTimeout(6000);
            urlCon.setReadTimeout(6000);
            int code = urlCon.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                throw new Exception("文件读取失败");
            }
            return urlCon.getInputStream();
    }*/
}
