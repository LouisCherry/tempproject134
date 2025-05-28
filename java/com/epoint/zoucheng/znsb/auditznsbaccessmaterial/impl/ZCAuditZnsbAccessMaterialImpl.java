package com.epoint.zoucheng.znsb.auditznsbaccessmaterial.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.auditznsbaccessmaterial.domain.AuditZnsbAccessMaterial;
import com.epoint.basic.auditqueue.service.AuditQueueBasicService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.zoucheng.znsb.auditznsbaccessmaterial.inter.IZCAuditZnsbAccessMaterial;

@Component
@Service
public class ZCAuditZnsbAccessMaterialImpl implements IZCAuditZnsbAccessMaterial
{


    
    @Override
    public AuditCommonResult<List<AuditZnsbAccessMaterial>> getAccessMaterialList(Map<String, String> conditionMap,
            String fields) {
        AuditQueueBasicService<AuditZnsbAccessMaterial> cabinetService = new AuditQueueBasicService<AuditZnsbAccessMaterial>();
        AuditCommonResult<List<AuditZnsbAccessMaterial>> result = new AuditCommonResult<List<AuditZnsbAccessMaterial>>();
        try {
            result.setResult(cabinetService.selectRecordList(AuditZnsbAccessMaterial.class, conditionMap, fields));
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }
}
