package com.epoint.apimanage.utils.api;



import java.io.Serializable;
import java.util.List;

import com.epoint.apimanage.log.entity.ApiManageLog;

/**	
 * 接口调用异常通知
 */
public interface IApiErroInfoSendService extends Serializable {


    void sendErroMsg(ApiManageLog apiManageLog);
}
