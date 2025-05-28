package com.epoint.tongbufw;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.jn.inproject.api.IWebUploaderService;
import com.epoint.jn.inproject.api.entity.eajcstepbasicinfogt;
import com.epoint.jn.inproject.api.entity.eajcstepdonegt;
import com.epoint.jn.inproject.api.entity.eajcstepprocgt;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@DisallowConcurrentExecution
public class ByslSyncProject implements Job {
    transient Logger log = LogUtil.getLog(ByslSyncProject.class);


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            byslSyncProjectService service = new byslSyncProjectService();
            List<AuditProject> projectList = service.getProjectbytsflag();
            IWebUploaderService IWebUploaderService = ContainerFactory.getContainInfo().getComponent(IWebUploaderService.class);
            IAuditTaskMaterial iAuditTaskMaterial = ContainerFactory.getContainInfo().getComponent(IAuditTaskMaterial.class);
            IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);

            EpointFrameDsManager.begin(null);
            long startTime = System.currentTimeMillis();
            //办件状态更新办件办结信息
            log.info("开始同步不予受理办件");
            for (AuditProject project : projectList) {
                AuditTask task = iAuditTask.getAuditTaskByGuid(project.getTaskguid(), true).getResult();
                if (task != null) {
                    FrameOu frameOu = IWebUploaderService.getFrameOuByOuname(project.getOuname());

                    if (StringUtil.isBlank(task.getStr("inner_code")) || StringUtil.isBlank(frameOu.getOucode())) {
                        //不为浪潮的办件信息
                        service.updateProjectIssynacwaveByRowguid("94", project.getRowguid());
                        continue;
                    }

                    String Orgbusno = UUID.randomUUID().toString();
                    eajcstepbasicinfogt baseinfo = new eajcstepbasicinfogt();
                    baseinfo.set("rowguid", Orgbusno);
                    baseinfo.setOrgbusno(Orgbusno);
                    baseinfo.setProjid(project.getFlowsn());
                    baseinfo.setProjpwd(project.getStr("pwd"));
                    baseinfo.setValidity_flag("1");
                    baseinfo.setDataver("1");
                    baseinfo.setStdver("1");
                    baseinfo.setItemno(task.getStr("inner_code"));
                    List<AuditTaskMaterial> materials = iAuditTaskMaterial.getUsableMaterialListByTaskguid(task.getRowguid()).getResult();
                    String materialname = "";
                    if (materials != null && materials.size() > 0) {
                        for (AuditTaskMaterial material : materials) {
                            materialname += "[纸质]" + material.getMaterialname() + ";";
                        }
                    } else {
                        materialname = "无";
                    }
                    baseinfo.set("ACCEPTLIST", materialname);
                    String shenpilb = "";
                    if ("11".equals(task.getShenpilb())) {
                        shenpilb = "20";
                    } else if ("10".equals(task.getShenpilb())) {
                        shenpilb = "10";
                    } else if ("07".equals(task.getShenpilb())) {
                        shenpilb = "07";
                    } else if ("06".equals(task.getShenpilb())) {
                        shenpilb = "08";
                    } else if ("05".equals(task.getShenpilb())) {
                        shenpilb = "05";
                    } else if ("02".equals(task.getShenpilb())) {
                        shenpilb = "02";
                    } else if ("01".equals(task.getShenpilb())) {
                        shenpilb = "01";
                    } else if ("03".equals(task.getShenpilb())) {
                        shenpilb = "03";
                    } else if ("04".equals(task.getShenpilb())) {
                        shenpilb = "04";
                    } else if ("08".equals(task.getShenpilb())) {
                        shenpilb = "09";
                    } else if ("09".equals(task.getShenpilb())) {
                        shenpilb = "06";
                    }

                    baseinfo.set("ITEMTYPE", shenpilb);
                    baseinfo.set("CATALOGCODE", task.getStr("CATALOGCODE"));
                    baseinfo.set("TASKCODE", task.getStr("TASKCODE"));
					if (10 == project.getApplyertype()) {
						baseinfo.set("APPLYERTYPE", "2");
						baseinfo.set("ApplyerPageType", "01");
					} else {
						baseinfo.set("APPLYERTYPE", "1");
						baseinfo.set("ApplyerPageType", "111");
					}
					//申请人证件号码
					baseinfo.set("ApplyerPageCode", project.getCertnum());
                    baseinfo.setItemname(task.getTaskname());
                    baseinfo.setProjectname(project.getProjectname());
                    baseinfo.setApplicant(project.getApplyername());
                    baseinfo.setApplicantCardCode(project.getCertnum());
                    baseinfo.setApplicanttel(project.getContactmobile());
                    baseinfo.setAcceptdeptid(frameOu.getOucode());
                    baseinfo.setRegion_id(task.getAreacode().replace("370882", "370812") + "000000");
                    baseinfo.setAcceptdeptname(frameOu.getOuname());
                    baseinfo.setApprovaltype("2");
                    baseinfo.setPromisetimelimit("0");
                    baseinfo.setPromisetimeunit("1");
                    baseinfo.setTimelimit("0");
                    baseinfo.setItemregionid(project.getAreacode().replace("370882", "370812"));
                    if ("22".equals(project.getCerttype())) {
                        baseinfo.setApplicantCardtype("111");
                    } else if ("16".equals(project.getCerttype())) {
                        baseinfo.setApplicantCardtype("01");
                    } else if ("14".equals(project.getCerttype())) {
                        baseinfo.setApplicantCardtype("02");
                    }
                    baseinfo.setSubmit("1");
                    baseinfo.setOccurtime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    baseinfo.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    baseinfo.set("Status", "0");
                    if (StringUtil.isNotBlank(project.getCertnum())) {
                        baseinfo.setAcceptdeptcode(project.getCertnum());
                    } else {
                        baseinfo.setAcceptdeptcode("无");
                    }
                    baseinfo.setAcceptdeptcode1("无");
                    baseinfo.setAcceptdeptcode2("无");

                    IWebUploaderService.insertQzkBaseInfo(baseinfo);

                    //插入办件流程表（受理步骤）
                    eajcstepprocgt process = new eajcstepprocgt();
                    process.set("rowguid", UUID.randomUUID().toString());
                    process.setOrgbusno(Orgbusno);
                    process.setProjid(project.getFlowsn());
                    process.setValidity_flag("1");
                    process.setDataver("1");
                    process.setStdver("1");
                    process.setSn("1");
                    process.setNodename("受理");
                    process.setNodecode("act1");
                    process.setNodetype("1");
                    process.setNodeprocer(UUID.randomUUID().toString());
                    process.setNodeprocername(project.getAcceptusername());
                    process.setNodeprocerarea(project.getAreacode().replace("370882", "370812") + "000000");
                    process.setRegion_id(project.getAreacode().replace("370882", "370812") + "000000");
                    process.setProcunit(frameOu.getOucode());
                    process.setProcunitname(frameOu.getOuname());
                    process.setNodestate("02");
                    String accepttime = EpointDateUtil.convertDate2String(project.getBanjiedate(), EpointDateUtil.DATE_TIME_FORMAT);
                    process.setNodestarttime(accepttime);
                    process.setNodeendtime(accepttime);
                    process.setNoderesult("4");
                    process.setOccurtime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    process.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    process.setSignstate("0");
                    process.setItemregionid(project.getAreacode().replace("370882", "370812"));
                    IWebUploaderService.insertQzkProcess(process);

                    eajcstepdonegt done = new eajcstepdonegt();
                    done.set("rowguid", UUID.randomUUID().toString());
                    done.setOrgbusno(Orgbusno);
                    done.setProjid(project.getFlowsn());
                    done.setValidity_flag("1");
                    done.setStdver("1");
                    done.setRegion_id(project.getAreacode().replace("370882", "370812") + "000000");
                    done.setDataver("1");
                    done.setDoneresult("6");
                    done.setApprovallimit(new Date());
                    done.setCertificatenam("111");
                    done.setCertificateno(project.getCertnum());
                    done.setIsfee("0");
                    done.setOccurtime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    done.setTransactor(project.getAcceptusername());
                    done.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    done.setSignstate("0");
                    done.setItemregionid(project.getAreacode().replace("370882", "370812"));
                    IWebUploaderService.insertQzkDone(done);

                    //插入办件流程表（已办结）
                    eajcstepprocgt process1 = new eajcstepprocgt();
                    process1.set("rowguid", UUID.randomUUID().toString());
                    process1.setOrgbusno(Orgbusno);
                    process1.setProjid(project.getFlowsn());
                    process1.setValidity_flag("1");
                    process1.setDataver("1");
                    process1.setStdver("1");
                    process1.setSn("2");
                    process1.setNodename("办结");
                    process1.setNodecode("act2");
                    process1.setNodetype("3");
                    process1.setNodeprocer(project.getAcceptuserguid());
                    process1.setNodeprocername(project.getAcceptusername());
                    process1.setNodeprocerarea(project.getAreacode().replace("370882", "370812") + "000000");
                    process1.setRegion_id(project.getAreacode().replace("370882", "370812") + "000000");
                    process1.setProcunit(frameOu.getOucode());
                    process1.setProcunitname(frameOu.getOuname());
                    process1.setNodestate("02");
                    String banjietime = EpointDateUtil.convertDate2String(project.getBanjiedate(), EpointDateUtil.DATE_TIME_FORMAT);
                    process1.setNodestarttime(banjietime);
                    process1.setNodeendtime(banjietime);
                    process1.setNoderesult("3");
                    process1.setOccurtime(EpointDateUtil.convertDate2String(project.getBanjiedate(), "yyyy-MM-dd HH:mm:ss"));
                    process1.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    process1.setSignstate("0");
                    process1.setItemregionid(project.getStr("areacode").replace("370882", "370812"));
                    IWebUploaderService.insertQzkProcess(process1);

                    service.updateProjectIssynacwaveByRowguid("97", project.getRowguid());
                }
            }
            //结束之间
            long endTime = System.currentTimeMillis();
            //程序块运行时间
            log.info("getProjectbytsflag运行时间为：" + (endTime - startTime) / 1000 + "秒");
            EpointFrameDsManager.commit();
        } catch (Exception e) {
            EpointFrameDsManager.rollback();
            log.info("同步失败 =====" + e.getMessage());
        } finally {
            EpointFrameDsManager.close();
        }
    }

}
