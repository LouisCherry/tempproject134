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
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.jnycsl.utils.WavePushInterfaceUtils;
import com.epoint.workflow.bizlogic.domain.execute.WorkflowWorkItemHistory;
import com.alibaba.fastjson.JSONObject;

@DisallowConcurrentExecution
public class EpointSyncHpProject implements Job
{

    transient Logger log = LogUtil.getLog(EpointSyncHpProject.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {
            EpointFrameDsManager.begin(null);
            //job作业比框架起的早 导致部分接口没有实例化
            Thread.sleep(10000);
            doService();
            EpointFrameDsManager.commit();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            EpointFrameDsManager.close();
        }

    }

    private void doService() {
        try {
            List<Record> list2 = null;
            EpointSyncHpProjectService service = new EpointSyncHpProjectService();
            list2 = service.getProjectList();
            
            for (Record record : list2) {
            	
            	List<Record> proejcts = service.getProjectDetail(record.getStr("XH"));
            	
            	for (Record project : proejcts) {
            		
            	}
                AuditProject auditProject = new AuditProject();
                String areacode = record.getStr("areacode");
                switch(areacode){
                    case "370811":;
                    case "370882":;
                    case "370890":;
                    case "370891":;
                    case "370892": areacode ="370800";
                    break;
                }
                //申报流水号
                String PROJID = record.get("PROJID");
                //申请方式
                String SQFS = record.get("SQFS");
                //申报事项编码
                String SBSXBM = record.get("SBSXBM");
                //申请单位名称
                String SQDWMC = record.get("SQDWMC");
                //统一社会信用代码
                String TYSHXYDM = record.get("TYSHXYDM");
                //申请单位类型
                String SQDWLX = record.get("SQDWLX");
                //申请人类型
                String SQRLX = record.get("SQRLX");
                //申请人名称
                String BLR = record.get("BLR");
                //申请人联系手机
                String BLRLXSJ = record.get("BLRLXSJ");
                //申请人证件类型
                String SQRZJLX = record.get("SQRZJLX");
                //申请人证件号码
                String SQRZJHM = record.get("SQRZJHM");
                //证件有限期起
                String ZJYXQQ = record.get("ZJYXQQ");
                //证件有效期止
                String ZJYXQZ = record.get("ZJYXQZ");
                //法定代表人
                String FDDBR = record.get("FDDBR");
                //法定代表人类型
                String FDDBRLX = record.get("FDDBRLX");
                //法定代表人证件类型
                String FDDBRZJLX = record.get("FDDBRZJLX");
                //企业类型代码
                String QYLXDM = record.get("QYLXDM");
                //企业类型名称
                String QYLXMC = record.get("QYLXMC");
                //机构英文名称
                String JGYWMC = record.get("JGYWMC");
                //组织机构现状
                String ZZJGXZ = record.get("ZZJGXZ");
                //企业联系人
                String LXRXM = record.get("LXRXM");
                //企业联系电话
                String LXRSJ = record.get("LXRSJ");
                //受理人
                String SLR = record.get("SLR");
                //受理日期
                String SLRQ = record.get("SLRQ");
                
                String projectguid = UUID.randomUUID().toString();
                String applytype = record.get("applyType");
                String deptask = record.get("taskName");
               
                
                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("【济宁市自建系统同步办件】=====同步失败 "+ e.getMessage());
        }
    }

}
