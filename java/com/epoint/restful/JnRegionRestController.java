package com.epoint.restful;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditproject.audithandlecontrol.action.JNAuditHandleControlAction;
import com.epoint.basic.api.common.ParamException;
import com.epoint.basic.faces.fileupload.MultipartRequest;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.domain.JnAuditZnsbSelfmachineregion;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.inter.IJnAuditZnsbSelfmachineregion;
import com.esotericsoftware.minlog.Log;

@RestController
@RequestMapping("/jnregion")
public class JnRegionRestController {

    private Logger logger = Logger.getLogger(JnRegionRestController.class);
    
    @Autowired
    private IAttachService attachservice;
    
    @Autowired
    private IJnAuditZnsbSelfmachineregion regionservice;
    
    @Autowired
    private IConfigService configservice;

    
    /**
     * 获取所有长三角区域配置
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getregionlist", method = RequestMethod.POST)
    public String getRegionList(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            
            List<JnAuditZnsbSelfmachineregion> regionlist=regionservice.getAllCommonRegionList().getResult();
            
            dataJson.put("regionlist", regionlist);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    
    @RequestMapping(value = "/getPlaceByCode", method = RequestMethod.POST)
    public String getPlaceByCode(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String code =obj.getString("code");
            JSONObject dataJson = new JSONObject();
            
            JnAuditZnsbSelfmachineregion jazsn=regionservice.getRegionByCode(code);
            dataJson.put("region", jazsn);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    /**
     * 
     *  [获取可转地点列表] 
     *  @param params
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getAllUsedPlace", method = RequestMethod.POST)
    public String getAllUsedPlace(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            int currentpage =Integer.parseInt(obj.getString("currentpage"));
            int pagesize = Integer.parseInt(obj.getString("pagesize"));
            JSONObject dataJson = new JSONObject();
            logger.info("getAllUsedPlace parmas:[c]"+currentpage+" [p]"+pagesize);
            List<JnAuditZnsbSelfmachineregion> jazsnList=regionservice.getAllUsedPlaceList(currentpage,pagesize);
            int totalcount=regionservice.getAllUsedPlaceListNum();
            dataJson.put("list", jazsnList);
            dataJson.put("totalcount", totalcount);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (Exception e) {
            logger.error("getAllUsedPlace error:"+e.getMessage());
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    /**
     * 根据父级guid获取 区域list
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getregionlistbyparentguid", method = RequestMethod.POST)
    public String getRegionListByParentguid(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String parentguid = obj.getString("parentguid");
            JSONObject dataJson = new JSONObject();
            List<JnAuditZnsbSelfmachineregion> regionlist=null;
            if (StringUtil.isNotBlank(parentguid)) {
                regionlist= regionservice.getChildRegionListInuseByParentguid(parentguid).getResult();
            }else{
                regionlist= regionservice.getParentRegionListInuse().getResult();
            }
          //长三角区域配置数据获取接口
            String regionurl = configservice.getFrameConfigValue("AS_ZNSB_CSJ_REGIONURL");
            dataJson.put("regionurl", regionurl);   
            dataJson.put("regionlist", regionlist);   
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    
    @RequestMapping(value = "/uploadexcel", method = RequestMethod.POST)
    public String uploadexcel(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "file", required = false) CommonsMultipartFile file1) {

        try {
            // 从multipartRequest获取POST的流文件并保存到数据库
            request = (MultipartRequest) request;

            String cliengguid = request.getParameter("cliengguid");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter ou = new PrintWriter(response.getOutputStream());
            JSONObject jsonObject = new JSONObject();
            int uploadresult = 0;
            try {
                initExcelRegion((MultipartRequest) request, cliengguid);
                uploadresult = 1;
            }
            catch (Exception e) {
                Log.info(e.toString());
            }

            jsonObject.put("uploadresult", uploadresult);

            ou.write(jsonObject.toString());
            ou.flush();
            ou.close();

            return JsonUtils.zwdtRestReturn("1", "上传成功", "");
        }
        catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "上传失败：" + e.getMessage(), "");
        }

    }

    public void initExcelRegion(MultipartRequest multipartRequest, String cliengGuid) throws IOException {
        FrameAttachInfo frameAttachInfo = null;
        Map<String, List<FileItem>> map = multipartRequest.getFileParams();
        for (Map.Entry<String, List<FileItem>> en : map.entrySet()) {
            List<FileItem> fileItems = en.getValue();
            if (fileItems != null && !fileItems.isEmpty()) {
                for (FileItem fileItem : fileItems) {
                    if (!fileItem.isFormField()) {// 是文件流而不是表单数据

                        List<FrameAttachInfo> oldattachlist = attachservice.getAttachInfoListByGuid(cliengGuid);

                        String fileName;
                        // 从文件流中获取文件名
                        fileName = fileItem.getName();
                        int index = fileName.lastIndexOf("\\");
                        fileName = fileName.substring(++index);

                        // 从文件流中获取文件类型
                        String contentType = fileItem.getContentType();
                        // 获取流大小
                        long size = fileItem.getSize();
                        // 获取流
//                        InputStream inputStream = fileItem.getInputStream();
                        
                        String jsonstr=fileItem.getString();
                        
                        
                        List<Record> recordlist=null;    
                        
                        if (StringUtil.isNotBlank(jsonstr)) {
                            try {
                                recordlist= readString(jsonstr);
                            }
                            catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                        
                        if (StringUtil.isNotBlank(recordlist)&&recordlist.size()>0) {                          
                            JnAuditZnsbSelfmachineregion region;
                            
                            for (Record record : recordlist) {
                                if (StringUtil.isNotBlank(record)){
                                    region=regionservice.getRegionByRowguid(record.getStr("rowguid")).getResult();
                                    if (StringUtil.isNotBlank(region)&&(!"1".equals(region.getIsindividuation()))) {                                              
                                        region.setRegionlevel(record.getStr("regionlevel"));
                                        region.setParentguid(record.getStr("parentguid"));
                                        region.setClickurl(record.getStr("clickurl"));
                                        region.setIsenable(record.getStr("isenable"));
                                        region.setRegionname(record.getStr("regionname"));
                                        region.setPicattachguid(record.getStr("picattachguid"));
                                        region.setAreacode(record.getStr("areacode").split("\\.")[0]);
                                        region.setCreatedate(new Date());
                                        region.setIsindividuation(StringUtil.isNotBlank(record.getStr("isindividuation"))?record.getStr("isindividuation"):"0");
                                        region.setIstobeupdated("1");
                                        region.setPixel(record.getStr("pixel"));
                                        region.setOrdernum(record.getStr("ordernum"));
                                        regionservice.update(region);
                                    }else  if (StringUtil.isBlank(region)){
                                        region=new JnAuditZnsbSelfmachineregion();
                                        region.setRowguid(record.getStr("rowguid"));
                                        region.setRegionlevel(record.getStr("regionlevel"));
                                        region.setParentguid(record.getStr("parentguid"));
                                        region.setClickurl(record.getStr("clickurl"));
                                        region.setIsenable(record.getStr("isenable"));
                                        region.setRegionname(record.getStr("regionname"));
                                        region.setPicattachguid(record.getStr("picattachguid"));
                                        region.setAreacode(record.getStr("areacode").split("\\.")[0]);
                                        region.setCreatedate(new Date());
                                        region.setIsindividuation(StringUtil.isNotBlank(record.getStr("isindividuation"))?record.getStr("isindividuation"):"0");
                                        region.setIstobeupdated("1");
                                        region.setPixel(record.getStr("pixel"));
                                        region.setOrdernum(record.getStr("ordernum"));
                                        regionservice.insert(region); 
                                    }
                                    
                                }
                            }  
                            //先将旧数据删除
                            regionservice.deleteOldRegion();
                            //再将新数据转正
                            regionservice.updateNewRegion();
                            
                        }

                    }
                }
            }
        }
    }
    
    public List<Record> readString(String str){
        List<Record> recordlist=new ArrayList<Record>();  
        Record regionRecord;
        try {
            JSONArray jsonArray =  JSONObject.parseArray(str);
            for (Object object : jsonArray) {
                JSONObject regionobj=(JSONObject)object;
                regionRecord=new Record();
                regionRecord.put("rowguid", regionobj.getString("rowguid"));
                regionRecord.put("regionlevel", regionobj.getString("regionlevel"));
                regionRecord.put("parentguid", regionobj.getString("parentguid"));
                regionRecord.put("clickurl", regionobj.getString("clickurl"));
                regionRecord.put("isenable", regionobj.getString("isenable"));
                regionRecord.put("regionname",new String(regionobj.getString("regionname").getBytes("ISO-8859-1"),"UTF-8"));
                regionRecord.put("picattachguid", regionobj.getString("picattachguid"));
                regionRecord.put("areacode", regionobj.getString("areacode").split("\\.")[0]);
                regionRecord.put("isindividuation", StringUtil.isNotBlank(regionobj.getString("isindividuation"))?regionobj.getString("isindividuation"):"0");
                regionRecord.put("pixel", regionobj.getString("pixel"));
                regionRecord.put("ordernum", regionobj.getString("ordernum"));
                recordlist.add(regionRecord);
            }
        }
        catch (Exception e) {
            // TODO: handle exception
        }
        
        return recordlist;
    }
    
    public List<Record> readXlsx(InputStream inputStream) throws Exception{
        List<Record> recordlist=new ArrayList<Record>();        
        XSSFWorkbook xssbook=new XSSFWorkbook(inputStream);
        //循环页
        for (Sheet sheet : xssbook) {
            if (StringUtil.isBlank(sheet)||sheet.getLastRowNum()<1) {
                continue;
            }
            //循环行
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {                
                Row row=sheet.getRow(i);
                int min=row.getFirstCellNum();
                int max=row.getLastCellNum();
                Record record=new Record();
                //循环列
                for (int j = min; j < max; j++) {
                    Cell cell=row.getCell(j);
                    if (StringUtil.isBlank(cell)) {
                        continue;
                    }
                    record.put(numtomeaning(j), cell.toString());
                }
                recordlist.add(record);
                
                
            }
            
        }
        return recordlist;
    }
    
    public List<Record> readXls(InputStream inputStream) throws Exception{
        List<Record> recordlist=new ArrayList<Record>();    
        HSSFWorkbook hssbook=new HSSFWorkbook(inputStream);
        //循环页
        for (int sheetnum = 0; sheetnum < hssbook.getNumberOfSheets(); sheetnum++) {
            HSSFSheet hsssheet= hssbook.getSheetAt(sheetnum);
            if (StringUtil.isBlank(hsssheet)||hsssheet.getLastRowNum()<1) {
                continue;
            }
            //循环行
            for (int i = 1; i <= hsssheet.getLastRowNum(); i++) {
                HSSFRow hssrow=hsssheet.getRow(i);
                int min=hssrow.getFirstCellNum();
                int max=hssrow.getLastCellNum();
                Record record=new Record();
              //循环列
                for (int j = min; j < max; j++) {
                    HSSFCell  cell=hssrow.getCell(j);
                    if (StringUtil.isBlank(cell)) {
                        continue;
                    }
                    record.put(numtomeaning(j), cell.toString());
                }
                recordlist.add(record);
            }
            
        }
        
        
        return recordlist;
    }
    
    public String numtomeaning(int num){
        String returnste="other";
        switch (num) {
            case 0:
                returnste="rowguid"; 
                break;
            case 1:
                returnste="regionlevel"; 
                break;
            case 2:
                returnste="parentguid"; 
                break;
            case 3:
                returnste="clickurl"; 
                break;
            case 4:
                returnste="isenable"; 
                break;
            case 5:
                returnste="regionname"; 
                break;
            case 6:
                returnste="picattachguid"; 
                break;
            case 7:
                returnste="areacode"; 
                break;
            case 8:
                returnste="createdate"; 
                break;
            case 9:
                returnste="isindividuation"; 
                break;
            case 10:
                returnste="istobeupdated"; 
                break;
            case 11:
                returnste="pixel"; 
                break;
            case 12:
                returnste="ordernum"; 
                break;

            default:
                break;
        }
        return returnste;
    }
    

    /**
     * 下载附件
     * 
     * @param request
     *            请求
     * @param response
     *            响应
     * @return String 响应字符串
     */
    @RequestMapping(value = "/readAttach", method = { RequestMethod.POST, RequestMethod.GET })  
    public void readAttach(HttpServletRequest request, HttpServletResponse response) {      
        try {
            // 实际需要返回的范围
            String contentRange = "";
            // 需要跳过的长度
            long skipLength = 0;
            // 附件总长度
            long fullLength = 0;
            // 错误信息
            String errorInfo = "";
            // 文件名
            String filename = "";
            // 获取附件的唯一标识
            String attachGuid = request.getParameter("attachguid");
            List<FrameAttachInfo> frameAttachInfos = attachservice.getAttachInfoListByGuid(attachGuid);
            for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
                attachGuid=frameAttachInfo.getAttachGuid();
            }

            if (StringUtil.isBlank(attachGuid)) {
                throw new ParamException("输入参数不合法");
            }

            // 获取请求头中的Range
            String range = request.getHeader("Range");
            //String range="";
            // 获取附件信息
            FrameAttachInfo info = attachservice.getAttachInfoDetail(attachGuid);
            if (info == null) {
                errorInfo = "FrameAttachInfo信息未找到";
                OutputStream os = null;
                try {
                    // 输出文件流
                    os = response.getOutputStream();
                    os.write(errorInfo.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // 关闭流
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
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
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // 关闭流
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "FrameAttachStorage信息未找到,附件路径:" + storage.getFilePath());
                return;
            }
            if ("http".equals(info.getStorageType())) {
                response.sendRedirect(info.getFilePath());
                return;
            }
            
            InputStream in = storage.getContent();

            long contentLength = 0;
            // 如果请求头中有范围限制，则需要对查出来的文件进行截取
            if (StringUtil.isNotBlank(range)) {
                // 如果range不符合标准，返回416
                if (!range.matches("^bytes=\\d*-\\d*(,\\d*-\\d*)*$")) {
                    response.setHeader("Content-Range", "bytes */" + fullLength);
                    response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE, "range不符合标准");
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return;
                }

                String[] ranges = range.split("=")[1].split("-");
                int begin = Integer.parseInt(ranges[0]);
                int end;
                skipLength = begin;
                if (ranges.length > 1) {
                    end = Integer.parseInt(ranges[1]);
                    contentLength = (end - begin) + 1;
                    // 拼接Content-Range相应头
                    contentRange = range + "/" + fullLength;
                } else {
                    contentLength = fullLength - begin;
                    // 拼接Content-Range相应头
                    contentRange = range + (fullLength - 1) + "/" + fullLength;
                }
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            }
            else {
                contentLength = fullLength;
                // 如果请求头中没有包含文件的范围则直接把整个文件取出来
                contentRange = "bytes " + 0 + "-" + (fullLength - 1) + "/" + fullLength;
            }

            response.setContentType("application/octet-stream");
            // 因为强制下载，所以不考虑inline（可以通过request.getHeader("Accept")来判断客户端浏览器支持哪种类型的文件直接打开）
            response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(filename, "UTF-8"));
            response.addHeader("Content-Range", contentRange);
            response.addHeader("Content-Length", contentLength + "");
            response.addHeader("Accept-Ranges", "bytes");
            response.addHeader("Last-Modified",
                    EpointDateUtil.convertDate2String(info.getUploadDateTime(), "yyyy-MM-dd HH:mm:ss"));

            try {
                // 跳过一部分
                in.skip(skipLength);
                byte[] buffer = new byte[1024];
                int len = -1;
                OutputStream os = null;
                try {
                    // 输出文件流
                    os = response.getOutputStream();
                    while ((len = in.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // 关闭流
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
