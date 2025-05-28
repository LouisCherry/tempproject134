package com.epoint.auditresource.cert.bizcommon;

import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import org.xm.similarity.util.StringUtil;

import java.util.Date;

/**
 * 济宁个性化证照渲染类
 */
public class JnGenerateCertCommon {

    public String getFlowsn(int var1, int var2, String var3, Object... var4) {
        ICommonDao baseDao = CommonDao.getInstance();
        String flowSn = "";
        flowSn = baseDao.executeProcudureWithResult(var1, var2, var3, var4).toString();
        return flowSn;
    }

    /**
     * 取水许可审批（设区的市级权限）（首次申请）
     */
    public void qsxksp(CertInfoExtension dataBean, String applyername) {
        dataBean.set("jsdw", applyername);
        dataBean.set("fzrq", EpointDateUtil.convertDate2String(new Date(), "yyyy年MM月dd日"));
    }

    /**
     * 公路建设项目施工许可
     */
    public void gljsxmskxk(CertInfoExtension dataBean, String applyername) {
        dataBean.set("jsdw", applyername);
        dataBean.set("fzrq", EpointDateUtil.convertDate2String(new Date(), "yyyy年MM月dd日"));
    }

    /**
     * 设置X射线影像诊断建设项目放射性职业病危害预评价报告审核
     */
    public void szXsxyxzdypjbgsh(CertInfoExtension dataBean, Record record, CertInfo certinfo) {
        // 如：证书编号：汶审放预+年份+三位流水号
        String numberName = "设置X射线影像诊断建设项目放射性职业病危害预评价报告审核";
        String numberFlag = "汶审放预";
        Object[] args = new Object[7];
        args[0] = numberName;
        args[1] = numberFlag;
        args[2] = Integer.valueOf(EpointDateUtil.getYearOfDate(new Date()));
        args[3] = 4;
        args[4] = 0;
        args[5] = 0;
        //流水号长度
        args[6] = 3;
        String bh = getFlowsn(args.length + 1, java.sql.Types.VARCHAR, "Common_Getflowsn", args);
        dataBean.set("BH", bh);
        certinfo.setCertno(bh);
        if (record != null) {
            if (StringUtil.isNotBlank(record.getStr("xmmc"))) {
                dataBean.set("xmmc", record.getStr("xmmc"));
            }
            if (StringUtil.isNotBlank(record.getStr("xmdz"))) {
                dataBean.set("xmdz", record.getStr("xmdz"));
            }
            if (StringUtil.isNotBlank(record.getStr("xmxz"))) {
                dataBean.set("xmxq", record.getStr("xmxz"));
            }
            if (StringUtil.isNotBlank(record.getStr("fddbr"))) {
                dataBean.set("fddbr", record.getStr("fddbr"));
            }
            if (StringUtil.isNotBlank(record.getStr("xmfzr"))) {
                dataBean.set("xmfzr", record.getStr("xmfzr"));
            }
            if (StringUtil.isNotBlank(record.getStr("lxr"))) {
                dataBean.set("lxr", record.getStr("lxr"));
            }
            if (StringUtil.isNotBlank(record.getStr("lxdh"))) {
                dataBean.set("lxdh", record.getStr("lxdh"));
            }
            if (StringUtil.isNotBlank(record.getStr("ypjdw"))) {
                dataBean.set("ypjdw", record.getStr("ypjdw"));
            }
            if (StringUtil.isNotBlank(record.getStr("zybwhlb"))) {
                dataBean.set("zybwhlb", record.getStr("zybwhlb"));
            }
        }
    }

