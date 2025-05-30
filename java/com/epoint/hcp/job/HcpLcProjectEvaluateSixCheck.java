package com.epoint.hcp.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.hcp.api.IHcpService;
import com.epoint.hcp.api.entity.lcprojectsix;
import com.epoint.zwdt.util.TARequestUtil;

/**
 *  [实施主体表数据检查]
 *  [功能详细描述]
 * @作者 gowonco
 * @version [版本号, 2018年9月14日]
 * @see [相关类/方法]
 * @since [产品/模块版本] 
 */
public class HcpLcProjectEvaluateSixCheck implements Callable<String>
{

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(this.getClass());
    
    private static String HCPEVALUATE = ConfigUtil.getConfigValue("hcp", "HcpEvaluateUrlOld");
    private static String HCPARRAYCODEURL = ConfigUtil.getConfigValue("hcp", "HcpCodeArrayUrl");
	private static String HCPOFFLINETEMPURL = ConfigUtil.getConfigValue("hcp", "HcpOfflineTempUrl");
	private static String HCPAPPMARK = ConfigUtil.getConfigValue("hcp", "HcpAppMark");
	private static String HCPAPPWORD = ConfigUtil.getConfigValue("hcp", "HcpAppWord");
	private static String HCPCODEURL = ConfigUtil.getConfigValue("hcp", "HcpCodeUrl");
    
    
    IHcpService iHcpService = ContainerFactory.getContainInfo().getComponent(IHcpService.class);
	
	IOuService ouservice = ContainerFactory.getContainInfo().getComponent(IOuService.class);
	
	IAuditProject auditProjectServcie = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
	
	IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
	
    /**
     * 错误数据rowguid
     */
    @SuppressWarnings("unused")
    private List<String> errRowguidList;
    /**
     * 线程计数器
     */
    public CountDownLatch latch;

    public HcpLcProjectEvaluateSixCheck() {
        super();
    }

