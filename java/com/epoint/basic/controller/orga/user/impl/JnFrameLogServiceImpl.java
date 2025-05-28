package com.epoint.basic.controller.orga.user.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.controller.orga.user.api.IJnFrameLogSerivce;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.organ.user.entity.ViewFrameUser;
import com.epoint.sso.frame.domain.FrameLoginLog;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Service
public class JnFrameLogServiceImpl implements IJnFrameLogSerivce {

    @Override
    public Record getFrameLogByUserguid(String userguid) {
        return new JnFrameLogService().getFrameLogByUserguid(userguid);
    }

    @Override
    public List<ViewFrameUser> getFrameUserList(String leftTreeNodeGuid) {
        return new JnFrameLogService().getFrameUserList(leftTreeNodeGuid);
    }

    @Override
    public List<ViewFrameUser> getAllFrameUserList() {
        return new JnFrameLogService().getAllFrameUserList();
    }

    @Override
    public void updateExtendInfoByUserguid(String accesstime, String userGuid) {
        new JnFrameLogService().updateExtendInfoByUserguid(accesstime, userGuid);
    }

    @Override
    public void closeConnectionTyrz() {
        new JnFrameLogService().closeConnectionTyrz();
    }

    @Override
    public void closeConnectionZg() {
        new JnFrameLogService().closeConnectionZg();
    }

    @Override
    public String getAccesstimeByUserguid(String userGuid) {
        return new JnFrameLogService().getAccesstimeByUserguid(userGuid);
    }

    @Override
    public List<FrameLoginLog> getLastDayLoginLog(Date date) {
        return new JnFrameLogService().getLastDayLoginLog(date);
    }

    @Override
    public List<Record> getLoginUserBeforeDate(Date date,String expectdateString){
        return new JnFrameLogService().getLoginUserBeforeDate(date,expectdateString);
    }
}
