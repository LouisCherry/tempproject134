package com.epoint.auditselfservice.auditselfservicerest.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.FontSettings;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

@RestController
@RequestMapping(value = "/jnprint")
public class JnPrintRestController 
{
    @Autowired
    private IAttachService attachservice;
    
    @Autowired
    private IHandleConfig handleConfigservice;
    transient Logger log = LogUtil.getLog(PrintRestController.class);
   
    /**
     * 身份信息证明打印
     *  @param params
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/idcardcopy", method = RequestMethod.POST)
    public String idcardcopy(@RequestBody String params, @Context HttpServletRequest request) {
        try {           
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String username = obj.getString("username");
            String address = obj.getString("address");
            String idcard = obj.getString("idcard");
            String sex = obj.getString("sex");
            String birthyear = obj.getString("birthyear");
            String birthmonth = obj.getString("birthmonth");
            String birthday = obj.getString("birthday");
            String folk = obj.getString("folk");
            String valid = obj.getString("valid");
            String agency = obj.getString("agency");
            String photobuf = obj.getString("photobuf");
            String centerguid = obj.getString("centerguid");

            JSONObject dataJson = new JSONObject();
            
            String licenseName=ClassPathUtil.getClassesPath()+"license.xml";
            License license=new License();
            try {
                license.setLicense(licenseName);
                // 是否使用审批系统的字体（如果linux系统乱码才使用）
                FontSettings.setFontsFolder(ClassPathUtil.getClassesPath() + "font" + File.separator, true);
                
            } catch (Exception e1) {
                log.error(e1.toString());
            }
            
            String downconfig = handleConfigservice.getFrameConfig("AS_LOCAL_DOWNLOAD_URL", centerguid).getResult();
            String doctem;
            if (StringUtil.isNotBlank(downconfig)) {
                doctem = downconfig
                        + "/znsb/equipmentdisplay/selfservicemachine/bmservice/idcardcopy/sfz.docx";
            }
            else {
                doctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                        + "/znsb/equipmentdisplay/selfservicemachine/bmservice/idcardcopy/sfz.docx";
            }
           /* String doctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                    + "/znsb/equipmentdisplay/selfservicemachine/bmservice/idcardcopy/sfz.docx";*/
            Document doc = new Document(doctem);
            String[] fieldNames = null;
            Object[] values = null;
            // 获取域与对应的值
            Map<String, String> map = new HashMap<String, String>();
            map.put("n", username);
            map.put("address", address);
            map.put("idcard", idcard);
            map.put("s", sex);
            map.put("y", birthyear);
            map.put("m", birthmonth);
            map.put("d", birthday);
            map.put("f", folk);
            map.put("valid", valid);
            map.put("agency", agency);
            fieldNames = new String[map.size()];
            values = new Object[map.size()];
            int num = 0;

            for (Entry<String, String> entry : map.entrySet()) {
                fieldNames[num] = entry.getKey();
                values[num] = entry.getValue();
                num++;
            }
            // 替换域
            doc.getMailMerge().execute(fieldNames, values);
            byte[] pic = Base64Util.decodeBuffer(photobuf.replace("data:image/png;base64,", ""));
            InputStream input = new ByteArrayInputStream(pic);

            if (input != null) {
                DocumentBuilder build = new DocumentBuilder(doc);
                // 指定对应的书签
                build.moveToBookmark("photobuf");
                // 插入附件流信息
                build.insertImage(input, 200, 240);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            doc.save(outputStream, SaveFormat.DOC);// 保存成word
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            long size = inputStream.available();

            // 附件信息
            FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
            frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
            frameAttachInfo.setAttachFileName("身份证正反.doc");
            frameAttachInfo.setCliengTag("身份证复印");
            frameAttachInfo.setUploadUserGuid("");
            frameAttachInfo.setUploadUserDisplayName("");
            frameAttachInfo.setUploadDateTime(new Date());
            frameAttachInfo.setContentType("application/msword");
            frameAttachInfo.setAttachLength(size);
            dataJson.put("docattachguid", attachservice.addAttach(frameAttachInfo, inputStream).getAttachGuid());

            outputStream.close();
            inputStream.close();

            return JsonUtils.zwdtRestReturn("1", "获取附件成功！", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "获取附件发生异常：" + e.getMessage(), "");
        }
    }
    

}
