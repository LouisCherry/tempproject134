package com.epoint.jn.zndf.api;

import java.util.List;

import org.springframework.stereotype.Service;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;

@Service
public interface IJnzndf
{
 
    /**
     * 
     *  根据点位查询出事项名称
     *  [功能详细描述]
     *  @param pos
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditCommonResult<List<Record>> getTaskNameByPos(String pos);
    /**
     * 
     *  根据事项guid来获取能办理该事项的窗口编号
     *  [功能详细描述]
     *  @param taskguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditCommonResult<String> getWindowByTaskguid(String taskguid);
    /**
     * 
     *  根据mac地址获取设备号(横屏查询机)
     *  [功能详细描述]
     *  @param mac
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditCommonResult<String> getNumByMac(String mac);
    /**
     * 
     *  根据大厅的guid获取大厅的窗口列表
     *  [功能详细描述]
     *  @param lobbytype
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditCommonResult<List<Record>> getWindowNameByLobby(String lobbytype);
 
}
