package com.epoint.jnzwfw.jndtanddw.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;

import java.util.List;

/**
 * @author hzchen
 * @version 1.0
 * @date 2024/11/4 10:42
 */
public class JnDtAndDwService
{
    protected ICommonDao baseDao;

    public JnDtAndDwService() {
        baseDao = CommonDao.getInstance();
    }

    public List<Record> getDtInfoByItemGuid(String itemGuid) {
        String sql = "select di.*\n" + "from danti_sub_relation dsr\n"
                + "         inner join danti_info di on dsr.DANTIGUID = di.ROWGUID\n" + "where SUBAPPGUID in\n"
                + "      (select audit_sp_i_subapp.RowGuid from audit_sp_i_subapp where yewuguid = ?)";
        return baseDao.findList(sql, Record.class, itemGuid);
    }
}
