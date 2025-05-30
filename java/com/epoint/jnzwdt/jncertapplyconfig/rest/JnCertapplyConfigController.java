package com.epoint.jnzwdt.jncertapplyconfig.rest;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.jnzwdt.jncertapplyconfig.api.IJnCertapplyConfigService;
import com.epoint.jnzwdt.jncertapplyconfig.api.entity.JnCertapplyConfig;

/**
 * 证照申报配置接口
 * @author future
 * @version 2023年3月21日
 */
@RestController
@RequestMapping("/jncertapplyconfigcontroller")
public class JnCertapplyConfigController
{

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IAttachService iAttachService;

    @Autowired
    private IJnCertapplyConfigService iJnCertapplyConfigService;

    @Autowired
    private ICodeItemsService iCodeItemsService;

    @Autowired
    private IAuditTask iAuditTask;

    /**
     * 根据证照类型获取证照配置列表
     *
     * @param params  请求参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getListByCertType", method = RequestMethod.POST)
    public String getListByCertType(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getListByCertType接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject paramsObj = jsonObject.getJSONObject("params");
                List<JnCertapplyConfig> resultList = new ArrayList<>();
                // 证照分类
                String certtype = paramsObj.getString("certtype");
                String areacode = paramsObj.getString("areacode");
                if (StringUtil.isNotBlank(certtype)) {
                    String sql = " select * from  jn_certapply_config  where cert_type = ? and belongxiaqucode=? order by ordernum asc";
                    resultList = iJnCertapplyConfigService.findList(sql, certtype,areacode);
                }
                JSONObject dataJson = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                if (ValidateUtil.isNotBlankCollection(resultList)) {
                    for (JnCertapplyConfig cert : resultList) {
                        JSONObject obj = new JSONObject();
                        obj.put("rowguid", cert.getRowguid());
                        obj.put("task_relation", cert.getTask_relation() == null ? "" : cert.getTask_relation());
                        obj.put("cert_type", cert.getCert_type() == null ? "" : cert.getCert_type());
                        obj.put("applysysname", cert.getStr("applysysname") == null ? "" : cert.getStr("applysysname"));
                        obj.put("applyuser_type", "");
                        if (StringUtil.isNotBlank(cert.getApplyuser_type())) {
                            String text = iCodeItemsService.getItemTextByCodeName("申请人类型", cert.getApplyuser_type());
                            obj.put("applyuser_type", text == null ? "" : text);
                        }

                        obj.put("contact_phone", cert.getContact_phone() == null ? "" : cert.getContact_phone());
                        obj.put("certname", cert.getCertname() == null ? "" : cert.getCertname());
                        obj.put("cert_picture", cert.getCert_picture() == null ? "" : cert.getCert_picture());
                        obj.put("attachGuid", "");
                        if (StringUtil.isNotBlank(cert.getCert_picture())) {
                            List<FrameAttachStorage> attachListByGuid = iAttachService
                                    .getAttachListByGuid(cert.getCert_picture());
                            if (CollectionUtils.isNotEmpty(attachListByGuid)) {
                                obj.put("attachGuid", attachListByGuid.get(0).getAttachGuid());
                            }
                        }
                        obj.put("target_url", cert.getTarget_url() == null ? "" : cert.getTarget_url());
                        obj.put("target_type", cert.getTarget_type() == null ? "" : cert.getTarget_type());

                        obj.put("shouli_address", cert.getShouli_address() == null ? "" : cert.getShouli_address());
                        obj.put("cert_explain", cert.getCert_explain() == null ? "" : cert.getCert_explain());
                        obj.put("accept_ou", cert.getAccept_ou() == null ? "" : cert.getAccept_ou());
                        obj.put("apply_condition", cert.getApply_condition() == null ? "" : cert.getApply_condition());
                        obj.put("banli_free", cert.getBanli_free() == null ? "" : cert.getBanli_free());
                        obj.put("taskname",
                                StringUtil.isBlank(cert.getStr("taskname")) ? "无" : cert.getStr("taskname"));
                        // 标签分类
                        String tagType = iCodeItemsService.getItemTextByCodeName("证照配置标签维护", cert.getTagtype());
                        JSONArray tagArr = new JSONArray();
                        if (StringUtil.isNotBlank(tagType)) {
                            String[] split = tagType.split(",");
                            if (split != null && split.length > 0) {
                                for (String tag : split) {
                                    JSONObject jsonObject2 = new JSONObject();
                                    jsonObject2.put("tag", tag);
                                    tagArr.add(jsonObject2);
                                }
                            }
                        }

                        if ("1".equals(cert.getTarget_type())) {
                            obj.put("glsx", true);
                        }
                        else if ("2".equals(cert.getTarget_type())) {
                            obj.put("stj", true);
                        }

                        obj.put("tagtype", tagArr);

                        obj.put("taskguid", "");
                        obj.put("taskid", "");
                        // 事项
                        if (StringUtil.isNotBlank(cert.getTask_relation())) {
                            obj.put("taskid", cert.getTask_relation());
                            AuditTask task = iAuditTask.selectUsableTaskByTaskID(cert.getTask_relation()).getResult();
                            if (task != null) {
                                obj.put("taskguid", task.getRowguid());
                                obj.put("itemcode", task.getItem_id());
                            }
                        }

                        jsonArray.add(obj);
                    }
                }
                dataJson.put("certlist", jsonArray);
                log.info("=======结束调用getListByCertType接口=======");
                return JsonUtils.zwdtRestReturn("1", "据证照类型获取证照配置列表成功！", dataJson.toString());

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("接口调用失败！错误信息：" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "接口异常", "");
        }
    }

    /**
     * 根据证照类型获取证照配置列表
     *
     * @param params  请求参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getListBySearch", method = RequestMethod.POST)
    public String getListBySearch(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getListBySearch接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject paramsObj = jsonObject.getJSONObject("params");
                List<JnCertapplyConfig> resultList = new ArrayList<>();
                // 证照分类
                String search = paramsObj.getString("search");
                String areacode = paramsObj.getString("areacode");
                if (StringUtil.isNotBlank(search)) {
                    String sql = " select * from  jn_certapply_config  where certname like '%" + search
                            + "%' and belongxiaqucode=?  order by ordernum asc";
                    resultList = iJnCertapplyConfigService.findList(sql,areacode);
                }
                JSONObject dataJson = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                if (ValidateUtil.isNotBlankCollection(resultList)) {
                    for (JnCertapplyConfig cert : resultList) {
                        JSONObject obj = new JSONObject();
                        obj.put("rowguid", cert.getRowguid());
                        obj.put("task_relation", cert.getTask_relation() == null ? "" : cert.getTask_relation());
                        obj.put("cert_type", cert.getCert_type() == null ? "" : cert.getCert_type());
                        obj.put("applysysname", cert.getStr("applysysname") == null ? "" : cert.getStr("applysysname"));
                        obj.put("applyuser_type", "");
                        if (StringUtil.isNotBlank(cert.getApplyuser_type())) {
                            String text = iCodeItemsService.getItemTextByCodeName("申请人类型", cert.getApplyuser_type());
                            obj.put("applyuser_type", text == null ? "" : text);
                        }

                        obj.put("contact_phone", cert.getContact_phone() == null ? "" : cert.getContact_phone());
                        obj.put("certname", cert.getCertname() == null ? "" : cert.getCertname());
                        obj.put("cert_picture", cert.getCert_picture() == null ? "" : cert.getCert_picture());
                        obj.put("attachGuid", "");
                        if (StringUtil.isNotBlank(cert.getCert_picture())) {
                            List<FrameAttachStorage> attachListByGuid = iAttachService
                                    .getAttachListByGuid(cert.getCert_picture());
                            if (CollectionUtils.isNotEmpty(attachListByGuid)) {
                                obj.put("attachGuid", attachListByGuid.get(0).getAttachGuid());
                            }
                        }
                        obj.put("target_url", cert.getTarget_url() == null ? "" : cert.getTarget_url());
                        obj.put("target_type", cert.getTarget_type() == null ? "" : cert.getTarget_type());

                        obj.put("shouli_address", cert.getShouli_address() == null ? "" : cert.getShouli_address());
                        obj.put("cert_explain", cert.getCert_explain() == null ? "" : cert.getCert_explain());
                        obj.put("accept_ou", cert.getAccept_ou() == null ? "" : cert.getAccept_ou());
                        obj.put("apply_condition", cert.getApply_condition() == null ? "" : cert.getApply_condition());
                        obj.put("banli_free", cert.getBanli_free() == null ? "" : cert.getBanli_free());
                        obj.put("taskname",
                                StringUtil.isBlank(cert.getStr("taskname")) ? "无" : cert.getStr("taskname"));
                        // 标签分类
                        String tagType = iCodeItemsService.getItemTextByCodeName("证照配置标签维护", cert.getTagtype());
                        JSONArray tagArr = new JSONArray();
                        if (StringUtil.isNotBlank(tagType)) {
                            String[] split = tagType.split(",");
                            if (split != null && split.length > 0) {
                                for (String tag : split) {
                                    JSONObject jsonObject2 = new JSONObject();
                                    jsonObject2.put("tag", tag);
                                    tagArr.add(jsonObject2);
                                }
                            }
                        }
                        obj.put("tagtype", tagArr);

                        obj.put("taskguid", "");
                        obj.put("taskid", "");
                        // 事项
                        if (StringUtil.isNotBlank(cert.getTask_relation())) {
                            obj.put("taskid", cert.getTask_relation());
                            AuditTask task = iAuditTask.selectUsableTaskByTaskID(cert.getTask_relation()).getResult();
                            if (task != null) {
                                obj.put("taskguid", task.getRowguid());
                                obj.put("itemcode", task.getItem_id());
                            }
                        }

                        jsonArray.add(obj);
                    }
                }
                dataJson.put("certlist", jsonArray);
                log.info("=======结束调用getListBySearch接口=======");
                return JsonUtils.zwdtRestReturn("1", "据证照名称获取证照配置列表成功！", dataJson.toString());

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("接口调用失败！错误信息：" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "接口异常", "");
        }
    }

    /**
     * 根据证照guid
     *
     * @param params  请求参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getCertByGuid", method = RequestMethod.POST)
    public String getCertByGuid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getCertByGuid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject paramsObj = jsonObject.getJSONObject("params");
                // 证照
                String guid = paramsObj.getString("guid");
                JnCertapplyConfig cert = null;
                if (StringUtil.isNotBlank(guid)) {
                    cert = iJnCertapplyConfigService.find(guid);
                }
                JSONObject dataJson = new JSONObject();
                if (cert != null) {
                    JSONObject obj = new JSONObject();
                    obj.put("rowguid", cert.getRowguid());
                    obj.put("task_relation", cert.getTask_relation() == null ? "" : cert.getTask_relation());
                    obj.put("cert_type", cert.getCert_type() == null ? "" : cert.getCert_type());
                    obj.put("applyuser_type", "");
                    if (StringUtil.isNotBlank(cert.getApplyuser_type())) {
                        String text = iCodeItemsService.getItemTextByCodeName("申请人类型", cert.getApplyuser_type());
                        obj.put("applyuser_type", text == null ? "" : text);
                    }
                    obj.put("contact_phone", cert.getContact_phone() == null ? "" : cert.getContact_phone());
                    obj.put("certname", cert.getCertname() == null ? "" : cert.getCertname());
                    obj.put("cert_picture", cert.getCert_picture() == null ? "" : cert.getCert_picture());
                    obj.put("attachGuid", "");
                    if (StringUtil.isNotBlank(cert.getCert_picture())) {
                        List<FrameAttachStorage> attachListByGuid = iAttachService
                                .getAttachListByGuid(cert.getCert_picture());
                        if (CollectionUtils.isNotEmpty(attachListByGuid)) {
                            obj.put("attachGuid", attachListByGuid.get(0).getAttachGuid());
                        }
                    }
                    obj.put("target_url", cert.getTarget_url() == null ? "" : cert.getTarget_url());
                    obj.put("target_type", cert.getTarget_type() == null ? "" : cert.getTarget_type());
                    obj.put("shouli_address", cert.getShouli_address() == null ? "" : cert.getShouli_address());
                    obj.put("cert_explain", cert.getCert_explain() == null ? "" : cert.getCert_explain());
                    obj.put("accept_ou", cert.getAccept_ou() == null ? "" : cert.getAccept_ou());
                    obj.put("apply_condition", cert.getApply_condition() == null ? "" : cert.getApply_condition());
                    obj.put("banli_free", cert.getBanli_free() == null ? "" : cert.getBanli_free());
                    obj.put("applysysname", cert.getStr("applysysname") == null ? "" : cert.getStr("applysysname"));
                    obj.put("taskname", StringUtil.isBlank(cert.getStr("taskname")) ? "无" : cert.getStr("taskname"));
                    // 标签分类
                    String tagType = iCodeItemsService.getItemTextByCodeName("证照配置标签维护", cert.getTagtype());
                    JSONArray tagArr = new JSONArray();
                    if (StringUtil.isNotBlank(tagType)) {
                        String[] split = tagType.split(",");
                        if (split != null && split.length > 0) {
                            for (String tag : split) {
                                tagArr.add(tag);
                            }
                        }
                    }
                    obj.put("tagtype", tagArr);

                    obj.put("taskguid", "");
                    obj.put("taskid", "");
                    // 事项
                    if (StringUtil.isNotBlank(cert.getTask_relation())) {
                        obj.put("taskid", cert.getTask_relation());
                        AuditTask task = iAuditTask.selectUsableTaskByTaskID(cert.getTask_relation()).getResult();
                        if (task != null) {
                            obj.put("taskguid", task.getRowguid());
                            obj.put("itemcode", task.getItem_id());
                        }
                    }
                    dataJson.put("certinfo", obj);
                }
                log.info("=======结束调用getCertByGuid接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取证照配置成功！", dataJson.toString());

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (

        Exception e) {
            e.printStackTrace();
            log.info("接口调用失败！错误信息：" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "接口异常", "");
        }
    }

}
