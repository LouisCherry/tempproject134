package com.epoint.basic.auditsp.dantiinfo.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditsp.dantiinfo.api.IJnDantiinfoService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

@Service
@Component
public class JnDantiinfoImpl implements IJnDantiinfoService {

    @Override
    public List<DantiInfo> getUnFmDantiInfoList() {
        return new JnDantiinfoService().getUnFmDantiInfoList();
    }

    @Override
    public List<DantiInfo> getErrorDantiInfoListByProjectguid(String projectguid) {
        return new JnDantiinfoService().getErrorDantiInfoListByProjectguid(projectguid);
    }

    @Override
    public DantiInfo getErrorDantiInfoListByRowguid(String rowguid)  {
        return new JnDantiinfoService().getErrorDantiInfoListByRowguid(rowguid);
    }

    @Override
    public List<DantiInfo> getDantiInfoAttachList() {
        return new JnDantiinfoService().getDantiInfoAttachList();
    }

    @Override
    public PageData<DantiInfo> pageDantiList(Record record, int first, int pageSize, String sortField, String sortOrder) {
        return new JnDantiinfoService().pageDantiList(record, first, pageSize, sortField, sortOrder);
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
