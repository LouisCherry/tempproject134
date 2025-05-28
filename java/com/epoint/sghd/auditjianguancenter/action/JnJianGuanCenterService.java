package com.epoint.sghd.auditjianguancenter.action;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;

/**
 * 办件认领
 *
 * @author swy
 */
public class JnJianGuanCenterService {
    private ICommonDao commonDao;

    public JnJianGuanCenterService() {
        commonDao = CommonDao.getInstance();
    }

    /**
     * 更新认领时间
     *
     * @param ids
     */
    public void renling(String ids) {
        String sql = "update audit_project set renlingtime = now() where rowguid in (" + ids + ")";
        commonDao.execute(sql, new Object[]{});
        commonDao.close();
    }

    /**
     * project监管时间
     *
     * @param projectguid
     */
    public void jianguan(String projectguid) {
        String sql = "update audit_project set jianguantime = now() where rowguid = ?";
        commonDao.execute(sql, projectguid);
        commonDao.close();
    }

    public int getJgNum(String ouguid) {
        String sql = "select count(1) from audit_project_monitor where MONITOR_ORG_ID = ?";
        int i = commonDao.find(sql, Integer.class, ouguid);
        commonDao.close();
        return i;
    }

    /**
     * 判断添加文件时该事项是否添加过了
     *
     * @param taskguid
     * @return
     */
    public int getShareFileIsOn(String taskid) {
        String sql = "select count(1) from audit_task_sharefile where taskid = ?";
        int i = commonDao.find(sql, Integer.class, taskid);
        commonDao.close();
        return i;
    }

    /**
     * 该部门的事项file
     *
     * @param taskguid
     * @return
     */
    public int getShareFileNum(String ouguid) {
        String sql = "select count(1) from audit_task_sharefile where ouguid = ?";
        int i = commonDao.find(sql, Integer.class, ouguid);
        commonDao.close();
        return i;
    }

}
