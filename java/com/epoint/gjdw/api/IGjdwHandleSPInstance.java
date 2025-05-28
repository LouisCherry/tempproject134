package com.epoint.gjdw.api;

public interface IGjdwHandleSPInstance
{
    /**
     * 初始化工改申报实例
     * 
     * @param projectguid
     *            办件标识
     * @param param
     *            param
     * @return
     * @throws Exception
     */
    public void initInstance(String projectguid, String param) throws Exception;
}
