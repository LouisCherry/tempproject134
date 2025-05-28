package com.epoint.auditsp.yqxm.impl;

import com.epoint.auditsp.yqxm.api.ICrawlDataDY;
import com.epoint.auditsp.yqxm.api.entity.StSpglGcJdXxb;
import com.epoint.auditsp.yqxm.api.entity.StSpglXmSpsxblxxb;
import com.epoint.auditsp.yqxm.api.entity.StSpglXmjbxxb;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.cs.auditepidemiclog.api.entity.AuditEpidemicLog;
import com.epoint.cs.auditepidemiclog.impl.AuditEpidemicLogService;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created on 2022/6/7.
 *
 * @author ${阳佳}
 */
@Component
@Service
public class CrawlDataDYImpl implements ICrawlDataDY {
    @Override
    public void pushDataToStTables(String areacode) {
        new CrawldataDYService().CrawldataStart(areacode);
    }

    @Override
    public List<StSpglXmjbxxb> getEachAreaData() {
        return new CrawldataDYService().getEachAreaData();
    }

    @Override
    public List<StSpglXmjbxxb> getEachAreaYuQiPorjects(String areaCode, int first, int pageSize) {
        return new CrawldataDYService().getEachAreaYuQiPorjects(areaCode,first,pageSize);
    }

    @Override
    public AuditCommonResult<PageData<StSpglXmjbxxb>> getListByPage(Map<String, String> conditionMap, int first, int pageSize, String sortField, String sortOrder) {
        CrawldataDYService crawldataDYService = new CrawldataDYService();
        AuditCommonResult<PageData<StSpglXmjbxxb>> result = new AuditCommonResult<PageData<StSpglXmjbxxb>>();
        result.setResult(crawldataDYService.getListByPage(conditionMap, first, pageSize, sortField, sortOrder));
        return result;
    }

    @Override
    public List<StSpglGcJdXxb> getDataByXmdm(String xmdm) {
        return new CrawldataDYService().getDataByXmdm(xmdm);
    }

    @Override
    public List<StSpglXmSpsxblxxb> getBlxxbListByRowguid(String jsRowguid) {
        return  new CrawldataDYService().getBlxxbListByRowguid(jsRowguid);
    }

    @Override
    public List<StSpglXmjbxxb> getTotalData() {
        return new CrawldataDYService().getTotalData();
    }
}
