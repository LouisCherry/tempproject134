package com.epoint.zwdt.zwdtrest.project.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.spgl.inter.ISpglFail;
import com.epoint.common.util.ValidateUtil;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.xmz.wjw.api.ICxBusService;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.*;

/**
 * 中山水务接口封装
 */
public class ZhongShanShuiWuUtil {
    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 提交工单前校验
     * @param flowcode 流程编码
     * @param cardno 户号
     */
    public static JSONObject checkSubmitFlow(String flowcode, String cardno) {
        log.info("========== 开始调用提交工单前校验接口 ==========");
        try {
            JSONObject result = new JSONObject();
            result.put("isSuccess", false);
            String zhongShanShuiWuUrl = ConfigUtil.getConfigValue("duijie","zhongShanShuiWuUrl");
            if (StringUtil.isBlank(zhongShanShuiWuUrl)) {
                result.put("msg", "未获取到zhongShanShuiWuUrl值");
                return result;
            }
            String requesturl = zhongShanShuiWuUrl + "/api/appService/checkSubmitFlow";
            String params = "flowcode="+flowcode+"&"+"cardno="+cardno;
            log.info("=======开始调用checkSubmitFlow接口======="+requesturl+" 参数 "+params);
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Content-Type", "application/x-www-form-urlencoded");
            String resultStr = HttpUtil.doHttp(requesturl, headerMap, params, "post", HttpUtil.RTN_TYPE_STRING);
            log.info("=======结束调用checkSubmitFlow接口 resultStr======="+resultStr);
            if (StringUtil.isBlank(resultStr)) {
                log.info("未获取到接口返回值");
                result.put("msg", "未获取到接口返回值");
                result.put("code", "-1");
                return result;
            }

            JSONObject resultJson = JSON.parseObject(resultStr);
            if ("200".equals(resultJson.getString("code"))) {
                log.info("请求成功");
                result.put("isSuccess", true);
            }
            else {
                log.info("请求失败");
                result.put("code", "0");
                result.put("msg", resultJson.getString("msg"));
            }
            return result;
        }
        catch (Exception e){
            e.printStackTrace();
            log.info("调用提交工单前校验接口出现异常");
            JSONObject result = new JSONObject();
            result.put("isSuccess", false);
            result.put("code", "-1");
            result.put("msg", "调用提交工单前校验接口出现异常");
            return result;
        }
    }

