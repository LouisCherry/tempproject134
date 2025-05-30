package com.epoint.hcp.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.epoint.hcp.api.IAuditHcpShow;
import com.epoint.hcp.api.entity.AuditHcpAreainfo;
import com.epoint.hcp.api.entity.AuditHcpOuinfo;
import com.epoint.hcp.api.entity.AuditHcpUserinfo;
import com.epoint.hcp.api.entity.Evainstance;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 评价跟踪表对应的后台service实现类
 * 
 * @author lc
 * @version [版本号, 2019-09-06 15:08:28]
 */
@Component
public class AuditHcpShowServiceImpl implements IAuditHcpShow
{

    @Override
    public List<JSONObject> getArea() {
        return new AuditHcpShowService().getArea();
    }
    
    @Override
    public int addEvainstance(Evainstance evainstance) {
        return new AuditHcpShowService().addEvainstance(evainstance);
    }

	@Override
	public List<AuditHcpOuinfo> getOuList(Integer num, String areacode) {
		return new AuditHcpShowService().getOuList(num, areacode);
	}

	@Override
	public List<AuditHcpAreainfo> getAreaList(Integer num, String areacode) {
		return new AuditHcpShowService().getAreaList(num, areacode);
	}

	@Override
	public PageData<AuditHcpUserinfo> getAllByPage(Map<String, String> conditionMap, Integer first,
			Integer pageSize, String sortField, String sortOrder) {
        return new AuditHcpShowService().getAllByPage(AuditHcpUserinfo.class, conditionMap, first, pageSize, sortField,
                sortOrder);
	}

    @Override
    public AuditHcpAreainfo getAreaByCode(String areacode) {
        return new AuditHcpShowService().getAreaByCode(areacode);
    }

    @Override
    public List<Record> getUserList(String areacode, int currentPage, int pageSize) {
        return new AuditHcpShowService().getUserList(areacode, currentPage, pageSize);
    }

    @Override
    public int getUserListCount(String areacode) {
        return new AuditHcpShowService().getUserListCount(areacode);
    }
    
    @Override
    public List<Record> getEvaluateZb(String areacode,String evatype) {
        return new AuditHcpShowService().getEvaluateZb(areacode,evatype);
    }
    
}
