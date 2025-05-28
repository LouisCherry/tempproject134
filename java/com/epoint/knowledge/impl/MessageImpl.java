package com.epoint.knowledge.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.epoint.core.dao.CommonDao;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.knowledge.api.IMessageService;

@Service
@Component
public class MessageImpl implements IMessageService
{

    @Override
    public void deleteByMessageItemGuid(String messageItemGuid) {
        String sql = "SELECT * from messages_center where messageitemguid=?";
        MessagesCenter center = CommonDao.getInstance().find(sql, MessagesCenter.class,messageItemGuid);        
        CommonDao.getInstance().delete(center);
    }

    @Override
    public List<MessagesCenter> getWaitHandleByPviguidList(String pviguid) {
        String sql = "SELECT * from messages_center where pviguid=?";
        return CommonDao.getInstance().findList(sql, MessagesCenter.class,pviguid);
    }

    @Override
    public MessagesCenter getWaitHandleByPviguid(String pviguid, String userguid) {
        String sql = "SELECT * from messages_center where pviguid=?  and TARGETUSER =? ";
        return CommonDao.getInstance().find(sql, MessagesCenter.class,pviguid,userguid);
    }

}
