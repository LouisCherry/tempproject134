package com.epoint.xmz.kyinfo.job;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.xmz.kyinfo.api.IKyInfoService;
import com.epoint.xmz.kyinfo.api.entity.KyInfo;
import com.epoint.xmz.kyperson.api.IKyPersonService;
import com.epoint.xmz.kyperson.api.entity.KyPerson;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DisallowConcurrentExecution
public class SyncKyInfoJob implements Job {
    transient static Logger log = LogUtil.getLog(MethodHandles.lookup().lookupClass());
    IKyInfoService kyInfoService = ContainerFactory.getContainInfo().getComponent(IKyInfoService.class);
    IKyPersonService kyPersonService = ContainerFactory.getContainInfo().getComponent(IKyPersonService.class);
    IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            doSync();
            EpointFrameDsManager.commit();
        } catch (Exception e) {
            EpointFrameDsManager.rollback();
            e.printStackTrace();
        } finally {
            EpointFrameDsManager.close();
        }
    }

    private void doSync() {
        try {
            log.info("===============开始执行获取勘验结果服务===============");
            String apiUrl = "http://172.20.58.59:18015/free/data-model/api/get/anjianxinxi";
            List<KyInfo> KyInfos = kyInfoService.getNeedInfo();
            Map<String, String> headerMap = new HashMap<String, String>();
            headerMap.put("Content-Type", "application/json");
            headerMap.put("Accept-Charset", "utf-8");
            for (KyInfo kyInfo : KyInfos) {
                // 构建参数
                JSONObject param = new JSONObject();
                param.put("tasknum", kyInfo.getTaskNum());
                String result = HttpUtil.doPostJson(apiUrl, param.toString(), headerMap);

                JSONObject reJson = JSONObject.parseObject(result);
                if ("true".equals(reJson.getString("hasError"))) {
                    log.info(reJson);
                } else {
                    log.info(reJson);
                    JSONArray jsonArray = reJson.getJSONArray("result");
                    if (!jsonArray.isEmpty() && reJson.getInteger("totalCount") != 0) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String projectguid = kyInfo.getProjectguid();
                        KyPerson kyPerson = new KyPerson();
                        kyPerson.setOperatedate(new Date());
                        kyPerson.setKyguid(projectguid);
                        kyPerson.setRowguid(UUID.randomUUID().toString());
                        kyPerson.setName(jsonObject.getString("human_name"));
                        kyPerson.setRepplytime(jsonObject.getDate("end_time"));
                        kyPerson.setReason(jsonObject.getString("trans_opinion"));
                        kyPerson.setClientguid(UUID.randomUUID().toString());
                        kyPersonService.insert(kyPerson);

                        kyInfo.setIssync("1");
                        kyInfoService.update(kyInfo);
                    }
                }
            }
            log.info("===============结束执行获取勘验结果服务===============");
        } catch (Exception e) {
            log.info("===============接收勘验信息失败，服务异常===============");
            e.printStackTrace();
        }
    }

}
