package com.epoint.auditselfservice.auditselfservicerest.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.epoint.auditselfservice.service.IJNTask;
import com.epoint.basic.auditorga.auditconfig.domain.AuditOrgaConfig;
import com.epoint.basic.auditorga.auditconfig.inter.IAuditOrgaConfig;
import com.epoint.basic.auditqueue.auditznsbcentertask.domain.AuditZnsbCentertask;
import com.epoint.basic.auditqueue.auditznsbcentertask.inter.IAuditZnsbCentertask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskCase;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.common.util.*;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;

@RestController
@RequestMapping("/ssselfservicetask")
public class SSTaskRestController {

	@Autowired
	private IAuditZnsbCentertask centertaskservice;


	@Autowired
	private IAuditTask taskservice;

	@Autowired
	private IAuditTaskExtension taskextensionservice;

	@Autowired
	private ICodeItemsService codeitemsservice;

	@Autowired
	private IAuditOrgaConfig auditconfigservice;

	@Autowired
	private IAttachService attachservice;

	@Autowired
    private IJNTask jntask;

	@Autowired
	private IAuditTaskCase auditTaskCaseService;

	@Autowired
	private IAuditTaskResult taskresultservice;

	@Autowired
	private IAuditTaskMaterialCase auditTaskMaterialCaseService;

