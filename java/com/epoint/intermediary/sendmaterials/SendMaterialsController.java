package com.epoint.intermediary.sendmaterials;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.intermediary.sendmaterials.api.ISendMaterials;

@RestController
@RequestMapping("/sendmaterials")
public class SendMaterialsController
{

    @Autowired
    private ISendMaterials sendMaterials;

    /**
     * 
     * @Description: 下载材料接口(封装材料数据，字节方式传递)
     * @author male   
     * @date 2019年3月13日 下午2:54:26
     * @return String    返回类型    
     * @throws
     */
    @RequestMapping(value = "/getmaterials", method = RequestMethod.POST)
    public String getMaterials(@RequestBody String params) {
        JSONObject jsonObject = JSONObject.parseObject(params);
        String attachguid = jsonObject.getString("attachguid");

        /**
         * 文件名
         */
        String fileName = null;
        /**
         * 文件路径
         */
        String filePath = null;
        /**
         * 文件类型 如.jpg
         */
        String contentType = null;

        File file = null;

        InputStream in = null;
        /**
         * 文件转换byte
         */
        byte[] strBuffer = null;

        Record attachinfo = null;
        int flen = 0;

        //返回结果
        JSONObject sumbit = new JSONObject();

        try {
            if (StringUtil.isNotBlank(attachguid)) {
                attachinfo = sendMaterials.getMaterialsInfo(attachguid);
                if (attachinfo != null) {
                    fileName = attachinfo.getStr("attachfilename");
                    filePath = attachinfo.getStr("filepath");
                    contentType = attachinfo.getStr("contenttype");

                    sumbit.put("filename", fileName);
                    sumbit.put("filetype", contentType);
                }

                if (StringUtil.isNotBlank(filePath) && StringUtil.isNotBlank(fileName)) {
                    file = new File(filePath + fileName);
                    //测试file
                    //file = new File("E:/个性化规范V5.docx");

                    in = new FileInputStream(file);
                    flen = (int) file.length();
                    strBuffer = new byte[flen];
                    in.read(strBuffer, 0, flen);
                }
                if (strBuffer != null && strBuffer.length > 0) {
                    //base64加密                  
                    sumbit.put("filecontent", Base64Util.encode(strBuffer));
                    //System.out.println(sumbit.getString("filecontent"));
                }
                sumbit.put("status", 200);
            }
            else {
                //500不成功  200成功
                sumbit.put("status", 500);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
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

        return sumbit.toString();
    }
}
