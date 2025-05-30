package com.epoint.zwdt.zwdtrest.bdc.impl;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.grammar.Record;
import com.epoint.zwdt.zwdtrest.bdc.api.IBdcService;

/**
 * 好差评相关接口的实现
 * @作者 atjiao
 * @version [版本号, 2020年6月8日]
 */
@Component
@Service
public class BdcServiceImpl implements IBdcService
{

    /**
     * 
     */
    private static final long serialVersionUID = 3258268474743087728L;


	@Override
	public Record getBdcDetailByBdcdyh(String bdcdyh) {
		return  new BdcService().getBdcDetailByBdcdyh(bdcdyh);
	}

}
