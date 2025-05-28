package com.epoint.zwfw.dj.impl;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.grammar.Record;
import com.epoint.zwfw.dj.api.IJnDjService;
import com.epoint.zwfw.dj.service.JnDjService;


/**
 * 好差评相关接口的实现
 * @作者 atjiao
 * @version [版本号, 2020年6月8日]
 */
@Component
@Service
public class JnDjServiceImpl implements IJnDjService
{

    /**
     * 
     */
    private static final long serialVersionUID = 3258268474743087728L;

   

	@Override
	public int insert(Record rec) {
		return new JnDjService().insert(rec);
	}

	@Override
	public AuditTask getAuditBasicInfo(String taskName, String areacode) {
		return new JnDjService().getAuditBasicInfo(taskName, areacode);
	}

	@Override
	public AuditTask getAuditBasicInfo(String item_id) {
		return new JnDjService().getAuditBasicInfo(item_id);
	}

	@Override
	public Record getAuditProjectZjxtByRowguid(String rowguid) {
		return new JnDjService().getAuditProjectZjxtByRowguid(rowguid);
	}

	@Override
	public Record getAuditRsApplyZjxtByRowguid(String rowguid) {
		return  new JnDjService().getAuditRsApplyZjxtByRowguid(rowguid);
	}

    @Override
    public String getAreacodeByareaname(String areaname) {
        return  new JnDjService().getAreacodeByareaname(areaname);
    }



}
