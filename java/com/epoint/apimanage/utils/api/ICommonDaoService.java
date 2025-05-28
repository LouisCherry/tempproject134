package com.epoint.apimanage.utils.api;


import com.epoint.apimanage.log.entity.ApiManageLog;

import java.io.Serializable;
import java.util.List;

/**
 * 镇街经纬度对应的后台service接口
 *
 * @author Administrator
 * @version [版本号, 2019-04-24 15:18:22]
 */
public interface ICommonDaoService extends Serializable {


    List<ApiManageLog> findLogList();

    int update(ApiManageLog apiManageLog);
}
