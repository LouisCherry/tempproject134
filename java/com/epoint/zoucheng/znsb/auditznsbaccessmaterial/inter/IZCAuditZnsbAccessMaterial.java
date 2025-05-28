package com.epoint.zoucheng.znsb.auditznsbaccessmaterial.inter;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.epoint.basic.auditqueue.auditznsbaccessmaterial.domain.AuditZnsbAccessMaterial;
import com.epoint.basic.auditqueue.auditznsbaccessmaterial.inter.IAuditZnsbAccessMaterial;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;

@Service
public interface IZCAuditZnsbAccessMaterial
{


    /**
     * 获取列表
     *  @param conditionMap
     *  @param fields
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<List<AuditZnsbAccessMaterial>> getAccessMaterialList(Map<String, String> conditionMap,String fields);
}

