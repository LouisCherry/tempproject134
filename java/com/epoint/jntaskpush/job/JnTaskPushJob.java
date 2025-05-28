package com.epoint.jntaskpush.job;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.jnzwfw.jntask.api.IJnTaskService;
import com.epoint.jnzwfw.jntask.api.entity.JnTask;

@DisallowConcurrentExecution
public class JnTaskPushJob implements Job
{
    transient Logger log = LogUtil.getLog(JnTaskPushJob.class);

    /**
     * 程序入口
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            //job作业比框架起的早 导致部分接口没有实例化
            Thread.sleep(30000);
            doService();
            EpointFrameDsManager.commit();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }

    //同步一窗受理系统的办件信息
    private void doService() {
        try {
            IJnTaskService service = ContainerFactory.getContainInfo().getComponent(IJnTaskService.class);
            String sql = "SELECT rowguid,task_id,item_id,ouname,taskname FROM audit_task WHERE areacode = '370800' AND is_pushjn IS NULL AND shenpilb = '01' "; 
            	sql += "AND IS_EDITAFTERIMPORT = 1 AND IS_ENABLE = 1 AND (IS_HISTORY = 0 OR IS_HISTORY IS NULL) LIMIT 0,100";
            List<AuditTask> list = service.findList(sql);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = sdf.format(new Date());
            log.info("【推送济宁大汉事项】开始同步audit_task表数据："+list.size());
            if (list != null && list.size() > 0) {
                for (AuditTask info : list) {
                    if (info != null) {
                    	StringBuffer xml = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?><dahan><index><iid><![CDATA[");
                		String item_id = info.getStr("item_id");
                		String ouname = info.getStr("ouname");
                		String splb = "行政许可";
                		String areaname = "济宁市";
                		String taskname = info.getStr("taskname");
                		String applyer = "法人,个人";
                		String url = "http://jizwfw.sd.gov.cn/jnzwdt/epointzwmhwz/pages/eventdetail/personaleventdetail_new?taskguid="
                				+info.getStr("rowguid")+"&taskid="+info.getStr("task_id");
                		xml.append(item_id).append("]]></iid><sign><![CDATA[GGFW]]></sign><group><![CDATA[1]]></group><web><![CDATA[")
                			.append("370800").append("]]></web><webname><![CDATA[").append(areaname).append("]]></webname><column><![CDATA[01]]></column><module><![CDATA[")
                			.append(splb).append("]]></module><subject><![CDATA[").append(taskname).append("]]></subject><content><![CDATA[").append(taskname).append("]]></content><author><![CDATA[]]></author><date><![CDATA[")
                			.append(date).append("]]></date><opr>").append("A").append("</opr><url charset=\"UTF-8\"><![CDATA[").append(url).append("]]></url><attachfiles></attachfiles><tag><![CDATA[")
                			.append(splb).append("]]></tag><columnurl><![CDATA[http://jizwfw.sd.gov.cn/jnzwdt/epointzwmhwz/pages/default/index]]></columnurl><type><![CDATA[2]]></type><typename><![CDATA[省政府_权力事项格式]]></typename><fields><ITEM_CODE name=\"事项编码\"><![CDATA[")
                			.append(item_id).append("]]></ITEM_CODE><ORG_NAME name=\"所属部门\"><![CDATA[").append(ouname)
                			.append("]]></ORG_NAME><SERVICE_OBJECT name=\"实施对象\"><![CDATA[").append(applyer).append("]]></SERVICE_OBJECT></fields></index></dahan>");
                		Map<String, String> map  = new HashMap<String, String>();
                		map.put("xml", xml.toString());
                		try {
                			JSONObject rtn = postParam(map, "http://www.wenshang.gov.cn/jrobot/interface/receiveXml.do");
                			if(StringUtil.isNotBlank(rtn.toString())&& rtn.toString().contains("200")) {
                				info.set("is_pushjn", "S");
                				service.update(info);
                			}else{
                				log.info("【推送济宁大汉事项】IP："+rtn.toJSONString());
                				break;
                			};
                		} catch (Exception e) {
                			e.printStackTrace();
                		}
                    }else {
                    	log.info("【推送济宁大汉事项】不存在需要推送的audit_task事项");
                    }
                }
            }else {
                log.info("【推送济宁大汉事项】不存在需要推送的audit_task事项");
            }
            
        }
        catch (Exception e) {
            log.info("【推送济宁大汉事项】推送失败");
            e.printStackTrace();
        }
        log.info("【推送济宁大汉事项结束】");
    }
    
    public static JSONObject postParam( Map<String, String> params,String url) throws Exception {
		HttpClient client = new HttpClient();
		PostMethod method = new UTF8PostMethod(url);
		JSONObject rtn = new JSONObject();
		String result1 = "";
		//将原来写入postman中的form-data数据放入Map中
		for (Map.Entry<String, String> entry : params.entrySet()) {
			String value = new String(entry.getValue().getBytes(),"UTF-8");
			method.addParameter(entry.getKey(),value);
		}
		try {
			method.setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            client.executeMethod(method);
            byte[] response1 = method.getResponseBody();
            //获取请求后的响应体
			result1 = new String(response1,"UTF-8");
			rtn =  JSONObject.parseObject(result1);
			return rtn;
		} catch (IOException ex) {
			
		} catch (JSONException ex) {
			System.err.println(result1);
		}finally {
			//释放连接
			method.releaseConnection();
		}
		return rtn;
	}
    
    public static class UTF8PostMethod extends PostMethod{
        public UTF8PostMethod(String url){
            super(url);
        }
        @Override
        public String getRequestCharSet() {
            return "UTF-8";
        }
    }
}
