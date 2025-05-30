package com.epoint.znsb.auditznsbwater.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.znsb.auditznsbwater.api.IAuditznsbwaterService;
import com.epoint.znsb.auditznsbwater.api.entity.Auditznsbwater;
import com.epoint.znsb.auditznsbwaterjfinfo.api.IAuditZnsbWaterjfinfoService;
import com.epoint.znsb.auditznsbwaterjfinfo.api.entity.AuditZnsbWaterjfinfo;
import com.epoint.znsb.jnzwfw.water.WaterFtpUtil;
import com.epoint.znsb.jnzwfw.water.WaterSFTPUtil;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WaterJob implements Job{
    transient Logger log = LogUtil.getLog(WaterJob.class);
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
    	 try {
             log.info("====================水务对账文件开始上传===========================");
             EpointFrameDsManager.begin(null);
             String dir = ClassPathUtil.getDeployWarPath() + "jiningzwfw/water/";
             //查询未正上传的对账文件
             IAuditZnsbWaterjfinfoService jfService = ContainerFactory.getContainInfo().getComponent(IAuditZnsbWaterjfinfoService.class);
             IAuditznsbwaterService service = ContainerFactory.getContainInfo().getComponent(IAuditznsbwaterService.class);

              List<Auditznsbwater> waterlist = service.getListByIsupload();
              if(waterlist != null && !waterlist.isEmpty()){
                  for (Auditznsbwater water :waterlist) {
                      //获取对账信息 发送到 水务服务器
                      List<AuditZnsbWaterjfinfo> jflist = jfService.findListByTime(water.getWaterinfo());

                      List<JSONObject> jfJsonList = new ArrayList<>();
                      for (int i = 0; i < jflist.size(); i++) {
                          JSONObject jfJson = new JSONObject();
                          AuditZnsbWaterjfinfo jf = jflist.get(i);
                          jfJson.put("flowon",jf.getWaterflowon());
                          jfJson.put("number",jf.getWaternumber());
                          jfJson.put("jftime",jf.getWatertime());
                          jfJson.put("money",jf.getWaterpaymoney());
                          jfJson.put("starttime",jf.getStarttime());
                          jfJson.put("endtime",jf.getEndtime());
                          jfJson.put("jfdw","");
                          jfJsonList.add(jfJson);
                      }

                      JSONArray objects = JSONArray.parseArray(JSON.toJSONString(jfJsonList));
                      //生成txt文件
                      WaterFtpUtil.createTxtFile(objects,dir,"DZ" + water.getWaterinfo() + "022");
                      //传递到SFtp服务器
                      WaterSFTPUtil sftp = new WaterSFTPUtil( "sftp", "YzX1iBzE", "111.14.43.2", 22);
                      sftp.login();
                      File file = FileManagerUtil.createFile( dir + "DZ" + water.getWaterinfo() + "022.txt");
                      InputStream is = new FileInputStream(file);
                      sftp.upload( "dz", "", "DZ" + water.getWaterinfo() + "022.txt", is);
                      sftp.logout();
                      is.close();
                  }

              }
             EpointFrameDsManager.commit();
         }
         catch (Exception e) {
             e.printStackTrace();
             EpointFrameDsManager.rollback();
         }
         finally {
             EpointFrameDsManager.close();
         }
    }
}
