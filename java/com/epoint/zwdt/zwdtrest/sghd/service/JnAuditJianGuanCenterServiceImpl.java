package com.epoint.zwdt.zwdtrest.sghd.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.grammar.Record;
import com.epoint.zwdt.zwdtrest.sghd.api.IJnAuditJianGuanCenter;
import org.springframework.stereotype.Component;

import java.util.List;

@Service
@Component
public class JnAuditJianGuanCenterServiceImpl implements IJnAuditJianGuanCenter {

    /**
     *
     */
    private static final long serialVersionUID = 7992086633051515618L;

    @Override
    public int getSpxxCount(String sql) {
        return new JnAuditJianGuanCenterService().getSpxxCount(sql);
    }

    @Override
    public int getBianGengNum(String areaCode) {
        return new JnAuditJianGuanCenterService().getBianGengNum(areaCode);
    }

    @Override
    public int getHdxzCount(String areaCode) {
        return new JnAuditJianGuanCenterService().getHdxzCount(areaCode);
    }

    @Override
    public int getYrl(String areaCode) {
        return new JnAuditJianGuanCenterService().getYrl(areaCode);
    }

    @Override
    public int getWrl(String areaCode) {
        return new JnAuditJianGuanCenterService().getWrl(areaCode);
    }

    @Override
    public int getYrlByAreaCode(String areaCode) {
        return new JnAuditJianGuanCenterService().getYrlByAreaCode(areaCode);
    }

    @Override
    public int getYrlByOuguid(String ouguid) {
        return new JnAuditJianGuanCenterService().getYrlByOuguid(ouguid);
    }

    @Override
    public int getWrlByAreaCode(String areaCode) {
        return new JnAuditJianGuanCenterService().getWrlByAreaCode(areaCode);
    }

    @Override
    public int getWrlByOuguid(String ouguid) {
        return new JnAuditJianGuanCenterService().getWrlByOuguid(ouguid);
    }

    @Override
    public List<Record> getOuInfo(String areaCode) {
        return new JnAuditJianGuanCenterService().getOuInfo(areaCode);
    }

    @Override
    public List<AuditProject> getProejctCenter(String sql, int first, int pageSize) {
        return new JnAuditJianGuanCenterService().getProejctCenter(sql, first, pageSize);
    }

    @Override
    public String getOuNameFromAuditTask(String rowguid) {
        return new JnAuditJianGuanCenterService().getOuNameFromAuditTask(rowguid);
    }

    @Override
    public int getProejctCenterNum(String sql) {
        return new JnAuditJianGuanCenterService().getProejctCenterNum(sql);
    }

    @Override
    public List<Record> listOuByAreacode(String areacode) {
        return new JnAuditJianGuanCenterService().listOuByAreacode(areacode);
    }

}
