package com.epoint.basic.auditsp.dantiinfo.api;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

import java.util.List;

public interface IJnDantiinfoService {

    /**
     * 获取未赋码单体
     * @return
     */
    List<DantiInfo> getUnFmDantiInfoList();

    /**
     * 根据项目标志获取赋码失败的单体
     * @param projectguid
     * @return
     */
    List<DantiInfo> getErrorDantiInfoListByProjectguid(String projectguid);

    /**
     * 根据rowguid获取失败的单体
     * @param rowguid
     * @return
     */
    DantiInfo getErrorDantiInfoListByRowguid(String rowguid);

    List<DantiInfo> getDantiInfoAttachList();

    PageData<DantiInfo> pageDantiList(Record record, int first, int pageSize, String sortField, String sortOrder);

    /***** 工改单体赋码与住建图审系统对接需求登记单开始 *****/
    /**
     *  [工改赋码：推送项目信息] 
     *  @param itemName
     *  @param code
     *  @param temporaryCode
     *  @param socialCode
     *  @param cityCode
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public JSONObject insertInfo(String itemGuid);
    
    /**
     *  [工改赋码：新增单体] 
     *  @param danTiInfos
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public JSONObject insertSubsAsync(JSONArray danTiInfos);
    
    /**
     *  [工改赋码：更新单体] 
     *  @param danTiInfos
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public JSONObject updateSubsAsync(JSONArray danTiInfos);
    
    /***** 工改单体赋码与住建图审系统对接需求登记单结束 *****/

}
