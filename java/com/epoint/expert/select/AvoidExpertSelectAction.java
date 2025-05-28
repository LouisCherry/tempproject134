package com.epoint.expert.select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.expert.expertinfo.api.IExpertInfoService;
import com.epoint.expert.expertinfo.api.entity.ExpertInfo;
import com.epoint.expert.expertirule.api.IExpertIRuleService;
import com.epoint.expert.expertirule.api.entity.ExpertIRule;
import com.epoint.util.SqlUtils;

@RestController("avoidexpertselectaction")
@Scope("request")
public class AvoidExpertSelectAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Autowired
    private IExpertInfoService expertService;
    @Autowired
    private IExpertIRuleService expertIRuleService;
    private String instanceGuid;
    private static final String RULEOBJECT_EXPERT = "2";

    @Override
    public void pageLoad() {
        instanceGuid = getRequestParameter("instanceguid");
    }

    public List<Map<String, Object>> getUnChosenExpertList() {
        List<Map<String, Object>> unChosenList = new ArrayList<>();
        SqlUtils sqlUtils = new SqlUtils();
        //未删除
        sqlUtils.eq("is_del", ZwfwConstant.CONSTANT_STR_ZERO);
        //启用
        sqlUtils.eq("Status", ZwfwConstant.CONSTANT_STR_ONE);
        List<ExpertInfo> expertInfos = expertService.findListByCondition(sqlUtils.getMap());
        if (expertInfos.size() > 0) {
            Map<String, Object> map;
            for (ExpertInfo expert : expertInfos) {
                map = new HashMap<>();
                map.put("id", expert.getRowguid());
                map.put("text", expert.getName());
                unChosenList.add(map);
            }
        }
        List<Map<String, Object>> chosenList = getChosenExpertList();
        unChosenList.removeAll(chosenList);
        return unChosenList;
    }

    /**
     * 获取已选择回避的专家list
     *  [一句话功能简述]
     *  [功能详细描述]
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Map<String, Object>> getChosenExpertList() {
        List<Map<String, Object>> chosenList = new ArrayList<>();
        SqlUtils sqlUtil = new SqlUtils();
        sqlUtil.eq("instanceguid", instanceGuid);
        sqlUtil.eq("objecttype", RULEOBJECT_EXPERT);
        List<ExpertIRule> ruleList = expertIRuleService.findListByCondition(sqlUtil.getMap());
        if (ruleList.size() > 0) {
            Map<String, Object> map;
            for (ExpertIRule rule : ruleList) {
                map = new HashMap<>();
                map.put("id", rule.getObjectguid());
                map.put("text", rule.getObjectname());
                chosenList.add(map);
            }
        }
        return chosenList;
    }

    public void saveData(String ids, String names) {
        //先删除当前回避专家记录
        expertIRuleService.deleteObjectByInstanceguid(instanceGuid, "2");
        ExpertIRule rule;
        if(StringUtil.isNotBlank(ids)){
            String[] idArray = ids.split(";");
            String[] nameArray = names.split(";");
            for (int i = 0; i < idArray.length; i++) {
                rule = new ExpertIRule();
                rule.setRowguid(UUID.randomUUID().toString());
                rule.setInstanceguid(instanceGuid);
                rule.setObjectguid(idArray[i]);
                rule.setObjectname(nameArray[i]);
                rule.setObjecttype("2");
                expertIRuleService.insert(rule);
            }
            
        }
        addCallbackParam("msg", "保存成功！");
    }
}
