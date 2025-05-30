package com.epoint.tongbufw;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.workflow.bizlogic.domain.execute.WorkflowWorkItemHistory;

@DisallowConcurrentExecution
public class ProjectdownForBdc implements Job
{
    transient Logger log = LogUtil.getLog(ProjectdownForBdc.class);
    //浪潮前置库有用字段
    private final String fields = " DISTINCT projid,gtsync,PROJID,PROJPWD,REGION_ID,STDVER,ITEMNAME,"
            + "PROJECTNAME,APPLICANT,APPLICANTMOBILE,APPLICANTADDRESS,APPLICANTCARDCODE,APPLICANTTEL,APPLICANTEMAIL,ACCEPTDEPTNAME,ACCEPTDEPTID,"
            + "APPROVALTYPE,PROMISETIMELIMIT,PROMISETIMEUNIT,SUBMIT,OCCURTIME,TRANSACTOR,MAKETIME,INNERNO ";

    
    /**
     * 程序入口
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            //job作业比框架起的早 导致部分接口没有实例化
            Thread.sleep(30000);
            syncProjectinfo();
            EpointFrameDsManager.commit();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }

    //同步浪潮对接的自建系统的办件信息
    private void syncProjectinfo() {
    	ProjectdownForBdcService service = new ProjectdownForBdcService();
        List<Record> infolist = service.getInfoFromQzk(fields);
        log.info("开始同步浪潮对接的自建系统的办件信息");
        for (Record record : infolist) {
            String flowsn = record.getStr("PROJID");
            String taskname = record.getStr("ITEMNAME");
            String areacode = record.getStr("REGION_ID").substring(0, 6);
            String ACCEPTDEPTNAME = record.getStr("ACCEPTDEPTNAME");
            if (ACCEPTDEPTNAME.contains("高新")) {
                areacode = "370890";
            }else if (ACCEPTDEPTNAME.contains("经济开发")) {
                areacode = "370892";
            }else if (ACCEPTDEPTNAME.contains("北湖")) {
                areacode = "370891";
            }
            AuditTask task = service.gettaskbydeptask(taskname,areacode);
            log.info(taskname+"，事项已匹配到，开始进行办件同步");
            try {
                if (task != null && StringUtil.isNotBlank(task)) {
                    AuditProject project = service.getProjectByflowsn(flowsn);
                    String rowguid = "";
                    String pviguid = "";
                    IAuditProject projectService = (IAuditProject) ContainerFactory.getContainInfo()
                            .getComponent(IAuditProject.class);
                    if (project == null) {
                        System.out.println("开始新增project");
                        rowguid = UUID.randomUUID().toString();
                        pviguid = UUID.randomUUID().toString();
                        project = new AuditProject();
                        project.setRowguid(rowguid);
                        project.setPviguid(pviguid);
                        project.setFlowsn(flowsn);
                        project.setAreacode(areacode);
                        int applyway = record.getInt("SUBMIT");
                        switch (applyway) {
                            case 0:
                                applyway = 20;
                                break;
                            case 1:
                                applyway = 10;
                                break;
                            default:
                                applyway = 10;
                                break;
                        }
                        project.setApplyway(applyway);
                        project.setReceivedate(record.getDate("MAKETIME"));
                        project.setReceiveusername(record.getStr("TRANSACTOR"));
                        project.setAcceptuserdate(record.getDate("MAKETIME"));
                        project.setAcceptusername(record.getStr("TRANSACTOR"));
                        project.setProjectname(task.getTaskname());
                        project.setApplydate(record.getDate("OCCURTIME"));
                        project.setApplyername(record.getStr("APPLICANT"));
                        project.setCertnum(record.getStr("APPLICANTCARDCODE"));
                        project.setContactmobile(record.getStr("APPLICANTMOBILE"));
                        project.setContactphone(record.getStr("APPLICANTTEL"));
                        project.setContactemail(record.getStr("APPLICANTEMAIL"));
                        project.setApplyertype(10);
                        project.setOuguid(task.getOuguid());
                        project.setOuname(task.getOuname());
                        project.setOperatedate(new Date());
                        project.setTask_id(task.getTask_id());
                        project.setTasktype(task.getType());
                        project.setTaskguid(task.getRowguid());
                        project.setCerttype(ZwfwConstant.CERT_TYPE_ZZJGDMZ);
                        project.setStatus(80);
                        project.setOperateusername("浪潮自建部门数据");
                        //is_lczj null,0：一窗受理系统办件，1，浪潮对接自建办件，2：新点对接济宁市自建办件,5:不动产办件
                        project.set("is_lczj", 5);
                        projectService.addProject(project);
                        
                        service.updateInfoFlagByFlowsn("1", flowsn);
                    }
                    else {
                        rowguid = project.getRowguid();
                        pviguid = project.getPviguid();
                        service.updateInfoFlagByFlowsn("9", flowsn);
                    }
                    List<Record> procinfolist = service.getProcFromQzkByProjid(flowsn);
                    for (Record record2 : procinfolist) {
                        //插入流程信息表
                        WorkflowWorkItemHistory workflow = new WorkflowWorkItemHistory();
                        workflow.setActivityName(record2.getStr("NODENAME"));
                        workflow.setWorkItemGuid(UUID.randomUUID().toString());
                        workflow.setTransactorName(record2.getStr("NODEPROCERNAME"));
                        workflow.setCreateDate(record2.getDate("NODESTARTTIME"));
                        workflow.setEndDate(record2.getDate("NODEENDTIME"));
                        workflow.setOpinion(record2.getStr("NODEADV"));
                        workflow.setOperatorForDisplayName(record2.getStr("NODEPROCERNAME"));
                        workflow.setOperationDate(new Date());
                        workflow.setProcessGuid(task.getProcessguid());
                        workflow.setProcessVersionInstanceGuid(pviguid);
                        workflow.setOperatorName(record2.getStr("NODENAME"));
                        workflow.set("projectguid", rowguid);
                        service.updateProcFlagByFlowsn("1",flowsn,record2.getInt("sn"));
                        service.insertbyrecord(workflow);
                    }
                }
                else {
                    service.updateInfoFlagByFlowsn("2", flowsn);
                    log.info("同步浪潮自建系统数据 =====办件对应事项不存在:" + taskname + "办件编号为：" + flowsn +"辖区："+areacode);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                service.updateInfoFlagByFlowsn("3", flowsn);
                log.info("同步浪潮自建系统数据失败 =====" + e.getMessage()+record);

            }
        }
        log.info("同步浪潮自建系统数据结束"+infolist.size());
    }

    
}
