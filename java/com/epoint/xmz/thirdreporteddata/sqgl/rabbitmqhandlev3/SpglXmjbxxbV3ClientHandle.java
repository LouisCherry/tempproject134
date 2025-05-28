package com.epoint.xmz.thirdreporteddata.sqgl.rabbitmqhandlev3;

import com.epoint.auditclient.listener.AuditClientMessageListener;
import com.epoint.auditclient.mqconstant.MQConstant;
import com.epoint.auditmqmessage.domain.AuditMqMessage;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.spgl.inter.ISpglCommon;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglQypgsxxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglQypgxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmjbxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglQypgsxxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglQypgxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmjbxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglsplcxxb;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgsxxxb.api.ISpglQypgsxxxbService;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgsxxxb.api.entity.SpglQypgsxxxb;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.api.ISpglQypgxxbService;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.api.entity.SpglQypgxxb;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 消费者客户端
 *
 * @author WindowCC
 * @version [版本号, 2018年4月28日]
 */
public class SpglXmjbxxbV3ClientHandle extends AuditClientMessageListener {
    private static Logger log = LogUtil.getLog(SpglXmjbxxbV3ClientHandle.class);

    // 设置消息类型，判断消息是监听住建系统数据
    public SpglXmjbxxbV3ClientHandle() {
        super.setMassagetype(MQConstant.MESSAGETYPE_ZJ);
    }

