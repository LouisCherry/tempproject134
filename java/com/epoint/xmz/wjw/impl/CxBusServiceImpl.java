package com.epoint.xmz.wjw.impl;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.certhwslysjyxk.api.entity.CertHwslysjyxk;
import com.epoint.xmz.certhwslysjyxk.impl.CertHwslysjyxkService;
import com.epoint.xmz.wjw.api.ICxBusService;
/**
 * 车辆信息表对应的后台service实现类
 * 
 * @author 1
 * @version [版本号, 2021-02-03 15:42:45]
 */
@Component
@Service
public class CxBusServiceImpl implements ICxBusService
{
     
     public Record getHdzzl10tyszxhcByRowguid(String projectguid) {
         return new CxBusService().getHdzzl10tyszxhcByRowguid(projectguid);
     }
     
     public Record getHdzzl5tys10tyxzxhcByRowguid(String projectguid) {
    	 return new CxBusService().getHdzzl5tys10tyxzxhcByRowguid(projectguid);
     }
     
     public Record getZxhcByRowguid(String projectguid) {
    	 return new CxBusService().getZxhcByRowguid(projectguid);
     }

	public Record getWxpyscByRowguid(String projectguid) {
		 return new CxBusService().getWxpyscByRowguid(projectguid);
	}
	
	public Record getCsgccByRowguid(String projectguid) {
		return new CxBusService().getCsgccByRowguid(projectguid);
	}
	
	public Record getQxhcByRowguid(String projectguid) {
		return new CxBusService().getQxhcByRowguid(projectguid);
	}

	public Record getWlyyczqcdlyszhfByRowguid(String projectguid) {
		return new CxBusService().getWlyyczqcdlyszhfByRowguid(projectguid);
	}
	
	public List<Record> getWlyyczqcdlyszhfzbByRowguid(String projectguid) {
		return new CxBusService().getWlyyczqcdlyszhfzbByRowguid(projectguid);
	}
	
	public Record getXyczqcdlyszbfByRowguid(String projectguid) {
		return new CxBusService().getXyczqcdlyszbfByRowguid(projectguid);
	}
	
	public Record getHwcyzgsqByRowguid(String projectguid) {
		return new CxBusService().getHwcyzgsqByRowguid(projectguid);
	}
	
	public Record getLkcyzgsqByRowguid(String projectguid) {
		return new CxBusService().getLkcyzgsqByRowguid(projectguid);
	}
	
	public Record getDlkyjyxkByRowguid(String projectguid) {
		return new CxBusService().getDlkyjyxkByRowguid(projectguid);
	}
	
	public Record getWhjsycyzgxkByRowguid(String projectguid) {
		return new CxBusService().getWhjsycyzgxkByRowguid(projectguid);
	}
	
	public Record getShttzsbgdjByRowguid(String projectguid)  {
		return new CxBusService().getShttzsbgdjByRowguid(projectguid);
	}
	
	public Record getDlwxhwysyyrycyzgxkByRowguid(String projectguid) {
		return new CxBusService().getDlwxhwysyyrycyzgxkByRowguid(projectguid);
	}
	
	public Record getWlyyczcjsycyzgxkByRowguid(String projectguid) {
		return new CxBusService().getWlyyczcjsycyzgxkByRowguid(projectguid);
	}
	
	public Record getSlysqyhzbzsqByRowguid(String xkzbh, String qymc) {
		return new CxBusService().getSlysqyhzbzsqByRowguid(xkzbh,qymc);
	}

	@Override
	public Record getPorjectByRowguid(String tablename, String projectguid) {
		return new CxBusService().getPorjectByRowguid(tablename,projectguid);
	}
	
	@Override
	public Record getDzbdDetail(String tablename, String rowguid) {
		return new CxBusService().getDzbdDetail(tablename,rowguid);
	}
	
	public int insert(Record record) {
        return new CxBusService().insert(record);
    }
	
	public int update(Record record) {
        return new CxBusService().update(record);
    }
	

}
