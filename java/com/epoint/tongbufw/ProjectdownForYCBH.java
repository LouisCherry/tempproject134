package com.epoint.tongbufw;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;

@DisallowConcurrentExecution
public class ProjectdownForYCBH implements Job
{
    transient Logger log = LogUtil.getLog(ProjectdownForYCBH.class);

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
        ProjectdownForYCBHService service = new ProjectdownForYCBHService();
        List<Record> infolist = service.getAreaList();
        String url = "";
        String result = "";
        List<String> list = new ArrayList<String>();
        int count = 0;
        for (Record record : infolist) {
            url = "http://60.208.61.158:8088/socialorg/socExternal/querywaiw.action?morgArea="+record.getStr("areacode")+"&ID="+record.getStr("areaid");
            result = HttpUtil.doGet(url);
            log.info("=========================>开始同步省民政厅办件数据");
            JSONObject json = JSON.parseObject(result);
            JSONArray array = json.getJSONArray("rows");
            for (int i = 0;i<array.size();i++) {
                count++;
                JSONObject jsonobject = array.getJSONObject(i);
                String flowsn = jsonobject.getString("PROJID");
                String taskname = jsonobject.getString("ITEMNAME");
                String INNERNO = jsonobject.getString("INNERNO");
                String regionid = jsonobject.getString("REGION_ID");
                String areacode = "";
                if (StringUtil.isBlank(regionid)) {
                    areacode = "370000";
                }else {
                    areacode = regionid.substring(0, 6);
                }
                AuditTask task = service.getAuditTask(INNERNO, areacode);
                try {
                    if (task != null) {
                        AuditProject project = service.getProjectByflowsn(flowsn);
                        String rowguid = "";
                        String pviguid = "";
                        IAuditProject projectService = (IAuditProject) ContainerFactory.getContainInfo()
                                .getComponent(IAuditProject.class);
                        if (project == null) {
                            rowguid = UUID.randomUUID().toString();
                            pviguid = UUID.randomUUID().toString();
                            project = new AuditProject();
                            project.setRowguid(rowguid);
                            project.setPviguid(pviguid);
                            project.setFlowsn(flowsn);
                            project.setAreacode(areacode);
                            int applyway = Integer.parseInt(jsonobject.getString("SUBMIT"));
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
                            project.setReceivedate(jsonobject.getDate("MAKETIME"));//毫秒
                            project.setReceiveusername(jsonobject.getString("TRANSACTOR"));
                            project.setAcceptuserdate(jsonobject.getDate("MAKETIME"));//毫秒
                            project.setAcceptusername(jsonobject.getString("TRANSACTOR"));
                            project.setProjectname(jsonobject.getString("PROJECTNAME"));
                            project.setApplydate(jsonobject.getDate("OCCURTIME"));//毫秒
                            project.setApplyername(jsonobject.getString("APPLICANT"));
                            //project.setCertnum(record.getStr("APPLICANTCARDCODE"));
                            project.setContactmobile(jsonobject.getString("APPLICANTMOBILE"));
                            project.setContactphone(jsonobject.getString("APPLICANTTEL"));
                            project.setContactemail(jsonobject.getString("APPLICANTEMAIL"));
                            project.setApplyertype(10);
                            project.setOuguid(task.getOuguid());
                            project.setOuname(task.getOuname());
                            project.setOperatedate(new Date());
                            project.setTask_id(task.getTask_id());
                            project.setTasktype(task.getType());
                            project.setTaskguid(task.getRowguid());
                            project.setCerttype(ZwfwConstant.CERT_TYPE_ZZJGDMZ);
                            project.setStatus(80);
                            project.setOperateusername("省民政厅推送数据");
                            //is_lczj null,0：一窗受理系统办件，1，浪潮对接自建办件，2：新点对接济宁市自建办件,4 电力对接的办件，3扫码办件,5:对接省民政厅接口数据
                            project.set("is_lczj", 5);
                            projectService.addProject(project);
                        }
                    }
                    else {
                        if (!list.contains(taskname)) {
                            list.add(taskname);
                        }
                        //log.info("同步省民政厅推送数据 =====办件对应事项不存在:" + taskname + "办件编号为：" + flowsn +"辖区："+areacode);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    log.info("同步省民政厅推送数据失败 =====" + e.getMessage()+record);
                }
            }
        }
        log.info("同步省民政厅推送数据结束"+infolist.size());
    }
}
