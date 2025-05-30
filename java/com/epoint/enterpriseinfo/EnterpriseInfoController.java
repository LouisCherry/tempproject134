package com.epoint.enterpriseinfo;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;

@RestController
@RequestMapping("/enterpriseinfocontroller")
public class EnterpriseInfoController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    
    @Autowired
    private ICodeItemsService codeItemsService;
    
    public static final String url= ConfigUtil.getConfigValue("hcp", "url");
    
    public static final String appId= ConfigUtil.getConfigValue("hcp", "appId");
    
    public static final String singKey= ConfigUtil.getConfigValue("hcp", "singKey");
    
    /**
     * 获取企业信息的接口
     * 
     * @param params 接口入参
     * @return
     * @throws IOException 
     * @throws HttpException 
     */
    @RequestMapping(value = "/getEnterpriseByCertNum", method = RequestMethod.POST)
    public String getEnterpriseByCertNum(@RequestBody String params, @Context HttpServletRequest request) throws HttpException, IOException {
        try{
            log.info("=======开始调用getEnterpriseByCertNum接口=======");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("appid", appId);
            // 1、入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            JSONObject result = json.getJSONObject("params");
            String certNum = result.getString("certNum");
            if(certNum.length() == 18) {
                //18位统一社会信用代码
                map.put("tyshxydm", certNum);
            }else if (certNum.length() == 9) {
                //9位组织机构代码
                map.put("jgdm", certNum);
            }else {
                return JsonUtils.zwdtRestReturn("0", "机构/个人信用代码参数错误：","");
            }
            String jsonData = getDataJsonSign(map, url, singKey);

            
            //String jsonData = "{\"message\":\"查询接口成功\",\"result\":{\"bgrq\":\"2021-06-17\",\"bzrq\":\"2021-06-17\",\"fddbr\":\"黄慧\",\"jgdm\":\"MA94AUJX4\",\"jgdz\":\"山东省济宁市嘉祥县卧龙山街道中心街117号\",\"jglx\":\"1\",\"jgmc\":\"济宁信豪钢结构有限公司\",\"jjhy2011\":\"F5179\",\"jjlx\":\"62 \",\"jydz\":\"山东省济宁市嘉祥县卧龙山街道中心街117号\",\"jyfw\":\"一般项目：金属结构销售；普通机械设备安装服务；涂料销售（不含危险化学品）；轻质建筑材料销售；建筑材料销售；劳务服务（不含劳务派遣）；建筑工程机械与设备租赁；建筑物清洁服务。（除依法须经批准的项目外，凭营业执照依法自主开展经营活动）许可项目：各类工程建设活动；住宅室内装饰装修；建筑劳务分包；施工专业作业。（依法须经批准的项目，经相关部门批准后方可开展经营活动，具体经营项目以相关部门批准文件或许可证件为准）\",\"jyzt\":\"1\",\"newxzqhcode\":\"370829\",\"newxzqhname\":\"山东省济宁市嘉祥县\",\"njglx\":\"11\",\"njjhy\":\"H6379\",\"njjlx\":\"150\",\"pzjgdm\":\"004333806\",\"pzjgmc\":\"嘉祥县工商行政管理局\",\"tyshxydm\":\"91370829MA94AUJX45\",\"xzqh\":\"370829\",\"yzbm\":\"272000\",\"zch\":\"370829200174258\",\"zcrq\":\"2021-06-17\",\"zfrq\":\"9999-12-31\"},\"status\":\"success\"}";
            //String jsonData = "{\"message\":\"查询接口成功\",\"result\":{},\"status\":\"success\"}";
            JSONObject parseObject = JSONObject.parseObject(jsonData);
            Map<String, Object> mapResult = (Map<String, Object>)parseObject.get("result");
            //返回企业数据为空时
            if(mapResult.isEmpty()) {
                return JsonUtils.zwdtRestReturn("0", "企业信息为空","");
            }
            
            String jglxText = codeItemsService.getItemTextByCodeName("机构类型", mapResult.get("jglx").toString());
            String jjhyText = codeItemsService.getItemTextByCodeName("经济行业", mapResult.get("jjhy2011").toString());
            String jjlxText = codeItemsService.getItemTextByCodeName("经济类型", mapResult.get("jjlx").toString());
            String jyztText = codeItemsService.getItemTextByCodeName("经营状态", mapResult.get("jyzt").toString());
            mapResult.put("jglxText", (StringUtil.isNotBlank(jglxText)?jglxText:""));
            mapResult.put("jjhyText", (StringUtil.isNotBlank(jjhyText)?jjhyText:""));
            mapResult.put("jjlxText", (StringUtil.isNotBlank(jjlxText)?jjlxText:""));
            mapResult.put("jyztText", (StringUtil.isNotBlank(jyztText)?jyztText:""));
            JSONObject jsonObj=new JSONObject(mapResult);
            log.info("=======结束调用getEnterpriseByCertNum接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取企业信息成功", jsonObj.toString());
        }
        catch (Exception e) {
            log.info("=======getEnterpriseByCertNum接口参数：params【" + params + "】=======");
            log.info("=======getEnterpriseByCertNum异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取企业信息异常：" + e.getMessage(), "");
        }
     }
       
    
    /***
     * 组装访问链接签名
     * 
     * @param url
     * @param map
     * @param singKey
     * @return
     */
    private HttpMethod getPostMethod(String url,Map<String, Object> map,String singKey) {
        PostMethod post = new UTF8PostMethod(url);
        String sign = SignUtils.getSign(map, singKey);
        map.put("sign", sign);
        
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        for(String key : map.keySet()){
            list.add(new NameValuePair(key, map.get(key).toString()));
        }
        NameValuePair[] nameValuePairs = (NameValuePair[])list.toArray(new NameValuePair[list.size()]);
        post.setRequestBody(nameValuePairs);
        return post;
    }
    /***
     * http调用接口
     * 
     * @param map
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public String getDataJsonSign(Map<String, Object> map,String url,String signKey) throws HttpException, IOException{
        HttpClient client = new HttpClient();
        HttpMethod method = getPostMethod(url,map,signKey); // 使用 POST 方式提交数据
        client.executeMethod(method); // 打印服务器返回的状�??
        String response = method.getResponseBodyAsString();
        method.releaseConnection();
        return response;
    }
}
