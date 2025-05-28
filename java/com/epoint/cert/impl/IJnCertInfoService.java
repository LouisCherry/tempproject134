package com.epoint.cert.impl;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.frame.service.organ.user.entity.FrameUser;

import java.util.List;

/**
 * 一业一证基本信息Service
 *
 * @author shibin
 * @description
 * @date 2020年5月19日 下午2:52:35
 */
public class IJnCertInfoService {

    private static String qzkURL = ConfigUtil.getConfigValue("datasyncjdbc", "zzkqzkurl");
    private static String qzkNAME = ConfigUtil.getConfigValue("datasyncjdbc", "zzkqzkname");
    private static String qzkPASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "zzkqzkpassword");

    private DataSourceConfig dataSourceConfigQZK = new DataSourceConfig(qzkURL, qzkNAME, qzkPASSWORD);

    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    protected ICommonDao commonDaoQZK;

    public IJnCertInfoService() {
        baseDao = CommonDao.getInstance();
        commonDaoQZK = CommonDao.getInstance(dataSourceConfigQZK);
    }


    /**
     * 查找一个list
     */
    public List<CertInfo> getUnPublishCertInfo() {
        String sql = "SELECT c.* FROM cert_info c join cert_catalog b on c.CERTAREAGUID = b.rowguid  WHERE c.MATERIALTYPE = 1 AND (c.ISPUBLISH IS NULL OR c.ISPUBLISH = 0)  AND c.ISCREATECERT=1  ";
        sql += "and c.CERTNAME in ('行业综合许可证','防控地下室易地建设审批意见书','建设用地规划许可证','建设工程竣工规划核实合格证','消毒产品生产企业卫生许可证','施工许可证','建设工程规划许可证','建设项目选址意见书','临时建设工程规划许可证','临时建设用地规划许可证','人防工程防护设计文件审核意见书','医疗广告审查（中医医疗机构除外）','医疗机构放射性职业病危害建设项目竣工验收','医疗机构放射性职业病危害建设项目预评价报告审核') LIMIT 200 ";
        return baseDao.findList(sql, CertInfo.class);
    }

    public Record getCertInfoByTyshxydm(String tyshxydm) {
        String sql = "select * from EX_JZSGQYAQSCXKZX_1 where TONGYISHEHUIXINYONGDAIMA = ? ";
        return commonDaoQZK.find(sql, Record.class, tyshxydm);
    }

    public AuditProject getProjectByCertnum(String certnum) {
        String sql = "select rowguid,certrowguid from audit_project where certnum = ? and status = 90 and banjieresult = '40' and task_id in " +
                "('11370800MB285591843370117002011','11370800MB28559184300011711200001','11370800MB285591843370117002016','11370800MB285591843370117002013'," +
                "'11370800MB28559184300011711200002','11370800MB285591843370117002014','11370800MB28559184300011711200003','11370800MB285591843370117002015'," +
                "'11370800MB285591843370117002024','11370800MB285591843370117002027','11370800MB285591843370117002026','11370800MB285591843370117002025'," +
                "'11370800MB28559184300011711200004') order by BANJIEDATE desc limit 1";
        return baseDao.find(sql, AuditProject.class, certnum);
    }


    /**
     * 根据ouguid
     *
     * @param roleguid
     * @return
     */
    public List<FrameUser> getuserbyouguid(String ouguid, String roleguid) {
        String sql = "select b.* from frame_userrolerelation as  a left join frame_user as b on a.USERGUID=b.USERGUID where ROLEGUID=? and b.OUGUID=?";
        return baseDao.findList(sql, FrameUser.class,roleguid,ouguid);
    }


    /**
     * 查找一个含有二维码的list
     */
    public List<CertInfo> getCertWithErwm(String certownerno) {
        String sql = "SELECT * FROM cert_info WHERE certownerno = ? AND ifnull(ERWEIMA,'')!='' ";
        return baseDao.findList(sql, CertInfo.class,certownerno);
    }
}
