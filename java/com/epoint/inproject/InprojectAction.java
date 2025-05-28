package com.epoint.inproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.grammar.Record;

public class InprojectAction implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private InprojectImpl service = new InprojectImpl();
    
    public List<AuditProject> getInproject(String startDate, String endDate){
        List<AuditProject> list2 = new ArrayList<>();
        List<Record> list = service.findList(startDate,endDate);
        if(list!=null&&list.size()>0){
            for (Record record : list) {
                AuditProject project = new AuditProject();
                project.set("OUNAME", record.get("OUNAME"));
                project.set("JBACCEPTNUM", record.getStr("JBACCEPTNUM"));
                project.set("JBBANJIENUM", record.getStr("JBBANJIENUM"));
                project.set("CNACCEPTNUM", record.getStr("CNACCEPTNUM"));
                project.set("CNBANJIENUM", record.getStr("CNBANJIENUM"));
                project.set("SUMACCEPT", record.getStr("SUMACCEPT"));
                project.set("SUMBANJIE", record.getStr("SUMBANJIE"));
                project.set("CHAOSHINUM", record.getStr("CHAOSHINUM"));
                project.set("TIQIANNUM", record.getStr("TIQIANNUM"));
                project.set("ANQINNUM", record.getStr("ANQINNUM"));
                project.set("TIQIANLV", record.getStr("TIQIANLV"));
                project.set("ANQILV", record.getStr("ANQILV"));
                project.set("AVG", record.getStr("AVG"));
                list2.add(project);
            }
        }
        return list2;
    }

}
