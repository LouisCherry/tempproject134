package com.epoint.hcp.impl;

import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;

public class MsgCenterService
{
    /**
     * 数据增删改查组件O
     */
    protected ICommonDao baseDao;

    public MsgCenterService() {
        baseDao = CommonDao.getInstance();
    }

	public void insert(String rowguid, String content, String mobile, String areacode,String add_time) {
		baseDao.beginTransaction();
		String sql = "insert into msg_center (rowguid,content,mobile,areacode,add_time) values (?1,?2,?3,?4,?5) ";
		baseDao.execute(sql, rowguid,content,mobile,areacode,add_time);
		baseDao.commitTransaction();
		baseDao.close();
		
	}

	public List<Record> getMsgList(String areacode) {
		String sql = " select rowguid,content,mobile,areacode  from msg_center where sync is null  limit 200";
		return baseDao.findList(sql, Record.class,areacode);
	}

	public void update(String rowguid) {
		baseDao.beginTransaction();
		String sql = "update msg_center set sync='1' where rowguid=?";
		baseDao.execute(sql, rowguid);
		baseDao.commitTransaction();
		baseDao.close();
		
	}

   

  
  

}
