package com.epoint.xmz.job;

import java.lang.invoke.MethodHandles;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.zwdt.zwdtrest.space.api.ISpaceAcceptService;

@DisallowConcurrentExecution
public class YqAreaDataSyncJob implements Job {
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    private ISpaceAcceptService iSpaceAcceptService = ContainerFactory.getContainInfo().getComponent(ISpaceAcceptService.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            log.info("==========开始执行YqAreaDataSyncJob数据同步推送服务==========");
            EpointFrameDsManager.begin(null);

            tbApplyBaseInfo();

            EpointFrameDsManager.commit();
            log.info("==========执行YqAreaDataSyncJob数据同步推送服务成功！！！==========");
        } catch (Exception e) {
            e.printStackTrace();
            EpointFrameDsManager.rollback();
            log.info("==========执行YqAreaDataSyncJob数据同步推送服务失败！！！==========");
        } finally {
            EpointFrameDsManager.close();
            log.info("==========执行YqAreaDataSyncJob数据同步推送服务结束==========");
        }
    }

    /**
     * 同步办件基本信息
     */
    public void tbApplyBaseInfo(){
       String url = "http://jining-test.zhichongjia.com:7011/police-manage/common/convenience_point/list_all";
       String nonceStr = "nonce123456";
       String sign = "sign123456";
       JSONObject datajson = new JSONObject();
       datajson.put("openCityId", 0);
       datajson.put("nonceStr", nonceStr);
       datajson.put("sign", sign);
       String result = HttpUtil.doPostJson(url, datajson.toString());
       JSONObject resultJson = JSON.parseObject(result);
       if ("200".equals(resultJson.getString("code"))) {
    	   JSONArray dataqxs = resultJson.getJSONArray("data");
    	   if (!dataqxs.isEmpty()) {
    		   for (int i=0;i<dataqxs.size();i++) {
    			   JSONObject object = dataqxs.getJSONObject(i);
    			   String nid = object.getString("id");
    			   String addressFull = object.getString("addressFull");
    			   JSONArray pcs = resultJson.getJSONArray("relationPoliceList");
    			   String policeStationId = "";
    			   String policeStationName = "";
    			   if (pcs.size() > 0) {
    				   JSONObject pcsrecord = pcs.getJSONObject(0);
        			   policeStationId = pcsrecord.getString("policeStationId");
        			   policeStationName = pcsrecord.getString("policeStationName");
    			   }
    			  
    			   yqbm nyqareac = new yqbm();
    			   nyqareac.set("rowguid", UUID.randomUUID().toString());
    			   nyqareac.setName(addressFull);
    			   nyqareac.setValue(nid+";"+policeStationId+";"+policeStationName);
    			   iSpaceAcceptService.insertYqbm(nyqareac);
    			   
    		   }
    	   }
       }
       
       
       
       
    }

    
}
