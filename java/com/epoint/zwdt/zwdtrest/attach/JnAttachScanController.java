package com.epoint.zwdt.zwdtrest.attach;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.OfficeWebUrlEncryptUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/attachScan")
public class JnAttachScanController
{
    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IAttachService attachService;

    @RequestMapping(value = "/getAttachScan", method = RequestMethod.POST)
    public String getAttachScan(@RequestBody String params) {
        logger.info(StringUtils.center("调用getAttachScan接口",30,"="));
        logger.info("=======params=======" + params);
        try{
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = jsonObject.getJSONObject("params");

            JSONObject dataJson = new JSONObject();
            String originUrl=obj.getString("fileurl");
            if(!StringUtil.isBlank(originUrl)){
                String[] url = originUrl.split("\\?");
                if(url.length<2){
                    return JsonUtils.zwdtRestReturn("0", "附件地址格式不正确！", "");
                }
                if(!url[1].contains("attachGuid=")&&!url[1].contains("attachguid=")){
                    return JsonUtils.zwdtRestReturn("0", "缺少附件Guid参数！", "");
                }
                String[] urlParams = url[1].split("&");
                String attachGuid="";
                for (String paramStr :
                        urlParams) {
                    String[] paramSplit = paramStr.split("=");
                    if("attachGuid".equalsIgnoreCase(paramSplit[0])){
                        if(paramSplit.length>1){
                            attachGuid=paramSplit[1];
                        }
                    }
                }
                if(StringUtil.isBlank(attachGuid)){
                    return JsonUtils.zwdtRestReturn("0", "附件Guid参数不能为空！", "");
                }
                String officeweb365url = ConfigUtil.getConfigValue("officeweb365url");
                if (StringUtil.isBlank(officeweb365url)) {
                    return JsonUtils.zwdtRestReturn("0", "未配置预览地址！", "");
                }
                FrameAttachInfo attachInfoDetail = attachService.getAttachInfoDetail(attachGuid);
                if (attachInfoDetail!=null){
                    String contentType= attachInfoDetail.getContentType();
                    String fileName=attachInfoDetail.getAttachFileName();
                    if (StringUtil.isBlank(fileName)) {
                        fileName = UUID.randomUUID().toString();
                    }

                    String encode=fileName;
                    try {
                        //防止名称包含特殊字符，预览不了
                        encode = URLEncoder.encode(fileName, "utf-8");
                    }
                    catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String finalUrl=officeweb365url
                            + OfficeWebUrlEncryptUtil.getEncryptUrl(originUrl, contentType);
                    dataJson.put("attachscanurl",finalUrl);
                    logger.info("=======结束调用getAttachScan接口======="
                            + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                    return JsonUtils.zwdtRestReturn("1", "获取成功！", dataJson.toJSONString());
                }
                else{
                    return JsonUtils.zwdtRestReturn("0", "未获取到附件信息！", "");
                }

            }
            else{
                return JsonUtils.zwdtRestReturn("0", "附件地址不能为空！", "");
            }

        }
        catch (Exception e){
            logger.info("=======结束调用getAttachScan接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            logger.info("=======getAttachScan异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "获取预览地址失败！" + e.getMessage(), "");
        }


    }


}
