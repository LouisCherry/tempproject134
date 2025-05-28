package com.epoint.jnzwfw.auditorga.auditorgausercondition.rest;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.jnzwfw.auditorga.auditorgausercondition.api.IAuditOrgaUserconditionService;
import com.epoint.jnzwfw.auditorga.auditorgausercondition.api.entity.AuditOrgaUsercondition;

@RestController
@RequestMapping("/usercondition")
public class UserConditionRestController
{
    
    @Autowired
    private IAuditOrgaUserconditionService service;
    
    @Autowired
    private ICodeItemsService codeservice;

    @RequestMapping(value = "/getData", method = RequestMethod.POST)
    public String getQNOSum(@RequestBody String params) {
        try {
            JSONObject dataJson = new JSONObject();
            String sql = "select department,username,userstate from audit_orga_usercondition ORDER BY department,ORDERNUMBER desc";
            List<AuditOrgaUsercondition> list = service.findList(sql);
            int deptnum = 0;
            JSONObject listdata = new JSONObject();
            for (AuditOrgaUsercondition user : list) {
                String departval = user.getDepartment();
                JSONArray depart = listdata.getJSONArray(departval);
                if(depart==null){
                    depart = new JSONArray();
                    deptnum++;
                }
                String ksmc = codeservice.getItemTextByCodeName("科室名称", departval);
                if (StringUtil.isNotBlank(ksmc)) {
            	  user.setDepartment(ksmc);
                  user.set("state"+user.getUserstate(), "showstate");
                  depart.add(user);
                  listdata.put(departval, depart);
                }
               
            }
            dataJson.put("total", list.size());
            dataJson.put("deptnum", deptnum);
            dataJson.put("deptdata", listdata);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

}
