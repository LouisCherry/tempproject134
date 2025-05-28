package com.epoint.cs.sjtsproject.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.cs.sjtsproject.api.ISjTsprojectService;

@Service
@Component
public class SjTsprojectImpl implements ISjTsprojectService
{

    @Override
    public List<Record> findList(String taskname, String taskid, String ouname, String applyuser, String starttime,
            String endtime) {
        String sql = "SELECT a.unid,a.taskName,a.itemId,a.proName,a.proTime,a.taskDep,a.proId from pre_project a JOIN pre_phase b on a.proId=b.proId";
        if (StringUtil.isNotBlank(taskname)) {
            sql += " and a.taskname='" + taskname + "'";
        }
        if (StringUtil.isNotBlank(taskid)) {
            sql += " and a.itemid='" + taskid + "'";
        }
        if (StringUtil.isNotBlank(ouname)) {
            sql += " and a.taskDep='" + ouname + "'";
        }
        if (StringUtil.isNotBlank(applyuser)) {
            sql += " and a.proName='" + applyuser + "'";
        }
        if (StringUtil.isNotBlank(starttime)) {
            sql += " and a.proTime >='" + starttime + "'";
        }
        if (StringUtil.isNotBlank(endtime)) {
            sql += " and a.proTime <='" + endtime + "'";
        }
        sql +=" GROUP BY b.proId ";
        return CommonDao.getInstance().findList(sql, Record.class);
    }

    @Override
    public List<Record> findApply(Object object) {
        String sql = "SELECT dealName,dealTime from pre_phase where dealStep='受理' and proId=?";
        return CommonDao.getInstance().findList(sql, Record.class,object.toString());
    }

    @Override
    public List<Record> findBanjie(Object object) {
        String sql = "SELECT dealName,dealTime from pre_phase where dealStep='办结' and proId=?";
        return CommonDao.getInstance().findList(sql, Record.class,object.toString());
    }

    @Override
    public String findStatus(Object object) {
        String sql = "SELECT proStatus from pre_phase where proId=? ORDER BY receTime desc";
        return CommonDao.getInstance().find(sql, String.class,object);
    }

    @Override
    public Record findByUnid(String unid) {
        String sql = "select * from pre_project where unid=? ";
        return CommonDao.getInstance().find(sql, Record.class,unid);
    }

    @Override
    public List<Record> findPhase(String proId) {
        String sql = "select dealStep,dealName,dealPro,receTime,dealTime,dealOpinion,subName from pre_phase where proid=? ";
        return CommonDao.getInstance().findList(sql, Record.class,proId);
    }

}
