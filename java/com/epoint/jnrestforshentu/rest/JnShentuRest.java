package com.epoint.jnrestforshentu.rest;

import java.io.ByteArrayInputStream;
import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.faces.fileupload.MultipartRequest;
import com.epoint.common.util.AttachUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.jnrestforshentu.api.IJnShentuService;
import com.epoint.jnrestforshentu.api.entity.AuditThreeFirst;

@RestController
@RequestMapping("/jnts")
public class JnShentuRest
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    private IJnShentuService service;
    @Autowired
    private IAttachService attachService;
    
    /**
     * 
     *  获取业务信息接口
     *  @param params
     * @return 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getTsProjectInfo", method = RequestMethod.POST)
    public String getProjectInfo(@RequestBody String params) {
        JSONObject rtnjson = new JSONObject();
        try {
            JSONObject jsonparams = JSON.parseObject(params);
            if (jsonparams != null) {
                JSONObject param = jsonparams.getJSONObject("params");
                String flowsn = param.getString("itemcode");
                String name = param.getString("itemname");
                String creditcode = param.getString("creditcode");
                JSONObject dataJson = new JSONObject();
                if (StringUtil.isBlank(flowsn) && StringUtil.isBlank(creditcode)) {
                    return JsonUtils.zwdtRestReturn("0", "查询条件都为空",dataJson);
                }else {
                    List<AuditRsItemBaseinfo> project = service.getTsProjectInfo(flowsn, name, creditcode);
                    dataJson.put("data", project);
                    return JsonUtils.zwdtRestReturn("1", "接口调用成功", dataJson);
                }
            }
        }
        catch (Exception e) {
            log.info("【济宁评价办件信息结束调用getProjectInfo异常】" + params);
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "接口调用报错", e.getMessage());
        }
        return JsonUtils.zwdtRestReturn("0", "接口调用失败", "params：" + params + "rtnjson：" + rtnjson.toJSONString());
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
    @RequestMapping(value = "/addTsAttach", method = RequestMethod.POST)
    public String getOuInfo(@RequestBody String params) {
        JSONObject rtnjson = new JSONObject();
        try {
            JSONObject jsonparams = JSON.parseObject(params);
            JSONObject dataJson = new JSONObject();
            if (jsonparams != null) {
                JSONObject param = jsonparams.getJSONObject("params");
                String ywguid = param.getString("ywguid");
                String attachfilename = param.getString("attachfilename");
                String attachcontent = param.getString("attachcontent"); 
                byte[] pic = Base64Util.decodeBuffer(attachcontent);
                String attachGuid = UUID.randomUUID().toString();
                
                if (pic.length > 0) {
                    FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                    frameAttachInfo.setAttachGuid(attachGuid);
                    frameAttachInfo.setCliengGuid(ywguid);
                    frameAttachInfo.setAttachFileName(attachfilename);
                    frameAttachInfo.setCliengTag("图审上传结果");
                    frameAttachInfo.setUploadUserGuid("");
                    frameAttachInfo.setUploadUserDisplayName("");
                    frameAttachInfo.setUploadDateTime(new Date());
                    frameAttachInfo.setContentType("image/jpeg");
                    frameAttachInfo.setAttachLength(Long.valueOf((long) pic.length));
                    ByteArrayInputStream input = new ByteArrayInputStream(pic);
                    attachService.addAttach(frameAttachInfo, input);
                    input.close();
                }

                dataJson.put("attachguid", attachGuid);
                return JsonUtils.zwdtRestReturn("1", "插入附件成功", "");
            }
        }
        catch (Exception e) {
            log.info("【济宁结束调用addTsAttach异常】" + params);
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "插入附件失败！", e.getMessage());
        }
        return JsonUtils.zwdtRestReturn("0", "插入附件失败！", "params：" + params + "rtnjson：" + rtnjson.toJSONString());
    }



    /**
     * 
     *  上传审图意见
     *  @param params
     * @return 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/addTsOpinion", method = RequestMethod.POST)
    public String addTsOpinion(@RequestBody String params) {
        JSONObject rtnjson = new JSONObject();
        try {
            JSONObject jsonparams = JSON.parseObject(params);
            if (jsonparams != null) {
                JSONObject param = jsonparams.getJSONObject("params");
                String ywguid = param.getString("ywguid");
                String handlecomments = param.getString("handlecomments");// 
                String assignee = param.getString("assignee");// 
                String orgname = param.getString("orgname");// 
                Date signtime = param.getDate("signtime");// 
                // 先判断是否已评价
                if(service.isExistProject(ywguid)>0){
                    if (service.isExistEvaluate(ywguid) == 0) {
                        Record opinion = new Record();
                        opinion.setSql_TableName("audit_sp_tsopinion");
                        opinion.set("rowguid", UUID.randomUUID().toString());
                        opinion.set("handlecomments", handlecomments);
                        opinion.set("assignee", assignee);
                        opinion.set("orgname", orgname);
                        opinion.set("signtime", signtime);
                        opinion.set("ywguid", ywguid);
                        service.addTsOpinion(opinion);
                    }
                    return JsonUtils.zwdtRestReturn("1", "评价成功", "");
                }else{
                    return JsonUtils.zwdtRestReturn("0", "办件不存在："+ywguid, "");
                }
            }
        }
        catch (Exception e) {
            log.info("【济宁结束调用addTsOpinion异常】" + params);
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "接口调用报错", e.getMessage());
        }
        return JsonUtils.zwdtRestReturn("0", "接口调用失败", "params：" + params + "rtnjson：" + rtnjson.toJSONString());
    }
    
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
                clientGuid = request.getParameter("ywguid");
                source = "审图结果"; // 上传来源
                chunk = request.getParameter("chunk") != null ? Integer.parseInt(request.getParameter("chunk")) : 0; //当前剩余分片数
                chunks = request.getParameter("chunks") != null ? Integer.parseInt(request.getParameter("chunks")) : 1; // 总分片数
                size = NumberUtils.toInt(request.getParameter("size")); // 文件大小
                fileId = request.getParameter("id"); // 上传文件标识
                fileMaps = multipartRequest.getFileParams(); // 文件内容
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "文件未获取", "");
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
                        attachGuid, "", null, source);
            }
            else {
                frameAttachInfo = AttachUtil.saveSliceAttach(tempFileItem, size, clientGuid, attachName, attachGuid,
                        "", fileId, source, chunk, chunks);
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
                log.info("=======结束调用attachUpload接口=======");
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
     * 附件上传接口
     *  
     * @params params
     * @return
     */
    @RequestMapping(value = "/private/formattachUpload", method = RequestMethod.POST)
    public String formattachUpload(HttpServletRequest request) {
        try {
            log.info("=======开始调用formattachUpload接口=======");
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
            String type = "";
            Map<String, List<FileItem>> fileMaps = null;
            MultipartRequest multipartRequest = null;
            if (request instanceof MultipartRequest) {
                multipartRequest = (MultipartRequest) request;
                attachName = multipartRequest.getParameter("name"); // 文件名称
                clientGuid = "111";
                type = multipartRequest.getParameter("type");
                source = "三个一附件"; // 上传来源
                chunk = request.getParameter("chunk") != null ? Integer.parseInt(request.getParameter("chunk")) : 0; //当前剩余分片数
                chunks = request.getParameter("chunks") != null ? Integer.parseInt(request.getParameter("chunks")) : 1; // 总分片数
                size = NumberUtils.toInt(request.getParameter("size")); // 文件大小
                fileId = request.getParameter("id"); // 上传文件标识
                fileMaps = multipartRequest.getFileParams(); // 文件内容
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "文件未获取", "");
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
                        attachGuid, "", null, source);
            }
            else {
                frameAttachInfo = AttachUtil.saveSliceAttach(tempFileItem, size, clientGuid, attachName, attachGuid,
                        "", fileId, source, chunk, chunks);
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
                AuditThreeFirst record = new AuditThreeFirst();
                record.setRowguid(UUID.randomUUID().toString());
                record.setName(attachName);
                record.setAttachguid(frameAttachInfo.getAttachGuid());
                record.setYeWuGuid(clientGuid);
                record.setType(type);
                service.insert(record);
                log.info("=======结束调用formattachUpload接口=======");
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
}
