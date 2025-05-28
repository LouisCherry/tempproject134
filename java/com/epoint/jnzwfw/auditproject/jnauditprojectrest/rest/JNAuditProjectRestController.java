package com.epoint.jnzwfw.auditproject.jnauditprojectrest.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditsp.auditspbasetask.inter.IAuditSpBasetask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.spgl.inter.ISpglDfxmsplcjdsxxxb;
import com.epoint.common.rabbitmq.ProducerMQ;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.QueueConstant;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.jnzwfw.auditproject.jnauditprojectrest.api.IJNAuditProjectRestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jnauditprojectrest")
public class JNAuditProjectRestController
{
    @Autowired
    private IJNAuditProjectRestService service;
    
    @Autowired
    private IAuditTask auditTaskService;
    
    @Autowired
    private IAuditTask auditTaskBasicImpl;
    
    @Autowired
    private ISpglDfxmsplcjdsxxxb ispgldfxmsplcjdsxxxb;
    
    @Autowired
    private IAuditSpBasetask iauditspbasetask;
    
    @Autowired
    private IOuService iouservice;
    
    @Autowired
    private ISendMQMessage iSendMQMessage;
    
    @Autowired
    private IAuditZnsbEquipment equipmentservice;
    
    @Autowired
    private IAuditProject auditProjectServcie;
    
