package com.epoint.znsb.znsbclientarea.inter;

import java.util.List;
import java.util.Map;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.znsb.znsbclientarea.domain.ZnsbClientArea;

public interface IZnsbClientArea
{
    int insert(ZnsbClientArea var1);

    int deleteByGuid(String var1);

    int deleteByAreacode(String var1);

    int update(ZnsbClientArea var1);

    ZnsbClientArea find(Object var1);

    int batchupdateClient(String var1, String var2, String var3, String var4, String var5);

    AuditCommonResult<List<ZnsbClientArea>> getZnsbClientArea(Map<String, String> var1);

    AuditCommonResult<PageData<ZnsbClientArea>> getZnsbClientAreaByPage(Map<String, String> var1, Integer var2,
            Integer var3, String var4, String var5);
}
