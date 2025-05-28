package com.epoint.consultbyou.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.epoint.basic.auditonlineuser.auditonlineconsult.domain.AuditOnlineConsult;
import com.epoint.consultbyou.api.IConsultByou;
import com.epoint.consultbyou.impl.ConsultByouImpl;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

@Service
public class ConsultByouService implements IConsultByou
{

    private ConsultByouImpl service = new ConsultByouImpl();
    

    @Override
    public List<AuditOnlineConsult> getConsultByou(String startDate, String endDate, String areacode, String ouguid,
            String type, String status) {
        return service.getConsultByou(startDate,endDate,areacode,ouguid,type,status);
    }


    public List<FrameOu> getOUList(String areaCode) {
        // TODO Auto-generated method stub
        return service.getOUList(areaCode);
    }

}
