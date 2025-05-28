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
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import org.xm.similarity.util.StringUtil;

import java.sql.Types;
import java.util.Calendar;
import java.util.Date;

public class Dzbdjzgcsgxkz extends Dzbd4Dzzz {

    public Dzbdjzgcsgxkz(CertInfoExtension dataBean, AuditTask audittask, AuditProject auditproject, String tycertcatcode) {
        super(dataBean, audittask, auditproject, tycertcatcode);
    }

    @Override
    public void setBasicDataBean() {
        dataBean.set("bh",getFlow());
        dataBean.set("bzdw",audittask.getOuname());
        dataBean.set("fzrq", EpointDateUtil.convertDate2String(new Date(),"yyyy年MM月dd日"));
        Calendar calendar = Calendar.getInstance();
        dataBean.set("yymmdd", calendar.get(Calendar.YEAR));
        dataBean.set("mm",calendar.get(Calendar.MONTH) + 1);
        dataBean.set("dd",calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public String getFlow() {
        IAuditSpBusiness auditSpBusinessService = ContainerFactory.getContainInfo().getComponent(IAuditSpBusiness.class);
        IAuditSpISubapp iauditspisubapp = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);
        String numberName="建设工程规划许可证";
        String numberFlag=auditproject.getAreacode();
        Object[] args = new Object[8];
        args[0] = numberName;
        args[1] = numberFlag;
        args[2] = Integer.valueOf(EpointDateUtil.getYearOfDate(new Date()));
        args[3] = 4;
        args[4] = Integer.valueOf(EpointDateUtil.getMonthOfDate(new Date()) + 1);
        args[5] = Integer.valueOf(EpointDateUtil.getDayOfDate(new Date()));
        args[6] = "";
        AuditSpISubapp auditSpISubapp = iauditspisubapp.getSubappByGuid(auditproject.getSubappguid()).getResult();
        AuditSpBusiness auditBusiness = auditSpBusinessService.getAuditSpBusinessByRowguid(auditSpISubapp.getBusinessguid())
                .getResult();
        if(auditBusiness!=null){
            if(auditBusiness.getBusinessname().contains("市政")){
                args[6] = "02";
            }else{
                args[6] = "01";
            }
        }
        //流水号长度
        args[7] = 2;
        ICommonDao baseDao = CommonDao.getInstance();
        return baseDao.executeProcudureWithResult(args.length + 1, java.sql.Types.VARCHAR, "Common_Getflowsn_sgxkz", args).toString();
    }

    @Override
    public void beforPutData(Record dzbdRecord) {
        super.beforPutData(dzbdRecord);
        // 证照发证日期对应，表单两个字段范围
        if (StringUtil.isNotBlank(dzbdRecord.getStr("htgq")) && StringUtil.isNotBlank(dzbdRecord.getStr("htgqend"))) {
            System.out.println(dzbdRecord.getStr("htgq"));
            System.out.println(dzbdRecord.getStr("htgqend"));
            //日期返回为时间戳，转换成时间
            Date htgq = EpointDateUtil.convertString2Date(dzbdRecord.getStr("htgq"),EpointDateUtil.DATE_FORMAT);
            Date htgqend = EpointDateUtil.convertString2Date(dzbdRecord.getStr("htgqend"),EpointDateUtil.DATE_FORMAT);
            String start =  EpointDateUtil.convertDate2String(htgq,"yyyy年MM月dd日");
            String end  = EpointDateUtil.convertDate2String(htgqend,"yyyy年MM月dd日");
            dzbdRecord.set("htgq",start+"至" + end);
         }

    }
}
