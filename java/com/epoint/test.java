package com.epoint;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.cert.commonservice.DBServcie;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.common.util.OfficeWebUrlEncryptUtil;
import com.epoint.core.utils.code.MD5Util;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.rentcar.Constant.RentCarConstant;
import com.epoint.security.ClassPathUtil;
import com.epoint.sm2util.SM2Utils;
import com.epoint.sm2util.Util;
import com.epoint.xmz.zjxl.util.AesDemoUtil;
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


    public static void main(String[] args) throws IllegalArgumentException, IOException {
    	String tasknames = "";
    	List<String> splitStringList = chineseSplitFunction("建筑业企业资质", 3);
		for (String split:splitStringList) {
			tasknames += split + "%";
		}
		System.out.println(tasknames);
    	
    }
    
    
    public static List<String> chineseSplitFunction(String src, int bytes){
		try {
			if(src == null){
				return null;
			}
			List<String> splitList = new ArrayList<String>();
			int startIndex = 0;    //字符串截取起始位置
			int endIndex = bytes > src.length() ? src.length() : bytes;  //字符串截取结束位置 
			while(startIndex < src.length()){
				String subString = src.substring(startIndex,endIndex);
				//截取的字符串的字节长度大于需要截取的长度时，说明包含中文字符
				//在GBK编码中，一个中文字符占2个字节，UTF-8编码格式，一个中文字符占3个字节。
				while (subString.getBytes("GBK").length > bytes) {
					--endIndex;
					subString = src.substring(startIndex,endIndex);
				}
				splitList.add(src.substring(startIndex,endIndex));
				startIndex = endIndex;
				//判断结束位置时要与字符串长度比较(src.length())，之前与字符串的bytes长度比较了，导致越界异常。
				endIndex = (startIndex + bytes) > src.length() ? 
						src.length()  : startIndex+bytes ;

			}
			return splitList;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
    
    /**
     * 生成办件编号（好差评用）
     * @param taskCode
     * @return
     */
    public static String getProjectno(String taskCode) {
        String numberName = "自动生成证照编码";
        Calendar calendar = Calendar.getInstance();
        String numberFlag = "" + calendar.get(Calendar.YEAR) + String.format("%02d",calendar.get(Calendar.MONTH)+1) + calendar.get(Calendar.DAY_OF_MONTH);
        int theYearLength = 0;
        int snLength = 4;
        String certno = new DBServcie().getFlowSn(numberName, taskCode+numberFlag, theYearLength, false, snLength);
        return certno;
    }

}
  
