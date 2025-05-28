package com.epoint.xmz.thirdreporteddata.basic.audittask.meterial.service;

import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.frame.service.metadata.datasource.api.IDataSourceService;

import java.util.List;

public class GxhAuditTaskMaterialService {
    /**
     * 通用dao
     */
    private ICommonDao commonDao;
    private IDataSourceService dataSourceService = ContainerFactory.getContainInfo()
            .getComponent(IDataSourceService.class);

    public GxhAuditTaskMaterialService() {
        commonDao = CommonDao.getInstance("task");
    }


    public List<AuditTaskMaterial> getUsableMaterialListByTaskguid(String taskguid) {
        String sql = "select * from audit_task_material  where TASKGUID =? order by ordernum desc";
        return commonDao.findList(sql, AuditTaskMaterial.class, taskguid, taskguid);
    }
}
