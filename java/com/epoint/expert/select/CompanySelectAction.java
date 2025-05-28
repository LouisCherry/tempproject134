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
import com.epoint.expert.expertcompany.api.IExpertCompanyService;
import com.epoint.expert.expertcompany.api.entity.ExpertCompany;
import com.epoint.expert.expertirule.api.IExpertIRuleService;
import com.epoint.expert.expertirule.api.entity.ExpertIRule;
import com.epoint.util.SqlUtils;

/**
 *  [一句话功能简述]
 *  [功能详细描述]
 * @作者 Lee
 * @version [版本号, 2019年8月23日]
 * @see [相关类/方法]
 * @since [产品/模块版本] 
 */
@RestController("companyselectaction")
@Scope("request")
public class CompanySelectAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Autowired
    private IExpertCompanyService companyService;
    @Autowired
    private IExpertIRuleService expertIRuleService;
    private String instanceGuid;
    private static final String RULEOBJECT_COMPANY = "1";

    @Override
    public void pageLoad() {
        instanceGuid = getRequestParameter("instanceguid");
    }

    /**
     * 获取未选择单位
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Map<String, Object>> getUnChosenCompanyList() {
        List<Map<String, Object>> unChosenList = new ArrayList<>();
        SqlUtils sqlUtils = new SqlUtils();
        //未删除
        sqlUtils.eq("is_del", ZwfwConstant.CONSTANT_STR_ZERO);
        //启用
        sqlUtils.eq("Status", ZwfwConstant.CONSTANT_STR_ONE);
        List<ExpertCompany> companies = companyService.findListByCondition(sqlUtils.getMap());
        if (companies.size() > 0) {
            Map<String, Object> map;
            for (ExpertCompany company : companies) {
                map = new HashMap<>();
                map.put("id", company.getRowguid());
                map.put("text", company.getCompanyname());
                unChosenList.add(map);
            }
        }
        List<Map<String, Object>> chosenList = getChosenCompanyList();
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
    public List<Map<String, Object>> getChosenCompanyList() {
        List<Map<String, Object>> chosenList = new ArrayList<>();
        SqlUtils sqlUtil = new SqlUtils();
        sqlUtil.eq("instanceguid", instanceGuid);
        sqlUtil.eq("objecttype", RULEOBJECT_COMPANY);
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

    //保存回避记录
    public void saveData(String ids, String names) {
        //先删除当前回避公司记录
        expertIRuleService.deleteObjectByInstanceguid(instanceGuid, "1");
        if(StringUtil.isNotBlank(ids)){
            String[] idArray = ids.split(";");
            String[] nameArray = names.split(";");
            ExpertIRule rule;
            for (int i = 0; i < idArray.length; i++) {
                rule = new ExpertIRule();
                rule.setRowguid(UUID.randomUUID().toString());
                rule.setInstanceguid(instanceGuid);
                rule.setObjectguid(idArray[i]);
                rule.setObjectname(nameArray[i]);
                rule.setObjecttype("1");
                expertIRuleService.insert(rule);
            }
        }    
        addCallbackParam("msg", "保存成功！");
    }
}