	@SuppressWarnings("unchecked")
	public static  List<Record> syncbanlididiansj(String banlididiansj) {
		List<Record> list = new ArrayList<Record>();

		//解析XML
		Document document = null;
		try {
			document = DocumentHelper.parseText(banlididiansj);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		if (banlididiansj != null && StringUtil.isNotBlank(banlididiansj)) {
			if (document != null) {
				Element root = document.getRootElement();
				Element data1 = root.element("ACCEPT_ADDRESSS");
				List<Element> datas = data1.elements("ACCEPT_ADDRESS");
				for (Element data : datas) {
					Element NAME = data.element("NAME");
					Element ADDRESS = data.element("ADDRESS");
					Element OFFICE_HOUR = data.element("OFFICE_HOUR");
					Element WINDOW_NUM = data.element("WINDOW_NUM");
					Record record = new Record();
					if(StringUtil.isNotBlank(NAME)) {
						record.set("name", NAME.getStringValue());
					}
					if(StringUtil.isNotBlank(ADDRESS)) {
						record.set("address", ADDRESS.getStringValue());
					}
					if(StringUtil.isNotBlank(OFFICE_HOUR)) {
						record.set("officehour", OFFICE_HOUR.getStringValue());
					}
					if(StringUtil.isNotBlank(WINDOW_NUM)) {
						record.set("windownum", WINDOW_NUM.getStringValue());
					}

					list.add(record);
				}
			}
		}
		return list;

	}

	/**
	 * 获取事项详情Doc
	 *
	 * @params params
	 * @return
	 * @throws Exception
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping(value = "/getTaskDetailDoc", method = RequestMethod.POST)
	public String getTaskDetailDoc(@RequestBody String params, @Context HttpServletRequest request) throws Exception {
		try {

			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			JSONObject dataJson = new JSONObject();
			String taskguid = obj.getString("taskguid");
			String centerguid = obj.getString("centerguid");

			AuditTask task = taskservice.getAuditTaskByGuid(taskguid, true).getResult();
			if (task != null) {
				String officehour = null;
				String accept_address_info = task.get("accept_address_info");
				if(StringUtil.isNotBlank(accept_address_info)){
					List<Record> infoList = syncbanlididiansj(accept_address_info);
					if(infoList!=null && infoList.size()>0){
						Record record = infoList.get(0);
						if(record != null){
							officehour = record.getStr("officehour");
						}
					}
				}
				String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
				new License().setLicense(licenseName);// TODO
				String doctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
						+ "/znsb/selfservicemachine/print/tasktemplet.docx";
				com.aspose.words.Document doc = new com.aspose.words.Document(doctem);
				String[] fieldNames = null;
				Object[] values = null;
				// 获取域与对应的值
				Map<String, String> map = new HashMap<String, String>();
				map.put("TaskName", task.getTaskname());
				map.put("ItemID", task.getItem_id());
				map.put("OUName", task.getOuname());
				map.put("SUPERVISETEL", task.getSupervise_tel());
				map.put("BLTime", StringUtil.isNotBlank(officehour)?officehour:task.getTransact_time());
				map.put("BLLocation", task.getTransact_addr());
				map.put("BLOUName", task.getOuname());
				map.put("LINKTEL", task.getLink_tel());
				if (QueueConstant.Common_yes_String.equals(task.getCharge_flag())) {
					map.put("ChargeFlag", "是");
					AuditTaskExtension taskextension = taskextensionservice.getTaskExtensionByTaskGuid(taskguid, true)
							.getResult();
					if (taskextension != null) {
						map.put("ChargeWhen", codeitemsservice.getItemTextByCodeName("何时收费",
								String.valueOf(taskextension.getCharge_when())));
					} else {
						map.put("ChargeWhen", "无");
					}
				} else {
					map.put("ChargeFlag", "否");
					map.put("ChargeWhen", "无");
				}
				map.put("AnticipateDay", task.getAnticipate_day() + "个工作日");
				map.put("PromiseDay", task.getPromise_day() + "个工作日");
				fieldNames = new String[map == null ? 0 : map.size()];
				values = new Object[map == null ? 0 : map.size()];
				int num = 0;
				for (Entry<String, String> entry : map.entrySet()) {
					fieldNames[num] = entry.getKey();
					values[num] = entry.getValue();
					num++;
				}
				// 替换域
				doc.getMailMerge().execute(fieldNames, values);
				// 替换表格--实际是替换材料
				List<AuditTaskMaterial> taskmateriallist = jntask.getOrderdTaskMaterial(taskguid);
				List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
				Map<String, Object> data1 = null;
				if (taskmateriallist.size() == 0) {
					data1 = new HashMap<String, Object>();
					data1.put("TASKMATERIAL", "无");
					dataList.add(data1);
				} else {
					for (int i = 0; i < taskmateriallist.size(); i++) {
						data1 = new HashMap<String, Object>();
						data1.put("TASKMATERIAL", (i + 1) + "、" + taskmateriallist.get(i).getMaterialname());
						dataList.add(data1);
					}
				}
				doc.getMailMerge().executeWithRegions(new MapMailMergeDataSource(dataList, "Material"));

				String taskurl = "";
				if (StringUtil.isNotBlank(centerguid)) {
					SqlConditionUtil sql = new SqlConditionUtil();
					sql.eq("centerguid", centerguid);
					sql.eq("configname", "AS_TASK_URL");
					AuditOrgaConfig config = auditconfigservice.getCenterConfig(sql.getMap()).getResult();
					if (StringUtil.isNotBlank(config)) {
						taskurl = config.getConfigvalue();
						dataJson.put("taskurl", config.getConfigvalue());
					} else {
						taskurl = "";
					}
				}
				// 添加二维码
				InputStream qrCode = QrcodeUtil.getQrCode(taskurl + "?taskguid=" + taskguid, 150, 150);
				if (qrCode != null) {
					DocumentBuilder build = new DocumentBuilder(doc);
					// 指定对应的书签
					build.moveToBookmark("EWM");
					// 插入附件流信息
					build.insertImage(qrCode);
				}

				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				doc.save(outputStream, SaveFormat.DOC);// 保存成word
				ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
				long size = inputStream.available();

				// 附件信息
				FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
				frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
				frameAttachInfo.setAttachFileName("事项.doc");
				frameAttachInfo.setCliengTag("事项打印");
				frameAttachInfo.setUploadUserGuid("");
				frameAttachInfo.setUploadUserDisplayName("");
				frameAttachInfo.setUploadDateTime(new Date());
				frameAttachInfo.setContentType("application/msword");
				frameAttachInfo.setAttachLength(size);
				dataJson.put("taskdocattachguid",
						attachservice.addAttach(frameAttachInfo, inputStream).getAttachGuid());
				outputStream.close();
				inputStream.close();
			}

			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (JSONException e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

	}


	/**
	 * 获取事项详情
	 *
	 * @params params
	 * @return
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping(value = "/getTaskDetail", method = RequestMethod.POST)
	public String getTaskDetail(@RequestBody String params) {
		try {

			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			JSONObject dataJson = new JSONObject();
			String taskguid = obj.getString("taskguid");
			String centerguid = obj.getString("centerguid");

			AuditTask task = taskservice.getAuditTaskByGuid(taskguid, true).getResult();
			if (task != null) {

				String officehour = null;
				String accept_address_info = task.get("accept_address_info");
				if(StringUtil.isNotBlank(accept_address_info)){
					List<Record> infoList = syncbanlididiansj(accept_address_info);
					if(infoList!=null && infoList.size()>0){
						Record record = infoList.get(0);
						if(record != null){
							officehour = record.getStr("officehour");
						}
					}
				}

				dataJson.put("taskguid", task.getRowguid());
				dataJson.put("taskname", task.getTaskname());
				dataJson.put("itemid", task.getItem_id());
				dataJson.put("ouname", task.getOuname());
				dataJson.put("transacttime", StringUtil.isNotBlank(officehour)?officehour:task.getTransact_time());
				dataJson.put("transactaddr", task.getTransact_addr());
				dataJson.put("promiseday", task.getPromise_day() + "个工作日");

				dataJson.put("anticipateday", task.getAnticipate_day() + "个工作日");

				dataJson.put("linktel", task.getLink_tel());
				dataJson.put("supervisetel", task.getSupervise_tel());
				dataJson.put("condition", task.getAcceptcondition());
				dataJson.put("bylaw", task.getBy_law());
				dataJson.put("shenpilb", task.getShenpilb());
				dataJson.put("type", task.getType());
				dataJson.put("typetext",
						codeitemsservice.getItemTextByCodeName("事项类型", String.valueOf(task.getType())));

				dataJson.put("applyertype", task.getApplyertype());
				if ("10;".equals(task.getApplyertype())) {
					dataJson.put("applyertypetext", "企业");
				} else if ("20;".equals(task.getApplyertype())) {
					dataJson.put("applyertypetext", "个人");
				} else {
					dataJson.put("applyertypetext", "个人、企业");
				}

				dataJson.put("chargeflag", task.getCharge_flag());

				dataJson.put("chargeflagtext",
						codeitemsservice.getItemTextByCodeName("是否", String.valueOf(task.getCharge_flag())));

				AuditTaskExtension taskextension = taskextensionservice.getTaskExtensionByTaskGuid(taskguid, true)
						.getResult();
				if (taskextension != null) {
					dataJson.put("chargewhen", taskextension.getCharge_when());
					dataJson.put("chargewhentext", codeitemsservice.getItemTextByCodeName("何时收费",
							String.valueOf(taskextension.getCharge_when())));
				}

				AuditTaskResult taskresult = taskresultservice.getAuditResultByTaskGuid(taskguid, true).getResult();
				if (taskresult != null) {
					if (taskresult.getResulttype() == ZwfwConstant.LHSP_RESULTTYPE_ZZ) {
						dataJson.put("iscert", "1");
						dataJson.put("iscerttext", "是");
					} else {
						dataJson.put("iscert", "0");
						dataJson.put("iscerttext", "否");
					}
				} else {
					dataJson.put("iscert", "0");
					dataJson.put("iscerttext", "否");
				}

				AuditZnsbCentertask centertask = centertaskservice.getDetailbyTaskGuid(taskguid).getResult();
				if (centertask != null) {
					dataJson.put("reservationmanagement", centertask.getReservationmanagement());
					dataJson.put("webapplytype", centertask.getWebapplytype());
					dataJson.put("isshowform", centertask.getIs_show_form());
				}

				List<AuditTaskCase> auditTaskCases = auditTaskCaseService.selectTaskCaseByTaskGuid(taskguid)
						.getResult();

				List<JSONObject> caseconditionList = new ArrayList<JSONObject>();
				JSONObject conditionJson = new JSONObject();
				if (auditTaskCases.size() > 0) {
					for (AuditTaskCase auditTaskCase : auditTaskCases) {
						conditionJson = new JSONObject();
						conditionJson.put("taskcaseguid", auditTaskCase.getRowguid());// 多情形标识
						conditionJson.put("casename", auditTaskCase.getCasename());// 多情形名称
						caseconditionList.add(conditionJson);
					}
				}
				dataJson.put("caselist", caseconditionList);

				List<JSONObject> list = new ArrayList<JSONObject>();
				JSONObject matericalJson = new JSONObject();
				List<AuditTaskMaterial> taskmateriallist = jntask.getOrderdTaskMaterial(taskguid);

				for (AuditTaskMaterial taskmaterial : taskmateriallist) {
					matericalJson = new JSONObject();

					matericalJson.put("materialname", taskmaterial.getMaterialname());
					matericalJson.put("materialid", taskmaterial.getMaterialid());
					list.add(matericalJson);
				}
				if (StringUtil.isNotBlank(centerguid)) {
					SqlConditionUtil sql = new SqlConditionUtil();
					sql.eq("centerguid", centerguid);
					sql.eq("configname", "AS_TASK_URL");
					AuditOrgaConfig config = auditconfigservice.getCenterConfig(sql.getMap()).getResult();
					if (StringUtil.isNotBlank(config)) {
						dataJson.put("taskurl", config.getConfigvalue());
					} else {
						dataJson.put("taskurl", "");
					}
				}

				dataJson.put("matericallist", list);
			}

			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (JSONException e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

	}
}
