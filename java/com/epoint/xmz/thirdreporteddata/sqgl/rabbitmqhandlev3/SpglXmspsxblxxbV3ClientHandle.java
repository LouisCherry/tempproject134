package com.epoint.xmz.thirdreporteddata.sqgl.rabbitmqhandlev3;

import com.epoint.auditclient.listener.AuditClientMessageListener;
import com.epoint.auditclient.mqconstant.MQConstant;
import com.epoint.auditmqmessage.domain.AuditMqMessage;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspireview.domain.AuditSpIReview;
import com.epoint.basic.auditsp.auditspireview.inter.IAuditSpIReview;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.security.crypto.MDUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.mq.spgl.api.IJnSpglDfxmsplcjdsxxxb;
import com.epoint.sqgl.common.util.Zjconstant;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmspsxblxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmspsxblxxxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmspsxzqyjxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.*;
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
public class SpglXmspsxblxxbV3ClientHandle extends AuditClientMessageListener {
    private static Logger log = LogUtil.getLog(SpglXmspsxblxxbV3ClientHandle.class);

    // 设置消息类型，判断消息是监听住建系统数据
    public SpglXmspsxblxxbV3ClientHandle() {
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
    public void handleMessage(AuditMqMessage proMessage) throws Exception {

        IAuditProject iauditproject = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IAuditSpBusiness iauditspbusiness = ContainerFactory.getContainInfo().getComponent(IAuditSpBusiness.class);
        IAuditOrgaArea iauditorgaarea = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        IAuditSpISubapp iauditspisubapp = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);
        IAuditRsItemBaseinfo iauditrsitembaseinfo = ContainerFactory.getContainInfo()
                .getComponent(IAuditRsItemBaseinfo.class);
        IAuditTask iaudittask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        ISpglXmspsxblxxbV3 ispglxmspsxblxxbV3 = ContainerFactory.getContainInfo()
                .getComponent(ISpglXmspsxblxxbV3.class);
        IOuService iouservice = ContainerFactory.getContainInfo().getComponent(IOuService.class);
        IUserService iuserservice = ContainerFactory.getContainInfo().getComponent(IUserService.class);
        IAuditSpIReview iauditspireview = ContainerFactory.getContainInfo().getComponent(IAuditSpIReview.class);
        IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskExtension.class);
        IConfigService frameConfigService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        IAuditSpITask auditSpITaskService = ContainerFactory.getContainInfo().getComponent(IAuditSpITask.class);
        ISpglXmspsxzqyjxxbV3 iSpglXmspsxzqyjxxbV3 = ContainerFactory.getContainInfo()
                .getComponent(ISpglXmspsxzqyjxxbV3.class);
        ISpglXmspsxblxxxxbV3 iSpglXmspsxblxxxxbV3 = ContainerFactory.getContainInfo()
                .getComponent(ISpglXmspsxblxxxxbV3.class);
        ISpglsplcxxb iSpglsplcxxb = ContainerFactory.getContainInfo().getComponent(ISpglsplcxxb.class);
        ISpglsplcjdsxxxb iSpglsplcjdsxxxb = ContainerFactory.getContainInfo().getComponent(ISpglsplcjdsxxxb.class);
        ICodeItemsService codeItemsService = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        IOuService ouService = ContainerFactory.getContainInfo().getComponent(IOuService.class);
        IJnSpglDfxmsplcjdsxxxb jnSpglDfxmsplcjdsxxxb = ContainerFactory.getContainInfo().getComponent(IJnSpglDfxmsplcjdsxxxb.class);

