package com.epoint.sghd.auditjianguan.inter;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.sghd.auditjianguan.domain.AuditProjectPermissionChange;

import java.util.Map;


/**
 * 2018年9月19日
 *
 * @author swy
 */
public interface IAuditTaskBianGeng {
    /**
     * 获取政策文件
     *
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @param arg4
     * @return
     */
    AuditCommonResult<PageData<AuditProjectPermissionChange>> getShareFilePageData(Map<String, String> arg0, Integer arg1,
                                                                                   Integer arg2, String arg3, String arg4);

    /**
     * 获取政策文件by guid
     *
     * @param rowguid
     * @return
     */
    AuditCommonResult<AuditProjectPermissionChange> getShareFileByGuid(String rowguid);
}
