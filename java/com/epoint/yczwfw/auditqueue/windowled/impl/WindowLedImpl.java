package com.epoint.yczwfw.auditqueue.windowled.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.gxqzwfw.auditqueue.windowled.domain.WindowLed;
import com.epoint.yczwfw.auditqueue.windowled.inter.IWindowLed;

@Component
@Service
public class  WindowLedImpl implements IWindowLed
{

    @Override
    public void inset(String windowno, String content) {
        if(StringUtil.isNotBlank(windowno)&&StringUtil.isNotBlank(content)){
            WindowLedService service=  new WindowLedService();
            WindowLed windowled= service.find(windowno);
            if(windowled!=null){
                windowled.setOperatedate(new Date());
                windowled.setContent(content);
                service.update(windowled);
            }else{
                WindowLed record=new WindowLed();
                record.setRowguid(UUID.randomUUID().toString());
                record.setOperatedate(new Date());
                record.setWindowno(windowno);
                record.setContent(content);
                service.insert(record);
            }
        }
        
    }

    @Override
    public List<WindowLed> findlist(String windownostr) {
        List<WindowLed> list=new ArrayList<WindowLed>();
        List<WindowLed> returnlist=new ArrayList<WindowLed>();
        if(StringUtil.isNotBlank(windownostr)){
            if(windownostr.endsWith(";")){
                windownostr=windownostr.substring(0,windownostr.length()-1);
            }
            String instr="'"+windownostr.replace(";", "','")+"'";
            String sql="select * from audit_znsb_windowled where windowno in ( "+instr+" )";
            WindowLedService service=  new WindowLedService();
            list=service.findList(sql);
            
            
            Calendar current = Calendar.getInstance();
            Calendar today = Calendar.getInstance();    //今天
            
            today.set(Calendar.YEAR, current.get(Calendar.YEAR));
            today.set(Calendar.MONTH, current.get(Calendar.MONTH));
            today.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH));
            //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
            today.set( Calendar.HOUR_OF_DAY, 0);
            today.set( Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            for(WindowLed entity:list){
                current.setTime(entity.getOperatedate());
                if(current.before(today)){
                    entity.setContent("空闲");
                    entity.setOperatedate(new Date());
                    service.update(entity);
                }
                returnlist.add(entity);
            }
        }
        return returnlist;
    }
   
}