        try {
            log.info(" proMessage.getSendmessage():"+ proMessage.getSendmessage());
            // 解析mq消息内容
            String[] messageContent = proMessage.getSendmessage().split("@")[1].split("\\.");
            if (messageContent == null || messageContent.length < 3) {
                log.info("mq消息信息不正确！");
                return;
            }
            // 主题阶段唯一标识
            String subappguid = messageContent[0];
            // 辖区code
            String areacode = messageContent[1];
            // 项目编码
            String itemcode = messageContent[2];

            String isck = messageContent[3];

            String userguid = messageContent[4];

            String projects = messageContent[5];
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("subappguid", subappguid);

            if(StringUtil.isNotBlank(projects)){
                log.info("projects:"+projects);
                if(projects.startsWith("'")){
                    projects = projects.replace("'","");
                }
                String[] rowguids = projects.split(",");
                sqlc.in("rowguid", StringUtil.joinSql(rowguids));
            }else{
                log.info("rowguid:"+proMessage.getSendmessage());
            }
            List<AuditProject> listproject = iauditproject.getAuditProjectListByCondition(sqlc.getMap()).getResult();
            AuditTask audittask;
            if (listproject == null) {
                return;
            }
            AuditSpISubapp sub = iauditspisubapp.getSubappByGuid(subappguid).getResult();
            if (sub == null) {
                return;
            }
            AuditSpBusiness auditspbusiness = iauditspbusiness.getAuditSpBusinessByRowguid(sub.getBusinessguid())
                    .getResult();
            if (auditspbusiness == null) {
                return;
            }

            String businessareacode = auditspbusiness.getAreacode();
            AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(businessareacode).getResult();
            String sjsczt = "0";
            String sbyy = "";
            if (area != null) {
                // 如果是县级，查找市级主题
                if (ZwfwConstant.CONSTANT_STR_TWO.equals(area.getCitylevel())) {
                    sqlc.clear();
                    sqlc.eq("citylevel", ZwfwConstant.CONSTANT_STR_ONE);
                    // 查找市级辖区
                    AuditOrgaArea sjarea = iauditorgaarea.getAuditArea(sqlc.getMap()).getResult();
                    if (sjarea == null) {
                        sjsczt = "-1";
                        sbyy = "该主题类型未存在市级主题中！";
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
                            sbyy = "该主题类型未存在市级主题中！";
                        }
                    }
                }
            }
            // 重新设置subapp的bussuid
            sub.setBusinessguid(auditspbusiness.getRowguid());

            AuditRsItemBaseinfo rsitem = iauditrsitembaseinfo.getAuditRsItemBaseinfoByRowguid(sub.getYewuguid())
                    .getResult();
            if (rsitem == null) {
                return;
            }

