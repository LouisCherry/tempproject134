package com.epoint.auditresource.cert.dzbd;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;

public class Dzbd4DzzzUtil {
    public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());


    //建筑工程规划许可证（高新）	建筑工程规划许可证（济宁） 需要获取电子表单的
    public static String[] dzbdjzgcghxkz = new String[]{
            "11370800MB28559184337011505600001",
            "11370800MB28559184337011505600005",
            "11370800MB28559184337011505600002",
            "1137080123456789154370115056000",
            "11370800MB23415709400011513300501", "11370800MB23415709400011513300502",
            "11370800MB23415709400011513300503",
            "1137080123456789154370115226000"
    };
    //建筑工程施工许可证（高新）建筑工程施工许可证（济宁）
    public static String[] dzbdjzgcsgxkz = new String[]{
            "11370800MB28559184300011710900202",
            "11370800MB28559184737011704500001",
            "11370800MB28559184737011704500002",
            "11370800004234373R400011710900302"
     };
    //人防工程施工图设计文件核准
    public static String[] dzbdrfgcsgtsj = new String[]{
            "11370800MB28559184337108000900101",
            "11370800MB285591847371043008000"
     };


    public static List<String>  dzbdjzgcghxkzlist = Arrays.asList(dzbdjzgcghxkz);
    public static List<String>  dzbdjzgcsgxkzlist = Arrays.asList(dzbdjzgcsgxkz);
    public static List<String>  dzbdrfgcsgtsjlist = Arrays.asList(dzbdrfgcsgtsj);



    public Dzbd4DzzzUtil(CertInfoExtension dataBean, AuditTask audittask, AuditProject auditproject, String tycertcatcode) throws IOException {
        Dzbd4Dzzz dzbd4dzzz= null;
        if(dzbdjzgcghxkzlist.contains(audittask.getItem_id())){
            dzbd4dzzz = new Dzbdjzgcghxkz(dataBean,audittask,auditproject,tycertcatcode);
        }
        else if(dzbdjzgcsgxkzlist.contains(audittask.getItem_id())){
            dzbd4dzzz = new Dzbdjzgcsgxkz(dataBean,audittask,auditproject,tycertcatcode);
        }
        else if(dzbdrfgcsgtsjlist.contains(audittask.getItem_id())){
            dzbd4dzzz = new Dzbdrfgcsgtsj(dataBean,audittask,auditproject,tycertcatcode);
        }
        else{
            log.info("未匹配上事项!");
            return ;
        }
        // 设置基本信息
        dzbd4dzzz.setBasicDataBean();
        // 设置表单信息
        dzbd4dzzz.setFormData();


    }

}

