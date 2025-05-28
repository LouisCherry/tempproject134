package com.epoint.znsb.znsbclientarea.impl;

import com.epoint.basic.auditqueue.service.AuditQueueBasicService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.znsb.znsbclientarea.domain.ZnsbClientArea;
import com.epoint.znsb.znsbclientarea.inter.IZnsbClientArea;
import com.epoint.znsb.znsbclientarea.service.ZnsbClientAreaService;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ZnsbClientAreaImpl implements IZnsbClientArea {
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public int insert(ZnsbClientArea record) {
        return (new ZnsbClientAreaService()).insert(record);
    }

    public int deleteByGuid(String guid) {
        return (new ZnsbClientAreaService()).deleteByGuid(guid);
    }

    public int deleteByAreacode(String areacode) {
        return (new ZnsbClientAreaService()).deleteByAreacode(areacode);
    }

    public int update(ZnsbClientArea record) {
        return (new ZnsbClientAreaService()).update(record);
    }

    public ZnsbClientArea find(Object primaryKey) {
        return (new ZnsbClientAreaService()).find(primaryKey);
    }

    public int batchupdateClient(String filename, String version, String remark, String attachguid, String clientguid) {
        return (new ZnsbClientAreaService()).batchupdateClient(filename, version, remark, attachguid, clientguid);
    }

    public AuditCommonResult<List<ZnsbClientArea>> getZnsbClientArea(Map<String, String> conditionMap) {
        AuditQueueBasicService<ZnsbClientArea> baseservice = new AuditQueueBasicService();
        AuditCommonResult result = new AuditCommonResult();

        try {
            List<ZnsbClientArea> ZnsbClientArealist = baseservice.selectRecordList(ZnsbClientArea.class, conditionMap);
            result.setResult(ZnsbClientArealist);
        } catch (Exception var5) {
            this.log.error("异常信息:", var5);
            result.setSystemFail(var5.getMessage());
        }

        return result;
    }

    public AuditCommonResult<PageData<ZnsbClientArea>> getZnsbClientAreaByPage(Map<String, String> conditionMap,
            Integer first, Integer pageSize, String sortField, String sortOrder) {
        AuditQueueBasicService<ZnsbClientArea> commonservcie = new AuditQueueBasicService();
        AuditCommonResult result = new AuditCommonResult();

        try {
            PageData<ZnsbClientArea> clientarealist = commonservcie.getRecordPageData(ZnsbClientArea.class,
                    conditionMap, first, pageSize, sortField, sortOrder);
            result.setResult(clientarealist);
        } catch (Exception var9) {
            this.log.error("异常信息:", var9);
            result.setSystemFail(var9.toString());
        }

        return result;
    }
}