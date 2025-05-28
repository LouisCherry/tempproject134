package com.epoint.screen.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.dao.CommonDao;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.screen.api.ILyFrameOu;

@Component
@Service
public class LyFrameOuImpl implements ILyFrameOu{

	@Override
	public AuditCommonResult<FrameOu> getFrameOuByOuguid(String ouguid) {
		AuditCommonResult<FrameOu> result = new AuditCommonResult<FrameOu>();
		try {
        	String sql = " select * from frame_ou where ouguid = '"+ouguid+"' ";
        	
        	FrameOu frameou = new CommonDao().find(sql, FrameOu.class);
            result.setResult(frameou);
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
		return result;
	}

    @Override
    public AuditCommonResult<List<FrameOu>> getAllFrameOu() {
        AuditCommonResult<List<FrameOu>> result = new AuditCommonResult<>();
        String sql = "select * from frame_ou where ISSUBWEBFLOW =1";
        try {
            List<FrameOu> list = CommonDao.getInstance().findList(sql, FrameOu.class, new Object[]{});
            result.setResult(list);
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<FrameOu> getFrameOuByOuname(String ouname) {
        AuditCommonResult<FrameOu> result = new AuditCommonResult<FrameOu>();
        try {
            String sql = " select * from frame_ou where ouname = '"+ouname+"' ";
            
            FrameOu frameou = new CommonDao().find(sql, FrameOu.class);
            result.setResult(frameou);
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> getOulevelByOuname(String ouguid) {
        AuditCommonResult<String> result = new AuditCommonResult<>();
        String sql = "select isJD from frame_ou where ouguid =?1";
        try {
            String oulevel = CommonDao.getInstance().find(sql, String.class, new Object[]{ouguid});
            result.setResult(oulevel);
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

	@Override
	public AuditCommonResult<List<FrameOu>> getXZOu() {
		AuditCommonResult<List<FrameOu>> result = new AuditCommonResult<>();
        String sql = "select ouguid,ouname,isjd from frame_ou where isjd is null and LENGTH(OUCODE) = 20";
        try {
            List<FrameOu> list = CommonDao.getInstance().findList(sql, FrameOu.class, new Object[]{});
            result.setResult(list);
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
	}

	@Override
	public AuditCommonResult<List<FrameOu>> getSQOu(String parentouguid) {
		AuditCommonResult<List<FrameOu>> result = new AuditCommonResult<>();
        String sql = "select ouguid,ouname,oucode,isjd from frame_ou where parentouguid = ?1";
        try {
            List<FrameOu> list = CommonDao.getInstance().findList(sql, FrameOu.class, new Object[]{parentouguid});
            result.setResult(list);
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
	}

}
