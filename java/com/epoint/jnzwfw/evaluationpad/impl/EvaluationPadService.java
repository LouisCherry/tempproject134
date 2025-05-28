package com.epoint.jnzwfw.evaluationpad.impl;

import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;

public class EvaluationPadService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public EvaluationPadService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * @Description:获取部门电话
     * @author:许烨斌
     * @time 2019年1月21日下午5:05:52
     * @param windowguid  窗口guid
     * @return 部门电话
     */
    public String findOUTel(String windowguid) {
        String sqltel = "select o.TEL from frame_ou o INNER JOIN audit_orga_window a ON o.OUGUID = a.OUGUID WHERE a.RowGuid = ? ";
        String ouTel = baseDao.queryString(sqltel, windowguid);
        return ouTel;
    }

    /**
     * @Description:获取人员信息
     * @author:许烨斌
     * @time 2019年1月21日下午5:05:42
     * @param userguid 人员guid
     * @return
     */
    public Record findUserInfo(String userguid) {
        String sql = "SELECT zhengZMM, Is_Advanced, Is_HouB, is_BiaoBing, is_red FROM audit_orga_member WHERE UserGuid = ?";
        Record record = new Record();
        record = baseDao.find(sql, Record.class, userguid);
        return record;
    }

    /**
     *  根据窗口获取事项信息
     *  @authory shibin
     *  @version 2019年9月19日 上午9:13:18
     *  @param windowguid
     *  @return
     */
    public List<Record> findTaskInfoList(String windowguid) {
        String sql = "select a.WINDOWGUID,a.OUNAME,a.TaskName,b.serviceCodeclientguid,b.onetimenoticeguid from (select OUNAME,TaskName,w.TASKID,w.WINDOWGUID from audit_orga_windowtask w INNER JOIN audit_task t ON w.TASKID = t.TASK_ID WHERE WINDOWGUID = ? and IFNULL(IS_HISTORY,0) = '0' AND IS_EDITAFTERIMPORT = 1 AND IS_ENABLE = 1 ) a INNER JOIN audit_task_taian b ON a.TASKID = b.task_id ORDER BY convert (a.TaskName using gbk) ASC ";
        return baseDao.findList(sql, Record.class, windowguid);
    }

    /**
     * 根据macaddres获取windowguid
     * @authory shibin
     * @version 2019年9月23日 下午2:35:35
     * @param macaddress
     * @return
     */
    public String getWindowguidByMacAddress(String macaddress) {
        String sql = "select WINDOWGUID from audit_znsb_equipment WHERE MACADDRESS = ? ";
        return baseDao.queryString(sql, macaddress);
    }

    /**
     * 获取attachguid
     * @authory shibin
     * @version 2019年9月25日 下午12:12:35
     * @param str
     * @return
     */
    public String getAttachguidByCliengguid(String str) {
        String sql = "select ATTACHGUID from frame_attachinfo WHERE CLIENGGUID = ? ";
        return baseDao.queryString(sql, str);
    }

    /**
     * @description
     * @author shibin
     * @date  2020年6月8日 上午10:32:51
     */
    public int updateQRcodeinfoByprojectGuid(String projectGuid, String qRcodeinfo) {
        String sql = "UPDATE audit_project SET QRcodeinfo = ? WHERE RowGuid = ? ";
        return baseDao.execute(sql, qRcodeinfo, projectGuid);
    }
    
    /**
     * @description
     * @author shibin
     * @date  2020年6月8日 上午10:32:51
     */
    public int updateJSTcodeinfoByprojectGuid(String projectGuid, String qRcodeinfo) {
        String sql = "UPDATE audit_project SET jstcodeinfo = ? WHERE RowGuid = ? ";
        return baseDao.execute(sql, qRcodeinfo, projectGuid);
    }
    
    public int updateLegalQRcodeinfoByprojectGuid(String projectGuid, String qRcodeinfo) {
        String sql = "UPDATE audit_project SET dzyyqcode = ? WHERE RowGuid = ? ";
        return baseDao.execute(sql, qRcodeinfo, projectGuid);
    }
}
