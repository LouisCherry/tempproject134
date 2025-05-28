package com.epoint.hcp.impl;

import java.util.List;

import com.epoint.hcp.api.IMsgCenterService;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.grammar.Record;

@Component
@Service
public class MsgCenterServiceImpl implements IMsgCenterService
{

    /**
     * 
     */
    private static final long serialVersionUID = 4884504129463503050L;

    @Override
    public List<Record> getMsgList(String areacode) {
       
        return new MsgCenterService().getMsgList(areacode);
    }

    @Override
    public void insert(String rowguid, String content, String mobile, String areacode,String add_time) {
        new MsgCenterService().insert(rowguid,content,mobile,areacode,add_time);
        
    }

	@Override
	public void update(String rowguid) {
		
		new MsgCenterService().update(rowguid);
	}

   
}