    /**
     * 办理环节操作逻辑
     *
     * @param proMessage 参数
     * @return
     * @exception/throws
     */
    @Override
    public void handleMessage(AuditMqMessage proMessage) {
        ISpglXmjbxxbV3 iSpglXmjbxxbV3 = ContainerFactory.getContainInfo().getComponent(ISpglXmjbxxbV3.class);
        IAuditRsItemBaseinfo iAuditRsItemBaseinfo = ContainerFactory.getContainInfo()
                .getComponent(IAuditRsItemBaseinfo.class);
        IAuditSpInstance iauditspinstance = ContainerFactory.getContainInfo().getComponent(IAuditSpInstance.class);
        IAuditSpBusiness iauditspbusiness = ContainerFactory.getContainInfo().getComponent(IAuditSpBusiness.class);
        IAuditOrgaArea iauditorgaarea = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        ISpglsplcxxb iSpglsplcxxb = ContainerFactory.getContainInfo().getComponent(ISpglsplcxxb.class);
        ISpglQypgsxxxbService iSpglQypgsxxxbService = ContainerFactory.getContainInfo().getComponent(ISpglQypgsxxxbService.class);
        ISpglQypgxxbService iSpglQypgxxbService = ContainerFactory.getContainInfo().getComponent(ISpglQypgxxbService.class);
        ISpglQypgxxbV3Service iSpglQypgxxbV3Service = ContainerFactory.getContainInfo().getComponent(ISpglQypgxxbV3Service.class);
        ISpglQypgsxxxbV3Service iSpglQypgsxxxbV3Service = ContainerFactory.getContainInfo().getComponent(ISpglQypgsxxxbV3Service.class);
        IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        ISpglCommon iSpglCommon = ContainerFactory.getContainInfo().getComponent(ISpglCommon.class);

        try {
            SpglXmjbxxbV3 spglXmjbxxb = new SpglXmjbxxbV3();
            AuditRsItemBaseinfo auditRsItemBaseinfo = new AuditRsItemBaseinfo();
            // 解析mq消息内容
            String[] messageContent = proMessage.getSendmessage().split("@")[1].split("\\.");
            if (messageContent == null || messageContent.length < 1) {
                log.info("mq消息信息不正确！");
                return;
            }
            // 项目主键
            String rowGuid = messageContent[0];
            // 行政区划
            String areacode = messageContent[1];

            String userareacode = messageContent[1];

            String subappguid = messageContent[2];

            auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(rowGuid).getResult();

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
            String jsdw = "";
            String jsdwdm = "";
            if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                AuditRsItemBaseinfo pauditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(
                        auditRsItemBaseinfo.getParentid()).getResult();
                xmdm = pauditRsItemBaseinfo.getItemcode();
                jsdw = pauditRsItemBaseinfo.getItemlegaldept();
                jsdwdm = pauditRsItemBaseinfo.getItemlegalcertnum();
            } else {
                xmdm = auditRsItemBaseinfo.getItemcode();
                jsdw = auditRsItemBaseinfo.getItemlegaldept();
                jsdwdm = auditRsItemBaseinfo.getItemlegalcertnum();
            }
            spglXmjbxxb.setDfsjzj(auditRsItemBaseinfo.getRowguid());
            spglXmjbxxb.setRowguid(UUID.randomUUID().toString());
            spglXmjbxxb.setXzqhdm("370800");
            spglXmjbxxb.setXmdm(xmdm);
            spglXmjbxxb.setOperatedate(new Date());
            spglXmjbxxb.setOperateusername("mq逻辑生成");
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

            spglXmjbxxb.setSfwcqypg(auditRsItemBaseinfo.getSfwcqypg());
            if (!isInCode("是否完成区域评估", auditRsItemBaseinfo.getSfwcqypg(), true)) {
                sjsczt = "-1";
                sbyy.append("是否完成区域评估的值不在代码项之中！");
            }

            //如果完成区域评估则上报关联的区域评估数据
            if (auditRsItemBaseinfo.getSfwcqypg() == 1) {
                List<SpglQypgxxb> SpglQypgxxblist = iSpglQypgxxbService.getSpglQypgxxbListByitemguid(auditRsItemBaseinfo.getRowguid());
                if (SpglQypgxxblist.isEmpty()) {
                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                        SpglQypgxxblist = iSpglQypgxxbService.getSpglQypgxxbListByitemguid(auditRsItemBaseinfo.getParentid());
                    }
                }
                if (!SpglQypgxxblist.isEmpty()) {
                    for (SpglQypgxxb sqQypgxxbedit : SpglQypgxxblist) {
                        SpglQypgxxbV3 spQypgxxbV3 = new SpglQypgxxbV3();
                        spQypgxxbV3.setRowguid(UUID.randomUUID().toString());
                        spQypgxxbV3.setDfsjzj(sqQypgxxbedit.getRowguid());
                        spQypgxxbV3.setXzqhdm("370800");
                        spQypgxxbV3.setSync(ZwfwConstant.CONSTANT_INT_ZERO);
                        spQypgxxbV3.setQypgdybm(sqQypgxxbedit.getQypgdybm());
                        spQypgxxbV3.setQypgqymc(sqQypgxxbedit.getQypgqymc());
                        spQypgxxbV3.setQypgfwms(sqQypgxxbedit.getQypgfwms());
                        spQypgxxbV3.setQypgfwzbxx(sqQypgxxbedit.getQypgfwzbxx());
                        spQypgxxbV3.setQypgmj(sqQypgxxbedit.getQypgmj());
                        // 查询是否有同步过的数据
                        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                        sqlConditionUtil.eq("dfsjzj", sqQypgxxbedit.getRowguid());
                        sqlConditionUtil.eq("sjyxbs", "1");
                        List<SpglQypgxxbV3> list = iSpglQypgxxbV3Service.getAuditSpDanitemByPage(sqlConditionUtil.getMap(), -1, -1, null, null).getResult().getList();
                        if (EpointCollectionUtils.isNotEmpty(list)) {
                            //若存在则说明【同步过】
                            spQypgxxbV3.set("sjsczt", ZwfwConstant.CONSTANT_INT_ZERO);
                            iSpglCommon.editToPushData(list.get(0), spQypgxxbV3, true);
                        } else {
                            //不存在则说明【未同步过】则直接将数据插入至【区域评估信息上报表(SPGL_QYPGXXB_V3)】
                            spQypgxxbV3.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                            spQypgxxbV3.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                            iSpglQypgxxbV3Service.insert(spQypgxxbV3);
                        }
                        sqQypgxxbedit.setSync(ZwfwConstant.CONSTANT_INT_ONE);
                        iSpglQypgxxbService.update(sqQypgxxbedit);

                        // 处理区域评估事项信息
                        sqlConditionUtil.clear();
                        sqlConditionUtil.eq("qypgdybm", sqQypgxxbedit.getQypgdybm());
                        List<SpglQypgsxxxb> listByMap = iSpglQypgsxxxbService.getListByMap(sqlConditionUtil.getMap());
                        if (EpointCollectionUtils.isNotEmpty(listByMap)) {
                            for (SpglQypgsxxxb spglQypgsxxxb : listByMap) {
                                SpglQypgsxxxbV3 spglQypgsxxxbV3 = new SpglQypgsxxxbV3();
                                spglQypgsxxxbV3.setRowguid(UUID.randomUUID().toString());
                                spglQypgsxxxbV3.setDfsjzj(spglQypgsxxxb.getRowguid());
                                spglQypgsxxxbV3.setXzqhdm("370800");
                                spglQypgsxxxbV3.setQypgdybm(spglQypgsxxxb.getQypgdybm());
                                spglQypgsxxxbV3.setQypgsxbm(spglQypgsxxxb.getQypgsxbm());
                                spglQypgsxxxbV3.setQypgsxmc(spglQypgsxxxb.getQypgsxmc());
                                spglQypgsxxxbV3.setDybzspsxbm(spglQypgsxxxb.getDybzspsxbm());
                                spglQypgsxxxbV3.setQypgcgsxrq(spglQypgsxxxb.getQypgcgsxrq());
                                spglQypgsxxxbV3.setQypgcgjzrq(spglQypgsxxxb.getQypgcgjzrq());
                                spglQypgsxxxbV3.setJhspdfs(spglQypgsxxxb.getJhspdfs());
                                // 查询附件
                                List<FrameAttachInfo> attachInfos = attachService
                                        .getAttachInfoListByGuid(spglQypgsxxxb.getStr("cliengguid"));
                                if (EpointCollectionUtils.isNotEmpty(attachInfos)) {
                                    spglQypgsxxxbV3.setQypgcgcllx(attachInfos.get(0).getContentType());
                                    spglQypgsxxxbV3.setQypgcgclmc(attachInfos.get(0).getAttachFileName());
                                    spglQypgsxxxbV3.setQypgcgfjid(attachInfos.get(0).getAttachGuid());
                                }
                                spglQypgsxxxbV3.setSync(ZwfwConstant.CONSTANT_STR_ZERO);
                                // 查询是否有同步过的数据
                                SpglQypgsxxxbV3 spglQypgsxxxbV3edit = iSpglQypgsxxxbV3Service.getSpglQypgsxxxbByDfsjzj(spglQypgsxxxb.getRowguid());
                                if (spglQypgsxxxbV3edit != null) {
                                    // 若存在则说明【同步过】
                                    iSpglCommon.editToPushData(spglQypgsxxxbV3edit, spglQypgsxxxbV3);
                                } else {
                                    // 不存在则说明【未同步过】则直接将数据插入至【区域评估事项信息上报表(SPGL_QYPGSXXXB_V3)】
                                    spglQypgsxxxbV3.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                                    spglQypgsxxxbV3.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                    iSpglQypgsxxxbV3Service.insert(spglQypgsxxxbV3);
                                }
                                spglQypgsxxxb.setSync(ZwfwConstant.CONSTANT_STR_ONE);
                                iSpglQypgsxxxbService.update(spglQypgsxxxb);

                            }
                        }
                    }
                }
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
                spglXmjbxxb.setLxlx(1);
            }
            if (StringUtil.isNotBlank(auditRsItemBaseinfo.getConstructionproperty())) {
                spglXmjbxxb.setJsxz(Integer.parseInt(auditRsItemBaseinfo.getConstructionproperty()));
            }
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
            // 个性化：使用新字段
            String jsddxzqh = auditRsItemBaseinfo.getStr("jsddxzqh");
            spglXmjbxxb.setJsddxzqh(StringUtil.isBlank(jsddxzqh) ? userareacode : jsddxzqh);

