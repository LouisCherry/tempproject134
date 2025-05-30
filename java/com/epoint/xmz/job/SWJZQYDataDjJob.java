package com.epoint.xmz.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.bizlogic.sysconf.systemparameters.service.ConfigServiceImpl;
import com.epoint.common.util.ValidateUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.utils.WavePushInterfaceUtil;
import com.epoint.xmz.api.IJnService;
import com.epoint.xmz.job.DJWebService.HttpClient;
import com.epoint.xmz.job.util.TokenUtil;
import com.epoint.zwdt.util.TARequestUtil;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;

@DisallowConcurrentExecution
public class SWJZQYDataDjJob implements Job {
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    private IJnService jnService = ContainerFactory.getContainInfo().getComponent(IJnService.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            log.info("==========开始执行SWJZQYDataDjJob数据同步推送服务==========");
            EpointFrameDsManager.begin(null);

            Date date = new Date();
            //获取前一天的时间
            Date dateBefore = EpointDateUtil.getDateBefore(date, 1);
            //请求的入参时间即：【2021-08-25】类型的字符串
            String jobDate = EpointDateUtil.convertDate2String(dateBefore);

            //前一天开始的时间和结束时间
            Date beginOfDate = EpointDateUtil.getBeginOfDate(date);
            Date endOfDate = EpointDateUtil.getEndOfDate(date);
            //查询是否已有同步的数据；防止重复录入
            List<Record> list = jnService.getSWJZQYDataAuditProjectZjxtByTime(beginOfDate, endOfDate);
            if (list.size() < 1) {
                //获取token
                String token = TokenUtil.createToken("JINZJJ");
                //获取数据
                String retStr = HttpClient.doHttp(token,jobDate);
                //截取数据
                String data = subString(retStr, "<GetJsonCorpRegisterInfoListByTimeStampResult>", "</GetJsonCorpRegisterInfoListByTimeStampResult>");
                //数据转化
                JSONObject retJson = JSONObject.parseObject(data);
                String dataStr = retJson.getString("data");
                JSONObject listJson = JSONObject.parseObject(dataStr);
                String corpRegisterInfoList = listJson.getString("CorpRegisterInfoList");

                JSONArray objects = JSONObject.parseArray(corpRegisterInfoList);
                for (int i = 0; i < objects.size(); i++) {
                    JSONObject obj = objects.getJSONObject(i);
                    String areaCode = obj.getString("AuditAreaCode");
                    if ("370800".equals(areaCode)) {
                        AuditTask auditTask = jnService.getAuditTaskInfo("省外建筑业企业入鲁报送基本信息", "370800");
                        if (ValidateUtil.isNotNull(auditTask)) {
                            //接口获取的具体办件详情数据
                            String rowGuid = obj.getString("RowGuid");//办件主键

                            Record auditProjectZjxtByRowguid = jnService.getAuditProjectZjxtByRowguid(rowGuid);
                            //判断数据是否已经入库了
                            if (ValidateUtil.isNull(auditProjectZjxtByRowguid)) {
                                //接口获取的具体办件详情数据
                                String legalMan = obj.getString("LegalMan");//法人
                                String linkMan = obj.getString("LinkMan");//联系人
                                String linkTel = obj.getString("LinkTel");//联系电话
                                String linkPhone = obj.getString("LinkPhone");//手机
                                String applicant = obj.getString("Applicant");//申请人姓名
                                String auditAreaCode = obj.getString("AuditAreaCode");//审核地辖区编码
                                String auidtUserName = obj.getString("AuidtUserName");//审核人姓名

                                //事项详情
                                String taskguid = auditTask.getRowguid();
                                String taskname = auditTask.getTaskname();
                                String ouname = auditTask.getOuname();
                                String ouguid = auditTask.getOuguid();
                                String areacode = auditTask.getAreacode();
                                String task_id = auditTask.getTask_id();


                                //赋值，插入数据库
                                Record record = new Record();
                                record.setSql_TableName("audit_project_zjxt");
                                record.set("datasource", "006");//省外建筑业企业入鲁报送基本信息，固定为006
                                record.set("OperateUserName", "省外建筑业企业入鲁报送基本信息数据同步服务");
                                record.set("OperateDate", date);
                                record.set("OUGUID", ouguid);
                                record.set("OUNAME", ouname);
                                record.set("AREACODE", areacode);
                                record.set("TASKID", task_id);
                                record.set("STATUS", "90");
                                record.set("taskguid", taskguid);
                                record.set("projectname", taskname);
                                record.set("rowguid", rowGuid);
                                record.set("LEGAL", legalMan);
                                record.set("CONTACTPERSON", linkMan);
                                record.set("CONTACTPHONE", linkTel);
                                record.set("CONTACTMOBILE", linkPhone);
                                record.set("CONTACTEMAIL", applicant);
                                record.set("HANDLEAREACODE", auditAreaCode);
                                record.set("ACCEPTUSERNAME", auidtUserName);
                                

                                // 获取事项Unid
                                String unid = auditTask.getStr("unid");
                                if(StringUtil.isNotBlank(unid)){
                                	String zwdturl = new ConfigServiceImpl().getFrameConfigValue("zwdtflowsnurl");
                                	JSONObject submit = new JSONObject();
                                	JSONObject json = new JSONObject();
                                	json.put("unid", unid);
                        			submit.put("params", json);
                        			submit.put("token", "Epoint_WebSerivce_**##0601");
                        			String resultsign = TARequestUtil.sendPostInner(zwdturl, submit.toJSONString(), "", "");

                        			if (StringUtil.isNotBlank(resultsign)) {
                        				JSONObject jsonobject = JSONObject.parseObject(resultsign);
                        				JSONObject jsonstatus = (JSONObject) jsonobject.get("status");
                        				if ("200".equals(jsonstatus.get("code").toString())) {
                        					JSONObject jsoncustom = (JSONObject) jsonobject.get("custom");
                        					if ("1".equals(jsoncustom.get("code").toString())) {
                        						String flowsn = jsoncustom.getString("flowsn");
                        						record.set("flowsn", flowsn);
                                                jnService.insert(record);
                        					} else
                        						log.info("获取办件编号失败：" + unid + "，原因：" + resultsign);
                        				} else {
                        					log.info("获取办件编号失败：" + unid + "，原因：" + resultsign);
                        				}
                        			} else {
                        				log.info("=====网厅连接失败");
                        			}
                                }
                            }
                        }
                    }
                    //字段对应
                }
            }
            EpointFrameDsManager.commit();
            log.info("==========执行SWJZQYDataDjJob数据同步推送服务成功！！！==========");
        } catch (Exception e) {
            e.printStackTrace();
            EpointFrameDsManager.rollback();
            log.info("==========执行SWJZQYDataDjJob数据同步推送服务失败！！！==========");
        } finally {
            EpointFrameDsManager.close();
            log.info("==========执行SWJZQYDataDjJob数据同步推送服务结束==========");
        }
    }


    /**
     * 截取字符串str中指定字符 strStart、strEnd之间的字符串
     *
     * @param str
     * @param strStart
     * @param strEnd
     * @return
     */
    public static String subString(String str, String strStart, String strEnd) {

        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);

        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0 || strEndIndex < 0) {
            return null;
        }
        /* 开始截取 */
        return str.substring(strStartIndex, strEndIndex).substring(strStart.length());
    }
}
