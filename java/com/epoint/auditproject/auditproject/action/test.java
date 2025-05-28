package com.epoint.auditproject.auditproject.action;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.epoint.third.apache.commons.codec.binary.Base64;


import javax.crypto.Cipher;

import java.util.Set;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.cert.commonservice.DBServcie;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.cert.util.CertCheckUtil;
import com.epoint.common.util.SM4Util;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.code.MD5Util;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.rentcar.Constant.RentCarConstant;
import com.epoint.security.ClassPathUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itextpdf.text.log.SysoCounter;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;


/**
 * DateUpperChange
 *
 * @author littlehow
 * @time 2016-06-28 09:08
 */
public class test {
    public final static char[] upper = "零一二三四五六七八九十".toCharArray();

    /**
     * 根据小写数字格式的日期转换成大写格式的日期
     * @param date
     * @return
     */
    public static String getUpperDate(String date) {
        //支持yyyy-MM-dd、yyyy/MM/dd、yyyyMMdd等格式
        if(date == null) return null;
        //非数字的都去掉
        date = date.replaceAll("\\D", "");
        if(date.length() != 8) return null;
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<4;i++) {//年
            sb.append(upper[Integer.parseInt(date.substring(i, i+1))]);
        }
        sb.append("年");//拼接年
        int month = Integer.parseInt(date.substring(4, 6));
        if(month <= 10) {
            sb.append(upper[month]);
        } else {
            sb.append("十").append(upper[month%10]);
        }
        sb.append("月");//拼接月

        int day = Integer.parseInt(date.substring(6));
        if (day <= 10) {
            sb.append(upper[day]);
        } else if(day < 20) {
            sb.append("十").append(upper[day % 10]);
        } else {
            sb.append(upper[day / 10]).append("十");
            int tmp = day % 10;
            if (tmp != 0) sb.append(upper[tmp]);
        }
        sb.append("日");//拼接日
        return sb.toString();
    }


    public static void main(String[] arg0){
        
    	String year = EpointDateUtil.convertDate2String(new Date(), "yyyy");
		String idnumv = "808061;080003;080863;080013;080009;080709;080701;081459;080379;180567;081432;080844;081419;080411;081499;080858;080012;081429;081454;081453;080864;080704;081431;080204;081618;081457;081476;080544;080859;080002;080510;081340;080868;080697;081300;180005;080836;080005;081472;080705;081497;081474;080865;081427;081482;080851;081516;081479;081480";
		String[] idnums = idnumv.split(";");
		//system.out.println(idnums.length);
		for (String id : idnums) {
			String code = CertCheckUtil.getCheckCode("11100000000013338W050113700000045030270"+year+id+"001");
			String associatedZzeCertID = "1.2.156.3005.2.11100000000013338W050.113700000045030270."+year+id+".001."+code;
	        //system.out.println(associatedZzeCertID);
		}
		
    }

    
    /**
     * 将文件转为字节数组
     * @param file 
     * @param size 1024
     * @return
     */
    public static byte[] BufferStreamForByte(File file, int size) {
        byte[] content = null;
        try {
            BufferedInputStream bis = null;
            ByteArrayOutputStream out = null;
            try {
                FileInputStream input = new FileInputStream(file);
                bis = new BufferedInputStream(input, size);
                byte[] bytes = new byte[1024];
                int len;
                out = new ByteArrayOutputStream();
                while ((len = bis.read(bytes)) > 0) {
                    out.write(bytes, 0, len);
                }
  
                bis.close();
                content = out.toByteArray();
            } finally {
                if (bis != null) {
                    bis.close();
                }
                if (out != null) {
                    out.close();
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return content;
  
    }
    
    
    /**
     * 生成办件编号（好差评用）
     * @param taskCode
     * @return
     */
    public static String getProjectno() {
        String numberName = "Jn_SpglXmspsxpfw";
        Calendar calendar = Calendar.getInstance();
        String numberFlag = "" + calendar.get(Calendar.YEAR) + String.format("%02d",calendar.get(Calendar.MONTH)+1) + calendar.get(Calendar.DAY_OF_MONTH);
        int theYearLength = 0;
        int snLength = 3;
        String certno = new DBServcie().getFlowSn(numberName, numberFlag, theYearLength, false, snLength);
        return certno;
    }
    
    public static String RasDecrypt(String str) throws Exception {
        // 64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        // base64编码的私钥
        byte[] decoded = Base64.decodeBase64("MIIBVwIBADANBgkqhkiG9w0BAQEFAASCAUEwggE9AgEAAkEAl56HOLhFWuSifkob2vKRcCl1I8DTSap5sTsgsZX80L4OBzOSSH7T1RQzpAW9rEPtOQXowaYMph5aQM8YbrYKlQIDAQABAkEAiVVcojG3EId77+xsoruIpRHIOuRT/aveonwuNuzmnKPCvErgGRPnRY+uzEAKY01fVQangBvY6yOS0cYopmhUgQIhANvncACMGTbRjs1vsTaQh/lgKVNwc1BOiCYw5JMAQc/FAiEAsIGz+Tgsyj7tIZELusiopZ3BF81d/phx6MtRH3jrrJECIQCEIul2Krjr67f3UeoWc3qBKnsqnCNuWgINkMuIWVsyUQIhAKBN3CevVjaEuhcvRYjpbwmjYdh9Qy3URDgaV94Ok3SRAiEAzSjziF2AY9cVytAAzw9XLj6Yvqf0TjlH1srCJ2EYYLA=");
        // PKCS8EncodedKeySpec该类代表私有密钥的ASN.1编码
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(decoded));
        // RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte));
    }
    
    

}
  
