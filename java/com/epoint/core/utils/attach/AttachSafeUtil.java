package com.epoint.core.utils.attach;

import com.epoint.basic.EpointKeyNames9;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.regex.RegexValidateUtil;
import com.epoint.core.utils.security.crypto.MDUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件安全校验通用工具
 *
 * @author cjli
 * @date 2023-06-21
 */
public class AttachSafeUtil
{

    /**
     * 文件类型限制校验失败标识
     */
    public static final String FILE_LIMIT_TYPE_ERROR = "在开启了安全防御的情况下 如果直接修改页面上的上传控件的fileNumLimit, limitType, fileSingleSizeLimit属性值后而没有重启的话，上传时也有可能被无拦截，请管理员前往 后台管理-安全配置管理-安全配置的通用安全类上找到 刷新校验缓存并点击。具体拦截原理请前往技术门户-框架研究中心-F9安全框架-安全架构-数据防御链-附件-附件数量和类型，文档链接：https://fdoc.epoint.com.cn/onlinedoc/rest/d/3YjABj";

    /**
     * 文件大小限制校验失败标识
     */
    public static final String FILE_LIMIT_SIZE_ERROR = "在开启了安全防御的情况下 如果直接修改页面上的上传控件的fileNumLimit, limitType, fileSingleSizeLimit属性值后而没有重启的话，上传时也有可能被无拦截，请管理员前往 后台管理-安全配置管理-安全配置的通用安全类上找到 刷新校验缓存并点击。具体拦截原理请前往技术门户-框架研究中心-F9安全框架-安全架构-数据防御链-附件-附件数量和类型，文档链接：https://fdoc.epoint.com.cn/onlinedoc/rest/d/3YjABj";

    /**
     * 配置文件系统参数，文件名长度精确模式,默认关闭。
     */
    public static final String SECURITY_VTYPEDEFENSE_LENGTHACCURATECHECK = "security_vtype_length_accurate_enable";

    /**
     * 上传控件限制文件类型
     */
    public static final String FILE_LIMIT_TYPE = "file_limit_type";
    /**
     * 配置文件系统参数，上传控件限制大小
     */
    public static final String FILE_LIMIT_SIZE = "file_limit_size";

    /**
     * 配置文件系统参数，附件上传控件大小限制
     */
    public static final String ATTACH_FILENAME_LIMITLENGTH = "file_limit_namelength";

    public static final String CONTENT = "content";
    public static final String MSG = "msg";
    public static final String SOLUTION = "solution";

    /**
     * 配置文件系统参数，文件名称特殊字符过滤
     */
    public static final String FILE_NAME_PATTERN_STR = "[\\\\/:*?\"<>|]";

    public static final Pattern FILE_NAME_PATTERN = Pattern.compile(FILE_NAME_PATTERN_STR);

    /**
     * 考兼容低版本框架
     */
    private static transient Logger logger = LogUtil.getLog(AttachSafeUtil.class);

    /**
     *
     * @param attachname 文件完整名称（带后缀）
     * @param requestContentType 请求上下文中的requestContentType
     * @param fileIn 文件流
     * @return
     */
    public static Map<String, Object> checkFileUpload(String attachname, String requestContentType,
            InputStream fileIn) {
        return checkFileUpload(attachname, requestContentType, fileIn, null);
    }

