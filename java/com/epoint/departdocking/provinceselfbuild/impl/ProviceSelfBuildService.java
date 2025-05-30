package com.epoint.departdocking.provinceselfbuild.impl;

import java.util.List;

import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;

/**
 * 后台service
 * @author shibin
 * @date  2020年7月17日 下午2:45:45
 */
public class ProviceSelfBuildService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public ProviceSelfBuildService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 根据办件标识获取办件材料
     * @description
     * @author shibin
     * @date  2020年7月17日 下午2:51:45
     */
    public List<AuditProjectMaterial> findProjectMaterialListByProjectguid(String rowguid) {
        String sql = "SELECT * from audit_project_material WHERE PROJECTGUID = ?1 ";
        return baseDao.findList(sql, AuditProjectMaterial.class, rowguid);
    }

}
