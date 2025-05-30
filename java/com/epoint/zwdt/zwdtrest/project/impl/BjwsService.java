package com.epoint.zwdt.zwdtrest.project.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;

import java.util.List;

public class BjwsService {
    public BjwsService() {
    }

    public String getCodeValue(String text){
        String sql = "select ITEMVALUE from code_items where itemtext = ? and CODEID in (select CODEID from code_main where CODENAME = '经营类别')";
        return CommonDao.getInstance().queryString(sql, text);
    }

    public String getDoc(String businessType,String docType,String areacode){
        String sql = "select rowguid from audit_notifydoc_attachinfo where doctype=? and BusinessType like ? and areacode = ?";
        return CommonDao.getInstance().queryString(sql, docType, "%"+businessType+"%",areacode);
    }
}
