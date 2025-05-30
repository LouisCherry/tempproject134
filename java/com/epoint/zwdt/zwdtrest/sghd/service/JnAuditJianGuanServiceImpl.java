package com.epoint.zwdt.zwdtrest.sghd.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.BaseEntity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.zwdt.zwdtrest.sghd.api.IJnAuditJianGuan;
import org.springframework.stereotype.Component;

import java.util.List;

@Service
@Component
public class JnAuditJianGuanServiceImpl implements IJnAuditJianGuan {

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public <T extends Record> int insert(T record) {
        return CommonDao.getInstance().insert(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    @Override
    public <T> T find(Class<? extends BaseEntity> clazz, Object primaryKey) {
        return CommonDao.getInstance().find(clazz, primaryKey);
    }

    /**
     *
     */
    private static final long serialVersionUID = 8242268576782484543L;

    @Override
    public int getAuditProjectPermissionNumByOuguid(String areaCode, String ouguid) {
        return new JnAuditJianGuanService().getAuditProjectPermissionNumByOuguid(areaCode, ouguid);
    }


    @Override
    public int getAuditProjectPermissionNum(String areaCode) {
        return new JnAuditJianGuanService().getAuditProjectPermissionNum(areaCode);
    }

    @Override
    public List<Record> getOuByareaCode(String areaCode) {
        return new JnAuditJianGuanService().getOuByareaCode(areaCode);
    }

    @Override
    public int getAuditProjectPermissionNum2(String areaCode) {
        return new JnAuditJianGuanService().getAuditProjectPermissionNum2(areaCode);
    }


    @Override
    public int getAuditProjectPermissionNumByOuguid2(String areaCode, String ouguid) {
        return new JnAuditJianGuanService().getAuditProjectPermissionNumByOuguid2(areaCode, ouguid);
    }

    @Override
    public String getThemeguidFromAuditPP(String monitorGuid) {
        return new JnAuditJianGuanService().getThemeguidFromAuditPP(monitorGuid);
    }


    @Override
    public List<Record> getFileclientguidByThemeguid(String themeGuid) {
        return new JnAuditJianGuanService().getFileclientguidByThemeguid(themeGuid);
    }

    @Override
    public List<FrameAttachInfo> getAllFrameAttachInfoByCliengGuid(String cliengguid) {
        return new JnAuditJianGuanService().getAllFrameAttachInfoByCliengGuid(cliengguid);
    }



    @Override
    public String getFileclientguidFromMonitor(String rowguid) {
        return new JnAuditJianGuanService().getFileclientguidFromMonitor(rowguid);
    }


    @Override
    public List<Record> getChangeOpinionOuInfo(String areaCode) {
        return new JnAuditJianGuanService().getChangeOpinionOuInfo(areaCode);
    }

    @Override
    public List<FrameUser> getChangeOpinionFrameOu(String ouguid) {
        return new JnAuditJianGuanService().getChangeOpinionFrameOu(ouguid);
    }

    @Override
    public String getCenterUserGuid(String roleName) {
        return new JnAuditJianGuanService().getCenterUserGuid(roleName);
    }

    @Override
    public List<Record> getChangeOpinionFrameOu2(String centerUserGuid, String ouGuid) {
        return new JnAuditJianGuanService().getChangeOpinionFrameOu2(centerUserGuid, ouGuid);
    }

    @Override
    public List<Record> getChangeOpinionFrameOu2(String ouguid) {
        return new JnAuditJianGuanService().getChangeOpinionFrameOu2(ouguid);
    }

    @Override
    public int getTaJianGuanTabYrlCount(String areaCode, String ouguid, String userguid) {
        return new JnAuditJianGuanService().getTaJianGuanTabYrlCount(areaCode, ouguid, userguid);
    }

    @Override
    public int getTaJianGuanTabWrlCount(String areaCode, String ouguid, String userguid) {
        return new JnAuditJianGuanService().getTaJianGuanTabWrlCount(areaCode, ouguid, userguid);
    }

    @Override
    public int getTaJianGuanTabSpxxCount(String sql) {
        return new JnAuditJianGuanService().getTaJianGuanTabSpxxCount(sql);
    }

    @Override
    public int getTaJianGuanTabjgcount(String ouguid, String areaCode) {
        return new JnAuditJianGuanService().getTaJianGuanTabjgcount(ouguid, areaCode);
    }

    @Override
    public int updateFileclientguid(String fileclientguid, String rowguid) {
        return new JnAuditJianGuanService().updateFileclientguid(fileclientguid, rowguid);
    }

    @Override
    public List<AuditProject> getTaProjectWrlInfo(String sql, int first, int pageSize) {
        return new JnAuditJianGuanService().getTaProjectWrlInfo(sql, first, pageSize);
    }

    @Override
    public int getTaProjectWrlNum(String sql) {
        return new JnAuditJianGuanService().getTaProjectWrlNum(sql);
    }


    @Override
    public Boolean judgeSign(String rowguid) {
        return new JnAuditJianGuanService().judgeSign(rowguid);
    }

    @Override
    public String getHandleUrlByRowguid(String rowguid) {
        return new JnAuditJianGuanService().getHandleUrlByRowguid(rowguid);
    }

    @Override
    public String getOuNameFromAuditTask(String rowguid) {
        return new JnAuditJianGuanService().getOuNameFromAuditTask(rowguid);
    }

    @Override
    public List<Record> getCenterOunameinfo(String areaCode) {
        return new JnAuditJianGuanService().getCenterOunameinfo(areaCode);
    }

    @Override
    public List<Record> findChildOuByParentguid(String guid) {
        return new JnAuditJianGuanService().findChildOuByParentguid(guid);
    }

    @Override
    public List<String> findTaskidListByUserguidAndOuguid(String userguid, String ouguid) {
        return new JnAuditJianGuanService().findTaskidListByUserguidAndOuguid(userguid, ouguid);
    }

}
