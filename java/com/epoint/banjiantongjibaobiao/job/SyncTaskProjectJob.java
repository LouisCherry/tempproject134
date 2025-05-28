package com.epoint.banjiantongjibaobiao.job;

import com.epoint.banjiantongjibaobiao.api.ISyncTaskProjectJobService;
import com.epoint.banjiantongjibaobiao.api.TjfxTaskProject;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.*;

/**
 * 定时统计audit_project中的数据，获取系统参数SyncTjfxDays中设置的开始时间和最大天数同步到tjfx_task_project中
 *
 * @author Epoint
 */

public class SyncTaskProjectJob implements Job {
    private Logger log = Logger.getLogger(this.getClass());
    HashMap<Integer, String> statusMap = new HashMap<>();// 存储状态对应的字段名称

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("---------------------------------------开始执行统计定时任务---------------------------------------");
        run();
        log.info("---------------------------------------结束执行统计定时任务---------------------------------------");
    }

    /**
     * 执行各种统计操作
     */
    public void run() {
        // 执行开始先判断统计表中是否有数据,如果有数据则判断最新的日期是否小于当前日期的前30天
        ISyncTaskProjectJobService syncService = ContainerFactory.getContainInfo().getComponent(ISyncTaskProjectJobService.class);
        // 获取系统参数开始时间以及统计的天数
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        // 获取系统参数中单次job最大同步的天数
        String syncTjfxDays = configService.getFrameConfigValue("SyncTjfxDays");
        setStatusMap();
        Date startDate = null;
        int days = 30;
        if (StringUtil.isNotBlank(syncTjfxDays)) {
            String dateStr = syncTjfxDays.split(";")[0];
            if (StringUtil.isNotBlank(dateStr)) {
                startDate = EpointDateUtil.convertString2Date(dateStr, "yyyy-MM-dd");
            }
            int day = Integer.parseInt(syncTjfxDays.split(";")[1]);  // 统计的天数
            days = (day - 1 >= 0) ? day : days;
        }
        if (startDate == null) {
            // 查询统计表中的记录条数以及最大日期
            Record lastDate = syncService.getLastDate();
            if (lastDate.getLong("counts") == 0) {
                startDate = syncService.getMinDate();
            }
            else {
                // 判断统计表中的最大日期是否是最近一个月内的日期，不是则从最大日期当天开始统计，否则统计最近指定天数的数据
                Date maxDate = lastDate.getDate("maxDate");
                int intervalDays = EpointDateUtil.getIntervalDays(new Date(), maxDate);
                startDate = intervalDays > days ? maxDate : EpointDateUtil.getDateBefore(new Date(), days);
            }
        }
        if (EpointDateUtil.getIntervalDays(new Date(), startDate) < 30 && days > 30) {
            days = 30;
        }
        runJob(startDate, days);

    }

    /**
     * 执行统计任务
     */
    public void runJob(Date startDate, int days) {
        try {
            Date currentDate = startDate;
            ISyncTaskProjectJobService syncService = ContainerFactory.getContainInfo().getComponent(ISyncTaskProjectJobService.class);
            while (true) {
                int i = EpointDateUtil.getIntervalDays(currentDate, startDate);
                int intervalDays = EpointDateUtil.getIntervalDays(new Date(), currentDate);
                if (i > days - 1 || intervalDays < 0) {
                    break;
                }
                log.info("---------------------当前统计日期：" + EpointDateUtil.convertDate2String(currentDate) + "---------------------");
                // 获取指定日期存在办件的部门GUID，
                List<String> ouGuidList = syncService.getOuGuidList(currentDate);
                for (String ouGuid : ouGuidList) {
                    // 获取指定日期指定部门的办件信息列表
                    List<Record> infos = syncService.getTjInfo(currentDate, ouGuid);
                    // 处理数据数据后得到一个map，map的value就是数据库中要插入的值
                    Map<String, TjfxTaskProject> afterHandleMap = handleBanJianList(ouGuid, currentDate, infos);

                    // 开启事务，清除可能已经存在的数据
                    EpointFrameDsManager.begin(null);
                    syncService.deleteExistDate(currentDate, ouGuid);
                    EpointFrameDsManager.commit();
                    // 遍历map，写入数据到统计表中
                    for (Map.Entry<String, TjfxTaskProject> entry : afterHandleMap.entrySet()) {
                        syncService.insertNewRecord(entry.getValue());
                        EpointFrameDsManager.commit();
                    }
                }
                currentDate = EpointDateUtil.getDateAfter(currentDate, 1);
            }
        }
        catch (Exception e) {
            log.error("========== 统计job出现异常 ==========");
            log.error(e.getMessage(), e);
            EpointFrameDsManager.rollback();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }

    /**
     * 处理办件，将查询到的办件列表转换成map，map的值为表中要存储的数据
     *
     * @param banJianList
     * @return
     */
    public Map<String, TjfxTaskProject> handleBanJianList(String ouGuid, Date startDate, List<Record> banJianList) {
        HashMap<String, TjfxTaskProject> resultMap = new HashMap<>();
        for (Record banJian : banJianList) {
            String projectName = banJian.getStr("projectName");  // 板件名称
            String taskGuid = banJian.getStr("taskguid"); // 事项GUID
            String taskType = banJian.getStr("tasktype"); // 事项类型
            int status = banJian.getInt("status"); // 事项类型
            String areaCode = banJian.getStr("areacode");  // 区域代码
            Integer totalCount = banJian.getInt("totalcount"); // 总量
            int drbjl = 0;
            if (StringUtil.isNotBlank(banJian.get("drbjl"))) {
                drbjl = banJian.getInt("drbjl");// 外部导入办件量
            }
            String key = ouGuid + ";" + projectName + ";" + taskGuid + ";" + taskType;
            // 第一次
            if (resultMap.get(key) == null) {
                TjfxTaskProject taskProject = new TjfxTaskProject();
                taskProject.setRowguid(UUID.randomUUID().toString());
                taskProject.setOuguid(ouGuid);
                taskProject.setTaskname(projectName);
                taskProject.setTaskguid(taskGuid);
                taskProject.setTasktype(Integer.valueOf(taskType));
                taskProject.setAreacode(areaCode);
                taskProject.setDate(startDate);
                taskProject.setDrbjl(drbjl);  // 导入办件量
                if (statusMap.get(status) != null) {
                    taskProject.setProjectcount(totalCount);
                    if (status >= 26) {
                        // 设置已接件的数量
                        taskProject.setYjj(totalCount);
                    }
                    if (status >= 30) {
                        // 设置已受理的数量
                        taskProject.setYsl(totalCount);
                    }
                    taskProject.set(statusMap.get(status), totalCount);
                }
                resultMap.put(key, taskProject);
            }
            else {
                TjfxTaskProject tjfxTaskProject = resultMap.get(key);
                if (statusMap.get(status) != null) {
                    tjfxTaskProject.set(statusMap.get(status), totalCount);
                    tjfxTaskProject.set("projectcount", tjfxTaskProject.getInt("projectcount") + totalCount);
                    if (status >= 26) {
                        tjfxTaskProject.set("yjj", tjfxTaskProject.getInt("yjj") + totalCount);
                    }
                    if (status >= 30) {
                        tjfxTaskProject.set("ysl", tjfxTaskProject.getInt("ysl") + totalCount);
                    }
                }
                resultMap.put(key, tjfxTaskProject);
            }
        }
        return resultMap;
    }


    /**
     * 存储指定状态对应的字段名称
     */
    public void setStatusMap() {
        this.statusMap.put(12, "wwsbytj");
        this.statusMap.put(14, "wwsbysth");
        this.statusMap.put(24, "djj");
        this.statusMap.put(26, "yjj");
        this.statusMap.put(28, "dbb");
        this.statusMap.put(30, "ysl");
        this.statusMap.put(40, "spbtg");
        this.statusMap.put(50, "sptg");
        this.statusMap.put(90, "zcbj");
        this.statusMap.put(97, "bysl");
        this.statusMap.put(98, "cxsq");
        this.statusMap.put(99, "yczz");
        this.statusMap.put(37, "ysldbb");
        this.statusMap.put(80, "spz");
        this.statusMap.put(999, "ztz");
    }

}
