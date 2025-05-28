package com.epoint.xmz.thirdreporteddata.sqgl.job.api;

import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;

import java.io.Serializable;
import java.util.List;

public interface ISpglXmjbxxbV3Job extends Serializable {

    /**
     * 查询满足条件的项目列表
     */
    public List<AuditRsItemBaseinfo> getAuditRsItemBaseInfoList();

    /**
     * 查询前置库是否有该项目
     * @param gcdm
     * @return
     */
    public boolean IsXmjbxxbV3(String gcdm);



}
