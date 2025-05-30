package com.epoint.xmz.dataexcahnge.impl;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.xmz.dataexcahnge.api.ISzjtXmExchangeService;

@Component
@Service
public class SzjtXmExchangeServiceImpl implements ISzjtXmExchangeService
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Object getProjectInfo(String xmdm,String creditcode) {
        return new SzjtXmExchangeService().getProjectInfo(xmdm,creditcode);
    }

    @Override
    public Object getclshyj(String cliengguid) {
        return new SzjtXmExchangeService().getclshyj(cliengguid);
    }
    
    @Override
    public String getCertificate(String url,String json) {
    	return new SzjtXmExchangeService().getCertificate(url,json);
    }

   

}