    public void dealResult() {
        int recordNum = 1000;
        boolean flag = true;
        
        //List<String> gssOrgList = service.getGssRowguids();
        ExecutorService exService = null;
        int size = 100;
        int start = 0;
        if (recordNum % size == 0) {
            latch = new CountDownLatch(recordNum / size);
        }
        else {
            latch = new CountDownLatch(recordNum / size + 1);
        }
        exService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        while (flag) {
            log.info("----------------------本次查询导入办件数量---"+recordNum+"------------------");
            if (recordNum <= size) {
                flag = false;
                size = recordNum;
            }
            exService.execute(new Task(start, size));
            recordNum = recordNum - size;
            start = start + size;
            log.info("-------------while-end-zone------------------");
        }
        exService.shutdown();
        try {
            latch.await();
            log.info(">>>>>>>>>>>>>>>>>>>>check-end-zone>>>>>>>>>>>>>>>>>");
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0;) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String call() throws Exception {
        dealResult();
        return "check-zone";
    }

    class Task implements Runnable
    {

        public int size;
        public int start;

        public Task(int start, int size) {
            this.size = size;
            this.start = start;
        }

        @Override
        public void run() {
            try {
                List<lcprojectsix> projects = iHcpService.getLcEvaluateSbSixList(start, size);
                log.info("-------------for-start-zoneCheck------------------");
                EpointFrameDsManager.begin(null);
                try {
                	for (lcprojectsix project : projects) {
                		
                		Date date = new Date();
   			            Calendar c = new GregorianCalendar();
   			            c.setTime(date);//设置参数时间
   			            c.add(Calendar.MINUTE,-6);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
   			            date=c.getTime(); //这个时间就是日期往后推一天的结果
   			            String newserviceTime = EpointDateUtil.convertDate2String(date, "yyyy-MM-dd HH:mm:ss");
   			            
   			            Date date1 = new Date();
   			            Calendar c1 = new GregorianCalendar();
   			            c1.setTime(date1);//设置参数时间
   			            c1.add(Calendar.MINUTE,-5);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
   			            date1=c1.getTime(); //这个时间就是日期往后推一天的结果
   			            String newserviceTime1 = EpointDateUtil.convertDate2String(date1, "yyyy-MM-dd HH:mm:ss");
   			            
   			            Date date2 = new Date();
			            Calendar c2 = new GregorianCalendar();
			            c2.setTime(date2);//设置参数时间
			            c2.add(Calendar.MINUTE,-4);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
			            date2=c2.getTime(); //这个时间就是日期往后推一天的结果
			            String newserviceTime2 = EpointDateUtil.convertDate2String(date2, "yyyy-MM-dd HH:mm:ss");
   			            
   			            
   						String s = turnhcpevaluate(project, 1,"提交申请信息",newserviceTime);
   						EpointFrameDsManager.commit();
   						String ss = turnhcpevaluate(project, 2,"待受理",newserviceTime1);
   						EpointFrameDsManager.commit();
   						String sss = turnhcpevaluate(project, 3,"出证办结",newserviceTime2);
   						EpointFrameDsManager.commit();
   						String status = "1";
   						if("1".equals(s)) {
   							status += "1";
   						}else {
   							status += "97,";
   						}
   						if("1".equals(ss)) {
   							status += "1";
   						}else {
   							status += "98,";
   						}
   						if("1".equals(sss)) {
   							status += "1";
   						}else {
   							status += "99,";
   						}
   						
   						iHcpService.updateLcProjectsix(status, project.getRowguid());
   						EpointFrameDsManager.commit();
   				}
                	EpointFrameDsManager.commit();
                   log.info("-------------for-end-zoneCheck------------------");
                }
                catch (Exception e) {
                    EpointFrameDsManager.rollback();
                    e.printStackTrace();
                }
                finally {
                    EpointFrameDsManager.close();
                }
                log.info("-------------for-end-zoneCheck------------------");
                
            }
            finally {
                latch.countDown();
            }
        }
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
    public String turnhcpevaluate(lcprojectsix auditProject, int serviceNumber,String servicename,String newserviceTime) {
    	AuditTask auditTask = iAuditTask.getUseTaskAndExtByTaskid(auditProject.getTaskid()).getResult();
		if (auditTask != null) {
			
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
			    
				//log.info("=====================开始推送社保办件服务数据=================");
	            JSONObject json = new JSONObject();
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
	            json.put("taskCode", auditTask.getItem_id());
	            json.put("areaCode", auditProject.getAreacode().replace("370882", "370812")+"000000");
	            json.put("taskName", auditTask.getTaskname());
	            json.put("projectNo", auditProject.getFlowsn());
	            String proStatus = serviceNumber+"";
	            String acceptdate = EpointDateUtil.convertDate2String(auditProject.getApplydate(), "yyyy-MM-dd HH:mm:ss");
	            json.put("proStatus", proStatus);
	            json.put("orgcode", auditProject.getAreacode().replace("370882", "370812") + "000000_" + deptcode);
	            json.put("ouguid",ouguid);
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
	            json.put("userName", StringUtil.isNotBlank(auditProject.getApplyername()) ? auditProject.getApplyername().trim() : "无");
	            json.put("userPageType", "111");
	            json.put("proManager", StringUtil.isNotBlank(auditProject.getAcceptusername()) ? auditProject.getAcceptusername().trim() : "无");
	            json.put("certKey", StringUtil.isNotBlank(auditProject.getCertnum()) ? auditProject.getCertnum().trim() : "0");
	            json.put("certKeyGOV", StringUtil.isNotBlank(auditProject.getCertnum()) ? auditProject.getCertnum().trim() : "0");
	            json.put("serviceName", servicename);// 环节名称
	            
	            json.put("serviceNumber", serviceNumber);
		        json.put("serviceTime", newserviceTime);
	            json.put("projectType", auditProject.getTasktype());
	            if (3 == Integer.parseInt(proStatus)) {
	                json.put("resultDate",
	                        EpointDateUtil.convertDate2String(auditProject.getBanjiedate(), "yyyy-MM-dd HH:mm:ss"));
	            }
	            json.put("taskType", auditProject.getTasktype());
	            json.put("mobile", StringUtil.isNotBlank(auditProject.getContactmobile()) ? auditProject.getContactmobile().trim() : "0");
	            json.put("deptCode", deptcode);
	            json.put("projectName", "关于" + auditProject.getApplyername().trim() + auditTask.getTaskname() + "的业务");
	            json.put("creditNum", StringUtil.isNotBlank(auditProject.getCertnum()) ? auditProject.getCertnum().trim() : "0");
	            // 默认证照类型为身份证
	            json.put("creditType", "111");
	            json.put("promiseDay", auditTask.getPromise_day() + "");
	            // 默认办结时间单位为工作日
	            json.put("anticipateDay", "1");
	            // 线上评价为1
	            json.put("proChannel", "2");
	            json.put("month", "six");
	            json.put("promiseTime", auditTask.getType() + "");
	            //log.info("社保办件数据加密前入参：" + json.toString());
	            JSONObject submit = new JSONObject();
	            submit.put("params", json);
	            String resultsign = TARequestUtil.sendPostInner(HCPEVALUATE, submit.toJSONString(), "", "");
	            //log.info("resultsign:"+resultsign);
	            if (StringUtil.isNotBlank(resultsign)) {
	                JSONObject result = JSONObject.parseObject(resultsign);
	                JSONObject custom = result.getJSONObject("custom");
	                if ("1".equals(custom.getString("code"))) {
	                	turnEvaluate(taskType,auditProject.getAreacode().replace("370882", "370812"),"37"+auditProject.getFlowsn(),StringUtil.isNotBlank(auditProject.getApplyername()) ? auditProject.getApplyername().trim() : "无", String.valueOf(serviceNumber));
	    				return "1";
	        			//auditProject.set("hcpstatus", "1");
	        			//iHcpService.updateLcProject("1", auditProject.getRowguid());
	                	//log.info("社保保存办件服务数据成功:" + auditProject.getFlowsn());
	                }
	                else {
	                	//iHcpService.updateLcProject("3", auditProject.getRowguid());
//	    				r.set("status", "2");
//	    				iHcpService.addEvaluate(r);
	                	turnEvaluate(taskType,auditProject.getAreacode().replace("370882", "370812"),"37"+auditProject.getFlowsn(),StringUtil.isNotBlank(auditProject.getApplyername()) ? auditProject.getApplyername().trim() : "无", String.valueOf(serviceNumber));
	                	log.info("社保保存办件服务数据失败:" + auditProject.getFlowsn()+"原因："+resultsign);
	                	return "0";
	                	
	                }
	            }
	            else {
//	            	r.set("status", "2");
//    				iHcpService.addEvaluate(r);
                	turnEvaluate(taskType,auditProject.getAreacode().replace("370882", "370812"),"37"+auditProject.getFlowsn(),StringUtil.isNotBlank(auditProject.getApplyername()) ? auditProject.getApplyername().trim() : "无", String.valueOf(serviceNumber));
	            	log.info("社保保存办件服务数据失败：" + auditProject.getFlowsn()+"原因："+resultsign);
	            	return "0";
//	            	
	            }
		}else {
			return "0";
			//status=2表示事项找不到
//			r.set("status", "2");
//			iHcpService.addEvaluate(r);
		}
       
    }
    
    private void turnEvaluate(String taskType,String areacode,String projectno,String userName,String serviceNumber) {
		JSONObject json = new JSONObject();

		json.put("taskType", taskType);
		
		json.put("projectNo", projectno);

		json.put("satisfaction", "5");

		json.put("pf", "1");

		json.put("name", userName);

		json.put("evalDetail", "510,517");

		json.put("writingEvaluation", "");
		
		Date date = new Date();

        Calendar c = new GregorianCalendar();
        c.setTime(date);//设置参数时间
        c.add(Calendar.HOUR, -1);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
        date = c.getTime(); //这个时间就是日期往后推一天的结果
        String newassessTime = EpointDateUtil.convertDate2String(date, "yyyy-MM-dd HH:mm:ss");
			

        
		json.put("assessTime", newassessTime);

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

		Record r = new Record();
		r.setSql_TableName("evainstancesix");
		String[] primarykeys = { "projectno", "assessNumber" };
		r.setPrimaryKeys(primarykeys);
		r.set("Rowguid", UUID.randomUUID().toString());
		r.set("Flag", "I");
		r.set("Appstatus", Integer.valueOf(0));
		r.set("projectno", projectno);
		r.set("areacode", areacode);
		r.set("Datasource", "165");
		r.set("isdefault", "0");
		r.set("EffectivEvalua", "1");
		r.set("Evalevel", "5");
		r.set("Evacontant", "");
		r.set("evalDetail", "510,517");
		r.set("writingEvaluation", "");
		r.set("Promisetime", "1");
		r.set("createDate", new Date());
		r.set("sync_sign", "0");
		r.set("answerStatus", "0");
		r.set("pf", "1");
		r.set("satisfaction", "5");
		r.set("assessTime",  newassessTime);
		r.set("assessNumber", Integer.valueOf(serviceNumber));
		
		JSONObject result = new JSONObject();
		String resultOnline = "";
		try {
			resultOnline = HttpUtil.doPostJson(HCPOFFLINETEMPURL, submitString.toString());
			//log.info("添加评价数据返回结果如下：" + resultOnline);
			if (StringUtil.isNotBlank(resultOnline)) {
				result = JSONObject.parseObject(resultOnline);
				String code = result.getString("C-Response-Desc");
				JSONObject body = result.getJSONObject("C-Response-Body");
				if ("success".equals(code)) {
					String status = body.getString("success");
					if ("false".equals(status)) {
						String message = body.getString("message");
						r.set("sbsign", "98");
						r.set("sberrordesc", message);
						iHcpService.addEvaluate(r);
						EpointFrameDsManager.commit();
					}else {
						r.set("sbsign", "1");
						r.set("sberrordesc", "同步成功");
						iHcpService.addEvaluate(r);
						EpointFrameDsManager.commit();
					}
					
				}else {
					r.set("sbsign", "99");
					r.set("sberrordesc", code);
					iHcpService.addEvaluate(r);
					EpointFrameDsManager.commit();
					log.info("评价数据推送失败！");
				}
			}
		} catch (Exception e) {
			r.set("sbsign", "98");
			r.set("sberrordesc", "接口调用失败");
			iHcpService.addEvaluate(r);
			EpointFrameDsManager.commit();
			log.info("评价数据推送失败！");
			e.printStackTrace();
		}
	}
    
    
    /**
     * 加减对应时间后的日期
     *
     * @param date   需要加减时间的日期
     * @param amount 加减的时间(毫秒)
     * @return 加减对应时间后的日期
     */
    private Date subtractTime(Date date, int amount) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strTime = sdf.format(date.getTime() + amount);
            Date time = sdf.parse(strTime);
            return time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    

}
