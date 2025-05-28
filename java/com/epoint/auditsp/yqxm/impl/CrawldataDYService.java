package com.epoint.auditsp.yqxm.impl;

import com.epoint.auditsp.yqxm.api.entity.*;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.spgl.domain.SpglDfxmsplcjdsxxxb;
import com.epoint.basic.spgl.domain.SpglDfxmsplcjdxxb;
import com.epoint.basic.spgl.domain.SpglDfxmsplcxxb;
import com.epoint.basic.spgl.service.JnSpglXmjbxxbService;
import com.epoint.basic.spgl.service.SpglDfxmsplcjdsxxxbService;
import com.epoint.basic.spgl.service.SpglDfxmsplcjdxxbService;
import com.epoint.basic.spgl.service.SpglDfxmsplcxxbService;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 数据抓取服务接口
 *
 * @author anzyuan
 * @version [版本号, 2020-07-22 14:36:09]
 */
public class CrawldataDYService {

    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    /**
     * DAO
     */
    public CrawldataDYService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 数据抓取服务
     * 用于对数据的抓取及统计
     *
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void CrawldataStart(String citycode) {
        IAuditRsItemBaseinfo itemBaseinfo = ContainerFactory.getContainInfo().getComponent(IAuditRsItemBaseinfo.class);
        Logger logger = Logger.getLogger(CrawldataDYService.class);
        String rowguid = UUID.randomUUID().toString();
        List<String> errorList = new ArrayList<String>();

        baseDao = CommonDao.getInstance();
        baseDao.executeProcudure("ST_PRO_INTO_DATABASE", "spgl_xmjbxxb");
        baseDao.close();

        baseDao = CommonDao.getInstance();
        baseDao.executeProcudure("ST_PRO_INTO_DATABASE", "spgl_xmqqyjxxb");
        baseDao.close();

        baseDao = CommonDao.getInstance();
        baseDao.executeProcudure("ST_PRO_INTO_DATABASE", "spgl_xmspsxblxxb");
        baseDao.close();

        baseDao = CommonDao.getInstance();
        baseDao.executeProcudure("ST_PRO_INTO_DATABASE", "spgl_xmspsxblxxxxb");
        baseDao.close();

        baseDao = CommonDao.getInstance();
        baseDao.executeProcudure("ST_PRO_INTO_DATABASE", "spgl_xmspsxzqyjxxb");
        baseDao.close();

        baseDao = CommonDao.getInstance();
        baseDao.executeProcudure("ST_PRO_INTO_DATABASE", "spgl_xmspsxbltbcxxxb");
        baseDao.close();

        baseDao = CommonDao.getInstance();
        baseDao.executeProcudure("ST_PRO_INTO_DATABASE", "spgl_xmjgxxb");
        baseDao.close();

        baseDao = CommonDao.getInstance();
        baseDao.executeProcudure("ST_PRO_INTO_XMJBXXB");
        baseDao.close();

        baseDao = CommonDao.getInstance();
        try {
            // 1、更新所有带抓取数据
            // 2、获取更新工程代码集合
            List<String> tablelist = new ArrayList<String>();
            tablelist.add("SPGL_XMJBXXB");
            tablelist.add("SPGL_XMSPSXBLXXB");
            tablelist.add("SPGL_XMSPSXBLXXXXB");

            List<String> gcdmlist = new ArrayList<String>();

            for (String tablename : tablelist) {
                List<Record> listgcdm = baseDao.findList(
                        "select distinct XZQHDM,GCDM from " + tablename + " where  SJSJZQZT=1 and SJYXBS=1 and xzqhdm='" + citycode + "'", Record.class);
                for (Record li : listgcdm) {
                    String qhgcdm = li.get("XZQHDM").toString() + "&" + li.get("GCDM").toString();
                    if (!gcdmlist.contains(qhgcdm)) {
                        gcdmlist.add(qhgcdm);
                    }
                }

            }

            // 需要更新的项目代码集合
            List<String> xmdmlist = new ArrayList<String>();

            // 新增项目list结合，当第一次跑到该项目时，将该项目的阶段表信息删除，避免形成垃圾数据。
            List<String> xmdmlist_jd = new ArrayList<String>();
            // 3、遍历工程代码信息
            //
            for (String qhgcdm : gcdmlist) {
                try {
                    if (qhgcdm.split("&").length < 2) {
                        continue;
                    }
                    String sXZQHDM = qhgcdm.split("&")[0];
                    String sGCDM = qhgcdm.split("&")[1];

                    // 获取工程基本信息
                    StSpglGcjbxxb Gcjbxxb = new StSpglGcjbxxbService().find("select * from ST_SPGL_GCJBXXB where XZQHDM"
                            + "='" + sXZQHDM + "' and GCDM='" + sGCDM + "'", sXZQHDM);

                    if (!xmdmlist.contains(Gcjbxxb.getXmdm() + "&" + sXZQHDM)) {
                        xmdmlist.add(Gcjbxxb.getXmdm() + "&" + sXZQHDM);
                    }

                    // 遍历所有的事项办理信息
                    List<Record> sxlist = baseDao.findList(
                            "select * from ST_SPGL_XMSPSXBLXXB  where XZQHDM=? and GCDM=? AND  SJYXBS='1' ORDER BY LSH ",
                            Record.class, sXZQHDM, sGCDM);

                    String sSPSXSLBM = "";
                    for (Record sxrecord : sxlist) {

                        try {

                            String sxRowguid = sxrecord.get("RowGuid").toString();
                            String sSPLCBM = sxrecord.get("SPLCBM");
                            Double sSPLCBBH = sxrecord.get("SPLCBBH") == null ? null
                                    : Double.valueOf(sxrecord.get("SPLCBBH").toString());
                            String sSPSXBM = sxrecord.get("SPSXBM");
                            Double sSPSXBBH = sxrecord.get("SPSXBBH") == null ? null
                                    : Double.valueOf(sxrecord.get("SPSXBBH").toString());
                            ;
                            sSPSXSLBM = sxrecord.get("SPSXSLBM");

                            // 审批流程类型
                            Integer sSPLCLX = sxrecord.get("SPLCLX");
                            // 省级审批流程类型
                            Integer sSJSPLCLX = sxrecord.get("SJSPLCLX");
                            // 审批事项名称
                            String sSPSXMC = sxrecord.get("SPSXMC");
                            // 对应标准审批事项编码
                            String sDYBZSPSXBM = sxrecord.get("DYBZSPSXBM");
                            // 对应标准审批阶段序号
                            String sDYBZSPJDXH = sxrecord.get("DYBZSPJDXH");
                            // 审批阶段编码
                            String sSPJDBM = sxrecord.get("SPJDBM");
                            // 工程阶段表唯一表示
                            String sJSROWGUID = sxrecord.get("JSROWGUID");

                            // 事项办理时限
                            Integer sSXBLSX = sxrecord.get("SXBLSX");

                            // 项目代码
                            String sXMDM = sxrecord.get("XMDM");

                            if (sxrecord.get("XMDM") == null) {
                                sXMDM = Gcjbxxb.getXmdm();
                            }

                            // 若是项目上第一次跑，则删除该项目的阶段信息
                            if (!xmdmlist_jd.contains(sXMDM)) {
                                baseDao.execute("delete from ST_SPGL_GCJDXXB where  XZQHDM=? and XMDM=?", sXZQHDM,
                                        sXMDM);
                                xmdmlist_jd.add(sXMDM);
                            }

                            // 项目名称
                            String sXMMC = sxrecord.get("XMMC");

                            if (sxrecord.get("XMMC") == null) {
                                sXMMC = Gcjbxxb.getXmmc();
                            }

                            String wherecase = "";
                            SpglDfxmsplcxxb Dfxmsplcxxb = null;
                            try {
                                Dfxmsplcxxb = getST_SPGL_DFXMSPLCJDSXXXB(sXZQHDM, sSPLCBM, sSPLCBBH);

                            } catch (Exception ec) {
                                ec.printStackTrace();
                            }

                            SpglDfxmsplcjdsxxxb Dfxmsplcjdsxxxb = null;

                            try {
                                Dfxmsplcjdsxxxb = getST_SPGL_DFXMSPLCJDSXXXB(sXZQHDM, sSPLCBM, sSPLCBBH, sSPSXBM,
                                        sSPSXBBH);

                                if (Dfxmsplcjdsxxxb == null) {
                                }
                            } catch (Exception ec) {
                                ec.printStackTrace();
                            }

                            SpglDfxmsplcjdxxb Dfxmsplcjdxxb = null;
                            try {
                                Dfxmsplcjdxxb = getST_SPGL_DFXMSPLCJDXXB(sXZQHDM, sSPLCBM, sSPLCBBH,
                                        Dfxmsplcjdsxxxb.getSpjdxh());

                                if (Dfxmsplcjdxxb == null) {
                                }
                            } catch (Exception ec) {
                                ec.printStackTrace();

                            }

                            sSPLCLX = Dfxmsplcxxb.getSplclx();

                            if (sSPLCLX == 3 || sSPLCLX == 4) {
                                sSJSPLCLX = 2;
                            } else if (sSPLCLX == 1 || sSPLCLX == 2) {
                                sSJSPLCLX = 1;
                            } else if (sSPLCLX == 6) {
                                sSJSPLCLX = 3;
                            } else {
                                sSJSPLCLX = 4;
                            }

                            sSPSXMC = Dfxmsplcjdsxxxb.getSpsxmc();
                            sDYBZSPSXBM = Dfxmsplcjdsxxxb.getDybzspsxbm();
                            sDYBZSPJDXH = Dfxmsplcjdxxb.getDybzspjdxh();
                            sSPJDBM = Dfxmsplcjdxxb.getSpjdbm();

                            wherecase += " , SPLCLX ='" + sSPLCLX + "' , SJSPLCLX ='" + sSJSPLCLX + "' , SPSXMC='"
                                    + sSPSXMC + "' , DYBZSPSXBM='" + sDYBZSPSXBM + "' , DYBZSPJDXH='" + sDYBZSPJDXH
                                    + "'" + " , SPJDBM='" + sSPJDBM + "' ";

                            // 当前办理状态
                            Integer sBLZT = null;
                            // 最新办理意见
                            String sBLYJ = null;
                            // 最近办理时间
                            Date sBLSJ = null;
                            // 办件是否完全办结
                            Integer sBJSFWQBJ = null;
                            // 接件时间
                            Date sSJSJ = null;
                            // 受理时间
                            Date sSLSJ = null;
                            // 办结时间
                            Date sBJSJ = null;
                            // 承诺办结时间
                            Date sCNBJSJ = null;
                            // 办件审批用时
                            Integer sBJSPYS = null;
                            // 办件跨度用时
                            Integer sBJKDYS = null;
                            // 补正用时
                            Integer sBZYS = null;
                            // 特别程序用时
                            Integer sTBCXYS = null;
                            // 办件是否超期
                            Integer sBJSFCQ = null;

                            // 最新的办理状态
                            List<Record> sxxxlist = baseDao.findList(
                                    "select * from ST_SPGL_XMSPSXBLXXXXB  where XZQHDM=? and GCDM=? and SPSXSLBM=? AND  SJYXBS='1'  ORDER BY BLSJ desc LIMIT 1",
                                    Record.class, sXZQHDM, sGCDM, sSPSXSLBM);
                            if (sxxxlist != null && !sxxxlist.isEmpty()) {
                                // 判断当前的数据是否跟上一次取值一样，若一样，则跳过当前循环
                                if (sBLZT == sxxxlist.get(0).get("BLZT") && sBLSJ == sxxxlist.get(0).get("BLSJ")) {
                                    continue;
                                }
                                // 当前办理状态
                                sBLZT = sxxxlist.get(0).get("BLZT");
                                // 最新办理意见
                                sBLYJ = sxxxlist.get(0).get("BLYJ");
                                // 最近办理时间
                                sBLSJ = sxxxlist.get(0).get("BLSJ");

                            }

                            if (sBLZT != null) {
                                wherecase += " , BLZT ='" + sBLZT + "' ";
                            } else {
                                wherecase += " , BLZT =" + sBLZT + " ";
                            }
                            if (sBLYJ != null) {
                                wherecase += " , BLYJ ='" + sBLYJ + "' ";
                            } else {
                                wherecase += " , BLYJ =" + sBLYJ + " ";
                            }

                            if (sBLSJ != null) {
                                wherecase += " , BLSJ ='" + sBLSJ + "' ";
                            } else {
                                wherecase += " , BLYJ =" + sBLSJ + " ";
                            }

                            // 判断接件
                            if (sSJSJ == null && sBLZT != null) {
                                // 受理时间to接件时间（SJSJ）
                                if (1 == sBLZT) {
                                    sSJSJ = sxxxlist.get(0).get("BLSJ");
                                } else {
                                    // 接件时间没有才用受理时间
                                    List<Record> jjSllist = baseDao.findList(
                                            "select * from ST_SPGL_XMSPSXBLXXXXB  where XZQHDM=? and GCDM=? and SPSXSLBM=? AND  SJYXBS='1' and BLZT=1 ORDER BY LSH desc  LIMIT 1",
                                            Record.class, sXZQHDM, sGCDM, sSPSXSLBM);
                                    if (jjSllist != null & !jjSllist.isEmpty()) {
                                        sSJSJ = jjSllist.get(0).get("BLSJ");
                                    }
                                    if (sSJSJ == null) {
                                        List<Record> sllist = baseDao.findList(
                                                "select * from ST_SPGL_XMSPSXBLXXXXB  where XZQHDM=? and GCDM=? and SPSXSLBM=? AND  SJYXBS='1' and BLZT=3 ORDER BY LSH desc  LIMIT 1",
                                                Record.class, sXZQHDM, sGCDM, sSPSXSLBM);
                                        if (sllist != null & !sllist.isEmpty()) {
                                            sSJSJ = sllist.get(0).get("BLSJ");
                                        }
                                    }
                                }
                            }

                            // 判断受理
                            if (sSLSJ == null && sBLZT != null) {
                                // 受理时间to接件时间（SJSJ）
                                if (3 == sBLZT) {
                                    sSLSJ = sxxxlist.get(0).get("BLSJ");
                                } else {
                                    // 接件时间没有才用受理时间
                                    List<Record> jjSllist = baseDao.findList(
                                            "select * from ST_SPGL_XMSPSXBLXXXXB  where XZQHDM=? and GCDM=? and SPSXSLBM=? AND  SJYXBS='1' and BLZT=3 ORDER BY LSH desc  LIMIT 1",
                                            Record.class, sXZQHDM, sGCDM, sSPSXSLBM);
                                    if (jjSllist != null && !jjSllist.isEmpty()) {
                                        sSLSJ = jjSllist.get(0).get("BLSJ");
                                    }
                                }
                            }

                            // 是否办结
                            // 最新需求：已撤件办件属于已办结办件，但是系统中没有算作办结一直在计时，导致超期未办结（滁州）
                            if (sBJSFWQBJ == null || sBJSFWQBJ == 0) {
                                if (sBLZT != null && (sBLZT == 11 || sBLZT == 12 || sBLZT == 13 || sBLZT == 2
                                        || sBLZT == 4 || sBLZT == 5 || sBLZT == 14 || sBLZT == 15)) {
                                    sBJSFWQBJ = 1;
                                    sBJSJ = sxxxlist.get(0).get("BLSJ");
                                } else {
                                    List<Record> sllist = baseDao.findList(
                                            "select * from ST_SPGL_XMSPSXBLXXXXB  where XZQHDM=? and GCDM=? and SPSXSLBM=? AND  SJYXBS='1' and (BLZT=11 or BLZT=12 or BLZT=13  OR BLZT=4 OR BLZT=5 OR BLZT=2 ) ORDER BY LSH desc  LIMIT 1",
                                            Record.class, sXZQHDM, sGCDM, sSPSXSLBM);
                                    if (sllist != null && !sllist.isEmpty()) {
                                        sBJSFWQBJ = 1;
                                        sBJSJ = sllist.get(0).get("BLSJ");
                                    } else {
                                        sBJSFWQBJ = 0;
                                    }
                                }

                            }

                            // 根据受理时间 计算出数据
                            if (sSLSJ != null) {
                                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                sCNBJSJ = sDateFormat.parse(new JnSpglXmjbxxbService().getWorkDay(sSLSJ, sSXBLSX));

                                if (sCNBJSJ == null) {

                                } else {
                                    wherecase += " , CNBJSJ ='" + new JnSpglXmjbxxbService().getWorkDay(sSLSJ, sSXBLSX)
                                            + "'  ";
                                }
                                Date endtime = new Date();

                                if (sBJSJ != null) {
                                    endtime = sBJSJ;
                                }

                                // 不受理 、 不予受理
                                if (sBLZT != null && (sBLZT == 4 || sBLZT == 5 || sBLZT == 2 || sBLZT == 13 || sBLZT == 14 || sBLZT == 15)) {
                                    sBJSPYS = null;
                                    sBJKDYS = null;
                                    sBJSFCQ = 0;
                                    sBJSJ = sBLSJ;
                                } else {
                                    // 添加条件，补正时间、特别程序时间必须在受理时间之后
                                    // 若有补正 或者特别程序，需减去相关时间
                                    // 补正开始列表
                                    List<StSpglXmspsxblxxxxb> bz_start_list = baseDao.findList(
                                            "select * from ST_SPGL_XMSPSXBLXXXXB  where XZQHDM=? and GCDM=? and SPSXSLBM=? "
                                                    + "AND SJYXBS=1 and (BLZT=6) and blsj>=? ORDER BY blsj",
                                            StSpglXmspsxblxxxxb.class, sXZQHDM, sGCDM, sSPSXSLBM, sSLSJ);
                                    // 补正结束列表
                                    List<StSpglXmspsxblxxxxb> bz_end_list = baseDao.findList(
                                            "select * from ST_SPGL_XMSPSXBLXXXXB  where XZQHDM=? and GCDM=? and SPSXSLBM=? "
                                                    + "AND SJYXBS=1 and (BLZT=7) and blsj>=? ORDER BY blsj",
                                            StSpglXmspsxblxxxxb.class, sXZQHDM, sGCDM, sSPSXSLBM, sSLSJ);
                                    // 特别程序开始列表
                                    List<StSpglXmspsxblxxxxb> tbcx_start_list = baseDao.findList(
                                            "select * from ST_SPGL_XMSPSXBLXXXXB  where XZQHDM=? and GCDM=? and SPSXSLBM=? "
                                                    + "AND SJYXBS=1 and (BLZT=9) and blsj>=? ORDER BY blsj",
                                            StSpglXmspsxblxxxxb.class, sXZQHDM, sGCDM, sSPSXSLBM, sSLSJ);
                                    // 特别程序结束列表
                                    List<StSpglXmspsxblxxxxb> tbcx_end_list = baseDao.findList(
                                            "select * from ST_SPGL_XMSPSXBLXXXXB  where XZQHDM=? and GCDM=? and SPSXSLBM=? "
                                                    + "AND SJYXBS=1 and (BLZT=10) and blsj>=? ORDER BY blsj",
                                            StSpglXmspsxblxxxxb.class, sXZQHDM, sGCDM, sSPSXSLBM, sSLSJ);

                                    if (CollectionUtils.isEmpty(bz_start_list)
                                            && CollectionUtils.isEmpty(tbcx_start_list)) {
                                        // 审批用时
                                        sBJSPYS = new JnSpglXmjbxxbService().countWorkingDay(sDateFormat.format(sSLSJ),
                                                sDateFormat.format(endtime));
                                    } else {
                                        // 补正 yes 特别程序no
                                        if (!CollectionUtils.isEmpty(bz_start_list)
                                                && CollectionUtils.isEmpty(tbcx_start_list)) {
                                            sBJSPYS = getAuditTime(sSLSJ, sDateFormat, sBJSJ,
                                                    bz_start_list, bz_end_list);
                                            sBZYS = getTBCXTime(sDateFormat, bz_start_list, bz_end_list);
                                        }
                                        // 补正 no 特别程序yes
                                        else if (CollectionUtils.isEmpty(bz_start_list)
                                                && !CollectionUtils.isEmpty(tbcx_start_list)) {
                                            sBJSPYS = getAuditTime(sSLSJ, sDateFormat, sBJSJ,
                                                    tbcx_start_list, tbcx_end_list);
                                            sTBCXYS = getTBCXTime(sDateFormat, tbcx_start_list, tbcx_end_list);
                                        }
                                        // 补正 yes 特别程序yes
                                        else {
                                            // 特别程序、补正开始都有值
                                            // merge 特别程序、补正同等对待
                                            sTBCXYS = getTBCXTime(sDateFormat, tbcx_start_list, tbcx_end_list);
                                            sBZYS = getTBCXTime(sDateFormat, bz_start_list, bz_end_list);
                                            if (sBJSJ == null) {// 未办结
                                                sBJSPYS = new JnSpglXmjbxxbService().countWorkingDay(sDateFormat.format(sSLSJ),
                                                        sDateFormat.format(new Date()));
                                            } else {
                                                sBJSPYS = new JnSpglXmjbxxbService().countWorkingDay(sDateFormat.format(sSLSJ), sDateFormat.format(sBJSJ));
                                            }
                                            sBJSPYS = sBJSPYS - sBZYS - sTBCXYS;

                                        }
                                    }
                                    // 跨度用时
                                    sBJKDYS = new JnSpglXmjbxxbService().daysBetween(sSLSJ, endtime);
                                }

                                if (sBJSPYS != null && sSXBLSX != null && sBJSPYS > sSXBLSX) {
                                    sBJSFCQ = 1;
                                } else {
                                    sBJSFCQ = 0;
                                }

                                if (sSJSJ != null) {
                                    wherecase += " , SJSJ ='" + sSJSJ + "'  ";
                                } else {
                                    wherecase += " , SJSJ =" + sSJSJ + "  ";
                                }

                                if (sSLSJ != null) {
                                    wherecase += " , SLSJ ='" + sSLSJ + "'  ";
                                } else {
                                    wherecase += " , SLSJ =" + sSLSJ + "  ";
                                }

                                if (sBJSJ != null) {
                                    wherecase += " , BJSJ ='" + sBJSJ + "'  ";
                                } else {
                                    wherecase += " , BJSJ =" + sBJSJ + "  ";
                                }

                                if (sBJSPYS != null && sBJSPYS == 0) {
                                    sBJSPYS = 0;
                                }
                                wherecase += " , BJSFWQBJ ='" + sBJSFWQBJ + "', BJSPYS=" + sBJSPYS + ",BJKDYS="
                                        + sBJKDYS + ",BJSFCQ='" + sBJSFCQ + "', BJBZYS=" + sBZYS + ", BJTBCXYS=" + sTBCXYS + " ";
                            } else {
                                // 不受理 、 不予受理
                                // if (sBLZT != null && (sBLZT == 4 || sBLZT ==
                                // 5)) {
                                // }
                                sBJSPYS = null;
                                sBJKDYS = null;
                                sBJSFCQ = 0;
                                wherecase += " , BJSFWQBJ ='" + sBJSFWQBJ + "', BJSPYS=" + sBJSPYS + ",BJKDYS="
                                        + sBJKDYS + ",BJSFCQ='" + sBJSFCQ + "' ";

                                wherecase += ",CNBJSJ= null ";
                                //wherecase += ", SJSJ= null ,BJSJ =null ";
                                if (sSJSJ != null) {
                                    wherecase += " , SJSJ ='" + sSJSJ + "'  ";
                                } else {
                                    wherecase += " , SJSJ =" + sSJSJ + "  ";
                                }

                                if (sBJSJ != null) {
                                    wherecase += " , BJSJ ='" + sBJSJ + "'  ";
                                } else {
                                    wherecase += " , BJSJ =" + sBJSJ + "  ";
                                }
								/*if (sBLZT != null && (sBLZT == 4 || sBLZT == 5 || sBLZT == 2 || sBLZT == 13 || sBLZT == 14 || sBLZT == 15)) {
									
								} 
								else {
								    
								}*/
                            }

                            // 根据事项编码确定工程阶段信息表
                            List<Record> gcjdlist = baseDao.findList(
                                    "select * from ST_SPGL_GCJDXXB  where XZQHDM=? and GCDM=?   AND DYBZSPJDXH=?     ORDER BY CREATETIMESTAMP desc  LIMIT 1",
                                    Record.class, sXZQHDM, sGCDM, sDYBZSPJDXH);

                            String gcjdRowGuid = UUID.randomUUID().toString();
                            if (gcjdlist != null && !gcjdlist.isEmpty()) {
                                gcjdRowGuid = gcjdlist.get(0).get("RowGuid");
                            } else {
                                EpointFrameDsManager.begin(null);
                                // 插入工程阶段审批表
                                baseDao.execute("insert into  ST_SPGL_GCJDXXB  (RowGuid,XZQHDM,XMDM"
                                                + ",XMMC,GCDM,GCFW,SPJDBM,SPJDMC,SPJDXH,DYBZSPJDXH"
                                                + ",SPJDSX,SPLCBM,QJDGCDM,SPLCBBH,SJYXBS,"
                                                + "SJWXYY,SJSCZT,SBYY,TIMESTAMP,DATASOURCE,CREATETIMESTAMP,SPJDSFCQ"
                                                + ") value (" + " ?,?,?" + ",?,?,?,?,?,?,?" + ",?,?,?,?,?,?,?,?,?,?,?,?" + ") ",
                                        gcjdRowGuid, sXZQHDM, sXMDM, Gcjbxxb.getXmmc(), sGCDM, Gcjbxxb.getGcfw(),
                                        sSPJDBM, Dfxmsplcjdxxb.getSpjdmc(), Dfxmsplcjdxxb.getSpjdxh(),
                                        Dfxmsplcjdxxb.getDybzspjdxh(), Dfxmsplcjdxxb.getSpjdsx(), sSPLCBM,
                                        Gcjbxxb.getQjdgcdm(), sSPLCBBH, Gcjbxxb.getSjyxbs(), Gcjbxxb.getSjwxyy(),
                                        Gcjbxxb.getSjsczt(), Gcjbxxb.getSbyy(), DateToStr(Gcjbxxb.getTimestamp()),
                                        Gcjbxxb.getDatasource(), DateToStr(Gcjbxxb.getCreatetimestamp()), 0);
                                EpointFrameDsManager.commit();
                            }

                            sJSROWGUID = gcjdRowGuid;

                            wherecase += ",JSROWGUID='" + sJSROWGUID + "'";

                            // 添加项目代码和项目名称
                            wherecase += ",XMDM='" + sXMDM + "'";
                            wherecase += ",XMMC='" + sXMMC + "'";

                            // 更新数据
                            EpointFrameDsManager.begin(null);
                            baseDao.execute("update ST_SPGL_XMSPSXBLXXB set RowGuid=RowGuid " + wherecase
                                    + " where RowGuid='" + sxRowguid + "'");
                            EpointFrameDsManager.commit();

                            errorList.add("成功1！");
                        } catch (Exception ex) {
                            ex.printStackTrace();

                            //system.out.println("遍历事项异常：工程代码：" + sxrecord.get("gcdm").toString());
                            //system.out.println("遍历事项异常：事项编码：" + sxrecord.get("SPSXBM").toString());

                            // 出现异常，继续执行
                            continue;
                        }

                    }
                    // 4、遍历工程阶段表
                    // 遍历所有的事项办理信息
                    List<StSpglGcJdXxb> Gcjdxxblist = new StSpglGcjbxxbService().findGcJdXxb(
                            "SELECT * FROM st_spgl_gcjdxxb WHERE (XZQHDM,XMDM,GCDM,DYBZSPJDXH) IN (SELECT "
                                    + "XZQHDM,XMDM,GCDM,DYBZSPJDXH FROM st_spgl_xmspsxblxxb WHERE SJYXBS=1 AND XZQHDM=? and GCDM=?)  "
                                    + "ORDER BY XZQHDM,XMDM,GCDM,DYBZSPJDXH",
                            sXZQHDM, sGCDM);

                    List<String> jdlist = new ArrayList<String>();

                    for (StSpglGcJdXxb Gcjdxxb : Gcjdxxblist) {
                        if (Gcjdxxb.getDybzspjdxh() == null) {
                            //system.out.println("对应标准审批阶段序号异常");
                        } else {
                            jdlist.add(Gcjdxxb.getDybzspjdxh());
                        }
                    }

                    String wherecase = "";

                    for (StSpglGcJdXxb Gcjdxxb : Gcjdxxblist) {

                        try {
                            Date sGCJDJSSJ = new Date();

                            // 判断是否完全办结
                            if (Gcjbxxb.getXmsfwqbj() == 1) {
                                Gcjdxxb.setGcjdsfbj(1);

                                wherecase += ", GCJDSFBJ=1 ";
                            } else {
                                // 判断阶段是否办结，
                                // 1、当前阶段办件均办结 2、下个阶段出现
                                /*
                                 * int bjnum = baseDao
                                 * .find("select count(*) from ST_SPGL_XMSPSXBLXXB  WHERE  (BJSFWQBJ <>1 OR BJSFWQBJ IS NULL ) AND  SJYXBS='1'   "
                                 * + " and JSROWGUID=? ", Integer.class,
                                 * Gcjdxxb.getRowguid()); int JDXH = 0; if
                                 * (Gcjdxxb.getDybzspjdxh() == null) {
                                 *
                                 * } else if
                                 * (Gcjdxxb.getDybzspjdxh().contains(",")) {
                                 *
                                 * if (Gcjdxxb.getDybzspjdxh().contains("4")) {
                                 * JDXH = 4; } else if
                                 * (Gcjdxxb.getDybzspjdxh().contains("3")) {
                                 * JDXH = 3; } else if
                                 * (Gcjdxxb.getDybzspjdxh().contains("2")) {
                                 * JDXH = 2; } else { JDXH = 1; }
                                 *
                                 * } else { JDXH =
                                 * Integer.parseInt(Gcjdxxb.getDybzspjdxh()); }
                                 *
                                 * int jdnum = baseDao.find(
                                 * "select  count(*)  from ST_SPGL_GCJDXXB  WHERE (case   WHEN  DYBZSPJDXH like '%4%' then 4      WHEN  DYBZSPJDXH like '%3%' then 3"
                                 * +
                                 * "  WHEN  DYBZSPJDXH like '%2%' then 2      WHEN  DYBZSPJDXH like '%1%' then 1 else  0 end )> ?  "
                                 * +
                                 * "  and  XZQHDM=? and GCDM=? AND  SJYXBS='1' "
                                 * , Integer.class, JDXH, sXZQHDM, sGCDM);
                                 *
                                 * if (bjnum == 0 && jdnum > 0) {
                                 * Gcjdxxb.setGcjdsfbj(1);
                                 *
                                 * wherecase += ", GCJDSFBJ=1 "; } else {
                                 * Gcjdxxb.setGcjdsfbj(0); wherecase +=
                                 * ", GCJDSFBJ=0 "; }
                                 */
                                // 判断阶段是否办结，
                                // 阶段里程碑事项类型
                                SpglDfxmsplcjdxxb spgldfxmsplcjdxxb = new SpglDfxmsplcjdxxbService()
                                        .findBYSplcbmAndSpjdbm(Gcjdxxb.getSplcbm(),
                                                String.valueOf(Gcjdxxb.getSplcbbh()), Gcjdxxb.getXzqhdm(),
                                                String.valueOf(Gcjdxxb.getSpjdxh()));

                                if (spgldfxmsplcjdxxb != null) {
                                    int LCBSXLX = spgldfxmsplcjdxxb.getLcbsxlx();
                                    if (LCBSXLX == 1) {
                                        // 是否存在未办理或者未办结的里程碑事项
                                        int jdsxnum = baseDao.find(
                                                "select  COUNT(*)  from   SPGL_DFXMSPLCJDSXXXB B  where  B.SFLCBSX =1  and  B.XZQHDM=? AND  B.SJYXBS='1'"
                                                        + " AND  B.SPJDXH=? AND B.SPLCBM=? AND B.SPLCBBH=? ",
                                                Integer.class, Gcjdxxb.getXzqhdm(), Gcjdxxb.getSpjdxh(),
                                                Gcjdxxb.getSplcbm(), Gcjdxxb.getSplcbbh());
                                        int SXNUM = baseDao.find(
                                                "select  COUNT(*)  from   SPGL_DFXMSPLCJDSXXXB B LEFT JOIN ST_SPGL_XMSPSXBLXXB S"
                                                        + " ON S.GCDM=?  AND B.SFLCBSX =1 AND  S.XZQHDM=B.XZQHDM AND S.SPSXBM =B.SPSXBM"
                                                        + " AND S.SPSXBBH =B.SPSXBBH AND S.SJYXBS='1' WHERE  B.XZQHDM=? AND  B.SJYXBS='1'"
                                                        + " AND  B.SPJDXH=? AND B.SPLCBM=? AND B.SPLCBBH=? AND  "
                                                        + "(S.SPSXBM is  NULL or S.BJSFWQBJ IS NULL  OR S.BJSFWQBJ='0')",
                                                Integer.class, Gcjdxxb.getGcdm(), Gcjdxxb.getXzqhdm(),
                                                Gcjdxxb.getSpjdxh(), Gcjdxxb.getSplcbm(), Gcjdxxb.getSplcbbh());

                                        if (jdsxnum > 0 && SXNUM == 0) {
                                            Gcjdxxb.setGcjdsfbj(1);
                                            wherecase += ", GCJDSFBJ=1 ";
                                        } else {
                                            Gcjdxxb.setGcjdsfbj(0);
                                            wherecase += ", GCJDSFBJ=0 ";
                                        }

                                    } else {
                                        // 是否存已经办结的里程碑事项
                                        int jdsxnum = baseDao.find(
                                                "select  COUNT(*)  from   SPGL_DFXMSPLCJDSXXXB B  where  B.SFLCBSX =1  and  B.XZQHDM=? AND  B.SJYXBS='1'"
                                                        + " AND  B.SPJDXH=? AND B.SPLCBM=? AND B.SPLCBBH=? ",
                                                Integer.class, Gcjdxxb.getXzqhdm(), Gcjdxxb.getSpjdxh(),
                                                Gcjdxxb.getSplcbm(), Gcjdxxb.getSplcbbh());

                                        int SXNUM = baseDao.find(
                                                "select  COUNT(*)  from   SPGL_DFXMSPLCJDSXXXB B LEFT JOIN ST_SPGL_XMSPSXBLXXB S"
                                                        + " ON S.GCDM=?  AND B.SFLCBSX =1 AND  S.XZQHDM=B.XZQHDM AND S.SPSXBM =B.SPSXBM"
                                                        + " AND S.SPSXBBH =B.SPSXBBH AND S.SJYXBS='1' WHERE  B.XZQHDM=? AND  B.SJYXBS='1'"
                                                        + " AND  B.SPJDXH=? AND B.SPLCBM=? AND B.SPLCBBH=? AND  "
                                                        + "  S.BJSFWQBJ='1' and blzt='11'",
                                                Integer.class, Gcjdxxb.getGcdm(), Gcjdxxb.getXzqhdm(),
                                                Gcjdxxb.getSpjdxh(), Gcjdxxb.getSplcbm(), Gcjdxxb.getSplcbbh());

                                        if (jdsxnum > 0 && SXNUM > 0) {
                                            Gcjdxxb.setGcjdsfbj(1);
                                            wherecase += ", GCJDSFBJ=1 ";
                                        } else {
                                            Gcjdxxb.setGcjdsfbj(0);
                                            wherecase += ", GCJDSFBJ=0 ";
                                        }
                                    }
                                } else {
                                    Gcjdxxb.setGcjdsfbj(0);
                                    wherecase += ", GCJDSFBJ=0 ";
                                }
                            }
                            // 工程阶段开始时间
                            List<Record> gckslist = baseDao.findList(
                                    "select * from ST_SPGL_XMSPSXBLXXB  WHERE JSROWGUID=? and sjsj IS NOT NULL  AND SJYXBS='1'   order by  sjsj   LIMIT 1 ",
                                    Record.class, Gcjdxxb.getRowguid());

                            if (gckslist != null && !gckslist.isEmpty()) {
                                Gcjdxxb.setGcjdkssj(gckslist.get(0).getDate("sjsj"));

                                wherecase += ", GCJDKSSJ='" + gckslist.get(0).get("sjsj") + "'";
                            }
                            if (Gcjdxxb.getGcjdsfbj() == null || Gcjdxxb.getGcjdsfbj() == 0) {
                                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                List<Record> wbjlist = baseDao.findList(
                                        "SELECT * FROM st_spgl_xmspsxblxxb WHERE SJYXBS=1 AND BJSFWQBJ =0 AND JSROWGUID = ? ",
                                        Record.class, Gcjdxxb.getRowguid());
                                List<Record> gcjslist = baseDao.findList(
                                        "SELECT * FROM st_spgl_xmspsxblxxb WHERE SJYXBS=1 AND BJSFWQBJ =1 AND JSROWGUID = ? ORDER BY BJSJ DESC LIMIT 1",
                                        Record.class, Gcjdxxb.getRowguid());
                                if (!wbjlist.isEmpty()) {
                                    wherecase += ", GCJDJSSJ='" + sDateFormat.format(new Date()) + "'";
                                } else {
                                    wherecase += ", GCJDJSSJ='" + gcjslist.get(0).get("BJSJ") + "'";
                                    sGCJDJSSJ = gcjslist.get(0).get("BLSJ");
                                }
                            } else {
                                // 工程阶段结束时间
                                List<Record> gcjslist = baseDao.findList(
                                        "select * from ST_SPGL_XMSPSXBLXXB  WHERE JSROWGUID=? and BLSJ IS NOT NULL AND  SJYXBS='1'    order by  BLSJ  desc  LIMIT 1 ",
                                        Record.class, Gcjdxxb.getRowguid());

                                if (gcjslist != null && !gcjslist.isEmpty()) {
                                    Gcjdxxb.setGcjdjssj(gcjslist.get(0).getDate("BLSJ"));

                                    wherecase += ", GCJDJSSJ='" + gcjslist.get(0).get("BLSJ") + "'";

                                    sGCJDJSSJ = gcjslist.get(0).get("BLSJ");
                                }

                            }

                            // 法定审批事项个数
                            /*
                             * int num =baseDao.
                             * find("select count(*) from SPGL_DFXMSPLCJDSXXXB C INNER JOIN SPGL_DFXMSPLCJDXXB B "
                             * +
                             * " ON  C.XZQHDM=B.XZQHDM AND  C.SPLCBM =B.SPLCBM AND C.SPLCBBH=B.SPLCBBH AND C.SPJDXH=B.SPJDXH "
                             * +
                             * "where  C.SJYXBS=1  and  B.SJYXBS=1 AND  C.XZQHDM=? and C.SPLCBM=? and C.SPLCBBH=? and B.DYBZSPJDXH=?"
                             * ,Integer.class, sXZQHDM,
                             * Gcjdxxb.getSplcbm(),Gcjdxxb.getSplcbbh(),Gcjdxxb.
                             * getDybzspjdxh());
                             */

                            // 实际审批事项类型个数

                            // 实际审批次数

                            // 判断是否有退件
                            int num = baseDao.find(
                                    "select count(*) from  ST_SPGL_XMSPSXBLXXB A INNER JOIN SPGL_XMSPSXBLXXXXB B ON A.XZQHDM=B.XZQHDM AND A.GCDM=B.GCDM "
                                            + "AND A.SPSXSLBM=B.SPSXSLBM WHERE  ( B.BLZT='4' OR B.BLZT='5') AND  A.SJYXBS='1'  AND    B.SJYXBS='1'    and A.JSROWGUID=? ",
                                    Integer.class, Gcjdxxb.getRowguid());

                            if (num > 0) {
                                wherecase += ", SFYTJ=1";
                            } else {
                                wherecase += ", SFYTJ=0";
                            }

                            // 判断是否有其他类型办件
                            num = baseDao.find(
                                    "select count(*) from  ST_SPGL_XMSPSXBLXXB A INNER JOIN SPGL_XMSPSXBLXXXXB B ON A.XZQHDM=B.XZQHDM AND A.GCDM=B.GCDM "
                                            + "AND A.SPSXSLBM=B.SPSXSLBM WHERE  ( B.BLZT='2' OR B.BLZT='14' OR B.BLZT='15')  AND  A.SJYXBS='1'  AND    B.SJYXBS='1'  AND A.JSROWGUID=? ",
                                    Integer.class, Gcjdxxb.getRowguid());

                            if (num > 0) {
                                wherecase += ", SFYQTLXBJ=1";
                            } else {
                                wherecase += ", SFYQTLXBJ=0";
                            }
							/*// 工程阶段审批用时
							List<Record> spxxlist = baseDao.findList(
									"select BJSPYS , BLSPSLBM from ST_SPGL_XMSPSXBLXXB  WHERE JSROWGUID=?   AND   SJYXBS='1'   ",
									Record.class, Gcjdxxb.getRowguid());
							Double sGCJDSPYS = 0.0;
							Map<String, Double> map = new HashMap<String, Double>();
							for (Record spxx : spxxlist) {
								if (spxx.get("BLSPSLBM") == null || spxx.get("BLSPSLBM").toString() == "") {
									if (spxx.get("BJSPYS") == null){

									} else {
										sGCJDSPYS += spxx.getDouble("BJSPYS");
									}
								} else {
									// 并联审批事项计算
									if (map.containsKey(spxx.get("BLSPSLBM"))) {
										if (spxx.get("BJSPYS") == null) {
										 // do something
										} else {
											// 并联事项，取最大涉农用时
											if (map.get(spxx.getStr("BLSPSLBM")) > spxx.getDouble("BJSPYS")) {

											} else {
												sGCJDSPYS = sGCJDSPYS + spxx.getDouble("BJSPYS")
														- map.get(spxx.getStr("BLSPSLBM"));
												map.put(spxx.getStr("BLSPSLBM"), spxx.getDouble("BJSPYS"));
											}
										}
									} else {
										// 首次出现事项，直接相加
										map.put(spxx.getStr("BLSPSLBM"),
												spxx.get("BJSPYS") == null ? 0.0 : spxx.getDouble("BJSPYS"));
										sGCJDSPYS = sGCJDSPYS
												+ (spxx.get("BJSPYS") == null ? 0.0 : spxx.getDouble("BJSPYS"));
									}
								}
							}*/
                            int sGCJDSPYS = getDate(Gcjdxxb.getRowguid()).getInt("GCJDSPYS");
                            wherecase += ", GCJDSPYS='" + sGCJDSPYS + "'";

                            //工程阶段特别程序用时
                            int sGCJDTBCXYS = getDate(Gcjdxxb.getRowguid()).getInt("GCJDTBCXYS");
                            wherecase += ", GCJDTBCXYS='" + sGCJDTBCXYS + "'";

                            //工程阶段阶段用时
                            Double sGCJDJDYS = getGCJDJDYS(Gcjdxxb.getRowguid(), Gcjdxxb.getGcjdkssj(), sGCJDJSSJ);
                            wherecase += ", GCJDJDYS='" + sGCJDJDYS + "'";

                            //建设单位合理化用时
                            Double sJSDWHLHYS = sGCJDJDYS - sGCJDTBCXYS - sGCJDSPYS;
                            wherecase += ", JSDWHLHYS='" + sJSDWHLHYS + "'";

                            if (Gcjdxxb.getGcjdkssj() != null && sGCJDJSSJ != null) {
                                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                // 工程阶段跨度用时
                                Double sGCJDKDYS = new JnSpglXmjbxxbService().daysBetweenD(
                                        sDateFormat.format(Gcjdxxb.getGcjdkssj()), sDateFormat.format(sGCJDJSSJ));
                                wherecase += ", GCJDKDYS='" + sGCJDKDYS + "'";
                            }

                            // 判断阶段是否超期
                            Integer sSPJDSFCQ = null;
                            if (sGCJDSPYS > Gcjdxxb.getSpjdsx()) {
                                sSPJDSFCQ = 1;
                            } else {
                                // 遍历所有事项信息
                                num = baseDao.find(
                                        "select count(*) from  ST_SPGL_XMSPSXBLXXB A   "
                                                + "WHERE   A.SJYXBS='1' and A.BJSFCQ=1 AND A.JSROWGUID=? ",
                                        Integer.class, Gcjdxxb.getRowguid());

                                if (num > 0) {
                                    sSPJDSFCQ = 1;
                                } else {
                                    sSPJDSFCQ = 0;
                                }
                            }

                            wherecase += ", SPJDSFCQ='" + sSPJDSFCQ + "'";
                            // 更新工程阶段数据
                            EpointFrameDsManager.begin(null);
                            baseDao.execute("update ST_SPGL_GCJDXXB set RowGuid=RowGuid " + wherecase
                                    + " where RowGuid='" + Gcjdxxb.getRowguid() + "'");
                            EpointFrameDsManager.commit();
                            errorList.add("成功2！");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            //system.out.println("遍历工程阶段异常：项目代码：" + Gcjdxxb.getXmdm());
                            //system.out.println("遍历工程阶段异常：工程代码：" + Gcjdxxb.getGcdm());
                            //system.out.println("遍历工程阶段异常：阶段序号：" + Gcjdxxb.getSpjdxh());
                            //system.out.println("遍历工程阶段异常：标准阶段序号：" + Gcjdxxb.getDybzspjdxh());

                            continue;
                        }
                    }
                    // 5、工程信息表更新
                } catch (Exception e) {
                    e.printStackTrace();
                    //system.out.println("遍历工程异常：工程代码：" + qhgcdm);
                    continue;
                }

            }

            // 6、项目信息表更新
            for (String xmdmls : xmdmlist) {

                try {
                    List<Record> spxmlist = baseDao.findList(
                            "select  * from ST_SPGL_GCJDXXB  WHERE XZQHDM=?  and XMDM=? and  SJYXBS='1' ",
                            Record.class, xmdmls.split("&")[1], xmdmls.split("&")[0]);

                    List<Record> firstgcjdlist = baseDao.findList(
                            "select  * from ST_SPGL_GCJDXXB  WHERE   (`XZQHDM` ,`XMDM` ,gcdm) = (select `XZQHDM` ,`XMDM` ,gcdm "
                                    + "from ST_SPGL_GCJDXXB where XZQHDM= ?  and XMDM= ? and  SJYXBS='1' order  by `GCJDKSSJ` limit 1) ",
                            Record.class, xmdmls.split("&")[1], xmdmls.split("&")[0]);

                    // 判断项目是否完全办结
                    StSpglXmjbxxb stspglxmjbxxb = new StSpglXmjbxxbService().find(
                            "select * from st_spgl_xmjbxxb where  XZQHDM=?  and XMDM=? and  SJYXBS='1' ",
                            xmdmls.split("&")[1], xmdmls.split("&")[0]);

                    if (stspglxmjbxxb == null) {
                        continue;
                    }
                    // 项目是否完全办结
                    Integer sXMSFWQBJ = stspglxmjbxxb.getXmsfwqbj();

                    // 是否超期
                    int sXMSFYQ = 0;
                    // 是否退件
                    int sSFYTJ = 0;
                    // 是否其他
                    int sSFYQTLXBJ = 0;
                    // 阶段用时map
                    Map<String, Double> map = new HashMap<String, Double>();
                    // 审批用时
                    Double sXMSPYS = 0.0;
                    // 跨度用时map
                    Map<String, Date> mapk = new HashMap<String, Date>();

                    // 跨度用时
                    Double sXMKDYS = 0.0;
                    // 省级审批流程类型
                    int sSJSPLCLX = 0;
                    // 总审批事项个数
                    int sZSPSXGS = 0;
                    // 并行审批总时限
                    Double sBXSPZSX = 0.0;
                    // 并行审批事项个数
                    int sBXSPSXGS = 0;
                    // 并联审批总时限
                    Double sBLSPZSX = 0.0;
                    // 并联审批事项个数
                    int sBLSPSXGS = 0;
                    // 申请时间（取所有办件信息内最早的办件时间）
                    Date sSqsj = null;

                    for (Record firgc : firstgcjdlist) {
                        // 审批用时
                        if (map.containsKey(firgc.get("DYBZSPJDXH"))) {
                            if (firgc.get("GCJDSPYS") == null) {

                            } else {
                                if (firgc.getDouble("GCJDSPYS") > map.get(firgc.getStr("DYBZSPJDXH"))) {

                                    sXMSPYS = sXMSPYS - map.get(firgc.getStr("DYBZSPJDXH")) + firgc.getDouble("GCJDSPYS");
                                    map.put(firgc.get("DYBZSPJDXH"), firgc.getDouble("GCJDSPYS"));
                                }
                            }

                        } else {
                            sXMSPYS += firgc.get("GCJDSPYS") == null ? 0.0 : firgc.getDouble("GCJDSPYS");

                            map.put(firgc.get("DYBZSPJDXH"),
                                    firgc.get("GCJDSPYS") == null ? 0.0 : firgc.getDouble("GCJDSPYS"));
                        }
                    }

                    for (Record spxm : spxmlist) {

                        // 判断是否逾期
                        sXMSFYQ = baseDao.find(
                                " select count(*) from ST_SPGL_XMSPSXBLXXB where  BJSFCQ='1' and  SJYXBS='1' and xmdm =? and XZQHDM=? ",
                                Integer.class, spxm.get("XMDM"), spxm.get("XZQHDM"));

                        if (sSFYTJ == 0) {
                            sSFYTJ = spxm.get("SFYTJ") == null ? 0 : spxm.getInt("SFYTJ");
                        }
                        if (sSFYQTLXBJ == 0) {
                            sSFYQTLXBJ = spxm.get("SFYQTLXBJ") == null ? 0 : spxm.getInt("SFYQTLXBJ");
                        }


                        // 跨度用时
                        if (mapk.containsKey("min")) {
                            if (spxm.getDate("GCJDKSSJ") == null) {

                            } else if (mapk.get("min") == null
                                    || mapk.get("min").getTime() - spxm.getDate("GCJDKSSJ").getTime() > 0) {
                                mapk.put("min", spxm.getDate("GCJDKSSJ"));
                            }

                        } else {
                            mapk.put("min", spxm.getDate("GCJDKSSJ"));
                        }

                        if (mapk.containsKey("max")) {
                            if (sXMSFWQBJ == null || sXMSFWQBJ == 0) {
                                mapk.put("max", new Date());
                            } else {
                                if (spxm.getDate("GCJDJSSJ") == null) {

                                } else if (mapk.get("max") == null
                                        || mapk.get("max").getTime() - spxm.getDate("GCJDJSSJ").getTime() < 0) {
                                    mapk.put("max", spxm.getDate("GCJDJSSJ"));
                                }
                            }

                        } else {
                            if (sXMSFWQBJ == null || sXMSFWQBJ == 0) {
                                mapk.put("max", new Date());
                            } else {
                                mapk.put("max", spxm.getDate("GCJDJSSJ"));
                            }

                        }

                        if (mapk.get("min") != null && mapk.get("max") != null) {
                            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            sXMKDYS = new JnSpglXmjbxxbService().daysBetweenD(sDateFormat.format(mapk.get("min")),
                                    sDateFormat.format(mapk.get("max")));
                        }

                        // 省级审批流程类型
                        /*
                         * if (spxm.getInt("SPLCLX") == 3 ||
                         * spxm.getInt("SPLCLX") == 4) { sSJSPLCLX = 2; } else
                         * if (spxm.getInt("SPLCLX") == 5) { sSJSPLCLX = 3; }
                         * else if (spxm.getInt("SPLCLX") == 6) { sSJSPLCLX = 4;
                         * } else { sSJSPLCLX = 1; }
                         */

                        // 并联审批事项个数
                        sBLSPSXGS = baseDao.find(
                                "select count(*)  from ST_SPGL_XMSPSXBLXXB where XMDM=? and XZQHDM=? and SJYXBS='1' ",
                                Integer.class, spxm.get("XMDM"), spxm.get("XZQHDM"));

                        // 并行审批事项个数
                        sBXSPSXGS = baseDao.find(
                                "select count(*)  from ST_SPGL_XMSPSXBLXXB where XMDM=?  and XZQHDM=? and SJYXBS='1' and   DYBZSPJDXH=5  ",
                                Integer.class, spxm.get("XMDM"), spxm.get("XZQHDM"));

                        // 总审批事项个数(主流程)
                        sZSPSXGS = sBLSPSXGS - sBXSPSXGS;

                        // 并行审批总时限
                        sBXSPZSX = baseDao.find(
                                "select  ifnull(sum( ifnull(BJSPYS,0)),0)  from ST_SPGL_XMSPSXBLXXB where XMDM=?  and XZQHDM=? and SJYXBS='1' and   DYBZSPJDXH=5  ",
                                Double.class, spxm.get("XMDM"), spxm.get("XZQHDM"));

                        // 并联审批总时限
                        sBLSPZSX = sBXSPZSX + sXMSPYS;

                        // 申请时间 取最早的办件时间
                        sSqsj = new StSpglXmspsxblxxxxbService().getFirstBlsjByXmdm(spxm.get("XMDM"));
                    }
                    //where条件为数据有效标识 SJYXBS
                    //2022/06/07 阳佳 给st_spgl_xmjbxxb加上areacode 根据xddm查询
                    String itemCode = xmdmls.split("&")[0];
                    //查询audit_rs_item_baseinfo
                    AuditRsItemBaseinfo auditRsItemBaseinfo = itemBaseinfo.getAuditRsItemBaseinfoByItemcode(itemCode).getResult();
                    String areaCode = "";
                    if (auditRsItemBaseinfo != null) {
                        areaCode = auditRsItemBaseinfo.getBelongxiaqucode();
                    }
                    //system.out.println("阳佳的测试：" + areaCode);
                    if (sSqsj == null) {
                        EpointFrameDsManager.begin(null);
                        baseDao.execute(
                                "update ST_SPGL_XMJBXXB set  XMSFYQ=?,SFYTJ	=?,SFYQTLXBJ=?,XMSPYS=?,XMKDYS=?,"
                                        + "ZSPSXGS=?,BXSPZSX=?,BXSPSXGS=?,BLSPZSX=?,BLSPSXGS=?,SJSPLCLX=(CASE WHEN SPLCLX =3 OR  SPLCLX =4 THEN 2  "
                                        + " WHEN SPLCLX=6 THEN 3 WHEN SPLCLX=1 OR  SPLCLX =2 THEN 1 ELSE 4  END),areacode=?  where XMDM=?  and XZQHDM=? and SJYXBS='1' "
                                        + "",
                                sXMSFYQ, sSFYTJ, sSFYQTLXBJ, sXMSPYS, sXMKDYS, sZSPSXGS, sBXSPZSX, sBXSPSXGS, sBLSPZSX,
                                sBLSPSXGS, areaCode, xmdmls.split("&")[0], xmdmls.split("&")[1]);
                        EpointFrameDsManager.commit();

                    } else {
                        EpointFrameDsManager.begin(null);
                        baseDao.execute(
                                "update ST_SPGL_XMJBXXB set  XMSFYQ=?,SFYTJ=?,SFYQTLXBJ=?,XMSPYS=?,XMKDYS=?,"
                                        + "ZSPSXGS=?,BXSPZSX=?,BXSPSXGS=?,BLSPZSX=?,BLSPSXGS=?,SJSPLCLX=(CASE WHEN SPLCLX =3 OR  SPLCLX =4 THEN 2  "
                                        + " WHEN SPLCLX=6 THEN 3 WHEN SPLCLX=1 OR  SPLCLX =2 THEN 1 ELSE 4  END),Sqsj=?  where XMDM=?  and XZQHDM=? and SJYXBS='1' "
                                        + "",
                                sXMSFYQ, sSFYTJ, sSFYQTLXBJ, sXMSPYS, sXMKDYS, sZSPSXGS, sBXSPZSX, sBXSPSXGS, sBLSPZSX,
                                sBLSPSXGS, DateToStr(sSqsj), xmdmls.split("&")[0],
                                xmdmls.split("&")[1]);
                        EpointFrameDsManager.commit();
                    }

                    errorList.add("成功3！" + citycode);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    //system.out.println("遍历项目异常：项目代码：" + xmdmls.split("&")[0]);

                }
            }
			/*if (errorList.size() == 0) {
				// 计算无误，完成抽取结果
				// baseDao.executeProcudure("ST_PRO_COMPLETE_SPGL");
			}*/
            for (String error : errorList) {

                //system.out.println(error);
            }

            //system.out.println("=====数据抽取服务结束时间  ：" + EpointDateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss") + "=====");

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private int getAuditTime(Date sSJSJ, SimpleDateFormat sDateFormat, Date sBJSJ, List<StSpglXmspsxblxxxxb> start_list,
                             List<StSpglXmspsxblxxxxb> end_list) {
        int sBJSPYS = 0;
        int sTbcxsj = 0;
        sTbcxsj = getTBCXTime(sDateFormat, start_list, end_list);
        if (sBJSJ == null) {// 未办结
            sBJSPYS = new JnSpglXmjbxxbService().countWorkingDay(sDateFormat.format(sSJSJ),
                    sDateFormat.format(new Date()));
        } else {
            sBJSPYS = new JnSpglXmjbxxbService().countWorkingDay(sDateFormat.format(sSJSJ), sDateFormat.format(sBJSJ));
        }
        sBJSPYS = sBJSPYS - sTbcxsj;

        return sBJSPYS;
    }

    // 返回特别程序时间
    private int getTBCXTime(SimpleDateFormat sDateFormat, List<StSpglXmspsxblxxxxb> start_list, List<StSpglXmspsxblxxxxb> end_list) {
        int sTbcxsj = 0;
        for (int i = 0; i < start_list.size(); i++) {
            String startdate = sDateFormat.format(start_list.get(i).getBlsj());
            String enddate = null;
            if (CollectionUtils.isEmpty(end_list)) {
                enddate = sDateFormat.format(new Date());
            }
            if (i < end_list.size()) {
                enddate = sDateFormat.format(end_list.get(i).getBlsj());
            } else {
                enddate = sDateFormat.format(new Date());
            }
            sTbcxsj += new JnSpglXmjbxxbService().countWorkingDay(startdate, enddate);
        }

        return sTbcxsj;
    }

    /**
     * 获取阶段审批用时，阶段特殊程序用时
     *
     * @param JSROWGUID JSROWGUID
     * @return 返回值
     */
    public Record getDate(String JSROWGUID) {
        Record daterc = new Record();
        int gcjdspys = 0;
        int bjbzys = 0;
        int bjtbcxys = 0;
        // 工程阶段审批用时
        List<Record> spxxlist = baseDao.findList(//查询不包含撤件、不受理、不予受理的审批件
                "SELECT a.XZQHDM,a.XMDM,a.GCDM,a.SPSXSLBM,a.SJSJ,IFNULL(BJSJ,NOW()) AS BJSJ,a.BJBZYS,a.BJTBCXYS FROM st_spgl_xmspsxblxxb"
                        + " a WHERE SJYXBS=1 AND JSROWGUID=? AND "
                        + "SPSXSLBM NOT IN (SELECT SPSXSLBM FROM st_spgl_xmspsxblxxxxb WHERE SJYXBS=1 AND BLZT IN (2,4,5,13,14,15) "
                        + "AND GCDM=a.GCDM  GROUP BY SPSXSLBM) order by sjsj ",
                Record.class, JSROWGUID);
        if (!spxxlist.isEmpty()) {
            List<Record> spxxxxlist = baseDao.findList(//查询该阶段下是否包含特殊用时
                    "select blzt,blsj,spsxslbm from ST_SPGL_XMSPSXBLXXXXB  WHERE xzqhdm = ? and gcdm=? AND SPSXSLBM IN "
                            + "(SELECT SPSXSLBM FROM st_spgl_xmspsxblxxb WHERE JSROWGUID = ? ) and blzt in (6,7,9,10) AND   SJYXBS='1' order by blsj  ",
                    Record.class, spxxlist.get(0).getStr("xzqhdm"), spxxlist.get(0).getStr("gcdm"), JSROWGUID);
            if (!spxxxxlist.isEmpty()) {//判断该阶段下的办件是否含有特别程序或补正时间
                if (spxxlist.size() > 1) {//多个审批件且含有特殊用时
                    //多个事项并且含有补正
                    List<Record> sjdlist = gettscxsjdlist(spxxlist);//获取特殊程序时间段
                    sjdlist = getbjsjdlist(sjdlist);//获取特殊程序并集时间段
                    spxxlist = getbjspsjdlist(spxxlist);//获取审批并集时间段

                    for (Record spxx : spxxlist) {
                        gcjdspys += getAuditTime(spxx.getDate("SJSJ"), spxx.getDate("BJSJ"));
                    }
                    for (Record tbxx : sjdlist) {
                        bjtbcxys += getAuditTime(tbxx.getDate("kssj"), tbxx.getDate("jssj"));
                    }
                    gcjdspys = gcjdspys - bjtbcxys;
                } else {//判断该阶段是否只有一个办件
                    //单个事项并且含有补正
                    if (spxxlist.get(0).getInt("BJBZYS") != null) {
                        bjbzys = spxxlist.get(0).getInt("BJBZYS");
                    }
                    if (spxxlist.get(0).getInt("BJTBCXYS") != null) {
                        bjtbcxys = spxxlist.get(0).getInt("BJTBCXYS");
                    }
                    gcjdspys = getAuditTime(spxxlist.get(0).getDate("SJSJ"), spxxlist.get(0).getDate("BJSJ")) - bjbzys - bjtbcxys;
                }
                daterc.set("GCJDSPYS", gcjdspys);
                daterc.set("GCJDTBCXYS", bjtbcxys + bjbzys);
            } else {
                if (spxxlist.size() > 1) {//判断是否含有多个办件
                    //多个事项并且不含有补正
                    spxxlist = getbjspsjdlist(spxxlist);//获取审批并集时间段
                    for (Record spsx : spxxlist) {
                        gcjdspys += getAuditTime(spsx.getDate("SJSJ"), spsx.getDate("BJSJ"));
                    }
                } else {//判断该阶段是否只有一个办件
                    //单个事项并且不含有补正
                    gcjdspys = getAuditTime(spxxlist.get(0).getDate("SJSJ"), spxxlist.get(0).getDate("BJSJ"));
                }
                daterc.set("GCJDSPYS", gcjdspys);
                daterc.set("GCJDTBCXYS", bjtbcxys);
            }
        }
        daterc.set("GCJDSPYS", gcjdspys);
        daterc.set("GCJDTBCXYS", bjtbcxys);
        return daterc;
    }

    /**
     * 通过审批办结列表获取特别程序用时时间段
     *
     * @param spxxlist spxxlist
     * @return 返回值
     */
    public List<Record> gettscxsjdlist(List<Record> spxxlist) {
        List<Record> sjdlist = new ArrayList<>();
        for (Record spxx : spxxlist) {
            List<Record> tbcxkslist = baseDao.findList("select * from st_spgl_xmspsxblxxxxb where sjyxbs=1 and xzqhdm=? and gcdm=? and "
                            + "spsxslbm=? and blzt in (6,9) group by DATE_FORMAT(blsj,'%Y-%m-%d %H:%m:%s')",
                    Record.class, spxx.getStr("xzqhdm"), spxx.getStr("gcdm"), spxx.getStr("spsxslbm"));
            List<Record> tbcxjslist = baseDao.findList("select * from st_spgl_xmspsxblxxxxb where sjyxbs=1 and xzqhdm=? and gcdm=? and "
                            + "spsxslbm=? and blzt in (7,10) group by DATE_FORMAT(blsj,'%Y-%m-%d %H:%m:%s')",
                    Record.class, spxx.getStr("xzqhdm"), spxx.getStr("gcdm"), spxx.getStr("spsxslbm"));
            if (!tbcxkslist.isEmpty()) {//特殊程序开始时间列表不为空
                for (int i = 0; i < tbcxkslist.size(); i++) {
                    Record sjd = new Record();
                    sjd.set("kssj", tbcxkslist.get(i).getDate("BLSJ"));
                    if (tbcxjslist.isEmpty()) {//判断特别程序结束时间列表为空
                        sjd.set("jssj", new Date());
                    } else if (i >= tbcxjslist.size()) {
                        sjd.set("jssj", new Date());
                    } else {
                        sjd.set("jssj", tbcxjslist.get(i).getDate("BLSJ"));
                    }
                    sjdlist.add(sjd);
                }
            }
        }
        sjdlist = sort(sjdlist);
        return sjdlist;
    }

    /**
     * 特殊程序时间段按照开始时间升序排序
     *
     * @param sjdlist sjdlist
     * @return 返回值
     */
    public List<Record> sort(List<Record> sjdlist) {
        Collections.sort(sjdlist, new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                Date dt1 = o1.getDate("kssj");
                Date dt2 = o2.getDate("kssj");
                if (dt1.getTime() > dt2.getTime()) {
                    return 1;
                } else if (dt1.getTime() < dt2.getTime()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }); // 按时间排序
        return sjdlist;
    }

    /**
     * 通过特殊程序时间段列表循环遍历获取并集时间段
     *
     * @param sjdlist sjdlist
     * @return 返回值
     */
    public List<Record> getbjsjdlist(List<Record> sjdlist) {
        for (int i = 0; i < sjdlist.size(); i++) {//遍历获取特殊用时时间段列表
            Record dataI = sjdlist.get(i);
            for (int j = i + 1; j < sjdlist.size(); j++) {
                Record dataJ = sjdlist.get(j);
                if (dataI.getDate("jssj").compareTo(dataJ.getDate("kssj")) >= 0 && dataI.getDate("jssj").compareTo(dataJ.getDate("jssj")) <= 0) {
                    dataI.set("jssj", dataJ.getDate("jssj"));
                    sjdlist.remove(j);
                    j--;
                } else if (dataI.getDate("jssj").compareTo(dataJ.getDate("jssj")) >= 0) {
                    sjdlist.remove(j);
                    j--;
                } else {
                    break;
                }
                i = j;
            }
        }
        return sjdlist;
    }

    /**
     * 获取该阶段下审批时间段（并集）
     *
     * @param spxxlist spxxlist
     * @return 返回值
     */
    public List<Record> getbjspsjdlist(List<Record> spxxlist) {
        for (int i = 0; i < spxxlist.size(); i++) {//遍历获取该阶段下审批时间时间段列表
            Record dataI = spxxlist.get(i);
            for (int j = i + 1; j < spxxlist.size(); j++) {
                Record dataJ = spxxlist.get(j);
                if (dataI.getDate("BJSJ").compareTo(dataJ.getDate("SJSJ")) >= 0 && dataI.getDate("BJSJ").compareTo(dataJ.getDate("BJSJ")) <= 0) {
                    dataI.set("BJSJ", dataJ.getDate("BJSJ"));
                    spxxlist.remove(j);
                    j--;
                } else if (dataI.getDate("BJSJ").compareTo(dataJ.getDate("BJSJ")) >= 0) {
                    spxxlist.remove(j);
                    j--;
                } else {
                    break;
                }
                i = j;
            }
        }
        return spxxlist;
    }

    public Double getGCJDJDYS(String JSROWGUID, Date gcjdkssj, Date gcjdjssj) {
        Double gcjdjdys = 0.0;
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String qjdsql = "SELECT * FROM st_spgl_gcjdxxb WHERE (XZQHDM,XMDM,GCDM,DYBZSPJDXH) "
                + "IN (SELECT XZQHDM,XMDM,GCDM,DYBZSPJDXH-1 FROM st_spgl_gcjdxxb WHERE SJYXBS=1 AND ROWGUID = ?)";
        String sql = "SELECT * FROM st_spgl_gcjdxxb WHERE ROWGUID = ?";

        List<Record> qgcjdlist = baseDao.findList(qjdsql, Record.class, JSROWGUID);
        List<Record> gcjdlist = baseDao.findList(sql, Record.class, JSROWGUID);
        if (qgcjdlist.isEmpty()) {
            gcjdjdys = new JnSpglXmjbxxbService().daysBetweenD(
                    sDateFormat.format(gcjdkssj), sDateFormat.format(gcjdjssj));
        } else {
            if ("4".equals(gcjdlist.get(0).getStr("DYBZSPJDXH"))) {
                gcjdjdys = new JnSpglXmjbxxbService().daysBetweenD(
                        sDateFormat.format(gcjdkssj), sDateFormat.format(gcjdjssj));
            } else {
                gcjdjdys = new JnSpglXmjbxxbService().daysBetweenD(
                        sDateFormat.format(qgcjdlist.get(0).get("gcjdjssj")), sDateFormat.format(gcjdjssj));
            }

        }
        return gcjdjdys;
    }

    //返回接件到办结之间的工作日用时
    private int getAuditTime(Date sSJSJ, Date sBJSJ) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int sBJSPYS = 0;
        if (sBJSJ == null) {// 未办结
            sBJSPYS = new JnSpglXmjbxxbService().countWorkingDay(sDateFormat.format(sSJSJ),
                    sDateFormat.format(new Date()));
        } else {
            sBJSPYS = new JnSpglXmjbxxbService().countWorkingDay(sDateFormat.format(sSJSJ), sDateFormat.format(sBJSJ));
        }

        return sBJSPYS;
    }

    /**
     * 获取补正或特殊程序的时间轴
     *
     * @param start_list 开始
     * @param end_list   结束
     * @return 时间轴
     */
    private LinkedList<Date> getSortedDatesOfStartAndEndStage(List<StSpglXmspsxblxxxxb> start_list,
                                                              List<StSpglXmspsxblxxxxb> end_list) {
        LinkedList<Date> dates = new LinkedList<Date>();

        for (int i = 0; i < start_list.size(); i++) {
            StSpglXmspsxblxxxxb stSpglXmspsxblxxxxb = start_list.get(i);
            Date blsj = stSpglXmspsxblxxxxb.getBlsj();
            dates.add(blsj);

            if (end_list.size() > i) {
                dates.add(end_list.get(i).getBlsj());
            }

        }
        return dates;
    }

    /**
     * 获取每个区间的间隔时间
     *
     * @param sDateFormat 格式化器
     * @param dates       时间轴
     * @return 间隔时间
     */
    private double getIntervalTime(SimpleDateFormat sDateFormat, LinkedList<Date> dates) {
        double intervalTime = 0D;
        for (int i = 0; i < dates.size() - 1; i++) {
            if (i % 2 != 0) {
                Date pre_end = dates.get(i);
                Date post_start = dates.get(i + 1);
                if (post_start == null) {
                    continue;
                }
                if (pre_end.before(post_start)) {
                    intervalTime += new JnSpglXmjbxxbService().countWorkingDayD(sDateFormat.format(pre_end),
                            sDateFormat.format(post_start));
                }
            }
        }
        return intervalTime;
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return str
     */
    public static String DateToStr(Date date) {
        Logger logger = Logger.getLogger(CrawldataDYService.class);
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String str = format.format(date);
            return str;
        } catch (Exception ex) {
            //system.out.println(ex);
            return null;
        }
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     */
    public int daysBetween(Date smdate, Date bdate) {
        long l = bdate.getTime() - smdate.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        return Integer.parseInt(String.valueOf(day)) + 1;
    }

    // 计算出事项的审批流程
    public SpglDfxmsplcxxb getST_SPGL_DFXMSPLCJDSXXXB(String sXZQHDM, String sSPLCBM, Double sSPLCBBH) {
        return new SpglDfxmsplcxxbService().find(
                "select * from SPGL_DFXMSPLCXXB where XZQHDM=? and SPLCBM=? and SPLCBBH=?  ORDER BY LSH desc  LIMIT 1  ",
                sXZQHDM, sSPLCBM, sSPLCBBH);
    }

    // 计算出事项的阶段信息
    public SpglDfxmsplcjdxxb getST_SPGL_DFXMSPLCJDXXB(String sXZQHDM, String sSPLCBM, Double sSPLCBBH, int sSPJDXH) {
        return new SpglDfxmsplcjdxxbService().find(
                "select * from SPGL_DFXMSPLCJDXXB where XZQHDM=? and SPLCBM=? and SPLCBBH=? and SPJDXH=?   ORDER BY LSH desc  LIMIT 1  ",
                sXZQHDM, sSPLCBM, sSPLCBBH, sSPJDXH);
    }

    // 计算出审批事项信息
    public SpglDfxmsplcjdsxxxb getST_SPGL_DFXMSPLCJDSXXXB(String sXZQHDM, String sSPLCBM, Double sSPLCBBH,
                                                          String sSPSXBM, Double sSPSXBBH) {
        return new SpglDfxmsplcjdsxxxbService().find(
                "select * from SPGL_DFXMSPLCJDSXXXB where XZQHDM=? and SPLCBM=? and SPLCBBH=? and SPSXBM=? and SPSXBBH=?  ORDER BY LSH desc  LIMIT 1  ",
                sXZQHDM, sSPLCBM, sSPLCBBH, sSPSXBM, sSPSXBBH);
    }


    public List<String> handleCirculationDate(String today, String passday) {
        List<String> listDate = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = dateFormat.parse(passday);
            Date endDate = dateFormat.parse(today);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            while (calendar.getTime().before(endDate)) {

                listDate.add(dateFormat.format(calendar.getTime()));
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            return listDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * TODO areacode是否为空
     * 查询st_spgl_xmjbxxb里面逾期的数据
     *
     * @return
     */
    public List<StSpglXmjbxxb> getEachAreaData() {
        String sql = "select areacode ,count(*) as projectCount,sum(case when XMSFYQ='1' then 1 else 0 end) as yuQiProjectCount from st_spgl_xmjbxxb ssx  group by areacode";
        return baseDao.findList(sql, StSpglXmjbxxb.class);
    }

    public List<StSpglXmjbxxb> getEachAreaYuQiPorjects(String areaCode, int first, int pageSize) {
        String sql = "select * from st_spgl_xmjbxxb ssx where XMSFYQ ='0' ";
        if (StringUtil.isNotBlank(areaCode) && !"undefined".equals(areaCode)) {
            sql += " and areacode = ?";
        }
        return baseDao.findList(sql, first, pageSize, StSpglXmjbxxb.class, areaCode);
    }

    public PageData<StSpglXmjbxxb> getListByPage(Map<String, String> conditionMap, int first, int pageSize, String sortField, String sortOrder) {
        return new SQLManageUtil().getDbListByPage(StSpglXmjbxxb.class, conditionMap, first, pageSize, sortField, sortOrder);
    }

    public List<StSpglGcJdXxb> getDataByXmdm(String xmdm) {
        String sql = "select * from st_spgl_gcjdxxb where xmdm = ?";
        return baseDao.findList(sql, StSpglGcJdXxb.class, xmdm);
    }

    public List<StSpglXmSpsxblxxb> getBlxxbListByRowguid(String jsRowguid) {
        String sql = "select * from st_spgl_xmspsxblxxb where jsrowguid = ?";
        return baseDao.findList(sql, StSpglXmSpsxblxxb.class, jsRowguid);
    }

    public List<StSpglXmjbxxb> getTotalData() {
        String sql = "select count(*) as projectCount,sum(case when XMSFYQ='1' then 1 else 0 end) as yuQiProjectCount from st_spgl_xmjbxxb ssx  ";
        return baseDao.findList(sql, StSpglXmjbxxb.class);
    }
}
