package com.epoint.xmz.onlinetaskconfig.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
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
import java.lang.reflect.Method;
import java.util.List;

import static com.alibaba.csp.sentinel.log.RecordLog.info;

@RestController
@RequestMapping("/onlinetask")
public class onlinetaskConfingController {
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
    @RequestMapping(value = "/getOnlineTaskInfo" ,method = RequestMethod.POST)
    public String getOnlinetaskInfo(@RequestBody String params){
        try {
            log.info("=======开始调用getOnlineTaskInfo接口=======");
            List<OnlinetaskConfig> OnlinetaskConfigs = iOnlinetaskConfigService.getAllConfg();
            JSONObject rtnJson = new JSONObject();
            JSONArray rtnArray = new JSONArray();
            List<CodeItems> codeItems = iCodeItemsService.listCodeItemsByCodeName("居民办事分类");
            for (CodeItems codeitem:codeItems ) {
                JSONObject kindJson = new JSONObject();
                kindJson.put("category",codeitem.getItemText());
                JSONArray sites = new JSONArray();
                for (OnlinetaskConfig onlinetaskConfig:OnlinetaskConfigs) {
                    if (onlinetaskConfig.getKind().equals(codeitem.getItemValue())){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("guid",onlinetaskConfig.getRowguid());
                        jsonObject.put("siteName",onlinetaskConfig.getTitle());
                        if ("1".equals(onlinetaskConfig.getInfoKind())){
                            jsonObject.put("type","site");
                        }else {
                            jsonObject.put("type","app");
                        }
                        sites.add(jsonObject);
                    }
                    kindJson.put("sites",sites);
                }
                rtnArray.add(kindJson);
                rtnJson.put("custom",rtnArray);
            }
            return JsonUtils.zwdtRestReturn("200", "信息查询成功！", rtnJson.toString());
        }catch (Exception e){
            e.printStackTrace();
            log.info("=======getOnlineTaskInfo接口参数：params【" + params + "】=======");
            log.info("=======getOnlineTaskInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "查询居民办事信息失败：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getOnlineTaskDetail" ,method = RequestMethod.POST)
    public String getOnlineTaskDetail(@RequestBody String params, @Context HttpServletRequest request){
        try {
            log.info("=======开始调用getOnlineTaskDetail接口=======");
            JSONObject param = JSONObject.parseObject(params);
            JSONObject paramJson = param.getJSONObject("params");
            String siteGuid = paramJson.getString("siteGuid");
            String type = paramJson.getString("type");
            String zwdturl = configServicce.getFrameConfigValue("zwdturl");
            OnlinetaskConfig onlinetaskConfig = iOnlinetaskConfigService.find(siteGuid);
            JSONObject rtnJson = new JSONObject();
            JSONArray infoArray = new JSONArray();
            infoArray.add(onlinetaskConfig.getDescribeinfo());
            String attachguid="";
            if (StringUtil.isNotBlank(onlinetaskConfig.getClientguid())){
                List<FrameAttachStorage> attachList = attachService.getAttachListByGuid(onlinetaskConfig.getClientguid());
                if (attachList.size()!=0){
                    attachguid = attachList.get(0).getAttachGuid();
                }
            }

            String fileurl =  zwdturl + "rest/attachAction/getContent?isCommondto=true&attachGuid=" + attachguid;
            rtnJson.put("describeTxt",infoArray);
            rtnJson.put("name",onlinetaskConfig.getTitle());
            rtnJson.put("previewImg",fileurl);
            rtnJson.put("siteUrl",onlinetaskConfig.getUrl());
            if ("1".equals(onlinetaskConfig.getInfoKind())){
                rtnJson.put("type","site");
            }else {
                rtnJson.put("type","app");
            }
            return JsonUtils.zwdtRestReturn("200", "信息查询成功！", rtnJson.toString());
        }catch (Exception e){
            e.printStackTrace();
            log.info("=======getOnlineTaskDetail接口参数：params【" + params + "】=======");
            log.info("=======getOnlineTaskDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "查询居民办事详情失败：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getDeptData" ,method = RequestMethod.POST)
    public String getDeptData(@RequestBody String params, @Context HttpServletRequest request){
        try {
            log.info("=======开始调用getDeptData接口=======");
            JSONObject rtnJson = new JSONObject();
            JSONArray rtnArray = new JSONArray();
            List<CodeItems> codeItems = iCodeItemsService.listCodeItemsByCodeName("居民办事部门");
            for (CodeItems codeitem:codeItems ) {
                JSONObject infokindJson = new JSONObject();
                infokindJson.put("name",codeitem.getItemText());
                JSONArray sites = new JSONArray();
                String ouValue = codeitem.getItemValue();
                List<OnlinetaskConfig> configByOu = iOnlinetaskConfigService.getAllConfgByOu(ouValue);
                for (OnlinetaskConfig onlinetaskConfig:configByOu) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("guid",onlinetaskConfig.getRowguid());
                    jsonObject.put("siteName",onlinetaskConfig.getTitle());
                    if ("1".equals(onlinetaskConfig.getInfoKind())){
                        jsonObject.put("type","site");
                    }else {
                        jsonObject.put("type","app");
                    }
                    sites.add(jsonObject);

                }
                infokindJson.put("data",sites);
                rtnArray.add(infokindJson);
                rtnJson.put("custom",rtnArray);
            }
            return JsonUtils.zwdtRestReturn("200", "信息查询成功！", rtnJson.toString());
        }catch (Exception e){
            e.printStackTrace();
            log.info("=======getDeptData接口参数：params【" + params + "】=======");
            log.info("=======getDeptData异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "查询居民办事详情失败：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/searchDept" ,method = RequestMethod.POST)
    public String searchDept(@RequestBody String params, @Context HttpServletRequest request){
        try {
            log.info("=======开始调用searchDept接口=======");
            JSONObject param = JSONObject.parseObject(params);
            JSONObject paramJson = param.getJSONObject("params");
            String keywords = paramJson.getString("keywords");
            JSONObject rtnJson = new JSONObject();
            JSONArray rtnArray = new JSONArray();
            List<OnlinetaskConfig> configByOuname = iOnlinetaskConfigService.getConfigByOuname(keywords);
            JSONArray sites = new JSONArray();
            for (OnlinetaskConfig onlinetaskConfig:configByOuname) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("guid",onlinetaskConfig.getRowguid());
                jsonObject.put("siteName",onlinetaskConfig.getTitle());
                if ("1".equals(onlinetaskConfig.getInfoKind())){
                    jsonObject.put("type","site");
                }else {
                    jsonObject.put("type","app");
                }
                sites.add(jsonObject);
            }
            rtnJson.put("custom",sites);
            return JsonUtils.zwdtRestReturn("200", "信息查询成功！", rtnJson.toString());
        }catch (Exception e){
            e.printStackTrace();
            log.info("=======searchDept接口参数：params【" + params + "】=======");
            log.info("=======searchDept异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "查询居民办事详情失败：" + e.getMessage(), "");
        }
    }
}
