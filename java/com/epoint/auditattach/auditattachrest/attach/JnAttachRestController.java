package com.epoint.auditattach.auditattachrest.attach;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.api.common.ParamException;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;

@RestController
@RequestMapping("/jnauditattach")
public class JnAttachRestController
{

    @Autowired
    private IAttachService attachservice;

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
        FrameAttachInfo info = attachservice.getAttachInfoDetail(attachGuid);
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
        FrameAttachStorage storage = attachservice.getAttach(attachGuid);
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

}
