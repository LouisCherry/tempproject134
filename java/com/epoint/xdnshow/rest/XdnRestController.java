package com.epoint.xdnshow.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.xdnshow.util.AesUtil;

@RestController
@RequestMapping("/xdnshow")
public class XdnRestController
{
    
    @RequestMapping(value = "/getTokenString", method = RequestMethod.POST)
    private String getTokenString() {
        String  skey = "D6d4C30f208e40ad";  //服务方提供的秘钥
        String url = "http://59.206.96.178:9960/gateway/api/1/jnsfgw_xmjz";
        try {
            //JSONObject result = JSONObject.parseObject(HttpUtil.doPostJson(url, json));
            Map<String, String> headerMap = new HashMap<String, String>();
            Map<String, String> param = new HashMap<String, String>();
            param.put("type", "sxzspfwj");
            param.put("token", "C3B05FA025563DE1D6E7B9973A2AEADD");
            headerMap.put("ApiKey", "618122818722201600");
            String json = HttpUtil.doHttp(url, headerMap, param, "post", HttpUtil. RTN_TYPE_2);
            JSONObject jsonObject = JSONObject.parseObject(json);
            String result = jsonObject.getString("data").replaceAll("\\\\", "");
            JSONObject results = JSON.parseObject(result);
            String token = results.getString("token");
            String tokenString = new String(AesUtil.decrypt(AesUtil.parseHexStr2Byte(token), skey), "utf-8");
            return tokenString;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /***
     * 
     *  项目阶段进展情况
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/stageProgress", method = RequestMethod.POST)
    public String stageProgress(HttpServletRequest request) {
        try {
            String param = getTokenString();
            JSONObject result = JSON.parseObject(param);
            JSONObject jdjzObj = result.getJSONObject("jdjz");
            JSONObject dataJson = new JSONObject();
            dataJson.put("completeInvest", jdjzObj.getString("xjLjTz"));
            dataJson.put("yearlyPlanInvest", jdjzObj.getString("xjJhTz"));
            dataJson.put("continuedPro", jdjzObj.getString("xjProjectNum"));
            dataJson.put("yearlyCompleteInvest", jdjzObj.getString("xjWcTz"));
            dataJson.put("totalInvest", jdjzObj.getString("xjAllTz"));
            dataJson.put("startPro", jdjzObj.getString("ykgCount"));
            dataJson.put("stratProRate", jdjzObj.getString("kglAll"));
            dataJson.put("yearlyPlanInvest1", jdjzObj.getString("xkgJhTz"));
            dataJson.put("completeInvest", jdjzObj.getString("xjLjTz"));
            dataJson.put("newPro", jdjzObj.getString("xkgProjectNum"));
            dataJson.put("yearlyCompleteInvest1", jdjzObj.getString("xkgWcTz"));
            dataJson.put("totalInvest1", jdjzObj.getString("xkgAllTz"));
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
    
    /***
     * 
     *  分级管理项目进度情况
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/hierarchicalManage", method = RequestMethod.POST)
    public String hierarchicalManage(HttpServletRequest request) {
        try {
            String param = getTokenString();
            JSONObject result = JSON.parseObject(param);
            JSONObject fjglObj = result.getJSONObject("fjgl");
            JSONObject dataJson = new JSONObject();
            JSONObject optimalPro = new JSONObject();
            optimalPro.put("proTotal", fjglObj.getString("syxProjectNum"));
            optimalPro.put("startWork", fjglObj.getString("syxStartNum"));
            optimalPro.put("startRate", fjglObj.getString("syxKgl"));
            optimalPro.put("money", fjglObj.getString("syxJhTz"));
            optimalPro.put("totalInvest", fjglObj.getString("syxAllTz"));
            optimalPro.put("yearlyComplete", fjglObj.getString("syxWcTz"));
            JSONObject majorPro = new JSONObject();
            majorPro.put("proTotal", fjglObj.getString("szdProjectNum"));
            majorPro.put("startWork", fjglObj.getString("szdStartNum"));
            majorPro.put("startRate", fjglObj.getString("szdKgl"));
            majorPro.put("money", fjglObj.getString("szdJhTz"));
            majorPro.put("totalInvest", fjglObj.getString("szdAllTz"));
            majorPro.put("yearlyComplete", fjglObj.getString("szdWcTz"));
            dataJson.put("optimalPro", optimalPro);
            dataJson.put("majorPro", majorPro);
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
    
    /***
     * 
     *  济宁市项目进展情况
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/cityProject", method = RequestMethod.POST)
    public String cityProject(HttpServletRequest request) {
        try {
            String param = getTokenString();
            JSONObject result = JSON.parseObject(param);
            JSONObject ztjzObj = result.getJSONObject("ztjz");
            JSONObject dataJson = new JSONObject();
            dataJson.put("proTotal", ztjzObj.getString("projectAll"));
            dataJson.put("startPro", ztjzObj.getString("projectStarts"));
            dataJson.put("startWorkRate", ztjzObj.getString("kgl"));
            dataJson.put("totalInvest", ztjzObj.getString("investAll"));
            dataJson.put("planInvest", ztjzObj.getString("yearRate"));
            dataJson.put("completeInvest", ztjzObj.getString("yearFinish"));
            
            JSONArray projectNumList =  result.getJSONArray("projectNumMap");
            if (projectNumList != null && projectNumList.size() > 0) {
                for (int i=0;i<projectNumList.size();i++) {
                    JSONObject json = projectNumList.getJSONObject(i);
                    switch (json.getString("name")) { // 或者获取wa.getNote()
                        case "新一代信息技术":
                            dataJson.put("firstGeneration", json.getString("ne")); 
                            break;
                        case "高端装备":
                            dataJson.put("equipment", json.getString("ne")); 
                            break;
                        case "新能源新材料":
                            dataJson.put("newEnergy", json.getString("ne")); 
                            break;
                        case "节能环保":
                            dataJson.put("energySave", json.getString("ne")); 
                            break;
                        case "医养健康":
                            dataJson.put("health", json.getString("ne")); 
                            break;
                        case "高端化工":
                            dataJson.put("chemicalIndustry", json.getString("ne")); 
                            break;
                        case "纺织服装":
                            dataJson.put("textileClothing", json.getString("ne")); 
                            break;
                        case "高效农业":
                            dataJson.put("efficientAgriculture", json.getString("ne")); 
                            break;
                        case "文化旅游":
                            dataJson.put("culturalTourism", json.getString("ne")); 
                            break;
                        case "现代金融":
                            dataJson.put("finance", json.getString("ne")); 
                            break;
                        case "重大基础设施":
                            dataJson.put("majorInfrastructure", json.getString("ne")); 
                            break;
                        case "其他":
                            dataJson.put("otherPro", json.getString("ne")); 
                            break;
                        default:
                            break;
                    }
                }
            }
            
            
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
    
    /***
     * 
     *  项目建设进展情况
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/construction", method = RequestMethod.POST)
    public String construction(HttpServletRequest request) {
        try {
            String param = getTokenString();
            JSONObject result = JSON.parseObject(param);
            JSONArray projectNumList =  result.getJSONArray("projectNumMap");
            List<Map<String,String>> list = new ArrayList<Map<String,String>>();
            if (projectNumList != null && projectNumList.size() > 0) {
                for (int i=0;i<projectNumList.size();i++) {
                    JSONObject json = projectNumList.getJSONObject(i);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("name", json.getString("name"));
                    map.put("building", json.getString("al"));
                    map.put("newBuild", json.getString("ne"));
                    list.add(map);
                }
            }
            JSONObject dataJson = new JSONObject();
            dataJson.put("list", list);
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    /***
     * 
     *  10大产业项目进展情况
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/topTenIndustries", method = RequestMethod.POST)
    public String topTenIndustries(HttpServletRequest request) {
        try {
            String param = getTokenString();
            JSONObject result = JSON.parseObject(param);
            JSONObject dataJson = new JSONObject();
            JSONArray jzqkList =  result.getJSONArray("jzqkMap");
            List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
            if (jzqkList != null && jzqkList.size() > 0) {
                for (int i=0;i<jzqkList.size();i++) {
                    JSONObject json = jzqkList.getJSONObject(i);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("name", json.getString("name"));
                    map.put("planInvest", Double.parseDouble(json.getString("plan")));
                    map.put("completeInvest", Double.parseDouble(json.getString("ytz")));
                    list.add(map);
                }
            }
            dataJson.put("list", list);
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
    
    
}
