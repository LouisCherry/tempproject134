package com.epoint.tongbufw;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.util.HttpRequestUtils;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.hcp.api.IHcpService;
import com.epoint.util.TARequestUtil;
import com.epoint.workflow.bizlogic.domain.execute.WorkflowWorkItemHistory;

@DisallowConcurrentExecution
public class ProjectdownForBdc implements Job
{
    transient Logger log = LogUtil.getLog(ProjectdownForBdc.class);
    //浪潮前置库有用字段
    private final String fields = " DISTINCT projid,gtsync,PROJID,PROJPWD,REGION_ID,STDVER,ITEMNAME,"
            + "PROJECTNAME,APPLICANT,APPLICANTMOBILE,APPLICANTADDRESS,APPLICANTCARDCODE,APPLICANTTEL,APPLICANTEMAIL,ACCEPTDEPTNAME,ACCEPTDEPTID,"
            + "APPROVALTYPE,PROMISETIMELIMIT,PROMISETIMEUNIT,SUBMIT,OCCURTIME,TRANSACTOR,MAKETIME,INNERNO ";

    private static String HCPEVALUATE = ConfigUtil.getConfigValue("hcp", "HcpEvaluateUrl");
    
    private static String HCPCODEURL = ConfigUtil.getConfigValue("hcp", "HcpCodeUrl");
	private static String HCPARRAYCODEURL = ConfigUtil.getConfigValue("hcp", "HcpCodeArrayUrl");
	private static String HCPOFFLINETEMPURL = ConfigUtil.getConfigValue("hcp", "HcpOfflineTempUrl");
	private static String HCPAPPMARK = ConfigUtil.getConfigValue("hcp", "HcpAppMark");
	private static String HCPAPPWORD = ConfigUtil.getConfigValue("hcp", "HcpAppWord");
	
    
    /**
     * 程序入口
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            //job作业比框架起的早 导致部分接口没有实例化
            Thread.sleep(30000);
            syncProjectinfo();
            EpointFrameDsManager.commit();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }

    //同步浪潮对接的自建系统的办件信息
    private void syncProjectinfo() {
    	ProjectdownForBdcService service = new ProjectdownForBdcService();
        List<Record> infolist = service.getInfoFromQzk(fields);
        log.info("开始同步浪潮对接的自建系统的办件信息");
        for (Record record : infolist) {
            String flowsn = record.getStr("PROJID");
            String taskname = record.getStr("ITEMNAME");
            String areacode = record.getStr("REGION_ID").substring(0, 6);
            String ACCEPTDEPTNAME = record.getStr("ACCEPTDEPTNAME");
            if (ACCEPTDEPTNAME.contains("高新")) {
                areacode = "370890";
            }else if (ACCEPTDEPTNAME.contains("经济开发")) {
                areacode = "370892";
            }else if (ACCEPTDEPTNAME.contains("北湖")) {
                areacode = "370891";
            }
            AuditTask task = service.gettaskbydeptask(taskname,areacode);
            log.info(taskname+"，事项已匹配到，开始进行办件同步");
            try {
                if (task != null && StringUtil.isNotBlank(task)) {
                    AuditProject project = service.getProjectByflowsn(flowsn);
                    String rowguid = "";
                    String pviguid = "";
                    IAuditProject projectService = (IAuditProject) ContainerFactory.getContainInfo()
                            .getComponent(IAuditProject.class);
                    if (project == null) {
                        //system.out.println("开始新增project");
                        rowguid = UUID.randomUUID().toString();
                        pviguid = UUID.randomUUID().toString();
                        project = new AuditProject();
                        project.setRowguid(rowguid);
                        project.setPviguid(pviguid);
                        project.setFlowsn(flowsn);
                        project.setAreacode(areacode);
                        int applyway = record.getInt("SUBMIT");
                        switch (applyway) {
                            case 0:
                                applyway = 20;
                                break;
                            case 1:
                                applyway = 10;
                                break;
                            default:
                                applyway = 10;
                                break;
                        }
                        project.setApplyway(applyway);
                        project.setReceivedate(record.getDate("MAKETIME"));
                        project.setReceiveusername(record.getStr("TRANSACTOR"));
                        project.setAcceptuserdate(record.getDate("MAKETIME"));
                        project.setAcceptusername(record.getStr("TRANSACTOR"));
                        project.setProjectname(task.getTaskname());
                        project.setApplydate(record.getDate("OCCURTIME"));
                        project.setApplyername(record.getStr("APPLICANT"));
                        project.setCertnum(record.getStr("APPLICANTCARDCODE"));
                        project.setContactmobile(record.getStr("APPLICANTMOBILE"));
                        project.setContactphone(record.getStr("APPLICANTTEL"));
                        project.setContactemail(record.getStr("APPLICANTEMAIL"));
                        project.setApplyertype(10);
                        project.setOuguid(task.getOuguid());
                        project.setOuname(task.getOuname());
                        project.setOperatedate(new Date());
                        project.setTask_id(task.getTask_id());
                        project.setTasktype(task.getType());
                        project.setTaskguid(task.getRowguid());
                        project.setCerttype(ZwfwConstant.CERT_TYPE_ZZJGDMZ);
                        project.setStatus(80);
                        project.setOperateusername("浪潮自建部门数据");
                        //is_lczj null,0：一窗受理系统办件，1，浪潮对接自建办件，2：新点对接济宁市自建办件,5:不动产办件
                        project.set("is_lczj", 5);
                        projectService.addProject(project);
                        // 推送好差评办件服务数据
                        turnhcpevaluate(task, project, 1,"提交申请信息");
                        
                        service.updateInfoFlagByFlowsn("1", flowsn);
                    }
                    else {
                        rowguid = project.getRowguid();
                        pviguid = project.getPviguid();
                        service.updateInfoFlagByFlowsn("9", flowsn);
                    }
                    List<Record> procinfolist = service.getProcFromQzkByProjid(flowsn);
                    for (Record record2 : procinfolist) {
                        //插入流程信息表
                        WorkflowWorkItemHistory workflow = new WorkflowWorkItemHistory();
                        workflow.setActivityName(record2.getStr("NODENAME"));
                        workflow.setWorkItemGuid(UUID.randomUUID().toString());
                        workflow.setTransactorName(record2.getStr("NODEPROCERNAME"));
                        workflow.setCreateDate(record2.getDate("NODESTARTTIME"));
                        workflow.setEndDate(record2.getDate("NODEENDTIME"));
                        workflow.setOpinion(record2.getStr("NODEADV"));
                        workflow.setOperatorForDisplayName(record2.getStr("NODEPROCERNAME"));
                        workflow.setOperationDate(new Date());
                        workflow.setProcessGuid(task.getProcessguid());
                        workflow.setProcessVersionInstanceGuid(pviguid);
                        workflow.setOperatorName(record2.getStr("NODENAME"));
                        workflow.set("projectguid", rowguid);
                        service.updateProcFlagByFlowsn("1",flowsn,record2.getInt("sn"));
                        service.insertbyrecord(workflow);
                    }
                }
                else {
                    service.updateInfoFlagByFlowsn("2", flowsn);
                    log.info("同步浪潮自建系统数据 =====办件对应事项不存在:" + taskname + "办件编号为：" + flowsn +"辖区："+areacode);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                service.updateInfoFlagByFlowsn("3", flowsn);
                log.info("同步浪潮自建系统数据失败 =====" + e.getMessage()+record);

            }
        }
        log.info("同步浪潮自建系统数据结束"+infolist.size());
    }

    /**
     * 
     *  [推送好差评办件服务数据] 
     *  @param auditTask
     *  @param auditProject
     *  @param serviceNumber    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void turnhcpevaluate(AuditTask auditTask, AuditProject auditProject, int serviceNumber,String servicename) {
        try {
            JSONObject json = new JSONObject();
            IOuService ouservice = ContainerFactory.getContainInfo().getComponent(IOuService.class);
            
            String taskType = auditTask.getShenpilb();
		    // 公共服务类型不一致，优先转换
		    if ("11".equals(taskType)) {
		        taskType = "20";
		    }
		    switch (taskType) {
		        case "01":
		        case "05":
		        case "07":
		        case "08":
		        case "09":
		        case "10":
		        case "20":
		            break;
		        default:
		            taskType = "99";
		            break;
		    }
		    
		    
            String ouguid = auditProject.getOuguid();
            FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(ouguid);
            String deptcode = "";
            if (frameOuExten != null) {
                deptcode = frameOuExten.getStr("orgcode");
                if(StringUtil.isBlank(deptcode)) {
               	 deptcode = "11370900MB28449441";
               }
            }
            else {
                deptcode = "11370900MB28449441";
            }

            String areacode = auditProject.getAreacode();
            if ("370882".equals(areacode)) {
            	areacode = "370812";
            }
            json.put("taskCode", auditTask.getItem_id());
            json.put("areaCode", areacode+"000000");
            json.put("taskName", auditTask.getTaskname());
            json.put("projectNo", auditProject.getFlowsn());
            String proStatus = serviceNumber+"";
            Integer status = auditProject.getStatus();
//            if (status < 30) {
//                proStatus = "1";
//            }else if (status >= 30 && status< 90) {
//                proStatus = "2";
//            }else if (status >= 90) {
//                proStatus = "3";
//            }else {
//                proStatus = "1";
//            }
            String acceptdate = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss");
            json.put("proStatus", proStatus);
            json.put("orgcode", areacode + "000000_" + deptcode);
            json.put("ouguid", auditProject.getOuguid());
            // json.put("orgcode", "370900000000_11370900004341048Y");
            json.put("orgName", auditProject.getOuname());
            json.put("acceptDate", acceptdate);
            Integer applyertype = auditProject.getApplyertype();
            if (applyertype == 10) {
                applyertype = 2;
            }
            else if (applyertype == 20) {
                applyertype = 1;
            }
            else if (applyertype == 30) {
                applyertype = 9;
            }
            else {
                applyertype = 9;
            }
            json.put("userProp", applyertype);
            json.put("userName", StringUtil.isNotBlank(auditProject.getApplyername()) ? auditProject.getApplyername().trim() : "无" );
            json.put("userPageType", "111");
            json.put("proManager", auditProject.getAcceptusername());
            json.put("certKey", auditProject.getCertnum());
            json.put("certKeyGOV", auditProject.getCertnum());
            String auditServiceName = "";
            json.put("serviceName", servicename);// 环节名称
            

            String serviceNumUrl = ConfigUtil.getConfigValue("hcp", "HcpServiceNumber");

            JSONObject evJson = new JSONObject();
            evJson.put("commented", "0");
            evJson.put("mark", "0");
            evJson.put("userName", auditProject.getApplyername());
            evJson.put("creditType", 111);
            evJson.put("creditNum", auditProject.getCertnum());
            // evJson.put("commented", "0");
            String getServiceNumber = HttpRequestUtils.sendPost(serviceNumUrl, evJson.toString(),
                    new String[] {"application/json;charset=utf-8" });
            JSONObject jsonN = JSONObject.parseObject(getServiceNumber);
            String content = jsonN.getString("content");
            JSONArray jsonArr = JSONArray.parseArray(content);
            json.put("serviceNumber", serviceNumber);
            
            Calendar c = new GregorianCalendar();
            Date date = new Date();
            c.setTime(date);//设置参数时间
            c.add(Calendar.SECOND,-20);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
            date=c.getTime(); //这个时间就是日期往后推一天的结果
            json.put("serviceTime", EpointDateUtil.convertDate2String(date, "yyyy-MM-dd HH:mm:ss"));
            
            json.put("projectType", auditProject.getTasktype());
            if (3 == Integer.parseInt(proStatus)) {
                json.put("resultDate",
                        EpointDateUtil.convertDate2String(auditProject.getBanjiedate(), "yyyy-MM-dd HH:mm:ss"));
            }
            json.put("tasktype", auditProject.getTasktype());
            json.put("mobile", auditProject.getContactmobile());
            json.put("deptCode", deptcode);
            json.put("projectName", "关于" + auditProject.getApplyername() + auditTask.getTaskname() + "的业务");
            json.put("creditNum", auditProject.getCertnum());
            // 默认证照类型为身份证
            json.put("creditType", "111");
            json.put("promiseDay", auditTask.getPromise_day() + "");
            // 默认办结时间单位为工作日
            json.put("anticipateDay", "1");
            // 线上评价为1
            json.put("proChannel", "2");
            json.put("promiseTime", auditTask.getType() + "");
            log.info("办件数据加密前入参：" + json.toString());
            JSONObject submit = new JSONObject();
            submit.put("params", json);
            String resultsign = TARequestUtil.sendPostInner(HCPEVALUATE, submit.toJSONString(), "", "");
            if (StringUtil.isNotBlank(resultsign)) {
                JSONObject result = JSONObject.parseObject(resultsign);
                if ("success".equals(result.getString("custom"))) {
                	Record record = new Record();
                	record.set("projectno", "37" + auditProject.getFlowsn());
                	record.set("userName", servicename);
                	record.set("serviceNumber", serviceNumber);
                	turnEvaluate(record,taskType);
                	
                	log.info("保存办件服务数据成功:" + auditProject.getFlowsn());
                }
                else {
                	log.info("保存办件服务数据失败:" + auditProject.getFlowsn());
                }
            }
            else {

            	log.info("保存办件服务数据失败：" + auditProject.getFlowsn());
            }
        }
        catch (Exception e) {
        	 e.printStackTrace();
        }
       
    }
    
    private void turnEvaluate(Record record,String taskType) {
    	
    	IHcpService iHcpService = ContainerFactory.getContainInfo().getComponent(IHcpService.class);
    	
		String projectno = record.getStr("projectno");
		String userName = record.getStr("userName");
		String serviceNumber = record.getStr("serviceNumber");
		JSONObject json = new JSONObject();

		json.put("taskType", taskType);
		
		json.put("projectNo", projectno);

		json.put("satisfaction", "5");

		json.put("pf", "1");

		json.put("name", userName);

		json.put("evalDetail", "510,517");

		json.put("writingEvaluation", "");

		json.put("assessTime", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));

		json.put("serviceNumber", serviceNumber);

		JSONObject jsonObjectOnline = new JSONObject();
		JSONObject jsonObject1 = new JSONObject();
		List<JSONObject> list = new ArrayList<>();
		List<String> list1 = new ArrayList<>();
		list.add(json);
		//log.info("线下新增评价加密前入参：" + list.toString());
		JSONObject submit = new JSONObject();
		Map<String, String> contentsign = new HashMap<String, String>();
		contentsign.put("evaluate", list.toString());
		jsonObject1.put("content", contentsign);
		submit.put("params", jsonObject1);
		String resultsign = TARequestUtil.sendPostInner(HCPARRAYCODEURL, submit.toJSONString(), "", "");
		JSONObject json1 = new JSONObject();
		if (!"修改用户默认地址失败".equals(resultsign)) {
			json1 = JSON.parseObject(resultsign);
		}
		list1.add(json1.getString("signcontent"));
		jsonObjectOnline.put("evaluate", list1);

		//log.info("办件数据加密后入参：" + resultsign);
		JSONObject contentOnlineMap = new JSONObject();
		String time = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHHmmss");
		//log.info("办件数据加密前时间：" + time);

		JSONObject submit1 = new JSONObject();
		Map<String, String> contentsign1 = new HashMap<String, String>();
		contentsign1.put("content", time + HCPAPPMARK + HCPAPPWORD);
		submit1.put("params", contentsign1);

		String resultsign1 = TARequestUtil.sendPostInner(HCPCODEURL, submit1.toJSONString(), "", "");
		JSONObject json2 = new JSONObject();
		if (!"修改用户默认地址失败".equals(resultsign1)) {
			json2 = JSON.parseObject(resultsign1);
		}

		contentOnlineMap.put("sign", json2.getString("signcontent"));
		contentOnlineMap.put("params", jsonObjectOnline);
		contentOnlineMap.put("time", time);
		contentOnlineMap.put("appMark", HCPAPPMARK);
		JSONObject submitString = new JSONObject();
		submitString.put("txnBodyCom", contentOnlineMap);
		submitString.put("txnCommCom", new JSONObject());
		//log.info("办件数据所有入参：" + contentOnlineMap.toString());
		//log.info("办件数据url：" + HCPOFFLINETEMPURL);

		String resultOnline = "";
		try {
			resultOnline = HttpUtil.doPostJson(HCPOFFLINETEMPURL, submitString.toString());
			log.info("添加评价数据返回结果如下：" + resultOnline);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject result = new JSONObject();
		JSONObject dataJson = new JSONObject();
		if (StringUtil.isNotBlank(resultOnline)) {
			result = JSONObject.parseObject(resultOnline);
			String code = result.getString("C-Response-Desc");
			if ("success".equals(code)) {
				Record r = new Record();
				r.setSql_TableName("evainstance");
				String[] primarykeys = { "projectno", "assessNumber" };
				r.setPrimaryKeys(primarykeys);
				r.set("Rowguid", UUID.randomUUID().toString());
				r.set("Flag", "I");
				r.set("Appstatus", Integer.valueOf(0));
				r.set("projectno", projectno);
				r.set("Datasource", "165");
				r.set("Assessnumber", Integer.valueOf(1));
				r.set("isdefault", "0");
				r.set("EffectivEvalua", "1");
				r.set("Areacode", record.getStr("areacode"));
				r.set("Prostatus", record.getStr("proStatus"));
				r.set("Evalevel", "5");
				r.set("Evacontant", "");
				r.set("evalDetail", "510,517");
				r.set("writingEvaluation", "");
				r.set("Taskname", record.getStr("Taskname"));
				r.set("Taskcode", record.getStr("Taskcode"));
				r.set("Promisetime", "1");
				r.set("Deptcode", record.getStr("orgcode"));
				r.set("Userprop", record.getStr("Userprop"));
				r.set("Username", record.getStr("Username"));
				r.set("Applicant", record.getStr("Username"));
				r.set("Certkey", record.getStr("Certkey"));
				r.set("Certkeygov", record.getStr("Certkeygov").trim());
				r.set("Acceptdate", record.getStr("Acceptdate"));
				r.set("createDate", new Date());
				r.set("sync_sign", "0");
				r.set("answerStatus", "0");
				r.set("pf", "1");
				r.set("satisfaction", "5");
				r.set("assessTime", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				r.set("assessNumber", Integer.valueOf(serviceNumber));
				r.set("sbsign", "1");
				r.set("sberrordesc", "同步成功");
				iHcpService.addEvaluate(r);
				dataJson.put("success", "success");
				log.info("新增服务评价数据成功");
				record.set("sbsign", "2");
				record.set("sberrordesc", "评价数据重新推送成功");
				iHcpService.updateProService(record);
			}else
			{
				record.set("sbsign", "4");
				record.set("sberrordesc", "评价数据推送成功了");
				iHcpService.updateProService(record);
			}
		} else {
			//log.info("新增服务评价数据失败,result:" + resultOnline);
			record.set("sbsign", "3");
			record.set("sberrordesc", "评价数据重新推送失败");
			iHcpService.updateProService(record);
		}
	}
    
}
