package com.epoint.sghd.auditjianguan.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditproject.service.AuditProjectService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.sghd.auditjianguan.domain.AuditProjectPermissionChange;
import com.epoint.sghd.auditjianguan.inter.IAuditTaskBianGeng;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 2018年9月19日
 *
 * @author swy
 */
@Component
@Service
public class AuditTaskBianGengImpl implements IAuditTaskBianGeng {

    @Override
    public AuditCommonResult<PageData<AuditProjectPermissionChange>> getShareFilePageData(Map<String, String> conditionMap,
                                                                                          Integer firstResult, Integer maxResults, String sortField, String sortOrder) {
        AuditCommonResult<PageData<AuditProjectPermissionChange>> result = new AuditCommonResult<PageData<AuditProjectPermissionChange>>();
        try {
            new PageData<AuditProjectPermissionChange>();
            SQLManageUtil sqlManageUtil = new SQLManageUtil();
            PageData<AuditProjectPermissionChange> e = sqlManageUtil.getDbListByPage(AuditProjectPermissionChange.class, conditionMap, firstResult, maxResults,
                    sortField, sortOrder);
            result.setResult(e);
        } catch (Exception arg9) {
            arg9.printStackTrace();
            result.setBusinessFail(arg9.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<AuditProjectPermissionChange> getShareFileByGuid(String rowguid) {
        AuditProjectService auditProjectService = new AuditProjectService();
        AuditCommonResult result = new AuditCommonResult();

        try {
            result.setResult(auditProjectService.getDetail("*", AuditProjectPermissionChange.class, rowguid, "rowGuid"));
        } catch (Exception arg4) {
            arg4.printStackTrace();
            result.setBusinessFail(arg4.toString());
        }
        return result;
    }

}
