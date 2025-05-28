package com.epoint.watersupply.impl;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WaterSupplyProjectService {

    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 查询供水事项未办结的办件
     */
    public List<AuditProject> getWaterSupplyAuditProjectList() {
        ICommonDao commonDao = CommonDao.getInstance();
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String gsPushTaskItem = configService.getFrameConfigValue("rqPushTaskItem");
        String collect = Arrays.stream(gsPushTaskItem.split(",")).collect(Collectors.joining("','"));
        // 先查询供水在用事项的taskGuid
        String sql = "select rowGuid from audit_task where is_enable=1 AND IS_EDITAFTERIMPORT='1' and ifnull(IS_HISTORY,'0')='0'" +
                " and item_id in('" + collect + "')";
        log.info("查询供水在用事项taskGuid的sql--->" + sql);
        List<AuditTask> list = commonDao.findList(sql, AuditTask.class);
        if(EpointCollectionUtils.isEmpty(list)) {
            log.info("未查询到供水在用事项");
            return null;
        }
        String taskGuids = list.stream().map(item -> item.getRowguid()).collect(Collectors.joining("','"));
        // 再根据taskGuid查询供水的办件
        sql = "select * from audit_project where taskguid in ('" + taskGuids + "') and status < 90";
        log.info("查询供水事项对应办件的sql--->" + sql);
        return commonDao.findList(sql, AuditProject.class);
    }

    /**
     * 查询步骤办结前的数据是否存在
     * @param flowSn 办件流水号
     */
    public Integer getBeforeFinishData(String flowSn) {
        String sql = "select count(*) from spgl_xmspsxblxxxxb where SPSXSLBM = '" + flowSn + "' and (BLZT = 3 or BLZT = 8)";
        log.info("查询步骤办结前的数据是否存在sql--->" + sql);
        ICommonDao commonDao = CommonDao.getInstance();
        return commonDao.queryInt(sql, flowSn);
    }

}
