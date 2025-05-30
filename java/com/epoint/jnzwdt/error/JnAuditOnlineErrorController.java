package com.epoint.jnzwdt.error;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditflowsn.handleflowsn.inter.IHandleFlowSn;
import com.epoint.basic.auditonlineuser.auditonlineerrorcorrection.domain.AuditOnlineErrorCorrection;
import com.epoint.basic.auditonlineuser.auditonlineerrorcorrection.inner.IAuditOnlineErrorCorrection;
import com.epoint.basic.auditonlineuser.auditonlinemessages.inter.IAuditOnlineMessages;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.faces.fileupload.MultipartRequest;
import com.epoint.common.util.AttachUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

/**
 *  网上办事大厅网站纠错相关接口
 *  
 * @author WST
 * @version [9.3, 2017年10月25日]
 */
@RestController
@RequestMapping("/jnzwdtError")
public class JnAuditOnlineErrorController
{

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 网上办事大厅注册用户API
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;

    /**
     * 部门API
     */
    @Autowired
    private IAuditOnlineErrorCorrection iAuditOnlineErrorCorrection;

    /**
     * 部门API
     */
    @Autowired
    private IOuService iOuService;

    /**
     * 附件API
     */
    @Autowired
    private IAttachService iAttachService;

    /**
     * 代码项API
     */
    @Autowired
    private ICodeItemsService iCodeItemsService;

    /**
     * 流水号api
     */
    @Autowired
    private IHandleFlowSn iHandleFlowSn;

    /**
     * 消息api
     */
    @Autowired
    private IAuditOnlineMessages iAuditOnlineMessages;

    /**
    
    /**
     *  新增纠错信息接口
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return    
     */
    @RequestMapping(value = "/addErrorCorrection", method = RequestMethod.POST)
    public String addErrorCorrection(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用addErrorCorrection接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取纠错信息内容
                String errorContent = obj.getString("errorcontent");
                // 1.2、获取纠错标题
                String title = obj.getString("title");
                // 1.3、获取提问者姓名
                String submitUserName = obj.getString("submitusername");
                // 1.4、获取手机号
                String submitMobile = obj.getString("submitmobile");
                // 1.5、获取错误页面地址
                String errorPageUrl = obj.getString("errorpageurl");
                // 1.5、获取错误页面地址
                String clientGuid = obj.getString("clientguid");
                // 1.6、 设置查询码6位随机数
                String searchCode = JsonUtils.bulidCheckCode(6);
                // 1.7、 设置信件编码
                String messageCode = iHandleFlowSn.getFlowsn("信件编码", "JC").getResult();
                // 1.8 获取用户信息  
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                String submitUserGuid = auditOnlineRegister == null ? "" : auditOnlineRegister.getAccountguid();
                // 1.9 获取用户信息
                String errorType = obj.getString("errortype");
                // 1.10 获取辖区
                String areaCode = obj.getString("areacode");
                // 2、新增纠错
                AuditOnlineErrorCorrection auditOnlineErrorCorrection = new AuditOnlineErrorCorrection();
                String errorGuid = UUID.randomUUID().toString();
                auditOnlineErrorCorrection.setRowguid(errorGuid);
                auditOnlineErrorCorrection.setSubmitdate(new Date());// 提交时间
                auditOnlineErrorCorrection.setSubmitmobile(submitMobile); // 提交人手机
                auditOnlineErrorCorrection.setSubmituserguid(submitUserGuid); // 提交人标识
                auditOnlineErrorCorrection.setSubmitusername(submitUserName); // 提交人名称
                auditOnlineErrorCorrection.setErrorpageurl(errorPageUrl); // 错误页面地址
                auditOnlineErrorCorrection.setErrorcontent(errorContent); // 纠错内容
                auditOnlineErrorCorrection.setSearchcode(searchCode); // 查询码
                auditOnlineErrorCorrection.setTitle(title); // 错误标题
                auditOnlineErrorCorrection.setErrortype(errorType); // 错误类别
                auditOnlineErrorCorrection.setClientguid(clientGuid); // 附件标识
                auditOnlineErrorCorrection.setMessagecode(messageCode); //信件编码
                auditOnlineErrorCorrection.setStatus(0); // 办件状态 默认为0
                auditOnlineErrorCorrection.setAreacode(areaCode); // 所属辖区
                iAuditOnlineErrorCorrection.addErrorCorrection(auditOnlineErrorCorrection);
                // 向指定用户发送待办
                String handleUrl = "/epointzwfw/auditonlineerrorcorrection/auditonlineerrorcorrectionedit?guid="
                        + errorGuid;
                title = "【纠错】" + title + "(" + submitUserName + ")";
                String rolename = "";
                if (areaCode.length() == 9) {
                    rolename = "乡镇中心管理员";
                }
                else if (areaCode.length() == 12) {
                    rolename = "村（社区）中心管理员";
                }
                else {
                    rolename = "中心管理员";
                }
                iAuditOnlineMessages.sendMsg(rolename, title, submitUserGuid, submitUserName, areaCode, handleUrl,
                        errorGuid, "zwfwMsgurl", null);
                // 3、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("messagecode", messageCode);
                dataJson.put("searchcode", searchCode);
                log.info("=======结束调用addErrorCorrection接口=======");
                return JsonUtils.zwdtRestReturn("1", "新增成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======addErrorCorrection接口参数：params【" + params + "】=======");
            log.info("=======addErrorCorrection异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "新增纠错信息失败：" + e.getMessage(), "");
        }
    }