    /**
     * 设置X射线影像诊断的建设项目放射性职业病防护设施竣工验收
     */
    public void szXsxzybfhssjgys(CertInfoExtension dataBean, Record record, CertInfo certinfo) {
        // 如：证书编号：汶审放竣+年份+三位流水号
        String numberName = "汶上县射线影像诊断的建设项目放射性职业病防护设施竣工验收";
        String numberFlag = "汶审放竣";
        Object[] args = new Object[7];
        args[0] = numberName;
        args[1] = numberFlag;
        args[2] = Integer.valueOf(EpointDateUtil.getYearOfDate(new Date()));
        args[3] = 4;
        args[4] = 0;
        args[5] = 0;
        //流水号长度
        args[6] = 3;
        String bh = getFlowsn(args.length + 1, java.sql.Types.VARCHAR, "Common_Getflowsn", args);
        dataBean.set("bh", bh);
        certinfo.setCertno(bh);
        if (record != null) {
            if (StringUtil.isNotBlank(record.getStr("yljgmc"))) {
                dataBean.set("yljgmc", record.getStr("yljgmc"));
            }
            if (StringUtil.isNotBlank(record.getStr("xmxz"))) {
                dataBean.set("xmxz", record.getStr("xmxz"));
            }
            if (StringUtil.isNotBlank(record.getStr("jydz"))) {
                dataBean.set("xmdz", record.getStr("jydz"));
            }
            if (StringUtil.isNotBlank(record.getStr("fddbr"))) {
                dataBean.set("fddbr", record.getStr("fddbr"));
            }
            if (StringUtil.isNotBlank(record.getStr("lxr"))) {
                dataBean.set("lxr", record.getStr("lxr"));
            }
            if (StringUtil.isNotBlank(record.getStr("zyfzr"))) {
                dataBean.set("xmfzr", record.getStr("zyfzr"));
            }
            if (StringUtil.isNotBlank(record.getStr("lxdh"))) {
                dataBean.set("lxdh", record.getStr("lxdh"));
            }
            if (StringUtil.isNotBlank(record.getStr("zybwhlb"))) {
                dataBean.set("zybwhlb", record.getStr("zybwhlb"));
            }
            if (StringUtil.isNotBlank(record.getStr("baogbzdw"))) {
                dataBean.set("kzxgpjdw", record.getStr("baogbzdw"));
            }
        }

    }

    /**
     * 设置X射线影像诊断项目许可（新办）
     */
    public void szXsxzdxmxkxb(CertInfoExtension dataBean, Record record, String areacode, CertInfo certinfo) {
        //鲁汶卫放证字+年份+第370830三位流水号（不可有重复编号）注：鲁汶卫放证字[2024]第370830002号开始生成
        String numberName = "设置X射线影像诊断项目许可（新办）";
        String numberFlag = "鲁汶卫放证字[";
        Object[] args = new Object[8];
        args[0] = numberName;
        args[1] = numberFlag;
        args[2] = Integer.valueOf(EpointDateUtil.getYearOfDate(new Date()));
        args[3] = 4;
        args[4] = 0;
        args[5] = 0;
        //流水号长度
        args[6] = 3;
        args[7] = areacode;
        String bh = getFlowsn(args.length + 1, java.sql.Types.VARCHAR, "Common_Getflowsn_szXsxzdxmxkxb", args);
        dataBean.set("bh", bh);
        certinfo.setCertno(bh);
        if (record != null) {
            if (StringUtil.isNotBlank(record.getStr("yljgmc"))) {
                dataBean.set("yljgmc", record.getStr("yljgmc"));
            }
            if (StringUtil.isNotBlank(record.getStr("xmxz"))) {
                dataBean.set("xmxz", record.getStr("xmxz"));
            }
            if (StringUtil.isNotBlank(record.getStr("jydz"))) {
                dataBean.set("xmdz", record.getStr("jydz"));
            }
            if (StringUtil.isNotBlank(record.getStr("fddbr"))) {
                dataBean.set("fddbr", record.getStr("fddbr"));
            }
            if (StringUtil.isNotBlank(record.getStr("lxr"))) {
                dataBean.set("lxr", record.getStr("lxr"));
            }
            if (StringUtil.isNotBlank(record.getStr("zyfzr"))) {
                dataBean.set("xmfzr", record.getStr("zyfzr"));
            }
            if (StringUtil.isNotBlank(record.getStr("lxdh"))) {
                dataBean.set("lxdh", record.getStr("lxdh"));
            }
            if (StringUtil.isNotBlank(record.getStr("zybwhlb"))) {
                dataBean.set("zybwhlb", record.getStr("zybwhlb"));
            }
            if (StringUtil.isNotBlank(record.getStr("baogbzdw"))) {
                dataBean.set("kzxgpjdw", record.getStr("baogbzdw"));
            }
        }
    }

