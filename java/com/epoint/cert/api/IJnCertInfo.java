package com.epoint.cert.api;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.organ.user.entity.FrameUser;

import java.io.Serializable;
import java.util.List;

/**
 * 个性化获取证照清单信息接口
 *
 * @author hlx
 * @description
 * @date 2020年7月21日 下午2:52:35
 */
public interface IJnCertInfo extends Serializable {

    public List<CertInfo> getUnPublishCertInfo();

    Record getCertInfoByTyshxydm(String tyshxydm);

    AuditProject getProjectByCertnum(String certnum);
    /**
     * 推送安许
     * @param ifgjfm 是否归集赋码
     */
    CertInfo pushfuma( String[] fieldNames, Object[] values,
                       CertInfo certInfo, boolean ifgjfm);

    /**
     * 推送证照
     */
    CertInfo pushanxu( String[] fieldNames, Object[] values,
                          CertInfo certInfo, boolean ifgjfm);

    /**
     * 推送竣工验收
     * @param ifgjfm 是否归集赋码
     */
    CertInfo pushjgys( String[] fieldNames, Object[] values,
                       CertInfo certInfo, boolean ifgjfm);

    /**
     * 根据ouguid
     *
     * @param roleguid
     * @return
     */
    List<FrameUser> getuserbyouguid(String ouguid, String roleguid);

}
