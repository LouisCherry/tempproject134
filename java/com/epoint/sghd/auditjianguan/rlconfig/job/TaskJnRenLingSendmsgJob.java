package com.epoint.sghd.auditjianguan.rlconfig.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;

/**
 * 未认领的事项办件推送审管互动短信job
 *
 * @version 1.0
 * @Author Ljw
 * @Date 2023/5/18
 */
@DisallowConcurrentExecution
public class TaskJnRenLingSendmsgJob implements Job
{

    private static final Logger logger = Logger.getLogger(TaskJnRenLingSendmsgJob.class);

    private IMessagesCenterService imessageservice = ContainerFactory.getContainInfo()
            .getComponent(IMessagesCenterService.class);
    private IAuditOrgaWorkingDay workingdayService = ContainerFactory.getContainInfo()
            .getComponent(IAuditOrgaWorkingDay.class);
    private IUserService iUserService = ContainerFactory.getContainInfo().getComponent(IUserService.class);
    private IOuService iOuService = ContainerFactory.getContainInfo().getComponent(IOuService.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("推送审管互动短信开始");

        Date nowDate = new Date();
        // 判断当天是否为工作日
        EpointFrameDsManager.begin(null);

        String centerguid = ConfigUtil.getFrameConfigValue("CENTERGUID_370800");
        boolean isWorkingDay = workingdayService.isWorkingDay(centerguid, nowDate).getResult();

        // 系统参数，审管互动认领时间起始
        String sghdstarttime = ConfigUtil.getFrameConfigValue("sghdstarttime");
        // 系统参数，审管互动短信模板
        String sghdsmstemplate = ConfigUtil.getFrameConfigValue("sghdsmstemplate");

        EpointFrameDsManager.close();

        if (!isWorkingDay) {
            // 非工作日结束
            logger.info("非工作日不推送短信");
            logger.info("推送审管互动短信结束");
            return;
        }
        if (StringUtil.isBlank(sghdsmstemplate)) {
            logger.info("审管互动短信模板未配置");
            logger.info("推送审管互动短信结束");
            return;
        }

        ICommonDao commonDao = CommonDao.getInstance();
        commonDao.beginTransaction();

        List<Object> params = new ArrayList<>();
        String fields = " a.jg_ouguid,a.jg_userguid ";
        String sql = "select " + fields
                + " from audit_project p inner join audit_task_jn a on a.task_id = p.task_id and a.is_hz = 1"
                + " where p.status = 90 ";
        sql += " and p.Banjieresult = '40' ";
        // 未认领
        sql += " and not EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '')";
        if (StringUtil.isNotBlank(sghdstarttime)) {
            sql += " and p.applyDate >= ? ";
            params.add(sghdstarttime);
        }
        List<AuditProject> auditProjectList = commonDao.findList(sql, AuditProject.class, params.toArray());

        commonDao.close();
        // 存在数据
        Map<String, String> userguidMap = new HashMap<String, String>();
        if (auditProjectList != null && !auditProjectList.isEmpty()) {
            for (AuditProject auditProject : auditProjectList) {
                String jg_userguid = auditProject.getStr("jg_userguid");
                String jg_ouguid = auditProject.getStr("jg_ouguid");
                userguidMap.put(jg_userguid, jg_ouguid);
            }
        }
        if (userguidMap != null && !userguidMap.isEmpty()) {
            String basicSql = "select count(1) from audit_project p INNER JOIN audit_task_jn a ON a.TASK_ID = p.TASK_ID and a.is_hz = 1 where 1=1 ";
            basicSql += " and p.Banjieresult = '40' and p.status = 90 ";
            basicSql += " and a.jg_ouguid = ? ";
            basicSql += " and a.jg_userguid = ? ";
            if (StringUtil.isNotBlank(sghdstarttime)) {
                basicSql += " and p.applyDate >= '" + sghdstarttime + "'";
            }

            String spxxSql = basicSql;

            // 需认领
            String xrlSql = basicSql;
            xrlSql += " and not EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '')";

            // 当日已认领
            String dryrlSql = basicSql;
            dryrlSql += " and EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and date(r.renlingdate)=date(?) )";

            // 本月累计未认领
            String dywrlSql = basicSql;
            dywrlSql += " and not EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '')";
            dywrlSql += " and date(p.banjiedate) >= date(?)";

            String nowDateStr = EpointDateUtil.convertDate2String(nowDate);
            String beginDateStr = nowDateStr.split("-")[0] + "-" + nowDateStr.split("-")[1] + "-01";
            Date beginDate = EpointDateUtil.convertString2Date(beginDateStr, EpointDateUtil.DATE_FORMAT);

            for (Map.Entry<String, String> entry : userguidMap.entrySet()) {
                String jg_userguid = entry.getKey();
                String jg_ouguid = entry.getValue();
                try {
                    commonDao.beginTransaction();
                    EpointFrameDsManager.begin(null);

                    FrameUser frameUser = iUserService.getUserByUserField("userguid", jg_userguid);
                    if (frameUser == null || StringUtil.isBlank(frameUser.getMobile())) {
                        continue;
                    }
                    String ouname = iOuService.getOuNameByUserGuid(frameUser.getUserGuid());

                    int spxx = commonDao.queryInt(spxxSql, new Object[] {jg_ouguid, jg_userguid });
                    int xrl = commonDao.queryInt(xrlSql, new Object[] {jg_ouguid, jg_userguid });
                    int dryrl = commonDao.queryInt(dryrlSql, new Object[] {jg_ouguid, jg_userguid, nowDate });
                    int dywrl = commonDao.queryInt(dywrlSql, new Object[] {jg_ouguid, jg_userguid, beginDate });

                    String Content = sghdsmstemplate.replace("#spxx#", spxx + "").replace("#xrl#", xrl + "")
                            .replace("#dryrl#", dryrl + "").replace("#dywrl#", dywrl + "");

                    String MessageItemGuid = UUID.randomUUID().toString();
                    int IsSchedule = 0;// 是否定时发送
                    String MessageTarget = frameUser.getMobile();// 电话号码
                    String TargetUser = frameUser.getUserGuid();
                    String TargetDispName = frameUser.getDisplayName();
                    String FromUser = "TaskJnRenLingSendmsgJob";
                    String FromDispName = "审管互动短信";
                    String FromMobile = "";
                    String OuGuid = frameUser.getOuGuid();
                    String BaseOuGuid = ouname;
                    boolean NotAddUserInfo = false;
                    String messageType = "370800";
                    // 发送短信提醒
                    imessageservice.insertSmsMessage(MessageItemGuid, Content, nowDate, IsSchedule, nowDate,
                            MessageTarget, TargetUser, TargetDispName, FromUser, FromDispName, FromMobile, OuGuid,
                            BaseOuGuid, NotAddUserInfo, messageType);

                    commonDao.commitTransaction();
                    EpointFrameDsManager.commit();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    commonDao.rollBackTransaction();
                    EpointFrameDsManager.rollback();
                }
                finally {
                    commonDao.close();
                    EpointFrameDsManager.close();
                }
            }
        }
        logger.info("推送审管互动短信结束");
    }
}
