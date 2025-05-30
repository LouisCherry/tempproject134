package com.epoint.hcp.job;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.hcp.api.IHcpService;
import com.epoint.hcp.api.IWebUploaderService;
import com.epoint.hcp.api.entity.eajcstepbasicinfogt;
import com.epoint.hcp.api.entity.eajcstepdonegt;
import com.epoint.hcp.api.entity.eajcstepprocgt;
import com.epoint.hcp.api.entity.lcprojectten;
import com.epoint.hcp.api.entity.lcprojectthree;

/**
 *  [实施主体表数据检查]
 *  [功能详细描述]
 * @作者 gowonco
 * @version [版本号, 2018年9月14日]
 * @see [相关类/方法]
 * @since [产品/模块版本] 
 */
public class InProjectMoveCheck implements Callable<String>
{

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(this.getClass());
    
    
    IHcpService iHcpService = ContainerFactory.getContainInfo().getComponent(IHcpService.class);
	
	IOuService ouservice = ContainerFactory.getContainInfo().getComponent(IOuService.class);
	
	ICodeItemsService iCodeItemsService = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
	
	IAuditProject auditProjectServcie = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
	
	IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
	
	
	IWebUploaderService service = ContainerFactory.getContainInfo().getComponent(IWebUploaderService.class);
	
	IAuditTaskMaterial iAuditTaskMaterial = ContainerFactory.getContainInfo().getComponent(IAuditTaskMaterial.class);
    /**
     * 错误数据rowguid
     */
    @SuppressWarnings("unused")
    private List<String> errRowguidList;
    /**
     * 线程计数器
     */
    public CountDownLatch latch;

    public InProjectMoveCheck() {
        super();
    }

