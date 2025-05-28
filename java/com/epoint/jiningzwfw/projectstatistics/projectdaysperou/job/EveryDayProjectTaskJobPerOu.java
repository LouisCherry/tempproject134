package com.epoint.jiningzwfw.projectstatistics.projectdaysperou.job;

import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.jiningzwfw.projectstatistics.projectdaysperou.api.IAuditOuProjectDaysService;
import com.epoint.jiningzwfw.projectstatistics.projectdaysperou.api.entity.AuditOuProjectDays;
import com.epoint.jiningzwfw.projectstatistics.projectdaysperou.impl.AuditOuProjectDaysService;
import com.epoint.jiningzwfw.projectstatistics.projecttaskr.impl.ProjectTaskRService;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 每日办件事项记录
 */
@DisallowConcurrentExecution
public class EveryDayProjectTaskJobPerOu implements Job {

    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    private IAuditOuProjectDaysService iAuditOuProjectDaysService = ContainerFactory.getContainInfo().getComponent(IAuditOuProjectDaysService.class);
    private IConfigService iConfigService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try {
            log.info("开始执行部门每日办件事项Job ,开始时间：" + System.currentTimeMillis());
            EpointFrameDsManager.begin(null);
            ProjectTaskRService projectTaskRService = new ProjectTaskRService();
            AuditOuProjectDaysService projectDaysService = new AuditOuProjectDaysService();
            List<Record> allProjectsList = new ArrayList<>();
            List<Record> allConsultLists = new ArrayList<>();
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            //1.获取记录的最小时间
            String minDate = "";
            //1.1 查询每日办件事项记录表（AUDIT_OU_PROJECT_DAYS）SDate字段最大时间
            sqlConditionUtil.setSelectFields(" MAX(SDate) as minDate ");
            List<AuditOuProjectDays> list = iAuditOuProjectDaysService.findListByCondition(sqlConditionUtil.getMap()).getResult();
            if (ValidateUtil.isNotBlankCollection(list)) {
                minDate = list.get(0).getStr("minDate");
            }
            if (StringUtil.isBlank(minDate)) {
                //1.2查询audit_project 和 lc_project中applydate最小的时间
                minDate = projectTaskRService.getMinDateFromTables();
            }
            minDate = "2021-06-28 00:00:00";
            //2.获取到当前日期
            Date now = new Date();
            Integer intervals = EpointDateUtil.getIntervalDays(EpointDateUtil.convertString2Date(minDate), now);
            for (int i = 0; i < intervals; i++) {
                //统计各部门办件量 外网办结量 评价量 等数据
                String date = EpointDateUtil.convertDate2String(EpointDateUtil.addDay(EpointDateUtil.convertString2Date(minDate), i), "yyyy-MM-dd");
                String startDate = date + " 00:00:00";
                String endDate = date + " 23:59:59";
                List<Record> projectLists = projectDaysService.getProjectInfoByDate(startDate, endDate);
                if (ValidateUtil.isNotBlankCollection(projectLists)) {
                    allProjectsList.addAll(projectLists);
                }
            }
            //3.存到AUDIT_OU_PROJECT_DAYS
            if (ValidateUtil.isNotBlankCollection(allProjectsList)) {
                for (Record record : allProjectsList) {
                    AuditOuProjectDays auditOuProjectDays = new AuditOuProjectDays();
                    auditOuProjectDays.setRowguid(UUID.randomUUID().toString());
                    auditOuProjectDays.setSdate(record.getStr("SDate"));
                    auditOuProjectDays.setOuguid(record.get("OUGUID"));
                    auditOuProjectDays.setOuname(record.getStr("OUNAME"));
                    auditOuProjectDays.setTocnum(record.getInt("tocnum") == null ? 0 : record.getInt("tocnum"));
                    auditOuProjectDays.setExttocnum(record.getInt("exttocnum")==null?0:record.getInt("exttocnum"));
                    auditOuProjectDays.setEvanum(record.getInt("EvaNum")==null?0:record.getInt("EvaNum"));
                    auditOuProjectDays.setExtnotacceptednum(record.getInt("extnotacceptednum")==null?0:record.getInt("extnotacceptednum"));
                    auditOuProjectDays.setOnnum(record.getInt("onnum")==null?0:record.getInt("onnum"));
                    auditOuProjectDays.setAreacode(record.getStr("AREACODE"));
                    auditOuProjectDays.setExtconnum(0);
                    auditOuProjectDays.setExtconreplynum(0);
                    //4.根据SDate及OUGUID判断是否存在、
                    sqlConditionUtil.clear();
                    if (StringUtil.isNotBlank(auditOuProjectDays.getSdate()) && StringUtil.isNotBlank(auditOuProjectDays.getOuguid())){
                        sqlConditionUtil.eq("SDate",auditOuProjectDays.getSdate());
                        sqlConditionUtil.eq("OUGUID",auditOuProjectDays.getOuguid());
                        List<AuditOuProjectDays> list1 = iAuditOuProjectDaysService.findListByCondition(sqlConditionUtil.getMap()).getResult();
                        if(ValidateUtil.isBlankCollection(list1)){
                            iAuditOuProjectDaysService.insert(auditOuProjectDays);
                        }
                    }

                }
            }
            //4.查询网上咨询量 及网上按期答复量 ExtConNum ExtConReplyNum
            Integer consultDays = Integer.valueOf(iConfigService.getFrameConfigValue("EXT_CONSULT_DAY"));
            Integer mins = consultDays * 1440;
            for (int i = 0; i < intervals; i++) {
                String date = EpointDateUtil.convertDate2String(EpointDateUtil.addDay(EpointDateUtil.convertString2Date(minDate), i), "yyyy-MM-dd");
                String startDate = date + " 00:00:00";
                String endDate = date + " 23:59:59";
                List<Record> recordList = projectDaysService.getInfoByDate(startDate,endDate,mins);
                if (ValidateUtil.isNotBlankCollection(recordList)){
                    allConsultLists.addAll(recordList);
                }
            }
            //4.1将咨询量存在表中 根据SDate和OUGUID组成联合主键判断是否存在，若不存在则新增，如存在则更新
            for (Record allConsult : allConsultLists) {
                sqlConditionUtil.clear();
                sqlConditionUtil.eq("OUGUID",allConsult.getStr("OUGUID"));
                sqlConditionUtil.eq("SDate",allConsult.getStr("SDate"));
                AuditOuProjectDays auditOuProjectDays = iAuditOuProjectDaysService.findListByCondition(sqlConditionUtil.getMap()).getResult().get(0);
                //存在 更新
                if (ValidateUtil.isNotNull(auditOuProjectDays)){
                    auditOuProjectDays.setExtconnum(allConsult.getInt("extconnum")==null?0:allConsult.getInt("extconnum"));
                    auditOuProjectDays.setExtconreplynum(allConsult.getInt("extconreplynum")==null?0:allConsult.getInt("extconreplynum"));
                    iAuditOuProjectDaysService.update(auditOuProjectDays);
                }
                //不存在 插入
                else{
                    AuditOuProjectDays ouProjectDays = new AuditOuProjectDays();
                    ouProjectDays.setRowguid(UUID.randomUUID().toString());
                    ouProjectDays.setSdate(allConsult.getStr("ASKDATE"));
                    ouProjectDays.setOuguid(allConsult.getStr("ouguid"));
                    ouProjectDays.setOuname(allConsult.getStr("OUNAME"));
                    ouProjectDays.setAreacode(allConsult.getStr("areacode"));
                    ouProjectDays.setTocnum(0);
                    ouProjectDays.setExttocnum(0);
                    ouProjectDays.setEvanum(0);
                    ouProjectDays.setExtnotacceptednum(0);
                    ouProjectDays.setOnnum(0);
                    ouProjectDays.setExtconnum(allConsult.getInt("extconnum")==null?0:allConsult.getInt("extconnum"));
                    ouProjectDays.setExtconreplynum(allConsult.getInt("extconreplynum")==null?0:allConsult.getInt("extconreplynum"));
                    iAuditOuProjectDaysService.insert(ouProjectDays);
                }
            }
            log.info("结束执行部门每日办件事项Job ,结束时间：" + System.currentTimeMillis());
            EpointFrameDsManager.commit();
        } 
        catch (Exception e) {
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        } 
        finally {
            EpointFrameDsManager.close();
        }
    }


}