    /**
     * 提交工单
     * @param auditProject
     * @return
     */
    public static String submit(AuditProject auditProject,String tableName,String itemcode) {
        IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IAuditProjectMaterial iAuditProjectMaterial = ContainerFactory.getContainInfo().getComponent(IAuditProjectMaterial.class);
        IAttachService attachService = (IAttachService)ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        ICxBusService cxBusService = ContainerFactory.getContainInfo().getComponent(ICxBusService.class);
        log.info("========== 开始调用提交工单接口 ==========");
        try{
            String zhongShanShuiWuUrl = ConfigUtil.getConfigValue("duijie","zhongShanShuiWuUrl");
            String requesturl = zhongShanShuiWuUrl + "/api/appService/submit";
            if (StringUtil.isBlank(zhongShanShuiWuUrl)) {
                log.info("未获取到系统参数zhongShanShuiWuUrl值");
                return "";
            }
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Content-Type", "application/x-www-form-urlencoded");
            JSONObject params = new JSONObject();
            // 固定值
            params.put("appuserno", "99999");
            params.put("flowcode", "projectgg");
            params.put("clientid", "GG");
            params.put("bustype", "1");
            params.put("approvaluser", "99999");
            //
            params.put("cardname", auditProject.getApplyername());
            params.put("address", auditProject.getAddress());
            params.put("linkman", auditProject.getContactperson());
            params.put("tel", auditProject.getContactphone());
            params.put("flowsn", auditProject.getFlowsn());

            // 表单字段
            // 用水电子表单表名：
            Record formDetail = cxBusService.getDzbdDetail(tableName, auditProject.getSubappguid());
            log.info("用水电子表单查询结果--->" + formDetail);
            // 工改项目	WorkReform	表单
            //项目编码	itemcode	文本
            //证件号码	credno	文本按钮
            //证件类型	credtype	下拉框

            //法人代表	corporative	文本
            //口径	caliber	下拉框
            //用水类别	watertype	下拉框
            //附件管理	photoFile	高拍仪
            //用户类型	usertype	下拉框
            params.put("itemcode", itemcode);
            if(formDetail!=null){
                params.put("credno", formDetail.getStr("field4")); //证件号码	credno
                //证件类型credtype TODO
                params.put("credtype", "");
                //备注	remark	文本
                params.put("remark", formDetail.getStr("field10"));
                //法人代表	corporative	文本
                params.put("corporative", formDetail.getStr("field8"));
                //口径	caliber	下拉框
                params.put("caliber", formDetail.getStr("field7"));
                //用水类别	watertype	下拉框
                params.put("watertype", formDetail.getStr("field6"));
                //用户类型 usertype
                params.put("usertype", formDetail.getStr("field2"));
            }

            // 附件
            params.put("columnname", "photoFile");
            JSONArray attachListArray = new JSONArray();
            log.info("根据projectGuid查询AuditProjectMaterial");
            List<AuditProjectMaterial> materialList = iAuditProjectMaterial
                    .selectProjectMaterial(auditProject.getRowguid()).getResult();
            log.info("查询出的materialList--->" + materialList);
            if (ValidateUtil.isNotBlankCollection(materialList)) {
                log.info("附件list不为空");
                for (AuditProjectMaterial auditProjectMaterial : materialList){

                    List<FrameAttachInfo> attachList = attachService.getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
                    log.info("查询出cliengguid为" + auditProjectMaterial.getCliengguid() +" 材料名称："+ auditProjectMaterial.getTaskmaterial() + "对应的附件list--->" + attachList);
                    if (ValidateUtil.isNotBlankCollection(attachList)) {
                        log.info("附件list不为空");
                        // TODO 这边不知道材料名称能不能对上
                        // TODO 这边先写死86
                        String filetype = "86";
                        if("不动产权证或规划许可证".equals(auditProjectMaterial.getTaskmaterial())){
                            JSONObject attach = new JSONObject();
                            JSONArray files = new JSONArray();
                            filetype = "86";
                            for (FrameAttachInfo frameAttachInfo : attachList){
                                JSONObject file = new JSONObject();
                                // 根据附件attachGuid获取附件流
                                InputStream content = attachService.getAttach(frameAttachInfo.getAttachGuid()).getContent();
                                String mimeType = getMimeTypeFromExtension(frameAttachInfo.getAttachFileName());
                                String filesuffix = getExtension(frameAttachInfo.getAttachFileName()).toLowerCase();
                                String filecontent = attachToBase64(content,mimeType);
                                file.put("filecontent", filecontent);
                                file.put("filename", frameAttachInfo.getAttachFileName());
                                file.put("filesuffix", filesuffix);
                                files.add(file);
                            }
                            attach.put("filetype", filetype);
                            attach.put("files", files);
                            attachListArray.add(attach);
                        }
                        else if("申报资料".equals(auditProjectMaterial.getTaskmaterial())){
                            JSONObject attach = new JSONObject();
                            JSONArray files = new JSONArray();
                            filetype = "86";
                            for (FrameAttachInfo frameAttachInfo : attachList){
                                JSONObject file = new JSONObject();
                                // 根据附件attachGuid获取附件流
                                InputStream content = attachService.getAttach(frameAttachInfo.getAttachGuid()).getContent();
                                String mimeType = GongYongShuiWuUtil.getMimeTypeFromExtension(frameAttachInfo.getAttachFileName());
                                String filesuffix = getExtension(frameAttachInfo.getAttachFileName()).toLowerCase();
                                String filecontent = attachToBase64(content,mimeType);
                                file.put("filecontent", filecontent);
                                file.put("filename", frameAttachInfo.getAttachFileName());
                                file.put("filesuffix", filesuffix);
                                files.add(file);
                            }
                            attach.put("filetype", filetype);
                            attach.put("files", files);
                            attachListArray.add(attach);
                        }
                        else if("营业执照".equals(auditProjectMaterial.getTaskmaterial())){
                            JSONObject attach = new JSONObject();
                            JSONArray files = new JSONArray();
                            filetype = "86";
                            for (FrameAttachInfo frameAttachInfo : attachList){
                                // 根据附件attachGuid获取附件流
                                JSONObject file = new JSONObject();
                                // 根据附件attachGuid获取附件流
                                InputStream content = attachService.getAttach(frameAttachInfo.getAttachGuid()).getContent();
                                String mimeType = GongYongShuiWuUtil.getMimeTypeFromExtension(frameAttachInfo.getAttachFileName());
                                String filesuffix = getExtension(frameAttachInfo.getAttachFileName()).toLowerCase();
                                String filecontent = attachToBase64(content,mimeType);
                                file.put("filecontent", filecontent);
                                file.put("filename", frameAttachInfo.getAttachFileName());
                                file.put("filesuffix", filesuffix);
                                files.add(file);
                            }
                            attach.put("filetype", filetype);
                            attach.put("files", files);
                            attachListArray.add(attach);
                        }
                        else if("法人身份证正反面".equals(auditProjectMaterial.getTaskmaterial())){
                            JSONObject attach = new JSONObject();
                            JSONArray files = new JSONArray();
                            filetype = "86";
                            for (FrameAttachInfo frameAttachInfo : attachList){
                                // 根据附件attachGuid获取附件流
                                JSONObject file = new JSONObject();
                                // 根据附件attachGuid获取附件流
                                InputStream content = attachService.getAttach(frameAttachInfo.getAttachGuid()).getContent();
                                String mimeType = GongYongShuiWuUtil.getMimeTypeFromExtension(frameAttachInfo.getAttachFileName());
                                String filesuffix = getExtension(frameAttachInfo.getAttachFileName()).toLowerCase();
                                String filecontent = attachToBase64(content,mimeType);
                                file.put("filecontent", filecontent);
                                file.put("filename", frameAttachInfo.getAttachFileName());
                                file.put("filesuffix", filesuffix);
                                files.add(file);
                            }
                            attach.put("filetype", filetype);
                            attach.put("files", files);
                            attachListArray.add(attach);
                        }
                        else if("相关文件".equals(auditProjectMaterial.getTaskmaterial())){
                            JSONObject attach = new JSONObject();
                            JSONArray files = new JSONArray();
                            filetype = "86";
                            for (FrameAttachInfo frameAttachInfo : attachList){
                                // 根据附件attachGuid获取附件流
                                JSONObject file = new JSONObject();
                                // 根据附件attachGuid获取附件流
                                InputStream content = attachService.getAttach(frameAttachInfo.getAttachGuid()).getContent();
                                String mimeType = GongYongShuiWuUtil.getMimeTypeFromExtension(frameAttachInfo.getAttachFileName());
                                String filesuffix = getExtension(frameAttachInfo.getAttachFileName()).toLowerCase();
                                String filecontent = attachToBase64(content,mimeType);
                                file.put("filecontent", filecontent);
                                file.put("filename", frameAttachInfo.getAttachFileName());
                                file.put("filesuffix", filesuffix);
                                files.add(file);
                            }
                            attach.put("filetype", filetype);
                            attach.put("files", files);
                            attachListArray.add(attach);
                        }
                    }
                }
            }
            if(attachListArray != null && attachListArray.size()>0){
                params.put("attachList", attachListArray);
            }
            String requestparam = "data="+params.toJSONString();
            log.info("========== 开始调用提交工单接口 ========== requesturl:"+requesturl+" params："+requestparam);
            String resultStr = HttpUtil.doHttp(requesturl, headerMap, requestparam, "post", HttpUtil.RTN_TYPE_STRING);
            log.info("接口返回值--->" + resultStr);

            if (StringUtil.isBlank(resultStr)) {
                log.info("未获取到接口返回值");
                return "";
            }

            JSONObject resultJson = JSON.parseObject(resultStr);
            // TODO 接口文档没有给出返回格式 需要调试
            if ("200".equals(resultJson.getString("code"))) {
                // 如果成功，获取单据编号进行保存
                String billNo = resultJson.getString("msg").split(",")[0];
                log.info("办件" + auditProject.getFlowsn() + "对应的单据编号为：" + billNo);
                auditProject.set("billno", billNo);
                auditProjectService.updateProject(auditProject); // 将单据编号保存到办件表中
                log.info("单据编号已保存");
                return billNo;
            }
            else {
                log.info("请求失败，" + resultJson.getString("msg"));
                return "";
            }
        }
        catch (Exception e){
            e.printStackTrace();
            log.info("调用提交工单接口异常");
            return "";
        }
    }

    /**
     * 附件转base64
     */
    public static String attachToBase64(InputStream content, String mimeType) {
        String fileBase64 = "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while (true) {
            try {
                if (!((bytesRead = content.read(buffer)) != -1)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            outputStream.write(buffer, 0, bytesRead);
        }
        byte[] bytes = outputStream.toByteArray();
        fileBase64 = Base64.getEncoder().encodeToString(bytes);
        return mimeType + ";base64," + fileBase64;
    }

    public static String getMimeTypeFromExtension(String fileName) {
        String extension = getExtension(fileName).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "pdf":
                return "application/pdf";
            case "txt":
                return "text/plain";
            case "doc":
            case "docx":
                return "application/msword";
            case "xls":
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            default:
                return "application/octet-stream";
        }
    }

    private static String getExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return ""; // No extension
        }
        return fileName.substring(lastDotIndex + 1);
    }
}
