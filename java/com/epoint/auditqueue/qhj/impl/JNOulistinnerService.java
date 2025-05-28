package com.epoint.auditqueue.qhj.impl;

import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

public class JNOulistinnerService
{
public List<FrameOu> getOrderdLinkedOulist(int firstpage,int pagesize,String condition){
    ICommonDao commondao  = CommonDao.getInstance();
    String sql = "select b.* from  ( select DISTINCT OUGUID from audit_orga_window where 1=1 "+condition+" ) a LEFT JOIN frame_ou b on a.OUGUID = b.OUGUID  ORDER BY b.ORDERNUMBER DESC";
    return commondao.findList(sql, firstpage, pagesize, FrameOu.class);
}
public int getOrderdLinkedOucount(String condition){
    ICommonDao commondao  = CommonDao.getInstance();
    String sql = "select count(1) from  ( select DISTINCT OUGUID from audit_orga_window where 1=1  "+condition+" ) a ";
    return commondao.queryInt(sql);
}
}
