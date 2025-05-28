package com.epoint.xmz.thirdreporteddata.datacheck.checkrule;

import com.epoint.core.grammar.Record;
import com.epoint.zwbg.datacheck.dataverify.inter.IDataVerify;
import com.epoint.zwbg.datacheck.model.CheckRuleInfo;

public class SpglsplcjdxxbCheck implements IDataVerify {

    @Override
    public Boolean check(Record record, CheckRuleInfo checkRuleInfo) {
        if (!"5".equals(record.getStr("spjdxh"))) {
            if (record.getInt("spjdsx") <= 0 || record.getInt("spjdsx") > 50) {
                return false;
            }
        }
        return true;
    }
}
