package com.epoint.znsb.jnzwfw.selfservicemachine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.common.service.AuditCommonService;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.log.LogUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.*;

@RestController
@RequestMapping(value = "/loginface")
public class LoginFaceRestController extends AuditCommonService
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Autowired
    private IAuditOnlineRegister registerservice;

    transient Logger log = LogUtil.getLog(LoginFaceRestController.class);

    /**
     * 人脸比对
     */
    @RequestMapping(value = "/getUserByPhoto", method = RequestMethod.POST)
    public String getUserByPhoto(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("==================姓名身份证接口登录=================");
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            JSONObject dataJson = new JSONObject();
            String timestamp = new Date().getTime() + "";
            String signJson = getBackString(obj,true,"",timestamp);
            JSONObject sign = JSONObject.parseObject(signJson);
            String backString = "";
            if("true".equals(sign.getString("success"))){
                JSONObject data = sign.getJSONObject("data");
                String signString = data.getString("sign");
                if(StringUtil.isNotBlank(signString)){
                    backString = getBackString(obj,false,signString,timestamp);
                    JSONObject bacKjson = JSONObject.parseObject(backString);
                    if("true".equals(bacKjson.getString("success"))){
                        //解析data的值
                        JSONObject newdata = bacKjson.getJSONObject("data");
                        if("200".equals(newdata.getString("code"))){
                            JSONObject newDataJson = newdata.getJSONObject("data");
                            JSONObject bizPackage = newDataJson.getJSONObject("bizPackage");
                            if("true".equals(bizPackage.getString("success"))){
                                String authResult = bizPackage.getString("authResult");
                                if(authResult.startsWith("0")){
                                    //注册与获取信息
                                    AuditOnlineRegister register = registerservice.getRegisterByIdNumber(obj.getString("sfzhm")).getResult();
                                    if(register != null){
                                        //传递register
                                        dataJson.put("isregister","1");
                                        dataJson.put("user",register);
                                    }else{
                                        dataJson.put("isregister","0");
                                    }

                                }else{
                                    return JsonUtils.zwdtRestReturn("0", "对比失败，请重试", "");
                                }
                            }else{
                                return JsonUtils.zwdtRestReturn("0", "对比失败，请重试", "");
                            }

                        }else{
                            return JsonUtils.zwdtRestReturn("0", "对比接口异常，请联系工作人员处理", "");
                        }
                    }else{
                        return JsonUtils.zwdtRestReturn("0", "对比接口异常，请联系工作人员处理", "");
                    }
                }
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }


    private  String getBackString(JSONObject jsonString,boolean issign,String sign,String timestamp) throws Exception {
        String app_id = "khddzjkrawxb";
        String interface_id = "gahtjc";
        String version = "1.0";
        String header = "{}";
        String biz_content = jsonString.toString();
        String charset = "UTF-8";
        String origin = "1";
        Map<String,Object> map = new HashMap<>();
        map.put("app_id",app_id);
        map.put("interface_id",interface_id);
        map.put("version",version);
        map.put("header",header);
        map.put("biz_content",biz_content);
        map.put("charset",charset);
        map.put("timestamp",timestamp);
        map.put("origin",origin);
        String url = "http://jst.jiningdq.cn:81/jpaas-jags-server/interface/createsign";
        if (!issign){
            map.put("sign",sign);
            url = "http://jst.jiningdq.cn:81/jpaas-jags-server/interface/gateway";
        }
        log.info("接口：" + url + "上传内容" + map.toString());
        String backString = HTTPSClientUtil.doFPost(url,map);
        log.info("接口：" + url + "返回内容" + backString);
        return backString;
    }




    @RequestMapping(value = "/getRecruitList", method = RequestMethod.POST)
    public String getRecruitList(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String name = obj.getString("name");
            String CurPage = obj.getString("curpage");
            Map<String,Object> map = new HashMap<>();
            map.put("CurPage",CurPage);
            map.put("KeyWord",name);
            //调用接口
            String url = "http://60.211.225.19:7080/api/PostSearchPosition";
            String bacKString = HTTPSClientUtil.doPost(url,map);
            List<JSONObject> RecruitList = new ArrayList<>();
            JSONObject backJson = JSONObject.parseObject(bacKString);
            log.info("==================招聘职位=================" + bacKString);
            if("true".equals(backJson.getString("Success"))){
                //获取正常的职位信息
                JSONArray list = backJson.getJSONArray("Info");
                //找到对应的
                for (int i = 0; i < list.size(); i++) {
                    JSONObject object = list.getJSONObject(i);
                    RecruitList.add(object);
                }
            }
            dataJson.put("RecruitList",RecruitList);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }



    @RequestMapping(value = "/ZhaoPinHuiRiChengAnPai", method = RequestMethod.POST)
    public String ZhaoPinHuiRiChengAnPai(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            Map<String,Object> map = new HashMap<>();
            //调用接口
            String url = "http://60.211.225.19:7080/api/ZhaoPinHuiRiChengAnPai";
            String bacKString = HTTPSClientUtil.doPost(url,map);
            log.info("==================招聘会=================" + bacKString);
            List<JSONObject> ZhaoPinList = new ArrayList<>();
            JSONObject backJson = JSONObject.parseObject(bacKString);
            if("true".equals(backJson.getString("Success"))){
                //获取正常的职位信息
                JSONArray list = backJson.getJSONArray("Info");
                //找到对应的
                for (int i = 0; i < list.size(); i++) {
                    JSONObject object = list.getJSONObject(i);
                    ZhaoPinList.add(object);
                }
            }

            dataJson.put("ZhaoPinList",ZhaoPinList);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    


}
