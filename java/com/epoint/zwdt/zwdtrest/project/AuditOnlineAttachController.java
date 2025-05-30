package com.epoint.zwdt.zwdtrest.project;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.faces.fileupload.MultipartRequest;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.common.util.AttachUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.security.crypto.ParamEncryptUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping("/zwdtAttach")
public class AuditOnlineAttachController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     *用户
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;
    /**
     * 附件API
     */
    @Autowired
    private IAttachService iAttachService;
    /**
     * 证照库附件API
     */
    @Autowired
    private ICertAttachExternal iCertAttachExternal;

    @Autowired
    private IConfigService iConfigService;
    
    /**
     * 附件上传接口
     *  
     * @params params
     * @return
     */
    @RequestMapping(value = "/private/attachUpload", method = RequestMethod.POST)
    public String attachUpload(HttpServletRequest request) {
        try {
            log.info("=======开始调用attachUpload接口=======");
            // 从multipartRequest获取POST的流文件并保存到数据库
            String attachName = "";
            String clientGuid = "";
            //分片数
            int chunks = 1;
            //当前剩余片数
            int chunk = 0;
            int size = 0; //总文件大小
            // 文件上传标识
            String fileId = "";
            String source = "";
            Map<String, List<FileItem>> fileMaps = null;
            MultipartRequest multipartRequest = null;
            if (request instanceof MultipartRequest) {
                multipartRequest = (MultipartRequest) request;
                attachName = multipartRequest.getParameter("name"); // 文件名称
                clientGuid = request.getParameter("clientguid");
               if(StringUtil.isBlank(clientGuid)) {
                	clientGuid = WebUtil.getRequestParameter(request, "clientguid");
                }

               if(StringUtil.isBlank(clientGuid)) {
            	   return JsonUtils.zwdtRestReturn("0", "clientGuid不能为空", "");
               }
               
                source = request.getParameter("source"); // 上传来源
                chunk = request.getParameter("chunk") != null ? Integer.parseInt(request.getParameter("chunk")) : 0; //当前剩余分片数
                chunks = request.getParameter("chunks") != null ? Integer.parseInt(request.getParameter("chunks")) : 1; // 总分片数
                size = NumberUtils.toInt(request.getParameter("size")); // 文件大小
                fileId = request.getParameter("id"); // 上传文件标识
                fileMaps = multipartRequest.getFileParams(); // 文件内容
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "文件未获取", "");
            }
            // 获取用户信息
            AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
            String accountguid = auditOnlineRegister.getAccountguid();
            String attachGuid = UUID.randomUUID().toString();
            FrameAttachInfo frameAttachInfo = null;
            FileItem tempFileItem = null;
            for (Map.Entry<String, List<FileItem>> entry : fileMaps.entrySet()) {
                FileItem file = entry.getValue().get(0);
                tempFileItem = file;
            }

            //判断文件是否进行了分片
            if (chunks <= 1) {
                //文件未分片
                frameAttachInfo = AttachUtil.saveOnlineAttach((MultipartRequest) request, attachName, clientGuid,
                        attachGuid, accountguid, null, source);
            }
            else {
                frameAttachInfo = AttachUtil.saveSliceAttach(tempFileItem, size, clientGuid, attachName, attachGuid,
                        accountguid, fileId, source, chunk, chunks);
            }
            // 定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            if (frameAttachInfo != null) {
                //上传最终完成（考虑文件分片情况）
                OAuthCheckTokenInfo oAuthCheckTokenInfo = CheckTokenUtil.getCheckTokenInfo(request);
                String sysLoginId = "";
                if (oAuthCheckTokenInfo != null) {
                    sysLoginId = oAuthCheckTokenInfo.getLoginid();
                }
                String frameAttachGuid = ParamEncryptUtil.encrypt(frameAttachInfo.getAttachGuid(),
                        sysLoginId, null);
                dataJson.put("attachguid", frameAttachInfo.getAttachGuid()); // 加密后的附件标识
                String urlroot = JsonUtils.getRootUrl(request);
                dataJson.put("attachsrc",
                        urlroot + "rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=" + frameAttachGuid);// 加密后的附件标识下载地址
                log.info("=======结束调用attachUpload接口=======");
                return JsonUtils.zwdtRestReturn("1", "上传成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "上传文件未完成", "");
            }
        }
        catch (Exception e) {
        	e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "上传失败：" + e.getMessage(), "");
        }
    }
    
    /**
     * 
     *  上传审图附件
     *  @param params
     * @return 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/addAttach", method = RequestMethod.POST)
    public String getOuInfo(@RequestBody String params) {
        JSONObject rtnjson = new JSONObject();
        try {
            JSONObject jsonparams = JSON.parseObject(params);
            JSONObject dataJson = new JSONObject();
            if (jsonparams != null) {
                JSONObject param = jsonparams.getJSONObject("params");
                String clientguid = param.getString("clientguid");
                String attachfilename = param.getString("attachfilename");
                String attachcontent = param.getString("attachcontent"); 
                String ContentType = param.getString("contenttype"); 
                byte[] pic = Base64Util.decodeBuffer(attachcontent);
                String attachGuid = UUID.randomUUID().toString();
                
                if (pic.length > 0) {
                    FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                    frameAttachInfo.setAttachGuid(attachGuid);
                    frameAttachInfo.setCliengGuid(clientguid);
                    frameAttachInfo.setAttachFileName(attachfilename);
                    frameAttachInfo.setCliengTag("爱山东上传文件");
                    frameAttachInfo.setUploadUserGuid("");
                    frameAttachInfo.setUploadUserDisplayName("");
                    frameAttachInfo.setUploadDateTime(new Date());
                    frameAttachInfo.setContentType(ContentType);
                    frameAttachInfo.setAttachLength(Long.valueOf((long) pic.length));
                    ByteArrayInputStream input = new ByteArrayInputStream(pic);
                    iAttachService.addAttach(frameAttachInfo, input);
                    input.close();
                }

                dataJson.put("attachguid", attachGuid);
//                dataJson.put("attachsrc","http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=" + attachGuid);// 加密后的附件标识下载地址
                return JsonUtils.zwdtRestReturn("1", "插入附件成功", dataJson.toString());
            }
        }
        catch (Exception e) {
            log.info("【调用addAttach异常】" + params);
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "插入附件失败！", e.getMessage());
        }
        return JsonUtils.zwdtRestReturn("0", "插入附件失败！", "params：" + params + "rtnjson：" + rtnjson.toJSONString());
    }
    

    /**
     * 附件列表获取接口
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/getAttachList", method = RequestMethod.POST)
    public String getConsultNum(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getAttachList接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String clientGuid = obj.getString("clientguid");
            List<FrameAttachInfo> frameAttachInfos = iAttachService.getAttachInfoListByGuid(clientGuid);
            List<JSONObject> attachList = new ArrayList<JSONObject>();
            for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
                JSONObject attachJson = new JSONObject();
                OAuthCheckTokenInfo oAuthCheckTokenInfo = CheckTokenUtil.getCheckTokenInfo(request);
                String sysLoginId = "";
                if (oAuthCheckTokenInfo != null) {
                    sysLoginId = oAuthCheckTokenInfo.getLoginid();
                }
                String attachGuid = ParamEncryptUtil.encrypt(frameAttachInfo.getAttachGuid(),
                        sysLoginId, null);
                attachJson.put("attachguid1", frameAttachInfo.getAttachGuid());
                attachJson.put("contenttype", frameAttachInfo.getContentType());
                attachJson.put("attachguid", attachGuid);                
                attachJson.put("attachfilename", frameAttachInfo.getAttachFileName());
                attachJson.put("filepath", frameAttachInfo.getFilePath());
                attachList.add(attachJson);
            }
            // 定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            dataJson.put("attachList", attachList);
            log.info("=======结束调用getAttachList接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取附件列表成功", dataJson.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取附件列表失败：" + e.getMessage(), "");
        }
    }

    /**
     *  获取附件标识
     *  @param params
     *  @return    
     */
    @RequestMapping(value = "/getAttachGuidByClientGuid", method = RequestMethod.POST)
    public String getAttachGuidByClientGuid(@RequestBody String params) {
        try {
            log.info("=======开始调用getAttachGuidByClientGuid接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String clientGuid = obj.getString("clientguid");
                JSONObject dataJson = new JSONObject();
                String attachGuid = "";
                String attachName = "";
                if (StringUtil.isNotBlank(clientGuid)) {
                    List<FrameAttachInfo> frameAttachInfos = iAttachService.getAttachInfoListByGuid(clientGuid);
                    if (frameAttachInfos != null && frameAttachInfos.size() > 0) {
                        attachGuid = frameAttachInfos.get(0).getAttachGuid();
                        attachName = frameAttachInfos.get(0).getAttachFileName();
                        dataJson.put("attachguid", attachGuid);
                        dataJson.put("attachname", attachName);
                    }
                }
                log.info("=======结束调用getAttachGuidByClientGuid接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取附件标识成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取附件标识失败:" + e.getMessage(), "");
        }
    }

    /**
     * 附件删除接口
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/private/attachDelete", method = RequestMethod.POST)
    public String attachDelete(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用attachDelete接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String attachguid = obj.getString("attachguid");
                attachguid = ParamEncryptUtil.decrypt(attachguid);
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister == null) {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
                // 3、获取附件信息
                FrameAttachInfo frameAttachInfo = iAttachService.getAttachInfoDetail(attachguid);
                if (frameAttachInfo == null) {
                    return JsonUtils.zwdtRestReturn("0", "未获取到附件信息！", "");
                }
                // 3.1、判断附件上传人员和当前登录人员是否一致
                // userguid为空代表是引用证照的情况
                // userguid与用户表保持一致说明是本地上传或者云盘或者引用历史材料或者中介成果的情况
                // userguid为“扫码上传”说明是手机端扫码上传的情况
                if ((StringUtil.isBlank(frameAttachInfo.getUploadUserGuid()))
                        || (auditOnlineRegister.getAccountguid().equals(frameAttachInfo.getUploadUserGuid()))
                        || ("扫码上传".equals(frameAttachInfo.getUploadUserGuid())) || "监管事项材料复制".equals(frameAttachInfo.getCliengTag())) {
                    iAttachService.deleteAttachByAttachGuid(attachguid);
                    log.info("=======结束调用attachDelete接口=======");
                    return JsonUtils.zwdtRestReturn("1", "删除成功", "");
                }
                else {
                    log.info("=======结束调用attachDelete接口=======");
                    return JsonUtils.zwdtRestReturn("0", "用户身份验证失败！", "");
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "删除失败：" + e.getMessage(), "");
        }
    }

    /**
     * 附件下载(证照库用)
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/readCertAttach")
    public void readAttach(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        byte[] result = null;
        // 新的range
        String contentrange = "";
        // 附件总长度
        Integer fulllength = 0;
        String filename = "";
        String attachGuid = request.getParameter("attachguid");
        // 获取Range
        String range = request.getHeader("Range");
        InputStream inputStream = iCertAttachExternal.getAttach(attachGuid, ZwdtConstant.CERTAPPKEY);
        FrameAttachInfo attachInfo = iCertAttachExternal.getAttachByAttachGuid(attachGuid, ZwdtConstant.CERTAPPKEY);
        filename = attachInfo.getAttachFileName();
        result = FileManagerUtil.getContentFromInputStream(inputStream);
        fulllength = result.length;
        if (StringUtil.isNotBlank(range)) {
            String[] ranges = range.split("=");
            String[] indexs = ranges[1].split("-");
            int begin = Integer.parseInt(indexs[0]);
            int end;
            if (indexs.length > 1) {
                end = Integer.parseInt(indexs[1]);
                result = subBytes(result, begin, end);
                begin = end;
            }
            else {
                end = fulllength - 1;
                result = subBytes(result, begin, end);
            }

            if (range.endsWith("-")) {
                contentrange = range + (fulllength - 1) + "/" + fulllength;
            }
            else {
                contentrange = range + "/" + fulllength;
            }
        }
        else {
            contentrange = "bytes " + 0 + "-" + (fulllength - 1) + "/" + fulllength;
        }
        response.setContentType("application/force-download");// 设置强制下载不打开
        response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(filename, "UTF-8"));// 设置文件名
        response.addHeader("Content-Range", contentrange);
        response.addHeader("Content-Length", fulllength + "");
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            os.write(result, 0, result.length);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (os != null) {
                try {
                    os.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 附件删除接口(证照库用)
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/private/certAttachDelete", method = RequestMethod.POST)
    public String certAttachDelete(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用certAttachDelete接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String attachguid = obj.getString("attachguid");
                String asUseCertrest = iConfigService.getFrameConfigValue("AS_USE_CERTREST");
                // 删除本地库中的附件
                if(ZwfwConstant.CONSTANT_STR_ONE.equals(asUseCertrest)){
                    iAttachService.deleteAttachByAttachGuid(attachguid);
                }
                iCertAttachExternal.deleteAttach(attachguid, ZwdtConstant.CERTAPPKEY);
                log.info("=======结束调用certAttachDelete接口=======");
                return JsonUtils.zwdtRestReturn("1", "删除成功！", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "删除失败：" + e.getMessage(), "");
        }
    }

    /*****************************演示用的刻章接口******************************************/
    /**
     * 附件上传接口(电子签章用)
     *  
     * @param request
     * @return
     */
    @RequestMapping(value = "/attachUploadSignature", method = RequestMethod.POST)
    public String attachUploadSignature(HttpServletRequest request) {
        try {
            log.info("=======开始调用attachUploadSignature接口=======");
            String parameter = "";//拼接attachguid和cliengguid
            String attachName = "";//附件名称
            String clientGuid = "";//附件标识
            String oldattachguid = "";//老的附件下载标识
            String source = "";//附件来源
            String accountguid = "";
            FrameAttachInfo old = null;
            MultipartRequest multipartRequest = null;
            if (request instanceof MultipartRequest) {
                multipartRequest = (MultipartRequest) request;
                parameter = multipartRequest.getParameter("FileCode"); // 获取拼接的attachguid和cliengguid参数
                oldattachguid = parameter.split("&")[0].split("=")[1].toString();// 为了将它删除先进行保存（老的附件下载标识）
                clientGuid = parameter.split("&")[1].split("=")[1].toString(); // 附加标识
                source = multipartRequest.getParameter("source"); // 上传来源
                // 获取用户信息
                old = iAttachService.getAttachInfoDetail(oldattachguid);
                attachName = old.getAttachFileName();
                // 附件经过签章后默认都为pdf文件
                String partName = attachName.substring(0, attachName.lastIndexOf("."));
                attachName = partName + ".pdf";
                Map<String, List<FileItem>> map = multipartRequest.getFileParams();
                for (Map.Entry<String, List<FileItem>> en : map.entrySet()) {
                    List<FileItem> fileItems = en.getValue();
                    if (fileItems != null && !fileItems.isEmpty()) {
                        for (FileItem fileItem : fileItems) {
                            if (!fileItem.isFormField()) {// 是文件流而不是表单数据
                                String fileName;
                                // 从文件流中获取文件名
                                if (StringUtil.isNotBlank(attachName)) {
                                    fileName = attachName;
                                }
                                else {
                                    fileName = fileItem.getName();
                                }
                                int index = fileName.lastIndexOf("\\");
                                fileName = fileName.substring(++index);
                            }
                        }
                    }
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "file not found", "");
            }
            // 删除老的附件
            iAttachService.deleteAttach(old);
            String attachGuid = UUID.randomUUID().toString();
            FrameAttachInfo frameAttachInfo = null;
            //保存新的附件
            accountguid = old.getUploadUserGuid();
            source = old.getCliengTag();
            frameAttachInfo = AttachUtil.saveOnlineAttach((MultipartRequest) request, attachName, clientGuid,
                    attachGuid, accountguid, null, source);
            // 定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            if (frameAttachInfo != null) {
                dataJson.put("attachguid", frameAttachInfo.getAttachGuid());
                //获取请求地址
                String urlroot = JsonUtils.getRootUrl(request);
                dataJson.put("attachsrc",
                        urlroot + "rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=" + frameAttachInfo.getAttachGuid());
                log.info("=======结束调用attachUploadSignature接口=======");
                return JsonUtils.zwdtRestReturn("1", "file upload success!", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "file upload fail!", "");
            }
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "上传file upload fail:" + e.getMessage(), "");
        }
    }
    
    private byte[] subBytes(byte[] src, int begin, int end) {
        byte[] bs = new byte[end + 1 - begin];
        for (int i = begin; i <= end; i++) {
            bs[i - begin] = src[i];
        }
        return bs;
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
}
