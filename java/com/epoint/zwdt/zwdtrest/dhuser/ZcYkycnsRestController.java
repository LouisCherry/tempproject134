package com.epoint.zwdt.zwdtrest.dhuser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.common.util.AttachUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;

/**
 * 云勘验承诺书签名接口
 * 
 * @作者 xl
 * @version [版本号, 2022年4月14日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RestController
@RequestMapping("/zcYkycnsRestController")
public class ZcYkycnsRestController {
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * 办件API
	 */
	@Autowired
	private IAuditProject iAuditProject;

	/**
	 * 附件API
	 */
	@Autowired
	private IAttachService iAttachService;

	/**
	 * 网上注册用户API
	 */
	@Autowired
	private IAuditOnlineRegister iAuditOnlineRegister;

	/**
	 * 保存签字并进行域填充和附件生成并上传
	 * 
	 * @param params  接口的入参
	 * @param request HTTP请求
	 * @return
	 */
	@RequestMapping(value = "/savesign", method = RequestMethod.POST)
	public String saveSign(@RequestBody String params, @Context HttpServletRequest request) {
		try {
			log.info("=======开始调用saveSign接口=======");
			JSONObject jsonObject = JSON.parseObject(params);
			String token = jsonObject.getString("token");
			if (ZwdtConstant.SysValidateData.equals(token)) {
				// 1.2、获取用户唯一标识
				AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
				if (auditOnlineRegister != null) {
					JSONObject obj = (JSONObject) jsonObject.get("params");
					// 签名base64文件流
					String sign = obj.getString("signpic");
					sign=sign.replaceFirst("data:image/png;base64,", "");
					// 办件唯一标识
					String projectguid = obj.getString("projectguid");
					AuditProject auditProject = CommonDao.getInstance().find(AuditProject.class, projectguid);
					if (auditProject == null) {
						return JsonUtils.zwdtRestReturn("0", "办件不存在！", "");
					}
					String cliengguid;
					if (StringUtil.isNotBlank(auditProject.getStr("promisecliengguid"))) {
						cliengguid = auditProject.getStr("promisecliengguid");
					} else {
						cliengguid = UUID.randomUUID().toString();
						auditProject.set("promisecliengguid", cliengguid);
					}
					// 获取图片字节
					byte[] pic = Base64Util.decodeBuffer(sign);
					// 系统部署路径
					String fileNameOne = "云勘验承诺书.doc";
					String deployPath = ClassPathUtil.getDeployWarPath();
					String filePath = deployPath + "jnzwdt/zcyky/wordtemp";
					File mkdir = new File(filePath);
					if (!mkdir.exists()) {
						mkdir.mkdir();
					}
					String licenseName = ClassPathUtil.getDeployWarPath() + "WEB-INF/license.xml";
					Document doc = new Document(filePath + "/" + fileNameOne);
					String[] fieldNames = null;
					Object[] values = null;
					Map<String, Object> map = new HashMap<>();
					String rq = new SimpleDateFormat("yyyy年MM月dd日").format(new Date());
					map.put("dh", auditOnlineRegister.getMobile());
					map.put("rq", rq);
					fieldNames = new String[map.size()];
					values = new Object[map.size()];
					int num = 0;
					// 获取对应的key、value数组
					for (Iterator<Entry<String, Object>> ite = map.entrySet().iterator(); ite.hasNext(); ++num) {
						Entry<String, Object> entry = ite.next();
						fieldNames[num] = entry.getKey();
						values[num] = entry.getValue();
					}
					// 获取aspose的license
					new License().setLicense(licenseName);
					doc.getMailMerge().execute(fieldNames, values);
					insertImage(pic, doc, "cnr");
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					doc.save(outputStream, SaveFormat.DOC);// 保存
					ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
					long size1 = inputStream.available();
					// 先删除原先的附件
					iAttachService.deleteAttachByGuid(cliengguid);
					AttachUtil.saveFileInputStream(UUID.randomUUID().toString(), cliengguid, fileNameOne, ".doc",
							"云勘验承诺书", size1, inputStream, "", "");

					// 设置办件签名状态已完成
					auditProject.set("is_sign", "1");
					CommonDao.getInstance().update(auditProject);
					log.info("=======结束调用saveSign接口=======");
					return JsonUtils.zwdtRestReturn("1", "签名成功！", "");
				} else {
					return JsonUtils.zwdtRestReturn("0", "用户未登录！", "");
				}
			} else {
				return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======saveSign接口参数：params【" + params + "】=======");
			log.info("=======saveSign异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "查询失败：" + e.getMessage(), "");
		}

	}

	public void insertImage(byte[] imageByteStream, Document doc, String tag) throws IOException, Exception {
		DocumentBuilder build = new DocumentBuilder(doc);
		build.moveToBookmark(tag);
		build.insertImage(imageByteStream, 70, 35);
	}

	/**
	 * 判断该承诺书是否已签字提交
	 * 
	 * @param params  接口的入参
	 * @param request HTTP请求
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/issign", method = RequestMethod.POST)
	public String isSign(@RequestBody String params, @Context HttpServletRequest request) {
		try {
			log.info("=======开始调用isSign接口=======");
			JSONObject jsonObject = JSON.parseObject(params);
			String token = jsonObject.getString("token");
			if (ZwdtConstant.SysValidateData.equals(token)) {
				JSONObject obj = (JSONObject) jsonObject.get("params");
				String projectguid = obj.getString("projectguid");
				JSONObject dataJson = new JSONObject();
				AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(projectguid, "").getResult();
				String url = request.getRequestURL().substring(0, request.getRequestURL().indexOf("/rest/"));
				if (auditProject != null && ZwfwConstant.CONSTANT_STR_ONE.equals(auditProject.getStr("is_sign"))) {
					List<FrameAttachInfo> frameAttachStorages = iAttachService.getAttachInfoListByGuid(auditProject.getStr("promisecliengguid"));
					dataJson.put("filename",frameAttachStorages.get(0).getAttachFileName());
					dataJson.put("attchurl",
							url + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
									+ frameAttachStorages.get(0).getAttachGuid());
					return JsonUtils.zwdtRestReturn("1", "已签名！", dataJson);
				} else {
					dataJson.put("filename","云勘验承诺书.doc");
					dataJson.put("attchurl", url + "/jnzwdt/zcyky/wordtemp/云勘验承诺书.doc");
					return JsonUtils.zwdtRestReturn("0", "未检测到签名！", dataJson);
				}
			} else {
				return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======isSign接口参数：params【" + params + "】=======");
			log.info("=======isSign异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "查询失败：" + e.getMessage(), "");
		}

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

}