    public void dealResult() {
        int recordNum = 1000;
        boolean flag = true;
        
        //List<String> gssOrgList = service.getGssRowguids();
        ExecutorService exService = null;
        int size = 100;
        int start = 0;
        if (recordNum % size == 0) {
            latch = new CountDownLatch(recordNum / size);
        }
        else {
            latch = new CountDownLatch(recordNum / size + 1);
        }
        exService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        while (flag) {
            log.info("----------------------本次查询迁移办件数量---"+recordNum+"------------------");
            if (recordNum <= size) {
                flag = false;
                size = recordNum;
            }
            exService.execute(new Task(start, size));
            recordNum = recordNum - size;
            start = start + size;
            log.info("-------------while-end-zone------------------");
        }
        exService.shutdown();
        try {
            latch.await();
            log.info(">>>>>>>>>>>>>>>>>>>>check-end-zone>>>>>>>>>>>>>>>>>");
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0;) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String call() throws Exception {
        dealResult();
        return "check-zone";
    }

    class Task implements Runnable
    {

        public int size;
        public int start;

        public Task(int start, int size) {
            this.size = size;
            this.start = start;
        }

        @Override
        public void run() {
            try {
            	
                List<lcprojectten> projects = iHcpService.getProjectMoveList(start, size);
                log.info("-------------for-start-zoneCheck------------------");
                EpointFrameDsManager.begin(null);
                try {
                	for (lcprojectten project : projects) {
                		AuditTask task = iAuditTask.getAuditTaskByGuid(project.getTaskguid(), true).getResult();
                		FrameOu frameOu = service.getFrameOuByOuname(project.getOuname());
                		
                		lcprojectthree threeproject = new lcprojectthree();
                		threeproject.putAll(project);
                		service.insert(threeproject);
                		project.set("sync", "1");
                		service.update(project);
                		EpointFrameDsManager.commit();
                		
                		if (task != null && frameOu != null) {
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
                        			materialname += "[纸质]"+material.getMaterialname()+";";
                        		}
                        	}else {
                        		materialname = "无";
                        	}
                        	baseinfo.set("ACCEPTLIST", materialname);
                        	String shenpilb = "";
                        	if ("11".equals(task.getShenpilb())) {
                        		shenpilb = "20";
                        	}
                        	else if ("10".equals(task.getShenpilb())) {
                        		shenpilb = "10";
                        	}
                        	else if ("07".equals(task.getShenpilb())) {
                        		shenpilb = "07";
                        	}
                        	else if ("06".equals(task.getShenpilb())) {
                        		shenpilb = "08";
                        	}
                        	else if ("05".equals(task.getShenpilb())) {
                        		shenpilb = "05";
                        	}
                        	else if ("02".equals(task.getShenpilb())) {
                        		shenpilb = "02";
                        	}
                        	else if ("01".equals(task.getShenpilb())) {
                        		shenpilb = "01";
                        	}
                        	else if ("03".equals(task.getShenpilb())) {
                        		shenpilb = "03";
                        	}
                        	else if ("04".equals(task.getShenpilb())) {
                        		shenpilb = "04";
                        	}
                        	else if ("08".equals(task.getShenpilb())) {
                        		shenpilb = "09";
                        	}
                        	else if ("09".equals(task.getShenpilb())) {
                        		shenpilb = "06";
                        	}
                        	
                        	baseinfo.set("ITEMTYPE", shenpilb);
                        	baseinfo.set("CATALOGCODE", task.getStr("CATALOGCODE"));
                        	baseinfo.set("TASKCODE", task.getStr("TASKCODE"));
                        	if (10 == project.getApplyertype()) {
                        		baseinfo.set("APPLYERTYPE", "2");
                        	}else {
                        		baseinfo.set("APPLYERTYPE", "1");
                        	}
                        	//baseinfo.setSubitemno(SUBITEMNO);
                        	baseinfo.setItemname(task.getTaskname());
                        	baseinfo.setProjectname(project.getProjectname());
                        	//baseinfo.setSubitemname(SUBITEMNAME);
                        	//baseinfo.setAPPITEMNAME();
                        	baseinfo.setApplicant(project.getApplyername());
                        	baseinfo.setApplicantCardCode(project.getCertnum());
                        	//baseinfo.setApplicantmobile(APPLICANTMOBILE);
                        	baseinfo.setApplicanttel(project.getContactmobile());
                        	//baseinfo.setApplicantemail(APPLICANTEMAIL);
                        	baseinfo.setAcceptdeptid(frameOu.getOucode());
                        	baseinfo.setRegion_id(task.getAreacode().replace("370882", "370812")+"000000");
                        	baseinfo.setAcceptdeptname(frameOu.getOuname());
                        	baseinfo.setApprovaltype("2");
                        	baseinfo.setPromisetimelimit("0");
                        	baseinfo.setPromisetimeunit("1");
                        	baseinfo.setTimelimit("0");
                        	baseinfo.setItemregionid(project.getAreacode().replace("370882", "370812"));
                        	if ("22".equals(project.getCerttype())) {
                        		baseinfo.setApplicantCardtype("111");
                        	}
                        	else if ("16".equals(project.getCerttype())) {
                        		baseinfo.setApplicantCardtype("01");
                        	}
                        	else if ("14".equals(project.getCerttype())) {
                        		baseinfo.setApplicantCardtype("02");
                        	}
                        	//baseinfo.setTimeunit(TIMEUNIT);
                        	baseinfo.setSubmit("1");
                        	//baseinfo.setERRORCODE
                        	//baseinfo.setERRORMSG
                        	baseinfo.setOccurtime(new Date());
                        	//baseinfo.setTransactor(TRANSACTOR);
                        	baseinfo.setMaketime(new Date());
                        	baseinfo.set("Status", "0");
                        	if (StringUtil.isNotBlank(project.getCertnum())) {
                        		baseinfo.setAcceptdeptcode(project.getCertnum());
                        	}else {
                        		baseinfo.setAcceptdeptcode("无");
                        	}
                        	baseinfo.setAcceptdeptcode1("无");
                        	baseinfo.setAcceptdeptcode2("无");
                        	//baseinfo.setSCANFLAG
                        	//baseinfo.setERRORCODE1
                        	//baseinfo.setERRORMSG1
                        	//baseinfo.setSTATUS1
                        	//FIXME 111
                        	service.insertQzkBaseInfo(baseinfo);
                        	
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
                        	process.setNodeprocerarea(project.getAreacode().replace("370882", "370812")+"000000");
                        	process.setRegion_id(project.getAreacode().replace("370882", "370812")+"000000");
                        	process.setProcunit(frameOu.getOucode());
                        	process.setProcunitname(frameOu.getOuname());
                        	process.setNodestate("02");
                        	String accepttime = EpointDateUtil.convertDate2String(project.getAcceptuserdate(), EpointDateUtil.DATE_TIME_FORMAT);
    						process.setNodestarttime(accepttime);
                            process.setNodeendtime(accepttime);
                        	process.setNoderesult("4");
                        	process.setOccurtime(new Date());
                        	process.setMaketime(new Date());
                        	process.setSignstate("0");
                        	process.setItemregionid(project.getAreacode().replace("370882", "370812"));
                        	//FIXME 111
                        	service.insertQzkProcess(process);
                        	
                        	eajcstepdonegt done = new eajcstepdonegt();
                    		done.set("rowguid", UUID.randomUUID().toString());
                    		done.setOrgbusno(Orgbusno);
                    		done.setProjid(project.getFlowsn());
                    		done.setValidity_flag("1");
                    		done.setStdver("1");
                    		done.setRegion_id(project.getAreacode().replace("370882", "370812")+"000000");
                    		done.setDataver("1");
                    		done.setDoneresult("0");
                    		done.setApprovallimit(new Date());
                    		done.setCertificatenam("111");
                    		done.setCertificateno(project.getCertnum());
                    		done.setIsfee("0");
                    		done.setOccurtime(new Date());
                    		done.setTransactor(project.getAcceptusername());
                    		done.setMaketime(new Date());
                    		done.setSignstate("0");
                    		done.setItemregionid(project.getAreacode().replace("370882", "370812"));
                    		//FIXME 111
                    		service.insertQzkDone(done);
                    		
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
                        	process1.setNodeprocer(UUID.randomUUID().toString());
                        	process1.setNodeprocername(project.getAcceptusername());
                        	process1.setNodeprocerarea(project.getAreacode().replace("370882", "370812")+"000000");
                        	process1.setRegion_id(project.getAreacode().replace("370882", "370812")+"000000");
                        	process1.setProcunit(frameOu.getOucode());
                        	process1.setProcunitname(frameOu.getOuname());
                        	process1.setNodestate("02");
                        	String banjietime = EpointDateUtil.convertDate2String(project.getBanjiedate(), EpointDateUtil.DATE_TIME_FORMAT);
                        	process1.setNodestarttime(banjietime);
                        	process1.setNodeendtime(banjietime);
                        	process1.setNoderesult("1");
                        	process1.setOccurtime(project.getBanjiedate());
                        	process1.setMaketime(new Date());
                        	process1.setSignstate("0");
                        	process1.setItemregionid(project.getStr("areacode").replace("370882", "370812"));
                        	//FIXME 111
                        	service.insertQzkProcess(process1);
                        	EpointFrameDsManager.commit();
                		}
                		
                	}
                	EpointFrameDsManager.commit();
                   log.info("-------------for-end-zoneCheck------------------");
                }
                catch (Exception e) {
                    EpointFrameDsManager.rollback();
                    e.printStackTrace();
                }
                finally {
                    EpointFrameDsManager.close();
                }
                log.info("-------------for-end-zoneCheck------------------");
                
            }
            finally {
                latch.countDown();
            }
        }
    }
    

}
