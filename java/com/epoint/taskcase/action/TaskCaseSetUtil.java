package com.epoint.taskcase.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.audittask.casematerial.inter.IAuditTaskCaseMaterialService;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskCase;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.taskcase.api.ITaskCaseService;


@Scope("request")
@RestController("taskcaseinfoaction")
public class TaskCaseSetUtil
{

    @Autowired
    private IAuditTaskExtension auditTaskExtension;
    @Autowired
    private IAuditTaskCase auditTaskCase;
    @Autowired
    private IAuditTaskMaterialCase caseMaterialService;
    @Autowired
    private ITaskCaseService caseService;

    //解析xml
    public void saveTaskcase() {
        List<String> taskguidlist = caseService.findTaskGuid();
        try {
            if (taskguidlist != null && taskguidlist.size() > 0) {
                for (String string : taskguidlist) {
                    AuditTaskExtension extension = auditTaskExtension.getTaskExtensionByTaskGuid(string, true)
                            .getResult();
                    if (extension != null) {
                        // 通过reader对象的read方法加载books.xml文件,获取docuemnt对象。
                        Document document = DocumentHelper.parseText(extension.get("CASE_SETTING_INFO"));
                        // 通过document对象获取根节点bookstore
                        Element rootElt = document.getRootElement();
                        // 通过element对象的elementIterator方法获取迭代器
                        Element caserecords = rootElt.element("CASESETTINGS");
                        Iterator caselist = caserecords.elementIterator();
                        // 遍历迭代器
                        List<Record> caseinfolist = new ArrayList<>();
                        while (caselist.hasNext()) {
                            List<Record> materlist = new ArrayList<>();
                            Element record = (Element) caselist.next();
                            Iterator itt = record.elementIterator();
                            Record record2 = new Record();
                            while (itt.hasNext()) {
                                Element caserecod = (Element) itt.next();
                                if ("CODE".equals(caserecod.getName())) {
                                    record2.set("casecode", caserecod.getStringValue());
                                }
                                if ("NAME".equals(caserecod.getName())) {
                                    record2.set("casename", caserecod.getStringValue());
                                }
                                if ("MAIN_POINT".equals(caserecod.getName())) {
                                    record2.set("casedetail", caserecod.getStringValue());
                                }
                                if ("MATERIALS".equals(caserecod.getName())) {
                                    Iterator mater = caserecod.elementIterator();
                                    int i = 0;
                                    while (mater.hasNext()) {
                                        Record record3 = new Record();
                                        Element ma = (Element) mater.next();
                                        Iterator mm = ma.elementIterator();
                                        while (mm.hasNext()) {
                                            Element m = (Element) mm.next();
                                            record3.set(m.getName(), m.getStringValue());
                                        }
                                        materlist.add(record3);
                                        i++;
                                    }
                                    record2.set("mater", materlist);
                                }
                            }
                            record2.set("taskguid", string);
                            caseinfolist.add(record2);
                        }
                        //system.out.println(caseinfolist);
                        handleTaskCase(caseinfolist);
                    }
                }
            }
        }
        catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //保存情形信息
    private void handleTaskCase(List<Record> caseinfolist) {
        //system.out.println(caseinfolist);
        if (caseinfolist != null && caseinfolist.size() > 0) {
            for (Record record : caseinfolist) {
                String casecode = record.get("casecode");
                String casename = record.get("casename");
                String casedetail = record.get("casedetail");
                List<Record> list = record.get("mater");
                String taskguid = record.get("taskguid");
                //system.out.println(list);
                AuditTaskCase taskcase = caseService.Check(taskguid, casename, casecode);
                String caserowguid = "";
                if (taskcase == null) { // 审批库情形不存在，进行新增
                    AuditTaskCase newcase = new AuditTaskCase();
                    newcase.setRowguid(casecode);
                    newcase.setOperatedate(new Date());
                    newcase.setOperateusername("同步服务新增");
                    newcase.setCasedetail(casedetail);
                    newcase.setCasename(casename);
                    newcase.setTaskguid(taskguid);
                    auditTaskCase.addAuditTaskCase(newcase);
                    caserowguid = newcase.getRowguid();
                    for (Record record2 : list) {
                        AuditTaskMaterialCase mcase = new AuditTaskMaterialCase();
                        mcase.setOperatedate(new Date());
                        mcase.setOperateusername("同步服务新增");
                        mcase.setRowguid(UUID.randomUUID().toString());
                        mcase.setMaterialguid(record2.get("code").toString());
                        mcase.setTaskcaseguid(caserowguid);
                        mcase.setTaskguid(taskguid);
                        mcase.setIs_show(0);
                        caseMaterialService.addAuditTaskMaterialCase(mcase);
                    }
                }
            }
        }
    }

}