    /**
     *
     * @param attachname 文件完整名称（带后缀）
     * @param requestContentType 请求上下文中的contenttype
     * @param fileIn 文件流
     * @param size 文件大小
     * @return
     */
    public static Map<String, Object> checkFileUpload(String attachname, String requestContentType, InputStream fileIn,
            Integer size) {
        Map<String, Object> result = new HashMap<>();
        //不管是否对流做校验，最后都要返回,rug
        result.put(CONTENT, fileIn);
        //1、校验文件名合法性
        boolean validFile = isValidFile(attachname);
        if (validFile) {
            try {
                if (size == null || size == null && fileIn != null) {
                    size = fileIn.available();
                }
                //2、校验文件配置
                String[] strings = checkConfigFile(attachname, size);
                if (strings != null) {
                    result.put(MSG, strings[0]);
                    result.put(SOLUTION, strings[1]);
                    return result;
                }
                //3、校验文件类型上下文篡改
                if (checkFileTypeChanged(attachname, requestContentType)) {
                    result.put(MSG, "文件类型上下文信息被篡改");
                    return result;
                }
                //4、校验文件后缀是否篡改
                if (fileIn != null) {
                    String fileType = attachname.substring(attachname.indexOf('.') + 1);
                    result = checkActualFileType(fileIn, fileType, size);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
        else {
            result.put(MSG, "文件名不合法");
            return result;
        }
    }

    /**
     * @param fileMaps  文件类型请求中的文件信息，MultipartRequest.getFileParam
     * @return String["拦截原因"，"解决方案"]，为空则校验通过
     */
    public static String[] checkFileUpload(Map<String, List<FileItem>> fileMaps) {
        if (fileMaps == null || fileMaps.isEmpty()) {
            return null;
        }
        FileItem file = null;
        for (Map.Entry<String, List<FileItem>> entry : fileMaps.entrySet()) {
            List<FileItem> fileList = entry.getValue();
            if (fileList != null && !fileList.isEmpty()) {
                file = fileList.get(0);
            }
            if (file != null) {
                //1、先校验文件名合法性，如果文件名都不合法直接结束。
                if (!isValidFile(file.getName())) {
                    return new String[] {"文件名称不合法！", "请检查文件名称是否包含此类特殊字符：" + FILE_NAME_PATTERN_STR};
                }

                String fieldName = file.getFieldName();
                if (!fieldName.contains("NTKO_LF_BLOCK")) {
                    String fileAllName = file.getName();
                    int index = fileAllName.lastIndexOf('.');
                    //3、校验请求中的文件类型信息是否被篡改
                    if (checkFileTypeChanged(fileAllName, file.getContentType())) {
                        return new String[] {"文件类型上下文信息被篡改", ""};
                    }

                    //4、校验文件后缀是否篡改
                    String errorMsg = checkActualFileType(file);
                    if (StringUtil.isNotBlank(errorMsg)) {
                        return new String[] {errorMsg, ""};
                    }

                    //5、校验文件安全配置
                    String[] result = checkConfigFile(file);
                    if (result != null && result.length > 0) {
                        return result;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 魔数校验
     * 校验文件头部编码对应的文件类型是否与文件后缀名一致。
     * @param fileItem
     */
    public static String checkActualFileType(FileItem fileItem) {
        String name = fileItem.getName();
        String fileType = name.substring(name.lastIndexOf('.') + 1);
        InputStream inputStream = null;
        try {
            inputStream = fileItem.getInputStream();
            byte[] b = new byte[28];
            inputStream.read(b, 0, 28);
            //fileitem.getInputStream每次都会返回一个新的流，此处的读取不会影响到fileitem中的文件内容
            return checkActualFileType(b, fileType);
        }
        catch (Exception e) {
            logger.error("文件流读取出现异常", e);
        }
        return null;
    }

    /**
     * 此方法需要重新给流赋值
     * 校验文件头部编码对应的文件类型是否与文件后缀名一致。
     * @param fileIn
     * @param fileType
     * @param size
     * @return map<> msg:校验结果,content:原始的输入流
     */
    public static Map<String, Object> checkActualFileType(InputStream fileIn, String fileType, long size) {
        if (fileType.contains(".")) {
            fileType = fileType.substring(fileType.indexOf('.') + 1);
        }
        byte[] content = new byte[28];
        InputStream resultIn = fileIn;
        Map<String, Object> result = new HashMap<>();
        //先获取文件头内容
        if (size < 10 * 1024 * 1024) {
            // 小于10M转byte数组
            content = FileManagerUtil.getContentFromInputStream(fileIn);
            // 流会关闭,重新生成一个流
            resultIn = new ByteArrayInputStream(content);
        }
        else {
            // 大于10M先生成临时文件
            String tempPath = ClassPathUtil.getDeployWarPath() + EpointKeyNames9.EPOINT_FOLDER_TEMP + File.separator
                    + EpointKeyNames9.EPOINT_FOLDER_TEMPCLAM + File.separator + UUID.randomUUID().toString()
                    + File.separator;
            String temName = UUID.randomUUID().toString() + fileType;
            FileManagerUtil.writeContentToFileByStream(tempPath, temName, fileIn);
            File tempFile = FileManagerUtil.createFile(tempPath + temName);
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(tempFile);
                inputStream.read(content, 0, 28);
                resultIn = new FileInputStream(tempFile);
            }
            catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
            finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    }
                    catch (IOException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                tempFile.delete();
            }
        }
        result.put(CONTENT, resultIn);
        result.put(MSG, checkActualFileType(content, fileType));
        return result;
    }

    /**
     * 魔数校验（考虑性能问题可以截取头部28位字节传入）inputStream.read(byte[] b, 0, 28);
     * 校验文件头部编码对应的文件类型是否与文件后缀名一致。
     * @param fileHeader
     * @param fileType
     */
    private static String checkActualFileType(byte[] fileHeader, String fileType) {
        StringBuffer buffer = new StringBuffer();
        int subIndex = 28;
        try {
            if (fileHeader.length < subIndex) {
                subIndex = fileHeader.length;
            }
            for (int i = 0; i < subIndex; i++) {
                int v = fileHeader[i] & 0xFF;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    buffer.append(0);
                }
                buffer.append(hv);
            }
            // 校验文件后缀名是否被篡改
            String fileTypeCode = FileTypeEnums.getEnumsIgnoreCase(fileType) == null ?
                    "" :
                    FileTypeEnums.getEnumsIgnoreCase(fileType).getCode();
            if (buffer.toString().startsWith("application/octet-stream")) {
                return null;
            }
            if (StringUtil.isNotBlank(fileTypeCode)) {
                // 可能有多种字节码前缀
                String[] fileTypeCodes = fileTypeCode.split(",");
                boolean machTypeCode = false;
                for (String typeCode : fileTypeCodes) {
                    // 包含说明头部编码与文件名后缀匹配，没有修改后缀
                    if (StringUtil.toUpperCase(buffer.toString()).startsWith(typeCode)
                            //UTF-8编码的文件可能存在BOM头（也可以没有），也就是EFBBBF出现在文件头部标识UTF-8编码
                    || StringUtil.toUpperCase(buffer.toString()).startsWith("EFBBBF"+typeCode)) {
                        machTypeCode = true;
                        break;
                    }
                }
                if (!machTypeCode) {
                    // 抛出文件类型不匹配的异常，说明是文件后缀被修改过
                    return "检测到文件类型篡改，文件后缀与真实的文件类型不符";
                }
            }
            // 没匹配到，有2种情况：
            // 1、fileTypeCode="" 文件头标识录入了该文件类型，但是该类型没有固定的文件头标识，跳过
            else if (FileTypeEnums.FILE_LIMIT_TYPE.contains(fileType)) {
                logger.error("当前上传文件类型没有明确的文件头标识，不做校验");
            }
        }
        catch (IllegalArgumentException e) {
            // 2、fileTypeCode不在枚举类中， 平台配置了默认不支持的文件类型，没有录入文件头标识，匹配不到，那么跳过
            logger.error("当前上传文件类型不再枚举类型中，不做校验", e);
        }
        return null;
    }

    /**
     * 校验contenttype是否被篡改
     * @param fileName
     * @param requestContentType
     * @return true 被篡改；false 没被篡改
     */
    public static boolean checkFileTypeChanged(String fileName, String requestContentType) {
        if(logger.isDebugEnabled()){
            logger.debug("根据当前文件contentType: "+requestContentType);
        }
        //application/octet-stream,代表服务器也不知道具体的文件类型，那么先跳过contenttype校验
        if ("application/octet-stream".equals(requestContentType)) {
            return false;
        }
        String suffx = fileName.substring(fileName.lastIndexOf('.') + 1);
        String mimeType ="";
        if (WebUtil.getRequest() != null && WebUtil.getRequest().getServletContext() != null) {
            mimeType = WebUtil.getRequest().getServletContext().getMimeType(suffx);
            if(logger.isDebugEnabled()){
                logger.debug("根据当前文件后缀 "+suffx+" 获取到标准上下文contentType："+mimeType);
            }
        }
        // 校验请求中的contentType是否被篡改
        boolean machContentType = false;
        //与上下文标准类型匹配 或者 枚举类中不存在的 放行
        if ((StringUtil.isNotBlank(mimeType) && StringUtil.isNotBlank(requestContentType) && mimeType
                .equals(requestContentType)) || FileTypeEnums.getEnumsIgnoreCase(suffx) == null) {
            return false;
        }
        // 可能有多种contenttype
        String fileContentTypes = FileTypeEnums.getEnumsIgnoreCase(suffx).getContentType();
        if (StringUtil.isNotBlank(requestContentType) && StringUtil.isNotBlank(fileContentTypes)) {
            // 包含说明contentType与文件名后缀匹配，没有修改后缀
            if (fileContentTypes.contains(requestContentType)) {
                return false;
            }

            //兼容规范不一致的情况,例如application/x-tar application/tar 都可以
            requestContentType = requestContentType.replace("/x-", "/");
            fileContentTypes = fileContentTypes.replace("/x-", "/");
            //压缩包类型特有的
            if(requestContentType.endsWith("-compressed")){
                requestContentType = requestContentType.substring(0,requestContentType.indexOf("-compressed"));
            }
            if(requestContentType.endsWith("-compressed")){
                fileContentTypes = fileContentTypes.substring(0,requestContentType.indexOf("-compressed"));
            }

            if(fileContentTypes.contains(requestContentType)){
                return false;
            }
            return true;
        }
        //没有requestContentType就校验不了 直接放行
        return false;
    }

    public static String[] checkConfigFile(FileItem file) {
        if (file == null) {
            return null;
        }
        return checkConfigFile(file.getName(), file.getSize());
    }

    /**
     * 附件上传附件配置防御（依据配置文件配置）
     * <p>
     * author mjjia
     *
     * @param fileAllName 附件全名称，包含后缀
     * @param size 文件长度
     */
    public static String[] checkConfigFile(String fileAllName, long size) {
        if (StringUtil.isBlank(fileAllName) && size < 0) {
            return null;
        }
        String fileConfigLimitType = ConfigUtil.getConfigValue(FILE_LIMIT_TYPE);
        String fileLimitSize = ConfigUtil.getConfigValue(FILE_LIMIT_SIZE);
        Integer fileConfigLimitSize;
        Integer fileConfigLimitNameLen;
        // 上传大小限制，默认单位MB，需转换为KB
        if (StringUtil.isBlank(fileLimitSize) || "0".equals(fileLimitSize)) {
            fileConfigLimitSize = null;
        }
        else {
            fileConfigLimitSize = Integer.valueOf(fileLimitSize);
        }
        if (fileConfigLimitSize != null && !"0".equals(fileConfigLimitSize.toString())) {
            fileConfigLimitSize = fileConfigLimitSize * 1024;
        }
        // 文件名长度限制
        String fileLimitNameLength = ConfigUtil.getConfigValue(ATTACH_FILENAME_LIMITLENGTH);
        fileConfigLimitNameLen = StringUtil.isBlank(fileLimitNameLength) ? 255 : Integer.valueOf(fileLimitNameLength);

        // 附件全名称，包含类型
        // 附件长度，默认单位自己，需转为KB
        long fileSize = size / 1024L;
        int index = fileAllName.lastIndexOf('.');
        if (index == -1) {
            return new String[] {"附件名称没有后缀名，不合法！请重新选择上传附件", ""};
        }
        else {
            String fileName = fileAllName.substring(0, index);
            int fileNameLength = "1".equals(ConfigUtil
                    .getConfigValue(SECURITY_VTYPEDEFENSE_LENGTHACCURATECHECK))
                    ? StringUtil.length(fileName)
                    : fileName.length();
            // 附件类型
            String fileType = StringUtil.toLowerCase(fileAllName.substring(index + 1));
            // 附件类型限制不为空，且上传附件类型不好含其中
            if (StringUtil.isNotBlank(fileConfigLimitType) && !fileConfigLimitType.contains(fileType)) {
                return new String[] {"该文件类型" + fileType + "不允许上传！", "可支持类型请参考[系统设置]-[附件配置]-[上传类型限制]配置项"};

            }
            // 附件名长度限制不为空，且附件名称长度大于限制长度
            else if (fileConfigLimitNameLen < fileNameLength) {
                return new String[] {
                        "该文件名称" + fileName + "，长度" + fileNameLength + "大于文件名限制长度" + fileConfigLimitNameLen + "不允许上传！",
                        "文件名长度限制请参考[系统设置]-[附件配置]-[文件名长度限制]配置项"};
            }
            else if (fileNameLength == 0) {
                return new String[] {"该文件名称" + fileNameLength + "长度为0,不允许上传！", ""};
            }
            // 附件大小限制不为空，且附件大小大于校址大小
            else if (fileConfigLimitSize != null && fileConfigLimitSize.longValue() < fileSize) {
                return new String[] {"该文件大小" + fileSize + "KB，大于上传限制大小" + fileConfigLimitSize.longValue() + "KB，不允许上传！",
                        "文件名长度限制请参考[系统设置]-[附件配置]-[上传大小限制]配置项"};
            }
            // 文件名称内容检测
            if (!isValidFile(fileName)) {
                return new String[] {"该文件名称" + fileName + "含有敏感字符如下':'、'\"'、'<>'"};
            }
        }
        return null;
    }

    /**
     * 只校验长度
     * @param content
     * @return
     */
    public static String checkChunkSize(byte[] content) {
        return checkChunk(content, null, false);
    }

    /**
     * 校验md5一致性和长度是否符合
     * @param content
     * @param serverMD5
     * @return
     */
    public static String checkChunkSize(byte[] content, String serverMD5) {
        return checkChunk(content, serverMD5, true);
    }

    /**
     * 校验分片的内容篡改和分片长度
     * @param content
     * @param serverMD5
     * @param checkMD5
     * @return
     */
    public static String checkChunk(byte[] content, String serverMD5, boolean checkMD5) {
        boolean checkSize = false;
        if (content != null && content.length > 0) {
            //不为空才校验
            if (checkMD5) {
                if (StringUtil.isBlank(serverMD5) || !serverMD5.equals(MDUtils.md5Hex(content))) {
                    return "服务端MD5与客户端MD5不一致";
                }
            }
            String limitSizeStr = WebUtil.getRequestParameterStr(WebUtil.getRequest(), "chunkSize");
            long limitSize = 1024 * 128L;
            if (StringUtil.isNotBlank(limitSizeStr)) {
                limitSize = Long.valueOf(limitSizeStr);
                if (limitSize > 2097152) {
                    limitSize = 1024 * 128L;
                }
            }
            if (content.length > limitSize) {
                return "分片长度超限制,当前分片长度是:" + content.length + "限制长度:" + limitSize;
            }
        }
        return "";
    }

    private static boolean isValidFile(String attachname) {
        Matcher matcher = RegexValidateUtil.FILE_NAME_PATTERN.matcher(attachname);
        return !matcher.find();
    }

    /**
     * @author cjli
     */
    enum FileTypeEnums
    {

        /**
         * 文件类型
         */
        ISO("ISO", "434D0100", "application/x-cd-image"),
        FLV("FLV", "464C5601", "video/x-flv"),
        DOC("DOC", "D0CF11E0,504B0304", "application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
        XLS("XLS", "D0CF11E0,504B0304", "application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        VSD("VSD", "D0CF11E0,504B0304", ""),
        PPT("PPT", "D0CF11E0,006E1EF0,0F00E803,504B0304", "application/vnd.ms-powerpoint,application/vnd.openxmlformats-officedocument.presentationml.presentation"),

        TXT("TXT", "", "text/plain,text/html,text/xml"),
        RAR("RAR", "52617221", "application/x-rar-compressed,application/x-rar,application/rar"),
        JPG("JPG", "FFD8FFE1,FFD8FFE0,FFD8FF", "image/jpeg"),
        JPEG("JPEG", "FFD8FFE1,FFD8FFE0,FFD8FF", "image/jpeg"),
        PDF("PDF", "25504446", "application/pdf"),
        MP3("MP3", "494433", "audio/mpeg"),
        GIF("GIF", "47494638", "image/gif"),
        DOCX("DOCX", "504B0304,D0CF11E0", "application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
        PPTX("PPTX", "504B0304,D0CF11E0", "application/vnd.ms-powerpoint,application/vnd.openxmlformats-officedocument.presentationml.presentation"),
        XLSX("XLSX", "504B0304,D0CF11E0", "application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        VSDX("VSDX", "504B0304,D0CF11E0", ""),
        ZIP("ZIP", "504B0304", "application/zip,application/x-zip-compressed"),
        VOB("VOB", "000001BA", "video/mpeg"),
        SWF("SWF", "435753,465753", "application/x-shockwave-flash"),
        RTF("RTF", "7B5C7274", "application/rtf"),
        CHM("CHM", "49545346", "application/x-chm"),
        TIF("TIF", "49492A00,492049,4D4D00,4D4D2A", "image/tiff"),
        _7Z("7Z", "377ABCAF271C", "application/x-7z-compressed"),
        XML("XML", "3C3F786D,FFFE3C0052004F004F0", "text/xml,application/xml"),
        MHT("MHT", "3026B2", ""),
        WMV("WMV", "3026B2758E66CF11", "video/x-ms-wmv"),
        WAV("WMV", "52494646", "audio/wav"),
        BMP("BMP", "424D", "image/bmp"),
        PNG("PNG", "89504E470D0A1A0A,89504E", "image/png"),
        SVG("SVG", "3C737667,3C3F786D6C", "image/svg+xml"),
        AVI("AVI", "41564920", "video/x-msvideo"),
        MPG("MPG", "000001B3,000001BA", "video/mpeg"),
        APK("APK", "504b0304", ""),
        IPA("IPA", "504b0304", ""),
        MP4("MP4", "", ""),
        M4A("M4A", "", "audio/x-m4a,audio/mp4"),
        M4V("M4V", "", "video/x-m4v,video/mp4"),
        MP2("MP2", "", "video/mpeg,audio/mpeg"),
        MPA("MPA", "", "video/mpeg,audio/mpeg"),

        MPP("MPP", "00010000", ""),
        CEB("CEB", "43454246", ""),
        RP("RP", "52502D5061636B", ""),
        CRASH("CRASH", "43525348", ""),
        LIC("LIC", "", ""),
        P12("P12", "", ""),
        PSD("PSD", "", "application/x-photoshop,image/vnd.adobe.photoshop"),
        WPS("WPS", "", "application/kswps,application/vnd.ms-works");
        private String fileType;
        private String value;
        private String contentType;

        /**
         * 统一附件默认允许上传的附件类型
         */
        public static final String FILE_LIMIT_TYPE = "iso,flv,doc,txt,rar,jpg,jpeg,pdf,mp3,xls,gif,ppt,xlsx,vob,swf,zip,pptx,mpp,docx,rtf,chm,tif,7z,xml,mht,wmv,lic,mp4,bmp,rp,ceb,vsd,vsdx,png,svg,avi,mpg,p12,apk,ipa,crash";

        private static final String POT = ".";

        FileTypeEnums(String fileType, String value, String contentType) {
            this.fileType = fileType;
            this.value = value;
            this.contentType = contentType;
        }

        private static FileTypeEnums getEnumsIgnoreCase(String fileType) {
            fileType = StringUtil.toUpperCase(fileType);
            if (fileType.startsWith(POT)) {
                fileType = fileType.substring(1);
            }
            try {
                return valueOf(fileType);
            }
            catch (IllegalArgumentException e) {
                if (e.getMessage().contains("No enum constant")) {
                    return null;
                }
                throw e;
            }
        }

        private String getCode() {
            return this.value;
        }

        private String getContentType() {
            return this.contentType;
        }

        private String getFileType() {
            return this.fileType;
        }

    }

}
