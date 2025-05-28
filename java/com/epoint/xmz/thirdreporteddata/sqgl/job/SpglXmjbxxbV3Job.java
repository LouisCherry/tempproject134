package com.epoint.xmz.thirdreporteddata.sqgl.job;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmjbxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmjbxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglsplcxxb;
import com.epoint.xmz.thirdreporteddata.sqgl.job.api.ISpglXmjbxxbV3Job;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 同步2.0已推送过，并且3.0库里没有的项目
 * 每次推送500条
 */
public class SpglXmjbxxbV3Job implements Job {

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(this.getClass());

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            log.info("补推3.0项目表服务开始===");
            EpointFrameDsManager.begin(null);
            SyncXmjbxxbV3();
            EpointFrameDsManager.commit();
            log.info("补推3.0项目表服务结束===");
        } catch (Exception e) {
            EpointFrameDsManager.rollback();
            e.printStackTrace();
        } finally {
            EpointFrameDsManager.close();
        }
    }

    public void SyncXmjbxxbV3() {
        ISpglXmjbxxbV3Job xmjbxxbV3JobService = ContainerFactory.getContainInfo().getComponent(ISpglXmjbxxbV3Job.class);
        ISpglXmjbxxbV3 iSpglXmjbxxbV3 = ContainerFactory.getContainInfo().getComponent(ISpglXmjbxxbV3.class);
        IAuditRsItemBaseinfo iAuditRsItemBaseinfo = ContainerFactory.getContainInfo()
                .getComponent(IAuditRsItemBaseinfo.class);
        IAuditSpInstance iauditspinstance = ContainerFactory.getContainInfo().getComponent(IAuditSpInstance.class);
        IAuditSpBusiness iauditspbusiness = ContainerFactory.getContainInfo().getComponent(IAuditSpBusiness.class);
        IAuditOrgaArea iauditorgaarea = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        ISpglsplcxxb iSpglsplcxxb = ContainerFactory.getContainInfo().getComponent(ISpglsplcxxb.class);
        List<AuditRsItemBaseinfo> list = xmjbxxbV3JobService.getAuditRsItemBaseInfoList();
        if (!list.isEmpty()) {
            for (AuditRsItemBaseinfo auditRsItemBaseinfo : list) {
                SpglXmjbxxbV3 spglXmjbxxb = new SpglXmjbxxbV3();
                // 行政区划
                String areacode = "370800";
                // 国泰测试项目不上报
                if (auditRsItemBaseinfo == null || auditRsItemBaseinfo.getItemname().contains("国泰测试")) {
                    return;
                }
                AuditSpInstance auditspinstance = iauditspinstance.getDetailByBIGuid(auditRsItemBaseinfo.getBiguid())
                        .getResult();
                if (auditspinstance == null) {
                    return;
                }
                AuditSpBusiness auditspbusiness = iauditspbusiness.getAuditSpBusinessByRowguid(
                        auditspinstance.getBusinessguid()).getResult();

                if (auditspbusiness == null) {
                    return;
                }

                String sjsczt = "0";
                StringBuilder sbyy = new StringBuilder();// 说明

                String businessareacode = auditspbusiness.getAreacode();
                AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(businessareacode).getResult();
                if (area != null) {
                    // 如果是县级，查找市级主题
                    if (ZwfwConstant.CONSTANT_STR_TWO.equals(area.getCitylevel())) {
                        SqlConditionUtil sqlc = new SqlConditionUtil();
                        sqlc.eq("citylevel", ZwfwConstant.CONSTANT_STR_ONE);
                        // 查找市级辖区
                        AuditOrgaArea sjarea = iauditorgaarea.getAuditArea(sqlc.getMap()).getResult();
                        if (sjarea == null) {
                            sjsczt = "-1";
                            sbyy.append("该主题类型未存在市级主题中！");
                        } else {
                            sqlc.clear();
                            sqlc.eq("splclx", String.valueOf(auditspbusiness.getSplclx()));
                            sqlc.eq("areacode", sjarea.getXiaqucode());
                            List<AuditSpBusiness> listb = iauditspbusiness.getAllAuditSpBusiness(sqlc.getMap()).getResult();
                            if (ValidateUtil.isNotBlankCollection(listb)) {
                                auditspbusiness = listb.get(0);
                                areacode = sjarea.getXiaqucode();
                            } else {
                                sjsczt = "-1";
                                sbyy.append("该主题类型未存在市级主题中！");
                            }
                        }
                    }
                }

                String xmdm = "";
                if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                    AuditRsItemBaseinfo pauditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(
                            auditRsItemBaseinfo.getParentid()).getResult();
                    xmdm = pauditRsItemBaseinfo.getItemcode();
                } else {
                    xmdm = auditRsItemBaseinfo.getItemcode();
                }
                spglXmjbxxb.setDfsjzj(auditRsItemBaseinfo.getRowguid());
                spglXmjbxxb.setOperateusername("补推服务生成");
                spglXmjbxxb.setRowguid(UUID.randomUUID().toString());
                spglXmjbxxb.setXzqhdm(areacode);
                spglXmjbxxb.setXmdm(xmdm);
                spglXmjbxxb.setOperatedate(new Date());
                spglXmjbxxb.setGcfw(auditRsItemBaseinfo.getStr("GCFW"));// 工程范围
                spglXmjbxxb.setQjdgcdm(null);// 前阶段关联工程代码
                spglXmjbxxb.setGcdm(auditRsItemBaseinfo.getItemcode());
                spglXmjbxxb.setSplcbm(auditspbusiness.getRowguid());
                Double verison = iSpglsplcxxb.getMaxSplcbbh(auditspbusiness.getRowguid());
                spglXmjbxxb.setSplcbbh(verison);

                spglXmjbxxb.setXmtzly(auditRsItemBaseinfo.getXmtzly());
                // 0 不在范围内，特殊处理
                if (0 == auditRsItemBaseinfo.getXmtzly()) {
                    if (1 == auditspbusiness.getSplclx() || 2 == auditspbusiness.getSplclx()) {
                        spglXmjbxxb.setXmtzly(1);
                    } else {
                        spglXmjbxxb.setXmtzly(2);

                    }
                }

                if (!isInCode("项目投资来源", spglXmjbxxb.getXmtzly(), true)) {
                    sjsczt = "-1";
                    sbyy.append("项目投资来源的值不在代码项之中！");
                }

                spglXmjbxxb.setTdhqfs(auditRsItemBaseinfo.getTdhqfs());
                if (!isInCode("土地获取方式", auditRsItemBaseinfo.getTdhqfs(), true)) {
                    sjsczt = "-1";
                    sbyy.append("土地获取方式的值不在代码项之中！");
                }
                spglXmjbxxb.setTdsfdsjfa(auditRsItemBaseinfo.getTdsfdsjfa());
                if (!isInCode("土地是否带设计方案", auditRsItemBaseinfo.getTdsfdsjfa(), true)) {
                    sjsczt = "-1";
                    sbyy.append("土地是否带设计方案的值不在代码项之中！");
                }

                //是否完成区域评估
                if (StringUtil.isNotBlank(auditRsItemBaseinfo.getSfwcqypg())) {
                    spglXmjbxxb.setSfwcqypg(auditRsItemBaseinfo.getSfwcqypg());
                } else {
                    spglXmjbxxb.setSfwcqypg(0);
                }

                spglXmjbxxb.setSplclx(auditspbusiness.getSplclx());
                if (!isInCode("审批流程类型", auditspbusiness.getSplclx(), true)) {
                    sjsczt = "-1";
                    sbyy.append("审批流程类型的值不在代码项之中！");
                }

                spglXmjbxxb.setXmmc(auditRsItemBaseinfo.getItemname());
                spglXmjbxxb.setGcfl(31);
                if (StringUtil.isNotBlank(auditRsItemBaseinfo.getStr("LXLX"))) {
                    spglXmjbxxb.setLxlx(Integer.parseInt(auditRsItemBaseinfo.getStr("LXLX")));
                } else {
                    spglXmjbxxb.setLxlx(9);
                }

                spglXmjbxxb.setJsxz(Integer.parseInt(auditRsItemBaseinfo.getConstructionproperty()));
                if (!isInCode("建设性质", auditRsItemBaseinfo.getConstructionproperty(), true)) {
                    sjsczt = "-1";
                    sbyy.append("建设性质的值不在代码项之中！");
                }

                spglXmjbxxb.setXmzjsx(Integer.valueOf(auditRsItemBaseinfo.getXmzjsx()));
                spglXmjbxxb.setGbhydmfbnd("2017");// 国标行业代码发布年代 默认2017
                //子项目可能没有国标行业，统一用主项目
                if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                    AuditRsItemBaseinfo parentinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(auditRsItemBaseinfo.getParentid()).getResult();
                    spglXmjbxxb.setGbhy(parentinfo.getGbhy());
                } else {
                    spglXmjbxxb.setGbhy(auditRsItemBaseinfo.getGbhy());
                }
                spglXmjbxxb.setNkgsj(auditRsItemBaseinfo.getItemstartdate());
                spglXmjbxxb.setNjcsj(auditRsItemBaseinfo.getItemfinishdate());
                checkBack("拟开工时间", auditRsItemBaseinfo.getItemstartdate());
                checkBack("拟建成时间", auditRsItemBaseinfo.getItemfinishdate());

                spglXmjbxxb.setXmsfwqbj(ZwfwConstant.CONSTANT_INT_ZERO); // 项目是否完全办结
                // 默认尚未完全办结
                spglXmjbxxb.setXmwqbjsj(null); // 项目完全办结时间
                // TODO 个性化：使用新字段
                String jsddxzqh = auditRsItemBaseinfo.getStr("jsddxzqh");
                spglXmjbxxb.setJsddxzqh(StringUtil.isBlank(jsddxzqh) ? "370800" : jsddxzqh);

                spglXmjbxxb.setZtze(auditRsItemBaseinfo.getTotalinvest());
                checkBack("总投资额（万元", auditRsItemBaseinfo.getTotalinvest());

                spglXmjbxxb.setJsdd(auditRsItemBaseinfo.getConstructionsite());
                checkBack("建设地点", auditRsItemBaseinfo.getConstructionsite());
                // 3.0新增变更字段开始
                spglXmjbxxb.setXmjwdzb(auditRsItemBaseinfo.get("XMJWDZB"));
                spglXmjbxxb.setJsdwdm(auditRsItemBaseinfo.getItemlegalcertnum());
                spglXmjbxxb.setJsdwlx(auditRsItemBaseinfo.getInt("JSDWLX"));
                spglXmjbxxb.setSfxxgc(auditRsItemBaseinfo.getInt("SFXXGC"));
                spglXmjbxxb.setCd(auditRsItemBaseinfo.getDouble("CD"));
                spglXmjbxxb.setJsdw(auditRsItemBaseinfo.getDepartname());
                spglXmjbxxb.setGchyfl(auditRsItemBaseinfo.getStr("GCHYFL"));
                // 3.0新增变更字段结束
                spglXmjbxxb.setJsgmjnr(auditRsItemBaseinfo.getConstructionscaleanddesc());
                checkBack("建设规模及内容", auditRsItemBaseinfo.getConstructionscaleanddesc());

                spglXmjbxxb.setYdmj(auditRsItemBaseinfo.getLandarea() != null ? auditRsItemBaseinfo.getLandarea() : 0);
                spglXmjbxxb.setJzmj(auditRsItemBaseinfo.getJzmj());
                checkBack("用地面积", auditRsItemBaseinfo.getLandarea());
                checkBack("建筑面积", auditRsItemBaseinfo.getJzmj());

                spglXmjbxxb.setSbsj(auditspinstance.getCreatedate());
                spglXmjbxxb.setSplcbm(auditspbusiness.getRowguid());
                spglXmjbxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                spglXmjbxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                spglXmjbxxb.set("sjsczt", sjsczt);
                spglXmjbxxb.set("sbyy", sbyy.toString());

                //瀚高库新增字段 -- 默认塞值非重点项目
                spglXmjbxxb.set("ZDXMLX", "0");

                // 没向住建发送过发送过信息   新字段issendzj_v3
                if (!ZwfwConstant.CONSTANT_STR_ONE.equals(auditRsItemBaseinfo.getStr("issendzj_v3"))) {
                    iSpglXmjbxxbV3.insert(spglXmjbxxb);
                    // 更新状态为已更新
                    auditRsItemBaseinfo.set("issendzj_v3", ZwfwConstant.CONSTANT_STR_ONE);
                    iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsItemBaseinfo);

                }
            }
        }

    }


    /**
     * @param codename 主项名称
     * @param value    子项值
     * @param a        是否必须
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public boolean isInCode(String codename, Object value, boolean a) {
        if (a) {
            if (value == null) {
                throw new RuntimeException(codename + "的值不能为空！");
            }
        }
        String v = value.toString();
        codename = "国标_" + codename;
        ICodeItemsService icodeitemsservice = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        String s = icodeitemsservice.getItemTextByCodeName(codename, v);
        return StringUtil.isNotBlank(s);
    }

    public void checkBack(String field, Object o) {
        if (o == null) {
            throw new RuntimeException(field + "的值不能为空！");
        }
    }

}
