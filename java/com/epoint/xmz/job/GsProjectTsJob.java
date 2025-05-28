package com.epoint.xmz.job;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.jn.inproject.api.IWebUploaderService;
import com.epoint.jn.inproject.api.entity.eajcstepbasicinfogt;
import com.epoint.jn.inproject.api.entity.eajcstepdonegt;
import com.epoint.jn.inproject.api.entity.eajcstepprocgt;
import com.epoint.xmz.job.api.IGsProjectService;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.invoke.MethodHandles;
import java.util.*;

//防止并发执行导致数据重复
@DisallowConcurrentExecution
public class GsProjectTsJob implements Job {

    transient static Logger log = LogUtil.getLog(MethodHandles.lookup().lookupClass());

    IGsProjectService iGsProjectService = ContainerFactory.getContainInfo().getComponent(IGsProjectService.class);
    IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
    IAuditProject iAuditProject = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
    IOuService iOuService = ContainerFactory.getContainInfo().getComponent(IOuService.class);
    IWebUploaderService service = ContainerFactory.getContainInfo().getComponent(IWebUploaderService.class);
    IAuditTaskMaterial iAuditTaskMaterial = ContainerFactory.getContainInfo().getComponent(IAuditTaskMaterial.class);

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            doService();
            EpointFrameDsManager.commit();
        } catch (Exception e) {
            EpointFrameDsManager.rollback();
            e.printStackTrace();
        } finally {
            EpointFrameDsManager.close();
        }
    }

    private void doService() {

        try {
            log.info("===============开始推送公积金社保的办件数据===============");
            List<AuditProject> projects = iGsProjectService.getWaitEvaluateSbList();
            if (projects == null || projects.size() == 0) {
                log.info("===============无需要推送的公积金社保办件数据，结束服务===============");
            } else {
                Map<String, String> taskToMaterail = new HashMap<>();
                log.info("size:"+projects.size());
                for (AuditProject project : projects) {
                    log.info("flowsn:"+project.getFlowsn());
                    AuditTask audittask = iAuditTask.getAuditTaskByGuid(project.getTaskguid(), false).getResult();
                    if (audittask != null) {
                        FrameOu frameOu = iOuService.getOuByOuGuid(project.getOuguid());
                        if (frameOu != null) {
                            eajcstepbasicinfogt baseinfo = new eajcstepbasicinfogt();
//                            baseinfo.set("rowguid", project.getRowguid());
                            baseinfo.setOrgbusno(project.getRowguid());
                            baseinfo.setProjid(project.getFlowsn());
                            baseinfo.setProjpwd("111111");
                            baseinfo.setValidity_flag("1");
                            baseinfo.setDataver("1");
                            baseinfo.setStdver("1");
                            baseinfo.setItemno(audittask.getStr("inner_code"));

                            String materialname = "";
                            if (taskToMaterail.containsKey(audittask.getItem_id())) {
                                materialname = taskToMaterail.get(audittask.getItem_id());
                            } else {
                                List<AuditTaskMaterial> materials = iAuditTaskMaterial.getUsableMaterialListByTaskguid(project.getTaskguid()).getResult();
                                if (materials != null && materials.size() > 0) {
                                    for (AuditTaskMaterial material : materials) {
                                        materialname += "[纸质]" + material.getMaterialname() + ";";
                                    }
                                } else {
                                    materialname = "无";
                                }
                                taskToMaterail.put(audittask.getItem_id(), materialname);
                            }
                            baseinfo.setAcceptlist(materialname);
                            String shenpilb = "";
                            if ("11".equals(audittask.getShenpilb())) {
                                shenpilb = "20";
                            } else if ("10".equals(audittask.getShenpilb())) {
                                shenpilb = "10";
                            } else if ("07".equals(audittask.getShenpilb())) {
                                shenpilb = "07";
                            } else if ("06".equals(audittask.getShenpilb())) {
                                shenpilb = "08";
                            } else if ("05".equals(audittask.getShenpilb())) {
                                shenpilb = "05";
                            } else if ("02".equals(audittask.getShenpilb())) {
                                shenpilb = "02";
                            } else if ("01".equals(audittask.getShenpilb())) {
                                shenpilb = "01";
                            } else if ("03".equals(audittask.getShenpilb())) {
                                shenpilb = "03";
                            } else if ("04".equals(audittask.getShenpilb())) {
                                shenpilb = "04";
                            } else if ("08".equals(audittask.getShenpilb())) {
                                shenpilb = "09";
                            } else if ("09".equals(audittask.getShenpilb())) {
                                shenpilb = "06";
                            }
                            baseinfo.set("ITEMTYPE", shenpilb);
                            baseinfo.set("CATALOGCODE", audittask.getStr("CATALOGCODE"));
                            baseinfo.set("TASKCODE", audittask.getStr("TASKCODE"));
							if (10 == project.getApplyertype()) {
								baseinfo.set("APPLYERTYPE", "2");
								baseinfo.set("ApplyerPageType", "01");
							} else {
								baseinfo.set("APPLYERTYPE", "1");
								baseinfo.set("ApplyerPageType", "111");
							}
							//申请人证件号码
							baseinfo.set("ApplyerPageCode", project.getCertnum());
                            baseinfo.setItemname(audittask.getTaskname());
                            baseinfo.setProjectname(project.getProjectname());
                            baseinfo.setApplicant(StringUtil.isNotBlank(project.getApplyername()) ? project.getApplyername() : "无");
                            baseinfo.setApplicantCardCode(project.getCertnum());
                            baseinfo.setApplicanttel(project.getContactmobile());
                            baseinfo.setAcceptdeptid(frameOu.getOucode());
                            baseinfo.setRegion_id(audittask.getAreacode() + "000000");
                            baseinfo.setAcceptdeptname(frameOu.getOuname());
                            baseinfo.setApprovaltype("2");
                            baseinfo.setPromisetimelimit("0");
                            baseinfo.setPromisetimeunit("1");
                            baseinfo.setTimelimit("0");
                            baseinfo.setItemregionid("370800000000");
                            if ("22".equals(project.getCerttype())) {
                                baseinfo.setApplicantCardtype("111");
                            } else if ("16".equals(project.getCerttype())) {
                                baseinfo.setApplicantCardtype("01");
                            } else if ("14".equals(project.getCerttype())) {
                                baseinfo.setApplicantCardtype("02");
                            }

                            baseinfo.setSubmit("1");
                            baseinfo.setOccurtime(EpointDateUtil.convertDate2String(project.getAcceptuserdate(), "yyyy-MM-dd HH:mm:ss"));
                            baseinfo.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                            baseinfo.set("Status", "0");
                            if (StringUtil.isNotBlank(project.getCertnum())) {
                                baseinfo.setAcceptdeptcode(project.getCertnum());
                            } else {
                                baseinfo.setAcceptdeptcode("无");
                            }
                            baseinfo.setAcceptdeptcode1("无");
                            baseinfo.setAcceptdeptcode2("无");

                            service.insertQzkBaseInfo(baseinfo);
                            EpointFrameDsManager.commit();

                            //插入办件流程表（受理步骤）
                            eajcstepprocgt process = new eajcstepprocgt();
                            process.set("rowguid", UUID.randomUUID().toString());
                            process.setOrgbusno(project.getRowguid());
                            process.setProjid(project.getFlowsn());
                            process.setValidity_flag("1");
                            process.setDataver("1");
                            process.setStdver("1");
                            process.setSn("1");
                            process.setNodename("受理");
                            process.setNodecode("act1");
                            process.setNodetype("4");
                            process.setNodeprocer(project.getAcceptusername());
                            process.setNodeprocername(project.getAcceptusername());
                            process.setNodeprocerarea("370800000000");
                            process.setRegion_id("370800000000");
                            process.setProcunit(frameOu.getOucode());
                            process.setProcunitname(frameOu.getOuname());
                            process.setNodestate("02");
                            process.setNodestarttime(EpointDateUtil.convertDate2String(project.getAcceptuserdate(), "yyyy-MM-dd HH:mm:ss"));
                            process.setNodeendtime(EpointDateUtil.convertDate2String(project.getAcceptuserdate(), "yyyy-MM-dd HH:mm:ss"));
                            process.setNoderesult("4");
                            process.setOccurtime(EpointDateUtil.convertDate2String(project.getAcceptuserdate(), "yyyy-MM-dd HH:mm:ss"));
                            process.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                            process.setSignstate("0");
                            process.setItemregionid("370800000000");
                            service.insertQzkProcess(process);
                            EpointFrameDsManager.commit();
                            //推送办结表信息
                            eajcstepdonegt done = new eajcstepdonegt();
                            done.set("rowguid", UUID.randomUUID().toString());
                            done.setOrgbusno(project.getRowguid());
                            done.setProjid(project.getFlowsn());
                            done.setValidity_flag("1");
                            done.setStdver("1");
                            done.setRegion_id("370800000000");
                            done.setDataver("1");
                            done.setDoneresult("0");
                            done.setApprovallimit(new Date());
                            done.setCertificatenam("111");
                            done.setCertificateno(project.getCertnum());
                            done.setIsfee("0");
                            done.setOccurtime(EpointDateUtil.convertDate2String(project.getAcceptuserdate(), "yyyy-MM-dd HH:mm:ss"));
                            done.setTransactor(project.getAcceptusername());
                            done.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                            done.setSignstate("0");
                            done.setItemregionid("370800000000");
                            service.insertQzkDone(done);
                            EpointFrameDsManager.commit();
                            project.set("zjsync", "1");
                            iAuditProject.updateProject(project);
                        } else {
                            project.set("zjsync", "3");
                            iAuditProject.updateProject(project);
                            log.info("===============办件对应的部门未查询到===============");
                        }
                    } else {
                        project.set("zjsync", "2");
                        iAuditProject.updateProject(project);
                        log.info("===============办件对应的事项未查询到===============");
                    }
                }
                log.info("===============结束推送公积金社保办件===============");
            }

        } catch (Exception e) {
            log.info("===============推送失败，服务异常===============");
            e.printStackTrace();
        }
    }
}
