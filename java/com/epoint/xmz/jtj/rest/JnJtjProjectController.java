
package com.epoint.xmz.jtj.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.intermediary.sendmaterials.api.ISendMaterials;
import com.epoint.xmz.wjw.CommUtil;
import com.epoint.xmz.wjw.api.ICxBusService;

@RestController
@RequestMapping("/jnjtj")
public class JnJtjProjectController {
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());


	@Autowired
	private IAttachService attachService;
	
	private String userCode = "asdapp";
	
	private String key = "YXNkYXBwMjAyMDAyMjY";
	/**
	 * 办件材料API
	 */
	@Autowired
	private IAuditProjectMaterial iAuditProjectMaterial;

	@Autowired
    private IAuditSpISubapp iAuditSpISubapp;
	
	@Autowired
	private ISendMaterials sendMaterials;

	@Autowired
	private ICxBusService iCxBusService;

	@Autowired
	private IAuditProject iAuditProject;

	@Autowired
	private IAuditOnlineProject iAuditOnlineProject;
	
	private Map<String,String> tokenmsg = new HashMap<String,String>();

	/**
	 * 政务大厅注册用户API
	 */
	@Autowired
	private IAuditOnlineRegister iAuditOnlineRegister;

	public String uploadAttach(String attachguid,String id) {
		try {
			log.info("=======开始调用wjwuploadAttach接口=======");
			// 1、接口的入参转化为JSON对象
			String uploadurl = "https://fileserver.sdyzgl.com/upload?a=bcgl&s="+id;
			FrameAttachStorage attachStorage = attachService.getAttach(attachguid);
			String result = CommUtil.uploadForm("file", attachStorage.getContent(), attachStorage.getAttachFileName(), 
					uploadurl, attachStorage.getSize()+"");
			log.info("wjw上传附件接口返回结果：" + result);
			JSONObject dataresult = JSONObject.parseObject(result);
			if ("true".equals(dataresult.getString("concrete"))) {
				JSONArray files = dataresult.getJSONArray("files");
				JSONObject filetail = files.getJSONObject(0);
				return filetail.toString();
			} else {
				return "error";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}

	}

	/**
	 * 添加 巡游出租汽车道路运输证补发 申请信息
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/addJtjXyCHfApply", method = RequestMethod.POST)
	public String addJtjXyCHfApply(@RequestBody String params) {
		try {
			log.info("=======开始调用addJtjXyCHfApply接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			String projectguid = jsonObject.getString("projectguid");
			JSONObject json = new JSONObject();
			
			log.info("addJtjXyCHfApply入参：" + jsonObject);
			// 8、定义返回JSON对象

			AuditProject auditProject = sendMaterials.getAuditProjectByRowguid(projectguid);

			if (auditProject == null) {
				return JsonUtils.zwdtRestReturn("0", "该办件未查询到", "");
			}
			
			Record record = iCxBusService.getXyczqcdlyszbfByRowguid(projectguid);
			

			if (record == null) {
				return JsonUtils.zwdtRestReturn("0", "未找到该表单数据", "");
			}
			
			log.info("巡游出租汽车道路运输证换发record:" + record);
			

			// 车牌号
			String vehNo = record.getStr("cph");
			// 车牌颜色
			String vehPlateColor = record.getStr("cpys");
			// 申请换发原因
			String newsLose = record.getStr("bkdmtyssm");
			// 备注说明
			String remark = record.getStr("bzsm");
			// 创建人
			String createMan = record.getStr("cjr");
			
			
			SqlConditionUtil materialSqlConditionUtil = new SqlConditionUtil();
			materialSqlConditionUtil.eq("projectguid", projectguid);
			List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
					.selectProjectMaterialByCondition(materialSqlConditionUtil.getMap()).getResult();

			log.info("获取到的材料信息为：" + auditProjectMaterials);
			JSONArray files = new JSONArray();
			boolean inattach = true;
			boolean uploadresult = false;
			
			String now = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHH");
			String tokenid = "";
			if (StringUtil.isBlank(tokenmsg.get(now))) {
				String tokenurl = "https://services.sdyzgl.com/test/asdVehFacade/tokenInfoForApp/1.0";
				JSONObject json2 = new JSONObject();
				json2.put("hmac", "00E83326C00CAF3F7C05836CC13DCCBC7C955768");
				json2.put("userCode", userCode);
				String tokenidresult = HttpUtil.doPostJson(tokenurl, json2.toString());
				JSONObject tokenresult = JSONObject.parseObject(tokenidresult);
				String id = tokenresult.getString("id");
				tokenid = id;
				tokenmsg.put(now, id);
				log.info("tokenmsg0:"+tokenmsg);
			}
			else {
				tokenid = tokenmsg.get(now);
			}
		
			
			for (AuditProjectMaterial material : auditProjectMaterials) {
				if ("车辆行车证机动车登记证书及车辆照片（规格：45度角拍摄9.0cm×6.2cm彩色照片）；".equals(material.getTaskmaterial())) {
					List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
					if (attachs.size() != 0) {
						inattach = false;
					}
					for (FrameAttachInfo attach : attachs) {
						String result = uploadAttach( attach.getAttachGuid(),tokenid);
						if ("error".equals(result)) {
							uploadresult = true;
							break;
						}else {
							JSONObject attachnew = JSONObject.parseObject(result);
							Record rec = new Record();
							rec.set("attachmentName", attachnew.getString("name"));
							rec.set("fileNo", "602");
							rec.set("resourceId", attachnew.getString("resourceId"));
							files.add(rec);
						}
					}
				}
				else if("济宁市巡游出租汽车道路运输证配发、补发、换发申请表".equals(material.getTaskmaterial())) {
					List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
					if (attachs.size() != 0) {
						inattach = false;
					}
					for (FrameAttachInfo attach : attachs) {
						String result = uploadAttach( attach.getAttachGuid(),tokenid);
						if ("error".equals(result)) {
							uploadresult = true;
							break;
						}else {
							JSONObject attachnew = JSONObject.parseObject(result);
							Record rec = new Record();
							rec.set("attachmentName", attachnew.getString("name"));
							rec.set("fileNo", "601");
							rec.set("resourceId", attachnew.getString("resourceId"));
							files.add(rec);
						}
					}
				}
				else if("个人户的提供业户身份证".equals(material.getTaskmaterial())) {
					List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
					if (attachs.size() != 0) {
						inattach = false;
					}
					for (FrameAttachInfo attach : attachs) {
						String result = uploadAttach( attach.getAttachGuid(),tokenid);
						if ("error".equals(result)) {
							uploadresult = true;
							break;
						}else {
							JSONObject attachnew = JSONObject.parseObject(result);
							Record rec = new Record();
							rec.set("attachmentName", attachnew.getString("name"));
							rec.set("fileNo", "604");
							rec.set("resourceId", attachnew.getString("resourceId"));
							files.add(rec);
						}
					}
				}
				Thread.sleep(1000);
			}

			if (inattach) {
				return JsonUtils.zwdtRestReturn("0", "办件材料不存在！", "");
			}
			if (uploadresult) {
				return JsonUtils.zwdtRestReturn("0", "办件附件上传失败", "");
			}

			String asdFileInfoList = files.toString();
			
			String data = asdFileInfoList + createMan + newsLose + projectguid + remark + vehNo + vehPlateColor;
			
			log.info("hmac加密前："+data);
			String hmac = hamcsha1(data.getBytes(),key.getBytes());
			
			log.info("hmac:"+hmac);
			
			json.put("userCode", userCode);
			json.put("hmac", hmac);
			json.put("vehNo", vehNo);
			json.put("vehPlateColor", vehPlateColor);
			json.put("newsLose", newsLose);
			json.put("remark", remark);
			json.put("projectguid", projectguid);
			json.put("createMan", createMan);
			json.put("asdFileInfoList", JSON.toJSONString(files));
			
			log.info("接口申请信息如下："+json);
			String result = HttpUtil.doPostJson("https://services.sdyzgl.com/test/asdVehFacade/vehReissue/1.0", json+"");
			log.info("返回结果如下："+result);

			JSONObject dataresult = JSONObject.parseObject(result);
			
			if ("true".equals(dataresult.getString("success"))) {
				return JsonUtils.zwdtRestReturn("1", "添加申请信息成功", "");
			} else {
				return JsonUtils.zwdtRestReturn("0", "添加申请信息失败", "");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======addApply接口参数：params【" + params + "】=======");
			log.info("=======addApply异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "添加申请信息失败：" + e.getMessage(), "");
		}
	}

	/**
	 * 添加 经营性道路货物运输驾驶员从业资格许可 申请信息
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/addJtjHwcyzgsqApply", method = RequestMethod.POST)
	public String addJtjHwcyzgsqApply(@RequestBody String params) {
		try {
			log.info("=======开始调用addJtjHwcyzgsqApply接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			String projectguid = jsonObject.getString("projectguid");
			JSONObject json = new JSONObject();
			log.info("addJtjHwcyzgsqApply入参：" + jsonObject);
			// 8、定义返回JSON对象

			AuditProject auditProject = sendMaterials.getAuditProjectByRowguid(projectguid);
			if (auditProject == null) {
				return JsonUtils.zwdtRestReturn("0", "该办件未查询到", "");
			}
			Record record = iCxBusService.getHwcyzgsqByRowguid(projectguid);
			if (record == null) {
				return JsonUtils.zwdtRestReturn("0", "未找到该表单数据", "");
			}
			log.info("经营性道路货物运输驾驶员从业资格许可record:" + record);

			// 从业人员姓名
			String staffName = record.getStr("cyryxm");
			// 联系电话
			String telephone = record.getStr("lxdh");
			// 证件号码
			String staffCredentialNo = record.getStr("zjhm");
			// 住址
			String address = record.getStr("zz");
			// 备注说明
			String remark = record.getStr("bzsm");
			// 行政区划
			String districtCode = record.getStr("xzqh");
			// 出生日期
			String birthday = EpointDateUtil.convertDate2String(record.getDate("csrq"), EpointDateUtil.DATE_FORMAT);
			// 性别
			String sex = record.getStr("xb");
			// 国籍
			String nationality = record.getStr("gj");
			// 文化程度
			String eduLevel = record.getStr("whcd");
			// 民族
			String ethnicity = record.getStr("mz");
			// 准驾车型
			String drivingClass = record.getStr("zjcx");
			// 驾驶证初领日期
			String drivingLicFirstIssueDate = EpointDateUtil.convertDate2String(record.getDate("jszclrq"), EpointDateUtil.DATE_FORMAT);
			// 驾照有效期限
			String drivingLicValidPeriod = record.getStr("jzyxqx");
			// 资格类别
			String qualificationType = "02001";
			// 创建人
			String createMan = record.getStr("cjr");
			
			
			SqlConditionUtil materialSqlConditionUtil = new SqlConditionUtil();
			materialSqlConditionUtil.eq("projectguid", projectguid);
			List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
					.selectProjectMaterialByCondition(materialSqlConditionUtil.getMap()).getResult();

			JSONArray files = new JSONArray();
			boolean inattach = true;
			boolean uploadresult = false;
			
			String now = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHH");
			String tokenid = "";
			if (StringUtil.isBlank(tokenmsg.get(now))) {
				String tokenurl = "https://services.sdyzgl.com/test/asdVehFacade/tokenInfoForApp/1.0";
				JSONObject json2 = new JSONObject();
				json2.put("hmac", "00E83326C00CAF3F7C05836CC13DCCBC7C955768");
				json2.put("userCode", userCode);
				String tokenidresult = HttpUtil.doPostJson(tokenurl, json2.toString());
				JSONObject tokenresult = JSONObject.parseObject(tokenidresult);
				String id = tokenresult.getString("id");
				tokenid = id;
				tokenmsg.put(now, id);
				log.info("tokenmsg1:"+tokenmsg);
			}
			else {
				tokenid = tokenmsg.get(now);
			}
		
			
			
			for (AuditProjectMaterial material : auditProjectMaterials) {
				if ("经营性道路运输客货危驾驶员从业资格考试申请表".equals(material.getTaskmaterial())) {
					List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
					if (attachs.size() != 0) {
						inattach = false;
					}
					for (FrameAttachInfo attach : attachs) {
						String result = uploadAttach( attach.getAttachGuid(),tokenid);
						if ("error".equals(result)) {
							uploadresult = true;
							break;
						}else {
							JSONObject attachnew = JSONObject.parseObject(result);
							Record rec = new Record();
							rec.set("attachmentName", attachnew.getString("name"));
							rec.set("fileNo", "1201");
							rec.set("resourceId", attachnew.getString("resourceId"));
							files.add(rec);
						}
					}
				}
				else if("流动人口信息回执单或居住证（申请人在暂住地报名时需提供）".equals(material.getTaskmaterial())) {
					List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
					if (attachs.size() != 0) {
						inattach = false;
					}
					for (FrameAttachInfo attach : attachs) {
						String result = uploadAttach( attach.getAttachGuid(),tokenid);
						if ("error".equals(result)) {
							uploadresult = true;
							break;
						}else {
							JSONObject attachnew = JSONObject.parseObject(result);
							Record rec = new Record();
							rec.set("attachmentName", attachnew.getString("name"));
							rec.set("fileNo", "1202");
							rec.set("resourceId", attachnew.getString("resourceId"));
							files.add(rec);
						}
					}
				}
				else if("原从业资格证（初次申请不需要提供）、申请人驾驶证、身份证".equals(material.getTaskmaterial())) {
					List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
					if (attachs.size() != 0) {
						inattach = false;
					}
					for (FrameAttachInfo attach : attachs) {
						String result = uploadAttach( attach.getAttachGuid(),tokenid);
						if ("error".equals(result)) {
							uploadresult = true;
							break;
						}else {
							JSONObject attachnew = JSONObject.parseObject(result);
							Record rec = new Record();
							rec.set("attachmentName", attachnew.getString("name"));
							rec.set("fileNo", "1203");
							rec.set("resourceId", attachnew.getString("resourceId"));
							files.add(rec);
						}
					}
				}
				Thread.sleep(1000);
			}

			if (inattach) {
				return JsonUtils.zwdtRestReturn("0", "办件材料不存在！", "");
			}
			if (uploadresult) {
				return JsonUtils.zwdtRestReturn("0", "办件附件上传失败", "");
			}

			String asdFileInfoList = files.toString();
			
			String data = address + asdFileInfoList + birthday + createMan + districtCode + drivingClass + drivingLicFirstIssueDate + drivingLicValidPeriod 
					+ eduLevel + ethnicity + nationality + projectguid + qualificationType + remark + sex + staffCredentialNo + staffName + telephone ;
			
			log.info("hmac加密前："+data);
			String hmac = hamcsha1(data.getBytes(),key.getBytes());
			
			log.info("hmac:"+hmac);
			
			json.put("userCode", userCode);
			json.put("hmac", hmac);
			json.put("staffName", staffName);
			json.put("telephone", telephone);
			json.put("staffCredentialNo", staffCredentialNo);
			json.put("address", address);
			json.put("remark", remark);
			json.put("districtCode", districtCode);
			json.put("birthday", birthday);
			json.put("sex", sex);
			json.put("nationality", nationality);
			json.put("eduLevel", eduLevel);
			json.put("ethnicity", ethnicity);
			json.put("drivingClass", drivingClass);
			json.put("drivingLicFirstIssueDate", drivingLicFirstIssueDate);
			json.put("drivingLicValidPeriod", drivingLicValidPeriod);
			json.put("qualificationType", qualificationType);
			json.put("createMan", createMan);
			json.put("projectguid", projectguid);
			json.put("createMan", createMan);
			json.put("asdFileInfoList", JSON.toJSONString(files));
			
			log.info("接口申请信息如下："+json);
			String result = HttpUtil.doPostJson("https://services.sdyzgl.com/test/asdStaffFacade/freightDriverQua/1.0", json+"");
			log.info("返回结果如下："+result);

			JSONObject dataresult = JSONObject.parseObject(result);
			
			if ("true".equals(dataresult.getString("success"))) {
				return JsonUtils.zwdtRestReturn("1", "添加申请信息成功", "");
			} else {
				return JsonUtils.zwdtRestReturn("0", "添加申请信息失败", "");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======addJtjHwcyzgsqApply接口参数：params【" + params + "】=======");
			log.info("=======addJtjHwcyzgsqApply异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "添加申请信息失败：" + e.getMessage(), "");
		}
	}

	/**
	 * 添加 经营性道路旅客运输驾驶员从业资格许可  申请信息
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/addJtjLkcyzgsqApply", method = RequestMethod.POST)
	public String addJtjLkcyzgsqApply(@RequestBody String params) {
		try {
			log.info("=======开始调用addJtjLkcyzgsqApply接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			String projectguid = jsonObject.getString("projectguid");
			JSONObject json = new JSONObject();
			log.info("addJtjLkcyzgsqApply入参：" + jsonObject);
			// 8、定义返回JSON对象

			AuditProject auditProject = sendMaterials.getAuditProjectByRowguid(projectguid);
			if (auditProject == null) {
				return JsonUtils.zwdtRestReturn("0", "该办件未查询到", "");
			}
			Record record = iCxBusService.getLkcyzgsqByRowguid(projectguid);
			if (record == null) {
				return JsonUtils.zwdtRestReturn("0", "未找到该表单数据", "");
			}
			log.info("经营性道路旅客运输驾驶员从业资格许可record:" + record);

			// 从业人员姓名
			String staffName = record.getStr("cyryxm");
			// 联系电话
			String telephone = record.getStr("lxdh");
			// 证件号码
			String staffCredentialNo = record.getStr("zjhm");
			// 住址
			String address = record.getStr("zz");
			// 备注说明
			String remark = record.getStr("bzsm");
			// 行政区划
			String districtCode = record.getStr("xzqh");
			// 出生日期
			String birthday = EpointDateUtil.convertDate2String(record.getDate("csrq"), EpointDateUtil.DATE_FORMAT);
			// 性别
			String sex = record.getStr("xb");
			// 国籍
			String nationality = record.getStr("gj");
			// 文化程度
			String eduLevel = record.getStr("whcd");
			// 民族
			String ethnicity = record.getStr("mz");
			// 准驾车型
			String drivingClass = record.getStr("zjcx");
			// 驾驶证初领日期
			String drivingLicFirstIssueDate = EpointDateUtil.convertDate2String(record.getDate("jszclrq"), EpointDateUtil.DATE_FORMAT);
			// 驾照有效期限
			String drivingLicValidPeriod = record.getStr("jszyxrq");
			// 资格类别
			String qualificationType = "01001";
			// 创建人
			String createMan = record.getStr("cjr");
			
			
			SqlConditionUtil materialSqlConditionUtil = new SqlConditionUtil();
			materialSqlConditionUtil.eq("projectguid", projectguid);
			List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
					.selectProjectMaterialByCondition(materialSqlConditionUtil.getMap()).getResult();

			JSONArray files = new JSONArray();
			boolean inattach = true;
			boolean uploadresult = false;
			
			String now = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHH");
			String tokenid = "";
			if (StringUtil.isBlank(tokenmsg.get(now))) {
				String tokenurl = "https://services.sdyzgl.com/test/asdVehFacade/tokenInfoForApp/1.0";
				JSONObject json2 = new JSONObject();
				json2.put("hmac", "00E83326C00CAF3F7C05836CC13DCCBC7C955768");
				json2.put("userCode", userCode);
				String tokenidresult = HttpUtil.doPostJson(tokenurl, json2.toString());
				JSONObject tokenresult = JSONObject.parseObject(tokenidresult);
				String id = tokenresult.getString("id");
				tokenid = id;
				tokenmsg.put(now, id);
				log.info("tokenmsg2:"+tokenmsg);
			}
			else {
				tokenid = tokenmsg.get(now);
			}
			
			for (AuditProjectMaterial material : auditProjectMaterials) {
				if ("经营性道路运输客货危驾驶员从业资格考试申请表（现场报名系统录入）".equals(material.getTaskmaterial())) {
					List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
					if (attachs.size() != 0) {
						inattach = false;
					}
					for (FrameAttachInfo attach : attachs) {
						String result = uploadAttach( attach.getAttachGuid(),tokenid);
						if ("error".equals(result)) {
							uploadresult = true;
							break;
						}else {
							JSONObject attachnew = JSONObject.parseObject(result);
							Record rec = new Record();
							rec.set("attachmentName", attachnew.getString("name"));
							rec.set("fileNo", "1104");
							rec.set("resourceId", attachnew.getString("resourceId"));
							files.add(rec);
						}
					}
				}
				else if("流动人口信息回执单或居住证（申请人在暂住地报名时需提供）".equals(material.getTaskmaterial())) {
					List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
					if (attachs.size() != 0) {
						inattach = false;
					}
					for (FrameAttachInfo attach : attachs) {
						String result = uploadAttach( attach.getAttachGuid(),tokenid);
						if ("error".equals(result)) {
							uploadresult = true;
							break;
						}else {
							JSONObject attachnew = JSONObject.parseObject(result);
							Record rec = new Record();
							rec.set("attachmentName", attachnew.getString("name"));
							rec.set("fileNo", "1101");
							rec.set("resourceId", attachnew.getString("resourceId"));
							files.add(rec);
						}
					}
				}
				else if("原从业资格证（初次申请不需提供）、申请人驾驶证、身份证".equals(material.getTaskmaterial())) {
					List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
					if (attachs.size() != 0) {
						inattach = false;
					}
					for (FrameAttachInfo attach : attachs) {
						String result = uploadAttach( attach.getAttachGuid(),tokenid);
						if ("error".equals(result)) {
							uploadresult = true;
							break;
						}else {
							JSONObject attachnew = JSONObject.parseObject(result);
							Record rec = new Record();
							rec.set("attachmentName", attachnew.getString("name"));
							rec.set("fileNo", "1102");
							rec.set("resourceId", attachnew.getString("resourceId"));
							files.add(rec);
						}
					}
				}
				Thread.sleep(1000);
			}

			if (inattach) {
				return JsonUtils.zwdtRestReturn("0", "办件材料不存在！", "");
			}
			if (uploadresult) {
				return JsonUtils.zwdtRestReturn("0", "办件附件上传失败", "");
			}

			String asdFileInfoList = files.toString();
			
			String data = address + asdFileInfoList  + birthday + createMan + districtCode + drivingClass + drivingLicFirstIssueDate + drivingLicValidPeriod 
					+ eduLevel + ethnicity + nationality + projectguid + qualificationType + remark + sex + staffCredentialNo + staffName + telephone ;
			
			log.info("hmac加密前："+data);
			String hmac = hamcsha1(data.getBytes(),key.getBytes());
			
			log.info("hmac:"+hmac);
			
			json.put("userCode", userCode);
			json.put("hmac", hmac);
			json.put("staffName", staffName);
			json.put("telephone", telephone);
			json.put("staffCredentialNo", staffCredentialNo);
			json.put("address", address);
			json.put("remark", remark);
			json.put("districtCode", districtCode);
			json.put("birthday", birthday);
			json.put("sex", sex);
			json.put("nationality", nationality);
			json.put("eduLevel", eduLevel);
			json.put("ethnicity", ethnicity);
			json.put("drivingClass", drivingClass);
			json.put("drivingLicFirstIssueDate", drivingLicFirstIssueDate);
			json.put("drivingLicValidPeriod", drivingLicValidPeriod);
			json.put("qualificationType", qualificationType);
			json.put("createMan", createMan);
			json.put("projectguid", projectguid);
			json.put("createMan", createMan);
			json.put("asdFileInfoList", JSON.toJSONString(files));
			
			log.info("接口申请信息如下："+json);
			String result = HttpUtil.doPostJson("https://services.sdyzgl.com/test/asdStaffFacade/passengerDriverQua/1.0", json+"");
			log.info("返回结果如下："+result);

			JSONObject dataresult = JSONObject.parseObject(result);
			
			if ("true".equals(dataresult.getString("success"))) {
				return JsonUtils.zwdtRestReturn("1", "添加申请信息成功", "");
			} else {
				return JsonUtils.zwdtRestReturn("0", "添加申请信息失败", "");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======addJtjHwcyzgsqApply接口参数：params【" + params + "】=======");
			log.info("=======addJtjHwcyzgsqApply异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "添加申请信息失败：" + e.getMessage(), "");
		}
	}

	
	
	/**
	 * 获取办件结果
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/getProjectReuslt", method = RequestMethod.POST)
	public String getProjectReuslt(@RequestBody String params) {
		try {
			log.info("=======开始调用getProjectReuslt接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			log.info("getProjectReuslt入参：" + jsonObject);
			// 8、定义返回JSON对象
			JSONObject dataJson = new JSONObject();
			String projectguid = jsonObject.getString("projectguid");

			AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(projectguid, "370800").getResult();

			if (auditProject == null) {
				SqlConditionUtil condition = new SqlConditionUtil();
				condition.eq("SUBAPPGUID", projectguid);;
				List<AuditProject> projects = iAuditProject.getAuditProjectListByCondition(condition.getMap()).getResult();
				if (!projects.isEmpty()) {
					for (AuditProject project : projects) {
						Map<String, String> updateFieldMap = new HashMap<>();
						Map<String, String> updateDateFieldMap = new HashMap<String, String>();
						updateFieldMap.put("status=", "90");
						updateFieldMap.put("Banjieresult=", "40");
						updateFieldMap.put("Operateusername=", "交通厅办结");
						SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
						sqlConditionUtil.eq("sourceguid", project.getRowguid());
						iAuditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap,
								sqlConditionUtil.getMap());
						
						project.setOperateusername("交通厅办结");
						project.setStatus(90);
						project.setBanjieresult(40);
						iAuditProject.updateProject(project);
					}
					
				}else {
					return JsonUtils.zwdtRestReturn("0", "办件信息为空", "");
				}
			}else {
				Map<String, String> updateFieldMap = new HashMap<>();
				Map<String, String> updateDateFieldMap = new HashMap<String, String>();
				updateFieldMap.put("status=", "90");
				updateFieldMap.put("Banjieresult=", "40");
				updateFieldMap.put("Operateusername=", "交通厅办结");
				SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
				sqlConditionUtil.eq("sourceguid", projectguid);
				iAuditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap,
						sqlConditionUtil.getMap());
				
				auditProject.setOperateusername("交通厅办结");
				auditProject.setStatus(90);
				auditProject.setBanjieresult(40);
				iAuditProject.updateProject(auditProject);
			}
			
			return JsonUtils.zwdtRestReturn("1", "办件办结", dataJson.toString());
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getReuslt接口参数：params【" + params + "】=======");
			log.info("=======getReuslt异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "查询办结信息结果失败：" + e.getMessage(), "");
		}
	}

	/**
	 * 获得服务器端的数据,以InputStream形式返回
	 * 
	 * @return
	 */
	public static InputStream getInputStream(String URLPATH, String header) {
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

	/**
	 * 获取用户唯一标识
	 * 
	 * @param httpServletRequest
	 * @return
	 */
	private AuditOnlineRegister getOnlineRegister(HttpServletRequest httpServletRequest) {
		AuditOnlineRegister auditOnlineRegister;
		OAuthCheckTokenInfo oAuthCheckTokenInfo = CheckTokenUtil.getCheckTokenInfo(httpServletRequest);
		if (oAuthCheckTokenInfo != null) {
			// 手机端
			// 通过登录名获取用户
			auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(oAuthCheckTokenInfo.getLoginid())
					.getResult();
		} else {
			// PC端
			String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
			if (StringUtil.isNotBlank(accountGuid)) {
				auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
			} else {
				// 通过登录名获取用户
				auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.getCurrentIdentity())
						.getResult();
			}
		}
		return auditOnlineRegister;
	}

	
	
	public static String hamcsha1(byte[] data, byte[] key) 
	{
	      try {
	          SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
	          Mac mac = Mac.getInstance("HmacSHA1");
	          mac.init(signingKey);
	          return byte2hex(mac.doFinal(data));
	      } catch (NoSuchAlgorithmException e) {
	           e.printStackTrace();
	      } catch (InvalidKeyException e) {
	           e.printStackTrace();
	      }
	     return null;
	 }
	
	//二行制转字符串  
	public static String byte2hex(byte[] b) 
	{
	    StringBuilder hs = new StringBuilder();
	    String stmp;
	    for (int n = 0; b!=null && n < b.length; n++) {
	        stmp = Integer.toHexString(b[n] & 0XFF);
	        if (stmp.length() == 1)
	            hs.append('0');
	        hs.append(stmp);
	    }
	    return hs.toString().toUpperCase();
	}
	
	/**
     *  获取天气信息
     *  
     *  @param params 接口的入参
     *  @return
     */
    @RequestMapping(value = "/getWeatherInfo", method = RequestMethod.POST)
    public String getWeatherInfo(@RequestBody String params) {
        try {
            log.info("=======开始调用getWeatherInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String cityName = obj.getString("cityname");
                String apiUrl = String.format("http://www.sojson.com/open/api/weather/json.shtml?city=%s", cityName);
                URL url = new URL(apiUrl);
                URLConnection open = url.openConnection();
                InputStream input = open.getInputStream();
                String weatherResultStr = org.apache.commons.io.IOUtils.toString(input, "utf-8");
                JSONObject weatherResult = JSONObject.parseObject(weatherResultStr);
                JSONObject dataJson = new JSONObject();
                if (StringUtil.isNotBlank(weatherResultStr)) {
                    // 获取天气返回的Json字串
                    if (weatherResult != null) {
                        dataJson.put("city", weatherResult.getString("city"));
                        JSONObject nowWeatherJson = weatherResult.getJSONObject("data");
                        dataJson.put("nowwendu", nowWeatherJson.getString("wendu"));
                        dataJson.put("nowshidu", nowWeatherJson.getString("shidu"));
                        JSONArray jSONArray = nowWeatherJson.getJSONArray("forecast");
                        if (jSONArray != null) {
                            JSONObject objWeather = jSONArray.getJSONObject(0);
                            dataJson.put("today", objWeather.getString("date"));
                            dataJson.put("high", objWeather.getString("high"));
                            dataJson.put("low", objWeather.getString("low"));
                            dataJson.put("type", objWeather.getString("type"));
                        }
                    }
                }
                System.out.println(dataJson.toString());
                log.info("=======结束调用getWeatherInfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取天气成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "获取天气数据失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getWeatherInfo接口参数：params【" + params + "】=======");
            log.info("=======getWeatherInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取天气数据失败：" + e.getMessage(), "");
        }
    }
    
    
    /**
     * 
     *  天气
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getWeather", method = RequestMethod.POST)
    public String getWeather(@RequestBody String params, @Context HttpServletRequest request) {
        log.info("=======开始调用getWeather接口=======");
        try {
            // 1.1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 默认获取石家庄市天气
                URL url = new URL("http://www.weather.com.cn/data/cityinfo/101120701.html");
                InputStreamReader isReader = new InputStreamReader(url.openStream(), "UTF-8");
                BufferedReader br = new BufferedReader(isReader);// 采用缓冲式读入
                String str = null;
                String strReturn = null;
                while ((str = br.readLine()) != null) {
                    strReturn = str;
                }
                br.close();// 网上资源使用结束后，数据流及时关闭
                isReader.close();
                // 获取总数
                JSONObject dataJson = new JSONObject();
                JSONObject dataJson1 = new JSONObject();
                
                JSONObject result = JSONObject.parseObject(strReturn);
                
                JSONObject weatherinfo = result.getJSONObject("weatherinfo");
                
                String temp1 = weatherinfo.getString("temp1");
                String temp2 = weatherinfo.getString("temp2");
                String weather = weatherinfo.getString("weather");
                
                String result1 = "济宁市 " + weather + temp1 + "~" + temp2;
                String date = EpointDateUtil.convertDate2String(new Date(), "yyyy年MM月dd日 EEEE");
                dataJson1.put("weather", result1);
                dataJson1.put("time", date);
                dataJson.put("item", dataJson1);
                log.info("=======结束调用getWeather接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取天气成功", dataJson.toString());
            }
            else {
                log.info("=======结束调用getWeather接口=======");
                return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
            }
        }
        catch (Exception e) {
            log.info("=======getWeather初始化数据接口调用异常=======");
            e.printStackTrace();
        }
        log.info("=======结束调用getWeather接口=======");
        return JsonUtils.zwdtRestReturn("0", "获取天气失败：", "");
    }
    
    
    /**
	 * 添加 道路班车客运（含班线）经营许可核发 信息
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/addDlkyjyxkApply", method = RequestMethod.POST)
	public String addDlkyjyxkApply(@RequestBody String params) {
		try {
			log.info("=======开始调用addDlkyjyxkApply接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			String projectguid = jsonObject.getString("projectguid");
			JSONObject json = new JSONObject();
			
			log.info("addDlkyjyxkApply入参：" + jsonObject);
			// 8、定义返回JSON对象

			AuditProject auditProject = sendMaterials.getAuditProjectByRowguid(projectguid);

			if (auditProject == null) {
				return JsonUtils.zwdtRestReturn("0", "该办件未查询到", "");
			}
			
			Record record = iCxBusService.getDlkyjyxkByRowguid(projectguid);
			

			if (record == null) {
				return JsonUtils.zwdtRestReturn("0", "未找到该表单数据", "");
			}
			
			log.info(" 道路班车客运（含班线）经营许可核发record:" + record);
			

			// 业户档案编号
			String archiveNo = record.getStr("archiveNo");
			// 管辖区域代码
			String mgmtAreaCode = record.getStr("mgmtAreaCode");
			// 申请换发原因
			String ownerName = record.getStr("ownerName");
			// 备注
			String remark = record.getStr("remark");
			// 行政许可决定书文号
			String licenDecNo = record.getStr("licenDecNo");
			// 企业负责人
			String legalPerson = record.getStr("legalPerson");
			// 企业负责人电话
			String legalTel = record.getStr("legalTel");
			// 业户地址
			String ownerAddress = record.getStr("ownerAddress");
			// 户籍地行政区划编码
			String districtCode = record.getStr("districtCode");
			// 组织机构代码
			String orgCode = record.getStr("orgCode");
			// 创建人
			String ownerTel = record.getStr("ownerTel");
			// 创建人
			String faxNo = record.getStr("faxNo");
			// 创建人
			String economicType = record.getStr("economicType");
			// 创建人
			String bizLicCode = record.getStr("bizLicCode");
			// 创建人
			String bizLicIssueDate = record.getStr("bizLicIssueDate");
			// 创建人
			String investor = record.getStr("investor");
			// 创建人
			String investorNationality = record.getStr("investorNationality");
			// 创建人
			String stateTaxNo = record.getStr("stateTaxNo");
			// 创建人
			String stateTaxCertDate = record.getStr("stateTaxCertDate");
			// 地税税务登记号
			String localTaxNo = record.getStr("localTaxNo");
			// 创建人
			String localTaxCertDate = record.getStr("localTaxCertDate");
			// 创建人
			String licenceWord = record.getStr("licenceWord");
			// 创建人
			String licenceCode = record.getStr("licenceCode");
			// 创建人
			String licValidDateBegin = record.getStr("licValidDateBegin");
			// 创建人
			String licValidDateEnd = record.getStr("licValidDateEnd");
			// 创建人
			String registeredCapital = record.getStr("registeredCapital");
			// 创建人
			String foreignCapital = record.getStr("foreignCapital");
			// 创建人
			String bizScopes = record.getStr("bizScopes");
			// 创建人
			String provinceOwner = record.getStr("provinceOwner");
			// 创建人
			String createMan = record.getStr("createMan");
			// 创建人
			String child = record.getStr("child");
			// 创建人
			String parentOwnerName = record.getStr("parentOwnerName");
			
			
			
			SqlConditionUtil materialSqlConditionUtil = new SqlConditionUtil();
			materialSqlConditionUtil.eq("projectguid", projectguid);
			List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
					.selectProjectMaterialByCondition(materialSqlConditionUtil.getMap()).getResult();

			log.info("获取到的材料信息为：" + auditProjectMaterials);
			JSONArray files = new JSONArray();
			boolean inattach = true;
			boolean uploadresult = false;
			
			String now = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHH");
			String tokenid = "";
			if (StringUtil.isBlank(tokenmsg.get(now))) {
				String tokenurl = "https://services.sdyzgl.com/test/asdVehFacade/tokenInfoForApp/1.0";
				JSONObject json2 = new JSONObject();
				json2.put("hmac", "00E83326C00CAF3F7C05836CC13DCCBC7C955768");
				json2.put("userCode", userCode);
				String tokenidresult = HttpUtil.doPostJson(tokenurl, json2.toString());
				JSONObject tokenresult = JSONObject.parseObject(tokenidresult);
				String id = tokenresult.getString("id");
				tokenid = id;
				tokenmsg.put(now, id);
				log.info("tokenmsg0:"+tokenmsg);
			}
			else {
				tokenid = tokenmsg.get(now);
			}
		
			
			for (AuditProjectMaterial material : auditProjectMaterials) {
				if ("车辆行车证机动车登记证书及车辆照片（规格：45度角拍摄9.0cm×6.2cm彩色照片）；".equals(material.getTaskmaterial())) {
					List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
					if (attachs.size() != 0) {
						inattach = false;
					}
					for (FrameAttachInfo attach : attachs) {
						String result = uploadAttach( attach.getAttachGuid(),tokenid);
						if ("error".equals(result)) {
							uploadresult = true;
							break;
						}else {
							JSONObject attachnew = JSONObject.parseObject(result);
							Record rec = new Record();
							rec.set("attachmentName", attachnew.getString("name"));
							rec.set("fileNo", "602");
							rec.set("resourceId", attachnew.getString("resourceId"));
							files.add(rec);
						}
					}
				}
				else if("济宁市巡游出租汽车道路运输证配发、补发、换发申请表".equals(material.getTaskmaterial())) {
					List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
					if (attachs.size() != 0) {
						inattach = false;
					}
					for (FrameAttachInfo attach : attachs) {
						String result = uploadAttach( attach.getAttachGuid(),tokenid);
						if ("error".equals(result)) {
							uploadresult = true;
							break;
						}else {
							JSONObject attachnew = JSONObject.parseObject(result);
							Record rec = new Record();
							rec.set("attachmentName", attachnew.getString("name"));
							rec.set("fileNo", "601");
							rec.set("resourceId", attachnew.getString("resourceId"));
							files.add(rec);
						}
					}
				}
				else if("个人户的提供业户身份证".equals(material.getTaskmaterial())) {
					List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
					if (attachs.size() != 0) {
						inattach = false;
					}
					for (FrameAttachInfo attach : attachs) {
						String result = uploadAttach( attach.getAttachGuid(),tokenid);
						if ("error".equals(result)) {
							uploadresult = true;
							break;
						}else {
							JSONObject attachnew = JSONObject.parseObject(result);
							Record rec = new Record();
							rec.set("attachmentName", attachnew.getString("name"));
							rec.set("fileNo", "604");
							rec.set("resourceId", attachnew.getString("resourceId"));
							files.add(rec);
						}
					}
				}
				Thread.sleep(1000);
			}

			if (inattach) {
				return JsonUtils.zwdtRestReturn("0", "办件材料不存在！", "");
			}
			if (uploadresult) {
				return JsonUtils.zwdtRestReturn("0", "办件附件上传失败", "");
			}

			String asdFileInfoList = files.toString();
			
			String data = archiveNo + asdFileInfoList + bizLicCode + bizLicIssueDate + bizScopes + child + createMan + districtCode + economicType + faxNo + foreignCapital + 
					investor + investorNationality + legalPerson + legalTel + licenceCode + licenceWord + licenDecNo + licValidDateBegin + licValidDateEnd + 
					localTaxCertDate + localTaxNo + mgmtAreaCode + orgCode + ownerAddress + ownerName + ownerTel + parentOwnerName + projectguid + provinceOwner + registeredCapital + remark + stateTaxCertDate + stateTaxNo;
			
			log.info("addDlkyjyxkApplyhmac加密前："+data);
			String hmac = hamcsha1(data.getBytes(),key.getBytes());
			
			log.info("addDlkyjyxkApplyhmac:"+hmac);
			
			json.put("userCode", userCode);
			json.put("hmac", hmac);
			json.put("archiveNo", archiveNo);
			json.put("mgmtAreaCode", mgmtAreaCode);
			json.put("ownerName", ownerName);
			json.put("remark", remark);
			json.put("licenDecNo", licenDecNo);
			json.put("legalPerson", legalPerson);
			json.put("legalTel", legalTel);
			json.put("ownerAddress", ownerAddress);
			json.put("districtCode", districtCode);
			json.put("orgCode", orgCode);
			json.put("ownerTel", ownerTel);
			json.put("faxNo", faxNo);
			json.put("economicType", economicType);
			json.put("bizLicCode", bizLicCode);
			json.put("bizLicIssueDate", bizLicIssueDate);
			json.put("investor", investor);
			json.put("investorNationality", investorNationality);
			json.put("stateTaxNo", stateTaxNo);
			json.put("stateTaxCertDate", stateTaxCertDate);
			json.put("localTaxNo", localTaxNo);
			json.put("localTaxCertDate", localTaxCertDate);
			json.put("licenceWord", licenceWord);
			json.put("licenceCode", licenceCode);
			json.put("licValidDateBegin", licValidDateBegin);
			json.put("licValidDateEnd", licValidDateEnd);
			json.put("registeredCapital", registeredCapital);
			json.put("foreignCapital", foreignCapital);
			json.put("bizScopes", bizScopes);
			json.put("provinceOwner", provinceOwner);
			json.put("createMan", createMan);
			json.put("child", child);
			json.put("parentOwnerName", parentOwnerName);
			json.put("projectguid", projectguid);
			json.put("asdFileInfoList", JSON.toJSONString(files));
			
			
			
			log.info("addDlkyjyxkApply接口申请信息如下："+json);
			String result = HttpUtil.doPostJson("https://services.sdyzgl.com/test/asdOwnerFacade/passengerBusiness/1.0", json+"");
			log.info("addDlkyjyxkApply返回结果如下："+result);

			JSONObject dataresult = JSONObject.parseObject(result);
			
			if ("true".equals(dataresult.getString("success"))) {
				return JsonUtils.zwdtRestReturn("1", "添加申请信息成功", "");
			} else {
				return JsonUtils.zwdtRestReturn("0", "添加申请信息失败", "");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======addDlkyjyxkApply接口参数：params【" + params + "】=======");
			log.info("=======addDlkyjyxkApply异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "添加申请信息失败：" + e.getMessage(), "");
		}
	}
	
	  /**
		 * 添加 网络预约出租汽车道路运输证核发 信息
		 * 
		 * @param params
		 *            接口的入参
		 * @return
		 */
		@RequestMapping(value = "/addWlyyczqcdlyshfApply", method = RequestMethod.POST)
		public String addWlyyczqcdlyshfApply(@RequestBody String params) {
			try {
				log.info("=======开始调用addWlyyczqcdlyshfApply接口=======");
				// 1、接口的入参转化为JSON对象
				JSONObject jsonObject = JSONObject.parseObject(params);
				String projectguid = jsonObject.getString("projectguid");
				JSONObject json = new JSONObject();
				
				log.info("addWlyyczqcdlyshfApply入参：" + jsonObject);
				// 8、定义返回JSON对象

				AuditProject auditProject = sendMaterials.getAuditProjectByRowguid(projectguid);

				if (auditProject == null) {
					return JsonUtils.zwdtRestReturn("0", "该办件未查询到", "");
				}
				
				Record record = iCxBusService.getWlyyczqcdlyszhfByRowguid(projectguid);
				

				if (record == null) {
					return JsonUtils.zwdtRestReturn("0", "未找到该表单数据", "");
				}
				
				List<Record> record2 = iCxBusService.getWlyyczqcdlyszhfzbByRowguid(projectguid);
				

				if (record2 == null || record2.size() == 0) {
					return JsonUtils.zwdtRestReturn("0", "未找到该表单2数据", "");
				}
				
				
				log.info("网络预约出租汽车道路运输证核发record:" + record);
				log.info("网络预约出租汽车道路运输证核发record2:" + record2);
				
				String driverName = "";
				String driverCN = "";
				String driverPhone = "";
				String driverName2 = "";
				String driverCN2 = "";
				String driverPhone2 = "";
				String driverName3 = "";
				String driverCN3 = "";
				String driverPhone3 = "";
				
				if(record2.size() == 1) {
					driverName = record2.get(0).getStr("jsyxm");
					driverCN = record2.get(0).getStr("jsysfzh");
					driverPhone = record2.get(0).getStr("jsysjh");
				}else if(record2.size() == 2) {
					driverName = record2.get(0).getStr("jsyxm");
					driverCN = record2.get(0).getStr("jsysfzh");
					driverPhone = record2.get(0).getStr("jsysjh");
					driverName2 = record2.get(1).getStr("jsyxm");
					driverCN2 = record2.get(1).getStr("jsysfzh");
					driverPhone2 = record2.get(1).getStr("jsysjh");
				}else if(record2.size() == 3) {
					driverName = record2.get(0).getStr("jsyxm");
					driverCN = record2.get(0).getStr("jsysfzh");
					driverPhone = record2.get(0).getStr("jsysjh");
					driverName2 = record2.get(1).getStr("jsyxm");
					driverCN2 = record2.get(1).getStr("jsysfzh");
					driverPhone2 = record2.get(1).getStr("jsysjh");
					driverName3 = record2.get(2).getStr("jsyxm");
					driverCN3 = record2.get(2).getStr("jsysfzh");
					driverPhone3 = record2.get(2).getStr("jsysjh");
				}

				// 车牌颜色
				String vehPlateColor = record.getStr("cpys");
				// 经营人身份证号
				String vehOperatorIDCardNo = record.getStr("jyrsfzh");
				// 管辖区域
				String mgmtAreaCode = record.getStr("gxqy");
				// 车经营人联系电话
				String vehOperatorTel = record.getStr("cjyrlxdh");
				// 购车日期
				String buyDate = EpointDateUtil.convertDate2String(record.getDate("gcrq"), EpointDateUtil.DATE_FORMAT);
				// 车牌号
				String vehNo = record.getStr("cph");
				// 网络预约运营平台
				String taxiNetPlat = record.getStr("wlyyyypt");
				// 车高毫米
				String vehHeight = record.getStr("cghm");
				// 车长毫米
				String vehLength = record.getStr("czhm");
				// 税后价格元
				String priceIncVAT = record.getStr("shjgy");
				// 车宽毫米
				String vehWidth = record.getStr("ckhm");
				// 燃料类型
				String fuelType = record.getStr("rllx");
				// 车经营人
				String vehOperator = record.getStr("cjyr");
				// 车经营人地址
				String vehOperatorAddress = record.getStr("cjyrdz");
				// 核定载客位数
				String seatCount = record.getStr("hdzkws");
				// 是否个人车
				String isPersonal = record.getStr("sfgrc");
				// 车辆厂牌
				String vehBrand = record.getStr("clcp");
				// 轴距毫米
				String wheelbase = record.getStr("zjhm");
				// 发动机号
				String engineNo = record.getStr("fdjh");
				// 创建人
				String createMan = record.getStr("cjr");
				// 车辆型号
				String vehModel = record.getStr("clxh");
				// 轴数
				String axleCount = record.getStr("zhous");
				// 发动机功率千瓦
				String enginePower = record.getStr("fdjglqw");
				// 续航能力电动车公里
				String endurance = record.getStr("xhnlddcgl");
				// 车经营人统一社会信用代码
				String bizLicCode = record.getStr("cjyrtyshxydm");
				// vin码
				String vinCode = record.getStr("vinm");
				// 车经营人经济类型
				String economicType = record.getStr("cjyrjjlx");
				// 车辆类型
				String vehType = record.getStr("cllx");
				// 车籍地
				String districtCode = record.getStr("cjd");
				// 备注说明
				String remark = record.getStr("bzsm");
				// 车经营人法人
				String legalMan = record.getStr("cjyrfr");
				// 注册日期
				String productionDate = EpointDateUtil.convertDate2String(record.getDate("zcrq"), EpointDateUtil.DATE_FORMAT);
				// 车经营人联系人
				String linkMan = record.getStr("cjyrlxr");
				
				
				
				SqlConditionUtil materialSqlConditionUtil = new SqlConditionUtil();
				materialSqlConditionUtil.eq("projectguid", projectguid);
				List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
						.selectProjectMaterialByCondition(materialSqlConditionUtil.getMap()).getResult();

				log.info("获取到的材料信息为：" + auditProjectMaterials);
				JSONArray files = new JSONArray();
				boolean inattach = true;
				boolean uploadresult = false;
				
				String now = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHH");
				String tokenid = "";
				if (StringUtil.isBlank(tokenmsg.get(now))) {
					String tokenurl = "https://services.sdyzgl.com/test/asdVehFacade/tokenInfoForApp/1.0";
					JSONObject json2 = new JSONObject();
					json2.put("hmac", "00E83326C00CAF3F7C05836CC13DCCBC7C955768");
					json2.put("userCode", userCode);
					String tokenidresult = HttpUtil.doPostJson(tokenurl, json2.toString());
					JSONObject tokenresult = JSONObject.parseObject(tokenidresult);
					String id = tokenresult.getString("id");
					tokenid = id;
					tokenmsg.put(now, id);
					log.info("tokenmsg0:"+tokenmsg);
				}
				else {
					tokenid = tokenmsg.get(now);
				}
			
				
				for (AuditProjectMaterial material : auditProjectMaterials) {
					if ("网络预约出租汽车运输证申请表".equals(material.getTaskmaterial())) {
						List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
						if (attachs.size() != 0) {
							inattach = false;
						}
						for (FrameAttachInfo attach : attachs) {
							String result = uploadAttach( attach.getAttachGuid(),tokenid);
							if ("error".equals(result)) {
								uploadresult = true;
								break;
							}else {
								JSONObject attachnew = JSONObject.parseObject(result);
								Record rec = new Record();
								rec.set("attachmentName", attachnew.getString("name"));
								rec.set("fileNo", "401");
								rec.set("resourceId", attachnew.getString("resourceId"));
								files.add(rec);
							}
						}
					}
					else if("网络预约出租汽车驾驶员证".equals(material.getTaskmaterial())) {
						List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
						if (attachs.size() != 0) {
							inattach = false;
						}
						for (FrameAttachInfo attach : attachs) {
							String result = uploadAttach( attach.getAttachGuid(),tokenid);
							if ("error".equals(result)) {
								uploadresult = true;
								break;
							}else {
								JSONObject attachnew = JSONObject.parseObject(result);
								Record rec = new Record();
								rec.set("attachmentName", attachnew.getString("name"));
								rec.set("fileNo", "402");
								rec.set("resourceId", attachnew.getString("resourceId"));
								files.add(rec);
							}
						}
					}
					else if("车辆购置发票（属于二手车的需要提供车辆原始发票）、车辆合格证明，行驶证、登记证书、保险证明、车辆综合性能检验报告单的原件及复印件".equals(material.getTaskmaterial())) {
						List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
						if (attachs.size() != 0) {
							inattach = false;
						}
						for (FrameAttachInfo attach : attachs) {
							String result = uploadAttach( attach.getAttachGuid(),tokenid);
							if ("error".equals(result)) {
								uploadresult = true;
								break;
							}else {
								JSONObject attachnew = JSONObject.parseObject(result);
								Record rec = new Record();
								rec.set("attachmentName", attachnew.getString("name"));
								rec.set("fileNo", "403");
								rec.set("resourceId", attachnew.getString("resourceId"));
								files.add(rec);
							}
						}
					}
					else if("车辆安装卫星定位装置、应急报警装置相关证明材料".equals(material.getTaskmaterial())) {
						List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
						if (attachs.size() != 0) {
							inattach = false;
						}
						for (FrameAttachInfo attach : attachs) {
							String result = uploadAttach( attach.getAttachGuid(),tokenid);
							if ("error".equals(result)) {
								uploadresult = true;
								break;
							}else {
								JSONObject attachnew = JSONObject.parseObject(result);
								Record rec = new Record();
								rec.set("attachmentName", attachnew.getString("name"));
								rec.set("fileNo", "405");
								rec.set("resourceId", attachnew.getString("resourceId"));
								files.add(rec);
							}
						}
					}
					else if("车辆照片（原件1张，规格：45度角拍摄9.0cm×6.2cm彩色照片）。".equals(material.getTaskmaterial())) {
						List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
						if (attachs.size() != 0) {
							inattach = false;
						}
						for (FrameAttachInfo attach : attachs) {
							String result = uploadAttach( attach.getAttachGuid(),tokenid);
							if ("error".equals(result)) {
								uploadresult = true;
								break;
							}else {
								JSONObject attachnew = JSONObject.parseObject(result);
								Record rec = new Record();
								rec.set("attachmentName", attachnew.getString("name"));
								rec.set("fileNo", "407");
								rec.set("resourceId", attachnew.getString("resourceId"));
								files.add(rec);
							}
						}
					}
					else if("车辆所有人与网约车平台公司签订的入网营运协议（平台自有车辆除外）".equals(material.getTaskmaterial())) {
						List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
						if (attachs.size() != 0) {
							inattach = false;
						}
						for (FrameAttachInfo attach : attachs) {
							String result = uploadAttach( attach.getAttachGuid(),tokenid);
							if ("error".equals(result)) {
								uploadresult = true;
								break;
							}else {
								JSONObject attachnew = JSONObject.parseObject(result);
								Record rec = new Record();
								rec.set("attachmentName", attachnew.getString("name"));
								rec.set("fileNo", "406");
								rec.set("resourceId", attachnew.getString("resourceId"));
								files.add(rec);
							}
						}
					}
				}

				if (inattach) {
					return JsonUtils.zwdtRestReturn("0", "办件材料不存在！", "");
				}
				if (uploadresult) {
					return JsonUtils.zwdtRestReturn("0", "办件附件上传失败", "");
				}

				String asdFileInfoList = files.toString();
				
				String data = asdFileInfoList + axleCount + bizLicCode + buyDate + createMan + districtCode + driverCN + driverCN2 + driverCN3 + driverName + driverName2 + driverName3 + driverPhone + driverPhone2 + driverPhone3 + 
						economicType + endurance + engineNo + enginePower + fuelType + isPersonal + legalMan + linkMan + mgmtAreaCode + priceIncVAT + productionDate + projectguid + remark + seatCount + taxiNetPlat + vehBrand +
						vehHeight + vehLength + vehModel + vehNo + vehOperator + vehOperatorAddress + vehOperatorIDCardNo + vehOperatorTel + vehPlateColor + vehType + vehWidth + vinCode + wheelbase;
				
				log.info("addWlyyczqcdlyshfApplyhmac加密前："+data);
				String hmac = hamcsha1(data.getBytes(),key.getBytes());
				
				log.info("addWlyyczqcdlyshfApplyhmac:"+hmac);
				
				json.put("userCode", userCode);
				json.put("hmac", hmac);
				json.put("vehNo", vehNo);
				json.put("vehPlateColor", vehPlateColor);
				json.put("mgmtAreaCode", mgmtAreaCode);
				json.put("vehLength", vehLength);
				json.put("vehWidth", vehWidth);
				json.put("vehHeight", vehHeight);
				json.put("remark", remark);
				json.put("productionDate", productionDate);
				json.put("vinCode", vinCode);
				json.put("vehType", vehType);
				json.put("engineNo", engineNo);
				json.put("vehModel", vehModel);
				json.put("vehBrand", vehBrand);
				json.put("wheelbase", wheelbase);
				json.put("axleCount", axleCount);
				json.put("enginePower", enginePower);
				json.put("fuelType", fuelType);
				json.put("seatCount", seatCount);
				json.put("driverName", driverName);
				json.put("driverCN", driverCN);
				json.put("driverPhone", driverPhone);
				json.put("driverName2", driverName2);
				json.put("driverCN2", driverCN2);
				json.put("driverPhone2", driverPhone2);
				json.put("driverName3", driverName3);
				json.put("driverCN3", driverCN3);
				json.put("driverPhone3", driverPhone3);
				json.put("isPersonal", isPersonal);
				json.put("vehOperator", vehOperator);
				json.put("vehOperatorIDCardNo", vehOperatorIDCardNo);
				json.put("vehOperatorTel", vehOperatorTel);
				json.put("vehOperatorAddress", vehOperatorAddress);
				json.put("buyDate", buyDate);
				json.put("priceIncVAT", priceIncVAT);
				json.put("endurance", endurance);
				json.put("taxiNetPlat", taxiNetPlat);
				json.put("bizLicCode", bizLicCode);
				json.put("legalMan", legalMan);
				json.put("linkMan", linkMan);
				json.put("economicType", economicType);
				json.put("districtCode", districtCode);
				json.put("createMan", createMan);
				json.put("projectguid", projectguid);
				json.put("asdFileInfoList", JSON.toJSONString(files));
				
				
				
				log.info("addWlyyczqcdlyshfApply接口申请信息如下："+json);
				String result = HttpUtil.doPostJson("https://services.sdyzgl.com/test/asdVehFacade/netTaxiIssue/1.0", json+"");
				log.info("addWlyyczqcdlyshfApply返回结果如下："+result);

				JSONObject dataresult = JSONObject.parseObject(result);
				
				if ("true".equals(dataresult.getString("success"))) {
					return JsonUtils.zwdtRestReturn("1", "添加申请信息成功", "");
				} else {
					return JsonUtils.zwdtRestReturn("0", "添加申请信息失败", "");
				}

			} catch (Exception e) {
				e.printStackTrace();
				log.info("=======addWlyyczqcdlyshfApply接口参数：params【" + params + "】=======");
				log.info("=======addWlyyczqcdlyshfApply异常信息：" + e.getMessage() + "=======");
				return JsonUtils.zwdtRestReturn("0", "添加申请信息失败：" + e.getMessage(), "");
			}
		}
		
		
		/**
		 * 添加 道路危险货物运输驾驶员从业资格许可 信息
		 * 
		 * @param params
		 *            接口的入参
		 * @return
		 */
		@RequestMapping(value = "/addWhjsycyzgxkApply", method = RequestMethod.POST)
		public String addWhjsycyzgxkApply(@RequestBody String params) {
			try {
				log.info("=======开始调用addWhjsycyzgxkApply接口=======");
				// 1、接口的入参转化为JSON对象
				JSONObject jsonObject = JSONObject.parseObject(params);
				String projectguid = jsonObject.getString("projectguid");
				JSONObject json = new JSONObject();
				
				log.info("addWhjsycyzgxkApply入参：" + jsonObject);
				// 8、定义返回JSON对象
				
				AuditProject auditProject = sendMaterials.getAuditProjectByRowguid(projectguid);
				
				if (auditProject == null) {
					return JsonUtils.zwdtRestReturn("0", "该办件未查询到", "");
				}
				
				Record record = iCxBusService.getWhjsycyzgxkByRowguid(projectguid);
				
				
				if (record == null) {
					return JsonUtils.zwdtRestReturn("0", "未找到该表单数据", "");
				}
				
				
				log.info("网络预约出租汽车道路运输证核发record:" + record);
				
				
				// 从业人员姓名
				String staffName = record.getStr("cyryxm");
				// 联系电话
				String telephone = record.getStr("lxdh");
				// 证件号码
				String staffCredentialNo = record.getStr("zjhm");
				// 住址
				String address = record.getStr("zz");
				// 备注说明
				String remark = record.getStr("bzsm");
				// 行政区划
				String districtCode = record.getStr("xzqh");
				// 出生日期
				String birthday = EpointDateUtil.convertDate2String(record.getDate("csrq"), EpointDateUtil.DATE_FORMAT);
				// 性别
				String sex = record.getStr("xb");
				// 国籍
				String nationality = record.getStr("gj");
				// 文化程度
				String eduLevel = record.getStr("whcd");
				// 民族
				String ethnicity = record.getStr("mz");
				// 准驾车型
				String drivingClass = record.getStr("zjcx");
				// 驾驶证初领日期
				String drivingLicFirstIssueDate = EpointDateUtil.convertDate2String(record.getDate("jszclrq"), EpointDateUtil.DATE_FORMAT);
				// 驾照有效期限
				String drivingLicValidPeriod = record.getStr("jszyxrq");
				// 资格类别
				String qualificationType = record.getStr("zglb");
				// 创建人
				String createMan = record.getStr("cjr");
				
				
				SqlConditionUtil materialSqlConditionUtil = new SqlConditionUtil();
				materialSqlConditionUtil.eq("projectguid", projectguid);
				List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
						.selectProjectMaterialByCondition(materialSqlConditionUtil.getMap()).getResult();
				
				log.info("获取到的材料信息为：" + auditProjectMaterials);
				JSONArray files = new JSONArray();
				boolean inattach = true;
				boolean uploadresult = false;
				
				String now = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHH");
				String tokenid = "";
				if (StringUtil.isBlank(tokenmsg.get(now))) {
					String tokenurl = "https://services.sdyzgl.com/test/asdVehFacade/tokenInfoForApp/1.0";
					JSONObject json2 = new JSONObject();
					json2.put("hmac", "00E83326C00CAF3F7C05836CC13DCCBC7C955768");
					json2.put("userCode", userCode);
					String tokenidresult = HttpUtil.doPostJson(tokenurl, json2.toString());
					JSONObject tokenresult = JSONObject.parseObject(tokenidresult);
					String id = tokenresult.getString("id");
					tokenid = id;
					tokenmsg.put(now, id);
					log.info("tokenmsg0:"+tokenmsg);
				}
				else {
					tokenid = tokenmsg.get(now);
				}
				
				
				for (AuditProjectMaterial material : auditProjectMaterials) {
					if ("原从业资格证（初次申请的不需要提供）、申请人驾驶证、身份证".equals(material.getTaskmaterial())) {
						List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
						if (attachs.size() != 0) {
							inattach = false;
						}
						for (FrameAttachInfo attach : attachs) {
							String result = uploadAttach( attach.getAttachGuid(),tokenid);
							if ("error".equals(result)) {
								uploadresult = true;
								break;
							}else {
								JSONObject attachnew = JSONObject.parseObject(result);
								Record rec = new Record();
								rec.set("attachmentName", attachnew.getString("name"));
								rec.set("fileNo", "901");
								rec.set("resourceId", attachnew.getString("resourceId"));
								files.add(rec);
							}
						}
					}
					else if("道路危险货物运输从业人员从业资格考试申请表".equals(material.getTaskmaterial())) {
						List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
						if (attachs.size() != 0) {
							inattach = false;
						}
						for (FrameAttachInfo attach : attachs) {
							String result = uploadAttach( attach.getAttachGuid(),tokenid);
							if ("error".equals(result)) {
								uploadresult = true;
								break;
							}else {
								JSONObject attachnew = JSONObject.parseObject(result);
								Record rec = new Record();
								rec.set("attachmentName", attachnew.getString("name"));
								rec.set("fileNo", "902");
								rec.set("resourceId", attachnew.getString("resourceId"));
								files.add(rec);
							}
						}
					}
					else if("满2年以上的经营性道路旅客或货物运输驾驶员从业资格证，或者全日制驾驶职业教育毕业证书；".equals(material.getTaskmaterial())) {
						List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
						if (attachs.size() != 0) {
							inattach = false;
						}
						for (FrameAttachInfo attach : attachs) {
							String result = uploadAttach( attach.getAttachGuid(),tokenid);
							if ("error".equals(result)) {
								uploadresult = true;
								break;
							}else {
								JSONObject attachnew = JSONObject.parseObject(result);
								Record rec = new Record();
								rec.set("attachmentName", attachnew.getString("name"));
								rec.set("fileNo", "903");
								rec.set("resourceId", attachnew.getString("resourceId"));
								files.add(rec);
							}
						}
					}
					else if("三年内无重大以上交通责任事故且无交通违法记满12分记录的证明".equals(material.getTaskmaterial())) {
						List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
						if (attachs.size() != 0) {
							inattach = false;
						}
						for (FrameAttachInfo attach : attachs) {
							String result = uploadAttach( attach.getAttachGuid(),tokenid);
							if ("error".equals(result)) {
								uploadresult = true;
								break;
							}else {
								JSONObject attachnew = JSONObject.parseObject(result);
								Record rec = new Record();
								rec.set("attachmentName", attachnew.getString("name"));
								rec.set("fileNo", "904");
								rec.set("resourceId", attachnew.getString("resourceId"));
								files.add(rec);
							}
						}
					}
					else if("两寸近期免冠彩色照片（png、jpg格式）".equals(material.getTaskmaterial())) {
						List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
						if (attachs.size() != 0) {
							inattach = false;
						}
						for (FrameAttachInfo attach : attachs) {
							String result = uploadAttach( attach.getAttachGuid(),tokenid);
							if ("error".equals(result)) {
								uploadresult = true;
								break;
							}else {
								JSONObject attachnew = JSONObject.parseObject(result);
								Record rec = new Record();
								rec.set("attachmentName", attachnew.getString("name"));
								rec.set("fileNo", "905");
								rec.set("resourceId", attachnew.getString("resourceId"));
								files.add(rec);
							}
						}
					}
				}
				
				if (inattach) {
					return JsonUtils.zwdtRestReturn("0", "办件材料不存在！", "");
				}
				if (uploadresult) {
					return JsonUtils.zwdtRestReturn("0", "办件附件上传失败", "");
				}
				
				String asdFileInfoList = files.toString();
				
				String data = address + asdFileInfoList + birthday + createMan + districtCode + drivingClass + drivingLicFirstIssueDate + drivingLicValidPeriod + eduLevel + ethnicity + nationality +  
						projectguid + qualificationType + remark + sex + staffCredentialNo + staffName + telephone;
				
				log.info("addWhjsycyzgxkApplyhmac加密前："+data);
				String hmac = hamcsha1(data.getBytes(),key.getBytes());
				
				log.info("addWhjsycyzgxkApplyhmac:"+hmac);
				
				json.put("userCode", userCode);
				json.put("hmac", hmac);
				json.put("staffName", staffName);
				json.put("telephone", telephone);
				json.put("staffCredentialNo", staffCredentialNo);
				json.put("address", address);
				json.put("remark", remark);
				json.put("districtCode", districtCode);
				json.put("birthday", birthday);
				json.put("sex", sex);
				json.put("nationality", nationality);
				json.put("eduLevel", eduLevel);
				json.put("ethnicity", ethnicity);
				json.put("drivingClass", drivingClass);
				json.put("drivingLicFirstIssueDate", drivingLicFirstIssueDate);
				json.put("drivingLicValidPeriod", drivingLicValidPeriod);
				json.put("qualificationType", qualificationType);
				json.put("createMan", createMan);
				json.put("projectguid", projectguid);
				json.put("asdFileInfoList", JSON.toJSONString(files));
				
				
				
				log.info("addWhjsycyzgxkApply接口申请信息如下："+json);
				String result = HttpUtil.doPostJson("https://services.sdyzgl.com/test/asdStaffFacade/dangerDriverQua/1.0", json+"");
				log.info("addWhjsycyzgxkApply返回结果如下："+result);
				
				JSONObject dataresult = JSONObject.parseObject(result);
				
				if ("true".equals(dataresult.getString("success"))) {
					return JsonUtils.zwdtRestReturn("1", "添加申请信息成功", "");
				} else {
					return JsonUtils.zwdtRestReturn("0", "添加申请信息失败", "");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				log.info("=======addWhjsycyzgxkApply接口参数：params【" + params + "】=======");
				log.info("=======addWhjsycyzgxkApply异常信息：" + e.getMessage() + "=======");
				return JsonUtils.zwdtRestReturn("0", "添加申请信息失败：" + e.getMessage(), "");
			}
		}
		
    
		/**
		 * 添加 道路危险货物运输押运人员从业资格许可 信息
		 * 
		 * @param params
		 *            接口的入参
		 * @return
		 */
		@RequestMapping(value = "/addWhyyycyzgxkApply", method = RequestMethod.POST)
		public String addWhyyycyzgxkApply(@RequestBody String params) {
			try {
				log.info("=======开始调用addWhyyycyzgxkApply接口=======");
				// 1、接口的入参转化为JSON对象
				JSONObject jsonObject = JSONObject.parseObject(params);
				String projectguid = jsonObject.getString("projectguid");
				JSONObject json = new JSONObject();
				
				log.info("addWhyyycyzgxkApply入参：" + jsonObject);
				// 8、定义返回JSON对象
				
				AuditProject auditProject = sendMaterials.getAuditProjectByRowguid(projectguid);
				
				if (auditProject == null) {
					return JsonUtils.zwdtRestReturn("0", "该办件未查询到", "");
				}
				
				Record record = iCxBusService.getDlwxhwysyyrycyzgxkByRowguid(projectguid);
				
				if (record == null) {
					return JsonUtils.zwdtRestReturn("0", "未找到该表单数据", "");
				}
				
				
				log.info("危货押运员从业资格许可record:" + record);
				
				
				// 从业人员姓名
				String staffName = record.getStr("cyryxm");
				// 联系电话
				String telephone = record.getStr("lxdh");
				// 证件号码
				String staffCredentialNo = record.getStr("zjhm");
				// 住址
				String address = record.getStr("zz");
				// 备注说明
				String remark = record.getStr("bzsm");
				// 行政区划
				String districtCode = record.getStr("xzqh");
				// 出生日期
				String birthday = EpointDateUtil.convertDate2String(record.getDate("csrq"), EpointDateUtil.DATE_FORMAT);
				// 性别
				String sex = record.getStr("xb");
				// 国籍
				String nationality = record.getStr("gj");
				// 文化程度
				String eduLevel = record.getStr("whcd");
				// 民族
				String ethnicity = record.getStr("mz");
				// 准驾车型
				String drivingClass = record.getStr("zjcx");
				// 驾驶证初领日期
				String drivingLicFirstIssueDate = EpointDateUtil.convertDate2String(record.getDate("jszclrq"), EpointDateUtil.DATE_FORMAT);
				// 驾照有效期限
				String drivingLicValidPeriod = record.getStr("jzyxqx");
				// 资格类别
				String qualificationType = record.getStr("zglb");
				// 创建人
				String createMan = record.getStr("cjr");
				
				
				SqlConditionUtil materialSqlConditionUtil = new SqlConditionUtil();
				materialSqlConditionUtil.eq("projectguid", projectguid);
				List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
						.selectProjectMaterialByCondition(materialSqlConditionUtil.getMap()).getResult();
				
				log.info("获取到的材料信息为：" + auditProjectMaterials);
				JSONArray files = new JSONArray();
				boolean inattach = true;
				boolean uploadresult = false;
				
				String now = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHH");
				String tokenid = "";
				if (StringUtil.isBlank(tokenmsg.get(now))) {
					String tokenurl = "https://services.sdyzgl.com/test/asdVehFacade/tokenInfoForApp/1.0";
					JSONObject json2 = new JSONObject();
					json2.put("hmac", "00E83326C00CAF3F7C05836CC13DCCBC7C955768");
					json2.put("userCode", userCode);
					String tokenidresult = HttpUtil.doPostJson(tokenurl, json2.toString());
					JSONObject tokenresult = JSONObject.parseObject(tokenidresult);
					String id = tokenresult.getString("id");
					tokenid = id;
					tokenmsg.put(now, id);
					log.info("tokenmsg0:"+tokenmsg);
				}
				else {
					tokenid = tokenmsg.get(now);
				}
				
				
				for (AuditProjectMaterial material : auditProjectMaterials) {
					if ("原从业资格证（初次申请的不需要提供）、申请人身份证".equals(material.getTaskmaterial())) {
						List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
						if (attachs.size() != 0) {
							inattach = false;
						}
						for (FrameAttachInfo attach : attachs) {
							String result = uploadAttach( attach.getAttachGuid(),tokenid);
							if ("error".equals(result)) {
								uploadresult = true;
								break;
							}else {
								JSONObject attachnew = JSONObject.parseObject(result);
								Record rec = new Record();
								rec.set("attachmentName", attachnew.getString("name"));
								rec.set("fileNo", "801");
								rec.set("resourceId", attachnew.getString("resourceId"));
								files.add(rec);
							}
						}
					}
					else if("初中以上的学历证明".equals(material.getTaskmaterial())) {
						List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
						if (attachs.size() != 0) {
							inattach = false;
						}
						for (FrameAttachInfo attach : attachs) {
							String result = uploadAttach( attach.getAttachGuid(),tokenid);
							if ("error".equals(result)) {
								uploadresult = true;
								break;
							}else {
								JSONObject attachnew = JSONObject.parseObject(result);
								Record rec = new Record();
								rec.set("attachmentName", attachnew.getString("name"));
								rec.set("fileNo", "802");
								rec.set("resourceId", attachnew.getString("resourceId"));
								files.add(rec);
							}
						}
					}
					else if("道路危险货物运输从业人员从业资格考试申请表".equals(material.getTaskmaterial())) {
						List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
						if (attachs.size() != 0) {
							inattach = false;
						}
						for (FrameAttachInfo attach : attachs) {
							String result = uploadAttach( attach.getAttachGuid(),tokenid);
							if ("error".equals(result)) {
								uploadresult = true;
								break;
							}else {
								JSONObject attachnew = JSONObject.parseObject(result);
								Record rec = new Record();
								rec.set("attachmentName", attachnew.getString("name"));
								rec.set("fileNo", "803");
								rec.set("resourceId", attachnew.getString("resourceId"));
								files.add(rec);
							}
						}
					}
					else if("两寸近期免冠彩色照片（png、jpg格式）".equals(material.getTaskmaterial())) {
						List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
						if (attachs.size() != 0) {
							inattach = false;
						}
						for (FrameAttachInfo attach : attachs) {
							String result = uploadAttach( attach.getAttachGuid(),tokenid);
							if ("error".equals(result)) {
								uploadresult = true;
								break;
							}else {
								JSONObject attachnew = JSONObject.parseObject(result);
								Record rec = new Record();
								rec.set("attachmentName", attachnew.getString("name"));
								rec.set("fileNo", "804");
								rec.set("resourceId", attachnew.getString("resourceId"));
								files.add(rec);
							}
						}
					}
				}
				
				if (inattach) {
					return JsonUtils.zwdtRestReturn("0", "办件材料不存在！", "");
				}
				if (uploadresult) {
					return JsonUtils.zwdtRestReturn("0", "办件附件上传失败", "");
				}
				
				String asdFileInfoList = files.toString();
				
				String data = address + asdFileInfoList + birthday + createMan + districtCode + drivingClass + drivingLicFirstIssueDate + drivingLicValidPeriod + eduLevel + ethnicity + nationality +  
						projectguid + qualificationType + remark + sex + staffCredentialNo + staffName + telephone;
				
				log.info("addWhyyycyzgxkApplyhmac加密前："+data);
				String hmac = hamcsha1(data.getBytes(),key.getBytes());
				
				log.info("addWhyyycyzgxkApplyhmac:"+hmac);
				
				json.put("userCode", userCode);
				json.put("hmac", hmac);
				json.put("staffName", staffName);
				json.put("telephone", telephone);
				json.put("staffCredentialNo", staffCredentialNo);
				json.put("address", address);
				json.put("remark", remark);
				json.put("districtCode", districtCode);
				json.put("birthday", birthday);
				json.put("sex", sex);
				json.put("nationality", nationality);
				json.put("eduLevel", eduLevel);
				json.put("ethnicity", ethnicity);
				json.put("drivingClass", drivingClass);
				json.put("drivingLicFirstIssueDate", drivingLicFirstIssueDate);
				json.put("drivingLicValidPeriod", drivingLicValidPeriod);
				json.put("qualificationType", qualificationType);
				json.put("createMan", createMan);
				json.put("projectguid", projectguid);
				json.put("asdFileInfoList", JSON.toJSONString(files));
				
				log.info("addWhyyycyzgxkApply接口申请信息如下："+json);
				String result = HttpUtil.doPostJson("https://services.sdyzgl.com/test/asdStaffFacade/dangerEscortQua/1.0", json+"");
				log.info("addWhyyycyzgxkApply返回结果如下："+result);
				
				JSONObject dataresult = JSONObject.parseObject(result);
				
				if ("true".equals(dataresult.getString("success"))) {
					return JsonUtils.zwdtRestReturn("1", "添加申请信息成功", "");
				} else {
					return JsonUtils.zwdtRestReturn("0", "添加申请信息失败", "");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				log.info("=======addWhyyycyzgxkApply接口参数：params【" + params + "】=======");
				log.info("=======addWhyyycyzgxkApply异常信息：" + e.getMessage() + "=======");
				return JsonUtils.zwdtRestReturn("0", "添加申请信息失败：" + e.getMessage(), "");
			}
		}
		
		
		/**
		 * 添加 巡游出租汽车客运经营许可 信息
		 * 
		 * @param params
		 *            接口的入参
		 * @return
		 */
		@RequestMapping(value = "/addXyczckyjyxkApply", method = RequestMethod.POST)
		public String addXyczckyjyxkApply(@RequestBody String params) {
			try {
				log.info("=======开始调用addDlkyjyxkApply接口=======");
				// 1、接口的入参转化为JSON对象
				JSONObject jsonObject = JSONObject.parseObject(params);
				String projectguid = jsonObject.getString("projectguid");
				JSONObject json = new JSONObject();
				
				log.info("addXyczckyjyxkApply入参：" + jsonObject);
				// 8、定义返回JSON对象
				
				AuditProject auditProject = sendMaterials.getAuditProjectByRowguid(projectguid);
				
				if (auditProject == null) {
					return JsonUtils.zwdtRestReturn("0", "该办件未查询到", "");
				}
				
				Record record = iCxBusService.getPorjectByRowguid("xyczqckyjyxkhf",projectguid);
				
				if (record == null) {
					return JsonUtils.zwdtRestReturn("0", "未找到该表单数据", "");
				}
				
				
				log.info("巡游出租汽车客运经营许可record:" + record);
				
				// 业户档案编号
				String archiveNo = record.getStr("archiveNo");
				// 管辖区域代码
				String mgmtAreaCode = record.getStr("mgmtAreaCode");
				// 申请换发原因
				String ownerName = record.getStr("ownerName");
				// 备注
				String remark = record.getStr("remark");
				// 行政许可决定书文号
				String licenDecNo = record.getStr("licenDecNo");
				// 企业负责人
				String legalPerson = record.getStr("legalPerson");
				// 企业负责人电话
				String legalTel = record.getStr("legalTel");
				// 业户地址
				String ownerAddress = record.getStr("ownerAddress");
				// 户籍地行政区划编码
				String districtCode = record.getStr("districtCode");
				// 组织机构代码
				String orgCode = record.getStr("orgCode");
				// 创建人
				String ownerTel = record.getStr("ownerTel");
				// 创建人
				String faxNo = record.getStr("faxNo");
				// 创建人
				String economicType = record.getStr("economicType");
				// 创建人
				String bizLicCode = record.getStr("bizLicCode");
				// 创建人
				String bizLicIssueDate = record.getStr("bizLicIssueDate");
				// 创建人
				String investor = record.getStr("investor");
				// 创建人
				String investorNationality = record.getStr("investorNationality");
				// 创建人
				String stateTaxNo = record.getStr("stateTaxNo");
				// 创建人
				String stateTaxCertDate = record.getStr("stateTaxCertDate");
				// 地税税务登记号
				String localTaxNo = record.getStr("localTaxNo");
				// 创建人
				String localTaxCertDate = record.getStr("localTaxCertDate");
				// 创建人
				String licenceWord = record.getStr("licenceWord");
				// 创建人
				String licenceCode = record.getStr("licenceCode");
				// 创建人
				String licValidDateBegin = record.getStr("licValidDateBegin");
				// 创建人
				String licValidDateEnd = record.getStr("licValidDateEnd");
				// 创建人
				String registeredCapital = record.getStr("registeredCapital");
				// 创建人
				String foreignCapital = record.getStr("foreignCapital");
				// 创建人
				String bizScopes = record.getStr("bizScopes");
				// 创建人
				String provinceOwner = record.getStr("provinceOwner");
				// 创建人
				String createMan = record.getStr("createMan");
				// 创建人
				String child = record.getStr("child");
				// 创建人
				String parentOwnerName = record.getStr("parentOwnerName");
				
				
				
				SqlConditionUtil materialSqlConditionUtil = new SqlConditionUtil();
				materialSqlConditionUtil.eq("projectguid", projectguid);
				List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
						.selectProjectMaterialByCondition(materialSqlConditionUtil.getMap()).getResult();

				log.info("获取到的材料信息为：" + auditProjectMaterials);
				JSONArray files = new JSONArray();
				boolean inattach = true;
				boolean uploadresult = false;
				
				String now = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHH");
				String tokenid = "";
				if (StringUtil.isBlank(tokenmsg.get(now))) {
					String tokenurl = "https://services.sdyzgl.com/test/asdVehFacade/tokenInfoForApp/1.0";
					JSONObject json2 = new JSONObject();
					json2.put("hmac", "00E83326C00CAF3F7C05836CC13DCCBC7C955768");
					json2.put("userCode", userCode);
					String tokenidresult = HttpUtil.doPostJson(tokenurl, json2.toString());
					JSONObject tokenresult = JSONObject.parseObject(tokenidresult);
					String id = tokenresult.getString("id");
					tokenid = id;
					tokenmsg.put(now, id);
					log.info("tokenmsg0:"+tokenmsg);
				}
				else {
					tokenid = tokenmsg.get(now);
				}
			
				
				for (AuditProjectMaterial material : auditProjectMaterials) {
					if ("《巡游出租汽车经营申请表》".equals(material.getTaskmaterial())) {
						List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
						if (attachs.size() != 0) {
							inattach = false;
						}
						for (FrameAttachInfo attach : attachs) {
							String result = uploadAttach( attach.getAttachGuid(),tokenid);
							if ("error".equals(result)) {
								uploadresult = true;
								break;
							}else {
								JSONObject attachnew = JSONObject.parseObject(result);
								Record rec = new Record();
								rec.set("attachmentName", attachnew.getString("name"));
								rec.set("fileNo", "1902");
								rec.set("resourceId", attachnew.getString("resourceId"));
								files.add(rec);
							}
						}
					}
					else if("投资人、负责人身份、资信证明，经办人的身份证明和委托书".equals(material.getTaskmaterial())) {
						List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
						if (attachs.size() != 0) {
							inattach = false;
						}
						for (FrameAttachInfo attach : attachs) {
							String result = uploadAttach( attach.getAttachGuid(),tokenid);
							if ("error".equals(result)) {
								uploadresult = true;
								break;
							}else {
								JSONObject attachnew = JSONObject.parseObject(result);
								Record rec = new Record();
								rec.set("attachmentName", attachnew.getString("name"));
								rec.set("fileNo", "1903");
								rec.set("resourceId", attachnew.getString("resourceId"));
								files.add(rec);
							}
						}
					}
					else if("投资人、负责人身份、资信证明，经办人的身份证明和委托书".equals(material.getTaskmaterial())) {
						List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
						if (attachs.size() != 0) {
							inattach = false;
						}
						for (FrameAttachInfo attach : attachs) {
							String result = uploadAttach( attach.getAttachGuid(),tokenid);
							if ("error".equals(result)) {
								uploadresult = true;
								break;
							}else {
								JSONObject attachnew = JSONObject.parseObject(result);
								Record rec = new Record();
								rec.set("attachmentName", attachnew.getString("name"));
								rec.set("fileNo", "1904");
								rec.set("resourceId", attachnew.getString("resourceId"));
								files.add(rec);
							}
						}
					}
					else if("拟投入车辆承诺书".equals(material.getTaskmaterial())) {
						List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
						if (attachs.size() != 0) {
							inattach = false;
						}
						for (FrameAttachInfo attach : attachs) {
							String result = uploadAttach( attach.getAttachGuid(),tokenid);
							if ("error".equals(result)) {
								uploadresult = true;
								break;
							}else {
								JSONObject attachnew = JSONObject.parseObject(result);
								Record rec = new Record();
								rec.set("attachmentName", attachnew.getString("name"));
								rec.set("fileNo", "1905");
								rec.set("resourceId", attachnew.getString("resourceId"));
								files.add(rec);
							}
						}
					}
					else if("巡游出租汽车经营管理制度文本".equals(material.getTaskmaterial())) {
						List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
						if (attachs.size() != 0) {
							inattach = false;
						}
						for (FrameAttachInfo attach : attachs) {
							String result = uploadAttach( attach.getAttachGuid(),tokenid);
							if ("error".equals(result)) {
								uploadresult = true;
								break;
							}else {
								JSONObject attachnew = JSONObject.parseObject(result);
								Record rec = new Record();
								rec.set("attachmentName", attachnew.getString("name"));
								rec.set("fileNo", "1907");
								rec.set("resourceId", attachnew.getString("resourceId"));
								files.add(rec);
							}
						}
					}
					else if("经营场所、停车场地有关使用证明等".equals(material.getTaskmaterial())) {
						List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
						if (attachs.size() != 0) {
							inattach = false;
						}
						for (FrameAttachInfo attach : attachs) {
							String result = uploadAttach( attach.getAttachGuid(),tokenid);
							if ("error".equals(result)) {
								uploadresult = true;
								break;
							}else {
								JSONObject attachnew = JSONObject.parseObject(result);
								Record rec = new Record();
								rec.set("attachmentName", attachnew.getString("name"));
								rec.set("fileNo", "1908");
								rec.set("resourceId", attachnew.getString("resourceId"));
								files.add(rec);
							}
						}
					}
					Thread.sleep(1000);
				}

				if (inattach) {
					return JsonUtils.zwdtRestReturn("0", "办件材料不存在！", "");
				}
				if (uploadresult) {
					return JsonUtils.zwdtRestReturn("0", "办件附件上传失败", "");
				}

				String asdFileInfoList = files.toString();
				
				String data = archiveNo + asdFileInfoList + bizLicCode + bizLicIssueDate + bizScopes + child + createMan + districtCode + economicType + faxNo + foreignCapital + 
						investor + investorNationality + legalPerson + legalTel + licenceCode + licenceWord + licenDecNo + licValidDateBegin + licValidDateEnd + 
						localTaxCertDate + localTaxNo + mgmtAreaCode + orgCode + ownerAddress + ownerName + ownerTel + parentOwnerName + projectguid + provinceOwner + registeredCapital + remark + stateTaxCertDate + stateTaxNo;
				
				log.info("addDlkyjyxkApplyhmac加密前："+data);
				String hmac = hamcsha1(data.getBytes(),key.getBytes());
				
				log.info("addDlkyjyxkApplyhmac:"+hmac);
				
				json.put("userCode", userCode);
				json.put("hmac", hmac);
				json.put("archiveNo", archiveNo);
				json.put("mgmtAreaCode", mgmtAreaCode);
				json.put("ownerName", ownerName);
				json.put("remark", remark);
				json.put("licenDecNo", licenDecNo);
				json.put("legalPerson", legalPerson);
				json.put("legalTel", legalTel);
				json.put("ownerAddress", ownerAddress);
				json.put("districtCode", districtCode);
				json.put("orgCode", orgCode);
				json.put("ownerTel", ownerTel);
				json.put("faxNo", faxNo);
				json.put("economicType", economicType);
				json.put("bizLicCode", bizLicCode);
				json.put("bizLicIssueDate", bizLicIssueDate);
				json.put("investor", investor);
				json.put("investorNationality", investorNationality);
				json.put("stateTaxNo", stateTaxNo);
				json.put("stateTaxCertDate", stateTaxCertDate);
				json.put("localTaxNo", localTaxNo);
				json.put("localTaxCertDate", localTaxCertDate);
				json.put("licenceWord", licenceWord);
				json.put("licenceCode", licenceCode);
				json.put("licValidDateBegin", licValidDateBegin);
				json.put("licValidDateEnd", licValidDateEnd);
				json.put("registeredCapital", registeredCapital);
				json.put("foreignCapital", foreignCapital);
				json.put("bizScopes", bizScopes);
				json.put("provinceOwner", provinceOwner);
				json.put("createMan", createMan);
				json.put("child", child);
				json.put("parentOwnerName", parentOwnerName);
				json.put("projectguid", projectguid);
				json.put("asdFileInfoList", JSON.toJSONString(files));
				
				
				
				log.info("addDlkyjyxkApply接口申请信息如下："+json);
				String result = HttpUtil.doPostJson("https://services.sdyzgl.com/test/asdOwnerFacade/passengerBusiness/1.0", json+"");
				log.info("addDlkyjyxkApply返回结果如下："+result);

				JSONObject dataresult = JSONObject.parseObject(result);
				
				if ("true".equals(dataresult.getString("success"))) {
					return JsonUtils.zwdtRestReturn("1", "添加申请信息成功", "");
				} else {
					return JsonUtils.zwdtRestReturn("0", "添加申请信息失败", "");
				}

				
			} catch (Exception e) {
				e.printStackTrace();
				log.info("=======addXyczckyjyxkApply接口参数：params【" + params + "】=======");
				log.info("=======addXyczckyjyxkApply异常信息：" + e.getMessage() + "=======");
				return JsonUtils.zwdtRestReturn("0", "添加申请信息失败：" + e.getMessage(), "");
			}
		}
		
		
		/**
		 * 添加 网络预约出租汽车驾驶员从业资格许可 信息
		 * 
		 * @param params
		 *            接口的入参
		 * @return
		 */
		@RequestMapping(value = "/addWlyyczcjsycyzgxk", method = RequestMethod.POST)
		public String addWlyyczcjsycyzgxk(@RequestBody String params) {
			try {
				log.info("=======开始调用addWlyyczcjsycyzgxk接口=======");
				// 1、接口的入参转化为JSON对象
				JSONObject jsonObject = JSONObject.parseObject(params);
				String projectguid = jsonObject.getString("projectguid");
				JSONObject json = new JSONObject();
				
				log.info("addWlyyczcjsycyzgxk入参：" + jsonObject);
				// 8、定义返回JSON对象
                AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(projectguid).getResult();

                
				if (auditSpISubapp == null) {
					return JsonUtils.zwdtRestReturn("0", "该一件事未查询到", "");
				}
				
				Record record = iCxBusService.getWlyyczcjsycyzgxkByRowguid(projectguid);
				
				if (record == null) {
					return JsonUtils.zwdtRestReturn("0", "未找到该表单数据", "");
				}
				
				
				log.info("网络预约出租汽车驾驶员从业资格许可record:" + record);
				
				
				// 从业人员姓名
				String staffName = record.getStr("cyryxm");
				// 联系电话
				String telephone = record.getStr("lxdh");
				// 证件号码
				String staffCredentialNo = record.getStr("zjhm");
				// 住址
				String address = record.getStr("zz");
				// 备注说明
				String remark = record.getStr("bzsm");
				// 行政区划
				String districtCode = record.getStr("xzqh");
				// 出生日期
				String birthday = EpointDateUtil.convertDate2String(record.getDate("csrq"), EpointDateUtil.DATE_FORMAT);
				// 性别
				String sex = record.getStr("xb");
				// 国籍
				String nationality = record.getStr("gj");
				// 文化程度
				String eduLevel = record.getStr("whcd");
				// 民族
				String ethnicity = record.getStr("mz");
				// 准驾车型
				String drivingClass = record.getStr("zjcx");
				// 驾驶证初领日期
				String drivingLicFirstIssueDate = EpointDateUtil.convertDate2String(record.getDate("jszclrq"), EpointDateUtil.DATE_FORMAT);
				// 驾照有效期限
				String drivingLicValidPeriod = record.getStr("jzyxqx");
				// 资格类别
				String qualificationType = record.getStr("zglb");
				// 创建人
				String createMan = record.getStr("cjr");
				
				
				
				
				JSONArray files = new JSONArray();
				boolean inattach = true;
				boolean uploadresult = false;
				
				String now = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHH");
				String tokenid = "";
				if (StringUtil.isBlank(tokenmsg.get(now))) {
					String tokenurl = "https://services.sdyzgl.com/test/asdVehFacade/tokenInfoForApp/1.0";
					JSONObject json2 = new JSONObject();
					json2.put("hmac", "00E83326C00CAF3F7C05836CC13DCCBC7C955768");
					json2.put("userCode", userCode);
					String tokenidresult = HttpUtil.doPostJson(tokenurl, json2.toString());
					JSONObject tokenresult = JSONObject.parseObject(tokenidresult);
					String id = tokenresult.getString("id");
					tokenid = id;
					tokenmsg.put(now, id);
					log.info("tokenmsg0:"+tokenmsg);
				}
				else {
					tokenid = tokenmsg.get(now);
				}
				
				
				String asdFileInfoList = files.toString();
				
				String data = address + asdFileInfoList + birthday + createMan + districtCode + drivingClass + drivingLicFirstIssueDate + drivingLicValidPeriod + eduLevel + ethnicity + nationality +  
						projectguid + qualificationType + remark + sex + staffCredentialNo + staffName + telephone;
				
				log.info("addWhyyycyzgxkApplyhmac加密前："+data);
				String hmac = hamcsha1(data.getBytes(),key.getBytes());
				
				log.info("addWhyyycyzgxkApplyhmac:"+hmac);
				
				json.put("userCode", userCode);
				json.put("hmac", hmac);
				json.put("staffName", staffName);
				json.put("telephone", telephone);
				json.put("staffCredentialNo", staffCredentialNo);
				json.put("address", address);
				json.put("remark", remark);
				json.put("districtCode", districtCode);
				json.put("birthday", birthday);
				json.put("sex", sex);
				json.put("nationality", nationality);
				json.put("eduLevel", eduLevel);
				json.put("ethnicity", ethnicity);
				json.put("drivingClass", drivingClass);
				json.put("drivingLicFirstIssueDate", drivingLicFirstIssueDate);
				json.put("drivingLicValidPeriod", drivingLicValidPeriod);
				json.put("qualificationType", qualificationType);
				json.put("createMan", createMan);
				json.put("projectguid", projectguid);
				json.put("asdFileInfoList", JSON.toJSONString(files));
				
				log.info("addWlyyczcjsycyzgxk接口申请信息如下："+json);
				String result = HttpUtil.doPostJson("https://services.sdyzgl.com/test/asdStaffFacade/taxiDriverQua/1.0", json+"");
				log.info("addWlyyczcjsycyzgxk返回结果如下："+result);
				
				JSONObject dataresult = JSONObject.parseObject(result);
				
				if ("true".equals(dataresult.getString("success"))) {
					return JsonUtils.zwdtRestReturn("1", "添加申请信息成功", "");
				} else {
					return JsonUtils.zwdtRestReturn("0", "添加申请信息失败", "");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				log.info("=======addWlyyczcjsycyzgxk接口参数：params【" + params + "】=======");
				log.info("=======addWlyyczcjsycyzgxk异常信息：" + e.getMessage() + "=======");
				return JsonUtils.zwdtRestReturn("0", "添加申请信息失败：" + e.getMessage(), "");
			}
		}
		
		/**
		 * 添加 客运从业资格许可 信息
		 * 
		 * @param params
		 *            接口的入参
		 * @return
		 */
		@RequestMapping(value = "/addKycyzgxk", method = RequestMethod.POST)
		public String addKycyzgxk(@RequestBody String params) {
			try {
				log.info("=======开始调用addKycyzgxk接口=======");
				// 1、接口的入参转化为JSON对象
				JSONObject jsonObject = JSONObject.parseObject(params);
				String projectguid = jsonObject.getString("projectguid");
				JSONObject json = new JSONObject();
				
				log.info("addKycyzgxk入参：" + jsonObject);
				// 8、定义返回JSON对象
                AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(projectguid).getResult();

                
				if (auditSpISubapp == null) {
					return JsonUtils.zwdtRestReturn("0", "该一件事未查询到", "");
				}
				
				Record record = iCxBusService.getWlyyczcjsycyzgxkByRowguid(projectguid);
				
				if (record == null) {
					return JsonUtils.zwdtRestReturn("0", "未找到该表单数据", "");
				}
				
				
				log.info("客运从业资格许可record:" + record);
				
				
				// 从业人员姓名
				String staffName = record.getStr("cyryxm");
				// 联系电话
				String telephone = record.getStr("lxdh");
				// 证件号码
				String staffCredentialNo = record.getStr("zjhm");
				// 住址
				String address = record.getStr("zz");
				// 备注说明
				String remark = record.getStr("bzsm");
				// 行政区划
				String districtCode = record.getStr("xzqh");
				// 出生日期
				String birthday = EpointDateUtil.convertDate2String(record.getDate("csrq"), EpointDateUtil.DATE_FORMAT);
				// 性别
				String sex = record.getStr("xb");
				// 国籍
				String nationality = record.getStr("gj");
				// 文化程度
				String eduLevel = record.getStr("whcd");
				// 民族
				String ethnicity = record.getStr("mz");
				// 准驾车型
				String drivingClass = record.getStr("zjcx");
				// 驾驶证初领日期
				String drivingLicFirstIssueDate = EpointDateUtil.convertDate2String(record.getDate("jszclrq"), EpointDateUtil.DATE_FORMAT);
				// 驾照有效期限
				String drivingLicValidPeriod = record.getStr("jzyxqx");
				// 资格类别
				String qualificationType = record.getStr("zglb");
				// 创建人
				String createMan = record.getStr("cjr");
				
				
				
				JSONArray files = new JSONArray();
				boolean inattach = true;
				boolean uploadresult = false;
				
				String now = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHH");
				String tokenid = "";
				if (StringUtil.isBlank(tokenmsg.get(now))) {
					String tokenurl = "https://services.sdyzgl.com/test/asdVehFacade/tokenInfoForApp/1.0";
					JSONObject json2 = new JSONObject();
					json2.put("hmac", "00E83326C00CAF3F7C05836CC13DCCBC7C955768");
					json2.put("userCode", userCode);
					String tokenidresult = HttpUtil.doPostJson(tokenurl, json2.toString());
					JSONObject tokenresult = JSONObject.parseObject(tokenidresult);
					String id = tokenresult.getString("id");
					tokenid = id;
					tokenmsg.put(now, id);
					log.info("tokenmsg0:"+tokenmsg);
				}
				else {
					tokenid = tokenmsg.get(now);
				}
				
				
				String asdFileInfoList = files.toString();
				
				String data = address + asdFileInfoList + birthday + createMan + districtCode + drivingClass + drivingLicFirstIssueDate + drivingLicValidPeriod + eduLevel + ethnicity + nationality +  
						projectguid + qualificationType + remark + sex + staffCredentialNo + staffName + telephone;
				
				log.info("addKycyzgxk加密前："+data);
				String hmac = hamcsha1(data.getBytes(),key.getBytes());
				
				log.info("addKycyzgxk:"+hmac);
				
				json.put("userCode", userCode);
				json.put("hmac", hmac);
				json.put("staffName", staffName);
				json.put("telephone", telephone);
				json.put("staffCredentialNo", staffCredentialNo);
				json.put("address", address);
				json.put("remark", remark);
				json.put("districtCode", districtCode);
				json.put("birthday", birthday);
				json.put("sex", sex);
				json.put("nationality", nationality);
				json.put("eduLevel", eduLevel);
				json.put("ethnicity", ethnicity);
				json.put("drivingClass", drivingClass);
				json.put("drivingLicFirstIssueDate", drivingLicFirstIssueDate);
				json.put("drivingLicValidPeriod", drivingLicValidPeriod);
				json.put("qualificationType", qualificationType);
				json.put("createMan", createMan);
				json.put("projectguid", projectguid);
				json.put("asdFileInfoList", JSON.toJSONString(files));
				
				log.info("addKycyzgxk接口申请信息如下："+json);
				String result = HttpUtil.doPostJson("https://services.sdyzgl.com/test/asdStaffFacade/passengerDriverQua/1.0", json+"");
				log.info("addKycyzgxk返回结果如下："+result);
				
				JSONObject dataresult = JSONObject.parseObject(result);
				
				if ("true".equals(dataresult.getString("success"))) {
					return JsonUtils.zwdtRestReturn("1", "添加申请信息成功", "");
				} else {
					return JsonUtils.zwdtRestReturn("0", "添加申请信息失败", "");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				log.info("=======addKycyzgxk接口参数：params【" + params + "】=======");
				log.info("=======addKycyzgxk异常信息：" + e.getMessage() + "=======");
				return JsonUtils.zwdtRestReturn("0", "添加申请信息失败：" + e.getMessage(), "");
			}
		}
		
		
		
}
