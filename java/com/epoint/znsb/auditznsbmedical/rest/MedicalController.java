package com.epoint.znsb.auditznsbmedical.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.znsb.auditznsbmedical.api.IAuditZnsbMedicalService;
import com.epoint.znsb.auditznsbmedical.api.entity.AuditZnsbMedical;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/medical")
public class MedicalController {

    @Autowired
    private IAuditZnsbMedicalService medicalService;

    @RequestMapping(value = "/getMedicalList", method = RequestMethod.POST)
    public String getMedicalList(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String medicalname = obj.getString("medicalname");
            List<AuditZnsbMedical> medicalList = new ArrayList<>();
            if(StringUtil.isBlank(medicalname)){
                medicalList =  medicalService.findList("getMedicalList");
            }else{
                medicalList = medicalService.findList("getMedicalListByName","%"+medicalname +"%");
            }
            dataJson.put("medicallist",medicalList);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }



}