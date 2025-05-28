package com.epoint.screen.api;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.string.StringUtil;

/**
 * 通知公告对应的后台service
 * 
 * @author cuirun
 * @version [版本号, 2017-12-19 17:53:55]
 */
public class LyAuditVisitCountService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public LyAuditVisitCountService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 查询访问量
     * 
     * 
     * @return int
     */
    public int findVisitcount(String searchtimefrom, String searchtimeto) {
        String sql = "select count(1) from audit_visitcount where 1 = 1";
        if (StringUtil.isNotBlank(searchtimefrom)) {
            sql += " and visitdate >= '" + searchtimefrom + "'";
        }
        if (StringUtil.isNotBlank(searchtimeto)) {
            sql += " and visitdate <= '" + searchtimeto + "'";
        }
        return baseDao.queryInt(sql, new Object(){});
    }
    
    /**
     * 查询注册量
     * 
     * 
     * @return int
     */
    public int findRegistercount() {
        String sql = "select count(1) from audit_online_register";
        return baseDao.queryInt(sql, new Object(){});
    }

}
