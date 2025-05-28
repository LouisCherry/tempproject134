package com.epoint.tongbufw;

import com.epoint.auditproject.util.FlowsnUtil;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.workflow.bizlogic.domain.execute.WorkflowWorkItemHistory;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@DisallowConcurrentExecution
public class EpointSynProject implements Job {

    transient Logger log = LogUtil.getLog(EpointSynProject.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {
            log.info("公积金服务开始===============");
            EpointFrameDsManager.begin(null);
            //job作业比框架起的早 导致部分接口没有实例化
            Thread.sleep(30000);
            doService();
            EpointFrameDsManager.commit();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            EpointFrameDsManager.close();
        }

    }

    private void doService() {
        try {
            List<Record> list2 = null;
            EpointSynProjectService service = new EpointSynProjectService();
            String conditionbdc = "and proId not like '%-%' and LENGTH(proId)>=12";
            String conditionsb = "and proId not like '%-%' and LENGTH(proId)<12";
            String conditiongjj = "and proId like '%-%'";
            list2 = service.getProjectList(conditionbdc);
            if (null == list2 || list2.size() == 0) {
                list2 = service.getProjectList(conditionsb);
            }
            if (null == list2 || list2.size() == 0) {
                list2 = service.getProjectList(conditiongjj);
            }
            if (null == list2 || list2.size() == 0) {
                list2 = service.getProjectList("");
            }

            if (null == list2 || list2.size() == 0) {
                list2 = service.getProjectListone();
                if (null == list2 || list2.size() == 0) {
                    log.info("【济宁市自建系统同步办件】>没有要同步的市办件了");
                    return;
                }
            }
            log.info("【济宁市自建系统同步办件列表：】" + list2);
            for (Record record : list2) {
                AuditProject auditProject = new AuditProject();
                String areacode = record.getStr("areacode");
                switch (areacode) {
                    case "370811":
                        ;
                    case "370882":
                        ;
                    case "370890":
                        ;
                    case "370891":
                        ;
                    case "370892":
                        areacode = "370800";
                        break;
                }
                //公积金办件入库
                if (StringUtil.isNotBlank(record.getStr("taskdep")) &&
                        record.getStr("taskdep").contains("公积金")) {
                    areacode = "370800";
                }
                String proid = record.get("proId");
                String projectguid = UUID.randomUUID().toString();
                String applytype = record.get("applyType");
                String deptask = record.get("taskName");
                AuditTask auditTask = service.gettaskbydeptask(deptask, areacode);
                if (StringUtil.isNotBlank(auditTask)) {
                    log.info("【济宁市自建系统同步办件】>>>unid" + auditTask.getStr("unid"));
                    // 获取事项ID
                    String item_Id = auditTask.getStr("unid");
                    //生成办件编号
                    if (StringUtil.isNotBlank(item_Id)) {
                        String result = FlowsnUtil.createReceiveNum(item_Id, auditTask.getRowguid());
                        if (!"error".equals(result)) {
                            log.info("========================>获取受理编码成功！" + result);
                            auditProject.setFlowsn(result);
                        } else {
                            log.info("========================>获取受理编码失败！");
                            continue;
                        }
                    }

                    String code = auditTask.getAreacode();
                    String centerguid = service.getcenterbyarea(code);
                    auditProject.setRowguid(projectguid);
                    auditProject.setStatus(90);
                    auditProject.set("is_lczj", "2");
                    auditProject.set("zj_flowsn", proid);
                    auditProject.setAreacode(areacode);
                    auditProject.setCenterguid(centerguid);
                    auditProject.setBanjieresult(40);
                    auditProject.setApplyway(20);
                    auditProject.setApplydate(record.getDate("proTime"));
                    auditProject.set("ISSYNACWAVE", 1);
                    if ("10".equals(applytype)) {
                        auditProject.setApplyertype(10);
                        auditProject.setCerttype(ZwfwConstant.CERT_TYPE_ZZJGDMZ);
                        auditProject.setCertnum(record.get("certId"));
                        auditProject.setContactperson(record.get("proName"));
                        auditProject.setContactmobile(record.get("proNumber"));
                        auditProject.setContactphone(record.get("proNumber"));
                        auditProject.setApplyername(record.get("proName"));
                        auditProject.setLegal(record.get("legal"));
                        auditProject.setContactcertnum(record.get("legalId"));
                        auditProject.setLegalid(record.get("legalId"));
                        if (StringUtil.isNotBlank(record.get("address"))) {
                            auditProject.setAddress(record.get("address"));
                        }
                        if (StringUtil.isNotBlank(record.get("email"))) {
                            auditProject.setContactemail(record.get("email"));
                        }
                        service.addProject(auditProject, auditTask);
                    } else {
                        auditProject.setCerttype(ZwfwConstant.CERT_TYPE_SFZ);
                        auditProject.setApplyername(record.get("proName"));
                        auditProject.setCertnum(record.get("certId"));
                        auditProject.setContactemail(record.get("email"));
                        auditProject.setAddress(record.get("address"));
                        auditProject.setContactmobile(record.get("proNumber"));
                        auditProject.setContactphone(record.get("proNumber"));
                        auditProject.setContactperson(record.get("proName"));
                        auditProject.setContactcertnum(record.get("certId"));
                        auditProject.setApplyertype(20);
                        service.addProject(auditProject, auditTask);
                    }
                    //同步流程信息
                    List<Record> list = service.getPhaseList(proid);
                    for (Record recordlc : list) {
                        //插入流程信息表
                        WorkflowWorkItemHistory workflow = new WorkflowWorkItemHistory();
                        workflow.setActivityName(recordlc.getStr("dealStep"));
                        workflow.setWorkItemGuid(UUID.randomUUID().toString());
                        workflow.setTransactorName(recordlc.getStr("dealName"));
                        workflow.setCreateDate(recordlc.getDate("receTime"));
                        workflow.setEndDate(recordlc.getDate("dealTime"));
                        workflow.setOpinion("同意");
                        workflow.setOperatorForDisplayName(recordlc.getStr("subName"));
                        workflow.setOperationDate(new Date());
                        workflow.set("projectguid", projectguid);
                        workflow.setOperatorName(recordlc.getStr("dealStep"));
                        if ("受理".equals(recordlc.getStr("dealStep"))) {
                            String name = recordlc.getStr("dealName");
                            Date time = recordlc.getDate("dealTime");
                            String accepttime = "";
                            if (StringUtil.isNotBlank(time)) {
                                accepttime = EpointDateUtil.convertDate2String(time, "yyyy-MM-dd HH:mm:ss");
                            } else {
                                accepttime = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss");
                            }
                            service.updateProject(name, accepttime, projectguid);
                        }
                        if ("办结".equals(recordlc.getStr("dealStep"))) {
                            String name = recordlc.getStr("dealName");
                            Date time = recordlc.getDate("dealTime");
                            String accepttime = "";
                            if (StringUtil.isNotBlank(time)) {
                                accepttime = EpointDateUtil.convertDate2String(time, "yyyy-MM-dd HH:mm:ss");
                            } else {
                                accepttime = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss");
                            }
                            service.updatebanjieProject(name, accepttime, projectguid);
                        }
                        if ("结束".equals(recordlc.getStr("dealStep"))) {
                            String name = recordlc.getStr("dealName");
                            Date time = recordlc.getDate("dealTime");
                            String accepttime = "";
                            if (StringUtil.isNotBlank(time)) {
                                accepttime = EpointDateUtil.convertDate2String(time, "yyyy-MM-dd HH:mm:ss");
                            } else {
                                accepttime = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss");
                            }
                            service.updatebanjieProject(name, accepttime, projectguid);
                        }
                        int insertresult = service.insertbyrecord(workflow);
                        if (insertresult == 1) {
                            int flag = service.updateflag(recordlc.getStr("UNID"), "S");
                            log.info("【济宁市自建系统同步办件】>>>插入流程信息成功" + flag);
                        } else {
                            int flag = service.updateflag(recordlc.getStr("UNID"), "E");
                            log.info("【济宁市自建系统同步办件】>>>插入流程信息失败" + flag);
                        }

                    }
                    //更新同步标识
                    log.info("【济宁市自建系统同步办件】办件同步成功==>>>" + proid);
                    service.updatesysid(record.getStr("unid"));
                } else {
                    //更新同步标识
                    int result = service.updateonesysid(record.getStr("unid"));
                    if (result == 1) {
                        log.info("【济宁市自建系统同步办件】>>>对应事项不存在" + record.getStr("unid") + deptask);
                    } else {
                        log.info("【济宁市自建系统同步办件】>>>对应事项不存在，标识为更新失败" + record.getStr("unid") + deptask);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("【济宁市自建系统同步办件】=====同步失败 " + e.getMessage());
        }
    }

}
