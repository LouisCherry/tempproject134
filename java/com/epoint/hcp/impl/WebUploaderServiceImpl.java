package com.epoint.hcp.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.hcp.api.IWebUploaderService;
import com.epoint.hcp.api.entity.eajcstepbasicinfogt;
import com.epoint.hcp.api.entity.eajcstepbasicinfogtnew;
import com.epoint.hcp.api.entity.eajcstepdonegt;
import com.epoint.hcp.api.entity.eajcstepdonegtnew;
import com.epoint.hcp.api.entity.eajcstepprocgt;
import com.epoint.hcp.api.entity.eajcstepprocgtnew;
import com.epoint.hcp.api.entity.lcproject;
import com.epoint.hcp.api.entity.lcprojecteight;
import com.epoint.hcp.api.entity.lcprojectfive;
import com.epoint.hcp.api.entity.lcprojectfour;
import com.epoint.hcp.api.entity.lcprojectnine;
import com.epoint.hcp.api.entity.lcprojectseven;
import com.epoint.hcp.api.entity.lcprojectsix;
import com.epoint.hcp.api.entity.lcprojectten;
import com.epoint.hcp.api.entity.lcprojectthree;
import com.epoint.hcp.api.entity.lcprojecttwo;

@Component
@Service
public class WebUploaderServiceImpl implements IWebUploaderService
{
    
	public List<Record> finList(int first, int pagesize, String ouguid,String areacode) {
        return new WebUploaderService().finList(first, pagesize, ouguid,areacode) ;
    }
	
	 /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new WebUploaderService().deleteByGuid(guid);
    }
    
    public Integer finTotal(String ouguid,String areacode) {
        return new WebUploaderService().finTotal(ouguid,areacode);
    }

	public void updatezjproject(String rowguid,String username,String userguid) {
	    new WebUploaderService().updatezjproject(rowguid, username, userguid);
	}
	
	public void insert(lcproject record) {
		new WebUploaderService().insert(record);
	}
	
	public void insert(lcprojecttwo record) {
		new WebUploaderService().insert(record);
	}
	
	public void insert(lcprojectthree record) {
		new WebUploaderService().insert(record);
	}
	
	public void insert(lcprojectfour record) {
		new WebUploaderService().insert(record);
	}
	
	public void insert(lcprojectfive record) {
		new WebUploaderService().insert(record);
	}
	
	public void insert(lcprojectsix record) {
		new WebUploaderService().insert(record);
	}
	
	public void insert(lcprojectseven record) {
		new WebUploaderService().insert(record);
	}
	
	public void insert(lcprojecteight record) {
		new WebUploaderService().insert(record);
	}
	
	public void insert(lcprojectnine record) {
		new WebUploaderService().insert(record);
	}
	
	public void insert(lcprojectten record) {
		new WebUploaderService().insert(record);
	}
	
	public void update(lcprojectten record) {
		new WebUploaderService().update(record);
	}
	
    public void insertQzkBaseInfo(eajcstepbasicinfogt record) {
		 new WebUploaderService().insertQzkBaseInfo(record);
    }
    
    public void insertQzkProcess(eajcstepprocgt record) {
         new WebUploaderService().insertQzkProcess(record);
    }
    
    public void insertQzkDone(eajcstepdonegt record) {
         new WebUploaderService().insertQzkDone(record);
    }
    
    public void insertQzkBaseInfonew(eajcstepbasicinfogtnew record) {
		new WebUploaderService().insertQzkBaseInfonew(record);
    }
    
    public void insertQzkProcessnew(eajcstepprocgtnew record) {
		new WebUploaderService().insertQzkProcessnew(record);
    }
    
    public void insertQzkDonenew(eajcstepdonegtnew record) {
		new WebUploaderService().insertQzkDonenew(record);
    }

	public AuditTask getAuditTaskByTaskname(String taskname, String areacode,String ouguid) {
		return new WebUploaderService().getAuditTaskByTaskname(taskname, areacode,ouguid);
	}

	public FrameOu getFrameOuByOuname(String ouname) {
		return new WebUploaderService().getFrameOuByOuname(ouname);
	}
	
	public FrameOu getFrameOuByOunameNew(String ouname,String areacode) {
		return new WebUploaderService().getFrameOuByOunameNew(ouname,areacode);
	}
	
	public Record getJnzjProjectByRowguid(String rowguid) {
		return  new WebUploaderService().getJnzjProjectByRowguid(rowguid);
	}
	
	public void updatezjprojectByrowguid(String syncdone,String rowguid) {
		 new WebUploaderService().updatezjprojectByrowguid(syncdone, rowguid);
	}
	
	public void deletejprojectByguid(String rowguid) {
		 new WebUploaderService().deletejprojectByguid(rowguid);
	}

	@Override
	public Record getTotalPorjectByNow() {
		return new WebUploaderService().getTotalPorjectByNow();
	}
	
	public Record getSizeInPorject() {
		return new WebUploaderService().getSizeInPorject();
	}

	@Override
	public int getInCountByAreacode(String areacode) {
		return new WebUploaderService().getInCountByAreacode(areacode);
	}
	
	@Override
	public int getNewInCountByAreacode(String areacode) {
		return new WebUploaderService().getNewInCountByAreacode(areacode);
	}

	@Override
	public void insertLcprojectRecord(String areacode, int count) {
		 new WebUploaderService().insertLcprojectRecord(areacode,count);
	}
	
	@Override
	public void insertExterRecord(String areacode, int count) {
		new WebUploaderService().insertExterRecord(areacode,count);
	}

    @Override
    public void deleteQzkBaseInfo(String guid) {
        new WebUploaderService().deleteQzkBaseInfo(guid);
    }

    @Override
    public void deleteQzkProcess(String guid) {
        new WebUploaderService().deleteQzkProcess(guid);
    }

    @Override
    public void deleteQzkDone(String guid) {
        new WebUploaderService().deleteQzkDone(guid);
    }

}