    /**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	
    /**
     * 
     *  获取办件表的列表清单
     *  @param params
     *  @param request
     *  @return    
     */
    @RequestMapping(value = "/getJNAuditProjectList", method = RequestMethod.POST)
    public String getJNAuditProjectList(@RequestBody String params, HttpServletRequest request) {
        JSONObject json = JSON.parseObject(params);
        JSONObject dataJson = new JSONObject();
        try {
            String areaCode = json.getString("areaCode");
            int pageNumber = Integer.parseInt(json.getString("pageNumber"));
            int pageSize = Integer.parseInt(json.getString("pageSize"));
            List<AuditProject> list = service.getAuditProjectRestList(pageNumber*pageSize, pageSize, areaCode);
            dataJson.put("list", list);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    /**
     * 
     *  获取办件的详细信息
     *  @param params
     *  @param request
     *  @return    
     */
    @RequestMapping(value = "/getJNAuditProjectDetail", method = RequestMethod.POST)
    public String getJNAuditProjectDetail(@RequestBody String params, HttpServletRequest request) {
        JSONObject json = JSON.parseObject(params);
        JSONObject dataJson = new JSONObject();
        try {
            String rowGuid = json.getString("rowGuid");
            AuditProject auditProject = service.getAuditProjectRestDetail(rowGuid);
            dataJson.put("auditProject", auditProject);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    /**
     * 
     *  统计年月日办件次数的数量
     *  @return    
     */
    @RequestMapping(value = "/getJNAuditCount", method = RequestMethod.POST)
    public String getJNAuditCount(@RequestBody String params, HttpServletRequest request) {
        JSONObject result = JSON.parseObject(params);
        String areaCode = result.getString("areaCode");
        JSONObject dataJson = new JSONObject();
        Map<String,String> map = new HashMap<String,String>();
        try {
            AuditProject auditProject = service.getAuditCount(areaCode);
            String everyYearCount = auditProject.getStr("everyYearCount");
            String everyMonthCount = auditProject.getStr("everyMonthCount");
            String everyDayCount = auditProject.getStr("everyDayCount");
            map.put("everyYearCount", everyYearCount);
            map.put("everyMonthCount", everyMonthCount);
            map.put("everyDayCount", everyDayCount);
            dataJson.put("data", map);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    /**
     * 
     *  统计办件满意度评价数量
     *  @return    
     */
    @RequestMapping(value = "/getJNAuditSatisfiedCount", method = RequestMethod.POST)
    public String getJNAuditSatisfiedCount(@RequestBody String params, HttpServletRequest request) {
        JSONObject result = JSON.parseObject(params);
        String areaCode = result.getString("areaCode");
        JSONObject dataJson = new JSONObject();
        Map<String,String> map = new HashMap<String,String>();
        try {
            AuditProject auditProject = service.getSatisfiedCount(areaCode);
            String basicSatisfiedCount = auditProject.getStr("basicSatisfiedCount");
            String totalAudit = service.getTotalSatisfiedCount(areaCode).getStr("totalAudit");
            String disSatisfiedCount = auditProject.getStr("disSatisfiedCount");
            int verySatisfiedCount = Integer.parseInt(totalAudit)-Integer.parseInt(basicSatisfiedCount)-Integer.parseInt(disSatisfiedCount);
            map.put("非常满意", String.valueOf(verySatisfiedCount));
            map.put("基本满意", basicSatisfiedCount);
            map.put("不满意", disSatisfiedCount);
            dataJson.put("data", map);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    /**
     * 
     *  统计办件满意度评价百分比
     *  @return    
     */
    @RequestMapping(value = "/getJNAuditSatisfiedPercent", method = RequestMethod.POST)
    public String getJNAuditSatisfiedPercent(@RequestBody String params, HttpServletRequest request) {
        JSONObject dataJson = new JSONObject();
        JSONObject result = JSON.parseObject(params);
        String areaCode = result.getString("areaCode");
        Map<String,String> map = new HashMap<String,String>();
        try {
            AuditProject auditProject = service.getSatisfiedCount(areaCode);
            int basicSatisfiedCount = Integer.parseInt(auditProject.getStr("basicSatisfiedCount"));
            int totalAudit = Integer.parseInt(service.getTotalSatisfiedCount(areaCode).getStr("totalAudit"));
            int disSatisfiedCount = Integer.parseInt(auditProject.getStr("disSatisfiedCount"));
            int verySatisfiedCount = totalAudit - basicSatisfiedCount - disSatisfiedCount;
            DecimalFormat df = new DecimalFormat("0.0");
            String verySatisfiedPercent = df.format(Math.floor((float)verySatisfiedCount*1000/totalAudit)/10);
            String basicSatisfiedPercent = df.format((float)basicSatisfiedCount*100/totalAudit);
            String disSatisfiedPercent = df.format(Math.ceil((float)disSatisfiedCount*1000/totalAudit)/10);
            map.put("verySatisfiedCount", verySatisfiedPercent+"%");
            map.put("basicSatisfiedCount", basicSatisfiedPercent+"%");
            map.put("disSatisfiedCount", disSatisfiedPercent+"%");
            dataJson.put("data", map);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    /**
     * 
     *  统计办件满意度评价百分比
     *  @return    
     */
    /*@RequestMapping(value = "/buchongspgl", method = RequestMethod.POST)
    public String buchongspgl(@RequestBody String params, HttpServletRequest request) {
        JSONObject dataJson = new JSONObject();
        JSONObject result = JSON.parseObject(params);
        Map<String,String> map = new HashMap<String,String>();
        try {
            List<SpglXmspsxblxxb> list1 = service.getSPglXmspxxb();
            //system.out.println("list1:"+list1);
            if (list1 != null && list1.size() > 0) {
                for (SpglXmspsxblxxb info : list1) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.isBlankOrValue("is_history", "0");
                    sql.eq("IS_EDITAFTERIMPORT", "1");
                    sql.eq("IS_ENABLE", "1");
                    sql.eq("ISTEMPLATE", "0");
                    sql.eq("item_id", info.getSpsxbm());
                    AuditTask task = auditTaskService.getAuditTaskList(sql.getMap()).getResult().get(0);
                    AuditTask audittask = auditTaskBasicImpl.selectUsableTaskByTaskID(task.getTask_id()).getResult();
                    if(audittask != null){
                      List<SpglDfxmsplcjdsxxxb> list = ispgldfxmsplcjdsxxxb.getNeedAddNewVersionByItemId(audittask.getItem_id()).getResult();
                      for (SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb1 : list) {
                          //system.out.println("spgllist:"+spglDfxmsplcjdsxxxb1);
                          SpglDfxmsplcjdsxxxb spgldfxmsplcjdsxxxb = new SpglDfxmsplcjdsxxxb();
                          spgldfxmsplcjdsxxxb.setRowguid(UUID.randomUUID().toString());
                          spgldfxmsplcjdsxxxb.setDfsjzj(spglDfxmsplcjdsxxxb1.getDfsjzj());
                          spgldfxmsplcjdsxxxb.setXzqhdm(spglDfxmsplcjdsxxxb1.getXzqhdm());
                          spgldfxmsplcjdsxxxb.setSplcbm(spglDfxmsplcjdsxxxb1.getSplcbm());
                          spgldfxmsplcjdsxxxb.setSplcbbh(spglDfxmsplcjdsxxxb1.getSplcbbh());
                          spgldfxmsplcjdsxxxb.setSpjdxh(spglDfxmsplcjdsxxxb1.getSpjdxh());
                          spgldfxmsplcjdsxxxb.setSpsxbbh(Double.valueOf(audittask.getVersion()));
                          spgldfxmsplcjdsxxxb.setSpsxmc(audittask.getTaskname());
                          spgldfxmsplcjdsxxxb.setSpsxbm(audittask.getItem_id());
                          AuditSpBasetask basetask = iauditspbasetask.getAuditSpBasetaskByrowguid(spglDfxmsplcjdsxxxb1.getDfsjzj()).getResult();
                          if (basetask != null) {
                              spgldfxmsplcjdsxxxb.setDybzspsxbm(basetask.getTaskcode());
                              spgldfxmsplcjdsxxxb.setSflcbsx(StringUtil.isNotBlank(basetask.getSflcbsx())
                                      ? Integer.valueOf(basetask.getSflcbsx()) : ZwfwConstant.CONSTANT_INT_ZERO); //是否里程碑事项。默认0
                          }
                          else {
                              spgldfxmsplcjdsxxxb.setSflcbsx(0);
                              spgldfxmsplcjdsxxxb.setDybzspsxbm("9990");
                          }
                          spgldfxmsplcjdsxxxb.setSfsxgzcnz(ZwfwConstant.CONSTANT_INT_ZERO);//是否实行告知承诺制 默认0

                          spgldfxmsplcjdsxxxb.setBjlx(audittask.getType());
                          if (audittask.getApplyertype().indexOf(ZwfwConstant.APPLAYERTYPE_QY) != -1
                                  && audittask.getApplyertype().indexOf(ZwfwConstant.APPLAYERTYPE_GR) != -1) {
                              spgldfxmsplcjdsxxxb.setSqdx(3);
                          }else{
                              if (audittask.getApplyertype().indexOf(ZwfwConstant.APPLAYERTYPE_GR) != -1) {
                                  spgldfxmsplcjdsxxxb.setSqdx(1);
                              }
                              else if (audittask.getApplyertype().indexOf(ZwfwConstant.APPLAYERTYPE_QY) != -1) {
                                  spgldfxmsplcjdsxxxb.setSqdx(2);
                              }
                          }
                          spgldfxmsplcjdsxxxb.setBljgsdfs("2"); //办理送达方式   默认 申请对象窗口领取
                          spgldfxmsplcjdsxxxb.setSpsxblsx(audittask.getPromise_day());
                          spgldfxmsplcjdsxxxb.setSpbmbm(iouservice.getOuByOuGuid(audittask.getOuguid()).getOucode());
                          spgldfxmsplcjdsxxxb.setSpbmmc(audittask.getOuname());
                          spgldfxmsplcjdsxxxb.setQzspsxbm(null);
                          spgldfxmsplcjdsxxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                          spgldfxmsplcjdsxxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                          ispgldfxmsplcjdsxxxb.insert(spgldfxmsplcjdsxxxb);
                          //system.out.println("插入到spgldfxmsplcjdsxxxb");
                      }
                      service.updateSPglXmspxxb(info.getRowguid());
                    }
                   
                }
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }*/
    
    
    /**
     * 
     *  推送MQ消息到手机端应用
     *  @return    
     */
    @RequestMapping(value = "/turnmqevaluate", method = RequestMethod.POST)
    public String turnmqevaluate(@RequestBody String params, HttpServletRequest request) {
		try {
			log.info("=======开始调用turnmqevaluate接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			JSONObject obj = (JSONObject) jsonObject.get("params");
			String token = jsonObject.getString("token");
			String fields = " rowguid,taskguid,applyername,applyeruserguid,8,status,centerguid,applyway,taskcaseguid,pviguid,projectname,applyertype,certnum,certtype,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,address,contactcertnum,legal,flowsn,tasktype,ouname,ouguid,hebingshoulishuliang,remark,windowguid,biguid,banjiedate,is_pause,spendtime,if_express,applydate,is_check,is_fee,banjieresult,if_express_ma,is_charge,is_test,currentareacode,handleareacode,taskid,task_id,is_lczj,businessguid,acceptareacode ";
			if (ZwdtConstant.SysValidateData.equals(token)) {
				String projectguid = obj.getString("projectguid");
				String areaCode = obj.getString("areacode");
				String windowguid = obj.getString("windowguid");
				AuditProject auditProject = auditProjectServcie.getAuditProjectByRowGuid(fields, projectguid, areaCode).getResult();
				String Macaddress = equipmentservice.getMacaddressbyWindowGuidAndType(windowguid, QueueConstant.EQUIPMENT_TYPE_PJPAD).getResult();

	            String proStatus = "";
	            String assessNumber = "";
	            Integer status = auditProject.getStatus();
	            if (status < 30) {
	                proStatus = "1";
	                assessNumber = "1";
	            }
	            else if (status >= 30 && status <= 90) {
	                proStatus = "2";
	                assessNumber = "2";
	            }
	            else {
	                proStatus = "1";
	                assessNumber = "1";
	            }
	            if (StringUtil.isNotBlank(Macaddress)) {
	            	try {
	                JSONObject dataJson = new JSONObject();
	                dataJson.put("status", "100");
	                dataJson.put("url", "http://112.6.110.176:28080/jnzwfw/jiningzwfw/pages/evaluate/step1?projectNo="+auditProject.getFlowsn()+"&areacode="+auditProject.getAreacode()+"&acceptareacode=" + auditProject.getAcceptareacode() +"&proStatus="+proStatus+"&assessNumber="+assessNumber+"&iszj=1");
					ProducerMQ.send(this.getAppMQQueue(Macaddress), dataJson.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
	            	return JsonUtils.zwdtRestReturn("1", "调用MQ成功！", "");
	            }else {
	            	return JsonUtils.zwdtRestReturn("0", "调用MQ失败！", "");
	            }
				
			} else {
				return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======turnmqevaluate接口参数：params【" + params + "】=======");
			log.info("=======turnmqevaluate异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "调用MQ失败：" + e.getMessage(), "");
		}
	
    }
    
    public String getAppMQQueue(String Macaddress) {

        return "mqtt-subscription-" + Macaddress + "qos1";
    }
    
    
    /**
     * 
     *  统计办件满意度评价百分比
     *  @return    
     */
    @RequestMapping(value = "/buchongspglbjlc", method = RequestMethod.POST)
    public String buchongspglbjlc(@RequestBody String params, HttpServletRequest request) {
        JSONObject result = JSON.parseObject(params);
        String flowsns = result.getString("flowsn");
        try {
            List<AuditProject> projects = service.getauditProjectByFlowsn(flowsns);
            if (projects != null && projects.size() > 0) {
            	for (AuditProject project : projects) {
            		String msg = project.getRowguid() + "." + project.getAreacode();
                    //接办分离  受理
            		iSendMQMessage.sendByExchange("exchange_handle", msg, "project."
                           + project.getAreacode() + ".accept." + project.getTask_id());
                   
                    String msg1 = project.getRowguid() + "." + project.getAreacode() + "."
                            + project.getApplyername();
                    iSendMQMessage.sendByExchange("exchange_handle", msg1, "project."
                            + project.getAreacode() + ".sendresult." + project.getTask_id());
            	}
            }
            return JsonUtils.zwdtRestReturn("1", "", "success");
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    /**
     * 
     *  统计办件满意度评价百分比
     *  @return    
     */
    @RequestMapping(value = "/buchongSpglXmspsxblxxb", method = RequestMethod.POST)
    public String buchongSpglXmspsxblxxb(@RequestBody String params, HttpServletRequest request) {
        JSONObject result = JSON.parseObject(params);
        String flowsns = result.getString("flowsn");
        try {
            List<AuditProject> projects = service.getauditProjectByFlowsn(flowsns);
             
            if (projects != null && projects.size() > 0) {
            	for (AuditProject project : projects) {
            		String subappguid = project.getSubappguid();
            		String projectguid = "'"+project.getRowguid()+"'";
            		String areacode = "370800";
            		String itemcode = "";
            		String userguid = project.getAcceptuserguid();
            		 //发送mq消息推送数据
                    String msg = subappguid + "." + areacode + "." + itemcode;
                    msg += ".isck";
                    //拼接办理人用户标识
                    msg+="."+userguid + "."+ projectguid;
                    iSendMQMessage.sendByExchange("exchange_handle", msg, "blsp.subapp." + "131");
            	}
            }
            return JsonUtils.zwdtRestReturn("1", "", "success");
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    
    
}
