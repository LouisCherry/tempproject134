package com.epoint.xmz.ywzt;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.string.StringUtil;

@RestController
@RequestMapping({"/ywztaction"})
public class YwztAction {
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    private IAuditTask iAuditTask;

    @RequestMapping(value = {"/getIsYwzt"}, method = {RequestMethod.POST})
    public String getIsYwzt(@RequestBody String params, @Context HttpServletRequest request) {
        JSONObject dataJson = new JSONObject();
        this.log.info("=======开始调用getIsYwzt接口=======");
        JSONObject jsonObject = JSONObject.parseObject(params);
        JSONObject obj = (JSONObject) jsonObject.get("params");
        String itemCode = obj.getString("itemCode");
        Map<String, String> map = new HashMap<>();
        map.put("is_history = ", "0");
        map.put("ITEM_ID = ", itemCode);
        map.put("IS_ENABLE = ", "1");
        map.put("IS_EDITAFTERIMPORT = ", "1");
        AuditTask auditTask = iAuditTask.getAuditTaskByCondition(map);
        if (StringUtil.isNotBlank(auditTask)) {
            if (StringUtil.isNotBlank(auditTask)) {
//                if (StringUtil.isNotBlank(auditTask.getStr("ywztcode"))) {
//                    jsonObject.put("result", true);
//                    String[] yeztcode = auditTask.getStr("ywztcode").split(";");
//                    if (yeztcode.length >= 1) {
//                        jsonObject.put("ywztcode", yeztcode[0]);
//                    }
//                    //  dataJson.put("itemcode", yeztcode[1]);
//                    jsonObject.put("newItemCode", auditTask.getStr("inner_code"));
//                    return JsonUtils.zwdtRestReturn("1", "业务中台事项", jsonObject.toString());
//                }
                if ("1".equals(auditTask.getStr("ywztcode"))) {
                     return JsonUtils.zwdtRestReturn("1", "业务中台事项", "");
                }
            }
        }
        jsonObject.put("result", false);
        return JsonUtils.zwdtRestReturn("0", "非业务中台事项", jsonObject.toString());
    }


}
