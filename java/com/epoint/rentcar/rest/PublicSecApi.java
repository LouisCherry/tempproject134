package com.epoint.rentcar.rest;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Document;
import com.aspose.words.FontSettings;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.common.util.AttachUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.rentcar.Constant.RentCarConstant;
import com.epoint.rentcar.service.RentCarService;
import com.epoint.rentcar.util.HttpUtil;
import com.epoint.search.inteligentsearch.sdk.util.EpointDateUtil;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.*;

/**
 * 背景核验接口
 *
 * @author jiem
 * @version
 */
@RestController
@RequestMapping("/publicSecApi")
public class PublicSecApi {

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 网上用户
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;

    @Autowired
    private IAuditProjectMaterial iAuditProjectMaterial;

    @Autowired
    private IAttachService iAttachService;



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
        }
        else {
            // PC端
            String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
            if (StringUtil.isNotBlank(accountGuid)) {
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
            }
            else {
                // 通过登录名获取用户
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.getCurrentIdentity())
                        .getResult();
            }
        }
        return auditOnlineRegister;
    }

    /**
     * 背景核验
     *
     * @param params  请求参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/isBgv", method = RequestMethod.POST)
    public String changeArea(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用isBgv接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject dataJson = new JSONObject();

                AuditOnlineRegister onlineRegister = getOnlineRegister(request);
                if (onlineRegister == null){
                    return JsonUtils.zwdtRestReturn("0", "登录信息已失效，请重新登陆！", dataJson.toString());
                }
                String idnumber = onlineRegister.getIdnumber();
                String username = onlineRegister.getUsername();
                if (StringUtil.isBlank(idnumber) && StringUtil.isBlank(username)){
                    return JsonUtils.zwdtRestReturn("0", "个人信息不完整，请完善个人信息！", dataJson.toString());
                }
                // 构建参数
                HashMap<String, String> map = new HashMap<>();
                map.put("XM",username);
                map.put("ZJHM",idnumber);
                JSONObject param = new JSONObject();
                param.put("operatorId", RentCarConstant.OPERATOR_ID);
                param.put("operatorQueryTime", EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                param.put("operatorIp",request.getRemoteAddr());
                param.put("operatorUnitName", RentCarConstant.UNIT_NAME);
                param.put("operatorQueryReason", RentCarConstant.QUERY_REASON);
                param.put("requesterPhoneNum", RentCarConstant.PHONENUM);
                param.put("operatorIdCard", RentCarConstant.ID_CARD);
                param.put("operatorPhoneNum", RentCarConstant.PHONENUM);
                map.put("GASJGX_PARAM",param.toJSONString());

                // 密文
                String text = HttpUtil.httpPostWithForm(RentCarConstant.BJHY_URL, map);
                SymmetricCrypto symmetricCrypto = SmUtil.sm4(RentCarConstant.KEY.getBytes());
                // 解密
                String returnJson = symmetricCrypto.decryptStr(text);
                if (StringUtil.isBlank(returnJson)) {
                    return JsonUtils.zwdtRestReturn("0", "核验失败，请稍后再试！", dataJson.toString());
                }
                JSONObject returnJsonObject = JSONObject.parseObject(returnJson);
                if(!"200".equals(returnJsonObject.getString("code"))){
                    log.info("接口调用失败！错误信息：" + returnJsonObject.getString("msg"));
                    return JsonUtils.zwdtRestReturn("0", "查询失败，请稍后重试！", "");
                }
                JSONArray data = returnJsonObject.getJSONArray("data");
                if (data.isEmpty()) {
                    return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());
                }

                log.info("=======isBgv接口=======");
                return JsonUtils.zwdtRestReturn("1", "经查询您在某个时间有不良记录，不允许申报该业务，详情请去柜台！", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            log.info("接口调用失败！错误信息：" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "背景核验异常", "");
        }
    }

    /**
     * 获取驾驶证信息并关联材料
     *
     * @param params  请求参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/relaDriMater", method = RequestMethod.POST)
    public String relaDriMater(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用relaDriMater接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject params1 = jsonObject.getJSONObject("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject dataJson = new JSONObject();

                String id = params1.getString("id");
                String name = params1.getString("name");
                String projectGuid = params1.getString("project_guid");

                // 参数的必填检验
                if (StringUtil.isBlank(id) && StringUtil.isBlank(name) && StringUtil.isBlank(projectGuid)){
                    return JsonUtils.zwdtRestReturn("0", "参数不完整！", dataJson.toString());
                }
                // 构建参数
                HashMap<String, String> paramMap = new HashMap<>();
                paramMap.put("XM",name);
                paramMap.put("SFZMHM",id);
                JSONObject param = new JSONObject();
                param.put("operatorId", RentCarConstant.OPERATOR_ID);
                param.put("operatorQueryTime", EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                param.put("operatorIp",request.getRemoteAddr());
                param.put("operatorUnitName", RentCarConstant.UNIT_NAME);
                param.put("operatorQueryReason", RentCarConstant.QUERY_REASON);
                param.put("requesterPhoneNum", RentCarConstant.PHONENUM);
                paramMap.put("GASJGX_PARAM",param.toJSONString());

                // 密文
                String text = HttpUtil.httpPostWithForm(RentCarConstant.JSZ_URL, paramMap);
                SymmetricCrypto symmetricCrypto = SmUtil.sm4(RentCarConstant.KEY.getBytes());
                // 解密
                String returnJson = symmetricCrypto.decryptStr(text);
                log.info("V_DRV接口返回的数据：" + returnJson);
                if (StringUtil.isBlank(returnJson)) {
                    return JsonUtils.zwdtRestReturn("0", "核验失败，请稍后再试！", dataJson.toString());
                }
                JSONObject returnJsonObject = JSONObject.parseObject(returnJson);
                if(!"200".equals(returnJsonObject.getString("code"))){
                    log.info("接口调用失败！错误信息：" + returnJsonObject.getString("msg"));
                    return JsonUtils.zwdtRestReturn("0", "查询失败，请稍后重试！", "");
                }
                JSONArray dataArray = returnJsonObject.getJSONArray("data");

                if (dataArray.isEmpty()) {
                    return JsonUtils.zwdtRestReturn("1", "无驾驶证信息", dataJson.toString());
                }
                JSONObject data = dataArray.getJSONObject(0);

                Map<String, String> map = getJszDocValue(params1, id, name, data);
                AuditTaskMaterial auditTaskMaterial = new RentCarService().getAuditTaskMaterial(projectGuid, 1);
                if (auditTaskMaterial == null){
                    return JsonUtils.zwdtRestReturn("0", "该办件没有驾驶证材料！", dataJson.toString());
                }
                String templateClientGuid = auditTaskMaterial.getTemplateattachguid();
                if (StringUtil.isNotBlank(templateClientGuid)){
                    List<FrameAttachStorage> attachListByGuid = iAttachService.getAttachListByGuid(templateClientGuid);
                    if (attachListByGuid.isEmpty() || attachListByGuid.size() > 1){
                        return JsonUtils.zwdtRestReturn("0", "模板获取错误，请联系管理员维护模板！", dataJson.toString());
                    }
                    FrameAttachStorage attach = attachListByGuid.get(0);
                    if (attach != null && (".doc".equals(attach.getDocumentType()) || ".docx".equals(attach.getDocumentType()))){
                        String[] fieldNames = null;
                        Object[] values = null;
                        activeLicense();
                        Document doc = new Document(attach.getContent());
                        fieldNames = new String[map == null ? 0 : map.size()];
                        values = new Object[map == null ? 0 : map.size()];
                        int num = 0;
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            fieldNames[num] = entry.getKey();
                            values[num] = entry.getValue();
                            num++;
                        }
                        // 替换域
                        doc.getMailMerge().execute(fieldNames, values);
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        // 保存成PDF
                        doc.save(outputStream, SaveFormat.PDF);
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                        long size = inputStream.available();
                        String attachGuid = UUID.randomUUID().toString();
                        String cliengGuid = UUID.randomUUID().toString();
                        AttachUtil.saveFileInputStream(attachGuid,
                                cliengGuid, name + "-驾驶证.pdf", ".pdf", "办件材料", size, inputStream, "",
                                "系统生成");
                        log.info("附件下载地址:http://localhost:8093/epoint-web-zwfw/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=" + attachGuid);
                        // 关联材料
                        AuditProjectMaterial auditProjectMaterial = new RentCarService().getAuditProjectMaterial(projectGuid, auditTaskMaterial.getRowguid());
                        auditProjectMaterial.setCliengguid(cliengGuid);
                        auditProjectMaterial.setStatus(20);
                        auditProjectMaterial.setAttachfilefrom("1");
                        iAuditProjectMaterial.updateProjectMaterial(auditProjectMaterial);
                        return JsonUtils.zwdtRestReturn("1", "成功", dataJson.toString());
                    }
                }
                log.info("=======relaDriMater接口=======");
                return JsonUtils.zwdtRestReturn("1", "未上传模板，请联系管理员上传！", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取驾驶证信息并关联材料失败", "");
        }
    }

    /**
     * 获取行驶证信息并关联材料
     *
     * @param params  请求参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/relaGDriMater", method = RequestMethod.POST)
    public String relaGDriMater(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用relaDriMater接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject params1 = jsonObject.getJSONObject("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject dataJson = new JSONObject();

                String syr = params1.getString("syr");
                String hphm = params1.getString("hphm");
                String projectGuid = params1.getString("project_guid");

                // 参数的必填检验
                if (StringUtil.isBlank(syr) && StringUtil.isBlank(hphm) && StringUtil.isBlank(projectGuid)){
                    return JsonUtils.zwdtRestReturn("0", "参数不完整！", dataJson.toString());
                }
                // 构建参数
                HashMap<String, String> paramMap = new HashMap<>();
                paramMap.put("SYR",syr);
                paramMap.put("HPHM",hphm);
                JSONObject param = new JSONObject();
                param.put("operatorId", RentCarConstant.OPERATOR_ID);
                param.put("operatorQueryTime", EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                param.put("operatorIp",request.getRemoteAddr());
                param.put("operatorUnitName", RentCarConstant.UNIT_NAME);
                param.put("operatorQueryReason", RentCarConstant.QUERY_REASON);
                param.put("requesterPhoneNum", RentCarConstant.PHONENUM);
                paramMap.put("GASJGX_PARAM",param.toJSONString());
                try {
                    // 密文
                    String text = HttpUtil.httpPostWithForm(RentCarConstant.JSZ_URL, paramMap);
                    SymmetricCrypto symmetricCrypto = SmUtil.sm4(RentCarConstant.KEY.getBytes());
                    // 解密
                    String returnJson = symmetricCrypto.decryptStr(text);
                    log.info("V_DRV接口返回的数据：" + returnJson);
                    if (StringUtil.isBlank(returnJson)) {
                        return JsonUtils.zwdtRestReturn("0", "核验失败，请稍后再试！", dataJson.toString());
                    }
                    JSONObject returnJsonObject = JSONObject.parseObject(returnJson);
                    JSONArray dataArray = returnJsonObject.getJSONArray("data");

                    if (dataArray.isEmpty()) {
                        return JsonUtils.zwdtRestReturn("1", "无行驶证信息", dataJson.toString());
                    }

                    JSONObject data = dataArray.getJSONObject(0);
                    Map<String, String> map = new HashMap<>();

                    getXszDocValue(params1, data, map);
                    AuditTaskMaterial auditTaskMaterial = new RentCarService().getAuditTaskMaterial(projectGuid, 2);
                    if (auditTaskMaterial == null) {
                        return JsonUtils.zwdtRestReturn("0", "该办件没有行驶证材料！", dataJson.toString());
                    }
                    String templateClientGuid = auditTaskMaterial.getTemplateattachguid();
                    if (StringUtil.isNotBlank(templateClientGuid)) {
                        List<FrameAttachStorage> attachListByGuid = iAttachService.getAttachListByGuid(templateClientGuid);
                        if (attachListByGuid.isEmpty() || attachListByGuid.size() > 1){
                            return JsonUtils.zwdtRestReturn("0", "模板获取错误，请联系管理员维护模板！", dataJson.toString());
                        }
                        FrameAttachStorage attach = attachListByGuid.get(0);
                        if (attach != null && (".doc".equals(attach.getDocumentType()) || ".docx".equals(attach.getDocumentType()))) {
                            String[] fieldNames = null;
                            Object[] values = null;
                            activeLicense();
                            Document doc = new Document(attach.getContent());
                            fieldNames = new String[map == null ? 0 : map.size()];
                            values = new Object[map == null ? 0 : map.size()];
                            int num = 0;
                            for (Map.Entry<String, String> entry : map.entrySet()) {
                                fieldNames[num] = entry.getKey();
                                values[num] = entry.getValue();
                                num++;
                            }
                            // 替换域
                            doc.getMailMerge().execute(fieldNames, values);
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            // 保存成PDF
                            doc.save(outputStream, SaveFormat.PDF);
                            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                            long size = inputStream.available();
                            String attachGuid = UUID.randomUUID().toString();
                            String cliengGuid = UUID.randomUUID().toString();
                            AttachUtil.saveFileInputStream(attachGuid,
                                    cliengGuid, syr + "-行驶证.pdf", ".pdf", "办件材料", size, inputStream, "",
                                    "系统生成");
                            log.info("附件下载地址:http://localhost:8093/epoint-web-zwfw/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=" + attachGuid);
                            // 关联材料
                            AuditProjectMaterial auditProjectMaterial = new RentCarService().getAuditProjectMaterial(projectGuid, auditTaskMaterial.getRowguid());
                            auditProjectMaterial.setCliengguid(cliengGuid);
                            auditProjectMaterial.setStatus(20);
                            auditProjectMaterial.setAttachfilefrom("1");
                            iAuditProjectMaterial.updateProjectMaterial(auditProjectMaterial);
                            return JsonUtils.zwdtRestReturn("1", "成功", dataJson.toString());
                        }
                    }
                }catch (Exception e){
                    log.info("接口调用失败！错误信息：" + e.getMessage());
                }
                log.info("=======relaGDriMater接口=======");
                return JsonUtils.zwdtRestReturn("1", "未上传模板，请联系管理员上传！", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取行驶证信息并关联材料失败", "");
        }
    }

    /**
     * 驾驶证模板域的值
     * @param params1
     * @param id
     * @param name
     * @param data
     * @return
     */
    @NotNull
    private Map<String, String> getJszDocValue(JSONObject params1, String id, String name, JSONObject data) {

        Map<String, String> map = new HashMap<>();
        map.put("姓名", name);
        map.put("性别", params1.getString("sex"));
        map.put("身份证号", id);
        map.put("家庭住址", params1.getString("jtdz"));
        map.put("出生日期",EpointDateUtil.convertDate2String(params1.getDate("csrq"),EpointDateUtil.DATE_FORMAT));
        if (StringUtil.isNotBlank(data.getString("ZJCX"))){
            map.put("准驾车型", data.getString("ZJCX"));
        }else {
            map.put("准驾车型", params1.getString("zjcx"));
        }
        // 领证日期
        Date lzrq = null;
        if (StringUtil.isNotBlank(data.getString("ZJCX"))){
            lzrq = data.getDate("CCLZRQ");
        }else {
            lzrq = params1.getDate("lzrq");
        }
        map.put("lzrq年", String.valueOf(EpointDateUtil.getYearOfDate(lzrq)));
        map.put("lzrq月", String.valueOf(EpointDateUtil.getMonthOfDate(lzrq) + 1));
        map.put("lzrq日", String.valueOf(EpointDateUtil.getDayOfDate(lzrq)));

        //有效期始
        Date yxqs = null;
        if (data.getDate("YXQS") != null){
            yxqs = data.getDate("YXQS");
        }else {
            yxqs = params1.getDate("yxrqs");
        }
        map.put("yxqs年", String.valueOf(EpointDateUtil.getYearOfDate(yxqs)));
        map.put("yxqs月", String.valueOf(EpointDateUtil.getMonthOfDate(yxqs) + 1));
        map.put("yxqs日", String.valueOf(EpointDateUtil.getDayOfDate(yxqs)));

        //有效期止
        Date yxqz = null;
        if (data.getDate("YXQZ") != null){
            yxqz = data.getDate("YXQZ");
        }else {
            yxqz = params1.getDate("yxrqz");
        }
        map.put("yxqz年", String.valueOf(EpointDateUtil.getYearOfDate(yxqz)));
        map.put("yxqz月", String.valueOf(EpointDateUtil.getMonthOfDate(yxqz) + 1));
        map.put("yxqz日", String.valueOf(EpointDateUtil.getDayOfDate(yxqz)));
        return map;
    }

    /**
     * 行驶证模板域的值
     * @param params1
     * @param data
     * @param map
     */
    private void getXszDocValue(JSONObject params1, JSONObject data, Map<String, String> map) {
        if (StringUtil.isBlank(data.getString("SYR"))){
            map.put("所有人", params1.getString("syr"));
        }else {
            map.put("所有人", data.getString("SYR"));
        }
        if (StringUtil.isBlank(data.getString("CLPP1"))){
            map.put("车辆品牌", params1.getString("clpp"));
        }else {
            map.put("车辆品牌", data.getString("CLPP1"));
        }
        if (StringUtil.isBlank(data.getString("CLLX"))){
            map.put("车辆类型", params1.getString("cllx"));
        }else {
            map.put("车辆类型", data.getString("CLLX"));
        }
        if (StringUtil.isBlank(data.getString("HBDBQK"))){
            map.put("环保达标情况", params1.getString("hbdbqk"));
        }else {
            map.put("环保达标情况", data.getString("HBDBQK"));
        }
        if (StringUtil.isBlank(data.getString("CSYSMC"))){
            map.put("车身颜色", params1.getString("csys"));
        }else {
            map.put("车身颜色", data.getString("CSYSMC"));
        }
        if (StringUtil.isBlank(data.getString("HPHM"))){
            map.put("号码号牌", params1.getString("hphm"));
        }else {
            map.put("号码号牌", data.getString("HPHM"));
        }
        if (StringUtil.isBlank(data.getString("DABH"))){
            map.put("档案编号", params1.getString("dabh"));
        }else {
            map.put("档案编号", data.getString("DABH"));
        }
        if (StringUtil.isBlank(data.getString("ZJ"))){
            map.put("轴距", params1.getString("zj"));
        }else {
            map.put("轴距", data.getString("ZJ"));
        }
        if (StringUtil.isBlank(data.getString("ZS"))){
            map.put("轴数", params1.getString("zs"));
        }else {
            map.put("轴数", data.getString("ZS"));
        }
        if (StringUtil.isBlank(data.getString("HDZZL"))){
            map.put("核定载质量", params1.getString("hdzzl"));
        }else {
            map.put("核定载质量", data.getString("HDZZL"));
        }
        if (StringUtil.isBlank(data.getString("SYXZMC"))){
            map.put("使用性质", params1.getString("syxz"));
        }else {
            map.put("使用性质", data.getString("SYXZMC"));
        }
        if (StringUtil.isBlank(data.getString("FZJG"))){
            map.put("发证机关", params1.getString("fzjg"));
        }else {
            map.put("发证机关", data.getString("FZJG"));
        }
        Date fzrq = data.getDate("FZRQ");
        if (fzrq == null){
            fzrq = params1.getDate("fzrq");
        }
        if (fzrq != null) {
            map.put("年", String.valueOf(EpointDateUtil.getYearOfDate(fzrq)));
            map.put("月", String.valueOf(EpointDateUtil.getMonthOfDate(fzrq) + 1));
            map.put("日", String.valueOf(EpointDateUtil.getDayOfDate(fzrq)));
        }else {
            map.put("年", " ");
            map.put("月", " ");
            map.put("日", " ");
        }
    }

    /**
     * 加载授权文件lic，激活AsposeWords
     */
    private void activeLicense() {
        License license = new License();
        try {
            // 从配置文件中获取许可证名称
            String licname = "license.xml";
            // 获取许可证文件路径
            String path = ClassPathUtil.getClassesPath() + licname;
            // 若文件存在，则直接激活
            if (FileManagerUtil.isExist(path, false)) {
                license.setLicense(path);
            }
            else {
                // 若路径下不存在许可证，则直接使用许可证名称（适用于tomcat中）激活许可证
                license.setLicense(licname);
            }
            // 设置字体否则pdf乱码
//            FontSettings.setFontsFolder(ClassPathUtil.getClassesPath() + "font" + File.separator, true);
        }
        catch (Exception e) {
            e.printStackTrace();
            // log.error("Aspose.Words许可证激活失败，请检查许可证信息是否正确！许可证是否存在，或许可证名称是否准确。");
        }
    }
}
