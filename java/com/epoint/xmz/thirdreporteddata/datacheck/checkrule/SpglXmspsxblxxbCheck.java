package com.epoint.xmz.thirdreporteddata.datacheck.checkrule;

import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglsplcjdsxxxb;
import com.epoint.zwbg.datacheck.dataverify.inter.IDataVerify;
import com.epoint.zwbg.datacheck.model.CheckRuleInfo;

public class SpglXmspsxblxxbCheck implements IDataVerify {


    @Override
    public Boolean check(Record record, CheckRuleInfo checkRuleInfo) {
        ISpglsplcjdsxxxb iSpglsplcjdsxxxb = ContainerFactory.getContainInfo().getComponent(ISpglsplcjdsxxxb.class);
        return iSpglsplcjdsxxxb.isExistSplcSx(record.getDouble("splcbbh"), record.getStr("splcbm"), record.getDouble("spsxbbh"),
                record.getStr("spsxbm"));
    }
}
