package com.epoint.auditproject.util;

import com.epoint.basic.auditproject.auditproject.inter.IJNAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.composite.auditsp.handleproject.service.TAHandleProjectService;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.Date;

public class FlowsnUtil {

    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());


    private static IJNAuditProject iJNAuditProject = ContainerFactory.getContainInfo().getComponent(IJNAuditProject.class);
    private static IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);


    public static String createReceiveNum(String unid, String taskguid) {
        String result = "";
        String shenpilb ="99";
        AuditTask auditTask = iJNAuditProject.getAuditTaskByUnid(unid);
        if (auditTask != null) {
            shenpilb= auditTask.getShenpilb();
        } else {
            auditTask = iAuditTask.getAuditTaskByGuid(taskguid,true).getResult();
            if(auditTask!=null){
                shenpilb= auditTask.getShenpilb();
            }
        }

//        Record rec = iJNAuditProject.getMaxZjNum("jnprojectnum");
//        Integer maxnum = rec.getInt("maxnum");
//        maxnum = maxnum + 1;
//        iJNAuditProject.UpdateMaxZjNum(String.valueOf(maxnum), "jnprojectnum");
//        String num = String.valueOf(maxnum);
//        if (num.length() == 1) {
//            num = "000000" + num;
//        } else if (num.length() == 2) {
//            num = "00000" + num;
//        } else if (num.length() == 3) {
//            num = "0000" + num;
//        } else if (num.length() == 4) {
//            num = "000" + num;
//        } else if (num.length() == 5) {
//            num = "00" + num;
//        } else if (num.length() == 6) {
//            num = "0" + num;
//        }

//        result = "0808" + shenpilb + EpointDateUtil.convertDate2String(new Date(), "yy") + num;
        String numberFlag = "0808" + shenpilb;

        result = getStrFlowSn("办件编号", numberFlag, 7);


        return result;
    }

    public static String getStrFlowSn(String numberName, String numberFlag, int snLength) {

        String flowSn = "";

        Object[] args = new Object[7];

        args[0] = numberName;

        args[1] = numberFlag;

        args[2] = Integer.valueOf(EpointDateUtil.getYearOfDate(new Date()));

        args[3] = 2;

        args[4] = Integer.valueOf(EpointDateUtil.getMonthOfDate(new Date()) + 1);

        args[5] = Integer.valueOf(EpointDateUtil.getDayOfDate(new Date()));

        args[6] = Integer.valueOf(snLength);

        try {

            flowSn = new TAHandleProjectService()
                    .executeProcudureWithResult(args.length + 1, java.sql.Types.VARCHAR, "Common_Getflowsn1", args).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flowSn;
    }

}
