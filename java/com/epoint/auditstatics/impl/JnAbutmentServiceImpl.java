package com.epoint.auditstatics.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditstatics.api.IJnabutmentService;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;

@Component
@Service
public class JnAbutmentServiceImpl implements IJnabutmentService {

	@Override
	public List<Record> outaskStatics(String areacode) {
		String sql="select ouname,sum(CASE WHEN SHENPILB='01' THEN 1 ELSE 0 END) XKNUM,sum(CASE WHEN SHENPILB='04' THEN 1 ELSE 0 END) ZSNUM,sum(CASE WHEN SHENPILB='05' THEN 1 ELSE 0 END) GFNUM,sum(CASE WHEN SHENPILB='06' THEN 1 ELSE 0 END) JLNUM,sum(CASE WHEN SHENPILB='07' THEN 1 ELSE 0 END) QRNUM,sum(CASE WHEN SHENPILB='10' THEN 1 ELSE 0 END) QTNUM,count(1) TOTALNUM FROM audit_task where ouname is not null and ouname!='' and ISTEMPLATE='0' and IS_EDITAFTERIMPORT='1' and (IS_HISTORY=0 or IS_HISTORY is null) and areacode=? GROUP BY ouname ORDER BY TOTALNUM DESC";
		return CommonDao.getInstance().findList(sql, Record.class, areacode);
	}

	@Override
	public String getItemTextByCodeName(String object) {
		String sql="SELECT b.ITEMTEXT FROM code_main a LEFT JOIN code_items b on a.CODEID=b.CODEID WHERE a.CODENAME='事项类型' AND b.ITEMVALUE=?";
		return CommonDao.getInstance().queryString(sql, object);
	}

	@Override
	public List<Record> categoryStatics(String areacode) {
		
      String sql = "select SHENPILB,count(1) value from audit_task where ISTEMPLATE='0' and IS_EDITAFTERIMPORT='1' and (IS_HISTORY=0 or IS_HISTORY is null) and areacode=? group by SHENPILB";
      return CommonDao.getInstance().findList(sql, Record.class, areacode);
		
		
	}

	@Override
	public List<Record> taskSourceStatics(String areacode) {
		 String sql = "select tasksource,count(1) value from audit_task where tasksource is not null and tasksource !='' and ISTEMPLATE='0' and IS_EDITAFTERIMPORT='1' and (IS_HISTORY=0 or IS_HISTORY is null) and areacode=? group by tasksource";
		 return CommonDao.getInstance().findList(sql, Record.class, areacode);
	}

	@Override
	public List<Record> typeStatics(String areacode) {
		 String sql = "select type,count(1) value from audit_task where type is not null and type !='' and ISTEMPLATE='0' and IS_EDITAFTERIMPORT='1' and (IS_HISTORY=0 or IS_HISTORY is null) and areacode=? group by type";
	    return CommonDao.getInstance().findList(sql, Record.class, areacode);
	}

}