    /**
     *  获取纠错信息列表接口
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return  
     */
    @RequestMapping(value = "/getMyErrorList", method = RequestMethod.POST)
    public String getMyErrorList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getMyErrorList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取当前页码
                String currentPage = obj.getString("currentpage");
                // 1.2、获取一页显示数量
                String pageSize = obj.getString("pagesize");
                // 1.3、获取查询关键字
                String keyWord = obj.getString("keyword");
                // 1.4、获取用户唯一标识
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                String accountGuid = auditOnlineRegister == null ? "" : auditOnlineRegister.getAccountguid();
                if (StringUtil.isNotBlank(accountGuid)) {
                    // 2、 定义查询条件
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(keyWord)) {
                        sqlConditionUtil.like("title", keyWord);
                    }
                    sqlConditionUtil.eq("submituserguid", accountGuid);
                    // 2.1、 查询当前用户下所有的纠错信息
                    PageData<AuditOnlineErrorCorrection> pageDataError = iAuditOnlineErrorCorrection.selectErrorByPage(
                            sqlConditionUtil.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                            Integer.parseInt(pageSize), "submitdate", "desc").getResult();
                    // 3、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    // 4、拼接返回的纠错信息列表
                    List<JSONObject> errorList = new ArrayList<JSONObject>();
                    List<AuditOnlineErrorCorrection> auditOnlineErrorCorrections = pageDataError.getList();
                    if (auditOnlineErrorCorrections != null && auditOnlineErrorCorrections.size() > 0) {
                        for (AuditOnlineErrorCorrection auditOnlineErrorCorrection : auditOnlineErrorCorrections) {
                            JSONObject errorJson = new JSONObject();
                            // 4.1、 获取处理部门
                            if (StringUtil.isNotBlank(auditOnlineErrorCorrection.getHandleouguid())) { //判断是否是部门还是中心
                                FrameOu frameOu = iOuService
                                        .getOuByOuGuid(auditOnlineErrorCorrection.getHandleouguid());
                                errorJson.put("ouname", frameOu == null ? "" : frameOu.getOuname());//回答部门
                            }
                            errorJson.put("errorguid", auditOnlineErrorCorrection.getRowguid()); // 纠错信息唯一标识
                            errorJson.put("title", auditOnlineErrorCorrection.getTitle()); // 标题
                            errorJson.put("submitdate", EpointDateUtil.convertDate2String(
                                    auditOnlineErrorCorrection.getSubmitdate(), EpointDateUtil.DATE_FORMAT)); // 提交日期
                            errorJson.put("status", auditOnlineErrorCorrection.getStatus()); // 纠错处理状态
                            errorJson.put("handledate", EpointDateUtil.convertDate2String(
                                    auditOnlineErrorCorrection.getHandledate(), EpointDateUtil.DATE_FORMAT)); // 处理日期
                            errorJson.put("messagecode", auditOnlineErrorCorrection.getMessagecode()); // 信件编码
                            errorList.add(errorJson);
                        }
                        dataJson.put("totalcount", pageDataError.getRowCount());
                        dataJson.put("errorlist", errorList);
                    }
                    log.info("=======结束调用getMyErrorList接口=======");
                    return JsonUtils.zwdtRestReturn("1", "查询纠错列表成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getMyErrorList接口参数：params【" + params + "】=======");
            log.info("=======getMyErrorList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "查询纠错列表失败：" + e.getMessage(), "");
        }
    }

    /**
     *  获取纠错详细信息
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return  
     */
    @RequestMapping(value = "/getErrorDetailByCode", method = RequestMethod.POST)
    public String getErrorDetailByCode(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getErrorDetailByCode接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取查询码
                String searchCode = obj.getString("searchcode");
                // 1.2、获取信件编码
                String messageCode = obj.getString("messagecode");
                // 1.3、获取纠错信息唯一标识
                String errorCorrectionGuid = obj.getString("errorcorrectionguid");
                // 2.1、 errorGuid不为空标识我的空间点击纠错列表中纠错信息的进行查询
                AuditOnlineErrorCorrection auditOnlineErrorCorrection;
                if (StringUtil.isNotBlank(errorCorrectionGuid)) {
                    auditOnlineErrorCorrection = iAuditOnlineErrorCorrection
                            .getErrorCorrectionByGuid(errorCorrectionGuid).getResult();
                }
                else {
                    // 2.2、 有信件码则根据信件码以及查询码共同查询,否则根据查询码单独查询
                    auditOnlineErrorCorrection = iAuditOnlineErrorCorrection
                            .getErrorCorrectionByCode(messageCode, searchCode).getResult();
                }
                // 3、 查询纠错信息
                JSONObject errorJson = new JSONObject();
                if (auditOnlineErrorCorrection != null) {
                    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    // 获取用户信息
                    AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                    if (auditOnlineRegister != null) {
                        if (StringUtil.isNotBlank(auditOnlineErrorCorrection.getSubmituserguid())) {
                            if (!auditOnlineErrorCorrection.getSubmituserguid()
                                    .equals(auditOnlineRegister.getAccountguid())) {
                                return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                            }
                        }
                    }
                    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    // 3.1、返回纠错详细信息
                    if (StringUtil.isNotBlank(auditOnlineErrorCorrection.getHandleouguid())) {
                        FrameOu frameOu = iOuService.getOuByOuGuid(auditOnlineErrorCorrection.getHandleouguid());
                        errorJson.put("ouname", frameOu == null ? "" : frameOu.getOuname());//处理部门
                    }
                    // 3.2、 获取附件
                    String clientGuid = auditOnlineErrorCorrection.getClientguid();
                    if (StringUtil.isNotBlank(clientGuid)) {
                        List<FrameAttachInfo> frameAttachInfos = iAttachService.getAttachInfoListByGuid(clientGuid);
                        List<JSONObject> attachList = new ArrayList<JSONObject>();
                        for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
                            JSONObject attachJson = new JSONObject();
                            String imgsrc = WebUtil.getRequestCompleteUrl(request)
                                    + "/rest/auditattach/readAttach?attachguid=" + frameAttachInfo.getAttachGuid();
                            attachJson.put("attachlength", frameAttachInfo.getAttachLength());
                            attachJson.put("attachguid", frameAttachInfo.getAttachGuid());
                            attachJson.put("attchname", frameAttachInfo.getAttachFileName());
                            attachJson.put("imgsrc", imgsrc);
                            attachList.add(attachJson);
                        }
                        errorJson.put("clientguid", clientGuid);
                        errorJson.put("attachcount", attachList.size());
                        errorJson.put("attachlist", attachList);
                    }
                    // 3.3获取纠错分类       
                    String errortype = "";
                    if (StringUtil.isNotBlank(auditOnlineErrorCorrection.getErrortype())) {
                        String[] types = auditOnlineErrorCorrection.getErrortype().split(";");
                        for (int i = 0; i < types.length; i++) {
                            errortype += iCodeItemsService.getItemTextByCodeName("纠错分类", types[i]) + ";";
                        }
                    }
                    errorJson.put("errorguid", auditOnlineErrorCorrection.getRowguid()); // 纠错信息唯一标识
                    errorJson.put("submitusername", auditOnlineErrorCorrection.getSubmitusername()); // 提交人姓名
                    errorJson.put("title", auditOnlineErrorCorrection.getTitle()); // 标题
                    errorJson.put("submitdate", EpointDateUtil.convertDate2String(
                            auditOnlineErrorCorrection.getSubmitdate(), EpointDateUtil.DATE_TIME_FORMAT)); // 提交日期
                    errorJson.put("errorcontent", auditOnlineErrorCorrection.getErrorcontent()); // 纠错内容
                    errorJson.put("messagecode", auditOnlineErrorCorrection.getMessagecode()); // 信件编码
                    errorJson.put("status", auditOnlineErrorCorrection.getStatus()); // 纠错状态
                    errorJson.put("handlecontent", auditOnlineErrorCorrection.getHandlecontent()); // 纠错处理内容
                    errorJson.put("handdate", EpointDateUtil.convertDate2String(
                            auditOnlineErrorCorrection.getHandledate(), EpointDateUtil.DATE_TIME_FORMAT)); // 纠错处理日期
                    errorJson.put("errortype", errortype); // 纠错类别
                    errorJson.put("errorpageurl", auditOnlineErrorCorrection.getErrorpageurl()); // 纠错类别
                }
                else {
                    log.info("=======结束调用getErrorDetailByCode接口=======");
                    return JsonUtils.zwdtRestReturn("0", "未查询到纠错信息,请确认您的查询信息是否正确!", "");
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("errordetail", errorJson);
                log.info("=======结束调用getErrorDetailByCode接口=======");
                return JsonUtils.zwdtRestReturn("1", "查询纠错详情成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======addErrorCorrection接口参数：params【" + params + "】=======");
            log.info("=======addErrorCorrection异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "查询纠错详情失败：" + e.getMessage(), "");
        }
    }

    /**
     * 附件上传接口
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return  
     */
    @RequestMapping(value = "/correctionAttachUpload", method = RequestMethod.POST)
    public String correctionAttachUpload(HttpServletRequest request) {
        try {
            log.info("=======开始调用correctionAttachUpload接口=======");
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
                if(!attachName.endsWith(".jpg") && !attachName.endsWith(".jpeg") && !attachName.endsWith(".png")
                        && !attachName.endsWith(".gif") && !attachName.endsWith(".bmp")){
                    return JsonUtils.zwdtRestReturn("0", "上传文件格式不正确", "");
                }
                clientGuid = request.getParameter("clientguid");
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
            String accountguid = "";
            if (auditOnlineRegister != null) {
                accountguid = auditOnlineRegister.getAccountguid();
            }
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
                dataJson.put("attachguid", frameAttachInfo.getAttachGuid());
                //获取请求地址
                String urlroot = JsonUtils.getRootUrl(request);
                dataJson.put("attachsrc",
                        urlroot + "rest/auditattach/readAttach?attachguid=" + frameAttachInfo.getAttachGuid());
                log.info("=======结束调用correctionAttachUpload接口=======");
                return JsonUtils.zwdtRestReturn("1", "上传成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "上传文件未完成", "");
            }
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "上传失败：" + e.getMessage(), "");
        }
    }

    /**
     * 附件删除接口
     * 
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return  
     */
    @RequestMapping(value = "/correctionAttachDelete", method = RequestMethod.POST)
    public String correctionAttachDelete(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用correctionAttachDelete接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String attachguid = obj.getString("attachguid");
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 1、获取附件信息
                FrameAttachInfo frameAttachInfo = iAttachService.getAttachInfoDetail(attachguid);
                if (frameAttachInfo == null) {
                    return JsonUtils.zwdtRestReturn("0", "未获取到附件信息！", "");
                }
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                // 2.1、用户未登录下进行删除操作，只可以删除用户未登录时上传的附件
                if (auditOnlineRegister == null) {
                    if (StringUtil.isBlank(frameAttachInfo.getUploadUserGuid())) {
                        iAttachService.deleteAttachByAttachGuid(attachguid);
                        log.info("=======结束调用correctionAttachDelete接口=======");
                        return JsonUtils.zwdtRestReturn("1", "删除成功", "");
                    }
                    else {
                        log.info("=======结束调用correctionAttachDelete接口=======");
                        return JsonUtils.zwdtRestReturn("0", "用户身份验证失败！", "");
                    }
                }
                // 2.2、用户登录情况下，只可以删除用户自己上传的附件
                else {
                    if ((StringUtil.isBlank(frameAttachInfo.getUploadUserGuid()))
                            || (auditOnlineRegister.getAccountguid().equals(frameAttachInfo.getUploadUserGuid()))) {
                        iAttachService.deleteAttachByAttachGuid(attachguid);
                        log.info("=======结束调用correctionAttachDelete接口=======");
                        return JsonUtils.zwdtRestReturn("1", "删除成功", "");
                    }
                    else {
                        log.info("=======结束调用correctionAttachDelete接口=======");
                        return JsonUtils.zwdtRestReturn("0", "用户身份验证失败！", "");
                    }
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