            spglXmjbxxb.setZtze(auditRsItemBaseinfo.getTotalinvest());
            checkBack("总投资额（万元", auditRsItemBaseinfo.getTotalinvest());

            spglXmjbxxb.setJsdd(auditRsItemBaseinfo.getConstructionsite());
            checkBack("建设地点", auditRsItemBaseinfo.getConstructionsite());
            // 3.0新增变更字段开始
            spglXmjbxxb.setXmjwdzb(auditRsItemBaseinfo.get("XMJWDZB"));
            spglXmjbxxb.setJsdwdm(jsdwdm);
            spglXmjbxxb.setJsdw(jsdw);
            //spglXmjbxxb.setJsdwlx(auditRsItemBaseinfo.getInt("JSDWLX"));
            spglXmjbxxb.setJsdwlx(1);
            spglXmjbxxb.setSfxxgc(auditRsItemBaseinfo.getInt("SFXXGC"));
            spglXmjbxxb.setCd(auditRsItemBaseinfo.getDouble("CD"));
            spglXmjbxxb.setGchyfl(auditRsItemBaseinfo.getStr("gchyfl"));
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
            if (StringUtil.isNotBlank(auditRsItemBaseinfo.getStr("zdxmlx"))) {
                spglXmjbxxb.set("zdxmlx", auditRsItemBaseinfo.getStr("zdxmlx"));
            } else {
                spglXmjbxxb.set("zdxmlx", "0");
            }

            // 没向住建发送过发送过信息   新字段issendzj_v3
            if (!ZwfwConstant.CONSTANT_STR_ONE.equals(auditRsItemBaseinfo.getStr("issendzj_v3"))) {
                iSpglXmjbxxbV3.insert(spglXmjbxxb);
                // 更新状态为已更新
                auditRsItemBaseinfo.set("issendzj_v3", ZwfwConstant.CONSTANT_STR_ONE);
                iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsItemBaseinfo);
            } else {
                int count = iSpglXmjbxxbV3.getCountByGcdm(auditRsItemBaseinfo.getItemcode());
                if (count == 0) {
                    iSpglXmjbxxbV3.insert(spglXmjbxxb);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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
