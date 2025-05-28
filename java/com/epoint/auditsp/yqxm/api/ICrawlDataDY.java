package com.epoint.auditsp.yqxm.api;

import com.epoint.auditsp.yqxm.api.entity.StSpglGcJdXxb;
import com.epoint.auditsp.yqxm.api.entity.StSpglXmSpsxblxxb;
import com.epoint.auditsp.yqxm.api.entity.StSpglXmjbxxb;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.cs.auditepidemiclog.api.entity.AuditEpidemicLog;
import com.epoint.database.peisistence.crud.impl.model.PageData;

import java.util.List;
import java.util.Map;

/**
 * Created on 2022/6/6.
 *
 * @author ${阳佳}
 */
public interface ICrawlDataDY {
    /**
     * 将spgl_的数据放在st_spgl_里
     */
    public void pushDataToStTables(String areacode);

    List<StSpglXmjbxxb> getEachAreaData();

    List<StSpglXmjbxxb> getEachAreaYuQiPorjects(String areaCode, int first, int pageSize);

    public AuditCommonResult<PageData<StSpglXmjbxxb>> getListByPage(Map<String, String> conditionMap,
                                                                              int first, int pageSize, String sortField, String sortOrder);

    /**
     * 根据项目代码找到工程阶段信息表
     * @param xmdm
     * @return
     */
    List<StSpglGcJdXxb> getDataByXmdm(String xmdm);

    List<StSpglXmSpsxblxxb> getBlxxbListByRowguid(String jsRowguid);

    List<StSpglXmjbxxb> getTotalData();
}
