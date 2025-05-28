package com.epoint.auditprojectunusual.utils;

import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


public class AuditProjectUnusualUtils {
    private IAuditOrgaWorkingDay auditCenterWorkingDayService = ContainerFactory.getContainInfo()
            .getComponent(IAuditOrgaWorkingDay.class);
    /**
     * 计算暂停时间（工作日）
     * @param unusuals
     * @param centerGuid
     * @return
     */
    public int calculateTotalWorkingDaysPaused(List<AuditProjectUnusual> unusuals, String centerGuid) {
        int totalWorkingDays = 0;
        LocalDateTime currentStartTime = null;
        for (AuditProjectUnusual unusual : unusuals) {
            Instant instant = unusual.getOperatedate().toInstant();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

            if (10 == unusual.getOperatetype()) {
                // 开始暂停
                currentStartTime = localDateTime;
            } else if (11 == unusual.getOperatetype() && currentStartTime != null) {
                // 结束暂停，计算这期间的工作日天数
                Date startDate = Date.from(currentStartTime.atZone(ZoneId.systemDefault()).toInstant());
                Date endDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

                // 调用工作日计算服务，计算两个日期之间的工作日天数
                int workingDays = auditCenterWorkingDayService.GetWorkingDays_Between_From_To(
                        centerGuid, startDate, endDate).getResult();

                totalWorkingDays += workingDays;
                currentStartTime = null;
            }
        }

        return totalWorkingDays;
    }
}
