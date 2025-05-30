package com.epoint.zwdt.zwdtrest.sghd.service;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

import java.util.List;

/**
 * 监管平台获取信息的service
 *
 * @version [版本号, 2018年10月10日]
 * @作者 shibin
 */
public class RenlingService {
    private ICommonDao commonDao;

    public RenlingService() {
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

    /**
     * 中心领导获取文件的数量
     *
     * @param areaCode
     * @return
     */
    public int getCenterFileNum(String areaCode) {
        CommonDao dao = new CommonDao();
        int num = 0;
        String sql = "select f.OUGUID from frame_ou f INNER JOIN frame_ou_extendinfo e on f.OUGUID = e.OUGUID WHERE AREACODE = ?  ";
        List<FrameOu> list = dao.findList(sql, FrameOu.class, areaCode);
        if (!list.isEmpty()) {
            for (FrameOu frameOu : list) {
                String sqlFile = "select count(1) from audit_task_sharefile where ouguid = ?";
                int fileNum = dao.queryInt(sqlFile, frameOu.getOuguid());
                num = num + fileNum;
            }
        }
        dao.close();
        return num;
    }

    /**
     * 中心领导获取互动协助的数量
     *
     * @param areaCode
     * @return
     */
    public int getCenterHdxzNum(String areaCode) {
        CommonDao dao = new CommonDao();
        int num = 0;
        String sql = "select count(1) from audit_project_monitor where areacode = ?";
        num = dao.queryInt(sql, areaCode);
        dao.close();
        return num;
    }

}
