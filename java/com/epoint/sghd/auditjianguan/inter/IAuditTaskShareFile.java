package com.epoint.sghd.auditjianguan.inter;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.sghd.auditjianguan.domain.AuditTaskShareFile;

import java.util.List;
import java.util.Map;



/**
 * 2018年9月19日
 *
 * @author swy
 */
public interface IAuditTaskShareFile {
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
    AuditCommonResult<PageData<AuditTaskShareFile>> getShareFilePageData(Map<String, String> arg0, Integer arg1,
                                                                         Integer arg2, String arg3, String arg4);

    /**
     * 获取政策文件by guid
     *
     * @param rowguid
     * @return
     */
    AuditCommonResult<AuditTaskShareFile> getShareFileByGuid(String rowguid);

    /**
     * 根据辖区获取文件
     *
     * @param sql
     * @param first
     * @param pageSize
     * @return
     */
    public List<AuditTaskShareFile> getShareFileInfoByAreacode(String sql, int first, int pageSize);

    /**
     * 根据辖区获取文件数目
     *
     * @param sql
     * @return
     */
    public int getShareFileInfoNumByAreacode(String sql);

}
