package com.epoint.gassupply.rest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.gassupply.util.V2AuthUtil;
import com.epoint.xmz.wjw.api.ICxBusService;

/**
 * 燃气对接第三方接口
 */
@RestController
@RequestMapping("/gasdjrest")
public class GasSupplyRest
{

    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 燃气身份证用户注册
     */
    @PostMapping("/rqsfzyhzc")
    public String rqsfzyhzc(@RequestBody String param) {
        try {
            log.info("========== 开始调用燃气身份证用户注册接口 ==========");
            log.info("接口入参--->" + param);
            IConfigService configservice = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            String rqEnv = configservice.getFrameConfigValue("rqEnv");
            if (StringUtil.isBlank(rqEnv)) {
                log.info("未获取到燃气接口环境系统参数");
                return "";
            }
            String rqUrlPrefixDev = configservice.getFrameConfigValue("rqUrlPrefixDev");
            String rqUrlPrefixProd = configservice.getFrameConfigValue("rqUrlPrefixProd");
            if (StringUtil.isBlank(rqUrlPrefixDev) || StringUtil.isBlank(rqUrlPrefixProd)) {
                log.info("未获取到燃气接口地址前缀");
                return "";
            }
            String rqUrlPrefix = "0".equals(rqEnv) ? rqUrlPrefixDev : rqUrlPrefixProd;
            String apiUrl = rqUrlPrefix + "/com/common/accountRegister?authVersion=v2";
            log.info("燃气身份证用户注册接口地址--->" + apiUrl);
            // String idCard = auditProject.getCertnum();
            String idCard = JSONObject.parseObject(param).getString("idCard");
            String projectGuid = JSONObject.parseObject(param).getString("projectGuid");
            JSONObject params = new JSONObject();
            params.put("idCardNo", idCard);
            String encryptParams = Base64.getEncoder()
                    .encodeToString(params.toJSONString().getBytes(StandardCharsets.UTF_8));
            JSONObject finalParams = new JSONObject();
            finalParams.put("request", encryptParams);
            log.info("燃气身份证用户注册接口请求参数明文--->" + params);
            log.info("燃气身份证用户注册接口请求参数密文--->" + finalParams);
            Map<String, String> headers = new HashMap<>();
            // 获取请求头签名
            String sign = generateRqHeader();
            headers.put("param", sign);
            log.info("燃气身份证用户注册接口请求头--->" + headers);
            String resultStr = HttpUtil.doPostJson(apiUrl, finalParams.toJSONString(), headers);
            log.info("燃气身份证用户注册接口返回值--->" + resultStr);

            if (StringUtil.isBlank(resultStr)) {
                log.info("未获取到接口返回值");
                return "";
            }
            // 获取到返回值
            JSONObject resultJson = JSON.parseObject(resultStr);
            if ("0".equals(resultJson.getString("errcode"))) {
                // 请求成功
                String accountId = resultJson.getJSONObject("data").getString("accountId");
                log.info("获取到的accountId--->" + accountId);
                log.info("将accountId存入audit_project的billNo字段");
                AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid(projectGuid, null).getResult();
                auditProject.set("billno", accountId);
                log.info("更新auditProject");
                auditProjectService.updateProject(auditProject);
                log.info("更新成功");
                log.info("========== 结束调用燃气身份证用户注册接口 ==========");
                return "请求成功";
            }
            else {
                // 请求失败，打印错误信息
                log.info("请求失败，错误信息：" + resultJson.getString("errormsg"));
                return "";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("调用燃气报装提交接口出现异常");
            return "";
        }
    }

    /**
     * 燃气报装提交接口
     */
    @PostMapping("/rqbztj")
    public String rqbztj(@RequestBody String param) {
        InputStream content = null;
        ByteArrayOutputStream outputStream = null;
        try {
            log.info("========== 开始调用燃气报装提交接口 ==========");
            log.info("接口入参--->" + param);
            IConfigService configservice = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
            ICxBusService cxBusService = ContainerFactory.getContainInfo().getComponent(ICxBusService.class);
            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            IAttachService iAttachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
            IAuditProjectMaterial projectMaterialService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectMaterial.class);
            String subAppGuid = JSON.parseObject(param).getString("subAppGuid");
            String projectGuid = JSON.parseObject(param).getString("projectGuid");
            String rqEnv = configservice.getFrameConfigValue("rqEnv");
            if (StringUtil.isBlank(rqEnv)) {
                log.info("未获取到燃气接口环境系统参数");
                return "";
            }
            String rqUrlPrefixDev = configservice.getFrameConfigValue("rqUrlPrefixDev");
            String rqUrlPrefixProd = configservice.getFrameConfigValue("rqUrlPrefixProd");
            if (StringUtil.isBlank(rqUrlPrefixDev) || StringUtil.isBlank(rqUrlPrefixProd)) {
                log.info("未获取到燃气接口地址前缀");
                return "";
            }
            String rqUrlPrefix = "0".equals(rqEnv) ? rqUrlPrefixDev : rqUrlPrefixProd;
            String apiUrl = rqUrlPrefix + "/com/common/saveInstall?authVersion=v2";

            log.info("subAppGuid--->" + subAppGuid);
            log.info("查询燃气报装的电子表单数据，参数subAppGuid--->" + subAppGuid);
            // 根据参数获取电子表单业务数据，燃气报装表名：formtable20231123163828
            Record formDetail = cxBusService.getDzbdDetail("formtable20231123163828", subAppGuid);
            log.info("查询结果--->" + formDetail);

            AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid(projectGuid, null).getResult();
            log.info("查询到的办件--->" + auditProject);

            JSONObject params = new JSONObject();
            // 城市行政区域编码
            params.put("cityNo", auditProject.getAreacode());
            // 对接第三方名称
            params.put("outsideSystemName", "济宁市一窗受理综合服务平台");
            // 报装类型
            params.put("installType", formDetail.getStr("bzlx"));
            // 外部工单编号
            params.put("serialId", projectGuid);
            // 用户编号
            params.put("consNo", "");
            // 联系人
            params.put("linkMan", formDetail.getStr("lxr"));
            // 账户id
            if (ObjectUtils.isEmpty(auditProject)) {
                log.info("未查询到办件，accountId设置为0");
                params.put("accountId", "0");
            }
            else {
                log.info("查询到办件，获取办件的accountId");
                params.put("accountId", auditProject.getStr("billno"));
            }
            // 联系电话
            params.put("phone", formDetail.getStr("lxdh"));
            // 报装地址
            params.put("address", formDetail.getStr("bzdz"));
            // 报装说明
            params.put("remark", formDetail.getStr("bzsm"));
            // 附件 需要调用接口

            // 用气设备列表
            JSONArray deviceList = new JSONArray();
            // 目前电子表单就写死为一个用气设备
            JSONObject device = new JSONObject();
            // 设备名称
            device.put("deviceName", formDetail.getStr("sbmc"));
            // 型号
            device.put("model", formDetail.getStr("xh"));
            // 外形尺寸
            device.put("size", formDetail.getStr("wxcc"));
            // 单台额定用气量
            device.put("normalGasRate", formDetail.getStr("dtedyql"));
            // 气管接口管径
            device.put("pipeDiameter", formDetail.getStr("qgjkgj"));
            // 台数
            device.put("quantity", formDetail.getStr("ts"));
            // 额定热负荷
            device.put("heatLoad", formDetail.getStr("edrfh"));
            deviceList.add(device);
            params.put("deviceList", deviceList);

            // 附件
            List<String> fileIdList = new ArrayList<>();
            List<String> fileTypeList = Arrays.asList(".pdf", ".png", ".jpg", ".jpeg", ".bmp");
            // 获取办件对应的材料附件
            log.info("根据projectGuid查询AuditProjectMaterial");
            List<AuditProjectMaterial> materialList = projectMaterialService
                    .selectProjectMaterial(auditProject.getRowguid()).getResult();
            log.info("查询出的materialList--->" + materialList);
            if (!ObjectUtils.isEmpty(materialList)) {
                log.info("材料list不为空");
                for (AuditProjectMaterial material : materialList) {
                    List<FrameAttachInfo> attachList = iAttachService.getAttachInfoListByGuid(material.getCliengguid());
                    log.info("查询出cliengguid为" + material.getCliengguid() + "对应的附件list--->" + attachList);
                    if (!ObjectUtils.isEmpty(attachList)) {
                        log.info("附件list不为空");
                        for (FrameAttachInfo frameAttachInfo : attachList) {
                            // 燃气上传附件接口只识别图片和pdf
                            if (fileTypeList.contains(frameAttachInfo.getContentType())) {
                                // 当符合类型才进行上传
                                log.info("附件：" + frameAttachInfo.getAttachFileName() + "符合类型，转base64");
                                content = iAttachService.getAttach(frameAttachInfo.getAttachGuid()).getContent();
                                outputStream = new ByteArrayOutputStream();
                                byte[] buffer = new byte[4096];
                                int bytesRead;
                                while ((bytesRead = content.read(buffer)) != -1) {
                                    outputStream.write(buffer, 0, bytesRead);
                                }
                                byte[] bytes = outputStream.toByteArray();
                                String fileBase64 = Base64.getEncoder().encodeToString(bytes);
                                String fileName = frameAttachInfo.getAttachFileName();
                                String fileId = rqFileUpload(fileBase64, fileName);
                                if (StringUtil.isNotBlank(fileId)) {
                                    log.info("上传成功，将fileId添加");
                                    fileIdList.add(fileId);
                                }
                            }
                            else {
                                log.info("附件：" + frameAttachInfo.getAttachFileName() + "不符合类型，不进行上传");
                            }
                        }
                    }
                }
            }
            params.put("fileIdList", fileIdList);

            String encryptParams = Base64.getEncoder()
                    .encodeToString(params.toJSONString().getBytes(StandardCharsets.UTF_8));
            JSONObject finalParams = new JSONObject();
            finalParams.put("request", encryptParams);

            // 由于请求头签名有效期1分钟，所以在调用接口之前设置
            Map<String, String> headers = new HashMap<>();
            // 获取请求头签名
            String sign = generateRqHeader();
            headers.put("param", sign);
            log.info("燃气报装提交接口地址--->" + apiUrl);
            log.info("燃气报装提交接口请求头--->" + headers);
            log.info("燃气报装提交接口请求参数明文--->" + params);
            log.info("燃气报装提交接口请求参数密文--->" + finalParams);
            String resultStr = HttpUtil.doPostJson(apiUrl, finalParams.toJSONString(), headers);
            log.info("接口返回值--->" + resultStr);

            JSONObject resultJson = JSON.parseObject(resultStr);
            if ("0".equals(resultJson.getString("errcode"))) {
                log.info("请求成功");
                return resultJson.getString("errmsg");
            }
            else {
                log.info("请求失败，原因：" + resultJson.getString("errmsg"));
                return "";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("调用燃气报装提交接口出现异常", e);
            return "";
        }
        finally {
            if (content != null) {
                try {
                    content.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    log.error("输入流关闭异常");
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    log.error("输出流关闭异常");
                }
            }
        }
    }

    /**
     * 燃气附件上传
     */
    public String rqFileUpload(String fileBase64, String fileName) {
        try {
            log.info("========== 开始调用燃气附件上传接口 ==========");
            IConfigService configservice = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
            String rqEnv = configservice.getFrameConfigValue("rqEnv");
            if (StringUtil.isBlank(rqEnv)) {
                log.info("未获取到燃气接口环境系统参数");
                return "";
            }
            String rqUrlPrefixDev = configservice.getFrameConfigValue("rqUrlPrefixDev");
            String rqUrlPrefixProd = configservice.getFrameConfigValue("rqUrlPrefixProd");
            if (StringUtil.isBlank(rqUrlPrefixDev) || StringUtil.isBlank(rqUrlPrefixProd)) {
                log.info("未获取到燃气接口地址前缀");
                return "";
            }
            String rqUrlPrefix = "0".equals(rqEnv) ? rqUrlPrefixDev : rqUrlPrefixProd;
            String apiUrl = rqUrlPrefix + "/com/file/imageUploadBase64";
            log.info("燃气附件上传接口地址--->" + apiUrl);

            JSONObject params = new JSONObject();
            params.put("base64", fileBase64);
            params.put("fileName", fileName);
            log.info("燃气附件上传接口请求参数--->" + params);
            String resultStr = HttpUtil.doPostJson(apiUrl, params.toJSONString(), new HashMap<>());
            log.info("接口返回值--->" + resultStr);

            JSONObject resultJson = JSON.parseObject(resultStr);
            if ("0".equals(resultJson.getString("errcode"))) {
                String fileId = resultJson.getJSONObject("data").getString("fileId");
                log.info("请求成功，返回的fileId--->" + fileId);
                return fileId;
            }
            else {
                log.info("请求失败，原因：" + resultJson.getString("errmsg"));
                return "";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("调用燃气附件上传接口出现异常", e);
            return "";
        }
    }

    /**
     * 查询报装列表接口
     */
    @PostMapping("/rqbzlb")
    public String cxbzlb(@RequestBody String param) {
        try {
            log.info("========== 开始调用查询报装列表接口 ==========");
            log.info("入参--->" + param);
            String accountId = JSON.parseObject(param).getString("accountId");
            IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
            String rqEnv = configService.getFrameConfigValue("rqEnv");
            if (StringUtil.isBlank(rqEnv)) {
                log.info("未获取到燃气接口环境系统参数");
                return null;
            }
            String rqUrlPrefixDev = configService.getFrameConfigValue("rqUrlPrefixDev");
            String rqUrlPrefixProd = configService.getFrameConfigValue("rqUrlPrefixProd");
            if (StringUtil.isBlank(rqUrlPrefixDev) || StringUtil.isBlank(rqUrlPrefixProd)) {
                log.info("未获取到燃气接口地址前缀");
                return null;
            }
            String rqUrlPrefix = "0".equals(rqEnv) ? rqUrlPrefixDev : rqUrlPrefixProd;
            String apiUrl = rqUrlPrefix + "/com/common/getInstallList?authVersion=v2";
            log.info("查询报装列表接口地址--->" + apiUrl);

            JSONObject params = new JSONObject();
            params.put("accountId", accountId);
            String encryptParams = Base64.getEncoder()
                    .encodeToString(params.toJSONString().getBytes(StandardCharsets.UTF_8));
            JSONObject finalParams = new JSONObject();
            finalParams.put("request", encryptParams);

            // 由于请求头签名有效期1分钟，所以在调用接口之前设置
            Map<String, String> headers = new HashMap<>();
            // 获取请求头签名
            String sign = generateRqHeader();
            headers.put("param", sign);
            log.info("查询报装列表接口地址--->" + apiUrl);
            log.info("查询报装列表接口请求头--->" + headers);
            log.info("查询报装列表接口请求参数明文--->" + params);
            log.info("查询报装列表接口请求参数密文--->" + finalParams);
            String resultStr = HttpUtil.doPostJson(apiUrl, finalParams.toJSONString(), headers);
            log.info("接口返回值--->" + resultStr);

            if (StringUtil.isBlank(resultStr)) {
                log.info("未获取到接口返回值");
                return null;
            }

            JSONObject resultJson = JSON.parseObject(resultStr);
            if ("0".equals(resultJson.getString("errcode"))) {
                // 请求成功
                JSONArray data = resultJson.getJSONArray("data");
                log.info("报装数据列表--->" + data);
                // if(EpointCollectionUtils.isNotEmpty(data)) {
                if (!ObjectUtils.isEmpty(data)) {
                    return data.toJSONString();
                }
                else {
                    log.info("未获取到报装数据");
                    return null;
                }
            }
            else {
                log.info("请求失败，原因：" + resultJson.getString("errmsg"));
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("调用查询报装列表接口异常");
            return null;
        }
    }

    /**
     * 燃气生成请求头
     */
    public String generateRqHeader() {
        try {
            log.info("开始生成燃气接口请求头");
            IConfigService configservice = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
            String userName = configservice.getFrameConfigValue("rqUserName");
            String password = configservice.getFrameConfigValue("rqPassword");
            String publicKey = configservice.getFrameConfigValue("rqPublicKey");
            String param = V2AuthUtil.getParam(userName, password, publicKey);
            log.info("生成的签名--->" + param);
            return param;
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("生成燃气接口签名失败");
            return "";
        }
    }
}
