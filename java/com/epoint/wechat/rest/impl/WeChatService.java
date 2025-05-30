package com.epoint.wechat.rest.impl;

import java.util.List;

import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

public class WeChatService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public WeChatService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     *  获取一级部门
     *  @param areacode
     * @param keyword 
     *  @return
     */
    public List<FrameOu> findFirstOu(String areacode, String keyword) {
        //String sql = "select f.OUNAME,f.OUGUID,f.TEL,f.ADDRESS from frame_ou f INNER JOIN frame_ou_extendinfo e ON f.OUGUID = e.OUGUID WHERE AREACODE = ? ORDER BY ORDERNUMBER DESC ";
        String sql = "select a.OUNAME,a.OUGUID,a.TEL,a.ADDRESS,a.DESCRIPTION from frame_ou a INNER JOIN frame_ou_extendinfo b ON a.OUGUID = b.OUGUID inner join audit_orga_area c on a.PARENTOUGUID=c.ouguid WHERE b.AREACODE = ? AND b.OUFAX = '88888888' ";
        if (StringUtil.isNotBlank(keyword)) {
            sql += " AND a.OUNAME LIKE '%" + keyword + "%' ";
        }
        sql += " ORDER BY ORDERNUMBER DESC ";
        return baseDao.findList(sql, FrameOu.class, areacode);
    }

    /**
     *  根据Parentguid获取部门
     *  @param ouguid
     *  @return
     */
    public List<FrameOu> findOuByParentguid(String ouguid) {
        String sql = "select a.OUNAME,a.OUGUID,a.TEL,a.ADDRESS,a.DESCRIPTION from frame_ou a WHERE PARENTOUGUID = ? ORDER BY ORDERNUMBER DESC ";
        return baseDao.findList(sql, FrameOu.class, ouguid);
    }

    /**
     * 获取窗口信息
     * @authory shibin
     * @version 2019年9月27日 下午8:35:01
     * @param ouguid
     * @return
     */
    public List<AuditOrgaWindow> findWindowInfo(String ouguid) {
        String sql = "select WINDOWNAME,WINDOWNO,NOTE,w.TEL from audit_orga_window w INNER JOIN frame_ou f on w.OUGUID = f.OUGUID WHERE w.isWeChatShow = '1' AND w.OUGUID = ? ORDER BY w.windowno asc ";
        return baseDao.findList(sql, AuditOrgaWindow.class, ouguid);
    }

}
