package com.epoint.knowledge.api;

import java.util.List;

import com.epoint.frame.service.message.entity.MessagesCenter;

public interface IMessageService
{

    void deleteByMessageItemGuid(String messageItemGuid);
    
    List<MessagesCenter> getWaitHandleByPviguidList(String pviguid);

    MessagesCenter getWaitHandleByPviguid(String pviguid, String userguid);

}
