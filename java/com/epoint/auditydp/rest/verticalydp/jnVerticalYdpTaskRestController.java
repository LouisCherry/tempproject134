package com.epoint.auditydp.rest.verticalydp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditqueue.auditznsbcentertask.domain.AuditZnsbCentertask;
import com.epoint.basic.auditqueue.auditznsbcentertask.inter.IAuditZnsbCentertask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.dict.domain.AuditTaskDict;
import com.epoint.basic.audittask.dict.inter.IAuditTaskDict;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

/**
 * [竖屏引导屏事项查询的restful接口] [F9.3竖屏引导屏事项查询的restful接口]
 * 
 * @author xuyunhai
 * @version [1.0, 2017年5月10日]
 
 * @since [智能化产品/v1.0]
 */
@RestController
@RequestMapping("/jnverticalydptask")
public class jnVerticalYdpTaskRestController {
	@Autowired
	private IAuditTask taskservice;

	@Autowired
	private IAuditTaskExtension taskextensionservice;

	@Autowired
	private IOuService ouService;

	@Autowired
	private ICodeItemsService codeItemsService;

	@Autowired
	private IAttachService attachService;

	@Autowired
	private IAuditTaskMaterial taskmaterialservice;

	@Autowired
	private IAuditZnsbCentertask centertaskservice;

	@Autowired
	private IAuditTaskDict taskdictservice;

	/**
	 * 
	 * [获取事项查询左侧列表] [功能详细描述]
	 * 
	 * @param params
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(value = "/getCategoryList", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public String getCategoryList(@RequestBody String params) {
		try {
			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));// token验证
			JSONObject obj = (JSONObject) json.get("params");
			String centerguid = obj.getString("centerguid");
			String userType = obj.getString("UserType");// 用户类型 企业: company ；个人:
														// person
			String targetId = obj.getString("targetId");// 查询类别 主题:theme 企业:
														// industry 部门:
														// department
			JSONObject dataJson = new JSONObject();
			List<Record> datalist = new ArrayList<>();
			String applyertype = "";
			if ("department".equals(targetId)) {
				int type = 0;
				if ("company".equals(userType)) {
					type = 1;
					applyertype = "10";
				} else {
					type = 2;
					applyertype = "20";
				}
				/**
				 * 获取到所有的部门guid
				 */
				List<String> ourecord = centertaskservice.getTaskOUList(centerguid, applyertype).getResult();

