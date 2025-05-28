package com.epoint.lsyc.comprehensivewindow.meta;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.epoint.basic.auditresource.auditrssharemaerial.domain.AuditRsShareMetadata;
import com.epoint.basic.auditresource.auditrssharemaerial.inter.IAuditRsShareMetadata;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.BaseEntity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.lsyc.common.ZjjZwfwConstant;
import com.epoint.lsyc.comprehensivewindow.meta.domain.AuditRsShareMetadataSp;
import com.epoint.lsyc.comprehensivewindow.meta.domain.MetaShare;
import com.epoint.lsyc.comprehensivewindow.meta.domain.MetaShareRelation;

/**
 * 111对应的后台service
 * 
 * @author xc
 * @version [版本号, 2019-03-28 14:23:25]
 */
public class AuditRsShareMetadataSpService {
    /**
     * 数据增删改查组件
     */
    private ICommonDao baseDao;

    public AuditRsShareMetadataSpService() {
        baseDao = CommonDao.getInstance();
    }

    public AuditCommonResult<Void> initSPShareMetaFromTask(String businedssguid, String phaseguid) {
        //处理共享元数据
        //1.拿到主题配置的事项
        //2.拿到事项对应的共享元数据
        //3.new一个AuditRsShareMetadataSp
        //4.建立AuditRsShareMetadataSp和单个事项元数据的对应关系，存在MetaShareRelation
        //5.对于非共享元数据，每个事项对应一个AuditRsShareMetadataSp
        IAuditSpTask spTaskService=ContainerFactory.getContainInfo().getComponent(IAuditSpTask.class);
        List<String> taskIDs = (List) spTaskService.getAllAuditSpTaskByPhaseguid(phaseguid).getResult().stream().map(sptask -> sptask.getTaskid()).collect(Collectors.toList());
        //删除原先共享数据
        baseDao.execute(
                "delete from metasharerelation where metaspguid in "
                        + "(select rowguid from AUDIT_RS_SHARE_METADATA_SP where businessGuid=? and phaseguid=?)",
                businedssguid, phaseguid);
        baseDao.execute("delete from AUDIT_RS_SHARE_METADATA_SP where businessGuid=? and phaseguid=?", businedssguid,
                phaseguid);
        if (taskIDs != null && taskIDs.size() > 0) {
            IAuditTask auditTaskBasicImpl=ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            IAuditRsShareMetadata iAuditRsShareMetadata=ContainerFactory.getContainInfo().getComponent(IAuditRsShareMetadata.class);
            IMetaShare imetashare=ContainerFactory.getContainInfo().getComponent(IMetaShare.class);
            for (String taskID : taskIDs) {
                //获得taskbasicid
                AuditTask auditTask = auditTaskBasicImpl.selectUsableTaskByTaskID(taskID).getResult();
                String taskbasicid = "";
                if (auditTask != null) {
                    taskbasicid = auditTask.getStr("taskbasicid");
                }
                SqlConditionUtil sql = new SqlConditionUtil();
                // 共享材料配置信息主键
                sql.eq("MATERIALGUID", taskbasicid);
                sql.setOrderDesc("ordernum");
                List<AuditRsShareMetadata> rsShareMetas = iAuditRsShareMetadata.selectAuditRsShareMetadataByCondition(sql.getMap()).getResult();
                //把每个事项的元数据放在联办主题中
                Iterator iterator = rsShareMetas.iterator();
                while (iterator.hasNext()) {
                    AuditRsShareMetadata rssharemeta = (AuditRsShareMetadata) iterator.next();
                    if (rssharemeta != null) {
                        AuditRsShareMetadataSp shareMetaSp = new AuditRsShareMetadataSp();
                        BeanUtils.copyProperties(rssharemeta, shareMetaSp, "Sql_TableName");
                        shareMetaSp.setBusinessGuid(businedssguid);
                        shareMetaSp.setRowguid(UUID.randomUUID().toString());
                        shareMetaSp.setSql_TableName("AUDIT_RS_SHARE_METADATA_SP");
                        shareMetaSp.setPhaseguid(phaseguid);
                        if (StringUtils.isNotBlank(rssharemeta.get("shareguid"))) {
                            MetaShare metaShare1 = imetashare.getMetaShare(rssharemeta.get("shareguid"));
                            if (metaShare1 != null) {
                                shareMetaSp.setFieldname(metaShare1.getFieldname());
                                shareMetaSp.setFieldchinesename(metaShare1.getFieldchinesename());
                            }
                            shareMetaSp.setIsshare(ZwfwConstant.CONSTANT_INT_ONE);
                            shareMetaSp.setEffictiverange(ZjjZwfwConstant.EFFICTIVE_GLOBAL);
                            int count = baseDao.queryInt(
                                    "select count(*) from metasharerelation where metaspguid in"
                                            + "(select rowguid from AUDIT_RS_SHARE_METADATA_SP where businessGuid=? and phaseguid=?) and shareguid=?",
                                    businedssguid, phaseguid, rssharemeta.get("shareguid"));
                            if (count == 0) {
                                baseDao.insert(shareMetaSp);
                            }
                            else {
                                shareMetaSp = baseDao.find(
                                        "select * from AUDIT_RS_SHARE_METADATA_SP where businessGuid=? and phaseguid=? and rowguid in"
                                                + "(select metaspguid from metasharerelation where shareguid=?)",
                                        AuditRsShareMetadataSp.class, businedssguid, phaseguid,
                                        rssharemeta.get("shareguid"));
                            }
                            MetaShareRelation metaShare = new MetaShareRelation();
                            metaShare.setRowguid(UUID.randomUUID().toString());
                            metaShare.setMetaspguid(shareMetaSp.getRowguid());
                            metaShare.setShareguid(rssharemeta.get("shareguid"));
                            metaShare.setMetaguid(rssharemeta.getRowguid());
                            baseDao.insert(metaShare);
                        }
                        else {
                            baseDao.insert(shareMetaSp);
                        }
                    }
                }
            }
        }
        return null;
    }

