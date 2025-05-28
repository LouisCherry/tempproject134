package com.epoint.auditqueue.qhj.api;

import java.util.List;

import com.epoint.frame.service.organ.ou.entity.FrameOu;

public interface IJNOulistinner
{

    List<FrameOu> getOrderdLinkedOulist(int firstpage, int pagesize, String condition);

    int getOrderdLinkedOucount(String condition);

}
