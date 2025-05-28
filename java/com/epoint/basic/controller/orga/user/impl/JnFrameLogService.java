package com.epoint.basic.controller.orga.user.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.frame.service.organ.user.entity.ViewFrameUser;
import com.epoint.sso.frame.domain.FrameLoginLog;

import java.util.Date;
import java.util.List;

public class JnFrameLogService {

    private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "tyrzurl");
    private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "tyrzusername");
    private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "tyrzpassword");

    private ICommonDao commonDao;
    private ICommonDao baseDao;

    private DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);

    public JnFrameLogService() {
        commonDao = CommonDao.getInstance(dataSourceConfig);
        baseDao = CommonDao.getInstance();
    }

    public Record getFrameLogByUserguid(String userguid) {
        String sql = "select * from frame_login_log where Loginid = ? and Loginstate = '1' order by AccessTime desc limit 1";
        Record record= commonDao.find(sql, Record.class, userguid);
        commonDao.close();
        return record;
    }

    public List<ViewFrameUser> getFrameUserList(String leftTreeNodeGuid) {
        String sql = "select * from view_frame_user_java where Parentouguid = ? or OUGuid = ? ";
        List<ViewFrameUser> viewFrameUsers= commonDao.findList(sql, ViewFrameUser.class, leftTreeNodeGuid, leftTreeNodeGuid);
        commonDao.close();
        return viewFrameUsers;
    }

    public List<ViewFrameUser> getAllFrameUserList() {
        String sql = "select * from view_frame_user_java";
        List<ViewFrameUser> viewFrameUsers= commonDao.findList(sql, ViewFrameUser.class);
        commonDao.close();
        return viewFrameUsers;
    }

    public void updateExtendInfoByUserguid(String accesstime, String userGuid) {
        String sql = "UPDATE frame_user_extendinfo SET accesstime = ? where userGuid = ?";
        baseDao.execute(sql, accesstime, userGuid);
    }

    public void closeConnectionTyrz() {
        commonDao.close();
    }

    public void closeConnectionZg() {
        baseDao.close();
    }

    public String getAccesstimeByUserguid(String userGuid) {
        String sql = "select accesstime from frame_user_extendinfo where userGuid = ? ";
        return baseDao.queryString(sql, userGuid);
    }

    public List<FrameLoginLog> getLastDayLoginLog(Date date) {
        String sql = "select * from frame_login_log where AccessTime>=? and Loginstate = '1' ";
        List<FrameLoginLog> frameLoginLogs =  commonDao.findList(sql, FrameLoginLog.class, date);
        commonDao.close();
        return frameLoginLogs;
    }

    public List<Record> getLoginUserBeforeDate(Date date,String expectdateString) {
        String sql = "select a.LOGINID,b.USERGUID,b.accesstime from frame_user as a left join frame_user_extendinfo as b on a.USERGUID=b.USERGUID where " +
                " b.accesstime!=? and b.accesstime<?";
        List<Record> records =  baseDao.findList(sql, Record.class,expectdateString,date);
        baseDao.close();
        return records;
    }
}
