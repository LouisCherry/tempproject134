package com.epoint.auditresource.cert.dzbd;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.date.EpointDateUtil;

import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

public class Dzbdjzgcghxkz extends Dzbd4Dzzz {

    public static String[] jn = new String[]{
            "11370800MB28559184337011505600001",
            "11370800MB28559184337011505600005",
            "11370800MB28559184337011505600002",
    };
    public Dzbdjzgcghxkz(CertInfoExtension dataBean, AuditTask audittask, AuditProject auditproject, String tycertcatcode) {
        super(dataBean, audittask, auditproject, tycertcatcode);
    }

    @Override
    public void setBasicDataBean() {
        dataBean.set("bh",getFlow());
        dataBean.set("fzjg",audittask.getOuname());
        dataBean.set("rq", EpointDateUtil.convertDate2String(new Date(),"yyyy年MM月dd日"));
    }

    @Override
    public String getFlow() {
        //如果是济宁
        if(Arrays.stream(jn).filter(a -> a.equals(audittask.getItem_id())).findAny().isPresent()){
            return "";
        };
        Object[] args = new Object[7];
        args[0] = "建设工程规划许可证";
        args[1] = "建字第3708";
        args[2] = Integer.valueOf(EpointDateUtil.getYearOfDate(new Date()));
        args[3] = 4;
        args[4] = Integer.valueOf(EpointDateUtil.getMonthOfDate(new Date()) + 1);
        args[5] = Integer.valueOf(EpointDateUtil.getDayOfDate(new Date()));
        //流水号长度
        args[6] = 3;
        ICommonDao baseDao = CommonDao.getInstance();
        return baseDao.executeProcudureWithResult(args.length + 1, Types.VARCHAR,"Common_Getflowsn_ydghxkz" , args).toString();
    }
}
