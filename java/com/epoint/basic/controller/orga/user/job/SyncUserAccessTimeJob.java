package com.epoint.basic.controller.orga.user.job;

import com.epoint.basic.controller.orga.user.api.IJnFrameLogSerivce;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.frame.service.organ.user.entity.ViewFrameUser;
import com.epoint.sso.frame.domain.FrameLoginLog;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;

public class SyncUserAccessTimeJob implements Job {

    transient private static Logger log = LogUtil.getLog(SyncUserAccessTimeJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("=====最后一次登录时间同步服务开始=====");
        SyncUserAccessTime();
        log.info("=====最后一次登录时间同步服务结束=====");
    }

    private void SyncUserAccessTime() {
        IJnFrameLogSerivce frameLogSerivce = ContainerFactory.getContainInfo().getComponent(IJnFrameLogSerivce.class);
        IUserService iUserService = ContainerFactory.getContainInfo().getComponent(IUserService.class);
        //查询用户最后一次登录时间(统一支撑库)
        String days = ConfigUtil.getFrameConfigValue("lastlogindays");
        int day = Integer.parseInt(days);
        //时间不为空，和配置天数比较
        Date before = EpointDateUtil.getDateBefore(new Date(), day);

        //昨天
        Date lasday = EpointDateUtil.getDateBefore(new Date(), 1);
        //查询当天登录过的账号,更新最新登录时间
        List<FrameLoginLog> frameLoginLogs = frameLogSerivce.getLastDayLoginLog(lasday);
        if (EpointCollectionUtils.isNotEmpty(frameLoginLogs)) {
            for (FrameLoginLog frameLoginLog : frameLoginLogs) {
                FrameUser frameUser = iUserService.getUserByUserField("loginid", frameLoginLog.getLoginId());
                frameLogSerivce.updateExtendInfoByUserguid(EpointDateUtil.convertDate2String(frameLoginLog.getAccessTime(), EpointDateUtil.DATE_TIME_FORMAT), frameUser.getUserGuid());
            }
        }
        //查询 accesstime !='day + "天前"' 且 accesstime<= before 的账号，根据账号，确认其有无最近登录，无则更新为 day + "天前"
        List<Record> records = frameLogSerivce.getLoginUserBeforeDate(before, day + "天前");
        if (EpointCollectionUtils.isNotEmpty(records)) {
            for (Record record : records) {
                frameLogSerivce.updateExtendInfoByUserguid(day + "天前", record.get("USERGUID"));
            }
        }
        frameLogSerivce.closeConnectionZg();

    }

}
