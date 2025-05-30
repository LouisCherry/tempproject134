package com.epoint.hcp.job;

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
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  [实施主体表数据检查]
 *  [功能详细描述]
 * @作者 gowonco
 * @version [版本号, 2018年9月14日]
 * @see [相关类/方法]
 * @since [产品/模块版本] 
 */
public class HcpWaitEvaluateJobCheck implements Callable<String>
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

    public HcpWaitEvaluateJobCheck() {
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
            	List<Record> waitevaluates = iHcpService.getWaitEvaluateList(start, size);
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
         					Record evainstance = iHcpService.findEvaluateserviceck(projectno, servicenumber);
         					if (evainstance != null) {
         						if ("1".equals(evainstance.getStr("sbsign"))) {
         							record.set("sbsign", "4");
         							record.set("sberrordesc", "该评价数据已经正常推送");
         							iHcpService.updateProService(record,"ck");
         						} else {
         							String result =  turnEvaluate(record);
         							if("1".equals(result)) {
         								record.set("sbsign", "4");
             							record.set("sberrordesc", "该评价数据已经正常推送");
             							iHcpService.updateProService(record,"ck");
         							}else {
         								record.set("sbsign", "99");
             							record.set("sberrordesc", "该评价数据已经正常推送");
             							iHcpService.updateProService(record,"ck");
         							}
         							
         						}
         					} else {
         						String result = turnEvaluate(record);
         						if("1".equals(result)) {
     								record.set("sbsign", "4");
         							record.set("sberrordesc", "该评价数据已经正常推送");
         							iHcpService.updateProService(record,"ck");
     							}else {
     								record.set("sbsign", "99");
         							record.set("sberrordesc", "该评价数据已经正常推送");
         							iHcpService.updateProService(record,"ck");
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
		String projectno = record.getStr("projectno");
		String userName = record.getStr("userName");
		String serviceNumber = record.getStr("serviceNumber");
		String taskType = record.getStr("taskType");
    			
		Date date = new Date();

        Calendar c = new GregorianCalendar();
        c.setTime(date);//设置参数时间
        c.add(Calendar.SECOND, -30);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
        date = c.getTime(); //这个时间就是日期往后推一天的结果
        String newassessTime = EpointDateUtil.convertDate2String(date, "yyyy-MM-dd HH:mm:ss");
        
		
		JSONObject json = new JSONObject();

		json.put("projectNo", projectno);
		
		json.put("taskType", taskType);

		json.put("satisfaction", "5");

		json.put("pf", "1");

		json.put("name", userName);

		json.put("evalDetail", "510,517");

		json.put("writingEvaluation", "");

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
		r.setSql_TableName("evainstance_ck");
		String[] primarykeys = { "projectno", "assessNumber" };
		r.setPrimaryKeys(primarykeys);
		r.set("Rowguid", UUID.randomUUID().toString());
		r.set("Flag", "I");
		r.set("Appstatus", Integer.valueOf(0));
		r.set("projectno", projectno);
		r.set("areacode", record.getStr("areacode"));
		r.set("Datasource", "165");
		r.set("Assessnumber", Integer.valueOf(1));
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
				log.info("窗口好差评评价服务："+result);
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
						return "1";
					}else {
						r.set("sbsign", "1");
						r.set("sberrordesc", "同步成功");
						iHcpService.addEvaluate(r);
						EpointFrameDsManager.commit();
						return "1";
					}
					
				}else {
					r.set("sbsign", "99");
					r.set("sberrordesc", code);
					iHcpService.addEvaluate(r);
					EpointFrameDsManager.commit();
					log.info("评价数据推送失败！");
					return "0";
				}
			}
			else {
				return "0";
			}
		} catch (Exception e) {
			r.set("sbsign", "98");
			r.set("sberrordesc", "接口调用失败");
			iHcpService.addEvaluate(r);
			EpointFrameDsManager.commit();
			log.info("评价数据推送失败！");
			e.printStackTrace();
			return "0";
		}
	}
    

}
