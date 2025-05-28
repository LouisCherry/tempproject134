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
import com.epoint.core.utils.string.StringUtil;
import com.epoint.expert.expertinfo.api.IExpertInfoService;
import com.epoint.expert.expertinfo.api.entity.ExpertInfo;
import com.epoint.expert.expertinstance.api.IExpertInstanceService;
import com.epoint.expert.expertinstance.api.entity.ExpertInstance;
import com.epoint.expert.expertiresult.api.IExpertIResultService;
import com.epoint.expert.expertiresult.api.entity.ExpertIResult;
import com.epoint.util.SqlUtils;

@RestController("expertselectaction")
@Scope("request")
public class ExpertSelectAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Autowired
    private IExpertInfoService expertService;
    @Autowired
    private IExpertIResultService resultService;
    @Autowired
    private IExpertInstanceService instanceService;
    private String instanceGuid;
    private String expertGuids;

    @Override
    public void pageLoad() {
        instanceGuid = getRequestParameter("instanceguid");
        expertGuids = getRequestParameter("expertguids");
    }

    public List<Map<String, Object>> getUnChosenExpertList() {
        List<Map<String, Object>> unChosenList = new ArrayList<>();
        if(StringUtil.isNotBlank(expertGuids)){
            String[] expertArray = expertGuids.split(",");
            ExpertInfo expertInfo;
            Map<String, Object> map;
            for (int i = 0; i < expertArray.length; i++) {
                expertInfo = expertService.find(expertArray[i]);
                map = new HashMap<>();
                map.put("id", expertInfo.getRowguid());
                map.put("text", expertInfo.getName());
                unChosenList.add(map);
            }
        }
        List<Map<String, Object>> chosenList = getChosenExpertList();
        unChosenList.removeAll(chosenList);
        return unChosenList;
    }

    /**
     * 获取已选择专家list
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
        sqlUtil.eq("is_auto", "0");
        List<ExpertIResult> resultList = resultService.findListByCondition(sqlUtil.getMap());
        if (resultList.size() > 0) {
            Map<String, Object> map;
            for (ExpertIResult result : resultList) {
                map = new HashMap<>();
                map.put("id", result.getExpertguid());
                map.put("text", result.getExpertname());
                chosenList.add(map);
            }
        }
        return chosenList;
    }

    public void saveData(String ids, String names) {
        //先删除当前已抽取专家记录
        resultService.deleteByInstanceguid(instanceGuid, "0");
        ExpertInstance expertInstance = instanceService.find(instanceGuid);
        ExpertInfo expertInfo;
        if (StringUtil.isNotBlank(ids)) {
            String[] idArray = ids.split(";");
            for (int i = 0; i < idArray.length; i++) {
                expertInfo = expertService.find(idArray[i]);
                ExpertIResult result = new ExpertIResult();
                result.setRowguid(UUID.randomUUID().toString());
                result.setInstanceguid(instanceGuid);
                result.setExpertguid(expertInfo.getRowguid());
                result.setExpertno(expertInfo.getExpertno());
                result.setExpertname(expertInfo.getName());
                result.setPingbzy(expertInfo.getPingbzy());
                result.setProvince(expertInfo.getProvince());
                result.setCountry(expertInfo.getCountry());
                result.setCity(expertInfo.getCity());
                result.setContactphone(expertInfo.getContactphone());
                result.setEmail(expertInfo.getEmail());
                result.setIs_attend("1");
                result.setIs_auto("0");
                result.set("bidtime", expertInstance.getBidtime());
                resultService.insert(result);
            }

        }
        addCallbackParam("msg", "保存成功！");
    }
}
