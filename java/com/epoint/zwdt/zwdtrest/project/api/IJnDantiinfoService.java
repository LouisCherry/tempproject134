package com.epoint.zwdt.zwdtrest.project.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;

public interface IJnDantiinfoService {

    DantiInfo getDantiInfoByBm(String dtbm);

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
