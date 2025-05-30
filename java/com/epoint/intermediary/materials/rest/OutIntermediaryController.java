package com.epoint.intermediary.materials.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.zwdt.util.TARequestUtil;

@RestController
@RequestMapping("/intermediary")
public class OutIntermediaryController
{

    private static String intermediaryAdress = ConfigUtil.getConfigValue("taconfig", "intermediary");

    /**
     * 
     * @Description: 外网获取材料信息
     * @author male   
     * @date 2019年3月14日 下午7:34:04
     * @return void    返回类型    
     * @throws
     */
    @RequestMapping(value = "/getmaterials", method = RequestMethod.GET)
    public void getMaterials(HttpServletResponse response, HttpServletRequest request) {
        //获取attachguid
        JSONObject webParam = new JSONObject();
        String attachguid = request.getParameter("attachguid");

        webParam.put("attachguid", attachguid);

        String spMaterials = null;
        if (StringUtil.isNotBlank(attachguid)) {
            //请求审批系统获取材料数据 本地测试
            /*spMaterials = TARequestUtil.sendPost(
                    "http://localhost:8070/epoint-web-zwdt/rest/sendmaterials/getmaterials", webParam.toString(), "",
                    "");*/

            //服务器
            spMaterials = TARequestUtil.sendPost(intermediaryAdress + "rest/sendmaterials/getmaterials",
                    webParam.toString(), "", "");

        }
        if (StringUtil.isBlank(spMaterials))
            return;

        JSONObject jsonObject = JSONObject.parseObject(spMaterials);
        String fileName = jsonObject.getString("filename");
        String fileType = jsonObject.getString("filetype");
        String fileContent = jsonObject.getString("filecontent");
        int status = jsonObject.getInteger("status");

        InputStream in = null;

        //500没有获取到
        if (status == 500)
            return;

        try {
            if (StringUtil.isNotBlank(fileName) && StringUtil.isNotBlank(fileType)
                    && StringUtil.isNotBlank(fileContent)) {

                //设置response
                response.reset();
                //判断文件类型设置
                response.setContentType(getResponseType(fileType));

                fileName = URLEncoder.encode(fileName, "UTF-8");

                response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

                //字节转换
                in = new ByteArrayInputStream(Base64Util.decodeBuffer(fileContent));

                response.setContentLength(in.available());

                int len = 0;

                byte buf[] = new byte[1024];//缓存作用  

                while ((len = in.read(buf)) > 0)
                    response.getOutputStream().write(buf, 0, len);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (in != null)
                try {
                    in.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
        }

    }

    /**
     * 
     * @Description: response下载设置文件类型
     * @author male   
     * @date 2019年3月15日 下午5:44:20
     * @return String    返回类型    
     * @throws
     */
    public String getResponseType(String fileType) {
        if (".jpg".equalsIgnoreCase(fileType)) {
            return "application/x-jpg; charset=utf-8";
        }
        else if (".jpeg".equalsIgnoreCase(fileType)) {
            return "image/jpeg; charset=utf-8";
        }
        else if (".gif".equalsIgnoreCase(fileType)) {
            return "image/gif; charset=utf-8";
        }
        else if (".txt".equalsIgnoreCase(fileType)) {
            return "text/plain; charset=utf-8";
        }
        else if (".png".equalsIgnoreCase(fileType)) {
            return "application/x-png; charset=utf-8";
        }
        else if (".doc".equalsIgnoreCase(fileType) || ".docx".equalsIgnoreCase(fileType)) {
            return "application/msword; charset=utf-8";
        }
        else if (".xls".equalsIgnoreCase(fileType) || ".xlsx".equalsIgnoreCase(fileType)) {
            return "application/vnd.ms-excel; charset=utf-8";
        }
        else if (".zip".equalsIgnoreCase(fileType)) {
            return "application/zip; charset=utf-8";
        }
        else if (".rar".equalsIgnoreCase(fileType)) {
            return "application/x-rar; charset=utf-8";
        }
        else if (".pdf".equalsIgnoreCase(fileType)) {
            return "application/pdf; charset=utf-8";
        }
        else if (".bmp".equalsIgnoreCase(fileType)) {
            return "application/x-bmp; charset=utf-8";
        }
        else if (".apk".equalsIgnoreCase(fileType)) {
            return "application/vnd.android.package-archive; charset=utf-8";
        }
        else if (".mp4".equalsIgnoreCase(fileType)) {
            return "video/mp4; charset=utf-8";
        }

        return "text/plain; charset=utf-8";
    }

}
