package com.epoint.xmz.thirdreporteddata.datacheck.checkrule;

import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmspsxblxxbV3;
import com.epoint.zwbg.datacheck.dataverify.inter.IDataVerify;
import com.epoint.zwbg.datacheck.model.CheckRuleInfo;

public class SpglSpsxslbmCheck implements IDataVerify {

    @Override
    public Boolean check(Record record, CheckRuleInfo checkRuleInfo) {
        ISpglXmspsxblxxbV3 ispglxmspsxblxxbV3 = ContainerFactory.getContainInfo().getComponent(ISpglXmspsxblxxbV3.class);
        return ispglxmspsxblxxbV3.isExistFlowsn(record.getStr("spsxslbm"));
    }
}