            Double maxbbh = iSpglsplcxxb.getMaxSplcbbh(sub.getBusinessguid());
            List<AuditSpIReview> listreview = iauditspireview.getReviewBySubappGuid(subappguid).getResult();
            Date date = new Date();
            for (AuditProject auditProject : listproject) {
                // 插入批事项办理信息
                audittask = iaudittask.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();
                if (audittask == null) {
                    continue;
                }
                AuditTaskExtension auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(
                        auditProject.getTaskguid(), true).getResult();
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskExtension.getIszijianxitong().toString())) {
                    // 如果是自建系统办件、根据系统参数判断是否上报自建系统办件
                    String notreport = frameConfigService.getFrameConfigValue("AS_NOTREPORTED_ZJ");
                    if (StringUtil.isNotBlank(notreport) && ZwfwConstant.CONSTANT_STR_ONE.equals(notreport)) {
                        continue;
                    }
                }
                //查重复
                SpglXmspsxblxxbV3 spgl = ispglxmspsxblxxbV3.getSpglXmspsxblxxbBySlbm(auditProject.getRowguid());
                if(spgl==null){
                    spgl = new SpglXmspsxblxxbV3();
                    spgl.setRowguid(UUID.randomUUID().toString());
                    spgl.setDfsjzj(auditProject.getRowguid());
                    spgl.setXzqhdm("370800");
                    spgl.setGcdm(rsitem.getItemcode());
                    spgl.setSpsxbm(audittask.getItem_id());
                    Zjconstant.dealDataToJs(spgl, audittask.getTask_id());

                    spgl.setSplcbm(sub.getBusinessguid());
                    spgl.setSplcbbh(maxbbh);// 设置关联最新的版本号

                    log.info("3.0搜索事项入参：" + spgl.getSplcbbh() + "##" + spgl.getSplcbm() + "##" + spgl.getSpsxbm());

                    //重新取事项版本
                    String spsxbbh = jnSpglDfxmsplcjdsxxxb.getMaxSpsxbbhV3(spgl.getSplcbbh(), spgl.getSplcbm(), spgl.getSpsxbm());
                    log.info("3.0搜索事项版本号：" + spsxbbh);
                    if (StringUtil.isNotBlank(spsxbbh)) {
                        spgl.setSpsxbbh(Double.valueOf(spsxbbh));
                    } else {
                        spgl.set("sjsczt", "-1");
                        spgl.setSbyy("事项版本在已同步的审批流程阶段事项信息表中无对应，请同步审批流程信息！");
                    }

                    spgl.setSpsxslbm(auditProject.getFlowsn());
                    spgl.setSpbmbm(iouservice.getOuByOuGuid(audittask.getOuguid()).getOucode());
                    spgl.setSpbmmc(Zjconstant.getOunamebyOuguid(audittask.getOuguid()));
                    if ("nck".equals(isck)) {
                        spgl.setSlfs(4); // 全流程网上申请办理
                    } else {
                        spgl.setSlfs(1); // 窗口受理
                    }
                    spgl.setGkfs(ZwfwConstant.CONSTANT_INT_ONE);// 默认1主动公开

                    // 根据batchguid去填并联审批实例标识
                    AuditSpITask auditspitask = auditSpITaskService.getAuditSpITaskByProjectGuid(auditProject.getRowguid())
                            .getResult();
                    if (StringUtil.isNotBlank(auditspitask.getBatchguid())) {
                        spgl.setBlspslbm(auditspitask.getBatchguid());
                    }else{
                        spgl.setBlspslbm(auditProject.getSubappguid());
                    }
                    spgl.setSxblsx(audittask.getPromise_day());
                    spgl.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                    spgl.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);

                    // 数据验证，查询主题是否推送成功，事项版本号和流程版本号，流程编码是否能获取数据
                    if (!iSpglsplcjdsxxxb.isExistSplcSx(spgl.getSplcbbh(), spgl.getSplcbm(), spgl.getSpsxbbh(),
                            spgl.getSpsxbm())) {
                        spgl.set("sjsczt", "-1");
                        spgl.setSbyy("事项版本在已同步的审批流程阶段事项信息表中无对应，请同步审批流程信息！");
                    }
                    if (!ZwfwConstant.CONSTANT_STR_ZERO.equals(sjsczt)) {
                        spgl.set("sjsczt", sjsczt);
                        spgl.setSbyy(spgl.getSbyy() + sbyy);
                    }
                    spgl.setOperatedate(date);
                    spgl.setOperateusername("mq生成数据");
                    // 3.0新增变更字段
                    spgl.setLxr(auditProject.getContactperson());
                    spgl.setLxrsjh(auditProject.getContactphone());
                    spgl.setYwqx(audittask.getInt("YWQX"));
                    // 3.0新增变更字段结束
                    ispglxmspsxblxxbV3.insert(spgl);
                }


                // 同送征求信息，新增征求信息，根据部门查询征求
                for (AuditSpIReview auditSpIReview : listreview) {
                    if (audittask.getOuguid().equals(auditSpIReview.getOuguid())) {
                        // 添加记录
                        SpglXmspsxzqyjxxbV3 spglxmspsxzqyjxxb = new SpglXmspsxzqyjxxbV3();
                        spglxmspsxzqyjxxb.setRowguid(UUID.randomUUID().toString());
                        spglxmspsxzqyjxxb.setDfsjzj(
                                MDUtils.md5Hex(auditSpIReview.getRowguid() + auditProject.getRowguid()));
                        spglxmspsxzqyjxxb.setXzqhdm("370800");
                        spglxmspsxzqyjxxb.setGcdm(rsitem.getItemcode());
                        spglxmspsxzqyjxxb.setSpsxslbm(auditProject.getFlowsn());
                        spglxmspsxzqyjxxb.setBldwdm(iouservice.getOuByOuGuid(audittask.getOuguid()).getOucode());
                        spglxmspsxzqyjxxb.setBldwmc(Zjconstant.getOunamebyOuguid(audittask.getOuguid()));
                        spglxmspsxzqyjxxb.setFksj(auditSpIReview.getPingshengdate());
                        spglxmspsxzqyjxxb.setBlr(
                                iuserservice.getUserNameByUserGuid(auditSpIReview.getPingshenguserguid()));
                        spglxmspsxzqyjxxb.setFkyj(auditSpIReview.getPingshenopinion());
                        spglxmspsxzqyjxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                        spglxmspsxzqyjxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                        // 判断办件信息是否推送成功
                        if (!ispglxmspsxblxxbV3.isExistFlowsn(auditProject.getFlowsn())) {
                            spglxmspsxzqyjxxb.set("sjsczt", "-1");
                            spglxmspsxzqyjxxb.setSbyy(
                                    "审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                        }
                        iSpglXmspsxzqyjxxbV3.insert(spglxmspsxzqyjxxb);
                    }
                }

                // 插入接件的办理详细信息表
                SpglXmspsxblxxxxbV3 spgl1 = new SpglXmspsxblxxxxbV3();
                spgl1.setRowguid(UUID.randomUUID().toString());
                spgl1.setDfsjzj(auditProject.getRowguid());
                spgl1.setXzqhdm("370800");
                spgl1.setGcdm(rsitem.getItemcode());
                spgl1.setSpsxslbm(auditProject.getFlowsn());

                FrameOu ou = ouService.getOuByOuGuid(auditProject.getOuguid());
                if (ou != null) {
                    spgl1.setDwmc(ou.getOuname());
                } else {
                    spgl1.setDwmc("错误数据");
                    spgl1.set("sjsczt", "-1");
                    spgl1.setSbyy("单位名称校验有误！");
                }

                FrameOuExtendInfo raExtendInfo = ouService.getFrameOuExtendInfo(auditProject.getOuguid());
                if (raExtendInfo != null) {
                    spgl1.setDwtyshxydm(raExtendInfo.getStr("ORGCODE"));
                } else {
                    spgl1.set("sjsczt", "-1");
                    spgl1.setSbyy("单位统一社会信用代码校验有误！");
                }
                if (auditTaskExtension != null) {
                    String SYSTEM_NAME = codeItemsService.getItemTextByCodeName("办理系统",
                            auditTaskExtension.getStr("handle_system"));
                    if (StringUtil.isNotBlank(SYSTEM_NAME)) {
                        spgl1.setSjly(SYSTEM_NAME);
                    } else {
                        //塞入自己系统默认值
                        spgl1.setSjly("济宁市工程建设项目网上申报系统");
                    }
                }

                if (StringUtil.isBlank(userguid)) {
                    userguid = auditProject.getReceiveuserguid();
                }
                FrameUser frameuser = iuserservice.getUserByUserField("userguid", userguid);
                spgl1.setBlcs(Zjconstant.getOunamebyuser(frameuser));
                if (frameuser != null) {
                    spgl1.setBlr(frameuser.getDisplayName());
                }
                spgl1.setBlzt(ZwfwConstant.CONSTANT_INT_ONE);
                spgl1.setBlyj(Zjconstant.SPGL_JJ);
                spgl1.setBlsj(auditProject.getApplydate());
                spgl1.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                spgl1.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                spgl1.setOperatedate(date);
                spgl.setOperateusername("SpglXmspsxblxxbV3ClientHandle");
                if (!ispglxmspsxblxxbV3.isExistFlowsn(auditProject.getFlowsn())) {
                    spgl1.set("sjsczt", "-1");
                    spgl1.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                }
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("Spsxslbm",auditProject.getFlowsn());
                sqlConditionUtil.eq("Blzt","1");
                sqlConditionUtil.eq("Sjyxbs","1");
                sqlConditionUtil.nq("sjsczt","-1");
                int count = iSpglXmspsxblxxxxbV3.getCountByCondition(sqlConditionUtil.getMap()).getResult();
                if(count==0){
                    iSpglXmspsxblxxxxbV3.insert(spgl1);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
