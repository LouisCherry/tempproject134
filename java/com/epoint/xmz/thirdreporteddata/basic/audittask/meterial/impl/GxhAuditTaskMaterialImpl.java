package com.epoint.xmz.thirdreporteddata.basic.audittask.meterial.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.xmz.thirdreporteddata.basic.audittask.meterial.inter.GxhIAuditTaskMaterial;
import com.epoint.xmz.thirdreporteddata.basic.audittask.meterial.service.GxhAuditTaskMaterialService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Service
public class GxhAuditTaskMaterialImpl implements GxhIAuditTaskMaterial {

    @Override
    public AuditCommonResult<List<AuditTaskMaterial>> getUsableMaterialListByTaskguid(String taskguid) {
        GxhAuditTaskMaterialService audittaskmaterialservice = new GxhAuditTaskMaterialService();
        AuditCommonResult<List<AuditTaskMaterial>> result = new AuditCommonResult<List<AuditTaskMaterial>>();
        try {
            List<AuditTaskMaterial> materialList = audittaskmaterialservice.getUsableMaterialListByTaskguid(taskguid);
            result.setResult(materialList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
