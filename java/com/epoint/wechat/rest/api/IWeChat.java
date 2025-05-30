package com.epoint.wechat.rest.api;

import java.util.List;

import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

/**
 *  微信相关接口
 * @作者 shibin
 * @version [版本号, 2019年9月10日]
 */
public interface IWeChat
{

    /**
     *  获取一级部门
     *  @param areacode
     * @param keyword 
     *  @return
     */
    List<FrameOu> findFirstOu(String areacode, String keyword);

    /**
     *  根据Parentguid获取部门
     *  @param ouguid
     *  @return
     */
    List<FrameOu> findOuByParentguid(String ouguid);

    /**
     * 获取窗口信息
     * @authory shibin
     * @version 2019年9月27日 下午8:34:32
     * @param ouguid
     * @return
     */
    List<AuditOrgaWindow> findWindowInfo(String ouguid);

}
