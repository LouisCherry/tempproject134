package com.epoint.departdocking.provinceselfbuild.api;

import java.io.Serializable;
import java.util.List;

import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;

/**
 * service接口
 * @author shibin
 * @date  2020年7月17日 下午2:45:37
 */
public interface IProviceSelfBuild extends Serializable
{

    /**
     * 根据办件标识获取办件材料
     * @description
     * @author shibin
     * @return 
     * @date  2020年7月17日 下午2:49:55
     */
    List<AuditProjectMaterial> findProjectMaterialListByProjectguid(String rowguid);

}
