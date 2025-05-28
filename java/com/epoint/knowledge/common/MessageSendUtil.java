package com.epoint.knowledge.common;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.epoint.basic.authentication.UserSession;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.message.impl.MessagesCenterService9;
import com.epoint.knowledge.api.IMessageService;
import com.epoint.knowledge.common.domain.CnsKinfo;
import com.epoint.workflow.bizlogic.api.WFAPI9;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;

public class MessageSendUtil
{   
    private WFAPI9 wfapi  = new WFAPI9();
    
    @Autowired
    private static IMessageService service;
    
    private static MessagesCenterService9 mcs = null;
    private static IMessagesCenterService imservice= ContainerFactory.getContainInfo().getComponent(IMessagesCenterService.class);
    public static void closeWaitHandlemsgbyguid(String pviguid) {
        

        try {
            
            mcs = new MessagesCenterService9();
            List<MessagesCenter> list = service.getWaitHandleByPviguidList(pviguid);
            for(MessagesCenter info : list){
                service.deleteByMessageItemGuid(info.getMessageItemGuid());
            }
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {

        }
    }
    public static boolean sendWaitHandleMessage(String title, String targetUser, String targetDispName,
            String handleUrl, String ClientIdentifier, String pviGuid, String clientTag, String beiZhu) {
        ICommonDao commonDao = null;
        boolean flag = false;
        try {
            commonDao = CommonDao.getInstance();
            mcs = new MessagesCenterService9(commonDao);
            String MessageItemGuid = UUID.randomUUID().toString();
            if ((StringUtil.isNotBlank(handleUrl)) && (handleUrl.indexOf("?") > 0)) {
                handleUrl = handleUrl + "&messageItemGuid=" + MessageItemGuid;
            }
            else {
                handleUrl = handleUrl + "?messageItemGuid=" + MessageItemGuid;
            }
            handleUrl = handleUrl + "&clientIdentifier=" + ClientIdentifier;        
            
            flag = mcs.insertWaitHandleMessage(MessageItemGuid, title, "办理", targetUser, targetDispName,UserSession.getInstance().getUserGuid(),
                    UserSession.getInstance().getDisplayName(), beiZhu, handleUrl,UserSession.getInstance().getOuGuid() ,
                    UserSession.getInstance().getBaseOUGuid(), 1, "", "", ClientIdentifier, clientTag,
                    new Date(), pviGuid, UserSession.getInstance().getUserGuid(), "办理", "", null, null, null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    public static void closeWaitHandlemsgbyBeizhu(String beizhu) {
        Object commonDao = null;

        try {
            MessagesCenterService9 e = new MessagesCenterService9();
            
            List list = service.getWaitHandleByPviguidList(beizhu);
            if (list != null && list.size() > 0) {
                Iterator arg3 = list.iterator();

                while (arg3.hasNext()) {
                    MessagesCenter message = (MessagesCenter) arg3.next();
                    service.deleteByMessageItemGuid(message.getMessageItemGuid());
                    
                }
            }
        } catch (Exception arg8) {
            arg8.printStackTrace();
        } finally {
            if (commonDao != null) {
                ((ICommonDao) commonDao).close();
            }

        }

    }
}
