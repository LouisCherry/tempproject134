package com.epoint.auditresource.cert.dzbd;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;

import java.sql.Types;
import java.util.Calendar;
import java.util.Date;

public class Dzbdrfgcsgtsj extends Dzbd4Dzzz {

    public Dzbdrfgcsgtsj(CertInfoExtension dataBean, AuditTask audittask, AuditProject auditproject, String tycertcatcode) {
        super(dataBean, audittask, auditproject, tycertcatcode);
    }

    @Override
    public void setBasicDataBean() {
        dataBean.set("bh",getFlow());
        dataBean.set("fzrq", EpointDateUtil.convertDate2String(new Date(),"yyyy年MM月dd日"));
    }
}
