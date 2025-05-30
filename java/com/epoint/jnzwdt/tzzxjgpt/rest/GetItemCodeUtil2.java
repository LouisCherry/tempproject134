package com.epoint.jnzwdt.tzzxjgpt.rest;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.fmgl.auditrsitembaseinfoextends.api.IAuditRsItemBaseinfoExtendsService;
import com.epoint.fmgl.auditrsitembaseinfoextends.api.entity.AuditRsItemBaseinfoExtends;
import com.esotericsoftware.minlog.Log;

import ewebeditor.admin.login_jsp;
public class GetItemCodeUtil2
{
    public static String getItemCode(String id) {
        IAuditRsItemBaseinfoExtendsService itemextendsService=ContainerFactory.getContainInfo().getComponent(IAuditRsItemBaseinfoExtendsService.class);
        IAuditRsItemBaseinfo itemService=ContainerFactory.getContainInfo().getComponent(IAuditRsItemBaseinfo.class);
        JSONObject dataJson = new JSONObject();
        try {
            
            //String url = "http://172.20.84.14/gateway/api/1/rzs_tzxmzxjgpt_xmxxcs";
            String url = ConfigUtil.getConfigValue("dhlogin", "sfgwcxxmxxurl");
            String ApiKey = ConfigUtil.getConfigValue("dhlogin", "cxApiKey");

            AuditRsItemBaseinfoExtends itemBaseinfoExtend =itemextendsService.find(id);
            String seqid = itemBaseinfoExtend.getSeqid();
            String investid = itemBaseinfoExtend.getInvestid();
            //请求头
            Map<String, String> headers = new HashMap<>();
            headers.put("appkey", ApiKey);
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            // 设置参数
            Map<String, Object> map = new HashMap<>(16);
            map.put("seqId", seqid);
            map.put("investId", investid);
            Log.info("赋码申请查询接口请求参数" +map);
            String data = HttpUtil.doPost(url, map, headers);
            Log.info("赋码申请查询接口返回结果" +data);
            //转为json对象
            JSONObject jsondata1 = JSONObject.parseObject(data);
            String responsecode = jsondata1.getString("code");
            if("200".equals(responsecode)) {
                String responsedata = jsondata1.getString("data");
                JSONObject jsondata2 = JSONObject.parseObject(responsedata);
                String responsecode2 = jsondata2.getString("code");
                if("200".equals(responsecode2)) {
                    String responsedata2 = jsondata2.getString("data");
                    JSONObject jsondata3 = JSONObject.parseObject(responsedata2);
                    String responsecode3 = jsondata3.getString("code");
                    if("200".equals(responsecode3)) {
                        String responsedata3 = jsondata3.getString("project");
                        JSONObject jsondata4 = JSONObject.parseObject(responsedata3);
                        String resultstatus = jsondata4.getString("STATUS");
                        switch (resultstatus) {
                            //审核中
                            case "00":
                                itemBaseinfoExtend.setItemcode("赋码审核中");
                                itemextendsService.update(itemBaseinfoExtend);
                                dataJson.put("code", "1");
                                dataJson.put("text", "赋码审核中");
                                break;
                            case "99":
                                String PROJECT_CODE = jsondata4.getString("PROJECT_CODE");
                                itemBaseinfoExtend.setItemcode(PROJECT_CODE);
                                itemBaseinfoExtend.setStatus("2");
                                itemextendsService.update(itemBaseinfoExtend);
                                SaveFmUtil2.saveFmbaseinfo(itemBaseinfoExtend);
                                dataJson.put("code", "1");
                                dataJson.put("text", "获取赋码成功");
                                break;
                            case "98":
                                itemBaseinfoExtend.setItemcode("赋码失败（驳回）");
                                itemBaseinfoExtend.setStatus("3");
                                itemextendsService.update(itemBaseinfoExtend);
                                dataJson.put("code", "1");
                                dataJson.put("text", "赋码失败（驳回）");
                             break;
                            case "97":
                                itemBaseinfoExtend.setItemcode("赋码失败（作废）");
                                itemBaseinfoExtend.setStatus("3");
                                itemextendsService.update(itemBaseinfoExtend);
                                dataJson.put("code", "1");
                                dataJson.put("text", "赋码失败（作废）");
                             break;
                            default:
                                break;
                        }
                    }
                }else {
                    dataJson.put("code", "0");
                }
            }else {
                dataJson.put("code", "0");
            }
            }catch (Exception e) {
            }
         return dataJson.toString();
     
    }
}
