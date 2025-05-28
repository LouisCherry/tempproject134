package com.epoint.zoucheng.znsb.auditcxj.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.api.common.ParamException;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbvideopic.domain.AuditZnsbVideopic;
import com.epoint.basic.auditqueue.auditznsbvideopic.inter.IAuditZnsbVideopicService;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.QueueCommonUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;

@RestController
@RequestMapping("/zccxjcommon")
public class ZCCxjCommonRestController
{

    @Autowired
    private IAuditZnsbEquipment equipmentservice;

    @Autowired
    private IAuditZnsbVideopicService auditZnsbVideopicService;

    @Autowired
    private IAttachService attachService;

    /**
     * 获取音频视频图片列表
     * [一句话功能简述]
     * [功能详细描述]
     * 
     * @param params
     * @param request
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getVideoPic", method = RequestMethod.POST)
    public String getPic(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String macaddress = obj.getString("macaddress");// 根据macaddress获取对应的视频或图片
            String videopictype = obj.getString("videopictype");// 兼容图片轮播
            String currentpage = "0";// obj.getString("currentpage");//当前页数
            String pagesize = "100";// obj.getString("pagesize");//每页数
            JSONObject dataJson = new JSONObject();
            List<Record> recordlist = new ArrayList<>();
            String centerguid = "";

            AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(macaddress).getResult();
            if (StringUtil.isNotBlank(equipment)) {
                // 根据macaddress获取中心guid
                centerguid = equipment.getCenterguid();
                if (StringUtil.isNotBlank(centerguid)) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    // 先获取视频地址，与文件名
                    sql.eq("Centerguid", centerguid);
                    sql.eq("videopictype", videopictype);
                    // 判断视频是否启用
                    sql.eq("is_enable", QueueConstant.CONSTANT_STR_ONE);

                    PageData<AuditZnsbVideopic> videopagedata = auditZnsbVideopicService
                            .getFileByPage(sql.getMap(), Integer.parseInt(currentpage) * Integer.parseInt(pagesize),
                                    Integer.parseInt(pagesize), "ordernum", "desc")
                            .getResult();
                    if (videopagedata.getList() != null && !videopagedata.getList().isEmpty()) {
                        for (AuditZnsbVideopic video : videopagedata.getList()) {
                            Record record = new Record();
                            if (StringUtil.isNotBlank(video.getVideopicattachguid())) {
                                // 视频文件
                                List<FrameAttachInfo> attachinfolist = attachService
                                        .getAttachInfoListByGuid(video.getVideopicattachguid());

                                if (attachinfolist != null && !attachinfolist.isEmpty()) {
                                    if ("1".equals(videopictype)) {
                                        record.put("videoguid",
                                                QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                                                        + "/rest/zccxjcommon/readAttachVideo?attachguid="
                                                        + attachinfolist.get(0).getAttachGuid());
                                    }
                                    else if ("3".equals(videopictype)) {
                                        record.put("audioguid",
                                                QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                                                        + "/rest/zccxjcommon/readAttachAudio?attachguid="
                                                        + attachinfolist.get(0).getAttachGuid());
                                    }
                                    record.put("filename", video.getFilename());
                                }
                                if ("1".equals(videopictype)) {
                                    // 视频封面文件
                                    List<FrameAttachInfo> picinfolist = attachService
                                            .getAttachInfoListByGuid(video.getPicofvideo());
                                    if (picinfolist != null && !picinfolist.isEmpty()) {
                                        record.put("picofvideo",
                                                QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                                                        + "/rest/auditattach/readAttach?attachguid="
                                                        + picinfolist.get(0).getAttachGuid());
                                    }
                                    else {
                                        // 如果没有获取到封面则使用默认的图片
                                        record.put("picofvideo",
                                                QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                                                        + "/rest/auditattach/"
                                                        + "readAttach?attachguid=8e4a36a5-4e80-4844-bd5a-3ef91d211b7d");
                                    }
                                }
                            }
                            recordlist.add(record);
                        }
                    }
                    dataJson.put("recordlist", recordlist);
                    dataJson.put("totalcount", videopagedata.getRowCount());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取失败，未获取到有效的大厅Guid", "");
                }
            }
            return JsonUtils.zwdtRestReturn("1", "获取成功", dataJson);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取失败：" + e.getMessage(), "");
        }

    }

    /**
     * 
     * [视频附件获取接口]
     * 
     */
    @RequestMapping(value = "/readAttachVideo", method = {RequestMethod.POST, RequestMethod.GET })
    public void readAttachVideo(HttpServletRequest request, HttpServletResponse response)
            throws ParamException, IOException {
        String attachGuid = WebUtil.getRequestParameter(request, "attachguid");
        if (StringUtil.isBlank(attachGuid)) {
            throw new ParamException("输入参数不合法");
        }
        // 附件总长度
        long fullLength = 0;
        // 错误信息
        String errorInfo = "";
        // 文件名
        String filename = "";
        // 获取附件信息
        FrameAttachInfo info = attachService.getAttachInfoDetail(attachGuid);
        if (info == null) {
            errorInfo = "FrameAttachInfo信息未找到";
            OutputStream os = null;
            try {
                // 输出文件流
                os = response.getOutputStream();
                os.write(errorInfo.getBytes());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                // 关闭流
                if (os != null) {
                    try {
                        os.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "FrameAttachInfo信息未找到");
            return;
        }
        fullLength = info.getAttachLength();
        filename = info.getAttachFileName();
        // 获取附件二进制流
        FrameAttachStorage storage = attachService.getAttach(attachGuid);
        if (storage == null || storage.getContent() == null) {
            errorInfo = "FrameAttachStorage信息未找到";
            OutputStream os = null;
            try {
                // 输出文件流
                os = response.getOutputStream();
                os.write(errorInfo.getBytes());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                // 关闭流
                if (os != null) {
                    try {
                        os.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (storage != null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "FrameAttachStorage信息未找到,附件路径:" + storage.getFilePath());
            }
            else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "FrameAttachStorage信息未找到");
            }
            return;
        }
        if ("http".equals(info.getStorageType())) {
            response.sendRedirect(info.getFilePath());
            return;
        }
        InputStream in = storage.getContent();
        String range = request.getHeader("Range");
        int start = 0;
        int end = 0;
        if (range != null && range.startsWith("bytes=")) {
            String[] values = range.split("=")[1].split("-");
            start = Integer.parseInt(values[0]);
            if (values.length > 1) {
                end = Integer.parseInt(values[1]);
            }
        }
        int requestSize = 0;
        if (end != 0 && end > start) {
            requestSize = end - start + 1;
        }
        else {
            requestSize = Integer.MAX_VALUE;
        }
        byte[] buffer = new byte[Integer.parseInt("4096")];
        response.setContentType("video/mp4");
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("ETag", filename);
        response.setHeader("Last-Modified", new Date().toString());
        // 第一次请求只返回content length来让客户端请求多次实际数据
        if (range == null) {
            response.setHeader("Content-length", fullLength + "");
        }
        else {
            // 以后的多次以断点续传的方式来返回视频数据
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);// 206
            long requestStart = 0;
            int requestEnd = 0;
            String[] ranges = range.split("=");
            if (ranges.length > 1) {
                String[] rangeDatas = ranges[1].split("-");
                requestStart = Integer.parseInt(rangeDatas[0]);
                if (rangeDatas.length > 1) {
                    requestEnd = Integer.parseInt(rangeDatas[1]);
                }
            }
            long length = 0;
            if (requestEnd > 0) {
                length = requestEnd - requestStart + 1;
                response.setHeader("Content-length", "" + length);
                response.setHeader("Content-Range", "bytes " + requestStart + "-" + requestEnd + "/" + fullLength);
            }
            else {
                length = fullLength - requestStart;
                response.setHeader("Content-length", "" + length);
                response.setHeader("Content-Range",
                        "bytes " + requestStart + "-" + (fullLength - 1) + "/" + fullLength);
            }
        }
        ServletOutputStream out = response.getOutputStream();
        int needSize = requestSize;
        in.skip(start);
        while (needSize > 0) {
            int len = in.read(buffer);
            if (needSize < buffer.length) {
                out.write(buffer, 0, needSize);
            }
            else {
                out.write(buffer, 0, len);
                if (len < buffer.length) {
                    break;
                }
            }
            needSize -= buffer.length;
        }
        in.close();
        out.close();
    }

    /**
     * 
     * [音频附件获取接口]
     * 
     */
    @RequestMapping(value = "/readAttachAudio", method = {RequestMethod.POST, RequestMethod.GET })
    public void readAttachAudio(HttpServletRequest request, HttpServletResponse response)
            throws ParamException, IOException {
        String attachGuid = WebUtil.getRequestParameter(request, "attachguid");
        if (StringUtil.isBlank(attachGuid)) {
            throw new ParamException("输入参数不合法");
        }
        // 附件总长度
        long fullLength = 0;
        // 错误信息
        String errorInfo = "";
        // 文件名
        String filename = "";
        // 获取附件信息
        FrameAttachInfo info = attachService.getAttachInfoDetail(attachGuid);
        if (info == null) {
            errorInfo = "FrameAttachInfo信息未找到";
            OutputStream os = null;
            try {
                // 输出文件流
                os = response.getOutputStream();
                os.write(errorInfo.getBytes());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                // 关闭流
                if (os != null) {
                    try {
                        os.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "FrameAttachInfo信息未找到");
            return;
        }
        fullLength = info.getAttachLength();
        filename = info.getAttachFileName();
        // 获取附件二进制流
        FrameAttachStorage storage = attachService.getAttach(attachGuid);
        if (storage == null || storage.getContent() == null) {
            errorInfo = "FrameAttachStorage信息未找到";
            OutputStream os = null;
            try {
                // 输出文件流
                os = response.getOutputStream();
                os.write(errorInfo.getBytes());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                // 关闭流
                if (os != null) {
                    try {
                        os.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (storage != null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "FrameAttachStorage信息未找到,附件路径:" + storage.getFilePath());
            }
            else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "FrameAttachStorage信息未找到");
            }
            return;
        }
        if ("http".equals(info.getStorageType())) {
            response.sendRedirect(info.getFilePath());
            return;
        }
        InputStream in = storage.getContent();
        String range = request.getHeader("Range");
        int start = 0;
        int end = 0;
        if (range != null && range.startsWith("bytes=")) {
            String[] values = range.split("=")[1].split("-");
            start = Integer.parseInt(values[0]);
            if (values.length > 1) {
                end = Integer.parseInt(values[1]);
            }
        }
        int requestSize = 0;
        if (end != 0 && end > start) {
            requestSize = end - start + 1;
        }
        else {
            requestSize = Integer.MAX_VALUE;
        }
        byte[] buffer = new byte[Integer.parseInt("4096")];
        response.setContentType("audio/mp3");
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("ETag", filename);
        response.setHeader("Last-Modified", new Date().toString());
        // 第一次请求只返回content length来让客户端请求多次实际数据
        if (range == null) {
            response.setHeader("Content-length", fullLength + "");
        }
        else {
            // 以后的多次以断点续传的方式来返回视频数据
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);// 206
            long requestStart = 0;
            int requestEnd = 0;
            String[] ranges = range.split("=");
            if (ranges.length > 1) {
                String[] rangeDatas = ranges[1].split("-");
                requestStart = Integer.parseInt(rangeDatas[0]);
                if (rangeDatas.length > 1) {
                    requestEnd = Integer.parseInt(rangeDatas[1]);
                }
            }
            long length = 0;
            if (requestEnd > 0) {
                length = requestEnd - requestStart + 1;
                response.setHeader("Content-length", "" + length);
                response.setHeader("Content-Range", "bytes " + requestStart + "-" + requestEnd + "/" + fullLength);
            }
            else {
                length = fullLength - requestStart;
                response.setHeader("Content-length", "" + length);
                response.setHeader("Content-Range",
                        "bytes " + requestStart + "-" + (fullLength - 1) + "/" + fullLength);
            }
        }
        ServletOutputStream out = response.getOutputStream();
        int needSize = requestSize;
        in.skip(start);
        while (needSize > 0) {
            int len = in.read(buffer);
            if (needSize < buffer.length) {
                out.write(buffer, 0, needSize);
            }
            else {
                out.write(buffer, 0, len);
                if (len < buffer.length) {
                    break;
                }
            }
            needSize -= buffer.length;
        }
        in.close();
        out.close();
    }

}
