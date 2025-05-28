package com.epoint.auditspitem.job;


import com.epoint.auditspitem.api.IAuditSpItemService;
import com.epoint.auditspitem.api.entity.AuditSpItem;
import com.epoint.auditspitem.job.spglxmbqxxbv3.api.ISpglXmbqxxbV3Service;
import com.epoint.auditspitem.job.spglxmbqxxbv3.api.entity.SpglXmbqxxb;
import com.epoint.basic.bizlogic.sysconf.systemparameters.service.FrameConfigService9;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.majoritem.itemplan.api.IItemPlanService;
import com.epoint.majoritem.itemplan.api.entity.ItemPlan;
import com.epoint.majoritem.itemzczctype.api.IItemZczcTypeService;
import com.epoint.majoritem.itemzczctype.api.entity.ItemZczcType;

import com.epoint.majoritem.itmeservelog.api.IItmeServelogService;
import com.epoint.majoritem.itmeservelog.api.entity.ItmeServelog;
import com.epoint.xmz.spglxmfjxxb.api.ISpglXmfjxxbService;
import com.epoint.xmz.spglxmfjxxb.api.entity.SpglXmfjxxb;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 推送重点项目表到本地SPGL XMBOXXBV3表
 *
 * @author lgb
 * @description
 * @date 2022年12月22日09:57:51
 */
