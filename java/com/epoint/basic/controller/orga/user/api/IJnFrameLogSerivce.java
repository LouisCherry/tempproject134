package com.epoint.basic.controller.orga.user.api;

import com.epoint.core.grammar.Record;
import com.epoint.frame.service.organ.user.entity.ViewFrameUser;
import com.epoint.sso.frame.domain.FrameLoginLog;

import java.util.Date;
import java.util.List;

public interface IJnFrameLogSerivce {

    /**
     * 得到最后一次成功登录的时间
     *
     * @return
     */
    Record getFrameLogByUserguid(String userguid);

    List<ViewFrameUser> getFrameUserList(String leftTreeNodeGuid);

    /**
     * 得到所有待同步的用户列表
     * @return
     */
    List<ViewFrameUser> getAllFrameUserList();

    /**
     * 更新用户拓展表
     * @param accesstime  最后一次登录时间
     * @param userGuid
     */
    void updateExtendInfoByUserguid(String accesstime, String userGuid);

    void closeConnectionTyrz();

    void closeConnectionZg();

    String getAccesstimeByUserguid(String userGuid);

    /**
     *
     * 查询在指定日期之后登录记录
     * @param date
     * @return
     */
    List<FrameLoginLog> getLastDayLoginLog(Date date);

    /**
     * 查询在指定日期之前，以及登录时间！=expectdateString  的用户记录
     * @param date
     * @param expectdateString
     * @return
     */
    List<Record> getLoginUserBeforeDate(Date date,String expectdateString);
}
