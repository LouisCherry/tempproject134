package com.epoint.zwfw.apprest.qjrestta;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.app.auditattachapp.api.IAuditAttachAppService;
import com.epoint.app.auditattachapp.api.entity.AuditAttachApp;
import com.epoint.basic.faces.fileupload.MultipartRequest;
import com.epoint.common.util.AttachUtil;

@RestController
@RequestMapping("/WebDiskServerDemo")
public class PostFileController
{
    /**
     * 日志
     */
    private static final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 最大文件size
     */
    private static final Long MAX_SIZE = 1024 * 1024 * 50L;

    /**
     *errocode1
     */
    private static final String ERROR_CODE1 = "0001";
    /**
     *errocode2
     */
    private static final String ERROR_CODE2 = "0002";

    /**
     *成功code
     */
    private static final String SUCCESS_CODE = "0000";

    @Autowired
    private IAuditAttachAppService iAuditAttachAppService;

    /**
     *  办件初始化（办件申报页面初始化调用）
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return 
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String initProject(HttpServletRequest request) {
        JSONObject returnValue = new JSONObject();
        try {
            log.debug("=======开始调用attachUpload接口=======");
            // 从multipartRequest获取POST的流文件并保存到数据库
            String attachName = "";
            String clientGuid = "";
            //总文件大小
            long size = 0;
            String folder_name = "";
            String type = "";
            AuditAttachApp attachApp = null;
            Map<String, List<FileItem>> fileMaps = null;
            MultipartRequest multipartRequest = null;
            if (!(request instanceof MultipartRequest)) {
                returnValue.put("msg", "文件未获取");
                return returnValue.toString();
            }
            multipartRequest = (MultipartRequest) request;

            String uid = multipartRequest.getParameter("uid");
            // 文件名称
            folder_name = multipartRequest.getParameter("folder_name");
            type = multipartRequest.getParameter("type");
            attachApp = iAuditAttachAppService.findByAppkey(uid);
            if (attachApp == null) {
                returnValue.put("code", ERROR_CODE2);
                returnValue.put("msg", "uid不存在");
                return returnValue.toString();
            }
            if (attachApp.getIs_enable() == null && attachApp.getIs_enable() == 0) {
                returnValue.put("code", ERROR_CODE2);
                returnValue.put("msg", "未启用或启用状态为null");
                return returnValue.toString();
            }
            clientGuid = UUID.randomUUID().toString();
            // 文件内容
            fileMaps = multipartRequest.getFileParams();
            // 获取用户信息
//            String attachGuid = UUID.randomUUID().toString();
            String attachGuid = (System.currentTimeMillis()+"").substring(2);
            if (fileMaps == null) {
                returnValue.put("code", ERROR_CODE2);
                returnValue.put("msg", "无附件信息");
                return returnValue.toString();
            }
            // 获取文件内容
            List<FileItem> fileItems = fileMaps.get("file");
            FileItem tempFileItem = null;
            for (FileItem fileItem : fileItems) {
                if ("file".equals(fileItem.getFieldName())) {
                    tempFileItem = fileItem;
                    attachName = tempFileItem.getName();
                    size = tempFileItem.getSize();
                }
            }
            if (size > MAX_SIZE) {
                returnValue.put("code", ERROR_CODE2);
                returnValue.put("msg", "您上传的文件大小已超出网盘系统限制。");
                return returnValue.toString();
            }
            //文件未分片
            AttachUtil.saveOnlineAttach((MultipartRequest) request, attachName, clientGuid, attachGuid, "", null, null);
            log.debug("=======结束调用attachUpload接口=======");
            returnValue.put("code", SUCCESS_CODE);
            returnValue.put("msg", "OK");
            returnValue.put("uuid", attachGuid);
            returnValue.put("docid", attachGuid);
            return returnValue.toString();

        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            returnValue.put("code", ERROR_CODE1);
            returnValue.put("msg", "系统异常" + e.getMessage());
            return returnValue.toString();
        }
    }

    /**
     *  办件初始化（办件申报页面初始化调用）
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return 
     */
    @RequestMapping(value = "/gdupload", method = RequestMethod.POST)
    public String gdUpload(HttpServletRequest request) {
        JSONObject returnValue = new JSONObject();
        try {
            log.debug("=======开始调用gdupload接口=======");
            // 从multipartRequest获取POST的流文件并保存到数据库
            String attachName = "";
            String clientGuid = "";
            //总文件大小
            long size = 0;
            String folder_name = "";
            String type = "";
            AuditAttachApp attachApp = null;
            Map<String, List<FileItem>> fileMaps = null;
            MultipartRequest multipartRequest = null;
            if (!(request instanceof MultipartRequest)) {
                returnValue.put("msg", "文件未获取");
                return returnValue.toString();
            }
            multipartRequest = (MultipartRequest) request;

            String uid = "201907022043";
            // 文件名称
            folder_name = "taiangd";
            type = "doc";
            attachApp = iAuditAttachAppService.findByAppkey(uid);
            if (attachApp == null) {
                returnValue.put("code", ERROR_CODE2);
                returnValue.put("msg", "uid不存在");
                return returnValue.toString();
            }
            if (attachApp.getIs_enable() == null && attachApp.getIs_enable() == 0) {
                returnValue.put("code", ERROR_CODE2);
                returnValue.put("msg", "未启用或启用状态为null");
                return returnValue.toString();
            }
            clientGuid = UUID.randomUUID().toString();
            // 文件内容
            fileMaps = multipartRequest.getFileParams();
            // 获取用户信息
            String attachGuid = UUID.randomUUID().toString();
            if (fileMaps == null) {
                returnValue.put("code", ERROR_CODE2);
                returnValue.put("msg", "无附件信息");
                return returnValue.toString();
            }
            // 获取文件内容
            List<FileItem> fileItems = fileMaps.get("file");
            FileItem tempFileItem = null;
            for (FileItem fileItem : fileItems) {
                if ("file".equals(fileItem.getFieldName())) {
                    tempFileItem = fileItem;
                    attachName = tempFileItem.getName();
                    size = tempFileItem.getSize();
                }
            }
            if (size > MAX_SIZE) {
                returnValue.put("code", ERROR_CODE2);
                returnValue.put("msg", "您上传的文件大小已超出网盘系统限制。");
                return returnValue.toString();
            }
            //文件未分片
            AttachUtil.saveOnlineAttach((MultipartRequest) request, attachName, clientGuid, attachGuid, "", null, null);
            log.debug("=======结束调用gdupload接口=======");
            returnValue.put("code", SUCCESS_CODE);
            returnValue.put("msg", "OK");
            returnValue.put("uuid", attachGuid);
            returnValue.put("docid", attachGuid);
            return returnValue.toString();

        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            returnValue.put("code", ERROR_CODE1);
            returnValue.put("msg", "系统异常" + e.getMessage());
            return returnValue.toString();
        }
    }

}
