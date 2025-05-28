package com.epoint.mq.spgl;

import com.epoint.auditclient.listener.AuditClientMessageListener;
import com.epoint.auditclient.mqconstant.MQConstant;
import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
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
import com.epoint.basic.spgl.domain.SpglXmspsxblxxb;
import com.epoint.basic.spgl.domain.SpglXmspsxblxxxxb;
import com.epoint.basic.spgl.domain.SpglXmspsxzqyjxxb;
import com.epoint.basic.spgl.inter.*;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.security.crypto.MDUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.mq.spgl.api.IJnSpglDfxmsplcjdsxxxb;
import com.epoint.sqgl.common.util.Zjconstant;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.UUID;

/**
 * 消费者客户端
 *
 * @author WindowCC
 * @version [版本号, 2018年4月28日]
 */
public class SpglXmspsxblxxbClientHandle extends AuditClientMessageListener {

    private static Logger log = LogUtil.getLog(SpglXmspsxblxxbClientHandle.class);

    //设置消息类型，判断消息是监听住建系统数据
    public SpglXmspsxblxxbClientHandle() {
        super.setMassagetype(MQConstant.MESSAGETYPE_ZJ);
    }

    /**
     * 办理环节操作逻辑
     *
     * @param proMessage 参数
     * @return
     * @exception/throws
     */
    @SuppressWarnings("deprecation")
    @Override
    public void handleMessage(AuditMqMessage proMessage) throws Exception {

        IAuditProject iauditproject = ContainerFactory.getContainInfo()
                .getComponent(IAuditProject.class);
        IAuditSpBusiness iauditspbusiness = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpBusiness.class);
        IAuditOrgaArea iauditorgaarea = ContainerFactory.getContainInfo()
                .getComponent(IAuditOrgaArea.class);
        IAuditSpISubapp iauditspisubapp = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpISubapp.class);
        IAuditRsItemBaseinfo iauditrsitembaseinfo = ContainerFactory.getContainInfo()
                .getComponent(IAuditRsItemBaseinfo.class);
        ISpglDfxmsplcxxb ispgldfxmsplcxxb = ContainerFactory.getContainInfo()
                .getComponent(ISpglDfxmsplcxxb.class);
        IAuditTask iaudittask = ContainerFactory.getContainInfo()
                .getComponent(IAuditTask.class);
        ISpglXmspsxblxxb ispglxmspsxblxxb = ContainerFactory.getContainInfo()
                .getComponent(ISpglXmspsxblxxb.class);
        ISpglXmspsxblxxxxb ispglxmspsxblxxxxb = ContainerFactory.getContainInfo()
                .getComponent(ISpglXmspsxblxxxxb.class);
        IOuService iouservice = ContainerFactory.getContainInfo()
                .getComponent(IOuService.class);
        IUserService iuserservice = ContainerFactory.getContainInfo().getComponent(IUserService.class);
        IAuditSpIReview iauditspireview = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpIReview.class);
        ISpglXmspsxzqyjxxb ispglxmspsxzqyjxxb = ContainerFactory.getContainInfo()
                .getComponent(ISpglXmspsxzqyjxxb.class);
        ISpglDfxmsplcjdsxxxb ispgldfxmsplcjdsxxxb = ContainerFactory.getContainInfo()
                .getComponent(ISpglDfxmsplcjdsxxxb.class);
        IJnSpglDfxmsplcjdsxxxb jnSpglDfxmsplcjdsxxxb = ContainerFactory.getContainInfo().getComponent(IJnSpglDfxmsplcjdsxxxb.class);
        ISendMQMessage sendMQMessageService = ContainerFactory.getContainInfo().getComponent(ISendMQMessage.class);
        IAuditSpITask iAuditSpITask = ContainerFactory.getContainInfo().getComponent(IAuditSpITask.class);

        if (iauditproject == null || iauditspbusiness == null
                || iauditorgaarea == null || iauditspisubapp == null
                || iauditrsitembaseinfo == null || ispgldfxmsplcxxb == null
                || iaudittask == null || ispglxmspsxblxxb == null
                || ispglxmspsxblxxxxb == null || iouservice == null
                || iuserservice == null || iauditspireview == null
                || ispglxmspsxzqyjxxb == null || ispgldfxmsplcjdsxxxb == null
        ) {
            return;
        }
        log.info("proMessage.getSendmessage()"+proMessage.getSendmessage());
        //解析mq消息内容
        String[] messageContent = proMessage.getSendmessage().split("@")[1].split("\\.");
        if (messageContent == null || messageContent.length < 3) {
            //system.out.println("mq消息信息不正确！");
            return;
        }
        //主题阶段唯一标识
        String subappguid = messageContent[0];
        //辖区code
        String areacode = messageContent[1];
        //项目编码
        String itemcode = messageContent[2];

        String isck = messageContent[3];

        String userguid = messageContent[4];

        String projects = messageContent[5];

        String msg = "";
        for (int i = 0; i < messageContent.length; i++) {
            if (StringUtil.isNotBlank(msg)) {
                msg += "." + messageContent[i];
            } else {
                msg += messageContent[i];
            }
        }

        //发送3.0办件mq
        sendMQMessageService.sendByExchange("exchange_handle", msg, "blspV3.subapp.1");

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
        AuditSpBusiness auditspbusiness = iauditspbusiness.getAuditSpBusinessByRowguid(sub.getBusinessguid()).getResult();
        if (auditspbusiness == null) {
            return;
        }
        String businessareacode = auditspbusiness.getAreacode();
        AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(businessareacode).getResult();
        String sjsczt = "0";
        String sbyy = "";
        if (area != null) {
            //如果是县级，查找市级主题
            if (ZwfwConstant.CONSTANT_STR_TWO.equals(area.getCitylevel())) {
                sqlc.clear();
                sqlc.eq("citylevel", ZwfwConstant.CONSTANT_STR_ONE);
                //查找市级辖区
                AuditOrgaArea sjarea = iauditorgaarea.getAuditArea(sqlc.getMap()).getResult();
                if (sjarea == null) {
                    sjsczt = "-1";
                    sbyy = "该主题类型未存在市级主题中！";
                } else {
                    sqlc.clear();
                    sqlc.eq("splclx", String.valueOf(auditspbusiness.getSplclx()));
                    sqlc.eq("areacode", sjarea.getXiaqucode());
                    sqlc.eq("del", "0");
                    sqlc.eq("businesstype", "1");
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
        //重新设置subapp的bussuid
        sub.setBusinessguid(auditspbusiness.getRowguid());

        AuditRsItemBaseinfo rsitem = iauditrsitembaseinfo.getAuditRsItemBaseinfoByRowguid(sub.getYewuguid()).getResult();
        if (rsitem == null) {
            return;
        }

        Double maxbbh = ispgldfxmsplcxxb.getMaxSplcbbh(sub.getBusinessguid());
        log.info("SpglXmspsxblxxbClientHandle获取最大流程版本号：" + maxbbh + "，主题标识:" + sub.getBusinessguid());
        List<AuditSpIReview> listreview = iauditspireview.getReviewBySubappGuid(subappguid).getResult();
        for (AuditProject auditProject : listproject) {
            //插入批事项办理信息
            audittask = iaudittask.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();
            if (audittask == null) {
                continue;
            }
            SpglXmspsxblxxb spgl = new SpglXmspsxblxxb();
            spgl.setRowguid(UUID.randomUUID().toString());
            spgl.setDfsjzj(auditProject.getRowguid());
            spgl.setXzqhdm("370800");
            spgl.setGcdm(rsitem.getItemcode());
            spgl.setSpsxbm(audittask.getItem_id());
            Zjconstant.dealDataToJs(spgl, audittask.getTask_id());
            //江苏通用设置版本号为一
            String isjs = ConfigUtil.getConfigValue("spgl", "isJs");
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(isjs)) {
                spgl.setSpsxbbh(Double.valueOf(ZwfwConstant.CONSTANT_INT_ONE));
            }
            spgl.setSplcbm(sub.getBusinessguid());
            spgl.setSplcbbh(maxbbh);//设置关联最新的版本号

            spgl.setSpsxslbm(auditProject.getFlowsn());
            spgl.setSpbmbm(iouservice.getOuByOuGuid(audittask.getOuguid()).getOucode());
            spgl.setSpbmmc(Zjconstant.getOunamebyOuguid(audittask.getOuguid()));
            if ("nck".equals(isck)) {
                spgl.setSlfs(4); // 全流程网上申请办理
            } else {
                spgl.setSlfs(1); //窗口受理
            }
            spgl.setGkfs(ZwfwConstant.CONSTANT_INT_ONE);//默认1主动公开

            //20240909 更新为产品设置并联审批实例编码方式(保持主子项的实例编码一致)
//            AuditSpITask auditSpITask = iAuditSpITask.getAuditSpITaskByProjectGuid(auditProject.getRowguid()).getResult();
//            if (StringUtil.isNotBlank(auditSpITask.getBatchguid())) {
//                spgl.setBlspslbm(auditSpITask.getBatchguid());
//            }
            //原设置并联审批实例编码方式
            spgl.setBlspslbm(auditProject.getSubappguid());
            spgl.setSxblsx(audittask.getPromise_day());
            spgl.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
            spgl.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);

            log.info("2.0搜索事项入参：" + spgl.getSplcbbh() + "##" + spgl.getSplcbm() + "##" + spgl.getSpsxbm());

            //重新取逻辑
            String spsxbbh = jnSpglDfxmsplcjdsxxxb.getMaxSpsxbbh(spgl.getSplcbbh(), spgl.getSplcbm(), spgl.getSpsxbm());
            log.info("2.0搜索事项版本号：" + spsxbbh);
            //调整，如果是五位，改为截取后三位
            double version;
            if (StringUtil.isNotBlank(spsxbbh)) {
                if (spsxbbh.length() > 5) {
                    version = Double.parseDouble(spsxbbh.substring(spsxbbh.length() - 5));
                } else {
                    version = Double.parseDouble(spsxbbh);
                }
                spgl.setSpsxbbh(version);
            } else {
                spgl.set("sjsczt", "-1");
                spgl.setSbyy("事项版本在已同步的审批流程阶段事项信息表中无对应，请同步审批流程信息！");
            }

            if (!ZwfwConstant.CONSTANT_STR_ZERO.equals(sjsczt)) {
                spgl.set("sjsczt", sjsczt);
                spgl.setSbyy(spgl.getSbyy() + sbyy);
            }

            ispglxmspsxblxxb.insert(spgl);

            //同送征求信息，新增征求信息，根据部门查询征求
            for (AuditSpIReview auditSpIReview : listreview) {
                if (audittask.getOuguid().equals(auditSpIReview.getOuguid())) {
                    //添加记录
                    SpglXmspsxzqyjxxb spglxmspsxzqyjxxb = new SpglXmspsxzqyjxxb();
                    spglxmspsxzqyjxxb.setRowguid(UUID.randomUUID().toString());
                    spglxmspsxzqyjxxb.setDfsjzj(MDUtils.md5Hex(auditSpIReview.getRowguid() + auditProject.getRowguid()));
                    spglxmspsxzqyjxxb.setXzqhdm("370800");
                    spglxmspsxzqyjxxb.setGcdm(rsitem.getItemcode());
                    spglxmspsxzqyjxxb.setSpsxslbm(auditProject.getFlowsn());
                    spglxmspsxzqyjxxb.setBldwdm(iouservice.getOuByOuGuid(audittask.getOuguid()).getOucode());
                    spglxmspsxzqyjxxb.setBldwmc(Zjconstant.getOunamebyOuguid(audittask.getOuguid()));
                    spglxmspsxzqyjxxb.setFksj(auditSpIReview.getPingshengdate());
                    spglxmspsxzqyjxxb.setBlr(iuserservice.getUserNameByUserGuid(auditSpIReview.getPingshenguserguid()));
                    spglxmspsxzqyjxxb.setFkyj(auditSpIReview.getPingshenopinion());
                    spglxmspsxzqyjxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                    spglxmspsxzqyjxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                    //判断办件信息是否推送成功
                    if (!ispglxmspsxblxxb.isExistFlowsn(auditProject.getFlowsn())) {
                        spglxmspsxzqyjxxb.set("sjsczt", "-1");
                        spglxmspsxzqyjxxb.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                    }
                    ispglxmspsxzqyjxxb.insert(spglxmspsxzqyjxxb);
                }
            }

            //插入接件的办理详细信息表
            SpglXmspsxblxxxxb spgl1 = new SpglXmspsxblxxxxb();
            spgl1.setRowguid(UUID.randomUUID().toString());
            spgl1.setDfsjzj(auditProject.getRowguid());
            spgl1.setXzqhdm("370800");
            spgl1.setGcdm(rsitem.getItemcode());
            spgl1.setSpsxslbm(auditProject.getFlowsn());

            if (StringUtil.isBlank(userguid)) {
                userguid = auditProject.getReceiveuserguid();
            }
            FrameUser frameuser = iuserservice.getUserByUserField("userguid", userguid);
            String ouname = iouservice.getOuNameByUserGuid(userguid);
            spgl1.setBlcs(ouname);
            if (frameuser != null) {
                spgl1.setBlr(frameuser.getDisplayName());
            }
            spgl1.setBlzt(ZwfwConstant.CONSTANT_INT_ONE);
            spgl1.setBlyj("接件");
            spgl1.setBlsj(auditProject.getApplydate());
            spgl1.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
            spgl1.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
            if (!ispglxmspsxblxxb.isExistFlowsn(auditProject.getFlowsn())) {
                spgl1.set("sjsczt", "-1");
                spgl1.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
            }
            //system.out.println("插入ispglxmspsxblxxxxb成功");
            //判断有无存在数据
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.eq("Spsxslbm",auditProject.getFlowsn());
            sqlConditionUtil.eq("Blzt","1");
            sqlConditionUtil.eq("Sjyxbs","1");
            sqlConditionUtil.nq("sjsczt","-1");
            int count = ispglxmspsxblxxxxb.getCountByCondition(sqlConditionUtil.getMap()).getResult();
            if(count==0){
                ispglxmspsxblxxxxb.insert(spgl1);
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

}
