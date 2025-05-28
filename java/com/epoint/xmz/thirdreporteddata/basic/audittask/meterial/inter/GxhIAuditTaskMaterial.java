package com.epoint.xmz.thirdreporteddata.basic.audittask.meterial.inter;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.common.service.AuditCommonResult;

import java.util.List;

/**
 * 事项材料表对应的接口
 *
 * @author Administrator
 * @version [版本号, 2017年11月6日]
 */
@Service
public interface GxhIAuditTaskMaterial {


    /**
     * 根据事项唯一标识获取可用材料列表
     *
     * @param taskguid 事项唯一标识
     * @return
     */
    public AuditCommonResult<List<AuditTaskMaterial>> getUsableMaterialListByTaskguid(String taskguid);


}