    /**
     * 人力资源服务许可证-新办
     */
    public void rlzyfwxkxb(CertInfoExtension dataBean, Record record){
        if (record != null) {
            //机构名称
            if (StringUtil.isNotBlank(record.getStr("jgmc"))) {
                dataBean.set("jgmc", record.getStr("jgmc"));
            }
            //统一社会信用代码
            if (StringUtil.isNotBlank(record.getStr("tyshxydm"))) {
                dataBean.set("tyshxydm", record.getStr("tyshxydm"));
            }
            //地址
            if (StringUtil.isNotBlank(record.getStr("zcdz"))) {
                dataBean.set("dz", record.getStr("zcdz"));
            }
            //法定代表人（负责人）
            if (StringUtil.isNotBlank(record.getStr("xingm"))) {
                dataBean.set("fddbr", record.getStr("xingm"));
            }
            //机构性质
            if (StringUtil.isNotBlank(record.getStr("jglx"))) {
                dataBean.set("jgxz", record.getStr("jglx"));
            }
            //服务范围
            if (StringUtil.isNotBlank(record.getStr("sqlx"))) {
                dataBean.set("fwfw", record.getStr("sqlx"));
            }
        }
    }

    /**
     * 人力资源服务许可证-变更
     */
    public void rlzyfwxkbg(CertInfoExtension dataBean, Record record){
        if (record != null) {
            //机构名称
            if (StringUtil.isNotBlank(record.getStr("jgmc"))) {
                dataBean.set("jgmc", record.getStr("jgmc"));
            }
            //统一社会信用代码
            if (StringUtil.isNotBlank(record.getStr("tyshxydm"))) {
                dataBean.set("tyshxydm", record.getStr("tyshxydm"));
            }
            //地址
            if (StringUtil.isNotBlank(record.getStr("zcdz"))) {
                dataBean.set("dz", record.getStr("zcdz"));
            }
            //法定代表人（负责人）
            if (StringUtil.isNotBlank(record.getStr("xingm"))) {
                dataBean.set("fddbr", record.getStr("xingm"));
            }
            //机构性质
            if (StringUtil.isNotBlank(record.getStr("jglx"))) {
                dataBean.set("jgxz", record.getStr("jglx"));
            }
            //服务范围
            if (StringUtil.isNotBlank(record.getStr("sqlx"))) {
                dataBean.set("fwfw", record.getStr("sqlx"));
            }
        }
    }

    /**
     * 劳务派遣经营许可证
     */
    public void lwpqjyxkz(CertInfoExtension dataBean, Record record){
        if (record != null) {
            //单位名称
            if (StringUtil.isNotBlank(record.getStr("wbk2"))) {
                dataBean.set("dwmc", record.getStr("wbk2"));
            }
            //住所
            if (StringUtil.isNotBlank(record.getStr("wbk5"))) {
                dataBean.set("zs", record.getStr("wbk5"));
            }
            //法定代表人
            if (StringUtil.isNotBlank(record.getStr("wbk13"))) {
                dataBean.set("fddbr", record.getStr("wbk13"));
            }
            //注册资本
            if (StringUtil.isNotBlank(record.getStr("wbk7"))) {
                dataBean.set("zczb", record.getStr("wbk7"));
            }
            //许可经营事项
            dataBean.set("xkjysx", "劳务派遣");
            //发证机关
            dataBean.set("zczb", "金乡县行政审批服务局");
        }
    }
}
