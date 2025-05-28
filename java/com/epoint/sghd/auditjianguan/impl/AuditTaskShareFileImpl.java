package com.epoint.sghd.auditjianguan.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditproject.service.AuditProjectService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.sghd.auditjianguan.domain.AuditTaskShareFile;
import com.epoint.sghd.auditjianguan.inter.IAuditTaskShareFile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 2018年9月19日
 *
 * @author swy
 */
@Component
@Service
public class AuditTaskShareFileImpl implements IAuditTaskShareFile {

    @Override
    public AuditCommonResult<PageData<AuditTaskShareFile>> getShareFilePageData(Map<String, String> conditionMap,
                                                                                Integer firstResult, Integer maxResults, String sortField, String sortOrder) {
        AuditCommonResult<PageData<AuditTaskShareFile>> result = new AuditCommonResult<PageData<AuditTaskShareFile>>();
        try {
            new PageData<AuditTaskShareFile>();
            SQLManageUtil sqlManageUtil = new SQLManageUtil();
            PageData<AuditTaskShareFile> e = sqlManageUtil.getDbListByPage(AuditTaskShareFile.class, conditionMap,
                    firstResult, maxResults, sortField, sortOrder);
            result.setResult(e);
        } catch (Exception arg9) {
            arg9.printStackTrace();
            result.setBusinessFail(arg9.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<AuditTaskShareFile> getShareFileByGuid(String rowguid) {
        AuditProjectService auditProjectService = new AuditProjectService();
        AuditCommonResult result = new AuditCommonResult();

        try {
            result.setResult(auditProjectService.getDetail("*", AuditTaskShareFile.class, rowguid, "rowGuid"));
        } catch (Exception arg4) {
            arg4.printStackTrace();
            result.setBusinessFail(arg4.toString());
        }
        return result;
    }

    @Override
    public List<AuditTaskShareFile> getShareFileInfoByAreacode(String sql, int first, int pageSize) {
        return new JnAuditJianGuanService().getShareFileInfoByAreacode(sql, first, pageSize);
    }

    @Override
    public int getShareFileInfoNumByAreacode(String sql) {
        return new JnAuditJianGuanService().getShareFileInfoNumByAreacode(sql);
    }

}