				if (ourecord != null && ourecord.size() > 0) {
					for (String record : ourecord) {
						if (StringUtil.isNotBlank(record)) {
							FrameOu frameou = ouService.getOuByOuGuid(record);
							if (frameou != null) {
								String ouname = StringUtil.isNotBlank(frameou.getOushortName())
										? frameou.getOushortName() : frameou.getOuname();
								
								Record datarecord = new Record();
								datarecord.put("userType", userType);
								datarecord.put("ouguid", record);
								datarecord.put("type", type);
								datarecord.put("ouname", ouname);
								datalist.add(datarecord);
							}
						}
					}
				}
				dataJson.put("content", datalist);
				dataJson.put("tabVuew", "bm");
			} else if ("industry".equals(targetId)) {
				// 根据行业获取左侧数据
				if ("company".equals(userType)) {
					SqlConditionUtil sql = new SqlConditionUtil();
					sql.eq("LENGTH(no)", "4");
					sql.like("No", "E2");
					List<AuditTaskDict> dictlist = taskdictservice.getAuditTaskDictList(sql.getMap()).getResult();

					if (dictlist != null && dictlist.size() > 0) {
						for (AuditTaskDict dict : dictlist) {
							String name = dict.getClassname();
							
							Record datarecord = new Record();
							datarecord.put("userType", userType);
							datarecord.put("type", 1);
							datarecord.put("name", name);
							datarecord.put("no", dict.getNo());
							datalist.add(datarecord);
						}
					}
				} else if ("person".equals(userType)) {
					SqlConditionUtil sql = new SqlConditionUtil();
					sql.eq("LENGTH(no)", "4");
					sql.like("No", "E1");
					List<AuditTaskDict> dictlist = taskdictservice.getAuditTaskDictList(sql.getMap()).getResult();

					if (dictlist != null && dictlist.size() > 0) {
						for (AuditTaskDict dict : dictlist) {
							String name = dict.getClassname();
							
							Record datarecord = new Record();
							datarecord.put("userType", userType);
							datarecord.put("type", 2);
							datarecord.put("name", name);
							datarecord.put("no", dict.getNo());
							datalist.add(datarecord);
						}
					}
				}
				dataJson.put("content", datalist);
				dataJson.put("tabVuew", "ws");
			} else if ("theme".equals(targetId)) {
				// 根据主题获取左侧列表
				if ("company".equals(userType)) {
					SqlConditionUtil sql= new SqlConditionUtil();
					sql.eq("LENGTH(no)", "4");
					sql.like("No", "B2");
					List<AuditTaskDict> dictlist = taskdictservice.getAuditTaskDictList(sql.getMap()).getResult();

					if (dictlist != null && dictlist.size() > 0) {
						for (AuditTaskDict dict : dictlist) {
							String name = dict.getClassname();
							
							Record datarecord = new Record();
							datarecord.put("userType", userType);
							datarecord.put("type", 1);
							datarecord.put("name", name);
							datarecord.put("no", dict.getNo());
							datalist.add(datarecord);
						}
					}
				} else if ("person".equals(userType)) {
					SqlConditionUtil sql= new SqlConditionUtil();
					sql.eq("LENGTH(no)", "4");
					sql.like("No", "B1");
					List<AuditTaskDict> dictlist = taskdictservice.getAuditTaskDictList(sql.getMap()).getResult();

					if (dictlist != null && dictlist.size() > 0) {
						for (AuditTaskDict dict : dictlist) {
							String name = dict.getClassname();
						
							Record datarecord = new Record();
							datarecord.put("userType", userType);
							datarecord.put("type", 2);
							datarecord.put("name", name);
							datarecord.put("no", dict.getNo());
							datalist.add(datarecord);
						}
					}
				}
				dataJson.put("content", datalist);
				dataJson.put("tabVuew", "zt");
			}

			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (JSONException e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

	}

	/**
	 * 
	 * [获取事项列表] [功能详细描述]
	 * 
	 * @param params
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(value = "/getTaskList", method = RequestMethod.POST)
	public String getTaskList(@RequestBody String params) {
		try {
			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));// token验证
			JSONObject obj = (JSONObject) json.get("params");

			String type = obj.getString("type");

			String centerguid = obj.getString("centerguid");
			String no = obj.getString("no");
			String usertype = obj.getString("UserType");
			String ouguid = obj.getString("ouguid");
			String page = obj.getString("page");
		
			int pageindex = 0;
			if (StringUtil.isNotBlank(page)) {
				pageindex = Integer.parseInt(page);
			}
			int firstResult = pageindex * 17;
			int maxResult = 17;

			SqlConditionUtil sql = new SqlConditionUtil();
			sql.eq("centerguid", centerguid);
			sql.eq("is_enable", QueueConstant.CONSTANT_STR_ONE);

			if ("company".equals(usertype)) {
				sql.like("applyertype", "%10%");
			} else if ("person".equals(usertype)) {
				sql.like("applyertype", "%20%");
			}

			if (StringUtil.isNotBlank(ouguid)) {
				sql.eq("ouguid", ouguid);
			}
			if (StringUtil.isNotBlank(no)) {
				sql.like("taskmap", no + ";");
			}
			PageData<AuditZnsbCentertask> taskpagedata = centertaskservice
					.getCenterTaskPageData(sql.getMap(), firstResult, maxResult, "ordernum", "desc").getResult();
			JSONObject dataJson = new JSONObject();
			List<Record> recordlist = new ArrayList<>();
			for (AuditZnsbCentertask task : taskpagedata.getList()) {
				Record record = new Record();
				record.put("type", type);
				record.put("taskguid", task.getTaskguid());
				record.put("itemid", task.getItem_id());
			
				record.put("taskname", task.getTaskname());

				recordlist.add(record);
			}

			dataJson.put("content", recordlist);
			dataJson.put("totalpages", String.valueOf(taskpagedata.getRowCount()/17));
			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (Exception e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

	}

	/**
	 * 
	 * [获取事项详情] [功能详细描述]
	 * 
	 * @param params
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(value = "/getTaskDetailByguid", method = RequestMethod.POST)
	public String getTaskDetailByguid(@RequestBody String params) {
		try {
			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));// token验证
			JSONObject obj = (JSONObject) json.get("params");
			String taskguid = obj.getString("taskguid");// audittask的唯一键
			JSONObject dataJson = new JSONObject();
				
			AuditTask task = taskservice.getAuditTaskByGuid(taskguid, true).getResult();
		
			if (task != null) {
				dataJson.put("taskname", task.getTaskname());
				dataJson.put("itemid", task.getItem_id());
				dataJson.put("ouname", task.getOuname());
				if (task.getPromise_day() == 0) {
					dataJson.put("promiseday", "即办件");
				} else {
					dataJson.put("promiseday", task.getPromise_day() + "个工作日");
				}
				if (task.getAnticipate_day() == 0) {
					dataJson.put("anticipateday", "即办件");
				} else {
					dataJson.put("anticipateday", task.getAnticipate_day() + "个工作日");
				}
			
				dataJson.put("shenpilb", task.getShenpilb());
				dataJson.put("shenpilbtext", codeItemsService.getItemTextByCodeName("审批类别", task.getShenpilb()));
				dataJson.put("linktel", task.getLink_tel());
				dataJson.put("supervisetel", task.getSupervise_tel());
				dataJson.put("transact_time", task.getTransact_time());
				if(StringUtil.isNotBlank(task.getTaskoutimgguid()))
				{
				
					List<FrameAttachInfo> attachInfolist = attachService
							.getAttachInfoListByGuid(task.getTaskoutimgguid());
					if (attachInfolist != null && attachInfolist.size() > 0)
					{
						dataJson.put("taskoutimgguid", attachInfolist.get(0).getAttachGuid());
					}				
					else
					{
						dataJson.put("taskoutimgguid", "");
					}
				}
				else
				{
					dataJson.put("taskoutimgguid", "");
				}
	        
				dataJson.put("chargeflag", task.getCharge_flag());

				dataJson.put("chargeflagtext",
						codeItemsService.getItemTextByCodeName("是否", String.valueOf(task.getCharge_flag())));

				AuditTaskExtension taskextension=taskextensionservice.getTaskExtensionByTaskGuid(taskguid, true).getResult();
				if(taskextension!=null)
				{
					dataJson.put("chargewhen", taskextension.getCharge_when());
					dataJson.put("chargewhentext",
							codeItemsService.getItemTextByCodeName("何时收费", String.valueOf(taskextension.getCharge_when())));
				}
				
				List<JSONObject> list = new ArrayList<JSONObject>();
				JSONObject matericalJson = new JSONObject();
				List<AuditTaskMaterial> taskmateriallist = taskmaterialservice
						.selectTaskMaterialListByTaskGuid(taskguid, true).getResult();

				for (AuditTaskMaterial taskmaterial : taskmateriallist) {
					matericalJson = new JSONObject();

					matericalJson.put("materialname", taskmaterial.getMaterialname());
					matericalJson.put("materialid", taskmaterial.getMaterialid());
					list.add(matericalJson);
				}

				
				
				dataJson.put("matericallist", list);
			}
		
			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (Exception e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

	}

}
