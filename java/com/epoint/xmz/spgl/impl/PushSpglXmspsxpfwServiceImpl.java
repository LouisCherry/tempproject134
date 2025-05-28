package com.epoint.xmz.spgl.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditproject.monitorsupervise.api.auditprojectold;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.xmz.spgl.api.IPushSpglXmspsxpfwService;

/**
 * 好差评相关接口的实现
 * @作者 atjiao
 * @version [版本号, 2020年6月8日]
 */
@Component
@Service
public class PushSpglXmspsxpfwServiceImpl implements IPushSpglXmspsxpfwService
{

    /**
     * 
     */
    private static final long serialVersionUID = 3258268474743087728L;

	public List<AuditProject> findProjectList() {
		return new PushSpglXmspsxpfwService().findProjectList();
	}
	
	public List<AuditProject> findYthProjectList() {
		return new PushSpglXmspsxpfwService().findYthProjectList();
	}
	public List<auditprojectold> findOldYthProjectList() {
		return new PushSpglXmspsxpfwService().findOldYthProjectList();
	}

	


}
