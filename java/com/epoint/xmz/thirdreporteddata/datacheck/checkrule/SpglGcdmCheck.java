package com.epoint.xmz.thirdreporteddata.datacheck.checkrule;

import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmjbxxbV3;
import com.epoint.zwbg.datacheck.dataverify.inter.IDataVerify;
import com.epoint.zwbg.datacheck.model.CheckRuleInfo;

public class SpglGcdmCheck implements IDataVerify {


    @Override
    public Boolean check(Record record, CheckRuleInfo checkRuleInfo) {
        ISpglXmjbxxbV3 iSpglXmjbxxbV3 = ContainerFactory.getContainInfo().getComponent(ISpglXmjbxxbV3.class);
        return iSpglXmjbxxbV3.isExistGcdm(record.getStr("gcdm"));
    }


}
