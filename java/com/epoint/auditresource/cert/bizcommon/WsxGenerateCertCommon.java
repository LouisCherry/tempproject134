package com.epoint.auditresource.cert.bizcommon;

import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.xmz.cxbus.api.ICxBusService;

import java.util.Date;

/**
 * 汶上县证照渲染
 */
public class WsxGenerateCertCommon {

    private ICxBusService iCxBusService = ContainerFactory.getContainInfo().getComponent(ICxBusService.class);
    private IAuditSpBusiness auditSpBusinessService = ContainerFactory.getContainInfo().getComponent(IAuditSpBusiness.class);
    private IAuditSpISubapp iAuditSpISubapp = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);

    public String getFlowsn(int var1, int var2, String var3, Object... var4) {
        ICommonDao baseDao = CommonDao.getInstance();
        String flowSn = "";
        flowSn = baseDao.executeProcudureWithResult(var1, var2, var3, var4).toString();
        return flowSn;
    }

    /**
     * 建设用地、临时建设用地规划许可
     */
    public void jsydghxk(CertInfoExtension dataBean, String subappguid) {
        //生成规则
        //地字第370830+2023（年份）+00001（年办件量）
        String numberName = "建设用地、临时建设用地规划许可（汶上县）";
        String numberFlag = "地字第370830";
        Object[] args = new Object[7];
        args[0] = numberName;
        args[1] = numberFlag;
        args[2] = Integer.valueOf(EpointDateUtil.getYearOfDate(new Date()));
        args[3] = 4;
        args[4] = Integer.valueOf(EpointDateUtil.getMonthOfDate(new Date()) + 1);
        args[5] = Integer.valueOf(EpointDateUtil.getDayOfDate(new Date()));
        //流水号长度
        args[6] = 5;
        WsxGenerateCertCommon wsxGenerateCertCommon = new WsxGenerateCertCommon();
        String bh = wsxGenerateCertCommon.getFlowsn(args.length + 1, java.sql.Types.VARCHAR, "Common_Getflowsn1", args);
        // 查询电子表单内容渲染 -- 建设用地、临时建设用地规划许可
        Record record = iCxBusService.getDzbdDetail("formtable20231008142821", subappguid);
        if (record != null) {
            dataBean.set("yddw", record.getStr("yddw"));
            dataBean.set("ydxmmc", record.getStr("xmmc"));
            dataBean.set("pzydjg", record.getStr("pzydjg"));
            dataBean.set("tdqdfs", record.getStr("tdhqfs"));
            dataBean.set("ft", record.getStr("ftjfjmc"));
            dataBean.set("jsgm", record.getStr("jsgm"));
            dataBean.set("pzydwh", record.getStr("pzydwh"));
            dataBean.set("ydwz", record.getStr("ydwz"));
            dataBean.set("ydmj", record.getStr("ydmj"));
            dataBean.set("ydxz", record.getStr("tdyt"));
        }
        dataBean.set("bh", bh);
        dataBean.set("rq", EpointDateUtil.convertDate2String(new Date(), "yyyy年MM月dd日"));
    }

    /**
     * 建筑工程施工许可证核发（汶上县）
     */
    public void jzgcsgxkz(CertInfoExtension dataBean, String subappguid) {
        //生成规则
        //370830（行政区划代码）+2023（年份）+01（月份）01（日）+01（二位数字的日办件量）+01（房屋建筑工程）
        String numberName = "建设工程规划许可证（汶上县）";
        String numberFlag = "370830";
        Object[] args = new Object[8];
        args[0] = numberName;
        args[1] = numberFlag;
        args[2] = Integer.valueOf(EpointDateUtil.getYearOfDate(new Date()));
        args[3] = 4;
        args[4] = Integer.valueOf(EpointDateUtil.getMonthOfDate(new Date()) + 1);
        args[5] = Integer.valueOf(EpointDateUtil.getDayOfDate(new Date()));
        args[6] = "";
        AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
        AuditSpBusiness auditBusiness = auditSpBusinessService.getAuditSpBusinessByRowguid(auditSpISubapp.getBusinessguid())
                .getResult();
        if (auditBusiness != null) {
            if (auditBusiness.getBusinessname().contains("市政")) {
                args[6] = "02";
            } else {
                args[6] = "01";
            }
        }

        //流水号长度
        args[7] = 2;
        String bh = getFlowsn(args.length + 1, java.sql.Types.VARCHAR, "Common_Getflowsn_sgxkz", args);
        dataBean.set("bh", bh);
        // 查询电子表单内容渲染 -- 建筑工程施工许可证核发
        Record record = iCxBusService.getDzbdDetail("formtable20230711184007", subappguid);
        if (record != null) {
            dataBean.set("jiansheguimo", record.getStr("jsgm"));
            dataBean.set("bdcdyh", record.getStr("bdcdyh"));
            dataBean.set("xiangmudidian", record.getStr("jsxmdz"));
            dataBean.set("xiangmumingchen", record.getStr("xmmc"));
            dataBean.set("jianshedanweimingchen", record.getStr("jsdw"));
            dataBean.set("hetongjiagewanyuan", record.getStr("htjg"));
            dataBean.set("shejidanwei", record.getStr("sjdw"));
            dataBean.set("kanchadanwei", record.getStr("kcdw"));
            dataBean.set("kanchadanweixiangmufuzeren", record.getStr("kcdwxmfzr"));
            dataBean.set("jianshedanweixiangmufuzeren", record.getStr("jsdwxmfzr"));
            dataBean.set("jianlidanwei", record.getStr("jldw"));
            dataBean.set("shigongzongchengbaoqiye", record.getStr("sgdw"));
            dataBean.set("zongjianligongchengshi", record.getStr("zjlgcs"));
            dataBean.set("shigongzongchengbaoqiyexiangmu", record.getStr("sgdwxmfzr"));
            dataBean.set("shejidanweixiangmufuzeren", record.getStr("sjdwxmfzr"));
            //合同工期  拼接
            String starttime = EpointDateUtil.convertDate2String(record.getDate("htgq"), "yyyy年MM月dd日");
            String endtime = EpointDateUtil.convertDate2String(record.getDate("htgqend"), "yyyy年MM月dd日");
            dataBean.set("htgq", starttime + "至" + endtime);
        }
        dataBean.set("bh", bh);
        dataBean.set("fzrq", EpointDateUtil.convertDate2String(new Date(), "yyyy年MM月dd日"));
    }

    /**
     * 建设工程规划许可证、乡村建设规划许可证
     */
    public void sjgcghxkz(CertInfoExtension dataBean, String subappguid) {
        //生成规则
        //370830+2023（年份）+00001（年办件量）
        String numberName = "建设工程规划许可证、乡村建设规划许可证（汶上县）";
        String numberFlag = "370830";
        Object[] args = new Object[7];
        args[0] = numberName;
        args[1] = numberFlag;
        args[2] = Integer.valueOf(EpointDateUtil.getYearOfDate(new Date()));
        args[3] = 4;
        args[4] = Integer.valueOf(EpointDateUtil.getMonthOfDate(new Date()) + 1);
        args[5] = Integer.valueOf(EpointDateUtil.getDayOfDate(new Date()));
        //流水号长度
        args[6] = 5;
        WsxGenerateCertCommon wsxGenerateCertCommon = new WsxGenerateCertCommon();
        String bh = wsxGenerateCertCommon.getFlowsn(args.length + 1, java.sql.Types.VARCHAR, "Common_Getflowsn1", args);
        // 查询电子表单内容渲染 -- 建设工程规划许可证、乡村建设规划许可证
        Record record = iCxBusService.getDzbdDetail("formtable20230711184045", subappguid);
        if (record != null) {
            dataBean.set("jsgm", record.getStr("jsgm"));
            dataBean.set("jswz", record.getStr("xmjswz"));
            dataBean.set("jsxmmc", record.getStr("jsxmmc"));
            dataBean.set("jsdw", record.getStr("jsdw"));
            dataBean.set("bdcdyh", record.getStr("bdcdyh"));
        }
        dataBean.set("bh", bh);
        dataBean.set("rq", EpointDateUtil.convertDate2String(new Date(), "yyyy年MM月dd日"));
    }

    /**
     * 人防工程施工图设计文件核准
     */
    public void rfgcsgt(CertInfoExtension dataBean, String subappguid) {
        //生成规则
        //370830（行政区划代码）+2023（年份）+01（月份）01（日）+01（二位数字的日办件量）
        String numberName = "人防工程施工图设计文件核准（汶上县）";
        String numberFlag = "370830";
        Object[] args = new Object[7];
        args[0] = numberName;
        args[1] = numberFlag;
        args[2] = Integer.valueOf(EpointDateUtil.getYearOfDate(new Date()));
        args[3] = 4;
        args[4] = Integer.valueOf(EpointDateUtil.getMonthOfDate(new Date()) + 1);
        args[5] = Integer.valueOf(EpointDateUtil.getDayOfDate(new Date()));
        //流水号长度
        args[6] = 2;
        WsxGenerateCertCommon wsxGenerateCertCommon = new WsxGenerateCertCommon();
        String bh = wsxGenerateCertCommon.getFlowsn(args.length + 1, java.sql.Types.VARCHAR, "Common_Getflowsn", args);
        // 查询电子表单内容渲染 -- 人防工程施工图设计文件核准
        Record record = iCxBusService.getDzbdDetail("formtable20230711183914", subappguid);
        if (record != null) {
            dataBean.set("gcmc", record.getStr("xmmc"));
            dataBean.set("jsdw", record.getStr("jsdw"));
            dataBean.set("gcdz", record.getStr("xmdz"));
            dataBean.set("sjdw", record.getStr("rfgcsjdw"));
            dataBean.set("scdw", record.getStr("rfsgtscdw"));
            dataBean.set("zjzmj", record.getStr("zjzmj"));
            dataBean.set("dsjzmj", record.getStr("dsjzmj"));
            dataBean.set("dxjzmj", record.getStr("dxjzmj"));
            dataBean.set("rfgcjzmj", record.getStr("rfgcjzmj"));
            dataBean.set("fhlb", record.getStr("fhlb"));
            dataBean.set("jjfkd", record.getStr("jjspyjbh"));
            dataBean.set("psyta", record.getStr("psyt1"));
            dataBean.set("psytb", record.getStr("psyt2"));
            dataBean.set("psytc", record.getStr("psyt3"));
            dataBean.set("psytd", record.getStr("psyt4"));
            dataBean.set("fhdya", record.getStr("fhdy1"));
            dataBean.set("fhdyb", record.getStr("fhdy2"));
            dataBean.set("fhdyc", record.getStr("fhdy3"));
            dataBean.set("fhdyd", record.getStr("fhdy4"));
            dataBean.set("mja", record.getStr("jzmj1"));
            dataBean.set("mjb", record.getStr("jzmj2"));
            dataBean.set("mjc", record.getStr("jzmj3"));
            dataBean.set("mjd", record.getStr("jzmj4"));
            dataBean.set("kldja", record.getStr("kljb1"));
            dataBean.set("kldjb", record.getStr("kljb2"));
            dataBean.set("kldjc", record.getStr("kljb3"));
            dataBean.set("kldjd", record.getStr("kljb4"));
            dataBean.set("fhjba", record.getStr("fhjb1"));
            dataBean.set("fhjbb", record.getStr("fhjb2"));
            dataBean.set("fhjbc", record.getStr("fhjb3"));
            dataBean.set("fhjbd", record.getStr("fhjb4"));
            dataBean.set("zsa", record.getStr("zsgn1"));
            dataBean.set("zsb", record.getStr("zsgn2"));
            dataBean.set("zsc", record.getStr("zsgn3"));
            dataBean.set("zsd", record.getStr("zsgn4"));
        }
        dataBean.set("bh", bh);
        dataBean.set("fzrq", EpointDateUtil.convertDate2String(new Date(), "yyyy年MM月dd日"));
    }


    /**
     * 防空地下室易地建设审批
     */
    public void fkdxs(CertInfoExtension dataBean, String subappguid) {
        //生成规则
        //370830（行政区划代码）+2023（年份）+01（月份）01（日）+01（二位数字的日办件量）
        String numberName = "防空地下室易地建设审批（汶上县）";
        String numberFlag = "370830";
        Object[] args = new Object[7];
        args[0] = numberName;
        args[1] = numberFlag;
        args[2] = Integer.valueOf(EpointDateUtil.getYearOfDate(new Date()));
        args[3] = 4;
        args[4] = Integer.valueOf(EpointDateUtil.getMonthOfDate(new Date()) + 1);
        args[5] = Integer.valueOf(EpointDateUtil.getDayOfDate(new Date()));
        //流水号长度
        args[6] = 2;
        WsxGenerateCertCommon wsxGenerateCertCommon = new WsxGenerateCertCommon();
        String bh = wsxGenerateCertCommon.getFlowsn(args.length + 1, java.sql.Types.VARCHAR, "Common_Getflowsn", args);
        // 查询电子表单内容渲染 -- 防空地下室易地建设审批
        Record record = iCxBusService.getDzbdDetail("formtable20231008143631", subappguid);
        if (record != null) {
            dataBean.set("jsdw", record.getStr("jsdw"));
            dataBean.set("jsxm", record.getStr("xmmc"));
            dataBean.set("jsdd", record.getStr("jsdz"));
            dataBean.set("dsjzmj", record.getStr("dsjzmj"));
            dataBean.set("dxjzmj", record.getStr("dxjzmj"));

            dataBean.set("yjfkdxs", record.getStr("yjjfkdxs"));
            dataBean.set("fhlb", record.getStr("fhlb"));
            dataBean.set("kldj", record.getStr("kldj"));
            dataBean.set("zsyt", record.getStr("zsyt"));
            dataBean.set("ydjsfjnbz", record.getStr("ydjsfjnbz"));
            dataBean.set("ydjsfjnse", record.getStr("ydjsfjnse"));
            dataBean.set("bz", record.getStr("beiz"));
            dataBean.set("jfrq", EpointDateUtil.convertDate2String(record.getDate("jfrq"), "yyyy年MM月dd日"));
        }
        dataBean.set("bh", bh);
        dataBean.set("rq", EpointDateUtil.convertDate2String(new Date(), "yyyy年MM月dd日"));
    }

}
