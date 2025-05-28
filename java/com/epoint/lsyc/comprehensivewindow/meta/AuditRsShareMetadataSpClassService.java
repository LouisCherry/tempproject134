package com.epoint.lsyc.comprehensivewindow.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.BaseEntity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.lsyc.comprehensivewindow.meta.domain.AuditRsShareMetadataSp;
import com.epoint.lsyc.comprehensivewindow.meta.domain.AuditRsShareMetadataSpClass;

/**
 * 111对应的后台service
 * 
 * @author xc
 * @version [版本号, 2019-03-28 14:23:25]
 */
public class AuditRsShareMetadataSpClassService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditRsShareMetadataSpClassService() {
        baseDao = CommonDao.getInstance();
    }

    public int updateAuditRsShareMetadataSpClass(AuditRsShareMetadataSpClass me) {
        return baseDao.update(me);
    }

    int deleteAuditRsShareMetadataSpClassByRowGuid(String rowguid) {
        AuditRsShareMetadataSpClass sp = baseDao.find(AuditRsShareMetadataSpClass.class, rowguid);
        if (sp != null) {
            //删除关联过的字段分类
            IAuditRsShareMetadataSp iAuditRsShareMetadataSp = ContainerFactory.getContainInfo()
                    .getComponent(IAuditRsShareMetadataSp.class);
            List<AuditRsShareMetadataSp> metadataSps = iAuditRsShareMetadataSp
                    .selectAuditRsShareMetadataByBusinessguidAndClassguid(sp.getBusinessGuid(), sp.getPhaseguid(),
                            rowguid)
                    .getResult();
            if (metadataSps != null && !metadataSps.isEmpty()) {
                for (AuditRsShareMetadataSp auditRsShareMetadataSp : metadataSps) {
                    auditRsShareMetadataSp.setClassguid(null);
                    iAuditRsShareMetadataSp.updateShareMetaSpData(auditRsShareMetadataSp);
                }
            }
            baseDao.execute("delete from metasharerelation where Metaspguid=?", rowguid);
            return baseDao.delete(sp);
        }
        else {
            return ZwfwConstant.CONSTANT_INT_ZERO;
        }
    }

    public int insertRecord(AuditRsShareMetadataSpClass me) {
        return baseDao.insert(me);
    }

    public AuditRsShareMetadataSpClass getAuditRsShareMetadataSpClass(String rowguid) {
        return baseDao.find(AuditRsShareMetadataSpClass.class, rowguid);
    }

    public AuditCommonResult<PageData<AuditRsShareMetadataSpClass>> selectAuditRsShareMetadataSpClassPageData(
            Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap, int first, int pageSize,
            String sortField, String sortOrder) {
        AuditCommonResult<PageData<AuditRsShareMetadataSpClass>> result = new AuditCommonResult<PageData<AuditRsShareMetadataSpClass>>();
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditRsShareMetadataSpClass.class);
        PageData<AuditRsShareMetadataSpClass> sps = sqlManageUtil.getDbListByPage(baseClass, conditionMap,
                Integer.valueOf(first), Integer.valueOf(pageSize), sortField, sortOrder);
        result.setResult(sps);
        return result;
    }

    public AuditCommonResult<List<AuditRsShareMetadataSpClass>> selectAuditRsShareMetadataSpClassByBusinessguid(
            String businessGuid, String phaseguid) {
        AuditCommonResult<List<AuditRsShareMetadataSpClass>> result = new AuditCommonResult<List<AuditRsShareMetadataSpClass>>();
        String sql = "SELECT * FROM AUDIT_RS_SHARE_METADATA_SP_CLASS WHERE BUSINESSGUID=?";
        if (StringUtil.isNotBlank(phaseguid)) {
            sql += " AND PHASEGUID='" + phaseguid + "'";
        }
        List<AuditRsShareMetadataSpClass> relas = baseDao.findList(sql, AuditRsShareMetadataSpClass.class,
                businessGuid);
        result.setResult(relas);
        return result;
    }

    public AuditCommonResult<List<AuditRsShareMetadataSpClass>> selectAuditRsShareMetadataSpClassByMaterialguid(
            String materialguid) {
        AuditCommonResult<List<AuditRsShareMetadataSpClass>> result = new AuditCommonResult<List<AuditRsShareMetadataSpClass>>();
        List<AuditRsShareMetadataSpClass> relas = new ArrayList<AuditRsShareMetadataSpClass>();
        if (StringUtil.isNotBlank(materialguid)) {
            String sql = "SELECT * FROM AUDIT_RS_SHARE_METADATA_SP_CLASS WHERE MATERIALGUID=?";
            relas = baseDao.findList(sql, AuditRsShareMetadataSpClass.class, materialguid);
        }
        result.setResult(relas);
        return result;
    }

    public void deleteAuditRsShareMetadataSpClassByPhaseguid(String phaseguid) {
        String sql = "delete from audit_rs_share_metadata_sp_class where phaseguid=?";
        baseDao.execute(sql, phaseguid);
    }

    public AuditRsShareMetadataSpClass selectAuditRsShareMetadataSpClassByTaskId(String phaseguid, String taskid) {
        String sql = "select * from audit_rs_share_metadata_sp_class where phaseguid=? and task_id=?";
        return baseDao.find(sql, AuditRsShareMetadataSpClass.class, phaseguid, taskid);
    }
}
