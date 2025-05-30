package com.epoint.zwdt.zwdtrest.project.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.zwdt.zwdtrest.project.api.IJnDantiinfoService;
import org.springframework.stereotype.Component;

@Service
@Component
public class JnDantiinfoImpl implements IJnDantiinfoService {

    @Override
    public DantiInfo getDantiInfoByBm(String dtbm) {
        return new JnDantiinfoService().getDantiInfoByBm(dtbm);
    }

    /***** 工改单体赋码与住建图审系统对接需求登记单开始 *****/
    @Override
    public JSONObject insertInfo(String itemGuid) {
        return new JnDantiinfoService().insertInfo(itemGuid);
    }

    @Override
    public JSONObject insertSubsAsync(JSONArray danTiInfos) {
        return new JnDantiinfoService().insertSubsAsync(danTiInfos);
    }

    @Override
    public JSONObject updateSubsAsync(JSONArray danTiInfos) {
        return new JnDantiinfoService().updateSubsAsync(danTiInfos);
    }
    /***** 工改单体赋码与住建图审系统对接需求登记单结束 *****/
}
