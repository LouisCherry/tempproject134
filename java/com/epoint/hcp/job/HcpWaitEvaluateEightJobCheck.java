package com.epoint.hcp.job;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
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
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.hcp.api.IHcpService;
import com.epoint.zwdt.util.TARequestUtil;

/**
 *  [实施主体表数据检查]
 *  [功能详细描述]
 * @作者 gowonco
 * @version [版本号, 2018年9月14日]
 * @see [相关类/方法]
 * @since [产品/模块版本] 
 */
public class HcpWaitEvaluateEightJobCheck implements Callable<String>
{

	transient static Logger log = LogUtil.getLog(MethodHandles.lookup().lookupClass());

	private static String HCPCODEURL = ConfigUtil.getConfigValue("hcp", "HcpCodeUrl");
	private static String HCPARRAYCODEURL = ConfigUtil.getConfigValue("hcp", "HcpCodeArrayUrl");
	private static String HCPOFFLINETEMPURL = ConfigUtil.getConfigValue("hcp", "HcpOfflineTempUrl");
	private static String HCPAPPMARK = ConfigUtil.getConfigValue("hcp", "HcpAppMark");
	private static String HCPAPPWORD = ConfigUtil.getConfigValue("hcp", "HcpAppWord");
	
	IHcpService iHcpService = ContainerFactory.getContainInfo().getComponent(IHcpService.class);
	
    /**
     * 线程计数器
     */
    public CountDownLatch latch;

    public HcpWaitEvaluateEightJobCheck() {
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
            log.info("----------------------本次查询评价办件数量---"+recordNum+"------------------");
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
            	List<Record> waitevaluates = iHcpService.getWaitEvaluateEightList(start, size);
                log.info("-------------for-start-banjianpingjia------------------");
                EpointFrameDsManager.begin(null);
                try {
                	 for (Record record : waitevaluates) {
                		//控制时间超过一个小时的进行推送评价数据
//                 		Date applydate = record.getDate("createdate");
//                 		int hours = (int) ((new Date().getTime() - applydate.getTime()) / (1000*3600*24));
//                		if(hours > 1) {
                			String projectno = record.getStr("projectno");
         					String servicenumber = record.getStr("servicenumber");
         					Record evainstance = iHcpService.findEvaluateserviceeight(projectno, servicenumber);
         					if (evainstance != null) {
         						if ("1".equals(evainstance.getStr("sbsign"))) {
         							/*Record res = new Record();
         							res.setSql_TableName("evaservice_record");
         							res.set("rowguid", UUID.randomUUID().toString());
         							res.set("projectguid", projectno);
         							res.set("servicenumber", servicenumber);
         							res.set("status", "4");
         							iHcpService.addEvaluate(res);*/
         							record.set("sbsign", "4");
         							record.set("sberrordesc", "该评价数据已经正常推送");
         							iHcpService.updateProServiceeight(record);
         						} else {
         							String result =  turnEvaluate(record);
         							if("1".equals(result)) {
         								record.set("sbsign", "4");
             							record.set("sberrordesc", "该评价数据已经正常推送");
             							iHcpService.updateProServiceeight(record);
         							}else {
         								record.set("sbsign", "99");
             							record.set("sberrordesc", "该评价数据已经正常推送");
             							iHcpService.updateProServiceeight(record);
         							}
         							
         						}
         					} else {
         						String result = turnEvaluate(record);
         						if("1".equals(result)) {
     								record.set("sbsign", "4");
         							record.set("sberrordesc", "该评价数据已经正常推送");
         							iHcpService.updateProServiceeight(record);
     							}else {
     								record.set("sbsign", "99");
         							record.set("sberrordesc", "该评价数据已经正常推送");
         							iHcpService.updateProServiceeight(record);
     							}
         					}
         					EpointFrameDsManager.commit();
     				}
                	 EpointFrameDsManager.commit();
                }
                catch (Exception e) {
                    EpointFrameDsManager.rollback();
                    e.printStackTrace();
                }
                finally {
                    EpointFrameDsManager.close();
                }
                
                log.info("-------------for-end-banjianpingjia------------------");
                
            }
            finally {
                latch.countDown();
            }
        }
    }
    
    private String turnEvaluate(Record record) {
    	//long startTime = System.currentTimeMillis();
		String projectno = record.getStr("projectno");
		String userName = record.getStr("userName");
		String serviceNumber = record.getStr("serviceNumber");
    			
//    	Record res = new Record();
//		res.setSql_TableName("evaservice_record");
//		res.set("rowguid", UUID.randomUUID().toString());
//		res.set("projectguid", projectno);
//		res.set("servicenumber", serviceNumber);
		
		JSONObject json = new JSONObject();

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
			//log.info("添加评价数据返回结果如下：" + resultOnline);
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
				r.setSql_TableName("evainstanceeight");
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
				//log.info("新增服务评价数据成功");
//				record.set("sbsign", "2");
//				record.set("sberrordesc", "评价数据重新推送成功");
//				iHcpService.updateProService(record);
//				res.set("status", "2");
//				iHcpService.addEvaluate(res);
				
				return "1";
				
			}else
			{
//				record.set("sbsign", "4");
//				record.set("sberrordesc", "评价数据推送成功了");
//				iHcpService.updateProService(record);
//				res.set("status", "4");
//				iHcpService.addEvaluate(res);
				return "0";
			}
		} else {
			return "0";
			//log.info("新增服务评价数据失败,result:" + resultOnline);
//			res.set("status", "3");
//			iHcpService.addEvaluate(res);
//			record.set("sbsign", "3");
//			record.set("sberrordesc", "评价数据重新推送失败");
//			iHcpService.updateProService(record);
		}
		//long endTime = System.currentTimeMillis();
		//log.info("办件推送省好差评运行时间为：" + (endTime - startTime)/1000 + "秒");
	}
    

}
