package com.epoint.xmz.job.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.cxbus.api.entity.CxBus;
import com.epoint.xmz.cxbus.impl.CxBusService;
import com.epoint.xmz.job.api.IGdjService;
import com.epoint.xmz.job.api.IGsProjectService;
import com.epoint.xmz.job.api.ISwService;

/**
 * 好差评相关接口的实现
 * @作者 atjiao
 * @version [版本号, 2020年6月8日]
 */
@Component
@Service
public class GdjServiceImpl implements IGdjService
{

    /**
     * 
     */
    private static final long serialVersionUID = 3258268474743087728L;

	@Override
	public void insertsw(AuditRsItemBaseinfo record) {
		new GdjService().insertsw(record);
	}

	@Override
	public void updatesw(AuditRsItemBaseinfo record) {
		new GdjService().updatesw(record);
	}

	@Override
	public AuditRsItemBaseinfo getSwInteminfoByItemcode(String Itemcode) {
		return new GdjService().getSwInteminfoByItemcode(Itemcode);
	}

	@Override
	public AuditRsItemBaseinfo getSwInteminfoByRowguid(String rowguid) {
		return new GdjService().getSwInteminfoByRowguid(rowguid);
	}


}
