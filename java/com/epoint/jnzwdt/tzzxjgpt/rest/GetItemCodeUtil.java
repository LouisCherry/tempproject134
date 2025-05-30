package com.epoint.jnzwdt.tzzxjgpt.rest;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.fmgl.auditrsitembaseinfoextends.api.IAuditRsItemBaseinfoExtendsService;
import com.epoint.fmgl.auditrsitembaseinfoextends.api.entity.AuditRsItemBaseinfoExtends;
public class GetItemCodeUtil
{
    public static String getItemCode(String itemguid) {
        IAuditRsItemBaseinfoExtendsService itemextendsService=ContainerFactory.getContainInfo().getComponent(IAuditRsItemBaseinfoExtendsService.class);
        IAuditRsItemBaseinfo itemService=ContainerFactory.getContainInfo().getComponent(IAuditRsItemBaseinfo.class);
        Map<String,Object> result = new HashMap<>();
        Map<String,Object> status = new HashMap<>();
        try {
            String ApiKey = ConfigUtil.getConfigValue("tzzxjgpt", "ApiKey2");
            //获取接口地址
            String url = ConfigUtil.getConfigValue("tzzxjgpt", "url2");
            AuditRsItemBaseinfo itemBaseinfo = itemService.getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
            String itemcode = itemBaseinfo.getItemcode();
            if(StringUtil.isNotBlank(itemcode)&&!"审核中".equals(itemcode)&&!"驳回".equals(itemcode)&&!"作废".equals(itemcode)) {
                status.put("code", "0"); 
                return JsonUtil.objectToJson(result);
            }
            AuditRsItemBaseinfoExtends itemBaseinfoExtend =itemextendsService.getitemextendsinfobyitemguid(itemguid);
            String seqid = itemBaseinfoExtend.getSeqid();
            String investid = itemBaseinfoExtend.getInvestid();
            //请求头
            Map<String, String> headers = new HashMap<>();
            headers.put("ApiKey", ApiKey);
            // 设置参数
            Map<String, Object> map = new HashMap<>(16);
            map.put("seqId", seqid);
            map.put("investId", investid);
            String data = HttpUtil.doPost(url, map, headers);
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
                                itemBaseinfo.setItemcode("审核中");
                                itemBaseinfoExtend.setItemcode("审核中");
                                itemService.updateAuditRsItemBaseinfo(itemBaseinfo);
                                itemextendsService.update(itemBaseinfoExtend);
                                break;
                            case "99":
                                String PROJECT_CODE = jsondata4.getString("PROJECT_CODE");
                                itemBaseinfo.setItemcode(PROJECT_CODE);
                                itemBaseinfoExtend.setItemcode(PROJECT_CODE);
                                itemBaseinfoExtend.setStatus("2");
                                itemService.updateAuditRsItemBaseinfo(itemBaseinfo);
                                itemextendsService.update(itemBaseinfoExtend);
                                break;
                            case "98":
                                itemBaseinfo.setItemcode("驳回");
                                itemBaseinfoExtend.setItemcode("驳回");
                                itemBaseinfoExtend.setStatus("3");
                                itemService.updateAuditRsItemBaseinfo(itemBaseinfo);
                                itemextendsService.update(itemBaseinfoExtend);
                             break;
                            case "97":
                                itemBaseinfo.setItemcode("作废");
                                itemBaseinfoExtend.setItemcode("作废");
                                itemBaseinfoExtend.setStatus("3");
                                itemService.updateAuditRsItemBaseinfo(itemBaseinfo);
                                itemextendsService.update(itemBaseinfoExtend);
                             break;
                            default:
                                break;
                        }
                        status.put("code", "1"); 
                        status.put("text", "成功");
                    }
                }else {
                    status.put("code", "0"); 
                }
                
            }else {
                status.put("code", "0"); 
            }
            }catch (Exception e) {
                status.put("code", "0"); 
            }
        return JsonUtil.objectToJson(result);
     
    }
}
