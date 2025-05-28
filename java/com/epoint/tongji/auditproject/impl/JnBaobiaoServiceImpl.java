package com.epoint.tongji.auditproject.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;

import com.epoint.tongji.auditproject.api.IJnBaobiaoService;
import com.epoint.tongji.auditproject.domain.JnAuditProjectTJ;

@Component
@Service
public class JnBaobiaoServiceImpl implements IJnBaobiaoService
{

    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<JnAuditProjectTJ> getTjs(String areacode) {
		JnBaobiaoService service = new JnBaobiaoService();
        return service.getTjs(areacode);
	}

  

}
