package com.epoint.rentcar.service;

import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;

public class RentCarService {

    private ICommonDao baseDao;

    public RentCarService(){
        if (baseDao == null){
            baseDao = CommonDao.getInstance();
        }
    }

    public AuditProjectMaterial getAuditProjectMaterial(String projectGuid,String taskMaterialGuid){
        String sql = "select apm.* from audit_project_material apm where projectguid=? and apm.TASKMATERIALGUID=?";
        return baseDao.find(sql, AuditProjectMaterial.class, projectGuid, taskMaterialGuid);
    }

    public AuditTaskMaterial getAuditTaskMaterial(String projectGuid, int type) {
        String sql = "select atm.* from audit_project_material apm,audit_task_material atm where projectguid=? and apm.TASKMATERIALGUID=atm.rowguid and atm.material_ownership=?";
        return baseDao.find(sql, AuditTaskMaterial.class, projectGuid, type);
    }
}
