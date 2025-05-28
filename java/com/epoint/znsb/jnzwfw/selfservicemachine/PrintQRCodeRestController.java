package com.epoint.znsb.jnzwfw.selfservicemachine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.FontSettings;
import com.aspose.words.License;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.common.service.AuditCommonService;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.security.ClassPathUtil;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.log.LogUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

@RestController
@RequestMapping(value = "/printQRCode")
public class PrintQRCodeRestController extends AuditCommonService
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IAttachService attachservice;

    transient Logger log = LogUtil.getLog(PrintQRCodeRestController.class);

    /**
     * 根据身份证获取打印模板
     */
    @RequestMapping(value = "/getQRCodeByIdCard", method = RequestMethod.POST)
    public String getQRCodeByIdCard(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("==================获取电子健康码的信息=================");
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            JSONObject dataJson = new JSONObject();

            String cardNo = obj.getString("idcard");
            String name = obj.getString("username");
            String address = obj.getString("address");
            String sex = obj.getString("sex");
            String nation = obj.getString("nation");
            String photobuf = obj.getString("photobuf");
            
            //先删除二维码
            GetHealthPassByUserInfo.deteleQRCode(name, cardNo);
            //获取二维码
            String backPrintString = GetHealthPassByUserInfo.printQRCode(name, cardNo, sex, nation);
            //获取健康状态
            String backHealthString = GetHealthPassByUserInfo.checkQRCode(name, cardNo);
            //获取接种详情
            String backjzString = GetHealthPassByUserInfo.jzQRCode(name, cardNo);

            //新增大数据接口 获取该人员7天内核酸检测
            String backnewJzString = GetHealthPassByUserInfo.newJzQRCode(name,cardNo);

            JSONObject backnewJz = JSON.parseObject(backnewJzString);

            JSONObject backHealth = JSON.parseObject(backHealthString);

            JSONObject backPrint = JSON.parseObject(backPrintString);
            
            JSONObject backJZ = JSON.parseObject(backjzString);

            String ewmcode = "";
            String jkzt = "";

            BufferedImage bufferedImage = null;
            if (backHealth.getBoolean("success") && backPrint.getBoolean("success")) {
                String ewm = backPrint.getString("barcode");
                //生成二维码
                JSONObject healJson = backHealth.getJSONObject("data");
                if (healJson.getBoolean("isHaveHealthCode")) {
                    if ("0".equals(healJson.getString("state"))) {
                        bufferedImage = QuickMarkUtil.buildBlueQuickMarkImage(ewm);
                        jkzt = "低风险";
                    }
                    else if ("1".equals(healJson.getString("state"))) {
                        bufferedImage = QuickMarkUtil.buildYellowQuickMarkImage(ewm);
                        jkzt = "中风险";
                    }
                    else if ("2".equals(healJson.getString("state"))) {
                        bufferedImage = QuickMarkUtil.buildRedQuickMarkImage(ewm);
                        jkzt = "高风险";
                    }
                    else {
                        //灰码
                        bufferedImage = QuickMarkUtil.buildGrayQuickMarkImage(ewm);
                        jkzt = "未上传健康信息";
                    }

                }
                else {
                    //灰码
                    bufferedImage = QuickMarkUtil.buildGrayQuickMarkImage(ewm);
                    jkzt = "未上传健康信息";
                }
                String jzqk = "未查到";
                String lastStationName1 = "";
                String vaccineCount1= "";
                String inoculateDate1 = "";
                String lastStationName2 = "";
                String vaccineCount2= "";
                String inoculateDate2 = "";
                String lastStationName3 = "";
                String vaccineCount3= "";
                String inoculateDate3 = "";
                //显示疫苗接种详情
                if (backJZ.getBoolean("success")) {
                   
                    //生成二维码
                    JSONObject jzJson = backJZ.getJSONObject("data");   
                    if("1".equals(jzJson.getString("flag"))){
                        jzqk = "已全程接种";
                        
                    }else if("0".equals(jzJson.getString("flag"))){
                        jzqk = "未全程接种";
                    }
                    JSONArray vaccineList = jzJson.getJSONArray("vaccineList");
                    if(vaccineList.size() > 0){
                       for (int i = 0; i < vaccineList.size(); i++) {
                           
                          JSONObject obJSON = vaccineList.getJSONObject(i);
                          if(i == 0){
                              lastStationName1 = "接种医院：" + obJSON.getString("lastStationName");
                              vaccineCount1 = "剂次：" + obJSON.getString("vaccineCount");
                              inoculateDate1 = "接种时间：" + obJSON.getString("inoculateDate");
                          }
                          if(i == 1){
                              lastStationName2 = "接种医院：" + obJSON.getString("lastStationName");
                              vaccineCount2 = "剂次：" + obJSON.getString("vaccineCount");
                              inoculateDate2 = "接种时间：" + obJSON.getString("inoculateDate");
                          }
                          
                          if(i == 2){
                              lastStationName3 = "接种医院：" + obJSON.getString("lastStationName");
                              vaccineCount3 = "剂次：" + obJSON.getString("vaccineCount");
                              inoculateDate3 = "接种时间：" + obJSON.getString("inoculateDate");
                          }
                          
                          
                    }
                    }
                    
                    
                    
                }
                Date jctime = null;
                //获取核酸检测日志
                if("200".equals(backnewJz.getString("code"))){
                    //获取核酸检测时间
                    JSONObject data = backnewJz.getJSONObject("data");

                    if("200".equals(data.getString("code"))){
                        JSONObject data1 = data.getJSONObject("data");
                        if(data1.getBoolean("success")){
                            JSONObject data2 = data1.getJSONObject("data");
                            if("1".equals(data2.getString("isTest"))){
                                jctime = data2.getDate("testTime");
                            }
                        }
                    }

                }

                

                ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
                ImageIO.write(bufferedImage, "png", baos);//写入流中
                byte[] bytes = baos.toByteArray();//转换成字节

                ewmcode = Base64Util.encode(bytes).trim();//转换成base64串
                ewmcode = ewmcode.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
                dataJson.put("ewmcode", ewmcode);
                dataJson.put("jkzt", jkzt);
                dataJson.put("printtime", new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
                
                //打印模块

                String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
                License license = new License();

                license.setLicense(licenseName);
                FontSettings.setFontsFolder(ClassPathUtil.getClassesPath() + "font" + File.separator, true);

                String doctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                        + "/jiningzwfw/individuation/overall/equipmentdisplay/selfservicemachine/bmservice/printQRCode/jkmz.docx";
               
                Document doc = new Document(doctem);
                String[] fieldNames = null;
                Object[] values = null;
                Map<String, String> map = new HashMap();
                map.put("name1", name);
                if(address.length() > 18){
                    map.put("address1", address.substring(0, 17)); 
                    map.put("address2", address.substring(17));
                    
                }else{
                    map.put("address1", address); 
                    map.put("address2", "");
                }
             
                String newcardNo =cardNo.replace(cardNo.substring(6, 14), "********");
                map.put("sfz1", newcardNo);
                map.put("jkzt1", jkzt);
                map.put("lxfs1", "");
                
                map.put("jzqk", jzqk);
                map.put("lastStationName1", lastStationName1);
                map.put("vaccineCount1", vaccineCount1);
                map.put("inoculateDate1", inoculateDate1);
                
                map.put("lastStationName2", lastStationName2);
                map.put("vaccineCount2", vaccineCount2);
                map.put("inoculateDate2", inoculateDate2);

                map.put("lastStationName3", lastStationName3);
                map.put("vaccineCount3", vaccineCount3);
                map.put("inoculateDate3", inoculateDate3);

                Date date = new Date();
                Calendar cal = Calendar.getInstance();

         
                cal.setTime(date);
                cal.add(Calendar.DAY_OF_MONTH, 15);

                if(StringUtil.isNotBlank(jctime)){
                    map.put("gototime1", new SimpleDateFormat("yyyy年MM月dd日").format(jctime));
                }
                else{
                    map.put("gototime1", new SimpleDateFormat("yyyy年MM月dd日").format(cal.getTime()));
                }


                map.put("printtime1", new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
                

                
             /*   map.put("name2", name);
                map.put("address2", address);
                map.put("sfz2", cardNo);
                map.put("jkzt2", jkzt);
                map.put("lxfs2", "");
                map.put("printtime2", new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
                
                map.put("name3", name);
                map.put("address3", address);
                map.put("sfz3", cardNo);
                map.put("jkzt3", jkzt);
                map.put("lxfs3", "");
                map.put("printtime3", new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
                
                map.put("name4", name);
                map.put("address4", address);
                map.put("sfz4", cardNo);
                map.put("jkzt4", jkzt);
                map.put("lxfs4", "");
                map.put("printtime4", new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));*/
                
                fieldNames = new String[map == null ? 0 : map.size()];
                values = new Object[map == null ? 0 : map.size()];
                int num = 0;

                for (Entry<String, String> entry : map.entrySet()) {
                    fieldNames[num] = entry.getKey();
                    values[num] = entry.getValue();
                    num++;
                }
                
                doc.getMailMerge().execute(fieldNames, values);
       
                if (photobuf != null) {
                	 byte[] pic = Base64Util.decodeBuffer(photobuf.replace("data:image/png;base64,", ""));
                     InputStream input = new ByteArrayInputStream(pic);
           
                     if (input != null) {
                         DocumentBuilder build = new DocumentBuilder(doc);
                         build.moveToBookmark("photopuf1");
                         build.insertImage(input, 50.0D, 60.0D);
                     }
                }
               
     /*          input = new ByteArrayInputStream(pic);
                if (input != null) {
                    DocumentBuilder build = new DocumentBuilder(doc);
                    build.moveToBookmark("photopuf2");
                    build.insertImage(input, 50.0D, 60.0D);
                }
                
                input = new ByteArrayInputStream(pic);
                if (input != null) {
                    DocumentBuilder build = new DocumentBuilder(doc);
                    build.moveToBookmark("photopuf3");
                    build.insertImage(input, 50.0D, 60.0D);
                }
                
                input = new ByteArrayInputStream(pic);
                if (input != null) {
                    DocumentBuilder build = new DocumentBuilder(doc);
                    build.moveToBookmark("photopuf4");
                    build.insertImage(input, 50.0D, 60.0D);
                }
                
*/
                byte[] ewmpic = Base64Util.decodeBuffer(ewmcode.replace("data:image/png;base64,", ""));
                InputStream ewminput = new ByteArrayInputStream(ewmpic);
                if (ewminput != null) {
                    DocumentBuilder build = new DocumentBuilder(doc);
                    build.moveToBookmark("ewm1");
                    build.insertImage(ewminput, 80.0D, 80.0D);
                }
                  /*  ewminput = new ByteArrayInputStream(ewmpic);
                if (ewminput != null) {
                    DocumentBuilder build = new DocumentBuilder(doc);
                    build.moveToBookmark("ewm2");
                    build.insertImage(ewminput, 50.0D, 50.0D);
                }
                ewminput = new ByteArrayInputStream(ewmpic);
                if (ewminput != null) {
                    DocumentBuilder build = new DocumentBuilder(doc);
                    build.moveToBookmark("ewm3");
                    build.insertImage(ewminput, 50.0D, 50.0D);
                }
                ewminput = new ByteArrayInputStream(ewmpic);
                if (ewminput != null) {
                    DocumentBuilder build = new DocumentBuilder(doc);
                    build.moveToBookmark("ewm4");
                    build.insertImage(ewminput, 50.0D, 50.0D);
                }*/

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                doc.save(outputStream, 10);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                long size = (long) inputStream.available();
                FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                frameAttachInfo.setAttachFileName("健康码打印.doc");
                frameAttachInfo.setCliengTag("健康码打印");
                frameAttachInfo.setUploadUserGuid("");
                frameAttachInfo.setUploadUserDisplayName("");
                frameAttachInfo.setUploadDateTime(new Date());
                frameAttachInfo.setContentType("application/msword");
                frameAttachInfo.setAttachLength(size);
                dataJson.put("docattachguid", attachservice.addAttach(frameAttachInfo, inputStream).getAttachGuid());
                outputStream.close();
                inputStream.close();
                
                //system.out.println(dataJson);
            }else{
                if (!backHealth.getBoolean("success")) {
                    return JsonUtils.zwdtRestReturn("0", backHealth.getString("msg"), "");
                }
                if (!backPrint.getBoolean("success")) {
                    return JsonUtils.zwdtRestReturn("0", backPrint.getString("msg"), "");
                }
                
                return JsonUtils.zwdtRestReturn("0", "", "");
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
        	e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }
}