@DisallowConcurrentExecution
public class SyncSpglXmbqxxbJob implements Job
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 程序入口
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            log.info("================ 推送重点项目表到本地SPGL XMBOXXBV3表服务启动=====================");
            doService();
            log.info("================= 推送重点项目表到本地SPGL XMBOXXBV3表服务结束====================");
            EpointFrameDsManager.commit();
        }
        catch (DocumentException e) {
            EpointFrameDsManager.rollback();
            e.printStackTrace();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }

    private void doService() throws DocumentException {
        ISpglXmbqxxbV3Service iSpglXmbqxxbV3Service = ContainerFactory.getContainInfo()
                .getComponent(ISpglXmbqxxbV3Service.class);
        IAuditSpItemService auditSpItemService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpItemService.class);
        IItemPlanService iItemPlanService = ContainerFactory.getContainInfo().getComponent(IItemPlanService.class);
        IItemZczcTypeService iItemZczcTypeService = ContainerFactory.getContainInfo()
                .getComponent(IItemZczcTypeService.class);

        ISpglXmfjxxbService iSpglXmfjxxbService = ContainerFactory.getContainInfo()
                .getComponent(ISpglXmfjxxbService.class);
        IItmeServelogService iItmeServelogService = ContainerFactory.getContainInfo()
                .getComponent(IItmeServelogService.class);
        IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        try {

            List<AuditSpItem> Itemlist = auditSpItemService.findsyncist();
            // 项目信息表
            log.info("开始遍历项目信息表");
            // 开启事务
            EpointFrameDsManager.begin(null);

            // 如标签项年份有要求，则填写要求的年份，如无具体要求，则填写当前年份。如“2024”
            String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");

            for (AuditSpItem auditSpItem : Itemlist) {
                SpglXmbqxxb spgl = new SpglXmbqxxb();
                spgl.setOperatedate(new Date());
                spgl.setDfsjzj(auditSpItem.getRowguid());
                // 行政区划代码,默认用市级
                spgl.setXzqhdm("370800");
                spgl.setXmdm(auditSpItem.getItemcode());
                spgl.setBz("");
                spgl.setSjyxbs("1");
                spgl.setSjsczt("0");
                String itemlevel = auditSpItem.getStr("itemlevel");
                if(StringUtil.isNotBlank(itemlevel)){
                    if("00101".equals(itemlevel)){
                        spgl.set("BBRYXM",auditSpItem.getStr("bbryname"));
                        spgl.set("BBRYLXFS",auditSpItem.getStr("bbryphone"));
                        spgl.set("BBRYDW",auditSpItem.getStr("bbrydw"));
                    }
                    else if ("00102".equals(itemlevel)) {
                        spgl.set("BBRYXM",auditSpItem.getStr("shibbryname"));
                        spgl.set("BBRYLXFS",auditSpItem.getStr("shibbryphone"));
                        spgl.set("BBRYDW",auditSpItem.getStr("shibbrydw"));
                    }
                    else if ("00103".equals(itemlevel)) {
                        spgl.set("BBRYXM",auditSpItem.getStr("xianbbryname"));
                        spgl.set("BBRYLXFS",auditSpItem.getStr("xianbbryphone"));
                        spgl.set("BBRYDW",auditSpItem.getStr("xianbbrydw"));
                    }
                }
                String[] itemlevelsplit = itemlevel.split(",");
                for (String level : itemlevelsplit) {
                    spgl.setBqmc("001");
                    spgl.setBqx(level);
                    if ("00101".equals(level)) {
                        spgl.setTjnf(auditSpItem.getStr("itemyear"));
                    }
                    else if ("00102".equals(level)) {
                        spgl.setTjnf(auditSpItem.getStr("shiitemyear"));
                    }
                    else if ("00103".equals(level)) {
                        spgl.setTjnf(auditSpItem.getStr("xianitemyear"));
                    }
                    else {
                        spgl.setTjnf(year);
                    }
                    spgl.setRowguid(UUID.randomUUID().toString());
                    iSpglXmbqxxbV3Service.insert(spgl);
                }
                String itemtype = auditSpItem.getStr("itemtype");
                String[] itemtypesplit = itemtype.split(",");
                for (String type : itemtypesplit) {
                    spgl.setBqmc("002");
                    spgl.setBqx(type);
                    spgl.setTjnf(year);
                    spgl.setRowguid(UUID.randomUUID().toString());
                    iSpglXmbqxxbV3Service.insert(spgl);
                }
                // 项目形象进度
                String itemimageprogress = auditSpItem.getStr("itemimageprogress");
                String[] itemimageprogresssplit = itemimageprogress.split(",");
                for (String type : itemimageprogresssplit) {
                    spgl.setBqmc("005");
                    spgl.setBqx(type);
                    spgl.setTjnf(year);
                    spgl.setRowguid(UUID.randomUUID().toString());
                    iSpglXmbqxxbV3Service.insert(spgl);
                }
                String sql = "  SELECT * from item_plan where itemguid= ? ";
                List<ItemPlan> itemPlanlist = iItemPlanService.findList(sql, auditSpItem.getRowguid());
                for (ItemPlan itemPlan : itemPlanlist) {
                    spgl.setBqmc("003");
                    spgl.setBqx(itemPlan.getItemplan());
                    spgl.setBz(itemPlan.getRemark());
                    spgl.setTjnf(year);
                    spgl.setRowguid(UUID.randomUUID().toString());
                    iSpglXmbqxxbV3Service.insert(spgl);
                }
                String ZczcTypesql = "  SELECT * from item_zczc_type where itemguid= ? ";
                List<ItemZczcType> zczcTypeList = iItemZczcTypeService.findList(ZczcTypesql, auditSpItem.getRowguid());
                for (ItemZczcType zczcType : zczcTypeList) {
                    spgl.setBqmc("004");
                    spgl.setBqx(zczcType.getZczclx());
                    spgl.setBz("");
                    spgl.setTjnf(zczcType.getTjyear());
                    spgl.setRowguid(UUID.randomUUID().toString());
                    iSpglXmbqxxbV3Service.insert(spgl);
                }

                //同步附件信息
                SpglXmfjxxb xmfjxxb = new SpglXmfjxxb();
                xmfjxxb.setOperatedate(new Date());
                xmfjxxb.setDfsjzj(auditSpItem.getRowguid());
                // 行政区划代码,默认用市级
                xmfjxxb.setXzqhdm("370800");
                xmfjxxb.setXmdm(auditSpItem.getItemcode());
                //spgl.setBz("");
                xmfjxxb.setSjyxbs(1);
                xmfjxxb.setSjsczt(0);


                String Zczcservesql = "  SELECT * from itme_servelog where itemguid= ? ";
                List<ItmeServelog> itmeServelogs = iItmeServelogService.findList(Zczcservesql, auditSpItem.getRowguid());
                for (ItmeServelog servelog : itmeServelogs) {

                    if(com.epoint.core.utils.string.StringUtil.isNotBlank(servelog.getSgxcclientguid())){
                        xmfjxxb.setRowguid(UUID.randomUUID().toString());
                        xmfjxxb.setFjid(servelog.getSgxcclientguid());
                        List<FrameAttachInfo> attachInfoListByGuid = attachService.getAttachInfoListByGuid(servelog.getSgxcclientguid());
                        if(EpointCollectionUtils.isNotEmpty(attachInfoListByGuid)){
                            FrameAttachInfo frameAttachInfo = attachInfoListByGuid.get(0);
                            xmfjxxb.setFjmc(frameAttachInfo.getAttachFileName());
                            xmfjxxb.setFjyllj(new FrameConfigService9().getFrameConfigValue("jxsystemurl") +"/rest/attachAction/getContent?isCommondto=true&attachGuid="+frameAttachInfo.getAttachGuid());
                            iSpglXmfjxxbService.insert(xmfjxxb);
                        }

                    }

                    if(com.epoint.core.utils.string.StringUtil.isNotBlank(servelog.getSplctclientguid())){
                        xmfjxxb.setRowguid(UUID.randomUUID().toString());
                        xmfjxxb.setFjid(servelog.getSplctclientguid());
                        List<FrameAttachInfo> attachInfoListByGuid = attachService.getAttachInfoListByGuid(servelog.getSplctclientguid());
                        if(EpointCollectionUtils.isNotEmpty(attachInfoListByGuid)){
                            FrameAttachInfo frameAttachInfo = attachInfoListByGuid.get(0);
                            xmfjxxb.setFjmc(frameAttachInfo.getAttachFileName());
                            xmfjxxb.setFjyllj(new FrameConfigService9().getFrameConfigValue("jxsystemurl") +"/rest/attachAction/getContent?isCommondto=true&attachGuid="+frameAttachInfo.getAttachGuid());
                            iSpglXmfjxxbService.insert(xmfjxxb);
                        }

                    }

                }
                auditSpItem.set("issync", "1");
                auditSpItemService.update(auditSpItem);

            }

            log.debug("同步成功");
            EpointFrameDsManager.commit();

        }
        catch (Exception e) {
            // 回滚事务
            EpointFrameDsManager.rollback();

            e.printStackTrace();
        }
        finally {
            // 关闭数据源
            EpointFrameDsManager.close();
        }
    }

}