    public int updateShareMetaSpData(AuditRsShareMetadataSp me) {
        return baseDao.update(me);
    }

    public int deleteShareMetaSpData(String rowguid) {
    	AuditRsShareMetadataSp sp = baseDao.find(AuditRsShareMetadataSp.class, rowguid);
        return baseDao.delete(sp);
    }
    
    public int deleteShareMetaDataByRowGuid(String rowguid) {
        AuditRsShareMetadataSp sp = baseDao.find(AuditRsShareMetadataSp.class, rowguid);
        //删除关联表
        baseDao.execute("delete from metasharerelation where Metaspguid=?", rowguid);
        return baseDao.delete(sp);
    }

    public int insertRecord(AuditRsShareMetadataSp me) {
        return baseDao.insert(me);
    }

    public AuditCommonResult<PageData<AuditRsShareMetadataSp>> selectAuditRsShareMetaDataPageData(
            Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap, int first, int pageSize,
            String sortField, String sortOrder) {
        AuditCommonResult<PageData<AuditRsShareMetadataSp>> result = new AuditCommonResult<PageData<AuditRsShareMetadataSp>>();
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditRsShareMetadataSp.class);
        PageData<AuditRsShareMetadataSp> sps = sqlManageUtil.getDbListByPage(baseClass, conditionMap,
                Integer.valueOf(first), Integer.valueOf(pageSize), sortField, sortOrder);
        result.setResult(sps);
        return result;
    }

    public AuditCommonResult<List<MetaShareRelation>> selectByShareMaterialGuid(String businessGuid, String shareguid) {
        AuditCommonResult<List<MetaShareRelation>> result = new AuditCommonResult<List<MetaShareRelation>>();
        List<MetaShareRelation> relas = baseDao.findList("select * from metasharerelation where metaspguid=?",
                MetaShareRelation.class, shareguid);
        result.setResult(relas);
        return result;
    }

    public AuditCommonResult<AuditRsShareMetadata> getMeta(String metaguid) {
        AuditCommonResult<AuditRsShareMetadata> result = new AuditCommonResult<AuditRsShareMetadata>();
        AuditRsShareMetadata meta = baseDao.find(AuditRsShareMetadata.class, metaguid);
        result.setResult(meta);
        return result;
    }

    public AuditCommonResult<List<AuditRsShareMetadataSp>> getMetaSpListByMetaguid(String metaguid) {
        AuditCommonResult<List<AuditRsShareMetadataSp>> result = new AuditCommonResult<List<AuditRsShareMetadataSp>>();
        String sql="SELECT * from AUDIT_RS_SHARE_METADATA_SP WHERE ROWGUID IN (SELECT METASPGUID FROM METASHARERELATION WHERE METAGUID=?)";
        List<AuditRsShareMetadataSp> splist = baseDao.findList(sql,AuditRsShareMetadataSp.class, metaguid);
        result.setResult(splist);
        return result;
    }
    
    public AuditCommonResult<AuditRsShareMetadataSp> getMetaSp(String metaspguid) {
        AuditCommonResult<AuditRsShareMetadataSp> result = new AuditCommonResult<AuditRsShareMetadataSp>();
        AuditRsShareMetadataSp sp = baseDao.find(AuditRsShareMetadataSp.class, metaspguid);
        result.setResult(sp);
        return result;
    }

    public boolean ifMetaRelation(String metaguid, String phaseguid) {
        int count = 0;
        count = baseDao.queryInt("select count(1) from metasharerelation where metaguid =? and metaspguid in"
                + "(select rowguid from AUDIT_RS_SHARE_METADATA_SP where phaseguid=?)", metaguid, phaseguid);
        if (count > 0) {
            return true;
        }
        return false;
    }

    public int deleteRelationByMetaSp(String metaspguid) {
        return baseDao.execute("delete from metasharerelation where metaspguid=?", metaspguid);
    }

    public AuditCommonResult<List<AuditRsShareMetadataSp>> selectAuditRsShareMetadataByBusinessguid(String businessguid,
            String sortField, String sortValue) {
        AuditCommonResult result = new AuditCommonResult();
        String url = ConfigUtil.getConfigValue("jdbczwfw", "rsurl");
        String name = ConfigUtil.getConfigValue("jdbczwfw", "rsusername");
        String password = ConfigUtil.getConfigValue("jdbczwfw", "rspassword");
        SQLManageUtil sqlManageUtil;
        if (StringUtil.isBlank(url)) {
            sqlManageUtil = new SQLManageUtil();
        }
        else {
            DataSourceConfig e = new DataSourceConfig(url, name, password);
            sqlManageUtil = new SQLManageUtil(e);
        }
        try {
            SqlConditionUtil e1 = new SqlConditionUtil();
            if (StringUtil.isNotBlank(businessguid)) {
                e1.eq("businessguid", businessguid);
            }

            e1.setOrder(sortField, sortValue);
            List results = sqlManageUtil.getListByCondition(AuditRsShareMetadataSp.class, e1.getMap());
            result.setResult(results);
            sqlManageUtil.closeDao();
        }
        catch (Exception arg10) {
            result.setSystemFail(arg10.toString());
        }

        return result;
    }

    public AuditCommonResult<List<AuditRsShareMetadataSp>> selectAuditRsShareMetadataByBusinessguid(String businessguid,
            String phaseguid, String sortField, String sortValue) {
        AuditCommonResult result = new AuditCommonResult();
        String url = ConfigUtil.getConfigValue("jdbczwfw", "rsurl");
        String name = ConfigUtil.getConfigValue("jdbczwfw", "rsusername");
        String password = ConfigUtil.getConfigValue("jdbczwfw", "rspassword");
        SQLManageUtil sqlManageUtil;
        if (StringUtil.isBlank(url)) {
            sqlManageUtil = new SQLManageUtil();
        }
        else {
            DataSourceConfig e = new DataSourceConfig(url, name, password);
            sqlManageUtil = new SQLManageUtil(e);
        }
        try {
            SqlConditionUtil e1 = new SqlConditionUtil();
            if (StringUtil.isNotBlank(businessguid)) {
                e1.eq("businessguid", businessguid);
            }
            if (StringUtil.isNotBlank(phaseguid)) {
                e1.eq("phaseguid", phaseguid);
            }
            e1.setOrder(sortField, sortValue);
            List results = sqlManageUtil.getListByCondition(AuditRsShareMetadataSp.class, e1.getMap());
            result.setResult(results);
            sqlManageUtil.closeDao();
        }
        catch (Exception arg10) {
            result.setSystemFail(arg10.toString());
        }

        return result;
    }

    public AuditCommonResult<List<AuditRsShareMetadataSp>> selectAuditRsShareMetadataByBusinessguidAndClassguid(String businessguid, String phaseguid, String classguid) {
        AuditCommonResult result = new AuditCommonResult();
        String url = ConfigUtil.getConfigValue("jdbczwfw", "rsurl");
        String name = ConfigUtil.getConfigValue("jdbczwfw", "rsusername");
        String password = ConfigUtil.getConfigValue("jdbczwfw", "rspassword");
        SQLManageUtil sqlManageUtil;
        if (StringUtil.isBlank(url)) {
            sqlManageUtil = new SQLManageUtil();
        }
        else {
            DataSourceConfig e = new DataSourceConfig(url, name, password);
            sqlManageUtil = new SQLManageUtil(e);
        }
        try {
            SqlConditionUtil e1 = new SqlConditionUtil();
            if (StringUtil.isNotBlank(businessguid)) {
                e1.eq("businessguid", businessguid);
            }
            if (StringUtil.isNotBlank(phaseguid)) {
                e1.eq("phaseguid", phaseguid);
            }
            if (StringUtil.isNotBlank(classguid)) {
                e1.eq("classguid", classguid);
            }
            List results = sqlManageUtil.getListByCondition(AuditRsShareMetadataSp.class, e1.getMap());
            result.setResult(results);
            sqlManageUtil.closeDao();
        }
        catch (Exception arg10) {
            result.setSystemFail(arg10.toString());
        }
        return result;
    }
    
    public AuditCommonResult<AuditRsShareMetadata> selectMeta(String shareguid, String taskid) {
        AuditCommonResult result = new AuditCommonResult();
        AuditRsShareMetadata meta = baseDao.find(
                "select * from AUDIT_RS_SHARE_METADATA where materialguid=? and rowguid in"
                        + "(select metaguid from metasharerelation where metaspguid=?)",
                AuditRsShareMetadata.class, taskid, shareguid);
        result.setResult(meta);
        return result;
    }

    public AuditCommonResult<List<AuditRsShareMetadata>> selectMetaList(String fieldname, String taskid) {
        AuditCommonResult result = new AuditCommonResult();
        List<AuditRsShareMetadata> metas = baseDao.findList(
                "select * from AUDIT_RS_SHARE_METADATA where materialguid=? and FIELDNAME =?",
                AuditRsShareMetadata.class, taskid, fieldname);
        result.setResult(metas);
        return result;
    }

    /* 
    public List<AuditTask> findByTaskbasicId(String taskbasicid) {
        //system.out.println("taskbasicid"+taskbasicid);
        List<AuditTask> audittasks = ibaseDao.findList("select rowguid,task_id from AUDIT_TASK where taskbasicid=? ",
                AuditTask.class, taskbasicid);
        return audittasks;
    }*/

    public List<AuditRsShareMetadata> getDatas(String rowguid) {
        String sql = "select m.materialguid ,m.rowguid from AUDIT_RS_SHARE_METADATA m , metasharerelation s"
                + "  where  m.rowguid=s.metaguid and s.metaspguid=?";
        return baseDao.findList(sql, AuditRsShareMetadata.class, rowguid);
    }
    
    public AuditRsShareMetadataSp getMetaShareRelationCountByBusinessGuidAndPhaseguidAndShareguid(String businessguid, String phaseguid, String shareguid) {
        return baseDao.find(
                "select * from AUDIT_RS_SHARE_METADATA_SP where businessGuid=? and phaseguid=? and rowguid in"
                        + "(select metaspguid from metasharerelation where shareguid=?)",
                AuditRsShareMetadataSp.class, businessguid, phaseguid,shareguid);
    }
    
    public List<AuditRsShareMetadata> seletemetadataListByMetasharerelationMetaspguidAndShareguid(String metaspguid, String shareguid) {
        String sql="SELECT * from audit_rs_share_metadata where rowguid in (SELECT metaguid from metasharerelation where metaspguid=? and shareguid=?) and NOTNULL='1'";
        return baseDao.findList(sql,AuditRsShareMetadata.class, metaspguid, shareguid);
    }
    
    public List<AuditRsShareMetadataSp> getAuditRsShareMetadataSpListByBusinessGuidAndPhaseguidAndFieldname(String businessguid, String phaseguid, String fieldname) {
        return baseDao.findList("select * from AUDIT_RS_SHARE_METADATA_SP where businessGuid=? and phaseguid=? and fieldname=?",
                AuditRsShareMetadataSp.class, businessguid, phaseguid,fieldname);
    }
}
