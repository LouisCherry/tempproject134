package com.epoint.yczwfw.auditqueue.windowled.inter;

import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.gxqzwfw.auditqueue.windowled.domain.WindowLed;

@Service
public interface  IWindowLed 
{
    public void inset(String windowno,String content);
    public List<WindowLed> findlist(String windownostr);
}
