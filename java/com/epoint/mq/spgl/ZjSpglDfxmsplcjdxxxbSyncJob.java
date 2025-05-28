package com.epoint.mq.spgl;

import com.epoint.audittaskzj.job.AuditTaskZJSynService;
import com.epoint.basic.spgl.domain.SpglDfxmsplcjdsxxxb;
import com.epoint.basic.spgl.domain.SpglFail;
import com.epoint.basic.spgl.inter.ISpglFail;
import com.epoint.common.util.StringUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.container.ContainerFactory;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.UUID;

/**
 * 2.0事项补推服务
 */
@DisallowConcurrentExecution
public class ZjSpglDfxmsplcjdxxxbSyncJob implements Job {
    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            //向前置库同步数据多张表
            syncSpglDfxmsplcjdsxxxb();
            //程序块运行时间
            EpointFrameDsManager.commit();

        } catch (Exception ex) {
            EpointFrameDsManager.rollback();
            ex.printStackTrace();
        } finally {
            EpointFrameDsManager.close();
        }
    }

    private void syncSpglDfxmsplcjdsxxxb() {
        AuditTaskZJSynService service = new AuditTaskZJSynService();
        Entity en = SpglDfxmsplcjdsxxxb.class.getAnnotation(Entity.class);
        ISpglFail ispglfail = ContainerFactory.getContainInfo().getComponent(ISpglFail.class);
        //查询需要同步的数据
        List<SpglDfxmsplcjdsxxxb> list1 = service.selectNeedsyncSpjd(SpglDfxmsplcjdsxxxb.class);
        if (list1 != null) {
            for (SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb : list1) {
                //去除多余字段
                String rowguid = spglDfxmsplcjdsxxxb.getRowguid();
                spglDfxmsplcjdsxxxb.remove("BelongXiaQuCode");
                spglDfxmsplcjdsxxxb.remove("OperateUserName");
                spglDfxmsplcjdsxxxb.remove("OperateDate");
                spglDfxmsplcjdsxxxb.remove("ROW_ID");
                spglDfxmsplcjdsxxxb.remove("YearFlag");
                spglDfxmsplcjdsxxxb.remove("sync");
                try {
                    //截取后四位进行上报
                    if (StringUtil.isNotBlank(spglDfxmsplcjdsxxxb.getSpsxbbh()) && spglDfxmsplcjdsxxxb.getSpsxbbh().toString().length() < 5) {
                        //添加记录
                        service.addRecord(SpglDfxmsplcjdsxxxb.class, spglDfxmsplcjdsxxxb);
                        //更新字段
                        service.updateSync(SpglDfxmsplcjdsxxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_ONE);
                        EpointFrameDsManager.commit();
                    } else {
                        String version = spglDfxmsplcjdsxxxb.getSpsxbbh().toString().substring(spglDfxmsplcjdsxxxb.getSpsxbbh().toString().length() - 3);
                        spglDfxmsplcjdsxxxb.setSpsxbbh(Double.parseDouble(version));
                        //添加记录
                        service.addRecord(SpglDfxmsplcjdsxxxb.class, spglDfxmsplcjdsxxxb);
                        //更新字段
                        service.updateSync(SpglDfxmsplcjdsxxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_ONE);
                        EpointFrameDsManager.commit();
                    }
                } catch (Exception e) {
                    log.info("========Exception信息========" + e.getMessage());
                    //存储错误信息,错误信息唯一标识为rowguid
                    SpglFail sf = new SpglFail();
                    sf.setRowguid(UUID.randomUUID().toString());
                    sf.setTablename(en.table());
                    sf.setLsh(0000L);
                    sf.setNote(e.getMessage());
                    sf.setIdentifyfield(spglDfxmsplcjdsxxxb.getRowguid());
                    sf.setStatus(ZwfwConstant.CONSTANT_STR_TWO);
                    ispglfail.insert(sf);
                }
            }
        }
        EpointFrameDsManager.commit();
    }

}
