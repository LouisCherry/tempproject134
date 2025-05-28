package com.epoint.consultbyou.api;

import java.util.List;

import com.epoint.basic.auditonlineuser.auditonlineconsult.domain.AuditOnlineConsult;

public interface IConsultByou 
{


    List<AuditOnlineConsult> getConsultByou(String startDate, String endDate, String areacode, String ouguid,
            String type, String status);

}
