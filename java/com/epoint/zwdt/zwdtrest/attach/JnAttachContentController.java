package com.epoint.zwdt.zwdtrest.attach;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.dto.annotation.RequestSupport;
import com.epoint.core.dto.annotation.RequestType;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.image.ImageUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;

@RestController
@RequestMapping("/jnattachcontent")
public class JnAttachContentController
{
	
	 @Autowired
	 private IAttachService attachService;
	 
	/**
     * 政务大厅注册用户API
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;

	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());


	
	 /**
     * 附件下载(证照库用)
     * 
     * @params params
     * @return
	 * @throws IOException 
     */
    @RequestMapping(value = "/readAttach")
    public void readAttach(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
    	
    	// 1.4、获取用户信息
        
        	 byte[] result = null;
             // 新的range
             String contentrange = "";
             // 附件总长度
             Integer fulllength = 0;
             String filename = "";
             String flietype = "";
             String attachGuid = request.getParameter("attachguid");
             // 获取Range
             String range = request.getHeader("Range");
             FrameAttachStorage attachStorage = attachService.getAttach(attachGuid);
             InputStream inputStream = attachStorage.getContent();
             FrameAttachInfo attachInfo = attachService.getAttachInfoDetail(attachGuid);
             filename = attachInfo.getAttachFileName();
             flietype = attachInfo.getContentType();
             if (StringUtil.isBlank(flietype) || flietype == null) {
             	if (!filename.endsWith("pdf")) {
             		flietype = ".pdf";
             	}
             }
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
             response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(filename + flietype, "UTF-8"));// 设置文件名
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
