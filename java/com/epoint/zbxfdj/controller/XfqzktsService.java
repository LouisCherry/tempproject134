package com.epoint.zbxfdj.controller;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.common.util.StringUtil;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.xmz.cxbus.api.ICxBusService;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglJsgcxfsjscxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglJsgcxfysbaxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglJsgcxfysxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglJsgcxfsjscxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglJsgcxfysbaxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglJsgcxfysxxbV3Service;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.UUID;

public class XfqzktsService {

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    ICxBusService iCxBusService = ContainerFactory.getContainInfo().getComponent(ICxBusService.class);
    IUserService userService = ContainerFactory.getContainInfo().getComponent(IUserService.class);
    IOuService ouService = ContainerFactory.getContainInfo().getComponent(IOuService.class);
    IAuditSpISubapp iSubapp = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);
    ISpglJsgcxfysxxbV3Service jsgcxfysxxbV3Service = ContainerFactory.getContainInfo().getComponent(ISpglJsgcxfysxxbV3Service.class);
    ISpglJsgcxfsjscxxbV3Service jsgcxfsjscxxbV3Service = ContainerFactory.getContainInfo().getComponent(ISpglJsgcxfsjscxxbV3Service.class);
    ISpglJsgcxfysbaxxbV3Service jsgcxfysbaxxbV3Service = ContainerFactory.getContainInfo().getComponent(ISpglJsgcxfysbaxxbV3Service.class);

    /**
     * 插入消防验收数据
     */
    public void insertxfys(String subappguid, AuditProject auditProject, String xmdm, String gcdm, String gcmc, String lsh) {
        try {
            if (StringUtil.isNotBlank(subappguid)) {
                AuditSpISubapp subapp = iSubapp.getSubappByGuid(subappguid).getResult();
                Record xfys = iCxBusService.getDzbdDetail("tsjsgcxfyssqb", subappguid);
                if (xfys != null) {
                    int count = jsgcxfysxxbV3Service.getSpglJsgcxfysxxbV3Byflowsn(auditProject.getFlowsn());
                    if (count < 1) {
                        SpglJsgcxfysxxbV3 jsgcxfysxxbV3 = new SpglJsgcxfysxxbV3();
                        String dfsjzj = UUID.randomUUID().toString();
                        jsgcxfysxxbV3.setRowguid(dfsjzj);
                        jsgcxfysxxbV3.setOperatedate(new Date());
                        jsgcxfysxxbV3.setOperateusername("消防同步");
                        if (StringUtil.isNotBlank(xfys.getStr("yyyt"))) {
                            //是否改变用途
                            jsgcxfysxxbV3.setSfgbyt(1);
                            //原有用途
                            jsgcxfysxxbV3.setYyyt(xfys.getStr("yyyt"));
                        } else {
                            //是否改变用途
                            jsgcxfysxxbV3.setSfgbyt(0);
                        }
                        //项目负责人
                        jsgcxfysxxbV3.setXmfzr(xfys.getStr("Contact"));
                        //项目代码
                        jsgcxfysxxbV3.setXmdm(xmdm);
                        //行政区划代码
                        jsgcxfysxxbV3.setXzqhdm("370800");
                        //建设性质
                        if (StringUtil.isNotBlank(xfys.getStr("lb"))) {
                            jsgcxfysxxbV3.setJsxz(Integer.parseInt(xfys.getStr("lb")));
                        }
                        //工程投资额
                        if (StringUtil.isNotBlank(xfys.getStr("TotalInvestment"))) {
                            jsgcxfysxxbV3.setGctze(Double.parseDouble(xfys.getStr("TotalInvestment")));
                        }
                        //建设单位项目负责人
                        jsgcxfysxxbV3.setJsdwxmfzr(xfys.getStr("Contact"));
                        //数据上传状态
                        jsgcxfysxxbV3.setSjsczt(0);
                        if (subapp != null) {
                            //申请日期
                            jsgcxfysxxbV3.setSqrq(subapp.getCreatedate());
                            //设计单位填表日期
                            jsgcxfysxxbV3.setSjdwtbrq(subapp.getCreatedate());
                            //建设单位填表日期
                            jsgcxfysxxbV3.setJsdwtbrq(subapp.getCreatedate());
                        } else {
                            //申请日期
                            jsgcxfysxxbV3.setSqrq(new Date());
                            //设计单位填表日期
                            jsgcxfysxxbV3.setSjdwtbrq(new Date());
                            //建设单位填表日期
                            jsgcxfysxxbV3.setJsdwtbrq(new Date());
                        }
                        //建设单位
                        jsgcxfysxxbV3.setJsdw(xfys.getStr("ConstructionName"));
                        //工程代码
                        jsgcxfysxxbV3.setGcdm(gcdm);
                        //根据是否包含装修部位是否有值判断，填写值为1，否则为0
                        if (StringUtil.isNotBlank(xfys.getStr("ZXBW"))) {
                            //是否包含装饰装修工程
                            jsgcxfysxxbV3.setSfbhzxzsgc(1);
                            //装饰装修部位
                            jsgcxfysxxbV3.setZxbw(xfys.getStr("zxbw"));
                            if (StringUtil.isNotBlank(xfys.getStr("zxmj"))) {
                                //装修面积（平方米）
                                jsgcxfysxxbV3.setZxmj(Double.parseDouble(xfys.getStr("zxmj")));
                            } else {
                                //装修面积（平方米）
                                jsgcxfysxxbV3.setZxmj(0.00);
                            }
                            if (StringUtil.isNotBlank(xfys.getStr("zxszcs"))) {
                                //装修所在层数
                                jsgcxfysxxbV3.setZxszcs(xfys.getStr("zxszcs"));
                            } else {
                                //装修所在层数
                                jsgcxfysxxbV3.setZxszcs("无");
                            }
                        } else {
                            //是否包含装饰装修工程
                            jsgcxfysxxbV3.setSfbhzxzsgc(0);
                        }

                        //联系人
                        jsgcxfysxxbV3.setLxr(auditProject.getContactperson());
                        //联系人手机号
                        jsgcxfysxxbV3.setLxrsjh(auditProject.getContactphone());

                        //建设单位类型
                        jsgcxfysxxbV3.setJsdwlx(1);
                        //工程名称
                        jsgcxfysxxbV3.setGcmc(gcmc);
                        //工程地址
                        jsgcxfysxxbV3.setGcdz(xfys.getStr("ConstructionSite"));
                        //数据有效标识
                        jsgcxfysxxbV3.setSjyxbs(1);
                        //建设单位代码
                        jsgcxfysxxbV3.setJsdwdm(xfys.getStr("jsdwtyxydm"));
                        //是否使用保温材料
                        jsgcxfysxxbV3.setSfsybwcl(1);
                        //建筑保温材料类别
                        jsgcxfysxxbV3.setBwcllb(xfys.getStr("cllb"));
                        if (StringUtil.isNotBlank(xfys.getStr("bwszcs"))) {
                            //保温所在层数
                            jsgcxfysxxbV3.set("BWSZCS", xfys.getStr("bwszcs"));
                        } else {
                            jsgcxfysxxbV3.set("BWSZCS", "无");
                        }

                        //保温部位
                        jsgcxfysxxbV3.setBwbw("");
                        if (StringUtil.isNotBlank(xfys.getStr("bwcl"))) {
                            //保温材料
                            jsgcxfysxxbV3.setBwcl(xfys.getStr("bwcl"));
                        } else {
                            jsgcxfysxxbV3.setBwcl("无");
                        }

                        //审批事项实例编码
                        jsgcxfysxxbV3.setSpsxslbm(auditProject.getFlowsn());
                        //地方数据主键
                        jsgcxfysxxbV3.setDfsjzj(dfsjzj);
                        jsgcxfysxxbV3Service.insert(jsgcxfysxxbV3);
                        log.info(lsh + "=======插入3.0消防数据成功=======");
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(lsh + "=======插入3.0消防数据失败=======");
        }
    }

    /**
     * 插入消防审查数据
     */
    public void insertxfsc(String subappguid, AuditProject auditProject, String xmdm, String gcdm, String gcmc, String lsh) {
        try {
            if (StringUtil.isNotBlank(subappguid)) {
                AuditSpISubapp subapp = iSubapp.getSubappByGuid(subappguid).getResult();
                Record xfsc = iCxBusService.getDzbdDetail("formtable20230906183031", subappguid);
                if (xfsc != null) {
                    String userguid = auditProject.getReceiveuserguid();
                    FrameUser user = userService.getUserByUserField("userguid", userguid);
                    int count = jsgcxfsjscxxbV3Service.getSpglJsgcxfsjscxxbV3ByFlowsn(auditProject.getFlowsn());
                    if (count < 1) {
                        SpglJsgcxfsjscxxbV3 jsgcxfsjscxxbV3 = new SpglJsgcxfsjscxxbV3();
                        String dfsjzj = UUID.randomUUID().toString();
                        jsgcxfsjscxxbV3.setRowguid(dfsjzj);
                        jsgcxfsjscxxbV3.setOperatedate(new Date());
                        jsgcxfsjscxxbV3.setOperateusername("消防同步");
                        StringBuilder tsjsgcqx = new StringBuilder();
                        //建筑总面积大于二万平方米的
                        if (StringUtil.isNotBlank(xfsc.getStr("jzzmjdyewpfmd"))) {
                            tsjsgcqx.append("1,");
                        }
                        //建筑总面积大于一万五千平方米的
                        if (StringUtil.isNotBlank(xfsc.getStr("jzzmjdyywwqpfmd"))) {
                            tsjsgcqx.append("2,");
                        }
                        //建筑总面积大于一万平方米的
                        if (StringUtil.isNotBlank(xfsc.getStr("jzzmjdyewpfmd"))) {
                            tsjsgcqx.append("3,");
                        }
                        //建筑总面积大于二千五百平方米的
                        if (StringUtil.isNotBlank(xfsc.getStr("jzzmjdyewpfmd"))) {
                            tsjsgcqx.append("4,");
                        }
                        //建筑总面积大于一千平方米的
                        if (StringUtil.isNotBlank(xfsc.getStr("jzzmjdyewpfmd"))) {
                            tsjsgcqx.append("5,");
                        }
                        //建筑总面积大于五百平方米的
                        if (StringUtil.isNotBlank(xfsc.getStr("jzzmjdyewpfmd"))) {
                            tsjsgcqx.append("6,");
                        }
                        //国家工程建设消防技术标准规定的一类高层住宅建筑
                        if (StringUtil.isNotBlank(xfsc.getStr("jzzmjdyewpfmd"))) {
                            tsjsgcqx.append("7,");
                        }
                        //城市交通发电工程
                        if (StringUtil.isNotBlank(xfsc.getStr("jzzmjdyewpfmd"))) {
                            tsjsgcqx.append("8,");
                        }
                        //易燃易爆危险物品
                        if (StringUtil.isNotBlank(xfsc.getStr("jzzmjdyewpfmd"))) {
                            tsjsgcqx.append("9,");
                        }
                        //国家机关办公楼
                        if (StringUtil.isNotBlank(xfsc.getStr("jzzmjdyewpfmd"))) {
                            tsjsgcqx.append("10,");
                        }
                        //设有本条第一项至第六项所列情形的建设工程
                        if (StringUtil.isNotBlank(xfsc.getStr("jzzmjdyewpfmd"))) {
                            tsjsgcqx.append("11,");
                        }
                        //本条第十项第十一项规定以外的单体建筑面积大于四万平方米或者建筑高度超过五十米的公共建筑
                        if (StringUtil.isNotBlank(xfsc.getStr("jzzmjdyewpfmd"))) {
                            tsjsgcqx.append("12,");
                        }
                        String tsjsgcqx1 = "";
                        if (StringUtil.isNotBlank(tsjsgcqx) && tsjsgcqx.length() > 0) {
                            tsjsgcqx1 = tsjsgcqx.substring(0, tsjsgcqx.length() - 1);
                        }

                        //特殊建设工程情形
                        jsgcxfsjscxxbV3.setTsjsgcqx(tsjsgcqx1);
                        //联系人
                        jsgcxfsjscxxbV3.setLxr(auditProject.getContactperson());
                        //联系人手机号
                        jsgcxfsjscxxbV3.setLxrsjh(auditProject.getContactphone());
                        if (user != null) {
                            String ouguid = user.getOuGuid();
                            FrameOu ou = ouService.getOuByOuGuid(ouguid);
                            if (ou != null) {
                                //审查单位
                                jsgcxfsjscxxbV3.setScdw(ou.getOuname());
                            } else {
                                //审查单位
                                jsgcxfsjscxxbV3.setScdw("");
                            }
                        } else {
                            //审查单位
                            jsgcxfsjscxxbV3.setScdw("");
                        }

                        if (StringUtil.isNotBlank(xfsc.getStr("yyyt"))) {
                            //是否改变用途
                            jsgcxfsjscxxbV3.setSfgbyt(1);
                            //原有用途
                            jsgcxfsjscxxbV3.setYyyt(xfsc.getStr("yyyt"));
                        } else {
                            //是否改变用途
                            jsgcxfsjscxxbV3.setSfgbyt(0);
                        }

                        //项目代码
                        jsgcxfsjscxxbV3.setXmdm(xmdm);
                        //行政区划代码
                        jsgcxfsjscxxbV3.setXzqhdm("370800");
                        //建设性质
                        if (StringUtil.isNotBlank(xfsc.getStr("lb"))) {
                            jsgcxfsjscxxbV3.setJsxz(Integer.parseInt(xfsc.getStr("lb")));
                        }

                        //工程投资额
                        if (StringUtil.isNotBlank(xfsc.getStr("TotalInvestment"))) {
                            jsgcxfsjscxxbV3.setGctze(Double.parseDouble(xfsc.getStr("TotalInvestment")));
                        }

                        //数据上传状态
                        jsgcxfsjscxxbV3.setSjsczt(0);
                        if (subapp != null) {
                            //申请日期
                            jsgcxfsjscxxbV3.setSqrq(subapp.getCreatedate());
                        } else {
                            //申请日期
                            jsgcxfsjscxxbV3.setSqrq(new Date());
                        }
                        //建设单位
                        jsgcxfsjscxxbV3.setJsdw(xfsc.getStr("ConstructionName"));

                        //工程代码
                        jsgcxfsjscxxbV3.setGcdm(gcdm);

                        //根据是否包含装修部位是否有值判断，填写值为1，否则为0
                        if (StringUtil.isNotBlank(xfsc.getStr("ZXBW"))) {
                            //是否包含装饰装修工程
                            jsgcxfsjscxxbV3.setSfbhzxzsgc(1);
                            //装饰装修部位
                            jsgcxfsjscxxbV3.setZxbw(xfsc.getStr("zxbw"));
                            if (StringUtil.isNotBlank(xfsc.getStr("zxmj"))) {
                                //装修面积（平方米）
                                jsgcxfsjscxxbV3.setZxmj(Double.parseDouble(xfsc.getStr("zxmj")));
                            } else {
                                //装修面积（平方米）
                                jsgcxfsjscxxbV3.setZxmj(0.00);
                            }
                            if (StringUtil.isNotBlank(xfsc.getStr("zxszcs"))) {
                                //装修所在层数
                                jsgcxfsjscxxbV3.setZxszcs(xfsc.getStr("zxszcs"));
                            } else {
                                //装修所在层数
                                jsgcxfsjscxxbV3.setZxszcs("无");
                            }
                        } else {
                            //是否包含装饰装修工程
                            jsgcxfsjscxxbV3.setSfbhzxzsgc(0);
                        }

                        //总建筑面积
                        if (StringUtil.isNotBlank(xfsc.getStr("TotalBuildArea"))) {
                            jsgcxfsjscxxbV3.setZjzmj(Double.parseDouble(xfsc.getStr("TotalBuildArea")));
                        }
                        //工程名称
                        jsgcxfsjscxxbV3.setGcmc(gcmc);
                        //工程地址
                        jsgcxfsjscxxbV3.setGcdz(xfsc.getStr("ConstructionSite"));
                        //特殊消防设计
                        if (StringUtil.isNotBlank(xfsc.getStr("tsxfsj"))) {
                            jsgcxfsjscxxbV3.setTsxfsj(Integer.parseInt(xfsc.getStr("tsxfsj")));
                        }
                        //数据有效标识
                        jsgcxfsjscxxbV3.setSjyxbs(1);
                        //是否使用保温材料
                        jsgcxfsjscxxbV3.setSfsybwcl(1);
                        //建筑保温材料类别
                        jsgcxfsjscxxbV3.setBwcllb(xfsc.getStr("cllb"));
                        //保温部位
                        jsgcxfsjscxxbV3.setBwbw("");

                        if (StringUtil.isNotBlank(xfsc.getStr("bwszcs"))) {
                            //保温所在层数
                            jsgcxfsjscxxbV3.setBwszcs(xfsc.getStr("bwszcs"));
                        } else {
                            jsgcxfsjscxxbV3.setBwszcs("无");
                        }

                        //保温部位
                        jsgcxfsjscxxbV3.setBwbw("");
                        if (StringUtil.isNotBlank(xfsc.getStr("bwcl"))) {
                            //保温材料
                            jsgcxfsjscxxbV3.setBwcl(xfsc.getStr("bwcl"));
                        } else {
                            jsgcxfsjscxxbV3.setBwcl("无");
                        }

                        //审批事项实例编码
                        jsgcxfsjscxxbV3.setSpsxslbm(auditProject.getFlowsn());
                        //是否建筑高度大于 250m 的建筑采取加强性消防设计措施
                        if (StringUtil.isNotBlank(xfsc.getStr("jzgddy250mdjzcqjqxxfsjcs"))) {
                            jsgcxfsjscxxbV3.setSfjqxxf(Integer.parseInt(xfsc.getStr("jzgddy250mdjzcqjqxxfsjcs")));
                        }
                        //建设单位项目负责人
                        jsgcxfsjscxxbV3.setJsdwxmfzr(xfsc.getStr("Contact"));
                        //建设单位代码
                        jsgcxfsjscxxbV3.setJsdwdm(xfsc.getStr("jsdwtyxydm"));
                        //建设单位类型
                        jsgcxfsjscxxbV3.setJsdwlx(1);
                        jsgcxfsjscxxbV3.setDfsjzj(dfsjzj);
                        jsgcxfsjscxxbV3Service.insert(jsgcxfsjscxxbV3);
                        log.info(lsh + "=======插入3.0消防数据成功=======");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(lsh + "=======插入3.0消防数据失败=======");
        }
    }


    /**
     * 插入消防备案数据
     */
    public void insertxfba(String subappguid, AuditProject auditProject, String xmdm, String gcdm, String gcmc, String lsh) {
        try {
            if (StringUtil.isNotBlank(subappguid)) {
                AuditSpISubapp subapp = iSubapp.getSubappByGuid(subappguid).getResult();
                Record xfba = iCxBusService.getDzbdDetail("formtable20230113091411", subappguid);
                if (xfba != null) {
                    int count = jsgcxfysbaxxbV3Service.getSpglJsgcxfysbaxxbV3Byflowsn(auditProject.getFlowsn());
                    if (count < 1) {
                        SpglJsgcxfysbaxxbV3 jsgcxfysbaxxbV3 = new SpglJsgcxfysbaxxbV3();
                        String dfsjzj = UUID.randomUUID().toString();
                        jsgcxfysbaxxbV3.setRowguid(dfsjzj);
                        jsgcxfysbaxxbV3.setOperatedate(new Date());
                        jsgcxfysbaxxbV3.setOperateusername("消防同步");

                        if (StringUtil.isNotBlank(xfba.getStr("yyyt"))) {
                            //是否改变用途
                            jsgcxfysbaxxbV3.setSfgbyt(1);
                            //原有用途
                            jsgcxfysbaxxbV3.setYyyt(xfba.getStr("yyyt"));
                        } else {
                            //是否改变用途
                            jsgcxfysbaxxbV3.setSfgbyt(0);
                        }
                        //项目负责人
                        jsgcxfysbaxxbV3.setXmfzr(xfba.getStr("Contact"));
                        //项目代码
                        jsgcxfysbaxxbV3.setXmdm(xmdm);
                        //行政区划代码
                        jsgcxfysbaxxbV3.setXzqhdm("370800");
                        //建设性质
                        if (StringUtil.isNotBlank(xfba.getStr("lb"))) {
                            jsgcxfysbaxxbV3.setJsxz(Integer.parseInt(xfba.getStr("lb")));
                        }
                        //工程投资额
                        if (StringUtil.isNotBlank(xfba.getStr("TotalInvestment"))) {
                            jsgcxfysbaxxbV3.setGctze(Double.parseDouble(xfba.getStr("TotalInvestment")));
                        }

                        //建设单位项目负责人
                        jsgcxfysbaxxbV3.setJsdwxmfzr(xfba.getStr("Contact"));
                        //数据上传状态
                        jsgcxfysbaxxbV3.setSjsczt(0);
                        //建设单位
                        jsgcxfysbaxxbV3.setJsdw(xfba.getStr("ConstructionName"));
                        //工程代码
                        jsgcxfysbaxxbV3.setGcdm(gcdm);
                        //根据是否包含装修部位是否有值判断，填写值为1，否则为0
                        if (StringUtil.isNotBlank(xfba.getStr("ZXBW"))) {
                            //是否包含装饰装修工程
                            jsgcxfysbaxxbV3.setSfbhzxzsgc(1);
                            //装饰装修部位
                            jsgcxfysbaxxbV3.setZxbw(xfba.getStr("zxbw"));
                            if (StringUtil.isNotBlank(xfba.getStr("zxmj"))) {
                                //装修面积（平方米）
                                jsgcxfysbaxxbV3.setZxmj(Double.parseDouble(xfba.getStr("zxmj")));
                            } else {
                                //装修面积（平方米）
                                jsgcxfysbaxxbV3.setZxmj(0.00);
                            }
                            if (StringUtil.isNotBlank(xfba.getStr("zxszcs"))) {
                                //装修所在层数
                                jsgcxfysbaxxbV3.setZxszcs(xfba.getStr("zxszcs"));
                            } else {
                                //装修所在层数
                                jsgcxfysbaxxbV3.setZxszcs("无");
                            }
                        } else {
                            //是否包含装饰装修工程
                            jsgcxfysbaxxbV3.setSfbhzxzsgc(0);
                        }
                        //工程名称
                        jsgcxfysbaxxbV3.setGcmc(gcmc);
                        //工程地址
                        jsgcxfysbaxxbV3.setGcdz(xfba.getStr("gcdz"));
                        //数据有效标识
                        jsgcxfysbaxxbV3.setSjyxbs(1);
                        //建设单位代码
                        jsgcxfysbaxxbV3.setJsdwdm(xfba.getStr("ConstructionUscc"));
                        if (subapp != null) {
                            //申请日期
                            jsgcxfysbaxxbV3.setSqrq(subapp.getCreatedate());
                            //设计单位填表日期
                            jsgcxfysbaxxbV3.setSjdwtbrq(subapp.getCreatedate());
                            //建设单位填表日期
                            jsgcxfysbaxxbV3.setJsdwtbrq(subapp.getCreatedate());
                        } else {
                            //申请日期
                            jsgcxfysbaxxbV3.setSqrq(new Date());
                            //设计单位填表日期
                            jsgcxfysbaxxbV3.setSjdwtbrq(new Date());
                            //建设单位填表日期
                            jsgcxfysbaxxbV3.setJsdwtbrq(new Date());
                        }

                        //联系人
                        jsgcxfysbaxxbV3.setLxr(auditProject.getContactperson());
                        //联系人手机号
                        jsgcxfysbaxxbV3.setLxrsjh(auditProject.getContactphone());

                        //是否使用保温材料
                        jsgcxfysbaxxbV3.setSfsybwcl(1);
                        //建筑保温材料类别
                        jsgcxfysbaxxbV3.setBwcllb(xfba.getStr("cllb"));
                        //保温部位
                        jsgcxfysbaxxbV3.setBwbw("");
                        if (StringUtil.isNotBlank(xfba.getStr("bwszcs"))) {
                            //保温所在层数
                            jsgcxfysbaxxbV3.setBwszcs(xfba.getStr("bwszcs"));
                        } else {
                            jsgcxfysbaxxbV3.setBwszcs("无");
                        }

                        //保温部位
                        jsgcxfysbaxxbV3.setBwbw("");
                        if (StringUtil.isNotBlank(xfba.getStr("bwcl"))) {
                            //保温材料
                            jsgcxfysbaxxbV3.setBwcl(xfba.getStr("bwcl"));
                        } else {
                            jsgcxfysbaxxbV3.setBwcl("无");
                        }
                        //审批事项实例编码
                        jsgcxfysbaxxbV3.setSpsxslbm(auditProject.getFlowsn());
                        //建设单位类型
                        jsgcxfysbaxxbV3.setJsdwlx(1);
                        jsgcxfysbaxxbV3.setDfsjzj(dfsjzj);
                        jsgcxfysbaxxbV3Service.insert(jsgcxfysbaxxbV3);
                        log.info(lsh + "=======插入3.0消防数据成功=======");
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(lsh + "=======插入3.0消防数据失败=======");
        }
    }

}
