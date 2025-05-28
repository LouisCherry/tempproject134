package com.epoint.tongbufw;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.log.LogUtil;

@DisallowConcurrentExecution
public class EpointRsprojectrJob implements Job
{
    transient Logger log = LogUtil.getLog(EpointRsprojectrJob.class);

    /**
        * 程序入口
        */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            //job作业比框架起的早 导致部分接口没有实例化
            Thread.sleep(30000);
            doService();
            EpointFrameDsManager.commit();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }

    private void doService() {

        try {
            EpointRsprojectService service = new EpointRsprojectService();
            List<Record> recordlist = service.getRsproject();
            if (null == recordlist || recordlist.size() == 0) {
                List<Record> list = service.getRsgovaffproject();
                for (Record record : list) {
                    Record record2 = new Record();
                    Record record3 = new Record();
                    Record record4 = new Record();
                    String UNID = record.getStr("UNID");
                    String JBJGID = record.getStr("JBJGID");

                    String PDLABEL = record.getStr("PDLABEL");
                    String PRONAME = record.getStr("PRONAME");
                    String PROTIME = record.getStr("PROTIME");
                    String SLRY = record.getStr("SLRY");
                    String SLSJ = record.getStr("SLSJ");
                    String JSRY = record.getStr("JSRY");
                    String BJSJ = record.getStr("BJSJ");
                    String APPLYTYPE = record.getStr("APPLYTYPE");
                    String CERTID = record.getStr("CERTID");
                    String CERTTYPE = record.getStr("CERTTYPE");
                    String LEGALID = record.getStr("LEGALID");
                    String LEGAL = record.getStr("LEGAL");
                    String PRONUMBER = record.getStr("PRONUMBER");
                    String ADDRESS = record.getStr("ADDRESS");
                    record2.set("UNID", UUID.randomUUID().toString());
                    record2.set("create_date", new Date());
                    record2.set("proId", UNID);
                    record2.set("applytype", APPLYTYPE);
                    record2.set("certid", CERTID);
                    record2.set("certtype", CERTTYPE);
                    record2.set("legalid", LEGALID);
                    record2.set("legal", LEGAL);
                    record2.set("taskName", PDLABEL);
                    record2.set("proName", PRONAME);
                    record2.set("address", ADDRESS);
                    record2.set("areacode", JBJGID.substring(0, 6));
                    record2.set("itemId", "Rs-" + JBJGID);
                    record2.set("pronumber", PRONUMBER);
                    record2.set("proTime", PROTIME);
                    record2.set("synid", "Y");
                    int result = service.insertProject(record2);
                    if (result == 1) {
                        log.info("==============>>插入申报信息成功");
                    }
                    else {
                        log.info("==============>>插入申报信息失败");
                    }
                    record3.set("UNID", UUID.randomUUID().toString());
                    record3.set("proId", UNID);
                    record3.set("dealStep", "受理");
                    record3.set("create_date", new Date());
                    record3.set("dealName", SLRY);
                    record3.set("receTime", SLSJ);
                    record3.set("dealTime", SLSJ);
                    record3.set("dealOpinion", "同意");
                    record3.set("proStatus", "5");
                    record3.set("sysnid", "Y");
                    service.insertPhase(record3);
                    record4.set("UNID", UUID.randomUUID().toString());
                    record4.set("proId", UNID);
                    record4.set("dealStep", "办结");
                    record4.set("create_date", new Date());
                    record4.set("dealName", JSRY);
                    record4.set("receTime", BJSJ);
                    record4.set("dealTime", BJSJ);
                    record4.set("dealOpinion", "同意");
                    record4.set("proStatus", "10");
                    record4.set("sysnid", "Y");
                    service.insertPhase(record4);
                    //更新同步标识
                    service.updategovaSynid(UNID);
                }
            }
            else {
                for (Record record : recordlist) {
                    Record record2 = new Record();
                    Record record3 = new Record();
                    Record record4 = new Record();
                    String JBJGID = record.getStr("JBJGID");
                    String UNID = record.getStr("UNID");
                    String PDLABEL = record.getStr("PDLABEL");
                    String PRONAME = record.getStr("PRONAME");
                    String PROTIME = record.getStr("PROTIME");
                    String SLRY = record.getStr("SLRY");
                    String SLSJ = record.getStr("SLSJ");
                    String JSRY = record.getStr("JSRY");
                    String BJSJ = record.getStr("BJSJ");
                    String APPLYTYPE = record.getStr("APPLYTYPE");
                    String CERTID = record.getStr("CERTID");
                    String CERTTYPE = record.getStr("CERTTYPE");
                    String LEGALID = record.getStr("LEGALID");
                    String LEGAL = record.getStr("LEGAL");
                    String PRONUMBER = record.getStr("PRONUMBER");
                    String ADDRESS = record.getStr("ADDRESS");
                    record2.set("UNID", UUID.randomUUID().toString());
                    record2.set("create_date", new Date());
                    record2.set("proId", UNID);
                    record2.set("applytype", APPLYTYPE);
                    record2.set("certid", CERTID);
                    record2.set("certtype", CERTTYPE);
                    record2.set("legalid", LEGALID);
                    record2.set("legal", LEGAL);
                    record2.set("taskName", PDLABEL);
                    record2.set("proName", PRONAME);
                    record2.set("address", ADDRESS);
                    record2.set("areacode", JBJGID.substring(0, 6));
                    record2.set("itemId", "Rs-" + JBJGID);
                    record2.set("pronumber", PRONUMBER);
                    record2.set("proTime", PROTIME);
                    record2.set("synid", "Y");
                    int result = service.insertProject(record2);
                    if (result == 1) {
                        log.info("==============>>插入申报信息成功");
                    }
                    else {
                        log.info("==============>>插入申报信息失败");
                    }
                    record3.set("UNID", UUID.randomUUID().toString());
                    record3.set("proId", UNID);
                    record3.set("dealStep", "受理");
                    record3.set("create_date", new Date());
                    record3.set("dealName", SLRY);
                    record3.set("receTime", SLSJ);
                    record3.set("dealTime", SLSJ);
                    record3.set("dealOpinion", "同意");
                    record3.set("proStatus", "5");
                    record3.set("sysnid", "Y");
                    service.insertPhase(record3);
                    record4.set("UNID", UUID.randomUUID().toString());
                    record4.set("proId", UNID);
                    record4.set("dealStep", "办结");
                    record4.set("create_date", new Date());
                    record4.set("dealName", JSRY);
                    record4.set("receTime", BJSJ);
                    record4.set("dealTime", BJSJ);
                    record4.set("dealOpinion", "同意");
                    record4.set("proStatus", "10");
                    record4.set("sysnid", "Y");
                    service.insertPhase(record4);
                    //更新同步标识
                    service.updateSynid(UNID);

                }

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
