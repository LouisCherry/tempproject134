
package com.epoint.zwdt.zwdtrest.project;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

@RestController
@RequestMapping("/jnwxproject")
public class JnWxProjectController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    
    @Autowired
    IAttachService attachService;
    
    /**
     * 事项审批结果API
     */

    
    /**
     * 检查必要材料是否都上传（办件申报提交时调用）
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getResult", method = RequestMethod.POST)
    public String getResult(@RequestBody String params) {
        try {
            log.info("=======开始调用getResult接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = jsonObject.getJSONObject("params");
                // 1.1、获取事项ID
                String itemId = obj.getString("itemId");
                // 1.2、获取受理编号
                String flowsn = obj.getString("flowsn");
                //1.3、获取办件唯一标识
                String projectguid = obj.getString("projectguid");
                String materurl1 = "http://59.206.96.199:8080/web/approval/getMaterial?itemId="+itemId+"&receiveNumber="+flowsn;
                String materresults = HttpUtil.doGet(materurl1);
                System.out.println("materresults:"+materresults);
                JSONObject objdata = JSONObject.parseObject(materresults);
                JSONArray jsonArray = objdata.getJSONArray("data");
                System.out.println("jsonArray:"+jsonArray);
                JSONObject object = new JSONObject();
                JSONObject fileobject = new JSONObject();
                String fileName = "";
                String fileUrl = "";
                String attachguid = UUID.randomUUID().toString();
                int count = 0;
                if (jsonArray != null && jsonArray.size() > 0) {
                    System.out.println("jsonArray不为空");
                    for (int i=0;i<jsonArray.size();i++) {
                        object = jsonArray.getJSONObject(i);
                        if ("审批结果材料".equals(object.getString("BUSINESS_TYPE_VALUE"))) {
                            JSONArray jsonFileArray = object.getJSONArray("FILES");
                            for (int j=0;j<jsonFileArray.size();j++) {
                                fileobject = jsonFileArray.getJSONObject(j);
                                fileUrl = "http://59.206.96.197:8080/WebDiskServerDemo/doc?doc_id="+fileobject.getString("FILE_PATH");
                                List<FrameAttachInfo> attachlist = attachService.getAttachInfoListByGuid(projectguid);
                                System.out.println("attachlist:"+attachlist);
                                fileName = fileobject.getString("FILE_NAME");
                                InputStream in = downloadFile(fileUrl);
                                System.out.println("fileUrl:"+fileUrl);
                                if (attachlist!= null && attachlist.size() > 0) {
                                    for (FrameAttachInfo attach : attachlist) {
                                       if (fileName.equals(attach.getAttachFileName())) {
                                           attach.setUploadDateTime(new Date());
                                           attach.setAttachLength((long) inputStream2Byte(in).length);
                                           attachService.updateAttach(attach, in);
                                           break;
                                       }
                                       System.out.println("对附件进行了更新");
                                    }
                                }else {
                                    FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                                    frameAttachInfo.setAttachGuid(attachguid);
                                    frameAttachInfo.setCliengGuid(projectguid);
                                    frameAttachInfo.setAttachFileName(fileName);
                                    frameAttachInfo.setCliengTag("浪潮结果附件");
                                    frameAttachInfo.setUploadDateTime(new Date());
                                    frameAttachInfo.setAttachLength((long) inputStream2Byte(in).length);
                                    attachService.addAttach(frameAttachInfo, in);
                                }
                                count++;
                            }
                            
                        }
                    }
                }else {
                    System.out.println("jsonArray为空");
                    return JsonUtils.zwdtRestReturn("0", "该审批结果为空！", "");
                }
                // 8、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("total", count);
                log.info("=======结束调用getResult接口=======");
                return JsonUtils.zwdtRestReturn("1", "插入附件成功！", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "插入附件失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getResult接口参数：params【" + params + "】=======");
            log.info("=======getResult异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "错误信息为" + e.getMessage(), "");
        }
    }
    
    /**
     * 检查必要材料是否都上传（办件申报提交时调用）
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getWtResult", method = RequestMethod.POST)
    public String getWtResult(@RequestBody String params) {
        try {
            log.info("=======开始调用getWtResult接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = jsonObject.getJSONObject("params");
                // 1.2、获取受理编号
                String flowsn = obj.getString("flowsn");
                //1.3、获取办件唯一标识
                String projectguid = obj.getString("projectguid");
                String materurl1 = "http://59.206.96.200:8090/web/approval/getMaterial?receiveNumber="+flowsn;
                String materresults = HttpUtil.doGet(materurl1);
                System.out.println("materresults:"+materresults);
                JSONObject objdata = JSONObject.parseObject(materresults);
                JSONArray jsonArray = objdata.getJSONArray("data");
                System.out.println("jsonArray:"+jsonArray);
                JSONObject object = new JSONObject();
                JSONObject fileobject = new JSONObject();
                String fileName = "";
                String fileUrl = "";
                String attachguid = UUID.randomUUID().toString();
                int count = 0;
                if (jsonArray != null && jsonArray.size() > 0) {
                    System.out.println("jsonArray不为空");
                    for (int i=0;i<jsonArray.size();i++) {
                        object = jsonArray.getJSONObject(i);
                        if ("审批结果材料".equals(object.getString("BUSINESS_TYPE_VALUE"))) {
                            JSONArray jsonFileArray = object.getJSONArray("FILES");
                            for (int j=0;j<jsonFileArray.size();j++) {
                                fileobject = jsonFileArray.getJSONObject(j);
                                fileUrl = "http://59.206.96.197:8080/WebDiskServerDemo/doc?doc_id="+fileobject.getString("FILE_PATH");
                                List<FrameAttachInfo> attachlist = attachService.getAttachInfoListByGuid(projectguid);
                                System.out.println("attachlist:"+attachlist);
                                fileName = fileobject.getString("FILE_NAME");
                                InputStream in = downloadFile(fileUrl);
                                System.out.println("fileUrl:"+fileUrl);
                                if (attachlist!= null && attachlist.size() > 0) {
                                    for (FrameAttachInfo attach : attachlist) {
                                       if (fileName.equals(attach.getAttachFileName())) {
                                           attach.setUploadDateTime(new Date());
                                           attach.setAttachLength((long) inputStream2Byte(in).length);
                                           attachService.updateAttach(attach, in);
                                           break;
                                       }
                                       System.out.println("对附件进行了更新");
                                    }
                                }else {
                                    FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                                    frameAttachInfo.setAttachGuid(attachguid);
                                    frameAttachInfo.setCliengGuid(projectguid);
                                    frameAttachInfo.setAttachFileName(fileName);
                                    frameAttachInfo.setCliengTag("浪潮结果附件");
                                    frameAttachInfo.setUploadDateTime(new Date());
                                    frameAttachInfo.setAttachLength((long) inputStream2Byte(in).length);
                                    attachService.addAttach(frameAttachInfo, in);
                                }
                                count++;
                            }
                            
                        }
                    }
                }else {
                    System.out.println("jsonArray为空");
                    return JsonUtils.zwdtRestReturn("0", "该审批结果为空！", "");
                }
                // 8、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("total", count);
                log.info("=======结束调用getWtResult接口=======");
                return JsonUtils.zwdtRestReturn("1", "插入附件成功！", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "插入附件失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getResult接口参数：params【" + params + "】=======");
            log.info("=======getResult异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "错误信息为" + e.getMessage(), "");
        }
    }
    
    /**
     * 
     *  将inputstream转化为byte
     *  @param inStream
     *  @return
     *  @throws Exception    
     */
    public static byte[] inputStream2Byte(InputStream inStream)
            throws Exception {
        int count = 0;
        while (count == 0) {
            count = inStream.available();
        }
        byte[] b = new byte[count];
        inStream.read(b);
        return b;
    }
        
    /**
     * 
     *  调用浪潮获取附件接口将附件存到附件表中
     *  @param Urlcn
     *  @return
     *  @throws Exception    
     */
    public  InputStream downloadFile(String Urlcn) throws Exception {
            String FileURL ="http://59.206.96.197:8080/WebDiskServerDemo/doc?doc_id="+Urlcn;
            URL url = new URL(FileURL);
            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setConnectTimeout(6000);
            urlCon.setReadTimeout(6000);
            int code = urlCon.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                throw new Exception("文件读取失败");
            }
            return urlCon.getInputStream();
    }

}
