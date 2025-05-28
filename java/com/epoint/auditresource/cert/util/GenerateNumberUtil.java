package com.epoint.auditresource.cert.util;

import com.epoint.basic.auditproject.auditproject.impl.JNAuditProjectService;
import com.epoint.basic.auditproject.auditproject.inter.IJNAuditProject;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;

import java.util.Date;

public class GenerateNumberUtil {

    /**
     * 得到库里的最大值，生成规则里带年份的
     *
     * @param name
     * @return
     */
    public static String getCertNum(String name) {
        IJNAuditProject iJNAuditProject = ContainerFactory.getContainInfo().getComponent(IJNAuditProject.class);
        String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");
        boolean flag = false;
        //进入新的一年会没有记录，重置编号
        Record rec = iJNAuditProject.getMaxZjNum(name, String.valueOf(year));
        if (rec == null) {
            rec = new Record();
            rec.put("name", name);
            rec.put("maxnum", 0);
            flag = true;
        }
        Integer maxnum = rec.getInt("maxnum");
        maxnum = maxnum + 1;
        if (flag) {
            new JNAuditProjectService().insetZjNum(rec.getStr("maxnum"), rec.getStr("name"), String.valueOf(year));
        } else {
            iJNAuditProject.UpdateMaxZjNum(String.valueOf(maxnum), name, String.valueOf(year));
        }
        return String.valueOf(maxnum);
    }

    /**
     * 得到库里的最大值，生成规则里不带年份的
     *
     * @param name
     * @return
     */
    public static String getCertNumNotYear(String name) {
        Integer maxnum = 0;
        IJNAuditProject iJNAuditProject = ContainerFactory.getContainInfo().getComponent(IJNAuditProject.class);
        Record rec = iJNAuditProject.getMaxZjNumNew(name);
        if (rec != null) {
            maxnum = rec.getInt("maxnum");
            maxnum = maxnum + 1;
            iJNAuditProject.UpdateMaxZjNumNew(String.valueOf(maxnum), name);
        }
        return String.valueOf(maxnum);
    }

}
