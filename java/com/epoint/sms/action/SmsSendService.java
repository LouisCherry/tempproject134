package com.epoint.sms.action;
import java.util.Date;
import java.util.List;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.message.entity.MessagesCenterHistroy;
public class SmsSendService
{
    protected ICommonDao  commonDao;

    public ICommonDao getICommonDao() {
        return commonDao;
    }

    public void setICommonDao(ICommonDao commonDao) {
        this.commonDao = commonDao;
    }

    public SmsSendService() {
        commonDao = CommonDao.getInstance();
    }
    
    /**
     * 查询message_center中需要发送的短信
     *  messageType:1表示发送无需回复，2表示发送需要回复（满意度评价）
     *  @param args
     *  @throws Exception    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    //这边只市级事项办理时发短信，事项事项插入短信时候
    public List<MessagesCenter> selectMessageCenters() {
        String sql = "select * from Messages_Center where sendmode =1 and (content is not null or content <> '')"
                + " and (issend = '' or issend is null) and (messagetarget <> '' and messagetarget is not null)"
                + " AND ( IFNULL(IsSchedule, 0) = 0 OR ( IFNULL(IsSchedule, 0) = 1"
                + " AND now() > ScheduleSendDate)) AND to_days(GenerateDate) = to_days(now()) ORDER BY generatedate desc  limit 100";
         
        List<MessagesCenter> list = commonDao.findList(sql, MessagesCenter.class);
        commonDao.close();
        return list;
    }
    public MessagesCenterHistroy getMessagesCenterHistory(String mobile) {
        String hql = "select * from Messages_Center_History where sendmode = 1 and issend = 1 "
                + "and messagetarget = '" + mobile + "' ORDER BY generatedate desc";
      
        MessagesCenterHistroy message = (MessagesCenterHistroy) commonDao.findList(hql, MessagesCenterHistroy.class);
        commonDao.close();
        return message;
    }
    public void handleEpointMessage(MessagesCenter mc, int tag) {
        MessagesCenterHistroy mch = new MessagesCenterHistroy();             
        mch.setArchiveNo(mc.getArchiveNo());
        mch.setBaseOuGuid(mc.getBaseOuGuid());
        mch.setBeiZhu(mc.getBeiZhu());
        mch.setClientIdentifier(mc.getClientIdentifier());
        mch.setClientTag(mc.getClientTag());
        mch.setContent(mc.getContent());
        mch.setDoneDate(mc.getDoneDate());
        mch.setFromDispName(mc.getFromDispName());
        mch.setFromMobile(mc.getFromMobile());
        mch.setFromUser(mc.getFromUser());
        mch.setGenerateDate(mc.getGenerateDate());
        mch.setHandleType(mc.getHandleType());
        mch.setHandleUrl(mc.getHandleUrl());
        mch.setIsDel(mc.getIsDel());
        mch.setIsNoHandle(mc.getIsNoHandle());
        mch.setIsSchedule(mc.getIsSchedule());
        mch.setIsSend(tag);
        mch.setIsShow(mc.getIsShow());
        mch.setJobGuid(mc.getJobGuid());
        mch.setMessageItemGuid(mc.getMessageItemGuid());
        mch.setMessageTarget(mc.getMessageTarget());
        mch.setMessageType(mc.getMessageType());
        mch.setOperatorForDisplayGuid(mc.getOperatorForDisplayGuid());
        mch.setOuGuid(mc.getOuGuid());
        mch.setOperatorGuid(mc.getOperatorGuid());
        mch.setPviguid(mc.getPviguid());
        mch.setRdoType(mc.getRdoType());
        mch.setReadDate(mc.getReadDate());
        mch.setScheduleSendDate(mc.getScheduleSendDate());
        mch.setSendDate(mc.getSendDate());
        mch.setSendMode(mc.getSendMode());
        mch.setSendResult(mc.getSendResult());
        mch.setSourceRecordId(mc.getSourceRecordId());
        mch.setSourceTable(mc.getSourceTable());
        mch.setTargetDispName(mc.getTargetDispName());
        mch.setTargetUser(mc.getTargetUser());
        mch.setTitle(mc.getTitle());
        mch.setSendDate(new Date());
        commonDao.insert(mch);
        mch = null;
        // 删除Messages_center表中的已发送短信
        commonDao.delete(mc);
        commonDao.commitTransaction();
        mc = null;
        commonDao.close();
    }
}
