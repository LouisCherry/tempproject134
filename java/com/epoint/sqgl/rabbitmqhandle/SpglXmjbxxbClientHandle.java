package com.epoint.sqgl.rabbitmqhandle;

import com.epoint.auditclient.listener.AuditClientMessageListener;
import com.epoint.auditclient.mqconstant.MQConstant;
import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.auditmqmessage.domain.AuditMqMessage;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.basic.spgl.domain.SpglXmdwxxb;
import com.epoint.basic.spgl.domain.SpglXmjbxxb;
import com.epoint.basic.spgl.domain.SpglXmqqyjxxb;
import com.epoint.basic.spgl.inter.ISpglDfxmsplcxxb;
import com.epoint.basic.spgl.inter.ISpglXmdwxxb;
import com.epoint.basic.spgl.inter.ISpglXmjbxxb;
import com.epoint.basic.spgl.inter.ISpglXmqqyjxxb;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 消费者客户端
 *
 * @author WindowCC
 * @version [版本号, 2018年4月28日]
 */
public class SpglXmjbxxbClientHandle extends AuditClientMessageListener {
    //设置消息类型，判断消息是监听住建系统数据
    public SpglXmjbxxbClientHandle() {
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
        ISpglXmjbxxb iSpglXmjbxxbService = ContainerFactory.getContainInfo().getComponent(ISpglXmjbxxb.class);
        ISpglDfxmsplcxxb ispgldfxmsplcxxb = ContainerFactory.getContainInfo().getComponent(ISpglDfxmsplcxxb.class);
        ISpglXmdwxxb iSpglXmdwxxb = ContainerFactory.getContainInfo().getComponent(ISpglXmdwxxb.class);
        ISpglXmqqyjxxb iSpglXmqqyjxxb = ContainerFactory.getContainInfo().getComponent(ISpglXmqqyjxxb.class);
        IAuditRsItemBaseinfo iAuditRsItemBaseinfo = ContainerFactory.getContainInfo()
                .getComponent(IAuditRsItemBaseinfo.class);
        IAuditSpInstance iauditspinstance = ContainerFactory.getContainInfo().getComponent(IAuditSpInstance.class);
        IAuditSpBusiness iauditspbusiness = ContainerFactory.getContainInfo().getComponent(IAuditSpBusiness.class);
        IAuditOrgaArea iauditorgaarea = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        IParticipantsInfoService iparticipantsinfoservice = ContainerFactory.getContainInfo()
                .getComponent(IParticipantsInfoService.class);
        ISendMQMessage sendMQMessageService = ContainerFactory.getContainInfo().getComponent(ISendMQMessage.class);
        SpglXmjbxxb spglXmjbxxb = new SpglXmjbxxb();
        AuditRsItemBaseinfo auditRsItemBaseinfo = new AuditRsItemBaseinfo();
        //解析mq消息内容
        String[] messageContent = proMessage.getSendmessage().split("@")[1].split("\\.");
        if (messageContent == null || messageContent.length < 1) {
            //system.out.println("mq消息信息不正确！");
            return;
        }
        //项目主键
        String rowGuid = messageContent[0];
        //行政区划
        String areacode = messageContent[1];

        String userareacode = messageContent[1];

        String subappguid = messageContent[2];

        String msg = "";
        for (int i = 0; i < messageContent.length; i++) {
            if (StringUtil.isNotBlank(msg)) {
                msg += "." + messageContent[i];
            } else {
                msg += messageContent[i];
            }
        }

        //发送3.0项目mq
        sendMQMessageService.sendByExchange("exchange_handle", msg, "blspV3.rsitem.1");

        auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(rowGuid).getResult();
        if (auditRsItemBaseinfo == null) {
            return;
        }
        AuditSpInstance auditspinstance = iauditspinstance.getDetailByBIGuid(auditRsItemBaseinfo.getBiguid())
                .getResult();
        if (auditspinstance == null) {
            return;
        }
        AuditSpBusiness auditspbusiness = iauditspbusiness.getAuditSpBusinessByRowguid(auditspinstance.getBusinessguid()).getResult();

        if (auditspbusiness == null) {
            return;
        }

        String sjsczt = "0";
        StringBuilder sbyy = new StringBuilder();//说明

        String businessareacode = auditspbusiness.getAreacode();
        AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(businessareacode).getResult();
        if (area != null) {
            //如果是县级，查找市级主题
            if (ZwfwConstant.CONSTANT_STR_TWO.equals(area.getCitylevel())) {
                SqlConditionUtil sqlc = new SqlConditionUtil();
                sqlc.eq("citylevel", ZwfwConstant.CONSTANT_STR_ONE);
                //查找市级辖区
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
            AuditRsItemBaseinfo pauditRsItemBaseinfo = iAuditRsItemBaseinfo
                    .getAuditRsItemBaseinfoByRowguid(auditRsItemBaseinfo.getParentid()).getResult();
            xmdm = pauditRsItemBaseinfo.getItemcode();
        } else {
            xmdm = auditRsItemBaseinfo.getItemcode();
        }
        spglXmjbxxb.setDfsjzj(auditRsItemBaseinfo.getRowguid());
        spglXmjbxxb.setRowguid(UUID.randomUUID().toString());
        spglXmjbxxb.setOperatedate(new Date());
        spglXmjbxxb.setXzqhdm("370800");
        spglXmjbxxb.setXmdm(xmdm);
        spglXmjbxxb.setGcfw(null);//工程范围
        spglXmjbxxb.setQjdgcdm(null);//前阶段关联工程代码
        spglXmjbxxb.setGcdm(auditRsItemBaseinfo.getItemcode());

        spglXmjbxxb.setXmtzly(auditRsItemBaseinfo.getXmtzly());
        if (!isInCode("项目投资来源", auditRsItemBaseinfo.getXmtzly(), true)) {
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

        if (StringUtil.isNotBlank(auditRsItemBaseinfo.getSfwcqypg())) {
            spglXmjbxxb.setSfwcqypg(auditRsItemBaseinfo.getSfwcqypg());
        } else {
            spglXmjbxxb.setSfwcqypg(0);
        }

        if (!isInCode("是否完成区域评估", auditRsItemBaseinfo.getSfwcqypg(), true)) {
            sjsczt = "-1";
            sbyy.append("是否完成区域评估的值不在代码项之中！");
        }

        spglXmjbxxb.setSplclx(auditspbusiness.getSplclx());
        if (!isInCode("审批流程类型", auditspbusiness.getSplclx(), true)) {
            sjsczt = "-1";
            sbyy.append("审批流程类型的值不在代码项之中！");
        }

        spglXmjbxxb.setXmmc(auditRsItemBaseinfo.getItemname());
        spglXmjbxxb.setGcfl(23);
        if (StringUtil.isNotBlank(auditRsItemBaseinfo.getGcfl())) {
            spglXmjbxxb.setGcfl(Integer.parseInt(auditRsItemBaseinfo.getGcfl()));
        }
        spglXmjbxxb.setLxlx(1);
        spglXmjbxxb.setJsxz(Integer.parseInt(auditRsItemBaseinfo.getConstructionproperty()));
        if (!isInCode("建设性质", auditRsItemBaseinfo.getConstructionproperty(), true)) {
            sjsczt = "-1";
            sbyy.append("建设性质的值不在代码项之中！");
        }

        spglXmjbxxb.setXmzjsx(Integer.valueOf(auditRsItemBaseinfo.getXmzjsx()));
        spglXmjbxxb.setGbhydmfbnd("2017");//国标行业代码发布年代 默认2017
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

        spglXmjbxxb.setXmsfwqbj(ZwfwConstant.CONSTANT_INT_ZERO); //项目是否完全办结 默认尚未完全办结
        spglXmjbxxb.setXmwqbjsj(null); //项目完全办结时间
        spglXmjbxxb.setJsddxzqh(userareacode);//建设地点行政区划  默认当前辖区的区划

        spglXmjbxxb.setZtze(auditRsItemBaseinfo.getTotalinvest());
        checkBack("总投资额（万元", auditRsItemBaseinfo.getTotalinvest());

        spglXmjbxxb.setJsdd(auditRsItemBaseinfo.getConstructionsite());
        checkBack("建设地点", auditRsItemBaseinfo.getConstructionsite());

        spglXmjbxxb.setXmjsddx(null); //项目建设地点X坐标
        spglXmjbxxb.setXmjsddy(null); //项目建设地点y坐标
        spglXmjbxxb.setJsgmjnr(auditRsItemBaseinfo.getConstructionscaleanddesc());
        checkBack("建设规模及内容", auditRsItemBaseinfo.getConstructionscaleanddesc());

        spglXmjbxxb.setYdmj(auditRsItemBaseinfo.getLandarea() != null ? auditRsItemBaseinfo.getLandarea() : 0);
        spglXmjbxxb.setJzmj(auditRsItemBaseinfo.getJzmj());
        checkBack("用地面积", auditRsItemBaseinfo.getLandarea());
        checkBack("建筑面积", auditRsItemBaseinfo.getJzmj());

        spglXmjbxxb.setSbsj(auditspinstance.getCreatedate());
        spglXmjbxxb.setSplcbm(auditspbusiness.getRowguid());
        Double verison = ispgldfxmsplcxxb.getMaxSplcbbh(auditspbusiness.getRowguid());
        spglXmjbxxb.setSplcbbh(verison);
        spglXmjbxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
        spglXmjbxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
        spglXmjbxxb.set("sjsczt", sjsczt);
        spglXmjbxxb.set("sbyy", sbyy.toString());

        //没向住建发送过发送过信息
        if (!ZwfwConstant.CONSTANT_STR_ONE.equals(auditRsItemBaseinfo.getIssendzj())) {
            iSpglXmjbxxbService.insert(spglXmjbxxb);
            //更新状态为已更新
            auditRsItemBaseinfo.setIssendzj(ZwfwConstant.CONSTANT_STR_ONE);
            iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsItemBaseinfo);
        }

        //查询推送过的单位信息
        List<Record> dwInfos = iSpglXmdwxxb
                .getAddedDwInfo(auditspinstance.getAreacode(), auditRsItemBaseinfo.getItemcode()).getResult();

        //项目单位信息
        List<ParticipantsInfo> participantsinfolist = iparticipantsinfoservice
                .listParticipantsInfoBySubappGuid(subappguid);
        String sqlexcute = "select * from participants_info where corptype = ? and itemguid = ?";
        List<ParticipantsInfo> listp = iparticipantsinfoservice.findList(sqlexcute, "31",
                auditRsItemBaseinfo.getRowguid());
        participantsinfolist.addAll(listp);

        //根据统一社会代码确定是否重复上传
        for (ParticipantsInfo participantsInfo : participantsinfolist) {
            //如果新增数据则插入信息
            boolean added = false;
            Integer dwlx = getDwlxByCorptype(participantsInfo.getCorptype());
            if (ValidateUtil.isNotBlankCollection(dwInfos)) {
                for (Record info : dwInfos) {
                    if (participantsInfo.getCorpcode().equals(info.get("dwtyshxydm"))
                            && dwlx.toString().equals(info.getStr("dwlx"))) {
                        added = true;
                    }
                }
            }
            if (added) {
                continue;
            } else {
                Record record = new Record();
                record.set("dwtyshxydm", participantsInfo.getCorpcode());
                record.set("dwlx", getDwlxByCorptype(participantsInfo.getCorptype()));
                dwInfos.add(record);
            }
            SpglXmdwxxb spglXmdwxxb = new SpglXmdwxxb();
            spglXmdwxxb.setDfsjzj(participantsInfo.getRowguid());
            spglXmdwxxb.setRowguid(UUID.randomUUID().toString());
            spglXmdwxxb.setXzqhdm("370800");
            spglXmdwxxb.setXmdm(xmdm);
            spglXmdwxxb.setGcdm(auditRsItemBaseinfo.getItemcode());
            spglXmdwxxb.setDwtyshxydm(participantsInfo.getCorpcode());
            spglXmdwxxb.setDwmc(participantsInfo.getCorpname());
            checkBack("单位名称", participantsInfo.getCorpname());
            checkBack("单位统一社会信用代码", participantsInfo.getCorpcode());
            spglXmdwxxb.setDwlx(dwlx);
            spglXmdwxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
            spglXmdwxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);

            //数据验证,项目代码是否在工程代码
            if (!iSpglXmjbxxbService.isExistGcdm(auditRsItemBaseinfo.getItemcode())) {
                spglXmdwxxb.set("sjsczt", "-1");
                spglXmdwxxb.setSbyy("单位工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
            }
            iSpglXmdwxxb.insert(spglXmdwxxb);
        }

        //查询项目前期意见信息
        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
        sqlConditionUtil.eq("qqyjcode", auditRsItemBaseinfo.getStr("chcode"));
        sqlConditionUtil.eq("sync", "-1");
        List<SpglXmqqyjxxb> list = iSpglXmqqyjxxb.getXmqqyjByCondition(SpglXmqqyjxxb.class, sqlConditionUtil.getMap());
        if (list != null) {
            for (SpglXmqqyjxxb spglXmqqyjxxb : list) {
                spglXmqqyjxxb.setXzqhdm("370800");
                spglXmqqyjxxb.setXmdm(xmdm);
                spglXmqqyjxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                spglXmqqyjxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                spglXmqqyjxxb.set("sync", ZwfwConstant.CONSTANT_INT_ZERO);
                iSpglXmqqyjxxb.update(spglXmqqyjxxb);
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
        if (StringUtil.isBlank(s)) {
            return false;
        } else {
            return true;
        }
    }

    public void checkBack(String field, Object o) {
        if (o == null) {
            throw new RuntimeException(field + "的值不能为空！");
        }
    }

    private Integer getDwlxByCorptype(String corptype) {
        Integer dwlx = 7;
        if ("31".equals(corptype)) {
            dwlx = 1;
        } else if ("2".equals(corptype)) {
            dwlx = 4;
        } else if ("1".equals(corptype)) {
            dwlx = 3;
        } else if ("3".equals(corptype)) {
            dwlx = 2;
        } else if ("4".equals(corptype)) {
            dwlx = 5;
        }
        return dwlx;
    }

}
