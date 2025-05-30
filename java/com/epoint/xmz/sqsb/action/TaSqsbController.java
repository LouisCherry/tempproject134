
package com.epoint.xmz.sqsb.action;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetask.inter.IAuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.xmz.sqsb.api.ISqsbService;

@RestController
@RequestMapping("/tasqsb")
public class TaSqsbController {
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	 @Autowired
	 private IAttachService attachService;
	 
	 @Autowired
     private IAuditSpBusiness auditSpBusiness;
	 
	 @Autowired
     private IAuditSpTask auditSpTaskService;
	 
	 @Autowired
     private IAuditSpBasetaskR auditSpBasetaskRService;
	 
	 @Autowired
     private IAuditSpBasetask auditSpBasetaskService;
	 /**
	  * 双全双百service
	  */
	 @Autowired
	 private ISqsbService sqsbService;
	 
	 
	/**
	 * 根据传的申请类别获取双全双百主题
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/getBusinessListByType", method = RequestMethod.POST)
	public String getBusinessListByType(@RequestBody String params) {
		try {
			log.info("=======开始调用getBusinessListByType接口=======");
			// 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            log.info("getBusinessListByType入参："+jsonObject);
            // 8、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            JSONObject jsonObject1 = jsonObject.getJSONObject("params");
            String issqlb = jsonObject1.getString("issqlb");
//            String areacode = jsonObject1.getString("areacode");
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("issqlb", issqlb);
//            sql.eq("areacode", areacode);
            sql.eq("businesstype", "5");
            String orderCloumn = "ordernumber";
            String ordertype ="asc";
            List<AuditSpBusiness> auditSpBusinessList = auditSpBusiness.getAuditSpBusinessPageData(sql.getMap(), 0, 100, orderCloumn, ordertype).getResult().getList();
            JSONArray auditSpBusinessArray = new JSONArray();
            JSONObject result = new JSONObject();
            if(StringUtil.isNotBlank(auditSpBusinessList) && !auditSpBusinessList.isEmpty()) {
                for (AuditSpBusiness auditSpBusiness : auditSpBusinessList) {
                    JSONObject auditSpBusinessObject = new JSONObject();
                    auditSpBusinessObject.put("rowguid", auditSpBusiness.getRowguid());
                    auditSpBusinessObject.put("businessname", auditSpBusiness.getBusinessname());
                    if("出生".equals(auditSpBusiness.getBusinessname()) || "企业开办".equals(auditSpBusiness.getBusinessname())) {
                    	auditSpBusinessObject.put("index", 0);
                    }else {
                    	auditSpBusinessObject.put("index", 1);
                    }
                    auditSpBusinessArray.add(auditSpBusinessObject);
                }
            }
            result.put("list", auditSpBusinessArray);
            log.info("=======结束调用getBusinessListByType接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取主题信息成功" , result.toString());
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getAccessToken接口参数：params【" + params + "】=======");
			log.info("=======getAccessToken异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "获取acctss_token失败：" + e.getMessage(), "");
		}
	}
	
	
	/**
	 * 查询企业信息接口
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/getAuditTaskListByBusinessGuid", method = RequestMethod.POST)
	public String getAuditTaskListByBusinessGuid(@RequestBody String params) {
		try {
			log.info("=======开始调用getAuditTaskListByBusinessGuid接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			log.info("getAuditTaskListByBusinessGuid入参："+jsonObject);
			// 8、定义返回JSON对象
			JSONObject dataJson = new JSONObject();
			JSONObject jsonObject1 = jsonObject.getJSONObject("params");
			String businessguid = jsonObject1.getString("businessguid");
			JSONObject result = new JSONObject();
			JSONArray taskArray = new JSONArray();
			if(StringUtil.isNotBlank(businessguid)) {
			    List<AuditSpTask> auditSpTaskList = auditSpTaskService.getAllAuditSpTaskByBusinessGuid(businessguid).getResult();
			    if(StringUtil.isNotBlank(auditSpTaskList) && !auditSpTaskList.isEmpty()) { //主题绑定的事项清单列表
			        for (AuditSpTask auditSpTask : auditSpTaskList) {
			            if(StringUtil.isNotBlank(auditSpTask.getBasetaskguid())) {//根据事项清单查询
			                String basetaskguid = auditSpTask.getBasetaskguid();
			                //根据事项清单guid查询事项清单
			                AuditSpBasetask auditSpBasetask = auditSpBasetaskService.getAuditSpBasetaskByrowguid(basetaskguid).getResult();
			                //根据事项清单guid查询绑定的事项
			                List<AuditSpBasetaskR> auditSpBaseTaskRList = sqsbService.getAuditSpBasetaskrByBaseTaskGuid(basetaskguid);
			                //若是绑定的事项不为空，则进行下一步
			                if(StringUtil.isNotBlank(auditSpBaseTaskRList) && !auditSpBaseTaskRList.isEmpty()) {
			                    if(auditSpBaseTaskRList.size() > 1) { //说明配置的事项辖区就能办
			                        for (AuditSpBasetaskR auditSpBasetaskR : auditSpBaseTaskRList) {
                                        String taskid = auditSpBasetaskR.getTaskid();
                                        AuditTask auditTask = sqsbService.getAuditTaskByTaskId(taskid);
                                        if(StringUtil.isNotBlank(auditTask)) {
                                         JSONObject auditTaskObject = new JSONObject(); 
                                         auditTaskObject.put("taskguid", auditTask.getRowguid());
                                         auditTaskObject.put("taskid", auditTask.getTask_id());
                                         auditTaskObject.put("taskname", auditSpBasetask.getTaskname());
                                         auditTaskObject.put("areacode", auditTask.getAreacode());
                                         taskArray.add(auditTaskObject);
                                         break;
                                        }
                                    }
			                    }
			                    else {//1.有可能这个事项只有一个辖区能办；2.这个是为了主题选择事项时加的一个自建事项，要通过这个事项名去找可以办这个事项的乡镇的事项；
			                        String taskname = auditSpBaseTaskRList.get(0).getTaskname();
			                        String taskid = auditSpBaseTaskRList.get(0).getTaskid();
			                        AuditTask auditTask = sqsbService.getAuditTaskByTaskId(taskid);
			                        if(StringUtil.isNotBlank(auditTask)) {
                                        JSONObject auditTaskObject = new JSONObject(); 
                                        auditTaskObject.put("taskid", auditTask.getTask_id());
                                        auditTaskObject.put("taskguid", auditTask.getRowguid());
                                        auditTaskObject.put("taskname", auditSpBasetask.getTaskname());
                                        auditTaskObject.put("areacode", auditTask.getAreacode());
                                        taskArray.add(auditTaskObject);
                                    }
			                        else {
			                            List<AuditTask> auditTaskList = sqsbService.getAuditTaskListByTaskName(taskname);
			                            if(StringUtil.isNotBlank(auditTaskList) && !auditTaskList.isEmpty()) {
			                                AuditTask finalAuditTask = auditTaskList.get(0);
			                                JSONObject auditTaskObject = new JSONObject(); 
			                                auditTaskObject.put("taskid", finalAuditTask.getTask_id());
	                                        auditTaskObject.put("taskguid", finalAuditTask.getRowguid());
	                                        auditTaskObject.put("taskname", auditSpBasetask.getTaskname());
	                                        auditTaskObject.put("areacode", finalAuditTask.getAreacode());
	                                        taskArray.add(auditTaskObject);
			                            }
			                        }
			                    }
			                    
			                }
			            }
                        
                    }
			    }
			    else {
			        return JsonUtils.zwdtRestReturn("0", "未获取到主题配置的事项信息！" , "");
			    }
			    
			}
			else {
			    return JsonUtils.zwdtRestReturn("0", "未获取到主题信息！" , "");
			}
			result.put("list", taskArray);
			log.info("=======结束调用getAuditTaskListByBusinessGuid=======");
			return JsonUtils.zwdtRestReturn("1", "获取成功", result.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getAuditTaskListByBusinessGuid接口参数：params【" + params + "】=======");
			log.info("=======getAuditTaskListByBusinessGuid异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "查询企业信息接口失败：" + e.getMessage(), "");
		}
	}
	
	
	/**
	 * 查询企业信息接口
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/getBusinessListByBusinessGuid", method = RequestMethod.POST)
	public String getBusinessListByBusinessGuid(@RequestBody String params) {
		try {
			log.info("=======开始调用getAuditTaskListByBusinessGuid接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			log.info("getAuditTaskListByBusinessGuid入参："+jsonObject);
			// 8、定义返回JSON对象
			JSONObject dataJson = new JSONObject();
			JSONObject jsonObject1 = jsonObject.getJSONObject("params");
			String businessguid = jsonObject1.getString("businessguid");
			JSONObject result = new JSONObject();
			JSONArray taskArray = new JSONArray();
			if(StringUtil.isNotBlank(businessguid)) {
				AuditSpBusiness business  = auditSpBusiness.getAuditSpBusinessByRowguid(businessguid).getResult();
				if (business != null) {
					String sqsbtype = business.getStr("sqsbtype");
					if (sqsbtype.contains(",")) {
						String[] sqsbtypes = sqsbtype.split(",");
						for (String guid : sqsbtypes) {
							AuditSpBusiness bus  = auditSpBusiness.getAuditSpBusinessByRowguid(guid).getResult();
							if (bus != null) {
								  JSONObject auditTaskObject = new JSONObject(); 
                                  auditTaskObject.put("businessguid", bus.getRowguid());
                                  auditTaskObject.put("businessname", bus.getBusinessname());
                                  auditTaskObject.put("areacode", bus.getAreacode());
                                  taskArray.add(auditTaskObject);
							}
						}
					}else {
						AuditSpBusiness bus  = auditSpBusiness.getAuditSpBusinessByRowguid(sqsbtype).getResult();
						if (bus != null) {
							  JSONObject auditTaskObject = new JSONObject(); 
                              auditTaskObject.put("businessguid", bus.getRowguid());
                              auditTaskObject.put("businessname", bus.getBusinessname());
                              auditTaskObject.put("areacode", bus.getAreacode());
                              taskArray.add(auditTaskObject);
						}
					}
				}
			}
			else {
			    return JsonUtils.zwdtRestReturn("0", "未获取到主题信息！" , "");
			}
			result.put("list", taskArray);
			log.info("=======结束调用getBusinessListByBusinessGuid=======");
			return JsonUtils.zwdtRestReturn("1", "获取成功", result.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getBusinessListByBusinessGuid接口参数：params【" + params + "】=======");
			log.info("=======getBusinessListByBusinessGuid异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "查询企业信息接口失败：" + e.getMessage(), "");
		}
	}
	
	
	/**
	 * 查询企业信息接口
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/getAuditTaskListSqsb", method = RequestMethod.POST)
	public String getAuditTaskListSqsb(@RequestBody String params) {
		try {
			log.info("=======开始调用getAuditTaskListSqsb接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			log.info("getAuditTaskListSqsb入参："+jsonObject);
			// 8、定义返回JSON对象
			JSONObject dataJson = new JSONObject();
			String areacode = jsonObject.getString("areacode");
			String issqlb = jsonObject.getString("issqlb");
			JSONObject result = new JSONObject();
			JSONArray taskArray = new JSONArray();
			List<AuditSpBasetaskR> auditSpBaseTaskRList = sqsbService.getTaskNameBySqsb(issqlb, areacode);
			    if(StringUtil.isNotBlank(auditSpBaseTaskRList) && !auditSpBaseTaskRList.isEmpty()) { //主题绑定的事项清单列表
			    	for (AuditSpBasetaskR auditSpBasetaskR : auditSpBaseTaskRList) {
			    		AuditSpBasetask auditSpBasetask = auditSpBasetaskService.getAuditSpBasetaskByrowguid(auditSpBasetaskR.getBasetaskguid()).getResult();
			    		if (auditSpBasetask == null) {
			    			continue;
			    		}
                        String taskid = auditSpBasetaskR.getTaskid();
                        AuditTask auditTask = sqsbService.getAuditTaskByTaskId(taskid);
                        if(StringUtil.isNotBlank(auditTask)) {
                         JSONObject auditTaskObject = new JSONObject(); 
                         auditTaskObject.put("taskguid", auditTask.getRowguid());
                         auditTaskObject.put("taskid", auditTask.getTask_id());
                         auditTaskObject.put("taskname", auditSpBasetask.getTaskname());
                         auditTaskObject.put("areacode", auditTask.getAreacode());
                         taskArray.add(auditTaskObject);
                        }
                    }
			    }
			    else {
			        return JsonUtils.zwdtRestReturn("0", "未获取到主题配置的事项信息！" , "");
			    }
			result.put("list", taskArray);
			log.info("=======结束调用getAuditTaskListSqsb=======");
			return JsonUtils.zwdtRestReturn("1", "获取成功", result.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getAuditTaskListSqsb接口参数：params【" + params + "】=======");
			log.info("=======getAuditTaskListSqsb异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "查询企业信息接口失败：" + e.getMessage(), "");
		}
	}
	
	/**//**
	 * 上传附件到电子印章系统
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 *//*
	@RequestMapping(value = "/uploadAttach", method = RequestMethod.POST)
	public String uploadAttach(@RequestBody String params) {
		try {
			log.info("=======开始调用uploadAttach接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject dataJson = new JSONObject();
			JSONObject jsonObject = JSONObject.parseObject(params);
			JSONObject jsonObject1 = jsonObject.getJSONObject("params");
			Map<String, Object> param = new HashMap<String, Object>();
			String cliengguid = jsonObject1.getString("cliengguid");
			List<FrameAttachInfo> attachinfos = attachService.getAttachInfoListByGuid(cliengguid);
			if (attachinfos != null && attachinfos.size() > 0) {
				FrameAttachInfo attachinfo = attachinfos.get(0);
				String attachGuid = attachinfo.getAttachGuid();
				FrameAttachStorage attachStorage = attachService.getAttach(attachGuid);
				String filename = attachinfo.getAttachFileName();
			    String flietype = attachinfo.getContentType();
			    if (!flietype.contains("pdf")) {
			    	return JsonUtils.zwdtRestReturn("0", "签章的文件只能是PDF文件！", ""); 
			    }
			    if("1".equals(attachinfo.getStr("signstatus"))) {
			    	return JsonUtils.zwdtRestReturn("0", "该文件已签章！", ""); 
			    }
				String userid = jsonObject1.getString("userid");
				String token = jsonObject1.getString("token");
				Map<String,String> headers = new HashMap<String,String>();
	        	headers.put("token", token);
		        InputStream inputStream = attachStorage.getContent();
		        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) > -1) {
                    baos.write(buffer, 0, len);
                }
                baos.flush();
                byte[] bytes = baos.toByteArray();
                long sizes = bytes.length;
                String file = new Base64Encoder().encode(buffer);
		        if (StringUtil.isBlank(flietype) || flietype == null) {
		        	flietype = ".pdf";
		        }
				try {
					// 添加附件属性
					param.put("file_name", filename);
					param.put("file_type", "contract");
					param.put("user_id", userid);
					param.put("file", inputStream);
					// HttpUtil工具类调用
					String result = TARequestUtil.getResult1(dzyz_url + "/v1/file/upload?token="+token,param);
//					String result = HttpUtil.upload(dzyz_url + "/v1/file/upload", headers,
//							param, inputStream, filename);
					log.info("uploadAttach输出："+result);
					JSONObject jsonresult = JSON.parseObject(result);
					String result_code = jsonresult.getString("result_code");
					if ("0".equals(result_code)) {
						JSONObject data = jsonresult.getJSONObject("data");
						dataJson.put("fileid", data.getString("file_id"));
						dataJson.put("attachguid", attachinfo.getAttachGuid());
						log.info("=======结束调用uploadAttach接口=======");
						return JsonUtils.zwdtRestReturn("1", "文件上传成功", dataJson.toString());
					}
					else {
						return JsonUtils.zwdtRestReturn("0", "文件上传失败", dataJson.toString());
					}
				} catch (Exception e) {
					return JsonUtils.zwdtRestReturn("0", "文件上传失败","");
				} finally {
					// 关闭流
					try {
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (IOException e) {
						
					}
				}
			}else {
				return JsonUtils.zwdtRestReturn("0", "请先上传附件！", "");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======uploadAttach接口参数：params【" + params + "】=======");
			log.info("=======uploadAttach异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "文件上传失败：" + e.getMessage(), "");
		}
		
	}
	
	
	*//**
	 * 添加合同信息
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 *//*
	@RequestMapping(value = "/addcontract", method = RequestMethod.POST)
	public String addContract(@RequestBody String params) {
		try {
			log.info("=======开始调用addContract接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			JSONObject jsonObject1 = jsonObject.getJSONObject("params");
			log.info("addBus入参："+jsonObject);
			// 8、定义返回JSON对象
			JSONObject dataJson = new JSONObject();
			String token = jsonObject1.getString("token");
			String json = jsonObject1.getString("detail");
			String sign = SignUtils.createSign(json, appsecret);
			Map<String,String> headers = new HashMap<String,String>();
        	headers.put("sign", sign);
        	headers.put("Authentication", token);
			String result = TaHttpRequestUtils.postHttp1(dzyz_url+"/v1/contract/add", JSONObject.parseObject(json), headers);
			log.info("addcontract输出："+result);
			if (StringUtil.isNotBlank(result)) {
				JSONObject jsonresult = JSON.parseObject(result);
				String result_code = jsonresult.getString("result_code");
				if ("0".equals(result_code)) {
					JSONObject data = jsonresult.getJSONObject("data");
					dataJson.put("contractid", data.getString("contract_id"));
					long expirationtime = new Date(new Date().getTime() + 1000 * 60 *15).getTime();
					dataJson.put("expirationtime", expirationtime);
					log.info("=======结束调用addContract接口=======");
					return JsonUtils.zwdtRestReturn("1", "添加合同信息成功", dataJson.toString());
				}
				else {
					return JsonUtils.zwdtRestReturn("0", "添加合同信息失败", result);
				}
			}else {
				return JsonUtils.zwdtRestReturn("0", "添加合同信息失败", "");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======addContract接口参数：params【" + params + "】=======");
			log.info("=======addContract异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "添加合同信息失败：" + e.getMessage(), "");
		}
	}
	
	
	*//**
	 * 获取签章信息结果
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 *//*
	@RequestMapping(value = "/getReuslt", method = RequestMethod.POST)
	public String getReuslt(@RequestBody String params) {
		try {
			log.info("=======开始调用getReuslt接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			JSONObject jsonObject1 = jsonObject.getJSONObject("params");
			log.info("getAttach入参："+jsonObject);
			// 8、定义返回JSON对象
			JSONObject dataJson = new JSONObject();
			String businessid = jsonObject1.getString("business_id");
			String signstatus = jsonObject1.getString("sign_status");
			FrameAttachInfo attachinfo = attachService.getAttachInfoDetail(businessid);
			if (attachinfo != null) {
				if ("1".equals(signstatus)) {
					attachinfo.set("signstatus", "1");
					attachService.updateAttach(attachinfo, null);
					log.info("=======结束调用getReuslt接口=======");
					return JsonUtils.zwdtRestReturn("1", "获取签章信息结果成功", dataJson.toString());
				}
				else {
					return JsonUtils.zwdtRestReturn("0", "获取签章信息结果失败", dataJson.toString());
				}
			}else {
				return JsonUtils.zwdtRestReturn("0", "获取签章信息结果失败", "");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getReuslt接口参数：params【" + params + "】=======");
			log.info("=======getReuslt异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "获取签章信息结果失败：" + e.getMessage(), "");
		}
	}
	
	
	*//**
	 * 替换附件
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 *//*
	@RequestMapping(value = "/updateAttach", method = RequestMethod.POST)
	public String updateAttach(@RequestBody String params) {
		try {
			log.info("=======开始调用updateAttach接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			JSONObject jsonObject1 = jsonObject.getJSONObject("params");
			log.info("getAttach入参："+jsonObject);
			// 8、定义返回JSON对象
			JSONObject dataJson = new JSONObject();
			String businessid = jsonObject1.getString("attachguid");
			String fileid = jsonObject1.getString("fileid");
			String token = jsonObject1.getString("token");
			FrameAttachInfo attachinfo = attachService.getAttachInfoDetail(businessid);
        	InputStream inputstream = getInputStream(dzyz_url + "/v1/file/download?fileId="+fileid,token);
			if (attachinfo != null) {
				if ("1".equals(attachinfo.getStr("signstatus"))) {
					byte[] byt = new byte[inputstream.available()];
					attachinfo.setAttachLength(Long.valueOf((long) byt.length));
					attachService.updateAttach(attachinfo, inputstream);
					log.info("=======结束调用updateAttach接口=======");
					return JsonUtils.zwdtRestReturn("1", "签章成功！", dataJson.toString());
				}
				else {
					return JsonUtils.zwdtRestReturn("0", "签章失败！", dataJson.toString());
				}
			}else {
				return JsonUtils.zwdtRestReturn("0", "签章失败！", "");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======updateAttach接口参数：params【" + params + "】=======");
			log.info("=======updateAttach异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "签章失败：" + e.getMessage(), "");
		}
	}
	
	
	 *//**
     * 获得服务器端的数据,以InputStream形式返回
     * @return
     *//*
    public static InputStream getInputStream(String URLPATH,String header) {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(URLPATH);
            if (url != null) {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                // 设置连接网络的超时时间
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.setDoInput(true);
                // 表示设置本次http请求使用GET方式请求
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.addRequestProperty("Authentication", header);
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == 200) {
                    // 从服务器获得一个输入流
                    inputStream = httpURLConnection.getInputStream();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }
*/
	
	
}
