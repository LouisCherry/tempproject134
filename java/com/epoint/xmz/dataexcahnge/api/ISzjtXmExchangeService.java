package com.epoint.xmz.dataexcahnge.api;

import java.io.Serializable;



/**
 * 省厅对接获取项目基本信息对应的后台service接口
 * 
 * @author ll
 * @version [版本号, 2019-07-11 23:26:02]
 */
public interface ISzjtXmExchangeService extends Serializable
{

    /**
     * 获取项目数据
     */
    public Object getProjectInfo(String xmdm,String creditcode);

    public Object getclshyj(String cliengguid);

    public String getCertificate(String url, String json);

}
