package com.epoint.departdocking.provinceselfbuild.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.departdocking.provinceselfbuild.api.IProviceSelfBuild;

/**
 * service实现类
 * @author shibin
 * @date  2020年7月17日 下午2:45:24
 */
@Component
@Service
public class ProviceSelfBuildServiceImpl implements IProviceSelfBuild
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public List<AuditProjectMaterial> findProjectMaterialListByProjectguid(String rowguid) {

        return new ProviceSelfBuildService().findProjectMaterialListByProjectguid(rowguid);
    }

}
