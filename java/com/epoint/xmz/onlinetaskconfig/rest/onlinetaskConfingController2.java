package com.epoint.xmz.onlinetaskconfig.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.xmz.onlinetaskconfig.api.IOnlinetaskConfigService;
import com.epoint.xmz.onlinetaskconfig.api.entity.OnlinetaskConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/onlinetask2")
public class onlinetaskConfingController2 {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IOnlinetaskConfigService iOnlinetaskConfigService;
    @Autowired
    private ICodeItemsService iCodeItemsService;
    @Autowired
    private IConfigService configServicce;
    @Autowired
    private IAttachService attachService;

    @RequestMapping(value = "/hotEventList", method = RequestMethod.POST)
    public String getOnlinetaskInfo(@RequestBody String params) {
        try {
            log.info("=======开始调用hotEventList接口=======");
            CommonDao commonDao = new CommonDao();
            String sql = "select * from onlinetask_config_task";
            String sql2 = "select * from onlinetask_config where rowguid = ?";
            String sql3 = "select * from audit_task where rowguid = ?";
            List<Record> findlist = commonDao.findList(sql, Record.class);
            JSONObject rtnJson = new JSONObject();
            JSONArray rtnlist = new JSONArray();

                for (Record record : findlist) {
                    if (StringUtil.isNotBlank(record.get("configguid")) && StringUtil.isNotBlank(record.get("configguid"))) {
                        Record findrecord = commonDao.find(sql2, Record.class, record.getStr("configguid"));
                        Record findrecord2 = commonDao.find(sql3, Record.class, record.getStr("taskguid"));
                        JSONObject kindJson = new JSONObject();
                        kindJson.put("url", findrecord.getStr("url"));
                        kindJson.put("name", findrecord2.getStr("taskname"));
                        rtnlist.add(kindJson);
                    }
                }



            rtnJson.put("custom", rtnlist);

            return JsonUtils.zwdtRestReturn("200", "信息查询成功！", rtnJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======hotEventList接口参数：params【" + params + "】=======");
            log.info("=======hotEventList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "查询居民办事信息失败：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getAppList", method = RequestMethod.POST)
    public String getOnlineTaskDetail(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getAppList接口=======");
            JSONObject param = JSONObject.parseObject(params);
            JSONObject paramJson = param.getJSONObject("params");
            String keywords = paramJson.getString("keywords");
            CommonDao commonDao = new CommonDao();
            String zwdturl = configServicce.getFrameConfigValue("zwdturl");
            String sql ="select * from (select * from onlinetask_config order by pxh desc )a group by a.ouname order by pxh desc";
            if (StringUtil.isNotBlank(keywords)){
                sql="select * from (select * from onlinetask_config order by pxh desc )a where 1=1 and a.ouname like '%"+keywords+"%' group by a.ouname order by pxh desc";
            }
            List<Record> ounames = commonDao.findList(sql, Record.class);
            String sql2="select * from onlinetask_config where ouname = ?";
            JSONObject rtnJson = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (Record ouname : ounames) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("modName", ouname.getStr("ouname"));
                jsonArray.add(jsonObject);
                List<Record> findlist = commonDao.findList(sql2, Record.class, ouname.getStr("ouname"));
              if (findlist.size()>6){
                  JSONArray infoArray = new JSONArray();

                  for (int i = 0; i < 6; i++) {
                      JSONObject object = new JSONObject();
                      object.put("appUrl","../../jmbs/site_detail.html?guid="+findlist.get(i).getStr("rowguid"));
                      if (StringUtil.isNotBlank(findlist.get(i).getStr("clientguid2"))) {
                          List<FrameAttachStorage> attachList = attachService.getAttachListByGuid(findlist.get(i).getStr("clientguid2"));
                          if (attachList.size() != 0) {
                              String attachguid = attachList.get(0).getAttachGuid();
                              String fileurl = zwdturl + "rest/attachAction/getContent?isCommondto=true&attachGuid=" + attachguid;
                              object.put("appImg", fileurl);
                          }
                      }

                      object.put("appName", findlist.get(i).getStr("title"));
                      infoArray.add(object);
                  }

                  jsonObject.put("appList", infoArray);
              }else {
                  JSONArray infoArray = new JSONArray();

                  for (Record record1 : findlist) {
                      JSONObject object = new JSONObject();
                      object.put("appUrl","../../jmbs/site_detail.html?guid="+record1.getStr("rowguid"));
                      if (StringUtil.isNotBlank(record1.getStr("clientguid2"))) {
                          List<FrameAttachStorage> attachList = attachService.getAttachListByGuid(record1.getStr("clientguid2"));
                          if (attachList.size() != 0) {
                              String attachguid = attachList.get(0).getAttachGuid();
                              String fileurl = zwdturl + "rest/attachAction/getContent?isCommondto=true&attachGuid=" + attachguid;
                              object.put("appImg", fileurl);
                          }
                      }

                      object.put("appName", record1.getStr("title"));
                      infoArray.add(object);
                  }
                  jsonObject.put("appList", infoArray);

              }
            }
            rtnJson.put("custom", jsonArray);
            rtnJson.put("code","200");
            rtnJson.put("text","信息查询成功");
            return rtnJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getAppList接口参数：params【" + params + "】=======");
            log.info("=======getAppList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "查询居民办事详情失败：" + e.getMessage(), "");
        }
    }


}
